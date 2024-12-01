package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the PowerFunction class.
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
public class PowerFunctionTest
{
    /**
     * Test the PowerFunction class.
     */
    @Test
    public void testPowerFunction()
    {
        MathFunction pf = new PowerFunction(12.3);
        assertEquals(Math.pow(2, 12.3), pf.get(2), 0.0001, "default weight is 1.0 and broken powers work");
        pf = new PowerFunction(10, 0.5);
        assertEquals(10 * Math.pow(3.3, 0.5), pf.get(3.3), 0.0001, "weight works");
        pf = new PowerFunction(-0.5);
        assertEquals(1 / Math.sqrt(5), pf.get(5), 0.0001, "negative exponent works");
        pf = new PowerFunction(0, 0);
        assertEquals(0, pf.get(123), 0, "constant 0 is really 0");
        pf = new PowerFunction(123.456, 0);
        assertEquals(123.456, pf.get(654.321), 0, "constant non-zero is exact");
        pf = new PowerFunction(0.5).getDerivative(); // Expect 0.5 / sqrt(x)
        MathFunction pf2 = new PowerFunction(0.5, -0.5);
        for (double x : new double[] {0.1, 3, 999})
        {
            assertEquals(pf.get(x), pf2.get(x), 0.0001, "results are the same");
        }
        assertTrue(pf.equals(pf2), "these functions test equal");
        pf = new PowerFunction(4, 1); // 4 * x
        pf2 = pf.getDerivative();
        MathFunction constant4 = new Constant(4);
        assertEquals(constant4, pf2, "derivative of 4 * x is constant 4");
        pf = new PowerFunction(6, 0); // result is constant 6; but the object is a PowerFunction
        pf2 = pf.getDerivative();
        assertEquals(Constant.ZERO, pf2, "derivative is pre-defined constant ZERO");
        pf = new PowerFunction(1, 1); // x
        assertEquals(123.456, pf.get(123.456), 0, "result of this simple one is exact");
        pf2 = pf.getDerivative();
        assertEquals(Constant.ONE, pf2, "derivative is pre-defined constant ONE");
        pf = new PowerFunction(0, 8); // 0 * x^8
        pf2 = pf.getDerivative();
        assertEquals(Constant.ZERO, pf2, "derivative is pre-defined constant ZERO");
        // test the description method
        pf = new PowerFunction(0, 123);
        assertEquals("0", pf.toString(), "constant 0 powerfunction describes itself as 0");
        pf = new PowerFunction(2, 0);
        assertEquals("2", pf.toString(), "constant 2 powerfunction describes itself as 2");
        pf = new PowerFunction(1, 3);
        assertEquals("x\u00b3", pf.toString(), "power function with unit weight leaves of the weight");
        pf = new PowerFunction(3, 1);
        assertEquals("3x", pf.toString(), "exponent 1 is left off");
        pf = new PowerFunction(2, 3);
        assertEquals("2x\u00b3", pf.toString(), "general case");
        pf = pf.scaleBy(4);
        assertEquals("8x\u00b3", pf.toString(), "scaleBy works");
        MathFunction scaledByOne = pf.scaleBy(1);
        assertTrue(scaledByOne == pf, "scaling by 1 returns the original object");
        pf = pf.scaleBy(0);
        assertTrue(pf == Constant.ZERO, "scaleBy 0 yields ZERO");
        pf = new PowerFunction(3, 12);
        assertEquals("3x\u00b9\u00b2", pf.toString(), "multi digit superscript exponent");
        pf = new PowerFunction(3, 1.2);
        assertEquals("3x\u00b9\u02d9\u00b2", pf.toString(), "fractional exponents use the ^ notation");

        pf = new PowerFunction(3, 7);
        assertTrue(pf.equals(pf), "equal to itself");
        assertFalse(pf.equals(null), "not equal to null");
        assertFalse(pf.equals("Not a PowerFunction"), "not equal to some other kind of Object");
        pf2 = new PowerFunction(3, 7);
        assertTrue(pf.equals(pf2), "same content tests as equal");
        assertEquals(pf.hashCode(), pf2.hashCode(), "same content; same hash code");
        pf2 = new PowerFunction(4, 7);
        assertFalse(pf.equals(pf2), "weight is part of the equals test");
        assertNotEquals(pf.hashCode(), pf2.hashCode(), "hashCode takes weight into account");
        pf2 = new PowerFunction(3, 5);
        assertFalse(pf.equals(pf2), "power is part of the equals test");
        assertNotEquals(pf.hashCode(), pf2.hashCode(), "hashCode takes power into account");
        MathFunction chained = new Sine(1, 1, 0);
        pf = new PowerFunction(chained, 2);
        pf2 = new PowerFunction(chained, 1.0, 2);
        assertEquals(pf, pf2);
        assertEquals(pf.get(12), pf2.get(12), 0.0, "of course...");
        MathFunction derivative = pf.getDerivative();
        MathFunction expectedDerivative = new Product(new Constant(2), new Sine(1, 1, 0), new Sine(1, 1, Math.PI / 2));
        for (double x : new double[] {-10, -2, 0, 0.1, Math.E, Math.PI})
        {
            assertEquals(expectedDerivative.get(x), derivative.get(x), 0.00001, "derivative with chained function");
        }

        pf = new PowerFunction(3, 0);
        pf2 = pf.simplify();
        assertEquals(new Constant(3), pf2, "should simplify to constant 3");
        pf = new PowerFunction(0, 0);
        pf2 = pf.simplify();
        assertEquals(Constant.ZERO, pf2, "should simplify to ZERO");
        pf = new PowerFunction(5, 0);
        pf2 = pf.simplify();
        assertEquals(new Constant(5), pf2, "should simplify to Constant 5");
        pf = new PowerFunction(1, 0);
        pf2 = pf.simplify();
        assertEquals(new Constant(1), pf2, "should simplify to Constant 5");
        pf = new PowerFunction(-0.0, 0);
        pf2 = pf.simplify();
        assertEquals(Constant.ZERO, pf2, "should simplify to Constant 5");

        pf = new PowerFunction(chained, 2, 3);
        pf2 = new PowerFunction(chained, 3, 3);
        MathFunction combined = pf.mergeMultiply(pf2);
        assertEquals(new PowerFunction(chained, 6, 6), combined);
        pf2 = new PowerFunction(3, 4);
        assertNull(pf.mergeMultiply(pf2), "cannot merge those");
        pf2 = new PowerFunction(chained, 3, 3);
        combined = pf.mergeMultiply(pf2);
        assertEquals(new PowerFunction(chained, 6, 6), combined);
        pf2 = new PowerFunction(3, 4);
        assertNull(pf.mergeMultiply(pf2), "cannot merge those");
        pf2 = new PowerFunction(new PowerFunction(2, 2), 2, 3);
        combined = pf.mergeMultiply(pf2);
        assertEquals(new PowerFunction(new Product(new PowerFunction(2, 2), new Sine(1, 1, 0)), 4, 3), combined, "nice");

        pf = new PowerFunction(2, 3);
        try
        {
            pf.compareWithinSubType(Constant.ZERO);
            fail("compareWithinSubType should throw IllegalArgumentException for incompatible sub type");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        pf2 = new PowerFunction(3, 2);
        assertTrue(pf.compareWithinSubType(pf2) < 0, "should sort by power");
        assertTrue(pf2.compareWithinSubType(pf) > 0, "should sort by power");
        pf2 = new PowerFunction(3, 3);
        assertTrue(pf.compareWithinSubType(pf2) > 0, "same power sorts by weight");
        assertTrue(pf2.compareWithinSubType(pf) < 0, "same power sorts by weight");
        pf2 = new PowerFunction(chained, 2, 3);
        assertFalse(pf.equals(pf2), "equals takes chained into account");
        assertTrue(pf2.toString().contains("(" + chained.toString() + ")"), "toString output contains chained");

        chained = new Sine(1, 2, 3);
        pf = new PowerFunction(chained, 2, 3);
        MathFunction chained2 = new Sine(2, 3, 4);
        pf2 = new PowerFunction(chained2, 2, 3);
        assertNull(pf.mergeAdd(pf2), "cannot mergeAdd these");
        assertNotNull(pf.mergeMultiply(pf2), "can mergeMultiply these");
        pf2 = new PowerFunction(2, 3);
        assertNull(pf.mergeAdd(pf2), "cannot mergeAdd these");
        assertNotNull(pf.mergeMultiply(pf2), "can mergeMultiply these");
        // System.out.println(pf);
        // System.out.println(pf2);
        // System.out.println(pf.mergeMultiply(pf2));
        assertEquals(pf.get(10) * pf2.get(10), pf.mergeMultiply(pf2).get(10), 0.0001, "check");
        assertNull(pf2.mergeAdd(pf), "cannot mergeAdd these");
        assertNotNull(pf2.mergeMultiply(pf), "can mergeMultiply these");
        assertEquals(pf.get(10) * pf2.get(10), pf2.mergeMultiply(pf).get(10), 0.0001, "check");
        assertNotNull(pf.mergeAdd(pf), "can mergeAdd these");
        assertEquals(pf.get(10) + pf.get(10), pf.mergeAdd(pf).get(10), 0.0001, "check");
        pf = new PowerFunction(2, 3);
        assertNotNull(pf.mergeMultiply(pf2), "can mergeMultiply these");
        assertEquals(pf.get(10) * pf2.get(10), pf.mergeMultiply(pf2).get(10), 0.0001, "check");

        pf = new PowerFunction(2, 3);
        pf2 = new PowerFunction(3, 2);
        // System.out.println(pf);
        // System.out.println(pf2);
        MathFunction quotient = new Quotient(pf, pf2);
        // System.out.println(quotient);
        quotient = quotient.simplify();
        // System.out.println(quotient);
        assertEquals(new PowerFunction(2.0 / 3.0, 1), quotient, "should simplify to linear power function");
        quotient = new Quotient(pf2, pf);
        // System.out.println(quotient);
        quotient = quotient.simplify();
        // System.out.println(quotient);
        assertEquals(new PowerFunction(1.5, -1), quotient, "should simplify to this");
        quotient = new Quotient(pf, new Sine(1, 2, 3));
        assertNull(quotient.simplify(), "cannot simplify this");
        pf2 = new PowerFunction(chained, 3, 2);
        quotient = new Quotient(pf, pf2);
        assertNull(quotient.simplify(), "cannot simplify this, yet");
        quotient = new Quotient(pf2, pf);
        assertNull(quotient.simplify(), "cannot simplify this, yet");
        pf = new PowerFunction(chained, 2, 3);
        quotient = new Quotient(pf, pf2);
        // System.out.println(quotient);
        quotient = quotient.simplify();
        // System.out.println(quotient);
        assertEquals(new PowerFunction(chained, 2.0 / 3.0, 1), quotient, "should simplify to this");

    }

    /**
     * Test the remaining bits of the SuperScript class.
     */
    @Test
    public void superScriptTest()
    {
        // Should really run some OCR on the result to check that it resembles the original letter...
        assertEquals(":", new SuperScript().translate(":"), "a character not in the translate table translates into itself");
    }
}
