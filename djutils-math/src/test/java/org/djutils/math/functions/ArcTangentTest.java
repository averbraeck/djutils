package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the ArcTangentTest class.
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
public class ArcTangentTest
{
    /**
     * Test the ArcTangent class.
     */
    @Test
    public void arcTangentTest()
    {
        double[] xValues = new double[] {-100, -10, -1, -0.1, 0.0, 0.1, 5, 50};
        ArcTangent at1 = new ArcTangent();
        ArcTangent at2 = new ArcTangent(3);
        ArcTangent at3 = new ArcTangent(3, 4);
        Power p = new Power(0.01, 3);
        ArcTangent at4 = new ArcTangent(p, 3, 4);
        assertEquals(at4, at4.simplify(), "cannot simplify with non-constant chain function");
        ArcTangent at5 = new ArcTangent(0);
        MathFunction derivAt1 = at1.getDerivative();
        MathFunction derivAt2 = at2.getDerivative();
        MathFunction derivAt3 = at3.getDerivative();
        MathFunction derivAt4 = at4.getDerivative();
        MathFunction derivAt5 = at5.getDerivative();
        for (double x : xValues)
        {
            assertEquals(Math.atan(x), at1.get(x), 0.0001, "plain atan(x)");
            assertEquals(3 * Math.atan(x), at2.get(x), 0.0001, "3 * atan(x)");
            assertEquals(3 * (Math.atan(x) + 4), at3.get(x), 0.0001, "3 * (atan(x) + 4)");
            assertEquals(3 * (Math.atan(0.01 * x * x * x) + 4), at4.get(x), 0.0001, "3 * (atan(x^3*0.001) + 4)");
            assertEquals(0, at5.get(x), "short circuits to 0");
            assertEquals(1.0 / (x * x + 1), derivAt1.get(x), 0.0001, "derivative of atan(x)");
            assertEquals(3.0 / (x * x + 1), derivAt2.get(x), 0.0001, "derivative of 3 * atan(x)");
            assertEquals(3.0 / (x * x + 1), derivAt3.get(x), 0.0001, "derivative of 3 * (atan(x) + 4)");
            assertEquals(0.01 * 9 * x * x / (0.0001 * Math.pow(x, 6) + 1), derivAt4.get(x), 0.0001,
                    "derivative of 3 * (atan(x^3*0.001) + 4)");
            assertEquals(0, derivAt5.get(x), 0.0001, "derivative of constant zero is 0");
        }
        assertEquals(Constant.ZERO, at5.simplify(), "should simplify to constant ZERO");
        assertEquals(new Constant(2 * (Math.atan(33) + 3)), new ArcTangent(new Constant(33), 2, 3).simplify(),
                "constant chain function should yiels constant");
        assertEquals(at1, at1.simplify(), "cannot be further simplified");
        assertEquals(1, at1.getScale(), 0, "scale is 1");
        assertEquals(123, at1.scaleBy(123).getScale(), 0, "scaling works");
        assertEquals(at1, at1.scaleBy(1), "scaling one returns input");
        assertEquals(Constant.ZERO, at1.scaleBy(0), "scaling by zero returns ZERO");
        assertEquals(0, at1.compareWithinSubType(at1), "should compare same");
        assertEquals(0, at4.compareWithinSubType(at4), "should compare same");
        assertTrue(at1.compareTo(at2) < 0, "sorts before");
        assertTrue(at2.compareTo(at1) > 0, "sorts after");
        assertTrue(at1.compareTo(at4) < 0, "sorts before");
        assertTrue(at4.compareTo(at1) > 0, "sorts after");
        assertTrue(at2.compareTo(at3) < 0, "sorts before");
        assertTrue(at3.compareTo(at2) > 0, "sorts after");
        try
        {
            at1.compareWithinSubType(Constant.ONE);
            fail("Incompatible sub type should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertEquals("0", new ArcSine(0.0).toString(), "non-simplified zero arc sine does print as 0");
        assertEquals(0.0, new ArcSine(0.0).get(123), "non-simplified zero evalueates to 0");
        assertTrue(at1.toString().startsWith("atan"), "multiplier 1.0 is not printed");
        assertTrue(at2.toString().startsWith("3atan"), "non unit multiplier IS printed");
        assertTrue(new ArcTangent(1, -2).toString().contains("x-2"), "negative shift is printed");
        assertTrue(new ArcTangent(1, 3).toString().contains("x+3"), "positive shift is printed with sign");
        assertTrue(at4.toString().contains(p.toString()), "chain is printed");

        assertNotEquals(at1.hashCode(), at2.hashCode(), "hashCode takes omega into account");
        assertNotEquals(at3.hashCode(), new ArcTangent(3, 5).hashCode(), "hashCode takes shift into account");
        assertNotEquals(at3.hashCode(), at4.hashCode(), "hashCode takes chain into account");

        assertFalse(at1.equals(null), "not equal to null");
        assertTrue(at1.equals(at1), "equal to itself");
        assertTrue(at1.equals(new ArcTangent()), "equal to identical ArcTangent");
        assertFalse(at1.equals("Not an ArcTangent"), "not equal to some other object");
        assertFalse(at1.equals(new ArcTangent(2)), "not equal to ArcTangent with different omega");
        assertFalse(at1.equals(new ArcTangent(1, 2)), "not equal to ArcTangent with different shift");
        assertFalse(at1.equals(at4), "not equal to one with chained function");
        assertFalse(at4.equals(at1), "not equal to one with chained function");

        assertEquals(KnotReport.NONE,
                at1.getKnotReport(
                        new Interval<String>(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true, "string")),
                "no knots");
        assertEquals(0,
                at1.getKnots(new Interval<String>(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true, "string"))
                        .size(),
                "no knots");
        assertEquals(KnotReport.UNKNOWN,
                new ArcTangent(new Power(1, 2), 1, 1).getKnotReport(new Interval<String>(0, true, 1, true, "string")),
                "not possible");
        try
        {
            new ArcTangent(new Power(1, 2), 1, 1).getKnots(new Interval<String>(0, true, 1, true, "string"));
            fail("infinite set should throw an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
        assertTrue(new ArcTangent(0, 1).toString().equals("0"), "prints as 0");
    }
}
