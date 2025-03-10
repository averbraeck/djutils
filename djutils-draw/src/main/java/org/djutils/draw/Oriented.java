package org.djutils.draw;

/**
 * Oriented is an interface to indicate an object has a direction.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <O> The Oriented type (2d or 3d)
 */
public interface Oriented<O extends Oriented<O>>
{
    /**
     * Return a new Oriented with negated coordinate values. Adds 180 degrees (pi radians) to the rotation(s).
     * @return a new Oriented with negated coordinate values and a rotation in the opposite direction
     */
    O neg();

    /**
     * Return the rotation around the z-axis in radians.
     * @return the rotation around the z-axis in radians
     */
    double getDirZ();

    /**
     * Compare this Oriented with another Oriented with specified tolerances in the coordinates and the angles.
     * @param other the Oriented to compare with
     * @param epsilonCoordinate the upper bound of difference for one of the coordinates; use Double.POSITIVE_INFINITY
     *            if you do not want to check the coordinates
     * @param epsilonDirection the upper bound of difference for the direction(s); use Double.POSITIVE_INFINITY if you
     *            do not want to check the angles
     * @return <code>true</code> if <code>x</code>, <code>y</code>, and <code>z</code> are less than
     *         <code>epsilonCoordinate</code> apart, and <code>rotX</code>, <code>rotY</code> and <code>rotZ</code> are less
     *         than <code>epsilonRotation</code> apart, otherwise <code>false</code>
     * @throws NullPointerException when <code>other</code> is <code>null</code>
     * @throws ArithmeticException when <code>epsilonCoordinate</code> or <code>epsilonRotation</code> is <code>NaN</code>
     * @throws IllegalArgumentException <code>epsilonCoordinate</code> or <code>epsilonRotation</code> is negative
     */
    boolean epsilonEquals(O other, double epsilonCoordinate, double epsilonDirection);

}
