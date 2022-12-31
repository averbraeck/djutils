package org.djutils.stats.summarizers.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.junit.Test;

/**
 * The EventBasedWeightedTallyTest tests the EventBasedWeightedTally.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class EventBasedWeightedTallyTest
{
    /** an event to fire. */
    private static final EventType VALUE_EVENT = new EventType("VALUE_EVENT",
            new MetaData("WeightAndValue", "Double[] with Double weight and Double value ",
                    new ObjectDescriptor[] { new ObjectDescriptor("Weight", "Double weight", Double.class),
                            new ObjectDescriptor("Valuie", "Double value", Double.class) }));

    /** Test the EventBasedWeightedTally. */
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
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.1 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.2 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.3 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.4 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.5 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.6 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.7 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.8 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.9 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 2.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 1.0 }));

        // Now we check the EventBasedWeightedTally
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);
        assertEquals(0.316228, wt.getWeightedPopulationStDev(), 1.0E-6); // Computed with Excel sheet
        assertEquals(0.100000, wt.getWeightedPopulationVariance(), 1.0E-6);
        assertEquals(0.331662, wt.getWeightedSampleStDev(), 1.0E-6); // Computed with Excel sheet
        assertEquals(0.110000, wt.getWeightedSampleVariance(), 1.0E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance += Math.pow(1.5 - (1.0 + i / 10.0), 2);
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, wt.getWeightedSampleVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedSampleStDev(), 1.0E-6);

        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { -0.1, 123.456 }));
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
        catch (IndexOutOfBoundsException iobe)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1 }));
            fail("Object[] with one argument should have thrown an exception");
        }
        catch (IndexOutOfBoundsException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.1, 0.2, 0.3 }));
            fail("Object[] with thre arguments should have thrown an exception");
        }
        catch (IndexOutOfBoundsException iae)
        {
            // Ignore expected exception
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { "bla", 0.2 }));
            fail("EventBasedWeightedTally should fail on weight !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }
        try
        {
            wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.3, "bla" }));
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
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 13.0, 86.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 23.0, 26.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 4.0, 0.0 }));

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);

        // When we shift the times, we should get the same answers
        wt = new EventBasedWeightedTally("simple EventBasedWeightedTally statistic");
        wt.initialize();
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 13.0, 86.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 23.0, 26.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 14.0, 0.0 }));

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(34.32, wt.getWeightedSampleMean(), 0.001);

        // When we have observations with duration 0, we should get the same answers
        wt = new EventBasedWeightedTally("simple EventBasedWeightedTally statistic");
        wt.initialize();
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 13.0, 86.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.0, 86.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 23.0, 26.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 4.0, 0.0 }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0.0, 0.0 }));

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new EventBasedWeightedTally("NIST");
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 1d, 2d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 1d, 3d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0d, 5d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0d, 7d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 4d, 11d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 1d, 13d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 2d, 17d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 1d, 19d }));
        wt.notify(new Event(VALUE_EVENT, "EventBasedWeightedTallyTest", new Object[] { 0d, 23d }));

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, wt.getWeightedSampleStDev(), 0.01);
    }

    /**
     * Test produced events by EventBasedWeightedTally.
     */
    @Test
    public void testWeightedTallyEventProduction()
    {
        EventBasedWeightedTally weightedTally = new EventBasedWeightedTally("testTally");
        assertEquals(weightedTally, weightedTally.getSourceId());
        WeightedObservationEventListener woel = new WeightedObservationEventListener();
        weightedTally.addListener(woel, StatisticsEvents.WEIGHTED_OBSERVATION_ADDED_EVENT);
        assertEquals(0, woel.getObservationEvents());

        EventType[] types = new EventType[] { StatisticsEvents.N_EVENT, StatisticsEvents.MIN_EVENT, StatisticsEvents.MAX_EVENT,
                StatisticsEvents.WEIGHTED_POPULATION_MEAN_EVENT, StatisticsEvents.WEIGHTED_POPULATION_VARIANCE_EVENT,
                StatisticsEvents.WEIGHTED_POPULATION_STDEV_EVENT, StatisticsEvents.WEIGHTED_SUM_EVENT,
                StatisticsEvents.WEIGHTED_SAMPLE_MEAN_EVENT, StatisticsEvents.WEIGHTED_SAMPLE_VARIANCE_EVENT,
                StatisticsEvents.WEIGHTED_SAMPLE_STDEV_EVENT };
        LoggingEventListener[] listeners = new LoggingEventListener[types.length];
        for (int i = 0; i < types.length; i++)
        {
            listeners[i] = new LoggingEventListener();
            weightedTally.addListener(listeners[i], types[i]);
        }

        for (int i = 1; i <= 10; i++)
        {
            weightedTally.register(1.0 * i, 10.0 * i);
        }

        assertEquals(10, woel.getObservationEvents());

        // values based on formulas from https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        Object[] expectedValues = new Object[] { 10L, 10.0, 100.0, 70.0, 600.0, 24.4949, 3850.0, 70.0, 666.6667, 25.81989 };
        for (int i = 0; i < types.length; i++)
        {
            assertEquals("Number of events for listener " + types[i], 10, listeners[i].getNumberOfEvents());
            assertEquals("Event sourceId for listener " + types[i], weightedTally, listeners[i].getLastEvent().getSourceId());
            assertEquals("Event type for listener " + types[i], types[i], listeners[i].getLastEvent().getType());
            if (expectedValues[i] instanceof Long)
            {
                assertEquals("Final value for listener " + types[i], expectedValues[i],
                        listeners[i].getLastEvent().getContent());
            }
            else
            {
                double e = ((Double) expectedValues[i]).doubleValue();
                double c = ((Double) listeners[i].getLastEvent().getContent()).doubleValue();
                assertEquals("Final value for listener " + types[i], e, c, 0.001);
            }
        }
    }

    /** The listener that counts the OBSERVATION_ADDED_EVENT events and checks correctness. */
    class WeightedObservationEventListener implements EventListener
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** counter for the event. */
        private int observationEvents = 0;

        @Override
        public void notify(final Event event)
        {
            assertTrue(event.getType().equals(StatisticsEvents.WEIGHTED_OBSERVATION_ADDED_EVENT));
            assertTrue("Content of the event has a wrong type, not Object[]: " + event.getContent().getClass(),
                    event.getContent() instanceof Object[]);
            Object[] c = (Object[]) event.getContent();
            assertTrue("Content[0] of the event has a wrong type, not double: " + c[0].getClass(), c[0] instanceof Double);
            assertTrue("Content[1] of the event has a wrong type, not double: " + c[1].getClass(), c[1] instanceof Double);
            assertTrue("SourceId of the event has a wrong type, not EventBasedWeightedTally: " + event.getSourceId().getClass(),
                    event.getSourceId() instanceof EventBasedWeightedTally);
            this.observationEvents++;
        }

        /**
         * @return countEvents
         */
        public int getObservationEvents()
        {
            return this.observationEvents;
        }
    }

}
