package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
import org.djutils.event.EventType;
import org.junit.Test;

/**
 * The EventBasedWeightedTallyTest tests the EventBasedWeightedTally.
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
public class EventBasedWeightedTallyTest
{
    /** an event to fire. */
    private static final EventType VALUE_EVENT = new EventType("VALUE_EVENT");

    /** Test the persistent. */
    @Test
    public void testEventBasedWeightedTally()
    {
        String description = "THIS EVENT BASED WEIGHTED TALLY IS TESTED";
        EventBasedWeightedTally wt = new EventBasedWeightedTally(description);

        // check the description
        assertEquals(description, wt.getDescription());
        assertTrue(wt.toString().contains(description));

        // now we check the initial values
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStdDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.1}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.2}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.3}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.4}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.5}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.6}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.7}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.8}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.9}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 2.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 1.0}));

        // Now we check the EventBasedWeightedTally
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
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {-0.1, 123.456}));
            fail("negative weight should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", "123"));
            fail("non Object[] content should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1}));
            fail("Object[] with one argument should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.1, 0.2, 0.3}));
            fail("Object[] with thre arguments should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {"bla", 0.2}));
            fail("EventBasedWeightedTally should fail on weight !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.3, "bla"}));
            fail("EventBasedWeightedTally should fail on value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

    }

    /** Test the EventBasedWeightedTally on a simple example. */
    @Test
    public void testEventBasedWeightedTallySimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        EventBasedWeightedTally wt = new EventBasedWeightedTally("simple EventBasedWeightedTally statistic");
        wt.initialize();
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {13.0, 86.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {23.0, 26.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {4.0, 0.0}));

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);

        // When we shift the times, we should get the same answers
        wt = new EventBasedWeightedTally("simple EventBasedWeightedTally statistic");
        wt.initialize();
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {13.0, 86.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {23.0, 26.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {14.0, 0.0}));

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(34.32, wt.getWeightedSampleMean(), 0.001);

        // When we have observations with duration 0, we should get the same answers
        wt = new EventBasedWeightedTally("simple EventBasedWeightedTally statistic");
        wt.initialize();
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {13.0, 86.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.0, 86.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {23.0, 26.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {4.0, 0.0}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0.0, 0.0}));

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new EventBasedWeightedTally("NIST");
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {1, 2}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {1, 3}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0, 5}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0, 7}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {4, 11}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {1, 13}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {2, 17}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {1, 19}));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] {0, 23}));

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, wt.getWeightedSampleStdDev(), 0.01);
    }
}
