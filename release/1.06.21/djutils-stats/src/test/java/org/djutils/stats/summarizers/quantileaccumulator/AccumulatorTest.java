package org.djutils.stats.summarizers.quantileaccumulator;

import static org.junit.Assert.fail;

import org.djutils.stats.summarizers.Tally;
import org.junit.Test;

/**
 * AccumulatorTest tests the quantile accumulators for the right exceptions. <br>
 * <br>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class AccumulatorTest
{
    /** Test the accumulators for null and wrong probabilities. */
    @Test
    public void testAccumulators()
    {
        testErrors(new NoStorageAccumulator());
        testErrors(new FullStorageAccumulator());
        testErrors(new TDigestAccumulator());
    }

    /**
     * test null and wrong probabilities on an accumulator.
     * @param acc QuantileAccumulator; the accumulator to test
     */
    private void testErrors(final QuantileAccumulator acc)
    {
        try
        {
            acc.getQuantile(null, 0.5);
            fail("null tally should have caused exception");
        }
        catch (Exception exception)
        {
            // ok
        }

        Tally tally = new Tally("test");
        try
        {
            acc.getQuantile(tally, -0.1);
            fail("negative probability should have caused exception");
        }
        catch (Exception exception)
        {
            // ok
        }

        try
        {
            acc.getQuantile(tally, 1.1);
            fail("probability greater than 1 should have caused exception");
        }
        catch (Exception exception)
        {
            // ok
        }
    }
}
