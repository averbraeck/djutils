package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.InvalidProjectionException;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Closed PolyLine3d. The actual closing point (which is the same as the starting point) is NOT included in the super
 * PolyLine3d. The constructors automatically remove the last point if it is a at the same location as the first point.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Polygon3d extends PolyLine3d
{
    /** */
    private static final long serialVersionUID = 20209999L;

    /**
     * Construct a new Polygon3d.
     * @param x the x coordinates of the points
     * @param y the y coordinates of the points
     * @param z the z coordinates of the points
     * @throws IllegalArgumentException when any two successive points are equal, or when there are too few points
     */
    public Polygon3d(final double[] x, final double[] y, final double[] z)
    {
        this(NO_FILTER, x, y, z);
    }

    /**
     * Construct a new Polygon3d.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param x the x coordinates of the points
     * @param y the y coordinates of the points
     * @param z the z coordinates of the points
     * @throws IllegalArgumentException when any two successive points are equal, or when there are too few points
     */
    public Polygon3d(final double epsilon, final double[] x, final double[] y, final double[] z)
    {
        super(epsilon, fixClosingPoint(Throw.whenNull(x, "x"), Throw.whenNull(y, "y"), Throw.whenNull(z, "z")),
                fixClosingPoint(y, x, z), fixClosingPoint(z, x, y));
    }

    /**
     * Ensure that the last elements in three arrays are is not equal to the first. Remove the last element if necessary.
     * @param a the a array
     * @param b the b array
     * @param c the c array
     * @return the <code>a</code> array (possibly a copy with the last element removed)
     */
    static double[] fixClosingPoint(final double[] a, final double[] b, final double[] c)
    {
        if (a.length > 1 && b.length == a.length && c.length == a.length && a[0] == a[a.length - 1] && b[0] == b[a.length - 1]
                && c[0] == c[c.length - 1])
        {
            return Arrays.copyOf(a, a.length - 1);
        }
        return a;
    }

    /**
     * Construct a new Polygon3d.
     * @param points array of Point3d objects.
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>points</code> is too short, or contains successive duplicate points
     */
    public Polygon3d(final Point3d[] points)
    {
        this(NO_FILTER, points);
    }

    /**
     * Construct a new Polygon3d.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param points array of Point3d objects.
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>points</code> is too short, or contains successive duplicate points
     */
    public Polygon3d(final double epsilon, final Point3d[] points)
    {
        this(epsilon, PolyLine3d.makeArray(Throw.whenNull(points, "points"), p -> p.x), PolyLine3d.makeArray(points, p -> p.y),
                PolyLine3d.makeArray(points, p -> p.z));
    }

    /**
     * Construct a new Polygon3d.
     * @param point1 the first point of the new Polygon3d
     * @param point2 the second point of the new Polygon3d
     * @param otherPoints all remaining points of the new Polygon3d (may be null)
     * @throws NullPointerException when <code>point1</code> or <code>point2</code> is <code>null</code>, or contains a
     *             <code>null</code> value
     * @throws IllegalArgumentException when <code>point1</code> is equal to the last point of <code>otherPoints</code>, or any
     *             two successive points are equal
     */
    public Polygon3d(final Point3d point1, final Point3d point2, final Point3d... otherPoints)
    {
        this(NO_FILTER, point1, point2, otherPoints);
    }

    /**
     * Construct a new Polygon3d.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param point1 the first point of the new Polygon3d
     * @param point2 the second point of the new Polygon3d
     * @param otherPoints all remaining points of the new Polygon3d (may be null)
     * @throws NullPointerException when <code>point1</code> or <code>point2</code> is <code>null</code>, or contains a
     *             <code>null</code> value
     * @throws IllegalArgumentException when <code>point1</code> is equal to the last point of <code>otherPoints</code>, or any
     *             two successive points are equal
     */
    public Polygon3d(final double epsilon, final Point3d point1, final Point3d point2, final Point3d... otherPoints)
    {
        super(epsilon, Throw.whenNull(point1, "point1"), Throw.whenNull(point2, "point2"),
                fixClosingPoint(point1, otherPoints));
    }

    /**
     * Ensure that the last point of otherPoints is not equal to point1. Remove the last point if necessary.
     * @param point1 the first point of a new Polygon3d
     * @param otherPoints the remaining points of a new Polygon3d (may be null)
     * @return <code>otherPoints</code> (possibly a copy thereof with the last entry removed)
     */
    private static Point3d[] fixClosingPoint(final Point3d point1, final Point3d[] otherPoints)
    {
        if (otherPoints == null || otherPoints.length == 0)
        {
            return otherPoints;
        }
        Point3d[] result = otherPoints;
        Point3d lastPoint = result[result.length - 1];
        if (point1.x == lastPoint.x && point1.y == lastPoint.y)
        {
            result = Arrays.copyOf(otherPoints, result.length - 1);
            lastPoint = result[result.length - 1];
        }
        Throw.when(point1.x == lastPoint.x && point1.y == lastPoint.y, IllegalArgumentException.class,
                "Before last point and last point are at same location");
        return result;
    }

    /**
     * Construct a new Polygon3d from a list of Point3d objects.
     * @param points the list of points
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>points</code> is too short, or the last two points are at the same location
     */
    public Polygon3d(final List<Point3d> points)
    {
        this(NO_FILTER, points);
    }

    /**
     * Construct a new Polygon3d from a list of Point3d objects.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param points the list of points
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>points</code> is too short, or the last two points are at the same location
     */
    public Polygon3d(final double epsilon, final List<Point3d> points)
    {
        super(epsilon, fixClosingPoint(true, Throw.whenNull(points, "points")));
    }

    /**
     * Ensure that the last point in the list is different from the first point by possibly removing the last point.
     * @param doNotModifyList if<code>true</code>; the list of points will not be modified (if the last point is to be removed;
     *            the entire list up to the last point is duplicated)
     * @param points the list of points
     * @return the fixed list
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when the (resulting) list is too short, or the before last and last point of points have
     *             the same coordinates
     */
    private static List<Point3d> fixClosingPoint(final boolean doNotModifyList, final List<Point3d> points)
    {
        Throw.when(points.size() < 2, IllegalArgumentException.class, "Need at least two points");
        Point3d firstPoint = points.get(0);
        Point3d lastPoint = points.get(points.size() - 1);
        List<Point3d> result = points;
        if (firstPoint.x == lastPoint.x && firstPoint.y == lastPoint.y && firstPoint.z == lastPoint.z)
        {
            if (doNotModifyList)
            {
                result = new ArrayList<>(points.size() - 1);
                for (int i = 0; i < points.size() - 1; i++)
                {
                    result.add(points.get(i));
                }
            }
            else
            {
                result.remove(points.size() - 1);
            }
            lastPoint = result.get(result.size() - 1);
        }
        Throw.when(firstPoint.x == lastPoint.x && firstPoint.y == lastPoint.y && firstPoint.z == lastPoint.z,
                IllegalArgumentException.class, "Before last point and last point are at same location");
        return result;
    }

    /**
     * Construct a new Polygon3d from an iterator that yields Point3d.
     * @param iterator the iterator
     */
    public Polygon3d(final Iterator<Point3d> iterator)
    {
        this(NO_FILTER, iterator);
    }

    /**
     * Construct a new Polygon3d from an iterator that yields Point3d.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param iterator the iterator
     */
    public Polygon3d(final double epsilon, final Iterator<Point3d> iterator)
    {
        this(epsilon, fixClosingPoint(false, iteratorToList(Throw.whenNull(iterator, "iterator"))));
    }

    /**
     * Construct a new Polygon3d from an existing one. This constructor is primarily intended for use in extending classes.
     * @param polygon the existing Polygon3d
     * @throws NullPointerException when <code>polygon</code> is <code>null</code>
     */
    public Polygon3d(final Polygon3d polygon)
    {
        super(polygon);
    }

    @Override
    public double getLength()
    {
        // Length a polygon is computed by taking the length of the PolyLine and adding the length of the closing segment
        return super.getLength()
                + Math.hypot(Math.hypot(getX(size() - 1) - getX(0), getY(size() - 1) - getY(0)), getZ(size() - 1) - getZ(0));
    }

    @Override
    public LineSegment3d getSegment(final int index)
    {
        if (index < size() - 1)
        {
            return super.getSegment(index);
        }
        Throw.when(index != size() - 1, IndexOutOfBoundsException.class, "index must be in range [0, .size() - 1] (got %d)",
                index);
        return new LineSegment3d(getX(index), getY(index), getZ(index), getX(0), getY(0), getZ(0));
    }

    @Override
    public Polygon2d project() throws InvalidProjectionException
    {
        double[] projectedX = new double[this.size()];
        double[] projectedY = new double[this.size()];

        int nextIndex = 0;
        for (int i = 0; i < this.size(); i++)
        {
            if (i > 0 && getX(i) == getX(i - 1) && getY(i) == getY(i - 1))
            {
                continue;
            }
            projectedX[nextIndex] = getX(i);
            projectedY[nextIndex] = getY(i);
            nextIndex++;
        }
        Throw.when(nextIndex < 2, InvalidProjectionException.class,
                "projection yielded too few points to construct a valid Polygon2d");
        if (nextIndex < projectedX.length)
        {
            return new Polygon2d(Arrays.copyOf(projectedX, nextIndex), Arrays.copyOf(projectedY, nextIndex));
        }
        return new Polygon2d(projectedX, projectedY);
    }

    @Override
    public Polygon3d reverse()
    {
        return new Polygon3d(super.reverse().iterator());
    }

    @Override
    public final String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        StringBuilder result = new StringBuilder();
        if (!doNotIncludeClassName)
        {
            result.append("Polygon3d ");
        }
        result.append("[super=");
        result.append(super.toString(doubleFormat, false));
        result.append("]");
        return result.toString();
    }

}
