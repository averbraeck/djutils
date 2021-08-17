package org.djutils.float128;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Float128Test tests the basic functions of the Float128 quadruple precision floating point variable. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Float128Test
{
    /**
     * Test the construction of the Float128 and conversion to and from doubles.
     */
    @Test
    public void testConstruction()
    {
        for (double d : new double[] {0.0, 1.0, 2.0, 3.0, 10.0, 1.0E12, 0.5, 0.25, 0.1, 0.3, 1.0E-12, 1.0 / 3.0, Math.PI})
        {
            Float128 f = new Float128(d);
            assertEquals(d, f.doubleValue(), 0.0);
        }
        for (double d : new double[] {-0.0, -1.0, -2.0, -3.0, -10.0, -1.0E12, -0.5, -0.25, -0.1, -0.3, -1.0E-12, -1.0 / 3.0,
                -Math.PI})
        {
            Float128 f = new Float128(d);
            assertEquals(d, f.doubleValue(), 0.0);
        }
        for (double d : new double[] {Double.NaN, Double.MAX_VALUE, -Double.MAX_VALUE, Double.MIN_NORMAL, -Double.MIN_NORMAL,
                /* Double.MIN_VALUE, -Double.MIN_VALUE, */ Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
        {
            Float128 f = new Float128(d);
            assertEquals(d, f.doubleValue(), 0.0);
        }
    }

    /**
     * Test the plus operator of the Float128 and conversion to and from doubles.
     */
    @Test
    public void testPlus()
    {
        for (double x = 10.0; x < 1000; x += 3.0)
        {
            for (double y = 100.0; y < 1000; y += 7.0)
            {
                Float128 fx = new Float128(x);
                Float128 fy = new Float128(y);
                Float128 fs1 = fx.plus(fy);
                Float128 fs2 = fy.plus(fx);
                assertTrue("x+y != y+x: " + x + " + " + y, fs1.equals(fs2));
                assertEquals("fx+fy != x+y: " + x + " + " + y, x + y, fs1.doubleValue(), Math.ulp(x + y));
            }
        }

        for (double x = 10.0; x < 1000; x += 10. / 3.)
        {
            for (double y = 100.0; y < 1000; y += 3.1)
            {
                Float128 fx = new Float128(x);
                Float128 fy = new Float128(y);
                Float128 fs1 = fx.plus(fy);
                Float128 fs2 = fy.plus(fx);
                assertTrue("x+y != y+x: " + x + " + " + y, fs1.equals(fs2));
                assertEquals("fx+fy != x+y: " + x + " + " + y, x + y, fs1.doubleValue(), Math.ulp(x + y));
            }
        }

    }

}
