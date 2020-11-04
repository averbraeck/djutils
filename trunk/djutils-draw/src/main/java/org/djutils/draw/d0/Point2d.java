package org.djutils.draw.d0;

import java.awt.geom.Point2D;

import org.djutils.draw.DrawRuntimeException;
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
public class Point2d implements Point
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The x-coordinate. */
    private final double x;

    /** The y-coordinate. */
    private final double y;

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     */
    public Point2d(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param xy double[2]; the x and y coordinate
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2
     */
    public Point2d(final double[] xy) throws IllegalArgumentException
    {
        Throw.whenNull(xy, "xy-point cannot be null");
        Throw.when(xy.length != 2, IllegalArgumentException.class, "Dimension of xy-point should be 2");
        this.x = xy[0];
        this.y = xy[1];
    }

    /**
     * Create an immutable point with just two values, x and y, stored with double precision from an AWT Point2D
     * @param point Point2D; an AWT Point2D
     * @throws NullPointerException when point is null
     */
    public Point2d(final Point2D point)
    {
        Throw.whenNull(point, "point cannot be null");
        this.x = point.getX();
        this.y = point.getY();
    }

    /** {@inheritDoc} */
    @Override
    public Point2d translate(final double dx, final double dy)
    {
        return new Point2d(this.x + dx, this.y + dy);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d scale(final double factor)
    {
        return new Point2d(this.x * factor, this.y * factor);
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
        return new Point2d(Math.abs(this.x), Math.abs(this.y));
    }

    /** {@inheritDoc} */
    @Override
    public Point2d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d interpolate(final Point point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        return new Point2d((1.0 - fraction) * this.x + fraction * point.getX(),
                (1.0 - fraction) * this.y + fraction * point.getY());
    }

    /** {@inheritDoc} */
    @Override
    public double distanceSquared(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        double dx = this.x - point.getX();
        double dy = this.y - point.getY();
        return dx * dx + dy * dy;
    }

    /** {@inheritDoc} */
    @Override
    public double[] toArray()
    {
        return new double[] {this.x, this.y};
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
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("(%%.%1$df,%%.%1$df)", digits);
        return String.format(format, this.x, this.y);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("(%f,%f)", this.x, this.y);
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
        Point2d other = (Point2d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

}
