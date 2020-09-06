package org.djutils.draw.d2;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Transform2dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Transform2dTest
{
    /**
     * Test the matrix / vector multiplication.
     */
    @Test
    public void testMatrixMultiplication()
    {
        double[] mA = new double[] {1,2,3,4,5,6,7,8,9};
        double[] mB = new double[] {2,1,0,2,4,3,3,1,2};
        double[] mAmB = Transform2d.mulMatMat(mA, mB);
        double[] expected = new double[] {15, 12, 12, 36, 30, 27, 57, 48, 42};
        for (int i = 0; i < 9; i++)
        {
            if (mAmB[i] != expected[i])
            {
                fail(String.format("difference MA x MB at %d: expected %f, was: %f", i, expected[i], mAmB[i]));
            }
        }

        double[] m = new double[] {1, 4, 2, 5, 3, 1, 4, 2, 5};
        double[] v = new double[] {2, 5, 1};
        double[] mv = Transform2d.mulMatVec(m, v);
        double[] ev = new double[] {24, 26, 23};
        for (int i = 0; i < 3; i++)
        {
            if (mv[i] != ev[i])
            {
                fail(String.format("difference M x V at %d: expected %f, was: %f", i, ev[i], mv[i]));
            }
        }
        
        v = new double[] {1, 2};
        mv = Transform2d.mulMatVec2(m, v);
        ev = new double[] {11, 12};
        for (int i = 0; i < 2; i++)
        {
            if (mv[i] != ev[i])
            {
                fail(String.format("difference M x V3 at %d: expected %f, was: %f", i, ev[i], mv[i]));
            }
        }
    }
}

