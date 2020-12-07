package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.djutils.exceptions.Throw;

/**
 * AbstractPoint3d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class AbstractPoint3d implements Serializable
{
    /** */
    private static final long serialVersionUID = 20201201L;

    /** The x-coordinate. */
    private final double x;

    /** The y-coordinate. */
    private final double y;

    /** The z-coordinate. */
    private final double z;

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public AbstractPoint3d(final double x, final double y, final double z)
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param xyz double[3]; the x, y and z coordinate
     * @throws NullPointerException when xyz is null
     * @throws IllegalArgumentException when the dimension of xyz is not 3, or a coordinate is NaN
     */
    public AbstractPoint3d(final double[] xyz) throws NullPointerException, IllegalArgumentException
    {
        this(checkLengthIsThree(Throw.whenNull(xyz, "xyz-point cannot be null"))[0], xyz[1], xyz[2]);
    }

    /**
     * Create an immutable point with just two values, x and y, stored with double precision from an AWT Point2D
     * @param point Point2D; an AWT Point2D
     * @param z double; the z coordinate
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate, or z is NaN
     */
    public AbstractPoint3d(final Point2D point, final double z) throws NullPointerException, IllegalArgumentException
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
     * Return the z-coordinate.
     * @return double; the y-coordinate
     */
    public double getZ()
    {
        return this.z;
    }

    /**
     * Return the squared distance between this point and the provided point.
     * @param otherPoint AbstractPoint3d; the other point
     * @return double; the squared distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    public double distanceSquared(final AbstractPoint3d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        double dx = getX() - otherPoint.getX();
        double dy = getY() - otherPoint.getY();
        double dz = getZ() - otherPoint.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Return the Euclidean distance between this point and the provided point.
     * @param otherPoint AbstractPoint3d; the other point
     * @return double; the Euclidean distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    public double distance(final AbstractPoint3d otherPoint) throws NullPointerException
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
        temp = Double.doubleToLongBits(this.z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractPoint3d other = (AbstractPoint3d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

}
