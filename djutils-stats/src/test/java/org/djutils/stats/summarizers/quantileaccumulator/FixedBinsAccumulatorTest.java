package org.djutils.stats.summarizers.quantileaccumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * FixedBinsAccumulatorTest.java. <br>
 * <br>
 * Copyright (c) 2021-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class FixedBinsAccumulatorTest
{
    /**
     * Test the FixedBinsAccumulator.
     */
    @Test
    public void testFixedBinsAccumulator()
    {
        try
        {
            new FixedBinsAccumulator(Double.NaN, 0.25, 100);
            fail("Illegal minimumBinCenter should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(Double.NEGATIVE_INFINITY, 0.25, 100);
            fail("Illegal minimumBinCenter should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(Double.POSITIVE_INFINITY, 0.25, 100);
            fail("Illegal minimumBinCenter should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(0.0, Double.NaN, 100);
            fail("Illegal binWidth should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(0.0, Double.POSITIVE_INFINITY, 100);
            fail("Illegal binWidth should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(0.0, Double.NEGATIVE_INFINITY, 100);
            fail("Illegal binWidth should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(0.0, 0.0, 100);
            fail("Illegal binWidth should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new FixedBinsAccumulator(0.0, 0.25, 0);
            fail("Illegal binCount should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        FixedBinsAccumulator fba = new FixedBinsAccumulator(10.0, 0.25, 100);
        assertEquals("binWidth", 0.25, fba.getBinWidth(), 0.0);
        assertEquals("bin count", 100, fba.getBinCount());
        assertEquals("total count", 0, fba.getN());
        assertEquals("below count", 0, fba.getBelowCount());
        assertEquals("above count", 0, fba.getAboveCount());
        assertTrue("quantile cannot be computed yet", Double.isNaN(fba.getQuantile(null, 0.5)));

        for (int bin = 0; bin < 100; bin++)
        {
            assertEquals("Bin center", 10.0 + 0.25 * bin, fba.getBinCenter(bin), 0.0001);
        }
        try
        {
            fba.getBinCenter(-1);
            fail("negative bin number should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            fba.getBinCenter(100);
            fail("Too large bin number should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        fba = new FixedBinsAccumulator(1, (Math.E - 1) / 1000, 1001);
        assertEquals("highest bin is near e", Math.E, fba.getBinCenter(fba.getBinCount() - 1), 0.00001);
        // ingest some values with an exponential density distribution
        int max = 1000000;
        for (int i = 0; i < max; i++)
        {
            double range0to1 = 1.0 * i / max;
            double value = Math.exp(range0to1); // Should be in range 0..e
            fba.register(value);
        }
        assertEquals("total values is max", max, fba.getN());
        assertEquals("no values below 1", 0, fba.getBelowCount());
        assertEquals("no values above e", 0, fba.getAboveCount());
        assertEquals("0% quantile", 1.0, fba.getQuantile(null, 0.0), 0.001);
        assertEquals("100% quantile", Math.E, fba.getQuantile(null, 1.0), Math.E / 1000);
        assertEquals("50% quantile", Math.sqrt(Math.E), fba.getQuantile(null, 0.5), Math.E / 1000);
        assertEquals("25% quantile", Math.sqrt(Math.sqrt(Math.E)), fba.getQuantile(null, 0.25), Math.E / 1000);
        
        // Check the below and above counters
        for (int i = 0; i < 5; i++)
        {
            fba.register(-i);
            assertEquals("below counter", i + 1, fba.getBelowCount());
            assertEquals("above counter", 0, fba.getAboveCount());
        }
        for (int i = 0; i < 5; i++)
        {
            fba.register(10);
            assertEquals("below counter", 5, fba.getBelowCount());
            assertEquals("above counter", i + 1, fba.getAboveCount());
        }
        
        try
        {
            fba.getQuantile(null, -0.001);
            fail("Negative probability should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            fba.getQuantile(null, 1.001);
            fail("Negative probability should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            fba.getQuantile(null, Double.POSITIVE_INFINITY);
            fail("Negative probability should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            fba.getQuantile(null, Double.NEGATIVE_INFINITY);
            fail("Negative probability should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            fba.getQuantile(null, Double.NaN);
            fail("Negative probability should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        fba.initialize();
        assertEquals("binWidth", (Math.E - 1) / 1001, fba.getBinWidth(), 0.00001);
        assertEquals("bin count", 1001, fba.getBinCount());
        assertEquals("total count", 0, fba.getN());
        assertEquals("below count", 0, fba.getBelowCount());
        assertEquals("above count", 0, fba.getAboveCount());
        assertTrue("quantile cannot be computed yet", Double.isNaN(fba.getQuantile(null, 0.5)));
        
        assertTrue("toString returns something descriptive", fba.toString().startsWith("FixedBinsAccumulator "));
    }

}
