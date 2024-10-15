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
    public final double phi;

    /**
     * Construct a new DirectedPoint2d with an x and y coordinate and a direction.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate or dirZ is NaN
     */
    public DirectedPoint2d(final double x, final double y, final double phi) throws IllegalArgumentException
    {
        super(x, y);
        Throw.when(Double.isNaN(phi), IllegalArgumentException.class, "phi must be a number (not NaN)");
        this.phi = phi;
    }

    /**
     * Construct a new DirectedPoint2d from an x and y coordinates in a double[] and a direction.
     * @param xy double[]; the <cite>x</cite> and <cite>y</cite> coordinates in that order
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2 or any value in xy is NaN or rotZ is NaN
     */
    public DirectedPoint2d(final double[] xy, final double phi) throws IllegalArgumentException
    {
        super(xy);
        Throw.when(Double.isNaN(phi), IllegalArgumentException.class, "phi must be a number (not NaN)");
        this.phi = phi;
    }

    /**
     * Construct a new DirectedPoint2d from an AWT Point2D and a direction.
     * @param point Point2D; an AWT Point2D
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when any coordinate in point is NaN, or rotZ is NaN
     */
    public DirectedPoint2d(final Point2D point, final double phi) throws IllegalArgumentException
    {
        super(point);
        Throw.when(Double.isNaN(phi), IllegalArgumentException.class, "phi must be a number (not NaN)");
        this.phi = phi;
    }

    /**
     * Construct a new DirectedPoint2d from a Point2d and a direction.
     * @param point Point2d; a point (with or without orientation)
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @throws IllegalArgumentException when rotZ is NaN
     */
    public DirectedPoint2d(final Point2d point, final double phi) throws IllegalArgumentException
    {
        this(point.x, point.y, phi);
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
     * Build the direction vector.
     * @param dX double; x difference
     * @param dY double; y difference
     * @return double; the computed value of phi
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
        this(Throw.whenNull(point, "point may not be null").x, point.y, throughX, throughY);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint2d neg()
    {
        return new DirectedPoint2d(-getX(), -getY(), AngleUtil.normalizeAroundZero(this.phi + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public double getPhi()
    {
        return this.phi;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends DirectedPoint2d> getPoints()
    {
        return Arrays.stream(new DirectedPoint2d[] {this}).iterator();
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
                String.format("%1$s[x=%2$s, y=%2$s, rot=%2$s]", doNotIncludeClassName ? "" : "DirectedPoint2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.phi);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final DirectedPoint2d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other point cannot be null");
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
        if (Math.abs(AngleUtil.normalizeAroundZero(this.phi - other.phi)) > epsilonRotation)
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
        result = prime * result + Objects.hash(this.phi);
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
        DirectedPoint2d other = (DirectedPoint2d) obj;
        return Double.doubleToLongBits(this.phi) == Double.doubleToLongBits(other.phi);
    }

}
