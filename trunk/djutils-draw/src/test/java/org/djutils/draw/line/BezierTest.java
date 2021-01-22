package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * Test the B&eacute;zier class.
 * <p>
 * Copyright (c) 2013-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/v2/license.html">OpenTrafficSim License</a>.
 * </p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 2, 2017 <br>
 * @author <a href="https://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class BezierTest
{

    /**
     * Test the various 2d methods in the Bezier class.
     * @throws DrawRuntimeException when this happens uncaught this test has failed
     * @throws DrawException when this happens uncaught; this test has failed
     */
    @Test
    public final void bezierTest2d() throws DrawRuntimeException, DrawException
    {
        Point2d from = new Point2d(10, 0);
        Point2d control1 = new Point2d(20, 0);
        Point2d control2 = new Point2d(00, 20);
        Point2d to = new Point2d(0, 10);
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            PolyLine2d line = Bezier.cubic(n, from, control1, control2, to);
            assertTrue("result has n points", line.size() == n);
            assertTrue("result starts with from", line.get(0).equals(from));
            assertTrue("result ends with to", line.get(line.size() - 1).equals(to));
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point2d p = line.get(i);
                assertTrue("x of intermediate point has reasonable value", p.x > 0 && p.x < 15);
                assertTrue("y of intermediate point has reasonable value", p.y > 0 && p.y < 15);
            }
        }
        for (int n = -1; n <= 1; n++)
        {
            try
            {
                Bezier.cubic(n, from, control1, control2, to);
            }
            catch (DrawRuntimeException e)
            {
                // Ignore expected exception
            }
        }
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            for (double shape : new double[] { 0.5, 1.0, 2.0 })
            {
                for (boolean weighted : new boolean[] { false, true })
                {
                    Ray2d start = new Ray2d(from.x, from.y, Math.PI / 2);
                    Ray2d end = new Ray2d(to.x, to.y, Math.PI);
                    PolyLine2d line = 1.0 == shape ? Bezier.cubic(n, start, end) : Bezier.cubic(n, start, end, shape, weighted);
                    for (int i = 1; i < line.size() - 1; i++)
                    {
                        Point2d p = line.get(i);
                        assertTrue("x of intermediate point has reasonable value", p.x > 0 && p.x < 15);
                        assertTrue("y of intermediate point has reasonable value", p.y > 0 && p.y < 15);
                    }
                }
            }
        }
        // Pity that the value 64 is private in the Bezier class.
        assertEquals("Number of points is 64", 64,
                Bezier.cubic(new Ray2d(from.x, from.y, Math.PI / 2), new Ray2d(to.x, to.y, -Math.PI / 2)).size());
        assertEquals("Number of points is 64", 64, Bezier.bezier(from, control1, control2, to).size());
        control1 = new Point2d(5, 0);
        control2 = new Point2d(0, 5);
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            PolyLine2d line = Bezier.cubic(n, from, control1, control2, to);
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point2d p = line.get(i);
                // System.out.println("Point " + i + " of " + n + " is " + p);
                assertTrue("x of intermediate point has reasonable value", p.x > 0 && p.x < 10);
                assertTrue("y of intermediate point has reasonable value", p.y > 0 && p.y < 10);
            }
        }
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            PolyLine2d line = Bezier.cubic(n, new Ray2d(from.x, from.y, Math.PI), new Ray2d(to.x, to.y, Math.PI / 2));
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point2d p = line.get(i);
                assertTrue("x of intermediate point has reasonable value", p.x > 0 && p.x < 10);
                assertTrue("y of intermediate point has reasonable value", p.y > 0 && p.y < 10);
            }
        }

        Point2d start = new Point2d(1, 1);
        Point2d c1 = new Point2d(11, 1);
        // Point2d c3 = new Point2d(5, 1);
        Point2d c2 = new Point2d(1, 11);
        Point2d end = new Point2d(11, 11);
        double autoDistance = start.distance(end) / 2;
        Point2d c1Auto = new Point2d(start.x + autoDistance, start.y);
        Point2d c2Auto = new Point2d(end.x - autoDistance, end.y);
        // Should produce a right leaning S shape; something between a slash and an S
        PolyLine2d reference = Bezier.bezier(256, start, c1, c2, end);
        PolyLine2d referenceAuto = Bezier.bezier(256, start, c1Auto, c2Auto, end);
        // System.out.print("ref " + reference.toPlot());
        Ray2d startRay = new Ray2d(start, start.directionTo(c1));
        Ray2d endRay = new Ray2d(end, c2.directionTo(end));
        for (double epsilonPosition : new double[] { 3, 1, 0.1, 0.05, 0.02 })
        {
            // System.out.println("epsilonPosition " + epsilonPosition);
            PolyLine2d line = Bezier.bezier(epsilonPosition, start, c1, c2, end);
            // System.out.print("epsilonPosition " + epsilonPosition + " yields " + line.toPlot());
            // for (int percent = 0; percent <= 100; percent++)
            // {
            // Ray2d ray = reference.getLocationFraction(percent / 100.0);
            // double position = line.projectRay(ray);
            // Point2d pointAtPosition = line.getLocation(position);
            // double positionError = ray.distance(pointAtPosition);
            // System.out.print(String.format(" %.3f", positionError));
            // if (positionError >= epsilonPosition)
            // {
            // System.out.println();
            // System.out.println("percent " + percent + ", on " + ray + " projected to " + pointAtPosition
            // + " positionError " + positionError);
            // }
            // assertTrue("Actual error " + positionError + " exceeds epsilon " + epsilonPosition,
            // positionError < epsilonPosition);
            // }
            // System.out.println();
            compareBeziers("bezier with 2 explicit control points", reference, line, 100, epsilonPosition);
            line = Bezier.cubic(epsilonPosition, start, c1, c2, end);
            compareBeziers("cubic with 2 explicit control points", reference, line, 100, epsilonPosition);
            line = Bezier.cubic(epsilonPosition, startRay, endRay);
            compareBeziers("cubic with automatic control points", referenceAuto, line, 100, epsilonPosition);
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, 0, true);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, 0);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, -1);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, -1, true);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, Double.NaN, true);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, Double.NaN);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, Double.POSITIVE_INFINITY);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(0.1, startRay, endRay, Double.POSITIVE_INFINITY, true);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(0.1, new Point2d[] { start });
            fail("Too few points have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(0.1, new Point2d[] { });
            fail("Too few points have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(0, start, c1, c2, end);
            fail("illegal epsilon have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(-0.1, start, c1, c2, end);
            fail("illegal epsilon have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(Double.NaN, start, c1, c2, end);
            fail("illegal epsilon have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

    }

    /**
     * Compare B&eacute;zier curve approximations.
     * @param description String; description of the test
     * @param reference PolyLine2d; reference B&eacute;zier curve approximation
     * @param candidate PolyLine2d; candidate B&eacute;zier curve approximation
     * @param numberOfPoints int; number of point to compare the curves at, minus one; this method checks at 0% and at 100%
     * @param epsilon double; upper limit of the distance between the two curves
     * @throws DrawException if that happens uncaught; a test has failed
     */
    public void compareBeziers(final String description, final PolyLine2d reference, final PolyLine2d candidate,
            final int numberOfPoints, final double epsilon) throws DrawException
    {
        for (int step = 0; step <= numberOfPoints; step++)
        {
            double fraction = 1.0 * step / numberOfPoints;
            Ray2d ray = reference.getLocationFraction(fraction);
            double position = candidate.projectRay(ray);
            Point2d pointAtPosition = candidate.getLocation(position);
            double positionError = ray.distance(pointAtPosition);
            if (positionError >= epsilon)
            {
                System.out.println("fraction " + fraction + ", on " + ray + " projected to " + pointAtPosition
                        + " positionError " + positionError);
                System.out.print("connection: " + new PolyLine2d(ray, pointAtPosition).toPlot());
                System.out.print("reference: " + reference.toPlot());
                System.out.print("candidate: " + candidate.toPlot());
            }
            assertTrue(description + " actual error is less than epsilon ", positionError < epsilon);
        }
    }

    /**
     * Test the various 3d methods in the Bezier class.
     * @throws DrawRuntimeException when this happens uncaught this test has failed
     */
    @Test
    public final void bezierTest3d() throws DrawRuntimeException
    {
        Point3d from = new Point3d(10, 0, 0);
        Point3d control1 = new Point3d(20, 0, 10);
        Point3d control2 = new Point3d(0, 20, 20);
        Point3d to = new Point3d(0, 10, 30);
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            PolyLine3d line = Bezier.cubic(n, from, control1, control2, to);
            assertTrue("result has n points", line.size() == n);
            assertTrue("result starts with from", line.get(0).equals(from));
            assertTrue("result ends with to", line.get(line.size() - 1).equals(to));
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point3d p = line.get(i);
                // System.out.println(p);
                assertTrue("z of intermediate point has reasonable value", p.z > line.get(i - 1).z && p.z < line.get(i + 1).z);
                assertTrue("x of intermediate point has reasonable value", p.x > 0 && p.x < 15);
                assertTrue("y of intermediate point has reasonable value", p.y > 0 && p.y < 15);
            }
        }
        for (int n = -1; n <= 1; n++)
        {
            try
            {
                Bezier.cubic(n, from, control1, control2, to);
            }
            catch (DrawRuntimeException e)
            {
                // Ignore expected exception
            }
        }
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            for (double shape : new double[] { 0.5, 1.0, 2.0 })
            {
                for (boolean weighted : new boolean[] { false, true })
                {
                    Ray3d start = new Ray3d(from.x, from.y, from.z, Math.PI / 2, Math.PI / 3);
                    Ray3d end = new Ray3d(to.x, to.y, to.z, Math.PI, 0);
                    PolyLine3d line = 1.0 == shape ? Bezier.cubic(n, start, end) : Bezier.cubic(n, start, end, shape, weighted);
                    for (int i = 1; i < line.size() - 1; i++)
                    {
                        Point3d p = line.get(i);
                        // System.out.println(p);
                        assertTrue("x of intermediate point has reasonable value", p.x > -10 && p.x < 20);
                        assertTrue("y of intermediate point has reasonable value", p.y > -10 && p.y < 30);
                        assertTrue("z of intermediate point has reasonable value", p.z > -10 && p.z < 40);
                    }
                }
            }
        }
        // Pity that the value 64 is private in the Bezier class.
        assertEquals("Number of points is 64", 64, Bezier.cubic(new Ray3d(from.x, from.y, from.z, Math.PI / 2, -Math.PI / 2, 0),
                new Ray3d(to.x, to.y, to.z, Math.PI, 0, -Math.PI / 2)).size());
        assertEquals("Number of points is 64", 64, Bezier.bezier(from, control1, control2, to).size());
        control1 = new Point3d(5, 0, 10);
        control2 = new Point3d(0, 5, 20);
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            PolyLine3d line = Bezier.cubic(n, from, control1, control2, to);
            assertEquals("from x", from.x, line.getFirst().x, 0);
            assertEquals("from y", from.y, line.getFirst().y, 0);
            assertEquals("from z", from.z, line.getFirst().z, 0);
            assertEquals("to x", to.x, line.getLast().x, 0);
            assertEquals("to y", to.y, line.getLast().y, 0);
            assertEquals("to z", to.z, line.getLast().z, 0);
            for (int i = 0; i < line.size() - 1; i++)
            {
                Point3d p = line.get(i);
                // System.out.println(p);
                assertTrue("x of intermediate point has reasonable value", p.x > -10 && p.x < 20);
                assertTrue("y of intermediate point has reasonable value", p.y > -10 && p.y < 30);
                assertTrue("z of intermediate point has reasonable value", p.z > -10 && p.z < 30);
            }
        }
        for (int n : new int[] { 2, 3, 4, 100 })
        {
            PolyLine3d line = Bezier.cubic(n, new Ray3d(from.x, from.y, from.z, Math.PI / 2, Math.PI / 2, Math.PI),
                    new Ray3d(to.x, to.y, to.z, 0, 0, Math.PI / 2));
            for (int i = 0; i < line.size() - 1; i++)
            {
                Point3d p = line.get(i);
                // System.out.println(p);
                assertTrue("x of intermediate point has reasonable value", p.x > -10 && p.x < 20);
                assertTrue("y of intermediate point has reasonable value", p.y > -10 && p.y < 30);
                assertTrue("z of intermediate point has reasonable value", p.z > -10 && p.z < 30);
            }
        }
    }

    /**
     * Test the various exceptions of the 2d methods in the Bezier class.
     */
    @Test
    public void testExceptions2d()
    {
        Ray2d ray1 = new Ray2d(2, 3, 4);
        Ray2d ray2 = new Ray2d(2, 3, 5);
        Ray2d ray3 = new Ray2d(4, 5, 6);
        Point2d cp1 = new Point2d(2.5, 13.5);
        Point2d cp2 = new Point2d(3.5, 14.5);
        try
        {
            Bezier.cubic(null, ray2);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(ray1, null);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(ray1, ray2);
            fail("Coinciding start and end points should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(Bezier.DEFAULT_BEZIER_SIZE, ray1, ray3, -1);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(Bezier.DEFAULT_BEZIER_SIZE, ray1, ray3, Double.NaN);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(Bezier.DEFAULT_BEZIER_SIZE, ray1, ray3, Double.POSITIVE_INFINITY);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        PolyLine2d result = Bezier.bezier(2, ray1, ray3); // Should succeed
        assertEquals("size should be 2", 2, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);
        result = Bezier.bezier(ray1, ray3); // Should succeed
        assertEquals("size should be default", Bezier.DEFAULT_BEZIER_SIZE, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);
        try
        {
            Bezier.bezier(1, ray1, ray3);
            fail("size smaller than 2 should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(ray1);
            fail("cannot make a Bezier from only one point; should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        result = Bezier.cubic(2, ray1, cp1, cp2, ray3);
        assertEquals("size should be 2", 2, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);
        result = Bezier.cubic(4, ray1, cp1, cp2, ray3);
        assertEquals("size should be 4", 4, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);

        try
        {
            Bezier.cubic(1, ray1, cp1, cp2, ray3);
            fail("Cannot construct a Bezier approximation that has only one point");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

    }

    /**
     * Test the various exceptions of the 3d methods in the Bezier class.
     */
    @Test
    public void testExceptions3d()
    {
        Ray3d ray1 = new Ray3d(2, 3, 4, 5, 6, 7);
        Ray3d ray2 = new Ray3d(2, 3, 4, 7, 9, 11);
        Ray3d ray3 = new Ray3d(4, 5, 6, 1, 2, 3);
        Point3d cp1 = new Point3d(2.5, 13.5, 7);
        Point3d cp2 = new Point3d(3.5, 14.5, 9);
        try
        {
            Bezier.cubic(null, ray2);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(ray1, null);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(ray1, ray2);
            fail("Coinciding start and end points should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(Bezier.DEFAULT_BEZIER_SIZE, ray1, ray3, -1);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(Bezier.DEFAULT_BEZIER_SIZE, ray1, ray3, Double.NaN);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.cubic(Bezier.DEFAULT_BEZIER_SIZE, ray1, ray3, Double.POSITIVE_INFINITY);
            fail("Illegal shape value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        PolyLine3d result = Bezier.bezier(2, ray1, ray3); // Should succeed
        assertEquals("size should be 2", 2, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);
        result = Bezier.bezier(ray1, ray3); // Should succeed
        assertEquals("size should be default", Bezier.DEFAULT_BEZIER_SIZE, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);
        try
        {
            Bezier.bezier(1, ray1, ray3);
            fail("size smaller than 2 should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Bezier.bezier(ray1);
            fail("cannot make a Bezier from only one point; should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        result = Bezier.cubic(2, ray1, cp1, cp2, ray3);
        assertEquals("size should be 2", 2, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);
        result = Bezier.cubic(4, ray1, cp1, cp2, ray3);
        assertEquals("size should be 4", 4, result.size());
        assertEquals("start of result is at start", 0, ray1.distanceSquared(result.getFirst()), 0);
        assertEquals("end of result is at start", 0, ray3.distanceSquared(result.getLast()), 0);

        try
        {
            Bezier.cubic(1, ray1, cp1, cp2, ray3);
            fail("Cannot construct a Bezier approximation that has only one point");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

    }

}
