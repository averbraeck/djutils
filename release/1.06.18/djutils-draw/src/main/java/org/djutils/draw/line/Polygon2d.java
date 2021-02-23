package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Polygon2d.java. Closed PolyLine2d. The actual closing point (which is the same as the starting point) is NOT included in the
 * super PolyLine2d. The constructors automatically remove the last point if it is a at the same location as the first point.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Polygon2d extends PolyLine2d
{
    /** */
    private static final long serialVersionUID = 20209999L;

    /**
     * Construct a new Polygon2d.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @throws DrawRuntimeException when any two successive points are equal, or when there are too few points
     */
    public Polygon2d(final double[] x, final double[] y) throws DrawRuntimeException
    {
        super(fixClosingPointX(Throw.whenNull(x, "x may not be null"), Throw.whenNull(y, "y may not be null")),
                fixClosingPointY(x, y));
    }

    /**
     * Ensure that the last point is not equal to the first. Remove the last point of necessary.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @return double[]; the x coordinates of the points (possibly a copy with the last element removed)
     */
    private static double[] fixClosingPointX(final double[] x, final double[] y)
    {
        if (x.length > 1 && y.length == x.length && x[0] == x[x.length - 1] && y[0] == y[x.length - 1])
        {
            return Arrays.copyOf(x, x.length - 1);
        }
        return x;
    }

    /**
     * Ensure that the last point is not equal to the first. Remove the last point of necessary.
     * @param x double[]; the x coordinates of the points
     * @param y double[]; the y coordinates of the points
     * @return double[]; the y coordinates of the points (possibly a copy with the last element removed)
     */
    private static double[] fixClosingPointY(final double[] x, final double[] y)
    {
        if (x.length > 1 && y.length == x.length && x[0] == x[x.length - 1] && y[0] == y[x.length - 1])
        {
            return Arrays.copyOf(y, x.length - 1);
        }
        return y;
    }

    /**
     * Construct a new Polygon2d.
     * @param points Point2d[]; array of Point2d objects.
     * @throws NullPointerException when points is null
     * @throws DrawRuntimeException when points is too short, or contains successive duplicate points
     */
    public Polygon2d(final Point2d[] points) throws NullPointerException, DrawRuntimeException
    {
        this(PolyLine2d.makeX(points), PolyLine2d.makeY(points));
    }

    /**
     * Construct a new Polygon2d.
     * @param point1 Point2d; the first point of the new Polygon2d
     * @param point2 Point2d; the second point of the new Polygon2d
     * @param otherPoints Point2d[]; all remaining points of the new Polygon2d (may be null)
     * @throws NullPointerException when point1 or point2 is null
     * @throws DrawRuntimeException when point1 is equal to the last point of otherPoints, or any two successive points are
     *             equal
     */
    public Polygon2d(final Point2d point1, final Point2d point2, final Point2d... otherPoints)
            throws NullPointerException, DrawRuntimeException
    {
        super(Throw.whenNull(point1, "point1 may not be null"), Throw.whenNull(point2, "point2 may not be null"),
                fixClosingPoint(point1, otherPoints));
    }

    /**
     * Ensure that the last point of otherPoints is not equal to point1. Remove the last point if necessary.
     * @param point1 Point2d; the first point of a new Polygon2d
     * @param otherPoints Point2d[]; the remaining points of a new Polygon2d (may be null)
     * @return Point2d[]; otherPoints (possibly a copy thereof with the last entry removed)
     */
    private static Point2d[] fixClosingPoint(final Point2d point1, final Point2d[] otherPoints)
    {
        if (otherPoints == null || otherPoints.length == 0)
        {
            return otherPoints;
        }
        Point2d[] result = otherPoints;
        Point2d lastPoint = result[result.length - 1];
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
     * Construct a new Polygon2d from a list of Point2d objects.
     * @param points List&lt;Point2d&gt;; the list of points
     * @throws NullPointerException when points is null
     * @throws DrawRuntimeException when points is too short, or the last two points are at the same location
     */
    public Polygon2d(final List<Point2d> points) throws NullPointerException, DrawRuntimeException
    {
        super(fixClosingPoint(true, Throw.whenNull(points, "points may not be null")));

    }

    /**
     * Ensure that the last point in the list is different from the first point by possibly removing the last point.
     * @param doNotModifyList boolean; if true; the list of points will not be modified (if the last point is to be removed; the
     *            entire list up to the last point is duplicated)
     * @param points List&lt;Point2d&gt;; the list of points
     * @return List&lt;Point2d&gt;; the fixed list
     * @throws DrawRuntimeException when the (resulting) list is too short, or the before last and last point of points have the
     *             same coordinates
     */
    private static List<Point2d> fixClosingPoint(final boolean doNotModifyList, final List<Point2d> points)
            throws DrawRuntimeException
    {
        Throw.when(points.size() < 2, DrawRuntimeException.class, "Need at least two points");
        Point2d firstPoint = points.get(0);
        Point2d lastPoint = points.get(points.size() - 1);
        List<Point2d> result = points;
        if (firstPoint.x == lastPoint.x && firstPoint.y == lastPoint.y)
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
        Throw.when(firstPoint.x == lastPoint.x && firstPoint.y == lastPoint.y, DrawRuntimeException.class,
                "Before last point and last point are at same location");
        return result;
    }

    /**
     * Construct a new Polygon2d from an iterator that yields Point2d.
     * @param iterator Iterator&lt;Point2d&gt;; the iterator
     */
    public Polygon2d(final Iterator<Point2d> iterator)
    {
        this(fixClosingPoint(false, iteratorToList(Throw.whenNull(iterator, "iterator cannot be null"))));
    }

    /**
     * Determine if this Polygon2d is convex. Returns bogus result for self-intersecting polygons. Derived from
     * http://paulbourke.net/geometry/polygonmesh/source2.c
     * @return boolean; true if this Polygon2d is convex; false if this Polygon2d is concave
     */
    public final boolean isConvex()
    {
        int flag = 0;
        for (int i = 0; i < size(); i++)
        {
            int j = (i + 1) % size();
            int k = (j + 1) % size();
            double z = (getX(j) - getX(i)) * (getY(k) - getY(j)) - (getY(j) - getY(i)) * (getX(k) - getX(j));
            if (z < 0)
            {
                flag |= 1;
            }
            else if (z > 0)
            {
                flag |= 2;
            }
            if (flag == 3)
            {
                return false;
            }
        }
        return flag != 0;
    }

    /**
     * Determine if a point is inside this Polygon. Returns bogus results for self-intersecting polygons.
     * @param point Point2d; the point
     * @return boolean; true if the point is inside this polygon, false if the point is outside this polygon. Results are
     *         ill-defined for points on the edges of this Polygon.
     */
    public boolean contains(final Point2d point)
    {
        return contains(point.x, point.y);
    }

    /**
     * Determine if a point is inside this Polygon. Returns bogus results for self-intersecting polygons. Derived from
     * http://paulbourke.net/geometry/polygonmesh/
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @return boolean; true if the point is inside this polygon, false if the point is outside this polygon. Results are
     *         ill-defined for points on the edges of this Polygon.
     */
    public boolean contains(final double x, final double y)
    {
        if (!getBounds().contains(x, y))
        {
            return false;
        }
        int counter = 0;
        // Unlike Paul Bourke, we initialize prevPoint to the last point of the polygon (so we never have to wrap around)
        double prevPointX = getX(size() - 1);
        double prevPointY = getY(size() - 1);
        for (int i = 0; i < size(); i++)
        {
            double curPointX = getX(i);
            double curPointY = getY(i);
            // Combined 4 if statements into one; I trust that the java compiler will short-circuit this nicely
            if (y > Math.min(prevPointY, curPointY) && y <= Math.max(prevPointY, curPointY)
                    && x <= Math.max(prevPointX, curPointX) && prevPointY != curPointY)
            {
                double xIntersection = (y - prevPointY) * (curPointX - prevPointX) / (curPointY - prevPointY) + prevPointX;
                if (prevPointX == curPointX || x <= xIntersection)
                {
                    counter++;
                }
            }
            prevPointX = curPointX;
            prevPointY = curPointY;
        }
        return counter % 2 != 0;
    }

    /**
     * Compute the surface of this Polygon2d. Sign of the result reflects the winding-ness of this this Polygon2d. If this
     * Polygon2d self-intersects, the result is bogus.
     * @return double; the surface of this Polygon2d
     */
    public double surface()
    {
        // TODO scale all coordinates back to something local to reduce rounding errors
        double result = 0;
        // We initialize previous point to the last point of this Polygon2d to avoid wrapping problems
        double prevX = getX(size() - 1);
        double prevY = getY(size() - 1);
        for (int i = 0; i < size(); i++)
        {
            double thisX = getX(i);
            double thisY = getY(i);
            result += prevX * thisY - thisX * prevY;
            prevX = thisX;
            prevY = thisY;
        }
        return result / 2;
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        // Length a polygon is computed by taking the length of the PolyLine and adding the length of the closing segment
        return super.getLength() + Math.hypot(getX(size() - 1) - getX(0), getY(size() - 1) - getY(0));
    }

    /** {@inheritDoc} */
    @Override
    public LineSegment2d getSegment(final int index)
    {
        if (index < size() - 1)
        {
            return super.getSegment(index);
        }
        Throw.when(index != size() - 1, DrawRuntimeException.class, "index must be in range 0..size() - 1");
        return new LineSegment2d(getX(index), getY(index), getX(0), getY(0));
    }

    /** {@inheritDoc} */
    @Override
    public Polygon2d reverse()
    {
        return new Polygon2d(super.reverse().getPoints());
    }

    /** {@inheritDoc} */
    @Override
    public final String toExcel()
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < size(); i++)
        {
            s.append(getX(i) + "\t" + getY(i) + "\n");
        }
        s.append(getX(0) + "\t" + getY(0) + "\n");
        return s.toString();
    }

    /** {@inheritDoc} */
    @Override
    public final String toPlot()
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < size(); i++)
        {
            result.append(String.format(Locale.US, "%s%.3f,%.3f", 0 == result.length() ? "M" : " L", getX(i), getY(i)));
        }
        result.append(String.format(Locale.US, " L%.3f,%.3f", getX(0), getY(0)));
        result.append("\n");
        return result.toString();
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
            result.append("Polygon2d ");
        }
        result.append("[super=");
        result.append(super.toString(doubleFormat, false));
        result.append("]");
        return result.toString();
    }

}
