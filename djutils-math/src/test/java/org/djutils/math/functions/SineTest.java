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
 * Test the Sine class.
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
public class SineTest
{

    /**
     * Test the Sine class.
     */
    @Test
    public void sineTest()
    {
        double[] xValues = new double[] {0, 1, 2, 3, 100, -100};
        Sine s1 = new Sine(1, 1, 0);
        assertEquals(0, s1.get(0), 0.0001, "sin(0) = 0");
        assertEquals(1, s1.get(Math.PI / 2), 0.0001, "sin(pi/2) = 1");
        assertEquals(0, s1.get(Math.PI), 0.0001, "sin(pi) = 0");
        assertEquals(-1, s1.get(3 * Math.PI / 2), 0.0001, "sin(3*pi/2) = -1");
        assertEquals(0, s1.get(100 * Math.PI), 0.0001, "sin(100 * pi) = 0");
        s1 = new Sine(100, 1, 0);
        assertEquals(0, s1.get(0), 0.0001, "100 * sin(0) = 0");
        assertEquals(100, s1.get(Math.PI / 2), 0.0001, "100 * sin(pi/2) = 1");
        assertEquals(0, s1.get(Math.PI), 0.0001, "100 * sin(pi) = 0");
        assertEquals(-100, s1.get(3 * Math.PI / 2), 0.0001, "100 * sin(3*pi/2) = -1");
        assertEquals(0, s1.get(100 * Math.PI), 0.0001, "100 * sin(100 * pi) = 0");
        s1 = new Sine(1, 10, 0);
        assertEquals(0, s1.get(0), 0.0001, "sin(0) = 0");
        assertEquals(1, s1.get(Math.PI / 2 / 10), 0.0001, "sin(10 * pi / 20) = 0");
        assertEquals(0, s1.get(Math.PI / 1 / 10), 0.0001, "sin(10 * pi / 10) = 0");
        assertEquals(-1, s1.get(3 * Math.PI / 2 / 10), 0.0001, "sine(3 * 10 * pi / 2 / 10) = -1");
        s1 = new Sine(1, 1, 2);
        assertEquals(Math.sin(2), s1.get(0), 0.0001, "sin(0 + 2) = sin(2)");
        assertEquals(Math.sin(2 + Math.PI / 2), s1.get(Math.PI / 2), 0.0001, "sin(2 + pi / 2)");
        assertEquals(Math.sin(2 + Math.PI), s1.get(Math.PI), 0.0001, "sin(2 + pi)");
        assertEquals(Math.sin(2 + 1.5 * Math.PI), s1.get(3 * Math.PI / 2), 0.0001, "sin(2 + 1.5 * pi");
        s1 = new Sine(2, 3, 4); // 2 * sin(3 * x + 4)
        MathFunction derivative = s1.getDerivative(); // expect 6 * cos(3 * x + 4)
        for (double x : xValues)
        {
            assertEquals(6 * Math.cos(3 * x + 4), derivative.get(x), 0.0001, "derivative check");
        }
        s1 = new Sine(0, 1, 2); // 0 * sin(x + 2)
        assertEquals(0, s1.get(123), 0, "constant zero");
        MathFunction f = s1.simplify();
        assertEquals(Constant.ZERO, f, "should be simplified to ZERO");

        MathFunction chained = new Power(1, 2); // x^2
        s1 = new Sine(chained, 2, 3, 4); // 2 * sin(3 * x ^ 2 + 4)
        for (double x : xValues)
        {
            assertEquals(2 * Math.sin(3 * x * x + 4), s1.get(x), 0.0001, "chained function check");
        }
        derivative = s1.getDerivative(); // 12 * x * cos(3 * x * x + 4)
        for (double x : xValues)
        {
            assertEquals(12 * x * Math.cos(3 * x * x + 4), derivative.get(x), 0.0001, "derived chained function check");
        }
        MathFunction s2 = s1.scaleBy(1.0);
        assertTrue(s1 == s2, "scale by 1.0 returns original");
        s2 = s1.scaleBy(0.0);
        assertTrue(Constant.ZERO == s2, "scale by 0.0 return ZERO");

        try
        {
            s1.compareWithinSubType(Constant.ONE);
            fail("compare within sub type should throw IllegalArgumentException for incompatible type");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        s1 = new Sine(1, 2, 3); // sin(2 * x + 3)
        s2 = new Sine(2, 3, 3); // 2 * sin(3 * x + 3)
        assertTrue(s1.compareTo(s2) < 0, "sines sort by frequency");
        assertTrue(s2.compareTo(s1) > 0, "sines sort by frequency");
        s2 = new Sine(1, 2, 0); // sin(2 * x)
        MathFunction sum = new Sum(s1, s2); // sin(2 * x + 3) + sin(2 * x)
        MathFunction simplified = sum.simplify();
        for (double x : xValues)
        {
            assertEquals(Math.sin(2 * x + 3) + Math.sin(2 * x), sum.get(x), 0.0001, "sum of two sines, same omega");
            assertEquals(Math.sin(2 * x + 3) + Math.sin(2 * x), simplified.get(x), 0.0001,
                    "simplified sum of two sines, same omega");
        }
        s2 = new Sine(2, 2, 3);
        assertTrue(s1.compareTo(s2) < 0, "sort by amplitude");
        assertTrue(s2.compareTo(s1) > 0, "sort by amplitude");
        s2 = new Sine(1, 3, 0); // sin(3 * x)
        sum = new Sum(s1, s2); // sin(2 * x + 3) + sin(3 * x)
        simplified = sum.simplify();
        for (double x : xValues)
        {
            assertEquals(Math.sin(2 * x + 3) + Math.sin(3 * x), sum.get(x), 0.0001, "sum of two sines, different omega");
            assertEquals(Math.sin(2 * x + 3) + Math.sin(3 * x), simplified.get(x), 0.0001,
                    "simplified sum of two sines, different omega");
        }
        s1 = new Sine(chained, 1, 2, 3);
        s2 = new Sine(1, 2, 3);
        sum = new Sum(s1, s2); // sin(2 * x * x + 3) + sin(2 * x + 3)
        simplified = sum.simplify();
        for (double x : xValues)
        {
            assertEquals(Math.sin(2 * x * x + 3) + Math.sin(2 * x + 3), sum.get(x), 0.0001,
                    "sum of two sines, same omega, one chained");
            assertEquals(Math.sin(2 * x * x + 3) + Math.sin(2 * x + 3), simplified.get(x), 0.0001,
                    "simplified sum of two sines, same omega, one chained");
        }
        s2 = new Constant(5);
        sum = new Sum(s1, s2);
        simplified = sum.simplify();
        for (double x : xValues)
        {
            assertEquals(Math.sin(2 * x * x + 3) + 5, sum.get(x), 0.0001, "cannot simplify this");
            assertEquals(Math.sin(2 * x * x + 3) + 5, simplified.get(x), 0.0001, "cannot simplify this");
        }
        assertNull(s1.mergeAdd(s2), "cannot simplify this");
        assertNull(s1.mergeMultiply(s2), "won't simplify this (but it can be simplified...)");
        s1 = new Sine(1, 2, 3);
        s2 = new Sine(1, 3, 3);
        assertNull(s1.mergeMultiply(s2), "cannot simplify product of sines of different omega");
        s2 = new Sine(chained, 1, 2, 3);
        assertNull(s1.mergeMultiply(s2), "cannot simplify product of sines where one is chained");
        s1 = new Sine(chained, 1, 2, -1);
        assertTrue(s1.toString().contains("-1"));
        s1 = new Sine(chained, 1, 2, 3);
        assertEquals(s1.hashCode(), s2.hashCode(), "should be same");
        assertTrue(s1.equals(s2), "should be same");
        s1 = new Sine(1, 2, -1);
        assertNotEquals(s1.hashCode(), s2.hashCode(), "hash code checks chained");
        assertFalse(s1.equals(s2), "equals checks chained");
        s1 = new Sine(chained, 2, 2, 3);
        assertNotEquals(s1.hashCode(), s2.hashCode(), "hash code checks amplitude");
        assertFalse(s1.equals(s2), "equals checks amplitude");
        s1 = new Sine(chained, 1, 3, 3);
        assertNotEquals(s1.hashCode(), s2.hashCode(), "hash code checks omega");
        assertFalse(s1.equals(s2), "equals checks omega");
        s1 = new Sine(chained, 1, 2, 2);
        assertNotEquals(s1.hashCode(), s2.hashCode(), "hash code checks shift");
        assertFalse(s1.equals(s2), "equals checks shift");
        assertFalse(s1.equals(null), "not equal to null");

        s1 = Sine.cosine(2, 1, 0);
        assertTrue(s1.toString().contains("cos("));
        assertFalse(s1.toString().contains("-"));
        s1 = Sine.cosine(2, 1, Math.PI);
        assertTrue(s1.toString().contains("cos("));
        assertTrue(s1.toString().contains("-"));
        s1 = new Sine(2, 1, Math.PI);
        assertTrue(s1.toString().contains("sin("));
        assertTrue(s1.toString().contains("-"));
        s1 = new Sine(-2, 1, 0);
        assertTrue(s1.toString().contains("sin("));
        assertTrue(s1.toString().contains("-"));

        s1 = new Sine(chained, 1, 2, 3);
        s2 = new Sine(chained, 1, 2, 4);
        assertNotNull(s1.mergeMultiply(s2), "can mergeMultiply these");
        assertNotNull(s1.mergeAdd(s2), "can mergAdd these");
        s2 = new Sine(1, 2, 4);
        assertNull(s1.mergeMultiply(s2), "cannot mergeMultiply these");
        assertNull(s1.mergeAdd(s2), "cannot mergeAdd these");
        assertNull(s2.mergeMultiply(s1), "cannot mergeMultiply these");
        assertNull(s2.mergeAdd(s1), "cannot mergeAdd these");
        s2 = new Sine(chained, 1, 3, 4);
        assertNull(s1.mergeAdd(s2), "cannot mergeMultiply these, yet");

        s1 = new Sine(new Constant(3), 3, 2, 1);
        simplified = s1.simplify();
        assertEquals(new Constant(3 * Math.sin(2 * 3 + 1)), simplified, "should simplify to constant");

        s1 = Sine.cosine(2, 1, 0);
        assertEquals(KnotReport.NONE, s1.getKnotReport(new Interval<String>(10, true, 20, false, "string")),
                "sine has no knots");
        assertEquals(0, s1.getKnots(new Interval<String>(10, true, 20, false, "string")).size(), "zero knots");
        assertEquals(KnotReport.KNOWN_INFINITE,
                new Sine(new Logarithm(), 2, 3, 4).getKnotReport(new Interval<String>(-1, true, 1, true, "string")),
                "should be infinite");
        try
        {
            new Sine(new Logarithm(), 2, 3, 4).getKnots(new Interval<String>(-1, true, 1, true, "string"));
            fail("attempt to return infinitely many knots should have thrown an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
        assertEquals(KnotReport.NONE,
                new Sine(new Logarithm(), 2, 3, 4).getKnotReport(new Interval<String>(10, true, 20, true, "string")),
                "should be zero");
        assertEquals(0, new Sine(new Logarithm(), 2, 3, 4).getKnots(new Interval<String>(10, true, 20, true, "string")).size(),
                "zero knots");

    }
}
