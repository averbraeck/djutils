package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Sum class.
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
public class SumTest
{
    /**
     * Test the Sum class.
     */
    @Test
    public void sumClassTest()
    {
        try
        {
            new Sum();
            fail("empty argument list should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        Constant c1 = new Constant(12.34);
        Sum sum = new Sum(c1);
        double[] xValues = new double[] {-99999, -Math.PI, 0, Math.E, 2e200};
        for (double x : xValues)
        {
            checkOneValue(12.34, x, sum);
            Constant c2 = new Constant(-23.56);
            Sum sum2 = new Sum(c1, c2);
            checkOneValue(12.34 - 23.56, x, sum2);
            assertEquals(Constant.ZERO, sum2.getDerivative(), "derivative of constant is ZERO");
            MathFunction f = new Power(3, 1);
            sum2 = new Sum(c1, c2, f);
            checkOneValue(12.34 - 23.56 + 3 * x, x, sum2);
            assertEquals(3, sum2.getDerivative().get(x), 0, "derivative of slope 3 is 3");
            assertEquals(new Constant(3), sum2.getDerivative(), "derivative Sum is simplified to Constant");
            sum2 = new Sum(Constant.ZERO, c1, Constant.ZERO, c2, f, Constant.ZERO);
            assertEquals(12.34 - 23.56 + 3 * x, sum2.get(x), Math.abs(x / 1e10), "we can add constants and varying values");
            sum2 = new Sum(Constant.ZERO, Constant.ZERO);
            assertEquals(0, sum2.get(x), 0, "zeros work");
            sum2 = new Sum(f, f);
            assertEquals(f.get(x) + f.get(x), sum2.get(x), Math.abs(x / 1e10), "this one can now be simplified by Sum");
            MathFunction derivative = sum2.getDerivative();
            checkOneValue(6, x, derivative);
            assertTrue(derivative instanceof Constant);
            checkOneValue(3 * x, x, f);
        }
        sum = new Sum(new Constant(-1), new Constant(2));
        assertEquals(1, sum.get(123), 0, "short circuited to Constant.ONE (but we can't really check that");
        MathFunction simplified = sum.simplify(); // now it gets short circuited to Constant.ONE
        assertEquals(Constant.ONE, simplified, "should now be short circuited to Constant.ONE");

        sum = new Sum(new Constant(2), new Constant(3), new Sine(1, 2, 3), new Power(2, 3));
        for (double x : xValues)
        {
            checkOneValue(5 + Math.sin(2 * x + 3) + 2 * x * x * x, x, sum);
        }
        sum = new Sum(sum, new Constant(10));
        for (double x : xValues)
        {
            checkOneValue(5 + Math.sin(2 * x + 3) + 2 * x * x * x + 10, x, sum);
        }
        assertEquals(sum, sum.scaleBy(1.0), "scale by 1 is the identity operation");
        assertEquals(Constant.ZERO, sum.scaleBy(0.0), "scale by 0 return ZERO");
        MathFunction mf = sum.scaleBy(2);
        for (double x : xValues)
        {
            checkOneValue(2 * (5 + Math.sin(2 * x + 3) + 2 * x * x * x + 10), x, mf);
        }
        try
        {
            sum.compareWithinSubType(Constant.ONE);
            fail("compareWithinSubType with wrong type should throw an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }
        assertEquals(101, sum.sortPriority(), 0, "sorting priority of Sum is 101");
        assertTrue(sum.equals(sum), "equal to itself");
        assertFalse(sum.equals(null), "not equal to null");
        assertFalse(sum.equals(mf), "not equal to another MathFunction");
        assertFalse(sum.equals("Not a Sum"), "not equal to an unrelated object");
        sum = new Sum(new Constant(2), new Power(3, 4));
        Sum sum2 = new Sum(new Constant(2), new Power(3, 4));
        assertTrue(sum.equals(sum2), "equal to another Sum with same terms");
        assertEquals(0, sum.compareWithinSubType(sum2), "should compare 0 with any other Sum");
        assertEquals(sum.hashCode(), sum2.hashCode(), "hash code should be same");
        sum2 = new Sum(new Constant(3), new Power(3, 4));
        assertNotEquals(sum.hashCode(), sum2.hashCode(), "hash code takes terms into account");
        Product p = new Product(Constant.ONE);
        sum = new Sum(p);
        mf = sum.simplify();
        assertEquals(Constant.ONE, mf, "should be reduced to constant ONE");
        sum = new Sum(new Constant(2), new Power(2, 3), new Sine(1, 2, 3));
        sum2 = new Sum(new Constant(2), new Power(2, 3));
        assertTrue(sum.compareTo(sum2) > 0, "shortest goes first");
        assertTrue(sum2.compareTo(sum) < 0, "shortest goes first");
    }

    /**
     * Check that the result is as expected.
     * @param expectedResult the expected result
     * @param x the value of x to put in
     * @param f the Function to use to convert x to the actual result
     */
    public void checkOneValue(final double expectedResult, final double x, final MathFunction f)
    {
        double actualResult = f.get(x);
        assertEquals(expectedResult, actualResult, Math.abs(expectedResult) / 1e10, "verifying f(x)");
    }

}
