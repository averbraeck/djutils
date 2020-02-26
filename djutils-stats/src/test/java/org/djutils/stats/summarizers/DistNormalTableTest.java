package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test the DistNormalTable class.
 * </p>
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
            assertTrue("value are non-decreasing", prevValue <= value);
            prevValue = value;
        }
        assertEquals("last value is 1.0", 1.0, prevValue, 0.0);
    }

}
