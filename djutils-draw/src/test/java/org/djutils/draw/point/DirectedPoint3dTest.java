package org.djutils.draw.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Try;
import org.junit.jupiter.api.Test;

/**
 * DirectedPoint3dTest.java.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class DirectedPoint3dTest
{
    /**
     * Test the methods that are not covered by the Ray2dTest.
     */
    @SuppressWarnings({"unlikely-arg-type"})
    @Test
    public void testMethods()
    {
        DirectedPoint3d dp = new DirectedPoint3d(1, 2, 3, 4, 5);
        assertEquals(1, dp.getX(), 0.0, "x can be retrieved");
        assertEquals(2, dp.getY(), 0.0, "y can be retrieved");
        assertEquals(3, dp.getZ(), 0.0, "z can be retrieved");
        assertEquals(4, dp.getDirY(), 0.0, "dirY can be retrieved");
        assertEquals(5, dp.getDirZ(), 0.0, "dirZ can be retrieved");
        assertEquals(1, dp.size(), "size is 1");
        Iterator<Point3d> it = dp.iterator();
        assertTrue(it.hasNext(), "iterator has at least one point to provide");
        Point3d p = it.next();
        assertEquals(p.x, dp.x, 0, "x matches");
        assertEquals(p.y, dp.y, 0, "y matches");
        assertEquals(p.z, dp.z, 0, "z matches");
        assertFalse(it.hasNext(), "iterator is now exhausted");
        DirectedPoint3d neg = dp.neg();
        assertEquals(-1, neg.x, 0, "x is negated");
        assertEquals(-2, neg.y, 0, "y is negated");
        assertEquals(-3, neg.z, 0, "z is negated");
        assertEquals(AngleUtil.normalizeAroundZero(4 + Math.PI), neg.dirY, 0.0001, "dirY is altered by pi");
        assertEquals(AngleUtil.normalizeAroundZero(5 + Math.PI), neg.dirZ, 0.0001, "dirZ is altered by pi");
        try
        {
            it.next();
            fail("exhausted iterator should have thrown an exception");
        }
        catch (NoSuchElementException nse)
        {
            // Ignore expected exception
        }
        assertTrue(dp.toString().startsWith("DirectedPoint3d"));
        assertTrue(dp.toString(false).startsWith("DirectedPoint3d"));
        assertTrue(dp.toString(true).startsWith("["));
        assertEquals(dp, dp, "Equals to itself");
        assertFalse(dp.equals("bla"), "Not equal to some random string");
    }

    /**
     * Test the DirectedPoint3d operators.
     */
    @Test
    public void testDirectedPoint3dOperators()
    {
        DirectedPoint3d p = new DirectedPoint3d(-0.1, -0.2, -0.3, Math.PI / 2, -Math.PI / 4);
        assertEquals(0.1, p.abs().x, 1E-6);
        assertEquals(0.2, p.abs().y, 1E-6);
        assertEquals(0.3, p.abs().z, 1E-6);
        assertEquals(Math.PI / 2, p.abs().getDirY(), 1E-6);
        assertEquals(-Math.PI / 4, p.abs().getDirZ(), 1E-6);

        Iterator<Point3d> i = p.iterator();
        assertTrue(i.hasNext(), "iterator has one point");
        assertEquals(p, i.next(), "iterator returns p");
        assertFalse(i.hasNext(), "iterator does not have another point");

        DirectedPoint3d p2 = p.neg();
        assertEquals(-p.x, p2.x, 1E-6, "negated x");
        assertEquals(-p.y, p2.y, 1E-6, "negated y");
        assertEquals(-p.z, p2.z, 1E-6, "negated z");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirY() + Math.PI), p2.getDirY(), 1E-6, "negated dirY");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirZ() + Math.PI), p2.getDirZ(), 1E-6, "negated dirZ");

        p2 = p.scale(1.0);
        assertEquals(p.x, p2.x, 0, "unity scaled x");
        assertEquals(p.y, p2.y, 0, "unity scaled y");
        assertEquals(p.z, p2.z, 0, "unity scaled z");
        assertEquals(p.getDirY(), p2.getDirY(), 0, "unity scaled dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 0, "unity scaled dirZ");

        p2 = p.scale(10.0);
        assertEquals(10 * p.x, p2.x, 1E-6, "10 scaled x");
        assertEquals(10 * p.y, p2.y, 1E-6, "10 scaled y");
        assertEquals(10 * p.z, p2.z, 1E-6, "10 scaled z");
        assertEquals(p.getDirY(), p2.getDirY(), 0, "10 scaled dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 0, "10 scaled dirZ");

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
        assertEquals(p.x + 5.0, p2.x, 1E-6, "translated x");
        assertEquals(p.y - 1.0, p2.y, 1E-6, "translated y");
        assertEquals(p.z + 2.0, p2.z, 1E-6, "translated z");
        assertEquals(p.getDirY(), p2.getDirY(), 1E-6, "translated dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 1E-6, "translated dirZ");

        p2 = p.translate(5.0, -1.0);
        assertEquals(p.x + 5.0, p2.x, 1E-6, "translated x");
        assertEquals(p.y - 1.0, p2.y, 1E-6, "translated y");
        assertEquals(p.z, p2.z, 1E-6, "not translated z");
        assertEquals(p.getDirY(), p2.getDirY(), 1E-6, "translated dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 1E-6, "translated dirZ");

        p2 = p.rotate(-Math.PI / 4, Math.PI);
        assertEquals(p.x, p2.x, 1E-6, "rotated x");
        assertEquals(p.y, p2.y, 1E-6, "rotated y");
        assertEquals(p.z, p2.z, 1E-6, "rotated z");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirY() - Math.PI / 4), p2.getDirY(), 1E-6, "rotated dirY");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirZ() + Math.PI), p2.getDirZ(), 1E-6, "rotated dirZ");

        p2 = p.rotate(17 * Math.PI / 4);
        assertEquals(p.x, p2.x, 1E-6, "rotated x");
        assertEquals(p.y, p2.y, 1E-6, "rotated y");
        assertEquals(p.z, p2.z, 1E-6, "rotated z");
        assertEquals(p.getDirY(), p2.getDirY(), 1E-6, "not rotated dirY");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirZ() + 17 * Math.PI / 4), p2.getDirZ(), 1E-6, "rotated dirZ");

        // interpolate
        DirectedPoint3d p1 = new DirectedPoint3d(1.0, 2.0, 3.0, 0.2, 0.3);
        p2 = new DirectedPoint3d(5.0, 7.0, 9.0, 0.4, 0.5);
        assertEquals(p1, p1.interpolate(p2, 0.0), "p1 interpolated to p2 at 0");
        assertEquals(p2, p1.interpolate(p2, 1.0), "p1 interpolated to p2 at 1");
        assertEquals(p2, p2.interpolate(p1, 0.0), "p2 interpolated to p1 at 0");
        assertEquals(p1, p2.interpolate(p1, 1.0), "p2 interpolated to p1 at 1");
        assertEquals(p1, p1.interpolate(p1, 0.0), "p1 interpolated to itself at 0");
        assertTrue(new DirectedPoint3d(3.0, 4.5, 6.0, 0.3, 0.4).epsilonEquals(p1.interpolate(p2, 0.5), 1E-6, 1E-6),
                "p1 interpolated to p2 at 0.5");
        assertTrue(new DirectedPoint3d(9.0, 12.0, 15.0, 0.6, 0.7).epsilonEquals(p1.interpolate(p2, 2), 1E-6, 1E-6),
                "p1 extrapolated to p2 at 2");
        assertTrue(new DirectedPoint3d(-3.0, -3.0, -3.0, 0.0, 0.1).epsilonEquals(p1.interpolate(p2, -1), 1E-6, 1E-6),
                "p1 extrapolated to p2 at -1");

        // distance
        assertEquals(Math.sqrt(16 + 25 + 36), p1.distance(p2), 0.001, "Distance");
        assertEquals(16 + 25 + 36, p1.distanceSquared(p2), 0.001, "Distance squared");
        assertEquals(Math.sqrt(16 + 25), p1.horizontalDistance(p2), 0.001, "Horizontal distance");
        assertEquals(16 + 25, p1.horizontalDistanceSquared(p2), 0.001, "Horizontal distance squared");

        // direction
        assertEquals(Math.atan2(7, 5), p2.horizontalDirection(), 0.001, "Horizontal direction");
        assertEquals(Math.atan2(p2.y - p1.y, p2.x - p1.x), p1.horizontalDirection(p2), 0.001, "Horizontal direction");

        // normalize
        DirectedPoint3d pn = p2.normalize();
        assertEquals(p2.x / Math.sqrt(25 + 49 + 81), pn.x, 0.001, "normalized x");
        assertEquals(p2.y / Math.sqrt(25 + 49 + 81), pn.y, 0.001, "normalized y");
        assertEquals(p2.z / Math.sqrt(25 + 49 + 81), pn.z, 0.001, "normalized z");
        assertEquals(p2.getDirY(), pn.getDirY(), 0, "normalized dirY");
        assertEquals(p2.getDirZ(), pn.getDirZ(), 0, "normalized dirZ");

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
                p1.rotate(Double.NaN, 0);
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(0, Double.NaN);
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

}
