package org.djutils.draw.d0;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * Point is the interface for the Point2d and Point3d implementations, standardizing as many of the methods as possible.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Point extends Serializable
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
     * Return the z-coordinate.
     * @return double; the z-coordinate
     */
    double getZ();

    /**
     * Return a new point with a translation by the provided delta-x and delta-y.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return Point; a new point with the translated coordinates
     */
    Point translate(double dx, double dy);

    /**
     * Return a new 3d point with a translation by the provided delta-x, delta-y, and delta-z.
     * @param dx double; the translation in the x-direction
     * @param dy double; the translation in the y-direction
     * @param dz double; the translation in the z-direction
     * @return Point3d; a new point with the translated coordinates
     */
    default Point3d translate(double dx, double dy, double dz)
    {
        return new Point3d(getX() + dx, getY() + dy, getZ() + dz);
    }

    /**
     * Return a new point with the coordinates of this point scaled by the provided factor.
     * @param factor double; the scale factor
     * @return Point; a new point with the coordinates of this point scaled by the provided factor
     */
    Point scale(double factor);

    /**
     * Return a new point with negated coordinate values.
     * @return Point; a new point with negated coordinate values
     */
    Point neg();

    /**
     * Return a new point with absolute coordinate values.
     * @return Point; a new point with absolute coordinate values
     */
    Point abs();

    /**
     * Return the point with a length of 1 to the origin.
     * @return Point; the normalized point
     * @throws DrawRuntimeException when point is the origin, and no length can be established for scaling
     */
    Point normalize() throws DrawRuntimeException;

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
     * @param point Point3d; the other point
     * @return double; the direction of the projection of the point in the x-y plane to another point, in radians
     * @throws NullPointerException when point is null
     */
    default double horizontalDirection(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return Math.atan2(point.getY() - getY(), point.getX() - getX());
    }

    /**
     * Interpolate towards another point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation.
     * @param point Point; the other point
     * @param fraction the factor for interpolation towards the other point. When fraction is between 0 and 1, it is an
     *            interpolation, otherwise an extrapolation
     * @return Point; the point that is "fraction" away on the line between this point and the other point
     * @throws NullPointerException when point is null
     */
    Point interpolate(Point point, double fraction);

    /**
     * Interpolate between two points with a fraction. It is allowed for fraction to be less than zero or larger than 1. In that
     * case the interpolation turns into an extrapolation.
     * @param p1 Point; the first point
     * @param p2 Point; the second point
     * @param fraction the factor for interpolation between p1 and p2. When fraction is between 0 and 1, it is an interpolation,
     *            otherwise an extrapolation
     * @return Point; the point that is "fraction" away on the line between p1 and p2
     * @throws NullPointerException when p1 or p2 is null
     */
    static Point interpolate(final Point p1, final Point p2, final double fraction)
    {
        Throw.whenNull(p1, "p1 cannot be null");
        return p1.interpolate(p2, fraction);
    }

    /**
     * Return the squared distance between this point and the provided point.
     * @param point Point; the other point
     * @return double; the squared distance between this point and the other point
     * @throws NullPointerException when point is null
     */
    double distanceSquared(Point point);

    /**
     * Return the squared distance between two points.
     * @param p1 Point; the first point
     * @param p2 Point; the second point
     * @return double; the squared distance between the two points
     * @throws NullPointerException when p1 or p2 is null
     */
    static double distanceSquared(final Point p1, final Point p2)
    {
        Throw.whenNull(p1, "p1 cannot be null");
        return p1.distanceSquared(p2);
    }

    /**
     * Return the Euclidean distance between this point and the provided point.
     * @param point Point; the other point
     * @return double; the Euclidean distance between this point and the other point
     * @throws NullPointerException when point is null
     */
    default double distance(final Point point)
    {
        return Math.sqrt(distanceSquared(this, point));
    }

    /**
     * Return the Euclidean distance between two points.
     * @param p1 Point; the first point
     * @param p2 Point; the second point
     * @return double; the Euclidean distance between the two points
     * @throws NullPointerException when p1 or p2 is null
     */
    static double distance(final Point p1, final Point p2)
    {
        return Math.sqrt(distanceSquared(p1, p2));
    }

    /**
     * Return the squared distance between the coordinates of this point and the provided point, ignoring the z-coordinate.
     * @param point Point; the other point
     * @return double; the squared distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    default double horizontalDistanceSquared(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        double dx = getX() - point.getX();
        double dy = getY() - point.getY();
        return dx * dx + dy * dy;
    }

    /**
     * Return the squared distance between two points, ignoring the z-coordinate.
     * @param p1 Point; the first point
     * @param p2 Point; the second point
     * @return double; the squared distance between the two points, ignoring the z-coordinate
     * @throws NullPointerException when p1 or p2 is null
     */
    static double horizontalDistanceSquared(final Point p1, final Point p2)
    {
        Throw.whenNull(p1, "p1 cannot be null");
        return p1.horizontalDistanceSquared(p2);
    }

    /**
     * Return the Euclidean distance between this point and the provided point, ignoring the z-coordinate.
     * @param point Point3d; the other point
     * @return double; the Euclidean distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    default double horizontalDistance(final Point point)
    {
        return Math.sqrt(horizontalDistanceSquared(point));
    }

    /**
     * Return the Euclidean distance between two points, ignoring the z-coordinate.
     * @param p1 Point; the first point
     * @param p2 Point; the second point
     * @return double; the Euclidean distance between the two points, ignoring the z-coordinate
     * @throws NullPointerException when p1 or p2 is null
     */
    static double horizontalDistance(final Point p1, final Point p2)
    {
        return Math.sqrt(horizontalDistanceSquared(p1, p2));
    }

    /**
     * Return the coordinates as a double array.
     * @return double[]; the coordinates as a double array
     */
    double[] toArray();

    /**
     * Return the coordinates as an AWT Point2D.Double object, ignoring a z-position of present.
     * @return Point2D; the coordinates as an AWT Point2D.Double object
     */
    default Point2D toPoint2D()
    {
        return new Point2D.Double(getX(), getY());
    }

    /**
     * A comparison with another point that returns true of each of the coordinates is less than epsilon apart.
     * @param point Point; the point to compare with
     * @param epsilon double; the upper bound of difference for one of the coordinates
     * @return boolean; true if both x, y and zare less than epsilon apart, otherwise false
     * @throws NullPointerException when point is null
     */
    default boolean epsilonEquals(final Point point, final double epsilon)
    {
        Throw.whenNull(point, "point cannot be null");
        double diff = getX() - point.getX();
        if (Double.isNaN(diff))
            return false;
        if ((diff < 0 ? -diff : diff) > epsilon)
            return false;
        diff = getY() - point.getY();
        if (Double.isNaN(diff))
            return false;
        if ((diff < 0 ? -diff : diff) > epsilon)
            return false;
        diff = getZ() - point.getZ();
        if (Double.isNaN(diff))
            return false;
        if ((diff < 0 ? -diff : diff) > epsilon)
            return false;
        return true;
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
