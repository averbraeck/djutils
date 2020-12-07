package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;
import org.djutils.exceptions.Throw;

/**
 * Point is the interface for the Point2d and Point3d implementations, standardizing as many of the methods as possible.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type (2d or 3d)
 */
public interface Point<P extends Point<P>> extends Drawable<P>, Serializable
{
    /**
     * Return the x-coordinate.
     * @return double; the x-coordinate
     */
    double getX();

    /**
     * Return the y-coordinate.
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
     * Return the direction of the point in radians with respect to the origin, ignoring the z-coordinate.
     * @return double; the direction of the projection of the point in the x-y plane with respect to the origin, in radians
     */
    default double horizontalDirection()
    {
        return Math.atan2(getY(), getX());
    }

    /**
     * Return the direction to another point, in radians, ignoring the z-coordinate.
     * @param point P; the other point
     * @return double; the direction of the projection of the point in the x-y plane to another point, in radians
     * @throws NullPointerException when <code>point</code> is null
     */
    default double horizontalDirection(final P point) throws NullPointerException
    {
        Throw.whenNull(point, "point cannot be null");
        return Math.atan2(point.getY() - getY(), point.getX() - getX());
    }

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
    P interpolate(P point, double fraction) throws NullPointerException, IllegalArgumentException;

    /**
     * Return the squared distance between the coordinates of this point and the provided point, ignoring the z-coordinate.
     * @param point Point2d; the other point
     * @return double; the squared distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    default double horizontalDistanceSquared(final Point<?> point)
    {
        Throw.whenNull(point, "point cannot be null");
        double dx = getX() - point.getX();
        double dy = getY() - point.getY();
        return dx * dx + dy * dy;
    }

    /**
     * Return the Euclidean distance between this point and the provided point, ignoring the z-coordinate.
     * @param point Point2d; the other point
     * @return double; the Euclidean distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    default double horizontalDistance(final Point<?> point)
    {
        return Math.sqrt(horizontalDistanceSquared(point));
    }

    /**
     * Return the coordinates as an AWT Point2D.Double object, ignoring a z-position of present.
     * @return Point2D; the coordinates as an AWT Point2D.Double object
     */
    default Point2D toPoint2D()
    {
        return new Point2D.Double(getX(), getY());
    }

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
