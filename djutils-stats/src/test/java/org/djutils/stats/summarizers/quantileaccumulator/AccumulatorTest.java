package org.djutils.stats.summarizers.quantileaccumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.stats.summarizers.Tally;
import org.junit.Test;

/**
 * AccumulatorTest tests the quantile accumulators for the right exceptions. <br>
 * <br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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

    /**
     * Test the getCumulativeProbability method of the FullStorageAccumulator.
     */
    @Test
    public void testFullStorageAccumulator()
    {
        FullStorageAccumulator fsa = new FullStorageAccumulator();
        assertTrue("getCumulativeProbability with no data returns NaN", Double.isNaN(fsa.getCumulativeProbability(null, 12.3)));
        // Ingest one value
        fsa.register(10.0);
        assertEquals("below single value returns 0", 0.0, fsa.getCumulativeProbability(null, 9.0), 0);
        assertEquals("at single value returns 0", 0.5, fsa.getCumulativeProbability(null, 10.0), 0);
        assertEquals("above single value returns 0", 1.0, fsa.getCumulativeProbability(null, 20.0), 0);
        fsa.register(10.0);
        assertEquals("below range value returns 0", 0.0, fsa.getCumulativeProbability(null, 9.0), 0);
        assertEquals("at single value returns 0", 0.5, fsa.getCumulativeProbability(null, 10.0), 0);
        assertEquals("above range value returns 0", 1.0, fsa.getCumulativeProbability(null, 20.0), 0);
        fsa.register(10.0);
        assertEquals("below range value returns 0", 0.0, fsa.getCumulativeProbability(null, 9.0), 0);
        assertEquals("at single value returns 0", 0.5, fsa.getCumulativeProbability(null, 10.0), 0);
        assertEquals("above range value returns 0", 1.0, fsa.getCumulativeProbability(null, 20.0), 0);
        fsa.register(8.0);
        assertEquals("below range value returns 0", 0.0, fsa.getCumulativeProbability(null, 7.0), 0);
        assertEquals("at bottom range of length 4 value returns 0.125", 0.125, fsa.getCumulativeProbability(null, 8.0), 0);
        assertEquals("between 0 and 1 of length 4 returns 0.25", 0.25, fsa.getCumulativeProbability(null, 9.0), 0);
        assertEquals("in range of length 4 value returns 0.625", 0.625, fsa.getCumulativeProbability(null, 10.0), 0);
        assertEquals("above range value returns 0", 1.0, fsa.getCumulativeProbability(null, 20.0), 0);
        fsa.register(15.0);
        assertEquals("below range value returns 0", 0.0, fsa.getCumulativeProbability(null, 7.0), 0);
        assertEquals("at bottom range of length 5 value returns 0.1", 0.1, fsa.getCumulativeProbability(null, 8.0), 0.00001);
        assertEquals("between 0 and 1 of length 5 returns 0.2", 0.2, fsa.getCumulativeProbability(null, 9.0), 0.00001);
        assertEquals("in range of length 5 value returns 0.5", 0.5, fsa.getCumulativeProbability(null, 10.0), 0);
        assertEquals("between beforelast and last of length 5 value returns 0.8", 0.8, fsa.getCumulativeProbability(null, 13.0),
                0.00001);
        assertEquals("above range value returns 0", 1.0, fsa.getCumulativeProbability(null, 20.0), 0);
        fsa.initialize();
        for (int i = 0; i < 10; i++)
        {
            fsa.register(i + 10);
        }
        for (int i = 0; i < 30; i++)
        {
            double expect = 0;
            if (i >= 20)
            {
                expect = 1;
            }
            else if (i >= 10)
            {
                expect = (i - 10 + 0.5) / 10;
            }
            // System.out.println(String.format("i=%d, expect=%.3f, got=%.3f", i, expect, fsa.getCumulativeProbability(i)));
            assertEquals("p", expect, fsa.getCumulativeProbability(null, i), 0.0);
        }
    }

    /**
     * Test the getCumulativeProbability method of the FixedBinsAccumulator. This test does not cover cases where some of the
     * ingested values are out of range; as we have not decided how to compute cumulative probabilities for such cases.
     */
    @Test
    public void testFixedBinsAccumulator()
    {
        FixedBinsAccumulator fba = new FixedBinsAccumulator(5.0, 0.5, 5);
        // Bins are centered at 5.0, 5.5, 6.0, 6.5, 7.0
        assertTrue("getCumulativeProbability with no data returns NaN", Double.isNaN(fba.getCumulativeProbability(null, 12.3)));
        // Ingest one value
        fba.register(5.7); // should be counted in bin 5.5
        assertEquals("cumulative probability of value below any bin is 0.0", 0.0, fba.getCumulativeProbability(null, 2.2), 0);
        assertEquals("cumulative probability of value below any filled bin is 0.0", 0.0,
                fba.getCumulativeProbability(null, 5.2), 0);
        assertEquals("cumulatove probability of value exactly in center of the only filled bin is 0.5", 0.5,
                fba.getCumulativeProbability(null, 5.5), 0);
        assertEquals("cumulative probability of value above any filled bin is 1.0", 1.0,
                fba.getCumulativeProbability(null, 5.8), 0);
        assertEquals("cumulative probability of value above any bin is 1.0", 1.0, fba.getCumulativeProbability(null, 25.8), 0);
        for (int i = 0; i <= 10; i++)
        {
            double quantile = 5.25 + 0.5 * i / 10;
            double expect = 1.0 * i / 10;
            double got = fba.getCumulativeProbability(null, quantile);
            assertEquals("quantile at fraction in used bin", expect, got, 0.0001);
        }
        // Put some data in other buckets
        fba.register(6.1);
        fba.register(5.9);
        assertEquals("cumulative probability of value below any bin is 0.0", 0.0, fba.getCumulativeProbability(null, 2.2), 0);
        assertEquals("cumulative probability of value below any filled bin is 0.0", 0.0,
                fba.getCumulativeProbability(null, 5.2), 0);
        assertEquals("cumulatove probability of value exactly in center of bin 5.5 is 1/6", 1.0 / 6,
                fba.getCumulativeProbability(null, 5.5), 0.0001);
        assertEquals("cumulative probability of value above any filled bin is 1.0", 1.0,
                fba.getCumulativeProbability(null, 6.3), 0);
        assertEquals("cumulative probability of value above any bin is 1.0", 1.0, fba.getCumulativeProbability(null, 25.8), 0);
        // Step through the value range in small steps
        for (int i = 405; i < 800; i += 10)
        {
            double quantile = i / 100.0;
            double expect = 0.0;
            if (i > 625)
            {
                expect = 1.0;
            }
            else if (i > 575)
            {
                expect = 1.0 / 3 + 2 * (i - 575) / 50.0 / 3;
            }
            else if (i > 525)
            {
                expect = (i - 525) / 50.0 / 3;
            }
            double got = fba.getCumulativeProbability(null, quantile);
            // System.out.println(String.format("i=%d, quantile=%.3f, expect=%.3f, got=%.3f", i, quantile, expect, got));
            assertEquals("cumulative probability at quantile", expect, got, 0.0001);
        }
    }

    /**
     * Test the getCumulativeProbability method of the NoStorageAccumulator.
     */
    @Test
    public void testNoStorageAccumulator()
    {
        // This one can only be tested when cooperating with a Tally.
        Tally tally = new Tally("test Tally with NoStorageAccumulator");
        assertTrue("cumulative probability with no values ingested is NaN", Double.isNaN(tally.getCumulativeProbability(4)));
        tally.register(10);
        assertEquals("cumulative probability at single inserted value is 0.5", 0.5, tally.getCumulativeProbability(10), 0);
        assertEquals("cummulative probability below single inserted value is 0.0", 0.0,
                tally.getCumulativeProbability(10 - Math.ulp(10)), 0);
        assertEquals("cummulative probability above single inserted value is 1.0", 1.0,
                tally.getCumulativeProbability(10 + Math.ulp(10)), 0);
        tally.register(20);
        assertEquals("cumulative probability at mean is 0.5", 0.5, tally.getCumulativeProbability(15), 0);
        assertEquals("cumulative probability at mean -sigma is about 0.16", 0.16, tally.getCumulativeProbability(10), 0.01);
        assertEquals("cumulative probability at mean +sigma is about 0.84", 0.84, tally.getCumulativeProbability(20), 0.01);
    }

    /**
     * Test the getCumulativeProbability method of the TDigestAccumulator.
     */
    @Test
    public void testTDigestAccumulator()
    {
        TDigestAccumulator tda = new TDigestAccumulator();
        assertTrue("cumulative probability with no values ingested is NaN",
                Double.isNaN(tda.getCumulativeProbability(null, 4)));
        tda.register(10);
        assertEquals("cumulative probability at single inserted value is 0.5", 0.5, tda.getCumulativeProbability(null, 10), 0);
        assertEquals("cummulative probability below single inserted value is 0.0", 0.0,
                tda.getCumulativeProbability(null, 10 - Math.ulp(10)), 0);
        assertEquals("cummulative probability above single inserted value is 1.0", 1.0,
                tda.getCumulativeProbability(null, 10 + Math.ulp(10)), 0);
        tda.register(20);
        assertEquals("cumulative probability at mean is 0.5", 0.5, tda.getCumulativeProbability(null, 15), 0);
        assertEquals("cumulative probability at 0.25 of range is between 0 and 0.5 0.26", 0.25,
                tda.getCumulativeProbability(null, 12.5), 0.15);
        assertEquals("cumulative probability at 0.75 of range is between 0.5 and 1.0", 0.75,
                tda.getCumulativeProbability(null, 17.5), 0.15);
        assertEquals("cumulative probability at start of range is 0.0", 0.0, tda.getCumulativeProbability(null, 10), 0);
        assertEquals("cumulative probability at end of range is 1.0", 1.0, tda.getCumulativeProbability(null, 20), 0);
    }

}
