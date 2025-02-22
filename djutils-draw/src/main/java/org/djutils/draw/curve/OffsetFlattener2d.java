package org.djutils.draw.curve;

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
import org.djutils.math.AngleUtil;

/**
 * Flattens a Curve2d with piece-wise linear offset in to a PolyLine2d.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface OffsetFlattener2d extends Flattener<Flattener2d, Curve2d, PolyLine2d, Point2d, Double>
{
    /**
     * Flatten a OffsetCurve2d curve into a PolyLine2d while applying lateral offsets.
     * @param curve the curve
     * @param of the lateral offset to apply
     * @return flattened curve
     */
    PolyLine2d flatten(OffsetCurve2d curve, ContinuousPiecewiseLinearFunction of);

    /**
     * Load one knot in the map of fractions and points.
     * @param map the map
     * @param knot the fraction where the knot occurs
     * @param curve the curve that can compute the point for each <code>knot</code> position
     * @param of offset data
     * @throws NullPointerException when <code>map</code> is <code>null</code>, <code>knot</code> is <code>null</code>,
     *             <code>curve</code> is <code>null</code>, or <code>of</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>knot &lt; 0.0</code>, or <code>knot &gt; 1.0</code>
     */
    default void loadKnot(final NavigableMap<Double, Point2d> map, final double knot, final OffsetCurve2d curve,
            final ContinuousPiecewiseLinearFunction of)
    {
        Throw.when(knot < 0.0 || knot > 1.0, IllegalArgumentException.class, "Knots must all be between 0.0 and 1.0, (got %f)",
                knot);
        double t = curve.getT(knot * curve.getLength());
        if (map.containsKey(t))
        {
            return;
        }
        map.put(t, curve.getPoint(t, of));
    }

    /**
     * Load the knots into the navigable map (including the start point and the end point).
     * @param map the navigable map
     * @param curve the curve
     * @param of the offset data
     */
    default void loadKnots(final NavigableMap<Double, Point2d> map, final OffsetCurve2d curve,
            final ContinuousPiecewiseLinearFunction of)
    {
        map.put(0.0, curve.getPoint(0.0, of));
        Set<Double> knots = curve.getKnots();
        if (null != knots)
        {
            for (double knot : knots)
            {
                loadKnot(map, knot, curve, of);
            }
        }
        for (ContinuousPiecewiseLinearFunction.TupleSt knot : of)
        {
            loadKnot(map, knot.s(), curve, of);
        }
        map.put(1.0, curve.getPoint(1.0, of));
    }

    @Override
    default boolean checkLoopBack(final Double prevDirection, final Double nextDirection)
    {
        return Math.abs(AngleUtil.normalizeAroundZero(nextDirection - prevDirection)) > Math.PI / 2;
    }

    @Override
    default boolean checkDirectionError(final Double segmentDirection, final Double curveDirectionAtStart,
            final Double curveDirectionAtEnd, final double maxDirectionDeviation)
    {
        return (Math.abs(AngleUtil.normalizeAroundZero(segmentDirection - curveDirectionAtStart)) > maxDirectionDeviation)
                || Math.abs(AngleUtil.normalizeAroundZero(segmentDirection - curveDirectionAtEnd)) >= maxDirectionDeviation;
    }

    /**
     * Make a FlattableCurve object.
     * @param curve the curve
     * @param of the offset function
     * @return the flattable curve
     */
    default Flattener.FlattableCurve<Point2d, Double> makeFlattableCurve(final OffsetCurve2d curve,
            final ContinuousPiecewiseLinearFunction of)
    {
        return new Flattener.FlattableCurve<Point2d, Double>()
        {
            @Override
            public Point2d getPoint(final double fraction)
            {
                return curve.getPoint(fraction, of);
            }

            @Override
            public Double getDirection(final double fraction)
            {
                return curve.getDirection(fraction, of);
            }
        };
    }

    /**
     * Flattener that approximates the <code>OffsetCurve2d</code> with a specified number of segments.
     */
    class NumSegments implements OffsetFlattener2d
    {
        /** Number of segments. */
        private final int numSegments;

        /**
         * Construct a flattener that approximates the <code>OffsetCurve2d</code> with a specified number of segments.
         * @param numSegments number of segments to use in the construction of the <code>PolyLine2d</code>
         * @throws IllegalArgumentException when <code>numSegments &lt; 1</code>
         */
        public NumSegments(final int numSegments)
        {
            Throw.when(numSegments < 1, IllegalArgumentException.class, "Number of segments must be at least 1.");
            this.numSegments = numSegments;
        }

        @Override
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
        {
            Throw.whenNull(curve, "curve");
            List<Point2d> points = new ArrayList<>(this.numSegments + 1);
            for (int i = 0; i <= this.numSegments; i++)
            {
                double fraction = ((double) i) / this.numSegments;
                points.add(curve.getPoint(fraction, of));
            }
            return new PolyLine2d(points);
        }
    }

    /**
     * Flattener that limits the distance between the <code>OffsetCurvee2d</code> and the <code>PolyLine2d</code>.
     */
    class MaxDeviation implements OffsetFlattener2d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /**
         * Construct an OffsetFlattener that limits the distance between the <code>OffsetCurve2d</code> and the
         * <code>PolyLine2d</code>.
         * @param maxDeviation maximum deviation, must be above 0.0
         * @throws ArithmeticException when <code>maxDeviation</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxDeviation &le; 0.0</code>
         */
        public MaxDeviation(final double maxDeviation)
        {
            Throw.whenNaN(maxDeviation, "maxDeviation");
            Throw.when(maxDeviation <= 0.0, IllegalArgumentException.class, "Maximum deviation must be above 0.0 and finite");
            this.maxDeviation = maxDeviation;
        }

        @Override
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
        {
            Throw.whenNull(curve, "curve");
            Flattener.FlattableCurve<Point2d, Double> fc = makeFlattableCurve(curve, of);
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            loadKnots(result, curve, of);

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
                if (checkPositionError(medianPoint, prevPoint, nextPoint, this.maxDeviation))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && checkInflectionPoint(fc, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (checkLoopBack(curve.getDirection(prevT), curve.getDirection(nextT)))
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
     * OffsetFlattener that limits distance <b>and</b> angle difference between the <code>OffsetCurve2d</code> and the
     * <code>PolyLine2d</code>.
     */
    class MaxDeviationAndAngle implements OffsetFlattener2d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Construct a flattener that limits distance <b>and</b> angle difference between the <code>OffsetCurve2d</code> and the
         * <code>PolyLine2d</code>.
         * @param maxDeviation maximum deviation, must be above 0.0
         * @param maxAngle maximum angle, must be above 0.0
         * @throws ArithmeticException when <code>maxDeviation</code>, or <code>maxAngle</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxDeviation &le; 0.0</code>, or <code>maxAngle &le; 0.0</code>
         */
        public MaxDeviationAndAngle(final double maxDeviation, final double maxAngle)
        {
            Throw.whenNaN(maxDeviation, "maxDeviation");
            Throw.whenNaN(maxAngle, "maxAngle");
            Throw.when(maxDeviation <= 0.0, IllegalArgumentException.class, "Maximum deviation must be above 0.0.");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0.");
            this.maxDeviation = maxDeviation;
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
        {
            Throw.whenNull(curve, "curve");
            Flattener.FlattableCurve<Point2d, Double> fc = makeFlattableCurve(curve, of);
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            loadKnots(result, curve, of);
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
                Point2d medianPoint = curve.getPoint(medianT, of);
                // Check max deviation
                if (checkPositionError(medianPoint, prevPoint, nextPoint, this.maxDeviation))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, of));
                    continue;
                }
                // Check max angle
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, of));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 times "
                            + "around the same point (t=%f). Likely there is an (unreported) knot in the OffsetCurve2d.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && checkInflectionPoint(fc, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT, of));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (prevT < 1.0 && knots.contains(prevT))
                {
                    directions.put(prevT, curve.getDirection(prevT + Math.ulp(prevT), of));
                }
            }
            return new PolyLine2d(result.values().iterator());
        }
    }

    /**
     * OffsetFlattener that limits the angle difference between the <code>OffsetCurve2d</code> and the <code>PolyLine2d</code>.
     */
    class MaxAngle implements OffsetFlattener2d
    {
        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Construct a flattener that limits the angle difference between the <code>OffsetCurve2d</code> and the
         * <code>PolyLine2d</code>.
         * @param maxAngle maximum angle.
         * @throws ArithmeticException when <code>maxAngle</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxAngle &le; 0.0</code>
         */
        public MaxAngle(final double maxAngle)
        {
            Throw.whenNaN(maxAngle, "maxAngle");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0.");
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine2d flatten(final OffsetCurve2d curve, final ContinuousPiecewiseLinearFunction of)
        {
            Throw.whenNull(curve, "curve");
            Flattener.FlattableCurve<Point2d, Double> fc = makeFlattableCurve(curve, of);
            NavigableMap<Double, Point2d> result = new TreeMap<>();
            loadKnots(result, curve, of);
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
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class, "Required a new point 50 "
                            + "times around the same point (t=%f). Likely there is an (unreported) knot in the OffsetCurve2d.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (checkInflectionPoint(fc, prevT, medianT, nextT, prevPoint, nextPoint))
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
