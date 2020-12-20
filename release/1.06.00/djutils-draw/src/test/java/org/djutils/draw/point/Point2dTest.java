package org.djutils.draw.point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Point2D;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * Point2dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        assertEquals(10.0, p.getX(), 1E-6);
        assertEquals(-20.0, p.getY(), 1E-6);

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
        assertEquals(5.0, p.getX(), 0);
        assertEquals(6.0, p.getY(), 0);
        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = new Point2d(p2DD);
        assertEquals(-0.1, p.getX(), 1E-6);
        assertEquals(-0.2, p.getY(), 1E-6);
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
        assertEquals("x", p.getX() + 1, p3d.getX(), 0.00001);
        assertEquals("y", p.getY() + 2, p3d.getY(), 0.00001);
        assertEquals("z", 3, p3d.getZ(), 0);

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
        assertEquals(0.1, p.abs().getX(), 1E-6);
        assertEquals(0.2, p.abs().getY(), 1E-6);
        p = p.neg();
        assertEquals(0.1, p.getX(), 1E-6);
        assertEquals(0.2, p.getY(), 1E-6);
        p = p.scale(1.0);
        assertEquals(0.1, p.getX(), 1E-6);
        assertEquals(0.2, p.getY(), 1E-6);
        p = p.scale(10.0);
        assertEquals(1.0, p.getX(), 1E-6);
        assertEquals(2.0, p.getY(), 1E-6);
        p = p.translate(5.0, -1.0);
        assertEquals(6.0, p.getX(), 1E-6);
        assertEquals(1.0, p.getY(), 1E-6);
        Point3d p3d = p.translate(1.0, 1.0, 1.0);
        assertEquals(7.0, p3d.getX(), 1E-6);
        assertEquals(2.0, p3d.getY(), 1E-6);
        assertEquals(1.0, p3d.getZ(), 1E-6);

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
        assertEquals(1.0 / Math.sqrt(2.0), pn.getX(), 0.001);
        assertEquals(1.0 / Math.sqrt(2.0), pn.getY(), 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Point2d(0.0, 0.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

        Bounds2d bounds = p1.getBounds();
        assertEquals("Bounds min x", p1.getX(), bounds.getMinX(), 0);
        assertEquals("Bounds min y", p1.getY(), bounds.getMinY(), 0);
        assertEquals("Bounds max x", p1.getX(), bounds.getMaxX(), 0);
        assertEquals("Bounds max y", p1.getY(), bounds.getMaxY(), 0);
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

}
