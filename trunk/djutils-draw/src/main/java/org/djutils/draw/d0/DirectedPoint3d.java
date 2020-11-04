package org.djutils.draw.d0;

import java.awt.geom.Point2D;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * A DirectedPoint3d is an immutable directed point with an x, y, and z coordinate, stored with double precision. It differs
 * from many Point implementations by being immutable. The direction is a vector from the point, where its direction is
 * specified by the rotation around the x, y, and z-axis. A number of constructors and methods are provided for cases where only
 * the rotation around the z-axis is of importance.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint3d extends Point3d implements DirectedPoint
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** the rotation around the x-axis with the point as the center. */
    private final double rotX;

    /** the rotation around the y-axis with the point as the center. */
    private final double rotY;

    /** the rotation around the z-axis with the point as the center. */
    private final double rotZ;

    /**
     * Create an immutable directed point with x, y, and z coordinates, stored with double precision, and no rotation.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     */
    public DirectedPoint3d(final double x, final double y, final double z)
    {
        super(x, y, z);
        this.rotX = 0.0;
        this.rotY = 0.0;
        this.rotZ = 0.0;
    }

    /**
     * Create an immutable directed point with x, y, and z coordinates, stored with double precision, and no rotation.
     * @param xyz double[3]; the x, y and z coordinates
     * @throws NullPointerException when xyx is null
     * @throws IllegalArgumentException when the dimension of xyx is not 3
     */
    public DirectedPoint3d(final double[] xyz) throws IllegalArgumentException
    {
        super(xyz);
        this.rotX = 0.0;
        this.rotY = 0.0;
        this.rotZ = 0.0;
    }

    /**
     * Create an immutable directed point with x, y, and z coordinates, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param rotX double; the rotation around the x-axis with the point as the center
     * @param rotY double; the rotation around the y-axis with the point as the center
     * @param rotZ double; the rotation around the z-axis with the point as the center
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double rotX, final double rotY,
            final double rotZ)
    {
        super(x, y, z);
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    /**
     * Create an immutable directed point from another point, stored with double precision.
     * @param point Point; the ppoint from which this DirectedPoint3d will be instantiated
     * @param rotX double; the rotation around the x-axis with the point as the center
     * @param rotY double; the rotation around the y-axis with the point as the center
     * @param rotZ double; the rotation around the z-axis with the point as the center
     */
    public DirectedPoint3d(final Point point, final double rotX, final double rotY, final double rotZ)
    {
        super(point.getX(), point.getY(), point.getZ());
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    /**
     * Create an immutable point with x, y, and z coordinates, stored with double precision, and 3D rotation.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param rotation double[3]; the rotation around the x,y,z-axes with the point as the center
     * @throws NullPointerException when rotation is null
     * @throws IllegalArgumentException when the dimension of delta is not 3
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double[] rotation)
            throws IllegalArgumentException
    {
        super(x, y, z);
        Throw.whenNull(rotation, "rotation cannot be null");
        Throw.when(rotation.length != 3, IllegalArgumentException.class, "Dimension of rotation should be 3");
        this.rotX = rotation[0];
        this.rotY = rotation[1];
        this.rotZ = rotation[2];
    }

    /**
     * Create an immutable point with x, y, and z coordinates, stored with double precision, and no rotation.
     * @param xyz double[3]; the x, y and z coordinates
     * @param rotation double[3]; the rotation around the x,y,z-axis with the point as the center
     * @throws NullPointerException when xyx or rotation is null
     * @throws IllegalArgumentException when the dimension of xyx or of rotation is not 3
     */
    public DirectedPoint3d(final double[] xyz, final double[] rotation) throws IllegalArgumentException
    {
        super(xyz);
        Throw.whenNull(rotation, "rotation cannot be null");
        Throw.when(rotation.length != 3, IllegalArgumentException.class, "Dimension of rotation should be 3");
        this.rotX = rotation[0];
        this.rotY = rotation[1];
        this.rotZ = rotation[2];
    }

    /**
     * Create an immutable directed point with x, and y coordinates, stored with double precision, where the z-coordinate is 0,
     * and there is no rotation.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @return DirectedPoint3d; the new DirectedPoint3d in he x-y plane with z=0 and no rotation
     */
    public static DirectedPoint3d instantiateXY(final double x, final double y)
    {
        return new DirectedPoint3d(x, y, 0.0);
    }

    /**
     * Create an immutable directed point with x, y coordinates, from an AWT Point2D, where z will be 0, and there is no
     * rotation.
     * @param point Point2D; an AWT Point2D
     * @return DirectedPoint3d; the new DirectedPoint3d in he x-y plane with z=0 and no rotation
     */
    public static DirectedPoint3d instantiateXY(final Point2D point)
    {
        return new DirectedPoint3d(point.getX(), point.getY(), 0.0);
    }

    /**
     * Create an immutable directed point with x, y coordinates, from a Point2d, where z will be 0, and there is no rotation.
     * @param point Point2d; a 2-dimensional point
     * @return DirectedPoint3d; the new DirectedPoint3d in he x-y plane with z=0 and no rotation
     */
    public static DirectedPoint3d instantiateXY(final Point2d point)
    {
        return new DirectedPoint3d(point.getX(), point.getY(), 0.0);
    }

    /**
     * Return a new point with a translation by the provided delta. Leave the rotation unchanged.
     * @param dx double; the x-translation
     * @param dy double; the y-translation
     * @param dz double; the z-translation
     * @return DirectedPoint3d; a new point with the translated coordinates
     */
    @Override
    public DirectedPoint3d translate(final double dx, final double dy, final double dz)
    {
        return new DirectedPoint3d(this.getX() + dx, this.getY() + dy, this.getZ() + dz, this.rotX, this.rotY, this.rotZ);
    }

    /**
     * Return a new point with the coordinates of this point scaled by the provided factor. Leave the rotation unchanged.
     * @param factor double; the scale factor
     * @return DirectedPoint3d; a new point with the coordinates of this point scaled by the provided factor
     */
    @Override
    public DirectedPoint3d scale(final double factor)
    {
        return new DirectedPoint3d(this.getX() * factor, this.getY() * factor, this.getZ() * factor, this.rotX, this.rotY,
                this.rotZ);
    }

    /**
     * Return a new point with negated coordinate values. Shift the rotations around x, y, and z axis with pi.
     * @return DirectedPoint3d; a new point with negated coordinate values
     */
    @Override
    public DirectedPoint3d neg()
    {
        return new DirectedPoint3d(-this.getX(), -this.getY(), -this.getZ(), AngleUtil.normalizeAroundZero(this.rotX + Math.PI),
                AngleUtil.normalizeAroundZero(this.rotY + Math.PI), AngleUtil.normalizeAroundZero(this.rotZ + Math.PI));
    }

    /**
     * Return a new point with absolute coordinate values. Leave the rotation unchanged.
     * @return DirectedPoint3d; a new point with absolute coordinate values
     */
    @Override
    public DirectedPoint3d abs()
    {
        return new DirectedPoint3d(Math.abs(this.getX()), Math.abs(this.getY()), Math.abs(this.getZ()), this.rotX, this.rotY,
                this.rotZ);
    }

    /**
     * Return the point with a length of 1 to the origin. Leave the rotation unchanged.
     * @return Point3d; the normalized point with unchanged rotation
     * @throws DrawRuntimeException when point is (0,0,0)
     */
    @Override
    public DirectedPoint3d normalize() throws DrawRuntimeException
    {
        return new DirectedPoint3d(super.normalize(), this.rotX, this.rotY, this.rotZ);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d interpolate(final DirectedPoint point, final double fraction)
    {
        return new DirectedPoint3d(super.interpolate(point, fraction),
                AngleUtil.interpolateClockwise(this.rotX, point.getRotX(), fraction),
                AngleUtil.interpolateClockwise(this.rotY, point.getRotY(), fraction),
                AngleUtil.interpolateClockwise(this.rotZ, point.getRotZ(), fraction));
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d rotate(final double deltaRotZ)
    {
        return new DirectedPoint3d(getX(), getY(), getZ(), getRotX(), getRotY(),
                AngleUtil.normalizeAroundZero(getRotZ() + deltaRotZ));
    }

    @Override
    public double getRotX()
    {
        return this.rotX;
    }

    @Override
    public double getRotY()
    {
        return this.rotY;
    }

    @Override
    public double getRotZ()
    {
        return this.rotZ;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("[(%f,%f,%f), rot=(%f,%f,%f)]", this.getX(), this.getY(), this.getZ(), this.rotX, this.rotY,
                this.rotZ);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("[(%%.%1$df,%%.%1$df,%%.%1$df), rot=(%%.%1$df,%%.%1$df,%%.%1$df)]", digits);
        return String.format(format, this.getX(), this.getY(), this.getZ(), this.rotX, this.rotY, this.rotZ);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.rotX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.rotY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        DirectedPoint3d other = (DirectedPoint3d) obj;
        if (Double.doubleToLongBits(this.rotX) != Double.doubleToLongBits(other.rotX))
            return false;
        if (Double.doubleToLongBits(this.rotY) != Double.doubleToLongBits(other.rotY))
            return false;
        if (Double.doubleToLongBits(this.rotZ) != Double.doubleToLongBits(other.rotZ))
            return false;
        return true;
    }

}
