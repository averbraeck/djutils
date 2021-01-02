package org.djutils.draw.point;

import java.io.Serializable;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;
import org.djutils.draw.Space;

/**
 * Point is the interface for the Point2d and Point3d implementations, standardizing as many of the methods as possible.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type
 * @param <S> The space type (2d or 3d)
 */
public interface Point<P extends Point<P, S>, S extends Space> extends Drawable<P, S>, Serializable
{
    /**
     * Return the x-coordinate. When the point is not in Cartesian space, a calculation to Cartesian space has to be made.
     * @return double; the x-coordinate
     */
    double getX();

    /**
     * Return the y-coordinate. When the point is not in Cartesian space, a calculation to Cartesian space has to be made.
     * @return double; the y-coordinate
     */
    double getY();

    /**
     * Return a new Point with the coordinates of this point scaled by the provided factor.
     * @param factor double; the scale factor
     * @return Point; a new point with the coordinates of this point scaled by the provided factor
     * @throws IllegalArgumentException when factor is NaN
     */
    P scale(double factor) throws IllegalArgumentException;

    /**
     * Return a new Point with negated coordinate values.
     * @return Point; a new point with negated coordinate values
     */
    P neg();

    /**
     * Return a new Point with absolute coordinate values.
     * @return Point; a new point with absolute coordinate values
     */
    P abs();

    /**
     * Return a new Point with a distance of 1 to the origin.
     * @return Point; the normalized point
     * @throws DrawRuntimeException when point is the origin, and no length can be established for scaling
     */
    P normalize() throws DrawRuntimeException;

    /**
     * Return the distance to another point.
     * @param otherPoint P the other point
     * @return double; the distance (2d or 3d as applicable) to the other point
     */
    double distance(P otherPoint);

    /**
     * Return the squared distance between this point and the provided point.
     * @param otherPoint P; the other point
     * @return double; the squared distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    double distanceSquared(P otherPoint) throws NullPointerException;

    /**
     * Interpolate towards another Point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation.
     * @param point P; the other point
     * @param fraction the factor for interpolation towards the other point. When <code>fraction</code> is between 0 and 1, it
     *            is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0; <code>this</code> Point is
     *            returned; if <code>fraction</code> is 1, the other <code>point</code> is returned
     * @return P; the point that is <code>fraction</code> away on the line between this point and the other point
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    P interpolate(P point, double fraction);

    /**
     * A comparison with another point that returns true of each of the coordinates is less than epsilon apart.
     * @param other P; the point to compare with
     * @param epsilon double; the upper bound of difference for one of the coordinates
     * @return boolean; true if both x, y and z (if a Point3d) are less than epsilon apart, otherwise false
     * @throws NullPointerException when point is null
     */
    boolean epsilonEquals(P other, double epsilon);

    /**
     * Return a string representation of the point with a certain number of fraction digits for the coordinates.
     * @param fractionDigits int; the number of fraction digits. Should be 0 or a positive number
     * @return String; a formatted string with a certain number of fraction digits for the coordinates
     */
    String toString(int fractionDigits);

    /**
     * Return a string representation of the point.
     * @return String; a formatted string of the point
     */
    @Override
    String toString();

}
