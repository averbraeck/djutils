package org.djutils.draw.point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Oriented3d;
import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * A OrientedPoint3d is a point with an x, y, and z coordinate, plus a 3d orientation. The orientation is specified by the
 * rotations around the x, y, and z-axis. A number of constructors and methods are provided for cases where only the rotation
 * around the z-axis is of importance. Orientation in 3D is stored as three double values dirX,dirY,dirZ. This class does
 * <b>not</b> prescribe a particular order in which these rotations are to be applied. (Applying rotations is <b>not</b>
 * commutative, so this <i>is</i> important.)
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class OrientedPoint3d extends DirectedPoint3d implements Oriented3d<OrientedPoint3d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The direction as rotation around the x-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirX;

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and direction 0,0,0.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @throws IllegalArgumentException when <code>x</code>, <code>y</code>, or <code>z</code> is <code>NaN</code>
     */
    public OrientedPoint3d(final double x, final double y, final double z)
    {
        this(x, y, z, 0.0, 0.0, 0.0);
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and orientation dirX,dirY,dirZ.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param dirX the direction as rotation around the x-axis with the point as the center
     * @param dirY the direction as rotation around the y-axis with the point as the center
     * @param dirZ the direction as rotation around the z-axis with the point as the center
     * @throws ArithmeticException when <code>x</code>, <code>y</code>, <code>z</code>, <code>dirX</code>, <code>dirY</code>, or
     *             <code>dirZ</code> is <code>NaN</code>
     */
    public OrientedPoint3d(final double x, final double y, final double z, final double dirX, final double dirY,
            final double dirZ)
    {
        super(x, y, z, dirY, dirZ);
        Throw.whenNaN(dirX, "dirX");
        this.dirX = dirX;
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and direction 0,0,0.
     * @param xyz the x, y and z coordinates
     * @throws NullPointerException when <code>xyz</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>xyx</code> array is not 3
     * @throws IllegalArgumentException when the <code>xyx</code> array contains a <code>NaN</code> value
     */
    public OrientedPoint3d(final double[] xyz)
    {
        super(xyz, 0, 0);
        this.dirX = 0.0;
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and orientation dirX,dirY,dirZ.
     * @param xyz the x, y and z coordinates
     * @param dirX the direction as rotation around the x-axis with the point as the center
     * @param dirY the direction as rotation around the y-axis with the point as the center
     * @param dirZ the direction as rotation around the z-axis with the point as the center
     * @throws NullPointerException when <code>xyx</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the xyz array is not 3
     * @throws ArithmeticException when <code>xyz</code> contains a <code>NaN</code> value, or <code>dirX</code>,
     *             <code>dirY</code>, or <code>dirZ</code> is <code>NaN</code>
     */
    public OrientedPoint3d(final double[] xyz, final double dirX, final double dirY, final double dirZ)
            throws NullPointerException, IllegalArgumentException
    {
        super(xyz, dirY, dirZ);
        Throw.whenNaN(dirX, "dirX");
        this.dirX = dirX;
    }

    /**
     * Create a new OrientedPoint3d from another point and specified orientation dirX,dirY,dirZ.
     * @param point the point from which this OrientedPoint3d will be instantiated
     * @param dirX the direction as rotation around the x-axis with the point as the center
     * @param dirY the direction as rotation around the y-axis with the point as the center
     * @param dirZ the direction as rotation around the z-axis with the point as the center
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when <code>dirX</code>, <code>dirY</code>, or <code>dirZ</code> is <code>NaN</code>
     */
    public OrientedPoint3d(final Point3d point, final double dirX, final double dirY, final double dirZ)
    {
        this(point.x, point.y, point.z, dirX, dirY, dirZ);
    }

    /**
     * Verify that a double array is not null, has three elements.
     * @param orientation the array to check
     * @return the first element of the argument
     * @throws NullPointerException when <code>orientation</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>orientation</code> array is not 3
     */
    private static double checkOrientationVector(final double[] orientation)
    {
        Throw.when(orientation.length != 3, IllegalArgumentException.class, "length of orientation array must be 3");
        return orientation[0];
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates and orientation specified using a double array of three
     * elements (containing dirX,dirY,dirZ in that order).
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param orientation the three orientation values as rotations around the x,y,z-axes in a double array containing
     *            dirX,dirY,dirZ in that order
     * @throws NullPointerException when <code>rotation</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>direction</code> array is not 3
     */
    public OrientedPoint3d(final double x, final double y, final double z, final double[] orientation)
    {
        this(x, y, z, checkOrientationVector(orientation), orientation[1], orientation[2]);
    }

    /**
     * Create a new OrientedPoint3d with x, y, and z coordinates packed in a double array and orientation specified using a
     * double array of three elements (containing dirX,dirY,dirZ in that order).
     * @param xyz the x, y and z coordinates in that order
     * @param orientation the three orientation values as rotations around the x,y,z-axes in a double array containing
     *            dirX,dirY,dirZ in that order
     * @throws NullPointerException when <code>xyx</code> or <code>direction</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>xyx</code> array or the length of the
     *             <code>orientation</code> array is not 3
     */
    public OrientedPoint3d(final double[] xyz, final double[] orientation)
    {
        this(xyz, checkOrientationVector(orientation), orientation[1], orientation[2]);
    }

    @Override
    public OrientedPoint3d translate(final double dX, final double dY)
    {
        Throw.whenNaN(dX, "dX");
        Throw.whenNaN(dY, "dY");
        return new OrientedPoint3d(this.x + dX, this.y + dY, this.z, this.dirX, this.dirY, this.dirZ);
    }

    @Override
    public OrientedPoint3d translate(final double dX, final double dY, final double dZ)
    {
        Throw.whenNaN(dX, "dX");
        Throw.whenNaN(dY, "dY");
        Throw.whenNaN(dZ, "dZ");
        return new OrientedPoint3d(this.x + dX, this.y + dY, this.z + dZ, this.dirX, this.dirY, this.dirZ);
    }

    @Override
    public OrientedPoint3d scale(final double factor)
    {
        return new OrientedPoint3d(this.x * factor, this.y * factor, this.z * factor, this.dirX, this.dirY, this.dirZ);
    }

    @Override
    public OrientedPoint3d neg()
    {
        return new OrientedPoint3d(-this.x, -this.y, -this.z, AngleUtil.normalizeAroundZero(this.dirX + Math.PI),
                AngleUtil.normalizeAroundZero(this.dirY + Math.PI), AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public OrientedPoint3d abs()
    {
        return new OrientedPoint3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), this.dirX, this.dirY, this.dirZ);
    }

    @Override
    public OrientedPoint3d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return new OrientedPoint3d(this.x / length, this.y / length, this.z / length, this.dirX, this.dirY, this.dirZ);
    }

    /**
     * Interpolate towards another OrientedPoint3d with a fraction. It is allowed for fraction to be less than zero or larger
     * than 1. In that case the interpolation turns into an extrapolation. DirX, dirY and dirZ are interpolated/extrapolated
     * using the interpolateShortest method.
     * @param otherPoint the other point
     * @param fraction the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is
     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0;
     *            <code>this</code> Point is returned; if <code>fraction</code> is 1, the <code>otherPoint</code> is returned
     * @return a new <code>OrientedPoint3d</code> at the requested <code>fraction</code>
     * @throws NullPointerException when <code>otherPoint</code> is <code>null</code>
     * @throws ArithmeticException when <code>fraction</code> is <code>NaN</code>
     */
    public OrientedPoint3d interpolate(final OrientedPoint3d otherPoint, final double fraction)
    {
        Throw.whenNull(otherPoint, "otherPoint");
        Throw.whenNaN(fraction, "fraction");
        if (0.0 == fraction)
        {
            return this;
        }
        if (1.0 == fraction)
        {
            return otherPoint;
        }
        return new OrientedPoint3d((1.0 - fraction) * this.x + fraction * otherPoint.x,
                (1.0 - fraction) * this.y + fraction * otherPoint.y, (1.0 - fraction) * this.z + fraction * otherPoint.z,
                AngleUtil.interpolateShortest(this.dirX, otherPoint.dirX, fraction),
                AngleUtil.interpolateShortest(this.dirY, otherPoint.dirY, fraction),
                AngleUtil.interpolateShortest(this.dirZ, otherPoint.dirZ, fraction));
    }

    /**
     * Return a new OrientedPoint3d with an in-place rotation around the z-axis by the provided rotateZ. The resulting rotation
     * will be normalized between -&pi; and &pi;.
     * @param rotateZ the rotation around the z-axis
     * @return a new point with the same coordinates, <code>dirX</code> and <code>dirY</code> and modified
     *         <code>dirZ</code>
     * @throws ArithmeticException when <code>rotateZ</code> is <code>NaN</code>
     */
    @Override
    public OrientedPoint3d rotate(final double rotateZ)
    {
        Throw.whenNaN(rotateZ, "rotZ");
        return new OrientedPoint3d(this.x, this.y, this.z, this.dirX, this.dirY,
                AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    /**
     * Return a new OrientedPoint3d point with an in-place rotation by the provided rotateX, rotateY, and rotateZ. The resulting
     * rotations will be normalized between -&pi; and &pi;.
     * @param rotateX the rotation around the x-axis
     * @param rotateY the rotation around the y-axis
     * @param rotateZ the rotation around the z-axis
     * @return a new point with the same coordinates and applied rotations
     * @throws ArithmeticException when any of the rotations is <code>NaN</code>
     */
    public OrientedPoint3d rotate(final double rotateX, final double rotateY, final double rotateZ)
    {
        Throw.whenNaN(rotateX, "rotateX");
        Throw.whenNaN(rotateY, "rotateY");
        Throw.whenNaN(rotateZ, "rotateZ");
        return new OrientedPoint3d(this.x, this.y, this.z, AngleUtil.normalizeAroundZero(this.dirX + rotateX),
                AngleUtil.normalizeAroundZero(this.dirY + rotateY), AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    @Override
    public double getDirX()
    {
        return this.dirX;
    }

    @Override
    public double getDirY()
    {
        return this.dirY;
    }

    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    @Override
    public Iterator<Point3d> iterator()
    {
        return Arrays.stream(new Point3d[] {this}).iterator();
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2$s, rotX=%2$s, rotY=%2$s, rotZ=%2$s]",
                doNotIncludeClassName ? "" : "OrientedPoint3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.dirX, this.dirY, this.dirZ);
    }

    @Override
    public boolean epsilonEquals(final OrientedPoint3d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other");
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
