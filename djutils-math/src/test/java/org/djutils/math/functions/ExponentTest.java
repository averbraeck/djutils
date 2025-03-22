package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Exponent class.
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
public class ExponentTest
{
    /**
     * Test the Exponent class.
     */
    @Test
    public void testExponent()
    {
        double[] xValues = new double[] {-10, -1, 0, 0.3, 1, Math.PI, 10};
        Exponential e = new Exponential();
        MathFunction e3 = e.scaleBy(3.0);
        MathFunction derivative = e3.getDerivative();
        MathFunction chain = new Sine(2, 1, 0); // 2 * sin(x)
        MathFunction eOfSine = new Exponential(chain, 2); // 2 * exp(2 * sin(x))
        MathFunction derivativeChained = eOfSine.getDerivative(); // 4 * cos(x) * exp(2 * sin(x))
        Exponential eChain = new Exponential(chain);
        for (double x : xValues)
        {
            assertEquals(Math.exp(x), e.apply(x), 0.001, "Computes the exponent of x");
            assertEquals(3 * Math.exp(x), e3.apply(x), 0.01, "scaleBy works");
            assertEquals(3 * Math.exp(x), derivative.apply(x), 0.01, "derivative works");
            assertEquals(2 * Math.exp(2 * Math.sin(x)), eOfSine.apply(x), "chaining works");
            assertEquals(4 * Math.cos(x) * Math.exp(2 * Math.sin(x)), derivativeChained.apply(x), 0.0001,
                    "derivative of chained works");
            assertEquals(Math.exp(2 * Math.sin(x)), eChain.apply(x), 0.0001, "default factor works with chaining");
        }

        assertEquals(Constant.ZERO, new Exponential(0).simplify(), "simplifies to ZERO");
        assertEquals(e, e.scaleBy(1.0), "scale by 1.0 returns e");
        assertEquals(Constant.ZERO, e.scaleBy(0.0), "simplifies to ZERO");
        assertTrue(e.compareTo(e3) < 0, "sorts by factor");
        assertTrue(e3.compareTo(e) > 0, "sorts by factor");
        assertEquals(0, e.compareTo(e), "equal compares at 0");
        assertTrue(e.compareTo(eChain) > 0, "Simpler function sorts first");
        assertTrue(eChain.compareTo(e) < 0, "Simpler function sorts first");
        assertEquals(0, eChain.compareTo(eChain), "equal compares at 0");
        try
        {
            e.compareWithinSubType(chain);
            fail("compareWithinSubType should throw IllegalArgument for incompatible sub type");
        }
        catch (IllegalArgumentException exception)
        {
            // Ignore expected exception
        }

        MathFunction sum = new Sum(e, e3);
        MathFunction simplified = sum.simplify();
        assertEquals(new Exponential(4), simplified, "should simplify to 4 * exp(x)");
        assertNull(e.mergeAdd(chain), "cannot simplify this");
        assertEquals(new Exponential(2), e.mergeAdd(e), "should simplify to this");
        assertEquals(new Exponential(chain, 2), eChain.mergeAdd(eChain), "should simplify to this");
        assertNull(e.mergeAdd(eChain), "does not simplify");
        assertNull(eChain.mergeAdd(e), "does not simplify");
        assertEquals("e\u02e3", e.toString(), "should print like that");
        assertTrue(eOfSine.toString().startsWith("2exp("), "uses exp notation");
        assertTrue(eOfSine.toString().contains(chain.toString()), "contains toString of chained function");
        assertEquals("0", new Exponential(0.0).toString(), "effectively ZERO exponential prints like 0");

        assertEquals(new Constant(Math.exp(2)), new Exponential(new Constant(2)).simplify(), "simplifies to Constant");
        assertNotEquals(e.hashCode(), new Exponential(2).hashCode(), "hash code takes factor into account");
        assertFalse(e.equals(new Exponential(2)), "not equal");
        assertFalse(e.equals(null), "not equal to null");
        assertFalse(e.equals("not an Exponent"), "not equal to some String");
        assertFalse(eChain.equals(e), "checks chain");
        assertFalse(e.equals(eChain), "checks chain");
        assertTrue(eChain.equals(new Exponential(chain)), "should test equal");

        e = new Exponential(2);
        assertEquals(KnotReport.NONE, e.getKnotReport(new Interval<String>(-10, true, 20, true, "string")), "no knots");
        assertEquals(0, e.getKnots(new Interval<String>(-10, true, 20, true, "string")).size(), "zero knots");
        e = new Exponential(new Logarithm());
        assertEquals(KnotReport.KNOWN_FINITE, e.getKnotReport(new Interval<String>(0, true, 20, true, "string")), "one knot");
        assertEquals(1, e.getKnots(new Interval<String>(0, true, 20, true, "string")).size(), "one knot");
        assertEquals(0.0, e.getKnots(new Interval<String>(0, true, 20, true, "string")).first(), "one knot at 0.0");
        assertEquals(KnotReport.KNOWN_INFINITE, e.getKnotReport(new Interval<String>(-1, true, 20, true, "string")),
                "infinitely many");
        try
        {
            e.getKnots(new Interval<String>(-1, true, 20, true, "string"));
            fail("attempt to collect infinitely many knots should throw an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException exeption)
        {
            // Ignore expected exception
        }
    }
}
