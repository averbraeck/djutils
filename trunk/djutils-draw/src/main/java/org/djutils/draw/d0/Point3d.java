package org.djutils.draw.d0;

import java.awt.geom.Point2D;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * A Point3d is an immutable point with an x, y, and z coordinate, stored with double precision. It differs from many Point
 * implementations by being immutable.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point3d implements Point
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** the x-coordinate. */
    private final double x;

    /** the y-coordinate. */
    private final double y;

    /** the z-coordinate. */
    private final double z;

    /**
     * Create an immutable point with x, y, and z coordinates, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     */
    public Point3d(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create an immutable point with x, y, and z coordinates, stored with double precision.
     * @param xyz double[3]; the x, y and z coordinates
     * @throws NullPointerException when xyx is null
     * @throws IllegalArgumentException when the dimension of xyx is not 3
     */
    public Point3d(final double[] xyz) throws IllegalArgumentException
    {
        Throw.whenNull(xyz, "xyz-point cannot be null");
        Throw.when(xyz.length != 3, IllegalArgumentException.class, "Dimension of xyz-point should be 3");
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }

    /**
     * Create an immutable point with x, y coordinates, stored with double precision, where the z-coordinate is 0.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @return Point3d; the new immutable point with z=0
     */
    public static Point3d instantiateXY(final double x, final double y)
    {
        return new Point3d(x, y, 0.0);
    }

    /**
     * Create an immutable point with x, y, and z coordinates, from an AWT Point2D, where z will be 0.
     * @param point Point2D; an AWT Point2D
     * @return Point3d; the new immutable point with z=0
     * @throws NullPointerException when point is null
     */
    public static Point3d instantiateXY(final Point2D point)
    {
        Throw.whenNull(point, "point cannot be null");
        return new Point3d(point.getX(), point.getY(), 0.0);
    }

    /**
     * Create an immutable point with x, y, and z coordinates, from a Point2d, where z will be 0.
     * @param point Point2d; a 2-dimensional point
     * @return Point3d; the new immutable point with z=0
     * @throws NullPointerException when point is null
     */
    public static Point3d instantiateXY(final Point2d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return new Point3d(point.getX(), point.getY(), 0.0);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d translate(final double dx, final double dy)
    {
        return new Point3d(this.x + dx, this.y + dy, this.z);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d scale(final double factor)
    {
        return new Point3d(this.x * factor, this.y * factor, this.z * factor);
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
        return new Point3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    /** {@inheritDoc} */
    @Override
    public Point3d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d interpolate(final Point point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        return new Point3d((1.0 - fraction) * this.x + fraction * point.getX(),
                (1.0 - fraction) * this.y + fraction * point.getY(), (1.0 - fraction) * this.z + fraction * point.getZ());

    }

    /** {@inheritDoc} */
    @Override
    public double distanceSquared(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        double dx = this.x - point.getX();
        double dy = this.y - point.getY();
        double dz = this.z - point.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    /** {@inheritDoc} */
    @Override
    public double[] toArray()
    {
        return new double[] {this.x, this.y, this.z};
    }

    /** {@inheritDoc} */
    @Override
    public double getX()
    {
        return this.x;
    }

    /** {@inheritDoc} */
    @Override
    public double getY()
    {
        return this.y;
    }

    /** {@inheritDoc} */
    @Override
    public double getZ()
    {
        return this.z;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return String.format("(%f,%f,%f)", this.x, this.y, this.z);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("(%%.%1$df,%%.%1$df,%%.%1$df)", digits);
        return String.format(format, this.x, this.y, this.z);
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
    @Override
    @SuppressWarnings("checkstyle:needbraces")
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
