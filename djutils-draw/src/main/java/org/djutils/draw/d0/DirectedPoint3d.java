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

    /** The direction as rotation around the x-axis. */
    private final double dirX;

    /** The direction as rotation around the y-axis. */
    private final double dirY;

    /** The direction as rotation around the z-axis. */
    private final double dirZ;

    /**
     * Create an immutable directed point with x, y, and z coordinates, stored with double precision, and direction 0,0,0.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     */
    public DirectedPoint3d(final double x, final double y, final double z)
    {
        super(x, y, z);
        this.dirX = 0.0;
        this.dirY = 0.0;
        this.dirZ = 0.0;
    }

    /**
     * Create an immutable directed point with x, y, and z coordinates, stored with double precision, and direction 0,0,0.
     * @param xyz double[3]; the x, y and z coordinates
     * @throws NullPointerException when xyx is null
     * @throws IllegalArgumentException when the dimension of xyx is not 3
     */
    public DirectedPoint3d(final double[] xyz) throws IllegalArgumentException
    {
        super(xyz);
        this.dirX = 0.0;
        this.dirY = 0.0;
        this.dirZ = 0.0;
    }

    /**
     * Create an immutable directed point with x, y, and z coordinates, stored with double precision and direction.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param dirX double; the direction as rotation around the x-axis with the point as the center
     * @param dirY double; the direction as rotation around the y-axis with the point as the center
     * @param dirZ double; the direction as rotation around the z-axis with the point as the center
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double dirX, final double dirY,
            final double dirZ)
    {
        super(x, y, z);
        Throw.when(Double.isNaN(dirX) || Double.isNaN(dirY) || Double.isNaN(dirZ), IllegalArgumentException.class,
                "Rotation must be a number (not NaN)");
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Create an immutable directed point from another point, stored with double precision and specified direction.
     * @param point Point; the point from which this DirectedPoint3d will be instantiated
     * @param dirX double; the direction as rotation around the x-axis with the point as the center
     * @param dirY double; the direction as rotation around the y-axis with the point as the center
     * @param dirZ double; the direction as rotation around the z-axis with the point as the center
     */
    public DirectedPoint3d(final Point point, final double dirX, final double dirY, final double dirZ)
    {
        super(point.getX(), point.getY(), point.getZ());
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Create an immutable point with x, y, and z coordinates, stored with double precision, and direction.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param direction double[3]; the direction as rotations around the x,y,z-axes with the point as the center
     * @throws NullPointerException when <code>rotation</code> is null
     * @throws IllegalArgumentException when the dimension of <code>rotation</code> is not 3
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double[] direction)
            throws IllegalArgumentException
    {
        super(x, y, z);
        Throw.whenNull(direction, "rotation cannot be null");
        Throw.when(direction.length != 3, IllegalArgumentException.class, "Dimension of rotation should be 3");
        this.dirX = direction[0];
        this.dirY = direction[1];
        this.dirZ = direction[2];
    }

    /**
     * Create an immutable point with x, y, and z coordinates, stored with double precision, and direction.
     * @param xyz double[3]; the x, y and z coordinates
     * @param direction double[3]; the rotation around the x,y,z-axis with the point as the center
     * @throws NullPointerException when xyx or direction is null
     * @throws IllegalArgumentException when the dimension of xyx or direction is not 3
     */
    public DirectedPoint3d(final double[] xyz, final double[] direction) throws IllegalArgumentException
    {
        super(xyz);
        Throw.whenNull(direction, "rotation cannot be null");
        Throw.when(direction.length != 3, IllegalArgumentException.class, "Dimension of rotation should be 3");
        this.dirX = direction[0];
        this.dirY = direction[1];
        this.dirZ = direction[2];
    }

    /**
     * Create an immutable directed point with x, and y coordinates, stored with double precision, where the z-coordinate is 0,
     * and the direction is 0,0,0.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @return DirectedPoint3d; the new DirectedPoint3d in he x-y plane with z=0 and no rotation
     */
    public static DirectedPoint3d instantiateXY(final double x, final double y)
    {
        return new DirectedPoint3d(x, y, 0.0);
    }

    /**
     * Create an immutable directed point with x, y coordinates, from an AWT Point2D, where z will be 0, and direction 0,0,0.
     * @param point Point2D; an AWT Point2D
     * @return DirectedPoint3d; the new DirectedPoint3d in he x-y plane with z=0 and direction 0,0,0
     */
    public static DirectedPoint3d instantiateXY(final Point2D point)
    {
        return new DirectedPoint3d(point.getX(), point.getY(), 0.0);
    }

    /**
     * Create an immutable directed point with x, y coordinates, from a Point2d, where z will be 0, and direction 0,0,0.
     * @param point Point2d; a 2-dimensional point
     * @return DirectedPoint3d; the new DirectedPoint3d in he x-y plane with z=0 and direction 0,0,0
     */
    public static DirectedPoint3d instantiateXY(final Point2d point)
    {
        return new DirectedPoint3d(point.getX(), point.getY(), 0.0);
    }

    /**
     * Return a new DirectedPoint3d with a translation by the provided delta. The new DirectedPoint3D gets the same direction as
     * this DirectedPoint2d.
     * @param dx double; the x-translation
     * @param dy double; the y-translation
     * @param dz double; the z-translation
     * @return DirectedPoint3d; a new point with the translated coordinates
     */
    @Override
    public DirectedPoint3d translate(final double dx, final double dy, final double dz)
    {
        return new DirectedPoint3d(this.getX() + dx, this.getY() + dy, this.getZ() + dz, this.dirX, this.dirY, this.dirZ);
    }

    /**
     * Return a new DirectedPoint3d with the coordinates of this point scaled by the provided factor. The new DirectedPoint3d
     * gets the same direction as this DirectedPoint3d.
     * @param factor double; the scale factor
     * @return DirectedPoint3d; a new point with the coordinates of this point scaled by the provided factor
     */
    @Override
    public DirectedPoint3d scale(final double factor)
    {
        return new DirectedPoint3d(this.getX() * factor, this.getY() * factor, this.getZ() * factor, this.dirX, this.dirY,
                this.dirZ);
    }

    /**
     * Return a new DirectedPoint3d with negated coordinate values. The new DirectedPoint3d has the direction around x, y, and z
     * axis altered by pi.
     * @return DirectedPoint3d; a new point with negated coordinate values and inverted direction
     */
    @Override
    public DirectedPoint3d neg()
    {
        return new DirectedPoint3d(-this.getX(), -this.getY(), -this.getZ(), AngleUtil.normalizeAroundZero(this.dirX + Math.PI),
                AngleUtil.normalizeAroundZero(this.dirY + Math.PI), AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    /**
     * Return a new DirectedPoint3d with absolute coordinate values. The direction of the new DirectedPoint3d is the same as the
     * direction of this DirectedPoint3d.
     * @return DirectedPoint3d; a new point with absolute coordinate values
     */
    @Override
    public DirectedPoint3d abs()
    {
        return new DirectedPoint3d(Math.abs(this.getX()), Math.abs(this.getY()), Math.abs(this.getZ()), this.dirX, this.dirY,
                this.dirZ);
    }

    /**
     * Return a new DirectedPoint3d with a distance of 1 from the origin. The direction of the new DirectedPoint3d is the same
     * as the direction of this DirectedPoint3d.
     * @return Point3d; the normalized point with unchanged direction
     * @throws DrawRuntimeException when point is (0,0,0)
     */
    @Override
    public DirectedPoint3d normalize() throws DrawRuntimeException
    {
        return new DirectedPoint3d(super.normalize(), this.dirX, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d interpolate(final DirectedPoint point, final double fraction)
    {
        return new DirectedPoint3d(super.interpolate(point, fraction),
                AngleUtil.interpolateClockwise(this.dirX, point.getDirX(), fraction),
                AngleUtil.interpolateClockwise(this.dirY, point.getDirY(), fraction),
                AngleUtil.interpolateClockwise(this.dirZ, point.getDirZ(), fraction));
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d rotate(final double deltaRotZ)
    {
        return new DirectedPoint3d(getX(), getY(), getZ(), getDirX(), getDirY(),
                AngleUtil.normalizeAroundZero(getDirZ() + deltaRotZ));
    }

    /** {@inheritDoc} */
    @Override
    public double getDirX()
    {
        return this.dirX;
    }

    /** {@inheritDoc} */
    @Override
    public double getDirY()
    {
        return this.dirY;
    }

    /** {@inheritDoc} */
    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("[(%f,%f,%f), rot=(%f,%f,%f)]", this.getX(), this.getY(), this.getZ(), this.dirX, this.dirY,
                this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("[(%%.%1$df,%%.%1$df,%%.%1$df), rot=(%%.%1$df,%%.%1$df,%%.%1$df)]", digits);
        return String.format(format, this.getX(), this.getY(), this.getZ(), this.dirX, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.dirX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.dirY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        DirectedPoint3d other = (DirectedPoint3d) obj;
        if (Double.doubleToLongBits(this.dirX) != Double.doubleToLongBits(other.dirX))
            return false;
        if (Double.doubleToLongBits(this.dirY) != Double.doubleToLongBits(other.dirY))
            return false;
        if (Double.doubleToLongBits(this.dirZ) != Double.doubleToLongBits(other.dirZ))
            return false;
        return true;
    }

}
