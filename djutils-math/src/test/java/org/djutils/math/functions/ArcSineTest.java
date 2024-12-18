package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the ArcSine class.
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
public class ArcSineTest
{
    /**
     * Test the ArcSine class.
     */
    @Test
    public void testArcSine()
    {
        double[] xValues = new double[] {-0.9, -0.5, -0.1, 0.0, 0.1, 0.4, 0.96};
        MathFunction asin = new ArcSine();
        MathFunction asin2 = new ArcSine(2);
        MathFunction asin3 = new ArcSine(3, 2);
        MathFunction chain = new Power(0.9, 2); // 0.9 * x^2
        MathFunction asin4 = new ArcSine(chain);
        MathFunction asin5 = new ArcSine(chain, 2);
        MathFunction asin6 = new ArcSine(chain, 3, 2);
        MathFunction asin7 = asin6.scaleBy(1.5);
        MathFunction derivative = asin.getDerivative();
        MathFunction acos = ArcSine.arcCosine(null, 2);
        for (double x : xValues)
        {
            assertEquals(Math.asin(x), asin.get(x), 0.00001, "asin with no parameters");
            assertEquals(2 * Math.asin(x), asin2.get(x), 0.00001, "asin with one double paramater");
            assertEquals(3 * (Math.asin(x) + 2), asin3.get(x), 0.00001, "asin with two double parameters");
            assertEquals(Math.asin(0.9 * x * x), asin4.get(x), 0.00001, "asin with chain and no double parameters");
            assertEquals(2 * Math.asin(0.9 * x * x), asin5.get(x), 0.00001, "asin with chain and one double parameters");
            assertEquals(3 * (Math.asin(0.9 * x * x) + 2), asin6.get(x), 0.00001, "asin with chain and two double parameters");
            assertEquals(1.5 * 3 * (Math.asin(0.9 * x * x) + 2), asin7.get(x), 0.00001,
                    "asin with chain and two double parameters scaled");
            assertEquals(1.0 / Math.sqrt(1 - x * x), derivative.get(x), 0.00001, "derivative of simplest case works");
            assertEquals(2 * Math.acos(x), acos.get(x), 0.00001, "acos works");
        }
        assertEquals(3, asin3.getScale(), "omega is scale");
        assertEquals(Constant.ZERO, new ArcSine(0).simplify(), "should simplify to ZERO");
        assertEquals(new Constant(Math.asin(0.5)), new ArcSine(new Constant(0.5)).simplify(), "should simplify to constant");
        assertEquals(Constant.ZERO, asin.scaleBy(0), "scale by 0.0 yields ZERO");
        assertEquals(asin3, asin3.scaleBy(1.0), "scaling by 1.0 yields input");
        assertEquals(asin6, asin6.simplify(), "cannot simplify that one");
        assertEquals(5, asin.sortPriority(), "sort priority is 5");
        assertEquals(0, asin.compareWithinSubType(asin), "should compare same");
        assertEquals(0, asin7.compareWithinSubType(asin7), "should compare same");
        assertTrue(asin.compareTo(asin2) < 0, "sorts before");
        assertTrue(asin2.compareTo(asin) > 0, "sorts after");
        MathFunction asin8 = new ArcSine(1, 2);
        assertTrue(asin.compareTo(asin8) < 0, "sorts before");
        assertTrue(asin8.compareTo(asin) > 0, "sorts after");
        assertTrue(asin4.compareTo(asin) < 0, "sorts before");
        assertTrue(asin.compareTo(asin4) > 0, "sorts after");
        try
        {
            asin.compareWithinSubType(Constant.ONE);
            fail("Incompatible sub type should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertEquals("0", new ArcSine(0.0).toString(), "non-simplified zero arc sine does print as 0");
        assertEquals(0.0, new ArcSine(0.0).get(123), "non-simplified zero evalueates to 0");
        assertTrue(asin.toString().startsWith("asin"), "multiplier 1.0 is not printed");
        assertTrue(asin2.toString().startsWith("2asin"), "non unit multiplier IS printed");
        assertTrue(new ArcSine(1, -2).toString().contains("x-2"), "negative shift is printed");
        assertTrue(new ArcSine(1, 3).toString().contains("x+3"), "positive shift is printed with sign");
        assertTrue(asin6.toString().contains(chain.toString()), "chain is printed");

        assertNotEquals(asin.hashCode(), asin2.hashCode(), "hashCode takes omega into account");
        assertNotEquals(asin3.hashCode(), new ArcSine(2, 1).hashCode(), "hashCode takes shift into account");
        assertNotEquals(asin.hashCode(), asin4.hashCode(), "hashCode takes chain into account");

        assertFalse(asin.equals(null), "not equal to null");
        assertTrue(asin.equals(asin), "equal to itself");
        assertTrue(asin.equals(new ArcSine()), "equal to identical ArcSine");
        assertFalse(asin.equals("Not an ArcSine"), "not equal to some other object");
        assertFalse(asin.equals(new ArcSine(2)), "not equal to ArcSine with different omega");
        assertFalse(asin.equals(new ArcSine(1, 2)), "not equal to ArcSine with different shift");
        assertFalse(asin.equals(asin4), "not equal to one with chained function");
        assertFalse(asin4.equals(asin), "not equal to one with chained function");

        assertEquals(KnotReport.NONE, asin.getKnotReport(new Interval<String>(-1, true, 1, true, "string")), "no knots");
        assertEquals(0, asin.getKnots(new Interval<String>(-1, true, 1, true, "string")).size(), "no knots");

        assertEquals(KnotReport.KNOWN_INFINITE, asin.getKnotReport(new Interval<String>(-1.5, true, 1, true, "string")),
                "infinite");
        assertEquals(KnotReport.KNOWN_INFINITE, asin.getKnotReport(new Interval<String>(-1, true, 1.5, true, "string")),
                "infinite");
        try
        {
            asin.getKnots(new Interval<String>(-1.5, true, 1, true, "string"));
            fail("infinite set should throw an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }

        assertEquals(KnotReport.UNKNOWN,
                new ArcSine(new Power(1, 2), 1, 1).getKnotReport(new Interval<String>(0, true, 1, true, "string")),
                "not possible");
        try
        {
            new ArcSine(new Power(1, 2), 1, 1).getKnots(new Interval<String>(0, true, 1, true, "string"));
            fail("infinite set should throw an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
    }
}
