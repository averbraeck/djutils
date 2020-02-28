package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;
import org.junit.Test;

/**
 * Test the EventBasedCounter class.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EventBasedCounterTest
{
    /** the event to fire. */
    private static final EventType COUNT_EVENT = new EventType("CountEvent");

    /** Test the counter. */
    @Test
    public void testEventBasedCounter()
    {
        String description = "counter description";
        EventBasedCounter counter = new EventBasedCounter(description);
        assertEquals(description, counter.toString());
        assertEquals(description, counter.getDescription());

        assertEquals(0L, counter.getN());
        assertEquals(0L, counter.getCount());

        counter.notify(new Event(COUNT_EVENT, "EventBasedCounterTest", 2));
        assertEquals(1L, counter.getN());
        assertEquals(2L, counter.getCount());

        counter.initialize();
        assertEquals(0L, counter.getN());
        assertEquals(0L, counter.getCount());

        counter.addListener(new EventListenerInterface()
        {
            /** */
            private static final long serialVersionUID = 1L;

            @Override
            public void notify(final EventInterface event)
            {
                assertTrue(event.getType().equals(EventBasedCounter.OBSERVATION_ADDED_EVENT));
                assertTrue("Content of the event has a wrong type, not EventBasedCounter: " + event.getContent().getClass(),
                        event.getContent() instanceof EventBasedCounter);
            }
        }, EventBasedCounter.OBSERVATION_ADDED_EVENT);

        // test wrong event
        try
        {
            counter.notify(new Event(COUNT_EVENT, "EventBasedCounterTest", "abc"));
            fail("Wrong payload to EventBasedCounter should have triggreed an exception");
        }
        catch (RuntimeException exception)
        {
            // ok, should have given error
        }

        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.notify(new Event(COUNT_EVENT, "EventBasedCounterTest", 2 * i));
            value += 2 * i;
        }
        assertEquals(100, counter.getN());
        assertEquals(value, counter.getCount());
    }
}