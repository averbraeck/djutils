package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
import org.junit.Test;

/**
 * The PersistentTest test the persistent
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class WeightedTallyTest
{
    /** Test the persistent. */
    @Test
    public void testPersistent()
    {
        String description = "THIS PERSISTENT IS TESTED";
        WeightedTally wt = new WeightedTally(description);
        assertEquals(description, wt.getDescription());

        // check the description
        assertTrue(wt.toString().equals(description));

        // now we check the initial values
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStdDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        // We first fire a wrong event
        try
        {
            wt.notify(new Event(null, "ERROR", "ERROR"));
            fail("persistent should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        wt.ingest(0.1, 1.1);
        wt.ingest(0.1, 1.2);
        wt.ingest(0.1, 1.3);
        wt.ingest(0.1, 1.4);
        wt.ingest(0.1, 1.5);
        wt.ingest(0.1, 1.6);
        wt.ingest(0.1, 1.7);
        wt.ingest(0.1, 1.8);
        wt.ingest(0.1, 1.9);
        wt.ingest(0.1, 2.0);
        wt.ingest(0.1, 1.0);

        // Now we check the WeightedTally
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance += Math.pow(1.5 - (1.0 + i / 10.0), 2);
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, wt.getWeightedSampleVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedSampleStdDev(), 1.0E-6);

        try
        {
            wt.ingest(-0.1, 123.456);
            fail("negative weight should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
    }

    /** Test the WeightedTally on a simple example. */
    @Test
    public void testWeightedTallySimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        WeightedTally wt = new WeightedTally("simple WeightedTally statistic");
        wt.initialize();
        wt.ingest(13.0, 86.0);
        wt.ingest(23.0, 26.0);
        wt.ingest(4.0, 0.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);

        // When we shift the times, we should get the same answers
        wt = new WeightedTally("simple WeightedTally statistic");
        wt.initialize();
        wt.ingest(13.0, 86.0);
        wt.ingest(23.0, 26.0);
        wt.ingest(14.0, 0.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(34.32, wt.getWeightedSampleMean(), 0.001);

        // When we have observations with duration 0, we should get the same answers
        wt = new WeightedTally("simple WeightedTally statistic");
        wt.initialize();
        wt.ingest(13.0, 86.0);
        wt.ingest(0.0, 86.0);
        wt.ingest(23.0, 26.0);
        wt.ingest(4.0, 0.0);
        wt.ingest(0.0, 0.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new WeightedTally("NIST");
        wt.ingest(1, 2);
        wt.ingest(1, 3);
        wt.ingest(0, 5);
        wt.ingest(0, 7);
        wt.ingest(4, 11);
        wt.ingest(1, 13);
        wt.ingest(2, 17);
        wt.ingest(1, 19);
        wt.ingest(0, 23);

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, wt.getWeightedSampleStdDev(), 0.01);
    }
}
