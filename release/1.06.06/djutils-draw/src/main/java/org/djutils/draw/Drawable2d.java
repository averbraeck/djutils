package org.djutils.draw;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;

/**
 * Drawable2d.java. Interface that all drawable objects that use 2d coordinates must implement.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Drawable2d extends Drawable<Point2d, Space2d>
{
    /**
     * Retrieve the bounding rectangle of the object.
     * @return Bounds2d; the bounding box of the object
     */
    Bounds2d getBounds();
    
}
