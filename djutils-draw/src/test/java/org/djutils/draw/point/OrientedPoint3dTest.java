package org.djutils.draw.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Try;
import org.djutils.math.AngleUtil;
import org.junit.jupiter.api.Test;

/**
 * OrientedPoint3dTest.java.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class OrientedPoint3dTest
{
    /**
     * Test the OrientedPoint3d construction methods.
     */
    @Test
    @SuppressWarnings("checkstyle:methodlength")
    public void testOrientedPoint3dConstruction()
    {
        OrientedPoint3d p = new OrientedPoint3d(10.0, -20.0, 5.2);
        assertEquals(10.0, p.x, 1E-6, "x");
        assertEquals(-20.0, p.y, 1E-6, "y");
        assertEquals(5.2, p.z, 1E-6, "z");
        assertEquals(0.0, p.getDirX(), 1E-6, "dirX");
        assertEquals(0.0, p.getDirY(), 1E-6, "dirY");
        assertEquals(0.0, p.getDirZ(), 1E-6, "dirZ");
        assertEquals(0.0, p.dirZ, 1E-6, "dirX");
        assertEquals(0.0, p.dirY, 1E-6, "dirY");
        assertEquals(0.0, p.dirZ, 1E-6, "dirZ");

        p = new OrientedPoint3d(new double[] {-18.7, 3.4, 5.6});
        assertEquals(-18.7, p.x, 1E-6, "x");
        assertEquals(3.4, p.y, 1E-6, "y");
        assertEquals(5.6, p.z, 1E-6, "z");
        assertEquals(0.0, p.getDirX(), 1E-6, "dirX");
        assertEquals(0.0, p.getDirY(), 1E-6, "dirY");
        assertEquals(0.0, p.getDirZ(), 1E-6, "dirZ");

        p = new OrientedPoint3d(10.0, -20.0, 5.2, 0.1, -0.2, Math.PI);
        assertEquals(10.0, p.x, 1E-6, "x");
        assertEquals(-20.0, p.y, 1E-6, "y");
        assertEquals(5.2, p.z, 1E-6, "z");
        assertEquals(0.1, p.getDirX(), 1E-6, "dirX");
        assertEquals(-0.2, p.getDirY(), 1E-6, "dirY");
        assertEquals(3.1415926, p.getDirZ(), 1E-6, "dirZ");

        p = new OrientedPoint3d(new double[] {-18.7, 3.4, 5.6}, 0.1, -0.2, Math.PI);
        assertEquals(-18.7, p.x, 1E-6, "x");
        assertEquals(3.4, p.y, 1E-6, "y");
        assertEquals(5.6, p.z, 1E-6, "z");
        assertEquals(0.1, p.getDirX(), 1E-6, "dirX");
        assertEquals(-0.2, p.getDirY(), 1E-6, "dirY");
        assertEquals(3.1415926, p.getDirZ(), 1E-6, "dirZ");
        assertEquals(0.1, p.dirX, 1E-6, "dirX");
        assertEquals(-0.2, p.dirY, 1E-6, "dirY");
        assertEquals(3.1415926, p.dirZ, 1E-6, "dirZ");

        p = new OrientedPoint3d(new Point3d(new double[] {-18.7, 3.4, 5.6}), 0.1, -0.2, Math.PI);
        assertEquals(-18.7, p.x, 1E-6, "x");
        assertEquals(3.4, p.y, 1E-6, "y");
        assertEquals(5.6, p.z, 1E-6, "z");
        assertEquals(0.1, p.getDirX(), 1E-6, "dirX");
        assertEquals(-0.2, p.getDirY(), 1E-6, "dirY");
        assertEquals(3.1415926, p.getDirZ(), 1E-6, "dirZ");

        try
        {
            new OrientedPoint3d(0, 0, 0, Double.NaN, 0, 0);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(0, 0, 0, 0, Double.NaN, 0);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(0, 0, 0, 0, 0, Double.NaN);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(new double[3], Double.NaN, 0, 0);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(new double[3], 0, Double.NaN, 0);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(new double[3], 0, 0, Double.NaN);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(new Point3d(1, 2, 3), Double.NaN, 0, 0);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(new Point3d(1, 2, 3), 0, Double.NaN, 0);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new OrientedPoint3d(new Point3d(1, 2, 3), 0, 0, Double.NaN);
            fail("NaN rotation should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        double[] p3Arr = new double[] {5.0, 6.0, 7.0};
        double[] rotArr = new double[] {0.1, -0.2, 0.3};
        p = new OrientedPoint3d(5.0, 6.0, 7.0, rotArr);
        assertEquals(5.0, p.x, 0, "x");
        assertEquals(6.0, p.y, 0, "y");
        assertEquals(7.0, p.z, 0, "z");
        assertEquals(0.1, p.getDirX(), 1E-6, "dirX");
        assertEquals(-0.2, p.getDirY(), 1E-6, "dirY");
        assertEquals(0.3, p.getDirZ(), 1E-6, "dirZ");

        p = new OrientedPoint3d(p3Arr, rotArr);
        assertEquals(5.0, p.x, 0, "x");
        assertEquals(6.0, p.y, 0, "y");
        assertEquals(7.0, p.z, 0, "z");
        assertEquals(0.1, p.getDirX(), 1E-6, "dirX");
        assertEquals(-0.2, p.getDirY(), 1E-6, "dirY");
        assertEquals(0.3, p.getDirZ(), 1E-6, "dirZ");

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(0.1, 0.2, 0.3, new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(0.1, 0.2, 0.3, new double[] {0.1, 0.2});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(0.1, 0.2, 0.3, new double[] {0.1, 0.2, 0.3, 0.4});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(new double[] {});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(new double[] {0.1, 0.2});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(new double[] {0.1, 0.2, 0.3, 0.4});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(new double[] {1, 2, 3}, new double[] {0.1, 0.2});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(new double[] {1, 2, 3}, new double[] {0.1, 0.2, 0.3, 0.4});
            }
        }, "Should throw IAE", IllegalArgumentException.class);

    }

    /**
     * Test the OrientedPoint3d construction methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testOrientedPointEquals()
    {
        // equals and hashCode
        OrientedPoint3d p = new OrientedPoint3d(10.0, 20.0, 30.0, 0.1, 0.2, 0.3);
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
        p = new OrientedPoint3d(10.0, 20.0, 30.0, 0.1, 0.2, 0.3);
        assertEquals("OrientedPoint3d [x=10.000000, y=20.000000, z=30.000000, rotX=0.100000, rotY=0.200000, rotZ=0.300000]",
                p.toString());
        assertEquals("OrientedPoint3d [x=10.0, y=20.0, z=30.0, rotX=0.1, rotY=0.2, rotZ=0.3]", p.toString("%.1f"));
        assertEquals("[x=10, y=20, z=30, rotX=0, rotY=0, rotZ=0]", p.toString("%.0f", true));

        // epsilonEquals
        assertTrue(p.epsilonEquals(p, 0.1, 999));
        assertTrue(p.epsilonEquals(p, 0.001, 999));
        assertTrue(p.epsilonEquals(p, 0.0, 999));
        OrientedPoint3d p3 = p.translate(0.001, 0.0, 0.0);
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
     * Test the OrientedPoint3d operators.
     */
    @Test
    public void testOrientedPoint3dOperators()
    {
        OrientedPoint3d p = new OrientedPoint3d(-0.1, -0.2, -0.3, Math.PI / 4, -Math.PI / 4, Math.PI / 2);
        assertEquals(0.1, p.abs().x, 1E-6);
        assertEquals(0.2, p.abs().y, 1E-6);
        assertEquals(0.3, p.abs().z, 1E-6);
        assertEquals(Math.PI / 4, p.abs().getDirX(), 1E-6);
        assertEquals(-Math.PI / 4, p.abs().getDirY(), 1E-6);
        assertEquals(Math.PI / 2, p.abs().getDirZ(), 1E-6);

        Iterator<Point3d> i = p.iterator();
        assertTrue(i.hasNext(), "iterator has one point");
        assertEquals(p, i.next(), "iterator returns p");
        assertFalse(i.hasNext(), "iterator does not have another point");

        OrientedPoint3d p2 = p.neg();
        assertEquals(-p.x, p2.x, 1E-6, "negated x");
        assertEquals(-p.y, p2.y, 1E-6, "negated y");
        assertEquals(-p.z, p2.z, 1E-6, "negated z");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirX() + Math.PI), p2.getDirX(), 1E-6, "negated dirX");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirY() + Math.PI), p2.getDirY(), 1E-6, "negated dirY");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirZ() + Math.PI), p2.getDirZ(), 1E-6, "negated dirZ");

        p2 = p.scale(1.0);
        assertEquals(p.x, p2.x, 0, "unity scaled x");
        assertEquals(p.y, p2.y, 0, "unity scaled y");
        assertEquals(p.z, p2.z, 0, "unity scaled z");
        assertEquals(p.getDirX(), p2.getDirX(), 0, "unity scaled dirX");
        assertEquals(p.getDirY(), p2.getDirY(), 0, "unity scaled dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 0, "unity scaled dirZ");

        p2 = p.scale(10.0);
        assertEquals(10 * p.x, p2.x, 1E-6, "10 scaled x");
        assertEquals(10 * p.y, p2.y, 1E-6, "10 scaled y");
        assertEquals(10 * p.z, p2.z, 1E-6, "10 scaled z");
        assertEquals(p.getDirX(), p2.getDirX(), 0, "10 scaled dirX");
        assertEquals(p.getDirY(), p2.getDirY(), 0, "10 scaled dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 0, "10 scaled dirZ");

        try
        {
            p.translate(Double.NaN, 2);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1, Double.NaN);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(Double.NaN, 2, 3);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1, Double.NaN, 3);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            p.translate(1, 2, Double.NaN);
            fail("NaN value should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        p2 = p.translate(5.0, -1.0, 2.0);
        assertEquals(p.x + 5.0, p2.x, 1E-6, "translated x");
        assertEquals(p.y - 1.0, p2.y, 1E-6, "translated y");
        assertEquals(p.z + 2.0, p2.z, 1E-6, "translated z");
        assertEquals(p.getDirX(), p2.getDirX(), 1E-6, "translated dirX");
        assertEquals(p.getDirY(), p2.getDirY(), 1E-6, "translated dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 1E-6, "translated dirZ");

        p2 = p.translate(5.0, -1.0);
        assertEquals(p.x + 5.0, p2.x, 1E-6, "translated x");
        assertEquals(p.y - 1.0, p2.y, 1E-6, "translated y");
        assertEquals(p.z, p2.z, 1E-6, "not translated z");
        assertEquals(p.getDirX(), p2.getDirX(), 1E-6, "translated dirX");
        assertEquals(p.getDirY(), p2.getDirY(), 1E-6, "translated dirY");
        assertEquals(p.getDirZ(), p2.getDirZ(), 1E-6, "translated dirZ");

        p2 = p.rotate(15 * Math.PI / 4, -Math.PI / 4, Math.PI);
        assertEquals(p.x, p2.x, 1E-6, "rotated x");
        assertEquals(p.y, p2.y, 1E-6, "rotated y");
        assertEquals(p.z, p2.z, 1E-6, "rotated z");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirX() + 15 * Math.PI / 4), p2.getDirX(), 1E-6, "rotated dirX");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirY() - Math.PI / 4), p2.getDirY(), 1E-6, "rotated dirY");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirZ() + Math.PI), p2.getDirZ(), 1E-6, "rotated dirZ");

        p2 = p.rotate(17 * Math.PI / 4);
        assertEquals(p.x, p2.x, 1E-6, "rotated x");
        assertEquals(p.y, p2.y, 1E-6, "rotated y");
        assertEquals(p.z, p2.z, 1E-6, "rotated z");
        assertEquals(p.getDirX(), p2.getDirX(), 1E-6, "not rotated dirX");
        assertEquals(p.getDirY(), p2.getDirY(), 1E-6, "not rotated dirY");
        assertEquals(AngleUtil.normalizeAroundZero(p.getDirZ() + 17 * Math.PI / 4), p2.getDirZ(), 1E-6, "rotated dirZ");

        // interpolate
        OrientedPoint3d p1 = new OrientedPoint3d(1.0, 2.0, 3.0, 0.2, 0.3, 0.4);
        p2 = new OrientedPoint3d(5.0, 7.0, 9.0, 0.4, 0.5, 0.6);
        assertEquals(p1, p1.interpolate(p2, 0.0), "p1 interpolated to p2 at 0");
        assertEquals(p2, p1.interpolate(p2, 1.0), "p1 interpolated to p2 at 1");
        assertEquals(p2, p2.interpolate(p1, 0.0), "p2 interpolated to p1 at 0");
        assertEquals(p1, p2.interpolate(p1, 1.0), "p2 interpolated to p1 at 1");
        assertEquals(p1, p1.interpolate(p1, 0.0), "p1 interpolated to itself at 0");
        assertTrue(new OrientedPoint3d(3.0, 4.5, 6.0, 0.3, 0.4, 0.5).epsilonEquals(p1.interpolate(p2, 0.5), 1E-6, 1E-6),
                "p1 interpolated to p2 at 0.5");
        assertTrue(new OrientedPoint3d(9.0, 12.0, 15.0, 0.6, 0.7, 0.8).epsilonEquals(p1.interpolate(p2, 2), 1E-6, 1E-6),
                "p1 extrapolated to p2 at 2");
        assertTrue(new OrientedPoint3d(-3.0, -3.0, -3.0, 0.0, 0.1, 0.2).epsilonEquals(p1.interpolate(p2, -1), 1E-6, 1E-6),
                "p1 extrapolated to p2 at -1");

        // distance
        assertEquals(Math.sqrt(16 + 25 + 36), p1.distance(p2), 0.001, "Distance");
        assertEquals(16 + 25 + 36, p1.distanceSquared(p2), 0.001, "Distance squared");
        assertEquals(Math.sqrt(16 + 25), p1.horizontalDistance(p2), 0.001, "Horizontal distance");
        assertEquals(16 + 25, p1.horizontalDistanceSquared(p2), 0.001, "Horizontal distance squared");

        // direction
        assertEquals(Math.atan2(7, 5), p2.horizontalDirection(), 0.001, "Horizontal direction");
        assertEquals(Math.atan2(p2.y - p1.y, p2.x - p1.x), p1.horizontalDirection(p2), 0.001, "Horizontal direction");
        assertEquals(0.0, new OrientedPoint3d(0.0, 0.0, 0.0).horizontalDirection(), 0.001);

        // normalize
        OrientedPoint3d pn = p2.normalize();
        assertEquals(p2.x / Math.sqrt(25 + 49 + 81), pn.x, 0.001, "normalized x");
        assertEquals(p2.y / Math.sqrt(25 + 49 + 81), pn.y, 0.001, "normalized y");
        assertEquals(p2.z / Math.sqrt(25 + 49 + 81), pn.z, 0.001, "normalized z");
        assertEquals(p2.getDirX(), pn.getDirX(), 0, "normalized dirX");
        assertEquals(p2.getDirY(), pn.getDirY(), 0, "normalized dirY");
        assertEquals(p2.getDirZ(), pn.getDirZ(), 0, "normalized dirZ");

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new OrientedPoint3d(0.0, 0.0, 0.0, Math.PI / 4.0, Math.PI / 4.0, Math.PI / 4.0).normalize();
            }
        }, "Should throw exception", DrawRuntimeException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(Double.NaN, 0, 0);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(0, Double.NaN, 0);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.rotate(0, 0, Double.NaN);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(Double.NaN, 2);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(1, Double.NaN);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(Double.NaN, 2, 3);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(1, Double.NaN, 3);
            }
        }, "Should throw exception", ArithmeticException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.translate(1, 2, Double.NaN);
            }
        }, "Should throw exception", ArithmeticException.class);

    }

    /**
     * Test the OrientedPoint3d operators for NPE.
     */
    @Test
    public void testOrientedPoint3dOperatorsNPE()
    {
        final OrientedPoint3d p1 = new OrientedPoint3d(1.0, 1.0, Math.PI / 4.0);

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
                p1.horizontalDistance((OrientedPoint3d) null);
            }
        }, "Should throw NPE", NullPointerException.class);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                p1.horizontalDistanceSquared((OrientedPoint3d) null);
            }
        }, "Should throw NPE", NullPointerException.class);

    }

}
