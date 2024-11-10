package org.djutils.draw.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * ContinuousPiecewiseLinearTest.java.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class ContinuousPiecewiseLinearTest
{
    /**
     * Test the FractionalLengthData class.
     */
    @Test
    public void testContinuousPiecewiseLinearFunction()
    {
        testContinuousPiecewiseLinearFunctionConstructors("Zero values should have thrown an IllegalArgumentException");
        testContinuousPiecewiseLinearFunctionConstructors("Odd number of values should have thrown an IllegalArgumentException",
                0.1);
        testContinuousPiecewiseLinearFunctionConstructors("Odd number of values should have thrown an IllegalArgumentException",
                0.1, 5.0, 0.3);
        testContinuousPiecewiseLinearFunctionConstructors(
                "Negative fractional position should have thrown an IllegalArgumentException", -0.1, 2.0);
        testContinuousPiecewiseLinearFunctionConstructors(
                "Fractional position > 1.0 should have thrown an IllegalArgumentException", 1.1, 2.0);
        testContinuousPiecewiseLinearFunctionConstructors(
                "-0.0 fractional position should have thrown an IllegalArgumentException", -0.0, 2.0);
        testContinuousPiecewiseLinearFunctionConstructors("value must be finite", 0.2, Double.NaN);
        testContinuousPiecewiseLinearFunctionConstructors("value must be finite", 0.2, Double.NEGATIVE_INFINITY);
        testContinuousPiecewiseLinearFunctionConstructors("value must be finite", 0.2, Double.POSITIVE_INFINITY);

        try
        {
            new ContinuousPiecewiseLinearFunction((Map<Double, Double>) null);
            fail("null map should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        Map<Double, Double> map = new HashMap<>();
        map.put(null, 3.0);
        try
        {
            new ContinuousPiecewiseLinearFunction(map);
            fail("null key in map should have thrown an IllegalArgumentException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        ContinuousPiecewiseLinearFunction of = new ContinuousPiecewiseLinearFunction(0.1, 2, 0.7, 5);
        assertEquals(2, of.get(0.0), 0.000001, "get for data with key lower than first entry returns first value");
        assertEquals(5, of.get(1.0), 0.000001, "get for at first key higher than last entry returns first value");
        assertEquals(2, of.get(0.1), 0.000001, "get at first key returns first value");
        assertEquals(5, of.get(0.7), 0.000001, "get at last key returns first value");
        assertEquals(2 + (5 - 2) * (0.3 - 0.1) / (0.7 - 0.1), of.get(0.3), 0.00001, "Value between point interpolates");
        assertEquals(3 / 0.6, of.getDerivative(0.11), 0.00001, "get derivative works between the entries");
        assertEquals(3 / 0.6, of.getDerivative(0.69), 0.00001, "get derivative works between the entries");
        assertEquals(0, of.getDerivative(0.09), 0, "get derivative returns 0 outside the range of entries");
        assertEquals(0, of.getDerivative(0.71), 0, "get derivative returns 0 outside the range of entries");
        assertEquals(2, of.size(), "size is returned");

        of = new ContinuousPiecewiseLinearFunction(0.0, 0.0, 0.2, 1.0, 1.0, 2.0);
        int index = 0;
        for (ContinuousPiecewiseLinearFunction.TupleSt tuple : of)
        {
            assertEquals(index == 0 ? 0.0 : index == 1 ? 0.2 : 1.0, tuple.s(), 0.0, "domain point is returned");
            assertEquals(index == 0 ? 0.0 : index == 1 ? 1.0 : 1.0, tuple.t(), 2.0, "offset is returned");
            index++;
        }

        Iterator<ContinuousPiecewiseLinearFunction.TupleSt> iterator = of.iterator();
        assertTrue(iterator.hasNext(), "first offset is available");
        ContinuousPiecewiseLinearFunction.TupleSt tuple = iterator.next();
        assertEquals(0.0, tuple.s(), "first domain point is returned");
        assertEquals(0.0, tuple.t(), "first offset is returned");
        assertTrue(iterator.hasNext(), "second offset is available");
        tuple = iterator.next();
        assertEquals(0.2, tuple.s(), "second domain point is returned");
        assertEquals(1.0, tuple.t(), "second offset is returned");
        assertTrue(iterator.hasNext(), "third offset is available");
        tuple = iterator.next();
        assertEquals(1.0, tuple.s(), "third domain point is returned");
        assertEquals(2.0, tuple.t(), "third offset is returned");
        assertFalse(iterator.hasNext(), "iterator is now exhausted");
        try
        {
            iterator.next();
            fail("exhausted iterator should have thrown a NoSuchElementException");
        }
        catch (NoSuchElementException nsee)
        {
            // Ignore expected exception
        }

        of = ContinuousPiecewiseLinearFunction.of(0.3, 6);
        assertEquals(1, of.size(), "size matches");
    }

    /**
     * Test the various constructors of FractionalLengthData.
     * @param problem String; description of the problem with the input data that the test should detect
     * @param in double... input for the constructors
     */
    static void testContinuousPiecewiseLinearFunctionConstructors(final String problem, final double... in)
    {
        try
        {
            new ContinuousPiecewiseLinearFunction(in);
            fail(problem);
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        Map<Double, Double> map = new HashMap<>();
        for (int i = 0; i < in.length; i += 2)
        {
            map.put(in[i], i + 1 < in.length ? in[i + 1] : null);
        }
        if (in.length % 2 == 0)
        {
            try
            {
                new ContinuousPiecewiseLinearFunction(map);
                fail(problem);
            }
            catch (IllegalArgumentException iae)
            {
                // Ignore expected exception
            }
        }
        else
        {
            try
            {
                new ContinuousPiecewiseLinearFunction(map);
                fail(problem);
            }
            catch (NullPointerException npe)
            {
                // Ignore expected exception
            }
        }
        try
        {
            ContinuousPiecewiseLinearFunction.of(in);
            fail(problem);
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
    }

}
