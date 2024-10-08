package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
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
public class Ray3d extends Point3d implements Drawable3d, Ray<Ray3d, Point3d>
{
    /** */
    private static final long serialVersionUID = 20210119L;

    /** Phi; the angle from the positive X axis direction in radians. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double phi;

    /** Theta; the angle from the positive Z axis direction in radians. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double theta;

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
        super(x, y, z);
        Throw.when(Double.isNaN(phi) || Double.isNaN(theta), DrawRuntimeException.class, "phi and theta may not be NaN");
        this.phi = phi;
        this.theta = theta;
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
        this(Throw.whenNull(point, "point may not be null").x, point.y, point.z, phi, theta);
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
        super(x, y, z);
        Throw.when(throughX == x && throughY == y && throughZ == z, DrawRuntimeException.class,
                "the coordinates of the through point must differ from (x, y, z)");
        Throw.when(Double.isNaN(throughX) || Double.isNaN(throughY) || Double.isNaN(throughZ), DrawRuntimeException.class,
                "throughX, throughY and throughZ must be numbers (not NaN)");
        this.phi = Math.atan2(throughY - y, throughX - x);
        this.theta = Math.atan2(Math.hypot(throughX - x, throughY - y), throughZ - z);
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
        this(Throw.whenNull(point, "point may not be null").x, point.y, point.z, throughX, throughY, throughZ);
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
        this(x, y, z, Throw.whenNull(throughPoint, "througPoint may not be null").x, throughPoint.y, throughPoint.z);
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
        this(Throw.whenNull(point, "point may not be null").x, point.y, point.z,
                Throw.whenNull(throughPoint, "throughPoint may not be null").x, throughPoint.y, throughPoint.z);
    }

    /** {@inheritDoc} */
    @Override
    public final double getPhi()
    {
        return this.phi;
    }

    /**
     * Retrieve the angle from the positive Z axis direction in radians.
     * @return double; the angle from the positive Z axis direction in radians
     */
    public final double getTheta()
    {
        return this.theta;
    }

    /** {@inheritDoc} */
    @Override
    public Point3d getEndPoint()
    {
        return new Point3d(this.x, this.y, this.z);
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 2;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point3d> getPoints()
    {
        double sinPhi = Math.sin(this.phi);
        double cosPhi = Math.cos(this.phi);
        double sinTheta = Math.sin(this.theta);
        double cosTheta = Math.cos(this.theta);
        Point3d[] array = new Point3d[] {new Point3d(this.x, this.y, this.z),
                new Point3d(cosPhi * sinTheta == 0 ? this.x : cosPhi * sinTheta * Double.POSITIVE_INFINITY,
                        cosPhi * sinPhi == 0 ? this.y : cosPhi * sinPhi * Double.POSITIVE_INFINITY,
                        cosTheta == 0 ? this.z : cosTheta * Double.POSITIVE_INFINITY)};
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        double sinPhi = Math.sin(this.phi);
        double cosPhi = Math.cos(this.phi);
        double sinTheta = Math.sin(this.theta);
        double cosTheta = Math.cos(this.theta);
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
        return new Ray3d(-this.x, -this.y, -this.z, AngleUtil.normalizeAroundZero(this.phi + Math.PI),
                AngleUtil.normalizeAroundZero(this.theta + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d flip()
    {
        return new Ray3d(this.x, this.y, this.z, AngleUtil.normalizeAroundZero(this.phi + Math.PI),
                AngleUtil.normalizeAroundZero(Math.PI - this.theta));
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        double sinTheta = Math.sin(this.theta);
        double dX = Math.cos(this.phi) * sinTheta;
        double dY = Math.sin(this.phi) * sinTheta;
        double dZ = Math.cos(this.theta);
        return new Ray3d(this.x + dX * position, this.y + dY * position, this.z + dZ * position, this.phi, this.theta);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d closestPointOnRay(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.theta);
        return point.closestPointOnLine(this.x, this.y, this.z, this.x + Math.cos(this.phi) * sinTheta,
                this.y + Math.sin(this.phi) * sinTheta, this.z + Math.cos(this.theta), true, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d projectOrthogonal(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.theta);
        return point.closestPointOnLine(this.x, this.y, this.z, this.x + Math.cos(this.phi) * sinTheta,
                this.y + Math.sin(this.phi) * sinTheta, this.z + Math.cos(this.theta), null, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d projectOrthogonalExtended(final Point3d point)
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.theta);
        return point.closestPointOnLine(getX(), getY(), getZ(), getX() + Math.cos(this.phi) * sinTheta,
                getY() + Math.sin(this.phi) * sinTheta, getZ() + Math.cos(this.theta), false, false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractional(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.theta);
        return point.fractionalPositionOnLine(this.x, this.y, this.z, this.x + Math.cos(this.phi) * sinTheta,
                this.y + Math.sin(this.phi) * sinTheta, this.z + Math.cos(this.theta), null, false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double sinTheta = Math.sin(this.theta);
        return point.fractionalPositionOnLine(getX(), getY(), getZ(), getX() + Math.cos(this.phi) * sinTheta,
                getY() + Math.sin(this.phi) * sinTheta, getZ() + Math.cos(this.theta), false, false);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final Ray3d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other point may not be null");
        Throw.when(
                Double.isNaN(epsilonCoordinate) || epsilonCoordinate < 0 || Double.isNaN(epsilonRotation)
                        || epsilonRotation < 0,
                IllegalArgumentException.class, "epsilon values may not be negative and may not be NaN");
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
        if ((Math.abs(AngleUtil.normalizeAroundZero(this.phi - other.phi)) > epsilonRotation
                || Math.abs(AngleUtil.normalizeAroundZero(this.theta - other.theta)) > epsilonRotation)
                && (Math.abs(AngleUtil.normalizeAroundZero(Math.PI + this.phi - other.phi)) > epsilonRotation
                        || Math.abs(AngleUtil.normalizeAroundZero(Math.PI - this.theta - other.theta)) > epsilonRotation))
        {
            return false;
        }
        return true;
        // FIXME this method should return true if the same angle is approximated with inverted theta and phi off by PI
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
        return String.format(Locale.US, format, this.x, this.y, this.z, this.phi, this.theta);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.phi);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.theta);
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
        if (getClass() != obj.getClass())
            return false;
        Ray3d other = (Ray3d) obj;
        if (Double.doubleToLongBits(this.phi) != Double.doubleToLongBits(other.phi))
            return false;
        if (Double.doubleToLongBits(this.theta) != Double.doubleToLongBits(other.theta))
            return false;
        return true;
    }

}
