package org.djutils.draw.line;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;
import org.djutils.math.AngleUtil;

/**
 * Implementation of PolyLine for 2D space.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class PolyLine2d implements Drawable2d, PolyLine<PolyLine2d, Point2d, Ray2d, DirectedPoint2d, LineSegment2d>
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

    /** Heading at start point (only needed for degenerate PolyLine2d). */
    private final double startHeading;

    /**
     * Construct a new PolyLine2d from an array of double x values and an array of double y values.
     * @param copyNeeded if<code>true</code>; a deep copy of the points array is stored instead of the provided array
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param x the x-coordinates of the points
     * @param y the y-coordinates of the points
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points), or <code>x</code> and <code>y</code> differ in length
     */
    PolyLine2d(final boolean copyNeeded, final double epsilon, final double[] x, final double[] y)
    {
        Throw.when(x.length != y.length, IllegalArgumentException.class, "x and y arrays must have same length");
        Throw.when(x.length < 2, IllegalArgumentException.class, "Need at least two points");
        double[][] filtered = filterNearDuplicates(epsilon, x, y);
        this.x = copyNeeded && filtered[0].length == x.length ? Arrays.copyOf(x, x.length) : filtered[0];
        this.y = copyNeeded && filtered[0].length == x.length ? Arrays.copyOf(y, y.length) : filtered[1];
        double minX = x[0];
        double minY = y[0];
        double maxX = x[0];
        double maxY = y[0];
        this.lengthIndexedLine = new double[this.x.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < this.x.length; i++)
        {
            minX = Math.min(minX, this.x[i]);
            minY = Math.min(minY, this.y[i]);
            maxX = Math.max(maxX, this.x[i]);
            maxY = Math.max(maxY, this.y[i]);
            if (this.x[i - 1] == this.x[i] && this.y[i - 1] == this.y[i])
            {
                throw new IllegalArgumentException(
                        "Degenerate PolyLine2d; point " + (i - 1) + " has the same x and y as point " + i);
            }
            this.lengthIndexedLine[i] =
                    this.lengthIndexedLine[i - 1] + Math.hypot(this.x[i] - this.x[i - 1], this.y[i] - this.y[i - 1]);
        }
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.bounds = new Bounds2d(minX, maxX, minY, maxY);
        this.startHeading = Double.NaN;
    }

    /**
     * Construct a degenerate PolyLine2d (consisting of only one point).
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param heading the heading in radians
     * @throws ArithmeticException when <code>x</code>, <code>y</code>, or <code>heading</code> is <code>NaN</code>
     * @throws IllegalArgumentException when or heading is infinite
     */
    public PolyLine2d(final double x, final double y, final double heading)
    {
        Throw.whenNaN(x, "x");
        Throw.whenNaN(y, "y");
        Throw.whenNaN(heading, "heading");
        Throw.when(Double.isInfinite(heading), IllegalArgumentException.class, "heading must be finite");
        this.x = new double[] {x};
        this.y = new double[] {y};
        this.startHeading = heading;
        this.length = 0;
        this.bounds = new Bounds2d(x, x, y, y);
        this.lengthIndexedLine = new double[] {0.0};
    }

    /**
     * Construct a degenerate PolyLine2d (consisting of only one point).
     * @param p the point of the degenerate PolyLine2d
     * @param heading the heading in radians
     * @throws NullPointerException when <code>p</code> is <code>null</code>
     * @throws ArithmeticException when <code>heading</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>heading</code> is infinite
     */
    public PolyLine2d(final Point2d p, final double heading)
    {
        this(Throw.whenNull(p, "p").x, p.y, heading);
    }

    /**
     * Construct a degenerate PolyLine2d (consisting of only one point).
     * @param directedPoint2d point and heading (DirZ) of the degenerate PolyLine2d
     * @throws NullPointerException when <code>p</code> is <code>null</code>
     * @throws ArithmeticException when <code>heading</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>heading</code> is infinite
     */
    public PolyLine2d(final DirectedPoint2d directedPoint2d)
    {
        this(Throw.whenNull(directedPoint2d, "directedPoint2d").x, directedPoint2d.y, directedPoint2d.dirZ);
    }

    /**
     * Construct a new PolyLine2d from an array of x-values and an array of y-values. This constructor makes a deep copy of the
     * parameters.
     * @param x the x-coordinates of the points
     * @param y the y-coordinates of the points
     * @throws NullPointerException when any of the arrays is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points, or the arrays do not have the same length)
     */
    public PolyLine2d(final double[] x, final double[] y)
    {
        this(NO_FILTER, x, y);
    }

    /**
     * Construct a new PolyLine2d from an array of x-values and an array of y-values. This constructor makes a deep copy of the
     * parameters.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param x the x-coordinates of the points
     * @param y the y-coordinates of the points
     * @throws NullPointerException when any of the arrays is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points, or the arrays do not have the same length)
     */
    public PolyLine2d(final double epsilon, final double[] x, final double[] y)
    {
        this(true, epsilon, x, y);
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d.
     * @param points the array of points to construct this PolyLine2d from.
     * @throws NullPointerException when the array is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Point2d[] points)
    {
        this(NO_FILTER, points);
    }

    /**
     * Construct a new PolyLine2d from an array of Point2d.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param points the array of points to construct this PolyLine2d from.
     * @throws NullPointerException when the array is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final double epsilon, final Point2d[] points)
    {
        this(false, epsilon, makeArray(Throw.whenNull(points, "points"), p -> p.x), makeArray(points, p -> p.y));
    }

    /**
     * Make an array of double an fill it with the appropriate coordinate of points.
     * @param points array of points
     * @param getter function that obtains the intended coordinate
     * @return array of double filled with the requested coordinate values
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
     * Construct a new PolyLine2d from two or more Point2d arguments.
     * @param point1 starting point of the PolyLine2d
     * @param point2 second point of the PolyLine2d
     * @param otherPoints additional points of the PolyLine2d (may be <code>null</code>, or have zero length)
     * @throws NullPointerException when <code>point1</code>, or <code>point2</code> is <code>null</code>, or
     *             <code>otherPoints</code> contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Point2d point1, final Point2d point2, final Point2d... otherPoints)
    {
        this(spliceArray(Throw.whenNull(point1, "point1"), Throw.whenNull(point2, "point2"), otherPoints));
    }

    /**
     * Construct a new PolyLine2d from two or more Point2d arguments.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param point1 starting point of the PolyLine2d
     * @param point2 second point of the PolyLine2d
     * @param otherPoints additional points of the PolyLine2d (may be <code>null</code>, or have zero length)
     * @throws NullPointerException when <code>point1</code>, or <code>point2</code> is <code>null</code>, or
     *             <code>otherPoints</code> contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final double epsilon, final Point2d point1, final Point2d point2, final Point2d... otherPoints)
    {
        this(epsilon, spliceArray(Throw.whenNull(point1, "point1"), Throw.whenNull(point2, "point2"), otherPoints));
    }

    /**
     * Construct an array of Point2d from two points plus an array of Point2d.
     * @param point1 the first point (ends up at index 0 of the result)
     * @param point2 the second point (ends up at index 1 of the result)
     * @param otherPoints may be <code>null</code>, may be empty. If non empty, the elements in <code>otherPoints</code> end up
     *            at index 2 and up in the result
     * @return the combined array
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
     * @param iterator iterator that will provide all points that constitute the new PolyLine2d
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>, or yields a <code>null</code> value
     * @throws IllegalArgumentException when the iterator provides too few points, or some adjacent identical points)
     */
    public PolyLine2d(final Iterator<Point2d> iterator)
    {
        this(NO_FILTER, iterator);
    }

    /**
     * Construct a new PolyLine2d from an iterator that yields Point2d objects.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param iterator iterator that will provide all points that constitute the new PolyLine2d
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>, or yields a <code>null</code> value
     * @throws IllegalArgumentException when the iterator provides too few points, or some adjacent identical points)
     */
    public PolyLine2d(final double epsilon, final Iterator<Point2d> iterator)
    {
        this(epsilon, iteratorToList(Throw.whenNull(iterator, "iterator")));
    }

    /**
     * Construct a new PolyLine2d from a List&lt;Point2d&gt;.
     * @param pointList the list of points to construct the new PolyLine2d from.
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final List<Point2d> pointList)
    {
        this(NO_FILTER, pointList);
    }

    /**
     * Construct a new PolyLine2d from a List&lt;Point2d&gt;.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param pointList the list of points to construct the new PolyLine2d from.
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final double epsilon, final List<Point2d> pointList)
    {
        this(epsilon, pointList.toArray(new Point2d[Throw.whenNull(pointList, "pointList").size()]));
    }

    /**
     * Construct a new PolyLine2d from a Path2D.
     * @param path the Path2D to construct this PolyLine2d from.
     * @throws NullPointerException when <code>path</code> is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final Path2D path)
    {
        this(NO_FILTER, path);
    }

    /**
     * Construct a new PolyLine2d from a Path2D.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param path the Path2D to construct this PolyLine2d from.
     * @throws NullPointerException when <code>path</code> is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine2d(final double epsilon, final Path2D path)
    {
        this(epsilon, path2DtoArray(Throw.whenNull(path, "path")));
    }

    /**
     * Convert a path2D to a Point2d[] array to construct the line.
     * @param path the path to convert
     * @return an array of points based on MOVETO and LINETO elements of the Path2D
     * @throws NullPointerException when <code>path</code> is <code>null</code>
     * @throws IllegalArgumentException when the <code>pathIterator</code> of the <code>path</code> returns an unsupported
     *             command
     */
    private static Point2d[] path2DtoArray(final Path2D path)
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
                throw new IllegalArgumentException("path2DtoArray only handles SEG_MOVETO, SEG_LINETO and SEG_CLOSE");
            }
        }
        return result.toArray(new Point2d[result.size() - 1]);
    }

    /**
     * Build a list from the Point2d objects that an iterator provides.
     * @param iterator the iterator that will provide the points
     * @return a list of the points provided by the <code>iterator</code>
     */
    protected static List<Point2d> iteratorToList(final Iterator<Point2d> iterator)
    {
        List<Point2d> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    /**
     * Construct a new PolyLine2d from an existing one. This constructor is primarily intended for use in extending classes.
     * @param polyLine the existing <code>PolyLine2d</code>
     */
    public PolyLine2d(final PolyLine2d polyLine)
    {
        this.x = polyLine.x;
        this.y = polyLine.y;
        this.lengthIndexedLine = polyLine.lengthIndexedLine;
        this.length = polyLine.length;
        this.bounds = polyLine.bounds;
        this.startHeading = polyLine.startHeading;
    }

    @Override
    public PolyLine2d instantiate(final double epsilon, final List<Point2d> pointList)
    {
        return new PolyLine2d(epsilon, pointList);
    }

    @Override
    public int size()
    {
        return this.x.length;
    }

    @Override
    public final Point2d get(final int i)
    {
        return new Point2d(this.x[i], this.y[i]);
    }

    @Override
    public final double getX(final int i)
    {
        return this.x[i];
    }

    @Override
    public final double getY(final int i)
    {
        return this.y[i];
    }

    @Override
    public LineSegment2d getSegment(final int index)
    {
        Throw.when(index < 0 || index >= this.x.length - 1, IndexOutOfBoundsException.class,
                "index must be in range [0, size() - 1), got %d", index);
        return new LineSegment2d(this.x[index], this.y[index], this.x[index + 1], this.y[index + 1]);
    }

    @Override
    public final double lengthAtIndex(final int index)
    {
        return this.lengthIndexedLine[index];
    }

    @Override
    public double getLength()
    {
        return this.length;
    }

    @Override
    public Iterator<Point2d> iterator()
    {
        return new Iterator<Point2d>()
        {
            private int nextIndex = 0;

            @Override
            public boolean hasNext()
            {
                return this.nextIndex < size();
            }

            @Override
            public Point2d next()
            {
                return get(this.nextIndex++);
            }
        };
    }

    @Override
    public Bounds2d getAbsoluteBounds()
    {
        return this.bounds;
    }

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
        return new PolyLine2d(list);
    }

    /**
     * Concatenate several PolyLine2d instances.
     * @param lines One or more PolyLine2d objects. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match the
     *            first of the second, etc.
     * @return PolyLine2d
     * @throws NullPointerException when <code>lines</code> is <code>null</code>
     * @throws IllegalArgumentException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine2d concatenate(final PolyLine2d... lines)
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two PolyLine2d instances. This method is separate for efficiency reasons.
     * @param tolerance the tolerance between the end point of a line and the first point of the next line
     * @param line1 first line
     * @param line2 second line
     * @return the concatenation of the two lines
     * @throws NullPointerException when <code>line1</code>, or <code>line2</code> is <code>null</code>
     * @throws IllegalArgumentException when there is a gap larger than <code>tolerance</code> between the two lines
     */
    public static PolyLine2d concatenate(final double tolerance, final PolyLine2d line1, final PolyLine2d line2)
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new IllegalArgumentException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst()
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
     * @param tolerance the tolerance between the end point of a line and the first point of the next line
     * @param lines one or more PolyLine2d objects. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match the
     *            first of the second within the provided tolerance value, etc.
     * @return the concatenation of the lines
     * @throws NullPointerException when <code>lines</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException if zero lines are given, or when there is a gap larger than <code>tolerance</code>
     *             between consecutive lines
     */
    public static PolyLine2d concatenate(final double tolerance, final PolyLine2d... lines)
    {
        if (0 == lines.length)
        {
            throw new IllegalArgumentException("Empty argument list");
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
                throw new IllegalArgumentException(
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

    @Override
    public final DirectedPoint2d getLocationExtended(final double position)
    {
        if (position >= 0.0 && position <= this.length)
        {
            return getLocation(position);
        }

        // position before start point -- extrapolate using direction from first point to second point of this PolyLine2d
        if (position < 0.0)
        {
            double fraction = position / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            return new DirectedPoint2d(this.x[0] + fraction * (this.x[1] - this.x[0]),
                    this.y[0] + fraction * (this.y[1] - this.y[0]), this.x[1], this.y[1]);
        }

        // position beyond end point -- extrapolate using the direction from the before last point to the last point of this
        // PolyLine2d
        int n1 = this.x.length - 1; // index of last point
        int n2 = this.x.length - 2; // index of before last point
        double len = position - this.length;
        double fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        while (Double.isInfinite(fraction))
        {
            // Overflow occurred; move n2 back another point; if possible
            if (--n2 < 0)
            {
                CategoryLogger.always().error("lengthIndexedLine of {} is invalid", this);
                return new DirectedPoint2d(this.x[n1], this.y[n1], 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        return new DirectedPoint2d(this.x[n1] + fraction * (this.x[n1] - this.x[n2]),
                this.y[n1] + fraction * (this.y[n1] - this.y[n2]),
                Math.atan2(this.y[n1] - this.y[n2], this.x[n1] - this.x[n2]));
    }

    @Override
    public final DirectedPoint2d getLocation(final double position)
    {
        Throw.whenNaN(position, "position");
        Throw.when(position < 0.0 || position > this.length, IllegalArgumentException.class,
                "illegal location (got %f, valid range is [0.0, %f])", position, this.length);
        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            if (this.lengthIndexedLine.length == 1) // Extra special case; degenerate PolyLine2d
            {
                return new DirectedPoint2d(this.x[0], this.y[0], this.startHeading);
            }
            return new DirectedPoint2d(this.x[0], this.y[0], this.x[1], this.y[1]);
        }
        if (position == this.length)
        {
            return new DirectedPoint2d(this.x[this.x.length - 1], this.y[this.x.length - 1],
                    2 * this.x[this.x.length - 1] - this.x[this.x.length - 2],
                    2 * this.y[this.x.length - 1] - this.y[this.x.length - 2]);
        }
        // find the index of the line segment, use binary search
        int index = find(position);
        double remainder = position - this.lengthIndexedLine[index];
        while ((this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]) == 0) // Issue 82
        {
            int fromIndex = index;
            while (index < this.size() - 1 && (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[fromIndex]) == 0)
            {
                index++;
            }
            if ((this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[fromIndex]) != 0)
            {
                return new DirectedPoint2d(this.x[fromIndex], this.y[fromIndex], 2 * this.x[index + 1] - this.x[fromIndex],
                        2 * this.y[index + 1] - this.y[fromIndex]);
            }
            while (fromIndex > 0 && (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[fromIndex]) == 0)
            {
                fromIndex--;
            }
            return new DirectedPoint2d(this.x[index], this.y[index], 2 * this.x[index] - this.x[fromIndex],
                    2 * this.y[index] - this.y[fromIndex]);
        }
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        return new DirectedPoint2d(this.x[index] + fraction * (this.x[index + 1] - this.x[index]),
                this.y[index] + fraction * (this.y[index + 1] - this.y[index]), 2 * this.x[index + 1] - this.x[index],
                2 * this.y[index + 1] - this.y[index]);
    }

    /**
     * Perform the orthogonal projection operation.
     * @param point the point to project
     * @param limitHandling if <code>Null</code>; results outside the interval 0.0 .. 1.0 are replaced by <code>NaN</code>, if
     *            <code>false</code>, results outside that interval are returned as is; if <code>true</code> results outside the
     *            interval are truncated to the interval and therefore not truly orthogonal
     * @return the fractional position on this <code>PolyLine2d</code> that is closest to point, or <code>NaN</code>
     */
    private double projectOrthogonalFractional(final Point2d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point");
        double result = Double.NaN;
        if (this.lengthIndexedLine.length == 1)
        {
            // This is a degenerate PolyLine2d
            if (null != limitHandling && limitHandling)
            {
                return 0.0;
            }
            result = new Ray2d(getLocation(0.0)).projectOrthogonalFractionalExtended(point);
            if (null == limitHandling)
            {
                return result == 0.0 ? 0.0 : Double.NaN;
            }
            // limitHandling is false
            if (result == 0.0)
            {
                return 0.0;
            }
            return result > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        double bestDistance = Double.POSITIVE_INFINITY;
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
        return result / this.length;
    }

    @Override
    public Point2d closestPointOnPolyLine(final Point2d point)
    {
        return getLocation(projectOrthogonalFractional(point, true) * this.length);
    }

    /**
     * Perform the project orthogonal operation.
     * @param point the point to project
     * @param limitHandling if Null; results outside this <code>PolyLine2d</code> are replaced by Null, if <code>false</code>,
     *            results outside that interval are returned as is; if<code>true</code> results outside this
     *            <code>PolyLine2d</code> are truncated to the first or last point of this <code>PolyLine2d</code> and therefore
     *            not truly orthogonal
     * @return the orthogonal projection of point on this <code>PolyLine2d</code>
     */
    private Point2d projectOrthogonal(final Point2d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point");
        if (this.lengthIndexedLine.length == 1) // Handle degenerate case
        {
            // limitHandling == true is not handled because it cannot happen
            Point2d result = new Ray2d(this.getLocation(0.0)).projectOrthogonalExtended(point);
            if (null == limitHandling)
            {
                return result.x != this.x[0] || result.y != this.y[0] ? null : get(0);
            }
            // limitHandling is false
            return result;
        }
        double fraction = projectOrthogonalFractional(point, limitHandling);
        if (Double.isNaN(fraction))
        {
            return null;
        }
        return getLocationExtended(fraction * this.length);
    }

    @Override
    public Point2d projectOrthogonal(final Point2d point) throws NullPointerException
    {
        return projectOrthogonal(point, null);
    }

    @Override
    public Point2d projectOrthogonalExtended(final Point2d point) throws NullPointerException
    {
        return projectOrthogonal(point, false);
    }

    @Override
    public final double projectOrthogonalFractional(final Point2d point) throws NullPointerException
    {
        return projectOrthogonalFractional(point, null);
    }

    @Override
    public double projectOrthogonalFractionalExtended(final Point2d point) throws NullPointerException
    {
        return projectOrthogonalFractional(point, false);
    }

    @Override
    public PolyLine2d extract(final double start, final double end)
    {
        Throw.whenNaN(start, "start");
        Throw.whenNaN(end, "end");
        Throw.when(start < 0 || start >= end || end > this.length, IllegalArgumentException.class,
                "Bad interval (%f..%f; length of this PolyLine2d is %f)", start, end, this.length);
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
        return instantiate(pointList);
    }

    @Override
    public PolyLine2d truncate(final double position)
    {
        Throw.when(position <= 0.0 || position > this.length, IllegalArgumentException.class,
                "illegal position (got %f should be in range (0.0, %f])", position, this.length);

        // handle special case: position == length
        if (position == this.length)
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

    @Override
    @SuppressWarnings("checkstyle:methodlength")
    public PolyLine2d offsetLine(final double offset, final double circlePrecision, final double offsetMinimumFilterValue,
            final double offsetMaximumFilterValue, final double offsetFilterRatio, final double minimumOffset)
    {
        Throw.whenNaN(offset, "offset");
        Throw.whenNaN(circlePrecision, "circlePrecision");
        Throw.when(circlePrecision <= 0, IllegalArgumentException.class, "bad circlePrecision");
        Throw.whenNaN(offsetMinimumFilterValue, "offsetMinimumFilterValue");
        Throw.when(offsetMinimumFilterValue <= 0, IllegalArgumentException.class, "bad offsetMinimumFilterValue");
        Throw.whenNaN(offsetMaximumFilterValue, "offsetMaximumFilterValue");
        Throw.when(offsetMaximumFilterValue <= 0, IllegalArgumentException.class, "bad offsetMaximumFilterValue");
        Throw.whenNaN(offsetFilterRatio, "offsetFilterRatio");
        Throw.when(offsetFilterRatio <= 0, IllegalArgumentException.class, "bad offsetFilterRatio");
        Throw.whenNaN(minimumOffset, "minimumOffset");
        Throw.when(minimumOffset <= 0, IllegalArgumentException.class, "bad minimumOffset");
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
            if (index > 0)
            {
                double deltaAngle = AngleUtil.normalizeAroundZero(angle - prevAngle);
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
                                    // System.out.println(new PolyLine2d(tempPoints).toPlot());
                                }
                            }
                            prevSegFrom = prevSegTo;
                        }
                        Point2d nextSegmentIntersection =
                                Point2d.intersectionOfLineSegments(prevSegFrom, intermediatePoint, segmentFrom, segmentTo);
                        if (null != nextSegmentIntersection)
                        {
                            tempPoints.add(nextSegmentIntersection);
                            // System.out.println(new PolyLine2d(tempPoints).toPlot());
                        }
                        tempPoints.add(intermediatePoint);
                        // System.out.println(new PolyLine2d(tempPoints).toPlot());
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
                        double angleDifference = AngleUtil.normalizeAroundZero(angle - pAngle);
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
                            if (tempPoints.size() - 1 == i)
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
            tempPoints.add(segmentFrom);
            tempPoints.add(segmentTo);
            prevPoint = nextPoint;
            prevAngle = angle;
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
        return new PolyLine2d(0.0, tempPoints);
    }

    @Override
    public PolyLine2d offsetLine(final double offsetAtStart, final double offsetAtEnd, final double circlePrecision,
            final double offsetMinimumFilterValue, final double offsetMaximumFilterValue, final double offsetFilterRatio,
            final double minimumOffset)
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

    @Override
    public PolyLine2d transitionLine(final PolyLine2d endLine, final TransitionFunction transition)
    {
        Throw.whenNull(endLine, "endLine");
        Throw.whenNull(transition, "transition");
        List<Point2d> pointList = new ArrayList<>();
        int indexInStart = 0;
        int indexInEnd = 0;
        while (indexInStart < this.size() && indexInEnd < endLine.size())
        {
            double fractionInStart = lengthAtIndex(indexInStart) / this.length;
            double fractionInEnd = endLine.lengthAtIndex(indexInEnd) / endLine.length;
            if (fractionInStart < fractionInEnd)
            {
                pointList.add(get(indexInStart).interpolate(endLine.getLocation(fractionInStart * endLine.length),
                        transition.function(fractionInStart)));
                indexInStart++;
            }
            else if (fractionInStart > fractionInEnd)
            {
                pointList.add(this.getLocation(fractionInEnd * this.length).interpolate(endLine.get(indexInEnd),
                        transition.function(fractionInEnd)));
                indexInEnd++;
            }
            else
            {
                pointList.add(this.get(indexInStart).interpolate(endLine.getLocation(fractionInEnd * endLine.length),
                        transition.function(fractionInStart)));
                indexInStart++;
                indexInEnd++;
            }
        }
        return new PolyLine2d(NO_FILTER, pointList);
    }

    @Override
    public final PolyLine2d offsetLine(final double[] relativeFractions, final double[] offsets,
            final double offsetMinimumFilterValue)
    {
        Throw.whenNull(relativeFractions, "relativeFraction may not be null");
        Throw.whenNull(offsets, "offsets may not be null");
        Throw.when(relativeFractions.length < 2, IllegalArgumentException.class, "size of relativeFractions must be >= 2");
        Throw.when(relativeFractions.length != offsets.length, IllegalArgumentException.class,
                "size of relativeFractions must be equal to size of offsets");
        Throw.when(relativeFractions[0] < 0, IllegalArgumentException.class, "relativeFractions may not start before 0");
        Throw.when(relativeFractions[relativeFractions.length - 1] > 1, IllegalArgumentException.class,
                "relativeFractions may not end beyond 1");
        List<Double> fractionsList = DoubleStream.of(relativeFractions).boxed().collect(Collectors.toList());
        List<Double> offsetsList = DoubleStream.of(offsets).boxed().collect(Collectors.toList());
        if (relativeFractions[0] != 0)
        {
            fractionsList.add(0, 0.0);
            offsetsList.add(0, 0.0);
        }
        if (relativeFractions[relativeFractions.length - 1] < 1.0)
        {
            fractionsList.add(1.0);
            offsetsList.add(0.0);
        }
        PolyLine2d[] offsetLine = new PolyLine2d[fractionsList.size()];
        for (int i = 0; i < fractionsList.size(); i++)
        {
            offsetLine[i] = offsetLine(offsetsList.get(i));
        }
        List<Point2d> out = new ArrayList<>();
        Point2d prevCoordinate = null;
        for (int i = 0; i < offsetsList.size() - 1; i++)
        {
            Throw.when(fractionsList.get(i + 1) <= fractionsList.get(i), IllegalArgumentException.class,
                    "fractions must be in ascending order");
            PolyLine2d startGeometry = offsetLine[i].extractFractional(fractionsList.get(i), fractionsList.get(i + 1));
            PolyLine2d endGeometry = offsetLine[i + 1].extractFractional(fractionsList.get(i), fractionsList.get(i + 1));
            double firstLength = startGeometry.getLength();
            double secondLength = endGeometry.getLength();
            int firstIndex = 0;
            int secondIndex = 0;
            while (firstIndex < startGeometry.size() && secondIndex < endGeometry.size())
            {
                double firstRatio = firstIndex < startGeometry.size() ? startGeometry.lengthAtIndex(firstIndex) / firstLength
                        : Double.MAX_VALUE;
                double secondRatio = secondIndex < endGeometry.size() ? endGeometry.lengthAtIndex(secondIndex) / secondLength
                        : Double.MAX_VALUE;
                double ratio;
                if (firstRatio < secondRatio)
                {
                    ratio = firstRatio;
                    firstIndex++;
                }
                else
                {
                    ratio = secondRatio;
                    secondIndex++;
                }
                Point2d firstCoordinate = startGeometry.getLocation(ratio * firstLength);
                Point2d secondCoordinate = endGeometry.getLocation(ratio * secondLength);
                Point2d resultCoordinate = new Point2d((1 - ratio) * firstCoordinate.x + ratio * secondCoordinate.x,
                        (1 - ratio) * firstCoordinate.y + ratio * secondCoordinate.y);
                if (null == prevCoordinate || resultCoordinate.distance(prevCoordinate) > offsetMinimumFilterValue)
                {
                    out.add(resultCoordinate);
                    prevCoordinate = resultCoordinate;
                }
            }
        }
        return new PolyLine2d(out);
    }

    /**
     * Find a location on this PolyLine2d that is a reasonable projection of a Ray on this line. The result (if not
     * <code>NaN</code>) lies on a line perpendicular to the direction of the <code>Ray</code> and on some segment of this
     * <code>PolyLine</code>. This method attempts to give continuous results for continuous changes of the <code>Ray</code>
     * that must be projected. There are cases where this is simply impossible, or the optimal result is ambiguous. In these
     * cases this method will return something that is hopefully good enough.
     * @param ray the Ray
     * @return length along this <code>PolyLine</code> (some value between 0 and the length of this <code>PolyLine</code>) where
     *         <code>ray</code> projects, or <code>NaN</code> if there is no solution
     * @throws NullPointerException when <code>ray</code> is <code>null</code>
     */
    public double projectRay(final Ray2d ray)
    {
        Throw.whenNull(ray, "ray");
        double bestDistance = Double.POSITIVE_INFINITY;
        double positionAtBestDistance = Double.NaN;
        // Point2d prevPoint = null;
        // Define the line that is perpendicular to ray, passing through the start point of ray
        double perpendicularX = ray.x - Math.sin(ray.dirZ);
        double perpendicularY = ray.y + Math.cos(ray.dirZ);
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

    /**
     * Construct a Path2D from this PolyLine2d. The result is NOT cached (in the current implementation).
     * @return newly constructed <code>Path2D</code> consisting solely of straight segments.
     */
    public Path2D toPath2D()
    {
        Path2D.Double result = new Path2D.Double();
        result.moveTo(this.x[0], this.y[0]);
        for (int i = 1; i < this.x.length; i++)
        {
            result.lineTo(this.x[i], this.y[i]);
        }
        return result;
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

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
        if (this.lengthIndexedLine.length == 1)
        {
            format = String.format(", startHeading=%1$s", doubleFormat);
            result.append(String.format(Locale.US, format, this.startHeading));
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.startHeading);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
        return result;
    }

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
        if (Double.doubleToLongBits(this.startHeading) != Double.doubleToLongBits(other.startHeading))
            return false;
        if (!Arrays.equals(this.x, other.x))
            return false;
        if (!Arrays.equals(this.y, other.y))
            return false;
        return true;
    }

}
