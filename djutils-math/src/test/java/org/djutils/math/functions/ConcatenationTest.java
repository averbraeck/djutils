package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Concatenation class.
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
public class ConcatenationTest
{
    /**
     * Test the Concatenation class.
     */
    @Test
    public void concatenationTest()
    {
        try
        {
            Concatenation.continuousPiecewiseLinear(0.0);
            fail("odd number of points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            Concatenation.continuousPiecewiseLinear(0.0, 2.2);
            fail("too few points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            Concatenation.continuousPiecewiseLinear(0.0, 2.2, 0.0, 3.2);
            fail("different values for same point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            Concatenation.continuousPiecewiseLinear(0.0, 2.2, 0.0, 2.2);
            fail("too few unique points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        Concatenation c = Concatenation.continuousPiecewiseLinear(0.0, 2.0, 0.2, 2.1, 0.5, 1.5, 1.0, 5.5);
        assertEquals(2.0, c.apply(0.0), 0.000001, "value at provided domain point");
        assertEquals(2.1, c.apply(0.2), 0.000001, "value at provided domain point");
        assertEquals(1.5, c.apply(0.5), 0.000001, "value at provided domain point");
        assertEquals(5.5, c.apply(1.0), 0.000001, "value at provided domain point");
        Concatenation derivative = c.getDerivative();
        assertEquals(0.5, derivative.apply(0.1), 0.000001, "slope in segment");
        assertEquals(-2.0, derivative.apply(0.3), 0.000001, "slope in segment");
        assertEquals(8.0, derivative.apply(0.9), 0.000001, "slope in segment");
        try
        {
            c.apply(-0.01);
            fail("get for point outside domain should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            c.apply(1.01);
            fail("get for point outside domain should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        MathFunction scaled = c.scaleBy(3);
        assertEquals(2.0 * 3, scaled.apply(0.0), 0.000001, "value at provided domain point");
        assertEquals(2.1 * 3, scaled.apply(0.2), 0.000001, "value at provided domain point");
        assertEquals(1.5 * 3, scaled.apply(0.5), 0.000001, "value at provided domain point");
        assertEquals(5.5 * 3, scaled.apply(1.0), 0.000001, "value at provided domain point");
        MathFunction derivativeScaled = scaled.getDerivative();
        assertEquals(0.5 * 3, derivativeScaled.apply(0.1), 0.000001, "slope in segment");
        assertEquals(-2.0 * 3, derivativeScaled.apply(0.3), 0.000001, "slope in segment");
        assertEquals(8.0 * 3, derivativeScaled.apply(0.9), 0.000001, "slope in segment");

        assertTrue(c == c.scaleBy(1.0), "scaling by 1.0 return original");
        assertEquals(110, c.sortPriority(), "sort priority is returned");

        try
        {
            c.compareWithinSubType(Constant.ONE);
            fail("compareWithinSubType with incompatible type should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertEquals(0, c.compareWithinSubType(c), "compareWithinSubType with itself should return 0");

        assertTrue(c.toString().startsWith("IntervalSet("), "toString returns something descriptive");

        Concatenation c2 = Concatenation.continuousPiecewiseLinear(0.0, 2.0, 0.2, 2.1, 0.5, 1.5, 1.0, 5.5);
        assertEquals(c.hashCode(), c2.hashCode(), "same content should yield same hash code");
        assertTrue(c.equals(c2), "same content should test as equal");
        c2 = Concatenation.continuousPiecewiseLinear(0.1, 2.0, 0.2, 2.1, 0.5, 1.5, 1.0, 5.5);
        assertNotEquals(c.hashCode(), c2.hashCode(), "different start should affect hash code");
        assertFalse(c.equals(c2), "different start should cause equals test to fail");
        c2 = Concatenation.continuousPiecewiseLinear(0.0, 3.0, 0.2, 2.1, 0.5, 1.5, 1.0, 5.5);
        assertNotEquals(c.hashCode(), c2.hashCode(), "different start should affect hash code");
        assertFalse(c.equals(c2), "different start should cause equals test to fail");
        assertFalse(c.equals(null), "not equal to null");
        assertFalse(c.equals("not a Concatenation"), "not equal to some unrelated object");

        MathFunction mf1 = new Power(1, 2);
        MathFunction mf2 = new Power(2, 3);
        c = new Concatenation(new Interval<MathFunction>(1, true, 2, true, mf1),
                new Interval<MathFunction>(3, true, 5, true, mf2));
        assertEquals(mf1.apply(1.0), c.apply(1.0), 0.0, "mf1");
        assertEquals(mf1.apply(1.5), c.apply(1.5), 0.0, "mf1");
        assertEquals(mf1.apply(2.0), c.apply(2.0), 0.0, "mf1");
        assertEquals(mf2.apply(3.0), c.apply(3.0), 0.0, "mf3");
        assertEquals(mf2.apply(3.5), c.apply(3.5), 0.0, "mf3");
        assertEquals(mf2.apply(5.0), c.apply(5.0), 0.0, "mf3");
        assertTrue(Double.isNaN(c.apply(2.5)), "NaN between the two sections");
        c = new Concatenation(new Interval<MathFunction>(1, true, 2, false, mf1),
                new Interval<MathFunction>(3, false, 5, true, mf2));
        assertEquals(mf1.apply(1.0), c.apply(1.0), 0.0, "mf1");
        assertEquals(mf1.apply(1.5), c.apply(1.5), 0.0, "mf1");
        assertTrue(Double.isNaN(c.apply(2.0)), "NaN between the two sections");
        assertTrue(Double.isNaN(c.apply(3.0)), "NaN between the two sections");
        assertEquals(mf2.apply(3.5), c.apply(3.5), 0.0, "mf3");
        assertEquals(mf2.apply(5.0), c.apply(5.0), 0.0, "mf3");
        assertTrue(Double.isNaN(c.apply(2.5)), "NaN between the two sections");
        c = new Concatenation(new Interval<MathFunction>(1, true, 2, true, mf1),
                new Interval<MathFunction>(2, false, 5, true, mf2));
        assertEquals(mf1.apply(1.0), c.apply(1.0), 0.0, "mf1");
        assertEquals(mf1.apply(1.5), c.apply(1.5), 0.0, "mf1");
        assertEquals(mf1.apply(2.0), c.apply(2.0), 0.0, "mf1");
        assertEquals(mf2.apply(2.1), c.apply(2.1), 0.0, "mf3");
        assertEquals(mf2.apply(3.5), c.apply(3.5), 0.0, "mf3");
        assertEquals(mf2.apply(5.0), c.apply(5.0), 0.0, "mf3");
        c = new Concatenation(new Interval<MathFunction>(1, true, 2, false, mf1),
                new Interval<MathFunction>(2, true, 5, true, mf2));
        assertEquals(mf1.apply(1.0), c.apply(1.0), 0.0, "mf1");
        assertEquals(mf1.apply(1.5), c.apply(1.5), 0.0, "mf1");
        assertEquals(mf1.apply(1.9), c.apply(1.9), 0.0, "mf1");
        assertEquals(mf2.apply(2.0), c.apply(2.0), 0.0, "mf3");
        assertEquals(mf2.apply(3.5), c.apply(3.5), 0.0, "mf3");
        assertEquals(mf2.apply(5.0), c.apply(5.0), 0.0, "mf3");
        c = new Concatenation(new Interval<MathFunction>(1, true, 2, false, mf1),
                new Interval<MathFunction>(2, false, 5, true, mf2));
        assertEquals(mf1.apply(1.0), c.apply(1.0), 0.0, "mf1");
        assertEquals(mf1.apply(1.5), c.apply(1.5), 0.0, "mf1");
        assertEquals(mf1.apply(1.9), c.apply(1.9), 0.0, "mf1");
        assertEquals(mf2.apply(2.1), c.apply(2.1), 0.0, "mf3");
        assertEquals(mf2.apply(3.5), c.apply(3.5), 0.0, "mf3");
        assertEquals(mf2.apply(5.0), c.apply(5.0), 0.0, "mf3");
        assertTrue(Double.isNaN(c.apply(2.0)), "NaN between the two sections");
        try
        {
            new Concatenation(new Interval<MathFunction>(1, true, 2, true, mf1),
                    new Interval<MathFunction>(2, true, 5, true, mf2));
            fail("overlap at 2 should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            c.apply(0.5);
            fail("outside domain should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            c.apply(5.5);
            fail("outside domain should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Concatenation(new Interval<MathFunction>(1, true, 2, true, mf1),
                    new Interval<MathFunction>(2, true, 5, true, mf2));
            fail("domain value 2.0 falls in two intervals; should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Concatenation();
            fail("no arguments should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        
        assertTrue(c.equals(c), "equal to itself");
    }
}
