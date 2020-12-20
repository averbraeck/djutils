package org.djutils.draw.line;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.Space2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;

/**
 * Implementation of Line for 2D space.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PolyLine2d implements Drawable2d, PolyLine<PolyLine2d, Point2d, Space2d, DirectedPoint2d>
{
    /** */
    private static final long serialVersionUID = 20200911L;

    /** The points of the line. */
    private final Point2d[] points;

    /** The cumulative length of the line at point 'i'. */
    private final double[] lengthIndexedLine;

    /** The length. */
    private final double length;

    /** Bounding rectangle of this Line2d. */
    private final Bounds2d bounds;

    /**
     * Construct a new Line2D from an array of Point2d.
     * @param copyNeeded boolean; if true; a deep copy of the points array is stored instead of the provided array
     * @param points Point2d...; the array of points to construct this Line2d from.
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    private PolyLine2d(final boolean copyNeeded, final Point2d[] points) throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(points, "points may not be null");
        Throw.when(points.length < 2, DrawRuntimeException.class, "Need at least two points");
        this.points = copyNeeded ? Arrays.copyOf(points, points.length) : points;
        Point2d prevPoint = points[0];
        double minX = prevPoint.getX();
        double minY = prevPoint.getY();
        double maxX = prevPoint.getX();
        double maxY = prevPoint.getY();
        this.lengthIndexedLine = new double[points.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < points.length; i++)
        {
            Point2d point = points[i];
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
            if (prevPoint.getX() == point.getX() && prevPoint.getY() == point.getY())
            {
                throw new DrawRuntimeException("Degenerate Line2d; point " + (i - 1) + " has the same x and y as point " + i);
            }
            this.lengthIndexedLine[i] = this.lengthIndexedLine[i - 1] + prevPoint.distance(point);
            prevPoint = point;
        }
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.bounds = new Bounds2d(getPoints());
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d.
     * @param point1 Point2d; starting point of the PolyLine2d
     * @param point2 Point2d; second point of the PolyLine2d
     * @param otherPoints Point2d...; additional points of the PolyLine2d
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Point2d point1, final Point2d point2, final Point2d... otherPoints)
            throws NullPointerException, DrawRuntimeException
    {
        this(false, spliceArray(point1, point2, otherPoints));
    }

    /**
     * Construct an array of Point2d from two points plus an array of Point2d.
     * @param point1 Point2d; the first point (ends up at index 0 of the result)
     * @param point2 Point2d; the second point (ends up at index 1 of the result)
     * @param otherPoints Point2d[]; may be null, may be empty. If non empty, the elements in otherPoints end up at index 2 and
     *            up in the result
     * @return Point2d[]; the combined array
     */
    private static Point2d[] spliceArray(final Point2d point1, final Point2d point2, final Point2d... otherPoints)
    {
        Point2d[] result = new Point2d[2 + (otherPoints == null ? 0 : otherPoints.length)];
        result[0] = point1;
        result[1] = point2;
        if (otherPoints != null)
        {
            for (int i = 0; i < otherPoints.length; i++)
            {
                result[i + 2] = otherPoints[i];
            }
        }
        return result;
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d.
     * @param points Point2d[]; points of the PolyLine2d
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Point2d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(true, checkLengthIsTwoOrMore(Throw.whenNull(points, "points may not be null")));
    }

    /**
     * Check that the length of an array of Point2d is at least two.
     * @param points Point2d[]; the array of points to check
     * @return Point2d[]; points
     * @throws DrawRuntimeException when the length of points is less than two
     */
    private static Point2d[] checkLengthIsTwoOrMore(final Point2d[] points) throws DrawRuntimeException
    {
        Throw.when(points.length < 2, DrawRuntimeException.class, "Need at least two points");
        return points;
    }

    /**
     * Construct a new Line2d and initialize its length indexed line, bounds, centroid and length.
     * @param iterator Iterator&lt;Point2d&gt;; iterator that will provide all points that constitute the new Line2d
     * @throws NullPointerException when iterator is null
     * @throws DrawException when the iterator provides too few points, or some adjacent identical points)
     */
    public PolyLine2d(final Iterator<Point2d> iterator) throws NullPointerException, DrawException
    {
        this(iteratorToList(Throw.whenNull(iterator, "iterator cannot be null")));
    }

    /**
     * Construct a new Line2d from a List&lt;Point2d&gt;.
     * @param pointList List&lt;Point2d&gt;; the list of points to construct this Line2d from.
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final List<Point2d> pointList) throws DrawRuntimeException
    {
        this(false, pointList.toArray(new Point2d[pointList.size()]));
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d instantiate(List<Point2d> pointList) throws NullPointerException, DrawRuntimeException
    {
        return new PolyLine2d(pointList);
    }

    /**
     * Construct a new Line2d (closed shape) from a Path2D.
     * @param path Path2D; the Path2D to construct this Line2d from.
     * @throws DrawException when the provided points do not constitute a valid line (too few points or identical adjacent
     *             points)
     */
    public PolyLine2d(final Path2D path) throws DrawException
    {
        this(false, path2DtoArray(path));
    }

    /**
     * Convert a path2D to a Point2d[] array to construct the line.
     * @param path Path2D; the path to convert
     * @return Point2d[]; an array of points based on MOVETO and LINETO elements of the Path2D
     * @throws DrawException when the pathIterator of the path returns an unsupported command
     */
    private static Point2d[] path2DtoArray(final Path2D path) throws DrawException
    {
        List<Point2d> result = new ArrayList<>();
        for (PathIterator pi = path.getPathIterator(null); !pi.isDone(); pi.next())
        {
            double[] p = new double[6];
            int segType = pi.currentSegment(p);
            if (segType == PathIterator.SEG_MOVETO || segType == PathIterator.SEG_LINETO)
            {
                result.add(new Point2d(p[0], p[1]));
            }
            else if (segType == PathIterator.SEG_CLOSE)
            {
                if (!result.get(0).equals(result.get(result.size() - 1)))
                {
                    result.add(result.get(0));
                }
                break;
            }
            else
            {
                throw new DrawException("path2DtoArray only handles SEG_MOVETO, SEG_LINETO and SEG_CLOSE");
            }
        }
        return result.toArray(new Point2d[result.size() - 1]);
    }

    /**
     * Build a list from the Point2d objects that an iterator provides.
     * @param iterator Iterator&lt;Point2d&gt;; the iterator that will provide the points
     * @return List&lt;Point2d&gt;; a list of the points provided by the iterator
     */
    private static List<Point2d> iteratorToList(final Iterator<Point2d> iterator)
    {
        List<Point2d> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.points.length;
    }

    /** {@inheritDoc} */
    @Override
    public final Point2d get(final int i) throws IndexOutOfBoundsException
    {
        return this.points[i];
    }

    /** {@inheritDoc} */
    @Override
    public final double lengthAtIndex(final int index)
    {
        return this.lengthIndexedLine[index];
    }

    /** {@inheritDoc} */
    @Override
    public final double getLength()
    {
        return this.length;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point2d> getPoints()
    {
        return Arrays.stream(this.points).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return this.bounds;
    }

    /**
     * Construct a new Line2d that is equal to this line except for segments that are shorter than the <cite>noiseLevel</cite>.
     * The result is guaranteed to start with the first point of this line and end with the last point of this line.
     * @param noiseLevel double; the minimum segment length that is <b>not</b> removed
     * @return Line2d; the filtered line
     */
    public final PolyLine2d noiseFilteredLine(final double noiseLevel)
    {
        if (this.size() <= 2)
        {
            return this; // Except for some cached fields; an Line2d is immutable; so safe to return
        }
        Point2d prevPoint = null;
        List<Point2d> list = null;
        for (int index = 0; index < this.size(); index++)
        {
            Point2d currentPoint = this.points[index];
            if (null != prevPoint && prevPoint.distance(currentPoint) < noiseLevel)
            {
                if (null == list)
                {
                    // Found something to filter; copy this up to (and including) prevPoint
                    list = new ArrayList<>();
                    for (int i = 0; i < index; i++)
                    {
                        list.add(this.points[i]);
                    }
                }
                if (index == this.size() - 1)
                {
                    if (list.size() > 1)
                    {
                        // Replace the last point of the result by the last point of this Line2d
                        list.set(list.size() - 1, currentPoint);
                    }
                    else
                    {
                        // Append the last point of this even though it is close to the first point than the noise value to
                        // comply with the requirement that first and last point of this are ALWAYS included in the result.
                        list.add(currentPoint);
                    }
                }
                continue; // Do not replace prevPoint by currentPoint
            }
            else if (null != list)
            {
                list.add(currentPoint);
            }
            prevPoint = currentPoint;
        }
        if (null == list)
        {
            return this;
        }
        if (list.size() == 2 && list.get(0).equals(list.get(1)))
        {
            // Insert point 1 of this; it MUST be different from point 0; so we don't have to test for anything.
            list.add(1, this.points[1]);
        }
        try
        {
            return new PolyLine2d(list);
        }
        catch (DrawRuntimeException exception)
        {
            // Cannot happen
            CategoryLogger.always().error(exception);
            throw new Error(exception);
        }
    }

    /**
     * Concatenate several Line2d instances.
     * @param lines Line2d...; Line2d... one or more Line2d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match
     *            the first of the second, etc.
     * @return Line2d
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final PolyLine2d... lines) throws DrawException
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two Line2d instances. This method is separate for efficiency reasons.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param line1 Line2d; first line
     * @param line2 Line2d; second line
     * @return Line2d; the concatenation of the two lines
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final double tolerance, final PolyLine2d line1, final PolyLine2d line2)
            throws DrawException
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new DrawException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst() + " distance is "
                    + line1.getLast().distance(line2.getFirst()) + " > " + tolerance);
        }
        int size = line1.size() + line2.size() - 1;
        Point2d[] points = new Point2d[size];
        int nextIndex = 0;
        for (int j = 0; j < line1.size(); j++)
        {
            points[nextIndex++] = line1.get(j);
        }
        for (int j = 1; j < line2.size(); j++)
        {
            points[nextIndex++] = line2.get(j);
        }
        return new PolyLine2d(false, points);
    }

    /**
     * Concatenate several Line2d instances.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param lines Line2d...; Line2d... one or more Line2d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match
     *            the first of the second, etc.
     * @return Line2d; the concatenation of the lines
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final double tolerance, final PolyLine2d... lines) throws DrawException
    {
        if (0 == lines.length)
        {
            throw new DrawException("Empty argument list");
        }
        else if (1 == lines.length)
        {
            return lines[0];
        }
        int size = lines[0].size();
        for (int i = 1; i < lines.length; i++)
        {
            if (lines[i - 1].getLast().distance(lines[i].getFirst()) > tolerance)
            {
                throw new DrawException("Lines are not connected: " + lines[i - 1].getLast() + " to " + lines[i].getFirst()
                        + " distance is " + lines[i - 1].getLast().distance(lines[i].getFirst()) + " > " + tolerance);
            }
            size += lines[i].size() - 1;
        }
        Point2d[] points = new Point2d[size];
        int nextIndex = 0;
        for (int i = 0; i < lines.length; i++)
        {
            PolyLine2d line = lines[i];
            for (int j = 0 == i ? 0 : 1; j < line.size(); j++)
            {
                points[nextIndex++] = line.get(j);
            }
        }
        return new PolyLine2d(false, points);
    }

    /**
     * Create a new Line2d, filtering out repeating successive points.
     * @param points Point2d...; the coordinates of the line as Point2d
     * @return the line
     * @throws DrawException when number of points &lt; 2
     */
    public static PolyLine2d createAndCleanPolyLine2d(final Point2d... points) throws DrawException
    {
        if (points.length < 2)
        {
            throw new DrawException("Degenerate Line2d; has " + points.length + " point" + (points.length != 1 ? "s" : ""));
        }
        return createAndCleanPolyLine2d(new ArrayList<>(Arrays.asList(points)));
    }

    /**
     * Create an Line2d, while filtering out repeating successive points.
     * @param pointList List&lt;Point2d&gt;; list of the coordinates of the line as Point2d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @return Line2d; the line
     * @throws DrawException when number of non-equal points &lt; 2
     */
    public static PolyLine2d createAndCleanPolyLine2d(final List<Point2d> pointList) throws DrawException
    {
        // TODO avoid modifying the input list.
        // clean successive equal points
        int i = 1;
        while (i < pointList.size())
        {
            if (pointList.get(i - 1).equals(pointList.get(i)))
            {
                pointList.remove(i);
            }
            else
            {
                i++;
            }
        }
        return new PolyLine2d(pointList);
    }

    /** {@inheritDoc} */
    @Override
    public final DirectedPoint2d getLocationExtended(final double position)
    {
        if (position >= 0.0 && position <= getLength())
        {
            try
            {
                return getLocation(position);
            }
            catch (DrawException exception)
            {
                // cannot happen
            }
        }

        // position before start point -- extrapolate using direction from first point to second point of this Line2d
        if (position < 0.0)
        {
            double len = position;
            double fraction = len / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            Point2d p1 = this.points[0];
            Point2d p2 = this.points[1];
            return new DirectedPoint2d(p1.getX() + fraction * (p2.getX() - p1.getX()),
                    p1.getY() + fraction * (p2.getY() - p1.getY()), Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        }

        // position beyond end point -- extrapolate using the direction from the before last point to the last point of this
        // Line2d
        int n1 = this.lengthIndexedLine.length - 1;
        int n2 = this.lengthIndexedLine.length - 2;
        double len = position - getLength();
        double fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        while (Double.isInfinite(fraction))
        {
            if (--n2 < 0)
            {
                CategoryLogger.always().error("lengthIndexedLine of {} is invalid", this);
                Point2d p = this.points[n1];
                return new DirectedPoint2d(p.getX(), p.getY(), 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        Point2d p1 = this.points[n2];
        Point2d p2 = this.points[n1];
        return new DirectedPoint2d(p2.getX() + fraction * (p2.getX() - p1.getX()),
                p2.getY() + fraction * (p2.getY() - p1.getY()), Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
    }

    /** {@inheritDoc} */
    @Override
    public final DirectedPoint2d getLocation(final double position) throws DrawException
    {
        if (position < 0.0 || position > getLength())
        {
            throw new DrawException("getLocation for line: position < 0.0 or > line length. Position = " + position
                    + "; length = " + getLength());
        }

        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            Point2d p1 = this.points[0];
            Point2d p2 = this.points[1];
            return new DirectedPoint2d(p1.getX(), p1.getY(), Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        }
        if (position == getLength())
        {
            Point2d p1 = this.points[this.points.length - 2];
            Point2d p2 = this.points[this.points.length - 1];
            return new DirectedPoint2d(p2.getX(), p2.getY(), Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        }

        // find the index of the line segment, use binary search
        int index = find(position);
        double remainder = position - this.lengthIndexedLine[index];
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        Point2d p1 = this.points[index];
        Point2d p2 = this.points[index + 1];
        return new DirectedPoint2d(p1.getX() + fraction * (p2.getX() - p1.getX()),
                p1.getY() + fraction * (p2.getY() - p1.getY()), Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
    }

    /**
     * Returns the fractional position along this line of the orthogonal projection of a point on this line. If the point is not
     * orthogonal to the closest line segment, the nearest point is selected.
     * @param point Point2d; the point to project
     * @return fractional position along this line of the orthogonal projection on this line of a point
     */
    public final double projectOrthogonal(final Point2d point)
    {
        return projectOrthogonal(point.getX(), point.getY());
    }

    /**
     * Returns the fractional position along this line of the orthogonal projection of point (x, y) on this line. If the point
     * is not orthogonal to the closest line segment, the nearest point is selected.
     * @param x double; x-coordinate of point to project
     * @param y double; y-coordinate of point to project
     * @return fractional position along this line of the orthogonal projection on this line of a point
     */
    public final double projectOrthogonal(final double x, final double y)
    {
        // prepare
        double minDistance = Double.POSITIVE_INFINITY;
        double minSegmentFraction = 0;
        int minSegment = -1;

        // code based on Line2D.ptSegDistSq(...)
        for (int i = 0; i < size() - 1; i++)
        {
            double dx = this.points[i + 1].getX() - this.points[i].getX();
            double dy = this.points[i + 1].getY() - this.points[i].getY();
            // vector relative to (x(i), y(i))
            double px = x - this.points[i].getX();
            double py = y - this.points[i].getY();
            // dot product
            double dot1 = px * dx + py * dy;
            double f;
            double distance;
            if (dot1 > 0)
            {
                // vector relative to (x(i+1), y(i+1))
                px = dx - px;
                py = dy - py;
                // dot product
                double dot2 = px * dx + py * dy;
                if (dot2 > 0)
                {
                    // projection on line segment
                    double len2 = dx * dx + dy * dy;
                    double proj = dot2 * dot2 / len2;
                    f = dot1 / len2;
                    distance = px * px + py * py - proj;
                }
                else
                {
                    // dot<=0 projection 'after' line segment
                    f = 1;
                    distance = px * px + py * py;
                }
            }
            else
            {
                // dot<=0 projection 'before' line segment
                f = 0;
                distance = px * px + py * py;
            }
            // check if closer than previous
            if (distance < minDistance)
            {
                minDistance = distance;
                minSegmentFraction = f;
                minSegment = i;
            }
        }

        // return
        double segLen = this.lengthIndexedLine[minSegment + 1] - this.lengthIndexedLine[minSegment];
        return (this.lengthIndexedLine[minSegment] + segLen * minSegmentFraction) / getLength();
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d extract(final double start, final double end) throws DrawException
    {
        if (Double.isNaN(start) || Double.isNaN(end) || start < 0 || start >= end || end > getLength())
        {
            throw new DrawException(
                    "Bad interval (" + start + ".." + end + "; length of this Line2d is " + this.getLength() + ")");
        }
        double cumulativeLength = 0;
        double nextCumulativeLength = 0;
        double segmentLength = 0;
        int index = 0;
        List<Point2d> pointList = new ArrayList<>();
        while (start > cumulativeLength)
        {
            Point2d fromPoint = get(index);
            index++;
            Point2d toPoint = get(index);
            segmentLength = fromPoint.distance(toPoint);
            cumulativeLength = nextCumulativeLength;
            nextCumulativeLength = cumulativeLength + segmentLength;
            if (nextCumulativeLength >= start)
            {
                break;
            }
        }
        if (start == nextCumulativeLength)
        {
            pointList.add(get(index));
        }
        else
        {
            pointList.add(get(index - 1).interpolate(get(index), (start - cumulativeLength) / segmentLength));
            if (end > nextCumulativeLength)
            {
                pointList.add(get(index));
            }
        }
        while (end > nextCumulativeLength)
        {
            Point2d fromPoint = get(index);
            index++;
            if (index >= size())
            {
                break; // rounding error
            }
            Point2d toPoint = get(index);
            segmentLength = fromPoint.distance(toPoint);
            cumulativeLength = nextCumulativeLength;
            nextCumulativeLength = cumulativeLength + segmentLength;
            if (nextCumulativeLength >= end)
            {
                break;
            }
            pointList.add(toPoint);
        }
        if (end == nextCumulativeLength)
        {
            pointList.add(get(index));
        }
        else
        {
            Point2d point = get(index - 1).interpolate(get(index), (end - cumulativeLength) / segmentLength);
            // can be the same due to rounding
            if (!point.equals(pointList.get(pointList.size() - 1)))
            {
                pointList.add(point);
            }
        }
        try
        {
            return instantiate(pointList);
        }
        catch (DrawRuntimeException exception)
        {
            CategoryLogger.always().error(exception, "interval " + start + ".." + end + " too short");
            throw new DrawException("interval " + start + ".." + end + "too short");
        }
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d truncate(final double position) throws DrawException
    {
        if (position <= 0.0 || position > getLength())
        {
            throw new DrawException("truncate for line: position <= 0.0 or > line length. Position = " + position
                    + ". Length = " + getLength() + " m.");
        }

        // handle special case: position == length
        if (position == getLength())
        {
            return this;
        }

        // find the index of the line segment
        int index = find(position);
        double remainder = position - lengthAtIndex(index);
        double fraction = remainder / (lengthAtIndex(index + 1) - lengthAtIndex(index));
        Point2d p1 = get(index);
        Point2d lastPoint;
        if (0.0 == fraction)
        {
            index--;
            lastPoint = p1;
        }
        else
        {
            Point2d p2 = get(index + 1);
            lastPoint = p1.interpolate(p2, fraction);

        }
        // FIXME: Cannot create a P[]; will have to do it with a List<P>
        List<Point2d> coords = new ArrayList<>(index + 2);
        for (int i = 0; i <= index; i++)
        {
            coords.add(get(i));
        }
        coords.add(lastPoint);
        return instantiate(coords);
    }

    /** Default precision of approximation of arcs in the offsetLine method. */
    public static final double DEFAULT_CIRCLE_PRECISION = 0.001;

    /** By default, noise in the reference line of the offsetLine method less than this value is always filtered. */
    public static double DEFAULT_OFFSET_MINIMUM_FILTER_VALUE = 0.001;

    /** By default, noise in the reference line of the offsetLineMethod greater than this value is never filtered. */
    public static double DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE = 0.1;

    /**
     * By default, noise in the reference line of the offsetLineMethod less than <cite>offset / offsetFilterRatio</cite> is
     * filtered except when the resulting value exceeds <cite>offsetMaximumFilterValue</cite>.
     */
    public static double DEFAULT_OFFSET_FILTER_RATIO = 10;

    /** By default, the offsetLineMethod uses this offset precision. */
    public static double DEFAULT_OFFSET_PRECISION = 0.00001;

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks around the end points. This method
     * tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise in
     * <cite>this</cite> (the reference line) can cause major artifacts in the offset line. This method calls the underlying
     * method with default values for circlePrecision (<cite>DEFAULT_OFFSET</cite>), offsetMinimumFilterValue
     * (<cite>DEFAULT_OFFSET_MINIMUM_FILTER_VALUE</cite>), offsetMaximumFilterValue
     * (<cite>DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE</cite>), offsetFilterRatio (<cite>DEFAULT_OFFSET_FILTER_RATIO</cite>),
     * minimumOffset (<cite>DEFAULT_OFFSET_PRECISION</cite>).
     * @param offset double; the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @return PolyLine2d; a line at the specified offset from the reference line
     */
    public PolyLine2d offsetLine(final double offset)
    {
        return offsetLine(offset, DEFAULT_CIRCLE_PRECISION, DEFAULT_OFFSET_MINIMUM_FILTER_VALUE, DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE,
                DEFAULT_OFFSET_FILTER_RATIO, DEFAULT_OFFSET_PRECISION);
    }

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks around the end points. This method
     * tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise in
     * <cite>this</cite> (the reference line) can cause major artifacts in the offset line.
     * @param offset double; the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @param circlePrecision double; precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue double; noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue double; noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio double; noise in the reference line less than <cite>offset / offsetFilterRatio</cite> is
     *            filtered except when the resulting value exceeds <cite>offsetMaximumFilterValue</cite>
     * @param minimumOffset double; an offset value less than this value is treated as 0.0
     * @return PolyLine2d; a line at the specified offset from the reference line
     * @throws IllegalArgumentException when offset is NaN, or circlePrecision, offsetMinimumFilterValue,
     *             offsetMaximumfilterValue, offsetFilterRatio, or minimumOffset is not positive, or NaN, or
     *             offsetMinimumFilterValue &gt;= offsetMaximumFilterValue
     */
    public PolyLine2d offsetLine(final double offset, final double circlePrecision, final double offsetMinimumFilterValue,
            final double offsetMaximumFilterValue, final double offsetFilterRatio, final double minimumOffset)
            throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(offset), IllegalArgumentException.class, "Offset may not be NaN");
        Throw.when(Double.isNaN(circlePrecision) || circlePrecision <= 0, IllegalArgumentException.class,
                "bad circlePrecision");
        Throw.when(Double.isNaN(offsetMinimumFilterValue) || offsetMinimumFilterValue <= 0, IllegalArgumentException.class,
                "bad offsetMinimumFilterValue");
        Throw.when(Double.isNaN(offsetMaximumFilterValue) || offsetMaximumFilterValue <= 0, IllegalArgumentException.class,
                "bad offsetMaximumFilterValue");
        Throw.when(Double.isNaN(offsetFilterRatio) || offsetFilterRatio <= 0, IllegalArgumentException.class,
                "bad offsetFilterRatio");
        Throw.when(Double.isNaN(minimumOffset) || minimumOffset <= 0, IllegalArgumentException.class, "bad minimumOffset");
        Throw.when(offsetMinimumFilterValue >= offsetMaximumFilterValue, IllegalArgumentException.class,
                "bad offset filter values; minimum must be less than maximum");
        double bufferOffset = Math.abs(offset);
        if (bufferOffset < minimumOffset)
        {
            return this;
        }

        PolyLine2d filteredReferenceLine = noiseFilteredLine(
                Math.max(offsetMinimumFilterValue, Math.min(bufferOffset / offsetFilterRatio, offsetMaximumFilterValue)));
        List<Point2d> tempPoints = new ArrayList<>();
        // Make good use of the fact that an OTSLine3D cannot have consecutive duplicate points and has > 1 points
        Point2d prevPoint = filteredReferenceLine.get(0);
        Double prevAngle = null;
        for (int index = 0; index < filteredReferenceLine.size() - 1; index++)
        {
            Point2d nextPoint = filteredReferenceLine.get(index + 1);
            double angle = Math.atan2(nextPoint.getY() - prevPoint.getY(), nextPoint.getX() - prevPoint.getX());
            Point2d segmentFrom =
                    new Point2d(prevPoint.getX() - Math.sin(angle) * offset, prevPoint.getY() + Math.cos(angle) * offset);
            Point2d segmentTo =
                    new Point2d(nextPoint.getX() - Math.sin(angle) * offset, nextPoint.getY() + Math.cos(angle) * offset);
            boolean addSegment = true;
            if (index > 0)
            {
                double deltaAngle = angle - prevAngle;
                if (Math.abs(deltaAngle) > Math.PI)
                {
                    deltaAngle -= Math.signum(deltaAngle) * 2 * Math.PI;
                }
                if (deltaAngle * offset <= 0)
                {
                    // Outside of curve of reference line
                    // Approximate an arc using straight segments.
                    // Determine how many segments are needed.
                    int numSegments = 1;
                    if (Math.abs(deltaAngle) > Math.PI / 2)
                    {
                        numSegments = 2;
                    }
                    while (true)
                    {
                        double maxError = bufferOffset * (1 - Math.abs(Math.cos(deltaAngle / numSegments / 2)));
                        if (maxError < circlePrecision)
                        {
                            break; // required precision reached
                        }
                        numSegments *= 2;
                    }
                    Point2d prevArcPoint = tempPoints.get(tempPoints.size() - 1);
                    // Generate the intermediate points
                    for (int additionalPoint = 1; additionalPoint < numSegments; additionalPoint++)
                    {
                        double intermediateAngle =
                                (additionalPoint * angle + (numSegments - additionalPoint) * prevAngle) / numSegments;
                        if (prevAngle * angle < 0 && Math.abs(prevAngle) > Math.PI / 2 && Math.abs(angle) > Math.PI / 2)
                        {
                            intermediateAngle += Math.PI;
                        }
                        Point2d intermediatePoint = new Point2d(prevPoint.getX() - Math.sin(intermediateAngle) * offset,
                                prevPoint.getY() + Math.cos(intermediateAngle) * offset);
                        // Find any intersection points of the new segment and all previous segments
                        Point2d prevSegFrom = null;
                        int stopAt = tempPoints.size();
                        for (int i = 0; i < stopAt; i++)
                        {
                            Point2d prevSegTo = tempPoints.get(i);
                            if (null != prevSegFrom)
                            {
                                Point2d prevSegIntersection = Point2d.intersectionOfLineSegments(prevArcPoint,
                                        intermediatePoint, prevSegFrom, prevSegTo);
                                if (null != prevSegIntersection && prevSegIntersection.distance(prevArcPoint) > circlePrecision
                                        && prevSegIntersection.distance(prevSegFrom) > circlePrecision
                                        && prevSegIntersection.distance(prevSegTo) > circlePrecision)
                                {
                                    tempPoints.add(prevSegIntersection);
                                    // System.out.println(new OTSLine3D(tempPoints).toPlot());
                                }
                            }
                            prevSegFrom = prevSegTo;
                        }
                        Point2d nextSegmentIntersection =
                                Point2d.intersectionOfLineSegments(prevSegFrom, intermediatePoint, segmentFrom, segmentTo);
                        if (null != nextSegmentIntersection)
                        {
                            tempPoints.add(nextSegmentIntersection);
                            // System.out.println(new OTSLine3D(tempPoints).toPlot());
                        }
                        tempPoints.add(intermediatePoint);
                        // System.out.println(new OTSLine3D(tempPoints).toPlot());
                        prevArcPoint = intermediatePoint;
                    }
                }
                // Inside of curve of reference line.
                // Add the intersection point of each previous segment and the next segment
                Point2d pPoint = null;
                int currentSize = tempPoints.size(); // PK DO NOT use the "dynamic" limit
                for (int i = 0; i < currentSize /* tempPoints.size() */; i++)
                {
                    Point2d p = tempPoints.get(i);
                    if (null != pPoint)
                    {
                        double pAngle = Math.atan2(p.getY() - pPoint.getY(), p.getX() - pPoint.getX());
                        double angleDifference = angle - pAngle;
                        if (Math.abs(angleDifference) > Math.PI)
                        {
                            angleDifference -= Math.signum(angleDifference) * 2 * Math.PI;
                        }
                        if (Math.abs(angleDifference) > 0)// 0.01)
                        {
                            Point2d intersection = Point2d.intersectionOfLineSegments(pPoint, p, segmentFrom, segmentTo);
                            if (null != intersection)
                            {
                                if (tempPoints.size() - 1 == i)
                                {
                                    tempPoints.remove(tempPoints.size() - 1);
                                    segmentFrom = intersection;
                                }
                                else
                                {
                                    tempPoints.add(intersection);
                                }
                                // tempPoints.set(tempPoints.size() - 1, intermediatePoint);
                                if (tempPoints.size() > 1)
                                {
                                    // System.out.println(new OTSLine3D(tempPoints).toPlot());
                                }
                            }
                        }
                        else
                        {
                            // This is where things went very wrong in the TestGeometry demo.
                            if (i == tempPoints.size() - 1)
                            {
                                tempPoints.remove(tempPoints.size() - 1);
                                segmentFrom = tempPoints.get(tempPoints.size() - 1);
                                tempPoints.remove(tempPoints.size() - 1);
                            }
                        }
                    }
                    pPoint = p;
                }
            }
            if (addSegment)
            {
                tempPoints.add(segmentFrom);
                tempPoints.add(segmentTo);
                prevPoint = nextPoint;
                prevAngle = angle;
            }
        }
        // Remove points that are closer than the specified offset
        for (int index = 1; index < tempPoints.size() - 1; index++)
        {
            Point2d checkPoint = tempPoints.get(index);
            prevPoint = null;
            boolean tooClose = false;
            boolean somewhereAtCorrectDistance = false;
            for (int i = 0; i < filteredReferenceLine.size(); i++)
            {
                Point2d p = filteredReferenceLine.get(i);
                if (null != prevPoint)
                {
                    Point2d closestPoint = checkPoint.closestPointOnSegment(prevPoint, p);
                    double distance = closestPoint.distance(checkPoint);
                    if (distance < bufferOffset - circlePrecision)
                    {
                        tooClose = true;
                        break;
                    }
                    else if (distance < bufferOffset + minimumOffset)
                    {
                        somewhereAtCorrectDistance = true;
                    }
                }
                prevPoint = p;
            }
            if (tooClose || !somewhereAtCorrectDistance)
            {
                tempPoints.remove(index);
                index--;
            }
        }
        try
        {
            return PolyLine2d.createAndCleanPolyLine2d(tempPoints);
        }
        catch (DrawException exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "PolyLine2d [points=" + Arrays.toString(this.points) + "]";
    }

    /**
     * Convert this PolyLine2d to something that MS-Excel can plot.
     * @return excel XY plottable output
     */
    public final String toExcel()
    {
        StringBuffer s = new StringBuffer();
        for (Point2d p : this.points)
        {
            s.append(p.getX() + "\t" + p.getY() + "\n");
        }
        return s.toString();
    }

    /**
     * Convert this PolyLine3D to Peter's plot format.
     * @return Peter's format plot output
     */
    public final String toPlot()
    {
        StringBuffer result = new StringBuffer();
        for (Point2d p : this.points)
        {
            result.append(String.format(Locale.US, "%s%.3f,%.3f", 0 == result.length() ? "M" : " L", p.getX(), p.getY()));
        }
        result.append("\n");
        return result.toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.points);
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "checkstyle:designforextension", "checkstyle:needbraces" })
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PolyLine2d other = (PolyLine2d) obj;
        if (!Arrays.equals(this.points, other.points))
            return false;
        return true;
    }

}
