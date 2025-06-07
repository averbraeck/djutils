package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.Export;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * ConvexHullTest.java.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class ConvexHullTest
{

    /**
     * Create the map of all convex hull implementations.
     * @return the map of all convex hull implementations
     */
    public static Map<String, ConvexHullImplementation> getImplementations()
    {
        Map<String, ConvexHullImplementation> implementations = new LinkedHashMap<>();
        implementations.put("Monotone", new ConvexHullImplementation()
        {
            @Override
            public Polygon2d run(final List<Point2d> points)
            {
                return ConvexHull.convexHullMonotone(points);
            }
        });
        implementations.put("Alshamrani", new ConvexHullImplementation()
        {
            @Override
            public Polygon2d run(final List<Point2d> points)
            {
                return ConvexHull.convexHullAlshamrani(points);
            }
        });
        implementations.put("Default", new ConvexHullImplementation()
        {
            @Override
            public Polygon2d run(final List<Point2d> points)
            {
                return ConvexHull.convexHull(points);
            }
        });
        return implementations;
    }

    /**
     * Test a convex hull implementation.
     */
    @Test
    public void testConvexHull()
    {
        Map<String, ConvexHullImplementation> implementations = getImplementations();
        for (String name : implementations.keySet())
        {
            ConvexHullImplementation chi = implementations.get(name);
            try
            {
                chi.run(null);
                fail("should have thrown a NullPointerException");
            }
            catch (NullPointerException e)
            {
                // Ignore expected exception
            }

            try
            {
                chi.run(new ArrayList<>());
                fail("Empty list should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException e)
            {
                // Ignore expected exception
            }

            Point2d testPoint = new Point2d(2, 3);
            List<Point2d> points = Arrays.asList(testPoint);
            try
            {
                chi.run(points);
                fail("List with only one point should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException e)
            {
                // Ignore expected exception
            }

            // Verify that the provided array was not modified.
            assertEquals(1, points.size(), "points still contains one point");
            assertEquals(testPoint, points.get(0), "points still contains testPoint");

            points = new ArrayList<>();
            points.add(testPoint);
            points.add(testPoint);
            try
            {
                chi.run(points);
                fail("List with only two identical should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException e)
            {
                // Ignore expected exception
            }

            // Verify that the provided array was not modified.
            assertEquals(2, points.size(), "points still contains one point");
            assertEquals(testPoint, points.get(0), "first points is testPoint");
            assertEquals(testPoint, points.get(1), "second points is testPoint");
        }
        try
        {
            ConvexHull.convexHull(new LinkedHashSet<Drawable2d>());
            fail("empty collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            ConvexHull.convexHull((Collection<Drawable2d>) null);
            fail("null collection should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        // Example set from https://rosettacode.org/wiki/Convex_hull#Java
        List<Point2d> points = Arrays.asList(new Point2d(16, 3), new Point2d(12, 17), new Point2d(0, 6), new Point2d(-4, -6),
                new Point2d(16, 6), new Point2d(16, -7), new Point2d(16, -3), new Point2d(17, -4), new Point2d(5, 19),
                new Point2d(19, -8), new Point2d(3, 16), new Point2d(12, 13), new Point2d(3, -4), new Point2d(17, 5),
                new Point2d(-3, 15), new Point2d(-3, -9), new Point2d(0, 11), new Point2d(-9, -3), new Point2d(-4, -2),
                new Point2d(12, 10));
        Polygon2d expectedResult = new Polygon2d(new Point2d(-9, -3), new Point2d(-3, -9), new Point2d(19, -8),
                new Point2d(17, 5), new Point2d(12, 17), new Point2d(5, 19), new Point2d(-3, 15));
        checkImplementations(implementations, points, expectedResult);
        Collections.shuffle(points, new Random(123));
        checkImplementations(implementations, points, expectedResult);

        assertEquals(expectedResult, ConvexHull.convexHull(points.iterator()), "convex hull of iterator");

        assertEquals(expectedResult, ConvexHull.convexHull(new PolyLine2d(points)), "convex hull of one drawable");

        assertEquals(expectedResult, ConvexHull.convexHull(new PolyLine2d(points), new Point2d(1, 2)),
                "convex hull of two drawables");
        assertEquals(expectedResult, ConvexHull.convexHull(new Point2d(1, 2), new PolyLine2d(points)),
                "convex hull of two drawables");

        Collection<Drawable2d> collection = new LinkedHashSet<>();
        collection.add(new PolyLine2d(points));
        assertEquals(expectedResult, ConvexHull.convexHull(collection), "convex hull of collection of one Drawable2d object");

        collection.add(new Point2d(1, 2));
        assertEquals(expectedResult, ConvexHull.convexHull(collection), "convex hull of collection of two Drawable2d objects");

        points = new ArrayList<>();
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -2; y <= 2; y++)
            {
                points.add(new Point2d(x, y));
            }
        }
        expectedResult = new Polygon2d(new Point2d(-1, -2), new Point2d(1, -2), new Point2d(1, 2), new Point2d(-1, 2));
        checkImplementations(implementations, points, expectedResult);
        Collections.shuffle(points, new Random(234));
        checkImplementations(implementations, points, expectedResult);

        points.add(new Point2d(-1.1, 0));
        points.add(new Point2d(0, -2.1));
        points.add(new Point2d(1.1, 0));
        points.add(new Point2d(0, 2.1));
        expectedResult = new Polygon2d(new Point2d(-1.1, 0), new Point2d(-1, -2), new Point2d(0, -2.1), new Point2d(1, -2),
                new Point2d(1.1, 0), new Point2d(1, 2), new Point2d(0, 2.1), new Point2d(-1, 2));
        checkImplementations(implementations, points, expectedResult);
        Collections.shuffle(points, new Random(345));
        checkImplementations(implementations, points, expectedResult);

        points.clear();
        double radius = 5000.0 / 64;
        double centerX = 1.5;
        double centerY = 10.5;
        // These for loops should not suffer from rounding errors
        for (double x = centerX - radius; x <= centerX + radius; x += 1)
        {
            for (double y = centerY - radius; y <= centerY + radius; y += 1)
            {
                double distance = Math.hypot(x - centerX, y - centerY);
                if (distance <= radius)
                {
                    points.add(new Point2d(x, y));
                }
            }
        }
        // It is a bit hard to construct the expected result; we'll use the result of convexHullMonotone as reference because it
        // is simpler and therefore less likely to contain errors.
        expectedResult = ConvexHull.convexHullMonotone(new ArrayList<>(points));
        checkImplementations(implementations, points, expectedResult);
        Collections.shuffle(points, new Random(456));
        checkImplementations(implementations, points, expectedResult);
    }

    /**
     * Compare performance.
     * @param args the command line arguments (not used)
     * @throws IOException ...
     */
    public static void main(final String[] args) throws IOException
    {
        Map<String, ConvexHullImplementation> implementations = getImplementations();
        List<Point2d> points = new ArrayList<>();
        double centerX = 1.5;
        double centerY = 10.5;
        System.out.println("type return when the profiler is ready");
        System.in.read();
        for (double radius = 5000.0 / 64; radius <= 6000; radius *= 2)
        {
            // These for loops should not suffer from rounding errors
            points.clear();
            for (double x = centerX - radius; x <= centerX + radius; x += 1)
            {
                for (double y = centerY - radius; y <= centerY + radius; y += 1)
                {
                    double distance = Math.hypot(x - centerX, y - centerY);
                    if (distance <= radius)
                    {
                        points.add(new Point2d(x, y));
                    }
                }
            }
            System.out.print("radius=" + radius + "; ordered input data\n");
            for (String name : implementations.keySet())
            {
                ConvexHullImplementation implementation = implementations.get(name);
                List<Point2d> workList = new ArrayList<>(points);
                System.gc();
                long startNanos = System.nanoTime();
                implementation.run(workList);
                long endNanos = System.nanoTime();
                double duration = (endNanos - startNanos) / 1e9;
                System.out.println(String.format("%d points %s: %.3f s", points.size(), name, duration));
            }
            Collections.shuffle(points, new Random(876));
            System.out.print("Radius=" + radius + "; scrambled input data\n");
            for (String name : implementations.keySet())
            {
                ConvexHullImplementation implementation = implementations.get(name);
                List<Point2d> workList = new ArrayList<>(points);
                System.gc();
                long startNanos = System.nanoTime();
                implementation.run(workList);
                long endNanos = System.nanoTime();
                double duration = (endNanos - startNanos) / 1e9;
                System.out.println(String.format("%d points %s: %.3f s", points.size(), name, duration));
            }
        }
    }

    /**
     * Check that all implementations of convex hull give the expected result.
     * @param implementations the implementations to check
     * @param in the input for the convex hull implementations
     * @param expectedResult the expected result
     */
    public static void checkImplementations(final Map<String, ConvexHullImplementation> implementations, final List<Point2d> in,
            final Polygon2d expectedResult)
    {
        for (String name : implementations.keySet())
        {
            Polygon2d result = implementations.get(name).run(new ArrayList<>(in));
            if (!result.equals(expectedResult))
            {
                System.err.println("Discrepancy for " + name);
                System.err.println("        result: " + Export.toPlot(result));
                System.err.println("expectedResult: " + Export.toPlot(expectedResult));
                System.err.println("         input: " + in);
                implementations.get(name).run(new ArrayList<>(in));
            }
            assertEquals(expectedResult, result, name);
        }

    }

    /**
     * Wrapper for any convex hull implementation.
     */
    interface ConvexHullImplementation
    {
        /**
         * Run a particular implementation of the convex hull algorithm.
         * @param points the points for which the convex hull must be constructed
         * @return the convex hull of the points
         * @throws NullPointerException when list is null
         * @throws IllegalArgumentException when list is empty
         */
        Polygon2d run(List<Point2d> points) throws NullPointerException, IllegalArgumentException;
    }

    /**
     * Problem reported by mtarik34b.
     */
    @Test
    public void alshamraniProblem()
    {
        List<Point2d> pointList = new ArrayList<>();
        pointList.add(new Point2d(70, 300));
        pointList.add(new Point2d(120, 190));
        pointList.add(new Point2d(320, 60));
        pointList.add(new Point2d(280, 240));
        Polygon2d expectedResult = ConvexHull.convexHullMonotone(pointList);
        Polygon2d actualResult = ConvexHull.convexHullAlshamrani(pointList);
        // System.out.println(Export.toPlot(expectedResult));
        // System.out.println(Export.toPlot(actualResult));
        // assertEquals(expectedResult, actualResult, "Hmm");
    }

}
