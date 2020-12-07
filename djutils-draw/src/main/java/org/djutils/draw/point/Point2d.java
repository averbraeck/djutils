package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.exceptions.Throw;

/**
 * A Point2d is an immutable Point with an x and y coordinate, stored with double precision. It differs from many Point
 * implementations by being immutable.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point2d extends AbstractPoint2d implements Drawable2d, Point<Point2d>
{
    /** ... */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public Point2d(final double x, final double y)
    {
        super(x, y);
    }

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param xy double[2]; the x and y coordinate
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the length of the xy array is not 2, or a coordinate is NaN
     */
    public Point2d(final double[] xy) throws IllegalArgumentException
    {
        super(xy);
    }

    /**
     * Create an immutable point with just two values, x and y, stored with double precision from an AWT Point2D
     * @param point Point2D; an AWT Point2D
     * @throws NullPointerException when point is null
     */
    public Point2d(final Point2D point)
    {
        super(point);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point2d> getPoints()
    {
        return Arrays.stream(new Point2d[] { this }).iterator();
    }

    /**
     * Return a new Point with a translation by the provided dx and dy.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return P; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, or dy is NaN
     */
    public Point2d translate(final double dx, final double dy)
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new Point2d(getX() + dx, getY() + dy);
    }

    /**
     * Return a new Point3d with a translation by the provided delta-x, delta-y and deltaZ.
     * @param dx double; the x translation
     * @param dy double; the y translation
     * @param dz double; the z translation
     * @return Point2d; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, dy, or dz is NaN
     */
    public Point3d translate(final double dx, final double dy, final double dz)
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "translation may not be NaN");
        return new Point3d(getX() + dx, getY() + dy, dz);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d scale(final double factor)
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new Point2d(getX() * factor, getY() * factor);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d neg()
    {
        return scale(-1.0);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d abs()
    {
        return new Point2d(Math.abs(getX()), Math.abs(getY()));
    }

    /** {@inheritDoc} */
    @Override
    public Point2d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d interpolate(final Point2d point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new Point2d((1.0 - fraction) * getX() + fraction * point.getX(),
                (1.0 - fraction) * getY() + fraction * point.getY());
    }

    /**
     * A comparison with another point that returns true of each of the coordinates is less than epsilon apart.
     * @param other Point; the point to compare with
     * @param epsilon double; the upper bound of difference for one of the coordinates
     * @return boolean; true if both x, y and z (if a Point3d) are less than epsilon apart, otherwise false
     * @throws NullPointerException when point is null
     */
    public boolean epsilonEquals(final Point2d other, final double epsilon)
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
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return new Bounds2d(this);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getLocation()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Point2D toPoint2D()
    {
        return new Point2D.Double(getX(), getY());
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("(%%.%1$df,%%.%1$df)", digits);
        return String.format(format, getX(), getY());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("(%f,%f)", getX(), getY());
    }

}
