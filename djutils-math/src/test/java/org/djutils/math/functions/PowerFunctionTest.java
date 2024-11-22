package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Function pf = new PowerFunction(12.3);
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
        Function pf2 = new PowerFunction(0.5, -0.5);
        for (double x : new double[] {0.1, 3, 999})
        {
            assertEquals(pf.get(x), pf2.get(x), 0.0001, "results are the same");
        }
        assertTrue(pf.equals(pf2), "these functions test equal");
        pf = new PowerFunction(4, 1); // 4 * x
        pf2 = pf.getDerivative();
        Function constant4 = new Constant(4);
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
        assertEquals("0", pf.getDescription(), "constant 0 powerfunction describes itself as 0");
        pf = new PowerFunction(2, 0);
        assertEquals("2", pf.getDescription(), "constant 2 powerfunction describes itself as 2");
        pf = new PowerFunction(1, 3);
        assertEquals("x\u00b3", pf.getDescription(), "power function with unit weight leaves of the weight");
        pf = new PowerFunction(3, 1);
        assertEquals("3x", pf.getDescription(), "exponent 1 is left off");
        pf = new PowerFunction(2, 3);
        assertEquals("2x\u00b3", pf.getDescription(), "general case");
        pf = pf.scaleBy(4);
        assertEquals("8x\u00b3", pf.getDescription(), "scaleBy works");
        Function scaledByOne = pf.scaleBy(1);
        assertTrue(scaledByOne == pf, "scaling by 1 returns the original object");
        pf = pf.scaleBy(0);
        assertTrue(pf == Constant.ZERO, "scaleBy 0 yields ZERO");
        pf = new PowerFunction(3, 12);
        assertEquals("3x^12", pf.getDescription(), "larger exponents use the ^ notation");
        pf = new PowerFunction(3, 1.2);
        assertEquals("3x^1.2", pf.getDescription(), "fractional exponents use the ^ notation");
        
        pf = new PowerFunction(3, 7);
        assertEquals("POW", pf.getId(), "id is POW");
        assertTrue(pf.toString().startsWith("PowerFunction ["), "toString returns something descriptive");
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
    }
}
