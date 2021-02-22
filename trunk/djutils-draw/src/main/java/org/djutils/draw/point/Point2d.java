package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.Space2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.line.Ray2d;
import org.djutils.exceptions.Throw;

/**
 * A Point2d is an immutable Point with an x and y coordinate, stored with double precision. It differs from many Point
 * implementations by being immutable.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point2d implements Drawable2d, Point<Point2d, Space2d>
{
    /** */
    private static final long serialVersionUID = 20201201L;

    /** The x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double x;

    /** The y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double y;

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public Point2d(final double x, final double y) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param xy double[]; the x and y coordinate
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2, or a coordinate is NaN
     */
    public Point2d(final double[] xy) throws NullPointerException, IllegalArgumentException
    {
        this(checkLengthIsTwo(Throw.whenNull(xy, "xy-point cannot be null"))[0], xy[1]);
    }

    /**
     * Create an immutable point with just two values, x and y, stored with double precision from an AWT Point2D.
     * @param point Point2D; an AWT Point2D
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate
     */
    public Point2d(final Point2D point) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(point.getX()) || Double.isNaN(point.getY()), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Throw an IllegalArgumentException if the length of the provided array is not two.
     * @param xy double[]; the provided array
     * @return double[]; the provided array
     * @throws IllegalArgumentException when length of xy is not two
     */
    private static double[] checkLengthIsTwo(final double[] xy) throws IllegalArgumentException
    {
        Throw.when(xy.length != 2, IllegalArgumentException.class, "Length of xy-array must be 2");
        return xy;
    }

    /** {@inheritDoc} */
    @Override
    public final double getX()
    {
        return this.x;
    }

    /** {@inheritDoc} */
    @Override
    public final double getY()
    {
        return this.y;
    }

    /** {@inheritDoc} */
    @Override
    public double distance(final Point2d otherPoint)
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        return Math.hypot(otherPoint.x - this.x, otherPoint.y - this.y);
    }

    /** {@inheritDoc} */
    @Override
    public double distanceSquared(final Point2d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        double dx = this.x - otherPoint.x;
        double dy = this.y - otherPoint.y;
        return dx * dx + dy * dy;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point2d> getPoints()
    {
        return Arrays.stream(new Point2d[] {this}).iterator();
    }

    /**
     * Return a new Point with a translation by the provided dx and dy.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return P; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, or dy is NaN
     */
    public Point2d translate(final double dx, final double dy)
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new Point2d(this.x + dx, this.y + dy);
    }

    /**
     * Return a new Point3d with a translation by the provided delta-x, delta-y and deltaZ.
     * @param dx double; the x translation
     * @param dy double; the y translation
     * @param dz double; the z translation
     * @return Point2d; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, dy, or dz is NaN
     */
    public Point3d translate(final double dx, final double dy, final double dz)
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "translation may not be NaN");
        return new Point3d(this.x + dx, this.y + dy, dz);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d scale(final double factor)
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new Point2d(this.x * factor, this.y * factor);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d neg()
    {
        return scale(-1.0);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d abs()
    {
        return new Point2d(Math.abs(this.x), Math.abs(this.y));
    }

    /** {@inheritDoc} */
    @Override
    public Point2d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d interpolate(final Point2d point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new Point2d((1.0 - fraction) * this.x + fraction * point.x, (1.0 - fraction) * this.y + fraction * point.y);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final Point2d other, final double epsilon)
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(this.x - other.x) > epsilon)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilon)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return new Bounds2d(this);
    }

    /**
     * Compute the 2D intersection of two lines. Both lines are defined by two points (that should be distinct).
     * @param line1P1X double; x-coordinate of start point of line 1
     * @param line1P1Y double; y-coordinate of start point of line 1
     * @param line1P2X double; x-coordinate of end point of line 1
     * @param line1P2Y double; y-coordinate of end point of line 1
     * @param lowLimitLine1 boolean; if true; the intersection may not lie before the start point of line 1
     * @param highLimitLine1 boolean; if true; the intersection may not lie beyond the end point of line 1
     * @param line2P1X double; x-coordinate of start point of line 2
     * @param line2P1Y double; y-coordinate of start point of line 2
     * @param line2P2X double; x-coordinate of end point of line 2
     * @param line2P2Y double; y-coordinate of end point of line 2
     * @param lowLimitLine2 boolean; if true; the intersection may not lie before the start point of line 2
     * @param highLimitLine2 boolean; if true; the intersection may not lie beyond the end point of line 2
     * @return Point2d; the intersection of the two lines, or null if the lines are (almost) parallel, or the intersection point
     *         lies outside the permitted range
     * @throws DrawRuntimeException when any of the parameters is NaN
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public static Point2d intersectionOfLines(final double line1P1X, final double line1P1Y, final double line1P2X,
            final double line1P2Y, final boolean lowLimitLine1, final boolean highLimitLine1, final double line2P1X,
            final double line2P1Y, final double line2P2X, final double line2P2Y, final boolean lowLimitLine2,
            final boolean highLimitLine2) throws DrawRuntimeException
    {
        double line1DX = line1P2X - line1P1X;
        double line1DY = line1P2Y - line1P1Y;
        double l2p1x = line2P1X - line1P1X;
        double l2p1y = line2P1Y - line1P1Y;
        double l2p2x = line2P2X - line1P1X;
        double l2p2y = line2P2Y - line1P1Y;
        double denominator = (l2p2y - l2p1y) * line1DX - (l2p2x - l2p1x) * line1DY;
        Throw.when(Double.isNaN(denominator), DrawRuntimeException.class, "NaN value not permitted");
        if (denominator == 0.0)
        {
            return null; // lines are parallel (they might even be on top of each other, but we don't check that)
        }
        double uA = ((l2p2x - l2p1x) * (-l2p1y) - (l2p2y - l2p1y) * (-l2p1x)) / denominator;
        // System.out.println("uA is " + uA);
        if (uA < 0.0 && lowLimitLine1 || uA > 1.0 && highLimitLine1)
        {
            return null; // intersection outside line 1
        }
        double uB = (line1DY * l2p1x - line1DX * l2p1y) / denominator;
        // System.out.println("uB is " + uB);
        if (uB < 0.0 && lowLimitLine2 || uB > 1.0 && highLimitLine2)
        {
            return null; // intersection outside line 2
        }
        if (uA == 1.0) // maximize precision
        {
            return new Point2d(line1P2X, line1P2Y);
        }
        if (uB == 0.0)
        {
            return new Point2d(line2P1X, line2P1Y);
        }
        if (uB == 1.0)
        {
            return new Point2d(line2P2X, line2P2Y);
        }
        return new Point2d(line1P1X + uA * line1DX, line1P1Y + uA * line1DY);
    }

    /**
     * Compute the 2D intersection of two lines. Both lines are defined by two points (that should be distinct). The lines are
     * considered to be infinitely long; so unless the lines are parallel; there is an intersection.
     * @param l1P1X double; x-coordinate of start point of line segment 1
     * @param l1P1Y double; y-coordinate of start point of line segment 1
     * @param l1P2X double; x-coordinate of end point of line segment 1
     * @param l1P2Y double; y-coordinate of end point of line segment 1
     * @param l2P1X double; x-coordinate of start point of line segment 2
     * @param l2P1Y double; y-coordinate of start point of line segment 2
     * @param l2P2X double; x-coordinate of end point of line segment 2
     * @param l2P2Y double; y-coordinate of end point of line segment 2
     * @return Point2d; the intersection of the two lines, or null if the lines are (almost) parallel
     * @throws DrawRuntimeException when any of the parameters is NaN
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public static Point2d intersectionOfLines(final double l1P1X, final double l1P1Y, final double l1P2X, final double l1P2Y,
            final double l2P1X, final double l2P1Y, final double l2P2X, final double l2P2Y) throws DrawRuntimeException
    {
        return intersectionOfLines(l1P1X, l1P1Y, l1P2X, l1P2Y, false, false, l2P1X, l2P1Y, l2P2X, l2P2Y, false, false);
    }

    /**
     * Compute the 2D intersection of two lines. Both lines are defined by two points (that should be distinct). The lines are
     * considered to be infinitely long; so unless the lines are parallel; there is an intersection.
     * @param line1P1 Point2d; first point of line 1
     * @param line1P2 Point2d; second point of line 1
     * @param line2P1 Point2d; first point of line 2
     * @param line2P2 Point2d; second point of line 2
     * @return Point2d; the intersection of the two lines, or null if the lines are (almost) parallel
     * @throws NullPointerException when any of the points is null
     */
    public static Point2d intersectionOfLines(final Point2d line1P1, final Point2d line1P2, final Point2d line2P1,
            final Point2d line2P2) throws NullPointerException
    {
        Throw.when(line1P1 == null || line1P2 == null || line2P1 == null || line2P2 == null, NullPointerException.class,
                "Points may not be null");
        return intersectionOfLines(line1P1.x, line1P1.y, line1P2.x, line1P2.y, false, false, line2P1.x, line2P1.y, line2P2.x,
                line2P2.y, false, false);
    }

    /**
     * Compute the 2D intersection of two line segments. Both line segments are defined by two points (that should be distinct).
     * @param line1P1 Point2d; first point of line segment 1
     * @param line1P2 Point2d; second point of line segment 1
     * @param line2P1 Point2d; first point of line segment 2
     * @param line2P2 Point2d; second point of line segment 2
     * @return Point2d; the intersection of the two line segments, or null if the lines are parallel (within rounding error), or
     *         do not intersect
     * @throws NullPointerException when any of the points is null
     * @throws DrawRuntimeException when any of the line segments is ill-defined (begin point equals end point), or the two line
     *             segments are parallel or overlapping
     */
    public static Point2d intersectionOfLineSegments(final Point2d line1P1, final Point2d line1P2, final Point2d line2P1,
            final Point2d line2P2) throws NullPointerException, DrawRuntimeException
    {
        Throw.when(line1P1 == null || line1P2 == null || line2P1 == null || line2P2 == null, NullPointerException.class,
                "Points may not be null");
        return intersectionOfLines(line1P1.x, line1P1.y, line1P2.x, line1P2.y, true, true, line2P1.x, line2P1.y, line2P2.x,
                line2P2.y, true, true);
    }

    /**
     * Compute the 2D intersection of two line segments. Both line segments are defined by two points (that should be distinct).
     * @param line1P1X double; x coordinate of start point of first line segment
     * @param line1P1Y double; y coordinate of start point of first line segment
     * @param line1P2X double; x coordinate of end point of first line segment
     * @param line1P2Y double; y coordinate of end point of first line segment
     * @param line2P1X double; x coordinate of start point of second line segment
     * @param line2P1Y double; y coordinate of start point of second line segment
     * @param line2P2X double; x coordinate of end point of second line segment
     * @param line2P2Y double; y coordinate of end point of second line segment
     * @return Point2d; the intersection of the two line segments, or null if the lines are parallel (within rounding error), or
     *         do not intersect
     * @throws DrawRuntimeException when any of the values is NaN
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public static Point2d intersectionOfLineSegments(final double line1P1X, final double line1P1Y, final double line1P2X,
            final double line1P2Y, final double line2P1X, final double line2P1Y, final double line2P2X, final double line2P2Y)
            throws DrawRuntimeException
    {
        return intersectionOfLines(line1P1X, line1P1Y, line1P2X, line1P2Y, true, true, line2P1X, line2P1Y, line2P2X, line2P2Y,
                true, true);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnSegment(final Point2d segmentPoint1, final Point2d segmentPoint2)
    {
        Throw.whenNull(segmentPoint1, "linePoint1 may not be null");
        Throw.whenNull(segmentPoint2, "linePoint2 may not be null");
        return closestPointOnSegment(segmentPoint1.x, segmentPoint1.y, segmentPoint2.x, segmentPoint2.y);
    }

    /**
     * Compute the closest point on a line with optional limiting of the result on either end.
     * @param p1X double; the x coordinate of the first point on the line
     * @param p1Y double; the y coordinate of the first point on the line
     * @param p2X double; the x coordinate of the second point on the line
     * @param p2Y double; the y coordinate of the second point on the line
     * @param lowLimitHandling Boolean; controls handling of results that lie before the first point of the line. If null; this
     *            method returns null; else if true; this method returns (p1X,p1Y); else (lowLimitHandling is false); this
     *            method will return the closest point on the line
     * @param highLimitHandling Boolean; controls the handling of results that lie beyond the second point of the line. If null;
     *            this method returns null; else if true; this method returns (p2X,p2Y); else (highLimitHandling is false); this
     *            method will return the closest point on the line
     * @return Point2d; the closest point on the line after applying the indicated limit handling; so the result can be null
     * @throws DrawRuntimeException when any of the arguments is NaN
     */
    public Point2d closestPointOnLine(final double p1X, final double p1Y, final double p2X, final double p2Y,
            final Boolean lowLimitHandling, final Boolean highLimitHandling) throws DrawRuntimeException
    {
        double fraction = fractionalPositionOnLine(p1X, p1Y, p2X, p2Y, lowLimitHandling, highLimitHandling);
        if (Double.isNaN(fraction))
        {
            return null;
        }
        if (fraction == 1.0)
        {
            return new Point2d(p2X, p2Y); // Maximize precision in case fraction == 1.0
        }
        return new Point2d(p1X + fraction * (p2X - p1X), p1Y + fraction * (p2Y - p1Y));
    }

    /**
     * Compute the fractional position of the closest point on a line with optional limiting of the result on either end. If the
     * line has length 0; this method returns 0.0.
     * @param p1X double; the x coordinate of the first point on the line
     * @param p1Y double; the y coordinate of the first point on the line
     * @param p2X double; the x coordinate of the second point on the line
     * @param p2Y double; the y coordinate of the second point on the line
     * @param lowLimitHandling Boolean; controls handling of results that lie before the first point of the line. If null; this
     *            method returns NaN; else if true; this method returns 0.0; else (lowLimitHandling is false); this results &lt;
     *            0.0 are returned
     * @param highLimitHandling Boolean; controls the handling of results that lie beyond the second point of the line. If null;
     *            this method returns NaN; else if true; this method returns 1.0; else (highLimitHandling is false); results
     *            &gt; 1.0 are returned
     * @return double; the fractional position of the closest point on the line. Results within the range 0.0 .. 1.0 are always
     *         returned as is.. A result &lt; 0.0 is subject to lowLimitHandling. A result &gt; 1.0 is subject to
     *         highLimitHandling
     * @throws DrawRuntimeException when any of the arguments is NaN
     */
    public double fractionalPositionOnLine(final double p1X, final double p1Y, final double p2X, final double p2Y,
            final Boolean lowLimitHandling, final Boolean highLimitHandling) throws DrawRuntimeException
    {
        double dX = p2X - p1X;
        double dY = p2Y - p1Y;
        Throw.when(Double.isNaN(dX) || Double.isNaN(dY), DrawRuntimeException.class, "NaN values not permitted");
        if (0 == dX && 0 == dY)
        {
            return 0.0;
        }
        double fraction = ((this.x - p1X) * dX + (this.y - p1Y) * dY) / (dX * dX + dY * dY);
        if (fraction < 0.0)
        {
            if (lowLimitHandling == null)
            {
                return Double.NaN;
            }
            if (lowLimitHandling)
            {
                fraction = 0.0;
            }
        }
        else if (fraction > 1.0)
        {
            if (highLimitHandling == null)
            {
                return Double.NaN;
            }
            if (highLimitHandling)
            {
                fraction = 1.0;
            }
        }
        return fraction;
    }

    /**
     * Project a point on a line segment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param p1X double; the x coordinate of the start point of the line segment
     * @param p1Y double; the y coordinate of the start point of the line segment
     * @param p2X double; the x coordinate of the end point of the line segment
     * @param p2Y double; the y coordinate of the end point of the line segment
     * @return P; either <cite>segmentPoint1</cite>, or <cite>segmentPoint2</cite> or a new Point2d that lies somewhere in
     *         between those two.
     */
    public final Point2d closestPointOnSegment(final double p1X, final double p1Y, final double p2X, final double p2Y)
    {
        return closestPointOnLine(p1X, p1Y, p2X, p2Y, true, true);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnLine(final Point2d linePoint1, final Point2d linePoint2)
            throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(linePoint1, "linePoint1 may not be null");
        Throw.whenNull(linePoint2, "linePoint2 may not be null");
        return closestPointOnLine(linePoint1.x, linePoint1.y, linePoint2.x, linePoint2.y);
    }

    /**
     * Project a point on a line. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param p1X double; the x coordinate of a point of the line segment
     * @param p1Y double; the y coordinate of a point of the line segment
     * @param p2X double; the x coordinate of another point of the line segment
     * @param p2Y double; the y coordinate of another point of the line segment
     * @return Point2d; a point on the line that goes through the points
     * @throws DrawRuntimeException when the points on the line are identical
     */
    public final Point2d closestPointOnLine(final double p1X, final double p1Y, final double p2X, final double p2Y)
            throws DrawRuntimeException
    {
        Throw.when(p1X == p2X && p1Y == p2Y, DrawRuntimeException.class, "degenerate line not allowed");
        return closestPointOnLine(p1X, p1Y, p2X, p2Y, false, false);
    }

    /**
     * Closest point on a ray.
     * @param ray Ray2d; the ray
     * @return Point2d; the point on the ray that is closest to this
     * @throws NullPointerException when ray is null
     */
    public final Point2d closestPointOnLine(final Ray2d ray) throws NullPointerException
    {
        return closestPointOnLine(ray.x, ray.y, ray.x + Math.cos(ray.phi), ray.y + Math.sin(ray.phi), true, false);
    }

    /**
     * Return the zero, one or two intersections between two circles. The circles must be different. Derived from pseudo code by
     * <a href="http://paulbourke.net/geometry/circlesphere/">Paul Bourke</a> and C implementation by
     * <a href="http://paulbourke.net/geometry/circlesphere/tvoght.c">Tim Voght </a>.
     * @param center1 Point2d; the center of circle 1
     * @param radius1 double; the radius of circle 1
     * @param center2 Point2d; the center of circle 2
     * @param radius2 double; the radius of circle 2
     * @return List&lt;Point2d&gt; a list of zero, one or two points
     * @throws NullPointerException when center1 or center2 is null
     * @throws DrawRuntimeException when the two circles are identical, or radius1 &lt; 0 or radius2 &lt; 0
     */
    public static final List<Point2d> circleIntersections(final Point2d center1, final double radius1, final Point2d center2,
            final double radius2) throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(center1, "center1 may not be null");
        Throw.whenNull(center2, "center2 may not be null");
        Throw.when(radius1 < 0 || radius2 < 0, DrawRuntimeException.class, "radius may not be less than 0");
        Throw.when(center1.equals(center2) && radius1 == radius2, DrawRuntimeException.class, "Circles must be different");
        List<Point2d> result = new ArrayList<>();
        // dX,dY is the vector from center1 to center2
        double dX = center2.x - center1.x;
        double dY = center2.y - center1.y;
        double distance = Math.hypot(dX, dY);
        if (distance > radius1 + radius2 || distance < Math.abs(radius1 - radius2))
        {
            return result;
        }
        double a = (radius1 * radius1 - radius2 * radius2 + distance * distance) / (2 * distance);
        // x2,y2 is the point where the line through the circle intersections crosses the line through the circle centers
        double x2 = center1.x + (dX * a / distance);
        double y2 = center1.y + (dY * a / distance);
        // h is distance from x2,y2 to each of the solutions
        double h = Math.sqrt(radius1 * radius1 - a * a);
        // rX, rY is vector from x2,y2 to the first solution
        double rX = -dY * (h / distance);
        double rY = dX * (h / distance);
        result.add(new Point2d(x2 + rX, y2 + rY));
        if (h > 0)
        {
            // Two distinct solutions; add the second one
            result.add(new Point2d(x2 - rX, y2 - rY));
        }
        return result;
    }

    /**
     * Return the direction to another Point2d.
     * @param otherPoint Point2d; the other point
     * @return double; the direction to the other point in Radians (towards infinite X is 0; towards infinite Y is &pi; / 2;
     *         etc.). If the points are identical; this method returns NaN.
     */
    public double directionTo(final Point2d otherPoint)
    {
        return Math.atan2(otherPoint.y - this.y, otherPoint.x - this.x);
    }

    /**
     * Return the coordinates as an AWT Point2D.Double object.
     * @return Point2D; the coordinates as an AWT Point2D.Double object
     */
    public Point2D toPoint2D()
    {
        return new Point2D.Double(this.x, this.y);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return toString("%f");
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s]", doNotIncludeClassName ? "" : "Point2d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:needbraces")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point2d other = (Point2d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

}
