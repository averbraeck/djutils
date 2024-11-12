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
public interface Flattener3d extends Flattener<Flattener3d, Curve3d, PolyLine3d, Point3d>
{

    @Override
    default PolyLine3d flatten(final Curve3d curve)
    {
        return flatten(curve);
    }

    /**
     * Check for an inflection point by computing additional points at one quarter and three quarters. If these are on opposite
     * sides of the curve3d from prevPoint to nextPoint; there must be an inflection point.
     * @param curve Curve3d
     * @param prevT double; t of preceding inserted point
     * @param medianT double; t of point currently considered for insertion
     * @param nextT double; t of following inserted point
     * @param prevPoint Point3d; point on <code>curve</code> at <code>prevT</code>
     * @param nextPoint Point3d; point on <code>curve</code> at <code>nextT</code>
     * @return boolean; <code>true</code> if there is an inflection point between <code>prevT</code> and <code>nextT</code>;
     *         <code>false</code> if there is no inflection point between <code>prevT</code> and <code>nextT</code>
     */
    private static boolean checkInflection(final Curve3d curve, final double prevT, final double medianT, final double nextT,
            final Point3d prevPoint, final Point3d nextPoint)
    {
        Point3d oneQuarter = curve.getPoint((prevT + medianT) / 2);
        Direction3d oneQDir = oneQuarter.directionTo(oneQuarter.closestPointOnSegment(prevPoint, nextPoint));
        Point3d threeQuarter = curve.getPoint((nextT + medianT) / 2);
        Direction3d threeQDir = threeQuarter.directionTo(threeQuarter.closestPointOnSegment(prevPoint, nextPoint));
        Double angle = oneQDir.directionDifference(threeQDir);
        return angle > Math.PI / 2; // Projection direction varies by more than 90 degrees
    }

    /**
     * Check for a direction change of more than 90 degrees. If that happens, the MaxDeviation flattener must zoom in closer.
     * @param prevDirection double; the direction at the preceding (already added) point
     * @param nextDirection double; the direction at the succeeding (already added) point
     * @return boolean; <code>true</code> if the curve changes direction by more than 90 degrees; <code>false</code> if the
     *         curve does not change direction by more than 90 degrees
     */
    static boolean checkLoopBack(final Direction3d prevDirection, final Direction3d nextDirection)
    {
        return prevDirection.directionDifference(nextDirection) > Math.PI / 2;
    }

    /**
     * Check the position error at a point.
     * @param medianPoint Point3d; point at the median t value
     * @param prevPoint Point3d; point at the start of the current segment (should be on the Curve3d)
     * @param nextPoint Point3d; point at the end of the current segment (should be on the Curve3d)
     * @param maxDeviation double; maximum allowed position error
     * @return boolean; <code>true</code> if the position error exceeds <code>maxDeviation</code>; <code>false</code> if the
     *         position error does not exceed <code>maxDeviation</code>
     */
    static boolean checkPositionError(final Point3d medianPoint, final Point3d prevPoint, final Point3d nextPoint,
            final double maxDeviation)
    {
        Point3d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
        double positionError = medianPoint.distance(projectedPoint);
        return positionError > maxDeviation;
    }

    /**
     * Check direction difference at the start and end of a segment.
     * @param segmentDirection double; direction of the segment
     * @param curveDirectionAtStart double; direction of the curve at the start of the segment
     * @param curveDirectionAtEnd double; direction of the curve at the end of the segment
     * @param maxDirectionDeviation double; maximum permitted direction difference
     * @return boolean; <code>true</code> if the direction difference at the start and the end of the segment is smaller than
     *         <code>maxDirectionDeviation</code>; <code>false</code> if the direction difference at the start, or the end of
     *         the segment equals or exceeds <code>maxDirectionDeviation</code>
     */
    static boolean checkDirectionError(final Direction3d segmentDirection, final Direction3d curveDirectionAtStart,
            final Direction3d curveDirectionAtEnd, final double maxDirectionDeviation)
    {
        // System.out.println("segmentDirection=" + segmentDirection + ", curveDirectionAtStart=" + curveDirectionAtStart
        // + ", curveDirectionAtEnd=" + curveDirectionAtEnd);
        return (segmentDirection.directionDifference(curveDirectionAtStart) > maxDirectionDeviation
                || segmentDirection.directionDifference(curveDirectionAtEnd) > maxDirectionDeviation);
    }

