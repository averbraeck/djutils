package org.djutils.draw.line;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Transform2d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Generation of B&eacute;zier curves. <br>
 * The class implements the cubic(...) method to generate a cubic B&eacute;zier curve using the following formula: B(t) = (1 -
 * t)<sup>3</sup>P<sub>0</sub> + 3t(1 - t)<sup>2</sup> P<sub>1</sub> + 3t<sup>2</sup> (1 - t) P<sub>2</sub> + t<sup>3</sup>
 * P<sub>3</sub> where P<sub>0</sub> and P<sub>3</sub> are the end points, and P<sub>1</sub> and P<sub>2</sub> the control
 * points. <br>
 * For a smooth movement, one of the standard implementations if the cubic(...) function offered is the case where P<sub>1</sub>
 * is positioned halfway between P<sub>0</sub> and P<sub>3</sub> starting from P<sub>0</sub> in the direction of P<sub>3</sub>,
 * and P<sub>2</sub> is positioned halfway between P<sub>3</sub> and P<sub>0</sub> starting from P<sub>3</sub> in the direction
 * of P<sub>0</sub>.<br>
 * Finally, an n-point generalization of the B&eacute;zier curve is implemented with the bezier(...) function.
 * <p>
 * Copyright (c) 2013-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class Bezier
{
    /** The default number of points to use to construct a B&eacute;zier curve. */
    public static final int DEFAULT_BEZIER_SIZE = 64;

    /** Cached factorial values. */
    private static long[] fact = new long[] { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L,
            479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L,
            121645100408832000L, 2432902008176640000L };

    /** Utility class. */
    private Bezier()
    {
        // do not instantiate
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two control points.
     * @param size int; the number of points of the B&eacute;zier curve
     * @param start Point2D; the start point of the B&eacute;zier curve
     * @param control1 Point2D; the first control point
     * @param control2 Point2D; the second control point
     * @param end Point2D; the end point of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the two provided control
     *         points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final int size, final Point2d start, final Point2d control1, final Point2d control2,
            final Point2d end) throws DrawRuntimeException
    {
        Throw.when(size < 2, DrawRuntimeException.class, "Too few points (specified %d; minimum is 2)", size);
        Point2d[] points = new Point2d[size];
        for (int n = 0; n < size; n++)
        {
            double t = n / (size - 1.0);
            double x = B3(t, start.x, control1.x, control2.x, end.x);
            double y = B3(t, start.y, control1.y, control2.y, end.y);
            points[n] = new Point2d(x, y);
        }
        return new PolyLine2d(points);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two control points with a specified precision.
     * @param epsilon double; the precision.
     * @param start Point2D; the start point of the B&eacute;zier curve
     * @param control1 Point2D; the first control point
     * @param control2 Point2D; the second control point
     * @param end Point2D; the end point of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the two provided control
     *         points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final double epsilon, final Point2d start, final Point2d control1, final Point2d control2,
            final Point2d end) throws DrawRuntimeException
    {
        return bezier(epsilon, start, control1, control2, end);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end. TODO change start and end to Ray3d
     * @param size int; the number of points of the B&eacute;zier curve
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the directions of those
     *         points at start and end
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final int size, final Ray2d start, final Ray2d end)
            throws DrawRuntimeException
    {
        return cubic(size, start, end, 1.0);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end with specified precision.
     * @param epsilon double; the precision.
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the directions of those
     *         points at start and end
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final double epsilon, final Ray2d start, final Ray2d end)
            throws DrawRuntimeException
    {
        return cubic(epsilon, start, end, 1.0);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param size int; the number of points for the B&eacute;zier curve
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the directions of those
     *         points at start and end
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final int size, final Ray2d start, final Ray2d end, final double shape)
            throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(shape) || Double.isInfinite(shape) || shape <= 0, DrawRuntimeException.class,
                "shape must be a finite, positive value");
        return cubic(size, start, end, shape, false);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end with specified precision.
     * @param epsilon double; the precision.
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the directions of those
     *         points at start and end
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final double epsilon, final Ray2d start, final Ray2d end,
            final double shape) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(shape) || Double.isInfinite(shape) || shape <= 0, DrawRuntimeException.class,
                "shape must be a finite, positive value");
        return cubic(epsilon, start, end, shape, false);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param size int; the number of points for the B&eacute;zier curve
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0, finite and not NaN
     * @param weighted boolean; control point distance relates to distance to projected point on extended line from other end
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, with the two determined
     *         control points
     * @throws NullPointerException when start or end is null
     * @throws DrawRuntimeException in case size is less than 2, start is at the same location as end, shape is invalid, or the
     *             B&eacute;zier curve could not be constructed
     */
    public static PolyLine2d cubic(final int size, final Ray2d start, final Ray2d end, final double shape,
            final boolean weighted) throws NullPointerException, DrawRuntimeException
    {
        Point2d[] points = createControlPoints(start, end, shape, weighted);
        return cubic(size, points[0], points[1], points[2], points[3]);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end with specified precision.
     * @param epsilon double; the precision.
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0, finite and not NaN
     * @param weighted boolean; control point distance relates to distance to projected point on extended line from other end
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, with the two determined
     *         control points
     * @throws NullPointerException when start or end is null
     * @throws DrawRuntimeException in case size is less than 2, start is at the same location as end, shape is invalid, or the
     *             B&eacute;zier curve could not be constructed
     */
    public static PolyLine2d cubic(final double epsilon, final Ray2d start, final Ray2d end,
            final double shape, final boolean weighted) throws NullPointerException, DrawRuntimeException
    {
        Point2d[] points = createControlPoints(start, end, shape, weighted);
        return cubic(epsilon, points[0], points[1], points[2], points[3]);
    }

    /** Unit vector for transformations in createControlPoints. */
    private static final Point2d UNIT_VECTOR2D = new Point2d(1, 0);

    /**
     * Create control points for a cubic B&eacute;zier curve defined by two Rays.
     * @param start Ray2d; the start point (and direction)
     * @param end Ray2d; the end point (and direction)
     * @param shape double; the shape; higher values put the generated control points further away from end and result in a
     *            pointier B&eacute;zier curve
     * @param weighted boolean;
     * @return Point2d[]; an array of four Point2d elements: start, the first control point, the second control point, end.
     */
    private static Point2d[] createControlPoints(final Ray2d start, final Ray2d end, final double shape,
            final boolean weighted)
    {
        Throw.whenNull(start, "start point may not be null");
        Throw.whenNull(end, "end point may not be null");
        Throw.when(start.distanceSquared(end) == 0, DrawRuntimeException.class,
                "Cannot create control points if start and end points coincide");
        Throw.when(Double.isNaN(shape) || shape <= 0 || Double.isInfinite(shape), DrawRuntimeException.class,
                "shape must be a finite, positive value");

        Point2d control1;
        Point2d control2;
        if (weighted)
        {
            // each control point is 'w' * the distance between the end-points away from the respective end point
            // 'w' is a weight given by the distance from the end point to the extended line of the other end point
            double distance = shape * start.distance(end);
            double dStart = start.distance(start.closestPointOnLine(end));
            double dEnd = end.distance(end.closestPointOnLine(start));
            double wStart = dStart / (dStart + dEnd);
            double wEnd = dEnd / (dStart + dEnd);
            control1 = new Transform2d().translate(start).rotation(start.phi).scale(distance * wStart, distance * wStart)
                    .transform(UNIT_VECTOR2D);
            // - (minus) as the angle is where the line leaves, i.e. from shape point to end
            control2 = new Transform2d().translate(end).rotation(end.phi + Math.PI)
                    .scale(distance * wEnd, distance * wEnd).transform(UNIT_VECTOR2D);
        }
        else
        {
            // each control point is half the distance between the end-points away from the respective end point
            double distance = shape * start.distance(end) / 2.0;
            control1 = new Transform2d().translate(start).rotation(start.phi).scale(distance, distance)
                    .transform(UNIT_VECTOR2D);
            control2 = new Transform2d().translate(end).rotation(end.phi + Math.PI).scale(distance, distance)
                    .transform(UNIT_VECTOR2D);
        }
        return new Point2d[] { start, control1, control2, end };
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end. The size of the constructed curve is <code>DEFAULT_BEZIER_SIZE</code>.
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, following the directions of
     *         those points at start and end
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d cubic(final Ray2d start, final Ray2d end) throws DrawRuntimeException
    {
        return cubic(DEFAULT_BEZIER_SIZE, start, end);
    }

    /**
     * Approximate a B&eacute;zier curve of degree n.
     * @param size int; the number of points for the B&eacute;zier curve to be constructed
     * @param points Point2D...; the points of the curve, where the first and last are begin and end point, and the intermediate
     *            ones are control points. There should be at least two points.
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the provided control
     *         points
     * @throws NullPointerException when points contains a null
     * @throws DrawRuntimeException in case the number of points is less than 2, size is less than 2, or the B&eacute;zier curve
     *             could not be constructed
     */
    public static PolyLine2d bezier(final int size, final Point2d... points) throws NullPointerException, DrawRuntimeException
    {
        Throw.when(points.length < 2, DrawRuntimeException.class, "Too few points; need at least two");
        Throw.when(size < 2, DrawRuntimeException.class, "size too small (must be at least 2)");
        Point2d[] result = new Point2d[size];
        double[] px = new double[points.length];
        double[] py = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            Point2d p = points[i];
            Throw.whenNull(p, "points contains a null value");
            px[i] = p.x;
            py[i] = p.y;
        }
        for (int n = 0; n < size; n++)
        {
            double t = n / (size - 1.0);
            double x = Bn(t, px);
            double y = Bn(t, py);
            result[n] = new Point2d(x, y);
        }
        return new PolyLine2d(result);
    }

    /**
     * Approximate a B&eacute;zier curve of degree n using <code>DEFAULT_BEZIER_SIZE</code> points.
     * @param points Point2D...; the points of the curve, where the first and last are begin and end point, and the intermediate
     *            ones are control points. There should be at least two points.
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, using the provided control
     *         points
     * @throws NullPointerException when points contains a null value
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d bezier(final Point2d... points) throws NullPointerException, DrawRuntimeException
    {
        return bezier(DEFAULT_BEZIER_SIZE, points);
    }

    /**
     * Approximate a B&eacute;zier curve of degree n with a specified precision.
     * @param epsilon double; the precision.
     * @param points Point2d...; the points of the curve, where the first and last are begin and end point, and the intermediate
     *            ones are control points. There should be at least two points.
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, with the provided control
     *         points
     * @throws NullPointerException when points contains a null value
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine2d bezier(final double epsilon, final Point2d... points)
            throws NullPointerException, DrawRuntimeException
    {
        Throw.when(points.length < 2, DrawRuntimeException.class, "Too few points; need at least two");
        Throw.when(Double.isNaN(epsilon) || epsilon <= 0, DrawRuntimeException.class,
                "epsilonPosition must be a positive number");
        NavigableMap<Double, Point2d> result = new TreeMap<>();
        double[] px = new double[points.length];
        double[] py = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            Point2d p = points[i];
            Throw.whenNull(p, "points contains a null value");
            px[i] = p.x;
            py[i] = p.y;
        }
        int initialSize = points.length - 1;
        for (int n = 0; n < initialSize; n++)
        {
            double t = n / (initialSize - 1.0);
            double x = Bn(t, px);
            double y = Bn(t, py);
            result.put(t, new Point2d(x, y));
        }
        // Walk along all point pairs and see if additional points need to be inserted
        Double prevT = result.firstKey();
        Point2d prevPoint = result.get(prevT);
        Map.Entry<Double, Point2d> entry;
        while ((entry = result.higherEntry(prevT)) != null)
        {
            Double nextT = entry.getKey();
            Point2d nextPoint = entry.getValue();
            if (null != prevPoint)
            {
                double medianT = (prevT + nextT) / 2;
                double x = Bn(medianT, px);
                double y = Bn(medianT, py);
                Point2d medianPoint = new Point2d(x, y);
                Point2d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= epsilon)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (prevPoint.distance(nextPoint) > epsilon)
                {
                    // Check for an inflection point by creating additional points at one quarter and three quarters. If these
                    // are on opposite sides of the line from prevPoint to nextPoint; there must be an inflection point.
                    // https://stackoverflow.com/questions/1560492/how-to-tell-whether-a-point-is-to-the-right-or-left-side-of-a-line
                    double quarterT = (prevT + medianT) / 2;
                    double quarterX = Bn(quarterT, px);
                    double quarterY = Bn(quarterT, py);
                    int sign1 = (int) Math.signum((nextPoint.x - prevPoint.x) * (quarterY - prevPoint.y)
                            - (nextPoint.y - prevPoint.y) * (quarterX - prevPoint.x));
                    double threeQuarterT = (nextT + medianT) / 2;
                    double threeQuarterX = Bn(threeQuarterT, px);
                    double threeQuarterY = Bn(threeQuarterT, py);
                    int sign2 = (int) Math.signum((nextPoint.x - prevPoint.x) * (threeQuarterY - prevPoint.y)
                            - (nextPoint.y - prevPoint.y) * (threeQuarterX - prevPoint.x));
                    if (sign1 != sign2)
                    {
                        // There is an inflection point
                        System.out.println("Detected inflection point between " + prevPoint + " and " + nextPoint);
                        // Inserting the halfway point should take care of this
                        result.put(medianT, medianPoint);
                        continue;
                    }
                }
                // TODO check angles
            }
            prevT = nextT;
            prevPoint = nextPoint;
        }
        try
        {
            return new PolyLine2d(result.values().iterator());
        }
        catch (NullPointerException | DrawException e)
        {
            // Cannot happen? Really?
            e.printStackTrace();
            throw new DrawRuntimeException(e);
        }
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two control points.
     * @param size int; the number of points for the B&eacute;zier curve
     * @param start Point3D; the start point of the B&eacute;zier curve
     * @param control1 Point3D; the first control point
     * @param control2 Point3D; the second control point
     * @param end Point3D; the end point of the B&eacute;zier curve
     * @return PolyLine3d; an approximation of a cubic B&eacute;zier curve between start and end, with the two provided control
     *         points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final int size, final Point3d start, final Point3d control1, final Point3d control2,
            final Point3d end) throws DrawRuntimeException
    {
        return bezier(size, start, control1, control2, end);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two control points with a specified precision.
     * @param epsilon double; the precision.
     * @param start Point3D; the start point of the B&eacute;zier curve
     * @param control1 Point3D; the first control point
     * @param control2 Point3D; the second control point
     * @param end Point3D; the end point of the B&eacute;zier curve
     * @return PolyLine3d; an approximation of a cubic B&eacute;zier curve between start and end, with the two provided control
     *         points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final double epsilon, final Point3d start, final Point3d control1, final Point3d control2,
            final Point3d end) throws DrawRuntimeException
    {
        return bezier(epsilon, start, control1, control2, end);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param size int; the number of points for the B&eacute;zier curve
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, with the two provided control
     *         points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final int size, final Ray3d start, final Ray3d end)
            throws DrawRuntimeException
    {
        return cubic(size, start, end, 1.0);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end with specified precision.
     * @param epsilon double; the precision.
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @return PolyLine2d; an approximation of a cubic B&eacute;zier curve between start and end, with the two provided control
     *         points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final double epsilon, final Ray3d start, final Ray3d end)
            throws DrawRuntimeException
    {
        return cubic(epsilon, start, end, 1.0);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end.
     * @param size int; the number of points for the B&eacute;zier curve
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     * @return a cubic B&eacute;zier curve between start and end, with the two determined control points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final int size, final Ray3d start, final Ray3d end, final double shape)
            throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(shape) || Double.isInfinite(shape) || shape <= 0, DrawRuntimeException.class,
                "shape must be a finite, positive value");
        return cubic(size, start, end, shape, false);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end with specified precision.
     * @param epsilon double; the precision.
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     * @return a cubic B&eacute;zier curve between start and end, with the two determined control points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final double epsilon, final Ray3d start, final Ray3d end,
            final double shape) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(shape) || Double.isInfinite(shape) || shape <= 0, DrawRuntimeException.class,
                "shape must be a finite, positive value");
        return cubic(epsilon, start, end, shape, false);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end. The z-value is interpolated in a linear way.
     * @param size int; the number of points for the B&eacute;zier curve
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0
     * @param weighted boolean; control point distance relates to distance to projected point on extended line from other end
     * @return a cubic B&eacute;zier curve between start and end, with the two determined control points
     * @throws NullPointerException when start or end is null
     * @throws DrawRuntimeException in case size is less than 2, start is at the same location as end, shape is invalid, or the
     *             B&eacute;zier curve could not be constructed
     */
    public static PolyLine3d cubic(final int size, final Ray3d start, final Ray3d end, final double shape,
            final boolean weighted) throws NullPointerException, DrawRuntimeException
    {
        Point3d[] points = createControlPoints(start, end, shape, weighted);
        return cubic(size, points[0], points[1], points[2], points[3]);
    }

    /**
     * Approximate a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end with specified precision.
     * @param epsilon double; the precision.
     * @param start Ray3d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray3d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0, finite and not NaN
     * @param weighted boolean; control point distance relates to distance to projected point on extended line from other end
     * @return PolyLine3d; an approximation of a cubic B&eacute;zier curve between start and end, with the two determined
     *         control points
     * @throws NullPointerException when start or end is null
     * @throws DrawRuntimeException in case size is less than 2, start is at the same location as end, shape is invalid, or the
     *             B&eacute;zier curve could not be constructed
     */
    public static PolyLine3d cubic(final double epsilon, final Ray3d start, final Ray3d end,
            final double shape, final boolean weighted) throws NullPointerException, DrawRuntimeException
    {
        Point3d[] points = createControlPoints(start, end, shape, weighted);
        return cubic(epsilon, points[0], points[1], points[2], points[3]);
    }

    /**
     * Create control points for a cubic B&eacute;zier curve defined by two Rays.
     * @param start Ray3d; the start point (and direction)
     * @param end Ray3d; the end point (and direction)
     * @param shape double; the shape; higher values put the generated control points further away from end and result in a
     *            pointier B&eacute;zier curve
     * @param weighted boolean;
     * @return Point3d[]; an array of four Point3d elements: start, the first control point, the second control point, end.
     */
    private static Point3d[] createControlPoints(final Ray3d start, final Ray3d end, final double shape,
            final boolean weighted)
    {
        Throw.whenNull(start, "start point may not be null");
        Throw.whenNull(end, "end point may not be null");
        Throw.when(start.distanceSquared(end) == 0, DrawRuntimeException.class,
                "Cannot create control points if start and end points coincide");
        Throw.when(Double.isNaN(shape) || shape <= 0 || Double.isInfinite(shape), DrawRuntimeException.class,
                "shape must be a finite, positive value");

        Point3d control1;
        Point3d control2;
        if (weighted)
        {
            // each control point is 'w' * the distance between the end-points away from the respective end point
            // 'w' is a weight given by the distance from the end point to the extended line of the other end point
            double distance = shape * start.distance(end);
            double dStart = start.distance(start.closestPointOnLine(end));
            double dEnd = end.distance(end.closestPointOnLine(start));
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
        return new Point3d[] { start, control1, control2, end };
    }

    /**
     * Construct a cubic B&eacute;zier curve from start to end with two generated control points at half the distance between
     * start and end. The z-value is interpolated in a linear way. The size of the constructed curve is
     * <code>DEFAULT_BEZIER_SIZE</code>. TODO change start en end to Ray3d
     * @param start Ray3d; the start point and orientation of the B&eacute;zier curve
     * @param end Ray3d; the end point and orientation of the B&eacute;zier curve
     * @return a cubic B&eacute;zier curve between start and end, with the two provided control points
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d cubic(final Ray3d start, final Ray3d end) throws DrawRuntimeException
    {
        return cubic(DEFAULT_BEZIER_SIZE, start, end);
    }

    /**
     * Calculate the cubic B&eacute;zier point with B(t) = (1 - t)<sup>3</sup>P<sub>0</sub> + 3t(1 - t)<sup>2</sup>
     * P<sub>1</sub> + 3t<sup>2</sup> (1 - t) P<sub>2</sub> + t<sup>3</sup> P<sub>3</sub>.
     * @param t double; the fraction
     * @param p0 double; the first point of the curve
     * @param p1 double; the first control point
     * @param p2 double; the second control point
     * @param p3 double; the end point of the curve
     * @return the cubic bezier value B(t)
     */
    @SuppressWarnings("checkstyle:methodname")
    private static double B3(final double t, final double p0, final double p1, final double p2, final double p3)
    {
        double t2 = t * t;
        double t3 = t2 * t;
        double m = (1.0 - t);
        double m2 = m * m;
        double m3 = m2 * m;
        return m3 * p0 + 3.0 * t * m2 * p1 + 3.0 * t2 * m * p2 + t3 * p3;
    }

    /**
     * Construct a B&eacute;zier curve of degree n.
     * @param size int; the number of points for the B&eacute;zier curve to be constructed
     * @param points Point3D...; the points of the curve, where the first and last are begin and end point, and the intermediate
     *            ones are control points. There should be at least two points.
     * @return the B&eacute;zier value B(t) of degree n, where n is the number of points in the array
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d bezier(final int size, final Point3d... points) throws DrawRuntimeException
    {
        Throw.when(points.length < 2, DrawRuntimeException.class, "Too few points; need at least two");
        Throw.when(size < 2, DrawRuntimeException.class, "size too small (must be at least 2)");
        Point3d[] result = new Point3d[size];
        double[] px = new double[points.length];
        double[] py = new double[points.length];
        double[] pz = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            px[i] = points[i].x;
            py[i] = points[i].y;
            pz[i] = points[i].z;
        }
        for (int n = 0; n < size; n++)
        {
            double t = n / (size - 1.0);
            double x = Bn(t, px);
            double y = Bn(t, py);
            double z = Bn(t, pz);
            result[n] = new Point3d(x, y, z);
        }
        return new PolyLine3d(result);
    }

    /**
     * Approximate a B&eacute;zier curve of degree n using <code>DEFAULT_BEZIER_SIZE</code> points.
     * @param points Point3D...; the points of the curve, where the first and last are begin and end point, and the intermediate
     *            ones are control points. There should be at least two points.
     * @return the B&eacute;zier value B(t) of degree n, where n is the number of points in the array
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d bezier(final Point3d... points) throws DrawRuntimeException
    {
        return bezier(DEFAULT_BEZIER_SIZE, points);
    }

    /**
     * Approximate a B&eacute;zier curve of degree n with a specified precision.
     * @param epsilon double; the precision.
     * @param points Point3d...; the points of the curve, where the first and last are begin and end point, and the intermediate
     *            ones are control points. There should be at least two points.
     * @return PolyLine3d; an approximation of a cubic B&eacute;zier curve between start and end, with the provided control
     *         points
     * @throws NullPointerException when points contains a null value
     * @throws DrawRuntimeException in case the number of points is less than 2 or the B&eacute;zier curve could not be
     *             constructed
     */
    public static PolyLine3d bezier(final double epsilon, final Point3d... points)
            throws NullPointerException, DrawRuntimeException
    {
        Throw.when(points.length < 2, DrawRuntimeException.class, "Too few points; need at least two");
        Throw.when(Double.isNaN(epsilon) || epsilon <= 0, DrawRuntimeException.class,
                "epsilonPosition must be a positive number");
        NavigableMap<Double, Point3d> result = new TreeMap<>();
        double[] px = new double[points.length];
        double[] py = new double[points.length];
        double[] pz = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            Point3d p = points[i];
            Throw.whenNull(p, "points contains a null value");
            px[i] = p.x;
            py[i] = p.y;
            pz[i] = p.z;
        }
        int initialSize = points.length - 1;
        for (int n = 0; n < initialSize; n++)
        {
            double t = n / (initialSize - 1.0);
            double x = Bn(t, px);
            double y = Bn(t, py);
            double z = Bn(t, pz);
            result.put(t, new Point3d(x, y, z));
        }
        // Walk along all point pairs and see if additional points need to be inserted
        Double prevT = result.firstKey();
        Point3d prevPoint = result.get(prevT);
        Map.Entry<Double, Point3d> entry;
        while ((entry = result.higherEntry(prevT)) != null)
        {
            Double nextT = entry.getKey();
            Point3d nextPoint = entry.getValue();
            if (null != prevPoint)
            {
                double medianT = (prevT + nextT) / 2;
                double x = Bn(medianT, px);
                double y = Bn(medianT, py);
                double z = Bn(medianT, pz);
                Point3d medianPoint = new Point3d(x, y, z);
                Point3d projectedPoint = medianPoint.closestPointOnSegment(prevPoint, nextPoint);
                double errorPosition = medianPoint.distance(projectedPoint);
                if (errorPosition >= epsilon)
                {
                    // We need to insert another point
                    result.put(medianT, medianPoint);
                    continue;
                }
                if (prevPoint.distance(nextPoint) > epsilon)
                {
                    // Check for an inflection point by creating additional points at one quarter and three quarters. If these
                    // are on opposite sides of the line from prevPoint to nextPoint; there must be an inflection point.
                    // https://stackoverflow.com/questions/1560492/how-to-tell-whether-a-point-is-to-the-right-or-left-side-of-a-line
                    double quarterT = (prevT + medianT) / 2;
                    double quarterX = Bn(quarterT, px);
                    double quarterY = Bn(quarterT, py);
                    int sign1 = (int) Math.signum((nextPoint.x - prevPoint.x) * (quarterY - prevPoint.y)
                            - (nextPoint.y - prevPoint.y) * (quarterX - prevPoint.x));
                    double threeQuarterT = (nextT + medianT) / 2;
                    double threeQuarterX = Bn(threeQuarterT, px);
                    double threeQuarterY = Bn(threeQuarterT, py);
                    int sign2 = (int) Math.signum((nextPoint.x - prevPoint.x) * (threeQuarterY - prevPoint.y)
                            - (nextPoint.y - prevPoint.y) * (threeQuarterX - prevPoint.x));
                    if (sign1 != sign2)
                    {
                        // There is an inflection point
                        System.out.println("Detected inflection point between " + prevPoint + " and " + nextPoint);
                        // Inserting the halfway point should take care of this
                        result.put(medianT, medianPoint);
                        continue;
                    }
                }
                // TODO check angles
            }
            prevT = nextT;
            prevPoint = nextPoint;
        }
        try
        {
            return new PolyLine3d(result.values().iterator());
        }
        catch (NullPointerException | DrawException e)
        {
            // Cannot happen? Really?
            e.printStackTrace();
            throw new DrawRuntimeException(e);
        }
    }

    /**
     * Calculate the B&eacute;zier point of degree n, with B(t) = Sum(i = 0..n) [C(n, i) * (1 - t)<sup>n-i</sup> t<sup>i</sup>
     * P<sub>i</sub>], where C(n, k) is the binomial coefficient defined by n! / ( k! (n-k)! ), ! being the factorial operator.
     * @param t double; the fraction
     * @param p double...; the points of the curve, where the first and last are begin and end point, and the intermediate ones
     *            are control points
     * @return the B&eacute;zier value B(t) of degree n, where n is the number of points in the array
     */
    @SuppressWarnings("checkstyle:methodname")
    private static double Bn(final double t, final double... p)
    {
        double b = 0.0;
        double m = (1.0 - t);
        int n = p.length - 1;
        double fn = factorial(n);
        for (int i = 0; i <= n; i++)
        {
            double c = fn / (factorial(i) * (factorial(n - i)));
            b += c * Math.pow(m, n - i) * Math.pow(t, i) * p[i];
        }
        return b;
    }

    /**
     * Calculate factorial(k), which is k * (k-1) * (k-2) * ... * 1. For factorials up to 20, a lookup table is used.
     * @param k int; the parameter
     * @return factorial(k)
     */
    private static double factorial(final int k)
    {
        if (k < fact.length)
        {
            return fact[k];
        }
        double f = 1;
        for (int i = 2; i <= k; i++)
        {
            f = f * i;
        }
        return f;
    }

}
