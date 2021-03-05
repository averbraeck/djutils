package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.line.PolyLine.TransitionFunction;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * TestLine3d.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PolyLine3dTest
{
    /**
     * Test the constructors of PolyLine3d.
     * @throws DrawRuntimeException on failure
     */
    @Test
    public final void constructorsTest() throws DrawRuntimeException
    {
        double[] values = { -999, 0, 99, 9999 }; // Keep this list short; execution time grows with 9th power of length
        Point3d[] points = new Point3d[0]; // Empty array
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
                for (double z0 : values)
                {
                    points = new Point3d[1]; // Degenerate array holding one point
                    points[0] = new Point3d(x0, y0, z0);
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
            }
        }
    }

    /**
     * Test all the constructors of PolyLine3d.
     * @param points Point3d[]; array of Point3d to test with
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    private void runConstructors(final Point3d[] points) throws DrawRuntimeException
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
        verifyPoints(new PolyLine3d(line.getPoints()), points);
        assertEquals("length at index 0", 0.0, line.lengthAtIndex(0), 0);
        double length = 0;
        for (int i = 1; i < points.length; i++)
        {
            length += Math.sqrt(Math.pow(points[i].x - points[i - 1].x, 2) + Math.pow(points[i].y - points[i - 1].y, 2)
                    + Math.pow(points[i].z - points[i - 1].z, 2));
            assertEquals("length at index", length, line.lengthAtIndex(i), 0.0001);
        }
        assertEquals("length", length, line.getLength(), 10 * Math.ulp(length));

        assertEquals("size", points.length, line.size());

        Bounds3d b3d = line.getBounds();
        Bounds3d ref = new Bounds3d(points);
        assertEquals("bounds is correct", ref, b3d);

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
     * @param path Path2D; the path
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
     * @param line Line3d; the OTS line
     * @param points Point3d[]; the OTSPoint array
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    private void verifyPoints(final PolyLine3d line, final Point3d[] points) throws DrawRuntimeException
    {
        assertEquals("Line should have same number of points as point array", line.size(), points.length);
        for (int i = 0; i < points.length; i++)
        {
            assertEquals("x of point i should match", points[i].x, line.get(i).x, Math.ulp(points[i].x));
            assertEquals("y of point i should match", points[i].y, line.get(i).y, Math.ulp(points[i].y));
            assertEquals("z of point i should match", points[i].z, line.get(i).z, Math.ulp(points[i].z));
            assertEquals("x of point i should match", points[i].x, line.getX(i), Math.ulp(points[i].x));
            assertEquals("y of point i should match", points[i].y, line.getY(i), Math.ulp(points[i].y));
            assertEquals("z of point i should match", points[i].z, line.getZ(i), Math.ulp(points[i].z));
            if (i < points.length - 1)
            {
                LineSegment3d segment = line.getSegment(i);
                assertEquals("begin x of line segment i should match", points[i].x, segment.startX, Math.ulp(points[i].x));
                assertEquals("begin y of line segment i should match", points[i].y, segment.startY, Math.ulp(points[i].y));
                assertEquals("begin z of line segment i should match", points[i].z, segment.startZ, Math.ulp(points[i].z));
                assertEquals("end x of line segment i should match", points[i + 1].x, segment.endX, Math.ulp(points[i + 1].x));
                assertEquals("end y of line segment i should match", points[i + 1].y, segment.endY, Math.ulp(points[i + 1].y));
                assertEquals("end z of line segment i should match", points[i + 1].z, segment.endZ, Math.ulp(points[i + 1].z));
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
     * Test all constructors of a Line2d.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     */
    @Test
    public void testConstructors() throws DrawRuntimeException, DrawRuntimeException
    {
        runConstructors(new Point3d[] { new Point3d(1.2, 3.4, 5.5), new Point3d(2.3, 4.5, 6.6), new Point3d(3.4, 5.6, 7.7) });

        try
        {
            new PolyLine3d(new double[] { 1, 2, 3 }, new double[] { 4, 5, 6 }, new double[] { 7, 8 });
            fail("double arrays of unequal length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] { 1, 2, 3 }, new double[] { 4, 5 }, new double[] { 7, 8, 9 });
            fail("double arrays of unequal length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] { 1, 2 }, new double[] { 4, 5, 6 }, new double[] { 7, 8, 9 });
            fail("double arrays of unequal length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(null, new double[] { 1, 2 }, new double[] { 3, 4 });
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] { 1, 2 }, null, new double[] { 5, 6 });
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new double[] { 1, 2 }, new double[] { 3, 4 }, null);
            fail("null double array should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d((List<Point3d>) null);
            fail("null list should have thrown a nullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        List<Point3d> shortList = new ArrayList<>();
        try
        {
            new PolyLine3d(shortList);
            fail("empty list should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        shortList.add(new Point3d(1, 2, 3));
        try
        {
            new PolyLine3d(shortList);
            fail("one-point list should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        Point3d p1 = new Point3d(1, 2, 3);
        Point3d p2 = new Point3d(3, 4, 5);
        PolyLine3d pl = new PolyLine3d(p1, p2);
        assertEquals("two points", 2, pl.size());
        assertEquals("p1", p1, pl.get(0));
        assertEquals("p2", p2, pl.get(1));

        pl = new PolyLine3d(p1, p2, (Point3d[]) null);
        assertEquals("two points", 2, pl.size());
        assertEquals("p1", p1, pl.get(0));
        assertEquals("p2", p2, pl.get(1));

        pl = new PolyLine3d(p1, p2, new Point3d[0]);
        assertEquals("two points", 2, pl.size());
        assertEquals("p1", p1, pl.get(0));
        assertEquals("p2", p2, pl.get(1));

        try
        {
            new PolyLine3d(new Point3d[] {});
            fail("empty array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3) });
            fail("single point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3) });
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5) });
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new PolyLine3d(new Point3d[] { new Point3d(-1, -2, -3), new Point3d(1, 2, 3), new Point3d(1, 2, 3),
                    new Point3d(3, 4, 5) });
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test that exception is thrown when it should be.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void exceptionTest() throws DrawRuntimeException
    {
        PolyLine3d line = new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 5, 6) });
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
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void locationExtendedTest() throws DrawRuntimeException
    {
        Point3d p0 = new Point3d(10, 20, 30);
        Point3d p1 = new Point3d(40, 50, 60);
        Point3d p2 = new Point3d(90, 80, 70);
        PolyLine3d polyLine = new PolyLine3d(new Point3d[] { p0, p1, p2 });
        double expectedPhi1 = Math.atan2(p1.y - p0.y, p1.x - p0.x);
        double expectedTheta1 = Math.atan2(Math.hypot(p1.x - p0.x, p1.y - p0.y), p1.z - p0.z);
        checkGetLocation(polyLine, -10, null, expectedPhi1, expectedTheta1);
        checkGetLocation(polyLine, -0.0001, p0, expectedPhi1, expectedTheta1);
        checkGetLocation(polyLine, 0, p0, expectedPhi1, expectedTheta1);
        checkGetLocation(polyLine, 0.0001, p0, expectedPhi1, expectedTheta1);
        double expectedPhi2 = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        double expectedTheta2 = Math.atan2(Math.hypot(p2.x - p1.x, p2.y - p1.y), p2.z - p1.z);
        checkGetLocation(polyLine, 0.9999, p2, expectedPhi2, expectedTheta2);
        checkGetLocation(polyLine, 1.0, p2, expectedPhi2, expectedTheta2);
        checkGetLocation(polyLine, 1.0001, p2, expectedPhi2, expectedTheta2);
        checkGetLocation(polyLine, 10, null, expectedPhi2, expectedTheta2);
    }

    /**
     * Check the location returned by the various location methods.
     * @param line Line3d; the line
     * @param fraction double; relative position to check
     * @param expectedPoint Point3d; expected location of the result
     * @param expectedPhi double; expected angle of the result from the X axis
     * @param expectedTheta double; expected angle of the result from the Z axis
     * @throws DrawRuntimeException on failure
     */
    private void checkGetLocation(final PolyLine3d line, final double fraction, final Point3d expectedPoint,
            final double expectedPhi, final double expectedTheta) throws DrawRuntimeException
    {
        double length = line.getLength();
        checkRay3d(line.getLocationExtended(fraction * length), expectedPoint, expectedPhi, expectedTheta);
        if (fraction < 0 || fraction > 1)
        {
            try
            {
                line.getLocation(fraction * length);
                fail("getLocation should have thrown a DrawRuntimeException");
            }
            catch (DrawRuntimeException dre)
            {
                // Ignore expected exception
            }
            try
            {
                line.getLocationFraction(fraction);
                fail("getLocation should have thrown a DrawRuntimeException");
            }
            catch (DrawRuntimeException ne)
            {
                // Ignore expected exception
            }
        }
        else
        {
            checkRay3d(line.getLocation(fraction * length), expectedPoint, expectedPhi, expectedTheta);
            checkRay3d(line.getLocationFraction(fraction), expectedPoint, expectedPhi, expectedTheta);
        }

    }

    /**
     * Verify the location and direction of a DirectedPoint3d.
     * @param dp DirectedPoint3d; the DirectedPoint3d that should be verified
     * @param expectedPoint Point3d; the expected location (or null if location should not be checked)
     * @param expectedPhi double; the expected angle from the X axis
     * @param expectedTheta double; the expected angle from the Z axis
     */
    private void checkRay3d(final Ray3d dp, final Point3d expectedPoint, final double expectedPhi, final double expectedTheta)
    {
        if (null != expectedPoint)
        {
            Point3d p = new Point3d(dp.x, dp.y, dp.z);
            assertEquals("locationExtended(0) returns approximately expected point", 0, expectedPoint.distance(p), 0.1);
        }
        assertEquals("Phi (rotation of projection from X axis)", expectedPhi, dp.getPhi(), 0.001);
        assertEquals("Theta (rotation from Z axis)", expectedTheta, dp.getTheta(), 0.001);
    }

    /**
     * Test the createAndCleanLine3d method.
     * @throws DrawRuntimeException should never happen
     */
    @Test
    public final void cleanTest() throws DrawRuntimeException
    {
        Point3d[] tooShort = new Point3d[] {};
        try
        {
            PolyLine3d.createAndCleanPolyLine3d(tooShort);
            fail("Array with no points should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        tooShort = new Point3d[] { new Point3d(1, 2, 3) };
        try
        {
            PolyLine3d.createAndCleanPolyLine3d(tooShort);
            fail("Array with no points should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        Point3d p0 = new Point3d(1, 2, 3);
        Point3d p1 = new Point3d(4, 5, 6);
        Point3d[] points = new Point3d[] { p0, p1 };
        PolyLine3d result = PolyLine3d.createAndCleanPolyLine3d(points);
        assertTrue("first point is p0", p0.equals(result.get(0)));
        assertTrue("second point is p1", p1.equals(result.get(1)));
        Point3d p1Same = new Point3d(4, 5, 6);
        result = PolyLine3d.createAndCleanPolyLine3d(new Point3d[] { p0, p0, p0, p0, p1Same, p0, p1, p1, p1Same, p1, p1 });
        assertEquals("result should contain 4 points", 4, result.size());
        assertTrue("first point is p0", p0.equals(result.get(0)));
        assertTrue("second point is p1", p1.equals(result.get(1)));
        assertTrue("third point is p0", p0.equals(result.get(0)));
        assertTrue("last point is p1", p1.equals(result.get(1)));
    }

    /**
     * Test the equals method.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void equalsTest() throws DrawRuntimeException
    {
        Point3d p0 = new Point3d(1.1, 2.2, 3.3);
        Point3d p1 = new Point3d(2.1, 2.2, 3.3);
        Point3d p2 = new Point3d(3.1, 2.2, 3.3);

        PolyLine3d line = new PolyLine3d(new Point3d[] { p0, p1, p2 });
        assertTrue("Line3d is equal to itself", line.equals(line));
        assertFalse("Line3d is not equal to null", line.equals(null));
        assertFalse("Line3d is not equals to some other kind of Object", line.equals(new Object()));
        PolyLine3d line2 = new PolyLine3d(new Point3d[] { p0, p1, p2 });
        assertTrue("Line3d is equal ot other Line3d that has the exact same list of Point3d", line.equals(line2));
        Point3d p2Same = new Point3d(3.1, 2.2, 3.3);
        line2 = new PolyLine3d(new Point3d[] { p0, p1, p2Same });
        assertTrue("Line3d is equal ot other Line3d that has the exact same list of Point3d; even if some of "
                + "those point are different instances with the same coordinates", line.equals(line2));
        Point3d p2NotSame = new Point3d(3.1, 2.2, 3.35);
        line2 = new PolyLine3d(new Point3d[] { p0, p1, p2NotSame });
        assertFalse("Line3d is not equal ot other Line3d that differs in one coordinate", line.equals(line2));
        line2 = new PolyLine3d(new Point3d[] { p0, p1, p2, p2NotSame });
        assertFalse("Line3d is not equal ot other Line3d that has more points (but is identical up to the common length)",
                line.equals(line2));
        assertFalse("Line3d is not equal ot other Line3d that has fewer points  (but is identical up to the common length)",
                line2.equals(line));
    }

    /**
     * Test the concatenate method.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void concatenateTest() throws DrawRuntimeException
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
        assertEquals("size is 6", 6, ll.size());
        assertEquals("point 0 is p0", p0, ll.get(0));
        assertEquals("point 1 is p1", p1, ll.get(1));
        assertEquals("point 2 is p2", p2, ll.get(2));
        assertEquals("point 3 is p3", p3, ll.get(3));
        assertEquals("point 4 is p4", p4, ll.get(4));
        assertEquals("point 5 is p5", p5, ll.get(5));

        ll = PolyLine3d.concatenate(l1);
        assertEquals("size is 2", 2, ll.size());
        assertEquals("point 0 is p2", p2, ll.get(0));
        assertEquals("point 1 is p3", p3, ll.get(1));

        try
        {
            PolyLine3d.concatenate(l0, l2);
            fail("Gap should have throw an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        try
        {
            PolyLine3d.concatenate();
            fail("concatenate of empty list should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        // Test concatenate methods with tolerance
        PolyLine3d thirdLine = new PolyLine3d(p4, p5);
        for (double tolerance : new double[] { 0.1, 0.01, 0.001, 0.0001, 0.00001 })
        {
            for (double actualError : new double[] { tolerance * 0.9, tolerance * 1.1 })
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
                        catch (DrawRuntimeException dre)
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                            fail("concatenation with error " + actualError + " and tolerance " + tolerance
                                    + " should not have failed");
                        }
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine, thirdLine);
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
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                        }
                        catch (DrawRuntimeException dre)
                        {
                            // Ignore expected exception
                        }
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine, thirdLine);
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
     * Test the reverse and project methods.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void reverseAndProjectTest() throws DrawRuntimeException
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
        assertEquals("result has size 2", 2, r.size());
        assertEquals("point 0 is p1", p1, r.get(0));
        assertEquals("point 1 is p0", p0, r.get(1));

        PolyLine3d l05 = new PolyLine3d(p0, p1, p2, p3, p4, p5);
        r = l05.reverse();
        assertEquals("result has size 6", 6, r.size());
        assertEquals("point 0 is p5", p5, r.get(0));
        assertEquals("point 1 is p4", p4, r.get(1));
        assertEquals("point 2 is p3", p3, r.get(2));
        assertEquals("point 3 is p2", p2, r.get(3));
        assertEquals("point 4 is p1", p1, r.get(4));
        assertEquals("point 5 is p0", p0, r.get(5));

        PolyLine2d l2d = l05.project();
        assertEquals("result has size 6", 6, l2d.size());
        assertEquals("point 0 is p5", p0.project(), l2d.get(0));
        assertEquals("point 1 is p4", p1.project(), l2d.get(1));
        assertEquals("point 2 is p3", p2.project(), l2d.get(2));
        assertEquals("point 3 is p2", p3.project(), l2d.get(3));
        assertEquals("point 4 is p1", p4.project(), l2d.get(4));
        assertEquals("point 5 is p0", p5.project(), l2d.get(5));

        l05 = new PolyLine3d(p0, p1, p2, p2x, p3, p4, p5);
        l2d = l05.project();
        assertEquals("result has size 6", 6, l2d.size());
        assertEquals("point 0 is p5", p0.project(), l2d.get(0));
        assertEquals("point 1 is p4", p1.project(), l2d.get(1));
        assertEquals("point 2 is p3", p2.project(), l2d.get(2));
        assertEquals("point 3 is p2", p3.project(), l2d.get(3));
        assertEquals("point 4 is p1", p4.project(), l2d.get(4));
        assertEquals("point 5 is p0", p5.project(), l2d.get(5));

        PolyLine3d l22x = new PolyLine3d(p2, p2x);
        try
        {
            l22x.project();
            fail("Projecting a Polyline3d that entirely projects to one point should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the extract and extractFraction methods.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @SuppressWarnings("checkstyle:methodlength")
    @Test
    public final void extractTest() throws DrawRuntimeException
    {
        Point3d p0 = new Point3d(1, 2, 3);
        Point3d p1 = new Point3d(2, 3, 4);
        Point3d p1a = new Point3d(2.01, 3.01, 4.01);
        Point3d p1b = new Point3d(2.02, 3.02, 4.02);
        Point3d p1c = new Point3d(2.03, 3.03, 4.03);
        Point3d p2 = new Point3d(12, 13, 14);

        PolyLine3d l = new PolyLine3d(p0, p1);
        PolyLine3d e = l.extractFractional(0, 1);
        assertEquals("size of extraction is 2", 2, e.size());
        assertEquals("point 0 is p0", p0, e.get(0));
        assertEquals("point 1 is p1", p1, e.get(1));
        try
        {
            l.extractFractional(-0.1, 1);
            fail("negative start should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(Double.NaN, 1);
            fail("NaN start should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0, 1.1);
            fail("end > 1 should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0, Double.NaN);
            fail("NaN end should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0.6, 0.4);
            fail("start > end should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(-0.1, 1);
            fail("negative start should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(Double.NaN, 1);
            fail("NaN start should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0, l.getLength() + 0.1);
            fail("end > length should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0, Double.NaN);
            fail("NaN end should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0.6, 0.4);
            fail("start > end should have thrown an exception");
        }
        catch (DrawRuntimeException exception)
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
                for (PolyLine3d extractedLine : new PolyLine3d[] { l.extract(start, end),
                        l.extractFractional(1.0 * i / 10, 1.0 * j / 10) })
                {
                    assertEquals("size of extract is 2", 2, extractedLine.size());
                    assertEquals("x of 0", p0.x + (p1.x - p0.x) * i / 10, extractedLine.get(0).x, 0.0001);
                    assertEquals("y of 0", p0.y + (p1.y - p0.y) * i / 10, extractedLine.get(0).y, 0.0001);
                    assertEquals("z of 0", p0.z + (p1.z - p0.z) * i / 10, extractedLine.get(0).z, 0.0001);
                    assertEquals("x of 1", p0.x + (p1.x - p0.x) * j / 10, extractedLine.get(1).x, 0.0001);
                    assertEquals("y of 1", p0.y + (p1.y - p0.y) * j / 10, extractedLine.get(1).y, 0.0001);
                    assertEquals("z of 1", p0.z + (p1.z - p0.z) * j / 10, extractedLine.get(1).z, 0.0001);
                }
            }
        }

        for (PolyLine3d line : new PolyLine3d[] { new PolyLine3d(p0, p1, p2), new PolyLine3d(p0, p1, p1a, p1b, p1c, p2) })
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
                    for (PolyLine3d extractedLine : new PolyLine3d[] { line.extract(start, end),
                            line.extractFractional(1.0 * i / 110, 1.0 * j / 110) })
                    {
                        int expectedSize = i < 10 && j > 10 ? line.size() : 2;
                        assertEquals("size is " + expectedSize, expectedSize, extractedLine.size());
                        if (i < 10)
                        {
                            assertEquals("x of 0", p0.x + (p1.x - p0.x) * i / 10, extractedLine.get(0).x, 0.0001);
                            assertEquals("y of 0", p0.y + (p1.y - p0.y) * i / 10, extractedLine.get(0).y, 0.0001);
                            assertEquals("z of 0", p0.z + (p1.z - p0.z) * i / 10, extractedLine.get(0).z, 0.0001);
                        }
                        else
                        {
                            assertEquals("x of 0", p1.x + (p2.x - p1.x) * (i - 10) / 100, extractedLine.get(0).x, 0.0001);
                            assertEquals("y of 0", p1.y + (p2.y - p1.y) * (i - 10) / 100, extractedLine.get(0).y, 0.0001);
                            assertEquals("z of 0", p1.z + (p2.z - p1.z) * (i - 10) / 100, extractedLine.get(0).z, 0.0001);
                        }
                        if (j < 10)
                        {
                            assertEquals("x of 1", p0.x + (p1.x - p0.x) * j / 10, extractedLine.get(1).x, 0.0001);
                            assertEquals("y of 1", p0.y + (p1.y - p0.y) * j / 10, extractedLine.get(1).y, 0.0001);
                            assertEquals("z of 1", p0.z + (p1.z - p0.z) * j / 10, extractedLine.get(1).z, 0.0001);
                        }
                        else
                        {
                            assertEquals("x of last", p1.x + (p2.x - p1.x) * (j - 10) / 100, extractedLine.getLast().x, 0.0001);
                            assertEquals("y of last", p1.y + (p2.y - p1.y) * (j - 10) / 100, extractedLine.getLast().y, 0.0001);
                            assertEquals("z of last", p1.z + (p2.z - p1.z) * (j - 10) / 100, extractedLine.getLast().z, 0.0001);
                        }
                        if (extractedLine.size() > 2)
                        {
                            assertEquals("x of mid", p1.x, extractedLine.get(1).x, 0.0001);
                            assertEquals("y of mid", p1.y, extractedLine.get(1).y, 0.0001);
                            assertEquals("z of mid", p1.z, extractedLine.get(1).z, 0.0001);
                        }
                    }
                }
            }
        }
    }

    /**
     * Test other methods of PolyLine3d.
     * @throws DrawRuntimeException should not happen (if it does, this test has failed)
     */
    @Test
    @SuppressWarnings("unlikely-arg-type")
    public final void testOtherMethods() throws DrawRuntimeException
    {
        Point3d[] array =
                new Point3d[] { new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(3.2, 4.1, 5.1), new Point3d(5, 6, 7) };
        PolyLine3d line = new PolyLine3d(Arrays.stream(array).iterator());
        assertEquals("size", array.length, line.size());
        for (int i = 0; i < array.length; i++)
        {
            assertEquals("i-th point", array[i], line.get(i));
        }
        int nextIndex = 0;
        for (Iterator<Point3d> iterator = line.getPoints(); iterator.hasNext();)
        {
            assertEquals("i-th point from line iterator", array[nextIndex++], iterator.next());
        }
        assertEquals("iterator returned all points", array.length, nextIndex);

        PolyLine3d filtered = line.noiseFilteredLine(0.0);
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

        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(3.2, 4.1, 5.1), new Point3d(1, 2, 3) };
        line = new PolyLine3d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(10);
        assertEquals("size of filtered line is 3", 3, filtered.size());
        assertEquals("first point of filtered line matches", line.getFirst(), filtered.getFirst());
        assertEquals("last point of filtered line matches", line.getLast(), filtered.getLast());
        assertEquals("mid point of filtered line is point 1 of unfiltered line", line.get(1), filtered.get(1));

        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(1.1, 2.1, 3), new Point3d(1, 2, 3) };
        line = new PolyLine3d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(0.5);
        assertEquals("size of filtered line is 3", 3, filtered.size());
        assertEquals("first point of filtered line matches", line.getFirst(), filtered.getFirst());
        assertEquals("last point of filtered line matches", line.getLast(), filtered.getLast());
        assertEquals("mid point of filtered line is point 1 of unfiltered line", line.get(1), filtered.get(1));

        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(3, 4, 5) };
        line = new PolyLine3d(Arrays.stream(array).iterator());
        filtered = line.noiseFilteredLine(10);
        assertEquals("Filtering a two-point line returns that line", line, filtered);

        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5) };
        line = PolyLine3d.createAndCleanPolyLine3d(array);
        assertEquals("cleaned line has 2 points", 2, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(3, 4, 5) };
        line = PolyLine3d.createAndCleanPolyLine3d(array);
        assertEquals("cleaned line has 2 points", 2, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point3d[] { new Point3d(0, -1, 3), new Point3d(1, 2, 4), new Point3d(1, 2, 4), new Point3d(3, 4, 4) };
        line = PolyLine3d.createAndCleanPolyLine3d(array);
        assertEquals("cleaned line has 2 points", 3, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("last point", array[array.length - 1], line.getLast());

        array = new Point3d[] { new Point3d(0, -1, 3), new Point3d(1, 2, 4), new Point3d(1, 2, 4), new Point3d(1, 2, 4),
                new Point3d(3, 4, 5) };
        line = PolyLine3d.createAndCleanPolyLine3d(array);
        assertEquals("cleaned line has 3 points", 3, line.size());
        assertEquals("first point", array[0], line.getFirst());
        assertEquals("mid point", array[1], line.get(1));
        assertEquals("last point", array[array.length - 1], line.getLast());

        try
        {
            PolyLine3d.createAndCleanPolyLine3d(new Point3d[0]);
            fail("Too short array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine3d.createAndCleanPolyLine3d(new Point3d[] { new Point3d(1, 2, 3) });
            fail("Too short array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine3d.createAndCleanPolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3) });
            fail("All duplicate points in array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            PolyLine3d.createAndCleanPolyLine3d(
                    new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(1, 2, 3) });
            fail("All duplicate points in array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 6, 9), new Point3d(8, 9, 15) };
        line = new PolyLine3d(array);

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
        assertEquals("Length of line is about 15.6", 15.6, length, 0.1);

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

        assertEquals("Length of line is 15.6", 15.6, length, 0.1);

        try
        {
            line.getLocationFraction(1.1);
            fail("location beyond length should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
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
            Ray3d ray = line.getLocationExtended(position);
            if (position < length / 2)
            {
                Ray3d expected =
                        new Ray3d(array[0].interpolate(array[1], position / (length / 2)), Math.atan2(4, 3), Math.atan2(5, 6));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
            else
            {
                Ray3d expected = new Ray3d(array[1].interpolate(array[2], (position - length / 2) / (length / 2)),
                        Math.atan2(3, 4), Math.atan2(5, 6));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
            ray = line.getLocationFractionExtended(position / line.getLength());
            if (position < length / 2)
            {
                Ray3d expected =
                        new Ray3d(array[0].interpolate(array[1], position / (length / 2)), Math.atan2(4, 3), Math.atan2(5, 6));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
            else
            {
                Ray3d expected = new Ray3d(array[1].interpolate(array[2], (position - length / 2) / (length / 2)),
                        Math.atan2(3, 4), Math.atan2(5, 6));
                assertTrue("interpolated/extrapolated point", expected.epsilonEquals(ray, 0.0001, 0.00001));
            }
        }

        // Test the projectOrthogonal methods
        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 13) };
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
                        assertTrue("result must be >= 0.0", result >= 0);
                        assertTrue("result must be <= 1.0", result <= 1.0);
                        Ray3d ray = line.getLocationFraction(result);
                        Point3d projected = line.projectOrthogonal(xyz);
                        assertEquals("if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction", ray.x,
                                projected.x, 00001);
                        assertEquals("if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction", ray.y,
                                projected.y, 00001);
                        assertEquals("if fraction is between 0 and 1; projectOrthogonal yiels point at that fraction", ray.z,
                                projected.z, 00001);
                    }
                    else
                    {
                        assertNull("point projects outside line", line.projectOrthogonal(xyz));
                    }
                    result = line.projectOrthogonalFractionalExtended(xyz);
                    if (!Double.isNaN(result))
                    {
                        Point3d resultPoint = line.getLocationFractionExtended(result);
                        if (result >= 0.0 && result <= 1.0)
                        {
                            Point3d closestPointOnLine = line.closestPointOnPolyLine(xyz);
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
                        assertNull("point projects outside extended line", line.projectOrthogonalExtended(xyz));
                        Point3d closestPointOnLine = line.closestPointOnPolyLine(xyz);
                        assertNotNull("closest point is never null", closestPointOnLine);
                        boolean found = false;
                        for (int index = 0; index < line.size(); index++)
                        {
                            Point3d linePoint = line.get(index);
                            if (linePoint.x == closestPointOnLine.x && linePoint.y == closestPointOnLine.y)
                            {
                                found = true;
                            }
                        }
                        assertTrue("closestPointOnLine is one of the construction points of the line", found);
                    }
                    Point3d closestPointOnLine = line.closestPointOnPolyLine(xyz);
                    assertNotNull("closest point is never null", closestPointOnLine);
                }
            }
        }
        Point3d toleranceResultPoint = line.getLocationFraction(-0.01, 0.01);
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
        array = new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10) };
        line = new PolyLine3d(array);
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
                PolyLine3d truncated = line.truncate(to);
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
                    PolyLine3d fragment = line.extract(from, to);
                    Point3d fromPoint = line.getLocation(from);
                    assertTrue("fragment starts at from", fromPoint.epsilonEquals(fragment.getFirst(), 0.00001));
                    Point3d toPoint = line.getLocation(to);
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
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(1, 0, 0), new Point3d(1, 1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate of first point into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 1, 0), new Point3d(1, 1, 1)).hashCode());
        assertNotEquals("hash code takes z coordinate of first point into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 1), new Point3d(1, 1, 1)).hashCode());
        assertNotEquals("hash code takes x coordinate of second point into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(2, 1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate of second point into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 2, 1)).hashCode());
        assertNotEquals("hash code takes z coordinate of second point into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 2)).hashCode());

        // Verify the equals method.
        assertTrue("line is equal to itself", line.equals(line));
        assertFalse("line is not equal to a different line",
                line.equals(new PolyLine3d(new Point3d(123, 456, 789), new Point3d(789, 101112, 987))));
        assertFalse("line is not equal to null", line.equals(null));
        assertFalse("line is not equal to a different kind of object", line.equals("unlikely"));
        assertTrue("Line is equal to line from same set of points", line.equals(new PolyLine3d(line.getPoints())));
        // Make a line that differs only in the very last point
        Point3d[] otherArray = Arrays.copyOf(array, array.length);
        otherArray[otherArray.length - 1] = new Point3d(otherArray[otherArray.length - 1].x,
                otherArray[otherArray.length - 1].y + 5, otherArray[otherArray.length - 1].z);
        PolyLine3d other = new PolyLine3d(otherArray);
        assertFalse("PolyLine3d that differs in y of last point is different", line.equals(other));
    }

    /**
     * Test the find method.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws SecurityException if that happens uncaught; this test has failed
     * @throws NoSuchMethodException if that happens uncaught; this test has failed
     * @throws InvocationTargetException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException if that happens uncaught; this test has failed
     * @throws IllegalAccessException if that happens uncaught; this test has failed
     */
    @Test
    public final void testFind() throws DrawRuntimeException, NoSuchMethodException, SecurityException, IllegalAccessException,
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
            assertTrue("segment starts before pos", line.get(index).x <= pos);
            assertTrue("next segment starts after pos", line.get(index + 1).x >= pos);
        }
        assertEquals("pos 0 returns index 0", 0, line.find(0.0));
    }

    /**
     * Test the truncate method.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     */
    @Test
    public final void testTruncate() throws DrawRuntimeException
    {
        Point3d from = new Point3d(10, 20, 30);
        Point3d to = new Point3d(70, 80, 90);
        double length = from.distance(to);
        PolyLine3d line = new PolyLine3d(from, to);
        PolyLine3d truncatedLine = line.truncate(length);
        assertEquals("Start of line truncated at full length is the same as start of the input line", truncatedLine.get(0),
                from);
        assertEquals("End of line truncated at full length is about the same as end of input line", 0,
                truncatedLine.get(1).distance(to), 0.0001);
        try
        {
            line.truncate(-0.1);
            fail("truncate at negative length should have thrown DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        try
        {
            line.truncate(length + 0.1);
            fail("truncate at length beyond length of line should have thrown DrawExDrawRuntimeExceptionception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        truncatedLine = line.truncate(length / 2);
        assertEquals("Start of truncated line is the same as start of the input line", truncatedLine.get(0), from);
        Point3d halfWay = new Point3d((from.x + to.x) / 2, (from.y + to.y) / 2, (from.z + to.z) / 2);
        assertEquals("End of 50%, truncated 2-point line should be at the half way point", 0,
                halfWay.distance(truncatedLine.get(1)), 0.0001);
        Point3d intermediatePoint = new Point3d(20, 20, 20);
        line = new PolyLine3d(from, intermediatePoint, to);
        length = from.distance(intermediatePoint) + intermediatePoint.distance(to);
        truncatedLine = line.truncate(length);
        assertEquals("Start of line truncated at full length is the same as start of the input line", truncatedLine.get(0),
                from);
        assertEquals("End of line truncated at full length is about the same as end of input line", 0,
                truncatedLine.get(2).distance(to), 0.0001);
        truncatedLine = line.truncate(from.distance(intermediatePoint));
        assertEquals("Start of line truncated at full length is the same as start of the input line", truncatedLine.get(0),
                from);
        assertEquals("Line truncated at intermediate point ends at that intermediate point", 0,
                truncatedLine.get(1).distance(intermediatePoint), 0.0001);
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point3d[] points = new Point3d[] { new Point3d(123.456, 345.678, 901.234), new Point3d(234.567, 456.789, 12.345),
                new Point3d(-12.345, -34.567, 45.678) };
        PolyLine3d pl = new PolyLine3d(points);
        String[] out = pl.toExcel().split("\\n");
        assertEquals("Excel output consists of one line per point", points.length, out.length);
        for (int index = 0; index < points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals("each line consists of three fields", 3, fields.length);
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
            try
            {
                double z = Double.parseDouble(fields[2].trim());
                assertEquals("z matches", points[index].z, z, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[2] + " does not parse as a double");
            }
        }
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
        PolyLine3d line = new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10) });
        assertTrue("toString returns something descriptive", line.toString().startsWith("PolyLine3d ["));
        assertTrue("toString can suppress the class name", line.toString().indexOf(line.toString(true)) > 0);

        // Verify that hashCode. Check that the result depends on the actual coordinates.
        assertNotEquals("hash code takes x coordinate into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(1, 0, 0), new Point3d(1, 1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 1, 0), new Point3d(1, 1, 1)).hashCode());
        assertNotEquals("hash code takes z coordinate into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 1), new Point3d(1, 1, 1)).hashCode());
        assertNotEquals("hash code takes x coordinate into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(2, 1, 1)).hashCode());
        assertNotEquals("hash code takes y coordinate into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 2, 1)).hashCode());
        assertNotEquals("hash code takes z coordinate into account",
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)).hashCode(),
                new PolyLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 2)).hashCode());

        // Verify the equals method.
        assertTrue("line is equal to itself", line.equals(line));
        assertFalse("line is not equal to a different line",
                line.equals(new PolyLine2d(new Point2d(123, 456), new Point2d(789, 101112))));
        assertFalse("line is not equal to null", line.equals(null));
        assertFalse("line is not equal to a different kind of object", line.equals("unlikely"));
        assertEquals("equals verbatim copy", line,
                new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10) }));
        assertNotEquals("equals checks x", line,
                new PolyLine3d(new Point3d[] { new Point3d(2, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 10) }));
        assertNotEquals("equals checks y", line,
                new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 7, 8), new Point3d(8, 9, 10) }));
        assertNotEquals("equals checks z", line,
                new PolyLine3d(new Point3d[] { new Point3d(1, 2, 3), new Point3d(4, 6, 8), new Point3d(8, 9, 11) }));
        assertTrue("Line is equal to line from same set of points", line.equals(new PolyLine3d(line.getPoints())));
    }

    /**
     * Test for a problem that occurred in OTS2.
     * @throws DrawRuntimeException when that happens, this test has failed
     */
    @Test
    public void testProjectProblem() throws DrawRuntimeException
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
    @Test
    public void testTransitionLine()
    {
        // Create a Bezier with a 90 degree change of direction starting in X direction, ending in Y direction
        PolyLine3d bezier = Bezier.cubic(64, new Ray3d(-5, 0, 2, 0, 0, 2), new Ray3d(0, 5, 2, 0, 7, 2));
        // System.out.print("c1,0,0" + bezier1.project().toPlot());
        double length = bezier.getLength();
        double prevDir = Double.NaN;
        for (int step = 0; step <= 1000; step++)
        {
            double distance = length * step / 1000;
            Ray3d ray = bezier.getLocation(distance);
            double direction = Math.toDegrees(ray.phi);
            if (step > 0)
            {
                assertEquals("phi changes very little at step " + step, prevDir, direction, 2);
            }
            prevDir = Math.toDegrees(ray.phi);
        }
        // Make a gradually transitioning offset line
        PolyLine3d transitioningOffsetLine = bezier.offsetLine(0, 2);
        // Verify that this curve is fairly smooth
        length = transitioningOffsetLine.getLength();
        prevDir = Double.NaN;
        for (int step = 0; step <= 1000; step++)
        {
            double distance = length * step / 1000;
            Ray3d ray = transitioningOffsetLine.getLocation(distance);
            double direction = Math.toDegrees(ray.phi);
            if (step > 0)
            {
                assertEquals("phi changes very little at step " + step, prevDir, direction, 2);
            }
            prevDir = Math.toDegrees(ray.phi);
        }
        PolyLine3d endLine = bezier.offsetLine(-2);
        // System.out.print("c0,1,0" + endLine.project().toPlot());
        TransitionFunction transitionFunction = new TransitionFunction()
        {
            @Override
            public double function(final double fraction)
            {
                return 0.5 - Math.cos(fraction * Math.PI) / 2;
            }
        };
        PolyLine3d cosineSmoothTransitioningLine = bezier.transitionLine(endLine, transitionFunction);
        // System.out.print("c0,0,0" + cosineSmoothTransitioningLine.project().toPlot());
        length = cosineSmoothTransitioningLine.getLength();
        prevDir = Double.NaN;
        for (int step = 0; step <= 1000; step++)
        {
            double distance = length * step / 1000;
            Ray3d ray = cosineSmoothTransitioningLine.getLocation(distance);
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
        PolyLine3d cosineSmoothTransitioningLine2 =
                endLine.reverse().transitionLine(bezier.reverse(), transitionFunction).reverse();
        // Check that those lines are very similar
        assertEquals("Lengths are equal", cosineSmoothTransitioningLine.getLength(), cosineSmoothTransitioningLine2.getLength(),
                0.001);
        for (int step = 0; step <= 1000; step++)
        {
            Ray3d ray1 = cosineSmoothTransitioningLine.getLocation(step * cosineSmoothTransitioningLine.getLength() / 1000);
            Ray3d ray2 = cosineSmoothTransitioningLine2.getLocation(step * cosineSmoothTransitioningLine2.getLength() / 1000);
            assertEquals("rays are almost equal in x", ray1.x, ray2.x, 0.001);
            assertEquals("rays are almost equal in y", ray1.y, ray2.y, 0.001);
            assertEquals("rays are almost equal in z", ray1.z, ray2.z, 0.001);
            assertEquals("rays are almost equal in phi", ray1.phi, ray2.phi, 0.0001);
            assertEquals("rays are almost equal in theta", ray1.theta, ray2.theta, 0.0001);
        }

        assertEquals("offset by zero returns original", bezier, bezier.offsetLine(0, 0));
        assertEquals("offset by constant with two arguments returns same as offset with one argument", bezier.offsetLine(3, 3),
                bezier.offsetLine(3));
    }

    /**
     * Draw a X marker.
     * @param x double; x location
     * @param y double; y location
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
     * @throws DrawRuntimeException when that happens this test has triggered the problem
     */
    @Test
    public void testOTS2Problem() throws DrawRuntimeException
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
