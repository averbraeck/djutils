package org.djutils.draw.point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import org.djutils.draw.Directed3d;
import org.djutils.draw.Direction3d;
import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * A DirectedPoint3d is a point in 3d space that additionally carries a direction in 3d i.c. dirY (similar to tilt; measured as
 * an angle from the positive z-direction) and dirZ (similar to pan; measured as an angle from the positive x-direction).
 * <p>
 * Copyright (c) 2023-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class DirectedPoint3d extends Point3d implements Directed3d
{
    /** The direction as rotation around the x-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirY;

    /** The direction as rotation from the positive z-axis towards the x-y plane. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates and direction dirY,dirZ.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param dirY the complement of the slope
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws ArithmeticException when {@code x}, {@code y}, {@code z}, {@code dirY}, or {@code dirZ} is {@code NaN}
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double dirY, final double dirZ)
    {
        super(x, y, z);
        Throw.whenNaN(dirY, "dirY");
        Throw.whenNaN(dirZ, "dirZ");
        this.dirZ = dirZ;
        this.dirY = dirY;
    }

    /**
     * Create a new DirectedPoint3d with x, y and z coordinates and direction specified using a double array of two elements
     * (containing dirY,dirZ in that order).
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param directionVector the two direction angles (dirY and dirZ) in a double array containing dirY and dirZ in that order.
     *            DirY is the rotation from the positive z-axis to the direction. DirZ is the angle from the positive x-axis to
     *            the projection of the direction in the x-y-plane.
     * @throws NullPointerException when {@code directionVector} is {@code null}
     * @throws ArithmeticException when {@code x}, {@code y}, {@code z} is {@code NaN}, or {@code directionVector} contains a
     *             {@code NaN} value
     * @throws IllegalArgumentException when the length of the {@code directionVector} array is not 2, or contains a {@code NaN}
     *             value
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double[] directionVector)
    {
        this(x, y, z, checkDirectionVector(directionVector), directionVector[1]);
    }

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates and Direction3d.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param dir the direction
     * @throws NullPointerException when {@code dir>} is {@code null}
     * @throws ArithmeticException when {@code x}, {@code y}, {@code z}, {@code dirY}, or {@code dirZ} is {@code NaN}
     */
    public DirectedPoint3d(final double x, final double y, final double z, final Direction3d dir)
    {
        super(x, y, z);
        Throw.whenNull(dir, "dir");
        this.dirZ = dir.dirZ;
        this.dirY = dir.dirY;
    }

    /**
     * Construct a new DirectedPoint3d from three coordinates and the coordinates of a point that the direction goes through.
     * @param x the x coordinate of the new DirectedPoint
     * @param y the y coordinate of the new DirectedPoint
     * @param z the z coordinate of the new DirectedPoint
     * @param throughX the x-coordinate of a point that the direction goes through
     * @param throughY the y-coordinate of a point that the direction goes through
     * @param throughZ the z-coordinate of a point that the direction goes through
     * @throws ArithmeticException when {@code z}, {@code y}, {@code z}, {@code throughX}, {@code throughY}, or {@code throughZ}
     *             is {@code NaN}
     * @throws IllegalArgumentException when {@code throughX} == {@code x} and {@code throughY} == {@code y} and
     *             {@code throughZ} == {@code z}
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double throughX, final double throughY,
            final double throughZ)
    {
        this(x, y, z, buildDirectionVector(throughX - x, throughY - y, throughZ - z));
    }

    /**
     * Construct a new DirectedPoint3d from x, y and z coordinates and a point that the direction goes through.
     * @param x the x coordinate of the new DirectedPoint3d
     * @param y the y coordinate of the new DirectedPoint3d
     * @param z the z coordinate of the new DirectedPoint3d
     * @param throughPoint a point that the direction goes through
     * @throws NullPointerException when {@code throughPoint} is {@code null}
     * @throws ArithmeticException when {@code x}, {@code y}, or {@code z} is {@code NaN}
     * @throws IllegalArgumentException when {@code throughPoint} is exactly at {@code (x y,z)}
     */
    public DirectedPoint3d(final double x, final double y, final double z, final Point3d throughPoint)
    {
        this(x, y, z, Throw.whenNull(throughPoint, "througPoint").x, throughPoint.y, throughPoint.z);
    }

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates in a double[] and direction dirY,dirZ.
     * @param xyz the x, y and z coordinates
     * @param dirY the complement of the slope
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when {@code xyx} is {@code null}
     * @throws IllegalArgumentException when the length of the {@code xyz} array is not 3, or contains a {@code NaN} value, or
     *             {@code dirY}, or {@code dirZ} is {@code NaN}
     */
    public DirectedPoint3d(final double[] xyz, final double dirY, final double dirZ)
    {
        super(xyz);
        Throw.whenNaN(dirY, "dirY");
        Throw.whenNaN(dirZ, "dirZ");
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Create a new OrientedPoint3d from x, y and z coordinates packed in a double array of three elements and a direction
     * specified using a double array of two elements.
     * @param xyz the {@code x}, {@code y} and {@code z} coordinates in that order
     * @param directionVector the two direction angles {@code dirY} and {@code dirZ} in that order
     * @throws NullPointerException when {@code xyz}, or {@code directionVector} is {@code null}
     * @throws ArithmeticException when {@code xyz}, or {@code directionVector} contains a {@code NaN} value
     * @throws IllegalArgumentException when the length of the {@code xyx} is not 3 or the length of the {@code directionVector}
     *             is not 2
     */
    public DirectedPoint3d(final double[] xyz, final double[] directionVector)
    {
        this(xyz, checkDirectionVector(directionVector), directionVector[1]);
    }

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates in a double[] and a Direction3d.
     * @param xyz the x, y and z coordinates
     * @param dir the direction
     * @throws NullPointerException when {@code xyx} is {@code null}, or {@code dir} is {@code null}
     * @throws IllegalArgumentException when the length of the {@code xyz} array is not 3, or contains a {@code NaN} value
     */
    public DirectedPoint3d(final double[] xyz, final Direction3d dir)
    {
        super(xyz);
        Throw.whenNull(dir, "dir");
        this.dirY = dir.dirY;
        this.dirZ = dir.dirZ;
    }

    /**
     * Create a new DirectedPoint3d from another Point3d and and direction dirY,dirZ.
     * @param point the point from which this OrientedPoint3d will be instantiated
     * @param dirY the complement of the slope
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws ArithmeticException when {@code dirY}, or {@code dirZ} is {@code NaN}
     */
    public DirectedPoint3d(final Point3d point, final double dirY, final double dirZ)
    {
        this(point.x, point.y, point.z, dirY, dirZ);
    }

    /**
     * Create a new DirectedPoint3d from another Point3d and a Direction3d.
     * @param point the point from which this OrientedPoint3d will be instantiated
     * @param direction the direction
     * @throws NullPointerException when {@code point}, or {@code direction} is {@code null}
     */
    public DirectedPoint3d(final Point3d point, final Direction3d direction)
    {
        this(point.x, point.y, point.z, Throw.whenNull(direction, "direction").dirY, direction.dirZ);
    }

    /**
     * Construct a new DirectedPoint3d form a Point3d and the coordinates that the direction goes through.
     * @param point the point
     * @param throughX the x coordinate of a point that the direction goes through
     * @param throughY the y coordinate of a point that the direction goes through
     * @param throughZ the z coordinate of a point that the direction goes through
     * @throws NullPointerException when {@code point} is {@code null}
     * @throws ArithmeticException when {@code throughX}, or {@code throughY}, or {@code throughZ} is {@code NaN}
     * @throws IllegalArgumentException when {@code throughX} == {@code point.x} and {@code throughY} == {@code point.y} and
     *             {@code throughZ} == {@code point.z}
     */
    public DirectedPoint3d(final Point3d point, final double throughX, final double throughY, final double throughZ)
    {
        this(Throw.whenNull(point, "point").x, point.y, point.z, throughX, throughY, throughZ);
    }

    /**
     * Construct a new DirectedPoint3d.
     * @param point the location of the new DirectedPoint3d
     * @param throughPoint another point that the direction goes through
     * @throws NullPointerException when {@code point} is {@code null} or {@code throughPoint} is {@code null}
     * @throws IllegalArgumentException when {@code throughPoint} is exactly at {@code point}
     */
    public DirectedPoint3d(final Point3d point, final Point3d throughPoint)
    {
        this(Throw.whenNull(point, "point").x, point.y, point.z, Throw.whenNull(throughPoint, "throughPoint").x, throughPoint.y,
                throughPoint.z);
    }

    /**
     * Build the direction vector.
     * @param dX x difference
     * @param dY y difference
     * @param dZ z difference
     * @return a two-element array containing dirY and dirZ
     * @throws IllegalArgumentException when {@code dX} == {@code 0.0} and {@code dY} == {@code 0.0} and {@code dZ} ==
     *             {@code 0.0}
     */
    private static double[] buildDirectionVector(final double dX, final double dY, final double dZ)
    {
        Throw.when(0 == dX && 0 == dY && 0 == dZ, IllegalArgumentException.class, "Through point may not be equal to point");
        return new double[] {Math.atan2(Math.hypot(dX, dY), dZ), Math.atan2(dY, dX)};
    }

    /**
     * Verify that a double array is not null, has two elements.
     * @param direction the array to check
     * @return the first element of the argument
     * @throws NullPointerException when {@code direction} is {@code null}
     * @throws IllegalArgumentException when the length of the {@code direction} array is not 2
     */
    private static double checkDirectionVector(final double[] direction)
    {
        Throw.when(direction.length != 2, IllegalArgumentException.class, "length of direction array must be 2");
        return direction[0];
    }

    @Override
    public DirectedPoint3d translate(final double dR)
    {
        Throw.whenNaN(dR, "dR");
        double dx = Math.sin(this.dirY) * Math.cos(this.dirZ);
        double dy = Math.sin(this.dirY) * Math.sin(this.dirZ);
        double dz = Math.cos(this.dirY);
        return new DirectedPoint3d(this.x + dx * dR, this.y + dy * dR, this.z + dz * dR, this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d translate(final double dX, final double dY)
    {
        Throw.whenNaN(dX, "dX");
        Throw.whenNaN(dY, "dY");
        return new DirectedPoint3d(this.x + dX, this.y + dY, this.z, this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d translate(final double dX, final double dY, final double dZ)
    {
        Throw.whenNaN(dX, "dX");
        Throw.whenNaN(dY, "dY");
        Throw.whenNaN(dZ, "dZ");
        return new DirectedPoint3d(this.x + dX, this.y + dY, this.z + dZ, this.dirY, this.dirZ);
    }

    @Override
    public DirectedPoint3d scale(final double factor)
    {
        Throw.whenNaN(factor, "factor");
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
    public DirectedPoint3d normalize() throws IllegalArgumentException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        Throw.when(length == 0.0, IllegalArgumentException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return new DirectedPoint3d(this.x / length, this.y / length, this.z / length, this.dirY, this.dirZ);
    }

    /**
     * Interpolate towards another DirectedPoint3d with a fraction. It is allowed for fraction to be less than zero or larger
     * than 1. In that case the interpolation turns into an extrapolation. DirY and dirZ are interpolated/extrapolated using the
     * interpolateShortest method.
     * @param otherPoint the other point
     * @param fraction the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is between 0
     *            and 1, it is an interpolation, otherwise an extrapolation. If {@code fraction} is 0; {@code this} Point is
     *            returned; if {@code fraction} is 1, the {@code otherPoint} is returned
     * @return a new {@code DirectedPoint3d} at the requested {@code fraction}
     * @throws NullPointerException when {@code otherPoint} is {@code null}
     * @throws ArithmeticException when {@code fraction} is {@code NaN}
     */
    public DirectedPoint3d interpolate(final DirectedPoint3d otherPoint, final double fraction)
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
        return new DirectedPoint3d((1.0 - fraction) * this.x + fraction * otherPoint.x,
                (1.0 - fraction) * this.y + fraction * otherPoint.y, (1.0 - fraction) * this.z + fraction * otherPoint.z,
                AngleUtil.interpolateShortest(this.dirY, otherPoint.dirY, fraction),
                AngleUtil.interpolateShortest(this.dirZ, otherPoint.dirZ, fraction));
    }

    @Override
    public DirectedPoint3d rotate(final double rotateZ)
    {
        Throw.whenNaN(rotateZ, "rotateZ");
        return new DirectedPoint3d(this.x, this.y, this.z, this.dirY, AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    @Override
    public DirectedPoint3d rotate(final double rotateY, final double rotateZ)
    {
        Throw.whenNaN(rotateY, "rotateY");
        Throw.whenNaN(rotateZ, "rotateZ");
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
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2%s, dirY=%2$s, dirZ=%2$s]",
                doNotIncludeClassName ? "" : "DirectedPoint3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.dirY, this.dirZ);
    }

    /**
     * Compare this Directed with another Directed with specified tolerances in the coordinates and the angles.
     * @param other the Directed to compare to
     * @param epsilonCoordinate the upper bound of difference for one of the coordinates; use Double.POSITIVE_INFINITY if you do
     *            not want to check the coordinates
     * @param epsilonDirection the upper bound of difference for the direction(s); use Double.POSITIVE_INFINITY if you do not
     *            want to check the angles
     * @return boolean;{@code true} if {@code x}, {@code y}, and possibly {@code z} are less than {@code epsilonCoordinate}
     *         apart, and {@code rotZ} and possibly {@code rotX}, and possibly {@code rotY}are less than
     *         {@code epsilonDirection} apart, otherwise {@code false}
     * @throws NullPointerException when {@code other} is {@code null}
     * @throws ArithmeticException when {@code epsilonCoordinate} or {@code epsilonDirection} is {@code NaN}
     * @throws IllegalArgumentException {@code epsilonCoordinate} or {@code epsilonDirection} is {@code negative}
     */
    public boolean epsilonEquals(final DirectedPoint3d other, final double epsilonCoordinate, final double epsilonDirection)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other");
        Throw.when(epsilonCoordinate < 0 || epsilonDirection < 0, IllegalArgumentException.class,
                "epsilonCoordinate and epsilonDirection may not be negative");
        Throw.whenNaN(epsilonCoordinate, "epsilonCoordinate");
        Throw.whenNaN(epsilonDirection, "epsilonDirection");
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
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirZ - other.dirZ)) > epsilonDirection)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirY - other.dirY)) > epsilonDirection)
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
