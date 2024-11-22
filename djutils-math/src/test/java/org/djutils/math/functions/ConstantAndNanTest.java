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
        MathFunction otherZero = new Constant(0.0);
        assertTrue(Constant.ZERO.equals(otherZero), "equal to other constant with same value");
        assertFalse(otherZero == Constant.ZERO, "but it is not the same object");
        otherZero = otherZero.simplify();
        assertTrue(otherZero == Constant.ZERO, "now it IS the same object");
        MathFunction otherOne = new Constant(1.0);
        assertTrue(Constant.ONE.equals(otherOne), "equal to other constant with the same value");
        assertFalse(otherOne == Constant.ONE, "but not the same object");
        otherOne = otherOne.simplify();
        assertTrue(otherOne == Constant.ONE, "now it IS the same object");
        MathFunction constant = new Constant(4);
        constant = constant.scaleBy(2);
        assertEquals(8, constant.get(0), "scaleBy works");
        MathFunction simplified = constant.simplify();
        assertTrue(simplified == constant, "simplify could not make it simpler");
        constant = constant.scaleBy(1.0 / 8);
        assertEquals(Constant.ONE, constant, "now it is equal to ONE");
        assertTrue(constant == Constant.ONE, "in fact, it is the same object");
        constant = constant.scaleBy(0);
        assertEquals(Constant.ZERO, constant, "now it is ZERO");
        assertTrue(Constant.ZERO == constant, "and it is the same object");
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
        assertTrue(Nan.NAN.toString().startsWith("NaN ["), "toString returns something descriptive");
        assertTrue(Nan.NAN.getDescription().equals("NaN"), "description is \"NaN\"");
        assertTrue(Nan.NAN.getId().equals("NaN"), "id is \"NaN\"");
        assertEquals(Nan.NAN, Nan.NAN.getDerivative(), "derivative is itself");
        MathFunction otherNan = Nan.NAN.scaleBy(10);
        assertTrue(otherNan == Nan.NAN, "it is the same object");
    }
}
