package org.djutils.draw.d0;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * DirectedPoint2dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint2dTest
{
    /**
     * Test the DirectedPoint2d construction methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testDirectedPoint2dConstruction()
    {
        DirectedPoint2d p = new DirectedPoint2d(10.0, -20.0, Math.PI);
        assertNotNull(p);
        assertEquals(10.0, p.getX(), 1E-6);
        assertEquals(-20.0, p.getY(), 1E-6);
        assertEquals(0.0, p.getZ(), 1E-6);
        assertEquals(0.0, p.getRotX(), 1E-6);
        assertEquals(0.0, p.getRotY(), 1E-6);
        assertEquals(3.1415926, p.getRotZ(), 1E-6);
        DirectedPoint2d pNaN = new DirectedPoint2d(Double.NaN, Double.NaN, Double.NaN);
        assertNotNull(pNaN);
        assertTrue(Double.isNaN(pNaN.getX()));
        assertTrue(Double.isNaN(pNaN.getY()));
        assertEquals(0.0, p.getZ(), 1E-6);
        assertEquals(0.0, p.getRotX(), 1E-6);
        assertEquals(0.0, p.getRotY(), 1E-6);
        assertTrue(Double.isNaN(pNaN.getRotZ()));
        double[] p2Arr = new double[] {5.0, 6.0};
        p = new DirectedPoint2d(p2Arr, Math.PI / 2.0);
        assertEquals(5.0, p.getX(), 1E-6);
        assertEquals(6.0, p.getY(), 1E-6);
        assertArrayEquals(p2Arr, p.toArray(), 0.001);
        assertEquals(0.0, p.getRotX(), 1E-6);
        assertEquals(0.0, p.getRotY(), 1E-6);
        assertEquals(3.1415926 / 2.0, p.getRotZ(), 1E-6);
        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = new DirectedPoint2d(p2DD, Math.PI / 4.0);
        assertEquals(-0.1, p.getX(), 1E-6);
        assertEquals(-0.2, p.getY(), 1E-6);
        assertEquals(p2DD, p.toPoint2D());
        assertEquals(0.0, p.getRotX(), 1E-6);
        assertEquals(0.0, p.getRotY(), 1E-6);
        assertEquals(3.1415926 / 4.0, p.getRotZ(), 1E-6);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d((Point2D.Double) null, 0.0);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d((Point2D.Double) null, Math.PI);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d(new double[] {}, Math.PI / 2.0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d(new double[] {1.0}, Math.PI / 4.0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d(new double[] {1.0, 2.0, 3.0}, Math.PI);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        // equals and hashCode
        assertTrue(p.equals(p));
        assertEquals(p.hashCode(), p.hashCode());
        DirectedPoint3d p3d = p.translate(1.0, 1.0, 1.0);
        assertFalse(p.equals(p3d));
        assertFalse(p.equals(null));
        assertNotEquals(p3d.hashCode(), p.hashCode());
        assertEquals(Math.PI / 4.0, p.getRotZ(), 1E-6);
        assertTrue(p.equals(p.translate(0.0, 0.0)));
        assertFalse(p.equals(p.translate(1.0, 0.0)));
        assertFalse(p.equals(p.translate(0.0, 1.0)));
        assertFalse(p.equals(p.rotate(0.1)));

        // toString
        p = new DirectedPoint2d(10.0, 20.0, Math.PI);
        assertEquals("[(10.000000,20.000000), rot=3.141593]", p.toString());
        assertEquals("[(10.0,20.0), rot=3.1]", p.toString(1));
        assertEquals("[(10,20), rot=3]", p.toString(0));
        assertEquals("[(10,20), rot=3]", p.toString(-1));

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

        // NaN
        assertFalse(p.epsilonEquals(new DirectedPoint3d(Double.NaN, 20.0, 0.0), 0.1, 0.001));
        assertFalse(p.epsilonEquals(new DirectedPoint3d(10.0, Double.NaN, 0.0), 0.1, 0.001));
        assertFalse(p.epsilonEquals(new DirectedPoint3d(10.0, 20.0, Double.NaN), 0.1, 0.001));
        assertFalse(new DirectedPoint3d(Double.NaN, 20.0, 0.0).epsilonEquals(p, 0.1, 0.001));
        assertFalse(new DirectedPoint3d(10.0, Double.NaN, 0.0).epsilonEquals(p, 0.1, 0.001));
        assertFalse(new DirectedPoint3d(10.0, 20.0, Double.NaN).epsilonEquals(p, 0.1, 0.001));
        assertFalse(new DirectedPoint3d(10.0, 20.0, 0.0, Double.NaN, 0.0, 0.0).epsilonEquals(p, 0.1, 0.001));
        assertFalse(new DirectedPoint3d(10.0, 20.0, 0.0, 0.0, Double.NaN, 0.0).epsilonEquals(p, 0.1, 0.001));
        assertFalse(new DirectedPoint3d(10.0, 20.0, 0.0, 0.0, 0.0, Double.NaN).epsilonEquals(p, 0.1, 0.001));
    }

    /**
     * Test the DirectedPoint2d operators.
     */
    @Test
    public void testDirectedPoint2dOperators()
    {
        DirectedPoint2d p = new DirectedPoint2d(-0.1, -0.2, -Math.PI / 2);
        assertEquals(0.1, p.abs().getX(), 1E-6);
        assertEquals(0.2, p.abs().getY(), 1E-6);
        assertEquals(-Math.PI / 2, p.abs().getRotZ(), 1E-6);
        
        p = p.neg();
        assertEquals(0.1, p.getX(), 1E-6);
        assertEquals(0.2, p.getY(), 1E-6);
        assertEquals(Math.PI / 2, p.getRotZ(), 1E-6);
        
        p = p.scale(1.0);
        assertEquals(0.1, p.getX(), 1E-6);
        assertEquals(0.2, p.getY(), 1E-6);
        assertEquals(Math.PI / 2, p.getRotZ(), 1E-6);

        p = p.scale(10.0);
        assertEquals(1.0, p.getX(), 1E-6);
        assertEquals(2.0, p.getY(), 1E-6);
        assertEquals(Math.PI / 2, p.getRotZ(), 1E-6);

        p = p.translate(5.0, -1.0);
        assertEquals(6.0, p.getX(), 1E-6);
        assertEquals(1.0, p.getY(), 1E-6);
        assertEquals(Math.PI / 2, p.getRotZ(), 1E-6);

        Point3d p3d = p.translate(1.0, 1.0, 1.0);
        assertEquals(7.0, p3d.getX(), 1E-6);
        assertEquals(2.0, p3d.getY(), 1E-6);
        assertEquals(1.0, p3d.getZ(), 1E-6);
        assertEquals(Math.PI / 2, p.getRotZ(), 1E-6);

        p = p.rotate(-Math.PI / 4);
        assertEquals(6.0, p.getX(), 1E-6);
        assertEquals(1.0, p.getY(), 1E-6);
        assertEquals(0.0, p.getZ(), 1E-6);
        assertEquals(0.0, p.getRotX(), 1E-6);
        assertEquals(0.0, p.getRotY(), 1E-6);
        assertEquals(Math.PI / 4, p.getRotZ(), 1E-6);

        // interpolate
        DirectedPoint2d p1 = new DirectedPoint2d(1.0, 1.0, 0.0);
        DirectedPoint2d p2 = new DirectedPoint2d(5.0, 5.0, Math.PI / 2.0);
        assertEquals(p1, DirectedPoint.interpolate(p1, p2, 0.0));
        assertEquals(p2, DirectedPoint.interpolate(p1, p2, 1.0));
        assertEquals(p1, p1.interpolate(p2, 0.0));
        assertEquals(p2, p2.interpolate(p1, 0.0));
        assertEquals(p1, p1.interpolate(p1, 0.0));
        assertEquals(new DirectedPoint2d(3.0, 3.0, Math.PI / 4.0), p1.interpolate(p2, 0.5));

        // distance
        assertEquals(Math.sqrt(32.0), p1.distance(p2), 0.001);
        assertEquals(32.0, p1.distanceSquared(p2), 0.001);
        assertEquals(Math.sqrt(32.0), p1.horizontalDistance(p2), 0.001);
        assertEquals(32.0, p1.horizontalDistanceSquared(p2), 0.001);
        assertEquals(Math.sqrt(32.0), Point.distance(p2, p1), 0.001);
        assertEquals(32.0, Point.distanceSquared(p2, p1), 0.001);
        assertEquals(Math.sqrt(32.0), Point.horizontalDistance(p2, p1), 0.001);
        assertEquals(32.0, Point.horizontalDistanceSquared(p2, p1), 0.001);

        // direction
        assertEquals(Math.toRadians(45.0), p2.horizontalDirection(), 0.001);
        assertEquals(Math.toRadians(45.0), p1.horizontalDirection(p2), 0.001);
        assertEquals(0.0, new DirectedPoint2d(0.0, 0.0, Math.PI / 4.0).horizontalDirection(), 0.001);

        // normalize
        DirectedPoint2d pn = p2.normalize();
        assertEquals(1.0 / Math.sqrt(2.0), pn.getX(), 0.001);
        assertEquals(1.0 / Math.sqrt(2.0), pn.getY(), 0.001);
        assertEquals(0.0, pn.getZ(), 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d(0.0, 0.0, Math.PI / 4.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

    }

    /**
     * Test the DirectedPoint2d operators for NPE.
     */
    @Test
    public void testDirectedPoint2dOperatorsNPE()
    {
        final DirectedPoint2d p1 = new DirectedPoint2d(1.0, 1.0, Math.PI / 4.0);

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
