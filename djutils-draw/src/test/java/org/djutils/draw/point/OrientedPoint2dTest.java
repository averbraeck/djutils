package org.djutils.draw.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Try;
import org.junit.jupiter.api.Test;

/**
 * OrientedPoint2dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class OrientedPoint2dTest
{
    /**
     * Test the OrientedPoint2d construction methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testOrientedPoint2dConstruction()
    {
        OrientedPoint2d p = new OrientedPoint2d(10.0, -20.0, Math.PI);
        assertEquals(10.0, p.x, 0, "x");
        assertEquals(-20.0, p.y, 0, "y");
        assertEquals(3.1415926, p.getDirZ(), 1E-6, "dirZ");

        p = new OrientedPoint2d(10.0, -20.0);
        assertEquals(10.0, p.x, 0, "x");
        assertEquals(-20.0, p.y, 0, "y");
        assertEquals(0, p.getDirZ(), 0, "dirZ");
        assertEquals(0, p.dirZ, 0, "dirZ");

        Point2d p2d = new Point2d(10, -20);
        p = new OrientedPoint2d(p2d, Math.PI);
        assertEquals(10.0, p.x, 0, "x");
        assertEquals(-20.0, p.y, 0, "y");
        assertEquals(3.1415926, p.getDirZ(), 1E-6, "dirZ");
        assertEquals(3.1415926, p.dirZ, 1E-6, "dirZ");

        try
        {
            new OrientedPoint2d(Double.NaN, 0, 0);
            fail("NaN coordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint2d(0, Double.NaN, 0);
            fail("NaN coordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint2d(0, 0, Double.NaN);
            fail("NaN coordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        double[] p2Arr = new double[] {5.0, 6.0};
        p = new OrientedPoint2d(p2Arr, Math.PI / 2.0);
        assertEquals(5.0, p.x, 1E-6);
        assertEquals(6.0, p.y, 1E-6);
        assertEquals(3.1415926 / 2.0, p.getDirZ(), 1E-6);
        Point2D.Double p2DD = new Point2D.Double(-0.1, -0.2);
        p = new OrientedPoint2d(p2DD, Math.PI / 4.0);
        assertEquals(-0.1, p.x, 1E-6);
        assertEquals(-0.2, p.y, 1E-6);
        assertEquals(p2DD, p.toPoint2D());
        assertEquals(3.1415926 / 4.0, p.getDirZ(), 1E-6);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint2d((Point2D.Double) null, 0.0);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint2d((Point2D.Double) null, Math.PI);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint2d(new double[] {}, Math.PI / 2.0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint2d(new double[] {1.0}, Math.PI / 4.0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint2d(new double[] {1.0, 2.0, 3.0}, Math.PI);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        // equals and hashCode
        assertTrue(p.equals(p));
        assertEquals(p.hashCode(), p.hashCode());
        OrientedPoint3d p3d = p.translate(1.0, 2.0, 3.0);
        assertFalse(p.equals(p3d));
        assertFalse(p.equals(null));
        assertNotEquals(p3d.hashCode(), p.hashCode());
        assertEquals(p.x + 1.0, p3d.x, 0.00001, "translated x");
        assertEquals(p.y + 2.0, p3d.y, 0.00001, "translated y");
        assertEquals(0 + 3.0, p3d.z, 0.00001, "translated z");
        assertEquals(Math.PI / 4.0, p.getDirZ(), 1E-6);
        assertTrue(p.equals(p.translate(0.0, 0.0)));
        assertFalse(p.equals(p.translate(1.0, 0.0)));
        assertFalse(p.equals(p.translate(0.0, 1.0)));
        assertFalse(p.equals(p.rotate(0.1)));

        // toString
        p = new OrientedPoint2d(10.0, 20.0, Math.PI);
        assertEquals("OrientedPoint2d [x=10.000000, y=20.000000, rot=3.141593]", p.toString());
        assertEquals("OrientedPoint2d [x=10.0, y=20.0, rot=3.1]", p.toString("%.1f"));
        assertEquals("[x=10, y=20, rot=3]", p.toString("%.0f", true));

        // epsilonEquals
        OrientedPoint3d p3 = p.translate(0.001, 0.0, 0.0);
        OrientedPoint3d ref = p.translate(0, 0, 0);
        assertTrue(ref.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(ref, 0.09, 0.001));
        assertFalse(ref.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(ref, 0.0009, 0.001));
        p3 = p.translate(0.0, 0.001, 0.0);
        assertTrue(ref.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(ref, 0.09, 0.001));
        assertFalse(ref.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(ref, 0.0009, 0.001));
        OrientedPoint2d p2 = p.translate(0.001, 0.0);
        assertTrue(p.epsilonEquals(p2, 0.09, 0.001), "all");
        assertFalse(p.epsilonEquals(p2, 0.0009, 0.001), "dx");
        p2 = p.translate(0.0, 0.001);
        assertTrue(p.epsilonEquals(p2, 0.09, 0.001), "all");
        assertFalse(p.epsilonEquals(p2, 0.0009, 0.001), "dy");
        p3 = p.translate(0.0, 0.0, 0.001);
        assertTrue(ref.epsilonEquals(p3, 0.09, 0.001));
        assertTrue(p3.epsilonEquals(ref, 0.09, 0.001));
        assertFalse(ref.epsilonEquals(p3, 0.0009, 0.001));
        assertFalse(p3.epsilonEquals(ref, 0.0009, 0.001));
        OrientedPoint2d dp2 = p.rotate(0.001);
        assertTrue(p.epsilonEquals(dp2, 0.09, 0.009));
        assertTrue(dp2.epsilonEquals(p, 0.09, 0.009));
        assertFalse(p.epsilonEquals(dp2, 0.0009, 0.0009));
        assertFalse(dp2.epsilonEquals(p, 0.0009, 0.0009));
    }

    /**
     * Test the OrientedPoint2d operators.
     */
    @Test
    public void testOrientedPoint2dOperators()
    {
        OrientedPoint2d p = new OrientedPoint2d(-0.1, -0.2, -Math.PI / 7);
        OrientedPoint2d out = p.abs();
        assertEquals(0.1, out.x, 1E-6, "x");
        assertEquals(0.2, out.y, 1E-6, "y");
        assertEquals(-Math.PI / 7, out.getDirZ(), 1E-6, "dirZ");

        Iterator<? extends Point2d> i = p.getPoints();
        assertTrue(i.hasNext(), "iterator has one point");
        assertEquals(p, i.next(), "iterator returns p");
        assertFalse(i.hasNext(), "iterator does not have another point");

        out = p.neg();
        assertEquals(0.1, out.x, 1E-6, "neg x");
        assertEquals(0.2, out.y, 1E-6, "neg y");
        assertEquals(Math.PI - Math.PI / 7, out.getDirZ(), 1E-6, "neg dirZ");

        out = p.scale(1.0);
        assertEquals(-0.1, out.x, 1E-6, "x");
        assertEquals(-0.2, out.y, 1E-6, "y");
        assertEquals(-Math.PI / 7, out.getDirZ(), 1E-6, "dirZ");

        out = p.scale(10.0);
        assertEquals(-1.0, out.x, 1E-6, "10 x");
        assertEquals(-2.0, out.y, 1E-6, "10 y");
        assertEquals(-Math.PI / 7, out.getDirZ(), 1E-6, "dirZ");

        out = p.translate(5.0, -1.0);
        assertEquals(4.9, out.x, 1E-6, "x");
        assertEquals(-1.2, out.y, 1E-6, "y");
        assertEquals(-Math.PI / 7, out.getDirZ(), 1E-6, "dirZ");

        out = p.translate(1.0, 3.0);
        assertEquals(0.9, out.x, 1E-6, "x");
        assertEquals(2.8, out.y, 1E-6, "y");
        assertEquals(-Math.PI / 7, out.getDirZ(), 1E-6, "dirZ");

        out = p.rotate(-Math.PI / 4);
        assertEquals(-0.1, out.x, 1E-6, "x");
        assertEquals(-0.2, out.y, 1E-6, "y");
        assertEquals(-Math.PI / 7 - Math.PI / 4, out.getDirZ(), 1E-6, "dirZ");

        // interpolate
        OrientedPoint2d p1 = new OrientedPoint2d(1.0, 1.0, 0.0);
        OrientedPoint2d p2 = new OrientedPoint2d(5.0, 5.0, Math.PI / 2.0);
        assertEquals(p1, p1.interpolate(p2, 0.0), "p1 interpolated to p2 at 0");
        assertEquals(p2, p1.interpolate(p2, 1.0), "p1 interpolated to p2 at 1");
        assertEquals(p2, p2.interpolate(p1, 0.0), "p2 interpolated to p1 at 0");
        assertEquals(p1, p2.interpolate(p1, 1.0), "p2 interpolated to p1 at 1");
        assertEquals(p1, p1.interpolate(p1, 0.0), "p1 interpolated to itself at 0");
        assertEquals(new OrientedPoint2d(3.0, 3.0, Math.PI / 4.0), p1.interpolate(p2, 0.5), "interpolated at halfway");

        // distance
        assertEquals(Math.sqrt(32.0), p1.distance(p2), 0.001);
        assertEquals(32.0, p1.distanceSquared(p2), 0.001);
        // FIXME
        // assertEquals(Math.sqrt(32.0), p1.horizontalDistance(p2), 0.001);
        // assertEquals(32.0, p1.horizontalDistanceSquared(p2), 0.001);

        // direction
        // assertEquals(Math.toRadians(45.0), p2.horizontalDirection(), 0.001);
        // assertEquals(Math.toRadians(45.0), p1.horizontalDirection(p2), 0.001);
        // assertEquals(0.0, new OrientedPoint2d(0.0, 0.0, Math.PI / 4.0).horizontalDirection(), 0.001);

        // normalize
        OrientedPoint2d pn = p2.normalize();
        assertEquals(1.0 / Math.sqrt(2.0), pn.x, 0.001);
        assertEquals(1.0 / Math.sqrt(2.0), pn.y, 0.001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint2d(0.0, 0.0, Math.PI / 4.0).normalize();
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
     * Test the OrientedPoint2d operators for NPE.
     */
    @Test
    public void testOrientedPoint2dOperatorsNPE()
    {
        final OrientedPoint2d p1 = new OrientedPoint2d(1.0, 1.0, Math.PI / 4.0);

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

}
