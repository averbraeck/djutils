package org.djutils.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the DistNormalTable class. <br>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class DistNormalTableTest
{

    /**
     * Test that the values in the CUMULATIVE_NORMAL_PROBABILITIES table are non-decreasing and range from 0.5 to 1.0. This test
     * should catch accidental edits and typos.
     */
    @Test
    public void testSmoothness()
    {
        double prevValue = 0.5;
        for (double value : DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES)
        {
            assertTrue(prevValue <= value, "value are non-decreasing");
            prevValue = value;
        }
        assertEquals(1.0, prevValue, 0.0, "last value is 1.0");
    }

    /**
     * Test the cumulative normal probabilities based on well-known values. Values from
     * https://en.wikipedia.org/wiki/68%E2%80%9395%E2%80%9399.7_rule
     */
    @Test
    public void testCumulativeNormalProbabilities()
    {
        assertEquals(0.5, DistNormalTable.getCumulativeProbability(0.0, 1.0, 0.0), 0.001);
        assertEquals(0.5 + 0.682689492137086 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, 1.0), 1E-6); // 1 sigma
        assertEquals(0.5 + 0.954499736103642 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, 2.0), 1E-6); // 2 sigma
        assertEquals(0.5 + 0.997300203936740 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, 3.0), 1E-6); // 3 sigma
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, 100.0), 0.001);
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, 10000.0), 0.001);
        assertEquals(0.5 - 0.682689492137086 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, -1.0), 1E-6); // -1 sigma
        assertEquals(0.5 - 0.954499736103642 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, -2.0), 1E-6); // -2 sigma
        assertEquals(0.5 - 0.997300203936740 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, -3.0), 1E-6); // -3 sigma
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, -100.0), 0.001);
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(0.0, 1.0, -10000.0), 0.001);

        assertEquals(0.5, DistNormalTable.getCumulativeProbability(0.0, 3.0, 0.0), 0.001);
        assertEquals(0.5 + 0.682689492137086 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, 3.0), 1E-6); // 1 sigma
        assertEquals(0.5 + 0.954499736103642 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, 6.0), 1E-6); // 2 sigma
        assertEquals(0.5 + 0.997300203936740 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, 9.0), 1E-6); // 3 sigma
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, 100.0), 0.001);
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, 10000.0), 0.001);
        assertEquals(0.5 - 0.682689492137086 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, -3.0), 1E-6); // -1 sigma
        assertEquals(0.5 - 0.954499736103642 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, -6.0), 1E-6); // -2 sigma
        assertEquals(0.5 - 0.997300203936740 / 2.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, -9.0), 1E-6); // -3 sigma
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, -100.0), 0.001);
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(0.0, 3.0, -10000.0), 0.001);

        assertEquals(0.5, DistNormalTable.getCumulativeProbability(6.0, 2.0, 6.0), 0.001);
        assertEquals(0.5 + 0.682689492137086 / 2.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 8.0), 1E-6); // 1 sigma
        assertEquals(0.5 + 0.954499736103642 / 2.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 10.0), 1E-6); // 2 sigma
        assertEquals(0.5 + 0.997300203936740 / 2.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 12.0), 1E-6); // 3 sigma
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 100.0), 0.001);
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 10000.0), 0.001);
        assertEquals(0.5 - 0.682689492137086 / 2.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 4.0), 1E-6); // -1 sigma
        assertEquals(0.5 - 0.954499736103642 / 2.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 2.0), 1E-6); // -2 sigma
        assertEquals(0.5 - 0.997300203936740 / 2.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, 0.0), 1E-6); // -3 sigma
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, -100.0), 0.001);
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(6.0, 2.0, -10000.0), 0.001);

        // test negative sigma
        try
        {
            DistNormalTable.getCumulativeProbability(0.0, -1.0, 0.0);
            fail("negative sigma should have thrown IllegalArgumentException");
        }
        catch (Exception exception)
        {
            assertTrue(exception instanceof IllegalArgumentException);
        }

        assertEquals(0.5, DistNormalTable.getCumulativeProbability(1.0, 0, 1.0), 0);
        assertEquals(0.0, DistNormalTable.getCumulativeProbability(1.0, 0, 1.0 - Math.ulp(1.0)), 0);
        assertEquals(1.0, DistNormalTable.getCumulativeProbability(1.0, 0, 1.0 + Math.ulp(1.0)), 0);
    }

    /**
     * Test the inverse cumulative normal probabilities based on well-known values. Values from
     * https://en.wikipedia.org/wiki/68%E2%80%9395%E2%80%9399.7_rule
     */
    @Test
    public void testInverseCumulativeNormalProbabilities()
    {
        assertEquals(0.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5), 1E-6);
        assertEquals(1.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5 + 0.682689492137086 / 2.0), 1E-6);
        assertEquals(2.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5 + 0.954499736103642 / 2.0), 1E-6);
        assertEquals(3.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5 + 0.997300203936740 / 2.0), 1E-6);
        assertTrue(DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 1.0) > 5.0);
        assertEquals(-1.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5 - 0.682689492137086 / 2.0), 1E-6);
        assertEquals(-2.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5 - 0.954499736103642 / 2.0), 1E-6);
        assertEquals(-3.0, DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.5 - 0.997300203936740 / 2.0), 1E-6);
        assertTrue(DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 0.0) < -5.0);

        assertEquals(0.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5), 1E-6);
        assertEquals(2.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5 + 0.682689492137086 / 2.0), 1E-6);
        assertEquals(4.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5 + 0.954499736103642 / 2.0), 1E-6);
        assertEquals(6.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5 + 0.997300203936740 / 2.0), 1E-6);
        assertTrue(DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 1.0) > 10.0);
        assertEquals(-2.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5 - 0.682689492137086 / 2.0), 1E-6);
        assertEquals(-4.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5 - 0.954499736103642 / 2.0), 1E-6);
        assertEquals(-6.0, DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.5 - 0.997300203936740 / 2.0), 1E-6);
        assertTrue(DistNormalTable.getInverseCumulativeProbability(0.0, 2.0, 0.0) < -10.0);

        assertEquals(6.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5), 1E-6);
        assertEquals(8.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5 + 0.682689492137086 / 2.0), 1E-6);
        assertEquals(10.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5 + 0.954499736103642 / 2.0), 1E-6);
        assertEquals(12.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5 + 0.997300203936740 / 2.0), 1E-6);
        assertTrue(DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 1.0) > 16.0);
        assertEquals(4.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5 - 0.682689492137086 / 2.0), 1E-6);
        assertEquals(2.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5 - 0.954499736103642 / 2.0), 1E-6);
        assertEquals(0.0, DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.5 - 0.997300203936740 / 2.0), 1E-6);
        assertTrue(DistNormalTable.getInverseCumulativeProbability(6.0, 2.0, 0.0) < -4.0);

        // test negative sigma
        try
        {
            DistNormalTable.getInverseCumulativeProbability(0.0, -1.0, 0.5);
            fail("negative sigma should have thrown IllegalArgumentException");
        }
        catch (Exception exception)
        {
            assertTrue(exception instanceof IllegalArgumentException);
        }

        // test cumulative probability out of bounds
        try
        {
            DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, -0.1);
            fail("negative cumulative probability should have thrown IllegalArgumentException");
        }
        catch (Exception exception)
        {
            assertTrue(exception instanceof IllegalArgumentException);
        }

        try
        {
            DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, 1.1);
            fail("cumulative probability > 1 should have thrown IllegalArgumentException");
        }
        catch (Exception exception)
        {
            assertTrue(exception instanceof IllegalArgumentException);
        }

    }

}
