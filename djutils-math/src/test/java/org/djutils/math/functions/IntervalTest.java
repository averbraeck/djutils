package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Interval class.
 * <p>
 * Copyright (c) 2024-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class IntervalTest
{

    /**
     * Test the Interval class.
     */
    @Test
    public void intervalTest()
    {
        try
        {
            new Interval<String>(1, true, 0, true, null);
            fail("Illegal relation of boundaries should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Interval<String>(1, false, 1, false, null);
            fail("Illegal relation of boundaries should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        new Interval<String>(1, true, 1, false, null); // should succeed
        new Interval<String>(1, false, 1, true, null); // should succeed
        new Interval<String>(1, true, 1 + Math.ulp(1), true, null); // should succeed

        Interval<String> interval1 = new Interval<String>(1, true, 2, true, null);
        Interval<String> interval2 = new Interval<String>(2, true, 3, true, null);
        assertFalse(interval1.disjunct(interval2), "boundary is shared by both");
        assertFalse(interval2.disjunct(interval1), "boundary is shared by both");
        interval2 = new Interval<String>(2, false, 3, true, null);
        assertTrue(interval1.disjunct(interval2), "boundary is not shared");
        assertTrue(interval2.disjunct(interval1), "boundary is not shared");
        interval1 = new Interval<String>(1, true, 2, false, null);
        assertTrue(interval1.disjunct(interval2), "boundary is not shared");
        assertTrue(interval2.disjunct(interval1), "boundary is not shared");
        interval2 = new Interval<String>(2, true, 3, true, null);
        assertTrue(interval1.disjunct(interval2), "boundary is not shared (in fact, there is a infinitesimal gap)");
        assertTrue(interval2.disjunct(interval1), "boundary is not shared (in fact, there is a infinitesimal gap)");
        interval1 = new Interval<String>(1, true, 2, true, null);
        interval2 = new Interval<String>(2, true, 2, true, null);
        assertTrue(interval1.covers(interval2), "interval2 is completely inside interval1");
        assertFalse(interval2.covers(interval1), "interval2 is completely inside interval1");
        interval2 = new Interval<String>(3, true, 5, true, null);
        assertTrue(interval1.disjunct(interval2), "totally disjunct");
        assertTrue(interval2.disjunct(interval1), "totally disjunct");
        assertFalse(interval1.covers(interval2), "totally disjunct");
        assertFalse(interval2.covers(interval1), "totally disjunct");

        assertTrue(interval1.equals(interval1), "equal to itself");
        assertTrue(interval1.compareTo(interval1) == 0);
        assertFalse(interval1.equals(null), "not equal to null");
        assertFalse(interval1.equals("not an Interval"), "not equal to unrelated object");
        interval2 = new Interval<String>(0, true, 2, true, null);
        assertFalse(interval1.equals(interval2), "left boundary differs");
        assertTrue(interval1.compareTo(interval2) > 0);
        assertTrue(interval2.compareTo(interval1) < 0);
        interval2 = new Interval<String>(1, true, 3, true, null);
        assertFalse(interval1.equals(interval2), "right boundary differs");
        assertTrue(interval1.compareTo(interval2) < 0);
        assertTrue(interval2.compareTo(interval1) > 0);
        interval2 = new Interval<String>(1, true, 2, true, "Hello");
        assertFalse(interval1.equals(interval2), "payload differs");
        assertTrue(interval1.compareTo(interval2) > 0);
        assertTrue(interval2.compareTo(interval1) < 0);
        interval1 = new Interval<String>(1, true, 2, true, "XYZ");
        assertTrue(interval1.compareTo(interval2) > 0);
        assertTrue(interval2.compareTo(interval1) < 0);

        for (boolean i1Left : new boolean[] {true, false})
        {
            for (boolean i1Right : new boolean[] {true, false})
            {
                interval1 = new Interval<String>(1, i1Left, 2, i1Right, null);
                if (i1Left)
                {
                    assertTrue(interval1.toString().contains("[1"));
                }
                else
                {
                    assertTrue(interval1.toString().contains("(1"));
                }
                if (i1Right)
                {
                    assertTrue(interval1.toString().contains("]"));
                }
                else
                {
                    assertTrue(interval1.toString().contains(")"));
                }
                for (boolean i2Left : new boolean[] {true, false})
                {
                    for (boolean i2Right : new boolean[] {true, false})
                    {
                        interval2 = new Interval<String>(1, i2Left, 2, i2Right, null);
                        boolean covers = (i1Left || (!i2Left)) && (i1Right || (!i2Right));
                        boolean equals = i1Left == i2Left && i1Right == i2Right;
                        // System.out.println("interval1 " + interval1 + ", interval2 " + interval2 + ", covers " + covers);
                        if (covers)
                        {
                            assertTrue(interval1.covers(interval2));
                        }
                        else
                        {
                            assertFalse(interval1.covers(interval2));
                        }
                        if (equals)
                        {
                            assertEquals(interval1, interval2);
                            assertEquals(0, interval1.compareTo(interval2));
                        }
                        else
                        {
                            assertNotEquals(interval1, interval2);
                            if (i1Left && (!i2Left) || i1Left == i2Left && (!i1Right) && i2Right)
                            {
                                assertTrue(interval1.compareTo(interval2) < 0);
                                assertTrue(interval2.compareTo(interval1) > 0);
                            }
                        }
                    }
                }
            }
        }

        // Interval should sort by left boundary
        interval1 = new Interval<String>(2, true, 4, false, null);
        interval2 = new Interval<String>(2, true, 4, false, null);
        assertEquals(0, interval1.compareTo(interval1), "should compare equal");
        assertEquals(0, interval1.compareTo(interval2), "should compare equal");
        assertEquals(0, interval2.compareTo(interval1), "should compare equal");
        interval2 = new Interval<String>(2, false, 4, false, null);
        assertTrue(interval1.compareTo(interval2) < 0,
                "interval that starts at same point, but includes boundary should sort first");
        assertTrue(interval2.compareTo(interval1) > 0,
                "interval that starts at same point, but not includes boundary should sort last");
        interval1 = new Interval<String>(2, false, 4, false, null);
        interval2 = new Interval<String>(2, false, 4, false, null);
        // Check the covers method that takes a double argument
        interval1 = new Interval<String>(2, false, 4, false, null);
        assertEquals(0, interval1.compareTo(interval1), "should compare equal");
        assertEquals(0, interval1.compareTo(interval2), "should compare equal");
        assertTrue(interval1.covers(3.5));
        assertFalse(interval1.covers(2.0));
        assertFalse(interval1.covers(4.0));
        interval1 = new Interval<String>(2, true, 4, false, null);
        assertTrue(interval1.covers(2.0));
        assertFalse(interval1.covers(4.0));
        interval1 = new Interval<String>(2, false, 4, true, null);
        assertTrue(interval1.covers(4.0));
        assertFalse(interval1.covers(2.0));
        interval1 = new Interval<String>(2, true, 4, true, null);
        assertTrue(interval1.covers(4.0));
        assertTrue(interval1.covers(2.0));
        // Hash code; check that each field affects the result
        interval1 = new Interval<String>(2, true, 4, true, null);
        interval2 = new Interval<String>(2, true, 4, true, null);
        assertEquals(interval1.hashCode(), interval2.hashCode(), "same content, same hash code");
        interval2 = new Interval<String>(1, true, 4, true, null);
        assertNotEquals(interval1.hashCode(), interval2.hashCode(), "different content, different hash code");
        interval2 = new Interval<String>(2, false, 4, true, null);
        assertNotEquals(interval1.hashCode(), interval2.hashCode(), "different content, different hash code");
        interval2 = new Interval<String>(2, true, 3, true, null);
        assertNotEquals(interval1.hashCode(), interval2.hashCode(), "different content, different hash code");
        interval2 = new Interval<String>(2, true, 4, false, null);
        assertNotEquals(interval1.hashCode(), interval2.hashCode(), "different content, different hash code");
        interval2 = new Interval<String>(2, true, 4, true, "Hello");
        assertNotEquals(interval1.hashCode(), interval2.hashCode(), "different content, different hash code");
        interval1 = new Interval<String>(2, true, 4, true, "Bonjour");
        assertNotEquals(interval1.hashCode(), interval2.hashCode(), "different content, different hash code");
    }

    /**
     * Test the intersection method.
     */
    @Test
    public void intersectionTest()
    {
        Interval<String> a = new Interval<>(10, true, 20, true, "String");
        Interval<Double> b = new Interval<>(12, true, 18, true, 123.456);
        verifyIntersection(a.intersection(b), 12, true, 18, true, "String");
        verifyIntersection(b.intersection(a), 12, true, 18, true, 123.456);
        b = new Interval<>(8, false, 12, false, 234.56);
        verifyIntersection(a.intersection(b), 10, true, 12, false, "String");
        verifyIntersection(b.intersection(a), 10, true, 12, false, 234.56);
        b = new Interval<>(2, true, 8, false, 12.34);
        assertNull(a.intersection(b));
        assertNull(b.intersection(a));
        b = new Interval<>(10, true, 15, false, 1.2);
        verifyIntersection(a.intersection(b), 10, true, 15, false, "String");
        verifyIntersection(b.intersection(a), 10, true, 15, false, 1.2);
        b = new Interval<>(10, false, 15, false, 1.2);
        verifyIntersection(a.intersection(b), 10, false, 15, false, "String");
        verifyIntersection(b.intersection(a), 10, false, 15, false, 1.2);
        b = new Interval<>(12, false, 20, false, 1.2);
        verifyIntersection(a.intersection(b), 12, false, 20, false, "String");
        verifyIntersection(b.intersection(a), 12, false, 20, false, 1.2);
        b = new Interval<>(12, false, 20, true, 1.25);
        verifyIntersection(a.intersection(b), 12, false, 20, true, "String");
        verifyIntersection(b.intersection(a), 12, false, 20, true, 1.25);
        b = new Interval<>(12, true, 20, true, 1.25);
        verifyIntersection(a.intersection(b), 12, true, 20, true, "String");
        verifyIntersection(b.intersection(a), 12, true, 20, true, 1.25);
        a = new Interval<>(10, false, 20, false, "String");
        b = new Interval<>(10, true, 15, false, 1.2);
        verifyIntersection(a.intersection(b), 10, false, 15, false, "String");
        verifyIntersection(b.intersection(a), 10, false, 15, false, 1.2);
        b = new Interval<>(10, false, 15, false, 1.2);
        verifyIntersection(a.intersection(b), 10, false, 15, false, "String");
        verifyIntersection(b.intersection(a), 10, false, 15, false, 1.2);
        b = new Interval<>(12, false, 20, false, 1.2);
        verifyIntersection(a.intersection(b), 12, false, 20, false, "String");
        verifyIntersection(b.intersection(a), 12, false, 20, false, 1.2);
        b = new Interval<>(12, false, 20, true, 1.25);
        verifyIntersection(a.intersection(b), 12, false, 20, false, "String");
        verifyIntersection(b.intersection(a), 12, false, 20, false, 1.25);
        b = new Interval<>(12, true, 20, true, 1.25);
        verifyIntersection(a.intersection(b), 12, true, 20, false, "String");
        verifyIntersection(b.intersection(a), 12, true, 20, false, 1.25);
    }

    /**
     * Verify all aspects of an Interval.
     * @param intersection the Interval to verify
     * @param low expected low boundary
     * @param includesLow is low boundary included
     * @param high expected high boundary
     * @param includesHigh is high boundary included
     * @param payload the expected paylowd
     */
    public void verifyIntersection(final Interval<?> intersection, final double low, final boolean includesLow,
            final double high, final boolean includesHigh, final Object payload)
    {
        assertEquals(low, intersection.low(), 0.0, "intersection start");
        assertEquals(high, intersection.high(), 0.0, "intersection end");
        assertTrue(includesLow == intersection.lowInclusive(), "low inclusive");
        assertTrue(includesHigh == intersection.highInclusive(), "high includes");
        assertEquals(payload, intersection.payload());
    }
}
