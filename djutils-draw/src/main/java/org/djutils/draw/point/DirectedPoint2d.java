package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import org.djutils.base.AngleUtil;
import org.djutils.draw.Directed2d;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * DirectedPoint2d.java.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint2d extends Point2d implements Directed2d<DirectedPoint2d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The counter-clockwise rotation around the point in radians. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Construct a new DirectedPoint2d with an x and y coordinate and a direction.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate or dirZ is NaN
     */
    public DirectedPoint2d(final double x, final double y, final double dirZ) throws IllegalArgumentException
    {
        super(x, y);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "dirZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct a new DirectedPoint2d from an x and y coordinates in a double[] and a direction.
     * @param xy double[]; the <cite>x</cite> and <cite>y</cite> coordinates in that order
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2 or any value in xy is NaN or rotZ is NaN
     */
    public DirectedPoint2d(final double[] xy, final double dirZ) throws IllegalArgumentException
    {
        super(xy);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "dirZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct a new DirectedPoint2d from an AWT Point2D and a direction.
     * @param point Point2D; an AWT Point2D
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate in point is NaN, or rotZ is NaN
     */
    public DirectedPoint2d(final Point2D point, final double dirZ) throws IllegalArgumentException
    {
        super(point);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "dirZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct a new DirectedPoint2d from a Point2d and a direction.
     * @param point Point2d; a point (with or without orientation)
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when rotZ is NaN
     */
    public DirectedPoint2d(final Point2d point, final double dirZ) throws IllegalArgumentException
    {
        this(point.x, point.y, dirZ);
    }

    /**
     * Construct a new DirectedPoint2d from two coordinates and the coordinates of a point that the direction goes through.
     * @param x double; the x coordinate of the of the new DirectedPoint
     * @param y double; the y coordinate of the of the new DirectedPoint
     * @param throughX double; the x-coordinate of a point that the direction goes through
     * @param throughY double; the y-coordinate of a point that the direction goes through
     * @throws DrawRuntimeException when <cite>throughX</cite> == <cite>x</cite> and <cite>throughY</cite> == <cite>y</cite>, or
     *             any through-value is NaN
     */
    public DirectedPoint2d(final double x, final double y, final double throughX, final double throughY)
            throws DrawRuntimeException
    {
        this(x, y, buildDirection(throughX - x, throughY - y));
    }

    /**
     * Build the direction.
     * @param dX double; x difference
     * @param dY double; y difference
     * @return double; the computed value of dirZ
     */
    private static double buildDirection(final double dX, final double dY)
    {
        Throw.when(0 == dX && 0 == dY, IllegalArgumentException.class, "Through point may not be equal to point");
        return Math.atan2(dY, dX);
    }

    /**
     * Construct a new DirectedPoint2d from a Point2d and the coordinates of a point that the direction goes through.
     * @param point Point2d; the point
     * @param throughX double; the x coordinate of a point that the direction goes through
     * @param throughY double; the y coordinate of a point that the direction goes through
     * @throws NullPointerException when point is null
     * @throws DrawRuntimeException when <cite>throughX</cite> == <cite>point.x</cite> and <cite>throughY</cite> ==
     *             <cite>point.y</cite>, or any through-value is NaN
     */
    public DirectedPoint2d(final Point2d point, final double throughX, final double throughY)
            throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(point, "point").x, point.y, throughX, throughY);
    }

    @Override
    public DirectedPoint2d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new DirectedPoint2d(this.x + dx, this.y + dy, this.dirZ);
    }

    @Override
    public DirectedPoint3d translate(final double dx, final double dy, final double z) throws IllegalArgumentException
    {
        return new DirectedPoint3d(this.x + dx, this.y + dy, z, 0, this.dirZ);
    }

    @Override
    public DirectedPoint2d scale(final double factor) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
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
    public DirectedPoint2d normalize()
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /**
     * Interpolate towards another DirectedPoint2d with a fraction. It is allowed for fraction to be less than zero or larger
     * than 1. In that case the interpolation turns into an extrapolation. DirZ is interpolated using the
     * AngleUtil.interpolateShortest method.
     * @param otherPoint DirectedPoint2d; the other point
     * @param fraction double; the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is
     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0;
     *            <code>this</code> Point is returned; if <code>fraction</code> is 1, the <code>otherPoint</code> is returned
     * @return DirectedPoint2d; a new OrientedPoint2d at the requested fraction
     * @throws NullPointerException when otherPoint is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    public DirectedPoint2d interpolate(final DirectedPoint2d otherPoint, final double fraction)
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
        return new DirectedPoint2d((1.0 - fraction) * this.x + fraction * otherPoint.x,
                (1.0 - fraction) * this.y + fraction * otherPoint.y,
                AngleUtil.interpolateShortest(this.dirZ, otherPoint.dirZ, fraction));
    }

    /**
     * Return a new DirectedPoint2d with an in-place rotation around the z-axis by the provided rotateZ. The resulting rotation
     * is normalized between -&pi; and &pi;.
     * @param rotateZ double; the rotation around the z-axis
     * @return DirectedPoint2d; a new point with the same coordinates and applied rotation
     * @throws IllegalArgumentException when deltaRotZ is NaN
     */
    public DirectedPoint2d rotate(final double rotateZ) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateZ), IllegalArgumentException.class, "deltaDirZ must be a number (not NaN)");
        return new DirectedPoint2d(this.x, this.y, AngleUtil.normalizeAroundZero(this.dirZ + rotateZ));
    }
    
    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    @Override
    public Iterator<? extends DirectedPoint2d> getPoints()
    {
        return Arrays.stream(new DirectedPoint2d[] {this}).iterator();
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

    @Override
    public boolean epsilonEquals(final DirectedPoint2d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other");
        Throw.when(epsilonCoordinate < 0 || epsilonRotation < 0, IllegalArgumentException.class,
                "epsilonCoordinate and epsilongRotation may not be negative");
        Throw.when(Double.isNaN(epsilonCoordinate) || Double.isNaN(epsilonRotation), IllegalArgumentException.class,
                "epsilonCoordinate and epsilongRotation may not be NaN");
        if (Math.abs(this.x - other.x) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.dirZ - other.dirZ)) > epsilonRotation)
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
