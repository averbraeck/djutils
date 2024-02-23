package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * ConvexHull.java. Compute the convex hull of a collection of Point2d or Drawable2d. All implementations here return a
 * Polygon2d object. If the convex hull of the input would be a single point, the implementations will throw a
 * DrawRuntimeException because a single point does not make a valid Polygon2d.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class ConvexHull
{
    /**
     * Do not instantiate.
     */
    private ConvexHull()
    {
        // Do not instantiate
    }

    /**
     * Compute the convex hull of a collection of Point2d objects.
     * @param iterator Iterator&lt;Point2d&gt;; iterator that shall return all the points for which the convex hull is to be
     *            computed
     * @return Polygon2d; the convex hull of the points
     */
    public static Polygon2d convexHull(final Iterator<Point2d> iterator)
    {
        List<Point2d> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return convexHullAlshamrani(list);
    }

    /**
     * Compute the convex hull of one or more Drawable2d objects.
     * @param drawable2d Drawable2d...; the Drawable2d objects
     * @return Polygon2d; the convex hull of the Drawable2d objects
     * @throws NullPointerException when any of the drawable2d object is null
     * @throws IllegalArgumentException when zero arguments are provided
     */
    public static Polygon2d convexHull(final Drawable2d... drawable2d) throws NullPointerException, IllegalArgumentException
    {
        return convexHull(Bounds2d.pointsOf(drawable2d));
    }

    /**
     * Construct a Bounds2d for a Collection of Drawable2d objects.
     * @param drawableCollection Collection&lt;Drawable2d&gt;; the collection
     * @return Polygon2d; the convex hull of the Drawable2d objects
     * @throws NullPointerException when the collection is null, or contains null values
     * @throws IllegalArgumentException when the collection is empty
     */
    public static Polygon2d convexHull(final Collection<Drawable2d> drawableCollection)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(drawableCollection, "drawableCollection may not be null");
        Throw.when(drawableCollection.isEmpty(), DrawRuntimeException.class, "drawableCollection may not be empty");
        return convexHull(Bounds2d.pointsOf(drawableCollection));
    }

    /**
     * Compute the convex hull of a list of Point2d objects. The input list will not be modified.
     * @param list List&lt;Point2d&gt;; the list of Point2d objects
     * @return Polygon2d; the convex hull of the points
     */
    public static Polygon2d convexHull(final List<Point2d> list)
    {
        return convexHullAlshamrani(list);
    }

    /**
     * Return whether moving from a through b to c, the turn at b is counter-clockwise.
     * @param a Point2d; point a
     * @param b Point2d; point b
     * @param c Point2d; point c
     * @return boolean; true if the turn at b is counter clockwise; false if there is not turn; or it is clockwise
     */
    private static boolean ccw(final Point2d a, final Point2d b, final Point2d c)
    {
        // System.out.println("left " + ((b.x - a.x) * (c.y - a.y)) + ", right " + ((b.y - a.y) * (c.x - a.x)));
        return ((b.x - a.x) * (c.y - a.y)) > ((b.y - a.y) * (c.x - a.x));
    }

    /**
     * Repeatedly remove the last point if not counter clockwise with new point; then add the new point. If the new point is
     * equal to the last point in the list; do nothing.
     * @param list List&lt;Point2d&gt;; the list of points
     * @param newPoint Point2d; the point that will be added.
     */
    private static void cleanAndAppend(final List<Point2d> list, final Point2d newPoint)
    {
        Point2d last = list.get(list.size() - 1);
        if (last.x == newPoint.x && last.y == newPoint.y)
        {
            return;
        }
        while (list.size() >= 2 && !ccw(list.get(list.size() - 2), list.get(list.size() - 1), newPoint))
        {
            list.remove(list.size() - 1);
        }
        list.add(newPoint);
    }

    /**
     * Implementation of the convex hull algorithm by Reham Alshamrani c.s.; see
     * <a href="https://www.sciencedirect.com/science/article/pii/S1877050920304750">A Preprocessing Technique for Fast Convex
     * Hull Computation</a>.
     * @param list List&lt;Point2d&gt;; list of the points (will not be modified)
     * @return Polygon2d; the convex hull of the points
     * @throws NullPointerException when the list is null
     * @throws DrawRuntimeException when the list contains too few points
     */
    public static Polygon2d convexHullAlshamrani(final List<Point2d> list) throws NullPointerException, DrawRuntimeException
    {
        // Find the four extreme points
        Throw.whenNull(list, "list may not be null");
        Throw.when(list.size() < 1, DrawRuntimeException.class, "Too few points in list");
        Point2d minX = list.get(0); // Initialize to the first point in list to avoid checking for null on each iteration
        Point2d minY = list.get(0);
        Point2d maxX = list.get(0);
        Point2d maxY = list.get(0);
        for (Point2d point : list)
        {
            if (minX.x > point.x || minX.x == point.x && minX.y > point.y)
            {
                minX = point;
            }
            if (minY.y > point.y || minY.y == point.y && minY.x < point.x)
            {
                minY = point;
            }
            if (maxX.x < point.x || maxX.x == point.x && maxX.y > point.y)
            {
                maxX = point;
            }
            if (maxY.y < point.y || maxY.y == point.y && maxY.x < point.x)
            {
                maxY = point;
            }
        }
        // Filter and group the points into priority queues that order by x value (tie breaker is y value)
        // Alshamrani does not show how he tests that a point is outside each edge of the four extreme points. We use ccw.
        // Alshamrani poorly documents the ordering method for the four queues when the primary component values are the same.
        // Testing has shown that sorting a filled ArrayList is faster than putting the same elements one by one in a TreeSet.
        Comparator<Point2d> forwardComparator = new Comparator<Point2d>()
        {
            @Override
            public int compare(final Point2d point1, final Point2d point2)
            {
                if (point1.x == point2.x)
                {
                    return (int) Math.signum(point1.y - point2.y);
                }
                return (int) Math.signum(point1.x - point2.x);
            }
        };
        Comparator<Point2d> reverseComparator = new Comparator<Point2d>()
        {
            @Override
            public int compare(final Point2d point1, final Point2d point2)
            {
                if (point1.x == point2.x)
                {
                    return (int) Math.signum(point1.y - point2.y);
                }
                return (int) Math.signum(point2.x - point1.x);
            }
        };
        List<Point2d> lowerLeft = new ArrayList<>();
        List<Point2d> lowerRight = new ArrayList<>();
        List<Point2d> upperRight = new ArrayList<>();
        List<Point2d> upperLeft = new ArrayList<>();

        for (Point2d point : list)
        {
            if (point.x <= minY.x && point.y <= minX.y)
            {
                if (ccw(minX, point, minY))
                {
                    lowerLeft.add(point);
                }
            }
            else if (point.x >= minY.x && point.y <= maxX.y)
            {
                if (ccw(minY, point, maxX))
                {
                    lowerRight.add(point);
                }
            }
            else if (point.x >= maxY.x && point.y >= maxX.y)
            {
                if (ccw(maxX, point, maxY))
                {
                    upperRight.add(point);
                }
            }
            else if (point.x <= maxY.x && point.y >= minX.y)
            {
                if (ccw(maxY, point, minX))
                {
                    upperLeft.add(point);
                }
            }
        }
        // System.out.println(String.format("minX %s, minY %s, maxX %s, maxY %s", minX, minY, maxX, maxY));
        // System.out.println(String.format("total: %d, ll: %d (%.0f%%), lr: %d (%.0f%%), ur: %d (%.0f%%), ul: %d (%.0f%%)",
        // list.size(), lowerLeft.size(), 100.0 * lowerLeft.size() / list.size(), lowerRight.size(),
        // 100.0 * lowerRight.size() / list.size(), upperRight.size(), 100.0 * upperRight.size() / list.size(),
        // upperLeft.size(), 100.0 * upperLeft.size() / list.size()));
        Collections.sort(lowerLeft, forwardComparator);
        Collections.sort(lowerRight, forwardComparator);
        Collections.sort(upperRight, reverseComparator);
        Collections.sort(upperLeft, reverseComparator);
        // Construct the convex hull
        List<Point2d> result = new ArrayList<>();
        result.add(minX);
        for (Point2d point : lowerLeft)
        {
            cleanAndAppend(result, point);
        }
        cleanAndAppend(result, minY);
        for (Point2d point : lowerRight)
        {
            cleanAndAppend(result, point);
        }
        cleanAndAppend(result, maxX);
        for (Point2d point : upperRight)
        {
            cleanAndAppend(result, point);
        }
        cleanAndAppend(result, maxY);
        for (Point2d point : upperLeft)
        {
            cleanAndAppend(result, point);
        }
        return new Polygon2d(result);
    }

    /**
     * Implementation of Andrew's Monotone Chain convex hull algorithm. This implementation (sorts) modifies the provided list
     * of points!
     * @param list List&lt;Point2d&gt;; list of the points (will be modified)
     * @return Polygon2d; the convex hull of the points
     * @throws NullPointerException when the list is null
     * @throws DrawRuntimeException when the list contains too few points
     */
    public static Polygon2d convexHullMonotone(final List<Point2d> list) throws NullPointerException, DrawRuntimeException
    {
        Collections.sort(list, new Comparator<Point2d>()
        {
            @Override
            public int compare(final Point2d point1, final Point2d point2)
            {
                if (point1.x == point2.x)
                {
                    return (int) Math.signum(point1.y - point2.y);
                }
                return (int) Math.signum(point1.x - point2.x);
            }
        });
        // That sort operation was O(N log (N)); the remainder is O(N)
        List<Point2d> result = new ArrayList<>();
        // Lower hull
        for (Point2d p : list)
        {
            while (result.size() >= 2 && !ccw(result.get(result.size() - 2), result.get(result.size() - 1), p))
            {
                result.remove(result.size() - 1);
            }
            result.add(p);
        }
        // Upper hull
        int lowLimit = result.size() + 1;
        for (int i = list.size() - 1; i >= 0; i--)
        {
            Point2d p = list.get(i);
            while (result.size() >= lowLimit && !ccw(result.get(result.size() - 2), result.get(result.size() - 1), p))
            {
                result.remove(result.size() - 1);
            }
            result.add(p);
        }
        if (result.size() > 0)
        {
            result.remove(result.size() - 1);
        }
        // else; zero points; the constructor of the Polygon2d will throw a DrawRuntimeException
        return new Polygon2d(result);
    }

}
