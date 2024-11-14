package org.djutils.draw.curve;

import java.util.Arrays;

import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Continuous definition of a B&eacute;zier curve in 3d. This class is simply a helper class for (and a super of)
 * {@code BezierCubic3d}, which uses this class to determine curvature, offset lines, etc.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://pomax.github.io/bezierinfo/">B&eacute;zier info</a>
 */
public class Bezier3d implements Curve3d
{
    /** The x-coordinates of the points of this B&eacute;zier. */
    private final double[] x;

    /** The y-coordinates of the points of this B&eacute;zier. */
    private final double[] y;

    /** The z-coordinates of the points of this B&eacute;zier. */
    private final double[] z;

    /**
     * Create a B&eacute;zier curve of any order.
     * @param points Point2d... shape points that define the B&eacute;zier curve
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>points</code> has &lt; 2 elements
     */
    public Bezier3d(final Point3d... points)
    {
        Throw.when(points.length < 2, IllegalArgumentException.class, "minimum number of points is 2");
        this.x = new double[points.length];
        this.y = new double[points.length];
        this.z = new double[points.length];
        int index = 0;
        for (Point3d point : points)
        {
            Throw.whenNull(point, "One of the points is null");
            this.x[index] = point.x;
            this.y[index] = point.y;
            this.z[index] = point.z;
            index++;
        }
    }

    /**
     * Create a B&eacute;zier curve of any order.
     * @param x double[]; the x-coordinates of the points that define the B&eacute;zier curve
     * @param y double[]; the y-coordinates of the points that define the B&eacute;zier curve
     * @param z double[]; the z-coordinates of the points that define the B&eacute;zier curve
     * @throws NullPointerException when <code>x</code>, <code>y</code>, or <code>z</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>x</code> array is not equal to the length of the
     *             <code>y</code> array, or not equal to the length of the <code>z</code> array, or less than <code>2</code>
     */
    public Bezier3d(final double[] x, final double[] y, final double[] z)
    {
        this(true, x, y, z);
    }

    /**
     * Construct a B&eacute;zier curve of any order, optionally checking the lengths of the provided arrays.
     * @param checkLengths boolean; if <code>true</code>; check the lengths of the <code>x</code> and <code>y</code> arrays; if
     *            <code>false</code>; do not check those lengths
     * @param x double[]; the x-coordinates of the points that define the B&eacute;zier curve
     * @param y double[]; the y-coordinates of the points that define the B&eacute;zier curve
     * @param z double[]; the z-coordinates of the points that define the B&eacute;zier curve
     * @throws NullPointerException when <code>x</code>, <code>y</code>, or <code>z</code> is <code>null</code>
     * @throws IllegalArgumentException when the length of the <code>x</code> array is not equal to the length of the
     *             <code>y</code> array, or not equal to the length of the <code>z</code> array, or less than <code>2</code> and
     *             <code>checkLengths</code> is <code>true</code>
     */
    private Bezier3d(final boolean checkLengths, final double[] x, final double[] y, final double[] z)
    {
        if (checkLengths)
        {
            Throw.when(x.length < 1, IllegalArgumentException.class, "minimum number of points is 1");
            Throw.when(x.length != y.length || x.length != z.length, IllegalArgumentException.class,
                    "length of x-array must be same as length of y-array and length of z-array");
        }
        this.x = Arrays.copyOf(x, x.length);
        this.y = Arrays.copyOf(y, y.length);
        this.z = Arrays.copyOf(z, z.length);
    }

    /**
     * Returns the derivative for a B&eacute;zier, which is a B&eacute;zier of 1 order lower.
     * @return derivative B&eacute;zier
     * @throws IllegalStateException when the order of <code>this Bezier2d</code> is <code>1</code>
     */
    public Bezier3d derivative()
    {
        return new Bezier3d(false, Bezier.derivative(this.x), Bezier.derivative(this.y), Bezier.derivative(this.z));
    }

    /**
     * Return the number of points (or x-y pairs) that this B&eacute;zier curve is based on.
     * @return int; the number of points (or x-y pairs) that this B&eacute;zier curve is based on
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
    public double length()
    {
        double len = 0.0;
        Bezier3d derivativeBezier = derivative();
        for (int i = 0; i < Bezier.T.length; i++)
        {
            double t = 0.5 * Bezier.T[i] + 0.5;
            Point3d p = derivativeBezier.getPoint(t);
            len += Bezier.C[i] * Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
        }
        len *= 0.5;
        return len;
    }

    /**
     * Retrieve the x-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i int; the index
     * @return double; the x-coordinate of the i'th point of this B&eacute;zier curve
     * @throws IndexOutOfBoundsException when <code>i &lt; 0</code>, or <code>i &ge; size()</code>
     */
    public double getX(final int i)
    {
        return this.x[i];
    }

    /**
     * Retrieve the y-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i int; the index
     * @return double; the y-coordinate of the i'th point of this B&eacute;zier curve
     * @throws IndexOutOfBoundsException when <code>i &lt; 0</code>, or <code>i &ge; size()</code>
     */
    public double getY(final int i)
    {
        return this.y[i];
    }

    /**
     * Retrieve the z-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i int; the index
     * @throws IndexOutOfBoundsException when <code>i &lt; 0</code>, or <code>i &ge; size()</code>
     * @return double; the z-coordinate of the i'th point of this B&eacute;zier curve
     */
    public double getZ(final int i)
    {
        return this.z[i];
    }

    @Override
    public Point3d getPoint(final double t)
    {
        return new Point3d(Bezier.Bn(t, this.x), Bezier.Bn(t, this.y), Bezier.Bn(t, this.z));
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

    @Override
    public PolyLine3d toPolyLine(final Flattener3d flattener)
    {
        return flattener.flatten(this);
    }

    @Override
    public String toString()
    {
        return "Bezier3d [x=" + Arrays.toString(this.x) + ", y=" + Arrays.toString(this.y) + ", z=" + Arrays.toString(this.z)
                + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
        result = prime * result + Arrays.hashCode(this.z);
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
        Bezier3d other = (Bezier3d) obj;
        return Arrays.equals(this.x, other.x) && Arrays.equals(this.y, other.y) && Arrays.equals(this.z, other.z);
    }

}
