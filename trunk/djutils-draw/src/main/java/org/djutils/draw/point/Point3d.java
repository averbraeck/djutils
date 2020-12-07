package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
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
public class Point3d extends AbstractPoint3d implements Drawable3d, Point<Point3d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create a new Point3d with x, y, and z coordinates, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @throws IllegalArgumentException when a coordinate is NaN
     */
    public Point3d(final double x, final double y, final double z) throws IllegalArgumentException
    {
        super(x, y, z);
    }

    /**
     * Create a new Point3d with x, y, and z coordinates, stored with double precision.
     * @param xyz double[3]; the x, y and z coordinates
     * @throws NullPointerException when xyz is null
     * @throws IllegalArgumentException when the length of the xyz array is not 3, or any coordinate is NaN
     */
    public Point3d(final double[] xyz) throws NullPointerException, IllegalArgumentException
    {
        super(xyz);
    }

    /**
     * Create a new Point3d taking x and y from an AWT Point2D object and z from a double.
     * @param point Point2D; the AWT Point2D
     * @param z double; the value for the z coordinate
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate, or z is NaN
     */
    public Point3d(final Point2D point, final double z) throws NullPointerException, IllegalArgumentException
    {
        super(point, z);
    }

    /**
     * Create a new Point3d taking x and y from a Point2d and taking z from a provided double.
     * @param point2d Point2d; the existing Point2d
     * @param z double; the value for the z coordinate
     * @throws NullPointerException when point2d is null
     * @throws IllegalArgumentException when the point2d parameter has a NaN coordinate
     */
    public Point3d(final Point2d point2d, final double z) throws NullPointerException, IllegalArgumentException
    {
        super(Throw.whenNull(point2d, "point2d cannot be null").getX(), point2d.getY(), z);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point3d> getPoints()
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
    public Point3d translate(double dx, double dy) throws IllegalArgumentException
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
        return new Point3d((1.0 - fraction) * getX() + fraction * point.getX(),
                (1.0 - fraction) * getY() + fraction * point.getY(), (1.0 - fraction) * getZ() + fraction * point.getZ());

    }

    /**
     * A comparison with another point that returns true of each of the coordinates is less than epsilon apart.
     * @param other Point; the point to compare with
     * @param epsilon double; the upper bound of difference for one of the coordinates
     * @return boolean; true if both x, y and z (if a Point3d) are less than epsilon apart, otherwise false
     * @throws NullPointerException when point is null
     */
    public boolean epsilonEquals(final Point3d other, final double epsilon)
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(getX() - other.getX()) > epsilon)
        {
            return false;
        }
        if (Math.abs(getY() - other.getY()) > epsilon)
        {
            return false;
        }
        if (Math.abs(getZ() - other.getZ()) > epsilon)
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

    /** {@inheritDoc} */
    @Override
    public Point3d getLocation()
    {
        return this;
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

}
