package org.djutils.draw;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;

/**
 * Drawable2d is the interface that all drawable objects that use 2D coordinates must implement.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Drawable2d extends Drawable<Point2d>
{
    /**
     * Retrieve the bounding rectangle of the object in absolute coordinates.
     * @return the bounding box of the object in absolute coordinates
     */
    Bounds2d getAbsoluteBounds();

    @Override
    default int getDimensions()
    {
        return 2;
    }

}
