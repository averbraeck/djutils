package org.djutils.draw.point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Point2D;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * Point2dTest.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point2dTest
{
    /**
     * Test the Point2d construction methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testPoint2dConstruction()
    {
        Point2d p = new Point2d(10.0, -20.0);
        assertNotNull(p);
        assertEquals(10.0, p.x, 1E-6);
        assertEquals(-20.0, p.y, 1E-6);

        assertEquals("size method returns 1", 1, p.size());

        try
        {
            new Point2d(Double.NaN, 0);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Point2d(0, Double.NaN);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        double[] p2Arr = new double[] { 5.0, 6.0 };
        p = new Point2d(p2Arr);
        assertEquals(5.0, p.x, 0);
        assertEquals(6.0, p.y, 0);
        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = new Point2d(p2DD);
        assertEquals(-0.1, p.x, 1E-6);
        assertEquals(-0.2, p.y, 1E-6);
        assertEquals(p2DD, p.toPoint2D());

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d((Point2D.Double) null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new double[] { 1.0 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new double[] { 1.0, 2.0, 3.0 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new Point2D.Double(Double.NaN, 2));
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new Point2D.Double(1, Double.NaN));
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        // equals and hashCode
        assertTrue(p.equals(p));
        assertEquals(p.hashCode(), p.hashCode());
        Point3d p3d = p.translate(1.0, 2.0, 3.0);
        assertFalse(p.equals(p3d));
        assertFalse(p.equals(null));
        assertNotEquals(p3d.hashCode(), p.hashCode());
        assertEquals(p, p.translate(0.0, 0.0));
        assertNotEquals(p, p.translate(1.0, 0.0));
        assertNotEquals(p, p.translate(0.0, 1.0));
        assertEquals("x", p.x + 1, p3d.x, 0.00001);
        assertEquals("y", p.y + 2, p3d.y, 0.00001);
        assertEquals("z", 3, p3d.z, 0);

        // toString
        p = new Point2d(10.0, 20.0);
        assertEquals("(10.000000,20.000000)", p.toString());
        assertEquals("(10.0,20.0)", p.toString(1));
        assertEquals("(10,20)", p.toString(0));
        assertEquals("(10,20)", p.toString(-1));

        // epsilonEquals
        assertTrue(p.epsilonEquals(p, 0.1));
        assertTrue(p.epsilonEquals(p, 0.001));
        assertTrue(p.epsilonEquals(p, 0.0));
        Point2d p3 = p.translate(0.001, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09));
        assertTrue(p3.epsilonEquals(p, 0.09));
        assertFalse(p.epsilonEquals(p3, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009));
        p3 = p.translate(0.0, 0.001);
        assertTrue(p.epsilonEquals(p3, 0.09));
        assertTrue(p3.epsilonEquals(p, 0.09));
        assertFalse(p.epsilonEquals(p3, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009));
    }

    /**
     * Test the Point2d operators.
     */
    @Test
    public void testPoint2dOperators()
    {
        Point2d p = new Point2d(-0.1, -0.2);
        assertEquals(0.1, p.abs().x, 1E-6);
        assertEquals(0.2, p.abs().y, 1E-6);
        p = p.neg();
        assertEquals(0.1, p.x, 1E-6);
        assertEquals(0.2, p.y, 1E-6);
        p = p.scale(1.0);
        assertEquals(0.1, p.x, 1E-6);
        assertEquals(0.2, p.y, 1E-6);
        p = p.scale(10.0);
        assertEquals(1.0, p.x, 1E-6);
        assertEquals(2.0, p.y, 1E-6);
        p = p.translate(5.0, -1.0);
        assertEquals(6.0, p.x, 1E-6);
        assertEquals(1.0, p.y, 1E-6);
        Point3d p3d = p.translate(1.0, 1.0, 1.0);
        assertEquals(7.0, p3d.x, 1E-6);
        assertEquals(2.0, p3d.y, 1E-6);
        assertEquals(1.0, p3d.z, 1E-6);

        try
        {
            p.translate(Double.NaN, 2.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1.0, Double.NaN);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        // interpolate
        Point2d p1 = new Point2d(1.0, 1.0);
        Point2d p2 = new Point2d(5.0, 5.0);
        assertEquals(p1, p1.interpolate(p2, 0.0));
        assertEquals(p2, p2.interpolate(p1, 0.0));
        assertEquals(p1, p1.interpolate(p1, 0.0));
        assertEquals(new Point2d(3.0, 3.0), p1.interpolate(p2, 0.5));

        // distance
        assertEquals(Math.sqrt(32.0), p1.distance(p2), 0.001);
        assertEquals(32.0, p1.distanceSquared(p2), 0.001);
        // FIXME
        // assertEquals(Math.sqrt(32.0), p1.horizontalDistance(p2), 0.001);
        // assertEquals(32.0, p1.horizontalDistanceSquared(p2), 0.001);
        //
        // // direction
        // assertEquals(Math.toRadians(45.0), p2.horizontalDirection(), 0.001);
        // assertEquals(Math.toRadians(45.0), p1.horizontalDirection(p2), 0.001);
        // assertEquals(0.0, new Point2d(0.0, 0.0).horizontalDirection(), 0.001);

        // normalize
        Point2d pn = p2.normalize();
        assertEquals(1.0 / Math.sqrt(2.0), pn.x, 0.001);
        assertEquals(1.0 / Math.sqrt(2.0), pn.y, 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(0.0, 0.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

        Bounds2d bounds = p1.getBounds();
        assertEquals("Bounds min x", p1.x, bounds.getMinX(), 0);
        assertEquals("Bounds min y", p1.y, bounds.getMinY(), 0);
        assertEquals("Bounds max x", p1.x, bounds.getMaxX(), 0);
        assertEquals("Bounds max y", p1.y, bounds.getMaxY(), 0);
    }

    /**
     * Test the Point2d operators for NPE.
     */
    @Test
    public void testPoint2dOperatorsNPE()
    {
        final Point2d p1 = new Point2d(1.0, 1.0);

        try
        {
            p1.translate(Double.NaN, 2.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(1.0, Double.NaN);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(Double.NaN, 2.0, 3.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(1.0, Double.NaN, 3.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(1.0, 2.0, Double.NaN);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.interpolate(null, 0.5);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.distance(null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.distanceSquared(null);
            }
        }, "Should throw NPE", NullPointerException.class);
    }

    /**
     * Test the intersectionOfLineSegments method.
     */
    @Test
    public void testIntersectionOfLineSegments()
    {
        assertNull("horizontal line intersection with itself returns null",
                Point2d.intersectionOfLineSegments(new Point2d(1, 2), new Point2d(4, 2), new Point2d(1, 2), new Point2d(4, 2)));
        assertNull("vertical line intersection with itself returns null", Point2d.intersectionOfLineSegments(new Point2d(1, 2),
                new Point2d(1, 10), new Point2d(1, 2), new Point2d(1, 10)));
        assertEquals("Intersection is at (2,2)", new Point2d(2, 2), Point2d.intersectionOfLineSegments(new Point2d(1, 1),
                new Point2d(6, 6), new Point2d(4, 2), new Point2d(-2, 2)));
        // Check all four ways that two non-parallel lines can miss each other
        assertNull("line two passes before start of line one", Point2d.intersectionOfLineSegments(new Point2d(1, 1),
                new Point2d(5, 5), new Point2d(0, -3), new Point2d(10, 0)));
        assertNull("line two passes before after end of line one", Point2d.intersectionOfLineSegments(new Point2d(1, 1),
                new Point2d(5, 5), new Point2d(0, 20), new Point2d(100, 30)));
        assertNull("line one passes before start of line two", Point2d.intersectionOfLineSegments(new Point2d(1, 1),
                new Point2d(5, 5), new Point2d(5, 3), new Point2d(10, 2)));
        assertNull("line one passes after end of line two", Point2d.intersectionOfLineSegments(new Point2d(1, 1),
                new Point2d(5, 5), new Point2d(-10, 3), new Point2d(0, 2)));
    }

    /**
     * Test the intersectionOfLines method.
     */
    @Test
    public void testIntersectionOfLines()
    {
        assertNull("horizontal line intersection with itself returns null",
                Point2d.intersectionOfLines(new Point2d(1, 2), new Point2d(4, 2), new Point2d(1, 2), new Point2d(4, 2)));
        assertNull("vertical line intersection with itself returns null", Point2d.intersectionOfLineSegments(new Point2d(1, 2),
                new Point2d(1, 10), new Point2d(1, 2), new Point2d(1, 10)));
        assertEquals("Intersection is at (2,2)", new Point2d(2, 2),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(6, 6), new Point2d(4, 2), new Point2d(-2, 2)));
        // Check all four ways that two non-parallel lines can miss each other
        assertEquals("line two passes before start of line one", new Point2d(-1.5, -1.5),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(0, -3), new Point2d(10, -13)));
        assertEquals("line two passes before after end of line one", new Point2d(20, 20),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(0, 20), new Point2d(100, 20)));
        assertEquals("line one passes before start of line two", new Point2d(4, 4),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(7, 1), new Point2d(10, -2)));
        assertEquals("line one passes after end of line two", new Point2d(-3.5, -3.5),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(-10, 3), new Point2d(0, -7)));
    }

    /**
     * Test the closestPointOnSegment method.
     * @throws DrawException if that happens uncaught; this test has failed
     */
    @Test
    public void testClosestPointOnSegment() throws DrawException
    {
        Point2d p1 = new Point2d(-2, 3);
        for (Point2d p2 : new Point2d[] { new Point2d(7, 4)/* angled */, new Point2d(-3, 6) /* also angled */,
                new Point2d(-2, -5) /* vertical */, new Point2d(8, 3)/* horizontal */ })
        {
            PolyLine2d line = new PolyLine2d(p1, p2);
            for (double x = -10; x <= 10; x += 0.5)
            {
                for (double y = -10; y <= 10; y += 0.5)
                {
                    Point2d p = new Point2d(x, y);
                    Point2d result = p.closestPointOnSegment(p1, p2);
                    // Figure out the correct result using a totally different method (binary search over the line segment)
                    double fraction = 0.5;
                    double step = 0.25;
                    Point2d approximation = line.getLocationFraction(fraction);
                    double distance = approximation.distance(p);
                    // 10 iterations should get us to within one thousandth
                    for (int iteration = 0; iteration < 10; iteration++)
                    {
                        // Try stepping up
                        double upFraction = fraction + step;
                        Point2d upApproximation = line.getLocationFraction(upFraction);
                        double upDistance = upApproximation.distance(p);
                        if (upDistance < distance)
                        {
                            distance = upDistance;
                            fraction = upFraction;
                            approximation = upApproximation;
                        }
                        else
                        {
                            // Try stepping down
                            double downFraction = fraction - step;
                            Point2d downApproximation = line.getLocationFraction(downFraction);
                            double downDistance = downApproximation.distance(p);
                            if (downDistance < distance)
                            {
                                distance = downDistance;
                                fraction = downFraction;
                                approximation = downApproximation;
                            }
                        }
                        step /= 2;
                    }
                    assertEquals("distance should be less than one thousandth of line length", 0,
                            approximation.distance(result), line.getLength() / 1000);
                    assertEquals("zero length line segment should always return start point", p1,
                            p.closestPointOnSegment(p1, p1));
                }
            }
        }
    }

    /**
     * Test the circleIntersection method.
     */
    @Test
    public void circleIntersectionTest()
    {
        for (int x1 = -5; x1 <= 5; x1++)
        {
            for (int y1 = -5; y1 <= 5; y1++)
            {
                Point2d p1 = new Point2d(x1, y1);
                for (int r1 = 0; r1 < 5; r1++)
                {
                    for (int x2 = -5; x2 <= 5; x2++)
                    {
                        for (int y2 = -5; y2 <= 5; y2++)
                        {
                            Point2d p2 = new Point2d(x2, y2);
                            double distance = p1.distance(p2);
                            for (int r2 = 0; r2 < 5; r2++)
                            {
                                if (x1 == x2 && y1 == y2 && r1 == r2)
                                {
                                    try
                                    {
                                        Point2d.circleIntersections(p1, r1, p2, r2);
                                        fail("Identical circles should have thrown a DrawRuntimeException");
                                    }
                                    catch (DrawRuntimeException dre)
                                    {
                                        // Ignore expected exception
                                    }
                                }
                                else
                                {
                                    List<Point2d> result = Point2d.circleIntersections(p1, r1, p2, r2);
                                    // System.out.print("p1=" + p1 + ", r1=" + r1 + ", p2=" + p2 + " r2=" + r2 + ", result=");
                                    // for (Point2d p : result)
                                    // {
                                    // System.out
                                    // .print(String.format("%s d1=%.3f d2=%.3f ", p, p.distance(p1), p.distance(p2)));
                                    // }
                                    // System.out.println("");
                                    if (distance > r1 + r2 + 0.0001)
                                    {
                                        if (result.size() > 0)
                                        {
                                            Point2d.circleIntersections(p1, r1, p2, r2);
                                        }
                                        assertEquals("There are 0 intersections", 0, result.size());
                                    }
                                    if (distance < r1 + r2 - 0.0001 && distance > Math.abs(r2 - r1) + 0.0001)
                                    {
                                        if (result.size() != 2)
                                        {
                                            Point2d.circleIntersections(p1, r1, p2, r2);
                                        }
                                        assertEquals("There are 2 intersections", 2, result.size());
                                    }
                                    for (Point2d p : result)
                                    {
                                        if (Math.abs(r1 - p.distance(p1)) > 0.1 || Math.abs(r2 - p.distance(p2)) > 0.1)
                                        {
                                            Point2d.circleIntersections(p1, r1, p2, r2);
                                        }
                                        assertEquals("result is at r1 from p1", r1, p.distance(p1), 0.0001);
                                        assertEquals("result is at r2 from p2", r2, p.distance(p2), 0.0001);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        try
        {
            Point2d.circleIntersections(new Point2d(1, 2), -1, new Point2d(3, 4), 2);
            fail("negative radius should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.circleIntersections(new Point2d(1, 2), 5, new Point2d(3, 4), -2);
            fail("negative radius should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.circleIntersections(null, 5, new Point2d(3, 4), 2);
            fail("null for center1 should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.circleIntersections(new Point2d(3, 4), 5, null, 2);
            fail("null for center1 should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the direction method.
     */
    @Test
    public void testDirection()
    {
        Point2d reference = new Point2d(5, 8);
        assertEquals("East", 0, reference.directionTo(new Point2d(reference.x + 10, reference.y)), 0);
        assertEquals("North", Math.PI / 2, reference.directionTo(new Point2d(reference.x, reference.y + 5)), 0.00001);
        assertEquals("NorthEast", Math.PI / 4, reference.directionTo(new Point2d(reference.x + 2, reference.y + 2)), 0.00001);
        assertEquals("West", Math.PI, reference.directionTo(new Point2d(reference.x - 1, reference.y)), 0);
        assertEquals("South", -Math.PI / 2, reference.directionTo(new Point2d(reference.x, reference.y - 0.5)), 0.00001);
        assertEquals("SouthWst", -3 * Math.PI / 4, reference.directionTo(new Point2d(reference.x - 0.2, reference.y - 0.2)),
                0.00001);
    }

}
