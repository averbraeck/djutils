package org.djutils.draw.curve;

import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;

/**
 * A Flattable2d has the required methods to allow it to be converted to a PolyLine2d using a Flattener2d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Flattable2d extends Flattable<Flattener2d, PolyLine2d, Point2d, Double>
{

    @Override
    default Double getDirection(final double fraction)
    {
        Point2d p1, p2;
        if (fraction < 0.5) // to prevent going above 1.0
        {
            p1 = getPoint(fraction);
            p2 = getPoint(fraction + 1e-6);
        }
        else
        {
            p1 = getPoint(fraction - 1e-6);
            p2 = getPoint(fraction);
        }
        return p1.directionTo(p2);
    }

}
