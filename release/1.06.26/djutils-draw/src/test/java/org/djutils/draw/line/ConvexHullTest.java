package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * ConvexHullTest.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class ConvexHullTest
{

    /**
     * Create the map of all convex hull implementations.
     * @return Map&lt;String, ConvexHullImplementation&gt;; the map of all convex hull implementations
     */
    public static Map<String, ConvexHullImplementation> getImplementations()
    {
        Map<String, ConvexHullImplementation> implementations = new LinkedHashMap<>();
        implementations.put("Monotone", new ConvexHullImplementation()
        {
            @Override
            public Polygon2d run(final List<Point2d> points) throws NullPointerException, DrawRuntimeException
            {
                return ConvexHull.convexHullMonotone(points);
            }
        });
        implementations.put("Alshamrani", new ConvexHullImplementation()
        {
            @Override
            public Polygon2d run(final List<Point2d> points) throws NullPointerException, DrawRuntimeException
            {
                return ConvexHull.convexHullAlshamrani(points);
            }
        });
        implementations.put("Default", new ConvexHullImplementation()
        {
            @Override
            public Polygon2d run(final List<Point2d> points) throws NullPointerException, DrawRuntimeException
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
            catch (NullPointerException npe)
            {
                // Ignore expected exception
            }

            try
            {
                chi.run(new ArrayList<>());
                fail("Empty list should have thrown a DrawRuntimeException");
            }
            catch (DrawRuntimeException dre)
            {
                // Ignore expected exception
            }

            Point2d testPoint = new Point2d(2, 3);
            List<Point2d> points = Arrays.asList(testPoint);
            try
            {
                chi.run(points);
                fail("List with only one point should have thrown a DrawRuntimeException");
            }
            catch (DrawRuntimeException dre)
            {
                // Ignore expected exception
            }
            
            // Verify that the provided array was not modified.
            assertEquals("points still contains one point", 1, points.size());
            assertEquals("points still contains testPoint", testPoint, points.get(0));
            
            points = new ArrayList<>();
            points.add(testPoint);
            points.add(testPoint);
            try
            {
                chi.run(points);
                fail("List with only two identical should have thrown a DrawRuntimeException");
            }
            catch (DrawRuntimeException dre)
            {
                // Ignore expected exception
            }
            
            // Verify that the provided array was not modified.
            assertEquals("points still contains one point", 2, points.size());
            assertEquals("first points is testPoint", testPoint, points.get(0));
            assertEquals("second points is testPoint", testPoint, points.get(1));
        }
        try
        {
            ConvexHull.convexHull(new LinkedHashSet<Drawable2d>());
            fail("empty collection should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            ConvexHull.convexHull((Collection<Drawable2d>) null);
            fail("null collection should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
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

        assertEquals("convex hull of iterator", expectedResult, ConvexHull.convexHull(points.iterator()));

        assertEquals("convex hull of one drawable", expectedResult, ConvexHull.convexHull(new PolyLine2d(points)));

        assertEquals("convex hull of two drawables", expectedResult,
                ConvexHull.convexHull(new PolyLine2d(points), new Point2d(1, 2)));
        assertEquals("convex hull of two drawables", expectedResult,
                ConvexHull.convexHull(new Point2d(1, 2), new PolyLine2d(points)));

        Collection<Drawable2d> collection = new LinkedHashSet<>();
        collection.add(new PolyLine2d(points));
        assertEquals("convex hull of collection of one Drawable2d object", expectedResult, ConvexHull.convexHull(collection));

        collection.add(new Point2d(1, 2));
        assertEquals("convex hull of collection of two Drawable2d objects", expectedResult, ConvexHull.convexHull(collection));

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
     * @param args String[]; the command line arguments (not used)
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
     * @param implementations Map&lt;String, ConvexHullImplementation&gt;; the implementations to check
     * @param in List&lt;Point2d&gt;; the input for the convex hull implementations
     * @param expectedResult Polygon2d; the expected result
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
                System.err.println("        result: " + result.toPlot());
                System.err.println("expectedResult: " + expectedResult.toPlot());
                System.err.println("         input: " + in);
                implementations.get(name).run(new ArrayList<>(in));
            }
            assertEquals(name, expectedResult, result);
        }

    }

    /**
     * Wrapper for any convex hull implementation.
     */
    interface ConvexHullImplementation
    {
        /**
         * Run a particular implementation of the convex hull algorithm.
         * @param points List&lt;Point2d&gt;; the points for which the convex hull must be constructed
         * @return Polygon2d; the convex hull of the points
         * @throws NullPointerException when list is null
         * @throws DrawRuntimeException when list is empty
         */
        Polygon2d run(List<Point2d> points) throws NullPointerException, DrawRuntimeException;
    }

}
