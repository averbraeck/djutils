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
public class PolyLine3d implements Drawable3d, PolyLine<PolyLine3d, Point3d, Space3d, Ray3d, LineSegment3d>
{
    /** */
    private static final long serialVersionUID = 20200911L;

    /** X-coordinates of the points. */
    private final double[] x;

    /** Y-coordinates of the points. */
    private final double[] y;

    /** Z-coordinates of the points. */
    private final double[] z;

    /** The cumulative length of the line at point 'i'. */
    private final double[] lengthIndexedLine;

    /** The length. */
    private final double length;

    /** Bounding box of this Line3d. */
    private final Bounds3d bounds;

    /**
     * Construct a new Line3d from an array of double x values, an array of double y values and an array of double z values.
     * @param copyNeeded boolean; if true; a deep copy of the points array is stored instead of the provided array
     * @param x double[]; the x-coordinates of the points
     * @param y double[]; the y-coordinates of the points
     * @param z double[]; the z-coordinates of the points
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    private PolyLine3d(final boolean copyNeeded, final double[] x, final double[] y, final double[] z)
            throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(x, "x array may not be null");
        Throw.whenNull(y, "y array may not be null");
        Throw.whenNull(y, "z array may not be null");
        Throw.when(x.length != y.length || x.length != z.length, DrawRuntimeException.class,
                "x, y  and z arrays must have same length");
        Throw.when(x.length < 2, DrawRuntimeException.class, "Need at least two points");
        this.x = copyNeeded ? Arrays.copyOf(x, x.length) : x;
        this.y = copyNeeded ? Arrays.copyOf(y, y.length) : y;
        this.z = copyNeeded ? Arrays.copyOf(z, z.length) : z;
        double minX = x[0];
        double minY = y[0];
        double minZ = z[0];
        double maxX = x[0];
        double maxY = y[0];
        double maxZ = z[0];
        this.lengthIndexedLine = new double[x.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < x.length; i++)
        {
            minX = Math.min(minX, x[i]);
            minY = Math.min(minY, y[i]);
            minZ = Math.min(minZ, z[i]);
            maxX = Math.max(maxX, x[i]);
            maxY = Math.max(maxY, y[i]);
            maxZ = Math.max(maxZ, z[i]);
            if (x[i - 1] == x[i] && y[i - 1] == y[i] && (z[i - 1] == z[i]))
            {
                throw new DrawRuntimeException(
                        "Degenerate PolyLine2d; point " + (i - 1) + " has the same x, y and z as point " + i);
            }
            // There should be a varargs Math.hypot implementation
            this.lengthIndexedLine[i] =
                    this.lengthIndexedLine[i - 1] + Math.hypot(Math.hypot(x[i] - x[i - 1], y[i] - y[i - 1]), z[i] - z[i - 1]);
        }
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.bounds = new Bounds3d(minX, maxX, minY, maxY, minZ, maxZ);
    }

    /**
     * Construct a new Line3d from an array of Point2d. This constructor makes a deep copy of the parameters.
     * @param x double[]; the x-coordinates of the points
     * @param y double[]; the y-coordinates of the points
     * @param z double[]; the z-coordinates of the points
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final double[] x, final double[] y, final double[] z) throws NullPointerException, DrawRuntimeException
    {
        this(true, x, y, z);
    }

    /**
     * Construct a new Line3d from an array of Point2d.
     * @param points Point2d[]; the array of points to construct this Line2d from.
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final Point3d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(false, makeX(Throw.whenNull(points, "points may not be null")), makeY(points), makeZ(points));
    }

    /**
     * Make an array of double and fill it with the x-coordinates of points.
     * @param points Point2d[]; array of points
     * @return double[]; array filled with the x-coordinates of points
     */
    static double[] makeX(final Point3d[] points)
    {
        double[] xArray = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            xArray[i] = points[i].x;
        }
        return xArray;
    }

    /**
     * Make an array of double and fill it with the y-coordinates of points.
     * @param points Point2d[]; array of points
     * @return double[]; array filled with the y-coordinates of points
     */
    static double[] makeY(final Point3d[] points)
    {
        double[] yArray = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            yArray[i] = points[i].y;
        }
        return yArray;
    }

    /**
     * Make an array of double and fill it with the z-coordinates of points.
     * @param points Point2d[]; array of points
     * @return double[]; array filled with the z-coordinates of points
     */
    static double[] makeZ(final Point3d[] points)
    {
        double[] zArray = new double[points.length];
        for (int i = 0; i < points.length; i++)
        {
            zArray[i] = points[i].z;
        }
        return zArray;
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
        this(spliceArray(point1, point2, otherPoints));
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
        this(pointList.toArray(new Point3d[pointList.size()]));
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
    static List<Point3d> iteratorToList(final Iterator<Point3d> iterator)
    {
        List<Point3d> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.x.length;
    }

    /** {@inheritDoc} */
    @Override
    public final Point3d get(final int i) throws IndexOutOfBoundsException
    {
        return new Point3d(this.x[i], this.y[i], this.z[i]);
    }

    /** {@inheritDoc} */
    @Override
    public final double getX(final int i) throws IndexOutOfBoundsException
    {
        return this.x[i];
    }

    /** {@inheritDoc} */
    @Override
    public final double getY(final int i) throws IndexOutOfBoundsException
    {
        return this.y[i];
    }

    /**
     * Return the z-coordinate of a point of this PolyLine.
     * @param index int; the index of the requested z-coordinate
     * @return double; the z-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when index &lt; 0 or index &gt;= size()
     */
    public final double getZ(final int index) throws IndexOutOfBoundsException
    {
        return this.z[index];
    }

    /** {@inheritDoc} */
    @Override
    public LineSegment3d getSegment(final int index)
    {
        Throw.when(index < 0 || index >= this.x.length - 1, DrawRuntimeException.class, "index must be in range 0..size() - 1");
        return new LineSegment3d(this.x[index], this.y[index], this.z[index], this.x[index + 1], this.y[index + 1],
                this.z[index + 1]);
    }

    /** {@inheritDoc} */
    @Override
    public final double lengthAtIndex(final int index)
    {
        return this.lengthIndexedLine[index];
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        return this.length;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point3d> getPoints()
    {
        return new Iterator<Point3d>()
        {
            private int nextIndex = 0;

            /** {@inheritDoc} */
            @SuppressWarnings("synthetic-access")
            @Override
            public boolean hasNext()
            {
                return this.nextIndex < PolyLine3d.this.x.length;
            }

            /** {@inheritDoc} */
            @Override
            public Point3d next()
            {
                return get(this.nextIndex++);
            }
        };
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
            return this; // Except for some cached fields; an Line2d is immutable; so safe to return
        }
        Point3d prevPoint = null;
        List<Point3d> list = new ArrayList<>();
        for (int index = 0; index < this.size(); index++)
        {
            Point3d currentPoint = get(index);
            if (null != prevPoint && prevPoint.distance(currentPoint) < noiseLevel)
            {
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
            list.add(currentPoint);
            prevPoint = currentPoint;
        }
        if (list.size() == this.x.length)
        {
            return this;
        }
        if (list.size() == 2 && list.get(0).equals(list.get(1)))
        {
            // Insert point 1 of this; it MUST be different from point 0; so we don't have to test for anything.
            list.add(1, get(1));
        }
        try
        {
            return new PolyLine3d(list);
        }
        catch (DrawRuntimeException exception)
        {
            // Cannot happen
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
        return new PolyLine3d(points);
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
        return new PolyLine3d(points);
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d project() throws DrawRuntimeException
    {
        double[] projectedX = new double[this.x.length];
        double[] projectedY = new double[this.x.length];
        int nextIndex = 0;
        for (int i = 0; i < this.x.length; i++)
        {
            if (i > 0 && this.x[i] == this.x[i - 1] && this.y[i] == this.y[i - 1])
            {
                continue;
            }
            projectedX[nextIndex] = this.x[i];
            projectedY[nextIndex] = this.y[i];
            nextIndex++;
        }
        if (nextIndex < projectedX.length)
        {
            return new PolyLine2d(false, Arrays.copyOf(projectedX, nextIndex), Arrays.copyOf(projectedY, nextIndex));
        }
        return new PolyLine2d(false, this.x, this.y); // The x and y arrays are immutable; so we can safely share them
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
    public final Ray3d getLocationExtended(final double position)
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
            double fraction = position / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            return new Ray3d(this.x[0] + fraction * (this.x[1] - this.x[0]), this.y[0] + fraction * (this.y[1] - this.y[0]),
                    this.z[0] + fraction * (this.z[1] - this.z[0]), this.x[1], this.y[1], this.z[1]);
        }

        // position beyond end point -- extrapolate using the direction from the before last point to the last point of this
        // Line2d
        int n1 = this.x.length - 1; // index of last point
        int n2 = this.x.length - 2; // index of before last point
        double len = position - getLength();
        double fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        while (Double.isInfinite(fraction))
        {
            // Overflow occurred; move n2 back another point; if possible
            if (--n2 < 0)
            {
                CategoryLogger.always().error("lengthIndexedLine of {} is invalid", this);
                return new Ray3d(this.x[n1], this.y[n1], this.z[n1], 0.0, 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        return new Ray3d(this.x[n1] + fraction * (this.x[n1] - this.x[n2]), this.y[n1] + fraction * (this.y[n1] - this.y[n2]),
                this.z[n1] + fraction * (this.z[n1] - this.z[n2]), Math.atan2(this.y[n1] - this.y[n2], this.x[n1] - this.x[n2]),
                Math.atan2(this.z[n1] - this.z[n2], Math.hypot(this.x[n1] - this.x[n2], this.y[n1] - this.y[n2])));
    }

    /** {@inheritDoc} */
    @Override
    public final Ray3d getLocation(final double position) throws DrawException
    {
        Throw.when(Double.isNaN(position), DrawException.class, "position may not be NaN");
        if (position < 0.0 || position > getLength())
        {
            throw new DrawException("getLocation for line: position < 0.0 or > line length. Position = " + position
                    + "; length = " + getLength());
        }
        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            return new Ray3d(this.x[0], this.y[0], this.z[0], this.x[1], this.y[1], this.z[1]);
        }
        if (position == getLength())
        {
            return new Ray3d(this.x[this.x.length - 1], this.y[this.x.length - 1], this.z[this.x.length - 1],
                    2 * this.x[this.x.length - 1] - this.x[this.x.length - 2],
                    2 * this.y[this.x.length - 1] - this.y[this.x.length - 2],
                    2 * this.z[this.x.length - 1] - this.z[this.x.length - 2]);
        }
        // find the index of the line segment, use binary search
        int index = find(position);
        double remainder = position - this.lengthIndexedLine[index];
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        return new Ray3d(this.x[index] + fraction * (this.x[index + 1] - this.x[index]),
                this.y[index] + fraction * (this.y[index + 1] - this.y[index]),
                this.z[index] + fraction * (this.z[index + 1] - this.z[index]), this.x[index + 1], this.y[index + 1],
                this.z[index + 1]);
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
        else if (index < this.x.length)
        {
            Point3d point = get(index - 1).interpolate(get(index), (end - cumulativeLength) / segmentLength);
            // can be the same due to rounding
            if (!point.equals(pointList.get(pointList.size() - 1)))
            {
                pointList.add(point);
            }
        }
        // else: rounding error
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
            lastPoint = p1;
        }
        else
        {
            Point3d p2 = get(index + 1);
            lastPoint = p1.interpolate(p2, fraction);
            index++;
        }
        double[] truncatedX = new double[index + 1];
        double[] truncatedY = new double[index + 1];
        double[] truncatedZ = new double[index + 1];
        for (int i = 0; i < index; i++)
        {
            truncatedX[i] = this.x[i];
            truncatedY[i] = this.y[i];
            truncatedZ[i] = this.z[i];
        }
        truncatedX[index] = lastPoint.x;
        truncatedY[index] = lastPoint.y;
        truncatedZ[index] = lastPoint.z;
        return new PolyLine3d(truncatedX, truncatedY, truncatedZ);
    }

    /**
     * Convert this PolyLine2d to something that MS-Excel can plot.
     * @return excel X/Y/Z plottable output
     */
    public String toExcel()
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < size(); i++)
        {
            s.append(getX(i) + "\t" + getY(i) + "\t" + getZ(i) + "\n");
        }
        return s.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "PolyLine3d [x=" + Arrays.toString(this.x) + ", y=" + Arrays.toString(this.y) + ", z=" + Arrays.toString(this.z)
                + "]";
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:designforextension")
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
        PolyLine3d other = (PolyLine3d) obj;
        if (!Arrays.equals(this.x, other.x))
            return false;
        if (!Arrays.equals(this.y, other.y))
            return false;
        if (!Arrays.equals(this.z, other.z))
            return false;
        return true;
    }

}
