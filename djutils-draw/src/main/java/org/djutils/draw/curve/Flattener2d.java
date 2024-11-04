package org.djutils.draw.curve;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.djutils.base.AngleUtil;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Flattens a Curve2d in to a PolyLine2d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Flattener2d extends Flattener<Flattener2d, Flattable2d, PolyLine2d, Point2d>
{

    /**
     * Check for an inflection point by creating additional points at one quarter and three quarters. If these are on opposite
     * sides of the line from prevPoint to nextPoint; there must be an inflection point.
     * https://stackoverflow.com/questions/1560492/how-to-tell-whether-a-point-is-to-the-right-or-left-side-of-a-line
     * @param line FlattableLine2d
     * @param prevT double; t of preceding inserted point
     * @param medianT double; t of point currently considered for insertion
     * @param nextT double; t of following inserted point
     * @param prevPoint Point2d; point on <cite>line</cite> at <cite>prevT</cite>
     * @param nextPoint Point2d; point on <cite>line</cite> at <cite>nextT</cite>
     * @return boolean; <cite>true</cite> if there is an inflection point between <cite>prevT</cite> and <cite>nextT</cite>;
     *         <cite>false</cite> if there is no inflection point between <cite>prevT</cite> and <cite>nextT</cite>
     */
    private static boolean checkInflectionPoint(final Flattable2d line, final double prevT, final double medianT,
            final double nextT, final Point2d prevPoint, final Point2d nextPoint)
    {
        Point2d oneQuarter = line.getPoint((prevT + medianT) / 2);
        int sign1 = (int) Math.signum((nextPoint.x - prevPoint.x) * (oneQuarter.y - prevPoint.y)
                - (nextPoint.y - prevPoint.y) * (oneQuarter.x - prevPoint.x));
        Point2d threeQuarter = line.getPoint((nextT + medianT) / 2);
        int sign2 = (int) Math.signum((nextPoint.x - prevPoint.x) * (threeQuarter.y - prevPoint.y)
                - (nextPoint.y - prevPoint.y) * (threeQuarter.x - prevPoint.x));
        // System.out.println("medianT=" + medianT + " det1q="
        // + ((nextPoint.x - prevPoint.x) * (oneQuarter.y - prevPoint.y)
        // - (nextPoint.y - prevPoint.y) * (oneQuarter.x - prevPoint.x))
        // + " det3q=" + ((nextPoint.x - prevPoint.x) * (threeQuarter.y - prevPoint.y)
        // - (nextPoint.y - prevPoint.y) * (threeQuarter.x - prevPoint.x)));
        return sign1 != sign2;
    }

    /**
     * Check for a direction change of more than 90 degrees. If that happens, the MaxDeviation flattener must zoom in closer.
     * @param prevDirection double; the direction at the preceding (already added) point
     * @param nextDirection double; the direction at the succeeding (already added) point
     * @return boolean; <cite>true</cite> if the curve changes direction by more than 90 degrees; <cite>false</cite> if the
     *         curve does not change direction by more than 90 degrees
     */
    static boolean checkLoopBack(final double prevDirection, final double nextDirection)
    {
        return Math.abs(AngleUtil.normalizeAroundZero(nextDirection - prevDirection)) > Math.PI / 2;
    }

    /**
     * Check the position error at a point.
     * @param medianPoint Point2d; point at the median t value
     * @param prevPoint Point2d; point at the start of the current segment (should be on the Flattable2d)
     * @param nextPoint Point2d; point at the end of the current segment (should be on the Flattable2d)
     * @param maxDeviation double; maximum allowed position error
     * @return boolean; <cite>true</cite> if the position error exceeds <cite>maxDeviation</cite>; <cite>false</cite> if the
     *         position error does not exceed <cite>maxDeviation</cite>
     */
    static boolean checkPositionError(final Point2d medianPoint, final Point2d prevPoint, final Point2d nextPoint,
            final double maxDeviation)
    {
        Point2d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
        double errorPosition = medianPoint.distance(projectedPoint);
        return errorPosition > maxDeviation;
    }

    /**
     * Check direction difference at the start and end of a segment.
     * @param segmentDirection double; direction of the segment
     * @param curveDirectionAtStart double; direction of the curve at the start of the segment
     * @param curveDirectionAtEnd double; direction of the curve at the end of the segment
     * @param maxDirectionDeviation double; maximum permitted direction difference
     * @return boolean; <cite>true</cite> if the direction difference at the start and the end of the segment is smaller than
     *         <cite>maxDirectionDeviation</cite>; <cite>false</cite> if the direction difference at the start, or the end of
     *         the segment equals or exceeds <cite>maxDirectionDeviation</cite>
     */
    static boolean checkDirectionError(final double segmentDirection, final double curveDirectionAtStart,
            final double curveDirectionAtEnd, final double maxDirectionDeviation)
    {
        // System.out.println("segmentDirection=" + segmentDirection + ", curveDirectionAtStart=" + curveDirectionAtStart
        // + ", curveDirectionAtEnd=" + curveDirectionAtEnd);
        return (Math.abs(AngleUtil.normalizeAroundZero(segmentDirection - curveDirectionAtStart)) > maxDirectionDeviation)
                || Math.abs(AngleUtil.normalizeAroundZero(segmentDirection - curveDirectionAtEnd)) >= maxDirectionDeviation;
    }

    /**
     * Flattener based on number of segments.
     */
    class NumSegments implements Flattener2d
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
        public PolyLine2d flatten(final Flattable2d line)
        {
            Throw.whenNull(line, "Line function may not be null.");
            List<Point2d> points = new ArrayList<>(this.numSegments + 1);
            for (int i = 0; i <= this.numSegments; i++)
            {
                double fraction = ((double) i) / this.numSegments;
                points.add(line.getPoint(fraction));
            }
            return new PolyLine2d(points);
        }
    }

    /**
     * Flattener based on maximum deviation.
     */
    class MaxDeviation implements Flattener2d
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
        public PolyLine2d flatten(final Flattable2d line)
        {
            Throw.whenNull(line, "Line function may not be null");
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            loadKinks(result, line);

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point2d prevPoint = result.get(prevT);
            Map.Entry<Double, Point2d> entry;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point2d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point2d medianPoint = line.getPoint(medianT);

                // Check max deviation
                if (checkPositionError(medianPoint, prevPoint, nextPoint, this.maxDeviation))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }

                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && Flattener2d.checkInflectionPoint(line, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (Flattener2d.checkLoopBack(line.getDirection(prevT), line.getDirection(nextT)))
                {
                    // The curve loops back onto itself. Inserting the halfway point should prevent missing out a major detour
                    // This check is NOT needed in the MaxDeviationAndAngle flattener.
                    result.put(medianT, medianPoint);
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

    /**
     * Flattener based on maximum deviation and maximum angle.
     */
    class MaxDeviationAndAngle implements Flattener2d
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
        public MaxDeviationAndAngle(final double maxDeviation, final double maxAngle)
        {
            Throw.when(maxDeviation <= 0.0 || Double.isNaN(maxDeviation), IllegalArgumentException.class,
                    "Maximum deviation must be above 0.0 and finite");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0");
            this.maxDeviation = maxDeviation;
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine2d flatten(final Flattable2d line)
        {
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            loadKinks(result, line);
            Map<Double, Double> directions = new LinkedHashMap<>();
            Set<Double> kinks = new HashSet<>();
            for (double fraction : result.keySet())
            {
                directions.put(fraction, line.getDirection(fraction));
                kinks.add(fraction);
            }

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point2d prevPoint = result.get(prevT);
            Map.Entry<Double, Point2d> entry;
            int iterationsAtSinglePoint = 0;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point2d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point2d medianPoint = line.getPoint(medianT);

                // Check max deviation
                if (checkPositionError(medianPoint, prevPoint, nextPoint, this.maxDeviation))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    continue;
                }

                // Check max angle
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
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

                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && Flattener2d.checkInflectionPoint(line, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (kinks.contains(prevT))
                {
                    directions.put(prevT, line.getDirection(prevT + Math.ulp(prevT)));
                }
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

    /**
     * Flattener based on maximum angle.
     */
    class MaxAngle implements Flattener2d
    {
        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Constructor.
         * @param maxAngle maximum angle.
         */
        public MaxAngle(final double maxAngle)
        {
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0");
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine2d flatten(final Flattable2d line)
        {
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            loadKinks(result, line);
            Map<Double, Double> directions = new LinkedHashMap<>();
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
            Point2d prevPoint = result.get(prevT);
            Map.Entry<Double, Point2d> entry;
            int iterationsAtSinglePoint = 0;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point2d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;

                // Check max angle
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
                {
                    // We need to insert another point
                    Point2d medianPoint = line.getPoint(medianT);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t={}). Likely there is an (unreported) kink in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (Flattener2d.checkInflectionPoint(line, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    Point2d medianPoint = line.getPoint(medianT);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (kinks.contains(prevT))
                {
                    directions.put(prevT, line.getDirection(prevT + Math.ulp(prevT)));
                }
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

}
