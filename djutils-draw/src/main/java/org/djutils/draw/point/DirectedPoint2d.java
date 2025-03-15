package org.djutils.draw.point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import org.djutils.draw.Directed;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * A DirectedPoint2d is a Point2d that additionally carries a direction in 2d-space (dirZ). This is <b>not</b> the direction
 * that the point is when viewed from the origin (0,0).
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class DirectedPoint2d extends Point2d implements Directed
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The counter-clockwise rotation around the point in radians. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Construct a new DirectedPoint2d with an x and y coordinate and a direction.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate or <code>dirZ</code> is <code>NaN</code>
     */
    public DirectedPoint2d(final double x, final double y, final double dirZ)
    {
        super(x, y);
        Throw.whenNaN(dirZ, "dirZ");
        this.dirZ = dirZ;
    }

    /**
     * Construct a new DirectedPoint2d from an x and y coordinates in a double[] and a direction.
     * @param xy the <code>x</code> and <code>y</code> coordinates in that order
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when <code>xy</code> is <code>null</code>
     * @throws ArithmeticException when any value in <code>xy</code> is <code>NaN</code> or <code>rotZ</code> is
     *             <code>NaN</code>
     * @throws IllegalArgumentException when the length of <code>xy</code> is not 2
     */
    public DirectedPoint2d(final double[] xy, final double dirZ)
    {
        super(xy);
        Throw.whenNaN(dirZ, "dirZ");
        this.dirZ = dirZ;
    }

    /**
     * Construct a new DirectedPoint2d from an AWT Point2D and a direction.
     * @param point java.awt.geom.Point2D
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when <code>rotZ</code> is <code>NaN</code>
     */
    public DirectedPoint2d(final java.awt.geom.Point2D point, final double dirZ)
    {
        super(point);
        Throw.whenNaN(dirZ, "dirZ");
        this.dirZ = dirZ;
    }

    /**
     * Construct a new DirectedPoint2d from a Point2d and a direction.
     * @param point a point (with or without orientation)
     * @param dirZ the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when <code>rotZ</code> is <code>NaN</code>
     */
    public DirectedPoint2d(final Point2d point, final double dirZ)
    {
        this(point.x, point.y, dirZ);
    }

    /**
     * Construct a new DirectedPoint2d from two coordinates and the coordinates of a point that the direction goes through.
     * @param x the x coordinate of the of the new DirectedPoint
     * @param y the y coordinate of the of the new DirectedPoint
     * @param throughX the x-coordinate of a point that the direction goes through
     * @param throughY the y-coordinate of a point that the direction goes through
     * @throws ArithmeticException when <code>throughX</code>, or <code>throughY</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>throughX == x</code> and <code>throughY == y</code>
     */
    public DirectedPoint2d(final double x, final double y, final double throughX, final double throughY)
    {
        this(x, y, buildDirection(throughX - x, throughY - y));
    }

    /**
     * Build the direction.
     * @param dX x difference
     * @param dY y difference
     * @return the computed value of dirZ
     * @throws IllegalArgumentException when <code>dX == 0.0</code> and <code>dY == 0.0</code>
     */
    private static double buildDirection(final double dX, final double dY)
    {
        Throw.when(0 == dX && 0 == dY, IllegalArgumentException.class, "Through point may not be equal to point");
        return Math.atan2(dY, dX);
    }

    /**
     * Construct a new DirectedPoint2d from a Point2d and a point that the direction goes through.
     * @param point the point
     * @param throughPoint the point that the direction goes through
     * @throws NullPointerException when <code>point</code> is <code>null</code>, or <code>throughPoint</code> ==
     *             <code>null</code>
     * @throws IllegalArgumentException when <code>throughX == point.x</code> and <code>throughY ==
     *             point.y</code>
     */
    public DirectedPoint2d(final Point2d point, final Point2d throughPoint)
    {
        this(Throw.whenNull(point, "point").x, point.y, Throw.whenNull(throughPoint, "throughPoint").x, throughPoint.y);
    }

    /**
     * Construct a new DirectedPoint2d from a Point2d and the coordinates of a point that the direction goes through.
     * @param point the point
     * @param throughX the x coordinate of a point that the direction goes through
     * @param throughY the y coordinate of a point that the direction goes through
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when <code>throughX</code>, or <code>throughY</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>throughX == point.x</code> and <code>throughY ==
     *             point.y</code>
     */
    public DirectedPoint2d(final Point2d point, final double throughX, final double throughY)
    {
        this(Throw.whenNull(point, "point").x, point.y, throughX, throughY);
    }

    @Override
    public DirectedPoint2d translate(final double dX, final double dY)
    {
        return new DirectedPoint2d(this.x + dX, this.y + dY, this.dirZ);
    }

    @Override
    public DirectedPoint3d translate(final double dX, final double dY, final double z)
            throws ArithmeticException, IllegalArgumentException
    {
        return new DirectedPoint3d(this.x + dX, this.y + dY, z, 0, this.dirZ);
    }

    @Override
    public DirectedPoint2d scale(final double factor) throws IllegalArgumentException
    {
        Throw.whenNaN(factor, "factor");
        return new DirectedPoint2d(this.x * factor, this.y * factor, this.dirZ);
    }

    @Override
    public DirectedPoint2d neg()
    {
        return new DirectedPoint2d(-this.x, -this.y, AngleUtil.normalizeAroundZero(this.dirZ + Math.PI));
    }

    @Override
    public DirectedPoint2d abs()
    {
        return new DirectedPoint2d(Math.abs(this.x), Math.abs(this.y), this.dirZ);
    }

    @Override
    public DirectedPoint2d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /**
     * Interpolate towards another DirectedPoint2d with a fraction. It is allowed for fraction to be less than zero or larger
     * than 1. In that case the interpolation turns into an extrapolation. DirZ is interpolated using the
     * AngleUtil.interpolateShortest method.
     * @param otherPoint the other point
     * @param fraction the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is between 0
     *            and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0; <code>this</code>
     *            Point is returned; if <code>fraction</code> is 1, the <code>otherPoint</code> is returned
     * @return a new OrientedPoint2d at the requested fraction
     * @throws NullPointerException when <code>otherPoint</code> is <code>null</code>
     * @throws ArithmeticException when <code>fraction</code> is <code>NaN</code>
     */
    public DirectedPoint2d interpolate(final DirectedPoint2d otherPoint, final double fraction)
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
        return new DirectedPoint2d((1.0 - fraction) * this.x + fraction * otherPoint.x,
                (1.0 - fraction) * this.y + fraction * otherPoint.y,
                AngleUtil.interpolateShortest(this.dirZ, otherPoint.dirZ, fraction));
    }

    /**
     * Return a new DirectedPoint2d with an in-place rotation around the z-axis by the provided rotateZ. The resulting rotation
     * is normalized between -&pi; and &pi;.
     * @param rotateZ the rotation around the z-axis
     * @return a new point with the same coordinates and applied rotation
     * @throws ArithmeticException when <code>rotateZ</code> is <code>NaN</code>
     */
    public DirectedPoint2d rotate(final double rotateZ)
    {
        Throw.whenNaN(rotateZ, "rotateZ");
        return new DirectedPoint2d(this.x, this.y, AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }

    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    @Override
    public Iterator<Point2d> iterator()
    {
        return Arrays.stream(new Point2d[] {this}).iterator();
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format =
                String.format("%1$s[x=%2$s, y=%2$s, dirZ=%2$s]", doNotIncludeClassName ? "" : "DirectedPoint2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.dirZ);
    }

    /**
     * Compare this Directed with another Directed with specified tolerances in the coordinates and the angles.
     * @param other the Directed to compare to
     * @param epsilonCoordinate the upper bound of difference for one of the coordinates; use Double.POSITIVE_INFINITY if you do
     *            not want to check the coordinates
     * @param epsilonDirection the upper bound of difference for the direction(s); use Double.POSITIVE_INFINITY if you do not
     *            want to check the angles
     * @return boolean;<code>true</code> if <code>x</code> and <code>y</code> are less than <code>epsilonCoordinate</code>
     *         apart, and <code>rotZ</code> is less than <code>epsilonDirection</code> apart, otherwise <code>false</code>
     * @throws NullPointerException when <code>other</code> is <code>null</code>
     * @throws ArithmeticException when <code>epsilonCoordinate</code> or <code>epsilonDirection</code> is <code>NaN</code>
     * @throws IllegalArgumentException <code>epsilonCoordinate</code> or <code>epsilonDirection</code> is <code>negative</code>
     */
    public boolean epsilonEquals(final DirectedPoint2d other, final double epsilonCoordinate, final double epsilonDirection)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other");
        Throw.whenNaN(epsilonCoordinate, "epsilonCoordinate");
        Throw.whenNaN(epsilonDirection, "epsilonDirection");
        Throw.when(epsilonCoordinate < 0 || epsilonDirection < 0, IllegalArgumentException.class,
                "epsilonCoordinate and epsilonRotation may not be negative");
        if (Math.abs(this.x - other.x) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirZ - other.dirZ)) > epsilonDirection)
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
        result = prime * result + Objects.hash(this.dirZ);
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
        DirectedPoint2d other = (DirectedPoint2d) obj;
        return Double.doubleToLongBits(this.dirZ) == Double.doubleToLongBits(other.dirZ);
    }

}
