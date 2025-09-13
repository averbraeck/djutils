package org.djutils.draw.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.List;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.exceptions.Try;
import org.junit.jupiter.api.Test;

/**
 * Point2dTest.java.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
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
        assertEquals(10.0, p.x, 1E-6, "Access x");
        assertEquals(-20.0, p.y, 1E-6, "Access y");
        assertEquals(2, p.getDimensions(), "Dimensions is 2");

        assertEquals(1, p.size(), "Size method returns 1");

        try
        {
            new Point2d(Double.NaN, 0);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Point2d(0, Double.NaN);
            fail("NaN should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        double[] p2Arr = new double[] {5.0, 6.0};
        p = new Point2d(p2Arr);
        assertEquals(5.0, p.x, 0);
        assertEquals(6.0, p.y, 0);
        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = new Point2d(p2DD);
        assertEquals(-0.1, p.x, 1E-6);
        assertEquals(-0.2, p.y, 1E-6);
        assertEquals(p2DD, p.toPoint2D());

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d((Point2D.Double) null);
            }
        }, "Should throw NPE", NullPointerException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new double[] {1.0});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new double[] {1.0, 2.0, 3.0});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new Point2D.Double(Double.NaN, 2));
            }
        }, "Should throw ArithmeticException", ArithmeticException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(new Point2D.Double(1, Double.NaN));
            }
        }, "Should throw ArithmeticException", ArithmeticException.class);

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
        assertEquals(p.x + 1, p3d.x, 0.00001, "x");
        assertEquals(p.y + 2, p3d.y, 0.00001, "y");
        assertEquals(3, p3d.z, 0, "z");

        // toString
        p = new Point2d(10.0, 20.0);
        assertEquals("Point2d [x=10.000000, y=20.000000]", p.toString());
        assertEquals("Point2d [x=10.0, y=20.0]", p.toString("%.1f"));
        assertEquals("[x=10, y=20]", p.toString("%.0f", true));

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
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1.0, Double.NaN);
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
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

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(0.0, 0.0).normalize();
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Bounds2d bounds = p1.getAbsoluteBounds();
        assertEquals(p1.x, bounds.getMinX(), 0, "Bounds min x");
        assertEquals(p1.y, bounds.getMinY(), 0, "Bounds min y");
        assertEquals(p1.x, bounds.getMaxX(), 0, "Bounds max x");
        assertEquals(p1.y, bounds.getMaxY(), 0, "Bounds max y");
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
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(1.0, Double.NaN);
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(Double.NaN, 2.0, 3.0);
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(1.0, Double.NaN, 3.0);
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.translate(1.0, 2.0, Double.NaN);
            fail("NaN translation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.interpolate(null, 0.5);
            }
        }, "Should throw NPE", NullPointerException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.distance(null);
            }
        }, "Should throw NPE", NullPointerException.class);

        UnitTest.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.distanceSquared(null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Point2d p = new Point2d(1, 2);
        UnitTest.testFail(new Try.Execution()
        {

            @Override
            public void execute() throws Throwable
            {
                p.epsilonEquals(p, -0.1);
            }
        }, "Should throw IllegalArgumentException", IllegalArgumentException.class);
    }

    /**
     * Test the intersectionOfLineSegments method.
     */
    @Test
    public void testIntersectionOfLineSegments()
    {
        assertNull(
                Point2d.intersectionOfLineSegments(new Point2d(1, 2), new Point2d(4, 2), new Point2d(1, 2), new Point2d(4, 2)),
                "horizontal line intersection with itself returns null");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(1, 2), new Point2d(1, 10), new Point2d(1, 2),
                new Point2d(1, 10)), "vertical line intersection with itself returns null");
        assertEquals(new Point2d(2, 2),
                Point2d.intersectionOfLineSegments(new Point2d(1, 1), new Point2d(6, 6), new Point2d(4, 2), new Point2d(-2, 2)),
                "Intersection is at (2,2)");
        assertEquals(new Point2d(2, 2), Point2d.intersectionOfLineSegments(1, 1, 6, 6, 4, 2, -2, 2),
                "Intersection is at (2,2)");
        assertEquals(new Point2d(2, 2),
                Point2d.intersectionOfLineSegments(new LineSegment2d(1, 1, 6, 6), new LineSegment2d(4, 2, -2, 2)),
                "Intersection is at (2,2)");
        // Check all four ways that two non-parallel lines can miss each other
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(1, 1), new Point2d(5, 5), new Point2d(0, -3),
                new Point2d(10, 0)), "line two passes before start of line one");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(1, 1), new Point2d(5, 5), new Point2d(0, 20),
                new Point2d(100, 30)), "line two passes before after end of line one");
        assertNull(
                Point2d.intersectionOfLineSegments(new Point2d(1, 1), new Point2d(5, 5), new Point2d(5, 3), new Point2d(10, 2)),
                "line one passes before start of line two");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(1, 1), new Point2d(5, 5), new Point2d(-10, 3),
                new Point2d(0, 2)), "line one passes after end of line two");
        assertNull(Point2d.intersectionOfLineSegments(1, 1, 5, 5, 0, -3, 10, 0), "line two passes before start of line one");
        assertNull(Point2d.intersectionOfLineSegments(new LineSegment2d(1, 15, 5, 5), new LineSegment2d(0, -3, 10, 0)),
                "line two passes before start of line one");
        assertNull(Point2d.intersectionOfLineSegments(1, 1, 5, 5, 0, 20, 100, 30),
                "line two passes before after end of line one");
        assertNull(Point2d.intersectionOfLineSegments(1, 1, 5, 5, 5, 3, 10, 2), "line one passes before start of line two");
        assertNull(Point2d.intersectionOfLineSegments(new LineSegment2d(1, 1, 5, 5), new LineSegment2d(5, 3, 10, 2)),
                "line one passes before start of line two");
        assertNull(Point2d.intersectionOfLineSegments(1, 1, 5, 5, -10, 3, 0, 2), "line one passes after end of line two");
        assertNull(Point2d.intersectionOfLineSegments(new LineSegment2d(1, 1, 5, 5), new LineSegment2d(-10, 3, 0, 2)),
                "line one passes after end of line two");

        Point2d line1P1 = new Point2d(1, 2);
        Point2d line1P2 = new Point2d(3, 2);
        Point2d line2P1 = new Point2d(2, 0);
        Point2d line2P2 = new Point2d(2, 4);
        try
        {
            Point2d.intersectionOfLines(null, line1P2, line2P1, line2P2);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLines(line1P1, null, line2P1, line2P2);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLines(line1P1, line1P2, null, line2P2);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLines(line1P1, line1P2, line2P1, null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLineSegments(null, line1P2, line2P1, line2P2);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLineSegments(line1P1, null, line2P1, line2P2);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLineSegments(line1P1, line1P2, null, line2P2);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.intersectionOfLineSegments(line1P1, line1P2, line2P1, null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

    }

    /**
     * Test the intersectionOfLines method.
     */
    @Test
    public void testIntersectionOfLines()
    {
        assertNull(Point2d.intersectionOfLines(new Point2d(1, 2), new Point2d(4, 2), new Point2d(1, 2), new Point2d(4, 2)),
                "horizontal line intersection with itself returns null");
        assertNull(Point2d.intersectionOfLines(1, 2, 4, 2, 1, 2, 4, 2),
                "horizontal line intersection with itself returns null");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(1, 2), new Point2d(1, 10), new Point2d(1, 2),
                new Point2d(1, 10)), "vertical line intersection with itself returns null");
        assertNull(Point2d.intersectionOfLineSegments(1, 2, 1, 10, 1, 2, 1, 10),
                "vertical line intersection with itself returns null");
        assertNull(Point2d.intersectionOfLineSegments(new LineSegment2d(1, 2, 1, 10), new LineSegment2d(1, 2, 1, 10)),
                "vertical line intersection with itself returns null");
        assertEquals(new Point2d(2, 2),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(6, 6), new Point2d(4, 2), new Point2d(-2, 2)),
                "Intersection is at (2,2)");
        // Check all four ways that two non-parallel lines can miss each other
        assertEquals(new Point2d(-1.5, -1.5),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(0, -3), new Point2d(10, -13)),
                "line two passes before start of line one");
        assertEquals(new Point2d(20, 20),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(0, 20), new Point2d(100, 20)),
                "line two passes before after end of line one");
        assertEquals(new Point2d(4, 4),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(7, 1), new Point2d(10, -2)),
                "line one passes before start of line two");
        assertEquals(new Point2d(-3.5, -3.5),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(5, 5), new Point2d(-10, 3), new Point2d(0, -7)),
                "line one passes after end of line two");
        // Test the various exact hits at begin or end point
        assertEquals(new Point2d(1, 1),
                Point2d.intersectionOfLines(new Point2d(1, 1), new Point2d(2, 1), new Point2d(1, 0), new Point2d(1, 3)),
                "begin of first is on second");
        assertEquals(new Point2d(1, 1),
                Point2d.intersectionOfLines(new Point2d(-1, 1), new Point2d(1, 1), new Point2d(1, 0), new Point2d(1, 3)),
                "end of first is on second");
        assertEquals(new Point2d(1, 1),
                Point2d.intersectionOfLines(new Point2d(-1, 1), new Point2d(2, 1), new Point2d(1, 1), new Point2d(1, 3)),
                "begin of second is on first");
        assertEquals(new Point2d(1, 1),
                Point2d.intersectionOfLines(new Point2d(-1, 1), new Point2d(2, 1), new Point2d(1, -1), new Point2d(1, 1)),
                "end of second is on first");
        // Test the various not quite exact hits at begin or end point
        assertTrue(new Point2d(1, 1).epsilonEquals(
                Point2d.intersectionOfLines(new Point2d(1.001, 1), new Point2d(2, 1), new Point2d(1, 0), new Point2d(1, 3)),
                0.0001), "begin of first is just over second");
        assertTrue(new Point2d(1, 1).epsilonEquals(
                Point2d.intersectionOfLines(new Point2d(-1, 1), new Point2d(0.999, 1), new Point2d(1, 0), new Point2d(1, 3)),
                0.0001), "end of first is just over second");
        assertTrue(new Point2d(1, 1).epsilonEquals(
                Point2d.intersectionOfLines(new Point2d(-1, 1), new Point2d(2, 1), new Point2d(1, 1.001), new Point2d(1, 3)),
                0.0001), "begin of second is just over first");
        assertTrue(new Point2d(1, 1).epsilonEquals(
                Point2d.intersectionOfLines(new Point2d(-1, 1), new Point2d(2, 1), new Point2d(1, -1), new Point2d(1, 0.999)),
                0.0001), "end of second is just over first");
        // Test the various close hits at begin or end point
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(1.001, 1), new Point2d(2, 1), new Point2d(1, 0),
                new Point2d(1, 3)), "begin of first is just not on second");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(-1, 1), new Point2d(0.999, 1), new Point2d(1, 0),
                new Point2d(1, 3)), "end of first is just not on second");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(-1, 1), new Point2d(2, 1), new Point2d(1, 1.001),
                new Point2d(1, 3)), "begin of second is just not on first");
        assertNull(Point2d.intersectionOfLineSegments(new Point2d(-1, 1), new Point2d(2, 1), new Point2d(1, -1),
                new Point2d(1, 0.999)), "end of second is just not on first");
    }

    /**
     * Test the closestPointOnSegment and the closestPointOnLine methods.
     */
    @Test
    public void testClosestPointOnSegmentAndLine()
    {
        Point2d p1 = new Point2d(-2, 3);
        for (Point2d p2 : new Point2d[] {new Point2d(7, 4)/* angled */, new Point2d(-3, 6) /* also angled */,
                new Point2d(-2, -5) /* vertical */, new Point2d(8, 3)/* horizontal */ })
        {
            PolyLine2d line = new PolyLine2d(p1, p2);
            for (double x = -10; x <= 10; x += 0.5)
            {
                for (double y = -10; y <= 10; y += 0.5)
                {
                    Point2d p = new Point2d(x, y);
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
                    Point2d result = p.closestPointOnSegment(p1, p2);
                    assertEquals(0, approximation.distance(result), line.getLength() / 1000,
                            "distance should be less than one thousandth of line length");
                    assertEquals(p1, p.closestPointOnSegment(p1, p1),
                            "zero length line segment should always return start point");
                    result = p.closestPointOnSegment(p1.x, p1.y, p2.x, p2.y);
                    assertEquals(0, approximation.distance(result), line.getLength() / 1000,
                            "distance should be less than one thousandth of line length");

                    if (fraction > 0.001 && fraction < 0.999)
                    {
                        result = p.closestPointOnLine(p1, p2);
                        assertEquals(0, approximation.distance(result), line.getLength() / 1000,
                                "distance should be less than one thousandth of line length");
                        result = p.closestPointOnLine(p1, p2);
                        assertEquals(0, approximation.distance(result), line.getLength() / 1000,
                                "distance should be less than one thousandth of line length");
                        result = p.closestPointOnLine(p1.x, p1.y, p2.x, p2.y);
                        assertEquals(0, approximation.distance(result), line.getLength() / 1000,
                                "distance should be less than one thousandth of line length");
                    }
                    else
                    {
                        // extrapolating
                        double range = Math.max(Math.max(line.getLength(), p.distance(p1)), p.distance(p2));
                        step = 5.0;
                        fraction = 0.5;
                        distance = range;
                        // 20 iterations should get us to within one thousandth
                        for (int iteration = 0; iteration < 20; iteration++)
                        {
                            // Try stepping up
                            double upFraction = fraction + step;
                            Point2d upApproximation = line.getLocationFractionExtended(upFraction);
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
                                Point2d downApproximation = line.getLocationFractionExtended(downFraction);
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
                        result = p.closestPointOnLine(p1, p2);
                        assertEquals(0, approximation.distance(result), range / 1000,
                                "distance should be less than one thousandth of range");
                        result = p.closestPointOnLine(p1, p2);
                        assertEquals(0, approximation.distance(result), range / 1000,
                                "distance should be less than one thousandth of range");
                        result = p.closestPointOnLine(p1.x, p1.y, p2.x, p2.y);
                        assertEquals(0, approximation.distance(result), range / 1000,
                                "distance should be less than one thousandth of range");
                        if (fraction < -0.001 || fraction > 1.001)
                        {
                            assertNull(new LineSegment2d(p1, p2).projectOrthogonal(p), "projectOrthogonal should return null");
                            assertEquals(result, new LineSegment2d(p1, p2).projectOrthogonalExtended(p),
                                    "projectOrthogonalExtended should return same result as closestPointOnLine");
                        }
                    }
                }
            }
        }

        try
        {
            p1.closestPointOnLine(null, new Point2d(5, 6));
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(new Point2d(5, 6), null);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(Double.NaN, 7, 8, 9);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, Double.NaN, 8, 9);
            fail("NaN value should have thrown na ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, 7, Double.NaN, 9);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, 7, 8, Double.NaN);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(Double.NaN, 7, 8, 9);
            fail("NaN value should have thrown a ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, Double.NaN, 8, 9);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, Double.NaN, 9);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, 8, Double.NaN);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, 6, 7);
            fail("identical points should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
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
                                        fail("Identical circles should have thrown an IllegalArgumentException");
                                    }
                                    catch (IllegalArgumentException e)
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
                                        assertEquals(0, result.size(), "There are 0 intersections");
                                    }
                                    if (distance < r1 + r2 - 0.0001 && distance > Math.abs(r2 - r1) + 0.0001)
                                    {
                                        if (result.size() != 2)
                                        {
                                            Point2d.circleIntersections(p1, r1, p2, r2);
                                        }
                                        assertEquals(2, result.size(), "There are 2 intersections");
                                    }
                                    for (Point2d p : result)
                                    {
                                        if (Math.abs(r1 - p.distance(p1)) > 0.1 || Math.abs(r2 - p.distance(p2)) > 0.1)
                                        {
                                            Point2d.circleIntersections(p1, r1, p2, r2);
                                        }
                                        assertEquals(r1, p.distance(p1), 0.0001, "result is at r1 from p1");
                                        assertEquals(r2, p.distance(p2), 0.0001, "result is at r2 from p2");
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
            fail("negative radius should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            Point2d.circleIntersections(new Point2d(1, 2), 5, new Point2d(3, 4), -2);
            fail("negative radius should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
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
        assertEquals(0, reference.directionTo(new Point2d(reference.x + 10, reference.y)), 0, "East");
        assertEquals(Math.PI / 2, reference.directionTo(new Point2d(reference.x, reference.y + 5)), 0.00001, "North");
        assertEquals(Math.PI / 4, reference.directionTo(new Point2d(reference.x + 2, reference.y + 2)), 0.00001, "NorthEast");
        assertEquals(Math.PI, reference.directionTo(new Point2d(reference.x - 1, reference.y)), 0, "West");
        assertEquals(-Math.PI / 2, reference.directionTo(new Point2d(reference.x, reference.y - 0.5)), 0.00001, "South");
        assertEquals(-3 * Math.PI / 4, reference.directionTo(new Point2d(reference.x - 0.2, reference.y - 0.2)), 0.00001,
                "SouthWst");
    }

}
