package org.djutils.draw.point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Point2D;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * Point3dTest.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
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
        assertEquals(10.0, p.x, 0);
        assertEquals(-20.0, p.y, 0);
        assertEquals(16.0, p.z, 0);
        
        assertEquals("size method returns 1", 1, p.size());
        
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
        assertEquals("Translating over 0,0,0 returns p", p, p.translate(0.0, 0.0, 0.0));
        assertNotEquals(p, p.translate(1.0, 0.0, 0.0));
        assertNotEquals(p, p.translate(0.0, 1.0, 0.0));
        assertNotEquals(p, p.translate(0.0, 0.0, 1.0));

        // toString
        p = new Point3d(10.0, 20.0, 30.0);
        assertEquals("(10.000000,20.000000,30.000000)", p.toString());
        assertEquals("(10.0,20.0,30.0)", p.toString(1));
        assertEquals("(10,20,30)", p.toString(0));
        assertEquals("(10,20,30)", p.toString(-1));

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
        assertEquals("x", 123, p3.x, 0);
        assertEquals("y", 456, p3.y, 0);
        assertEquals("z", 789, p3.z, 0);
        
        Point2D p2D = new java.awt.geom.Point2D.Double(123, 456);
        p3 = new Point3d(p2D, 789);
        assertEquals("x", 123, p3.x, 0);
        assertEquals("y", 456, p3.y, 0);
        assertEquals("z", 789, p3.z, 0);
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
            p.translate(1.0,  Double.NaN);
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
            p.translate(1.0,  Double.NaN, 3.0);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1.0,  2.0, Double.NaN);
            fail("NaN translation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        // interpolate
        Point3d p1 = new Point3d(1.0, 1.0, 1.0);
        Point3d p2 = new Point3d(5.0, 5.0, 5.0);
        assertEquals("Interpolate at 0.0 returns this", p1, p1.interpolate(p2, 0.0));
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
        
        assertEquals("size of a Point3d is 1", 1, p1.size());
        Point2d projection = p1.project();
        assertEquals("projected x", p1.x, projection.x, 0);
        assertEquals("projected y", p1.y, projection.y, 0);

        Bounds3d bounds = p1.getBounds();
        assertEquals("Bounds min x", p1.x, bounds.getMinX(), 0);
        assertEquals("Bounds min y", p1.y, bounds.getMinY(), 0);
        assertEquals("Bounds min z", p1.z, bounds.getMinZ(), 0);
        assertEquals("Bounds max x", p1.x, bounds.getMaxX(), 0);
        assertEquals("Bounds max y", p1.y, bounds.getMaxY(), 0);
        assertEquals("Bounds max z", p1.z, bounds.getMaxZ(), 0);
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
//        Try.testFail(new Try.Execution()
//        {
//            @Override
//            public void execute() throws Throwable
//            {
//                p1.horizontalDistance((Point2d) null);
//            }
//        }, "Should throw NPE", NullPointerException.class);
//
//        Try.testFail(new Try.Execution()
//        {
//            @Override
//            public void execute() throws Throwable
//            {
//                p1.horizontalDistanceSquared((Point3d) null);
//            }
//        }, "Should throw NPE", NullPointerException.class);

    }

}
