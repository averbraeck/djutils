package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.Direction3d;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * Ray3d is a half-line in 3d; it has one end point with non-infinite coordinates; the other end point is infinitely far away.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class Ray3d extends DirectedPoint3d implements Drawable3d, Ray<Ray3d, DirectedPoint3d, Point3d>
{
    /**
     * Construct a new Ray3d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param z the z coordinate of the finite end point of the ray
     * @param dirY the angle from the positive Z axis direction in radians (the complement of the slope)
     * @param dirZ the angle from the positive X axis direction in radians
     * @throws IllegalArgumentException when {@code dirY}, or {@code dirZ} is {@code NaN} (should be impossible)
     */
    public Ray3d(final double x, final double y, final double z, final double dirY, final double dirZ)
    {
        super(x, y, z, dirY, dirZ);
    }

    /**
     * Create a new Ray3d with x, y, and z coordinates and orientation specified using a double array of three elements
     * (containing dirX,dirY,dirZ in that order).
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param directionVector the two direction angles in a double array containing dirY and dirZ in that order. DirY is the
     *            rotation from the positive z-axis to the direction. DirZ is the angle from the positive x-axis to the
     *            projection of the direction in the x-y-plane.
     * @throws NullPointerException when {@code direction} is {@code null}
     * @throws ArithmeticException when the {@code directionVector} array contains a {@code NaN} value
     * @throws IllegalArgumentException when the length of the {@code directionVector} array is not 2
     */
    public Ray3d(final double x, final double y, final double z, final double[] directionVector)
    {
        super(x, y, z, directionVector);
    }

    /**
     * Construct a new Ray3d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param z the z coordinate of the finite end point of the ray
     * @param dir the direction
     * @throws IllegalArgumentException when {@code dirY}, or {@code dirZ} is {@code NaN} (should be impossible)
     */
    public Ray3d(final double x, final double y, final double z, final Direction3d dir)
    {
        super(x, y, z, dir);
    }

    /**
     * Construct a new Ray3d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param z the z coordinate of the finite end point of the ray
     * @param throughX the x coordinate of another point on the ray
     * @param throughY the y coordinate of another point on the ray
     * @param throughZ the z coordinate of another point on the ray
     * @throws ArithmeticException when {@code throughX}, or {@code throughY}, or {@code throughZ} is {@code NaN}
     * @throws IllegalArgumentException when {@code throughX> == x} and {@code throughY == y} and {@code throughZ == z}
     */
    public Ray3d(final double x, final double y, final double z, final double throughX, final double throughY,
            final double throughZ)
    {
        super(x, y, z, throughX, throughY, throughZ);
    }

    /**
     * Construct a new Ray3d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param z the z coordinate of the finite end point of the ray
     * @param throughPoint another point on the ray
     * @throws NullPointerException when {@code throughPoint} is {@code null}
     * @throws ArithmeticException when {@code x}, {@code y}, or {@code z} is {@code NaN}
     * @throws IllegalArgumentException when {@code throughPoint} is exactly at {@code (x, y)}
     */
    public Ray3d(final double x, final double y, final double z, final Point3d throughPoint)
    {
        super(x, y, z, throughPoint);
    }

    /**
     * Create a new Ray3d from x, y, and z coordinates packed in a double array of three elements and direction dirY,dirZ.
     * @param xyz the {@code x}, {@code y} and {@code z} coordinates in that order
     * @param dirY the angle from the positive Z axis direction in radians (the complement of the slope)
     * @param dirZ the angle from the positive X axis direction in radians
     * @throws NullPointerException when {@code xyx} is {@code null}
     * @throws IllegalArgumentException when the length of the {@code xyz} array is not 3, or contains a {@code NaN} value, or
     *             {@code dirY}, or {@code dirZ} is {@code NaN}
     */
    public Ray3d(final double[] xyz, final double dirY, final double dirZ)
    {
        super(xyz, dirY, dirZ);
    }

    /**
     * Create a new Ray3d from x, y, and z coordinates packed in a double array of three elements and direction dirY,dirZ.
     * @param xyz the {@code x}, {@code y} and {@code z} coordinates in that order
     * @param directionVector the two direction angles {@code dirY} and {@code dirZ} in that order
     * @throws NullPointerException when {@code xyz}, or {@code directionVector} is {@code null}
     * @throws ArithmeticException when {@code xyz}, or {@code directionVector} contains a {@code NaN} value
     * @throws IllegalArgumentException when the length of the {@code xyx} is not 3 or the length of the {@code directionVector}
     *             is not 2
     */
    public Ray3d(final double[] xyz, final double[] directionVector)
    {
        super(xyz, directionVector);
    }

    /**
     * Create a new Rayt3d from x, y, and z coordinates packed in a double array of three elements and a direction specified
     * using a double array of two elements.
     * @param xyz the {@code x}, {@code y} and {@code z} coordinates in that order
     * @param dir the direction
     * @throws NullPointerException when {@code xyx} array or {@code dir} is {@code null}
     * @throws ArithmeticException when the {@code xyz} arraycontains a {@code NaN} value
     * @throws IllegalArgumentException when the length of the {@code xyx} array is not 3
     */
    public Ray3d(final double[] xyz, final Direction3d dir)
    {
        super(xyz, dir);
    }

    /**
     * Construct a new Ray3d.
     * @param point the finite end point of the ray
     * @param dirY the angle from the positive Z axis direction in radians (the complement of the slope)
     * @param dirZ the angle from the positive X axis direction in radians
     * @throws NullPointerException when {@code point} is {@code null}
     * @throws IllegalArgumentException when {@code dirY}, or {@code dirZ} is {@code NaN}
     */
    public Ray3d(final Point3d point, final double dirY, final double dirZ)
    {
        super(point, dirY, dirZ);
    }

    /**
     * Construct a new Ray3d.
     * @param point the finite end point of the ray
     * @param dir the direction
     * @throws NullPointerException when {@code point} is {@code null}, or {@code dir} is {@code null}
     */
    public Ray3d(final Point3d point, final Direction3d dir)
    {
        super(point, dir);
    }

    /**
     * Construct a new Ray3d.
     * @param point the finite end point of the ray
     * @param throughX the x coordinate of another point on the ray
     * @param throughY the y coordinate of another point on the ray
     * @param throughZ the z coordinate of another point on the ray
     * @throws NullPointerException when {@code point} is {@code null}
     * @throws ArithmeticException when {@code throughX}, or {@code throughY}, or {@code throughZ} is {@code NaN}
     * @throws IllegalArgumentException when {@code throughX == x} and {@code throughY == y} and {@code throughZ == z}
     */
    public Ray3d(final Point3d point, final double throughX, final double throughY, final double throughZ)
    {
        super(point, throughX, throughY, throughZ);
    }

    /**
     * Construct a new Ray3d.
     * @param point the finite end point of the ray
     * @param throughPoint another point on the ray
     * @throws NullPointerException when {@code point} is {@code null} or {@code throughPoint} is {@code null}
     * @throws IllegalArgumentException when {@code throughPoint} is exactly at {@code point}
     */
    public Ray3d(final Point3d point, final Point3d throughPoint) throws NullPointerException, IllegalArgumentException
    {
        super(point, throughPoint);
    }

    /**
     * Construct a new Ray3d.
     * @param directedPoint point and direction of the new Ray3d
     * @throws NullPointerException when {@code directedPoint} is {@code null}
     */
    public Ray3d(final DirectedPoint3d directedPoint)
    {
        this(directedPoint, directedPoint.dirY, directedPoint.dirZ);
    }

    @Override
    public Ray3d translate(final double dR)
    {
        Throw.whenNaN(dR, "dR");
        double dx = Math.sin(this.dirY) * Math.cos(this.dirZ);
        double dy = Math.sin(this.dirY) * Math.sin(this.dirZ);
        double dz = Math.cos(this.dirY);
        return new Ray3d(this.x + dx * dR, this.y + dy * dR, this.z + dz * dR, this.dirY, this.dirZ);
    }

    @Override
    public Ray3d translate(final double dX, final double dY)
    {
        Throw.whenNaN(dX, "dX");
        Throw.whenNaN(dY, "dY");
        return new Ray3d(this.x + dX, this.y + dY, this.z, this.dirY, this.dirZ);
    }
    
    @Override
    public Ray3d translate(final double dX, final double dY, final double dZ)
    {
        Throw.whenNaN(dX, "dX");
        Throw.whenNaN(dY, "dY");
        Throw.whenNaN(dZ, "dZ");
        return new Ray3d(this.x + dX, this.y + dY, this.z + dZ, this.dirY, this.dirZ);
    }

    @Override
    public Ray3d rotate(final double rotateZ)
    {
        Throw.whenNaN(rotateZ, "rotateZ");
        return new Ray3d(this.x, this.y, this.z, this.dirY, AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    @Override
    public Ray3d rotate(final double rotateY, final double rotateZ)
    {
        Throw.whenNaN(rotateY, "rotateY");
        Throw.whenNaN(rotateZ, "rotateZ");
        return new Ray3d(this.x, this.y, this.z, AngleUtil.normalizeAroundZero(this.dirY + rotateY),
                AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    @Override
    public final double getDirY()
    {
        return this.dirY;
    }

    @Override
    public final double getDirZ()
    {
        return this.dirZ;
    }

    @Override
    public DirectedPoint3d getEndPoint()
    {
        return this;
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public Iterator<Point3d> iterator()
    {
        double sinDirZ = Math.sin(this.dirZ);
        double cosDirZ = Math.cos(this.dirZ);
        double sinDirY = Math.sin(this.dirY);
        double cosDirY = Math.cos(this.dirY);
        Point3d[] array = new Point3d[] {this,
                new Point3d(cosDirZ * sinDirY == 0 ? this.x : cosDirZ * sinDirY * Double.POSITIVE_INFINITY,
                        cosDirZ * sinDirZ == 0 ? this.y : cosDirZ * sinDirZ * Double.POSITIVE_INFINITY,
                        cosDirY == 0 ? this.z : cosDirY * Double.POSITIVE_INFINITY)};
        return Arrays.stream(array).iterator();
    }

    @Override
    public Bounds3d getAbsoluteBounds()
    {
        double sinDirZ = Math.sin(this.dirZ);
        double cosDirZ = Math.cos(this.dirZ);
        double sinDirY = Math.sin(this.dirY);
        double cosDirY = Math.cos(this.dirY);
        return new Bounds3d(cosDirZ * sinDirY >= 0 ? this.x : Double.NEGATIVE_INFINITY,
                cosDirZ * sinDirY <= 0 ? this.x : Double.POSITIVE_INFINITY,
                sinDirZ * sinDirY >= 0 ? this.y : Double.NEGATIVE_INFINITY,
                sinDirZ * sinDirY <= 0 ? this.y : Double.POSITIVE_INFINITY, cosDirY >= 0 ? this.z : Double.NEGATIVE_INFINITY,
                cosDirY <= 0 ? this.z : Double.POSITIVE_INFINITY);
    }

    @Override
    public Ray3d neg()
    {
        return new Ray3d(-this.x, -this.y, -this.z, AngleUtil.normalizeAroundZero(this.dirY + Math.PI),
                AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public Ray3d flip()
    {
        return new Ray3d(this.x, this.y, this.z, AngleUtil.normalizeAroundZero(Math.PI - this.dirY),
                AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public Ray3d getLocationExtended(final double position) throws ArithmeticException, IllegalArgumentException
    {
        Throw.whenNaN(position, "position");
        Throw.when(Double.isInfinite(position), IllegalArgumentException.class, "position must be finite");
        double sinDirY = Math.sin(this.dirY);
        double dX = Math.cos(this.dirZ) * sinDirY;
        double dY = Math.sin(this.dirZ) * sinDirY;
        double dZ = Math.cos(this.dirY);
        return new Ray3d(this.x + dX * position, this.y + dY * position, this.z + dZ * position, this.dirY, this.dirZ);
    }

    @Override
    public Point3d closestPointOnRay(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        double sinDirY = Math.sin(this.dirY);
        return point.closestPointOnLine(this.x, this.y, this.z, this.x + Math.cos(this.dirZ) * sinDirY,
                this.y + Math.sin(this.dirZ) * sinDirY, this.z + Math.cos(this.dirY), true, false);
    }

    @Override
    public Point3d projectOrthogonal(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        double sinDirY = Math.sin(this.dirY);
        return point.closestPointOnLine(this.x, this.y, this.z, this.x + Math.cos(this.dirZ) * sinDirY,
                this.y + Math.sin(this.dirZ) * sinDirY, this.z + Math.cos(this.dirY), null, false);
    }

    @Override
    public Point3d projectOrthogonalExtended(final Point3d point)
    {
        Throw.whenNull(point, "point");
        double sinDirY = Math.sin(this.dirY);
        return point.closestPointOnLine(getX(), getY(), getZ(), getX() + Math.cos(this.dirZ) * sinDirY,
                getY() + Math.sin(this.dirZ) * sinDirY, getZ() + Math.cos(this.dirY), false, false);
    }

    @Override
    public double projectOrthogonalFractional(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        double sinDirY = Math.sin(this.dirY);
        return point.fractionalPositionOnLine(this.x, this.y, this.z, this.x + Math.cos(this.dirZ) * sinDirY,
                this.y + Math.sin(this.dirZ) * sinDirY, this.z + Math.cos(this.dirY), null, false);
    }

    @Override
    public double projectOrthogonalFractionalExtended(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        double sinDirY = Math.sin(this.dirY);
        return point.fractionalPositionOnLine(getX(), getY(), getZ(), getX() + Math.cos(this.dirZ) * sinDirY,
                getY() + Math.sin(this.dirZ) * sinDirY, getZ() + Math.cos(this.dirY), false, false);
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2$s, dirY=%2$s, dirZ=%2$s]",
                doNotIncludeClassName ? "" : "Ray3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.dirY, this.dirZ);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
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
        return true;
    }

}
