package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test the Constant class and the Nan class.
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
public class ConstantAndNanTest
{
    /**
     * Test the Constant class.
     */
    @Test
    public void testConstant()
    {
        for (double c : new double[] {-9999, -Math.PI, -1, 1, 1, Math.E, 3e200})
        {
            Constant constant = new Constant(c);
            assertEquals(Double.parseDouble(constant.getDescription()), c, Math.abs(c) / 99999999,
                    "value is produced as description");
            for (double x : new double[] {-12, 0, Math.PI, 2e12})
            {
                assertEquals(c, constant.get(x), 0.0, "value of c is returned for any x");
                assertEquals(Constant.ZERO, constant.getDerivative(), "derivative of constant is ZERO");
            }
            assertEquals("C", constant.getId(), "Id of constant is C");
            assertTrue(constant.toString().startsWith("Constant ["), "toString returns something descriptive");
        }
        for (double x : new double[] {-12, 0, 7777, Math.PI, 2e12, 3e100})
        {
            assertEquals(1.0, Constant.ONE.get(x), 0, "value of ONE is 1.0");
            assertEquals(Constant.ZERO, Constant.ONE.getDerivative(), "derivative of ONE is ZERO");
        }
        assertEquals(Constant.ZERO, Constant.ONE.getDerivative(), "derivative of ONE is ZERO");
        assertEquals(Constant.ZERO, Constant.ZERO.getDerivative(), "derivative of ZERO is ZERO");
        assertTrue(Constant.ZERO.hashCode() != Constant.ONE.hashCode(), "hash code depends on value");
        assertFalse(Constant.ZERO.equals(null), "not equal to null");
        assertFalse(Constant.ZERO.equals("Not a Constant"), "not equal to some other object");
        assertFalse(Constant.ZERO.equals(Constant.ONE), "not equal if value differs");
        Constant otherZero = new Constant(0.0);
        assertTrue(Constant.ZERO.equals(otherZero), "equal to other constant with same value");
    }

    /**
     * Test the Nan class.
     */
    @Test
    public void testNan()
    {
        for (double x : new double[] {-1e20, -5.555, 0, Math.E, 23.45e200})
        {
            assertTrue(Double.isNaN(Nan.NAN.get(x)), "value is NaN for every x");
        }
        assertTrue(Nan.NAN.toString().startsWith("Nan ["), "toString returns something descriptive");
        assertTrue(Nan.NAN.getDescription().equals("Nan"), "description is \"Nan\"");
        assertTrue(Nan.NAN.getId().equals("Nan"), "id is \"Nan\"");
        assertEquals(Nan.NAN, Nan.NAN.getDerivative(), "derivative is itself");
    }
}
