package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Constant class and the Nan class.
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
            assertEquals(Double.parseDouble(constant.toString()), c, Math.abs(c) / 99999999, "value is produced as toString");
            for (double x : new double[] {-12, 0, Math.PI, 2e12})
            {
                assertEquals(c, constant.apply(x), 0.0, "value of c is returned for any x");
                assertEquals(Constant.ZERO, constant.getDerivative(), "derivative of constant is ZERO");
            }
        }
        for (double x : new double[] {-12, 0, 7777, Math.PI, 2e12, 3e100})
        {
            assertEquals(1.0, Constant.ONE.apply(x), 0, "value of ONE is 1.0");
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
        assertEquals(8, constant.apply(0d), "scaleBy works");
        MathFunction simplified = constant.simplify();
        assertTrue(simplified == constant, "simplify could not make it simpler");
        constant = constant.scaleBy(1.0 / 8);
        assertEquals(Constant.ONE, constant, "now it is equal to ONE");
        assertTrue(constant == Constant.ONE, "in fact, it is the same object");
        constant = constant.scaleBy(0);
        assertEquals(Constant.ZERO, constant, "now it is ZERO");
        assertTrue(Constant.ZERO == constant, "and it is the same object");
        try
        {
            Constant.ZERO.compareWithinSubType(Nan.NAN);
            fail("compareWithinSubType with wrong type should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertEquals(1.0, Constant.ONE.getScale(), "scale factor of ONE is 1.0");
        assertEquals(0.0, Constant.ZERO.getScale(), "scale factor of ZERO is 0.0");
        constant = new Constant(123.456);
        assertEquals(123.456, constant.getScale(), 0.0, "scale factor is returned");
        constant = constant.mergeMultiply(new Constant(2.0));
        assertEquals(2 * 123.456, constant.getScale(), 0.0, "scale factor is updated");
        constant = constant.mergeAdd(new Constant(987.654));
        assertEquals(2 * 123.456 + 987.654, constant.getScale(), 0.0, "scale factor is updated");
        assertNull(constant.mergeMultiply(new Power(2, 3)), "Constant can not merge with PowerFunction");

        assertEquals(KnotReport.NONE, constant.getKnotReport(new Interval<String>(10, true, 20, false, "string")),
                "constant has no knots");
        assertEquals(0, constant.getKnots(new Interval<String>(10, true, 20, false, "string")).size(), "zero knots");
    }

    /**
     * Test the Nan class.
     */
    @Test
    public void testNan()
    {
        for (double x : new double[] {-1e20, -5.555, 0, Math.E, 23.45e200})
        {
            assertTrue(Double.isNaN(Nan.NAN.apply(x)), "value is NaN for every x");
        }
        assertEquals("NaN", Nan.NAN.toString(), "toString returns something descriptive");
        assertEquals(Nan.NAN, Nan.NAN.getDerivative(), "derivative is itself");
        MathFunction otherNan = Nan.NAN.scaleBy(10);
        assertTrue(otherNan == Nan.NAN, "it is the same object");
        assertEquals(3, Nan.NAN.sortPriority(), "sort priority is 3");
        assertEquals(0, Nan.NAN.compareWithinSubType(Nan.NAN), "compare within sub type is always 0");
        try
        {
            Nan.NAN.compareWithinSubType(Constant.ONE);
            fail("compareWithinSubType should throw an IllegalArgumentException when called with some other type");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        assertEquals(KnotReport.KNOWN_INFINITE, Nan.NAN.getKnotReport(new Interval<String>(10, true, 20, true, "string")),
                "infinitely many knots on any interval");
        try
        {
            Nan.NAN.getKnots(new Interval<String>(10, true, 20, true, "string"));
            fail("attempt to collect infinitely many knots should have thrown an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
        
        assertEquals(KnotReport.KNOWN_FINITE, Nan.NAN.getKnotReport(new Interval<String>(10, true, 10, true, "string")),
                "one knots on zero width interval");
        assertEquals(1, Nan.NAN.getKnots(new Interval<String>(10, true, 10, true, "string")).size(), "one knot");
        assertEquals(10.0, Nan.NAN.getKnots(new Interval<String>(10, true, 10, true, "string")).first(), "knot is at the 10.0");
    }
}
