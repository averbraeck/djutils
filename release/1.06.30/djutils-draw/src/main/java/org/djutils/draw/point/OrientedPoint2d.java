package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Oriented2d;
import org.djutils.exceptions.Throw;

/**
 * The OrientedPoint2d is a point in a 2-dimensional space with an orientation vector, which is specified in terms of its
 * counter-clockwise rotation around the point in radians.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class OrientedPoint2d extends Point2d implements Oriented2d<OrientedPoint2d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The counter-clockwise rotation around the point in radians. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Construct an oriented point with an x and y coordinate and a direction equal to 0.0.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @throws IllegalArgumentException when any coordinate is NaN
     */
    public OrientedPoint2d(final double x, final double y) throws IllegalArgumentException
    {
        super(x, y);
        this.dirZ = 0;
    }

    /**
     * Construct an oriented point with an x and y coordinate and a direction.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate or dirZ is NaN
     */
    public OrientedPoint2d(final double x, final double y, final double dirZ) throws IllegalArgumentException
    {
        super(x, y);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct an oriented point with an x and y coordinate and a direction.
     * @param xy double[]; the x and y coordinate
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2 or any value in xy is NaN or rotZ is NaN
     */
    public OrientedPoint2d(final double[] xy, final double dirZ) throws IllegalArgumentException
    {
        super(xy);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct an oriented point from an AWT Point2D and a direction.
     * @param point Point2D; an AWT Point2D
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate in point is NaN, or rotZ is NaN
     */
    public OrientedPoint2d(final Point2D point, final double dirZ) throws IllegalArgumentException
    {
        super(point);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /**
     * Construct an oriented point from a Point2d and a direction.
     * @param point Point2d; a point (with or without orientation)
     * @param dirZ double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when rotZ is NaN
     */
    public OrientedPoint2d(final Point2d point, final double dirZ) throws IllegalArgumentException
    {
        super(point.x, point.y);
        Throw.when(Double.isNaN(dirZ), IllegalArgumentException.class, "rotZ must be a number (not NaN)");
        this.dirZ = dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint2d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new OrientedPoint2d(getX() + dx, getY() + dy, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint3d translate(final double dx, final double dy, final double z) throws IllegalArgumentException
    {
        return new OrientedPoint3d(getX() + dx, getY() + dy, z, 0, 0, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint2d scale(final double factor) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new OrientedPoint2d(getX() * factor, getY() * factor, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint2d neg()
    {
        return new OrientedPoint2d(-getX(), -getY(), this.dirZ + Math.PI);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint2d abs()
    {
        return new OrientedPoint2d(Math.abs(getX()), Math.abs(getY()), this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public OrientedPoint2d normalize()
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /**
     * Interpolate towards another Point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation. DirZ is interpolated using the AngleUtil.interpolateShortest
     * method.
     * @param otherPoint OrientedPoint2d; the other point
     * @param fraction double; the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is
     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0;
     *            <code>this</code> Point is returned; if <code>fraction</code> is 1, the other <code>point</code> is returned
     * @return OrientedPoint2d; a new OrientedPoint2d at the requested fraction
     * @throws NullPointerException when otherPoint is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    public OrientedPoint2d interpolate(final OrientedPoint2d otherPoint, final double fraction)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new OrientedPoint2d((1.0 - fraction) * getX() + fraction * otherPoint.x,
                (1.0 - fraction) * getY() + fraction * otherPoint.y,
                AngleUtil.interpolateShortest(getDirZ(), otherPoint.getDirZ(), fraction));
    }

    /**
     * Return a new OrientedPoint2d with an in-place rotation around the z-axis by the provided delta. The resulting rotation is
     * normalized between -&pi; and &pi;.
     * @param rotateZ double; the rotation around the z-axis
     * @return OrientedPoint; a new point with the same coordinates and applied rotation
     * @throws IllegalArgumentException when deltaRotZ is NaN
     */
    public OrientedPoint2d rotate(final double rotateZ) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(rotateZ), IllegalArgumentException.class, "deltaDirZ must be a number (not NaN)");
        return new OrientedPoint2d(getX(), getY(), AngleUtil.normalizeAroundZero(getDirZ() + rotateZ));
    }

    /** {@inheritDoc} */
    @Override
    public double getDirZ()
    {
        return this.dirZ;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends OrientedPoint2d> getPoints()
    {
        return Arrays.stream(new OrientedPoint2d[] { this }).iterator();
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
        String format =
                String.format("%1$s[x=%2$s, y=%2$s, rot=%2$s]", doNotIncludeClassName ? "" : "OrientedPoint2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.dirZ);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final OrientedPoint2d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other point cannot be null");
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

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.dirZ);
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
        OrientedPoint2d other = (OrientedPoint2d) obj;
        if (Double.doubleToLongBits(this.dirZ) != Double.doubleToLongBits(other.dirZ))
            return false;
        return true;
    }

}
