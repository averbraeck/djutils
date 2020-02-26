package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
import org.djutils.event.TimedEvent;
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
public class PersistentTest
{
    /** Test the persistent. */
    @Test
    public void testPersistent()
    {
        String description = "THIS PERSISTENT IS TESTED";
        Persistent persistent = new Persistent(description);

        // check the description
        assertTrue(persistent.toString().equals(description));

        // now we check the initial values
        assertTrue(Double.isNaN(persistent.getMin()));
        assertTrue(Double.isNaN(persistent.getMax()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleMean()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(persistent.getWeightedStdDev()));
        assertEquals(0.0, persistent.getWeightedSum(), 0.0);
        assertEquals(0L, persistent.getN());

        // We first fire a wrong event
        try
        {
            persistent.notify(new Event(null, "ERROR", "ERROR"));
            fail("persistent should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.0, 0.0));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.1, 0.1));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.2, 0.2));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.3, 0.3));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.4, 0.4));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.5, 0.5));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.6, 0.6));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.7, 0.7));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.8, 0.8));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 1.9, 0.9));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 2.0, 1.0));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", 2.1, 1.1));
        }
        catch (Exception exception)
        {
            fail(exception.getMessage());
        }

        // Now we check the persistent
        assertEquals(2.1, persistent.getMax(), 1.0E-6);
        assertEquals(1.0, persistent.getMin(), 1.0E-6);
        assertEquals(12, persistent.getN());
        assertEquals(1.65, persistent.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, persistent.getWeightedSampleMean(), 1.0E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance += Math.pow(1.5 - (1.0 + i / 10.0), 2);
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, persistent.getWeightedSampleVariance(), 1.0E-6);
        assertEquals(stDev, persistent.getWeightedStdDev(), 1.0E-6);
    }

    /** Test the persistent on a simple example. */
    @Test
    public void testPersistentSimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        Persistent persistent = new Persistent("simple persistent statistic");
        persistent.initialize();
        persistent.ingest(0.0, 86.0);
        persistent.ingest(13.0, 26.0);
        persistent.ingest(36.0, 0.0);
        persistent.endObservations(40.0);

        assertEquals(1716.0, persistent.getWeightedSum(), 0.001);
        assertEquals(42.9, persistent.getWeightedSampleMean(), 0.001);

        // When we shift the times, we should get the same answers
        persistent = new Persistent("simple persistent statistic");
        persistent.initialize();
        persistent.ingest(10.0, 86.0);
        persistent.ingest(23.0, 26.0);
        persistent.ingest(46.0, 0.0);
        persistent.endObservations(50.0);

        assertEquals(1716.0, persistent.getWeightedSum(), 0.001);
        assertEquals(42.9, persistent.getWeightedSampleMean(), 0.001);

        // When we have observations with duration 0, we should get the same answers
        persistent = new Persistent("simple persistent statistic");
        persistent.initialize();
        persistent.ingest(10.0, 86.0);
        persistent.ingest(23.0, 26.0);
        persistent.ingest(23.0, 100.0);
        persistent.ingest(23.0, 26.0);
        persistent.ingest(46.0, 0.0);
        persistent.endObservations(50.0);

        assertEquals(1716.0, persistent.getWeightedSum(), 0.001);
        assertEquals(42.9, persistent.getWeightedSampleMean(), 0.001);

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        persistent = new Persistent("NIST");
        persistent.ingest(0, 2);
        persistent.ingest(1, 3);
        persistent.ingest(2, 5);
        persistent.ingest(2, 7);
        persistent.ingest(2, 11);
        persistent.ingest(6, 13);
        persistent.ingest(7, 17);
        persistent.ingest(9, 19);
        persistent.ingest(10, 23);
        persistent.endObservations(10.0);

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, persistent.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, persistent.getWeightedStdDev(), 0.01);
    }
}
