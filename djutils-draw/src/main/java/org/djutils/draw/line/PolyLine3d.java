package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import org.djutils.draw.Drawable3d;
import org.djutils.draw.InvalidProjectionException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;

/**
 * Implementation of PolyLine for 3D space.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class PolyLine3d implements Drawable3d, PolyLine<PolyLine3d, Point3d, Ray3d, DirectedPoint3d, LineSegment3d>
{
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

    /** Bounding box of this PolyLine3d. */
    private final Bounds3d bounds;

    /** Heading at start point (only needed for degenerate PolyLine3d). */
    private final double startDirZ;

    /** Complement of the slope at start point (only needed for degenerate PolyLine3d). */
    private final double startDirY;

    /**
     * Construct a new PolyLine3d from an array of double x values, an array of double y values and an array of double z values.
     * @param copyNeeded if<code>true</code>; a deep copy of the points array is stored instead of the provided array
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param x the x-coordinates of the points
     * @param y the y-coordinates of the points
     * @param z the z-coordinates of the points
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points, or or <code>x</code> and <code>y</code> and <code>z</code> differ in length)
     */
    private PolyLine3d(final boolean copyNeeded, final double epsilon, final double[] x, final double[] y, final double[] z)
    {
        Throw.when(x.length != y.length || x.length != z.length, IllegalArgumentException.class,
                "x, y  and z arrays must have same length");
        double[][] filtered = filterNearDuplicates(epsilon, x, y, z);
        Throw.when(x.length < 2, IllegalArgumentException.class, "Need at least two points");
        this.x = copyNeeded && filtered[0].length == x.length ? Arrays.copyOf(x, x.length) : filtered[0];
        this.y = copyNeeded && filtered[0].length == x.length ? Arrays.copyOf(y, y.length) : filtered[1];
        this.z = copyNeeded && filtered[0].length == x.length ? Arrays.copyOf(z, z.length) : filtered[2];
        double minX = this.x[0];
        double minY = this.y[0];
        double minZ = this.z[0];
        double maxX = this.x[0];
        double maxY = this.y[0];
        double maxZ = this.z[0];
        this.lengthIndexedLine = new double[this.x.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < this.x.length; i++)
        {
            minX = Math.min(minX, this.x[i]);
            minY = Math.min(minY, this.y[i]);
            minZ = Math.min(minZ, this.z[i]);
            maxX = Math.max(maxX, this.x[i]);
            maxY = Math.max(maxY, this.y[i]);
            maxZ = Math.max(maxZ, this.z[i]);
            if (this.x[i - 1] == this.x[i] && this.y[i - 1] == this.y[i] && (this.z[i - 1] == this.z[i]))
            {
                throw new IllegalArgumentException(
                        "Degenerate PolyLine2d; point " + (i - 1) + " has the same x, y and z as point " + i);
            }
            // There should be a varargs Math.hypot implementation
            this.lengthIndexedLine[i] = this.lengthIndexedLine[i - 1]
                    + Math.hypot(Math.hypot(this.x[i] - this.x[i - 1], this.y[i] - this.y[i - 1]), this.z[i] - this.z[i - 1]);
        }
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.bounds = new Bounds3d(minX, maxX, minY, maxY, minZ, maxZ);
        this.startDirZ = Double.NaN;
        this.startDirY = Double.NaN;
    }

    /**
     * Construct a degenerate PolyLine3d (consisting of only one point).
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @param dirY the angle from the positive Z axis direction in radians
     * @param dirZ the angle from the positive X axis direction in radians.
     * @throws ArithmeticException when <code>x</code>, <code>y</code>, <code>z</code>, or <code>dirY</code>, or
     *             <code>dirZ</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>dirY</code>, or <code>dirZ</code> is infinite
     */
    public PolyLine3d(final double x, final double y, final double z, final double dirY, final double dirZ)
    {
        Throw.whenNaN(x, "x");
        Throw.whenNaN(y, "y");
        Throw.whenNaN(z, "z");
        Throw.whenNaN(dirY, "dirY");
        Throw.when(Double.isInfinite(dirY), IllegalArgumentException.class, "dirY must be finite");
        Throw.whenNaN(dirZ, "dirZ");
        Throw.when(Double.isInfinite(dirZ), IllegalArgumentException.class, "dirZ must be finite");
        this.x = new double[] {x};
        this.y = new double[] {y};
        this.z = new double[] {z};
        this.startDirY = dirY;
        this.startDirZ = dirZ;
        this.length = 0;
        this.bounds = new Bounds3d(x, x, y, y, z, z);
        this.lengthIndexedLine = new double[] {0.0};
    }

    /**
     * Construct a degenerate PolyLine3d (consisting of only one point).
     * @param p the point of the degenerate PolyLine3d
     * @param dirY the angle from the positive Z axis direction in radians
     * @param dirZ the angle from the positive X axis direction in the XY plane in radians.
     * @throws NullPointerException when <code>p</code> is <code>null</code>
     * @throws ArithmeticException when <code>dirY</code>, or <code>dirZ</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>dirY</code>, or <code>dirZ</code> is infinite
     */
    public PolyLine3d(final Point3d p, final double dirY, final double dirZ)
    {
        this(Throw.whenNull(p, "p").x, p.y, p.z, dirY, dirZ);
    }

    /**
     * Construct a degenerate PolyLine3d (consisting of only one point).
     * @param directedPoint3d point, <code>dirY</code> and <code>dirZ</code> of the degenerate PolyLine3d
     * @throws NullPointerException when <code>p</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>dirY</code> or <code>dirZ</code> is infinite (should not be possible)
     */
    public PolyLine3d(final DirectedPoint3d directedPoint3d) throws NullPointerException, IllegalArgumentException
    {
        this(Throw.whenNull(directedPoint3d, "r").x, directedPoint3d.y, directedPoint3d.z, directedPoint3d.dirY,
                directedPoint3d.dirZ);
    }

    /**
     * Construct a new PolyLine3d from an array of Point2d. This constructor makes a deep copy of the parameters.
     * @param x the x-coordinates of the points
     * @param y the y-coordinates of the points
     * @param z the z-coordinates of the points
     * @throws NullPointerException when <code>x</code>, <code>y</code>, or <code>z</code> is <code>null</code>
     * @throws IllegalArgumentException when the provided coordinate values do not constitute a valid line (too few points or
     *             identical adjacent points, or the arrays are not the same length)
     */
    public PolyLine3d(final double[] x, final double[] y, final double[] z)
    {
        this(NO_FILTER, x, y, z);
    }

    /**
     * Construct a new PolyLine3d from an array of Point2d. This constructor makes a deep copy of the parameters.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param x the x-coordinates of the points
     * @param y the y-coordinates of the points
     * @param z the z-coordinates of the points
     * @throws NullPointerException when <code>x</code>, <code>y</code>, or <code>z</code> is <code>null</code>
     * @throws IllegalArgumentException when the provided coordinate values do not constitute a valid line (too few points or
     *             identical adjacent points, or the arrays are not the same length)
     */
    public PolyLine3d(final double epsilon, final double[] x, final double[] y, final double[] z)
    {
        this(true, epsilon, x, y, z);
    }

    /**
     * Construct a new PolyLine3d from an array of Point3d.
     * @param points the array of points to construct this PolyLine3d from.
     * @throws NullPointerException when the <code>points</code> array is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final Point3d[] points)
    {
        this(NO_FILTER, points);
    }

    /**
     * Construct a new PolyLine3d from an array of Point3d.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param points the array of points to construct this PolyLine3d from.
     * @throws NullPointerException when the <code>points</code> array is <code>null</code>
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final double epsilon, final Point3d[] points)
    {
        this(false, epsilon, makeArray(Throw.whenNull(points, "points"), p -> p.x), makeArray(points, p -> p.y),
                makeArray(points, p -> p.z));
    }

    /**
     * Make an array of double an fill it with the appropriate coordinate of points.
     * @param points array of points
     * @param getter function that obtains the intended coordinate
     * @return array of double values filled with the requested coordinate values
     */
    protected static double[] makeArray(final Point3d[] points, final Function<Point3d, Double> getter)
    {
        double[] array = new double[points.length];
        for (int index = 0; index < points.length; index++)
        {
            array[index] = getter.apply(points[index]);
        }
        return array;
    }

    /**
     * Construct a new PolyLine3d from two or more Point3d arguments.
     * @param point1 starting point of the PolyLine3d
     * @param point2 second point of the PolyLine3d
     * @param otherPoints additional points of the PolyLine3d (may be <code>null</code>, or have zero length)
     * @throws NullPointerException when <code>point1</code>, or <code>point2</code> is <code>null</code>, or
     *             <code>otherPoints</code> contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final Point3d point1, final Point3d point2, final Point3d... otherPoints)
    {
        this(NO_FILTER, point1, point2, otherPoints);
    }

    /**
     * Construct a new PolyLine3d from two or more Point3d arguments.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param point1 starting point of the PolyLine3d
     * @param point2 second point of the PolyLine3d
     * @param otherPoints additional points of the PolyLine3d (may be <code>null</code>, or have zero length)
     * @throws NullPointerException when <code>point1</code>, or <code>point2</code> is <code>null</code>, or
     *             <code>otherPoints</code> contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final double epsilon, final Point3d point1, final Point3d point2, final Point3d... otherPoints)
    {
        this(epsilon, spliceArray(point1, point2, otherPoints));
    }

    /**
     * Construct an array of Point3d from two points plus an array of Point3d.
     * @param point1 the first point (ends up at index 0 of the result)
     * @param point2 the second point (ends up at index 1 of the result)
     * @param otherPoints may be <code>null</code>, may be empty. If non empty, the elements in otherPoints end up at index 2
     *            and up in the result
     * @return the combined array
     * @throws NullPointerException when <code>point1</code> or <code>point2</code> is <code>null</code>
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
     * Construct a new PolyLine3d from an iterator that yields Point3d objects.
     * @param iterator iterator that will provide all points that constitute the new PolyLine3d
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>, or yields a <code>null</code> value
     * @throws IllegalArgumentException when the <code>iterator</code> provides too few points, or some adjacent identical
     *             points)
     */
    public PolyLine3d(final Iterator<Point3d> iterator)
    {
        this(NO_FILTER, iterator);
    }

    /**
     * Construct a new PolyLine3d from an iterator that yields Point3d objects.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param iterator iterator that will provide all points that constitute the new PolyLine3d
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>, or yields a <code>null</code> value
     * @throws IllegalArgumentException when the <code>iterator</code> provides too few points, or some adjacent identical
     *             points)
     */
    public PolyLine3d(final double epsilon, final Iterator<Point3d> iterator)
    {
        this(epsilon, iteratorToList(Throw.whenNull(iterator, "iterator")));
    }

    /**
     * Construct a new PolyLine3d from a List&lt;Point3d&gt;.
     * @param pointList the list of points to construct the new PolyLine3d from.
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final List<Point3d> pointList)
    {
        this(NO_FILTER, pointList);
    }

    /**
     * Construct a new PolyLine3d from a List&lt;Point3d&gt;.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param pointList the list of points to construct the new PolyLine3d from.
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final double epsilon, final List<Point3d> pointList)
    {
        this(epsilon, pointList.toArray(new Point3d[Throw.whenNull(pointList, "pointList").size()]));
    }

    /**
     * Construct a new PolyLine3d from an existing one. This constructor is primarily intended for use in extending classes.
     * @param polyLine the existing PolyLine3d.
     * @throws NullPointerException when <code>polyLine</code> is <code>null</code>
     */
    public PolyLine3d(final PolyLine3d polyLine)
    {
        this.x = polyLine.x;
        this.y = polyLine.y;
        this.z = polyLine.z;
        this.lengthIndexedLine = polyLine.lengthIndexedLine;
        this.length = polyLine.length;
        this.bounds = polyLine.bounds;
        this.startDirY = polyLine.startDirY;
        this.startDirZ = polyLine.startDirZ;
    }

    @Override
    public PolyLine3d instantiate(final double epsilon, final List<Point3d> pointList)
    {
        return new PolyLine3d(epsilon, pointList);
    }

    /**
     * Build a list from the Point3d objects that an iterator provides.
     * @param iterator the iterator that will provide the points
     * @return a list of the points provided by the iterator
     * @throws NullPointerException when <code>iterator</code> is <code>null</code>
     */
    static List<Point3d> iteratorToList(final Iterator<Point3d> iterator)
    {
        List<Point3d> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    @Override
    public int size()
    {
        return this.x.length;
    }

    @Override
    public final Point3d get(final int i) throws IndexOutOfBoundsException
    {
        return new Point3d(this.x[i], this.y[i], this.z[i]);
    }

    @Override
    public final double getX(final int i) throws IndexOutOfBoundsException
    {
        return this.x[i];
    }

    @Override
    public final double getY(final int i) throws IndexOutOfBoundsException
    {
        return this.y[i];
    }

    /**
     * Return the z-coordinate of a point of this PolyLine.
     * @param index the index of the requested z-coordinate
     * @return the z-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    public final double getZ(final int index)
    {
        return this.z[index];
    }

    @Override
    public LineSegment3d getSegment(final int index)
    {
        Throw.when(index < 0 || index >= this.x.length - 1, IndexOutOfBoundsException.class,
                "index must be in range [0, size() - 1) (got %d)", index);
        return new LineSegment3d(this.x[index], this.y[index], this.z[index], this.x[index + 1], this.y[index + 1],
                this.z[index + 1]);
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
    public Iterator<Point3d> iterator()
    {
        return new Iterator<Point3d>()
        {
            private int nextIndex = 0;

            @Override
            public boolean hasNext()
            {
                return this.nextIndex < size();
            }

            @Override
            public Point3d next()
            {
                return get(this.nextIndex++);
            }
        };
    }

    @Override
    public Bounds3d getAbsoluteBounds()
    {
        return this.bounds;
    }

    @Override
    public final PolyLine3d noiseFilteredLine(final double noiseLevel)
    {
        if (this.size() <= 2)
        {
            return this; // Except for some cached fields; a PolyLine2d is immutable; so safe to return
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
        return new PolyLine3d(list);
    }

    /**
     * Concatenate several PolyLine3d instances.
     * @param lines one or more PolyLine3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match the first of the
     *            second, etc.
     * @return PolyLine3d
     * @throws NullPointerException when <code>lines</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine3d concatenate(final PolyLine3d... lines)
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two PolyLine3d instances. This method is separate for efficiency reasons.
     * @param tolerance the tolerance between the end point of a line and the first point of the next line
     * @param line1 first line
     * @param line2 second line
     * @return the concatenation of the two lines
     * @throws NullPointerException when <code>line1</code>, or <code>line2</code> is <code>null</code>
     * @throws IllegalArgumentException when there is a gap between the lines
     */
    public static PolyLine3d concatenate(final double tolerance, final PolyLine3d line1, final PolyLine3d line2)
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new IllegalArgumentException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst()
                    + " distance is " + line1.getLast().distance(line2.getFirst()) + " > " + tolerance);
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
     * Concatenate several PolyLine3d instances.
     * @param tolerance the tolerance between the end point of a line and the first point of the next line
     * @param lines one or more PolyLine3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match the first of the
     *            second within the provided tolerance value, etc.
     * @return the concatenation of the lines
     * @throws NullPointerException when <code>lines</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException if zero lines are given, or when there is a gap larger than tolerance between
     *             consecutive lines
     */
    public static PolyLine3d concatenate(final double tolerance, final PolyLine3d... lines)
    {
        Throw.when(0 == lines.length, IllegalArgumentException.class, "Empty argument list");
        if (1 == lines.length)
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

    @Override
    public PolyLine2d project() throws InvalidProjectionException
    {
        for (int i = 1; i < size(); i++)
        {
            if (this.x[i] != this.x[0] || this.y[i] != this.y[0])
            {
                break;
            }
            Throw.when(i == size() - 1, InvalidProjectionException.class, "all points project onto the same point");
        }
        return new PolyLine2d(false, 0.0, this.x, this.y);
    }

    @Override
    public final DirectedPoint3d getLocationExtended(final double position)
    {
        if (position >= 0.0 && position <= this.length)
        {
            return getLocation(position);
        }

        // position before start point -- extrapolate using direction from first point to second point of this PolyLine2d
        if (position < 0.0)
        {
            double fraction = position / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            return new DirectedPoint3d(this.x[0] + fraction * (this.x[1] - this.x[0]),
                    this.y[0] + fraction * (this.y[1] - this.y[0]), this.z[0] + fraction * (this.z[1] - this.z[0]), this.x[1],
                    this.y[1], this.z[1]);
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
                return new DirectedPoint3d(this.x[n1], this.y[n1], this.z[n1], 0.0, 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        return new DirectedPoint3d(this.x[n1] + fraction * (this.x[n1] - this.x[n2]),
                this.y[n1] + fraction * (this.y[n1] - this.y[n2]), this.z[n1] + fraction * (this.z[n1] - this.z[n2]),
                Math.atan2(Math.hypot(this.x[n1] - this.x[n2], this.y[n1] - this.y[n2]), this.z[n1] - this.z[n2]),
                Math.atan2(this.y[n1] - this.y[n2], this.x[n1] - this.x[n2]));
    }

    @Override
    public final DirectedPoint3d getLocation(final double position)
    {
        Throw.whenNaN(position, "position");
        Throw.when(position < 0.0 || position > this.length, IllegalArgumentException.class,
                "illegal position (got %f, should be in range [0.0, %f])", position, this.length);
        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            if (this.lengthIndexedLine.length == 1) // Extra special case; degenerate PolyLine2d
            {
                return new Ray3d(this.x[0], this.y[0], this.z[0], this.startDirZ, this.startDirY);
            }
            return new DirectedPoint3d(this.x[0], this.y[0], this.z[0], this.x[1], this.y[1], this.z[1]);
        }
        if (position == this.length)
        {
            return new DirectedPoint3d(this.x[this.x.length - 1], this.y[this.x.length - 1], this.z[this.x.length - 1],
                    2 * this.x[this.x.length - 1] - this.x[this.x.length - 2],
                    2 * this.y[this.x.length - 1] - this.y[this.x.length - 2],
                    2 * this.z[this.x.length - 1] - this.z[this.x.length - 2]);
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
        return new DirectedPoint3d(this.x[index] + fraction * (this.x[index + 1] - this.x[index]),
                this.y[index] + fraction * (this.y[index + 1] - this.y[index]),
                this.z[index] + fraction * (this.z[index + 1] - this.z[index]), 2 * this.x[index + 1] - this.x[index],
                2 * this.y[index + 1] - this.y[index], 2 * this.z[index + 1] - this.z[index]);
    }

    /**
     * Perform the orthogonal projection operation.
     * @param point the point to project
     * @param limitHandling if <code>Null</code>; results outside the interval [0.0, 1.0] are replaced by <code>NaN</code>, if
     *            <code>false</code>, results outside that interval are returned as is; if <code>true</code> results outside the
     *            interval are truncated to the interval and therefore not truly orthogonal
     * @return the fractional position on this <code>PolyLine3d</code> that is closest to point, or <code>NaN</code>
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    private double projectOrthogonalFractional(final Point3d point, final Boolean limitHandling)
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
            result = new Ray3d(getLocation(0.0)).projectOrthogonalFractionalExtended(point);
            if (null == limitHandling)
            {
                return result == 0.0 ? 0.0 : Double.NaN;
            }
            // limitHanling is false
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
            double fraction = point.fractionalPositionOnLine(this.x[index - 1], this.y[index - 1], this.z[index - 1],
                    this.x[index], this.y[index], this.z[index], false, false);
            double distance = Math.hypot(
                    Math.hypot(point.x - (this.x[index - 1] + fraction * (this.x[index] - this.x[index - 1])),
                            point.y - (this.y[index - 1] + fraction * (this.y[index] - this.y[index - 1]))),
                    point.z - (this.z[index - 1] + fraction * (this.z[index] - this.z[index - 1])));
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
                distance = Math.hypot(Math.hypot(point.x - this.x[index - 1], point.y - this.y[index - 1]),
                        point.z - this.z[index - 1]);
                if (distance < bestDistance)
                {
                    bestDistance = distance;
                    result = lengthAtIndex(index - 1);
                }
            }
            else if (index == this.size() - 1 && limitHandling != null && limitHandling)
            {
                distance = Math.hypot(Math.hypot(point.x - this.x[index], point.y - this.y[index]), point.z - this.z[index]);
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
    public DirectedPoint3d closestPointOnPolyLine(final Point3d point)
    {
        return getLocation(projectOrthogonalFractional(point, true) * this.length);
    }

    /**
     * Perform the project orthogonal operation.
     * @param point the point to project
     * @param limitHandling if <code>Null</code>; results outside the interval [0.0, 1.0] are replaced by <code>NaN</code>, if
     *            <code>false</code>, results outside that interval are returned as is; if <code>true</code> results outside the
     *            interval are truncated to the interval and therefore not truly orthogonal
     * @return the fractional position on this <code>PolyLine3d</code> that is closest to point, or <code>NaN</code>
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    private Point3d projectOrthogonal(final Point3d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point");
        if (this.lengthIndexedLine.length == 1) // Handle degenerate case
        {
            // limitHandling == true is not handled because it cannot happen
            Point3d result = new Ray3d(getLocation(0.0)).projectOrthogonalExtended(point);
            if (null == limitHandling)
            {
                return result.x != this.x[0] || result.y != this.y[0] || result.z != this.z[0] ? null : get(0);
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
    public Point3d projectOrthogonal(final Point3d point)
    {
        return projectOrthogonal(point, null);
    }

    @Override
    public Point3d projectOrthogonalExtended(final Point3d point)
    {
        return projectOrthogonal(point, false);
    }

    @Override
    public final double projectOrthogonalFractional(final Point3d point)
    {
        return projectOrthogonalFractional(point, null);
    }

    @Override
    public double projectOrthogonalFractionalExtended(final Point3d point)
    {
        return projectOrthogonalFractional(point, false);
    }

    @Override
    public PolyLine3d extract(final double start, final double end)
    {
        Throw.whenNaN(start, "start");
        Throw.whenNaN(end, "end");
        Throw.when(start < 0 || start >= end || end > this.length, IllegalArgumentException.class,
                "Bad interval (%f...%f; length of this PolyLine3d is %f)", start, end, this.length);
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
        return instantiate(pointList);
    }

    @Override
    public PolyLine3d offsetLine(final double offset, final double circlePrecision, final double offsetMinimumFilterValue,
            final double offsetMaximumFilterValue, final double offsetFilterRatio, final double minimumOffset)
    {
        return restoreElevation(project().offsetLine(offset, circlePrecision, offsetMinimumFilterValue,
                offsetMaximumFilterValue, offsetFilterRatio, minimumOffset));
    }

    @Override
    public PolyLine3d offsetLine(final double[] relativeFractions, final double[] offsets,
            final double offsetMinimumFilterValue)
    {
        return restoreElevation(project().offsetLine(relativeFractions, offsets, offsetMinimumFilterValue));
    }

    /**
     * Approximate the z-values of a PolyLine that was made 2d in order to apply some modification.
     * @param flat the modified 2d PolyLine
     * @return the PolyLine with approximately restored z-values
     */
    private PolyLine3d restoreElevation(final PolyLine2d flat)
    {
        List<Point3d> points = new ArrayList<>();
        int start = 0;
        for (int index = 0; index < flat.size(); index++)
        {
            Point2d point2d = flat.get(index);
            // Find the closest point in reference to point2d; starting at start
            int bestIndex = start;
            double bestDistance = Double.POSITIVE_INFINITY;
            Point3d bestInReference = null;
            for (int i = start; i < size(); i++)
            {
                Point3d pointInReference = get(i);
                double distance = point2d.distance(pointInReference.project());
                if (distance < bestDistance)
                {
                    bestIndex = i;
                    bestDistance = distance;
                    bestInReference = pointInReference;
                }
            }
            points.add(new Point3d(point2d, bestInReference.z));
            start = bestIndex;
        }
        return new PolyLine3d(points);
    }

    @Override
    public PolyLine3d offsetLine(final double offsetAtStart, final double offsetAtEnd, final double circlePrecision,
            final double offsetMinimumFilterValue, final double offsetMaximumFilterValue, final double offsetFilterRatio,
            final double minimumOffset)
    {
        if (offsetAtStart == offsetAtEnd)
        {
            return offsetLine(offsetAtStart, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
                    offsetFilterRatio, minimumOffset);
        }
        PolyLine3d atStart = offsetLine(offsetAtStart, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
                offsetFilterRatio, minimumOffset);
        PolyLine3d atEnd = offsetLine(offsetAtEnd, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
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
    public PolyLine3d transitionLine(final PolyLine3d endLine, final TransitionFunction transition)
    {
        Throw.whenNull(endLine, "endLine");
        Throw.whenNull(transition, "transition");
        List<Point3d> pointList = new ArrayList<>();
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
        return new PolyLine3d(0.0, pointList);
    }

    @Override
    public PolyLine3d truncate(final double position)
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
            result.append("PolyLine3d ");
        }
        String format = String.format("%%sx=%1$s, y=%1$s, z=%1$s", doubleFormat);
        for (int index = 0; index < this.x.length; index++)
        {
            result.append(
                    String.format(Locale.US, format, index == 0 ? "[" : ", ", this.x[index], this.y[index], this.z[index]));
        }
        if (this.lengthIndexedLine.length == 1)
        {
            format = String.format(", startDirY=%1$s, startDirZ=%1$s", doubleFormat);
            result.append(String.format(Locale.US, format, this.startDirY, this.startDirZ));
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
        temp = Double.doubleToLongBits(this.startDirZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.startDirY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
        result = prime * result + Arrays.hashCode(this.z);
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
        PolyLine3d other = (PolyLine3d) obj;
        if (Double.doubleToLongBits(this.startDirZ) != Double.doubleToLongBits(other.startDirZ))
            return false;
        if (Double.doubleToLongBits(this.startDirY) != Double.doubleToLongBits(other.startDirY))
            return false;
        if (!Arrays.equals(this.x, other.x))
            return false;
        if (!Arrays.equals(this.y, other.y))
            return false;
        if (!Arrays.equals(this.z, other.z))
            return false;
        return true;
    }

}
