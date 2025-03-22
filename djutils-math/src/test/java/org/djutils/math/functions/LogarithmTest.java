package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Logarithm class.
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
public class LogarithmTest
{

    /**
     * Test the Logarithm class.
     */
    @Test
    public void testLogarithm()
    {
        Logarithm l = new Logarithm();
        MathFunction derivative = l.getDerivative();
        Logarithm log10 = new Logarithm(10);
        MathFunction derivative10 = log10.getDerivative();
        MathFunction chained = new Power(2, 2);
        MathFunction logChained = new Logarithm(chained, 10);
        MathFunction derivativeChained = logChained.getDerivative();
        MathFunction lnChained = new Logarithm(chained);
        MathFunction scaledLnChained = lnChained.scaleBy(5);
        MathFunction merged = new Sum(l, log10).simplify();
        assertTrue(merged instanceof Logarithm, "should simplify to a single logarithm");
        MathFunction mergedChained = new Sum(logChained, lnChained).simplify();
        assertTrue(mergedChained instanceof Logarithm, "should simplify to a single logarithm");
        MathFunction sum = new Sum(logChained, l).simplify();
        assertFalse(sum instanceof Logarithm, "cannot simplify if chained differs");
        MathFunction sum2 = new Sum(l, new Sine(1, 2, 3)).simplify();
        double[] xValues = new double[] {0.01, 0.1, 0.5, 1, Math.E, Math.PI, 10, 100, 1000, 10000};
        for (double x : xValues)
        {
            assertEquals(Math.log(x), l.apply(x), 0.000001, "natural log is returned");
            assertEquals(Math.log10(x), log10.apply(x), 0.000001, "log10 is returned");
            assertEquals(1.0 / x, derivative.apply(x), 0.000001, "derivative of natural log is 1/x");
            assertEquals(1.0 / Math.log(10) / x, derivative10.apply(x), 0.000001, "derivative of log10 is 1/ln(10)/x");
            assertEquals(Math.log10(2 * x * x), logChained.apply(x), 0.00001, "chained works");
            assertEquals(2 / Math.log(10) / x, derivativeChained.apply(x), 0.00001, "chained derivative works");
            assertEquals(Math.log(2 * x * x), lnChained.apply(x), 0.000001, "chained natural logarithm");
            assertEquals(5 * Math.log(2 * x * x), scaledLnChained.apply(x), 0.000001, "scaled chained natural logarithm");
            assertEquals(Math.log(x) + Math.log10(x), merged.apply(x), 0.000001, "mergeAdd works");
            assertEquals(Math.log(2 * x * x) + Math.log10(2 * x * x), mergedChained.apply(x), 0.000001,
                    "mergeAdd works if chained is the same");
            assertEquals(Math.log10(2 * x * x) + Math.log(x), sum.apply(x), 0.00001, "sum is correct");
            assertEquals(Math.log(x) + Math.sin(2 * x + 3), sum2.apply(x), 0.00001, "sum is correct");
        }
        assertEquals(6, l.sortPriority(), "sort priority is 6");
        assertEquals(lnChained, lnChained.scaleBy(1.0), "scaling by 1.0 returns original");
        assertEquals(Constant.ZERO, lnChained.scaleBy(0.0), "scaling by 0.0 returns ZERO");

        try
        {
            l.compareWithinSubType(Constant.ONE);
            fail("compare within sub type should throw IllegalArgumentException for wrong type");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertTrue(l.compareWithinSubType(log10) > 0, "should sort after");
        assertTrue(log10.compareWithinSubType(l) < 0, "should sort before");
        assertEquals(0, l.compareWithinSubType(l), "same");

        l.mergeAdd(logChained);
        assertNull(l.mergeAdd(new Sine(1, 2, 3)), "cannot merge those");

        assertEquals("ln(x)", l.toString(), "prints without factor");
        assertNotEquals("ln(x)", log10.toString(), "prints different");
        assertTrue(log10.toString().endsWith("ln(x)"), "only start differs");
        assertTrue(l.hashCode() != log10.hashCode(), "hash code takes factor into account");
        assertFalse(l.equals(null), "not equal to null");
        assertTrue(l.equals(new Logarithm()), "equal to exactly similar Logarithm");
        assertFalse(l.equals(new Logarithm(new Sine(1, 2, 3))), "checks chained");
        assertFalse(new Logarithm(new Sine(1, 2, 3)).equals(l), "checks chained");
        assertFalse(l.equals(log10), "checks factor");
        assertTrue(logChained.toString().contains(chained.toString()), "toString includes chain");

        Logarithm inf = new Logarithm(Double.POSITIVE_INFINITY);
        assertEquals(Constant.ZERO, inf.simplify(), "should simplify to ZERO");

        l = new Logarithm(new Constant(2));
        MathFunction simplified = l.simplify();
        assertEquals(new Constant(Math.log(2)), simplified, "should simplify to a constant");

        l = new Logarithm();
        assertEquals(KnotReport.NONE, l.getKnotReport(new Interval<String>(10, true, 20, true, "string")), "has no knots");
        assertEquals(0, l.getKnots(new Interval<String>(10, true, 20, true, "string")).size(), "zero knots");

        assertEquals(KnotReport.KNOWN_FINITE, l.getKnotReport(new Interval<String>(0, true, 20, true, "string")), "knot at 0");
        assertEquals(1, l.getKnots(new Interval<String>(0, true, 20, true, "string")).size(), "one knot");
        assertEquals(0.0, l.getKnots(new Interval<String>(0, true, 20, true, "string")).first(), "one knot at 0.0");

        assertEquals(KnotReport.KNOWN_INFINITE, l.getKnotReport(new Interval<String>(-1, true, 20, true, "string")),
                "infinite");
        try
        {
            l.getKnots(new Interval<String>(-1, true, 20, true, "string"));
            fail("attempt to collect infinitely many knots should fail with an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }

        l = new Logarithm(new Sine(1, 2, 3));
        assertEquals(KnotReport.UNKNOWN, l.getKnotReport(new Interval<String>(10, true, 20, true, "string")),
                "can't determine that");
        try
        {
            l.getKnots(new Interval<String>(10, true, 20, true, "string"));
            fail("attempt to collect uncollectable knots should throw an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }

        
    }
}
