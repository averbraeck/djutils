package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * TestLine2d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TestPolyLine2d
{

    /**
     * Test the constructors of PolyLine2d.
     * @throws DrawException on failure
     */
    @Test
    public final void constructorsTest() throws DrawException
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
     * Test all the constructors of Point3d.
     * @param points Point3d[]; array of Point3d to test with
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    private void runConstructors(final Point2d[] points) throws DrawException
    {
        verifyPoints(new PolyLine2d(points), points);
        List<Point2d> list = new ArrayList<>();
        for (int i = 0; i < points.length; i++)
        {
            list.add(points[i]);
        }
        PolyLine2d line = new PolyLine2d(list);
        verifyPoints(line, points);
        verifyPoints(new PolyLine2d(line.getPoints()), points);
        assertEquals("length at index 0", 0.0, line.lengthAtIndex(0), 0);
        double length = 0;
        for (int i = 1; i < points.length; i++)
        {
            length += Math.sqrt(Math.pow(points[i].getX() - points[i - 1].getX(), 2)
                    + Math.pow(points[i].getY() - points[i - 1].getY(), 2));
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
        path.moveTo(points[0].getX(), points[0].getY());
        // System.out.print("path is "); printPath2D(path);
        for (int i = 1; i < points.length; i++)
        {
            // Path2D is corrupt if same point is added twice in succession
            if (points[i].getX() != points[i - 1].getX() || points[i].getY() != points[i - 1].getY())
            {
                path.lineTo(points[i].getX(), points[i].getY());
                horizontalMoves++;
            }
        }
        try
        {
            line = new PolyLine2d(path);
            if (0 == horizontalMoves)
            {
                fail("Construction of Line3d from path with degenerate projection should have failed");
            }
            // This new Line3d has z=0 for all points so veryfyPoints won't work
            assertEquals("number of points should match", horizontalMoves + 1, line.size());
            int indexInLine = 0;
            for (int i = 0; i < points.length; i++)
            {
                if (i > 0 && (points[i].getX() != points[i - 1].getX() || points[i].getY() != points[i - 1].getY()))
                {
                    indexInLine++;
                }
                assertEquals("x in line", points[i].getX(), line.get(indexInLine).getX(), 0.001);
                assertEquals("y in line", points[i].getY(), line.get(indexInLine).getY(), 0.001);
            }
        }
        catch (DrawException e)
        {
            if (0 != horizontalMoves)
            {
                fail("Construction of Line3d from path with non-degenerate projection should not have failed");
            }
        }
    }

    /**
     * Test construction of a Line3d from a Path2D with SEG_CLOSE.
     * @throws DrawException on unexpected error
     */
    @Test
    public void testPathWithClose() throws DrawException
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
        catch (DrawException de)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test all constructors of a Line2d.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws DrawException if that happens uncaught; this test has failed
     */
    @Test
    public void testConstructors() throws DrawRuntimeException, DrawException
    {
        runConstructors(new Point2d[] { new Point2d(1.2, 3.4), new Point2d(2.3, 4.5), new Point2d(3.4, 5.6) });
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
     * Test the other methods of Line2d.
     * @throws DrawException if that happens uncaught; this test has failed
     * @throws NullPointerException if that happens uncaught; this test has failed
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testOtherMethods() throws NullPointerException, DrawException
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
        line = PolyLine2d.createAndCleanLine2d(array);
        assertEquals("cleaned line has 2 points", 2, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4), new Point2d(3, 4) };
        line = PolyLine2d.createAndCleanLine2d(array);
        assertEquals("cleaned line has 2 points", 2, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point2d[] { new Point2d(0, -1), new Point2d(1, 2), new Point2d(1, 2), new Point2d(3, 4) };
        line = PolyLine2d.createAndCleanLine2d(array);
        assertEquals("cleaned line has 2 points", 3, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point2d[] { new Point2d(0, -1), new Point2d(1, 2), new Point2d(1, 2), new Point2d(1, 2),
                new Point2d(3, 4) };
        line = PolyLine2d.createAndCleanLine2d(array);
        assertEquals("cleaned line has 3 points", 3, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("mid point", array[1], line.get(1));
        assertEquals("last point", array[array.length - 1], line.getLast());

        try
        {
            PolyLine2d.createAndCleanLine2d(new Point2d[0]);
            fail("Too short array should have thrown a DrawException");
        }
        catch (DrawException de)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine2d.createAndCleanLine2d(new Point2d[] { new Point2d(1, 2) });
            fail("Too short array should have thrown a DrawException");
        }
        catch (DrawException de)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine2d.createAndCleanLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(1, 2) });
            fail("All duplicate points in array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine2d.createAndCleanLine2d(new Point2d[] { new Point2d(1, 2), new Point2d(1, 2), new Point2d(1, 2) });
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
            fail("negative location should have thrown a DrawException");
        }
        catch (DrawException de)
        {
            // Ignore expected exception
        }

        double length = line.getLength();
        assertEquals("Length of line is 10", 10, length, 0.000001);

        try
        {
            line.getLocation(length + 0.1);
            fail("location beyond length should have thrown a DrawException");
        }
        catch (DrawException de)
        {
            // Ignore expected exception
        }

        for (double position : new double[] { -1, 0, 2.5, 4.9, 5.1, 7.5, 9.9, 10, 11 })
        {
            DirectedPoint2d dp = line.getLocationExtended(position);
            if (position < 5)
            {
                DirectedPoint2d expected = new DirectedPoint2d(array[0].interpolate(array[1], position / 5), Math.atan2(4, 3));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(dp, 0.0001, 0.00001));
            }
            else
            {
                DirectedPoint2d expected =
                        new DirectedPoint2d(array[1].interpolate(array[2], (position - 5) / 5), Math.atan2(3, 4));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(dp, 0.0001, 0.00001));
            }
        }

        // Test the projectOrthogonal method
        array = new Point2d[] { new Point2d(1, 2), new Point2d(4, 6), new Point2d(8, 9) };
        line = new PolyLine2d(array);
        // Verify that any projection ends up somewhere on the line
        for (double x : new double[] { -10, 0, 2, 4, 6, 8, 10, 20 })
        {
            for (double y : new double[] { -10, 0, 2, 4, 6, 8, 10, 20 })
            {
                Point2d xy = new Point2d(x, y);
                double result = line.projectOrthogonal(x, y);
                assertTrue("result must be >= 0.0", result >= 0);
                assertTrue("result must be <= 1.0", result <= 1.0);
                Point2d resultPoint = line.getLocationFraction(result);
                double distance = resultPoint.distance(xy);
                // We should not be able to find a point on the line that is closer to xy than resultPoint
                // Just walk the line in 100 small steps
                for (int step = 0; step <= 100; step++)
                {
                    Point2d checkPoint = line.getLocationFraction(step / 100.0);
                    double actualDistance = checkPoint.distance(xy);
                    assertTrue("No point along the line is closer than resultPoint (except for a rounding error)",
                            distance <= actualDistance + 0.000001);
                }
            }
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
                    fail("illegal truncate should have thrown a DrawException");
                }
                catch (DrawException de)
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
                        fail("Illegal range should have thrown a DrawException");
                    }
                    catch (DrawException de)
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
            fail("NaN value should have thrown a DrawException");
        }
        catch (DrawException de)
        {
            // Ignore expected exception
        }

        try
        {
            line.extract(0.0, Double.NaN);
            fail("NaN value should have thrown a DrawException");
        }
        catch (DrawException de)
        {
            // Ignore expected exception
        }

        assertTrue("toString returns something descriptive", line.toString().startsWith("PolyLine2d ["));

        // Verify that hashCode. Check that the result depends on the actual coordinates.
        assertNotEquals("hash code takes x coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(1, 0), new Point2d(1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 1), new Point2d(1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 0), new Point2d(2, 1)).hashCode());
        assertNotEquals("hash code takes x coordinate into account",
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 1)).hashCode(),
                new PolyLine2d(new Point2d(0, 0), new Point2d(1, 2)).hashCode());

        // Verify the equals method.
        assertTrue("line is equal to itself", line.equals(line));
        assertFalse("line is not equal to a different line",
                line.equals(new PolyLine2d(new Point2d(123, 456), new Point2d(789, 101112))));
        assertFalse("line is not equal to null", line.equals(null));
        assertFalse("line is not equal to a different kind of object", line.equals("unlikely"));
        assertTrue("Line is equal to line from same set of points", line.equals(new PolyLine2d(line.getPoints())));
    }

    /**
     * Verify that a Line3d contains the same points as an array of Point3d.
     * @param line Line3d; the OTS line
     * @param points Point3d[]; the OTSPoint array
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    private void verifyPoints(final PolyLine2d line, final Point2d[] points) throws DrawException
    {
        assertEquals("Line should have same number of points as point array", line.size(), points.length);
        for (int i = 0; i < points.length; i++)
        {
            assertEquals("x of point i should match", points[i].getX(), line.get(i).getX(), Math.ulp(points[i].getX()));
            assertEquals("y of point i should match", points[i].getY(), line.get(i).getY(), Math.ulp(points[i].getY()));
        }
    }

}
