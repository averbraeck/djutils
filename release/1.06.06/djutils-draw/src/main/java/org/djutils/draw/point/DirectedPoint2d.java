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
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint2d extends Point2d implements Directed2d
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
    public DirectedPoint2d(final double x, final double y) throws IllegalArgumentException
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
    public DirectedPoint2d(final double x, final double y, final double dirZ) throws IllegalArgumentException
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
    public DirectedPoint2d(final double[] xy, final double dirZ) throws IllegalArgumentException
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
    public DirectedPoint2d(final Point2D point, final double dirZ) throws IllegalArgumentException
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
        super(point.x, point.y);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new DirectedPoint2d(getX() + dx, getY() + dy, getDirZ());
    }

    /** {@inheritDoc} */
    @Override
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

    /**
     * Interpolate towards another Point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation. DirZ is interpolated using the interpolateShortest method.
     * @param otherPoint DirectedPoint2d; the other point
     * @param fraction the factor for interpolation towards the other point. When <code>fraction</code> is between 0 and 1, it
     *            is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0; <code>this</code> Point is
     *            returned; if <code>fraction</code> is 1, the other <code>point</code> is returned
     * @return DirectedPoint2d; a new DirectedPoint2d at the requested fraction
     * @throws NullPointerException when otherPoint is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    public DirectedPoint2d interpolate(final DirectedPoint2d otherPoint, final double fraction)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new DirectedPoint2d((1.0 - fraction) * getX() + fraction * otherPoint.x,
                (1.0 - fraction) * getY() + fraction * otherPoint.y,
                AngleUtil.interpolateShortest(getDirZ(), otherPoint.getDirZ(), fraction));
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
    public Iterator<? extends DirectedPoint2d> getPoints()
    {
        return Arrays.stream(new DirectedPoint2d[] {this}).iterator();
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
        if (Math.abs(getX() - other.x) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(getY() - other.y) > epsilonCoordinate)
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
