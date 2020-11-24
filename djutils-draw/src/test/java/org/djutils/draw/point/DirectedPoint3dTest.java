package org.djutils.draw.d0;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Point2D;

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
        assertNotNull(p);
        assertEquals(10.0, p.getX(), 1E-6);
        assertEquals(-20.0, p.getY(), 1E-6);
        assertEquals(5.2, p.getZ(), 1E-6);
        assertEquals(0.0, p.getDirX(), 1E-6);
        assertEquals(0.0, p.getDirY(), 1E-6);
        assertEquals(0.0, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(new double[] { -18.7, 3.4, 5.6 });
        assertNotNull(p);
        assertEquals(-18.7, p.getX(), 1E-6);
        assertEquals(3.4, p.getY(), 1E-6);
        assertEquals(5.6, p.getZ(), 1E-6);
        assertEquals(0.0, p.getDirX(), 1E-6);
        assertEquals(0.0, p.getDirY(), 1E-6);
        assertEquals(0.0, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(10.0, -20.0, 5.2, 0.1, -0.2, Math.PI);
        assertNotNull(p);
        assertEquals(10.0, p.getX(), 1E-6);
        assertEquals(-20.0, p.getY(), 1E-6);
        assertEquals(5.2, p.getZ(), 1E-6);
        assertEquals(0.1, p.getDirX(), 1E-6);
        assertEquals(-0.2, p.getDirY(), 1E-6);
        assertEquals(3.1415926, p.getDirZ(), 1E-6);

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

        double[] p3Arr = new double[] { 5.0, 6.0, 7.0 };
        double[] rotArr = new double[] { 0.1, -0.2, 0.3 };
        p = new DirectedPoint3d(5.0, 6.0, 7.0, rotArr);
        assertEquals(5.0, p.getX(), 1E-6);
        assertEquals(6.0, p.getY(), 1E-6);
        assertEquals(7.0, p.getZ(), 1E-6);
        assertEquals(0.1, p.getDirX(), 1E-6);
        assertEquals(-0.2, p.getDirY(), 1E-6);
        assertEquals(0.3, p.getDirZ(), 1E-6);

        p = new DirectedPoint3d(p3Arr, rotArr);
        assertEquals(5.0, p.getX(), 1E-6);
        assertEquals(6.0, p.getY(), 1E-6);
        assertEquals(7.0, p.getZ(), 1E-6);
        assertEquals(0.1, p.getDirX(), 1E-6);
        assertEquals(-0.2, p.getDirY(), 1E-6);
        assertEquals(0.3, p.getDirZ(), 1E-6);
        assertArrayEquals(p3Arr, p.toArray(), 0.001);

        p = DirectedPoint3d.instantiateXY(10.0, 20.0);
        assertEquals(10.0, p.getX(), 1E-6);
        assertEquals(20.0, p.getY(), 1E-6);
        assertEquals(0.0, p.getZ(), 1E-6);
        assertEquals(0.0, p.getDirX(), 1E-6);
        assertEquals(0.0, p.getDirY(), 1E-6);
        assertEquals(0.0, p.getDirZ(), 1E-6);

        p = DirectedPoint3d.instantiateXY(new Point2d(10.0, 20.0));
        assertEquals(10.0, p.getX(), 1E-6);
        assertEquals(20.0, p.getY(), 1E-6);
        assertEquals(0.0, p.getZ(), 1E-6);
        assertEquals(0.0, p.getDirX(), 1E-6);
        assertEquals(0.0, p.getDirY(), 1E-6);
        assertEquals(0.0, p.getDirZ(), 1E-6);

        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = DirectedPoint3d.instantiateXY(p2DD);
        assertEquals(-0.1, p.getX(), 1E-6);
        assertEquals(-0.2, p.getY(), 1E-6);
        assertEquals(0.0, p.getZ(), 1E-6);
        assertEquals(0.0, p.getDirX(), 1E-6);
        assertEquals(0.0, p.getDirY(), 1E-6);
        assertEquals(0.0, p.getDirZ(), 1E-6);
        assertEquals(p2DD, p.toPoint2D());

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                DirectedPoint3d.instantiateXY((Point2D.Double) null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                DirectedPoint3d.instantiateXY((Point2d) null);
            }
        }, "Should throw NPE", NullPointerException.class);

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
        assertTrue(p.epsilonEquals(p, 0.1));
        assertTrue(p.epsilonEquals(p, 0.001));
        assertTrue(p.epsilonEquals(p, 0.0));
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
        p3 = p.rotate(0.001, 0.0, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.009));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.0009));
        p3 = p.rotate(0.0, 0.001, 0.0);
        assertTrue(p.epsilonEquals(p3, 0.09, 0.009));
        assertTrue(p3.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(p3, 0.0009, 0.0009));
        assertFalse(p3.epsilonEquals(p, 0.0009, 0.0009));
        p3 = p.rotate(0.0, 0.0, 0.001);
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

        p = p.neg();
        assertEquals(0.1, p.getX(), 1E-6);
        assertEquals(0.2, p.getY(), 1E-6);
        assertEquals(0.3, p.getZ(), 1E-6);
        assertEquals(-0.75 * Math.PI, p.getDirX(), 1E-6);
        assertEquals(0.75 * Math.PI, p.getDirY(), 1E-6);
        assertEquals(-Math.PI / 2, p.getDirZ(), 1E-6);

        p = p.scale(1.0);
        assertEquals(0.1, p.getX(), 1E-6);
        assertEquals(0.2, p.getY(), 1E-6);
        assertEquals(0.3, p.getZ(), 1E-6);
        assertEquals(-0.75 * Math.PI, p.getDirX(), 1E-6);
        assertEquals(0.75 * Math.PI, p.getDirY(), 1E-6);
        assertEquals(-Math.PI / 2, p.getDirZ(), 1E-6);

        p = p.scale(10.0);
        assertEquals(1.0, p.getX(), 1E-6);
        assertEquals(2.0, p.getY(), 1E-6);
        assertEquals(3.0, p.getZ(), 1E-6);
        assertEquals(-0.75 * Math.PI, p.getDirX(), 1E-6);
        assertEquals(0.75 * Math.PI, p.getDirY(), 1E-6);
        assertEquals(-Math.PI / 2, p.getDirZ(), 1E-6);

        p = p.translate(5.0, -1.0, 2.0);
        assertEquals(6.0, p.getX(), 1E-6);
        assertEquals(1.0, p.getY(), 1E-6);
        assertEquals(5.0, p.getZ(), 1E-6);
        assertEquals(-0.75 * Math.PI, p.getDirX(), 1E-6);
        assertEquals(0.75 * Math.PI, p.getDirY(), 1E-6);
        assertEquals(-Math.PI / 2, p.getDirZ(), 1E-6);

        p = p.rotate(Math.PI / 4, -Math.PI / 4, Math.PI);
        assertEquals(6.0, p.getX(), 1E-6);
        assertEquals(1.0, p.getY(), 1E-6);
        assertEquals(5.0, p.getZ(), 1E-6);
        assertEquals(-0.5 * Math.PI, p.getDirX(), 1E-6);
        assertEquals(0.5 * Math.PI, p.getDirY(), 1E-6);
        assertEquals(0.5 * Math.PI, p.getDirZ(), 1E-6);

        p = p.rotate(Math.PI / 4);
        assertEquals(6.0, p.getX(), 1E-6);
        assertEquals(1.0, p.getY(), 1E-6);
        assertEquals(5.0, p.getZ(), 1E-6);
        assertEquals(-0.5 * Math.PI, p.getDirX(), 1E-6);
        assertEquals(0.5 * Math.PI, p.getDirY(), 1E-6);
        assertEquals(0.75 * Math.PI, p.getDirZ(), 1E-6);

        // interpolate
        DirectedPoint3d p1 = new DirectedPoint3d(1.0, 1.0, 1.0, 0.2, 0.3, 0.4);
        DirectedPoint3d p2 = new DirectedPoint3d(5.0, 5.0, 5.0, 0.4, 0.5, 0.6);
        assertEquals(p1, DirectedPoint.interpolate(p1, p2, 0.0));
        assertEquals(p2, DirectedPoint.interpolate(p1, p2, 1.0));
        assertEquals(p1, p1.interpolate(p2, 0.0));
        assertEquals(p2, p2.interpolate(p1, 0.0));
        assertEquals(p1, p1.interpolate(p1, 0.0));
        assertTrue(new DirectedPoint3d(3.0, 3.0, 3.0, 0.3, 0.4, 0.5).epsilonEquals(p1.interpolate(p2, 0.5), 1E-6, 1E-6));

        // distance
        assertEquals(Math.sqrt(48.0), p1.distance(p2), 0.001);
        assertEquals(48.0, p1.distanceSquared(p2), 0.001);
        assertEquals(Math.sqrt(32.0), p1.horizontalDistance(p2), 0.001);
        assertEquals(32.0, p1.horizontalDistanceSquared(p2), 0.001);
        assertEquals(Math.sqrt(48.0), Point.distance(p2, p1), 0.001);
        assertEquals(48.0, Point.distanceSquared(p2, p1), 0.001);
        assertEquals(Math.sqrt(32.0), Point.horizontalDistance(p2, p1), 0.001);
        assertEquals(32.0, Point.horizontalDistanceSquared(p2, p1), 0.001);

        // direction
        assertEquals(Math.toRadians(45.0), p2.horizontalDirection(), 0.001);
        assertEquals(Math.toRadians(45.0), p1.horizontalDirection(p2), 0.001);
        assertEquals(0.0, new DirectedPoint3d(0.0, 0.0, 0.0).horizontalDirection(), 0.001);

        // normalize
        DirectedPoint3d pn = p2.normalize();
        assertEquals(1.0 / Math.sqrt(3.0), pn.getX(), 0.001);
        assertEquals(1.0 / Math.sqrt(3.0), pn.getY(), 0.001);
        assertEquals(1.0 / Math.sqrt(3.0), pn.getZ(), 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint3d(0.0, 0.0, 0.0, Math.PI / 4.0, Math.PI / 4.0, Math.PI / 4.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

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
                p1.horizontalDistance(null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.horizontalDistanceSquared(null);
            }
        }, "Should throw NPE", NullPointerException.class);

        // statics, 1st arg is null

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.interpolate(null, p1, 0.5);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.distance(null, p1);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.distanceSquared(null, p1);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.horizontalDistance(null, p1);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.horizontalDistanceSquared(null, p1);
            }
        }, "Should throw NPE", NullPointerException.class);

        // statics, 2nd arg is null

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.interpolate(p1, null, 0.5);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.distance(p1, null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.distanceSquared(p1, null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.horizontalDistance(p1, null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                Point.horizontalDistanceSquared(p1, null);
            }
        }, "Should throw NPE", NullPointerException.class);

    }

}
