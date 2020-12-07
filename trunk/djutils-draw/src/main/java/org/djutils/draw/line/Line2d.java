package org.djutils.draw.line;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Line2d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Line2d implements Drawable2d, Line<Point2d>
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
     * @param points Point3d...; the array of points to construct this Line3d from.
     * @throws NullPointerException when iterator is null
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    private Line2d(final boolean copyNeeded, final Point2d[] points) throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(points, "points cannot be null");
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
     * Construct a new Line2D from an array of Point2d.
     * @param points Point3d...; the array of points to construct this Line3d from.
     * @throws NullPointerException when iterator is null
     * @throws DrawException when the provided points do not constitute a valid line (too few points or identical adjacent
     *             points)
     */
    public Line2d(final Point2d... points) throws NullPointerException, DrawException
    {
        this(true, points);
    }

    /**
     * Construct a new Line3d and initialize its length indexed line, bounds, centroid and length.
     * @param iterator Iterator&lt;Point3d&gt;; iterator that will provide all points that constitute the new Line3d
     * @throws NullPointerException when iterator is null
     * @throws DrawException when the iterator provides too few points, or some adjacent identical points)
     */
    public Line2d(final Iterator<Point2d> iterator) throws NullPointerException, DrawException
    {
        this(iteratorToList(Throw.whenNull(iterator, "iterator cannot be null")));
    }

    /**
     * Construct a new Line3d from a List&lt;Point3d&gt;.
     * @param pointList List&lt;Point3d&gt;; the list of points to construct this Line3d from.
     * @throws DrawRuntimeException when the provided points do not constitute a valid line (too few points or identical
     *             adjacent points)
     */
    public Line2d(final List<Point2d> pointList) throws DrawRuntimeException
    {
        this(false, pointList.toArray(new Point2d[pointList.size()]));
    }

    /**
     * Construct a new Line2d (closed shape) from a Path2D.
     * @param path Path2D; the Path2D to construct this Line3d from.
     * @throws DrawException when the provided points do not constitute a valid line (too few points or identical adjacent
     *             points)
     */
    public Line2d(final Path2D path) throws DrawException
    {
        this(false, path2DtoArray(path));
    }

    /**
     * Convert a path2D to a Point3d[] array to construct the line.
     * @param path Path2D; the path to convert
     * @return Point3d[]; an array of points based on MOVETO and LINETO elements of the Path2D
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
                    result.add(new Point2d(result.get(0).getX(), result.get(0).getY()));
                }
                break;
            }
        }
        return result.toArray(new Point2d[result.size() - 1]);
    }

    /**
     * Build a list from the Point3d objects that an iterator provides.
     * @param iterator Iterator&lt;Point3d&gt;; the iterator that will provide the points
     * @return List&lt;Point3d&gt;; a list of the points provided by the iterator
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
        if (i < 0 || i > size() - 1)
        {
            throw new IndexOutOfBoundsException("Line2d.get(i=" + i + "); i<0 or i>=size(), which is " + size());
        }
        return this.points[i];
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

    /** {@inheritDoc} */
    @Override
    public final Point2d getLocation()
    {
        return this.bounds.getLocation();
    }

    /**
     * Construct a new Line3d with all points of this Line3d in reverse order.
     * @return Line3d; the new Line3d
     */
    public final Line2d reverse()
    {
        Point2d[] resultPoints = new Point2d[size()];
        int nextIndex = size();
        for (int index = 0; index < this.size(); index++)
        {
            resultPoints[--nextIndex] = this.points[index];
        }
        try
        {
            return new Line2d(resultPoints);
        }
        catch (DrawException exception)
        {
            // Cannot happen
            throw new RuntimeException(exception);
        }
    }

}
