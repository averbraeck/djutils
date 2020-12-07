package org.djutils.draw.point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * DirectedPoint3dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint3dTest
{
    /**
     * Test the DirectedPoint3d construction methods.
     */
    @Test
    public void testDirectedPoint3dConstruction()
    {
        DirectedPoint3d p = new DirectedPoint3d(10.0, -20.0, 5.2);
        assertEquals("x", 10.0, p.getX(), 1E-6);
        assertEquals("y", -20.0, p.getY(), 1E-6);
        assertEquals("z", 5.2, p.getZ(), 1E-6);
        assertEquals("dirX", 0.0, p.getDirX(), 1E-6);
        assertEquals("dirY", 0.0, p.getDirY(), 1E-6);
        assertEquals("dirZ", 0.0, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(new double[] { -18.7, 3.4, 5.6 });
        assertEquals("x", -18.7, p.getX(), 1E-6);
        assertEquals("y", 3.4, p.getY(), 1E-6);
        assertEquals("z", 5.6, p.getZ(), 1E-6);
        assertEquals("dirX", 0.0, p.getDirX(), 1E-6);
        assertEquals("dirY", 0.0, p.getDirY(), 1E-6);
        assertEquals("dirZ", 0.0, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(10.0, -20.0, 5.2, 0.1, -0.2, Math.PI);
        assertEquals("x", 10.0, p.getX(), 1E-6);
        assertEquals("y", -20.0, p.getY(), 1E-6);
        assertEquals("z", 5.2, p.getZ(), 1E-6);
        assertEquals("dirX", 0.1, p.getDirX(), 1E-6);
        assertEquals("dirY", -0.2, p.getDirY(), 1E-6);
        assertEquals("dirZ", 3.1415926, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(new double[] { -18.7, 3.4, 5.6 }, 0.1, -0.2, Math.PI);
        {
            assertEquals("x", -18.7, p.getX(), 1E-6);
            assertEquals("y", 3.4, p.getY(), 1E-6);
            assertEquals("z", 5.6, p.getZ(), 1E-6);
            assertEquals("dirX", 0.1, p.getDirX(), 1E-6);
            assertEquals("dirY", -0.2, p.getDirY(), 1E-6);
            assertEquals("dirZ", 3.1415926, p.getDirZ(), 1E-6);
        }

        p = new DirectedPoint3d(new Point3d(new double[] { -18.7, 3.4, 5.6 }), 0.1, -0.2, Math.PI);
        {
            assertEquals("x", -18.7, p.getX(), 1E-6);
            assertEquals("y", 3.4, p.getY(), 1E-6);
            assertEquals("z", 5.6, p.getZ(), 1E-6);
            assertEquals("dirX", 0.1, p.getDirX(), 1E-6);
            assertEquals("dirY", -0.2, p.getDirY(), 1E-6);
            assertEquals("dirZ", 3.1415926, p.getDirZ(), 1E-6);
        }

        try
        {
            new DirectedPoint3d(0, 0, 0, Double.NaN, 0, 0);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(0, 0, 0, 0, Double.NaN, 0);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(0, 0, 0, 0, 0, Double.NaN);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(new double[3], Double.NaN, 0, 0);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(new double[3], 0, Double.NaN, 0);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(new double[3], 0, 0, Double.NaN);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(new Point3d(1, 2, 3), Double.NaN, 0, 0);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(new Point3d(1, 2, 3), 0, Double.NaN, 0);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new DirectedPoint3d(new Point3d(1, 2, 3), 0, 0, Double.NaN);
            fail("NaN rotation should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        double[] p3Arr = new double[] { 5.0, 6.0, 7.0 };
        double[] rotArr = new double[] { 0.1, -0.2, 0.3 };
        p = new DirectedPoint3d(5.0, 6.0, 7.0, rotArr);
        assertEquals("x", 5.0, p.getX(), 0);
        assertEquals("y", 6.0, p.getY(), 0);
        assertEquals("z", 7.0, p.getZ(), 0);
        assertEquals("dirX", 0.1, p.getDirX(), 1E-6);
        assertEquals("dirY", -0.2, p.getDirY(), 1E-6);
        assertEquals("dirZ", 0.3, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(p3Arr, rotArr);
        assertEquals("x", 5.0, p.getX(), 0);
        assertEquals("y", 6.0, p.getY(), 0);
        assertEquals("z", 7.0, p.getZ(), 0);
        assertEquals("dirX", 0.1, p.getDirX(), 1E-6);
        assertEquals("dirY", -0.2, p.getDirY(), 1E-6);
        assertEquals("dirZ", 0.3, p.getDirZ(), 1E-6);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(0.1, 0.2, 0.3, new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(0.1, 0.2, 0.3, new double[] { 0.1, 0.2 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(0.1, 0.2, 0.3, new double[] { 0.1, 0.2, 0.3, 0.4 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(new double[] { 0.1, 0.2 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(new double[] { 0.1, 0.2, 0.3, 0.4 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(new double[] { 1, 2, 3 }, new double[] { 0.1, 0.2 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(new double[] { 1, 2, 3 }, new double[] { 0.1, 0.2, 0.3, 0.4 });
            }
        }, "Should throw IAE", IllegalArgumentException.class);

    }

    /**
     * Test the DirectedPoint3d construction methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testDirectedPointEquals()
    {
        // equals and hashCode
        DirectedPoint3d p = new DirectedPoint3d(10.0, 20.0, 30.0, 0.1, 0.2, 0.3);
        assertTrue(p.equals(p));
        assertEquals(p.hashCode(), p.hashCode());
        assertFalse(p.equals(new Point2d(10.0, 20.0)));
        assertFalse(p.equals(null));
        assertNotEquals(new Point2d(10.0, 20.0), p.hashCode());
        assertEquals(0.3, p.getDirZ(), 1E-6);
        assertTrue(p.equals(p.translate(0.0, 0.0, 0.0)));
        assertFalse(p.equals(p.translate(1.0, 0.0, 0.0)));
        assertFalse(p.equals(p.translate(0.0, 1.0, 0.0)));
        assertFalse(p.equals(p.translate(0.0, 0.0, 1.0)));
        assertFalse(p.equals(p.rotate(0.1)));
        assertFalse(p.equals(p.rotate(0.1, 0.0, 0.0)));
        assertFalse(p.equals(p.rotate(0.0, 0.1, 0.0)));
        assertFalse(p.equals(p.rotate(0.0, 0.0, 0.1)));

        // toString
        p = new DirectedPoint3d(10.0, 20.0, 30.0, 0.1, 0.2, 0.3);
        assertEquals("[(10.000000,20.000000,30.000000), rot=(0.100000,0.200000,0.300000)]", p.toString());
        assertEquals("[(10.0,20.0,30.0), rot=(0.1,0.2,0.3)]", p.toString(1));
        assertEquals("[(10,20,30), rot=(0,0,0)]", p.toString(0));
        assertEquals("[(10,20,30), rot=(0,0,0)]", p.toString(-1));

        // epsilonEquals
        assertTrue(p.epsilonEquals(p, 0.1, 999));
        assertTrue(p.epsilonEquals(p, 0.001, 999));
        assertTrue(p.epsilonEquals(p, 0.0, 999));
        DirectedPoint3d p3 = p.translate(0.001, 0.0, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.001));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.001));
        p3 = p.translate(0.0, 0.001, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.001));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.001));
        p3 = p.translate(0.0, 0.0, 0.001);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.001));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.001));
        p3 = p.rotate(0.001);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.009));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.0009));
        p3 = p.rotate(0.001, 0, 0);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.009));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.0009));
        p3 = p.rotate(0, 0.001, 0);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.009));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.0009));
        p3 = p.rotate(0, 0, 0.001);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.009));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.0009));
    }

    /**
     * Test the DirectedPoint3d operators.
     */
    @Test
    public void testDirectedPoint3dOperators()
    {
        DirectedPoint3d p = new DirectedPoint3d(-0.1, -0.2, -0.3, Math.PI / 4, -Math.PI / 4, Math.PI / 2);
        assertEquals(0.1, p.abs().getX(), 1E-6);
        assertEquals(0.2, p.abs().getY(), 1E-6);
        assertEquals(0.3, p.abs().getZ(), 1E-6);
        assertEquals(Math.PI / 4, p.abs().getDirX(), 1E-6);
        assertEquals(-Math.PI / 4, p.abs().getDirY(), 1E-6);
        assertEquals(Math.PI / 2, p.abs().getDirZ(), 1E-6);

        Iterator<DirectedPoint3d> i = p.getPoints();
        assertTrue("iterator has one point", i.hasNext());
        assertEquals("iterator returns p", p, i.next());
        assertFalse("iterator does not have another point", i.hasNext());

        assertEquals("getLocation returns p", p, p.getLocation());

        DirectedPoint3d p2 = p.neg();
        assertEquals("negated x", -p.getX(), p2.getX(), 1E-6);
        assertEquals("negated y", -p.getY(), p2.getY(), 1E-6);
        assertEquals("negated z", -p.getZ(), p2.getZ(), 1E-6);
        assertEquals("negated dirX", AngleUtil.normalizeAroundZero(p.getDirX() + Math.PI), p2.getDirX(), 1E-6);
        assertEquals("negated dirY", AngleUtil.normalizeAroundZero(p.getDirY() + Math.PI), p2.getDirY(), 1E-6);
        assertEquals("negated dirZ", AngleUtil.normalizeAroundZero(p.getDirZ() + Math.PI), p2.getDirZ(), 1E-6);

        p2 = p.scale(1.0);
        assertEquals("unity scaled x", p.getX(), p2.getX(), 0);
        assertEquals("unity scaled y", p.getY(), p2.getY(), 0);
        assertEquals("unity scaled z", p.getZ(), p2.getZ(), 0);
        assertEquals("unity scaled dirX", p.getDirX(), p2.getDirX(), 0);
        assertEquals("unity scaled dirY", p.getDirY(), p2.getDirY(), 0);
        assertEquals("unity scaled dirZ", p.getDirZ(), p2.getDirZ(), 0);

        p2 = p.scale(10.0);
        assertEquals("10 scaled x", 10 * p.getX(), p2.getX(), 1E-6);
        assertEquals("10 scaled y", 10 * p.getY(), p2.getY(), 1E-6);
        assertEquals("10 scaled z", 10 * p.getZ(), p2.getZ(), 1E-6);
        assertEquals("10 scaled dirX", p.getDirX(), p2.getDirX(), 0);
        assertEquals("10 scaled dirY", p.getDirY(), p2.getDirY(), 0);
        assertEquals("10 scaled dirZ", p.getDirZ(), p2.getDirZ(), 0);

        try
        {
            p.translate(Double.NaN, 2);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1, Double.NaN);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(Double.NaN, 2, 3);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1, Double.NaN, 3);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1, 2, Double.NaN);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        p2 = p.translate(5.0, -1.0, 2.0);
        assertEquals("translated x", p.getX() + 5.0, p2.getX(), 1E-6);
        assertEquals("translated y", p.getY() - 1.0, p2.getY(), 1E-6);
        assertEquals("translated z", p.getZ() + 2.0, p2.getZ(), 1E-6);
        assertEquals("translated dirX", p.getDirX(), p2.getDirX(), 1E-6);
        assertEquals("translated dirY", p.getDirY(), p2.getDirY(), 1E-6);
        assertEquals("translated dirZ", p.getDirZ(), p2.getDirZ(), 1E-6);

        p2 = p.translate(5.0, -1.0);
        assertEquals("translated x", p.getX() + 5.0, p2.getX(), 1E-6);
        assertEquals("translated y", p.getY() - 1.0, p2.getY(), 1E-6);
        assertEquals("not translated z", p.getZ(), p2.getZ(), 1E-6);
        assertEquals("translated dirX", p.getDirX(), p2.getDirX(), 1E-6);
        assertEquals("translated dirY", p.getDirY(), p2.getDirY(), 1E-6);
        assertEquals("translated dirZ", p.getDirZ(), p2.getDirZ(), 1E-6);

        p2 = p.rotate(15 * Math.PI / 4, -Math.PI / 4, Math.PI);
        assertEquals("rotated x", p.getX(), p2.getX(), 1E-6);
        assertEquals("rotated y", p.getY(), p2.getY(), 1E-6);
        assertEquals("rotated z", p.getZ(), p2.getZ(), 1E-6);
        assertEquals("rotated dirX", AngleUtil.normalizeAroundZero(p.getDirX() + 15 * Math.PI / 4), p2.getDirX(), 1E-6);
        assertEquals("rotated dirY", AngleUtil.normalizeAroundZero(p.getDirY() - Math.PI / 4), p2.getDirY(), 1E-6);
        assertEquals("rotated dirZ", AngleUtil.normalizeAroundZero(p.getDirZ() + Math.PI), p2.getDirZ(), 1E-6);

        p2 = p.rotate(17 * Math.PI / 4);
        assertEquals("rotated x", p.getX(), p2.getX(), 1E-6);
        assertEquals("rotated y", p.getY(), p2.getY(), 1E-6);
        assertEquals("rotated z", p.getZ(), p2.getZ(), 1E-6);
        assertEquals("not rotated dirX", p.getDirX(), p2.getDirX(), 1E-6);
        assertEquals("not rotated dirY", p.getDirY(), p2.getDirY(), 1E-6);
        assertEquals("rotated dirZ", AngleUtil.normalizeAroundZero(p.getDirZ() + 17 * Math.PI / 4), p2.getDirZ(), 1E-6);

        // interpolate
        DirectedPoint3d p1 = new DirectedPoint3d(1.0, 2.0, 3.0, 0.2, 0.3, 0.4);
        p2 = new DirectedPoint3d(5.0, 7.0, 9.0, 0.4, 0.5, 0.6);
        assertEquals("p1 interpolated to p2 at 0", p1, p1.interpolate(p2, 0.0));
        assertEquals("p1 interpolated to p2 at 1", p2, p1.interpolate(p2, 1.0));
        assertEquals("p2 interpolated to p1 at 0", p2, p2.interpolate(p1, 0.0));
        assertEquals("p2 interpolated to p1 at 1", p1, p2.interpolate(p1, 1.0));
        assertEquals("p1 interpolated to itself at 0", p1, p1.interpolate(p1, 0.0));
        assertTrue("p1 interpolated to p2 at 0.5",
                new DirectedPoint3d(3.0, 4.5, 6.0, 0.3, 0.4, 0.5).epsilonEquals(p1.interpolate(p2, 0.5), 1E-6, 1E-6));
        assertTrue("p1 extrapolated to p2 at 2",
                new DirectedPoint3d(9.0, 12.0, 15.0, 0.6, 0.7, 0.8).epsilonEquals(p1.interpolate(p2, 2), 1E-6, 1E-6));
        assertTrue("p1 extrapolated to p2 at -1",
                new DirectedPoint3d(-3.0, -3.0, -3.0, 0.0, 0.1, 0.2).epsilonEquals(p1.interpolate(p2, -1), 1E-6, 1E-6));

        // distance
        assertEquals("Distance", Math.sqrt(16 + 25 + 36), p1.distance(p2), 0.001);
        assertEquals("Distance squared", 16 + 25 + 36, p1.distanceSquared(p2), 0.001);
        assertEquals("Horizontal distance", Math.sqrt(16 + 25), p1.horizontalDistance(p2), 0.001);
        assertEquals("Horizontal distance squared", 16 + 25, p1.horizontalDistanceSquared(p2), 0.001);

        // direction
        assertEquals("Horizontal direction", Math.atan2(7, 5), p2.horizontalDirection(), 0.001);
        assertEquals("Horizontal direction", Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()),
                p1.horizontalDirection(p2), 0.001);
        assertEquals(0.0, new DirectedPoint3d(0.0, 0.0, 0.0).horizontalDirection(), 0.001);

        // normalize
        DirectedPoint3d pn = p2.normalize();
        assertEquals("normalized x", p2.getX() / Math.sqrt(25 + 49 + 81), pn.getX(), 0.001);
        assertEquals("normalized y", p2.getY() / Math.sqrt(25 + 49 + 81), pn.getY(), 0.001);
        assertEquals("normalized z", p2.getZ() / Math.sqrt(25 + 49 + 81), pn.getZ(), 0.001);
        assertEquals("normalized dirX", p2.getDirX(), pn.getDirX(), 0);
        assertEquals("normalized dirY", p2.getDirY(), pn.getDirY(), 0);
        assertEquals("normalized dirZ", p2.getDirZ(), pn.getDirZ(), 0);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(0.0, 0.0, 0.0, Math.PI / 4.0, Math.PI / 4.0, Math.PI / 4.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(Double.NaN, 0, 0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(0, Double.NaN, 0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(0, 0, Double.NaN);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(Double.NaN, 2);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(1, Double.NaN);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(Double.NaN, 2, 3);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(1, Double.NaN, 3);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(1, 2, Double.NaN);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

    }

    /**
     * Test the DirectedPoint3d operators for NPE.
     */
    @Test
    public void testDirectedPoint3dOperatorsNPE()
    {
        final DirectedPoint3d p1 = new DirectedPoint3d(1.0, 1.0, Math.PI / 4.0);

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

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.horizontalDistance((DirectedPoint3d) null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.horizontalDistanceSquared((DirectedPoint3d) null);
            }
        }, "Should throw NPE", NullPointerException.class);

    }

}
