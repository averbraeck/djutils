package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.junit.Test;

/**
 * The TimestampWeightedTallyTest test the weighted tally that receives observations with a timestamp.
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
public class TimestampWeightedTallyTest
{
    /** Test the TimestampWeightedTally on Number based timestamps. */
    @Test
    public void testTimestampWeightedTallyNumber()
    {
        String description = "THIS TIMESTAMP WEIGHTED TALLY IS TESTED";
        TimestampWeightedTally wt = new TimestampWeightedTally(description);

        // check the description
        assertEquals(description, wt.getDescription());
        assertTrue(wt.toString().contains(description));

        // now we check the initial values
        assertTrue(wt.isActive());
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStdDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        wt.ingest(0.0, 1.0);
        wt.ingest(0.1, 1.1);
        wt.ingest(0.2, 1.2);
        wt.ingest(0.3, 1.3);
        wt.ingest(0.4, 1.4);
        wt.ingest(0.5, 1.5);
        wt.ingest(0.6, 1.6);
        wt.ingest(0.7, 1.7);
        wt.ingest(0.8, 1.8);
        wt.ingest(0.9, 1.9);
        wt.ingest(1.0, 2.0);

        try
        {
            wt.ingest(0.8, 123.456);
            fail("timestamp out of order should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertTrue(wt.isActive());
        wt.endObservations(1.1);
        assertFalse(wt.isActive());

        // Now we check the TimestampWeightedTally
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

        // Adding something after the active period should not make a change
        wt.ingest(10.0, 20.0);
        assertFalse(wt.isActive());
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);

    }

    /** Test the TimestampWeightedTally on a simple example. */
    @Test
    public void testTimestampWeightedTallySimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        TimestampWeightedTally wt = new TimestampWeightedTally("simple TimestampWeightedTally statistic");
        wt.initialize();
        wt.ingest(0.0, 86.0);
        wt.ingest(13.0, 26.0);
        wt.ingest(36.0, 0.0);
        wt.endObservations(40.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we shift the times, we should get the same answers
        wt = new TimestampWeightedTally("simple TimestampWeightedTally statistic");
        wt.initialize();
        wt.ingest(10.0, 86.0);
        wt.ingest(23.0, 26.0);
        wt.ingest(46.0, 0.0);
        wt.endObservations(50.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we have observations with duration 0, we should get the same answers
        wt = new TimestampWeightedTally("simple TimestampWeightedTally statistic");
        wt.initialize();
        wt.ingest(0.0, 86.0);
        wt.ingest(13.0, 26.0);
        wt.ingest(13.0, 0.0);
        wt.ingest(13.0, 26.0);
        wt.ingest(36.0, 0.0);
        wt.ingest(36.0, 0.0);
        wt.endObservations(40.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN()); // non-zero values only

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new TimestampWeightedTally("NIST");
        wt.ingest(0, 2);
        wt.ingest(1, 3);
        wt.ingest(2, 5);
        wt.ingest(2, 7);
        wt.ingest(2, 11);
        wt.ingest(6, 13);
        wt.ingest(7, 17);
        wt.ingest(9, 19);
        wt.ingest(10, 23);
        wt.endObservations(10.0);

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, wt.getWeightedSampleStdDev(), 0.01);
    }

    /** Test the TimestampWeightedTally for Calendar-based timestamps. */
    @Test
    public void testTimestampWeightedTallyCalendar()
    {
        String description = "THIS TIMESTAMP WEIGHTED TALLY IS TESTED";
        TimestampWeightedTally wt = new TimestampWeightedTally(description);

        int index = 10;
        for (int second = 30; second <= 40; second++)
        {
            Calendar calendar = new Calendar.Builder().setDate(2000, 2, 2).setTimeOfDay(4, 12, second, 10).build();
            wt.ingest(calendar, index++);
        }
        assertTrue(wt.isActive());
        wt.endObservations(new Calendar.Builder().setDate(2000, 2, 2).setTimeOfDay(4, 12, 41, 10).build());
        assertFalse(wt.isActive());

        // Now we check the TimestampWeightedTally
        assertEquals(20.0, wt.getMax(), 1.0E-6);
        assertEquals(10.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 10000 * 11, wt.getWeightedSum(), 1.0E-2);
        assertEquals(15.0, wt.getWeightedSampleMean(), 1.0E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance += Math.pow(15.0 - (10 + i), 2);
        }
        variance = variance / 10.0; // n - 1
        double stDev = Math.sqrt(variance);

        assertEquals(variance, wt.getWeightedSampleVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedSampleStdDev(), 1.0E-6);
    }

}
