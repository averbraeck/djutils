package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Polygon3d.java. Closed PolyLine3d. The actual closing point (which is the same as the starting point) is NOT included in the
 * super PolyLine3d. The constructors automatically remove the last point if it is a at the same location as the first point.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Polygon3d extends PolyLine3d
{
    /** */
    private static final long serialVersionUID = 20209999L;

    /**
     * Construct a new Polygon3d.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @param z double[]; the z coordinates of the points
     * @throws DrawRuntimeException when any two successive points are equal, or when there are too few points
     */
    public Polygon3d(final double[] x, final double[] y, final double[] z) throws DrawRuntimeException
    {
        super(fixClosingPointX(Throw.whenNull(x, "x may not be null"), Throw.whenNull(y, "y may not be null"),
                Throw.whenNull(z, "z may not be null")), fixClosingPointY(x, y, z), fixClosingPointZ(x, y, z));
    }

    /**
     * Ensure that the last point is not equal to the first. Remove the last point if necessary.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @param z double[]; the z coordinates of the points
     * @return double[]; the y coordinates of the points (possibly a copy with the last element removed)
     */
    static double[] fixClosingPointX(final double[] x, final double[] y, final double[] z)
    {
        if (x.length > 1 && y.length == x.length && z.length == x.length && x[0] == x[x.length - 1] && y[0] == y[x.length - 1]
                && z[0] == z[z.length - 1])
        {
            return Arrays.copyOf(x, x.length - 1);
        }
        return x;
    }

    /**
     * Ensure that the last point is not equal to the first. Remove the last point if necessary.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @param z double[]; the z coordinates of the points
     * @return double[]; the y coordinates of the points (possibly a copy with the last element removed)
     */
    static double[] fixClosingPointY(final double[] x, final double[] y, final double[] z)
    {
        if (x.length > 1 && y.length == x.length && z.length == x.length && x[0] == x[x.length - 1] && y[0] == y[x.length - 1]
                && z[0] == z[z.length - 1])
        {
            return Arrays.copyOf(y, x.length - 1);
        }
        return y;
    }

    /**
     * Ensure that the last point is not equal to the first. Remove the last point if necessary.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @param z double[]; the z coordinates of the points
     * @return double[]; the y coordinates of the points (possibly a copy with the last element removed)
     */
    static double[] fixClosingPointZ(final double[] x, final double[] y, final double[] z)
    {
        if (x.length > 1 && y.length == x.length && z.length == x.length && x[0] == x[x.length - 1] && y[0] == y[x.length - 1]
                && z[0] == z[z.length - 1])
        {
            return Arrays.copyOf(z, x.length - 1);
        }
        return z;
    }

    /**
     * Construct a new Polygon3d.
     * @param points Point3d[]; array of Point3d objects.
     * @throws NullPointerException when points is null
     * @throws DrawRuntimeException when points is too short, or contains successive duplicate points
     */
    public Polygon3d(final Point3d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(PolyLine3d.makeArray(Throw.whenNull(points, "points may not be null"), p -> p.x),
                PolyLine3d.makeArray(points, p -> p.y), PolyLine3d.makeArray(points, p -> p.z));
    }

    /**
     * Construct a new Polygon3d.
     * @param point1 Point3d; the first point of the new Polygon3d
     * @param point2 Point3d; the second point of the new Polygon3d
     * @param otherPoints Point3d[]; all remaining points of the new Polygon3d (may be null)
     * @throws NullPointerException when point1 or point2 is null
     * @throws DrawRuntimeException when point1 is equal to the last point of otherPoints, or any two successive points are
     *             equal
     */
    public Polygon3d(final Point3d point1, final Point3d point2, final Point3d... otherPoints)
            throws NullPointerException, DrawRuntimeException
    {
        super(Throw.whenNull(point1, "point1 may not be null"), Throw.whenNull(point2, "point2 may not be null"),
                fixClosingPoint(point1, otherPoints));
    }

    /**
     * Ensure that the last point of otherPoints is not equal to point1. Remove the last point if necessary.
     * @param point1 Point3d; the first point of a new Polygon3d
     * @param otherPoints Point3d[]; the remaining points of a new Polygon3d (may be null)
     * @return Point3d[]; otherPoints (possibly a copy thereof with the last entry removed)
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
        Throw.when(point1.x == lastPoint.x && point1.y == lastPoint.y, DrawRuntimeException.class,
                "Before last point and last point are at same location");
        return result;
    }

    /**
     * Construct a new Polygon3d from a list of Point3d objects.
     * @param points List&lt;Point3d&gt;; the list of points
     * @throws NullPointerException when points is null
     * @throws DrawRuntimeException when points is too short, or the last two points are at the same location
     */
    public Polygon3d(final List<Point3d> points) throws NullPointerException, DrawRuntimeException
    {
        super(fixClosingPoint(true, Throw.whenNull(points, "points may not be null")));

    }

    /**
     * Ensure that the last point in the list is different from the first point by possibly removing the last point.
     * @param doNotModifyList boolean; if true; the list of points will not be modified (if the last point is to be removed; the
     *            entire list up to the last point is duplicated)
     * @param points List&lt;Point3d&gt;; the list of points
     * @return List&lt;Point3d&gt;; the fixed list
     * @throws DrawRuntimeException when the (resulting) list is too short, or the before last and last point of points have the
     *             same coordinates
     */
    private static List<Point3d> fixClosingPoint(final boolean doNotModifyList, final List<Point3d> points)
            throws DrawRuntimeException
    {
        Throw.when(points.size() < 2, DrawRuntimeException.class, "Need at least two points");
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
                DrawRuntimeException.class, "Before last point and last point are at same location");
        return result;
    }

    /**
     * Construct a new Polygon3d from an iterator that yields Point3d.
     * @param iterator Iterator&lt;Point3d&gt;; the iterator
     */
    public Polygon3d(final Iterator<Point3d> iterator)
    {
        this(fixClosingPoint(false, iteratorToList(Throw.whenNull(iterator, "iterator cannot be null"))));
    }

    /**
     * Create a new Polygon3d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param points Point3d...; the coordinates of the polygon as Point3d
     * @throws DrawRuntimeException when number of points &lt; 2
     */
    public Polygon3d(final boolean filterDuplicates, final Point3d... points) throws DrawRuntimeException
    {
        this(PolyLine3d.cleanPoints(filterDuplicates, Arrays.stream(points).iterator()));
    }

    /**
     * Create a new Polygon3d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param pointList List&lt;Point3d&gt;; list of the coordinates of the line as Point3d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @throws DrawRuntimeException when number of non-equal points &lt; 2
     */
    public Polygon3d(final boolean filterDuplicates, final List<Point3d> pointList) throws DrawRuntimeException
    {
        this(PolyLine3d.cleanPoints(filterDuplicates, pointList.iterator()));
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        // Length a polygon is computed by taking the length of the PolyLine and adding the length of the closing segment
        return super.getLength()
                + Math.hypot(Math.hypot(getX(size() - 1) - getX(0), getY(size() - 1) - getY(0)), getZ(size() - 1) - getZ(0));
    }

    /** {@inheritDoc} */
    @Override
    public LineSegment3d getSegment(final int index)
    {
        if (index < size() - 1)
        {
            return super.getSegment(index);
        }
        Throw.when(index != size() - 1, DrawRuntimeException.class, "index must be in range 0..size() - 1");
        return new LineSegment3d(getX(index), getY(index), getZ(index), getX(0), getY(0), getZ(0));
    }

    /** {@inheritDoc} */
    @Override
    public Polygon2d project() throws DrawRuntimeException
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
        if (nextIndex < projectedX.length)
        {
            return new Polygon2d(Arrays.copyOf(projectedX, nextIndex), Arrays.copyOf(projectedY, nextIndex));
        }
        return new Polygon2d(projectedX, projectedY);
    }

    /** {@inheritDoc} */
    @Override
    public Polygon3d reverse()
    {
        return new Polygon3d(super.reverse().getPoints());
    }

    /** {@inheritDoc} */
    @Override
    public final String toExcel()
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < size(); i++)
        {
            s.append(getX(i) + "\t" + getY(i) + "\t" + getZ(i) + "\n");
        }
        s.append(getX(0) + "\t" + getY(0) + "\t" + getZ(0) + "\n");
        return s.toString();
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
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
            result.append("Polygon3d ");
        }
        result.append("[super=");
        result.append(super.toString(doubleFormat, false));
        result.append("]");
        return result.toString();
    }

}
