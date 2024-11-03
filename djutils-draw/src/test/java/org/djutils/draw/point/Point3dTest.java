package org.djutils.draw.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.line.LineSegment3d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.exceptions.Try;
import org.junit.jupiter.api.Test;

/**
 * Point3dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Point3dTest
{
    /**
     * Test the Point3d construction methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testPoint3dConstruction()
    {
        Point3d p = new Point3d(10.0, -20.0, 16.0);
        assertNotNull(p);
        assertEquals(10.0, p.x, 0, "Access x");
        assertEquals(-20.0, p.y, 0, "Access y");
        assertEquals(16.0, p.z, 0, "Access z");
        assertEquals(3, p.getDimensions(), "Dimensions is 3");

        assertEquals(1, p.size(), "Size method returns 1");

        Point2d projection = p.project();
        assertEquals(10.0, projection.x, 0);
        assertEquals(-20.0, projection.y, 0);

        try
        {
            new Point3d(Double.NaN, 0, 0);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Point3d(0, Double.NaN, 0);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Point3d(0, 0, Double.NaN);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        double[] p3Arr = new double[] {5.0, 6.0, 7.0};
        p = new Point3d(p3Arr);
        assertEquals(5.0, p.x, 0);
        assertEquals(6.0, p.y, 0);
        assertEquals(7.0, p.z, 0);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(new double[] {1.0});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(new double[] {1.0, 2.0});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(new double[] {1.0, 2.0, 3.0, 4.0});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d((Point2d) null, 0);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d((Point2D.Double) null, 0);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(new Point2D.Double(Double.NaN, 2), 0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(new Point2D.Double(1, Double.NaN), 0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        // equals and hashCode
        assertTrue(p.equals(p));
        assertEquals(p.hashCode(), p.hashCode());
        Point2d p2d = new Point2d(1.0, 1.0);
        assertFalse(p.equals(p2d));
        assertFalse(p.equals(null));
        assertNotEquals(p2d.hashCode(), p.hashCode());
        assertEquals(p, p.translate(0.0, 0.0, 0.0), "Translating over 0,0,0 returns p");
        assertNotEquals(p, p.translate(1.0, 0.0, 0.0));
        assertNotEquals(p, p.translate(0.0, 1.0, 0.0));
        assertNotEquals(p, p.translate(0.0, 0.0, 1.0));

        // toString
        p = new Point3d(10.0, 20.0, 30.0);
        assertEquals("Point3d [x=10.000000, y=20.000000, z=30.000000]", p.toString());
        assertEquals("Point3d [x=10.0, y=20.0, z=30.0]", p.toString("%.1f"));
        assertEquals("[x=10, y=20, z=30]", p.toString("%.0f", true));

        // epsilonEquals
        assertTrue(p.epsilonEquals(p, 0.1));
        assertTrue(p.epsilonEquals(p, 0.001));
        assertTrue(p.epsilonEquals(p, 0.0));
        Point3d p3 = p.translate(0.001, 0.0, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09));
        assertTrue(p3.epsilonEquals(p, 0.09));
        assertFalse(p.epsilonEquals(p3, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009));
        p3 = p.translate(0.0, 0.001, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09));
        assertTrue(p3.epsilonEquals(p, 0.09));
        assertFalse(p.epsilonEquals(p3, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009));
        p3 = p.translate(0.0, 0.0, 0.001);
        assertTrue(p.epsilonEquals(p3, 0.09));
        assertTrue(p3.epsilonEquals(p, 0.09));
        assertFalse(p.epsilonEquals(p3, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009));

        p2d = new Point2d(123, 456);
        p3 = new Point3d(p2d, 789);
        assertEquals(123, p3.x, 0, "x");
        assertEquals(456, p3.y, 0, "y");
        assertEquals(789, p3.z, 0, "z");

        Point2D p2D = new java.awt.geom.Point2D.Double(123, 456);
        p3 = new Point3d(p2D, 789);
        assertEquals(123, p3.x, 0, "x");
        assertEquals(456, p3.y, 0, "y");
        assertEquals(789, p3.z, 0, "z");
    }

    /**
     * Test the Point3d operators.
     */
    @Test
    public void testPoint3dOperators()
    {
        Point3d p = new Point3d(-0.1, -0.2, -0.3);
        assertEquals(0.1, p.abs().x, 1E-6);
        assertEquals(0.2, p.abs().y, 1E-6);
        assertEquals(0.3, p.abs().z, 1E-6);
        p = p.neg();
        assertEquals(0.1, p.x, 1E-6);
        assertEquals(0.2, p.y, 1E-6);
        assertEquals(0.3, p.z, 1E-6);
        p = p.scale(1.0);
        assertEquals(0.1, p.x, 1E-6);
        assertEquals(0.2, p.y, 1E-6);
        assertEquals(0.3, p.z, 1E-6);
        p = p.scale(10.0);
        assertEquals(1.0, p.x, 1E-6);
        assertEquals(2.0, p.y, 1E-6);
        assertEquals(3.0, p.z, 1E-6);
        p = p.translate(5.0, -1.0, 0.5);
        assertEquals(6.0, p.x, 1E-6);
        assertEquals(1.0, p.y, 1E-6);
        assertEquals(3.5, p.z, 1E-6);
        Point3d p3d = p.translate(1.0, 1.0, 1.0);
        assertEquals(7.0, p3d.x, 1E-6);
        assertEquals(2.0, p3d.y, 1E-6);
        assertEquals(4.5, p3d.z, 1E-6);
        p3d = p.translate(6.0, 1.0);
        assertEquals(12.0, p3d.x, 1E-6);
        assertEquals(2.0, p3d.y, 1E-6);
        assertEquals(3.5, p3d.z, 1E-6);

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

        try
        {
            p.translate(Double.NaN, 2.0, 3.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1.0, Double.NaN, 3.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1.0, 2.0, Double.NaN);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        // interpolate
        Point3d p1 = new Point3d(1.0, 1.0, 1.0);
        Point3d p2 = new Point3d(5.0, 5.0, 5.0);
        assertEquals(p1, p1.interpolate(p2, 0.0), "Interpolate at 0.0 returns this");
        assertEquals(p2, p2.interpolate(p1, 0.0));
        assertEquals(p1, p1.interpolate(p1, 0.0));
        assertEquals(new Point3d(3.0, 3.0, 3.0), p1.interpolate(p2, 0.5));

        // distance
        assertEquals(Math.sqrt(48.0), p1.distance(p2), 0.001);
        assertEquals(48.0, p1.distanceSquared(p2), 0.001);
        assertEquals(Math.sqrt(32.0), p1.horizontalDistance(p2), 0.001);
        assertEquals(32.0, p1.horizontalDistanceSquared(p2), 0.001);

        // direction
        assertEquals(Math.toRadians(45.0), p2.horizontalDirection(), 0.001);
        assertEquals(Math.toRadians(45.0), p1.horizontalDirection(p2), 0.001);
        assertEquals(0.0, new Point3d(0.0, 0.0, 0.0).horizontalDirection(), 0.001);
        assertEquals(Math.atan2(Math.sqrt(2.0), 1), p1.verticalDirection(p2), 0.001);
        assertEquals(Math.PI / 2, p1.verticalDirection(new Point3d(2.0, 2.0, 1.0)), 0.01);
        assertEquals(0, p1.verticalDirection(new Point3d(1.0, 1.0, 2.0)), 0.01);

        // normalize
        Point3d pn = p2.normalize();
        assertEquals(1.0 / Math.sqrt(3.0), pn.x, 0.001);
        assertEquals(1.0 / Math.sqrt(3.0), pn.y, 0.001);
        assertEquals(1.0 / Math.sqrt(3.0), pn.z, 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point3d(0.0, 0.0, 0.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

        assertEquals(1, p1.size(), "size of a Point3d is 1");
        Point2d projection = p1.project();
        assertEquals(p1.x, projection.x, 0, "projected x");
        assertEquals(p1.y, projection.y, 0, "projected y");

        Bounds3d bounds = p1.getBounds();
        assertEquals(p1.x, bounds.getMinX(), 0, "Bounds min x");
        assertEquals(p1.y, bounds.getMinY(), 0, "Bounds min y");
        assertEquals(p1.z, bounds.getMinZ(), 0, "Bounds min z");
        assertEquals(p1.x, bounds.getMaxX(), 0, "Bounds max x");
        assertEquals(p1.y, bounds.getMaxY(), 0, "Bounds max y");
        assertEquals(p1.z, bounds.getMaxZ(), 0, "Bounds max z");
    }

    /**
     * Test the Point3d operators for NPE.
     */
    @Test
    public void testPoint3dOperatorsNPE()
    {
        final Point3d p1 = new Point3d(1.0, 1.0, 1.0);

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

        // FIXME
        // Try.testFail(new Try.Execution()
        // {
        // @Override
        // public void execute() throws Throwable
        // {
        // p1.horizontalDistance((Point2d) null);
        // }
        // }, "Should throw NPE", NullPointerException.class);
        //
        // Try.testFail(new Try.Execution()
        // {
        // @Override
        // public void execute() throws Throwable
        // {
        // p1.horizontalDistanceSquared((Point3d) null);
        // }
        // }, "Should throw NPE", NullPointerException.class);

    }

    /**
     * Test the closestPointOnSegment and the closestPointOnLine methods.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     */
    @Test
    public void testClosestPointOnSegmentAndLine() throws DrawRuntimeException
    {
        Point3d p1 = new Point3d(-2, 3, 5);
        for (Point3d p2 : new Point3d[] {new Point3d(7, 4, -5)/* angled */, new Point3d(-3, 6, 5) /* also angled */,
                new Point3d(-2, -5, 5) /* vertical */, new Point3d(8, 3, 5)/* horizontal */, new Point3d(-2, 3, 1)/* z */ })
        {
            PolyLine3d line = new PolyLine3d(p1, p2);
            for (double x = -10; x <= 10; x += 0.5)
            {
                for (double y = -10; y <= 10; y += 0.5)
                {
                    for (double z = -10; z <= 10; z += 0.5)
                    {
                        Point3d p = new Point3d(x, y, z);
                        // Figure out the correct result using a totally different method (binary search over the line segment)
                        double fraction = 0.5;
                        double step = 0.25;
                        Point3d approximation = line.getLocationFraction(fraction);
                        double distance = approximation.distance(p);
                        // 10 iterations should get us to within one thousandth
                        for (int iteration = 0; iteration < 10; iteration++)
                        {
                            // Try stepping up
                            double upFraction = fraction + step;
                            Point3d upApproximation = line.getLocationFraction(upFraction);
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
                                Point3d downApproximation = line.getLocationFraction(downFraction);
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
                        Point3d result = p.closestPointOnSegment(p1, p2);
                        assertEquals(0, approximation.distance(result), line.getLength() / 1000,
                                "distance should be less than one thousandth of line length");
                        assertEquals(p1, p.closestPointOnSegment(p1, p1),
                                "zero length line segment should always return start point");
                        result = p.closestPointOnSegment(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
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
                            result = p.closestPointOnLine(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
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
                            // 10 iterations should get us to within one thousandth
                            for (int iteration = 0; iteration < 20; iteration++)
                            {
                                // Try stepping up
                                double upFraction = fraction + step;
                                Point3d upApproximation = line.getLocationFractionExtended(upFraction);
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
                                    Point3d downApproximation = line.getLocationFractionExtended(downFraction);
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
                            result = p.closestPointOnLine(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
                            assertEquals(0, approximation.distance(result), range / 1000,
                                    "distance should be less than one thousandth of range");
                            if (fraction < -0.001 || fraction > 1.001)
                            {
                                assertNull(new LineSegment3d(p1, p2).projectOrthogonal(p),
                                        "projectOrthogonal should return null");
                                assertEquals(result, new LineSegment3d(p1, p2).projectOrthogonalExtended(p),
                                        "projectOrthogonalExtended should return same result as closestPointOnLine");
                            }
                        }
                    }
                }
            }
        }

        try
        {
            p1.closestPointOnLine(null, new Point3d(5, 6, 7));
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(new Point3d(5, 6, 7), null);
            fail("null should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(Double.NaN, 7, 8, 9, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, Double.NaN, 8, 9, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, 7, Double.NaN, 9, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, 7, 8, Double.NaN, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, 7, 8, 9, Double.NaN, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnSegment(6, 7, 8, 9, 10, Double.NaN);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(Double.NaN, 7, 8, 9, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, Double.NaN, 8, 9, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, Double.NaN, 9, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, 8, Double.NaN, 10, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, 8, 9, Double.NaN, 11);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, 8, 9, 10, Double.NaN);
            fail("NaN value should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            p1.closestPointOnLine(6, 7, 8, 6, 7, 8);
            fail("identical points should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

    }

}
