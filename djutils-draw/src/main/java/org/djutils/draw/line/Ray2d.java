package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Ray2d is a half-line; it has one end point with non-infinite coordinates; the other end point is infinitely far away.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Ray2d extends Point2d implements Drawable2d, Ray<Ray2d, Point2d>
{
    /** */
    private static final long serialVersionUID = 20210119L;

    /** Phi; the angle from the positive X axis direction in radians. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double phi;

    /**
     * Construct a new Ray2d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param phi double; the angle from the positive X axis direction in radians.
     * @throws DrawRuntimeException when phi is NaN
     */
    public Ray2d(final double x, final double y, final double phi) throws DrawRuntimeException
    {
        super(x, y);
        Throw.when(Double.isNaN(phi), DrawRuntimeException.class, "phi may not be NaN");
        this.phi = phi;
    }

    /**
     * Construct a new Ray2d.
     * @param point Point2d; the finite end point of the ray
     * @param phi double; the angle from the positive X axis direction in radians.
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when phi is NaN
     */
    public Ray2d(final Point2d point, final double phi) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(point, "point may not be null").x, point.y, phi);
    }

    /**
     * Construct a new Ray2d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @throws DrawRuntimeException when throughX == x and throughY == y
     */
    public Ray2d(final double x, final double y, final double throughX, final double throughY) throws DrawRuntimeException
    {
        super(x, y);
        Throw.when(throughX == x && throughY == y, DrawRuntimeException.class,
                "the coordinates of the through point must differ from (x, y)");
        this.phi = Math.atan2(throughY - y, throughX - x);
    }

    /**
     * Construct a new Ray2d.
     * @param point Point2d; the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when throughX == point.x and throughY == point.y
     */
    public Ray2d(final Point2d point, final double throughX, final double throughY)
            throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(point, "point may not be null").x, point.y, throughX, throughY);
    }

    /**
     * Construct a new Ray2d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param throughPoint Point2d; another point on the ray
     * @throws NullPointerException when throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at (x, y)
     */
    public Ray2d(final double x, final double y, final Point2d throughPoint) throws NullPointerException, DrawRuntimeException
    {
        this(x, y, Throw.whenNull(throughPoint, "througPoint may not be null").x, throughPoint.y);
    }

    /**
     * Construct a new Ray2d.
     * @param point Point2d; the finite end point of the ray
     * @param throughPoint Point2d; another point on the ray
     * @throws NullPointerException when point is null or throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at point
     */
    public Ray2d(final Point2d point, final Point2d throughPoint) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(point, "point may not be null").x, point.y,
                Throw.whenNull(throughPoint, "throughPoint may not be null").x, throughPoint.y);
    }

    /** {@inheritDoc} */
    @Override
    public final double getPhi()
    {
        return this.phi;
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getEndPoint()
    {
        return new Point2d(this.x, this.y);
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 2;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point2d> getPoints()
    {
        double cosPhi = Math.cos(this.phi);
        double sinPhi = Math.sin(this.phi);
        Point2d[] array = new Point2d[] { new Point2d(this.x, this.y),
                new Point2d(cosPhi == 0 ? this.x : cosPhi * Double.POSITIVE_INFINITY,
                        sinPhi == 0 ? this.y : sinPhi * Double.POSITIVE_INFINITY) };
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        double cosPhi = Math.cos(this.phi);
        double sinPhi = Math.sin(this.phi);
        return new Bounds2d(cosPhi >= 0 ? this.x : Double.NEGATIVE_INFINITY, cosPhi <= 0 ? this.x : Double.POSITIVE_INFINITY,
                sinPhi >= 0 ? this.y : Double.NEGATIVE_INFINITY, sinPhi <= 0 ? this.y : Double.POSITIVE_INFINITY);
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d neg()
    {
        return new Ray2d(-this.x, -this.y, this.phi + Math.PI);
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d flip()
    {
        return new Ray2d(this.x, this.y, this.phi + Math.PI);
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        return new Ray2d(this.x + Math.cos(this.phi) * position, this.y + Math.sin(this.phi) * position, this.phi);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnRay(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        double dX = Math.cos(this.phi);
        double dY = Math.sin(this.phi);
        return point.closestPointOnLine(this.x, this.y, this.x + dX, this.y + dY, true, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonal(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.closestPointOnLine(this.x, this.y, this.x + Math.cos(this.phi), this.y + Math.sin(this.phi), null, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonalExtended(final Point2d point)
    {
        Throw.whenNull(point, "point may not be null");
        return point.closestPointOnLine(getX(), getY(), getX() + Math.cos(this.phi), getY() + Math.sin(this.phi), false, false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractional(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.fractionalPositionOnLine(this.x, this.y, this.x + Math.cos(this.phi), this.y + Math.sin(this.phi), null,
                false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.fractionalPositionOnLine(this.x, this.y, this.x + Math.cos(this.phi), this.y + Math.sin(this.phi), false,
                false);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final Ray2d other, final double epsilonCoordinate, final double epsilonDirection)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other point may not be null");
        Throw.when(
                Double.isNaN(epsilonCoordinate) || epsilonCoordinate < 0 || Double.isNaN(epsilonDirection)
                        || epsilonDirection < 0,
                IllegalArgumentException.class, "epsilon values may not be negative and may not be NaN");
        if (Math.abs(this.x - other.x) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.phi - other.phi)) > epsilonDirection)
        {
            return false;
        }
        return true;
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
        String format = String.format("%1$s[x=%2$s, y=%2$s, phi=%2%s]", doNotIncludeClassName ? "" : "Ray2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.phi);
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
        Ray2d other = (Ray2d) obj;
        if (Double.doubleToLongBits(this.phi) != Double.doubleToLongBits(other.phi))
            return false;
        return true;
    }

}
