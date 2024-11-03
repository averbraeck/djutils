package org.djutils.draw.curve;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.djutils.draw.Direction3d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Flattens a Curve3d in to a PolyLine3d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Flattener3d extends Flattener<Flattener3d, Flattable3d, PolyLine3d, Point3d>
{

    /**
     * Flatten a continuous line into a PolyLine.
     * @param line line function.
     * @return PolyLine2d; flattened line.
     */
    @Override
    default PolyLine3d flatten(final Flattable3d line)
    {
        return flatten(line);
    }

    /**
     * Check for an inflection point by creating additional points at one quarter and three quarters. If these are on opposite
     * sides of the line from prevPoint to nextPoint; there must be an inflection point.
     * @param line FlattableLine
     * @param prevT double; t of preceding inserted point
     * @param medianT double; t of point currently considered for insertion
     * @param nextT double; t of following inserted point
     * @param prevPoint Point3d; point on <cite>line</cite> at <cite>prevT</cite>
     * @param nextPoint Point3d; point on <cite>line</cite> at <cite>nextT</cite>
     * @return boolean; <cite>true</cite> if there is an inflection point between <cite>prevT</cite> and <cite>nextT</cite>;
     *         <cite>false</cite> if there is no inflection point between <cite>prevT</cite> and <cite>nextT</cite>
     */
    private static boolean checkZigZag(final Flattable3d line, final double prevT, final double medianT, final double nextT,
            final Point3d prevPoint, final Point3d nextPoint)
    {
        Point3d oneQuarter = line.getPoint((prevT + medianT) / 2);
        Direction3d oneQDir = oneQuarter.directionTo(oneQuarter.closestPointOnSegment(prevPoint, nextPoint));
        Point3d threeQuarter = line.getPoint((nextT + medianT) / 2);
        Direction3d threeQDir = threeQuarter.directionTo(threeQuarter.closestPointOnSegment(prevPoint, nextPoint));
        Double angle = oneQDir.directionDifference(threeQDir);
        return angle > Math.PI / 2; // Projection direction varies by more than 90 degrees
    }

    /**
     * Flattener based on number of segments.
     */
    class NumSegments implements Flattener3d
    {
        /** Number of segments. */
        private final int numSegments;

        /**
         * Construct a NumSegments flattener.
         * @param numSegments int; number of segments to use in the construction of the PolyLine2d.
         */
        public NumSegments(final int numSegments)
        {
            Throw.when(numSegments < 1, IllegalArgumentException.class, "Number of segments must be at least 1");
            this.numSegments = numSegments;
        }

        @Override
        public PolyLine3d flatten(final Flattable3d line)
        {
            Throw.whenNull(line, "Line function may not be null.");
            List<Point3d> points = new ArrayList<>(this.numSegments + 1);
            for (int i = 0; i <= this.numSegments; i++)
            {
                double fraction = ((double) i) / this.numSegments;
                points.add(line.getPoint(fraction));
            }
            return new PolyLine3d(points);
        }
    }

    /**
     * Flattener based on maximum deviation.
     */
    class MaxDeviation implements Flattener3d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /**
         * Construct a new MaxDeviation flattener.
         * @param maxDeviation maximum deviation, must be above 0.0.
         */
        public MaxDeviation(final double maxDeviation)
        {
            Throw.when(maxDeviation <= 0.0 || Double.isNaN(maxDeviation), IllegalArgumentException.class,
                    "Maximum deviation must be above 0.0 and finite");
            this.maxDeviation = maxDeviation;
        }

        @Override
        public PolyLine3d flatten(final Flattable3d line)
        {
            Throw.whenNull(line, "Line function may not be null.");
            NavigableMap<Double, Point3d> result = new TreeMap<>();
            loadKinks(result, line);

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point3d prevPoint = result.get(prevT);
            Map.Entry<Double, Point3d> entry;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point3d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point3d medianPoint = line.getPoint(medianT);

                // Check max deviation
                Point3d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= this.maxDeviation)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (Flattener3d.checkZigZag(line, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is probably an inflection point, inserting the halfway point should take care of this
                    Point3d midPoint = line.getPoint(medianT);
                    result.put(medianT, midPoint);
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
            }
            return new PolyLine3d(result.values().iterator());
        }
    }

    /**
     * Flattener based on maximum deviation and maximum angle.
     */
    class MaxDeviationAndAngle implements Flattener3d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Constructor.
         * @param maxDeviation maximum deviation, must be above 0.0.
         * @param maxAngle maximum angle, must be above 0.0.
         */
        MaxDeviationAndAngle(final double maxDeviation, final double maxAngle)
        {
            Throw.when(maxDeviation <= 0.0 || Double.isNaN(maxDeviation), IllegalArgumentException.class,
                    "Maximum deviation must be above 0.0 and finite");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0");
            this.maxDeviation = maxDeviation;
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine3d flatten(final Flattable3d line)
        {
            NavigableMap<Double, Point3d> result = new TreeMap<>();
            loadKinks(result, line);
            Map<Double, Direction3d> directions = new LinkedHashMap<>();
            Set<Double> kinks = new HashSet<>();
            for (double fraction : result.keySet())
            {
                directions.put(fraction, line.getDirection(fraction));
                kinks.add(fraction);
            }

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point3d prevPoint = result.get(prevT);
            Map.Entry<Double, Point3d> entry;
            int iterationsAtSinglePoint = 0;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point3d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point3d medianPoint = line.getPoint(medianT);

                // Check max deviation
                Point3d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= this.maxDeviation)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    continue;
                }

                // Check max angle
                double angle = prevPoint.directionTo(nextPoint).directionDifference(directions.get(prevT));
                if (Math.abs(angle) >= this.maxAngle)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t={}). Likely there is an (unreported) kink in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (Flattener3d.checkZigZag(line, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is probably an inflection point, inserting the halfway point should take care of this
                    Point3d midPoint = line.getPoint(medianT);
                    result.put(medianT, midPoint);
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (kinks.contains(prevT))
                {
                    directions.put(prevT, line.getDirection(prevT + Math.ulp(prevT)));
                }
            }
            return new PolyLine3d(result.values().iterator());
        }
    }

    /**
     * Flattener based on maximum angle.
     */
    class MaxAngle implements Flattener3d
    {
        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Constructor.
         * @param maxAngle maximum angle.
         */
        MaxAngle(final double maxAngle)
        {
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0");
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine3d flatten(final Flattable3d line)
        {
            NavigableMap<Double, Point3d> result = new TreeMap<>();
            loadKinks(result, line);
            Map<Double, Direction3d> directions = new LinkedHashMap<>();
            Set<Double> kinks = new HashSet<>();
            for (double kink : result.keySet())
            {
                directions.put(kink, line.getDirection(kink));
                if (kink != 0.0 && kink != 1.0)
                {
                    kinks.add(kink);
                }
            }
            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point3d prevPoint = result.get(prevT);
            Map.Entry<Double, Point3d> entry;
            int iterationsAtSinglePoint = 0;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point3d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;

                // Check max angle
                double angle = prevPoint.directionTo(nextPoint).directionDifference(directions.get(prevT));
                if (Math.abs(angle) >= this.maxAngle)
                {
                    // We need to insert another point
                    Point3d medianPoint = line.getPoint(medianT);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    iterationsAtSinglePoint++;
                    if (iterationsAtSinglePoint > 20)
                    {
                        for (Double t = result.firstKey(); t != null; t = result.higherKey(t))
                        {
                            System.out.println(String.format("t %8.6f angle %8.6f p=%s", t, directions.get(t), result.get(t)));
                        }
                        System.out.println("Breakpoint here");
                    }
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t={}). Likely there is an (unreported) kink in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (Flattener3d.checkZigZag(line, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is probably an inflection point, inserting the halfway point should take care of this
                    Point3d midPoint = line.getPoint(medianT);
                    result.put(medianT, midPoint);
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (kinks.contains(prevT))
                {
                    directions.put(prevT, line.getDirection(prevT + Math.ulp(prevT)));
                }
            }
            return new PolyLine3d(result.values().iterator());
        }
    }

}
