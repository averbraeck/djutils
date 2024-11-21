package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            Function f = new PowerFunction(3, 1);
            sum2 = new Sum(c1, c2, f);
            checkOneValue(12.34 - 23.56 + 3 * x, x, sum2);
            assertEquals(3, sum2.getDerivative().get(x), 0, "derivative of slope 3 is 3");
            assertEquals(new Constant(3), sum2.getDerivative(), "derivative Sum is simplified to Constant");
            sum2 = new Sum(Constant.ZERO, c1, Constant.ZERO, c2, f, Constant.ZERO);
            assertEquals(12.34 - 23.56 + 3 * x, sum2.get(x), Math.abs(x / 1e10), "we can add constants and varying values");
            sum2 = new Sum(Constant.ZERO, Constant.ZERO);
            assertEquals(0, sum2.get(x), 0, "zeros work");
            sum2 = new Sum(f, f);
            assertEquals(f.get(x) + f.get(x), sum2.get(x), Math.abs(x / 1e10), "this one cannot be simplified by Sum");
            assertEquals(sum2, sum2.simplify());
            Function derivative = sum2.getDerivative();
            checkOneValue(6, x, derivative);
            assertTrue(derivative instanceof Constant);
            checkOneValue(3 * x, x, f);
        }
        sum = new Sum(new Constant(-1), new Constant(2));
        assertEquals(1, sum.get(123), 0, "short circuited to Constant.ONE (but we can't really check that");
        Function simplified = sum.simplify(); // now it gets short circuited to Constant.ONE
        assertEquals(Constant.ONE, simplified, "should now be short circuited to Constant.ONE");

        assertEquals("Sum", sum.getId(), "id is \"Sum\"");
        assertTrue(sum.toString().startsWith("Sum ["), "toString method returns something descriptive");

        sum = new Sum(new Constant(2), new Constant(3), new Sine(1, 2, 3), new PowerFunction(2, 3));
        System.out.println(sum.getDescription());
        for (double x : xValues)
        {
            checkOneValue(5 + Math.sin(2 * x + 3) + 2 * x * x * x, x, sum);
        }
    }

    /**
     * Check that the result is as expected.
     * @param expectedResult the expected result
     * @param x the value of x to put in
     * @param f the Function to use to convert x to the actual result
     */
    public void checkOneValue(final double expectedResult, final double x, final Function f)
    {
        double actualResult = f.get(x);
        assertEquals(expectedResult, actualResult, Math.abs(expectedResult) / 1e10, "verifying f(x)");
    }

}
