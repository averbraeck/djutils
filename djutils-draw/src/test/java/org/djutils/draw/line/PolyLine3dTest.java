package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.Export;
import org.djutils.draw.InvalidProjectionException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.junit.jupiter.api.Test;

/**
 * TestLine3d.java.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class PolyLine3dTest
{
    /**
     * Test the constructors of PolyLine3d.
     */
    @Test
    public final void constructorsTest()
    {
        double[] values = {-999, 0, 99, 9999}; // Keep this list short; execution time grows with 9th power of length
        Point3d[] points = new Point3d[0]; // Empty array
        try
        {
            runConstructors(points);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        for (double x0 : values)
        {
            for (double y0 : values)
            {
                for (double z0 : values)
                {
                    points = new Point3d[1]; // Degenerate array holding one point
                    points[0] = new Point3d(x0, y0, z0);
                    try
                    {
                        runConstructors(points);
                        fail("Should have thrown an IllegalArgumentException");
                    }
                    catch (IllegalArgumentException e)
                    {
                        // Ignore expected exception
                    }
                    for (double x1 : values)
                    {
                        for (double y1 : values)
                        {
                            for (double z1 : values)
                            {
                                points = new Point3d[2]; // Straight line; two points
                                points[0] = new Point3d(x0, y0, z0);
                                points[1] = new Point3d(x1, y1, z1);
                                if (0 == points[0].distance(points[1]))
                                {
                                    try
                                    {
                                        runConstructors(points);
                                        fail("Should have thrown an IllegalArgumentException");
                                    }
                                    catch (IllegalArgumentException e)
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
                                            for (double z2 : values)
                                            {
                                                points = new Point3d[3]; // Line with intermediate point
                                                points[0] = new Point3d(x0, y0, z0);
                                                points[1] = new Point3d(x1, y1, z1);
                                                points[2] = new Point3d(x2, y2, z2);
                                                if (0 == points[1].distance(points[2]))
                                                {
                                                    try
                                                    {
                                                        runConstructors(points);
                                                        fail("Should have thrown an IllegalArgumentException");
                                                    }
                                                    catch (IllegalArgumentException e)
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
            }
        }
    }

    /**
     * Test all the constructors of PolyLine3d.
     * @param points array of Point3d to test with
     * @throws RuntimeException should not happen; this test has failed if it does happen
     */
    private void runConstructors(final Point3d[] points) throws RuntimeException
    {
        verifyPoints(new PolyLine3d(points), points);
        List<Point3d> list = new ArrayList<>();
        for (int i = 0; i < points.length; i++)
        {
            list.add(points[i]);
        }
        PolyLine3d line = new PolyLine3d(list);
        verifyPoints(line, points);
        // Convert it to Point3d[], create another Line3d from that and check that
        verifyPoints(new PolyLine3d(line.iterator()), points);
        assertEquals(0.0, line.lengthAtIndex(0), 0, "length at index 0");
        double length = 0;
        for (int i = 1; i < points.length; i++)
        {
            length += Math.sqrt(Math.pow(points[i].x - points[i - 1].x, 2) + Math.pow(points[i].y - points[i - 1].y, 2)
                    + Math.pow(points[i].z - points[i - 1].z, 2));
            assertEquals(length, line.lengthAtIndex(i), 0.0001, "length at index");
        }
        assertEquals(length, line.getLength(), 10 * Math.ulp(length), "length");

        assertEquals(points.length, line.size(), "size");

        Bounds3d b3d = line.getAbsoluteBounds();
        Bounds3d ref = new Bounds3d(points);
        assertEquals(ref, b3d, "bounds is correct");

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

        try
        {
            new PolyLine3d((List<Point3d>) null);
            fail("null list should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        // Construct a Path3D.Double that contains the horizontal moveto or lineto
        Path2D path = new Path2D.Double();
        path.moveTo(points[0].x, points[0].y);
        // System.out.print("path is "); printPath2D(path);
        for (int i = 1; i < points.length; i++)
        {
            // Path3D is corrupt if same point is added twice in succession
            if (points[i].x != points[i - 1].x || points[i].y != points[i - 1].y)
            {
                path.lineTo(points[i].x, points[i].y);
            }
        }
    }

    /**
     * Print a Path2D to the console.
     * @param path the path
     */
    public final void printPath2D(final Path2D path)
    {
        PathIterator pi = path.getPathIterator(null);
        double[] p = new double[6];
        while (!pi.isDone())
        {
            int segType = pi.currentSegment(p);
            if (segType == PathIterator.SEG_MOVETO)
            {
                System.out.print(" move to " + new Point3d(p[0], p[1], 0.0));
            }
            if (segType == PathIterator.SEG_LINETO)
            {
                System.out.print(" line to " + new Point3d(p[0], p[1], 0.0));
            }
            else if (segType == PathIterator.SEG_CLOSE)
            {
                System.out.print(" close");
            }
            pi.next();
        }
        System.out.println("");
    }

    /**
     * Verify that a Line3d contains the same points as an array of Point3d.
     * @param line the OTS line
     * @param points the OTSPoint array
     */
    private void verifyPoints(final PolyLine3d line, final Point3d[] points)
    {
        assertEquals(line.size(), points.length, "Line should have same number of points as point array");
        for (int i = 0; i < points.length; i++)
        {
            assertEquals(points[i].x, line.get(i).x, Math.ulp(points[i].x), "x of point i should match");
            assertEquals(points[i].y, line.get(i).y, Math.ulp(points[i].y), "y of point i should match");
            assertEquals(points[i].z, line.get(i).z, Math.ulp(points[i].z), "z of point i should match");
            assertEquals(points[i].x, line.getX(i), Math.ulp(points[i].x), "x of point i should match");
            assertEquals(points[i].y, line.getY(i), Math.ulp(points[i].y), "y of point i should match");
            assertEquals(points[i].z, line.getZ(i), Math.ulp(points[i].z), "z of point i should match");
            if (i < points.length - 1)
            {
                LineSegment3d segment = line.getSegment(i);
                assertEquals(points[i].x, segment.startX, Math.ulp(points[i].x), "begin x of line segment i should match");
                assertEquals(points[i].y, segment.startY, Math.ulp(points[i].y), "begin y of line segment i should match");
                assertEquals(points[i].z, segment.startZ, Math.ulp(points[i].z), "begin z of line segment i should match");
                assertEquals(points[i + 1].x, segment.endX, Math.ulp(points[i + 1].x), "end x of line segment i should match");
                assertEquals(points[i + 1].y, segment.endY, Math.ulp(points[i + 1].y), "end y of line segment i should match");
                assertEquals(points[i + 1].z, segment.endZ, Math.ulp(points[i + 1].z), "end z of line segment i should match");
            }
            else
            {
                try
                {
                    line.getSegment(i);
                    fail("Too large index should have thrown an IndexOutOfBoundsException");
                }
                catch (IndexOutOfBoundsException e)
                {
                    // Ignore expected exception
                }

                try
                {
                    line.getSegment(-1);
                    fail("Negative index should have thrown a IndexOutOfBoundsException");
                }
                catch (IndexOutOfBoundsException e)
                {
                    // Ignore expected exception
                }

            }
        }
    }

    /**
     * Test all constructors of a Line2d.
     */
    @Test
    public void testConstructors()
    {
        runConstructors(new Point3d[] {new Point3d(1.2, 3.4, 5.5), new Point3d(2.3, 4.5, 6.6), new Point3d(3.4, 5.6, 7.7)});

        try
        {
            new PolyLine3d(new double[] {1, 2, 3}, new double[] {4, 5, 6}, new double[] {7, 8});
            fail("double arrays of unequal length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] {1, 2, 3}, new double[] {4, 5}, new double[] {7, 8, 9});
            fail("double arrays of unequal length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] {1, 2}, new double[] {4, 5, 6}, new double[] {7, 8, 9});
            fail("double arrays of unequal length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(null, new double[] {1, 2}, new double[] {3, 4});
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] {1, 2}, null, new double[] {5, 6});
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] {1, 2}, new double[] {3, 4}, null);
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d((List<Point3d>) null);
            fail("null list should have thrown a nullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        List<Point3d> shortList = new ArrayList<>();
        try
        {
            new PolyLine3d(shortList);
            fail("empty list should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        shortList.add(new Point3d(1, 2, 3));
        try
        {
            new PolyLine3d(shortList);
            fail("one-point list should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        Point3d p1 = new Point3d(1, 2, 3);
        Point3d p2 = new Point3d(3, 4, 5);
        PolyLine3d pl = new PolyLine3d(p1, p2);
        assertEquals(2, pl.size(), "two points");
        assertEquals(p1, pl.get(0), "p1");
        assertEquals(p2, pl.get(1), "p2");

        pl = new PolyLine3d(p1, p2, (Point3d[]) null);
        assertEquals(2, pl.size(), "two points");
        assertEquals(p1, pl.get(0), "p1");
        assertEquals(p2, pl.get(1), "p2");

        pl = new PolyLine3d(p1, p2, new Point3d[0]);
        assertEquals(2, pl.size(), "two points");
        assertEquals(p1, pl.get(0), "p1");
        assertEquals(p2, pl.get(1), "p2");

        try
        {
            new PolyLine3d(new Point3d[] {});
            fail("empty array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3)});
            fail("single point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3)});
            fail("duplicate point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5)});
            fail("duplicate point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(
                    new Point3d[] {new Point3d(-1, -2, -3), new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5)});
            fail("duplicate point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test that exception is thrown when it should be.
     */
    @Test
    public final void exceptionTest()
    {
        PolyLine3d line = new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 5, 6)});
        try
        {
            line.get(-1);
            fail("Should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            line.get(2);
            fail("Should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the getLocationExtended method and friends.
     */
    @Test
    public final void locationExtendedTest()
    {
        Point3d p0 = new Point3d(10, 20, 30);
        Point3d p1 = new Point3d(40, 50, 60);
        Point3d p2 = new Point3d(90, 80, 70);
        PolyLine3d polyLine = new PolyLine3d(new Point3d[] {p0, p1, p2});
        double expectedDirZ1 = Math.atan2(p1.y - p0.y, p1.x - p0.x);
        double expectedDirY1 = Math.atan2(Math.hypot(p1.x - p0.x, p1.y - p0.y), p1.z - p0.z);
        checkGetLocation(polyLine, -10, null, expectedDirY1, expectedDirZ1);
        checkGetLocation(polyLine, -0.0001, p0, expectedDirY1, expectedDirZ1);
        checkGetLocation(polyLine, 0, p0, expectedDirY1, expectedDirZ1);
        checkGetLocation(polyLine, 0.0001, p0, expectedDirY1, expectedDirZ1);
        double expectedDirZ = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        double expectedDirY = Math.atan2(Math.hypot(p2.x - p1.x, p2.y - p1.y), p2.z - p1.z);
        checkGetLocation(polyLine, 0.9999, p2, expectedDirY, expectedDirZ);
        checkGetLocation(polyLine, 1.0, p2, expectedDirY, expectedDirZ);
        checkGetLocation(polyLine, 1.0001, p2, expectedDirY, expectedDirZ);
        checkGetLocation(polyLine, 10, null, expectedDirY, expectedDirZ);
    }

    /**
     * Check the location returned by the various location methods.
     * @param line the line
     * @param fraction relative position to check
     * @param expectedPoint expected location of the result
     * @param expectedDirY expected angle of the result from the Z axis
     * @param expectedDirZ expected angle of the result from the X axis
     */
    private void checkGetLocation(final PolyLine3d line, final double fraction, final Point3d expectedPoint,
            final double expectedDirY, final double expectedDirZ)
    {
        double length = line.getLength();
        checkDirectedPoint3d(line.getLocationExtended(fraction * length), expectedPoint, expectedDirY, expectedDirZ);
        if (fraction < 0 || fraction > 1)
        {
            try
            {
                line.getLocation(fraction * length);
                fail("getLocation should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException e)
            {
                // Ignore expected exception
            }
            try
            {
                line.getLocationFraction(fraction);
                fail("getLocation should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException e)
            {
                // Ignore expected exception
            }
        }
        else
        {
            checkDirectedPoint3d(line.getLocation(fraction * length), expectedPoint, expectedDirY, expectedDirZ);
            checkDirectedPoint3d(line.getLocationFraction(fraction), expectedPoint, expectedDirY, expectedDirZ);
        }

    }

    /**
     * Verify the location and direction of a DirectedPoint3d.
     * @param dp the DirectedPoint3d that should be verified
     * @param expectedPoint the expected location (or null if location should not be checked)
     * @param expectedDirY the expected angle from the Z axis
     * @param expectedDirZ the expected angle from the X axis
     */
    private void checkDirectedPoint3d(final DirectedPoint3d dp, final Point3d expectedPoint, final double expectedDirY,
            final double expectedDirZ)
    {
        if (null != expectedPoint)
        {
            Point3d p = new Point3d(dp.x, dp.y, dp.z);
            assertEquals(0, expectedPoint.distance(p), 0.1, "locationExtended(0) returns approximately expected point");
        }
        assertEquals(expectedDirY, dp.getDirY(), 0.001, "dirY (rotation from Z axis)");
        assertEquals(expectedDirZ, dp.getDirZ(), 0.001, "dirZ (rotation of projection from X axis)");
    }

    /**
     * Test the filtering constructors.
     */
    @Test
    public final void filterTest()
    {
        Point3d[] tooShort = new Point3d[] {};
        try
        {
            new PolyLine3d(tooShort);
            fail("Array with no points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        tooShort = new Point3d[] {new Point3d(1, 2, 3)};
        try
        {
            new PolyLine3d(tooShort);
            fail("Array with one point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        Point3d p0 = new Point3d(1, 2, 3);
        Point3d p1 = new Point3d(4, 5, 6);
        Point3d[] points = new Point3d[] {p0, p1};
        PolyLine3d result = new PolyLine3d(points);
        assertTrue(p0.equals(result.get(0)), "first point is p0");
        assertTrue(p1.equals(result.get(1)), "second point is p1");
        Point3d p1Same = new Point3d(4, 5, 6);
        result = new PolyLine3d(0.0, new Point3d[] {p0, p0, p0, p0, p1Same, p0, p1, p1, p1Same, p1, p1});
        assertEquals(4, result.size(), "result should contain 4 points");
        assertTrue(p0.equals(result.get(0)), "first point is p0");
        assertTrue(p1.equals(result.get(1)), "second point is p1");
        assertTrue(p0.equals(result.get(2)), "third point is p0");
        assertTrue(p1.equals(result.get(3)), "last point is p1");
        new PolyLine3d(new Point3d[] {p0, new Point3d(1, 3, 4)});
        new PolyLine3d(new Point3d[] {p0, new Point3d(1, 2, 4)});
    }

    /**
     * Test the equals method.
     */
    @Test
    public final void equalsTest()
    {
        Point3d p0 = new Point3d(1.1, 2.2, 3.3);
        Point3d p1 = new Point3d(2.1, 2.2, 3.3);
        Point3d p2 = new Point3d(3.1, 2.2, 3.3);

        PolyLine3d line = new PolyLine3d(new Point3d[] {p0, p1, p2});
        assertTrue(line.equals(line), "Line3d is equal to itself");
        assertFalse(line.equals(null), "Line3d is not equal to null");
        assertFalse(line.equals(new Object()), "Line3d is not equals to some other kind of Object");
        PolyLine3d line2 = new PolyLine3d(new Point3d[] {p0, p1, p2});
        assertTrue(line.equals(line2), "Line3d is equal ot other Line3d that has the exact same list of Point3d");
        Point3d p2Same = new Point3d(3.1, 2.2, 3.3);
        line2 = new PolyLine3d(new Point3d[] {p0, p1, p2Same});
        assertTrue(line.equals(line2), "Line3d is equal ot other Line3d that has the exact same list of Point3d; "
                + "even if some of those point are different instances with the same coordinates");
        Point3d p2NotSame = new Point3d(3.1, 2.2, 3.35);
        line2 = new PolyLine3d(new Point3d[] {p0, p1, p2NotSame});
        assertFalse(line.equals(line2), "Line3d is not equal ot other Line3d that differs in one coordinate");
        line2 = new PolyLine3d(new Point3d[] {p0, p1, p2, p2NotSame});
        assertFalse(line.equals(line2),
                "Line3d is not equal ot other Line3d that has more points (but is identical up to the common length)");
        assertFalse(line2.equals(line),
                "Line3d is not equal ot other Line3d that has fewer points  (but is identical up to the common length)");
    }

    /**
     * Test the concatenate method.
     */
    @Test
    public final void concatenateTest()
    {
        Point3d p0 = new Point3d(1.1, 2.2, 3.3);
        Point3d p1 = new Point3d(2.1, 2.2, 3.3);
        Point3d p2 = new Point3d(3.1, 2.2, 3.3);
        Point3d p3 = new Point3d(4.1, 2.2, 3.3);
        Point3d p4 = new Point3d(5.1, 2.2, 3.3);
        Point3d p5 = new Point3d(6.1, 2.2, 3.3);

        PolyLine3d l0 = new PolyLine3d(p0, p1, p2);
        PolyLine3d l1 = new PolyLine3d(p2, p3);
        PolyLine3d l2 = new PolyLine3d(p3, p4, p5);
        PolyLine3d ll = PolyLine3d.concatenate(l0, l1, l2);
        assertEquals(6, ll.size(), "size is 6");
        assertEquals(p0, ll.get(0), "point 0 is p0");
        assertEquals(p1, ll.get(1), "point 1 is p1");
        assertEquals(p2, ll.get(2), "point 2 is p2");
        assertEquals(p3, ll.get(3), "point 3 is p3");
        assertEquals(p4, ll.get(4), "point 4 is p4");
        assertEquals(p5, ll.get(5), "point 5 is p5");

        ll = PolyLine3d.concatenate(l1);
        assertEquals(2, ll.size(), "size is 2");
        assertEquals(p2, ll.get(0), "point 0 is p2");
        assertEquals(p3, ll.get(1), "point 1 is p3");

        try
        {
            PolyLine3d.concatenate(l0, l2);
            fail("Gap should have throw an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            PolyLine3d.concatenate();
            fail("concatenate of empty list should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        // Test concatenate methods with tolerance
        PolyLine3d thirdLine = new PolyLine3d(p4, p5);
        for (double tolerance : new double[] {0.1, 0.01, 0.001, 0.0001, 0.00001})
        {
            for (double actualError : new double[] {tolerance * 0.9, tolerance * 1.1})
            {
                int maxDirection = 10;
                for (int direction = 0; direction < maxDirection; direction++)
                {
                    double dx = actualError * Math.cos(Math.PI * 2 * direction / maxDirection);
                    double dy = actualError * Math.sin(Math.PI * 2 * direction / maxDirection);
                    PolyLine3d otherLine = new PolyLine3d(new Point3d(p2.x + dx, p2.y + dy, p2.z), p3, p4);
                    if (actualError < tolerance)
                    {
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                        }
                        catch (IllegalArgumentException dre)
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                            fail("concatenation with error " + actualError + " and tolerance " + tolerance
                                    + " should not have failed");
                        }
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine, thirdLine);
                        }
                        catch (IllegalArgumentException dre)
                        {
                            fail("concatenation with error " + actualError + " and tolerance " + tolerance
                                    + " should not have failed");
                        }
                    }
                    else
                    {
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                            fail("Con-connected lines should have thrown an IllegalArgumentException");
                        }
                        catch (IllegalArgumentException e)
                        {
                            // Ignore expected exception
                        }
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine, thirdLine);
                            fail("Con-connected lines should have thrown an IllegalArgumentException");
                        }
                        catch (IllegalArgumentException e)
                        {
                            // Ignore expected exception
                        }
                    }
                }
            }
        }
    }

    /**
     * Test the reverse and project methods.
     */
    @Test
    public final void reverseAndProjectTest()
    {
        Point3d p0 = new Point3d(1.1, 2.21, 3.1);
        Point3d p1 = new Point3d(2.1, 2.22, 3.2);
        Point3d p2 = new Point3d(2.1, 2.23, 3.3);
        Point3d p2x = new Point3d(p2.x, p2.y, p2.z + 1);
        Point3d p3 = new Point3d(4.1, 2.24, 3.4);
        Point3d p4 = new Point3d(5.1, 2.25, 3.5);
        Point3d p5 = new Point3d(6.1, 2.26, 3.6);

        PolyLine3d l01 = new PolyLine3d(p0, p1);
        PolyLine3d r = l01.reverse();
        assertEquals(2, r.size(), "result has size 2");
        assertEquals(p1, r.get(0), "point 0 is p1");
        assertEquals(p0, r.get(1), "point 1 is p0");

        PolyLine3d l05 = new PolyLine3d(p0, p1, p2, p3, p4, p5);
        r = l05.reverse();
        assertEquals(6, r.size(), "result has size 6");
        assertEquals(p5, r.get(0), "point 0 is p5");
        assertEquals(p4, r.get(1), "point 1 is p4");
        assertEquals(p3, r.get(2), "point 2 is p3");
        assertEquals(p2, r.get(3), "point 3 is p2");
        assertEquals(p1, r.get(4), "point 4 is p1");
        assertEquals(p0, r.get(5), "point 5 is p0");

        PolyLine2d l2d = l05.project();
        assertEquals(6, l2d.size(), "result has size 6");
        assertEquals(p0.project(), l2d.get(0), "point 0 is p5");
        assertEquals(p1.project(), l2d.get(1), "point 1 is p4");
        assertEquals(p2.project(), l2d.get(2), "point 2 is p3");
        assertEquals(p3.project(), l2d.get(3), "point 3 is p2");
        assertEquals(p4.project(), l2d.get(4), "point 4 is p1");
        assertEquals(p5.project(), l2d.get(5), "point 5 is p0");

        l05 = new PolyLine3d(p0, p1, p2, p2x, p3, p4, p5);
        l2d = l05.project();
        assertEquals(6, l2d.size(), "result has size 6");
        assertEquals(p0.project(), l2d.get(0), "point 0 is p5");
        assertEquals(p1.project(), l2d.get(1), "point 1 is p4");
        assertEquals(p2.project(), l2d.get(2), "point 2 is p3");
        assertEquals(p3.project(), l2d.get(3), "point 3 is p2");
        assertEquals(p4.project(), l2d.get(4), "point 4 is p1");
        assertEquals(p5.project(), l2d.get(5), "point 5 is p0");

        PolyLine3d l22x = new PolyLine3d(p2, p2x);
        try
        {
            l22x.project();
            fail("Projecting a Polyline3d that entirely projects to one point should have thrown an exception");
        }
        catch (InvalidProjectionException dre)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the extract and extractFraction methods.
     */
    @Test
    @SuppressWarnings("checkstyle:methodlength")
    public final void extractTest()
    {
        Point3d p0 = new Point3d(1, 2, 3);
        Point3d p1 = new Point3d(2, 3, 4);
        Point3d p1a = new Point3d(2.01, 3.01, 4.01);
        Point3d p1b = new Point3d(2.02, 3.02, 4.02);
        Point3d p1c = new Point3d(2.03, 3.03, 4.03);
        Point3d p2 = new Point3d(12, 13, 14);

        PolyLine3d l = new PolyLine3d(p0, p1);
        PolyLine3d ef = l.extractFractional(0, 1);
        assertEquals(2, ef.size(), "size of extraction is 2");
        assertEquals(p0, ef.get(0), "point 0 is p0");
        assertEquals(p1, ef.get(1), "point 1 is p1");
        try
        {
            l.extractFractional(-0.1, 1);
            fail("negative start should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(Double.NaN, 1);
            fail("NaN start should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0, 1.1);
            fail("end > 1 should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0, Double.NaN);
            fail("NaN end should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0.6, 0.4);
            fail("start > end should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(-0.1, 1);
            fail("negative start should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(Double.NaN, 1);
            fail("NaN start should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0, l.getLength() + 0.1);
            fail("end > length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0, Double.NaN);
            fail("NaN end should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0.6, 0.4);
            fail("start > end should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        for (int i = 0; i < 10; i++)
        {
            for (int j = i + 1; j < 10; j++)
            {
                double start = i * l.getLength() / 10;
                double end = j * l.getLength() / 10;
                // System.err.println("i=" + i + ", j=" + j);
                for (PolyLine3d extractedLine : new PolyLine3d[] {l.extract(start, end),
                        l.extractFractional(1.0 * i / 10, 1.0 * j / 10)})
                {
                    assertEquals(2, extractedLine.size(), "size of extract is 2");
                    assertEquals(p0.x + (p1.x - p0.x) * i / 10, extractedLine.get(0).x, 0.0001, "x of 0");
                    assertEquals(p0.y + (p1.y - p0.y) * i / 10, extractedLine.get(0).y, 0.0001, "y of 0");
                    assertEquals(p0.z + (p1.z - p0.z) * i / 10, extractedLine.get(0).z, 0.0001, "z of 0");
                    assertEquals(p0.x + (p1.x - p0.x) * j / 10, extractedLine.get(1).x, 0.0001, "x of 1");
                    assertEquals(p0.y + (p1.y - p0.y) * j / 10, extractedLine.get(1).y, 0.0001, "y of 1");
                    assertEquals(p0.z + (p1.z - p0.z) * j / 10, extractedLine.get(1).z, 0.0001, "z of 1");
                }
            }
        }

        for (PolyLine3d line : new PolyLine3d[] {new PolyLine3d(p0, p1, p2), new PolyLine3d(p0, p1, p1a, p1b, p1c, p2)})
        {
            for (int i = 0; i < 110; i++)
            {
                if (10 == i)
                {
                    continue; // results are not entirely predictable due to rounding errors
                }
                for (int j = i + 1; j < 110; j++)
                {
                    if (10 == j)
                    {
                        continue; // results are not entirely predictable due to rounding errors
                    }
                    double start = i * line.getLength() / 110;
                    double end = j * line.getLength() / 110;
                    // System.err.println("first length is " + firstLength);
                    // System.err.println("second length is " + line.getLength());
                    // System.err.println("i=" + i + ", j=" + j);
                    for (PolyLine3d extractedLine : new PolyLine3d[] {line.extract(start, end),
                            line.extractFractional(1.0 * i / 110, 1.0 * j / 110)})
                    {
                        int expectedSize = i < 10 && j > 10 ? line.size() : 2;
                        assertEquals(expectedSize, extractedLine.size(), "size is " + expectedSize);
                        if (i < 10)
                        {
                            assertEquals(p0.x + (p1.x - p0.x) * i / 10, extractedLine.get(0).x, 0.0001, "x of 0");
                            assertEquals(p0.y + (p1.y - p0.y) * i / 10, extractedLine.get(0).y, 0.0001, "y of 0");
                            assertEquals(p0.z + (p1.z - p0.z) * i / 10, extractedLine.get(0).z, 0.0001, "z of 0");
                        }
                        else
                        {
                            assertEquals(p1.x + (p2.x - p1.x) * (i - 10) / 100, extractedLine.get(0).x, 0.0001, "x of 0");
                            assertEquals(p1.y + (p2.y - p1.y) * (i - 10) / 100, extractedLine.get(0).y, 0.0001, "y of 0");
                            assertEquals(p1.z + (p2.z - p1.z) * (i - 10) / 100, extractedLine.get(0).z, 0.0001, "z of 0");
                        }
                        if (j < 10)
                        {
                            assertEquals(p0.x + (p1.x - p0.x) * j / 10, extractedLine.get(1).x, 0.0001, "x of 1");
                            assertEquals(p0.y + (p1.y - p0.y) * j / 10, extractedLine.get(1).y, 0.0001, "y of 1");
                            assertEquals(p0.z + (p1.z - p0.z) * j / 10, extractedLine.get(1).z, 0.0001, "z of 1");
                        }
                        else
                        {
                            assertEquals(p1.x + (p2.x - p1.x) * (j - 10) / 100, extractedLine.getLast().x, 0.0001, "x of last");
                            assertEquals(p1.y + (p2.y - p1.y) * (j - 10) / 100, extractedLine.getLast().y, 0.0001, "y of last");
                            assertEquals(p1.z + (p2.z - p1.z) * (j - 10) / 100, extractedLine.getLast().z, 0.0001, "z of last");
                        }
                        if (extractedLine.size() > 2)
                        {
                            assertEquals(p1.x, extractedLine.get(1).x, 0.0001, "x of mid");
                            assertEquals(p1.y, extractedLine.get(1).y, 0.0001, "y of mid");
                            assertEquals(p1.z, extractedLine.get(1).z, 0.0001, "z of mid");
                        }
                    }
                }
            }
        }
    }

    /**
     * Test other methods of PolyLine3d.
     */
    @Test
    @SuppressWarnings("unlikely-arg-type")
    public final void testOtherMethods()
    {
        Point3d[] array =
                new Point3d[] {new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(3.2, 4.1, 5.1), new Point3d(5, 6, 7)};
        PolyLine3d line = new PolyLine3d(Arrays.stream(array).iterator());
        assertEquals(array.length, line.size(), "size");
        for (int i = 0; i < array.length; i++)
        {
            assertEquals(array[i], line.get(i), "i-th point");
        }
        int nextIndex = 0;
        for (Iterator<Point3d> iterator = line.iterator(); iterator.hasNext();)
        {
            assertEquals(array[nextIndex++], iterator.next(), "i-th point from line iterator");
        }
        assertEquals(array.length, nextIndex, "iterator returned all points");
        List<Point3d> pointList = line.getPointList();
        for (nextIndex = 0; nextIndex < pointList.size(); nextIndex++)
        {
            assertEquals(array[nextIndex], pointList.get(nextIndex), "i-th point from point list");
        }
        assertEquals(array.length, nextIndex, "pointList contains all points");

        PolyLine3d filtered = line.noiseFilteredLine(0.0);
        assertEquals(line, filtered, "filtered with 0 tolerance returns line");
        filtered = line.noiseFilteredLine(0.01);
        assertEquals(line, filtered, "filtered with very low tolerance returns line");
        filtered = line.noiseFilteredLine(0.5);
        assertEquals(3, filtered.size(), "size of filtered line is 3");
        assertEquals(line.getFirst(), filtered.getFirst(), "first point of filtered line matches");
        assertEquals(line.getLast(), filtered.getLast(), "last point of filtered line matches");
        assertEquals(line.get(1), filtered.get(1), "mid point of filtered line is point 1 of unfiltered line");
        filtered = line.noiseFilteredLine(10);
        assertEquals(2, filtered.size(), "size of filtered line is 2");
        assertEquals(line.getFirst(), filtered.getFirst(), "first point of filtered line matches");
        assertEquals(line.getLast(), filtered.getLast(), "last point of filtered line matches");

        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(3.2, 4.1, 5.1), new Point3d(1, 2, 3)};
        line = new PolyLine3d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(10);
        assertEquals(3, filtered.size(), "size of filtered line is 3");
        assertEquals(line.getFirst(), filtered.getFirst(), "first point of filtered line matches");
        assertEquals(line.getLast(), filtered.getLast(), "last point of filtered line matches");
        assertEquals(line.get(1), filtered.get(1), "mid point of filtered line is point 1 of unfiltered line");

        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(1.1, 2.1, 3), new Point3d(1, 2, 3)};
        line = new PolyLine3d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(0.5);
        assertEquals(3, filtered.size(), "size of filtered line is 3");
        assertEquals(line.getFirst(), filtered.getFirst(), "first point of filtered line matches");
        assertEquals(line.getLast(), filtered.getLast(), "last point of filtered line matches");
        assertEquals(line.get(1), filtered.get(1), "mid point of filtered line is point 1 of unfiltered line");

        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(3, 4, 5)};
        line = new PolyLine3d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(10);
        assertEquals(line, filtered, "Filtering a two-point line returns that line");

        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5)};
        line = new PolyLine3d(0.0, array);
        assertEquals(2, line.size(), "cleaned line has 2 points");
        assertEquals(array[0], line.getFirst(), "first point");
        assertEquals(array[array.length - 1], line.getLast(), "last point");

        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(3, 4, 5)};
        line = new PolyLine3d(0.0, array);
        assertEquals(2, line.size(), "cleaned line has 2 points");
        assertEquals(array[0], line.getFirst(), "first point");
        assertEquals(array[array.length - 1], line.getLast(), "last point");

        array = new Point3d[] {new Point3d(0, -1, 3), new Point3d(1, 2, 4), new Point3d(1, 2, 4), new Point3d(3, 4, 4)};
        line = new PolyLine3d(0.0, array);
        assertEquals(3, line.size(), "cleaned line has 2 points");
        assertEquals(array[0], line.getFirst(), "first point");
        assertEquals(array[array.length - 1], line.getLast(), "last point");

        array = new Point3d[] {new Point3d(0, -1, 3), new Point3d(1, 2, 4), new Point3d(1, 2, 4), new Point3d(1, 2, 4),
                new Point3d(3, 4, 5)};
        line = new PolyLine3d(0.0, array);
        assertEquals(3, line.size(), "cleaned line has 3 points");
        assertEquals(array[0], line.getFirst(), "first point");
        assertEquals(array[1], line.get(1), "mid point");
        assertEquals(array[array.length - 1], line.getLast(), "last point");

        try
        {
            new PolyLine3d(new Point3d[0]);
            fail("Too short array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3)});
            fail("Too short array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3)});
            fail("All duplicate points in array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(1, 2, 3)});
            fail("All duplicate points in array should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 6, 9), new Point3d(8, 9, 15)};
        line = new PolyLine3d(array);

        try
        {
            line.getLocation(-0.1);
            fail("negative location should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        double length = line.getLength();
        assertEquals(15.6, length, 0.1, "Length of line is about 15.6");

        try
        {
            line.getLocation(length + 0.1);
            fail("location beyond length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            line.getLocation(-0.1);
            fail("negative location should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        assertEquals(15.6, length, 0.1, "Length of line is 15.6");

        try
        {
            line.getLocationFraction(1.1);
            fail("location beyond length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            line.getLocationFraction(-0.1);
            fail("negative location should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        for (double position : new double[] {-1, 0, 2.5, 4.9, 5.1, 7.5, 9.9, 10, 11})
        {
            DirectedPoint3d dp = line.getLocationExtended(position);
            if (position < length / 2)
            {
                Ray3d expected =
                        new Ray3d(array[0].interpolate(array[1], position / (length / 2)), Math.atan2(5, 6), Math.atan2(4, 3));
                assertTrue(expected.epsilonEquals(dp, 0.0001, 0.00001), "interpolated/extrapolated point");
            }
            else
            {
                Ray3d expected = new Ray3d(array[1].interpolate(array[2], (position - length / 2) / (length / 2)),
                        Math.atan2(5, 6), Math.atan2(3, 4));
                assertTrue(expected.epsilonEquals(dp, 0.0001, 0.00001), "interpolated/extrapolated point");
            }
            dp = line.getLocationFractionExtended(position / line.getLength());
            if (position < length / 2)
            {
                Ray3d expected =
                        new Ray3d(array[0].interpolate(array[1], position / (length / 2)), Math.atan2(5, 6), Math.atan2(4, 3));
                assertTrue(expected.epsilonEquals(dp, 0.0001, 0.00001), "interpolated/extrapolated point");
            }
            else
            {
                Ray3d expected = new Ray3d(array[1].interpolate(array[2], (position - length / 2) / (length / 2)),
                        Math.atan2(5, 6), Math.atan2(3, 4));
                assertTrue(expected.epsilonEquals(dp, 0.0001, 0.00001), "interpolated/extrapolated point");
            }
        }

        // Test the projectOrthogonal methods
        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 13)};
        line = new PolyLine3d(array);
        for (double x = -15; x <= 20; x++)
        {
            for (double y = -15; y <= 20; y++)
            {
                for (double z = -15; z <= 20; z++)
                {
                    Point3d xyz = new Point3d(x, y, z);
                    // System.out.println("x=" + x + ", y=" + y);
                    double result = line.projectOrthogonalFractional(xyz);
                    if (!Double.isNaN(result))
                    {
                        assertTrue(result >= 0, "result must be >= 0.0");
                        assertTrue(result <= 1.0, "result must be <= 1.0");
                        DirectedPoint3d ray = line.getLocationFraction(result);
                        Point3d projected = line.projectOrthogonal(xyz);
                        assertEquals(ray.x, projected.x, 00001,
                                "if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction");
                        assertEquals(ray.y, projected.y, 00001,
                                "if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction");
                        assertEquals(ray.z, projected.z, 00001,
                                "if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction");
                    }
                    else
                    {
                        assertNull(line.projectOrthogonal(xyz), "point projects outside line");
                    }
                    result = line.projectOrthogonalFractionalExtended(xyz);
                    if (!Double.isNaN(result))
                    {
                        Point3d resultPoint = line.getLocationFractionExtended(result);
                        if (result >= 0.0 && result <= 1.0)
                        {
                            Point3d closestPointOnLine = line.closestPointOnPolyLine(xyz);
                            assertEquals(resultPoint, closestPointOnLine, "resultPoint is equal to closestPoint");
                            assertEquals(resultPoint, line.getLocationFraction(result),
                                    "getLocationFraction returns same as getLocationfractionExtended");
                        }
                        else
                        {
                            try
                            {
                                line.getLocationFraction(result);
                                fail("illegal fraction should have thrown an IllegalArgumentException");
                            }
                            catch (IllegalArgumentException e)
                            {
                                // Ignore expected exception
                            }
                            if (result < 0)
                            {
                                assertEquals(resultPoint.distance(line.get(1)) - resultPoint.distance(line.getFirst()),
                                        line.getFirst().distance(line.get(1)), 0.0001,
                                        "resultPoint lies on extention of start segment");
                            }
                            else
                            {
                                // result > 1
                                assertEquals(
                                        resultPoint.distance(line.get(line.size() - 2)) - resultPoint.distance(line.getLast()),
                                        line.getLast().distance(line.get(line.size() - 2)), 0.0001,
                                        "resultPoint lies on extention of end segment");
                            }
                        }
                    }
                    else
                    {
                        assertNull(line.projectOrthogonalExtended(xyz), "point projects outside extended line");
                        Point3d closestPointOnLine = line.closestPointOnPolyLine(xyz);
                        assertNotNull(closestPointOnLine, "closest point is never null");
                        boolean found = false;
                        for (int index = 0; index < line.size(); index++)
                        {
                            Point3d linePoint = line.get(index);
                            if (linePoint.x == closestPointOnLine.x && linePoint.y == closestPointOnLine.y)
                            {
                                found = true;
                            }
                        }
                        assertTrue(found, "closestPointOnLine is one of the construction points of the line");
                    }
                    Point3d closestPointOnLine = line.closestPointOnPolyLine(xyz);
                    assertNotNull(closestPointOnLine, "closest point is never null");
                }
            }
        }
        Point3d toleranceResultPoint = line.getLocationFraction(-0.01, 0.01);
        assertEquals(line.getLocationFraction(0), toleranceResultPoint, "tolerance result matches extended fraction result");
        toleranceResultPoint = line.getLocationFraction(1.01, 0.01);
        assertEquals(line.getLocationFraction(1), toleranceResultPoint, "tolerance result matches extended fraction result");

        try
        {
            line.getLocationFraction(-.011, 0.01);
            fail("fraction outside tolerance should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            line.getLocationFraction(1.011, 0.01);
            fail("fraction outside tolerance should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        // Test the extract and truncate methods
        array = new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10)};
        line = new PolyLine3d(array);
        length = line.getLength();
        for (double to : new double[] {-10, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20})
        {
            if (to <= 0 || to > length)
            {
                try
                {
                    line.truncate(to);
                    fail("illegal truncate should have thrown an IllegalArgumentException");
                }
                catch (IllegalArgumentException e)
                {
                    // Ignore expected exception
                }
            }
            else
            {
                PolyLine3d truncated = line.truncate(to);
                assertEquals(line.getFirst(), truncated.getFirst(), "truncated line start with start point of line");
                assertEquals(to, truncated.getLength(), 0.0001, "Length of truncated line is truncate position");
            }
            for (double from : new double[] {-10, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20})
            {
                if (from >= to || from < 0 || to > length)
                {
                    try
                    {
                        line.extract(from, to);
                        fail("Illegal range should have thrown an IllegalArgumentException");
                    }
                    catch (IllegalArgumentException e)
                    {
                        // Ignore expected exception
                    }
                }
                else
                {
                    PolyLine3d fragment = line.extract(from, to);
                    Point3d fromPoint = line.getLocation(from);
                    assertTrue(fromPoint.epsilonEquals(fragment.getFirst(), 0.00001), "fragment starts at from");
                    Point3d toPoint = line.getLocation(to);
                    assertTrue(toPoint.epsilonEquals(fragment.getLast(), 0.00001), "fragment ends at to");
                    assertEquals(to - from, fragment.getLength(), 0.0001, "Length of fragment");
                    if (from == 0)
                    {
                        assertEquals(line.getFirst(), fragment.getFirst(), "fragment starts at begin of line");
                    }
                    if (to == length)
                    {
                        assertEquals(line.getLast(), fragment.getLast(), "fragment ends at end of line");
                    }
                }
            }
        }
        try
        {
            line.extract(Double.NaN, 10.0);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException ae)
        {
            // Ignore expected exception
        }

        try
        {
            line.extract(0.0, Double.NaN);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        // Verify that hashCode. Check that the result depends on the actual coordinates.
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(1, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                "hash code takes x coordinate of first point into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 1, 0), new Point3d(1, 1, 1)).hashCode(),
                "hash code takes y coordinate of first point into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 1), new Point3d(1, 1, 1)).hashCode(),
                "hash code takes z coordinate of first point into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(2, 1, 1)).hashCode(),
                "hash code takes x coordinate of second point into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 2, 1)).hashCode(),
                "hash code takes y coordinate of second point into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 2)).hashCode(),
                "hash code takes z coordinate of second point into account");

        // Verify the equals method.
        assertTrue(line.equals(line), "line is equal to itself");
        assertFalse(line.equals(new PolyLine3d(new Point3d(123, 456, 789), new Point3d(789, 101112, 987))),
                "line is not equal to a different line");
        assertFalse(line.equals(null), "line is not equal to null");
        assertFalse(line.equals("unlikely"), "line is not equal to a different kind of object");
        assertTrue(line.equals(new PolyLine3d(line.iterator())), "Line is equal to line from same set of points");
        // Make a line that differs only in the very last point
        Point3d[] otherArray = Arrays.copyOf(array, array.length);
        otherArray[otherArray.length - 1] = new Point3d(otherArray[otherArray.length - 1].x,
                otherArray[otherArray.length - 1].y + 5, otherArray[otherArray.length - 1].z);
        PolyLine3d other = new PolyLine3d(otherArray);
        assertFalse(line.equals(other), "PolyLine3d that differs in y of last point is different");
    }

    /**
     * Test the find method.
     * @throws SecurityException if that happens uncaught; this test has failed
     * @throws NoSuchMethodException if that happens uncaught; this test has failed
     * @throws InvocationTargetException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException if that happens uncaught; this test has failed
     * @throws IllegalAccessException if that happens uncaught; this test has failed
     */
    @Test
    public final void testFind() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        // Construct a line with exponentially increasing distances
        List<Point3d> points = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            points.add(new Point3d(Math.pow(2, i) - 1, 10, 20));
        }
        PolyLine3d line = new PolyLine3d(points);
        double end = points.get(points.size() - 1).x;
        for (int i = 0; i < end; i++)
        {
            double pos = i + 0.5;
            int index = line.find(pos);
            assertTrue(line.get(index).x <= pos, "segment starts before pos");
            assertTrue(line.get(index + 1).x >= pos, "next segment starts after pos");
        }
        assertEquals(0, line.find(0.0), "pos 0 returns index 0");
    }

    /**
     * Test the truncate method.
     */
    @Test
    public final void testTruncate()
    {
        Point3d from = new Point3d(10, 20, 30);
        Point3d to = new Point3d(70, 80, 90);
        double length = from.distance(to);
        PolyLine3d line = new PolyLine3d(from, to);
        PolyLine3d truncatedLine = line.truncate(length);
        assertEquals(truncatedLine.get(0), from,
                "Start of line truncated at full length is the same as start of the input line");
        assertEquals(0, truncatedLine.get(1).distance(to), 0.0001,
                "End of line truncated at full length is about the same as end of input line");
        try
        {
            line.truncate(-0.1);
            fail("truncate at negative length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        try
        {
            line.truncate(length + 0.1);
            fail("truncate at length beyond length of line should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        truncatedLine = line.truncate(length / 2);
        assertEquals(truncatedLine.get(0), from, "Start of truncated line is the same as start of the input line");
        Point3d halfWay = new Point3d((from.x + to.x) / 2, (from.y + to.y) / 2, (from.z + to.z) / 2);
        assertEquals(0, halfWay.distance(truncatedLine.get(1)), 0.0001,
                "End of 50%, truncated 2-point line should be at the half way point");
        Point3d intermediatePoint = new Point3d(20, 20, 20);
        line = new PolyLine3d(from, intermediatePoint, to);
        length = from.distance(intermediatePoint) + intermediatePoint.distance(to);
        truncatedLine = line.truncate(length);
        assertEquals(truncatedLine.get(0), from,
                "Start of line truncated at full length is the same as start of the input line");
        assertEquals(0, truncatedLine.get(2).distance(to), 0.0001,
                "End of line truncated at full length is about the same as end of input line");
        truncatedLine = line.truncate(from.distance(intermediatePoint));
        assertEquals(truncatedLine.get(0), from,
                "Start of line truncated at full length is the same as start of the input line");
        assertEquals(0, truncatedLine.get(1).distance(intermediatePoint), 0.0001,
                "Line truncated at intermediate point ends at that intermediate point");
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point3d[] points = new Point3d[] {new Point3d(123.456, 345.678, 901.234), new Point3d(234.567, 456.789, 12.345),
                new Point3d(-12.345, -34.567, 45.678)};
        PolyLine3d pl = new PolyLine3d(points);
        String[] out = Export.toTsv(pl).split("\\n");
        assertEquals(points.length, out.length, "Excel output consists of one line per point");
        for (int index = 0; index < points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals(3, fields.length, "each line consists of three fields");
            try
            {
                double x = Double.parseDouble(fields[0].trim());
                assertEquals(points[index].x, x, 0.001, "x matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("First field " + fields[0] + " does not parse as a double");
            }
            try
            {
                double y = Double.parseDouble(fields[1].trim());
                assertEquals(points[index].y, y, 0.001, "y matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[1] + " does not parse as a double");
            }
            try
            {
                double z = Double.parseDouble(fields[2].trim());
                assertEquals(points[index].z, z, 0.001, "z matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[2] + " does not parse as a double");
            }
        }
    }

    /**
     * Test the hashCode and Equals methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testToStringHashCodeAndEquals()
    {
        PolyLine3d line = new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10)});
        assertTrue(line.toString().startsWith("PolyLine3d ["), "toString returns something descriptive");
        assertFalse(line.toString().contains("startDirY"), "toString does not contain startDirY");
        assertFalse(line.toString().contains("startDirZ"), "toString does not contain startDirZ");
        assertTrue(line.toString().indexOf(line.toString(true)) > 0, "toString can suppress the class name");

        // Verify that hashCode. Check that the result depends on the actual coordinates.
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(1, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                "hash code takes x coordinate into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 1, 0), new Point3d(1, 1, 1)).hashCode(),
                "hash code takes y coordinate into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 1), new Point3d(1, 1, 1)).hashCode(),
                "hash code takes z coordinate into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(2, 1, 1)).hashCode(),
                "hash code takes x coordinate into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 2, 1)).hashCode(),
                "hash code takes y coordinate into account");
        assertNotEquals(new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 2)).hashCode(),
                "hash code takes z coordinate into account");

        // Verify the equals method.
        assertTrue(line.equals(line), "line is equal to itself");
        assertFalse(line.equals(new PolyLine2d(new Point2d(123, 456), new Point2d(789, 101112))),
                "line is not equal to a different line");
        assertFalse(line.equals(null), "line is not equal to null");
        assertFalse(line.equals("unlikely"), "line is not equal to a different kind of object");
        assertEquals(line, new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10)}),
                "equals verbatim copy");
        assertNotEquals(line, new PolyLine3d(new Point3d[] {new Point3d(2, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10)}),
                "equals checks x");
        assertNotEquals(line, new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 7, 8), new Point3d(8, 9, 10)}),
                "equals checks y");
        assertNotEquals(line, new PolyLine3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 11)}),
                "equals checks z");
        assertTrue(line.equals(new PolyLine3d(line.iterator())), "Line is equal to line from same set of points");
    }

    /**
     * Test for a problem that occurred in OTS2.
     */
    @Test
    public void testProjectProblem()
    {
        PolyLine3d polyLine3d = new PolyLine3d(new Point3d(1, 1, 2), new Point3d(11, 1, 5), new Point3d(16, 6, 0),
                new Point3d(21, 6, 0), new Point3d(21, 0, 0));
        double x = 11;
        double y = 1;
        Point2d point = new Point2d(x, y);
        // The difficult work is done with the line projected on the the Z=0 plane
        PolyLine2d projectedLine = polyLine3d.project();
        // Project (x, y) onto each segment of the projected line
        int bestSegmentIndex = -1;
        double bestDistance = Double.POSITIVE_INFINITY;
        double bestSegmentDirection = Double.NaN;
        Point2d prevPoint = null;
        // Find the nearest segment
        for (int index = 0; index < projectedLine.size(); index++)
        {
            Point2d nextPoint = projectedLine.get(index);
            if (null != prevPoint)
            {
                Point2d closestOnSegment = point.closestPointOnSegment(prevPoint, nextPoint);
                double distance = closestOnSegment.distance(point);
                if (distance < bestDistance)
                {
                    bestDistance = distance;
                    bestSegmentIndex = index;
                    bestSegmentDirection = prevPoint.directionTo(nextPoint);
                }
            }
            prevPoint = nextPoint;
        }
        // bestSegmentIndex is the index of the point where the best segment ENDS
        // Make the rays that bisect the angles at the start and end of the segment
        double prevDirection = projectedLine.get(bestSegmentIndex - 1).directionTo(projectedLine.get(bestSegmentIndex));
        double nextDirection = bestSegmentIndex < projectedLine.size() - 1
                ? projectedLine.get(bestSegmentIndex).directionTo(projectedLine.get(bestSegmentIndex + 1))
                : projectedLine.get(projectedLine.size() - 2).directionTo(projectedLine.getLast());
        Ray2d prevRay =
                new Ray2d(projectedLine.get(bestSegmentIndex - 1), (prevDirection + bestSegmentDirection) / 2 + Math.PI / 2);
        Ray2d nextRay =
                new Ray2d(projectedLine.get(bestSegmentIndex), (bestSegmentDirection + nextDirection) / 2 + Math.PI / 2);
        // Project the point onto each ray
        Point2d prevRayProjection = prevRay.projectOrthogonalExtended(point);
        Point2d nextRayProjection = nextRay.projectOrthogonalExtended(point);
        Point2d projectionOnBestSegment =
                prevRay.interpolate(nextRay, point.distance(prevRayProjection) / prevRayProjection.distance(nextRayProjection));
        // Find the corresponding fractional location on the input polyLine3d
        // Find the corresponding segment on the polyLine3d
        for (int index = 1; index < polyLine3d.size(); index++)
        {
            // Comparing double values; but that should work as the coordinates of the rays are exact copies of the x and y
            // coordinates of the polyLine3d
            if (polyLine3d.getX(index - 1) == prevRay.x && polyLine3d.getY(index - 1) == prevRay.y
                    && polyLine3d.getX(index) == nextRay.x && polyLine3d.getY(index) == nextRay.y)
            {
                double lengthAtPrevRay = polyLine3d.lengthAtIndex(index - 1);
                double fraction = (lengthAtPrevRay + prevRay.distance(projectionOnBestSegment) / prevRay.distance(nextRay)
                        * (polyLine3d.lengthAtIndex(index) - lengthAtPrevRay)) / polyLine3d.getLength();

                polyLine3d.getLocationFraction(fraction); // This operation failed
            }
        }
    }

    /**
     * Test the transitionLine method.
     */
    // @Test
    // public void testTransitionLine()
    // {
    // // Create a Bezier with a 90 degree change of direction starting in X direction, ending in Y direction
    // PolyLine3d bezier = Bezier.cubic(64, new Ray3d(-5, 0, 2, 0, 0, 2), new Ray3d(0, 5, 2, 0, 7, 2));
    // // System.out.print("c1,0,0" + bezier1.project().toPlot());
    // double length = bezier.getLength();
    // double prevDir = Double.NaN;
    // for (int step = 0; step <= 1000; step++)
    // {
    // double distance = length * step / 1000;
    // DirectedPoint3d dp = bezier.getLocation(distance);
    // double direction = Math.toDegrees(dp.dirZ);
    // if (step > 0)
    // {
    // assertEquals(prevDir, direction, 2, "dirZ changes very little at step " + step);
    // }
    // prevDir = Math.toDegrees(dp.dirZ);
    // }
    // // Make a gradually transitioning offset line
    // PolyLine3d transitioningOffsetLine = bezier.offsetLine(0, 2);
    // // Verify that this curve is fairly smooth
    // length = transitioningOffsetLine.getLength();
    // prevDir = Double.NaN;
    // for (int step = 0; step <= 1000; step++)
    // {
    // double distance = length * step / 1000;
    // DirectedPoint3d dp = transitioningOffsetLine.getLocation(distance);
    // double direction = Math.toDegrees(dp.dirZ);
    // if (step > 0)
    // {
    // assertEquals(prevDir, direction, 2, "dirZ changes very little at step " + step);
    // }
    // prevDir = Math.toDegrees(dp.dirZ);
    // }
    // PolyLine3d endLine = bezier.offsetLine(-2);
    // // System.out.print("c0,1,0" + endLine.project().toPlot());
    // TransitionFunction transitionFunction = new TransitionFunction()
    // {
    // @Override
    // public double function(final double fraction)
    // {
    // return 0.5 - Math.cos(fraction * Math.PI) / 2;
    // }
    // };
    // PolyLine3d cosineSmoothTransitioningLine = bezier.transitionLine(endLine, transitionFunction);
    // // System.out.print("c0,0,0" + cosineSmoothTransitioningLine.project().toPlot());
    // length = cosineSmoothTransitioningLine.getLength();
    // prevDir = Double.NaN;
    // for (int step = 0; step <= 1000; step++)
    // {
    // double distance = length * step / 1000;
    // DirectedPoint3d dp = cosineSmoothTransitioningLine.getLocation(distance);
    // double direction = Math.toDegrees(dp.dirZ);
    // if (step > 0)
    // {
    // assertEquals(prevDir, direction, 4, "dirZ changes very little at step " + step);
    // }
    // prevDir = Math.toDegrees(dp.dirZ);
    // }
    // // System.out.print(
    // // "c0,0,1" + Bezier.cubic(bezier1.getLocationFraction(0), endLine.getLocationFraction(1)).project().toPlot());
    // // Reverse the lines
    // PolyLine3d cosineSmoothTransitioningLine2 =
    // endLine.reverse().transitionLine(bezier.reverse(), transitionFunction).reverse();
    // // Check that those lines are very similar
    // assertEquals(cosineSmoothTransitioningLine.getLength(), cosineSmoothTransitioningLine2.getLength(), 0.001,
    // "Lengths are equal");
    // for (int step = 0; step <= 1000; step++)
    // {
    // DirectedPoint3d dp1 =
    // cosineSmoothTransitioningLine.getLocation(step * cosineSmoothTransitioningLine.getLength() / 1000);
    // DirectedPoint3d dp2 =
    // cosineSmoothTransitioningLine2.getLocation(step * cosineSmoothTransitioningLine2.getLength() / 1000);
    // assertEquals(dp1.x, dp2.x, 0.001, "rays are almost equal in x");
    // assertEquals(dp1.y, dp2.y, 0.001, "rays are almost equal in y");
    // assertEquals(dp1.z, dp2.z, 0.001, "rays are almost equal in z");
    // assertEquals(dp1.dirY, dp2.dirY, 0.0001, "rays are almost equal in dirY");
    // assertEquals(dp1.dirZ, dp2.dirZ, 0.0001, "rays are almost equal in dirZ");
    // }
    //
    // assertEquals(bezier, bezier.offsetLine(0, 0), "offset by zero returns original");
    // assertEquals(bezier.offsetLine(3, 3), bezier.offsetLine(3),
    // "offset by constant with two arguments returns same as offset with one argument");
    // }

    /**
     * Test the degenerate PolyLine3d.
     */
    @Test
    public void testDegenerate()
    {
        try
        {
            new PolyLine3d(Double.NaN, 2, 2.5, -1, 3);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, Double.NaN, 2.5, -1, 3);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, Double.NaN, -1, 3);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, 2.5, -1, Double.NaN);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, 2.5, Double.NaN, 3);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, 2.5, -1, Double.POSITIVE_INFINITY);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, 2.5, Double.POSITIVE_INFINITY, 3);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, 2.5, -1, Double.NEGATIVE_INFINITY);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(1, 2, 2.5, Double.NEGATIVE_INFINITY, 3);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        PolyLine3d l = new PolyLine3d(1, 2, 2.5, 3, -1);
        assertEquals(0, l.getLength(), 0, "length is 0");
        assertEquals(1, l.size(), "size is 1");
        assertEquals(1, l.getX(0), 0, "getX(0) is 1");
        assertEquals(2, l.getY(0), 0, "getY(0) is 2");
        DirectedPoint3d dp = l.getLocation(0.0);
        assertEquals(3, dp.getDirZ(), 0, "heading at 0");
        assertEquals(1, dp.getX(), 0, "x at 0 is 1");
        assertEquals(2, dp.getY(), 0, "y at 0 is 2");
        assertEquals(new Bounds3d(l.get(0)), l.getAbsoluteBounds(), "bounds");
        try
        {
            l.getLocation(0.1);
            fail("location at position != 0 should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            l.getLocation(-0.1);
            fail("location at position != 0 should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d(1, 2, 2.5), -1, Double.NaN);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d(1, 2, 2.5), Double.NaN, 3);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d((Ray3d) null);
            fail("null pointer should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        assertEquals(dp, l.closestPointOnPolyLine(new Point3d(4, -2, 7)), "closest point is the point");

        PolyLine3d straightXisZ = new PolyLine3d(1, 2, 5, 0, 0);
        for (int x = -10; x <= 10; x += 1)
        {
            for (int y = -10; y <= 10; y += 1)
            {
                for (int z = -10; z <= 10; z += 1)
                {
                    Point3d testPoint = new Point3d(x, y, z);
                    assertEquals(new Ray3d(dp).projectOrthogonalExtended(testPoint), l.projectOrthogonalExtended(testPoint),
                            "closest point extended");
                    assertEquals(l.getLocation(0.0), l.closestPointOnPolyLine(testPoint),
                            "closest point on degenerate line is the point of the degenerate line");
                    if (z == 5)
                    {
                        assertEquals(straightXisZ.get(0), straightXisZ.projectOrthogonal(testPoint),
                                "projection on X==Z degenerate line hits");
                    }
                    else
                    {
                        assertNull(straightXisZ.projectOrthogonal(testPoint), "projection on X==Z degenerate line misses");
                    }
                    if (x == 1 && y == 2 && z == 2.5)
                    {
                        assertEquals(testPoint, l.projectOrthogonal(testPoint),
                                "NonExtended projection will return point for exact match");
                        assertEquals(0, l.projectOrthogonalFractional(testPoint), 0,
                                "NonExtended fractional projection returns 0 for exact match");
                        assertEquals(0, l.projectOrthogonalFractionalExtended(testPoint), 0,
                                "Extended fractional projection returns 0 for exact match");
                    }
                    else
                    {
                        assertNull(l.projectOrthogonal(testPoint),
                                "For non-nice directions nonExtended projection will return null if point does not match");
                        assertTrue(Double.isNaN(l.projectOrthogonalFractional(testPoint)), "For non-nice directions "
                                + "non-extended fractional projection will return NaN if point does not match");
                        if (new Ray3d(l.getLocation(0.0)).projectOrthogonalFractional(testPoint) > 0)
                        {
                            assertTrue(Double.POSITIVE_INFINITY == l.projectOrthogonalFractionalExtended(testPoint),
                                    "ProjectOrthogonalFractionalExtended returns POSITIVE_INFINITY of projection misses "
                                            + "along startHeading side");
                        }
                        else
                        {
                            assertTrue(Double.NEGATIVE_INFINITY == l.projectOrthogonalFractionalExtended(testPoint),
                                    "ProjectOrthogonalFractionalExtended returns POSITIVE_INFINITY of projection misses "
                                            + ", but not along startHeading side");
                        }
                    }
                    if (z == 5)
                    {
                        assertEquals(straightXisZ.get(0), straightXisZ.projectOrthogonal(testPoint),
                                "Non-Extended projection will return point for matching X for line along X");
                    }
                    else
                    {
                        assertNull(straightXisZ.projectOrthogonal(testPoint),
                                "Non-Extended projection will return null for non matching X for line along X");
                    }
                }
            }
        }

        l = new PolyLine3d(new Point3d(1, 2, 2.5), 3, -1);
        assertEquals(0, l.getLength(), 0, "length is 0");
        assertEquals(1, l.size(), "size is 1");
        assertEquals(1, l.getX(0), 0, "getX(0) is 1");
        assertEquals(2, l.getY(0), 0, "getY(0) is 2");
        assertEquals(2.5, l.getZ(0), 0, "getZ(0) is 2.5");
        dp = l.getLocation(0.0);
        assertEquals(3, dp.getDirZ(), 0, "dirZ at 0");
        assertEquals(-1, dp.getDirY(), 0, "dirY at 0");
        assertEquals(1, dp.getX(), 0, "x at 0 is 1");
        assertEquals(2, dp.getY(), 0, "y at 0 is 2");
        assertEquals(2.5, dp.getZ(), 0, "z at 0 is 2.5");

        l = new PolyLine3d(new Ray3d(1, 2, 2.5, 3, -1));
        assertEquals(0, l.getLength(), 0, "length is 0");
        assertEquals(1, l.size(), "size is 1");
        assertEquals(1, l.getX(0), 0, "getX(0) is 1");
        assertEquals(2, l.getY(0), 0, "getY(0) is 2");
        assertEquals(2.5, l.getZ(0), 0, "getZ(0) is 2.5");
        dp = l.getLocation(0.0);
        assertEquals(3, dp.getDirZ(), 0, "dirZ at 0");
        assertEquals(-1, dp.getDirY(), 0, "dirY at 0");
        assertEquals(1, dp.getX(), 0, "x at 0 is 1");
        assertEquals(2, dp.getY(), 0, "y at 0 is 2");
        assertEquals(2.5, dp.getZ(), 0, "z at 0 is 2.5");

        PolyLine3d notEqual = new PolyLine3d(1, 2, 2.5, 4, -1);
        assertNotEquals(l, notEqual, "Check that the equals method verifies the startDirY");
        notEqual = new PolyLine3d(1, 2, 2.5, 3, -2);
        assertNotEquals(l, notEqual, "Check that the equals method verifies the startDirZ");

        assertTrue(l.toString().contains("startDirY"), "toString contains startDirY");
        assertTrue(l.toString().contains("startDirZ"), "toString contains startDirZ");
    }

    /**
     * Draw a X marker.
     * @param x x location
     * @param y y location
     * @return String
     */
    public static String marker(final double x, final double y)
    {
        final double markerSize = 0.05;
        return String.format("M%f,%f L%f,%f M%f,%f L%f,%f", x - markerSize / 2, y - markerSize / 2, x + markerSize / 2,
                y + markerSize / 2, x - markerSize / 2, y + markerSize / 2, x + markerSize / 2, y - markerSize / 2);
    }

    /**
     * Problem with limited precision when getting location almost at end.
     */
    @Test
    public void testOTS2Problem()
    {
        // Problem 1
        PolyLine3d line = new PolyLine3d(new Point3d(100, 0, 0), new Point3d(100.1, 0, 0));
        double length = line.getLength();
        line.getLocation(length - Math.ulp(length));

        // Problem 2
        line = new PolyLine3d(new Point3d(0, 0, 0), new Point3d(110.1, 0, 0), new Point3d(111, 0, 0));
        length = line.getLength();
        line.getLocation(length - Math.ulp(length));

        // Problem 3
        List<Point3d> list = new ArrayList<>();
        list.add(new Point3d(1, 2, 3));
        list.add(new Ray3d(2, 3, 4, 0, 0));
        new PolyLine3d(list);
    }

}
