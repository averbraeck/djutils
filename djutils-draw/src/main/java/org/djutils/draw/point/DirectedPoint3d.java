package org.djutils.draw.point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import org.djutils.base.AngleUtil;
import org.djutils.draw.Directed3d;
import org.djutils.draw.Direction3d;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * A DirectedPoint3d is a point in 3d space that additionally carries a direction in 3d i.c. dirY (similar to tilt; measured as
 * an angle from the positive z-direction) and dirZ (similar to pan; measured as an angle from the positive x-direction).
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class DirectedPoint3d extends Point3d implements Directed3d<DirectedPoint3d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The direction as rotation around the x-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirY;

    /** The direction as rotation from the positive z-axis towards the x-y plane. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates and orientation dirY,dirZ.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param dirY double; the complement of the slope
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when <cite>x</cite>, <cite>y</cite>, <cite>z</cite>, <cite>dirY</cite>, or
     *             <cite>dirZ</cite> is NaN
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double dirY, final double dirZ)
            throws IllegalArgumentException
    {
        super(x, y, z);
        Throw.when(Double.isNaN(dirY) || Double.isNaN(dirZ), IllegalArgumentException.class,
                "dirY and dirZ must be numbers (not NaN)");
        this.dirZ = dirZ;
        this.dirY = dirY;
    }

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates in a double[] and direction dirY,dirZ.
     * @param xyz double[]; the x, y and z coordinates
     * @param dirY double; the complement of the slope
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when <cite>xyx</cite> is null
     * @throws IllegalArgumentException when the length of the <cite>xyz</cite> array is not 3, or contains a NaN value, or
     *             <cite>dirY</cite>, or <cite>dirZ</cite> is NaN
     */
    public DirectedPoint3d(final double[] xyz, final double dirY, final double dirZ)
            throws NullPointerException, IllegalArgumentException
    {
        super(xyz);
        Throw.when(Double.isNaN(dirY) || Double.isNaN(dirZ), IllegalArgumentException.class,
                "dirY and dirZ must be numbers (not NaN)");
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Create a new DirectedPoint3d from another Point3d and and direction Direction3d.
     * @param point Point3d; the point from which this OrientedPoint3d will be instantiated
     * @param direction Direction3d; the direction
     * @throws NullPointerException when direction is null
     */
    public DirectedPoint3d(final Point3d point, final Direction3d direction) throws NullPointerException
    {
        this(point.x, point.y, point.z, Throw.whenNull(direction, "direction").dirY, direction.dirZ);
    }

    /**
     * Create a new DirectedPoint3d from another Point3d and and direction dirY,dirZ.
     * @param point Point3d; the point from which this OrientedPoint3d will be instantiated
     * @param dirY double; the complement of the slope
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when dirY, or dirZ is NaN
     */
    public DirectedPoint3d(final Point3d point, final double dirY, final double dirZ) throws IllegalArgumentException
    {
        this(point.x, point.y, point.z, dirY, dirZ);
    }

    /**
     * Construct a new DirectedPoint3d from three coordinates and the coordinates of a point that the direction goes through.
     * @param x double; the x coordinate of the new DirectedPoint
     * @param y double; the y coordinate of the new DirectedPoint
     * @param z double; the z coordinate of the new DirectedPoint
     * @param throughX double; the x-coordinate of a point that the direction goes through
     * @param throughY double; the y-coordinate of a point that the direction goes through
     * @param throughZ double; the z-coordinate of a point that the direction goes through
     * @throws DrawRuntimeException when <cite>throughX</cite> == <cite>x</cite> and <cite>throughY</cite> == <cite>y</cite> and
     *             <cite>throughZ</cite> == <cite>z</cite>, or any through-value is NaN
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double throughX, final double throughY,
            final double throughZ) throws DrawRuntimeException
    {
        this(x, y, z, buildDirectionVector(throughX - x, throughY - y, throughZ - z));
    }

    /**
     * Build the direction vector.
     * @param dX double; x difference
     * @param dY double; y difference
     * @param dZ double; z difference
     * @return double[]; a two-element array containing dirY and dirZ
     */
    private static double[] buildDirectionVector(final double dX, final double dY, final double dZ)
    {
        Throw.when(0 == dX && 0 == dY && 0 == dZ, IllegalArgumentException.class, "Through point may not be equal to point");
        return new double[] {Math.atan2(Math.hypot(dX, dY), dZ), Math.atan2(dY, dX)};
    }

    /**
     * Construct a new DirectedPoint3d form a Point3d and the coordinates that the direction goes through.
     * @param point Point3d; the point
     * @param throughX double; the x coordinate of a point that the direction goes through
     * @param throughY double; the y coordinate of a point that the direction goes through
     * @param throughZ double; the z coordinate of a point that the direction goes through
     * @throws DrawRuntimeException when <cite>throughX</cite> == <cite>point.x</cite> and <cite>throughY</cite> ==
     *             <cite>point.y</cite> and <cite>throughZ</cite> == <cite>point.z</cite>, or any through-value is NaN
     */
    public DirectedPoint3d(final Point3d point, final double throughX, final double throughY, final double throughZ)
            throws DrawRuntimeException
    {
        this(Throw.whenNull(point, "point").x, point.y, point.z, throughX, throughY, throughZ);
    }

    /**
     * Verify that a double array is not null, has two elements.
     * @param orientation double[]; the array to check
     * @return double; the first element of the argument
     * @throws NullPointerException when <code>orientation</code> is null
     * @throws IllegalArgumentException when the length of the <code>orientation</code> array is not 2
     */
    private static double checkOrientationVector(final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.when(orientation.length != 2, IllegalArgumentException.class, "length of orientation array must be 2");
        return orientation[0];
    }

    /**
     * Create a new DirectedPoint3d with x, y and z coordinates and orientation specified using a double array of two elements
     * (containing dirY,dirZ in that order).
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param orientation double[]; the two direction values (dirY and dirZ) in a double array containing dirY and dirZ in that
     *            order. DirY is the rotation from the positive z-axis to the direction. DirZ is the angle from the positive
     *            x-axis to the projection of the direction in the x-y-plane.
     * @throws NullPointerException when <code>orientation</code> is null
     * @throws IllegalArgumentException when the length of the <code>direction</code> array is not 2, or contains a NaN value
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        this(x, y, z, checkOrientationVector(orientation), orientation[1]);
    }

    /**
     * Create a new OrientedPoint3d from x, y and z coordinates packed in a double array of three elements and a direction
     * specified using a double array of two elements.
     * @param xyz double[]; the <cite>x</cite>, <cite>y</cite> and <cite>z</cite> coordinates in that order
     * @param orientation double[]; the two orientation angles <cite>dirY</cite> and <cite>dirZ</cite> in that order
     * @throws NullPointerException when <cite>xyx</cite> or <cite>orientation</cite> is null
     * @throws IllegalArgumentException when the length of the <cite>xyx</cite> array is not 3 or the length of the
     *             <cite>orientation</cite> array is not 2
     */
    public DirectedPoint3d(final double[] xyz, final double[] orientation) throws NullPointerException, IllegalArgumentException
    {
        this(xyz, checkOrientationVector(orientation), orientation[1]);
    }

    /**
     * Construct a new DirectedPoint3d from x, y and z coordinates and a point that the direction goes through.
     * @param x double; the x coordinate of the new DirectedPoint3d
     * @param y double; the y coordinate of the new DirectedPoint3d
     * @param z double; the z coordinate of the new DirectedPoint3d
     * @param throughPoint Point3d; a point that the direction goes through
     * @throws NullPointerException when throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at (x, y, z)
     */
    public DirectedPoint3d(final double x, final double y, final double z, final Point3d throughPoint)
            throws NullPointerException, DrawRuntimeException
    {
        this(x, y, z, Throw.whenNull(throughPoint, "througPoint").x, throughPoint.y, throughPoint.z);
    }

    /**
     * Construct a new DirectedPoint3d.
     * @param point Point3d; the location of the new DirectedPoint3d
     * @param throughPoint Point3d; another point that the direction goes through
     * @throws NullPointerException when point is null or throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at point
     */
    public DirectedPoint3d(final Point3d point, final Point3d throughPoint) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(point, "point").x, point.y, point.z, Throw.whenNull(throughPoint, "throughPoint").x, throughPoint.y,
                throughPoint.z);
    }

    @Override
    public DirectedPoint3d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not contain NaN");
        return new DirectedPoint3d(this.x + dx, this.y + dy, this.z, this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d translate(final double dx, final double dy, final double dz) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "Translation may not contain NaN");
        return new DirectedPoint3d(this.x + dx, this.y + dy, this.z + dz, this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d scale(final double factor) throws IllegalArgumentException
    {
        return new DirectedPoint3d(this.x * factor, this.y * factor, this.z * factor, this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d neg()
    {
        return new DirectedPoint3d(-this.x, -this.y, -this.z, AngleUtil.normalizeAroundZero(this.dirY + Math.PI),
                AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public DirectedPoint3d abs()
    {
        return new DirectedPoint3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return new DirectedPoint3d(this.x / length, this.y / length, this.z / length, this.dirY, this.dirZ);
    }

    /**
     * Interpolate towards another DirectedPoint3d with a fraction. It is allowed for fraction to be less than zero or larger
     * than 1. In that case the interpolation turns into an extrapolation. DirY and dirZ are interpolated/extrapolated using the
     * interpolateShortest method.
     * @param otherPoint OrientedPoint3d; the other point
     * @param fraction double; the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is
     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0;
     *            <code>this</code> Point is returned; if <code>fraction</code> is 1, the <code>otherPoint</code> is returned
     * @return DirectedPoint3d; a new OrientedPoint3d at the requested fraction
     * @throws NullPointerException when otherPoint is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    public DirectedPoint3d interpolate(final DirectedPoint3d otherPoint, final double fraction)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(otherPoint, "otherPoint");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        if (0.0 == fraction)
        {
            return this;
        }
        if (1.0 == fraction)
        {
            return otherPoint;
        }
        return new DirectedPoint3d((1.0 - fraction) * this.x + fraction * otherPoint.x,
                (1.0 - fraction) * this.y + fraction * otherPoint.y, (1.0 - fraction) * this.z + fraction * otherPoint.z,
                AngleUtil.interpolateShortest(this.dirY, otherPoint.dirY, fraction),
                AngleUtil.interpolateShortest(this.dirZ, otherPoint.dirZ, fraction));
    }

    /**
     * Return a new DirectedPoint3d with an in-place rotation around the z-axis by the provided rotateZ. The resulting rotation
     * will be normalized between -&pi; and &pi;.
     * @param rotateZ double; the rotation around the z-axis
     * @return DirectedPoint3d; a new point with the same coordinates, dirY and modified dirZ
     * @throws IllegalArgumentException when rotateZ is NaN
     */
    public DirectedPoint3d rotate(final double rotateZ) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateZ), IllegalArgumentException.class, "rotateZ must be a number (not NaN)");
        return new DirectedPoint3d(this.x, this.y, this.z, this.dirY, AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    /**
     * Return a new OrientedPoint3d point with an in-place rotation by the provided rotateY, and rotateZ. The resulting
     * rotations will be normalized between -&pi; and &pi;.
     * @param rotateY double; the rotation around the y-axis
     * @param rotateZ double; the rotation around the z-axis
     * @return DirectedPoint3d; a new point with the same coordinates and applied rotations
     * @throws IllegalArgumentException when any of the rotations is NaN
     */
    public DirectedPoint3d rotate(final double rotateY, final double rotateZ) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateY) || Double.isNaN(rotateZ), IllegalArgumentException.class,
                "rotateY and rotateZ must be a numbers (not NaN)");
        return new DirectedPoint3d(this.x, this.y, this.z, AngleUtil.normalizeAroundZero(this.dirY + rotateY),
                AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    @Override
    public double getDirY()
    {
        return this.dirY;
    }

    @Override
    public Iterator<? extends DirectedPoint3d> getPoints()
    {
        return Arrays.stream(new DirectedPoint3d[] {this}).iterator();
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2%s, dirY=%2$s, dirZ=%2$s]",
                doNotIncludeClassName ? "" : "DirectedPoint3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.dirY, this.dirZ);
    }

    @Override
    public boolean epsilonEquals(final DirectedPoint3d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other");
        Throw.when(epsilonCoordinate < 0 || epsilonRotation < 0, IllegalArgumentException.class,
                "epsilonCoordinate and epsilonRotation may not be negative");
        Throw.when(Double.isNaN(epsilonCoordinate) || Double.isNaN(epsilonRotation), IllegalArgumentException.class,
                "epsilonCoordinate and epsilonRotation may not be NaN");
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
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirZ - other.dirZ)) > epsilonRotation)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirY - other.dirY)) > epsilonRotation)
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
        result = prime * result + Objects.hash(this.dirZ, this.dirY);
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
        if (getClass() != obj.getClass())
            return false;
        DirectedPoint3d other = (DirectedPoint3d) obj;
        return Double.doubleToLongBits(this.dirZ) == Double.doubleToLongBits(other.dirZ)
                && Double.doubleToLongBits(this.dirY) == Double.doubleToLongBits(other.dirY);
    }

}
