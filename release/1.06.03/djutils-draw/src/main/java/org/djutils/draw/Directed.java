package org.djutils.draw;

/**
 * Directed.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <D> The Directed type (2d or 3d)
 */
public interface Directed<D extends Directed<D>>
{
    /**
     * Return a new DirectedPoint2d with negated coordinate values. Add 180 degrees (pi radians) to the rotation(s).
     * @return D; a new D with negated coordinate values and a rotation in the opposite direction
     */
    D neg();

//    /**
//     * Return a new DirectedPoint2d with absolute coordinate values. Leave the rotation unchanged.
//     * @return DirectedPoint2d; a new point with absolute coordinate values and an unchanged rotation
//     */
//    D abs();
//
//    /**
//     * Return the DirectedPoint2d with a length of 1 to the origin. Leave the rotation unchanged.
//     * @return DirectedPoint2d; the normalized point and an unchanged rotation
//     */
//    D normalize();
//
//    /**
//     * Interpolate the coordinates and rotation between this point and the given point. It is allowed for fraction to be less
//     * than zero or larger than 1. In that case the interpolation becomes extrapolation. The rotations along the z axis (and; if
//     * applicable, x, and y-axes) are also interpolated or extrapolated in a clockwise fashion and normalized between -&pi; and
//     * &pi;.
//     * @param point D; the other point
//     * @param fraction double; the factor for interpolation between <code>this</code> D and <code>point</code>. When fraction is
//     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. When <code>fraction</code> is 0 this
//     *            method returns <code>this</code> ; when <code>fraction</code> is 1 this method returns the <code>point</code>
//     *            parameter
//     * @return D; the point that is "fraction" away on the line between this D and the given D
//     * @throws NullPointerException when point is null
//     * @throws IllegalArgumentException when fraction is NaN
//     */
//    D interpolate(final P point, final double fraction);
//
}
