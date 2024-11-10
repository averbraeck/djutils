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

import org.djutils.draw.function.ContinuousPiecewiseLinearFunction;
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
     * @param curve line function
     * @param of the lateral offset to apply
     * @return PolyLine2d; flattened line
     */
    default PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
    {
        return flatten(curve, of);
    }

    /**
     * Load one knot in the map of fractions and points.
     * @param map NavigableMap<Double, Point2d> the map
     * @param knot double; the fraction where the knot occurs
     * @param curve OffsetFlattableLine2d; the curve that can compute the point for each <code>knot</code> position
     * @param of offset data
     * @throws NullPointerException when <code>map</code> is <code>null</code>, <code>knot</code> is <code>null</code>,
     *             <code>curve</code> is <code>null</code>, or <code>of</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>knot &lt; 0.0</code>, or <code>knot &gt; 1.0</code>
     */
    private static void loadKink(final NavigableMap<Double, Point2d> map, final double knot, final OffsetCurve2d curve,
            final ContinuousPiecewiseLinearFunction of)
    {
        Throw.when(knot < 0.0 || knot > 1.0, IllegalArgumentException.class, "Knots must all be between 0.0 and 1.0, (got %f)",
                knot);
        if (knot == 0.0 || knot == 1.0)
        {
            return; // Already loaded by <code>loadKnots</code>
        }
        if (map.containsKey(knot))
        {
            return;
        }

        double knotFraction = curve.getT(knot * curve.getLength()); // Translate fraction on of to fraction on curve
        Point2d knotPoint = curve.getPoint(knotFraction, of);
        // System.out.println("# Processing knot at " + knot + ", getT " + knotFraction + " point " + knotPoint);
        map.put(knotFraction, knotPoint);
    }

    /**
     * Load the knots into the navigable map (including the start point and the end point).
     * @param map navigableMap<Double, Point2d>; the navigable map
     * @param curve OffsetFlattableLine2d; the curve
     * @param of ContinuousPiecewiseLinearFunction; the offset data
     */
    private static void loadKinks(final NavigableMap<Double, Point2d> map, final OffsetCurve2d curve,
            final ContinuousPiecewiseLinearFunction of)
    {
        map.put(0.0, curve.getPoint(0.0, of));
        Set<Double> knots = curve.getKnots();
        if (null != knots)
        {
            for (double knot : knots)
            {
                loadKink(map, knot, curve, of);
            }
        }
        for (ContinuousPiecewiseLinearFunction.TupleSt knot : of)
        {
            loadKink(map, knot.s(), curve, of);
        }
        map.put(1.0, curve.getPoint(1.0, of));
    }

    /**
     * Check for an inflection point by creating additional points at one quarter and three quarters. If these are on opposite
     * sides of the line from prevPoint to nextPoint; there must be an inflection point.
     * https://stackoverflow.com/questions/1560492/how-to-tell-whether-a-point-is-to-the-right-or-left-side-of-a-line
     * @param curve OffsetFlattableLine2d
     * @param prevT double; t of preceding inserted point
     * @param medianT double; t of point currently considered for insertion
     * @param nextT double; t of following inserted point
     * @param prevPoint Point2d; point on <code>curve</code> at <code>prevT</code>
     * @param nextPoint Point2d; point on <code>curve</code> at <code>nextT</code>
     * @param of ContinuousPiecewiseLinearFunction; information about lateral offsets (may be <code>null</code>)
     * @return boolean; <code>true</code> if there is an inflection point between <code>prevT</code> and <code>nextT</code>;
     *         <code>false</code> if there is no inflection point between <code>prevT</code> and <code>nextT</code>
     */
    private static boolean checkInflectionPoint(final OffsetCurve2d curve, final double prevT, final double medianT,
            final double nextT, final Point2d prevPoint, final Point2d nextPoint, final ContinuousPiecewiseLinearFunction of)
    {
        Point2d oneQuarter = curve.getPoint((prevT + medianT) / 2, of);
        int sign1 = (int) Math.signum((nextPoint.x - prevPoint.x) * (oneQuarter.y - prevPoint.y)
                - (nextPoint.y - prevPoint.y) * (oneQuarter.x - prevPoint.x));
        Point2d threeQuarter = curve.getPoint((nextT + medianT) / 2, of);
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
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction fld)
                throws NullPointerException
        {
            Throw.whenNull(curve, "Line function may not be null");
            List<Point2d> points = new ArrayList<>(this.numSegments + 1);
            for (int i = 0; i <= this.numSegments; i++)
            {
                double fraction = ((double) i) / this.numSegments;
                points.add(curve.getPoint(fraction, fld));
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
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
        {
            Throw.whenNull(curve, "Line function may not be null");
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            OffsetFlattener2d.loadKinks(result, curve, of);

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point2d prevPoint = result.get(prevT);
            Map.Entry<Double, Point2d> entry;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point2d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point2d medianPoint = curve.getPoint(medianT, of);

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
                        && OffsetFlattener2d.checkInflectionPoint(curve, prevT, medianT, nextT, prevPoint, nextPoint, of))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (Flattener2d.checkLoopBack(curve.getDirection(prevT), curve.getDirection(nextT)))
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
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction fld)
        {
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            OffsetFlattener2d.loadKinks(result, curve, fld);
            Map<Double, Double> directions = new LinkedHashMap<>();
            directions.put(0.0, curve.getDirection(0.0, fld));
            Set<Double> knots = new HashSet<>();
            for (double fraction : result.keySet())
            {
                if (fraction > 0)
                {
                    directions.put(fraction, curve.getDirection(fraction - Math.ulp(fraction), fld));
                }
                knots.add(fraction);
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
                Point2d medianPoint = curve.getPoint(medianT, fld);

                // Check max deviation
                Point2d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= this.maxDeviation)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, fld));
                    continue;
                }

                // Check max angle
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, fld));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t=%f). Likely there is an (unreported) knot in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;

                if (OffsetFlattener2d.checkInflectionPoint(curve, prevT, medianT, nextT, prevPoint, nextPoint, fld))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, fld));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (prevT < 1.0 && knots.contains(prevT))
                {
                    directions.put(prevT, curve.getDirection(prevT + Math.ulp(prevT), fld));
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
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
        {
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            OffsetFlattener2d.loadKinks(result, curve, of);
            Map<Double, Double> directions = new LinkedHashMap<>();
            directions.put(0.0, curve.getDirection(0.0, of)); // directions can't do ULP before 0.0
            Set<Double> knots = new HashSet<>();
            for (double knot : result.keySet())
            {
                if (knot > 0)
                {
                    directions.put(knot, curve.getDirection(knot - Math.ulp(knot), of));
                }
                if (knot != 0.0 && knot != 1.0)
                {
                    knots.add(knot);
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
                    Point2d medianPoint = curve.getPoint(medianT, of);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, of));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t=%f). Likely there is an (unreported) knot in the FlattableLine.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (OffsetFlattener2d.checkInflectionPoint(curve, prevT, medianT, nextT, prevPoint, nextPoint, of))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    Point2d medianPoint = curve.getPoint(medianT, of);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, of));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (knots.contains(prevT))
                {
                    directions.put(prevT, curve.getDirection(prevT + Math.ulp(prevT), of));
                }
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

}
