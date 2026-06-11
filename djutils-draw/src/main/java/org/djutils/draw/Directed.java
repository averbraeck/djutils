package org.djutils.draw;

/**
 * Directed is the interface to specify a Direction in the XY-plane (a vector without a length, pointing in a direction). This
 * is <b>not</b> the direction of the object as seen from the origin (0,0,0).
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public interface Directed
{
    /**
     * Retrieve the angle from the positive x-axis. Positive rotates towards the positive y-axis (and beyond).
     * @return the angle from the positive x-axis
     */
    double getDirZ();

    /**
     * Return a new point with a translation by the provided dR.
     * @param dR the translation along the direction
     * @return a new point with translated coordinates
     * @throws IllegalArgumentException when {@code dR} is {@code NaN}
     */
    Directed translate(double dR);
    
    /**
     * Return a new point with an in-place rotation around the z-axis by the provided rotateZ. The resulting rotation will be
     * normalized between -&pi; and &pi;.
     * @param rotateZ the rotation around the z-axis
     * @return a new point with the same coordinates and modified {@code dirZ}
     * @throws ArithmeticException when {@code rotateZ} is {@code NaN}
     */
    Directed rotate(double rotateZ);

}
