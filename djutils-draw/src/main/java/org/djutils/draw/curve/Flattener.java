package org.djutils.draw.curve;

import java.util.NavigableMap;
import java.util.Set;

import org.djutils.draw.line.PolyLine;
import org.djutils.draw.point.Point;
import org.djutils.exceptions.Throw;

/**
 * Flattens a Curve in to a PolyLine.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <F> the Flattener type
 * @param <C> the Curve type
 * @param <PL> the PolyLine type
 * @param <P> the Point type
 * @param <DIR> the Direction type
 */
public interface Flattener<F extends Flattener<F, C, PL, P, DIR>, C extends Curve<?, ?, P, F, PL>,
        PL extends PolyLine<?, P, ?, ?, ?>, P extends Point<P>, DIR>
{
    /**
     * Load one knot in the map of fractions and points.
     * @param map the map
     * @param knot the fraction where the knot occurs
     * @param curve the curve that can compute the point
     */
    default void loadKnot(final NavigableMap<Double, P> map, final double knot, final C curve)
    {
        Throw.when(knot < 0.0 || knot > 1.0, IllegalArgumentException.class, "Knots must all be between 0.0 and 1.0, (got %f)",
                knot);
        if (map.containsKey(knot))
        {
            return;
        }
        map.put(knot, curve.getPoint(knot));
    }

    /**
     * Load the knots into the navigable map (including the start point and the end point).
     * @param map the navigable map
     * @param curve the curve that can yield a Point for every knot position
     */
    default void loadKnots(final NavigableMap<Double, P> map, final C curve)
    {
        map.put(0.0, curve.getPoint(0.0));
        Set<Double> knots = curve.getKnots();
        if (null != knots)
        {
            for (double knot : knots)
            {
                loadKnot(map, knot, curve);
            }
        }
        map.put(1.0, curve.getPoint(1.0));
    }

    /**
     * Report if the medianPoint is too far from the line segment from prevPoint to nextPoint.
     * @param medianPoint P
     * @param prevPoint P
     * @param nextPoint P
     * @param maxDeviation double
     * @return <code>true</code> if the <code>medianPoint</code> is too far from the line segment; <code>false</code>
     *         if the <code>medianPoint</code> is close enough to the line segment
     */
    default boolean checkPositionError(final P medianPoint, final P prevPoint, final P nextPoint, final double maxDeviation)
    {
        P projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
        double positionError = medianPoint.distance(projectedPoint);
        return positionError > maxDeviation;
    }

    /**
     * Check for a direction change of more than 90 degrees. If that happens, the MaxDeviation flattener must zoom in closer.
     * @param prevDirection the direction at the preceding (already added) point
     * @param nextDirection the direction at the succeeding (already added) point
     * @return <code>true</code> if the curve changes direction by more than 90 degrees; <code>false</code> if the
     *         curve does not change direction by more than 90 degrees
     */
    boolean checkLoopBack(DIR prevDirection, DIR nextDirection);

    /**
     * Check direction difference at the start and end of a segment.
     * @param segmentDirection direction of the segment
     * @param curveDirectionAtStart direction of the curve at the start of the segment
     * @param curveDirectionAtEnd direction of the curve at the end of the segment
     * @param maxDirectionDeviation maximum permitted direction difference
     * @return <code>true</code> if the direction difference at the start and the end of the segment is smaller than
     *         <code>maxDirectionDeviation</code>; <code>false</code> if the direction difference at the start, or the end of
     *         the segment equals or exceeds <code>maxDirectionDeviation</code>
     */
    boolean checkDirectionError(DIR segmentDirection, DIR curveDirectionAtStart, DIR curveDirectionAtEnd,
            double maxDirectionDeviation);

    /**
     * Check for an inflection point by computing additional points at one quarter and three quarters. If these are on opposite
     * sides of the curve2d from prevPoint to nextPoint; there must be an inflection point. This default implementation is
     * <b>only for the 2d case</b>.
     * https://stackoverflow.com/questions/1560492/how-to-tell-whether-a-point-is-to-the-right-or-left-side-of-a-line
     * @param curve Curve2d
     * @param prevT t of preceding inserted point
     * @param medianT t of point currently considered for insertion
     * @param nextT t of following inserted point
     * @param prevPoint point on <code>curve</code> at <code>prevT</code>
     * @param nextPoint point on <code>curve</code> at <code>nextT</code>
     * @return <code>true</code> if there is an inflection point between <code>prevT</code> and <code>nextT</code>;
     *         <code>false</code> if there is no inflection point between <code>prevT</code> and <code>nextT</code>
     */
    default boolean checkInflectionPoint(final FlattableCurve<P, DIR> curve, final double prevT, final double medianT,
            final double nextT, final P prevPoint, final P nextPoint)
    {
        P oneQuarter = curve.getPoint((prevT + medianT) / 2);
        int sign1 = (int) Math.signum((nextPoint.getX() - prevPoint.getX()) * (oneQuarter.getY() - prevPoint.getY())
                - (nextPoint.getY() - prevPoint.getY()) * (oneQuarter.getX() - prevPoint.getX()));
        P threeQuarter = curve.getPoint((nextT + medianT) / 2);
        int sign2 = (int) Math.signum((nextPoint.getX() - prevPoint.getX()) * (threeQuarter.getY() - prevPoint.getY())
                - (nextPoint.getY() - prevPoint.getY()) * (threeQuarter.getX() - prevPoint.getX()));
        return sign1 != sign2;
    }

    /**
     * Interface for getPoint and getDirection that hide whether or not an offset is applied.
     * @param <P> the Point type
     * @param <DIR> the Direction type
     */
    interface FlattableCurve<P, DIR>
    {
        /**
         * Get a Point for some fraction along the Curve.
         * @param fraction the fraction to use
         * @return the point at the <code>fraction</code>
         */
        P getPoint(double fraction);

        /**
         * Get the direction at some fraction along the Curve.
         * @param fraction the fraction to check
         * @return the direction at the <code>fraction</code>
         */
        DIR getDirection(double fraction);
    }

}
