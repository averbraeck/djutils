package org.djutils.draw.curve;

import org.djutils.draw.line.Ray3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Continuous definition of a cubic B&eacute;zier curves in 3d. This extends from the more general {@code Bezier} as certain
 * methods are applied to calculate e.g. the roots, that are specific to cubic B&eacute;zier curves. With such information this
 * class can also specify information to be a {@code Curve}.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://pomax.github.io/bezierinfo/">B&eacute;zier info</a>
 */
public class BezierCubic3d extends Bezier3d implements Curve3d
{
    /** Start point with direction. */
    private final DirectedPoint3d startPoint;

    /** End point with direction. */
    private final DirectedPoint3d endPoint;

    /** Length. */
    private final double length;

    /**
     * Create a cubic B&eacute;zier curve.
     * @param start start point.
     * @param control1 first intermediate shape point.
     * @param control2 second intermediate shape point.
     * @param end end point.
     * @throws NullPointerException when <code>start</code>, <code>control1</code>, <code>control2</code>, or <code>end</code>
     *             is <code>null</code>
     */
    public BezierCubic3d(final Point3d start, final Point3d control1, final Point3d control2, final Point3d end)
    {
        super(start, control1, control2, end);
        this.startPoint = new DirectedPoint3d(start, control1);
        this.endPoint = new DirectedPoint3d(end, control2.directionTo(end));
        this.length = length();
    }

    /**
     * Create a cubic B&eacute;zier curve.
     * @param points Point2d[]; array containing four Point2d objects
     * @throws NullPointerException when <code>points</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when length of <code>points</code> is not equal to <code>4</code>
     */
    public BezierCubic3d(final Point3d[] points)
    {
        this(checkArray(points)[0], points[1], points[2], points[3]);
    }

    /**
     * Verify that a Point3d[] contains exactly 4 elements.
     * @param points Point3d[]; the array to check
     * @return Point3d[]; the provided array
     * @throws IllegalArgumentException when length of <code>points</code> is not <code>4</code>
     */
    private static Point3d[] checkArray(final Point3d[] points)
    {
        Throw.when(points.length != 4, IllegalArgumentException.class, "points must contain exactly 4 Point2d objects");
        return points;
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @throws NullPointerException when <code>start</code>, or <code>end</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>start</code> and <code>end</code> are at the same location
     */
    public BezierCubic3d(final Ray3d start, final Ray3d end)
    {
        this(start, end, 1.0);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     * @throws NullPointerException when <code>start</code>, or <code>end</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>start</code> and <code>end</code> are at the same location,
     *             <code>shape &le; 0</code>, <code>shape</code> is <code>NaN</code>, or infinite
     */
    public BezierCubic3d(final Ray3d start, final Ray3d end, final double shape)
    {
        this(start, end, shape, false);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     * @param weighted boolean; control point distance relates to distance to projected point on extended line from other end
     * @throws NullPointerException when <code>start</code>, or <code>end</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>start</code> and <code>end</code> are at the same location,
     *             <code>shape &le; 0</code>, <code>shape</code> is <code>NaN</code>, or infinite
     */
    public BezierCubic3d(final Ray3d start, final Ray3d end, final double shape, final boolean weighted)

    {
        this(createControlPoints(start, end, shape, weighted));
    }

    /**
     * Create control points for a cubic B&eacute;zier curve defined by two Rays.
     * @param start Ray3d; the start point (and direction)
     * @param end Ray3d; the end point (and direction)
     * @param shape double; the shape; higher values put the generated control points further away from end and result in a
     *            pointier B&eacute;zier curve
     * @param weighted boolean;
     * @return Point3d[]; an array of four Point3d elements: start, the first control point, the second control point, end.
     * @throws NullPointerException when <code>start</code>, or <code>end</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>start</code> and <code>end</code> are at the same location,
     *             <code>shape &le; 0</code>, <code>shape</code> is <code>NaN</code>, or infinite
     */
    private static Point3d[] createControlPoints(final Ray3d start, final Ray3d end, final double shape, final boolean weighted)
    {
        Throw.whenNull(start, "start");
        Throw.whenNull(end, "end");
        Throw.when(start.distanceSquared(end) == 0, IllegalArgumentException.class,
                "Cannot create control points if start and end points coincide");
        Throw.whenNaN(shape, "shape");
        Throw.when(shape <= 0 || Double.isInfinite(shape), IllegalArgumentException.class,
                "shape must be a finite, positive value");

        Point3d control1;
        Point3d control2;
        if (weighted)
        {
            // each control point is 'w' * the distance between the end-points away from the respective end point
            // 'w' is a weight given by the distance from the end point to the extended line of the other end point
            double distance = shape * start.distance(end);
            double dStart = start.distance(end.projectOrthogonalExtended(start));
            double dEnd = end.distance(start.projectOrthogonalExtended(end));
            double wStart = dStart / (dStart + dEnd);
            double wEnd = dEnd / (dStart + dEnd);
            control1 = start.getLocation(distance * wStart);
            control2 = end.getLocationExtended(-distance * wEnd);
        }
        else
        {
            // each control point is half the distance between the end-points away from the respective end point
            double distance = shape * start.distance(end) / 2.0;
            control1 = start.getLocation(distance);
            control2 = end.getLocationExtended(-distance);
        }
        return new Point3d[] {start, control1, control2, end};
    }

    @Override
    public DirectedPoint3d getStartPoint()
    {
        return this.startPoint;
    }

    @Override
    public DirectedPoint3d getEndPoint()
    {
        return this.endPoint;
    }

    @Override
    public double getLength()
    {
        return this.length;
    }

}
