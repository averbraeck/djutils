package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Transform2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.line.PolyLine.TransitionFunction;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * TestLine2d.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PolyLine2dTest
{

    /**
     * Test the constructors of PolyLine2d.
     * @throws DrawRuntimeException on failure
     */
    @Test
    public final void constructorsTest() throws DrawRuntimeException
    {
        double[] values = { -999, 0, 99, 9999 }; // Keep this list short; execution time grows with 6th power of length
        Point2d[] points = new Point2d[0]; // Empty array
        try
        {
            runConstructors(points);
            fail("Should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        for (double x0 : values)
        {
            for (double y0 : values)
            {
                points = new Point2d[1]; // Degenerate array holding one point
                points[0] = new Point2d(x0, y0);
                try
                {
                    runConstructors(points);
                    fail("Should have thrown a DrawRuntimeException");
                }
                catch (DrawRuntimeException exception)
                {
                    // Ignore expected exception
                }
                for (double x1 : values)
                {
                    for (double y1 : values)
                    {
                        points = new Point2d[2]; // Straight line; two points
                        points[0] = new Point2d(x0, y0);
                        points[1] = new Point2d(x1, y1);
                        if (0 == points[0].distance(points[1]))
                        {
                            try
                            {
                                runConstructors(points);
                                fail("Should have thrown a DrawRuntimeException");
                            }
                            catch (DrawRuntimeException exception)
                            {
                                // Ignore expected exception
                            }
                        }
                        else
                        {
                            runConstructors(points);
                            for (double x2 : values)
                            {
                                for (double y2 : values)
                                {
                                    points = new Point2d[3]; // Line with intermediate point
                                    points[0] = new Point2d(x0, y0);
                                    points[1] = new Point2d(x1, y1);
                                    points[2] = new Point2d(x2, y2);
                                    if (0 == points[1].distance(points[2]))
                                    {
                                        try
                                        {
                                            runConstructors(points);
                                            fail("Should have thrown a DrawRuntimeException");
                                        }
                                        catch (DrawRuntimeException exception)
                                        {
                                            // Ignore expected exception
                                        }
                                    }
                                    else
                                    {
                                        runConstructors(points);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Test all the constructors of PolyLine2d.
     * @param points Point2d[]; array of Point2d to test with
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    private void runConstructors(final Point2d[] points) throws DrawRuntimeException
    {
        verifyPointsAndSegments(new PolyLine2d(points), points);
        List<Point2d> list = new ArrayList<>();
        for (int i = 0; i < points.length; i++)
        {
            list.add(points[i]);
        }
        PolyLine2d line = new PolyLine2d(list);
        verifyPointsAndSegments(line, points);
        verifyPointsAndSegments(new PolyLine2d(line.getPoints()), points);
        assertEquals("length at index 0", 0.0, line.lengthAtIndex(0), 0);
        double length = 0;
        for (int i = 1; i < points.length; i++)
        {
            length += Math.sqrt(Math.pow(points[i].x - points[i - 1].x, 2) + Math.pow(points[i].y - points[i - 1].y, 2));
            assertEquals("length at index", length, line.lengthAtIndex(i), 0.0001);
        }
        assertEquals("length", length, line.getLength(), 10 * Math.ulp(length));

        assertEquals("size", points.length, line.size());

        Bounds2d b2d = line.getBounds();
        Bounds2d ref = new Bounds2d(points);
        assertEquals("bounds is correct", ref, b2d);

        try
        {
            line.get(-1);
            fail("Negative index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            line.get(line.size() + 1);
            fail("Too large index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        int horizontalMoves = 0;
        Path2D path = new Path2D.Double();
        path.moveTo(points[0].x, points[0].y);
        // System.out.print("path is "); printPath2D(path);
        for (int i = 1; i < points.length; i++)
        {
            // Path2D is corrupt if same point is added twice in succession
            if (points[i].x != points[i - 1].x || points[i].y != points[i - 1].y)
            {
                path.lineTo(points[i].x, points[i].y);
                horizontalMoves++;
            }
        }
        try
        {
            line = new PolyLine2d(path);
            if (0 == horizontalMoves)
            {
                fail("Construction of Line2d from path with degenerate projection should have failed");
            }
            assertEquals("number of points should match", horizontalMoves + 1, line.size());
            int indexInLine = 0;
            for (int i = 0; i < points.length; i++)
            {
                if (i > 0 && (points[i].x != points[i - 1].x || points[i].y != points[i - 1].y))
                {
                    indexInLine++;
                }
                assertEquals("x in line", points[i].x, line.get(indexInLine).x, 0.001);
                assertEquals("y in line", points[i].y, line.get(indexInLine).y, 0.001);
            }
        }
        catch (DrawRuntimeException e)
        {
            if (0 != horizontalMoves)
            {
                fail("Construction of Line2d from path with non-degenerate projection should not have failed");
            }
        }
    }

    /**
     * Test construction of a Line2d from a Path2D with SEG_CLOSE.
     * @throws DrawRuntimeException on unexpected error
     */
    @Test
    public void testPathWithClose() throws DrawRuntimeException
    {
        Path2D path = new Path2D.Double();
        path.moveTo(1, 2);
        path.lineTo(4, 5);
        path.lineTo(4, 8);
        path.closePath();
        PolyLine2d line = new PolyLine2d(path);
        assertEquals("line has 4 points", 4, line.size());
        assertEquals("first point equals last point", line.getFirst(), line.getLast());
        // Now the case that the path was already closed
        path = new Path2D.Double();
        path.moveTo(1, 2);
        path.lineTo(4, 5);
        path.lineTo(1, 2);
        path.closePath();
        line = new PolyLine2d(path);
        assertEquals("line has 4 points", 3, line.size());
        assertEquals("first point equals last point", line.getFirst(), line.getLast());
        path = new Path2D.Double();
        path.moveTo(1, 2);
        path.lineTo(4, 5);
        path.lineTo(4, 8);
        path.curveTo(1, 2, 3, 4, 5, 6);
        try
        {
            new PolyLine2d(path);
            fail("unsupported SEG_CUBICTO should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test all constructors of a Line2d.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     */
    @Test
    public void testConstructors() throws DrawRuntimeException, DrawRuntimeException
    {
        runConstructors(new Point2d[] { new Point2d(1.2, 3.4), new Point2d(2.3, 4.5), new Point2d(3.4, 5.6) });

        try
        {
            new PolyLine2d(new double[] { 1, 2, 3 }, new double[] { 4, 5 });
            fail("double arrays of unequal length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(new double[] { 1, 2 }, new double[] { 3, 4, 5 });
            fail("double arrays of unequal length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(null, new double[] { 1, 2 });
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(new double[] { 1, 2 }, null);
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d((List<Point2d>) null);
            fail("null list should have thrown a nullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        List<Point2d> shortList = new ArrayList<>();
        try
        {
            new PolyLine2d(shortList);
            fail("empty list should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        shortList.add(new Point2d(1, 2));
        try
        {
            new PolyLine2d(shortList);
            fail("one-point list should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        Point2d p1 = new Point2d(1, 2);
        Point2d p2 = new Point2d(3, 4);
        PolyLine2d pl = new PolyLine2d(p1, p2);
        assertEquals("two points", 2, pl.size());
        assertEquals("p1", p1, pl.get(0));
        assertEquals("p2", p2, pl.get(1));

        pl = new PolyLine2d(p1, p2, (Point2d[]) null);
        assertEquals("two points", 2, pl.size());
        assertEquals("p1", p1, pl.get(0));
        assertEquals("p2", p2, pl.get(1));

        pl = new PolyLine2d(p1, p2, new Point2d[0]);
        assertEquals("two points", 2, pl.size());
        assertEquals("p1", p1, pl.get(0));
        assertEquals("p2", p2, pl.get(1));

        try
        {
            new PolyLine2d(new Point2d[] {});
            fail("empty array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(new Point2d[] { new Point2d(1, 2) });
            fail("single point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(1, 2) });
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4) });
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(new Point2d[] { new Point2d(-1, -2), new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4) });
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the other methods of PolyLine2d.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws NullPointerException if that happens uncaught; this test has failed
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testOtherMethods() throws NullPointerException, DrawRuntimeException
    {
        Point2d[] array = new Point2d[] { new Point2d(1, 2), new Point2d(3, 4), new Point2d(3.2, 4.1), new Point2d(5, 6) };
        PolyLine2d line = new PolyLine2d(Arrays.stream(array).iterator());
        assertEquals("size", array.length, line.size());
        for (int i = 0; i < array.length; i++)
        {
            assertEquals("i-th point", array[i], line.get(i));
        }
        int nextIndex = 0;
        for (Iterator<Point2d> iterator = line.getPoints(); iterator.hasNext();)
        {
            assertEquals("i-th point from line iterator", array[nextIndex++], iterator.next());
        }
        assertEquals("iterator returned all points", array.length, nextIndex);

        PolyLine2d filtered = line.noiseFilteredLine(0.0);
        assertEquals("filtered with 0 tolerance returns line", line, filtered);
        filtered = line.noiseFilteredLine(0.01);
        assertEquals("filtered with very low tolerance returns line", line, filtered);
        filtered = line.noiseFilteredLine(0.5);
        assertEquals("size of filtered line is 3", 3, filtered.size());
        assertEquals("first point of filtered line matches", line.getFirst(), filtered.getFirst());
        assertEquals("last point of filtered line matches", line.getLast(), filtered.getLast());
        assertEquals("mid point of filtered line is point 1 of unfiltered line", line.get(1), filtered.get(1));
        filtered = line.noiseFilteredLine(10);
        assertEquals("size of filtered line is 2", 2, filtered.size());
        assertEquals("first point of filtered line matches", line.getFirst(), filtered.getFirst());
        assertEquals("last point of filtered line matches", line.getLast(), filtered.getLast());

        array = new Point2d[] { new Point2d(1, 2), new Point2d(3, 4), new Point2d(3.2, 4.1), new Point2d(1, 2) };
        line = new PolyLine2d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(10);
        assertEquals("size of filtered line is 3", 3, filtered.size());
        assertEquals("first point of filtered line matches", line.getFirst(), filtered.getFirst());
        assertEquals("last point of filtered line matches", line.getLast(), filtered.getLast());
        assertEquals("mid point of filtered line is point 1 of unfiltered line", line.get(1), filtered.get(1));

        array = new Point2d[] { new Point2d(1, 2), new Point2d(3, 4), new Point2d(1.1, 2.1), new Point2d(1, 2) };
        line = new PolyLine2d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(0.5);
        assertEquals("size of filtered line is 3", 3, filtered.size());
        assertEquals("first point of filtered line matches", line.getFirst(), filtered.getFirst());
        assertEquals("last point of filtered line matches", line.getLast(), filtered.getLast());
        assertEquals("mid point of filtered line is point 1 of unfiltered line", line.get(1), filtered.get(1));

        array = new Point2d[] { new Point2d(1, 2), new Point2d(3, 4) };
        line = new PolyLine2d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(10);
        assertEquals("Filtering a two-point line returns that line", line, filtered);

        array = new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4) };
        line = new PolyLine2d(true, array);
        assertEquals("cleaned line has 2 points", 2, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4), new Point2d(3, 4) };
        line = new PolyLine2d(true, array);
        assertEquals("cleaned line has 2 points", 2, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point2d[] { new Point2d(0, -1), new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4) };
        line = new PolyLine2d(true, array);
        assertEquals("cleaned line has 2 points", 3, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point2d[] { new Point2d(0, -1), new Point2d(1, 2), new Point2d(1, 2), new Point2d(1, 2),
                new Point2d(3, 4) };
        line = new PolyLine2d(true, array);
        assertEquals("cleaned line has 3 points", 3, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("mid point", array[1], line.get(1));
        assertEquals("last point", array[array.length - 1], line.getLast());

        try
        {
            new PolyLine2d(true, new Point2d[0]);
            fail("Too short array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(true, new Point2d[] { new Point2d(1, 2) });
            fail("Too short array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(true, new Point2d[] { new Point2d(1, 2), new Point2d(1, 2) });
            fail("All duplicate points in array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine2d(true, new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(1, 2) });
            fail("All duplicate points in array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        array = new Point2d[] { new Point2d(1, 2), new Point2d(4, 6), new Point2d(8, 9) };
        line = new PolyLine2d(array);

        try
        {
            line.getLocation(-0.1);
            fail("negative location should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        double length = line.getLength();
        assertEquals("Length of line is 10", 10, length, 0.000001);

        try
        {
            line.getLocation(length + 0.1);
            fail("location beyond length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            line.getLocation(-0.1);
            fail("negative location should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        assertEquals("Length of line is 10", 10, length, 0.000001);

        try
        {
            line.getLocationFraction(1.1);
            fail("location beyond length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException de)
        {
            // Ignore expected exception
        }

        try
        {
            line.getLocationFraction(-0.1);
            fail("negative location should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        for (double position : new double[] { -1, 0, 2.5, 4.9, 5.1, 7.5, 9.9, 10, 11 })
        {
            Ray2d ray = line.getLocationExtended(position);
            if (position < 5)
            {
                Ray2d expected = new Ray2d(array[0].interpolate(array[1], position / 5), Math.atan2(4, 3));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
            else
            {
                Ray2d expected = new Ray2d(array[1].interpolate(array[2], (position - 5) / 5), Math.atan2(3, 4));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
            ray = line.getLocationFractionExtended(position / line.getLength());
            if (position < 5)
            {
                Ray2d expected = new Ray2d(array[0].interpolate(array[1], position / 5), Math.atan2(4, 3));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
            else
            {
                Ray2d expected = new Ray2d(array[1].interpolate(array[2], (position - 5) / 5), Math.atan2(3, 4));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
        }

        // Test the projectOrthogonal methods
        array = new Point2d[] { new Point2d(1, 2), new Point2d(4, 6), new Point2d(8, 9) };
        line = new PolyLine2d(array);
        // System.out.println(line.toPlot());
        for (double x = -15; x <= 20; x++)
        {
            for (double y = -15; y <= 20; y++)
            {
                Point2d xy = new Point2d(x, y);
                // System.out.println("x=" + x + ", y=" + y);
                double result = line.projectOrthogonalFractional(xy);
                if (!Double.isNaN(result))
                {
                    assertTrue("result must be >= 0.0", result >= 0);
                    assertTrue("result must be <= 1.0", result <= 1.0);
                    Ray2d ray = line.getLocationFraction(result);
                    Point2d projected = line.projectOrthogonal(xy);
                    assertEquals("if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction", ray.x,
                            projected.x, 00001);
                    assertEquals("if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction", ray.y,
                            projected.y, 00001);
                }
                else
                {
                    assertNull("point projects outside line", line.projectOrthogonal(xy));
                }
                result = line.projectOrthogonalFractionalExtended(xy);
                if (!Double.isNaN(result))
                {
                    Point2d resultPoint = line.getLocationFractionExtended(result);
                    if (result >= 0.0 && result <= 1.0)
                    {
                        Point2d closestPointOnLine = line.closestPointOnPolyLine(xy);
                        assertEquals("resultPoint is equal to closestPoint", resultPoint, closestPointOnLine);
                        assertEquals("getLocationFraction returns same as getLocationfractionExtended", resultPoint,
                                line.getLocationFraction(result));
                    }
                    else
                    {
                        try
                        {
                            line.getLocationFraction(result);
                            fail("illegal fraction should have thrown a DrawRuntimeException");
                        }
                        catch (DrawRuntimeException dre)
                        {
                            // Ignore expected exception
                        }
                        if (result < 0)
                        {
                            assertEquals("resultPoint lies on extention of start segment",
                                    resultPoint.distance(line.get(1)) - resultPoint.distance(line.getFirst()),
                                    line.getFirst().distance(line.get(1)), 0.0001);
                        }
                        else
                        {
                            // result > 1
                            assertEquals("resultPoint lies on extention of end segment",
                                    resultPoint.distance(line.get(line.size() - 2)) - resultPoint.distance(line.getLast()),
                                    line.getLast().distance(line.get(line.size() - 2)), 0.0001);
                        }
                    }
                }
                else
                {
                    assertNull("point projects outside extended line", line.projectOrthogonalExtended(xy));
                    Point2d closestPointOnLine = line.closestPointOnPolyLine(xy);
                    assertNotNull("closest point is never null", closestPointOnLine);
                    boolean found = false;
                    for (int index = 0; index < line.size(); index++)
                    {
                        Point2d linePoint = line.get(index);
                        if (linePoint.x == closestPointOnLine.x && linePoint.y == closestPointOnLine.y)
                        {
                            found = true;
                        }
                    }
                    assertTrue("closestPointOnLine is one of the construction points of the line", found);
                }
                Point2d closestPointOnLine = line.closestPointOnPolyLine(xy);
                assertNotNull("closest point is never null", closestPointOnLine);
            }
        }
        Point2d toleranceResultPoint = line.getLocationFraction(-0.01, 0.01);
        assertEquals("tolerance result matches extended fraction result", line.getLocationFraction(0), toleranceResultPoint);
        toleranceResultPoint = line.getLocationFraction(1.01, 0.01);
        assertEquals("tolerance result matches extended fraction result", line.getLocationFraction(1), toleranceResultPoint);

        try
        {
            line.getLocationFraction(-.011, 0.01);
            fail("fraction outside tolerance should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            line.getLocationFraction(1.011, 0.01);
            fail("fraction outside tolerance should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        // Test the extract and truncate methods
        array = new Point2d[] { new Point2d(1, 2), new Point2d(4, 6), new Point2d(8, 9) };
        line = new PolyLine2d(array);
        length = line.getLength();
        for (double to : new double[] { -10, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20 })
        {
            if (to <= 0 || to > length)
            {
                try
                {
                    line.truncate(to);
                    fail("illegal truncate should have thrown a DrawRuntimeException");
                }
                catch (DrawRuntimeException dre)
                {
                    // Ignore expected exception
                }
            }
            else
            {
                PolyLine2d truncated = line.truncate(to);
                assertEquals("truncated line start with start point of line", line.getFirst(), truncated.getFirst());
                assertEquals("Length of truncated line is truncate position", to, truncated.getLength(), 0.0001);
            }
            for (double from : new double[] { -10, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20 })
            {
                if (from >= to || from < 0 || to > length)
                {
                    try
                    {
                        line.extract(from, to);
                        fail("Illegal range should have thrown a DrawRuntimeException");
                    }
                    catch (DrawRuntimeException dre)
                    {
                        // Ignore expected exception
                    }
                }
                else
                {
                    PolyLine2d fragment = line.extract(from, to);
                    Point2d fromPoint = line.getLocation(from);
                    assertTrue("fragment starts at from", fromPoint.epsilonEquals(fragment.getFirst(), 0.00001));
                    Point2d toPoint = line.getLocation(to);
                    assertTrue("fragment ends at to", toPoint.epsilonEquals(fragment.getLast(), 0.00001));
                    assertEquals("Length of fragment", to - from, fragment.getLength(), 0.0001);
                    if (from == 0)
                    {
                        assertEquals("fragment starts at begin of line", line.getFirst(), fragment.getFirst());
                    }
                    if (to == length)
                    {
                        assertEquals("fragment ends at end of line", line.getLast(), fragment.getLast());
                    }
                }
            }
        }
        try
        {
            line.extract(Double.NaN, 10.0);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            line.extract(0.0, Double.NaN);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        // Verify that hashCode. Check that the result depends on the actual coordinates.
        assertNotEquals("hash code takes x coordinate of first point into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(1, 0), new Point2d(1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate of first point into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 1), new Point2d(1, 1)).hashCode());
        assertNotEquals("hash code takes x coordinate of second point into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 0), new Point2d(2, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate of second point into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 2)).hashCode());

        // Verify the equals method.
        assertTrue("line is equal to itself", line.equals(line));
        assertFalse("line is not equal to a different line",
                line.equals(new PolyLine2d(new Point2d(123, 456), new Point2d(789, 101112))));
        assertFalse("line is not equal to null", line.equals(null));
        assertFalse("line is not equal to a different kind of object", line.equals("unlikely"));
        assertTrue("Line is equal to line from same set of points", line.equals(new PolyLine2d(line.getPoints())));
        // Make a line that differs only in the very last point
        Point2d[] otherArray = Arrays.copyOf(array, array.length);
        otherArray[otherArray.length - 1] =
                new Point2d(otherArray[otherArray.length - 1].x, otherArray[otherArray.length - 1].y + 5);
        PolyLine2d other = new PolyLine2d(otherArray);
        assertFalse("PolyLine2d that differs in y of last point is different", line.equals(other));
    }

    /**
     * Test the concatenate method.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void concatenateTest() throws DrawRuntimeException
    {
        Point2d p0 = new Point2d(1.1, 2.2);
        Point2d p1 = new Point2d(2.1, 2.2);
        Point2d p2 = new Point2d(3.1, 2.2);
        Point2d p3 = new Point2d(4.1, 2.2);
        Point2d p4 = new Point2d(5.1, 2.2);
        Point2d p5 = new Point2d(6.1, 2.2);

        PolyLine2d l0 = new PolyLine2d(p0, p1, p2);
        PolyLine2d l1 = new PolyLine2d(p2, p3);
        PolyLine2d l2 = new PolyLine2d(p3, p4, p5);
        PolyLine2d ll = PolyLine2d.concatenate(l0, l1, l2);
        assertEquals("size is 6", 6, ll.size());
        assertEquals("point 0 is p0", p0, ll.get(0));
        assertEquals("point 1 is p1", p1, ll.get(1));
        assertEquals("point 2 is p2", p2, ll.get(2));
        assertEquals("point 3 is p3", p3, ll.get(3));
        assertEquals("point 4 is p4", p4, ll.get(4));
        assertEquals("point 5 is p5", p5, ll.get(5));

        ll = PolyLine2d.concatenate(l1);
        assertEquals("size is 2", 2, ll.size());
        assertEquals("point 0 is p2", p2, ll.get(0));
        assertEquals("point 1 is p3", p3, ll.get(1));

        try
        {
            PolyLine2d.concatenate(l0, l2);
            fail("Gap should have throw a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        try
        {
            PolyLine2d.concatenate();
            fail("concatenate of empty list should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        // Test concatenate methods with tolerance
        PolyLine2d thirdLine = new PolyLine2d(p4, p5);
        for (double tolerance : new double[] { 0.1, 0.01, 0.001, 0.0001, 0.00001 })
        {
            for (double actualError : new double[] { tolerance * 0.9, tolerance * 1.1 })
            {
                int maxDirection = 10;
                for (int direction = 0; direction < maxDirection; direction++)
                {
                    double dx = actualError * Math.cos(Math.PI * 2 * direction / maxDirection);
                    double dy = actualError * Math.sin(Math.PI * 2 * direction / maxDirection);
                    PolyLine2d otherLine = new PolyLine2d(new Point2d(p2.x + dx, p2.y + dy), p3, p4);
                    if (actualError < tolerance)
                    {
                        try
                        {
                            PolyLine2d.concatenate(tolerance, l0, otherLine);
                        }
                        catch (DrawRuntimeException dre)
                        {
                            PolyLine2d.concatenate(tolerance, l0, otherLine);
                            fail("concatenation with error " + actualError + " and tolerance " + tolerance
                                    + " should not have failed");
                        }
                        try
                        {
                            PolyLine2d.concatenate(tolerance, l0, otherLine, thirdLine);
                        }
                        catch (DrawRuntimeException dre)
                        {
                            fail("concatenation with error " + actualError + " and tolerance " + tolerance
                                    + " should not have failed");
                        }
                    }
                    else
                    {
                        try
                        {
                            PolyLine2d.concatenate(tolerance, l0, otherLine);
                        }
                        catch (DrawRuntimeException dre)
                        {
                            // Ignore expected exception
                        }
                        try
                        {
                            PolyLine2d.concatenate(tolerance, l0, otherLine, thirdLine);
                        }
                        catch (DrawRuntimeException dre)
                        {
                            // Ignore expected exception
                        }
                    }
                }
            }
        }
    }

    /**
     * Test the offsetLine methods.
     * @throws DrawRuntimeException when that happens uncaught; this test has failed
     */
    @Test
    public void testOffsetLine() throws DrawRuntimeException
    {
        for (Point2d[] points : new Point2d[][] { { new Point2d(1, 2), new Point2d(3, 50) },
                { new Point2d(-40, -20), new Point2d(5, -2), new Point2d(3, 50) },
                { new Point2d(-40, -20), new Point2d(5, -2), new Point2d(3, -50) } })
        {
            for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 360)
            {
                Transform2d transform = new Transform2d().rotation(angle);
                Point2d[] transformed = new Point2d[points.length];
                for (int index = 0; index < points.length; index++)
                {
                    transformed[index] = transform.transform(points[index]);
                }
                PolyLine2d line = new PolyLine2d(transformed);
                // System.out.println("angle " + Math.toDegrees(angle) + " line " + line);
                try
                {
                    line.offsetLine(Double.NaN);
                    fail("NaN offset should have thrown an IllegalArgumentException");
                }
                catch (IllegalArgumentException iae)
                {
                    // Ignore expected exception
                }
                assertEquals("offset 0 yields the reference line", line, line.offsetLine(0));
                // System.out.print("reference line " + line.toPlot());
                for (double offset : new double[] { 1, 10, 0.1, -0.1, -10 })
                {
                    PolyLine2d offsetLine = line.offsetLine(offset);
                    // System.out.print("angle " + angle + ", offset " + offset + ": " + offsetLine.toPlot());
                    if (points.length == 2)
                    {
                        assertEquals("two-point line should have a two-point offset line", 2, offsetLine.size());
                        assertEquals("length of offset line of two-point reference line equals length of reference line",
                                line.getLength(), offsetLine.getLength(), 0.01);
                    }
                    assertEquals("offset at start", Math.abs(offset), line.getFirst().distance(offsetLine.getFirst()), 0.01);
                    assertEquals("offset at end", Math.abs(offset), line.getLast().distance(offsetLine.getLast()), 0.01);
                    // Verify that negative offset works in the direction opposite to positive
                    assertEquals("offset to the left vs to the right differs by twice the offset", Math.abs(2 * offset),
                            offsetLine.getFirst().distance(line.offsetLine(-offset).getFirst()), 0.001);
                    // The following four may be false if the offset is not small comparable to the lenght of the first or last
                    // segment of the line
                    assertEquals("projection of first point of line onto offset line is (almost) first point of offset line", 0,
                            offsetLine.getLocationExtended(
                                    offsetLine.projectOrthogonalFractionalExtended(line.getFirst()) * offsetLine.getLength())
                                    .distance(offsetLine.getFirst()),
                            0.01);
                    double fraction = offsetLine.projectOrthogonalFractionalExtended(line.getLast());
                    assertEquals("fraction should be 1 with maximum error a few ULP", 1, fraction, 0.000001);
                    if (fraction > 1.0)
                    {
                        fraction = 1.0;
                    }
                    assertEquals("projection of last point of line onto offset line is (almost) last point of offset line", 0,
                            offsetLine.getLocation(fraction * offsetLine.getLength()).distance(offsetLine.getLast()), 0.01);
                    assertEquals("projection of first point of offset line onto line is (almost) first point of line", 0,
                            line.getLocationExtended(
                                    line.projectOrthogonalFractionalExtended(offsetLine.getFirst()) * line.getLength())
                                    .distance(line.getFirst()),
                            0.01);
                    fraction = line.projectOrthogonalFractionalExtended(offsetLine.getLast());
                    assertEquals("fraction should be 1 with maximum error a few ULP", 1, fraction, 0.000001);
                    if (fraction > 1.0)
                    {
                        fraction = 1.0;
                    }
                    assertEquals("projection of last point of offset line onto line is (almost) last point of line", 0,
                            line.getLocation(fraction * line.getLength()).distance(line.getLast()), 0.01);
                }
            }
        }

        PolyLine2d line = new PolyLine2d(new Point2d(1, 2), new Point2d(3, 4));
        try
        {
            line.offsetLine(1, 0, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE, PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_FILTER_RATIO, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("zero circle precision should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, Double.NaN, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, PolyLine.DEFAULT_OFFSET_FILTER_RATIO,
                    PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("NaN circle precision should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, 0, PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_FILTER_RATIO, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("zero offsetMinimumFilterValue should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, Double.NaN, PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_FILTER_RATIO, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("NaN offsetMinimumFilterValue should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, PolyLine.DEFAULT_OFFSET_FILTER_RATIO,
                    PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("offsetMinimumFilterValue not less than offsetMaximumFilterValue should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE, 0,
                    PolyLine.DEFAULT_OFFSET_FILTER_RATIO, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("zero offsetMaximumfilterValue should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE, Double.NaN,
                    PolyLine.DEFAULT_OFFSET_FILTER_RATIO, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("NaN offsetMaximumfilterValue should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, 0, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("zero offsetFilterRatio should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, Double.NaN, PolyLine.DEFAULT_OFFSET_PRECISION);
            fail("NaN offsetFilterRatio should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, PolyLine.DEFAULT_OFFSET_FILTER_RATIO, 0);
            fail("zero offsetPrecision should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            line.offsetLine(1, PolyLine.DEFAULT_CIRCLE_PRECISION, PolyLine.DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                    PolyLine.DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, PolyLine.DEFAULT_OFFSET_FILTER_RATIO, Double.NaN);
            fail("NaN offsetPrecision should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

    }

    /**
     * Test the projectRay method.
     * @throws DrawRuntimeException cannot happen
     */
    @Test
    public void testProjectRayTransition() throws DrawRuntimeException
    {
        List<Point2d> innerDesignLinePoints = new ArrayList<>();
        List<Point2d> outerDesignLinePoints = new ArrayList<>();
        // Approximate a quarter circle with radius 5
        double innerRadius = 5;
        // Approximate a quarter circle with radius 8
        double outerRadius = 8;
        for (int degree = 0; degree <= 90; degree++)
        {
            innerDesignLinePoints.add(new Point2d(innerRadius * Math.sin(Math.toRadians(degree)),
                    innerRadius * Math.cos(Math.toRadians(degree))));
            outerDesignLinePoints.add(new Point2d(outerRadius * Math.sin(Math.toRadians(degree)),
                    outerRadius * Math.cos(Math.toRadians(degree))));
        }
        PolyLine2d innerDesignLine = new PolyLine2d(innerDesignLinePoints);
        PolyLine2d outerDesignLine = new PolyLine2d(outerDesignLinePoints);
        List<Point2d> transitionLinePoints = new ArrayList<>();
        int degree = 0;
        Point2d prevPoint = innerDesignLinePoints.get(0);
        while (degree < 10)
        {
            double x = innerRadius * Math.sin(Math.toRadians(degree));
            double y = innerRadius * Math.cos(Math.toRadians(degree));
            double direction = prevPoint.directionTo(new Point2d(x, y));
            Ray2d ray = new Ray2d(x, y, direction);
            transitionLinePoints.add(ray);
            prevPoint = ray;
            degree++;
        }
        while (degree <= 80)
        {
            double phase = Math.PI * (degree - 10) / 70;
            double radius = innerRadius + (outerRadius - innerRadius) * (1 - Math.cos(phase) / 2 - 0.5);
            double x = radius * Math.sin(Math.toRadians(degree));
            double y = radius * Math.cos(Math.toRadians(degree));
            double direction = prevPoint.directionTo(new Point2d(x, y));
            Ray2d ray = new Ray2d(x, y, direction);
            transitionLinePoints.add(ray);
            prevPoint = ray;
            degree++;
        }
        while (degree < 90)
        {
            double x = outerRadius * Math.sin(Math.toRadians(degree));
            double y = outerRadius * Math.cos(Math.toRadians(degree));
            double direction = prevPoint.directionTo(new Point2d(x, y));
            Ray2d ray = new Ray2d(x, y, direction);
            transitionLinePoints.add(ray);
            prevPoint = ray;
            degree++;
        }
        PolyLine2d transitionLine = new PolyLine2d(transitionLinePoints);
        // System.out.print("inner design line: " + innerDesignLine.toPlot());
        // System.out.print("outer design line: " + outerDesignLine.toPlot());
        // System.out.print("transition line: " + transitionLine.toPlot());
        List<Point2d> projections = new ArrayList<>();
        for (Iterator<Point2d> iterator = transitionLine.getPoints(); iterator.hasNext();)
        {
            Point2d p = iterator.next();
            if (p instanceof Ray2d)
            {
                Ray2d ray = (Ray2d) p;
                Point2d transitionLinePoint = new Point2d(ray.x, ray.y);
                projections.add(transitionLinePoint);
                double location = innerDesignLine.projectRay(ray);
                if (!Double.isNaN(location))
                {
                    Point2d projection = innerDesignLine.getLocation(location);
                    projections.add(new Point2d(projection.x, projection.y));
                    projections.add(transitionLinePoint);
                }
                location = outerDesignLine.projectRay(ray);
                if (!Double.isNaN(location))
                {
                    Point2d projection = outerDesignLine.getLocation(location);
                    projections.add(new Point2d(projection.x, projection.y));
                    projections.add(transitionLinePoint);
                }
            }
        }
        // System.out.print("cosine projections: " + PolyLine2d.createAndCleanPolyLine2d(projections).toPlot());
        Ray2d from = new Ray2d(outerDesignLine.get(10).x, outerDesignLine.get(10).y,
                outerDesignLine.get(10).directionTo(outerDesignLine.get(11)));
        Ray2d to = new Ray2d(innerDesignLine.get(80).x, innerDesignLine.get(80).y,
                innerDesignLine.get(80).directionTo(innerDesignLine.get(81)));
        transitionLine = Bezier.cubic(from, to);
        // System.out.print("Bezier: " + transitionLine.toPlot());
        projections = new ArrayList<>();
        Point2d prev = null;
        for (Iterator<Point2d> iterator = transitionLine.getPoints(); iterator.hasNext();)
        {
            Point2d p = iterator.next();
            if (prev != null)
            {
                Ray2d ray = new Ray2d(prev, prev.directionTo(p));
                Point2d transitionLinePoint = new Point2d(ray.x, ray.y);
                projections.add(transitionLinePoint);
                double location = innerDesignLine.projectRay(ray);
                if (!Double.isNaN(location))
                {
                    innerDesignLine.getLocation(location);
                    Point2d projection = innerDesignLine.getLocation(location);
                    projections.add(new Point2d(projection.x, projection.y));
                    projections.add(transitionLinePoint);
                }
                location = outerDesignLine.projectRay(ray);
                if (!Double.isNaN(location))
                {
                    outerDesignLine.getLocation(location);
                    Point2d projection = outerDesignLine.getLocation(location);
                    projections.add(new Point2d(projection.x, projection.y));
                    projections.add(transitionLinePoint);
                }
            }
            prev = p;
        }
        // System.out.print("Bezier projections: " + PolyLine2d.createAndCleanPolyLine2d(projections).toPlot());
    }

    /**
     * Test the projectRay method.
     */
    @Test
    public void testProjectRay()
    {
        PolyLine2d reference = new PolyLine2d(new Point2d(0, 1), new Point2d(5, 1), new Point2d(10, 6), new Point2d(20, 6));
        // System.out.print("reference line is " + reference.toPlot());
        PolyLine2d offsetLine = reference.offsetLine(-10);
        // Now we have a line with a somewhat smooth 45 degree curve around (5, 1) with radius 10
        // System.out.print("offset line is " + offsetLine.toPlot());
        double slope = 0.25;
        double slopeAngle = Math.atan2(slope, 1);
        double prevProjection = -100;
        List<Point2d> projections = new ArrayList<>();
        for (double x = -0.5; x < 19; x += 0.25)
        {
            double y = -5 + x * slope;
            Ray2d ray = new Ray2d(x, y, slopeAngle);
            projections.add(ray);
            double projectionLocation = offsetLine.projectRay(ray);
            if (Double.isNaN(projectionLocation))
            {
                offsetLine.projectRay(ray);
            }
            assertFalse("There is a projection", Double.isNaN(projectionLocation));
            Point2d projectedPoint = offsetLine.getLocation(projectionLocation);
            // System.out.println(String.format("DirectedPoint %s projects on line at %.3f. which is at %s", ray,
            // projectionLocation, projectedPoint));
            projections.add(projectedPoint);
            projections.add(ray); // And back to ray
            assertTrue("projection increases monotonous", projectionLocation > prevProjection);
            prevProjection = projectionLocation;
        }
        // System.out.print("projections: " + new PolyLine2d(projections).toPlot());
        projections.clear();
        prevProjection = -100;
        for (double x = 1.5; x < 21; x += 0.25)
        {
            double y = -15 + x * slope;
            Ray2d ray = new Ray2d(x, y, slopeAngle);
            double projectionLocation = offsetLine.projectRay(ray);
            if (Double.isNaN(projectionLocation))
            {
                System.out.println("x " + x + " gives NaN result");
                continue;
            }
            projections.add(ray);
            Point2d projectedPoint = offsetLine.getLocation(projectionLocation);
            // System.out.println(String.format("DirectedPoint %s projects on line at %.3f. which is at %s", ray,
            // projectionLocation, projectedPoint));
            projections.add(projectedPoint);
            projections.add(ray); // And back to ray
            assertTrue("projection increases monotonous", projectionLocation > prevProjection);
            prevProjection = projectionLocation;
        }
        // System.out.print("projections: " + new PolyLine2d(projections).toPlot());
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point2d[] points =
                new Point2d[] { new Point2d(123.456, 345.678), new Point2d(234.567, 456.789), new Point2d(-12.345, -34.567) };
        PolyLine2d pl = new PolyLine2d(points);
        String[] out = pl.toExcel().split("\\n");
        assertEquals("Excel output consists of one line per point", points.length, out.length);
        for (int index = 0; index < points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals("each line consists of two fields", 2, fields.length);
            try
            {
                double x = Double.parseDouble(fields[0].trim());
                assertEquals("x matches", points[index].x, x, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("First field " + fields[0] + " does not parse as a double");
            }
            try
            {
                double y = Double.parseDouble(fields[1].trim());
                assertEquals("y matches", points[index].y, y, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[1] + " does not parse as a double");
            }
        }

        out = pl.toPlot().split(" L");
        assertEquals("Plotter output consists of one coordinate pair per point", points.length, out.length);
        for (int index = 0; index < points.length; index++)
        {
            String[] fields = out[index].split(",");
            assertEquals("each line consists of two fields", 2, fields.length);
            if (index == 0)
            {
                assertTrue(fields[0].startsWith("M"));
                fields[0] = fields[0].substring(1);
            }
            try
            {
                double x = Double.parseDouble(fields[0].trim());
                assertEquals("x matches", points[index].x, x, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("First field " + fields[0] + " does not parse as a double");
            }
            try
            {
                double y = Double.parseDouble(fields[1].trim());
                assertEquals("y matches", points[index].y, y, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[1] + " does not parse as a double");
            }
        }
    }

    /**
     * Verify that a Line2d contains the same points as an array of Point2d.
     * @param line Line2d; the OTS line
     * @param points Point2d[]; the OTSPoint array
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    private void verifyPointsAndSegments(final PolyLine2d line, final Point2d[] points) throws DrawRuntimeException
    {
        assertEquals("Line should have same number of points as point array", line.size(), points.length);
        for (int i = 0; i < points.length; i++)
        {
            assertEquals("x of point i should match", points[i].x, line.get(i).x, Math.ulp(points[i].x));
            assertEquals("y of point i should match", points[i].y, line.get(i).y, Math.ulp(points[i].y));
            assertEquals("x of point i should match", points[i].x, line.getX(i), Math.ulp(points[i].x));
            assertEquals("y of point i should match", points[i].y, line.getY(i), Math.ulp(points[i].y));
            if (i < points.length - 1)
            {
                LineSegment2d segment = line.getSegment(i);
                assertEquals("begin x of line segment i should match", points[i].x, segment.startX, Math.ulp(points[i].x));
                assertEquals("begin y of line segment i should match", points[i].y, segment.startY, Math.ulp(points[i].y));
                assertEquals("end x of line segment i should match", points[i + 1].x, segment.endX, Math.ulp(points[i + 1].x));
                assertEquals("end y of line segment i should match", points[i + 1].y, segment.endY, Math.ulp(points[i + 1].y));
            }
            else
            {
                try
                {
                    line.getSegment(i);
                    fail("Too large index should have thrown a DrawRuntimeException");
                }
                catch (DrawRuntimeException dre)
                {
                    // Ignore expected exception
                }

                try
                {
                    line.getSegment(-1);
                    fail("Negative index should have thrown a DrawRuntimeException");
                }
                catch (DrawRuntimeException dre)
                {
                    // Ignore expected exception
                }

            }
        }
    }

    /**
     * Test the transitionLine method.
     */
    @Test
    public void testTransitionLine()
    {
        // Create a Bezier with a 90 degree change of direction starting in X direction, ending in Y direction
        PolyLine2d bezier = Bezier.cubic(64, new Ray2d(-5, 0, 0, 0), new Ray2d(0, 5, 0, 7));
        // System.out.print("c1,0,0" + bezier1.project().toPlot());
        double length = bezier.getLength();
        double prevDir = Double.NaN;
        for (int step = 0; step <= 1000; step++)
        {
            double distance = length * step / 1000;
            Ray2d ray = bezier.getLocation(distance);
            double direction = Math.toDegrees(ray.phi);
            if (step > 0)
            {
                assertEquals("phi changes very little at step " + step, prevDir, direction, 2);
            }
            prevDir = Math.toDegrees(ray.phi);
        }
        // Make a gradually transitioning offset line
        PolyLine2d transitioningOffsetLine = bezier.offsetLine(0, 2);
        // Verify that this curve is fairly smooth
        length = transitioningOffsetLine.getLength();
        prevDir = Double.NaN;
        for (int step = 0; step <= 1000; step++)
        {
            double distance = length * step / 1000;
            Ray2d ray = transitioningOffsetLine.getLocation(distance);
            double direction = Math.toDegrees(ray.phi);
            if (step > 0)
            {
                assertEquals("phi changes very little at step " + step, prevDir, direction, 2);
            }
            prevDir = Math.toDegrees(ray.phi);
        }
        PolyLine2d endLine = bezier.offsetLine(-2);
        // System.out.print("c0,1,0" + endLine.project().toPlot());
        TransitionFunction transitionFunction = new TransitionFunction()
        {
            @Override
            public double function(final double fraction)
            {
                return 0.5 - Math.cos(fraction * Math.PI) / 2;
            }
        };
        PolyLine2d cosineSmoothTransitioningLine = bezier.transitionLine(endLine, transitionFunction);
        // System.out.print("c0,0,0" + cosineSmoothTransitioningLine.project().toPlot());
        length = cosineSmoothTransitioningLine.getLength();
        prevDir = Double.NaN;
        for (int step = 0; step <= 1000; step++)
        {
            double distance = length * step / 1000;
            Ray2d ray = cosineSmoothTransitioningLine.getLocation(distance);
            double direction = Math.toDegrees(ray.phi);
            if (step > 0)
            {
                assertEquals("phi changes very little at step " + step, prevDir, direction, 4);
            }
            prevDir = Math.toDegrees(ray.phi);
        }
        // System.out.print(
        // "c0,0,1" + Bezier.cubic(bezier1.getLocationFraction(0), endLine.getLocationFraction(1)).project().toPlot());
        // Reverse the lines
        PolyLine2d cosineSmoothTransitioningLine2 =
                endLine.reverse().transitionLine(bezier.reverse(), transitionFunction).reverse();
        // Check that those lines are very similar
        assertEquals("Lengths are equal", cosineSmoothTransitioningLine.getLength(), cosineSmoothTransitioningLine2.getLength(),
                0.001);
        for (int step = 0; step <= 1000; step++)
        {
            Ray2d ray1 = cosineSmoothTransitioningLine.getLocation(step * cosineSmoothTransitioningLine.getLength() / 1000);
            Ray2d ray2 = cosineSmoothTransitioningLine2.getLocation(step * cosineSmoothTransitioningLine2.getLength() / 1000);
            assertEquals("rays are almost equal in x", ray1.x, ray2.x, 0.001);
            assertEquals("rays are almost equal in y", ray1.y, ray2.y, 0.001);
            assertEquals("rays are almost equal in phi", ray1.phi, ray2.phi, 0.0001);
        }

        assertEquals("offset by zero returns original", bezier, bezier.offsetLine(0, 0));
        assertEquals("offset by constant with two arguments returns same as offset with one argument", bezier.offsetLine(3, 3),
                bezier.offsetLine(3));
    }

    /**
     * Test the filtering constructors.
     * @throws DrawRuntimeException should never happen
     */
    @Test
    public final void filterTest() throws DrawRuntimeException
    {
        Point2d[] tooShort = new Point2d[] {};
        try
        {
            new PolyLine2d(true, tooShort);
            fail("Array with no points should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        tooShort = new Point2d[] { new Point2d(1, 2) };
        try
        {
            new PolyLine2d(true, tooShort);
            fail("Array with one point should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        Point2d p0 = new Point2d(1, 2);
        Point2d p1 = new Point2d(4, 5);
        Point2d[] points = new Point2d[] { p0, p1 };
        PolyLine2d result = new PolyLine2d(true, points);
        assertTrue("first point is p0", p0.equals(result.get(0)));
        assertTrue("second point is p1", p1.equals(result.get(1)));
        Point2d p1Same = new Point2d(4, 5);
        result = new PolyLine2d(true, new Point2d[] { p0, p0, p0, p0, p1Same, p0, p1, p1, p1Same, p1, p1 });
        assertEquals("result should contain 4 points", 4, result.size());
        assertTrue("first point is p0", p0.equals(result.get(0)));
        assertTrue("second point is p1", p1.equals(result.get(1)));
        assertTrue("third point is p0", p0.equals(result.get(0)));
        assertTrue("last point is p1", p1.equals(result.get(1)));
        new PolyLine2d(true, new Point2d[] { p0, new Point2d(1, 3) });

        try
        {
            PolyLine2d.cleanPoints(true, null);
            fail("null iterator should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine2d.cleanPoints(true, new Iterator<Point2d>()
            {
                @Override
                public boolean hasNext()
                {
                    return false;
                }

                @Override
                public Point2d next()
                {
                    return null;
                }
            });
            fail("Iterator that has no data should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        Iterator<Point2d> iterator =
                PolyLine2d.cleanPoints(true, Arrays.stream(new Point2d[] { new Point2d(1, 2) }).iterator());
        iterator.next(); // should work
        assertFalse("iterator should now be out of data", iterator.hasNext());
        try
        {
            iterator.next();
            fail("Iterator that has no nore data should have thrown a NoSuchElementException");
        }
        catch (NoSuchElementException nse)
        {
            // Ignore expected exception
        }

        // Check that cleanPoints with false indeed does not filter
        iterator = PolyLine2d.cleanPoints(false,
                Arrays.stream(new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(1, 2) }).iterator());
        assertTrue("iterator has initial point", iterator.hasNext());
        iterator.next();
        assertTrue("iterator has second point", iterator.hasNext());
        iterator.next();
        assertTrue("iterator has second point", iterator.hasNext());
        iterator.next();
        assertFalse("iterator has no more data", iterator.hasNext());
    }

    /**
     * Test the hashCode and Equals methods.
     * @throws DrawRuntimeException when that happens uncaught; this test has failed
     * @throws NullPointerException when that happens uncaught; this test has failed
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testToStringHashCodeAndEquals() throws NullPointerException, DrawRuntimeException
    {
        PolyLine2d line = new PolyLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(4, 6), new Point2d(8, 9) });
        assertTrue("toString returns something descriptive", line.toString().startsWith("PolyLine2d ["));
        assertTrue("toString can suppress the class name", line.toString().indexOf(line.toString(true)) > 0);

        // Verify that hashCode. Check that the result depends on the actual coordinates.
        assertNotEquals("hash code takes x coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(1, 0), new Point2d(1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 1), new Point2d(1, 1)).hashCode());
        assertNotEquals("hash code takes x coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 0), new Point2d(2, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 2)).hashCode());

        // Verify the equals method.
        assertTrue("line is equal to itself", line.equals(line));
        assertFalse("line is not equal to a different line",
                line.equals(new PolyLine3d(new Point3d(123, 456, 789), new Point3d(789, 101112, 2))));
        assertFalse("line is not equal to null", line.equals(null));
        assertFalse("line is not equal to a different kind of object", line.equals("unlikely"));
        assertEquals("equals verbatim copy", line,
                new PolyLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(4, 6), new Point2d(8, 9) }));
        assertNotEquals("equals checks x", line,
                new PolyLine2d(new Point2d[] { new Point2d(2, 2), new Point2d(4, 6), new Point2d(8, 9) }));
        assertNotEquals("equals checks y", line,
                new PolyLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(4, 7), new Point2d(8, 9) }));
        assertTrue("Line is equal to line from same set of points", line.equals(new PolyLine2d(line.getPoints())));
    }

    /**
     * Problem with limited precision when getting location almost at end.
     * @throws DrawRuntimeException when that happens this test has triggered the problem
     */
    @Test
    public void testOTS2Problem() throws DrawRuntimeException
    {
        PolyLine2d line = new PolyLine2d(new Point2d(100, 0), new Point2d(100.1, 0));
        double length = line.getLength();
        line.getLocation(length - Math.ulp(length));
    }

}
