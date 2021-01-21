package org.djutils.draw.line;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Ray3d.java. A ray is a half-line; it has one end point with non-infinite coordinates; the other end point is infinitely far
 * away.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Ray3d extends Point3d implements Drawable3d, Ray<Ray3d>
{
    /** ... */
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
     * @throws DrawRuntimeException when throughX == x and throughY == y
     */
    public Ray3d(final double x, final double y, final double z, final double throughX, final double throughY,
            final double throughZ) throws DrawRuntimeException
    {
        super(x, y, z);
        Throw.when(throughX == x && throughY == y && throughZ == z, DrawRuntimeException.class,
                "the coordinates of the through points must differ from (x, y, z)");
        this.phi = Math.atan2(throughY - y, throughX - x);
        this.theta = Math.atan2(throughZ - z, Math.hypot(throughX - x, throughY - y));
    }

    /**
     * Construct a new Ray3d.
     * @param point Point3d; the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @param throughZ double; the z coordinate of another point on the ray
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when throughX == point.x and throughY == point.y
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

    /**
     * Retrieve the angle from the positive X axis direction in radians.
     * @return double; the angle from the positive X axis direction in radians
     */
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
    public Ray3d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position), DrawRuntimeException.class, "position must be finite");
        double sinTheta = Math.sin(this.theta);
        double dX = Math.cos(this.phi) * sinTheta;
        double dY = Math.sin(this.phi) * sinTheta;
        double dZ = Math.cos(this.theta);
        return new Ray3d(this.x + dX * position, this.y + dY * position, this.z + dZ * position, this.phi, this.theta);
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        double normalizedPhi = AngleUtil.normalizeAroundZero(this.phi);
        double normalizedTheta = AngleUtil.normalizeAroundZero(this.theta);
        boolean toPositiveX = Math.abs(normalizedPhi) <= Math.PI / 2; // Math.cos(Math.PI) is > 0 due to finite precision
        return new Bounds3d(toPositiveX ? this.x : Double.NEGATIVE_INFINITY, toPositiveX ? Double.POSITIVE_INFINITY : this.x,
                normalizedPhi >= 0 ? this.y : Double.NEGATIVE_INFINITY, normalizedPhi <= 0 ? this.y : Double.POSITIVE_INFINITY,
                normalizedTheta >= 0 ? this.z : Double.NEGATIVE_INFINITY,
                normalizedTheta <= 0 ? this.z : Double.POSITIVE_INFINITY);
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
        if (Math.abs(AngleUtil.normalizeAroundZero(this.phi - other.phi)) > epsilonRotation)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Ray3d [x=" + this.x + " y=" + this.y + " z=" + this.z + " phi=" + this.phi + " theta=" + this.theta + "]";
    }

}
