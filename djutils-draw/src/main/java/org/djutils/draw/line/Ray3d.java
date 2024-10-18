package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Ray3d is a half-line; it has one end point with non-infinite coordinates; the other end point is infinitely far away.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Ray3d extends DirectedPoint3d implements Drawable3d, Ray<Ray3d, DirectedPoint3d, Point3d>
{
    /** */
    private static final long serialVersionUID = 20210119L;

    /**
     * Construct a new Ray3d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param z double; the z coordinate of the finite end point of the ray
     * @param phi double; the angle from the positive X axis direction in radians.
     * @param theta double; the angle from the positive Z axis direction in radians
     * @throws DrawRuntimeException when phi is NaN
     */
    public Ray3d(final double x, final double y, final double z, final double phi, final double theta)
            throws DrawRuntimeException
    {
        super(x, y, z, phi, theta);
    }

    /**
     * Create a new Ray3d from x, y, and z coordinates packed in a double array of three elements and direction phi,theta.
     * @param xyz double[]; the <cite>x</cite>, <cite>y</cite> and <cite>z</cite> coordinates in that order
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @param theta double; the complement of the slope
     * @throws NullPointerException when <cite>xyx</cite> is null
     * @throws IllegalArgumentException when the length of the <cite>xyz</cite> array is not 3, or contains a NaN value, or phi,
     *             or theta is NaN
     */
    public Ray3d(final double[] xyz, final double phi, final double theta) throws NullPointerException, IllegalArgumentException
    {
        super(xyz, phi, theta);
    }

    /**
     * Construct a new Ray3d.
     * @param point Point3d; the finite end point of the ray
     * @param phi double; the angle from the positive X axis direction in radians.
     * @param theta double; the angle from the positive Z axis direction in radians
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when phi is NaN, or theta is NaN
     */
    public Ray3d(final Point3d point, final double phi, final double theta) throws NullPointerException, DrawRuntimeException
    {
        super(point, phi, theta);
    }

    /**
     * Construct a new Ray3d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param z double; the z coordinate of the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @param throughZ double; the z coordinate of another point on the ray
     * @throws DrawRuntimeException when throughX == x and throughY == y and throughZ == z, or any through-value is NaN
     */
    public Ray3d(final double x, final double y, final double z, final double throughX, final double throughY,
            final double throughZ) throws DrawRuntimeException
    {
        super(x, y, z, throughX, throughY, throughZ);
    }

    /**
     * Construct a new Ray3d.
     * @param point Point3d; the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @param throughZ double; the z coordinate of another point on the ray
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when throughX == point.x and throughY == point.y and point.z == throughZ, or any
     *             through-value is NaN
     */
    public Ray3d(final Point3d point, final double throughX, final double throughY, final double throughZ)
            throws NullPointerException, DrawRuntimeException
    {
        super(point, throughX, throughY, throughZ);
    }

    /**
     * Create a new Ray3d with x, y, and z coordinates and orientation specified using a double array of three elements
     * (containing dirX,dirY,dirZ in that order).
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param orientation double[]; the two direction values (theta and phi) in a double array containing theta and phi in that
     *            order. Theta is the angle from the positive x-axis to the projection of the direction in the x-y-plane. Phi is
     *            the rotation from the positive z-axis to the direction.
     * @throws NullPointerException when <code>orientation</code> is null, or contains a NaN value
     * @throws IllegalArgumentException when the length of the <code>direction</code> array is not 2
     */
    public Ray3d(final double x, final double y, final double z, final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        super(x, y, z, orientation);
    }

    /**
     * Create a new Rayt3d from x, y, and z coordinates packed in a double array of three elements and a direction specified
     * using a double array of two elements.
     * @param xyz double[]; the <cite>x</cite>, <cite>y</cite> and <cite>z</cite> coordinates in that order
     * @param orientation double[]; the two orientation angles <cite>phi</cite> and <cite>theta</cite> in that order
     * @throws NullPointerException when <cite>xyx</cite> or <cite>orientation</cite> is null
     * @throws IllegalArgumentException when the length of the <cite>xyx</cite> array is not 3 or the length of the
     *             <cite>orientation</cite> array is not 2
     */
    public Ray3d(final double[] xyz, final double[] orientation) throws NullPointerException, IllegalArgumentException
    {
        super(xyz, orientation);
    }

    /**
     * Construct a new Ray3d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param z double; the z coordinate of the finite end point of the ray
     * @param throughPoint Point3d; another point on the ray
     * @throws NullPointerException when throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at (x, y)
     */
    public Ray3d(final double x, final double y, final double z, final Point3d throughPoint)
            throws NullPointerException, DrawRuntimeException
    {
        super(x, y, z, throughPoint);
    }

    /**
     * Construct a new Ray3d.
     * @param point Point3d; the finite end point of the ray
     * @param throughPoint Point3d; another point on the ray
     * @throws NullPointerException when point is null or throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at point
     */
    public Ray3d(final Point3d point, final Point3d throughPoint) throws NullPointerException, DrawRuntimeException
    {
        super(point, throughPoint);
    }

    /** {@inheritDoc} */
    @Override
    public final double getDirY()
    {
        return this.dirY;
    }

    /** {@inheritDoc} */
    @Override
    public final double getDirZ()
    {
        return this.dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d getEndPoint()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 2;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<DirectedPoint3d> getPoints()
    {
        double sinPhi = Math.sin(this.dirZ);
        double cosPhi = Math.cos(this.dirZ);
        double sinTheta = Math.sin(this.dirY);
        double cosTheta = Math.cos(this.dirY);
        DirectedPoint3d[] array = new DirectedPoint3d[] {this,
                new DirectedPoint3d(cosPhi * sinTheta == 0 ? this.x : cosPhi * sinTheta * Double.POSITIVE_INFINITY,
                        cosPhi * sinPhi == 0 ? this.y : cosPhi * sinPhi * Double.POSITIVE_INFINITY,
                        cosTheta == 0 ? this.z : cosTheta * Double.POSITIVE_INFINITY, this.dirZ, this.dirY)};
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        double sinPhi = Math.sin(this.dirZ);
        double cosPhi = Math.cos(this.dirZ);
        double sinTheta = Math.sin(this.dirY);
        double cosTheta = Math.cos(this.dirY);
        return new Bounds3d(cosPhi * sinTheta >= 0 ? this.x : Double.NEGATIVE_INFINITY,
                cosPhi * sinTheta <= 0 ? this.x : Double.POSITIVE_INFINITY,
                sinPhi * sinTheta >= 0 ? this.y : Double.NEGATIVE_INFINITY,
                sinPhi * sinTheta <= 0 ? this.y : Double.POSITIVE_INFINITY, cosTheta >= 0 ? this.z : Double.NEGATIVE_INFINITY,
                cosTheta <= 0 ? this.z : Double.POSITIVE_INFINITY);
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d neg()
    {
        return new Ray3d(-this.x, -this.y, -this.z, AngleUtil.normalizeAroundZero(this.dirY + Math.PI),
                AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d flip()
    {
        return new Ray3d(this.x, this.y, this.z, AngleUtil.normalizeAroundZero(Math.PI - this.dirY),
                AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        double sinTheta = Math.sin(this.dirY);
        double dX = Math.cos(this.dirZ) * sinTheta;
        double dY = Math.sin(this.dirZ) * sinTheta;
        double dZ = Math.cos(this.dirY);
        return new Ray3d(this.x + dX * position, this.y + dY * position, this.z + dZ * position, this.dirY, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d closestPointOnRay(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.dirY);
        return point.closestPointOnLine(this.x, this.y, this.z, this.x + Math.cos(this.dirZ) * sinTheta,
                this.y + Math.sin(this.dirZ) * sinTheta, this.z + Math.cos(this.dirY), true, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d projectOrthogonal(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.dirY);
        return point.closestPointOnLine(this.x, this.y, this.z, this.x + Math.cos(this.dirZ) * sinTheta,
                this.y + Math.sin(this.dirZ) * sinTheta, this.z + Math.cos(this.dirY), null, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d projectOrthogonalExtended(final Point3d point)
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.dirY);
        return point.closestPointOnLine(getX(), getY(), getZ(), getX() + Math.cos(this.dirZ) * sinTheta,
                getY() + Math.sin(this.dirZ) * sinTheta, getZ() + Math.cos(this.dirY), false, false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractional(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.dirY);
        return point.fractionalPositionOnLine(this.x, this.y, this.z, this.x + Math.cos(this.dirZ) * sinTheta,
                this.y + Math.sin(this.dirZ) * sinTheta, this.z + Math.cos(this.dirY), null, false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.dirY);
        return point.fractionalPositionOnLine(getX(), getY(), getZ(), getX() + Math.cos(this.dirZ) * sinTheta,
                getY() + Math.sin(this.dirZ) * sinTheta, getZ() + Math.cos(this.dirY), false, false);
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
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2$s, phi=%2$s, theta=%2$s]",
                doNotIncludeClassName ? "" : "Ray3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.dirZ, this.dirY);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return super.hashCode();
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
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

}
