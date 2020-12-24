package org.djutils.draw;

import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;

/**
 * Drawable3d.java. Interface that all objects that use 3d coordinates must implement.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Drawable3d extends Drawable<Point3d, Space3d>
{
    /**
     * Retrieve the bounding box of the object.
     * @return Bounds3d; the bounding box of the object
     */
    Bounds3d getBounds();
    
    /**
     * Project the object onto the z=0 plane.
     * @return Drawable2d; the projected object
     * @throws DrawRuntimeException when projecting onto the z=0 plane results in an invalid object. E.g. a Line3d that consists
     *             of points that all have the exact same x and y coordinates cannot be a line after projecting on the z=0
     *             plane.
     */
    Drawable2d project() throws DrawRuntimeException;

}
