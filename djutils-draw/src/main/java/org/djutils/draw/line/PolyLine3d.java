package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.Space3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.OrientedPoint3d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;

/**
 * Implementation of Line for 3D space.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PolyLine3d implements Drawable3d, PolyLine<PolyLine3d, Point3d, Space3d, OrientedPoint3d>
{
    /** */
    private static final long serialVersionUID = 20200911L;

    /** The points of the line. */
    private final Point3d[] points;

    /** The cumulative length of the line at point 'i'. */
    private final double[] lengthIndexedLine;

    /** The length. */
    private final double length;

    /** Bounding box of this Line3d. */
    private final Bounds3d bounds;

    /**
     * Construct a new Line3d and initialize its length indexed line, bounds, centroid and length.
     * @param copyNeeded boolean; if true; a deep copy of the points array is stored instead of the provided array
     * @param points Point3d[]; the array of points to construct this Line3d from.
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    private PolyLine3d(final boolean copyNeeded, final Point3d[] points) throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(points, "points cannot be null");
        Throw.when(points.length < 2, DrawRuntimeException.class, "Need at least two points");
        this.points = copyNeeded ? Arrays.copyOf(points, points.length) : points;
        Point3d prevPoint = points[0];
        double minX = prevPoint.x;
        double minY = prevPoint.y;
        double minZ = prevPoint.z;
        double maxX = prevPoint.x;
        double maxY = prevPoint.y;
        double maxZ = prevPoint.z;
        this.lengthIndexedLine = new double[this.points.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < this.points.length; i++)
        {
            Point3d point = this.points[i];
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            minZ = Math.min(minZ, point.z);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
            maxZ = Math.max(maxZ, point.z);
            if (prevPoint.x == point.x && prevPoint.y == point.y && prevPoint.z == point.z)
            {
                throw new DrawRuntimeException(
                        "Degenerate Line3d; point " + (i - 1) + " has the same x, y and z as point " + i);
            }
            this.lengthIndexedLine[i] = this.lengthIndexedLine[i - 1] + prevPoint.distance(point);
            prevPoint = point;
        }
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.bounds = new Bounds3d(minX, maxX, minY, maxY, minZ, maxZ);
    }

    /**
     * Construct a new PolyLine3d from an array of Point3d.
     * @param point1 Point3d; starting point of the PolyLine3d
     * @param point2 Point3d; second point of the PolyLine3d
     * @param otherPoints Point3d...; additional points of the PolyLine3d
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final Point3d point1, final Point3d point2, final Point3d... otherPoints)
            throws NullPointerException, DrawRuntimeException
    {
        this(false, spliceArray(point1, point2, otherPoints));
    }

    /**
     * Construct an array of Point3d from two points plus an array of Point3d.
     * @param point1 Point3d; the first point (ends up at index 0 of the result)
     * @param point2 Point3d; the second point (ends up at index 1 of the result)
     * @param otherPoints Point3d...; may be null, may be empty. If non empty, the elements in otherPoints end up at index 2 and
     *            up in the result
     * @return Point2d[]; the combined array
     */
    private static Point3d[] spliceArray(final Point3d point1, final Point3d point2, final Point3d... otherPoints)
    {
        Point3d[] result = new Point3d[2 + (otherPoints == null ? 0 : otherPoints.length)];
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
     * Construct a new PolyLine3d from an array of Point3d.
     * @param points Point3d[]; points of the PolyLine3d
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final Point3d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(true, checkLengthIsTwoOrMore(Throw.whenNull(points, "points may not be null")));
    }

    /**
     * Check that the length of an array of Point3d is at least two.
     * @param points Point3d[]; the array of points to check
     * @return Point3d[]; points
     * @throws DrawRuntimeException when the length of points is less than two
     */
    private static Point3d[] checkLengthIsTwoOrMore(final Point3d[] points) throws DrawRuntimeException
    {
        Throw.when(points.length < 2, DrawRuntimeException.class, "Need at least two points");
        return points;
    }

    /**
     * Construct a new Line3d and initialize its length indexed line, bounds, centroid and length.
     * @param iterator Iterator&lt;Point3d&gt;; iterator that will provide all points that constitute the new Line3d
     * @throws NullPointerException when iterator is null
     * @throws DrawException when the iterator provides too few points, or some adjacent identical points)
     */
    public PolyLine3d(final Iterator<Point3d> iterator) throws NullPointerException, DrawException
    {
        this(iteratorToList(Throw.whenNull(iterator, "iterator cannot be null")));
    }

    /**
     * Construct a new Line3d from a List&lt;Point3d&gt;.
     * @param pointList List&lt;Point3d&gt;; the list of points to construct this Line3d from.
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final List<Point3d> pointList) throws DrawRuntimeException
    {
        this(false, pointList.toArray(new Point3d[pointList.size()]));
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine3d instantiate(final List<Point3d> pointList) throws NullPointerException, DrawRuntimeException
    {
        return new PolyLine3d(pointList);
    }

    /**
     * Build a list from the Point3d objects that an iterator provides.
     * @param iterator Iterator&lt;Point3d&gt;; the iterator that will provide the points
     * @return List&lt;Point3d&gt;; a list of the points provided by the iterator
     */
    private static List<Point3d> iteratorToList(final Iterator<Point3d> iterator)
    {
        List<Point3d> result = new ArrayList<>();
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
    public final Point3d get(final int i) throws IndexOutOfBoundsException
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
    public Iterator<Point3d> getPoints()
    {
        return Arrays.stream(this.points).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return this.bounds;
    }

    /**
     * Construct a new Line3d that is equal to this line except for segments that are shorter than the <cite>noiseLevel</cite>.
     * The result is guaranteed to start with the first point of this line and end with the last point of this line.
     * @param noiseLevel double; the minimum segment length that is <b>not</b> removed
     * @return Line3d; the filtered line
     */
    public final PolyLine3d noiseFilteredLine(final double noiseLevel)
    {
        if (this.size() <= 2)
        {
            return this; // Except for some cached fields; an Line3d is immutable; so safe to return
        }
        Point3d prevPoint = null;
        List<Point3d> list = null;
        for (int index = 0; index < this.size(); index++)
        {
            Point3d currentPoint = this.points[index];
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
                        // Replace the last point of the result by the last point of this Line3d
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
            return new PolyLine3d(list);
        }
        catch (DrawRuntimeException exception)
        {
            CategoryLogger.always().error(exception);
            throw new Error(exception);
        }
    }

    /**
     * Concatenate several Line3d instances.
     * @param lines PolyLine3d...; Line3d... one or more Line3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt;
     *            match the first of the second, etc.
     * @return Line3d
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine3d concatenate(final PolyLine3d... lines) throws DrawException
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two Line3d instances. This method is separate for efficiency reasons.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param line1 PolyLine3d; first line
     * @param line2 PolyLine3d; second line
     * @return Line3d; the concatenation of the two lines
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine3d concatenate(final double tolerance, final PolyLine3d line1, final PolyLine3d line2)
            throws DrawException
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new DrawException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst() + " distance is "
                    + line1.getLast().distance(line2.getFirst()) + " > " + tolerance);
        }
        int size = line1.size() + line2.size() - 1;
        Point3d[] points = new Point3d[size];
        int nextIndex = 0;
        for (int j = 0; j < line1.size(); j++)
        {
            points[nextIndex++] = line1.get(j);
        }
        for (int j = 1; j < line2.size(); j++)
        {
            points[nextIndex++] = line2.get(j);
        }
        return new PolyLine3d(false, points);
    }

    /**
     * Concatenate several Line3d instances.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param lines PolyLine3d...; Line3d... one or more Line3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt;
     *            match the first of the second, etc.
     * @return Line3d; the concatenation of the lines
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine3d concatenate(final double tolerance, final PolyLine3d... lines) throws DrawException
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
        Point3d[] points = new Point3d[size];
        int nextIndex = 0;
        for (int i = 0; i < lines.length; i++)
        {
            PolyLine3d line = lines[i];
            for (int j = 0 == i ? 0 : 1; j < line.size(); j++)
            {
                points[nextIndex++] = line.get(j);
            }
        }
        return new PolyLine3d(false, points);
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d project() throws DrawRuntimeException
    {
        List<Point2d> pointList = new ArrayList<>();
        Point2d prevPoint = null;
        for (Point3d point3d : this.points)
        {
            Point2d point = point3d.project();
            if (prevPoint != null)
            {
                if (prevPoint.x == point.x && prevPoint.y == point.y)
                {
                    continue;
                }
            }
            pointList.add(point);
            prevPoint = point;
        }
        return new PolyLine2d(pointList);
    }

    /**
     * Create a new Line3d, filtering out repeating successive points.
     * @param points Point3d...; the coordinates of the line as Point3d
     * @return the line
     * @throws DrawException when number of points &lt; 2
     */
    public static PolyLine3d createAndCleanLine3d(final Point3d... points) throws DrawException
    {
        if (points.length < 2)
        {
            throw new DrawException("Degenerate Line3d; has " + points.length + " point" + (points.length != 1 ? "s" : ""));
        }
        return createAndCleanLine3d(new ArrayList<>(Arrays.asList(points)));
    }

    /**
     * Create an Line3d, while filtering out repeating successive points.
     * @param pointList List&lt;Point3d&gt;; list of the coordinates of the line as Point3d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @return Line3d; the line
     * @throws DrawException when number of non-equal points &lt; 2
     */
    public static PolyLine3d createAndCleanLine3d(final List<Point3d> pointList) throws DrawException
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
        return new PolyLine3d(pointList);
    }

    /** {@inheritDoc} */
    @Override
    public final OrientedPoint3d getLocationExtended(final double position)
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

        // position before start point -- extrapolate using direction from first point to second point of this Line3d
        if (position < 0.0)
        {
            double len = position;
            double fraction = len / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            Point3d p1 = this.points[0];
            Point3d p2 = this.points[1];
            return new OrientedPoint3d(p1.x + fraction * (p2.x - p1.x), p1.y + fraction * (p2.y - p1.y),
                    p1.z + fraction * (p2.z - p1.z), 0.0, 0.0, Math.atan2(p2.y - p1.y, p2.x - p1.x));
        }

        // position beyond end point -- extrapolate using the direction from the before last point to the last point of this
        // Line3d
        int n1 = this.lengthIndexedLine.length - 1;
        int n2 = this.lengthIndexedLine.length - 2;
        double len = position - getLength();
        double fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        while (Double.isInfinite(fraction))
        {
            if (--n2 < 0)
            {
                CategoryLogger.always().error("lengthIndexedLine of {} is invalid", this);
                Point3d p = this.points[n1];
                return new OrientedPoint3d(p.x, p.y, p.z, 0.0, 0.0, 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        Point3d p1 = this.points[n2];
        Point3d p2 = this.points[n1];
        return new OrientedPoint3d(p2.x + fraction * (p2.x - p1.x), p2.y + fraction * (p2.y - p1.y),
                p2.z + fraction * (p2.z - p1.z), 0.0, 0.0, Math.atan2(p2.y - p1.y, p2.x - p1.x));
    }

    /** {@inheritDoc} */
    @Override
    public final OrientedPoint3d getLocation(final double position) throws DrawException
    {
        if (position < 0.0 || position > getLength())
        {
            throw new DrawException("getLocationSI for line: position < 0.0 or > line length. Position = " + position
                    + " m. Length = " + getLength() + " m.");
        }
        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            Point3d p1 = this.points[0];
            Point3d p2 = this.points[1];
            return new OrientedPoint3d(p1.x, p1.y, p1.z, 0.0, 0.0, Math.atan2(p2.y - p1.y, p2.x - p1.x));
        }
        if (position == getLength())
        {
            Point3d p1 = this.points[this.points.length - 2];
            Point3d p2 = this.points[this.points.length - 1];
            return new OrientedPoint3d(p2.x, p2.y, p2.z, 0.0, 0.0, Math.atan2(p2.y - p1.y, p2.x - p1.x));
        }

        // find the index of the line segment, use binary search
        int index = find(position);
        double remainder = position - this.lengthIndexedLine[index];
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        Point3d p1 = this.points[index];
        Point3d p2 = this.points[index + 1];
        return new OrientedPoint3d(p1.x + fraction * (p2.x - p1.x), p1.y + fraction * (p2.y - p1.y),
                p1.z + fraction * (p2.z - p1.z), 0.0, 0.0, Math.atan2(p2.y - p1.y, p2.x - p1.x));
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine3d extract(final double start, final double end) throws DrawException
    {
        if (Double.isNaN(start) || Double.isNaN(end) || start < 0 || start >= end || end > getLength())
        {
            throw new DrawException(
                    "Bad interval (" + start + ".." + end + "; length of this Line3d is " + this.getLength() + ")");
        }
        double cumulativeLength = 0;
        double nextCumulativeLength = 0;
        double segmentLength = 0;
        int index = 0;
        List<Point3d> pointList = new ArrayList<>();
        while (start > cumulativeLength)
        {
            Point3d fromPoint = get(index);
            index++;
            Point3d toPoint = get(index);
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
            Point3d fromPoint = get(index);
            index++;
            if (index >= size())
            {
                break; // rounding error
            }
            Point3d toPoint = get(index);
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
            Point3d point = get(index - 1).interpolate(get(index), (end - cumulativeLength) / segmentLength);
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
    public PolyLine3d truncate(final double position) throws DrawException
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
        Point3d p1 = get(index);
        Point3d lastPoint;
        if (0.0 == fraction)
        {
            index--;
            lastPoint = p1;
        }
        else
        {
            Point3d p2 = get(index + 1);
            lastPoint = p1.interpolate(p2, fraction);

        }
        // FIXME: Cannot create a P[]; will have to do it with a List<P>
        List<Point3d> coords = new ArrayList<>(index + 2);
        for (int i = 0; i <= index; i++)
        {
            coords.add(get(i));
        }
        coords.add(lastPoint);
        return instantiate(coords);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.points);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "PolyLine3d [points=" + Arrays.toString(this.points) + "]";
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({"checkstyle:designforextension", "checkstyle:needbraces"})
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PolyLine3d other = (PolyLine3d) obj;
        if (!Arrays.equals(this.points, other.points))
            return false;
        return true;
    }

}
