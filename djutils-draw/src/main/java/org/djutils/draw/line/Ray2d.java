package org.djutils.draw.line;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * Ray2d is a half-line in 2d; it has one end point with finite coordinates; the other end point is infinitely far away.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Ray2d extends DirectedPoint2d implements Drawable2d, Ray<Ray2d, DirectedPoint2d, Point2d>
{
    /**
     * Construct a new Ray2d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param dirZ the angle from the positive X axis direction in radians.
     * @throws ArithmeticException when <code>dirZ</code> is <code>NaN</code>
     */
    public Ray2d(final double x, final double y, final double dirZ)
    {
        super(x, y, dirZ);
    }

    /**
     * Construct a new Ray2d from x and y coordinates in a double[] and a direction.
     * @param xy the <code>x</code> and <code>y</code> coordinates of the finite end point in that order
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when <code>xy</code> is <code>null</code>
     * @throws ArithmeticException when <code>xy</code> contains <code>NaN</code>, or rotZ is <code>NaN</code>
     * @throws IllegalArgumentException when the length of <code>xy</code> is not 2, or <code>dirZ</code> is infinite
     */
    public Ray2d(final double[] xy, final double dirZ)
    {
        super(xy, dirZ);
    }

    /**
     * Construct a new Ray2d from an AWT Point2D and a direction.
     * @param point an AWT Point2D
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when any coordinate in <code>point</code> is <code>NaN</code>, or <code>rotZ</code> is
     *             <code>NaN</code>
     */
    public Ray2d(final Point2D point, final double dirZ)
    {
        super(point, dirZ);
    }

    /**
     * Construct a new Ray2d from a Point2d and a direction.
     * @param point the finite end point of the ray
     * @param dirZ the angle from the positive X axis direction in radians.
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when <code>dirZ</code> is <code>NaN</code>
     */
    public Ray2d(final Point2d point, final double dirZ)
    {
        super(point, dirZ);
    }

    /**
     * Construct a new Ray2d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param throughX the x coordinate of another point on the ray
     * @param throughY the y coordinate of another point on the ray
     * @throws IllegalArgumentException when <code>throughX == x</code> and <code>throughY ==
     *             y</code>
     * @throws ArithmeticException when any <code>throughX</code> or <code>throughY</code> is <code>NaN</code>
     */
    public Ray2d(final double x, final double y, final double throughX, final double throughY)
    {
        super(x, y, throughX, throughY);
    }

    /**
     * Construct a new Ray2d.
     * @param point the finite end point of the ray
     * @param throughX the x coordinate of another point on the ray
     * @param throughY the y coordinate of another point on the ray
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when any <code>throughX</code>, or <code>throughY</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>throughX == x</code> and <code>throughY ==
     *             y</code>
     */
    public Ray2d(final Point2d point, final double throughX, final double throughY)
    {
        super(point, throughX, throughY);
    }

    /**
     * Construct a new Ray2d.
     * @param x the x coordinate of the finite end point of the ray
     * @param y the y coordinate of the finite end point of the ray
     * @param throughPoint another point on the ray
     * @throws NullPointerException when <code>throughPoint</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>throughPoint</code> is exactly at (x, y)
     */
    public Ray2d(final double x, final double y, final Point2d throughPoint)
    {
        this(x, y, Throw.whenNull(throughPoint, "througPoint").x, throughPoint.y);
    }

    /**
     * Construct a new Ray2d.
     * @param point the finite end point of the ray
     * @param throughPoint another point on the ray
     * @throws NullPointerException when <code>throughPoint</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>throughPoint</code> is exactly at <code>(x,y)</code>
     */
    public Ray2d(final Point2d point, final Point2d throughPoint)
    {
        this(Throw.whenNull(point, "point").x, point.y, Throw.whenNull(throughPoint, "throughPoint").x, throughPoint.y);
    }

    /**
     * Construct a new Ray2d.
     * @param directedPoint point and direction of the new Ray2d
     */
    public Ray2d(final DirectedPoint2d directedPoint)
    {
        this(directedPoint, directedPoint.dirZ);
    }

    @Override
    public final double getDirZ()
    {
        return this.dirZ;
    }

    @Override
    public DirectedPoint2d getEndPoint()
    {
        return this;
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public Iterator<Point2d> iterator()
    {
        double cosDirZ = Math.cos(this.dirZ);
        double sinDirZ = Math.sin(this.dirZ);
        Point2d[] array = new Point2d[] {this, new Point2d(cosDirZ == 0 ? this.x : cosDirZ * Double.POSITIVE_INFINITY,
                sinDirZ == 0 ? this.y : sinDirZ * Double.POSITIVE_INFINITY)};
        return Arrays.stream(array).iterator();
    }

    @Override
    public Bounds2d getAbsoluteBounds()
    {
        double cosDirZ = Math.cos(this.dirZ);
        double sinDirZ = Math.sin(this.dirZ);
        return new Bounds2d(cosDirZ >= 0 ? this.x : Double.NEGATIVE_INFINITY, cosDirZ <= 0 ? this.x : Double.POSITIVE_INFINITY,
                sinDirZ >= 0 ? this.y : Double.NEGATIVE_INFINITY, sinDirZ <= 0 ? this.y : Double.POSITIVE_INFINITY);
    }

    @Override
    public Ray2d neg()
    {
        return new Ray2d(-this.x, -this.y, AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public Ray2d flip()
    {
        return new Ray2d(this.x, this.y, AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public Ray2d getLocationExtended(final double position)
    {
        Throw.whenNaN(position, "position");
        Throw.when(Double.isInfinite(position), IllegalArgumentException.class, "position must be finite");
        return new Ray2d(this.x + Math.cos(this.dirZ) * position, this.y + Math.sin(this.dirZ) * position, this.dirZ);
    }

    @Override
    public Point2d closestPointOnRay(final Point2d point)
    {
        Throw.whenNull(point, "point");
        double dX = Math.cos(this.dirZ);
        double dY = Math.sin(this.dirZ);
        return point.closestPointOnLine(this.x, this.y, this.x + dX, this.y + dY, true, false);
    }

    @Override
    public Point2d projectOrthogonal(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(this.x, this.y, this.x + Math.cos(this.dirZ), this.y + Math.sin(this.dirZ), null,
                false);
    }

    @Override
    public Point2d projectOrthogonalExtended(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(getX(), getY(), getX() + Math.cos(this.dirZ), getY() + Math.sin(this.dirZ), false,
                false);
    }

    @Override
    public double projectOrthogonalFractional(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return point.fractionalPositionOnLine(this.x, this.y, this.x + Math.cos(this.dirZ), this.y + Math.sin(this.dirZ), null,
                false);
    }

    @Override
    public double projectOrthogonalFractionalExtended(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return point.fractionalPositionOnLine(this.x, this.y, this.x + Math.cos(this.dirZ), this.y + Math.sin(this.dirZ), false,
                false);
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, dirZ=%2%s]", doNotIncludeClassName ? "" : "Ray2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.dirZ);
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
