package org.djutils.draw.point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Oriented3d;
import org.djutils.exceptions.Throw;

/**
 * A OrientedPoint3d is an immutable point with an x, y, and z coordinate, stored with double precision plus a 3d orientation.
 * The orientation is specified by the rotations around the x, y, and z-axis. A number of constructors and methods are provided
 * for cases where only the rotation around the z-axis is of importance. Orientation in 3D is stored as three double values
 * dirX,dirY,dirZ. This class does <b>not</b> prescribe a particular order in which these rotations are to be applied. (Applying
 * rotations is <b>not</b> commutative, so this <i>is</i> important.)
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class OrientedPoint3d extends Point3d implements Oriented3d<OrientedPoint3d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The direction as rotation around the x-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirX;

    /** The direction as rotation around the y-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirY;

    /** The direction as rotation around the z-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and direction 0,0,0.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @throws IllegalArgumentException when x, y, or z is NaN
     */
    public OrientedPoint3d(final double x, final double y, final double z) throws IllegalArgumentException
    {
        this(x, y, z, 0.0, 0.0, 0.0);
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and orientation dirX,dirY,dirZ.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param dirX double; the direction as rotation around the x-axis with the point as the center
     * @param dirY double; the direction as rotation around the y-axis with the point as the center
     * @param dirZ double; the direction as rotation around the z-axis with the point as the center
     * @throws IllegalArgumentException when x, y, z, dirX, dirY, or dirZ is NaN
     */
    public OrientedPoint3d(final double x, final double y, final double z, final double dirX, final double dirY,
            final double dirZ) throws IllegalArgumentException
    {
        super(x, y, z);
        Throw.when(Double.isNaN(dirX) || Double.isNaN(dirY) || Double.isNaN(dirZ), IllegalArgumentException.class,
                "dirX, dirY and dirZ must be numbers (not NaN)");
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and direction 0,0,0.
     * @param xyz double[]; the x, y and z coordinates
     * @throws NullPointerException when xyx is null
     * @throws IllegalArgumentException when the length of the xyx array is not 3, or contains a NaN value
     */
    public OrientedPoint3d(final double[] xyz) throws NullPointerException, IllegalArgumentException
    {
        super(xyz);
        this.dirX = 0.0;
        this.dirY = 0.0;
        this.dirZ = 0.0;
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and orientation dirX,dirY,dirZ.
     * @param xyz double[]; the x, y and z coordinates
     * @param dirX double; the direction as rotation around the x-axis with the point as the center
     * @param dirY double; the direction as rotation around the y-axis with the point as the center
     * @param dirZ double; the direction as rotation around the z-axis with the point as the center
     * @throws NullPointerException when xyx is null
     * @throws IllegalArgumentException when the length of the xyz array is not 3, or contains a NaN value, or dirX, dirY, or
     *             dirZ is NaN
     */
    public OrientedPoint3d(final double[] xyz, final double dirX, final double dirY, final double dirZ)
            throws NullPointerException, IllegalArgumentException
    {
        super(xyz);
        Throw.when(Double.isNaN(dirX) || Double.isNaN(dirY) || Double.isNaN(dirZ), IllegalArgumentException.class,
                "dirX, dirY and dirZ must be numbers (not NaN)");
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Create a new OrientedPoint3d from another point and specified orientation dirX,dirY,dirZ.
     * @param point Point3d; the point from which this OrientedPoint3d will be instantiated
     * @param dirX double; the direction as rotation around the x-axis with the point as the center
     * @param dirY double; the direction as rotation around the y-axis with the point as the center
     * @param dirZ double; the direction as rotation around the z-axis with the point as the center
     * @throws IllegalArgumentException when dirX, dirY, or dirZ is NaN
     */
    public OrientedPoint3d(final Point3d point, final double dirX, final double dirY, final double dirZ)
            throws IllegalArgumentException
    {
        this(point.x, point.y, point.z, dirX, dirY, dirZ);
    }

    /**
     * Verify that a double array is not null, has three elements.
     * @param orientation double[]; the array to check
     * @return double; the first element of the argument
     * @throws NullPointerException when <code>orientation</code> is null
     * @throws IllegalArgumentException when the length of the <code>orientation</code> array is not 3
     */
    private static double checkOrientationVector(final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.when(orientation.length != 3, IllegalArgumentException.class, "length of orientation array must be 3");
        return orientation[0];
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and orientation specified using a double array of three
     * elements (containing dirX,dirY,dirZ in that order).
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param orientation double[]; the three orientation values as rotations around the x,y,z-axes in a double array containing
     *            dirX,dirY,dirZ in that order
     * @throws NullPointerException when <code>rotation</code> is null
     * @throws IllegalArgumentException when the length of the <code>direction</code> array is not 3
     */
    public OrientedPoint3d(final double x, final double y, final double z, final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        this(x, y, z, checkOrientationVector(orientation), orientation[1], orientation[2]);
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates packed in a double array and orientation specified using a
     * double array of three elements (containing dirX,dirY,dirZ in that order).
     * @param xyz double[]; the x, y and z coordinates in that order
     * @param orientation double[]; the three orientation values as rotations around the x,y,z-axes in a double array containing
     *            dirX,dirY,dirZ in that order
     * @throws NullPointerException when xyx or direction is null
     * @throws IllegalArgumentException when the length of the xyx array or the length of the orientation array is not 3
     */
    public OrientedPoint3d(final double[] xyz, final double[] orientation) throws NullPointerException, IllegalArgumentException
    {
        this(xyz, checkOrientationVector(orientation), orientation[1], orientation[2]);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not contain NaN");
        return new OrientedPoint3d(getX() + dx, getY() + dy, getZ(), getDirX(), getDirY(), getDirZ());
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d translate(final double dx, final double dy, final double dz) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "Translation may not contain NaN");
        return new OrientedPoint3d(this.x + dx, this.y + dy, this.z + dz, this.dirX, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d scale(final double factor) throws IllegalArgumentException
    {
        return new OrientedPoint3d(this.x * factor, this.y * factor, this.z * factor, this.dirX, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d neg()
    {
        return new OrientedPoint3d(-this.x, -this.y, -this.z, AngleUtil.normalizeAroundZero(this.dirX + Math.PI),
                AngleUtil.normalizeAroundZero(this.dirY + Math.PI), AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d abs()
    {
        return new OrientedPoint3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), this.dirX, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return new OrientedPoint3d(this.x / length, this.y / length, this.z / length, this.dirX, this.dirY, this.dirZ);
    }

    /**
     * Interpolate towards another OrientedPoint3d with a fraction. It is allowed for fraction to be less than zero or larger
     * than 1. In that case the interpolation turns into an extrapolation. DirX, dirY and dirZ are interpolated/extrapolated
     * using the interpolateShortest method.
     * @param otherPoint OrientedPoint3d; the other point
     * @param fraction double; the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is
     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0;
     *            <code>this</code> Point is returned; if <code>fraction</code> is 1, the <code>otherPoint</code> is returned
     * @return OrientedPoint3d; a new OrientedPoint3d at the requested fraction
     * @throws NullPointerException when otherPoint is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    public OrientedPoint3d interpolate(final OrientedPoint3d otherPoint, final double fraction)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(otherPoint, "otherPoint cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        if (0.0 == fraction)
        {
            return this;
        }
        if (1.0 == fraction)
        {
            return otherPoint;
        }
        return new OrientedPoint3d((1.0 - fraction) * getX() + fraction * otherPoint.x,
                (1.0 - fraction) * getY() + fraction * otherPoint.y, (1.0 - fraction) * getZ() + fraction * otherPoint.z,
                AngleUtil.interpolateShortest(getDirX(), otherPoint.getDirX(), fraction),
                AngleUtil.interpolateShortest(getDirY(), otherPoint.getDirY(), fraction),
                AngleUtil.interpolateShortest(getDirZ(), otherPoint.getDirZ(), fraction));
    }

    /**
     * Return a new OrientedPoint3d with an in-place rotation around the z-axis by the provided rotateZ. The resulting rotation
     * will be normalized between -&pi; and &pi;.
     * @param rotateZ double; the rotation around the z-axis
     * @return OrientedPoint3d; a new point with the same coordinates, dirX and dirY and modified dirZ
     * @throws IllegalArgumentException when rotateZ is NaN
     */
    public OrientedPoint3d rotate(final double rotateZ) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateZ), IllegalArgumentException.class, "rotateZ must be a number (not NaN)");
        return new OrientedPoint3d(getX(), getY(), getZ(), getDirX(), getDirY(),
                AngleUtil.normalizeAroundZero(getDirZ() + rotateZ));
    }

    /**
     * Return a new OrientedPoint3d point with an in-place rotation by the provided rotateX, rotateY, and rotateZ. The resulting
     * rotations will be normalized between -&pi; and &pi;.
     * @param rotateX double; the rotation around the x-axis
     * @param rotateY double; the rotation around the y-axis
     * @param rotateZ double; the rotation around the z-axis
     * @return OrientedPoint3d; a new point with the same coordinates and applied rotations
     * @throws IllegalArgumentException when any of the rotations is NaN
     */
    public OrientedPoint3d rotate(final double rotateX, final double rotateY, final double rotateZ)
            throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateX) || Double.isNaN(rotateY) || Double.isNaN(rotateZ), IllegalArgumentException.class,
                "rotateX, rotateY and rotateZ must be a numbers (not NaN)");
        return new OrientedPoint3d(getX(), getY(), getZ(), AngleUtil.normalizeAroundZero(getDirX() + rotateX),
                AngleUtil.normalizeAroundZero(getDirY() + rotateY), AngleUtil.normalizeAroundZero(getDirZ() + rotateZ));
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
    public Iterator<OrientedPoint3d> getPoints()
    {
        return Arrays.stream(new OrientedPoint3d[] {this}).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2$s, rotX=%2$s, rotY=%2$s, rotZ=%2$s]",
                doNotIncludeClassName ? "" : "OrientedPoint3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.dirX, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final OrientedPoint3d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(this.x - other.x) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.z - other.z) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirX - other.dirX)) > epsilonRotation)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirY - other.dirY)) > epsilonRotation)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirZ - other.dirZ)) > epsilonRotation)
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
        OrientedPoint3d other = (OrientedPoint3d) obj;
        if (Double.doubleToLongBits(this.dirX) != Double.doubleToLongBits(other.dirX))
            return false;
        if (Double.doubleToLongBits(this.dirY) != Double.doubleToLongBits(other.dirY))
            return false;
        if (Double.doubleToLongBits(this.dirZ) != Double.doubleToLongBits(other.dirZ))
            return false;
        return true;
    }

}
