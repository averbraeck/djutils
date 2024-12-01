package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Quotient class.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class QuotientTest
{
    /**
     * Test the Quotient class.
     */
    @Test
    public void quotientTest()
    {
        double[] testValues = new double[] {-10, -Math.E, -1, 0, 1, Math.PI, 5};
        MathFunction numerator = new Constant(12);
        MathFunction denominator = new Constant(34);
        try
        {
            new Quotient(null, denominator);
            fail("null numerator should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Quotient(numerator, null);
            fail("null denominator should have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        Quotient q = new Quotient(numerator, denominator);
        for (double x : testValues)
        {
            assertEquals(12.0 / 34, q.get(x), 0.00001, "quotient is computed");
        }
        MathFunction simplified = q.simplify();
        assertTrue(simplified instanceof Constant, "Should simplify to a constant");
        numerator = new Sine(1, 2, 3);
        denominator = new PowerFunction(2, 3);
        q = new Quotient(numerator, denominator);
        MathFunction scaled = q.scaleBy(123);
        for (double x : testValues)
        {
            assertEquals(numerator.get(x) / denominator.get(x), q.get(x), 0.0001, "get returns quotient");
            assertEquals(123 * numerator.get(x) / denominator.get(x), scaled.get(x), 0.0001, "get returns quotient");
        }
        assertEquals(102, q.sortPriority(), "sort priority is 102");
        try
        {
            q.compareWithinSubType(numerator);
            fail("compareWithinSubType should throw IllegalArgumentException for incompatible type");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertEquals(0, q.compareWithinSubType(q), "should sort together");
        assertTrue(q.equals(q), "should test equal");
        Quotient q2 = new Quotient(numerator, denominator);
        assertTrue(q.equals(q2), "same content should test equal");
        q2 = new Quotient(Constant.ONE, denominator);
        assertFalse(q.equals(q2), "should test not equal");
        assertNotEquals(q.hashCode(), q2.hashCode(), "should be different");
        q2 = new Quotient(numerator, Constant.ONE);
        assertFalse(q.equals(q2), "should test not equal");
        assertNotEquals(q.hashCode(), q2.hashCode(), "should be different");
        assertTrue(q.compareTo(q2) < 0, "should sort before");
        assertTrue(q2.compareTo(q) > 0, "should sort before");
        assertFalse(q.equals(null));
        assertFalse(q.equals("Not a Quotient"));
        assertTrue(q.toString().contains(numerator.toString()), "numerator is in toString");
        assertTrue(q.toString().contains(denominator.toString()), "denominator is in toString");
        assertTrue(q.toString().contains("/"), "there is a forward slash in toString");
        
    }
}
