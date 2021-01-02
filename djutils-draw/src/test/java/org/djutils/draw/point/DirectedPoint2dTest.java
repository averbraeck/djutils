package org.djutils.draw.point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Point2D;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * DirectedPoint2dTest.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        assertEquals("x", 10.0, p.x, 0);
        assertEquals("y", -20.0, p.y, 0);
        assertEquals("dirZ", 3.1415926, p.getDirZ(), 1E-6);
        
        p = new DirectedPoint2d(10.0, -20.0);
        assertEquals("x", 10.0, p.x, 0);
        assertEquals("y", -20.0, p.y, 0);
        assertEquals("dirZ", 0, p.getDirZ(), 0);
        
        Point2d p2d = new Point2d(10, -20);
        p = new DirectedPoint2d(p2d, Math.PI);
        assertEquals("x", 10.0, p.x, 0);
        assertEquals("y", -20.0, p.y, 0);
        assertEquals("dirZ", 3.1415926, p.getDirZ(), 1E-6);
        
        try
        {
            new DirectedPoint2d(Double.NaN, 0, 0);
            fail("NaN coordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            new DirectedPoint2d(0, Double.NaN, 0);
            fail("NaN coordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            new DirectedPoint2d(0, 0, Double.NaN);
            fail("NaN coordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        double[] p2Arr = new double[] {5.0, 6.0};
        p = new DirectedPoint2d(p2Arr, Math.PI / 2.0);
        assertEquals(5.0, p.x, 1E-6);
        assertEquals(6.0, p.y, 1E-6);
        assertEquals(3.1415926 / 2.0, p.getDirZ(), 1E-6);
        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = new DirectedPoint2d(p2DD, Math.PI / 4.0);
        assertEquals(-0.1, p.x, 1E-6);
        assertEquals(-0.2, p.y, 1E-6);
        assertEquals(p2DD, p.toPoint2D());
        assertEquals(3.1415926 / 4.0, p.getDirZ(), 1E-6);

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
        DirectedPoint3d p3d = p.translate(1.0, 2.0, 3.0);
        assertFalse(p.equals(p3d));
        assertFalse(p.equals(null));
        assertNotEquals(p3d.hashCode(), p.hashCode());
        assertEquals("translated x", p.x + 1.0, p3d.x, 0.00001);
        assertEquals("translated y", p.y + 2.0, p3d.y, 0.00001);
        assertEquals("translated z", 0 + 3.0, p3d.z, 0.00001);
        assertEquals(Math.PI / 4.0, p.getDirZ(), 1E-6);
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
        DirectedPoint3d p3 = p.translate(0.001, 0.0, 0.0);
        DirectedPoint3d ref = p.translate(0, 0, 0);
        assertTrue(ref.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(ref, 0.09, 0.001));
        assertFalse(ref.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(ref, 0.0009, 0.001));
        p3 = p.translate(0.0, 0.001, 0.0);
        assertTrue(ref.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(ref, 0.09, 0.001));
        assertFalse(ref.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(ref, 0.0009, 0.001));
        DirectedPoint2d p2 = p.translate(0.001,  0.0);
        assertTrue("all", p.epsilonEquals(p2, 0.09, 0.001));
        assertFalse("dx", p.epsilonEquals(p2, 0.0009, 0.001));
        p2 = p.translate(0.0, 0.001);
        assertTrue("all", p.epsilonEquals(p2, 0.09, 0.001));
        assertFalse("dy", p.epsilonEquals(p2, 0.0009, 0.001));
        p3 = p.translate(0.0, 0.0, 0.001);
        assertTrue(ref.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(ref, 0.09, 0.001));
        assertFalse(ref.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(ref, 0.0009, 0.001));
        DirectedPoint2d dp2 = p.rotate(0.001);
        assertTrue(p.epsilonEquals(dp2, 0.09, 0.009));
        assertTrue(dp2.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(dp2, 0.0009, 0.0009));
        assertFalse(dp2.epsilonEquals(p, 0.0009, 0.0009));
    }

    /**
     * Test the DirectedPoint2d operators.
     */
    @Test
    public void testDirectedPoint2dOperators()
    {
        DirectedPoint2d p = new DirectedPoint2d(-0.1, -0.2, -Math.PI / 7);
        DirectedPoint2d out = p.abs();
        assertEquals("x", 0.1, out.x, 1E-6);
        assertEquals("y", 0.2, out.y, 1E-6);
        assertEquals("dirZ", -Math.PI / 7, out.getDirZ(), 1E-6);
        
        Iterator<? extends Point2d> i = p.getPoints();
        assertTrue("iterator has one point", i.hasNext());
        assertEquals("iterator returns p", p, i.next());
        assertFalse("iterator does not have another point", i.hasNext());
        
        out = p.neg();
        assertEquals("neg x", 0.1, out.x, 1E-6);
        assertEquals("neg y", 0.2, out.y, 1E-6);
        assertEquals("neg dirZ", Math.PI - Math.PI / 7, out.getDirZ(), 1E-6);
        
        out = p.scale(1.0);
        assertEquals("x", -0.1, out.x, 1E-6);
        assertEquals("y", -0.2, out.y, 1E-6);
        assertEquals("dirZ", -Math.PI / 7, out.getDirZ(), 1E-6);

        out = p.scale(10.0);
        assertEquals("10 x", -1.0, out.x, 1E-6);
        assertEquals("10 y", -2.0, out.y, 1E-6);
        assertEquals("dirZ", -Math.PI / 7, out.getDirZ(), 1E-6);

        out = p.translate(5.0, -1.0);
        assertEquals("x", 4.9, out.x, 1E-6);
        assertEquals("y", -1.2, out.y, 1E-6);
        assertEquals("dirZ", -Math.PI / 7, out.getDirZ(), 1E-6);

        out = p.translate(1.0, 3.0);
        assertEquals("x", 0.9, out.x, 1E-6);
        assertEquals("y", 2.8, out.y, 1E-6);
        assertEquals("dirZ", -Math.PI / 7, out.getDirZ(), 1E-6);

        out = p.rotate(-Math.PI / 4);
        assertEquals("x", -0.1, out.x, 1E-6);
        assertEquals("y", -0.2, out.y, 1E-6);
        assertEquals("dirZ", -Math.PI / 7 - Math.PI / 4, out.getDirZ(), 1E-6);

        // interpolate
        DirectedPoint2d p1 = new DirectedPoint2d(1.0, 1.0, 0.0);
        DirectedPoint2d p2 = new DirectedPoint2d(5.0, 5.0, Math.PI / 2.0);
        assertEquals("p1 interpolated to p2 at 0", p1, p1.interpolate(p2, 0.0));
        assertEquals("p1 interpolated to p2 at 1", p2, p1.interpolate(p2, 1.0));
        assertEquals("p2 interpolated to p1 at 0", p2, p2.interpolate(p1, 0.0));
        assertEquals("p2 interpolated to p1 at 1", p1, p2.interpolate(p1, 1.0));
        assertEquals("p1 interpolated to itself at 0", p1, p1.interpolate(p1, 0.0));
        assertEquals("interpolated at halfway", new DirectedPoint2d(3.0, 3.0, Math.PI / 4.0), p1.interpolate(p2, 0.5));

        // distance
        assertEquals(Math.sqrt(32.0), p1.distance(p2), 0.001);
        assertEquals(32.0, p1.distanceSquared(p2), 0.001);
        // FIXME
//        assertEquals(Math.sqrt(32.0), p1.horizontalDistance(p2), 0.001);
//        assertEquals(32.0, p1.horizontalDistanceSquared(p2), 0.001);

        // direction
//        assertEquals(Math.toRadians(45.0), p2.horizontalDirection(), 0.001);
//        assertEquals(Math.toRadians(45.0), p1.horizontalDirection(p2), 0.001);
//        assertEquals(0.0, new DirectedPoint2d(0.0, 0.0, Math.PI / 4.0).horizontalDirection(), 0.001);

        // normalize
        DirectedPoint2d pn = p2.normalize();
        assertEquals(1.0 / Math.sqrt(2.0), pn.x, 0.001);
        assertEquals(1.0 / Math.sqrt(2.0), pn.y, 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DirectedPoint2d(0.0, 0.0, Math.PI / 4.0).normalize();
            }
        }, "Should throw DRtE", DrawRuntimeException.class);

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
