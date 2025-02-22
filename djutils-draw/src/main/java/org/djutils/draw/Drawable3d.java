package org.djutils.draw;

import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;

/**
 * Drawable3d is the Interface that all drawable objects that use 3D coordinates must implement.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Drawable3d extends Drawable<Point3d>
{
    /**
     * Retrieve the bounding box of the object.
     * @return the bounding box of the object
     */
    Bounds3d getBounds();

    @Override
    default int getDimensions()
    {
        return 3;
    }

    /**
     * Project the object onto the z=0 plane.
     * @return the projected object
     * @throws DrawRuntimeException when projecting onto the <code>z=0</code> plane results in an invalid object. E.g. a Line3d
     *             that consists of points that all have the exact same <code>x</code> and <code>y</code> coordinates cannot be
     *             a line after projecting on the <code>z=0</code> plane.
     */
    Drawable2d project();

}
