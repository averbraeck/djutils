package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.djutils.exceptions.Throw;

/**
 * AbstractPoint2d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class AbstractPoint2d implements Serializable
{
    /** */
    private static final long serialVersionUID = 20201201L;

    /** The x-coordinate. */
    private final double x;

    /** The y-coordinate. */
    private final double y;

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public AbstractPoint2d(final double x, final double y) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param xy double[2]; the x and y coordinate
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2, or a coordinate is NaN
     */
    public AbstractPoint2d(final double[] xy) throws NullPointerException, IllegalArgumentException
    {
        this(checkLengthIsTwo(Throw.whenNull(xy, "xy-point cannot be null"))[0], xy[1]);
    }

    /**
     * Create an immutable point with just two values, x and y, stored with double precision from an AWT Point2D
     * @param point Point2D; an AWT Point2D
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate
     */
    public AbstractPoint2d(final Point2D point) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(point.getX()) || Double.isNaN(point.getY()), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Throw an IllegalArgumentException if the length of the provided array is not two.
     * @param xy double[]; the provided array
     * @return double[]; the provided array
     * @throws IllegalArgumentException when length of xy is not two
     */
    private static double[] checkLengthIsTwo(final double[] xy) throws IllegalArgumentException
    {
        Throw.when(xy.length != 2, IllegalArgumentException.class, "Length of xy-array must be 2");
        return xy;
    }

    /**
     * Return the x-coordinate.
     * @return double; the x-coordinate
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * Return the y-coordinate.
     * @return double; the y-coordinate
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * Return the squared distance between this point and the provided point.
     * @param otherPoint AbstractPoint2d; the other point
     * @return double; the squared distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    public double distanceSquared(final AbstractPoint2d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        double dx = getX() - otherPoint.getX();
        double dy = getY() - otherPoint.getY();
        return dx * dx + dy * dy;
    }

    /**
     * Return the Euclidean distance between this point and the provided point.
     * @param otherPoint AbstractPoint2d; the other point
     * @return double; the Euclidean distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    public double distance(final AbstractPoint2d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        return Math.sqrt(distanceSquared(otherPoint));
    }

    /**
     * Retrieve the number of points that make up the object.
     * @return int; the number of points that make up the object
     */
    public int size()
    {
        return 1;
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
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractPoint2d other = (AbstractPoint2d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

}
