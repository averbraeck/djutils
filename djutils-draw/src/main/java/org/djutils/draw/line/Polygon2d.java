package org.djutils.draw.line;

import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Polygon2d.java. Closed PolyLine2d. The actual closing point (which is the same as the starting point) is NOT included in the
 * super PolyLine2d. The constructors automatically remove the last point if it is a at the same location as the first point.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        super(fixClosingPointX(Throw.whenNull(x, "x"), Throw.whenNull(y, "y")), fixClosingPointY(x, y));
    }

    /**
     * Ensure that the last point is not equal to the first. Remove the last point if necessary.
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
     * Ensure that the last point is not equal to the first. Remove the last point if necessary.
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
        this(PolyLine2d.makeArray(points, p -> p.x), PolyLine2d.makeArray(points, p -> p.y));
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
        super(Throw.whenNull(point1, "point1"), Throw.whenNull(point2, "point2"), fixClosingPoint(point1, otherPoints));
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
        super(fixClosingPoint(true, Throw.whenNull(points, "points")));

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
        this(fixClosingPoint(false, iteratorToList(Throw.whenNull(iterator, "iterator"))));
    }

    /**
     * Create a new Polygon2d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param points Point2d...; the coordinates of the polygon as Point2d
     * @throws DrawRuntimeException when number of points &lt; 2
     */
    public Polygon2d(final boolean filterDuplicates, final Point2d... points) throws DrawRuntimeException
    {
        this(PolyLine2d.cleanPoints(filterDuplicates, Arrays.stream(points).iterator()));
    }

    /**
     * Create a new Polygon2d, optionally filtering out repeating successive points.
     * @param filterDuplicates boolean; if true; filter out successive repeated points; otherwise do not filter
     * @param pointList List&lt;Point2d&gt;; list of the coordinates of the line as Point3d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @throws DrawRuntimeException when number of non-equal points &lt; 2
     */
    public Polygon2d(final boolean filterDuplicates, final List<Point2d> pointList) throws DrawRuntimeException
    {
        this(PolyLine2d.cleanPoints(filterDuplicates, pointList.iterator()));
    }

    /**
     * Construct a new Polygon2d from an existing one. This constructor is primarily intended for use in extending classes.
     * @param polygon Polygon2d; the existing Polygon2d
     */
    public Polygon2d(final Polygon2d polygon)
    {
        super(polygon);
    }

    /**
     * Determine if this Polygon is convex. Returns bogus result for self-intersecting polygons. Derived from
     * <a href="http://paulbourke.net/geometry/polygonmesh/source2.c">Convex by Paul Bourke</a>
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
     * <a href="http://paulbourke.net/geometry/polygonmesh/">Polygons and meshes by Paul Bourke</a>
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
     * Determine if this Polygon completely contains a Bounds2d object. If this Polygon self-intersects, the results is bogus.
     * @param bounds Bounds2d; the Bounds2d object
     * @return boolean; true if the Bounds2d object is completely contained in this Polygon; false if any part (or all) of the
     *         Bounds2d object is outside this Polygon. If the Bounds2d object touches this Polygon the results are ill-defined.
     */
    public boolean contains(final Bounds2d bounds)
    {
        // Step 1: quick check to see if the bounds intersect
        if (getBounds().disjoint(bounds))
        {
            return false;
        }
        // This is a quick hack
        return toPath2D().contains(bounds.getMinX(), bounds.getMinY(), bounds.getDeltaX(), bounds.getDeltaY());
    }

    /**
     * Determine if this Polygon intersects another Polygon.
     * @param other Polygon2d; the other Polygon
     * @return boolean; true if the polygons intersect; false if the polygons are disjunct. Ill-defined if the polygons touch.
     */
    public boolean intersects(final Polygon2d other)
    {
        // step 1: quick check to see if the bounds intersect
        if (!getBounds().intersects(other.getBounds()))
        {
            return false;
        }

        // step 2: quick check to see if any of the points of this polygon lies inside the other polygon
        for (Iterator<Point2d> iterator = getPoints(); iterator.hasNext();)
        {
            if (other.contains(iterator.next()))
            {
                return true;
            }
        }

        // step 3: quick check to see if any of the points of the other polygon lies inside this polygon
        for (Iterator<Point2d> iterator = other.getPoints(); iterator.hasNext();)
        {
            if (contains(iterator.next()))
            {
                return true;
            }
        }

        // step 4: see if any of the lines of shape 1 and shape 2 intersect (expensive!)
        for (int i = 0; i < this.size(); i++)
        {
            LineSegment2d ourSegment = getSegment(i);
            for (int j = 0; j < other.size(); j++)
            {
                LineSegment2d otherSegment = other.getSegment(j);
                Point2d intersection = Point2d.intersectionOfLineSegments(ourSegment, otherSegment);
                if (intersection != null)
                {
                    double p1x = ourSegment.startX, p1y = ourSegment.startY;
                    double d1x = ourSegment.endX - p1x, d1y = ourSegment.endY - p1y;
                    double p2x = otherSegment.startX, p2y = otherSegment.startY;
                    double d2x = otherSegment.endX - p2x, d2y = otherSegment.endY - p2y;

                    double det = d2x * d1y - d2y * d1x;
                    if (det != 0)
                    {
                        double z = (d2x * (p2y - p1y) + d2y * (p1x - p2x)) / det;
                        if (Math.abs(z) < 10.0 * Math.ulp(1.0) || Math.abs(z - 1.0) > 10.0 * Math.ulp(1.0))
                        {
                            return true; // intersection at end point
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Compute the surface of this Polygon2d. Sign of the result reflects the winding-ness of this this Polygon2d. If this
     * Polygon2d self-intersects, the result is bogus.
     * @return double; the surface of this Polygon2d
     */
    public double surface()
    {
        // Translate all coordinates to the median of the bounding box to minimize rounding errors
        Point2d midPoint = getBounds().midPoint();
        double result = 0;
        // We initialize previous point to the last point of this Polygon2d to avoid wrapping problems
        double prevX = getX(size() - 1) - midPoint.x;
        double prevY = getY(size() - 1) - midPoint.y;
        for (int i = 0; i < size(); i++)
        {
            double thisX = getX(i) - midPoint.x;
            double thisY = getY(i) - midPoint.y;
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
        // Length of a polygon is computed by taking the length of the PolyLine and adding the length of the closing segment
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

    /**
     * Construct a Path2D from this PolyLine2d. The result is NOT cached (in the current implementation).
     * @return Path2D; newly construct Path2D consisting solely of straight segments.
     */
    @Override
    public Path2D toPath2D()
    {
        Path2D.Double result = (Double) super.toPath2D();
        result.closePath();
        return result;
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
