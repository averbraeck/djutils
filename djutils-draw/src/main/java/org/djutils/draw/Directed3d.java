package org.djutils.draw;

/**
 * Interface for objects that have a direction in 3d-space i.c. dirY (similar to tilt; measured as an angle from the positive
 * z-direction) and dirZ (similar to pan; measured as an angle from the positive x-direction).
 * <p>
 * Copyright (c) 2023-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * There are two naming conventions for phi and theta. Djutils draw uses neither to stay clear of this confusion. The angle from
 * the positive z-axis to the projection of the direction on the x-y-plane is named {@code dirY}. The angle from the positive
 * x-axis to the projection of the direction in the x-y-plane is named {@code dirZ}.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public interface Directed3d extends Directed
{
    /**
     * Retrieve the angle from the positive z axis to the direction. Normally these are values between [0:&pi;]. Angles &le;
     * &pi;/2 indicate above the x-y-plane; positive slope, angles &gt; &pi;/2 indicate angles below this plane; negative slope.
     * @return dirY
     */
    double getDirY();

    /**
     * Retrieve the Direction3d.
     * @return the direction
     */
    default Direction3d getDir()
    {
        return new Direction3d(getDirY(), getDirZ());
    }
    
    @Override
    Directed3d translate(double dR);

    @Override
    Directed3d rotate(double rotateZ);

    /**
     * Return a new point with an in-place rotation by the provided rotateY, and rotateZ. The resulting rotations will be
     * normalized between -&pi; and &pi;.
     * @param rotateY the rotation around the y-axis
     * @param rotateZ the rotation around the z-axis
     * @return a new point with the same coordinates and applied rotations
     * @throws ArithmeticException when {@code rotateY}, or {@code rotateZ} is {@code NaN}
     */
    Directed3d rotate(double rotateY, double rotateZ);

}
