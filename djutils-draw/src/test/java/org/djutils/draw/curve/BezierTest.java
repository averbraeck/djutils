package org.djutils.draw.curve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Export;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.line.Ray2d;
import org.djutils.draw.line.Ray3d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.junit.jupiter.api.Test;

/**
 * Test the B&eacute;zier class.
 * <p>
 * Copyright (c) 2013-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
     * @throws DrawRuntimeException when this happens uncaught; this test has failed
     */
    @Test
    public final void bezierTest2d() throws DrawRuntimeException, DrawRuntimeException
    {
        Point2d from = new Point2d(10, 0);
        Point2d control1 = new Point2d(20, 0);
        Point2d control2 = new Point2d(00, 20);
        Point2d to = new Point2d(0, 10);
        for (int n : new int[] {2, 3, 4, 100})
        {
            PolyLine2d line = new BezierCubic2d(from, control1, control2, to).toPolyLine(new Flattener2d.NumSegments(n - 1));
            assertTrue(line.size() == n, "result has n points");
            assertTrue(line.get(0).equals(from), "result starts with from");
            assertTrue(line.get(line.size() - 1).equals(to), "result ends with to");
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point2d p = line.get(i);
                assertTrue(p.x > 0 && p.x < 15, "x of intermediate point has reasonable value");
                assertTrue(p.y > 0 && p.y < 15, "y of intermediate point has reasonable value");
            }
        }
        for (int n = -1; n < 1; n++)
        {
            try
            {
                new BezierCubic2d(from, control1, control2, to).toPolyLine(new Flattener2d.NumSegments(n));
                fail("Illegal number of segments should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException iae)
            {
                // Ignore expected exception
            }
        }
        for (int n : new int[] {2, 3, 4, 100})
        {
            for (double shape : new double[] {0.5, 1.0, 2.0})
            {
                for (boolean weighted : new boolean[] {false, true})
                {
                    Ray2d start = new Ray2d(from.x, from.y, Math.PI / 2);
                    Ray2d end = new Ray2d(to.x, to.y, Math.PI);
                    BezierCubic2d cbc =
                            1.0 == shape ? new BezierCubic2d(start, end) : new BezierCubic2d(start, end, shape, weighted);
                    PolyLine2d line = cbc.toPolyLine(new Flattener2d.NumSegments(n));
                    for (int i = 1; i < line.size() - 1; i++)
                    {
                        Point2d p = line.get(i);
                        assertTrue(p.x > 0 && p.x < 15, "x of intermediate point has reasonable value");
                        assertTrue(p.y > 0 && p.y < 15, "y of intermediate point has reasonable value");
                    }
                }
            }
        }
        control1 = new Point2d(5, 0);
        control2 = new Point2d(0, 5);
        for (int n : new int[] {2, 3, 4, 100})
        {
            PolyLine2d line = new BezierCubic2d(from, control1, control2, to).toPolyLine(new Flattener2d.NumSegments(n));
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point2d p = line.get(i);
                // System.out.println("Point " + i + " of " + n + " is " + p);
                assertTrue(p.x > 0 && p.x < 10, "x of intermediate point has reasonable value");
                assertTrue(p.y > 0 && p.y < 10, "y of intermediate point has reasonable value");
            }
        }
        for (int n : new int[] {2, 3, 4, 100})
        {
            PolyLine2d line = new BezierCubic2d(new Ray2d(from.x, from.y, Math.PI), new Ray2d(to.x, to.y, Math.PI / 2))
                    .toPolyLine(new Flattener2d.NumSegments(n));
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point2d p = line.get(i);
                assertTrue(p.x > 0 && p.x < 10, "x of intermediate point has reasonable value");
                assertTrue(p.y > 0 && p.y < 10, "y of intermediate point has reasonable value");
            }
        }

        Point2d start = new Point2d(1, 1);
        Point2d c1 = new Point2d(11, 1);
        // Point2d c3 = new Point2d(5, 1);
        Point2d c2 = new Point2d(1, 11);
        Point2d end = new Point2d(11, 11);
        // double autoDistance = start.distance(end) / 2;
        // Point2d c1Auto = new Point2d(start.x + autoDistance, start.y);
        // Point2d c2Auto = new Point2d(end.x - autoDistance, end.y);
        // Should produce a right leaning S shape; something between a slash and an S
        // PolyLine2d reference = new BezierCubic2d(start, c1, c2, end).toPolyLine(new Flattener2d.NumSegments(256));
        // PolyLine2d referenceAuto = new BezierCubic2d(start, c1Auto, c2Auto, end).toPolyLine(new
        // Flattener2d.NumSegments(256));
        // // System.out.print("ref " + reference.toPlot());
        Ray2d startRay = new Ray2d(start, start.directionTo(c1));
        Ray2d endRay = new Ray2d(end, c2.directionTo(end));
        // for (double epsilonPosition : new double[] {3, 1, 0.1, 0.05, 0.02})
        // {
        // // System.out.println("epsilonPosition " + epsilonPosition);
        // PolyLine2d line = new Bezier2d(start, end).toPolyLine(new Flattener2d.MaxDeviation(epsilonPosition));
        // assertEquals(2, line.size(), "Bezier from two points should be 2-point poly line");
        // assertEquals(start, line.getFirst(), "Start point should be start");
        // assertEquals(end, line.getLast(), "End point shoujld be end");
        // line = new Bezier2d(start, c1, c2, end).toPolyLine(new Flattener2d.MaxDeviation(epsilonPosition));
        // compareBeziers("bezier with 2 explicit control points", reference, line, 100, epsilonPosition);
        // line = new BezierCubic2d(start, c1, c2, end).toPolyLine(new Flattener2d.MaxDeviation(epsilonPosition));
        // compareBeziers("cubic with 2 explicit control points", reference, line, 100, epsilonPosition);
        // line = new BezierCubic2d(startRay, endRay).toPolyLine(new Flattener2d.MaxDeviation(epsilonPosition));
        // compareBeziers("cubic with automatic control points", referenceAuto, line, 100, epsilonPosition);
        // }

        try
        {
            new BezierCubic2d(startRay, endRay, 0, true);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, 0);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, -1);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, -1, true);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, Double.NaN, true);
            fail("Illegal shape value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, Double.NaN);
            fail("Illegal shape value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, Double.POSITIVE_INFINITY);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(startRay, endRay, Double.POSITIVE_INFINITY, true);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier2d(new Point2d[] {start});
            fail("Too few points have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier2d(new Point2d[] {});
            fail("Too few points have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier2d(start, c1, c2, end).toPolyLine(new Flattener2d.MaxDeviation(0));
            fail("illegal epsilon have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier2d(start, c1, c2, end).toPolyLine(new Flattener2d.MaxDeviation(-0.1));
            fail("illegal epsilon have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier2d(start, c1, c2, end).toPolyLine(new Flattener2d.MaxDeviation(Double.NaN));
            fail("illegal epsilon have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(new Point2d[] {new Point2d(1, 2), new Point2d(3, 4), new Point2d(5, 6)});
            fail("too few points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(new Point2d[] {new Point2d(1, 2), new Point2d(3, 4), new Point2d(5, 6), new Point2d(7, 8),
                    new Point2d(9, 10)});
            fail("too many points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        // Make a totally straight BezierCubic2d and check the curvature
        Bezier2d bc2d =
                new Bezier2d(new Point2d(0, 0), new Point2d(0, 0), new Point2d(0, 0), new Point2d(0, 0));
        assertTrue(bc2d.curvature(0.2) == Double.POSITIVE_INFINITY, "curvature of a straight curve is infinite");
    }

    /**
     * Compare B&eacute;zier curve approximations.
     * @param description description of the test
     * @param reference reference B&eacute;zier curve approximation
     * @param candidate candidate B&eacute;zier curve approximation
     * @param numberOfPoints number of point to compare the curves at, minus one; this method checks at 0% and at 100%
     * @param epsilon upper limit of the distance between the two curves
     * @throws DrawRuntimeException if that happens uncaught; a test has failed
     */
    public void compareBeziers(final String description, final PolyLine2d reference, final PolyLine2d candidate,
            final int numberOfPoints, final double epsilon) throws DrawRuntimeException
    {
        for (int step = 0; step <= numberOfPoints; step++)
        {
            double fraction = 1.0 * step / numberOfPoints;
            Ray2d referenceRay = new Ray2d(reference.getLocationFraction(fraction));
            double position = candidate.projectRay(referenceRay);
            Point2d pointAtPosition = candidate.getLocation(position);
            double positionError = referenceRay.distance(pointAtPosition);
            if (positionError >= epsilon)
            {
                System.out.println("fraction " + fraction + ", on " + referenceRay + " projected to " + pointAtPosition
                        + " positionError " + positionError);
                System.out.print("connection: " + Export.toPlot(new PolyLine2d(referenceRay, pointAtPosition)));
                System.out.print("reference: " + Export.toPlot(reference));
                System.out.print("candidate: " + Export.toPlot(candidate));
            }
            assertTrue(positionError < epsilon, description + " actual error is less than epsilon ");
        }
    }

    /**
     * Compare B&eacute;zier curve approximations.
     * @param description description of the test
     * @param reference reference B&eacute;zier curve approximation
     * @param candidate candidate B&eacute;zier curve approximation
     * @param numberOfPoints number of point to compare the curves at, minus one; this method checks at 0% and at 100%
     * @param epsilon upper limit of the distance between the two curves
     * @throws DrawRuntimeException if that happens uncaught; a test has failed
     */
    public void compareBeziersDeviation(final String description, final PolyLine3d reference, final PolyLine3d candidate,
            final int numberOfPoints, final double epsilon) throws DrawRuntimeException
    {
        for (int step = 0; step <= numberOfPoints; step++)
        {
            double fraction = 1.0 * step / numberOfPoints;
            Point3d referencePoint = reference.getLocationFraction(fraction);
            Point3d candidatePont = candidate.getLocationFraction(fraction);
            double positionError = referencePoint.distance(candidatePont);
            if (positionError >= epsilon)
            {
                System.out.println(Export.toPlot(reference.project()));
                System.out.println(Export.toPlot(candidate.project()));
                System.out.println("Comparing at fraction " + fraction + ", on " + referencePoint + " compared to "
                        + candidatePont + " positionError " + positionError + " (should be below " + epsilon + ")");
                System.out.print("connection: " + Export.toPlot(new PolyLine3d(referencePoint, candidatePont).project()));
                System.out.print("reference: " + Export.toPlot(reference.project()));
                System.out.print("candidate: " + Export.toPlot(candidate.project()));
            }
            assertTrue(positionError < epsilon, description + " actual error is less than epsilon ");
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
        for (int n : new int[] {2, 3, 4, 100})
        {
            BezierCubic3d cbc = new BezierCubic3d(from, control1, control2, to);
            PolyLine3d line = cbc.toPolyLine(new Flattener3d.NumSegments(n - 1));
            assertTrue(line.size() == n, "result has n points");
            assertTrue(line.get(0).equals(from), "result starts with from");
            assertTrue(line.get(line.size() - 1).equals(to), "result ends with to");
            for (int i = 1; i < line.size() - 1; i++)
            {
                Point3d p = line.get(i);
                // System.out.println(p);
                assertTrue(p.z > line.get(i - 1).z && p.z < line.get(i + 1).z, "z of intermediate point has reasonable value");
                assertTrue(p.x > 0 && p.x < 15, "x of intermediate point has reasonable value");
                assertTrue(p.y > 0 && p.y < 15, "y of intermediate point has reasonable value");
            }
            assertEquals(from.directionTo(control1).dirY, cbc.getStartDirection().dirY, 0.0001, "start direction is reported");
            assertEquals(from.directionTo(control1).dirZ, cbc.getStartDirection().dirZ, 0.0001, "start direction is reported");
            assertEquals(control2.directionTo(to).dirY, cbc.getEndDirection().dirY, 0.0001, "end direction is reported");
            assertEquals(control2.directionTo(to).dirZ, cbc.getEndDirection().dirZ, 0.0001, "end direction is reported");
        }
        for (int n = -1; n < 1; n++)
        {
            try
            {
                new BezierCubic3d(from, control1, control2, to).toPolyLine(new Flattener3d.NumSegments(n));
                fail("Illegal number of segments should have thrown an IllegalArgumentException");
            }
            catch (IllegalArgumentException iae)
            {
                // Ignore expected exception
            }
        }
        for (int n : new int[] {2, 3, 4, 100})
        {
            for (double shape : new double[] {0.5, 1.0, 2.0})
            {
                for (boolean weighted : new boolean[] {false, true})
                {
                    Ray3d start = new Ray3d(from.x, from.y, from.z, Math.PI / 3, Math.PI / 2);
                    Ray3d end = new Ray3d(to.x, to.y, to.z, 0, Math.PI);
                    BezierCubic3d cbc =
                            1.0 == shape ? new BezierCubic3d(start, end) : new BezierCubic3d(start, end, shape, weighted);
                    PolyLine3d line = cbc.toPolyLine(new Flattener3d.NumSegments(n));
                    for (int i = 1; i < line.size() - 1; i++)
                    {
                        Point3d p = line.get(i);
                        // System.out.println(p);
                        assertTrue(p.x > -10 && p.x < 20, "x of intermediate point has reasonable value");
                        assertTrue(p.y > -10 && p.y < 30, "y of intermediate point has reasonable value");
                        assertTrue(p.z > -10 && p.z < 40, "z of intermediate point has reasonable value");
                    }
                }
            }
        }
        control1 = new Point3d(5, 0, 10);
        control2 = new Point3d(0, 5, 20);
        for (int n : new int[] {2, 3, 4, 100})
        {
            PolyLine3d line = new BezierCubic3d(from, control1, control2, to).toPolyLine(new Flattener3d.NumSegments(n));
            assertEquals(from.x, line.getFirst().x, 0, "from x");
            assertEquals(from.y, line.getFirst().y, 0, "from y");
            assertEquals(from.z, line.getFirst().z, 0, "from z");
            assertEquals(to.x, line.getLast().x, 0, "to x");
            assertEquals(to.y, line.getLast().y, 0, "to y");
            assertEquals(to.z, line.getLast().z, 0, "to z");
            for (int i = 0; i < line.size(); i++)
            {
                Point3d p = line.get(i);
                // System.out.println(p);
                assertTrue(p.x > -10 && p.x < 20, "x of intermediate point has reasonable value");
                assertTrue(p.y > -10 && p.y < 30, "y of intermediate point has reasonable value");
                assertTrue(p.z > -10 && p.z <= 30, "z of intermediate point has reasonable value");
            }
        }
        for (int n : new int[] {2, 3, 4, 100})
        {
            PolyLine3d line = new BezierCubic3d(new Ray3d(from.x, from.y, from.z, Math.PI / 2, Math.PI / 2),
                    new Ray3d(to.x, to.y, to.z, 0, Math.PI / 2)).toPolyLine(new Flattener3d.NumSegments(n));
            for (int i = 0; i < line.size(); i++)
            {
                Point3d p = line.get(i);
                // System.out.println(p);
                assertTrue(p.x > -10 && p.x < 20, "x of intermediate point has reasonable value");
                assertTrue(p.y > -10 && p.y < 30, "y of intermediate point has reasonable value");
                assertTrue(p.z > -10 && p.z <= 30, "z of intermediate point has reasonable value");
            }
        }

        try
        {
            new BezierCubic3d(new Point3d[] {new Point3d(1, 2, 3), new Point3d(3, 4, 5), new Point3d(8, 6, 7)});
            fail("array length != 4 should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic3d(new Ray3d(1, 2, 3, 0.1, 0.2), new Ray3d(2, 1, 4, 0.5, -0.2), 0.0, true);
            fail("shape <= 0 should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        // Point3d start = new Point3d(1, 1, 2);
        // Point3d c1 = new Point3d(11, 1, 2);
        // Point2d c3 = new Point2d(5, 1);
        // Point3d c2 = new Point3d(1, 11, 2);
        // Point3d end = new Point3d(11, 11, 2);
        // double autoDistance = start.distance(end) / 2;
        // Point3d c1Auto = new Point3d(start.x + autoDistance, start.y, 2);
        // Point3d c2Auto = new Point3d(end.x - autoDistance, end.y, 2);
        // // Should produce a right leaning S shape; something between a slash and an S
        // PolyLine3d reference = new BezierCubic3d(start, c1, c2, end).toPolyLine(new Flattener3d.NumSegments(256));
        // PolyLine3d referenceAuto = new BezierCubic3d(start, c1Auto, c2Auto, end).toPolyLine(new
        // Flattener3d.NumSegments(256));
        // // System.out.print("ref " + reference.toPlot());
        // Ray3d startRay = new Ray3d(start, c1);
        // Ray3d endRay = new Ray3d(end, c2).flip();
        // for (double epsilonPosition : new double[] {1, 0.1, 0.05})
        // {
        // Bezier3d bezier3d = new Bezier3d(start, end);
        // PolyLine3d line = bezier3d.toPolyLine(new Flattener3d.MaxDeviation(epsilonPosition));
        // assertEquals(2, line.size(), "Bezier from two points should be 2-point poly line");
        // assertEquals(start, line.getFirst(), "Start point should be start");
        // assertEquals(end, line.getLast(), "End point should be end");
        // bezier3d = new Bezier3d(start, c1, c2, end);
        // line = bezier3d.toPolyLine(new Flattener3d.MaxDeviation(epsilonPosition));
        // compareBeziersDeviation("bezier with 2 explicit control points, flattened with max deviation", reference, line, 100,
        // epsilonPosition);
        // for (double epsilonAngle : new double[] {0.1})
        // {
        // line = new BezierCubic3d(start, c1, c2, end).toPolyLine(new Flattener3d.MaxAngle(epsilonAngle));
        // compareBeziersAngle("cubic with 2 explicit control points, flattened with max angle", reference, line, 100,
        // epsilonAngle);
        // line = new BezierCubic3d(startRay, endRay)
        // .toPolyLine(new Flattener3d.MaxDeviationAndAngle(epsilonPosition, epsilonAngle));
        // compareBeziersDeviation("cubic with automatic control points", referenceAuto, line, 100, epsilonPosition);
        // }
        // }

        double[] x = new double[] {1, 2, 3};
        double[] y = new double[] {3, 5, 3};
        double[] z = new double[] {7, 4, 4};
        Bezier3d b3d = new Bezier3d(x, y, z);
        for (int i = 0; i < 3; i++)
        {
            assertEquals(x[i], b3d.getX(i), 0.00001, "x");
            assertEquals(y[i], b3d.getY(i), 0.00001, "y");
            assertEquals(z[i], b3d.getZ(i), 0.00001, "z");
        }
    }

    /**
     * Test the length of a BezierCubic3d.
     */
    @Test
    public void testBezierLength()
    {
        BezierCubic3d bc3 = new BezierCubic3d(new Point3d(1, 2, 3), new Point3d(10, 10, 10), new Point3d(19, 18, 17),
                new Point3d(28, 26, 24)); // Should be completely straight
        assertEquals(Math.sqrt(27 * 27 + 24 * 24 + 21 * 21), bc3.getLength(), 0.001, "length");

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
            new BezierCubic2d(null, ray2);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(ray1, null);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(ray1, ray2);
            fail("Coinciding start and end points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(ray1, ray3, -1);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(ray1, ray3, Double.NaN);
            fail("Illegal shape value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic2d(ray1, ray3, Double.POSITIVE_INFINITY);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        PolyLine2d result = new Bezier2d(ray1, ray3).toPolyLine(new Flattener2d.NumSegments(1)); // Should succeed
        assertEquals(2, result.size(), "size should be 1 segment (2 points)");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");
        result = new Bezier2d(ray1, ray3).toPolyLine(new Flattener2d.NumSegments(64)); // Should succeed
        assertEquals(64 + 1, result.size(), "size should be 65");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");
        try
        {
            new Bezier2d(ray1, ray3).toPolyLine(new Flattener2d.NumSegments(0));
            fail("size smaller than 1 segment should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier2d(ray1);
            fail("cannot make a Bezier from only one point; should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        result = new BezierCubic2d(ray1, cp1, cp2, ray3).toPolyLine(new Flattener2d.NumSegments(1));
        assertEquals(2, result.size(), "size should be 2 segment, 2 points");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");
        result = new BezierCubic2d(ray1, cp1, cp2, ray3).toPolyLine(new Flattener2d.NumSegments(4));
        assertEquals(5, result.size(), "size should be 4 segments, 5 ponts");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");

        try
        {
            new BezierCubic2d(ray1, cp1, cp2, ray3).toPolyLine(new Flattener2d.NumSegments(0));
            fail("Cannot construct a Bezier approximation that has zero segments");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        // Finally, test the toString method
        assertTrue(new BezierCubic2d(ray1, cp1, cp2, ray3).toString().startsWith("BezierCubic2d ["),
                "toString method returns something descriptive");
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
            new BezierCubic3d(null, ray2);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic3d(ray1, null);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic3d(ray1, ray2);
            fail("Coinciding start and end points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic3d(ray1, ray3, Double.NaN);
            fail("Illegal shape value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new BezierCubic3d(ray1, ray3, Double.POSITIVE_INFINITY);
            fail("Illegal shape value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        PolyLine3d result = new BezierCubic3d(ray1, ray3).toPolyLine(new Flattener3d.NumSegments(1)); // Should
                                                                                                      // succeed
        assertEquals(2, result.size(), "size should be 2 points (1 segment)");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");
        result = new BezierCubic3d(ray1, ray3).toPolyLine(new Flattener3d.NumSegments(64)); // Should succeed
        assertEquals(64 + 1, result.size(), "size should be 65 (number of segments + 1)");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");
        try
        {
            new BezierCubic3d(ray1, ray3).toPolyLine(new Flattener3d.NumSegments(0));
            fail("size smaller than 1 segment should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Bezier3d(ray1);
            fail("cannot make a Bezier from only one point; should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        result = new BezierCubic3d(ray1, cp1, cp2, ray3).toPolyLine(new Flattener3d.NumSegments(1));
        assertEquals(2, result.size(), "size should be 2");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");
        result = new BezierCubic3d(ray1, cp1, cp2, ray3).toPolyLine(new Flattener3d.NumSegments(3));
        assertEquals(4, result.size(), "size should be 4");
        assertEquals(0, ray1.distanceSquared(result.getFirst()), 0, "start of result is at start");
        assertEquals(0, ray3.distanceSquared(result.getLast()), 0, "end of result is at start");

        try
        {
            new BezierCubic3d(ray1, cp1, cp2, ray3).toPolyLine(new Flattener3d.NumSegments(0));
            fail("Cannot construct a Bezier approximation that has only one point");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the factorial method.
     * @throws SecurityException if that happens, this test has failed
     * @throws NoSuchMethodException if that happens, this test has failed
     * @throws InvocationTargetException if that happens, this test has failed
     * @throws IllegalArgumentException if that happens, this test has failed
     * @throws IllegalAccessException if that happens, this test has failed
     */
    @Test
    public void testFactorial() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        Class<?> bezierClass = Bezier.class;
        Method factorialMethod = bezierClass.getDeclaredMethod("factorial", int.class);
        factorialMethod.setAccessible(true);
        double expected = 1;
        for (int i = 1; i < 100; i++)
        {
            expected *= i;
            double result = (double) factorialMethod.invoke(null, i);
            // System.out.println(result);
            assertEquals(expected, result, expected / 1e10, "factorial");
        }
    }

}
