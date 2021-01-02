package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.Space3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.exceptions.Throw;

/**
 * A Point3d is an immutable point with an x, y, and z coordinate, stored with double precision. It differs from many Point
 * implementations by being immutable.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point3d implements Drawable3d, Point<Point3d, Space3d>
{
    /** */
    private static final long serialVersionUID = 20201201L;

    /** The x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double x;

    /** The y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double y;

    /** The z-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double z;

    /**
     * Create a new Point with just an x, y and z coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public Point3d(final double x, final double y, final double z)
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new Point with just an x, y and z coordinate, stored with double precision.
     * @param xyz double[3]; the x, y and z coordinate
     * @throws NullPointerException when xyz is null
     * @throws IllegalArgumentException when the dimension of xyz is not 3, or a coordinate is NaN
     */
    public Point3d(final double[] xyz) throws NullPointerException, IllegalArgumentException
    {
        this(checkLengthIsThree(Throw.whenNull(xyz, "xyz-point cannot be null"))[0], xyz[1], xyz[2]);
    }

    /**
     * Create an immutable point with just three values, x, y and z, stored with double precision from a Point2d and z.
     * @param point Point2d; a Point2d
     * @param z double; the z coordinate
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when z is NaN
     */
    public Point3d(final Point2d point, final double z) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(z), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = point.x;
        this.y = point.y;
        this.z = z;
    }

    /**
     * Create an immutable point with just three values, x, y and z, stored with double precision from an AWT Point2D and z.
     * @param point Point2D; an AWT Point2D
     * @param z double; the z coordinate
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate, or z is NaN
     */
    public Point3d(final Point2D point, final double z) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(point.getX()) || Double.isNaN(point.getY()), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        Throw.when(Double.isNaN(z), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = point.getX();
        this.y = point.getY();
        this.z = z;
    }

    /**
     * Throw an IllegalArgumentException if the length of the provided array is not three.
     * @param xyz double[]; the provided array
     * @return double[]; the provided array
     * @throws IllegalArgumentException when length of xyz is not three
     */
    private static double[] checkLengthIsThree(final double[] xyz) throws IllegalArgumentException
    {
        Throw.when(xyz.length != 3, IllegalArgumentException.class, "Length of xy-array must be 2");
        return xyz;
    }

    /** {@inheritDoc} */
    @Override
    public final double getX()
    {
        return this.x;
    }

    /** {@inheritDoc} */
    @Override
    public final double getY()
    {
        return this.y;
    }

    /**
     * Return the z-coordinate.
     * @return double; the z-coordinate
     */
    public final double getZ()
    {
        return this.z;
    }

    /** {@inheritDoc} */
    @Override
    public double distanceSquared(final Point3d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        double dx = getX() - otherPoint.x;
        double dy = getY() - otherPoint.y;
        double dz = getZ() - otherPoint.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /** {@inheritDoc} */
    @Override
    public double distance(final Point3d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        return Math.sqrt(distanceSquared(otherPoint));
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point3d> getPoints()
    {
        return Arrays.stream(new Point3d[] { this }).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Point2d project() throws DrawRuntimeException
    {
        return new Point2d(getX(), getY());
    }

    /**
     * Return a new Point with a translation by the provided dx and dy.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return Point3D; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, or dy is NaN
     */
    public Point3d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class,
                "Translation must be number (not NaN)");
        return new Point3d(getX() + dx, getY() + dy, getZ());
    }

    /**
     * Return a new Point3d with a translation by the provided dx, dy and dz.
     * @param dx double; the x translation
     * @param dy double; the y translation
     * @param dz double; the z translation
     * @return Point3d; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, dy, or dz is NaN
     */
    public Point3d translate(final double dx, final double dy, final double dz) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "dx, dy and dz must be numbers (not NaN)");
        return new Point3d(getX() + dx, getY() + dy, getZ() + dz);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d scale(final double factor) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new Point3d(getX() * factor, getY() * factor, getZ() * factor);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d neg()
    {
        return scale(-1.0);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d abs()
    {
        return new Point3d(Math.abs(getX()), Math.abs(getY()), Math.abs(getZ()));
    }

    /** {@inheritDoc} */
    @Override
    public Point3d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d interpolate(final Point3d point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new Point3d((1.0 - fraction) * getX() + fraction * point.x, (1.0 - fraction) * getY() + fraction * point.y,
                (1.0 - fraction) * getZ() + fraction * point.z);

    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final Point3d other, final double epsilon)
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(getX() - other.x) > epsilon)
        {
            return false;
        }
        if (Math.abs(getY() - other.y) > epsilon)
        {
            return false;
        }
        if (Math.abs(getZ() - other.z) > epsilon)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return new Bounds3d(this);
    }

    /**
     * Return the direction of the point in radians with respect to the origin, ignoring the z-coordinate.
     * @return double; the direction of the projection of the point in the x-y plane with respect to the origin, in radians
     */
    final double horizontalDirection()
    {
        return Math.atan2(getY(), getX());
    }

    /**
     * Return the direction to another point, in radians, ignoring the z-coordinate.
     * @param point Point&lt;?&gt;; the other point
     * @return double; the direction of the projection of the point in the x-y plane to another point, in radians
     * @throws NullPointerException when <code>point</code> is null
     */
    final double horizontalDirection(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point cannot be null");
        return Math.atan2(point.y - getY(), point.x - getX());
    }

    /**
     * Return the squared distance between the coordinates of this point and the provided point, ignoring the z-coordinate.
     * @param point Point2d; the other point
     * @return double; the squared distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    final double horizontalDistanceSquared(final Point3d point)
    {
        Throw.whenNull(point, "point cannot be null");
        double dx = getX() - point.x;
        double dy = getY() - point.y;
        return dx * dx + dy * dy;
    }

    /**
     * Return the Euclidean distance between this point and the provided point, ignoring the z-coordinate.
     * @param point Point2d; the other point
     * @return double; the Euclidean distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    final double horizontalDistance(final Point3d point)
    {
        return Math.sqrt(horizontalDistanceSquared(point));
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return String.format("(%f,%f,%f)", getX(), getY(), getZ());
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("(%%.%1$df,%%.%1$df,%%.%1$df)", digits);
        return String.format(format, getX(), getY(), getZ());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:needbraces")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point3d other = (Point3d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

}
