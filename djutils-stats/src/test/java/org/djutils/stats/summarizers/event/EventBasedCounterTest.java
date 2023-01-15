package org.djutils.stats.summarizers.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventType;
import org.djutils.metadata.MetaData;
import org.junit.Test;

/**
 * Test the EventBasedCounter class.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EventBasedCounterTest
{
    /** the event to fire. */
    private static final EventType COUNT_EVENT = new EventType("CountEvent", MetaData.NO_META_DATA);

    /**
     * Test the counter.
     * @throws RemoteException on network error for the event-based statistic
     */
    @Test
    public void testEventBasedCounter() throws RemoteException
    {
        String description = "counter description";
        EventBasedCounter counter = new EventBasedCounter(description);
        assertTrue(counter.toString().contains(description));
        assertEquals(description, counter.getDescription());

        assertEquals(0L, counter.getN());
        assertEquals(0L, counter.getCount());

        counter.notify(new Event(COUNT_EVENT, 2));
        assertEquals(1L, counter.getN());
        assertEquals(2L, counter.getCount());

        counter.initialize();
        assertEquals(0L, counter.getN());
        assertEquals(0L, counter.getCount());

        CounterEventListener cel = new CounterEventListener();
        counter.addListener(cel, StatisticsEvents.OBSERVATION_ADDED_EVENT);
        assertEquals(0, cel.getCountEvents());

        // test wrong event
        try
        {
            counter.notify(new Event(COUNT_EVENT, "abc"));
            fail("Wrong payload to EventBasedCounter should have triggreed an exception");
        }
        catch (RuntimeException exception)
        {
            // ok, should have given error
        }

        LoggingEventListener nListener = new LoggingEventListener();
        counter.addListener(nListener, StatisticsEvents.N_EVENT);
        LoggingEventListener countListener = new LoggingEventListener();
        counter.addListener(countListener, StatisticsEvents.COUNT_EVENT);

        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.notify(new Event(COUNT_EVENT, 2 * i));
            value += 2 * i;
        }
        assertEquals(100, counter.getN());
        assertEquals(value, counter.getCount());
        assertEquals(100, cel.getCountEvents());
        assertEquals(100, nListener.getNumberOfEvents());
        assertEquals(100, countListener.getNumberOfEvents());
        assertEquals(100L, nListener.getLastEvent().getContent());
        assertEquals(value, countListener.getLastEvent().getContent());
    }

    /** The listener that counts the OBSERVATION_ADDED_EVENT events and checks correctness. */
    class CounterEventListener implements EventListener
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** counter for the event. */
        private int countEvents = 0;

        @Override
        public void notify(final Event event)
        {
            assertTrue(event.getType().equals(StatisticsEvents.OBSERVATION_ADDED_EVENT));
            assertTrue("Content of the event has a wrong type, not Long: " + event.getContent().getClass(),
                    event.getContent() instanceof Long);
            this.countEvents++;
        }

        /**
         * @return countEvents
         */
        public int getCountEvents()
        {
            return this.countEvents;
        }
    }
}
