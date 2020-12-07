package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;

import org.djutils.base.AngleUtil;
import org.djutils.draw.Directed2d;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * The DirectedPoint2d is a point in a 2-dimensional space with a direction vector, which is specified in terms of its
 * counter-clockwise rotation around the point in radians.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint2d extends AbstractPoint2d implements Directed2d, Point<DirectedPoint2d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The counter-clockwise rotation around the point in radians. */
    private final double dirZ;

    /**
     * Construct an immutable directed point with an x and y coordinate, and a direction, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @throws IllegalArgumentException when any coordinate is NaN
     */
    public DirectedPoint2d(double x, double y) throws IllegalArgumentException
    {
        super(x, y);
        this.dirZ = 0;
    }

    /**
     * Construct an immutable directed point with an x and y coordinate, and a direction, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate or dirZ is NaN
     */
    public DirectedPoint2d(double x, double y, final double dirZ) throws IllegalArgumentException
    {
        super(x, y);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct an immutable directed point with an x and y coordinate, and a direction, stored with double precision.
     * @param xy double[2]; the x and y coordinate
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2 or any value in xy is NaN or rotZ is NaN
     */
    public DirectedPoint2d(double[] xy, final double dirZ) throws IllegalArgumentException
    {
        super(xy);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct an immutable directed point from an AWT Point2D, and a direction, stored with double precision.
     * @param point Point2D; an AWT Point2D
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate in point is NaN, or rotZ is NaN
     */
    public DirectedPoint2d(Point2D point, final double dirZ) throws IllegalArgumentException
    {
        super(point);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct an immutable directed point with a direction from another Point, stored with double precision.
     * @param point Point; a point with or without rotation
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when rotZ is NaN
     */
    public DirectedPoint2d(final Point2d point, final double dirZ) throws IllegalArgumentException
    {
        super(point.getX(), point.getY());
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Return a new DirectedPoint2d with a translation by the provided delta-x and delta-y from <code>this</code>
     * DirectedPoint2D. The rotation of the new DirectedPoint2d is equal to the rotation of this DirectedPoint2d.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return DirectedPoint2d; a new point with the translated coordinates and an unchanged rotation
     * @throws IllegalArgumentException when dx or dy is NaN
     */
    public DirectedPoint2d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new DirectedPoint2d(getX() + dx, getY() + dy, getDirZ());
    }

    /**
     * Return a new DirectedPoint3d with a translation by the provided delta-x, delta-y and z from <code>this</code>
     * DirectedPoint2D and the specified z value. The direction of the new DirectedPoint3d is equal to the rotation of this
     * DirectedPoint2d; i.e. dirX and dirY are set to 0.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @param z double; the vertical position
     * @return DirectedPoint2d; a new point with the translated coordinates and an unchanged rotation
     * @throws IllegalArgumentException when dx, dy or z is NaN
     */
    public DirectedPoint3d translate(final double dx, final double dy, final double z) throws IllegalArgumentException
    {
        return new DirectedPoint3d(getX() + dx, getY() + dy, z, 0, 0, getDirZ());
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d scale(final double factor) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new DirectedPoint2d(getX() * factor, getY() * factor, getDirZ());
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d neg()
    {
        return new DirectedPoint2d(-getX(), -getY(), getDirZ() + Math.PI);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d abs()
    {
        return new DirectedPoint2d(Math.abs(getX()), Math.abs(getY()), getDirZ());
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d normalize()
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d interpolate(DirectedPoint2d point, double fraction)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new DirectedPoint2d((1.0 - fraction) * getX() + fraction * point.getX(),
                (1.0 - fraction) * getY() + fraction * point.getY(),
                AngleUtil.interpolateShortest(getDirZ(), point.getDirZ(), fraction));
    }

    /**
     * Return a new DirectedPoint with an in-place rotation around the z-axis by the provided delta. The resulting rotation will
     * be normalized between -&pi; and &pi;.
     * @param rotateZ double; the rotation around the z-axis
     * @return DirectedPoint; a new point with the same coordinates and applied rotation
     * @throws IllegalArgumentException when deltaRotZ is NaN
     */
    public DirectedPoint2d rotate(final double rotateZ) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateZ), IllegalArgumentException.class, "deltaDirZ must be a number (not NaN)");
        return new DirectedPoint2d(getX(), getY(), AngleUtil.normalizeAroundZero(getDirZ() + rotateZ));
    }

    /** {@inheritDoc} */
    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<DirectedPoint2d> getPoints()
    {
        return Arrays.stream(new DirectedPoint2d[] { this }).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d getLocation()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("[(%f,%f), rot=%f]", getX(), getY(), getDirZ());
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("[(%%.%1$df,%%.%1$df), rot=%%.%1$df]", digits);
        return String.format(format, getX(), getY(), getDirZ());
    }

    /**
     * Compare this DirectedPoint2d with another DirectedPoint2d and return true of each of the coordinates is less than
     * epsilonCoordinate apart, and the direction components are (normalized) less that epsilonRotation apart.
     * @param other DirectedPoint2d; the point to compare with
     * @param epsilonCoordinate double; the upper bound of difference for one of the coordinates
     * @param epsilonRotation double; the upper bound of difference for one of the rotations
     * @return boolean; true if x, y, and z are less than epsilonCoordinate apart, and rotX, rotY and rotZ are less than
     *         epsilonRotation apart, otherwise false
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException epsilonCoordinate or epsilonRotation is NaN
     */
    public boolean epsilonEquals(final DirectedPoint2d other, final double epsilonCoordinate, final double epsilonRotation)
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(getX() - other.getX()) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(getY() - other.getY()) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(getDirZ() - other.getDirZ())) > epsilonRotation)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.dirZ);
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
        if (!super.equals(obj))
            return false;
        DirectedPoint2d other = (DirectedPoint2d) obj;
        if (Double.doubleToLongBits(this.dirZ) != Double.doubleToLongBits(other.dirZ))
            return false;
        return true;
    }

}
