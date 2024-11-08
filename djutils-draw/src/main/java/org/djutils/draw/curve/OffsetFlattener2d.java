package org.djutils.draw.curve;

import static org.djutils.draw.curve.Flattener2d.checkDirectionError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Flattens a Curve2d with piece-wise linear offset in to a PolyLine2d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface OffsetFlattener2d extends Flattener2d
{
    /**
     * Flatten a OffsetFlattable2d curve into a PolyLine2d while applying lateral offsets.
     * @param line line function
     * @param fld FractionalLengthData the lateral offset to apply
     * @return PolyLine2d; flattened line
     */
    default PolyLine2d flatten(final OffsetFlattable2d line, final PieceWiseLinearOffset2d fld)
    {
        return flatten(line, fld);
    }

    /**
     * Load one kink in the map of fractions and points.
     * @param map NavigableMap<Double, Point2d> the map
     * @param kink double; the fraction where the kink occurs
     * @param line OffsetFlattableLine2d; the line that can compute the point for each <code>kink</code> position
     * @param fld FractionalLengthData; offset data
     * @throws NullPointerException when <code>map</code> is <code>null</code>, <code>kink</code> is <code>null</code>,
     *             <code>line</code> is <code>null</code>, or <code>fld</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>kink &lt; 0.0</code>, or <code>kink &gt; 1.0</code>
     */
    private static void loadKink(final NavigableMap<Double, Point2d> map, final double kink, final OffsetFlattable2d line,
            final PieceWiseLinearOffset2d fld)
    {
        Throw.when(kink < 0.0 || kink > 1.0, IllegalArgumentException.class, "Kinks must all be between 0.0 and 1.0, (got %f)",
                kink);
        if (kink == 0.0 || kink == 1.0)
        {
            return; // Already loaded by <code>loadKinks</code>
        }
        if (map.containsKey(kink))
        {
            return;
        }

        double kinkFraction = line.getT(kink * line.getLength()); // Translate fraction on fld to fraction on line
        Point2d kinkPoint = line.getPoint(kinkFraction, fld);
        // System.out.println("# Processing kink at " + kink + ", getT " + kinkFraction + " point " + kinkPoint);
        map.put(kinkFraction, kinkPoint);
    }

    /**
     * Load the kinks into the navigable map (including the start point and the end point).
     * @param map navigableMap<Double, Point2d>; the navigable map
     * @param line OffsetFlattableLine2d; the OffsetFlattableLine2d
     * @param fld FractionalLengthData2d; the offset data
     */
    private static void loadKinks(final NavigableMap<Double, Point2d> map, final OffsetFlattable2d line,
            final PieceWiseLinearOffset2d fld)
    {
        map.put(0.0, line.getPoint(0.0, fld));
        Set<Double> kinks = line.getKinks();
        if (null != kinks)
        {
            for (double kink : kinks)
            {
                loadKink(map, kink, line, fld);
            }
        }
        for (double kink : fld)
        {
            loadKink(map, kink, line, fld);
        }
        map.put(1.0, line.getPoint(1.0, fld));
    }

    /**
     * Check for an inflection point by creating additional points at one quarter and three quarters. If these are on opposite
     * sides of the line from prevPoint to nextPoint; there must be an inflection point.
     * https://stackoverflow.com/questions/1560492/how-to-tell-whether-a-point-is-to-the-right-or-left-side-of-a-line
     * @param line OffsetFlattableLine2d
     * @param prevT double; t of preceding inserted point
     * @param medianT double; t of point currently considered for insertion
     * @param nextT double; t of following inserted point
     * @param prevPoint Point2d; point on <code>line</code> at <code>prevT</code>
     * @param nextPoint Point2d; point on <code>line</code> at <code>nextT</code>
     * @param fld FractionalLengthData2d; information about lateral offsets (may be  <code>null</code>)
     * @return boolean; <code>true</code> if there is an inflection point between <code>prevT</code> and <code>nextT</code>;
     *         <code>false</code> if there is no inflection point between <code>prevT</code> and <code>nextT</code>
     */
    private static boolean checkInflectionPoint(final OffsetFlattable2d line, final double prevT, final double medianT,
            final double nextT, final Point2d prevPoint, final Point2d nextPoint, final PieceWiseLinearOffset2d fld)
    {
        Point2d oneQuarter = line.getPoint((prevT + medianT) / 2, fld);
        int sign1 = (int) Math.signum((nextPoint.x - prevPoint.x) * (oneQuarter.y - prevPoint.y)
                - (nextPoint.y - prevPoint.y) * (oneQuarter.x - prevPoint.x));
        Point2d threeQuarter = line.getPoint((nextT + medianT) / 2, fld);
        int sign2 = (int) Math.signum((nextPoint.x - prevPoint.x) * (threeQuarter.y - prevPoint.y)
                - (nextPoint.y - prevPoint.y) * (threeQuarter.x - prevPoint.x));
        return sign1 != sign2;
    }

    /**
     * Flattener that approximates the <code>OffsetFlattable2d</code> with a specified number of segments.
     */
    class NumSegments implements OffsetFlattener2d
    {
        /** Number of segments. */
        private final int numSegments;

        /**
         * Construct a flattener that approximates the <code>OffsetFlattable2d</code> with a specified number of segments.
         * @param numSegments int; number of segments to use in the construction of the <code>PolyLine2d</code>
         * @throws IllegalArgumentException when <code>numSegments &lt; 1</code>
         */
        public NumSegments(final int numSegments)
        {
            Throw.when(numSegments < 1, IllegalArgumentException.class, "Number of segments must be at least 1.");
            this.numSegments = numSegments;
        }

        @Override
        public PolyLine2d flatten(final OffsetFlattable2d line, final PieceWiseLinearOffset2d fld) throws NullPointerException
        {
            Throw.whenNull(line, "Line function may not be null");
            List<Point2d> points = new ArrayList<>(this.numSegments + 1);
            for (int i = 0; i <= this.numSegments; i++)
            {
                double fraction = ((double) i) / this.numSegments;
                points.add(line.getPoint(fraction, fld));
            }
            return new PolyLine2d(points);
        }
    }

    /**
     * Flattener that limits the distance between the <code>Flattable2d</code> and the <code>PolyLine2d</code>.
     */
    class MaxDeviation implements OffsetFlattener2d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /**
         * Construct a flattener that limits the distance between the <code>OffsetFlattable2d</code> and the
         * <code>PolyLine2d</code>.
         * @param maxDeviation maximum deviation, must be above 0.0
         * @throws ArithmeticException when <code>maxDeviation</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxDeviation &le; 0.0</code>
         */
        public MaxDeviation(final double maxDeviation)
        {
            Throw.when(maxDeviation <= 0.0, IllegalArgumentException.class, "Maximum deviation must be above 0.0.");
            this.maxDeviation = maxDeviation;
        }

        @Override
        public PolyLine2d flatten(final OffsetFlattable2d line, final PieceWiseLinearOffset2d fld)
        {
            Throw.whenNull(line, "Line function may not be null");
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            OffsetFlattener2d.loadKinks(result, line, fld);

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point2d prevPoint = result.get(prevT);
            Map.Entry<Double, Point2d> entry;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point2d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point2d medianPoint = line.getPoint(medianT, fld);

                // Check max deviation
                Point2d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= this.maxDeviation)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }

                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && OffsetFlattener2d.checkInflectionPoint(line, prevT, medianT, nextT, prevPoint, nextPoint, fld))
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
     * Flattener that limits distance <b>and</b> angle difference between the <code>Flattable2d</code> and the
     * <code>PolyLine2d</code>.
     */
    class MaxDeviationAndAngle implements OffsetFlattener2d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Construct a flattener that limits distance <b>and</b> angle difference between the <code>OffsetFlattable2d</code> and
         * the <code>PolyLine2d</code>.
         * @param maxDeviation maximum deviation, must be above 0.0
         * @param maxAngle maximum angle, must be above 0.0
         * @throws ArithmeticException when <code>maxDeviation</code>, or <code>maxAngle</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxDeviation &le; 0.0</code>, or <code>maxAngle &le; 0.0</code>
         */
        public MaxDeviationAndAngle(final double maxDeviation, final double maxAngle)
        {
            Throw.when(maxDeviation <= 0.0, IllegalArgumentException.class, "Maximum deviation must be above 0.0.");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0.");
            this.maxDeviation = maxDeviation;
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine2d flatten(final OffsetFlattable2d line, final PieceWiseLinearOffset2d fld)
        {
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            OffsetFlattener2d.loadKinks(result, line, fld);
            Map<Double, Double> directions = new LinkedHashMap<>();
            directions.put(0.0, line.getDirection(0.0, fld));
            Set<Double> kinks = new HashSet<>();
            for (double fraction : result.keySet())
            {
                if (fraction > 0)
                {
                    directions.put(fraction, line.getDirection(fraction - Math.ulp(fraction), fld));
                }
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
                Point2d medianPoint = line.getPoint(medianT, fld);

                // Check max deviation
                Point2d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= this.maxDeviation)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT, fld));
                    continue;
                }

                // Check max angle
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT, fld));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t=%f). Likely there is an (unreported) kink in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;

                if (OffsetFlattener2d.checkInflectionPoint(line, prevT, medianT, nextT, prevPoint, nextPoint, fld))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT, fld));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (prevT < 1.0 && kinks.contains(prevT))
                {
                    directions.put(prevT, line.getDirection(prevT + Math.ulp(prevT), fld));
                }
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

    /**
     * Flattener that limits the angle difference between the <code>Flattable2d</code> and the <code>PolyLine2d</code>.
     */
    class MaxAngle implements OffsetFlattener2d
    {
        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Construct a flattener that limits the angle difference between the <code>Flattable2d</code> and the
         * <code>PolyLine2d</code>.
         * @param maxAngle maximum angle.
         * @throws ArithmeticException when <code>maxAngle</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxAngle &le; 0.0</code>
         */
        public MaxAngle(final double maxAngle)
        {
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0.");
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine2d flatten(final OffsetFlattable2d line, final PieceWiseLinearOffset2d fld)
        {
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            OffsetFlattener2d.loadKinks(result, line, fld);
            Map<Double, Double> directions = new LinkedHashMap<>();
            directions.put(0.0, line.getDirection(0.0, fld)); // directions can't do ULP before 0.0
            Set<Double> kinks = new HashSet<>();
            for (double kink : result.keySet())
            {
                if (kink > 0)
                {
                    directions.put(kink, line.getDirection(kink - Math.ulp(kink), fld));
                }
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
                    // System.out.println("Inserting point between " + prevPoint + " (dir=" + directions.get(prevT) + ") and "
                    // + nextPoint + " (dir=" + directions.get(nextT) + ") count=" + iterationsAtSinglePoint
                    // + ", segment angle=" + prevPoint.directionTo(nextPoint));
                    // We need to insert another point
                    Point2d medianPoint = line.getPoint(medianT, fld);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT, fld));
                    iterationsAtSinglePoint++;
                    /*-
                    if (iterationsAtSinglePoint == 20)
                    {
                        System.out.println("# prevT=" + prevT + ", prevPoint=" + prevPoint + " nextT=" + prevT + ", nextPoint="
                                + nextPoint + ", distance=" + prevPoint.distance(nextPoint));
                        System.out.println("c0,0,0" + Export.toPlot(line.toPolyLine2d(new Flattener2d.NumSegments(1000))));
                        System.out.println(
                                "c1,0,0" + Export.toPlot(line.toPolyLine2d(new OffsetFlattener2d.NumSegments(5000), fld)));
                        System.out.println("c0,0,1 " + Export.toPlot(new LineSegment2d(new Point2d(0, 0), prevPoint)));
                        System.out.println("c0,0,1 " + Export.toPlot(new LineSegment2d(new Point2d(0, 0), nextPoint)));
                        System.out.println("Breakpoint  here");
                    }
                    */
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t=%f). Likely there is an (unreported) kink in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (OffsetFlattener2d.checkInflectionPoint(line, prevT, medianT, nextT, prevPoint, nextPoint, fld))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    Point2d medianPoint = line.getPoint(medianT, fld);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, line.getDirection(medianT, fld));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (kinks.contains(prevT))
                {
                    directions.put(prevT, line.getDirection(prevT + Math.ulp(prevT), fld));
                }
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

}
