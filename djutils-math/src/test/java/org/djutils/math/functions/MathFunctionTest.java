package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.SortedSet;
import java.util.TreeSet;

import org.djutils.math.functions.MathFunction.TupleSt;
import org.junit.jupiter.api.Test;

/**
 * Test the Function interface
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
public class MathFunctionTest
{
    /**
     * Test the printValue method in the Function interface.
     */
    @Test
    public void printValueTest()
    {
        for (double v : new double[] {Long.MIN_VALUE - 0.1, Long.MIN_VALUE, -123456.789, -Math.PI, -1e-99, 0, 1.23e-200, Math.E,
                Long.MAX_VALUE, Long.MAX_VALUE + 0.1, 123e200})
        {
            String result = printValue(v);
            // System.out.println(v + " -> " + result);
            assertEquals(v, Double.parseDouble(result), Math.abs(v) / 1e10, "String represents the value");
            if (v % 1.0 != 0)
            {
                assertTrue(result.contains("."), "result is printed with a decimal point");
            }
            if (v >= Long.MIN_VALUE && v <= Long.MAX_VALUE && v % 1.0 == 0.0)
            {
                assertFalse(result.contains("."), "integer (within range) result is printed without a decimal point");
            }
        }
        // Nan does not have a simplify method, so we can use it to test the default implementation
        MathFunction simplifiedNan = Nan.NAN.simplify();
        assertTrue(simplifiedNan == Nan.NAN, "the're the same object");
    }

    /**
     * Construct a Constant with the specified value and return the result of the printValue method.
     * @param value the value to embed in the Constant
     * @return the result of the printValue method
     */
    private String printValue(final double value)
    {
        return new Constant(value).printValue(value);
    }

    /**
     * Test the default methods in MathFunction.
     */
    @Test
    public void testDefaultMethods()
    {
        SubClass subClass = new SubClass();
        assertNull(subClass.mergeAdd(Constant.ONE), "default mergeAdd returns null");
        assertNull(subClass.mergeMultiply(Constant.ONE), "default mergeMultiply returns null");
        assertNull(subClass.mergeDivide(Constant.ONE), "default mergeDivide returns null");
        assertEquals(1.0, subClass.getScale(), "default getScale returns 1");

        assertTrue(subClass.compareChains(null, Constant.ONE) > 0);
        assertTrue(subClass.compareChains(Constant.ONE, null) < 0);
        assertEquals(0, subClass.compareChains(null, null));
        assertEquals(0, subClass.compareChains(Constant.ONE, Constant.ONE));
        assertTrue(subClass.compareChains(Constant.ONE, Constant.ZERO) > 0);
    }

    /**
     * Test the TupleSt record in the Function interface.
     */
    @Test
    public void tupleStTest()
    {
        for (double s : new double[] {-1e15, -Math.PI, 0, 2, Math.E})
        {
            for (double t : new double[] {-123456, -5, 0, 1, 99999})
            {
                TupleSt tuple = new TupleSt(s, t);
                assertEquals(s, tuple.s(), "s can be retrieved");
                assertEquals(t, tuple.t(), "t can be retrieved");
            }
        }
    }

    /**
     * Sub class to verify the default methods in MathFunction.
     */
    class SubClass implements MathFunction
    {

        /** {@inheritDoc} */
        @Override
        public double get(final double fraction)
        {
            return 0;
        }

        /** {@inheritDoc} */
        @Override
        public MathFunction getDerivative()
        {
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public MathFunction scaleBy(final double factor)
        {
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public int sortPriority()
        {
            return 0;
        }

        /** {@inheritDoc} */
        @Override
        public int compareWithinSubType(final MathFunction other)
        {
            return 0;
        }

        /** {@inheritDoc} */
        @Override
        public KnotReport getKnotReport(final Interval<?> interval)
        {
            return KnotReport.NONE;
        }

        @Override
        public SortedSet<Double> getKnots(final Interval<?> interval)
        {
            return new TreeSet<Double>();
        }

    }

}
