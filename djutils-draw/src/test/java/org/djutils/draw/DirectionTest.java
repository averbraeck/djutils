package org.djutils.draw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * TestDirection.java.
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
public class DirectionTest
{
    /**
     * Test the Direction3d class.
     */
    @Test
    public void testDirection()
    {
        Direction3d d1 = new Direction3d(0, 0); // straight up
        for (double dirZ : new double[] {0, 1, 2, 3, 4, 5, -2})
        {
            Direction3d d2 = new Direction3d(Math.PI / 2, dirZ);
            assertEquals(Math.PI / 2, d1.directionDifference(d2), 0.000001, "perpendicular");
            d2 = new Direction3d(0, dirZ);
            assertEquals(0, d1.directionDifference(d2), 0.000001, "same");
            d2 = new Direction3d(Math.PI / 3, dirZ);
            assertEquals(Math.PI / 3, d1.directionDifference(d2), 0.000001, "60 degrees");
            d2 = new Direction3d(Math.PI, dirZ);
            assertEquals(Math.PI, d1.directionDifference(d2), 0.000001, "180 degrees");
        }
        d1 = new Direction3d(Math.PI / 6, 0); // 60 degrees up
        Direction3d d2 = new Direction3d(Math.PI / 2, 0);
        assertEquals(Math.PI / 3, d1.directionDifference(d2), 0.000001, "60 degrees");
        d2 = new Direction3d(Math.PI / 2, Math.PI);
        assertEquals(2 * Math.PI / 3, d1.directionDifference(d2), 0.000001, "120 degrees");
        d2 = new Direction3d(Math.PI / 3, Math.PI);
        assertEquals(Math.PI / 2, d1.directionDifference(d2), 0.000001, "90 degrees");

        d1 = new Direction3d(2, 3);
        assertEquals(2, d1.getDirY(), 0, "dirY can be retrieve");
        assertEquals(3, d1.getDirZ(), 0, "dirZ can be retrieve");
        assertTrue(d1.toString().startsWith("Direction3d ["), "toString method returns something descriptive");
        // test the exceptions
        try
        {
            new Direction3d(Double.NaN, 0);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(Double.POSITIVE_INFINITY, 0);
            fail("infinity value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(Double.NEGATIVE_INFINITY, 0);
            fail("infinity value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(0, Double.NaN);
            fail("NaN value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(0, Double.POSITIVE_INFINITY);
            fail("infinity value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(0, Double.NEGATIVE_INFINITY);
            fail("infinity value should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        d2 = new Direction3d(2, 3);
        assertEquals(d1, d1, "equal to itself");
        assertEquals(d1, d2, "equal to identical Direction3d");
        assertEquals(d1.hashCode(), d2.hashCode(), "hashCodes are consistent");
        d2 = new Direction3d(2, 4);
        assertNotEquals(d1, d2, "not equal to a different Direction3d");
        assertNotEquals(d1.hashCode(), d2.hashCode(), "hashcodes should be different");
        d2 = new Direction3d(-2, 3);
        assertNotEquals(d1, d2, "not equal to a different Direction3d");
        assertNotEquals(d1.hashCode(), d2.hashCode(), "hashcodes should be different");
        assertNotEquals(d1, "string", "not equal to some totally different object");
        assertNotEquals(d1, null, "not equal to null");
    }
}
