package org.djutils.draw.curve;

import java.util.NavigableMap;
import java.util.Set;

import org.djutils.draw.line.PolyLine;
import org.djutils.draw.point.Point;
import org.djutils.exceptions.Throw;

/**
 * Flattens a Curve in to a PolyLine.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
 */
public interface Flattener<F extends Flattener<F, C, PL, P>, C extends Curve<?, ?, P, F, PL>,
        PL extends PolyLine<?, P, ?, ?, ?>, P extends Point<P>>
{
    /**
     * Flatten a Curve into a PolyLine.
     * @param curve C; the curve
     * @return PolyLine; flattened line
     * @throws NullPointerException when <code>curve</code> is <code>null</code>
     */
    default PL flatten(final C curve)
    {
        return flatten(curve);
    }

    /**
     * Load one knot in the map of fractions and points.
     * @param map NavigableMap&lt;Double, P&gt;; the map
     * @param knot double; the fraction where the knot occurs
     * @param curve C; the curve that can compute the point
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
     * @param map navigableMap&lt;Double, P&gt;; the navigable map
     * @param curve C; the curve that can yield a Point for every knot position
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

}
