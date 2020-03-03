package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.djutils.event.Event;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;
import org.junit.Test;

/**
 * The EventBasedTimestampWeightedTallyTest test the weighted tally that receives event-observations with a timestamp.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class EventBasedTimestampWeightedTallyTest
{
    /** an event to fire. */
    private static final EventType VALUE_EVENT = new EventType("VALUE_EVENT");

    /** Test the EventBasedTimestampWeightedTally. */
    @Test
    public void testEventBasedTimestampWeightedTally()
    {
        String description = "THIS TIMESTAMP WEIGHTED TALLY IS TESTED";
        EventBasedTimestampWeightedTally wt = new EventBasedTimestampWeightedTally(description);

        // check the description
        assertEquals(description, wt.getDescription());
        assertTrue(wt.toString().contains(description));

        // now we check the initial values
        assertTrue(wt.isActive());
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        // We fire a wrong event with wrong content
        try
        {
            wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", "ERROR", 0.0));
            fail("tally should fail on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // We fire a wrong event with wrong timestamp
        try
        {
            wt.notify(new TimedEvent<Double>(VALUE_EVENT, "ERROR", 1.0, Double.NaN));
            fail("tally should fail on events.timestamp == NaN");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.0, 0.0));
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.1, 0.1));
        assertEquals(1.0, wt.getMin(), 0.000001);
        assertEquals(1.0, wt.getMax(), 0.000001);
        assertEquals(1.0, wt.getWeightedSampleMean(), 0.000001);
        assertEquals(1.0, wt.getWeightedMean(), 0.000001);
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        assertEquals(0, wt.getWeightedVariance(), 0.000001);
        assertEquals(0, wt.getWeightedStDev(), 0.0000001);
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.2, 0.2));
        assertFalse(Double.isNaN(wt.getWeightedSampleVariance()));
        assertFalse(Double.isNaN(wt.getWeightedSampleStDev()));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.3, 0.3));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.4, 0.4));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.5, 0.5));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.6, 0.6));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.7, 0.7));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.8, 0.8));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 1.9, 0.9));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 2.0, 1.0));

        try
        {
            wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 123.456, 0.8));
            fail("timestamp out of order should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertTrue(wt.isActive());
        wt.endObservations(1.1);
        assertFalse(wt.isActive());

        // Now we check the EventBasedTimestampWeightedTally
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);

        // Let's compute the standard deviation
        double varianceAccumulator = 0;
        for (int i = 0; i < 11; i++)
        {
            varianceAccumulator += Math.pow(1.5 - (1.0 + i / 10.0), 2);
        }
        double variance = varianceAccumulator / 10.0;
        double stDev = Math.sqrt(variance);
        assertEquals(variance, wt.getWeightedSampleVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedSampleStDev(), 1.0E-6);
        
        variance = varianceAccumulator / 11.0;
        stDev = Math.sqrt(variance);
        assertEquals(variance, wt.getWeightedVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedStDev(), 1.0E-6);

        // Adding something after the active period should not make a change
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 10.0, 20.0));
        assertFalse(wt.isActive());
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);
        
        // test some wrong events
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 123.456));
            fail("non time-based event should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new TimedEvent<String>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 123.456, "abc"));
            fail("non time-based evenevent with timestamp != Calendar or Number should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

    }

    /** Test the EventBasedTimestampWeightedTally on a simple example. */
    @Test
    public void testEventBasedTimestampWeightedTallySimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        EventBasedTimestampWeightedTally wt =
                new EventBasedTimestampWeightedTally("simple EventBasedTimestampWeightedTally statistic");
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 86.0, 0.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 26.0, 13.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 0.0, 36.0));
        wt.endObservations(40.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we shift the times, we should get the same answers
        wt = new EventBasedTimestampWeightedTally("simple EventBasedTimestampWeightedTally statistic");
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 86.0, 10.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 26.0, 23.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 0.0, 46.0));
        wt.endObservations(50.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we have observations with duration 0, we should get the same answers
        wt = new EventBasedTimestampWeightedTally("simple EventBasedTimestampWeightedTally statistic");
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 86.0, 0.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 26.0, 13.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 0.0, 13.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 26.0, 13.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 0.0, 36.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 0.0, 36.0));
        wt.endObservations(40.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN()); // non-zero values only

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new EventBasedTimestampWeightedTally("NIST");
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 2, 0.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 3, 1.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 5, 2.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 7, 2.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 11, 2.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 13, 6.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 17, 7.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 19, 9.0));
        wt.notify(new TimedEvent<Double>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", 23, 10.0));
        wt.endObservations(10.0);

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, wt.getWeightedSampleStDev(), 0.01);
    }

    /** Test the TimestampWeightedTally for Calendar-based timestamps. */
    @Test
    public void testEventBasedTimestampWeightedTallyCalendar()
    {
        String description = "THIS TIMESTAMP WEIGHTED TALLY IS TESTED";
        EventBasedTimestampWeightedTally wt = new EventBasedTimestampWeightedTally(description);

        int index = 10;
        for (int second = 30; second <= 40; second++)
        {
            Calendar calendar = new Calendar.Builder().setDate(2000, 2, 2).setTimeOfDay(4, 12, second, 10).build();
            wt.notify(new TimedEvent<Calendar>(VALUE_EVENT, "EventBasedTimestampWeightedTallyTest", index++, calendar));
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
        assertEquals(stDev, wt.getWeightedSampleStDev(), 1.0E-6);
    }

}
