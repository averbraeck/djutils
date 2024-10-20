package org.djutils.draw.line;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Ray2d is a half-line; it has one end point with finite coordinates; the other end point is infinitely far away.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Ray2d extends DirectedPoint2d implements Drawable2d, Ray<Ray2d, DirectedPoint2d, Point2d>
{
    /** */
    private static final long serialVersionUID = 20210119L;

    /**
     * Construct a new Ray2d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param dirZ double; the angle from the positive X axis direction in radians.
     * @throws DrawRuntimeException when dirZ is NaN
     */
    public Ray2d(final double x, final double y, final double dirZ) throws DrawRuntimeException
    {
        super(x, y, dirZ);
    }

    /**
     * Construct a new Ray2d from x and y coordinates in a double[] and a direction.
     * @param xy double[]; the <cite>x</cite> and <cite>y</cite> coordinates of the finite end point in that order
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2 or any value in xy is NaN or rotZ is NaN
     */
    public Ray2d(final double[] xy, final double dirZ) throws IllegalArgumentException
    {
        super(xy, dirZ);
    }

    /**
     * Construct a new Ray2d from an AWT Point2D and a direction.
     * @param point Point2D; an AWT Point2D
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate in point is NaN, or rotZ is NaN
     */
    public Ray2d(final Point2D point, final double dirZ) throws IllegalArgumentException
    {
        super(point, dirZ);
    }

    /**
     * Construct a new Ray2d from a Point2d and a direction.
     * @param point Point2d; the finite end point of the ray
     * @param dirZ double; the angle from the positive X axis direction in radians.
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when dirZ is NaN
     */
    public Ray2d(final Point2d point, final double dirZ) throws NullPointerException, DrawRuntimeException
    {
        super(point, dirZ);
    }

    /**
     * Construct a new Ray2d.
     * @param x double; the x coordinate of the finite end point of the ray
     * @param y double; the y coordinate of the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @throws DrawRuntimeException when throughX == x and throughY == y, or any through-value is NaN
     */
    public Ray2d(final double x, final double y, final double throughX, final double throughY) throws DrawRuntimeException
    {
        super(x, y, throughX, throughY);
    }

    /**
     * Construct a new Ray2d.
     * @param point Point2d; the finite end point of the ray
     * @param throughX double; the x coordinate of another point on the ray
     * @param throughY double; the y coordinate of another point on the ray
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when throughX == point.x and throughY == point.y, or any through-value is NaN
     */
    public Ray2d(final Point2d point, final double throughX, final double throughY)
            throws NullPointerException, DrawRuntimeException
    {
        super(point, throughX, throughY);
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
        this(x, y, Throw.whenNull(throughPoint, "througPoint").x, throughPoint.y);
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
        this(Throw.whenNull(point, "point").x, point.y, Throw.whenNull(throughPoint, "throughPoint").x, throughPoint.y);
    }

    /** {@inheritDoc} */
    @Override
    public final double getDirZ()
    {
        return this.dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d getEndPoint()
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
    public Iterator<DirectedPoint2d> getPoints()
    {
        double cosDirZ = Math.cos(this.dirZ);
        double sinDirZ = Math.sin(this.dirZ);
        DirectedPoint2d[] array =
                new DirectedPoint2d[] {this, new DirectedPoint2d(cosDirZ == 0 ? this.x : cosDirZ * Double.POSITIVE_INFINITY,
                        sinDirZ == 0 ? this.y : sinDirZ * Double.POSITIVE_INFINITY, this.dirZ)};
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        double cosDirZ = Math.cos(this.dirZ);
        double sinDirZ = Math.sin(this.dirZ);
        return new Bounds2d(cosDirZ >= 0 ? this.x : Double.NEGATIVE_INFINITY, cosDirZ <= 0 ? this.x : Double.POSITIVE_INFINITY,
                sinDirZ >= 0 ? this.y : Double.NEGATIVE_INFINITY, sinDirZ <= 0 ? this.y : Double.POSITIVE_INFINITY);
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d neg()
    {
        return new Ray2d(-this.x, -this.y, AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d flip()
    {
        return new Ray2d(this.x, this.y, AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        return new Ray2d(this.x + Math.cos(this.dirZ) * position, this.y + Math.sin(this.dirZ) * position, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnRay(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        double dX = Math.cos(this.dirZ);
        double dY = Math.sin(this.dirZ);
        return point.closestPointOnLine(this.x, this.y, this.x + dX, this.y + dY, true, false);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonal(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(this.x, this.y, this.x + Math.cos(this.dirZ), this.y + Math.sin(this.dirZ), null,
                false);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonalExtended(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(getX(), getY(), getX() + Math.cos(this.dirZ), getY() + Math.sin(this.dirZ), false,
                false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractional(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        return point.fractionalPositionOnLine(this.x, this.y, this.x + Math.cos(this.dirZ), this.y + Math.sin(this.dirZ), null,
                false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point");
        return point.fractionalPositionOnLine(this.x, this.y, this.x + Math.cos(this.dirZ), this.y + Math.sin(this.dirZ), false,
                false);
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
        String format = String.format("%1$s[x=%2$s, y=%2$s, dirZ=%2%s]", doNotIncludeClassName ? "" : "Ray2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.dirZ);
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
