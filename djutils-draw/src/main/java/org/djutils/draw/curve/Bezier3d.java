package org.djutils.draw.curve;

import java.util.Arrays;

import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Continuous definition of a B&eacute;zier curve in 3d. Note that this class does not implement {@code ContinuousLine}. This
 * class is simply a helper class for (and a super of) {@code ContinuousBezierCubic}, which uses this class to determine
 * curvature, offset lines, etc.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://pomax.github.io/bezierinfo/">B&eacute;zier info</a>
 */
public class Bezier3d implements Flattable3d
{
    /** The x-coordinates of the points of this B&eacute;zier. */
    private final double[] x;

    /** The y-coordinates of the points of this B&eacute;zier. */
    private final double[] y;

    /** The z-coordinates of the points of this B&eacute;zier. */
    private final double[] z;

    /**
     * Create a continuous B&eacute;zier curve of any order.
     * @param points Point2d... shape points that define the B&eacute;zier curve
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
            Throw.whenNull(point, "One of the points is null.");
            this.x[index] = point.x;
            this.y[index] = point.y;
            this.z[index] = point.z;
            index++;
        }
    }

    /**
     * Create a continuous B&eacute;zier curve of any order.
     * @param x double[]; the x-coordinates of the points that define the B&eacute;zier curve
     * @param y double[]; the y-coordinates of the points that define the B&eacute;zier curve
     * @param z double[]; the z-coordinates of the points that define the B&eacute;zier curve
     */
    public Bezier3d(final double[] x, final double[] y, final double[] z)
    {
        this(true, x, y, z);
    }

    /**
     * Construct a continuous B&eacute;zier curve of any order, optionally checking the lengths of the provided arrays.
     * @param checkLengths boolean; if true; check the lengths of the <cite>x</cite> and <cite>y</cite> arrays; if false; do not
     *            check those lengths
     * @param x double[]; the x-coordinates of the points that define the B&eacute;zier curve
     * @param y double[]; the y-coordinates of the points that define the B&eacute;zier curve
     * @param z double[]; the z-coordinates of the points that define the B&eacute;zier curve
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
     */
    public Bezier3d derivative()
    {
        Throw.when(this.x.length < 2, IllegalStateException.class,
                "Cannot make derivative of B&eacute;zier with fewer than 2 points");
        int n = this.x.length - 1;
        double[] dx = new double[n];
        double[] dy = new double[n];
        double[] dz = new double[n];
        for (int i = 0; i < n; i++)
        {
            dx[i] = n * this.x[i + 1] - this.x[i];
            dy[i] = n * this.y[i + 1] - this.y[i];
            dz[i] = n * this.z[i + 1] - this.z[i];
        }
        return new Bezier3d(dx, dy, dz);
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
     * Returns the estimated length using the method of numerical approach of Legendre-Gauss, which is quite accurate.
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
     */
    public double getX(final int i)
    {
        return this.x[i];
    }

    /**
     * Retrieve the y-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i int; the index
     * @return double; the y-coordinate of the i'th point of this B&eacute;zier curve
     */
    public double getY(final int i)
    {
        return this.y[i];
    }

    /**
     * Retrieve the z-coordinate of the i'th point of this B&eacute;zier curve.
     * @param i int; the index
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

    @Override
    public double getLength()
    {
        return this.length();
    }

    @Override
    public PolyLine3d toPolyLine(final Flattener3d flattener)
    {
        return flattener.flatten(this);
    }

    @Override
    public String toString()
    {
        return "ContinuousBezier [x=" + Arrays.toString(this.x) + ", y=" + Arrays.toString(this.y) + "]";
    }

}
