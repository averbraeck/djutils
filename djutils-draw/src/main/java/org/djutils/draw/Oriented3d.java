package org.djutils.draw;

import org.djutils.draw.point.OrientedPoint3d;

/**
 * Oriented3d is an interface to indicate an object has a direction in three dimensions.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public interface Oriented3d extends Directed3d
{
    /**
     * Return the rotation around the x-axis in radians.
     * @return the rotation around the x-axis in radians
     */
    double getDirX();

    @Override
    OrientedPoint3d translate(double dR);
    
    @Override
    OrientedPoint3d rotate(double rotateZ);

    /**
     * Return a new point with an in-place rotation by the provided rotateX, rotateY, and rotateZ. The resulting rotations will
     * be normalized between -&pi; and &pi;.
     * @param rotateX the rotation around the x-axis
     * @param rotateY the rotation around the y-axis
     * @param rotateZ the rotation around the z-axis
     * @return a new point with the same coordinates and applied rotations
     * @throws ArithmeticException when any of the rotations is {@code NaN}
     */
    Oriented3d rotate(double rotateX, double rotateY, double rotateZ);

}
