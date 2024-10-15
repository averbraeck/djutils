package org.djutils.draw.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.djutils.base.AngleUtil;
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
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint3dTest
{
    /**
     * Test the methods that are not covered by the Ray2dTest.
     */
    @SuppressWarnings({ "unlikely-arg-type" })
    @Test
    public void testMethods()
    {
        DirectedPoint3d dp = new DirectedPoint3d(1, 2, 3, 4, 5);
        assertEquals(1, dp.getX(), 0.0, "x can be retrieved");
        assertEquals(2, dp.getY(), 0.0, "y can be retrieved");
        assertEquals(3, dp.getZ(), 0.0, "z can be retrieved");
        assertEquals(4, dp.getPhi(), 0.0, "phi can be retrieved");
        assertEquals(5, dp.getTheta(), 0.0, "theta can be retrieved");
        assertEquals(1, dp.size(), "size is 1");
        Iterator<? extends DirectedPoint3d> it = dp.getPoints();
        assertTrue(it.hasNext(), "iterator has at least one point to provide");
        DirectedPoint3d p = it.next();
        assertEquals(p.x, dp.x, 0, "x matches");
        assertEquals(p.y, dp.y, 0, "y matches");
        assertEquals(p.z, dp.z, 0, "z matches");
        assertEquals(p.phi, dp.phi, 0, "phi matches");
        assertEquals(p.theta, dp.theta, 0, "theta matches");
        assertFalse(it.hasNext(), "iterator is now exhausted");
        DirectedPoint3d neg = dp.neg();
        assertEquals(-1, neg.x, 0, "x is negated");
        assertEquals(-2, neg.y, 0, "y is negated");
        assertEquals(-3, neg.z, 0, "z is negated");
        assertEquals(AngleUtil.normalizeAroundZero(4 + Math.PI), neg.phi, 0.0001, "Phi is altered by pi");
        assertEquals(AngleUtil.normalizeAroundZero(5 + Math.PI), neg.theta, 0.0001, "Theta is altered by pi");
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

}
