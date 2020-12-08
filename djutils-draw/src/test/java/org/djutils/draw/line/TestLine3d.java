package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * TestLine3d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TestLine3d
{
    /**
     * Test the constructors of Line3d.
     * @throws DrawException on failure
     */
    @Test
    public final void constructorsTest() throws DrawException
    {
        double[] values = {-999, 0, 99, 9999}; // Keep this list short; execution time grows with 9th power of length
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
     * Test all the constructors of Point3d.
     * @param points Point3d[]; array of Point3d to test with
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    private void runConstructors(final Point3d[] points) throws DrawException
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
        double length = 0;
        for (int i = 1; i < points.length; i++)
        {
            length += Math.sqrt(
                    Math.pow(points[i].getX() - points[i - 1].getX(), 2) + Math.pow(points[i].getY() - points[i - 1].getY(), 2)
                            + Math.pow(points[i].getZ() - points[i - 1].getZ(), 2));
        }
        assertEquals("length", length, line.getLength(), 10 * Math.ulp(length));

        // Construct a Path3D.Double that contains the horizontal moveto or lineto
        Path2D path = new Path2D.Double();
        path.moveTo(points[0].getX(), points[0].getY());
        // System.out.print("path is "); printPath2D(path);
        for (int i = 1; i < points.length; i++)
        {
            // Path2D is corrupt if same point is added twice in succession
            if (points[i].getX() != points[i - 1].getX() || points[i].getY() != points[i - 1].getY())
            {
                path.lineTo(points[i].getX(), points[i].getY());
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
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    private void verifyPoints(final PolyLine3d line, final Point3d[] points) throws DrawException
    {
        assertEquals("Line should have same number of points as point array", line.size(), points.length);
        for (int i = 0; i < points.length; i++)
        {
            assertEquals("x of point i should match", points[i].getX(), line.get(i).getX(), Math.ulp(points[i].getX()));
            assertEquals("y of point i should match", points[i].getY(), line.get(i).getY(), Math.ulp(points[i].getY()));
            assertEquals("z of point i should match", points[i].getZ(), line.get(i).getZ(), Math.ulp(points[i].getZ()));
        }
    }

    /**
     * Test that exception is thrown when it should be.
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    @Test
    public final void exceptionTest() throws DrawException
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
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    @Test
    public final void locationExtendedTest() throws DrawException
    {
        Point3d p0 = new Point3d(10, 20, 30);
        Point3d p1 = new Point3d(40, 50, 60);
        Point3d p2 = new Point3d(90, 80, 70);
        PolyLine3d l = new PolyLine3d(new Point3d[] {p0, p1, p2});
        checkGetLocation(l, -10, null, Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX()));
        checkGetLocation(l, -0.0001, p0, Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX()));
        checkGetLocation(l, 0, p0, Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX()));
        checkGetLocation(l, 0.0001, p0, Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX()));
        checkGetLocation(l, 0.9999, p2, Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        checkGetLocation(l, 1, p2, Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        checkGetLocation(l, 1.0001, p2, Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        checkGetLocation(l, 10, null, Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
    }

    /**
     * Check the location returned by the various location methods.
     * @param line Line3d; the line
     * @param fraction double; relative position to check
     * @param expectedPoint Point3d; expected location of the result
     * @param expectedZRotation double; expected Z rotation of the result
     * @throws DrawException on failure
     */
    private void checkGetLocation(final PolyLine3d line, final double fraction, final Point3d expectedPoint,
            final double expectedZRotation) throws DrawException
    {
        double length = line.getLength();
        checkDirectedPoint3d(line.getLocationExtended(fraction * length), expectedPoint, expectedZRotation);
        if (fraction < 0 || fraction > 1)
        {
            try
            {
                line.getLocation(fraction * length);
                fail("getLocation should have thrown a DrawException");
            }
            catch (DrawException ne)
            {
                // Ignore expected exception
            }
            try
            {
                line.getLocationFraction(fraction);
                fail("getLocation should have thrown a DrawException");
            }
            catch (DrawException ne)
            {
                // Ignore expected exception
            }
        }
        else
        {
            checkDirectedPoint3d(line.getLocation(fraction * length), expectedPoint, expectedZRotation);
            checkDirectedPoint3d(line.getLocationFraction(fraction), expectedPoint, expectedZRotation);
        }

    }

    /**
     * Verify the location and direction of a DirectedPoint3d.
     * @param dp DirectedPoint3d; the DirectedPoint3d that should be verified
     * @param expectedPoint Point3d; the expected location (or null if location should not be checked)
     * @param expectedZRotation double; the expected Z rotation
     */
    private void checkDirectedPoint3d(final DirectedPoint3d dp, final Point3d expectedPoint, final double expectedZRotation)
    {
        if (null != expectedPoint)
        {
            Point3d p = new Point3d(dp.getX(), dp.getY(), dp.getZ());
            assertEquals("locationExtended(0) returns approximately expected point", 0, expectedPoint.distance(p), 0.1);
        }
        assertEquals("z-rotation at 0", expectedZRotation, dp.getDirZ(), 0.001);
    }

    /**
     * Test the createAndCleanLine3d method.
     * @throws DrawException should never happen
     */
    @Test
    public final void cleanTest() throws DrawException
    {
        Point3d[] tooShort = new Point3d[] {};
        try
        {
            PolyLine3d.createAndCleanLine3d(tooShort);
            fail("Array with no points should have thrown an exception");
        }
        catch (DrawException ne)
        {
            // Ignore expected exception
        }
        tooShort = new Point3d[] {new Point3d(1, 2, 3)};
        try
        {
            PolyLine3d.createAndCleanLine3d(tooShort);
            fail("Array with no points should have thrown an exception");
        }
        catch (DrawException ne)
        {
            // Ignore expected exception
        }
        Point3d p0 = new Point3d(1, 2, 3);
        Point3d p1 = new Point3d(4, 5, 6);
        Point3d[] points = new Point3d[] {p0, p1};
        PolyLine3d result = PolyLine3d.createAndCleanLine3d(points);
        assertTrue("first point is p0", p0.equals(result.get(0)));
        assertTrue("second point is p1", p1.equals(result.get(1)));
        Point3d p1Same = new Point3d(4, 5, 6);
        result = PolyLine3d.createAndCleanLine3d(new Point3d[] {p0, p0, p0, p0, p1Same, p0, p1, p1, p1Same, p1, p1});
        assertEquals("result should contain 4 points", 4, result.size());
        assertTrue("first point is p0", p0.equals(result.get(0)));
        assertTrue("second point is p1", p1.equals(result.get(1)));
        assertTrue("third point is p0", p0.equals(result.get(0)));
        assertTrue("last point is p1", p1.equals(result.get(1)));
    }

    /**
     * Test the equals method.
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    @Test
    public final void equalsTest() throws DrawException
    {
        Point3d p0 = new Point3d(1.1, 2.2, 3.3);
        Point3d p1 = new Point3d(2.1, 2.2, 3.3);
        Point3d p2 = new Point3d(3.1, 2.2, 3.3);

        PolyLine3d line = new PolyLine3d(new Point3d[] {p0, p1, p2});
        assertTrue("Line3d is equal to itself", line.equals(line));
        assertFalse("Line3d is not equal to null", line.equals(null));
        assertFalse("Line3d is not equals to some other kind of Object", line.equals(new Object()));
        PolyLine3d line2 = new PolyLine3d(new Point3d[] {p0, p1, p2});
        assertTrue("Line3d is equal ot other Line3d that has the exact same list of Point3d", line.equals(line2));
        Point3d p2Same = new Point3d(3.1, 2.2, 3.3);
        line2 = new PolyLine3d(new Point3d[] {p0, p1, p2Same});
        assertTrue("Line3d is equal ot other Line3d that has the exact same list of Point3d; even if some of "
                + "those point are different instances with the same coordinates", line.equals(line2));
        Point3d p2NotSame = new Point3d(3.1, 2.2, 3.35);
        line2 = new PolyLine3d(new Point3d[] {p0, p1, p2NotSame});
        assertFalse("Line3d is not equal ot other Line3d that differs in one coordinate", line.equals(line2));
        line2 = new PolyLine3d(new Point3d[] {p0, p1, p2, p2NotSame});
        assertFalse("Line3d is not equal ot other Line3d that has more points (but is identical up to the common length)",
                line.equals(line2));
        assertFalse("Line3d is not equal ot other Line3d that has fewer points  (but is identical up to the common length)",
                line2.equals(line));
    }

    /**
     * Test the concatenate method.
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    @Test
    public final void concatenateTest() throws DrawException
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
        catch (DrawException e)
        {
            // Ignore expected exception
        }
        try
        {
            PolyLine3d.concatenate();
            fail("concatenate of empty list should have thrown an exception");
        }
        catch (DrawException e)
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
                    PolyLine3d otherLine = new PolyLine3d(new Point3d(p2.getX() + dx, p2.getY() + dy, p2.getZ()), p3, p4);
                    if (actualError < tolerance)
                    {
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                        }
                        catch (DrawException oge)
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine);
                            fail("concatenation with error " + actualError + " and tolerance " + tolerance
                                    + " should not have failed");
                        }
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine, thirdLine);
                        }
                        catch (DrawException oge)
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
                        catch (DrawException oge)
                        {
                            // Ignore expected exception
                        }
                        try
                        {
                            PolyLine3d.concatenate(tolerance, l0, otherLine, thirdLine);
                        }
                        catch (DrawException oge)
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
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    @Test
    public final void reverseAndProjectTest() throws DrawException
    {
        Point3d p0 = new Point3d(1.1, 2.21, 3.1);
        Point3d p1 = new Point3d(2.1, 2.22, 3.2);
        Point3d p2 = new Point3d(3.1, 2.23, 3.3);
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
        //TODO make project fail
    }

    /**
     * Test the extract and extractFraction methods.
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    @SuppressWarnings("checkstyle:methodlength")
    @Test
    public final void extractTest() throws DrawException
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
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(Double.NaN, 1);
            fail("NaN start should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0, 1.1);
            fail("end > 1 should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0, Double.NaN);
            fail("NaN end should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extractFractional(0.6, 0.4);
            fail("start > end should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(-0.1, 1);
            fail("negative start should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(Double.NaN, 1);
            fail("NaN start should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0, l.getLength() + 0.1);
            fail("end > length should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0, Double.NaN);
            fail("NaN end should have thrown an exception");
        }
        catch (DrawException exception)
        {
            // Ignore expected exception
        }
        try
        {
            l.extract(0.6, 0.4);
            fail("start > end should have thrown an exception");
        }
        catch (DrawException exception)
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
                    assertEquals("size of extract is 2", 2, extractedLine.size());
                    assertEquals("x of 0", p0.getX() + (p1.getX() - p0.getX()) * i / 10, extractedLine.get(0).getX(), 0.0001);
                    assertEquals("y of 0", p0.getY() + (p1.getY() - p0.getY()) * i / 10, extractedLine.get(0).getY(), 0.0001);
                    assertEquals("z of 0", p0.getZ() + (p1.getZ() - p0.getZ()) * i / 10, extractedLine.get(0).getZ(), 0.0001);
                    assertEquals("x of 1", p0.getX() + (p1.getX() - p0.getX()) * j / 10, extractedLine.get(1).getX(), 0.0001);
                    assertEquals("y of 1", p0.getY() + (p1.getY() - p0.getY()) * j / 10, extractedLine.get(1).getY(), 0.0001);
                    assertEquals("z of 1", p0.getZ() + (p1.getZ() - p0.getZ()) * j / 10, extractedLine.get(1).getZ(), 0.0001);
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
                        assertEquals("size is " + expectedSize, expectedSize, extractedLine.size());
                        if (i < 10)
                        {
                            assertEquals("x of 0", p0.getX() + (p1.getX() - p0.getX()) * i / 10, extractedLine.get(0).getX(),
                                    0.0001);
                            assertEquals("y of 0", p0.getY() + (p1.getY() - p0.getY()) * i / 10, extractedLine.get(0).getY(),
                                    0.0001);
                            assertEquals("z of 0", p0.getZ() + (p1.getZ() - p0.getZ()) * i / 10, extractedLine.get(0).getZ(),
                                    0.0001);
                        }
                        else
                        {
                            assertEquals("x of 0", p1.getX() + (p2.getX() - p1.getX()) * (i - 10) / 100,
                                    extractedLine.get(0).getX(), 0.0001);
                            assertEquals("y of 0", p1.getY() + (p2.getY() - p1.getY()) * (i - 10) / 100,
                                    extractedLine.get(0).getY(), 0.0001);
                            assertEquals("z of 0", p1.getZ() + (p2.getZ() - p1.getZ()) * (i - 10) / 100,
                                    extractedLine.get(0).getZ(), 0.0001);
                        }
                        if (j < 10)
                        {
                            assertEquals("x of 1", p0.getX() + (p1.getX() - p0.getX()) * j / 10, extractedLine.get(1).getX(),
                                    0.0001);
                            assertEquals("y of 1", p0.getY() + (p1.getY() - p0.getY()) * j / 10, extractedLine.get(1).getY(),
                                    0.0001);
                            assertEquals("z of 1", p0.getZ() + (p1.getZ() - p0.getZ()) * j / 10, extractedLine.get(1).getZ(),
                                    0.0001);
                        }
                        else
                        {
                            assertEquals("x of last", p1.getX() + (p2.getX() - p1.getX()) * (j - 10) / 100,
                                    extractedLine.getLast().getX(), 0.0001);
                            assertEquals("y of last", p1.getY() + (p2.getY() - p1.getY()) * (j - 10) / 100,
                                    extractedLine.getLast().getY(), 0.0001);
                            assertEquals("z of last", p1.getZ() + (p2.getZ() - p1.getZ()) * (j - 10) / 100,
                                    extractedLine.getLast().getZ(), 0.0001);
                        }
                        if (extractedLine.size() > 2)
                        {
                            assertEquals("x of mid", p1.getX(), extractedLine.get(1).getX(), 0.0001);
                            assertEquals("y of mid", p1.getY(), extractedLine.get(1).getY(), 0.0001);
                            assertEquals("z of mid", p1.getZ(), extractedLine.get(1).getZ(), 0.0001);
                        }
                    }
                }
            }
        }
    }

    /**
     * Test the noiseFilteredLine method.
     * @throws DrawException should not happen (if it does, this test has failed)
     */
    @Test
    public final void testFilter() throws DrawException
    {
        Point3d from = new Point3d(1, 2, 3);
        Point3d to = new Point3d(4, 3, 2);
        for (int steps = 0; steps < 10; steps++)
        {
            List<Point3d> points = new ArrayList<>(2 + steps);
            points.add(from);
            for (int i = 0; i < steps; i++)
            {
                points.add(from.interpolate(to, 1.0 * (i + 1) / (steps + 2)));
            }
            points.add(to);
            PolyLine3d line = new PolyLine3d(points);
            // System.out.println("ref: " + line);
            double segmentLength = line.getFirst().distance(line.get(1));
            PolyLine3d filteredLine = line.noiseFilteredLine(segmentLength * 0.9);
            assertEquals("filtering with a filter that is smaller than any segment should return the original", line.size(),
                    filteredLine.size());
            filteredLine = line.noiseFilteredLine(segmentLength * 1.1);
            int expectedSize = 2 + steps / 2;
            assertEquals("filtering with a filter slightly larger than each segment should return a line with " + expectedSize
                    + " points", expectedSize, filteredLine.size());
            filteredLine = line.noiseFilteredLine(segmentLength * 2.1);
            // System.out.println("flt: " + filteredLine);
            expectedSize = 2 + (steps - 1) / 3;
            assertEquals("filtering with a filter slightly larger than twice the length of each segment should return a "
                    + "line with " + expectedSize + " points", expectedSize, filteredLine.size());
            // Special case where start and end point are equal and all intermediate points are within the margin
            points.clear();
            points.add(from);
            for (int i = 1; i < 10; i++)
            {
                points.add(new Point3d(from.getX() + 0.0001 * i, from.getY() + 0.0001 * i, from.getZ()));
            }
            points.add(from);
            line = new PolyLine3d(points);
            filteredLine = line.noiseFilteredLine(0.2);
            assertEquals("filter returns line of three points", 3, filteredLine.size());
            assertEquals("first point matches", from, filteredLine.getFirst());
            assertEquals("last point matches", from, filteredLine.getLast());
            assertNotEquals("intermediate point differs from first point", from, filteredLine.get(1));
        }
    }

    /**
     * Test the find method.
     * @throws DrawException if that happens uncaught; this test has failed
     * @throws SecurityException if that happens uncaught; this test has failed
     * @throws NoSuchMethodException if that happens uncaught; this test has failed
     * @throws InvocationTargetException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException if that happens uncaught; this test has failed
     * @throws IllegalAccessException if that happens uncaught; this test has failed
     */
    @Test
    public final void testFind() throws DrawException, NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        // Construct a line with exponentially increasing distances
        List<Point3d> points = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            points.add(new Point3d(Math.pow(2, i) - 1, 10, 20));
        }
        PolyLine3d line = new PolyLine3d(points);
        double end = points.get(points.size() - 1).getX();
        for (int i = 0; i < end; i++)
        {
            double pos = i + 0.5;
            int index = line.find(pos);
            assertTrue("segment starts before pos", line.get(index).getX() <= pos);
            assertTrue("next segment starts after pos", line.get(index + 1).getX() >= pos);
        }
        assertEquals("pos 0 returns index 0", 0, line.find(0.0));
    }

    /**
     * Test the truncate method.
     * @throws DrawException if that happens uncaught; this test has failed
     */
    @Test
    public final void testTruncate() throws DrawException
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
            fail("truncate at negative length should have thrown DrawException");
        }
        catch (DrawException e)
        {
            // Ignore expected exception
        }
        try
        {
            line.truncate(length + 0.1);
            fail("truncate at length beyond length of line should have thrown DrawException");
        }
        catch (DrawException e)
        {
            // Ignore expected exception
        }
        truncatedLine = line.truncate(length / 2);
        assertEquals("Start of truncated line is the same as start of the input line", truncatedLine.get(0), from);
        Point3d halfWay =
                new Point3d((from.getX() + to.getX()) / 2, (from.getY() + to.getY()) / 2, (from.getZ() + to.getZ()) / 2);
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
     * Test the orthogonal projection on a line.
     * @throws DrawException when line construction fails
     */
//    @Test
//    public void testOrthogonalProjection() throws DrawException
//    {
//        Line3d line = new Line3d(new Point3d(0, 0, 0), new Point3d(10, 0, 0), new Point3d(10, 5, 0), new Point3d(0, 10, 0));
//        Point3d p = line.getLocationFraction(line.projectOrthogonal(0, 1));
//        assertTrue(new Point3d(0, 0, 0).epsilonEquals(p, 1E-6));
//        p = line.getLocationFraction(line.projectOrthogonal(4, 1));
//        assertTrue(new Point3d(4, 0, 0).epsilonEquals(p, 1E-6));
//        p = line.getLocationFraction(line.projectOrthogonal(10, 10));
//        assertTrue("point " + p, new Point3d(8, 6, 0).epsilonEquals(p, 1E-6));
//    }

}
