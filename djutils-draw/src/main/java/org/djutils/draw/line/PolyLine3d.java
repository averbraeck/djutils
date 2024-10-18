package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;

/**
 * Implementation of PolyLine for 3D space.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PolyLine3d implements Drawable3d, PolyLine<PolyLine3d, Point3d, Ray3d, DirectedPoint3d, LineSegment3d>
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

    /** Bounding box of this PolyLine3d. */
    private final Bounds3d bounds;

    /** Heading at start point (only needed for degenerate PolyLine3d). */
    private final double startPhi;

    /** Heading at start point (only needed for degenerate PolyLine3d). */
    private final double startTheta;

    /**
     * Construct a new PolyLine3d from an array of double x values, an array of double y values and an array of double z values.
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
        this.startPhi = Double.NaN;
        this.startTheta = Double.NaN;
    }

    /**
     * Construct a degenerate PolyLine3d (consisting of only one point).
     * @param x double; the x-coordinate
     * @param y double; the y-coordinate
     * @param z double; the z-coordinate
     * @param phi double; the angle from the positive X axis direction in radians.
     * @param theta double; the angle from the positive Z axis direction in radians
     * @throws DrawRuntimeException when x, y, or heading is NaN, or heading is infinite
     */
    public PolyLine3d(final double x, final double y, final double z, final double phi, final double theta)
            throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(x), DrawRuntimeException.class, "x may not be NaN");
        Throw.when(Double.isNaN(y), DrawRuntimeException.class, "y may not be NaN");
        Throw.when(Double.isNaN(z), DrawRuntimeException.class, "z may not be NaN");
        Throw.when(Double.isNaN(phi), DrawRuntimeException.class, "phi may not be NaN");
        Throw.when(Double.isInfinite(phi), DrawRuntimeException.class, "phi must be finite");
        Throw.when(Double.isNaN(theta), DrawRuntimeException.class, "theta may not be NaN");
        Throw.when(Double.isInfinite(theta), DrawRuntimeException.class, "theta must be finite");
        this.x = new double[] {x};
        this.y = new double[] {y};
        this.z = new double[] {z};
        this.startPhi = phi;
        this.startTheta = theta;
        this.length = 0;
        this.bounds = new Bounds3d(x, x, y, y, z, z);
        this.lengthIndexedLine = new double[] {0.0};
    }

    /**
     * Construct a degenerate PolyLine3d (consisting of only one point).
     * @param p Point3d; the point of the degenerate PolyLine3d
     * @param phi double; the angle from the positive X axis direction in the XY plane in radians.
     * @param theta double; the angle from the positive Z axis direction in radians
     * @throws NullPointerException when p is null
     * @throws DrawRuntimeException when heading is infinite
     */
    public PolyLine3d(final Point3d p, final double phi, final double theta) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(p, "p may not be null").x, p.y, p.z, phi, theta);
    }

    /**
     * Construct a degenerate PolyLine3d (consisting of only one point).
     * @param r Ray3d; point, phi and theta of the degenerate PolyLine3d
     * @throws NullPointerException when p is null
     * @throws DrawRuntimeException when phi or theta is infinite
     */
    public PolyLine3d(final Ray3d r) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(r, "r may not be NaN").x, r.y, r.z, r.dirZ, r.dirY);
    }

    /**
     * Construct a new PolyLine3d from an array of Point2d. This constructor makes a deep copy of the parameters.
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
     * Construct a new PolyLine3d from an array of Point3d.
     * @param points Point3d[]; the array of points to construct this PolyLine3d from.
     * @throws NullPointerException when the array is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final Point3d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(false, makeArray(Throw.whenNull(points, "points may not be null"), p -> p.x), makeArray(points, p -> p.y),
                makeArray(points, p -> p.z));
    }

    /**
     * Make an array of double an fill it with the appropriate coordinate of points.
     * @param points Point3d[]; array of points
     * @param getter Function&lt;Point3d, Double&gt;; function that obtains the intended coordinate
     * @return double[]; array of double filled with the requested coordinate values
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
     * @param point1 Point3d; starting point of the PolyLine3d
     * @param point2 Point3d; second point of the PolyLine3d
     * @param otherPoints Point3d...; additional points of the PolyLine3d (may be null, or have zero length)
     * @throws NullPointerException when point1, or point2 is null, or otherPoints contains a null value
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
     * @throws NullPointerException when point1 or point2 is null
     */
    private static Point3d[] spliceArray(final Point3d point1, final Point3d point2, final Point3d... otherPoints)
            throws NullPointerException
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
     * @param iterator Iterator&lt;Point3d&gt;; iterator that will provide all points that constitute the new PolyLine3d
     * @throws NullPointerException when iterator is null, or yields a null
     * @throws DrawRuntimeException when the iterator provides too few points, or some adjacent identical points)
     */
    public PolyLine3d(final Iterator<Point3d> iterator) throws NullPointerException, DrawRuntimeException
    {
        this(iteratorToList(Throw.whenNull(iterator, "iterator cannot be null")));
    }

    /**
     * Construct a new PolyLine3d from a List&lt;Point3d&gt;.
     * @param pointList List&lt;Point3d&gt;; the list of points to construct the new PolyLine3d from.
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public PolyLine3d(final List<Point3d> pointList) throws DrawRuntimeException
    {
        this(pointList.toArray(new Point3d[Throw.whenNull(pointList, "pointList may not be null").size()]));
    }

    /**
     * Create a new PolyLine3d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param points Point3d...; the coordinates of the line as Point2d
     * @throws DrawRuntimeException when number of points &lt; 2
     */
    public PolyLine3d(final boolean filterDuplicates, final Point3d... points) throws DrawRuntimeException
    {
        this(PolyLine3d.cleanPoints(filterDuplicates, Arrays.stream(points).iterator()));
    }

    /**
     * Create a new PolyLine3d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param pointList List&lt;Point3d&gt;; list of the coordinates of the line as Point3d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @throws DrawRuntimeException when number of non-equal points &lt; 2
     */
    public PolyLine3d(final boolean filterDuplicates, final List<Point3d> pointList) throws DrawRuntimeException
    {
        this(PolyLine3d.cleanPoints(filterDuplicates, pointList.iterator()));
    }

    /**
     * Return an iterator that optionally skips identical successive points.
     * @param filter boolean; if true; filter out identical successive points; if false; do not filter
     * @param iterator Iterator&lt;Point3d&gt;; iterator that generates points, potentially with successive duplicates
     * @return Iterator&lt;Point3d&gt;; iterator that skips identical successive points
     */
    static Iterator<Point3d> cleanPoints(final boolean filter, final Iterator<Point3d> iterator)
    {
        Throw.whenNull(iterator, "Iterator may not be null");
        Throw.when(!iterator.hasNext(), DrawRuntimeException.class, "Iterator has no points to return");
        if (!filter)
        {
            return iterator;
        }
        return new Iterator<Point3d>()
        {
            private Point3d currentPoint = iterator.next();

            @Override
            public boolean hasNext()
            {
                return this.currentPoint != null;
            }

            @Override
            public Point3d next()
            {
                Throw.when(this.currentPoint == null, NoSuchElementException.class, "Out of input");
                Point3d result = this.currentPoint;
                this.currentPoint = null;
                while (iterator.hasNext())
                {
                    this.currentPoint = iterator.next();
                    if (result.x != this.currentPoint.x || result.y != this.currentPoint.y || result.z != this.currentPoint.z)
                    {
                        break;
                    }
                    this.currentPoint = null;
                }
                return result;
            }
        };
    }

    /**
     * Construct a new PolyLine3d from an existing one. This constructor is primarily intended for use in extending classes.
     * @param polyLine PolyLine3d; the existing PolyLine3d.
     */
    public PolyLine3d(final PolyLine3d polyLine)
    {
        this.x = polyLine.x;
        this.y = polyLine.y;
        this.z = polyLine.z;
        this.lengthIndexedLine = polyLine.lengthIndexedLine;
        this.length = polyLine.length;
        this.bounds = polyLine.bounds;
        this.startPhi = polyLine.startPhi;
        this.startTheta = polyLine.startTheta;
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
            @Override
            public boolean hasNext()
            {
                return this.nextIndex < size();
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

    /** {@inheritDoc} */
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
     * @param lines PolyLine3d...; one or more PolyLine3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match
     *            the first of the second, etc.
     * @return PolyLine3d
     * @throws DrawRuntimeException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine3d concatenate(final PolyLine3d... lines) throws DrawRuntimeException
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two PolyLine3d instances. This method is separate for efficiency reasons.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param line1 PolyLine3d; first line
     * @param line2 PolyLine3d; second line
     * @return PolyLine3d; the concatenation of the two lines
     * @throws DrawRuntimeException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static PolyLine3d concatenate(final double tolerance, final PolyLine3d line1, final PolyLine3d line2)
            throws DrawRuntimeException
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new DrawRuntimeException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst()
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
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param lines PolyLine3d...; one or more PolyLine3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match
     *            the first of the second within the provided tolerance value, etc.
     * @return PolyLine3d; the concatenation of the lines
     * @throws DrawRuntimeException if zero lines are given, or when there is a gap larger than tolerance between consecutive
     *             lines
     */
    public static PolyLine3d concatenate(final double tolerance, final PolyLine3d... lines) throws DrawRuntimeException
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
                // TODO; rewrite so that the arrays are only copied when they need to be shortened
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

    /** {@inheritDoc} */
    @Override
    public final Ray3d getLocationExtended(final double position)
    {
        if (position >= 0.0 && position <= getLength())
        {
            return getLocation(position);
        }

        // position before start point -- extrapolate using direction from first point to second point of this PolyLine2d
        if (position < 0.0)
        {
            double fraction = position / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            return new Ray3d(this.x[0] + fraction * (this.x[1] - this.x[0]), this.y[0] + fraction * (this.y[1] - this.y[0]),
                    this.z[0] + fraction * (this.z[1] - this.z[0]), this.x[1], this.y[1], this.z[1]);
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
                return new Ray3d(this.x[n1], this.y[n1], this.z[n1], 0.0, 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        return new Ray3d(this.x[n1] + fraction * (this.x[n1] - this.x[n2]), this.y[n1] + fraction * (this.y[n1] - this.y[n2]),
                this.z[n1] + fraction * (this.z[n1] - this.z[n2]),
                Math.atan2(Math.hypot(this.x[n1] - this.x[n2], this.y[n1] - this.y[n2]), this.z[n1] - this.z[n2]),
                Math.atan2(this.y[n1] - this.y[n2], this.x[n1] - this.x[n2]));
    }

    /** {@inheritDoc} */
    @Override
    public final Ray3d getLocation(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position), DrawRuntimeException.class, "position may not be NaN");
        Throw.when(position < 0.0 || position > getLength(), DrawRuntimeException.class,
                "getLocation for line: position < 0.0 or > line length. Position = " + position + "; length = " + getLength());
        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            if (this.lengthIndexedLine.length == 1) // Extra special case; degenerate PolyLine2d
            {
                return new Ray3d(this.x[0], this.y[0], this.z[0], this.startPhi, this.startTheta);
            }
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
        // if (fraction >= 1.0 && index < this.x.length - 1)
        // {
        // // Rounding problem; move to the next segment.
        // index++;
        // remainder = position - this.lengthIndexedLine[index];
        // fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        // }
        return new Ray3d(this.x[index] + fraction * (this.x[index + 1] - this.x[index]),
                this.y[index] + fraction * (this.y[index + 1] - this.y[index]),
                this.z[index] + fraction * (this.z[index + 1] - this.z[index]), 2 * this.x[index + 1] - this.x[index],
                2 * this.y[index + 1] - this.y[index], 2 * this.z[index + 1] - this.z[index]);
    }

    /**
     * Perform the orthogonal projection operation.
     * @param point Point3d; the point to project
     * @param limitHandling Boolean; if Null; results outside the interval 0.0 .. 1.0 are replaced by NaN, if false, results
     *            outside that interval are returned as is; if true results outside the interval are truncated to the interval
     *            and therefore not truly orthogonal
     * @return double; the fractional position on this PolyLine3d that is closest to point, or NaN
     */
    private double projectOrthogonalFractional(final Point3d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point may not be null");
        double result = Double.NaN;
        if (this.lengthIndexedLine.length == 1)
        {
            // This is a degenerate PolyLine2d
            if (null != limitHandling && limitHandling)
            {
                return 0.0;
            }
            result = getLocation(0.0).projectOrthogonalFractionalExtended(point);
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
        return result / getLength();
    }

    /** {@inheritDoc} */
    @Override
    public Point3d closestPointOnPolyLine(final Point3d point)
    {
        return getLocation(projectOrthogonalFractional(point, true) * getLength());
    }

    /**
     * Perform the project orthogonal operation.
     * @param point Point3d; the point to project
     * @param limitHandling Boolean; if Null; results outside this PolyLin2de are replaced by Null, if false, results outside
     *            that interval are returned as is; if true results outside this PolyLine2d are truncated to the first or last
     *            point of this PolyLine2d and therefore not truly orthogonal
     * @return Point3d; the orthogonal projection of point on this PolyLine3d
     */
    private Point3d projectOrthogonal(final Point3d point, final Boolean limitHandling)
    {
        Throw.whenNull(point, "point may not be null");
        if (this.lengthIndexedLine.length == 1) // Handle degenerate case
        {
            // limitHandling == true is not handled because it cannot happen
            Point3d result = this.getLocation(0.0).projectOrthogonalExtended(point);
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
        return getLocationExtended(fraction * getLength());
    }

    /** {@inheritDoc} */
    @Override
    public Point3d projectOrthogonal(final Point3d point) throws NullPointerException
    {
        return projectOrthogonal(point, null);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d projectOrthogonalExtended(final Point3d point) throws NullPointerException
    {
        return projectOrthogonal(point, false);
    }

    /** {@inheritDoc} */
    @Override
    public final double projectOrthogonalFractional(final Point3d point) throws NullPointerException
    {
        return projectOrthogonalFractional(point, null);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point3d point) throws NullPointerException
    {
        return projectOrthogonalFractional(point, false);
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine3d extract(final double start, final double end) throws DrawRuntimeException
    {
        if (Double.isNaN(start) || Double.isNaN(end) || start < 0 || start >= end || end > getLength())
        {
            throw new DrawRuntimeException(
                    "Bad interval (" + start + ".." + end + "; length of this PolyLine3d is " + this.getLength() + ")");
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
        return instantiate(pointList);
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine3d offsetLine(final double offset, final double circlePrecision, final double offsetMinimumFilterValue,
            final double offsetMaximumFilterValue, final double offsetFilterRatio, final double minimumOffset)
            throws IllegalArgumentException
    {
        PolyLine2d flat = project().offsetLine(offset, circlePrecision, offsetMinimumFilterValue, offsetMaximumFilterValue,
                offsetFilterRatio, minimumOffset);
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

    /** {@inheritDoc} */
    @Override
    public PolyLine3d offsetLine(final double offsetAtStart, final double offsetAtEnd, final double circlePrecision,
            final double offsetMinimumFilterValue, final double offsetMaximumFilterValue, final double offsetFilterRatio,
            final double minimumOffset) throws IllegalArgumentException
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

    /** {@inheritDoc} */
    @Override
    public PolyLine3d transitionLine(final PolyLine3d endLine, final TransitionFunction transition) throws DrawRuntimeException
    {
        Throw.whenNull(endLine, "endLine may not be null");
        Throw.whenNull(transition, "transition may not be null");
        List<Point3d> pointList = new ArrayList<>();
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
        return new PolyLine3d(true, pointList);
    }

    /** {@inheritDoc} */
    @Override
    public PolyLine3d truncate(final double position) throws DrawRuntimeException
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

    /** {@inheritDoc} */
    @Override
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
        return toString("%f", false);
    }

    /** {@inheritDoc} */
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
            format = String.format(", startPhi=%1$s, startTheta=%1$s", doubleFormat);
            result.append(String.format(Locale.US, format, this.startPhi, this.startTheta));
        }
        result.append("]");
        return result.toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.startPhi);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.startTheta);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
        result = prime * result + Arrays.hashCode(this.z);
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
        PolyLine3d other = (PolyLine3d) obj;
        if (Double.doubleToLongBits(this.startPhi) != Double.doubleToLongBits(other.startPhi))
            return false;
        if (Double.doubleToLongBits(this.startTheta) != Double.doubleToLongBits(other.startTheta))
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
