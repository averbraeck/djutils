package org.djutils.draw.line;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.Space2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;

/**
 * Implementation of PolyLine for 2D space.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PolyLine2d implements Drawable2d, PolyLine<PolyLine2d, Point2d, Space2d, Ray2d, LineSegment2d>
{
    /** */
    private static final long serialVersionUID = 20200911L;

    /** X-coordinates of the points. */
    private final double[] x;

    /** Y-coordinates of the points. */
    private final double[] y;

    /** The cumulative length of the line at point 'i'. */
    private final double[] lengthIndexedLine;

    /** The length. */
    private final double length;

    /** Bounding rectangle of this PolyLine2d. */
    private final Bounds2d bounds;

    /**
     * Construct a new PolyLine2d from an array of double x values and an array of double y values.
     * @param copyNeeded boolean; if true; a deep copy of the points array is stored instead of the provided array
     * @param x double[]; the x-coordinates of the points
     * @param y double[]; the y-coordinates of the points
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    PolyLine2d(final boolean copyNeeded, final double[] x, final double[] y) throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(x, "x array may not be null");
        Throw.whenNull(y, "y array may not be null");
        Throw.when(x.length != y.length, DrawRuntimeException.class, "x and y arrays must have same length");
        Throw.when(x.length < 2, DrawRuntimeException.class, "Need at least two points");
        this.x = copyNeeded ? Arrays.copyOf(x, x.length) : x;
        this.y = copyNeeded ? Arrays.copyOf(y, y.length) : y;
        double minX = x[0];
        double minY = y[0];
        double maxX = x[0];
        double maxY = y[0];
        this.lengthIndexedLine = new double[x.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < x.length; i++)
        {
            minX = Math.min(minX, x[i]);
            minY = Math.min(minY, y[i]);
            maxX = Math.max(maxX, x[i]);
            maxY = Math.max(maxY, y[i]);
            if (x[i - 1] == x[i] && y[i - 1] == y[i])
            {
                throw new DrawRuntimeException(
                        "Degenerate PolyLine2d; point " + (i - 1) + " has the same x and y as point " + i);
            }
            this.lengthIndexedLine[i] = this.lengthIndexedLine[i - 1] + Math.hypot(x[i] - x[i - 1], y[i] - y[i - 1]);
        }
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.bounds = new Bounds2d(minX, maxX, minY, maxY);
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d. This constructor makes a deep copy of the parameters.
     * @param x double[]; the x-coordinates of the points
     * @param y double[]; the y-coordinates of the points
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final double[] x, final double[] y) throws NullPointerException, DrawRuntimeException
    {
        this(true, x, y);
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d.
     * @param points Point2d[]; the array of points to construct this PolyLine2d from.
     * @throws NullPointerException when the array is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Point2d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(false, makeArray(Throw.whenNull(points, "points may not be null"), p -> p.x), makeArray(points, p -> p.y));
    }

    /**
     * Make an array of double an fill it with the appropriate coordinate of points.
     * @param points Point2d[]; array of points
     * @param getter Function&lt;Point2d, Double&gt;; function that obtains the intended coordinate
     * @return double[]; array of double filled with the requested coordinate values
     */
    protected static double[] makeArray(final Point2d[] points, final Function<Point2d, Double> getter)
    {
        double[] array = new double[points.length];
        for (int index = 0; index < points.length; index++)
        {
            array[index] = getter.apply(points[index]);
        }
        return array;
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d.
     * @param point1 Point2d; starting point of the PolyLine2d
     * @param point2 Point2d; second point of the PolyLine2d
     * @param otherPoints Point2d...; additional points of the PolyLine2d (may be null)
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Point2d point1, final Point2d point2, final Point2d... otherPoints)
            throws NullPointerException, DrawRuntimeException
    {
        this(spliceArray(Throw.whenNull(point1, "point1 may not be null"), Throw.whenNull(point2, "point2 may not be null"),
                otherPoints));
    }

    /**
     * Construct an array of Point2d from two points plus an array of Point2d.
     * @param point1 Point2d; the first point (ends up at index 0 of the result)
     * @param point2 Point2d; the second point (ends up at index 1 of the result)
     * @param otherPoints Point2d...; may be null, may be empty. If non empty, the elements in otherPoints end up at index 2 and
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
     * Construct a new PolyLine2d from an iterator that yields Point2d objects.
     * @param iterator Iterator&lt;Point2d&gt;; iterator that will provide all points that constitute the new PolyLine2d
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the iterator provides too few points, or some adjacent identical points)
     */
    public PolyLine2d(final Iterator<Point2d> iterator) throws NullPointerException, DrawRuntimeException
    {
        this(iteratorToList(Throw.whenNull(iterator, "iterator cannot be null")));
    }

    /**
     * Construct a new PolyLine2d from a List&lt;Point2d&gt;.
     * @param pointList List&lt;Point2d&gt;; the list of points to construct this PolyLine2d from.
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final List<Point2d> pointList) throws DrawRuntimeException
    {
        this(pointList.toArray(new Point2d[pointList.size()]));
    }

    /**
     * Construct a new PolyLine2d (closed shape) from a Path2D.
     * @param path Path2D; the Path2D to construct this PolyLine2d from.
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Path2D path) throws DrawRuntimeException
    {
        this(path2DtoArray(path));
    }

    /**
     * Convert a path2D to a Point2d[] array to construct the line.
     * @param path Path2D; the path to convert
     * @return Point2d[]; an array of points based on MOVETO and LINETO elements of the Path2D
     * @throws DrawRuntimeException when the pathIterator of the path returns an unsupported command
     */
    private static Point2d[] path2DtoArray(final Path2D path) throws DrawRuntimeException
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
                throw new DrawRuntimeException("path2DtoArray only handles SEG_MOVETO, SEG_LINETO and SEG_CLOSE");
            }
        }
        return result.toArray(new Point2d[result.size() - 1]);
    }

    /**
     * Build a list from the Point2d objects that an iterator provides.
     * @param iterator Iterator&lt;Point2d&gt;; the iterator that will provide the points
     * @return List&lt;Point2d&gt;; a list of the points provided by the iterator
     */
    protected static List<Point2d> iteratorToList(final Iterator<Point2d> iterator)
    {
        List<Point2d> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    /**
     * Create a new PolyLine2d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param points Point2d...; the coordinates of the line as Point2d
     * @throws DrawRuntimeException when number of points &lt; 2
     */
    public PolyLine2d(final boolean filterDuplicates, final Point2d... points) throws DrawRuntimeException
    {
        this(PolyLine2d.cleanPoints(filterDuplicates, Arrays.stream(points).iterator()));
    }

    /**
     * Create a new PolyLine2d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param pointList List&lt;Point2d&gt;; list of the coordinates of the line as Point3d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @throws DrawRuntimeException when number of non-equal points &lt; 2
     */
    public PolyLine2d(final boolean filterDuplicates, final List<Point2d> pointList) throws DrawRuntimeException
    {
        this(PolyLine2d.cleanPoints(filterDuplicates, pointList.iterator()));
    }

    /**
     * Return an iterator that optionally skips identical successive points.
     * @param filter boolean; if true; filter out itentical successive points; if false; do not filter
     * @param iterator Iterator&lt;Point2d&gt;; iterator that generates points, potentially with successive duplicates
     * @return Iterator&lt;Point2d&gt;; iterator that skips identical successive points
     */
    static Iterator<Point2d> cleanPoints(final boolean filter, final Iterator<Point2d> iterator)
    {
        Throw.whenNull(iterator, "Iterator may not be null");
        Throw.when(!iterator.hasNext(), DrawRuntimeException.class, "Iterator has no points to return");
        if (!filter)
        {
            return iterator;
        }
        return new Iterator<Point2d>()
        {
            private Point2d currentPoint = iterator.next();

            @Override
            public boolean hasNext()
            {
                return this.currentPoint != null;
            }

            @Override
            public Point2d next()
            {
                Throw.when(this.currentPoint == null, NoSuchElementException.class, "Out of input");
                Point2d result = this.currentPoint;
                this.currentPoint = null;
                while (iterator.hasNext())
                {
                    this.currentPoint = iterator.next();
                    if (result.x != this.currentPoint.x || result.y != this.currentPoint.y)
                    {
                        break;
                    }
                    this.currentPoint = null;
                }
                return result;
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d instantiate(final List<Point2d> pointList) throws NullPointerException, DrawRuntimeException
    {
        return new PolyLine2d(pointList);
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.x.length;
    }

    /** {@inheritDoc} */
    @Override
    public final Point2d get(final int i) throws IndexOutOfBoundsException
    {
        return new Point2d(this.x[i], this.y[i]);
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

    /** {@inheritDoc} */
    @Override
    public LineSegment2d getSegment(final int index)
    {
        Throw.when(index < 0 || index >= this.x.length - 1, DrawRuntimeException.class, "index must be in range 0..size() - 1");
        return new LineSegment2d(this.x[index], this.y[index], this.x[index + 1], this.y[index + 1]);
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
    public Iterator<Point2d> getPoints()
    {
        return new Iterator<Point2d>()
        {
            private int nextIndex = 0;

            /** {@inheritDoc} */
            @Override
            public boolean hasNext()
            {
                return this.nextIndex < size();
            }

            /** {@inheritDoc} */
            @Override
            public Point2d next()
            {
                return get(this.nextIndex++);
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return this.bounds;
    }

    /** {@inheritDoc} */
    @Override
    public final PolyLine2d noiseFilteredLine(final double noiseLevel)
    {
        if (this.size() <= 2)
        {
            return this; // Except for some cached fields; a PolyLine2d is immutable; so safe to return
        }
        Point2d prevPoint = null;
        List<Point2d> list = new ArrayList<>();
        for (int index = 0; index < this.size(); index++)
        {
            Point2d currentPoint = get(index);
            if (null != prevPoint && prevPoint.distance(currentPoint) < noiseLevel)
            {
                if (index == this.size() - 1)
                {
                    if (list.size() > 1)
                    {
                        // Replace the last point of the result by the last point of this PolyLine2d
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
     * Concatenate several PolyLine2d instances.
     * @param lines PolyLine2d...; One or more PolyLine2d objects. The last point of the first &lt;strong&gt;must&lt;/strong&gt;
     *            match the first of the second, etc.
     * @return PolyLine2d
     * @throws DrawRuntimeException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final PolyLine2d... lines) throws DrawRuntimeException
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two PolyLine2d instances. This method is separate for efficiency reasons.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param line1 PolyLine2d; first line
     * @param line2 PolyLine2d; second line
     * @return PolyLine2d; the concatenation of the two lines
     * @throws DrawRuntimeException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final double tolerance, final PolyLine2d line1, final PolyLine2d line2)
            throws DrawRuntimeException
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new DrawRuntimeException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst()
                    + " distance is " + line1.getLast().distance(line2.getFirst()) + " > " + tolerance);
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
        return new PolyLine2d(points);
    }

    /**
     * Concatenate several PolyLine2d instances.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param lines PolyLine2d...; one or more PolyLine2d objects. The last point of the first &lt;strong&gt;must&lt;/strong&gt;
     *            match the first of the second, etc.
     * @return PolyLine2d; the concatenation of the lines
     * @throws DrawRuntimeException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final double tolerance, final PolyLine2d... lines) throws DrawRuntimeException
    {
        if (0 == lines.length)
        {
            throw new DrawRuntimeException("Empty argument list");
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
                throw new DrawRuntimeException(
                        "Lines are not connected: " + lines[i - 1].getLast() + " to " + lines[i].getFirst() + " distance is "
                                + lines[i - 1].getLast().distance(lines[i].getFirst()) + " > " + tolerance);
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
        return new PolyLine2d(points);
    }

    /** {@inheritDoc} */
    @Override
    public final Ray2d getLocationExtended(final double position)
    {
        if (position >= 0.0 && position <= getLength())
        {
            try
            {
                return getLocation(position);
            }
            catch (DrawRuntimeException exception)
            {
                // cannot happen
            }
        }

        // position before start point -- extrapolate using direction from first point to second point of this PolyLine2d
        if (position < 0.0)
        {
            double fraction = position / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            return new Ray2d(this.x[0] + fraction * (this.x[1] - this.x[0]), this.y[0] + fraction * (this.y[1] - this.y[0]),
                    this.x[1], this.y[1]);
        }

        // position beyond end point -- extrapolate using the direction from the before last point to the last point of this
        // PolyLine2d
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
                return new Ray2d(this.x[n1], this.y[n1], 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        return new Ray2d(this.x[n1] + fraction * (this.x[n1] - this.x[n2]), this.y[n1] + fraction * (this.y[n1] - this.y[n2]),
                Math.atan2(this.y[n1] - this.y[n2], this.x[n1] - this.x[n2]));
    }

    /** {@inheritDoc} */
    @Override
    public final Ray2d getLocation(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position), DrawRuntimeException.class, "position may not be NaN");
        Throw.when(position < 0.0 || position > getLength(), DrawRuntimeException.class,
                "getLocation for line: position < 0.0 or > line length. Position = " + position + "; length = " + getLength());
        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            return new Ray2d(this.x[0], this.y[0], this.x[1], this.y[1]);
        }
        if (position == getLength())
        {
            return new Ray2d(this.x[this.x.length - 1], this.y[this.x.length - 1],
                    2 * this.x[this.x.length - 1] - this.x[this.x.length - 2],
                    2 * this.y[this.x.length - 1] - this.y[this.x.length - 2]);
        }
        // find the index of the line segment, use binary search
        int index = find(position);
        double remainder = position - this.lengthIndexedLine[index];
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        // if (fraction >= 1.0 && index < this.x.length - 1)
        // {
        // // Rounding problem; move to the next segment.
        // index++;
        // remainder = position - this.lengthIndexedLine[index];
        // fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        // }
        return new Ray2d(this.x[index] + fraction * (this.x[index + 1] - this.x[index]),
                this.y[index] + fraction * (this.y[index + 1] - this.y[index]), 2 * this.x[index + 1] - this.x[index],
                2 * this.y[index + 1] - this.y[index]);
    }

    /**
     * Perform the orthogonal projection operation.
     * @param point Point2d; the point to project
     * @param limitHandling Boolean; if Null; results outside the interval 0.0 .. 1.0 are replaced by NaN, if false, results
     *            outside that interval are returned as is; if true results outside the interval are truncated to the interval
     *            and therefore not truly orthogonal
     * @return double; the fractional position on this PolyLine that is closest to point, or NaN
     */
    private double projectOrthogonalFractional(final Point2d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point may not be null");
        double bestDistance = Double.POSITIVE_INFINITY;
        double result = Double.NaN;
        double bestDistanceExtended = Double.POSITIVE_INFINITY;
        for (int index = 1; index < this.size(); index++)
        {
            double fraction = point.fractionalPositionOnLine(this.x[index - 1], this.y[index - 1], this.x[index], this.y[index],
                    false, false);
            double distance = Math.hypot(point.x - (this.x[index - 1] + fraction * (this.x[index] - this.x[index - 1])),
                    point.y - (this.y[index - 1] + fraction * (this.y[index] - this.y[index - 1])));
            if (distance < bestDistanceExtended && (fraction >= 0.0 && fraction <= 1.0 || (fraction < 0.0 && index == 1)
                    || fraction > 1.0 && index == this.size() - 1))
            {
                bestDistanceExtended = distance;
            }
            if (distance < bestDistance && (fraction >= 0.0 || index == 1 && limitHandling != null && !limitHandling)
                    && (fraction <= 1.0 || index == this.size() - 1 && limitHandling != null && !limitHandling))
            {
                bestDistance = distance;
                result = lengthAtIndex(index - 1) + fraction * (lengthAtIndex(index) - lengthAtIndex(index - 1));
            }
            else if (fraction < 0.0 && limitHandling != null && limitHandling)
            {
                distance = Math.hypot(point.x - this.x[index - 1], point.y - this.y[index - 1]);
                if (distance < bestDistance)
                {
                    bestDistance = distance;
                    result = lengthAtIndex(index - 1);
                }
            }
            else if (index == this.size() - 1 && limitHandling != null && limitHandling)
            {
                distance = Math.hypot(point.x - this.x[index], point.y - this.y[index]);
                if (distance < bestDistance)
                {
                    bestDistance = distance;
                    result = lengthAtIndex(index);
                }
            }
        }
        if (bestDistance > bestDistanceExtended && (limitHandling == null || !limitHandling))
        {
            return Double.NaN;
        }
        return result / getLength();
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnPolyLine(final Point2d point)
    {
        try
        {
            return getLocation(projectOrthogonalFractional(point, true) * getLength());
        }
        catch (DrawRuntimeException e)
        {
            // Cannot happen
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Perform the project orthogonal operation.
     * @param point Point2d; the point to project
     * @param limitHandling Boolean; if Null; results outside this PolyLin2de are replaced by Null, if false, results outside
     *            that interval are returned as is; if true results outside this PolyLine2d are truncated to the first or last
     *            point of this PolyLine2d and therefore not truly orthogonal
     * @return Point2d; the orthogonal projection of point on this PolyLine2d
     */
    private Point2d projectOrthogonal(final Point2d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point may not be null");
        double fraction = projectOrthogonalFractional(point, limitHandling);
        if (Double.isNaN(fraction))
        {
            return null;
        }
        return getLocationExtended(fraction * getLength());
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonal(final Point2d point) throws NullPointerException
    {
        return projectOrthogonal(point, null);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonalExtended(final Point2d point) throws NullPointerException
    {
        return projectOrthogonal(point, false);
    }

    /** {@inheritDoc} */
    @Override
    public final double projectOrthogonalFractional(final Point2d point) throws NullPointerException
    {
        return projectOrthogonalFractional(point, null);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point2d point) throws NullPointerException
    {
        return projectOrthogonalFractional(point, false);
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d extract(final double start, final double end) throws DrawRuntimeException
    {
        if (Double.isNaN(start) || Double.isNaN(end) || start < 0 || start >= end || end > getLength())
        {
            throw new DrawRuntimeException(
                    "Bad interval (" + start + ".." + end + "; length of this PolyLine2d is " + this.getLength() + ")");
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
        else if (index < this.x.length)
        {
            Point2d point = get(index - 1).interpolate(get(index), (end - cumulativeLength) / segmentLength);
            // can be the same due to rounding
            if (!point.equals(pointList.get(pointList.size() - 1)))
            {
                pointList.add(point);
            }
        }
        // else rounding error
        try
        {
            return instantiate(pointList);
        }
        catch (DrawRuntimeException exception)
        {
            CategoryLogger.always().error(exception, "interval " + start + ".." + end + " too short");
            throw new DrawRuntimeException("interval " + start + ".." + end + "too short");
        }
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d truncate(final double position) throws DrawRuntimeException
    {
        if (position <= 0.0 || position > getLength())
        {
            throw new DrawRuntimeException("truncate for line: position <= 0.0 or > line length. Position = " + position
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
            lastPoint = p1;
        }
        else
        {
            Point2d p2 = get(index + 1);
            lastPoint = p1.interpolate(p2, fraction);
            index++;
        }
        double[] truncatedX = new double[index + 1];
        double[] truncatedY = new double[index + 1];
        for (int i = 0; i < index; i++)
        {
            truncatedX[i] = this.x[i];
            truncatedY[i] = this.y[i];
        }
        truncatedX[index] = lastPoint.x;
        truncatedY[index] = lastPoint.y;
        return new PolyLine2d(truncatedX, truncatedY);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:methodlength")
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
        // Make good use of the fact that PolyLine3d cannot have consecutive duplicate points and has > 1 points
        Point2d prevPoint = filteredReferenceLine.get(0);
        Double prevAngle = null;
        for (int index = 0; index < filteredReferenceLine.size() - 1; index++)
        {
            Point2d nextPoint = filteredReferenceLine.get(index + 1);
            double angle = Math.atan2(nextPoint.y - prevPoint.y, nextPoint.x - prevPoint.x);
            Point2d segmentFrom = new Point2d(prevPoint.x - Math.sin(angle) * offset, prevPoint.y + Math.cos(angle) * offset);
            Point2d segmentTo = new Point2d(nextPoint.x - Math.sin(angle) * offset, nextPoint.y + Math.cos(angle) * offset);
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
                        Point2d intermediatePoint = new Point2d(prevPoint.x - Math.sin(intermediateAngle) * offset,
                                prevPoint.y + Math.cos(intermediateAngle) * offset);
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
                        double pAngle = Math.atan2(p.y - pPoint.y, p.x - pPoint.x);
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
            return new PolyLine2d(true, tempPoints);
        }
        catch (DrawRuntimeException exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d offsetLine(final double offsetAtStart, final double offsetAtEnd, final double circlePrecision,
            final double offsetMinimumFilterValue, final double offsetMaximumFilterValue, final double offsetFilterRatio,
            final double minimumOffset) throws IllegalArgumentException, DrawRuntimeException
    {
        if (offsetAtStart == offsetAtEnd)
        {
            return offsetLine(offsetAtStart, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
                    offsetFilterRatio, minimumOffset);
        }
        PolyLine2d atStart = offsetLine(offsetAtStart, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
                offsetFilterRatio, minimumOffset);
        PolyLine2d atEnd = offsetLine(offsetAtEnd, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
                offsetFilterRatio, minimumOffset);
        return atStart.transitionLine(atEnd, new TransitionFunction()
        {
            @Override
            public double function(final double fraction)
            {
                return fraction;
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine2d transitionLine(final PolyLine2d endLine, final TransitionFunction transition) throws DrawRuntimeException
    {
        Throw.whenNull(endLine, "endLine may not be null");
        Throw.whenNull(transition, "transition may not be null");
        List<Point2d> pointList = new ArrayList<>();
        int indexInStart = 0;
        int indexInEnd = 0;
        while (indexInStart < this.size() && indexInEnd < endLine.size())
        {
            double fractionInStart = lengthAtIndex(indexInStart) / getLength();
            double fractionInEnd = endLine.lengthAtIndex(indexInEnd) / endLine.getLength();
            if (fractionInStart < fractionInEnd)
            {
                pointList.add(get(indexInStart).interpolate(endLine.getLocation(fractionInStart * endLine.getLength()),
                        transition.function(fractionInStart)));
                indexInStart++;
            }
            else if (fractionInStart > fractionInEnd)
            {
                pointList.add(this.getLocation(fractionInEnd * getLength()).interpolate(endLine.get(indexInEnd),
                        transition.function(fractionInEnd)));
                indexInEnd++;
            }
            else
            {
                pointList.add(this.get(indexInStart).interpolate(endLine.getLocation(fractionInEnd * endLine.getLength()),
                        transition.function(fractionInStart)));
                indexInStart++;
                indexInEnd++;
            }
        }
        return new PolyLine2d(true, pointList);
    }

    /**
     * Find a location on this PolyLine2d that is a reasonable projection of a Ray on this line. The result (if not NaN) lies on
     * a line perpendicular to the direction of the Ray and on some segment of this PolyLine. This method attempts to give
     * continuous results for continuous changes of the Ray that must be projected. There are cases where this is simply
     * impossible, or the optimal result is ambiguous. In these cases this method will return something that is hopefully good
     * enough.
     * @param ray Ray2d; the Ray
     * @return double; length along this PolyLine (some value between 0 and the length of this PolyLine) where ray projects, or
     *         NaN if there is no solution
     * @throws NullPointerException when ray is null
     */
    public double projectRay(final Ray2d ray) throws NullPointerException
    {
        Throw.whenNull(ray, "ray may not be null");
        double bestDistance = Double.POSITIVE_INFINITY;
        double positionAtBestDistance = Double.NaN;
        // Point2d prevPoint = null;
        // Define the line that is perpendicular to directedPoint, passing through directedPoint
        double perpendicularX = ray.x - Math.sin(ray.phi);
        double perpendicularY = ray.y + Math.cos(ray.phi);
        for (int index = 1; index < this.x.length; index++)
        {
            Point2d intersection = Point2d.intersectionOfLines(ray.x, ray.y, perpendicularX, perpendicularY, false, false,
                    this.x[index - 1], this.y[index - 1], this.x[index], this.y[index], true, true);
            if (intersection != null) // Intersection is on the segment
            {
                double thisDistance = intersection.distance(ray);
                if (thisDistance < bestDistance)
                {
                    double distanceToPrevPoint =
                            Math.hypot(intersection.x - this.x[index - 1], intersection.y - this.y[index - 1]);
                    positionAtBestDistance = lengthAtIndex(index - 1) + distanceToPrevPoint;
                    bestDistance = thisDistance;
                }
            }
        }
        return positionAtBestDistance;
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
        StringBuilder result = new StringBuilder();
        if (!doNotIncludeClassName)
        {
            result.append("PolyLine2d ");
        }
        String format = String.format("%%sx=%1$s, y=%1$s", doubleFormat);
        for (int index = 0; index < this.x.length; index++)
        {
            result.append(String.format(Locale.US, format, index == 0 ? "[" : ", ", this.x[index], this.y[index]));
        }
        result.append("]");
        return result.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toExcel()
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < this.x.length; i++)
        {
            s.append(this.x[i] + "\t" + this.y[i] + "\n");
        }
        return s.toString();
    }

    /**
     * Convert this PolyLine3D to Peter's plot format.
     * @return Peter's format plot output
     */
    public String toPlot()
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < this.x.length; i++)
        {
            result.append(String.format(Locale.US, "%s%.3f,%.3f", 0 == result.length() ? "M" : " L", this.x[i], this.y[i]));
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
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
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
        PolyLine2d other = (PolyLine2d) obj;
        if (!Arrays.equals(this.x, other.x))
            return false;
        if (!Arrays.equals(this.y, other.y))
            return false;
        return true;
    }

}
