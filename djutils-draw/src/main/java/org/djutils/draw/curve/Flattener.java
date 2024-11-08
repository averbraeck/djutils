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
 * @param <FL> the Flattable Line type
 * @param <PL> the PolyLine type
 * @param <P> the Point type
 */
public interface Flattener<F extends Flattener<F, FL, PL, P>, FL extends Flattable<F, PL, P, ?>,
        PL extends PolyLine<?, P, ?, ?, ?>, P extends Point<P>>
{
    /**
     * Flatten a Flattable line into a PolyLine.
     * @param line line function.
     * @return PolyLine; flattened line.
     * @throws NullPointerException when <code>line</code> is <code>null</code>
     */
    default PL flatten(final FL line)
    {
        return flatten(line);
    }

    /**
     * Load one kink in the map of fractions and points.
     * @param map NavigableMap&lt;Double, P&gt;; the map
     * @param kink double; the fraction where the kink occurs
     * @param line FlattableLine2d; the line that can compute the point
     */
    default void loadKink(final NavigableMap<Double, P> map, final double kink, final FL line)
    {
        Throw.when(kink < 0.0 || kink > 1.0, IllegalArgumentException.class, "Kinks must all be between 0.0 and 1.0, (got %f)",
                kink);
        if (map.containsKey(kink))
        {
            return;
        }
        map.put(kink, line.getPoint(kink));
    }

    /**
     * Load the kinks into the navigable map (including the start point and the end point).
     * @param map navigableMap&lt;Double, P&gt;; the navigable map
     * @param line FlattableLine; the FlattableLine that can yield a Point2d for every kink position
     */
    default void loadKinks(final NavigableMap<Double, P> map, final FL line)
    {
        map.put(0.0, line.getPoint(0.0));
        Set<Double> kinks = line.getKinks();
        if (null != kinks)
        {
            for (double kink : kinks)
            {
                loadKink(map, kink, line);
            }
        }
        map.put(1.0, line.getPoint(1.0));
    }

}