    /**
     * Flattener that approximates the <code>Curve3d</code> with a specified number of segments.
     */
    class NumSegments implements Flattener3d
    {
        /** Number of segments. */
        private final int numSegments;

        /**
         * Construct a flattener that approximates the <code>Curve3d</code> with a specified number of segments.
         * @param numSegments int; number of segments to use in the construction of the PolyLine3d.
         * @throws IllegalArgumentException when <code>numSegments &lt; 1</code>
         */
        public NumSegments(final int numSegments)
        {
            Throw.when(numSegments < 1, IllegalArgumentException.class, "Number of segments must be at least 1");
            this.numSegments = numSegments;
        }

        @Override
        public PolyLine3d flatten(final Curve3d curve) throws NullPointerException
        {
            Throw.whenNull(curve, "curve");
            List<Point3d> points = new ArrayList<>(this.numSegments + 1);
            for (int i = 0; i <= this.numSegments; i++)
            {
                double fraction = ((double) i) / this.numSegments;
                points.add(curve.getPoint(fraction));
            }
            return new PolyLine3d(points);
        }
    }

    /**
     * Flattener that limits the distance between the <code>Curve3d</code> and the <code>PolyLine3d</code>.
     */
    class MaxDeviation implements Flattener3d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /**
         * Construct a flattener that limits the distance between the <code>Curve3d</code> and the <code>PolyLine3d</code>.
         * @param maxDeviation maximum deviation, must be above 0.0.
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
        public PolyLine3d flatten(final Curve3d curve)
        {
            Throw.whenNull(curve, "curve");
            NavigableMap<Double, Point3d> result = new TreeMap<>();
            loadKnots(result, curve);

            // Walk along all point pairs and see if additional points need to be inserted
            double prevT = result.firstKey();
            Point3d prevPoint = result.get(prevT);
            Map.Entry<Double, Point3d> entry;
            while ((entry = result.higherEntry(prevT)) != null)
            {
                double nextT = entry.getKey();
                Point3d nextPoint = entry.getValue();
                double medianT = (prevT + nextT) / 2;
                Point3d medianPoint = curve.getPoint(medianT);
                // Check max deviation
                if (checkPositionError(medianPoint, prevPoint, nextPoint, this.maxDeviation))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && Flattener3d.checkInflection(curve, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (Flattener3d.checkLoopBack(curve.getDirection(prevT), curve.getDirection(nextT)))
                {
                    // The curve loops back onto itself. Inserting the halfway point should prevent missing out a major detour
                    // This check is NOT needed in the MaxDeviationAndAngle flattener.
                    result.put(medianT, medianPoint);
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
            }
            return new PolyLine3d(result.values().iterator());
        }
    }

    /**
     * Flattener that limits the distance <b>and</b> angle difference between the <code>Curve3d</code> and the
     * <code>PolyLine3d</code>.
     */
    class MaxDeviationAndAngle implements Flattener3d
    {
        /** Maximum deviation. */
        private final double maxDeviation;

        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Construct a flattener that limits the distance <b>and</b> angle difference between the <code>curve3d</code> and the
         * <code>PolyLine3d</code>.
         * @param maxDeviation maximum deviation, must be above 0.0
         * @param maxAngle maximum angle, must be above 0.0
         * @throws ArithmeticException when <code>maxDeviation</code>, or <code>maxAngle</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxDeviation &le; 0.0</code>, or <code>maxAngle &le; 0.0</code>
         */
        public MaxDeviationAndAngle(final double maxDeviation, final double maxAngle)
        {
            Throw.whenNaN(maxDeviation, "maxDeviation");
            Throw.whenNaN(maxAngle, "maxAngle");
            Throw.when(maxDeviation <= 0.0, IllegalArgumentException.class, "Maximum deviation must be above 0.0 and finite");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0");
            this.maxDeviation = maxDeviation;
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine3d flatten(final Curve3d curve) throws NullPointerException
        {
            NavigableMap<Double, Point3d> result = new TreeMap<>();
            loadKnots(result, curve);
            Map<Double, Direction3d> directions = new LinkedHashMap<>();
            Set<Double> knots = new HashSet<>();
            for (double fraction : result.keySet())
            {
                directions.put(fraction, curve.getDirection(fraction));
                knots.add(fraction);
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
                Point3d medianPoint = curve.getPoint(medianT);
                // Check max deviation
                if (checkPositionError(medianPoint, prevPoint, nextPoint, this.maxDeviation))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT));
                    continue;
                }
                // Check max angle
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class,
                            "Required a new point 50 times "
                                    + "around the same point (t=%f). Likely there is an (unreported) knot in the Curve3d.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (prevPoint.distance(nextPoint) > this.maxDeviation
                        && Flattener3d.checkInflection(curve, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (knots.contains(prevT))
                {
                    directions.put(prevT, curve.getDirection(prevT + Math.ulp(prevT)));
                }
            }
            return new PolyLine3d(result.values().iterator());
        }
    }

    /**
     * Flattener that limits the angle difference between the <code>Curve3d</code> and the <code>PolyLine3d</code>.
     */
    class MaxAngle implements Flattener3d
    {
        /** Maximum angle. */
        private final double maxAngle;

        /**
         * Flattener that limits the angle difference between the <code>Curve3d</code> and the <code>PolyLine3d</code>.
         * @param maxAngle maximum angle
         * @throws ArithmeticException when <code>maxAngle</code> is <code>NaN</code>
         * @throws IllegalArgumentException when <code>maxAngle &le; 0.0</code>
         */
        public MaxAngle(final double maxAngle)
        {
            Throw.whenNaN(maxAngle, "maxAngle");
            Throw.when(maxAngle <= 0.0, IllegalArgumentException.class, "Maximum angle must be above 0.0");
            this.maxAngle = maxAngle;
        }

        @Override
        public PolyLine3d flatten(final Curve3d curve)
        {
            NavigableMap<Double, Point3d> result = new TreeMap<>();
            loadKnots(result, curve);
            Map<Double, Direction3d> directions = new LinkedHashMap<>();
            directions.put(0.0, curve.getDirection(0.0)); // directions can't do ULP before 0.0
            Set<Double> knots = new HashSet<>();
            for (double knot : result.keySet())
            {
                if (knot > 0)
                {
                    directions.put(knot, curve.getDirection(knot - Math.ulp(knot)));
                }
                if (knot != 0.0 && knot != 1.0)
                {
                    knots.add(knot);
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
                if (checkDirectionError(prevPoint.directionTo(nextPoint), directions.get(prevT), directions.get(nextT),
                        this.maxAngle))
                {
                    // We need to insert another point
                    Point3d medianPoint = curve.getPoint(medianT);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT));
                    iterationsAtSinglePoint++;
                    Throw.when(iterationsAtSinglePoint == 50, IllegalArgumentException.class,
                            "Required a new point 50 times "
                                    + "around the same point (t=%f). Likely there is an (unreported) knot in the Curve3d.",
                            medianT);
                    continue;
                }
                iterationsAtSinglePoint = 0;
                if (Flattener3d.checkInflection(curve, prevT, medianT, nextT, prevPoint, nextPoint))
                {
                    // There is an inflection point, inserting the halfway point should take care of this
                    Point3d medianPoint = curve.getPoint(medianT);
                    result.put(medianT, medianPoint);
                    directions.put(medianT, curve.getDirection(medianT));
                    continue;
                }
                prevT = nextT;
                prevPoint = nextPoint;
                if (knots.contains(prevT))
                {
                    directions.put(prevT, curve.getDirection(prevT + Math.ulp(prevT)));
                }
            }
            return new PolyLine3d(result.values().iterator());
        }
    }

}
