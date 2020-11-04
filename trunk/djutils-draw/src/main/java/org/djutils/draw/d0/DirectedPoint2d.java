package org.djutils.draw.d0;

import java.awt.geom.Point2D;

import org.djutils.base.AngleUtil;
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
public class DirectedPoint2d extends Point2d implements DirectedPoint
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The counter-clockwise rotation around the point in radians. */
    private final double rotZ;

    /**
     * Construct an immutable directed point with an x and y coordinate, and a direction, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param rotZ double; the counter-clockwise rotation around the point in radians
     */
    public DirectedPoint2d(double x, double y, final double rotZ)
    {
        super(x, y);
        this.rotZ = rotZ;
    }

    /**
     * Construct an immutable directed point with an x and y coordinate, and a direction, stored with double precision.
     * @param xy double[2]; the x and y coordinate
     * @param rotZ double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2
     */
    public DirectedPoint2d(double[] xy, final double rotZ) throws IllegalArgumentException
    {
        super(xy);
        this.rotZ = rotZ;
    }

    /**
     * Construct an immutable directed point from an AWT Point2D, and a direction, stored with double precision.
     * @param point Point2D; an AWT Point2D
     * @param rotZ double; the counter-clockwise rotation around the point in radians
     */
    public DirectedPoint2d(Point2D point, final double rotZ)
    {
        super(point);
        this.rotZ = rotZ;
    }

    /**
     * Construct an immutable directed point with a direction from another Point, stored with double precision.
     * @param point Point; a point with or without rotation
     * @param rotZ double; the counter-clockwise rotation around the point in radians
     */
    public DirectedPoint2d(final Point point, final double rotZ)
    {
        super(point.getX(), point.getY());
        this.rotZ = rotZ;
    }

    /**
     * Return a new DirectedPoint2d with a translation by the provided delta-x and delta-y from <code>this</code>
     * DirectedPoint2D. The rotation of the new DirectedPoint2d is equal to the rotation of this DirectedPoint2d.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return DirectedPoint2d; a new point with the translated coordinates and an unchanged rotation
     */
    @Override
    public DirectedPoint2d translate(final double dx, final double dy)
    {
        return new DirectedPoint2d(super.translate(dx, dy), this.rotZ);
    }

    /**
     * Return a new directedPoint2d with the coordinates of this point scaled by the provided factor. The rotation of the new
     * DirectedPoint2d is equal to the rotation of this DirectedPoint2d.
     * @param factor double; the scale factor
     * @return DirectedPoint2d; a new point with the coordinates of this point scaled by the provided factor and an unchanged
     *         rotation
     */
    @Override
    public DirectedPoint2d scale(final double factor)
    {
        return new DirectedPoint2d(super.scale(factor), this.rotZ);
    }

    /**
     * Return a new DirectedPoint2d with negated coordinate values. Add 180 degrees (pi radians) to the rotation.
     * @return DirectedPoint2d; a new DirectedPoint2d with negated coordinate values and a rotation in the opposite direction
     */
    @Override
    public DirectedPoint2d neg()
    {
        return new DirectedPoint2d(super.neg(), this.rotZ + Math.PI);
    }

    /**
     * Return a new DirectedPoint2d with absolute coordinate values. Leave the rotation unchanged.
     * @return DirectedPoint2d; a new point with absolute coordinate values and an unchanged rotation
     */
    @Override
    public DirectedPoint2d abs()
    {
        return new DirectedPoint2d(super.abs(), this.rotZ);
    }

    /**
     * Return the DirectedPoint2d with a length of 1 to the origin. Leave the rotation unchanged.
     * @return DirectedPoint2d; the normalized point and an unchanged rotation
     */
    @Override
    public DirectedPoint2d normalize()
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d translate(final double dx, final double dy, final double dz)
    {
        return new DirectedPoint3d(super.translate(dx, dy, dz), getRotX(), getRotY(), getRotZ());
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d interpolate(final DirectedPoint point, final double fraction)
    {
        return new DirectedPoint2d(super.interpolate(point, fraction),
                AngleUtil.interpolateClockwise(this.rotZ, point.getRotZ(), fraction));
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d rotate(final double deltaRotZ)
    {
        return new DirectedPoint2d(getX(), getY(), AngleUtil.normalizeAroundZero(getRotZ() + deltaRotZ));
    }

    /** {@inheritDoc} */
    @Override
    public double getRotX()
    {
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public double getRotY()
    {
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public double getRotZ()
    {
        return this.rotZ;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("[(%f,%f), rot=%f]", getX(), getY(), this.rotZ);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("[(%%.%1$df,%%.%1$df), rot=%%.%1$df]", digits);
        return String.format(format, getX(), getY(), this.rotZ);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.rotZ);
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
        if (Double.doubleToLongBits(this.rotZ) != Double.doubleToLongBits(other.rotZ))
            return false;
        return true;
    }

}
