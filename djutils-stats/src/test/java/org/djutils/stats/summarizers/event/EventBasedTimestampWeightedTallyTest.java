package org.djutils.stats.summarizers.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;
import org.djutils.exceptions.Try;
import org.djutils.metadata.MetaData;
import org.junit.jupiter.api.Test;

/**
 * The EventBasedTimestampWeightedTallyTest test the weighted tally that receives event-observations with a timestamp.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class EventBasedTimestampWeightedTallyTest
{
    /** an event to fire. */
    private static final EventType TIMED_VALUE_EVENT = new EventType("VALUE_EVENT", MetaData.NO_META_DATA);

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
        assertTrue(Double.isNaN(wt.getWeightedPopulationMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        // We fire a wrong event with wrong content
        try
        {
            wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, "ERROR", 0.0));
            fail("tally should fail on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // We fire a wrong event with wrong timestamp
        try
        {
            wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.0, Double.NaN));
            fail("tally should fail on events.timestamp == NaN");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.0, 0.0));
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedPopulationMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.1, 0.1));
        assertEquals(1.0, wt.getMin(), 0.000001);
        assertEquals(1.0, wt.getMax(), 0.000001);
        assertEquals(1.0, wt.getWeightedSampleMean(), 0.000001);
        assertEquals(1.0, wt.getWeightedPopulationMean(), 0.000001);
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        assertEquals(0, wt.getWeightedPopulationVariance(), 0.000001);
        assertEquals(0, wt.getWeightedPopulationStDev(), 0.0000001);
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.2, 0.2));
        assertFalse(Double.isNaN(wt.getWeightedSampleVariance()));
        assertFalse(Double.isNaN(wt.getWeightedSampleStDev()));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.3, 0.3));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.4, 0.4));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.5, 0.5));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.6, 0.6));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.7, 0.7));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.8, 0.8));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 1.9, 0.9));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 2.0, 1.0));

        Try.testFail(() -> wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 123.456, 0.8)),
                "timestamp out of order should have thrown an exception", IllegalArgumentException.class);

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
        assertEquals(variance, wt.getWeightedPopulationVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedPopulationStDev(), 1.0E-6);

        // Adding something after the active period should not make a change
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 10.0, 20.0));
        assertFalse(wt.isActive());
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);

        // test some wrong events
        Try.testFail(() -> wt.notify(new Event(TIMED_VALUE_EVENT, new Object[] {1.0, 2.0})),
                "non time-based event should have thrown an exception", IllegalArgumentException.class);
        Try.testFail(() -> wt.notify(new TimedEvent<String>(TIMED_VALUE_EVENT, 123.456, "abc")),
                "non time-based event with timestamp != Calendar or Number should have thrown an exception",
                IllegalArgumentException.class);

        // call initialize later to see if event firing is okay
        wt.initialize();
        assertEquals(0, wt.getN());

    }

    /** Test the EventBasedTimestampWeightedTally on a simple example. */
    @Test
    public void testEventBasedTimestampWeightedTallySimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        EventBasedTimestampWeightedTally wt =
                new EventBasedTimestampWeightedTally("simple EventBasedTimestampWeightedTally statistic");
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 86.0, 0.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 26.0, 13.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 0.0, 36.0));
        wt.endObservations(40.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we shift the times, we should get the same answers
        wt = new EventBasedTimestampWeightedTally("simple EventBasedTimestampWeightedTally statistic");
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 86.0, 10.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 26.0, 23.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 0.0, 46.0));
        wt.endObservations(50.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we have observations with duration 0, we should get the same answers
        wt = new EventBasedTimestampWeightedTally("simple EventBasedTimestampWeightedTally statistic");
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 86.0, 0.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 26.0, 13.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 0.0, 13.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 26.0, 13.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 0.0, 36.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 0.0, 36.0));
        wt.endObservations(40.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN()); // non-zero values only

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new EventBasedTimestampWeightedTally("NIST");
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 2, 0.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 3, 1.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 5, 2.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 7, 2.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 11, 2.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 13, 6.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 17, 7.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 19, 9.0));
        wt.notify(new TimedEvent<Double>(TIMED_VALUE_EVENT, 23, 10.0));
        wt.endObservations(10.0);

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals(5.82, wt.getWeightedSampleStDev(), 0.01);
    }

    /** Test the TimestampWeightedTally for Calendar-based timestamps. */
    @Test
    public void testEBTimestampWeightedTallyCalendarNotify()
    {
        String description = "THIS TIMESTAMP WEIGHTED TALLY IS TESTED";
        EventBasedTimestampWeightedTally wt = new EventBasedTimestampWeightedTally(description);

        int index = 10;
        for (int second = 30; second <= 40; second++)
        {
            Calendar calendar = new Calendar.Builder().setDate(2000, 2, 2).setTimeOfDay(4, 12, second, 10).build();
            wt.notify(new TimedEvent<Calendar>(TIMED_VALUE_EVENT, index++, calendar));
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

    /** Test the TimestampWeightedTally for Calendar-based timestamps with register(). */
    @Test
    public void testEBTimestampWeightedTallyCalendarregister()
    {
        String description = "THIS TIMESTAMP WEIGHTED TALLY IS TESTED";
        EventBasedTimestampWeightedTally wt = new EventBasedTimestampWeightedTally(description);

        int index = 10;
        for (int second = 30; second <= 40; second++)
        {
            Calendar calendar = new Calendar.Builder().setDate(2000, 2, 2).setTimeOfDay(4, 12, second, 10).build();
            wt.register(calendar, index++);
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

    /**
     * Test produced events by EventBasedWeightedTally.
     * @throws RemoteException on network error
     */
    @Test
    public void testEventBasedWeightedTallyEventProduction() throws RemoteException
    {
        EventBasedTimestampWeightedTally timestampedTally = new EventBasedTimestampWeightedTally("testTally");
        TimestampedObservationEventListener toel = new TimestampedObservationEventListener();
        timestampedTally.addListener(toel, StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT);
        assertEquals(0, toel.getObservationEvents());

        EventType[] types = new EventType[] {StatisticsEvents.TIMED_N_EVENT, StatisticsEvents.TIMED_MIN_EVENT,
                StatisticsEvents.TIMED_MAX_EVENT, StatisticsEvents.TIMED_WEIGHTED_POPULATION_MEAN_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_POPULATION_VARIANCE_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_POPULATION_STDEV_EVENT, StatisticsEvents.TIMED_WEIGHTED_SUM_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_SAMPLE_MEAN_EVENT, StatisticsEvents.TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_SAMPLE_STDEV_EVENT};
        LoggingEventListener[] listeners = new LoggingEventListener[types.length];
        for (int i = 0; i < types.length; i++)
        {
            listeners[i] = new LoggingEventListener();
            timestampedTally.addListener(listeners[i], types[i]);
        }

        double prevTime = 0.0;
        for (int i = 1; i <= 10; i++)
        {
            timestampedTally.register(prevTime, 10.0 * i);
            prevTime += i;
        }
        timestampedTally.endObservations(prevTime);

        // the endObservation fires an observation event, but does not increase N
        assertEquals(11, toel.getObservationEvents());

        // values based on formulas from https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        Object[] expectedValues = new Object[] {10L, 10.0, 100.0, 70.0, 600.0, 24.4949, 3850.0, 70.0, 666.6667, 25.81989};
        for (int i = 0; i < types.length; i++)
        {
            assertEquals(11, listeners[i].getNumberOfEvents(), "Number of events for listener " + types[i]);
            assertEquals(types[i], listeners[i].getLastEvent().getType(), "Event type for listener " + types[i]);
            if (expectedValues[i] instanceof Long)
            {
                assertEquals(expectedValues[i], listeners[i].getLastEvent().getContent(),
                        "Final value for listener " + types[i]);
            }
            else
            {
                double e = ((Double) expectedValues[i]).doubleValue();
                double c = ((Double) listeners[i].getLastEvent().getContent()).doubleValue();
                assertEquals(e, c, 0.001, "Final value for listener " + types[i]);
            }
        }
    }

    /**
     * Test produced events by EventBasedWeightedTally.
     * @throws RemoteException on network error
     */
    @Test
    public void testCalendarBasedWeightedTallyEventProduction() throws RemoteException
    {
        EventBasedTimestampWeightedTally timestampedTally = new EventBasedTimestampWeightedTally("testTally");
        CalendarObservationEventListener toel = new CalendarObservationEventListener();
        timestampedTally.addListener(toel, StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT);
        assertEquals(0, toel.getObservationEvents());

        EventType[] types = new EventType[] {StatisticsEvents.TIMED_N_EVENT, StatisticsEvents.TIMED_MIN_EVENT,
                StatisticsEvents.TIMED_MAX_EVENT, StatisticsEvents.TIMED_WEIGHTED_POPULATION_MEAN_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_POPULATION_VARIANCE_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_POPULATION_STDEV_EVENT, StatisticsEvents.TIMED_WEIGHTED_SUM_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_SAMPLE_MEAN_EVENT, StatisticsEvents.TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT,
                StatisticsEvents.TIMED_WEIGHTED_SAMPLE_STDEV_EVENT};
        LoggingEventListener[] listeners = new LoggingEventListener[types.length];
        for (int i = 0; i < types.length; i++)
        {
            listeners[i] = new LoggingEventListener();
            timestampedTally.addListener(listeners[i], types[i]);
        }

        Calendar calendar = new GregorianCalendar(2000, 1, 10, 20, 30);
        for (int i = 1; i <= 10; i++)
        {
            timestampedTally.register(calendar, 10.0 * i);
            calendar.roll(Calendar.MINUTE, true);
        }
        timestampedTally.endObservations(calendar);

        // the endObservation fires an observation event, but does not increase N
        assertEquals(11, toel.getObservationEvents());
    }

    /**
     * Test the event-based timestamp weighted tally for RemoteExceptions.
     * @throws RemoteException on network error for the event-based statistic
     */
    @Test
    public void testEventBasedTimestampTallyRemote() throws RemoteException
    {
        String description = "tally description";
        EventBasedTimestampWeightedTally tally = new EventBasedTimestampWeightedTally(description, new RmiErrorEventProducer());
        RmiErrorEventListener listener = new RmiErrorEventListener();
        tally.addListener(listener, StatisticsEvents.INITIALIZED_EVENT);
        tally.addListener(listener, StatisticsEvents.OBSERVATION_ADDED_EVENT);
        // RemoteException is packed in a RuntimeException
        Try.testFail(() -> tally.initialize(), RuntimeException.class);
        Try.testFail(() -> tally.register(1.0, 10.0), RuntimeException.class);
        Calendar calendar = new GregorianCalendar(2000, 1, 10, 20, 30);
        Try.testFail(() -> tally.register(calendar, 10.0), RuntimeException.class);
    }

    /** The listener that counts the OBSERVATION_ADDED_EVENT events and checks correctness. */
    class TimestampedObservationEventListener implements EventListener
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** counter for the event. */
        private int observationEvents = 0;

        @Override
        public void notify(final Event event)
        {
            assertTrue(event.getType().equals(StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT));
            assertTrue(event.getContent() instanceof Object[],
                    "Content of the event has a wrong type, not Object[]: " + event.getContent().getClass());
            Object[] c = (Object[]) event.getContent();
            assertTrue(c[0] instanceof Double, "Content[0] of the event has a wrong type, not double: " + c[0].getClass());
            assertTrue(c[1] instanceof Double, "Content[1] of the event has a wrong type, not double: " + c[1].getClass());
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

    /** The Calendar-based listener that counts the OBSERVATION_ADDED_EVENT events and checks correctness. */
    class CalendarObservationEventListener implements EventListener
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** counter for the event. */
        private int observationEvents = 0;

        @Override
        public void notify(final Event event)
        {
            assertTrue(event.getType().equals(StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT));
            assertTrue(event.getContent() instanceof Object[],
                    "Content of the event has a wrong type, not Object[]: " + event.getContent().getClass());
            Object[] c = (Object[]) event.getContent();
            assertTrue(c[0] instanceof Calendar, "Content[0] of the event has a wrong type, not Calendar: " + c[0].getClass());
            assertTrue(c[1] instanceof Double, "Content[1] of the event has a wrong type, not double: " + c[1].getClass());
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
