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
        for (double x : new double[] {-99999, -Math.PI, 0, Math.E, 2e200})
        {
            assertEquals(12.34, sum.get(x), 0, "result is exact");
            Constant c2 = new Constant(-23.56);
            Sum sum2 = new Sum(c1, c2);
            assertEquals(12.34 - 23.56, sum2.get(x), 0.00001, "we can add constants");
            assertEquals(Constant.ZERO, sum2.getDerivative(), "derivative of constant is ZERO");
            Function f = new PowerFunction(3, 1);
            sum2 = new Sum(c1, c2, f);
            assertEquals(12.34 - 23.56 + 3 * x, sum2.get(x), Math.abs(x / 1e10), "we can add constants and varying values");
            assertEquals(3, sum2.getDerivative().get(x), 0, "derivative of slope 3 is 3");
            assertEquals(new Constant(3), sum2.getDerivative(), "derivative Sum is simplified to Constant");
            sum2 = new Sum(Constant.ZERO, c1, Constant.ZERO, c2, f, Constant.ZERO);
            assertEquals(12.34 - 23.56 + 3 * x, sum2.get(x), Math.abs(x / 1e10), "we can add constants and varying values");
            sum2 = new Sum(Constant.ZERO, Constant.ZERO);
            assertEquals(0, sum2.get(x), 0, "zeros work");
            sum2 = new Sum(f, f);
            assertEquals(f.get(x) + f.get(x), sum2.get(x), Math.abs(x / 1e10), "this one cannot be simplified by Sum");
            Function derivative = sum2.getDerivative();
            assertEquals(6, derivative.get(x), 0.000001, "non-simplified derivative is correct");
        }
        sum = new Sum(new Constant(-1), new Constant(2));
        assertEquals(1, sum.get(123), 0, "short circuited to Constant.ONE (but we can't really check that");
        
        assertEquals("Sum", sum.getId(), "id is \"Sum\"");
        assertTrue(sum.toString().startsWith("Sum ["), "toString method returns something descriptive");
    }
}
