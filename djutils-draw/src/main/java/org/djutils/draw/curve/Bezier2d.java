package org.djutils.draw.curve;

import java.util.Arrays;

import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Continuous definition of a B&eacute;zier curve in 2d. This class is simply a helper class for (and a super of)
 * {@code BezierCubic2d}, which uses this class to determine curvature, offset lines, etc.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://pomax.github.io/bezierinfo/">B&eacute;zier info</a>
 */
public class Bezier2d implements Curve2d
{

    /** The x-coordinates of the points of this B&eacute;zier. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final double[] x;

    /** The y-coordinates of the points of this B&eacute;zier. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final double[] y;

    /**
     * Create a B&eacute;zier curve of any order.
     * @param points Point2d... shape points that define the B&eacute;zier curve
     * @throws NullPointerException when <code>points</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when the length of <code>points</code> is less than <code>2</code>
     */
    public Bezier2d(final Point2d... points)
    {
        Throw.when(points.length < 2, IllegalArgumentException.class, "minimum number of points is 2");
        this.x = new double[points.length];
        this.y = new double[points.length];
        int index = 0;
        for (Point2d point : points)
        {
            Throw.whenNull(point, "One of the points is null");
            this.x[index] = point.x;
            this.y[index] = point.y;
            index++;
        }
    }

    /**
     * Create a B&eacute;zier curve of any order.
     * @param x the x-coordinates of the points that define the B&eacute;zier curve
     * @param y the y-coordinates of the points that define the B&eacute;zier curve
     * @throws NullPointerException when <code>x</code>, or <code>y</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>x</code> array is not equal to the length of the
     *             <code>y</code> array, or less than <code>2</code>
     */
    public Bezier2d(final double[] x, final double[] y)
    {
        this(true, x, y);
    }

    /**
     * Construct a B&eacute;zier curve of any order, optionally checking the lengths of the provided arrays.
     * @param checkLengths if <code>true</code>; check the lengths of the <code>x</code> and <code>y</code> arrays; if
     *            <code>false</code>; do not check those lengths
     * @param x the x-coordinates of the points that define the B&eacute;zier curve
     * @param y the y-coordinates of the points that define the B&eacute;zier curve
     * @throws NullPointerException when <code>x</code>, or <code>y</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of <code>x</code> is not equal to the length of <code>y</code>, or less
     *             than <code>2</code> and <code>checkLengths</code> is <code>true</code>
     */
    private Bezier2d(final boolean checkLengths, final double[] x, final double[] y)
    {
        if (checkLengths)
        {
            Throw.when(x.length < 2, IllegalArgumentException.class, "minimum number of points is 2");
            Throw.when(x.length != y.length, IllegalArgumentException.class,
                    "length of x-array must be same as length of y-array");
        }
        this.x = Arrays.copyOf(x, x.length);
        this.y = Arrays.copyOf(y, y.length);
    }

    /**
     * Returns the derivative for a B&eacute;zier, which is a B&eacute;zier of 1 order lower.
     * @return derivative B&eacute;zier
     * @throws IllegalStateException when the order of <code>this Bezier2d</code> is <code>1</code>
     */
    public Bezier2d derivative()
    {
        return new Bezier2d(false, Bezier.derivative(this.x), Bezier.derivative(this.y));
    }

    /**
     * Return the number of points (or x-y pairs) that this B&eacute;zier curve is based on.
     * @return the number of points (or x-y pairs) that this B&eacute;zier curve is based on
     */
    public int size()
    {
        return this.x.length;
    }

    /**
     * Returns the estimated path length of this B&eacute;zier curve using the method of numerical approach of Legendre-Gauss,
     * which is quite accurate.
     * @return estimated length.
     */
    private double length()
    {
        double len = 0.0;
        Bezier2d derivativeBezier = derivative();
        for (int i = 0; i < Bezier.T.length; i++)
        {
            double t = 0.5 * Bezier.T[i] + 0.5;
            Point2d p = derivativeBezier.getPoint(t);
            len += Bezier.C[i] * Math.hypot(p.x, p.y);
        }
        len *= 0.5;
        return len;
    }

    /**
     * Retrieve the x-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i the index
     * @return the x-coordinate of the i'th point of this B&eacute;zier curve
     * @throws IndexOutOfBoundsException when <code>i &lt; 0</code>, or <code>i &ge; size()</code>
     */
    public double getX(final int i)
    {
        return this.x[i];
    }

    /**
     * Retrieve the y-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i the index
     * @return the y-coordinate of the i'th point of this B&eacute;zier curve
     * @throws IndexOutOfBoundsException when <code>i &lt; 0</code>, or <code>i &ge; size()</code>
     */
    public double getY(final int i)
    {
        return this.y[i];
    }

    @Override
    public Point2d getPoint(final double t)
    {
        return new Point2d(Bezier.Bn(t, this.x), Bezier.Bn(t, this.y));
    }

    /** Cache the result of the getLength method. */
    private double cachedLength = -1;

    @Override
    public double getLength()
    {
        if (this.cachedLength < 0)
        {
            this.cachedLength = length();
        }
        return this.cachedLength;
    }

    /**
     * Returns the curvature at the given t value.
     * @param t t value, moving from 0 to 1 along the B&eacute;zier.
     * @return double curvature at the given t value.
     */
    public double curvature(final double t)
    {
        Bezier2d derivativeBezier = derivative();
        Point2d d = derivativeBezier.getPoint(t);
        double denominator = Math.pow(d.x * d.x + d.y * d.y, 3.0 / 2.0);
        if (denominator == 0.0)
        {
            return Double.POSITIVE_INFINITY;
        }
        Point2d dd = derivativeBezier.derivative().getPoint(t);
        double numerator = d.x * dd.y - dd.x * d.y;
        return numerator / denominator;
    }

    @Override
    public PolyLine2d toPolyLine(final Flattener2d flattener)
    {
        return flattener.flatten(this);
    }

    // If toString is regenerated, take care to remove cashedLength from the result.
    @Override
    public String toString()
    {
        return "Bezier2d [x=" + Arrays.toString(this.x) + ", y=" + Arrays.toString(this.y) + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
        return result;
    }

    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bezier2d other = (Bezier2d) obj;
        return Arrays.equals(this.x, other.x) && Arrays.equals(this.y, other.y);
    }

}
