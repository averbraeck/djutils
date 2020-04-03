package org.djutils.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * EventTest tests the EventType, Event, and TimedEvent.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EventTest
{
    /**
     * Test the EventType.
     */
    @Test
    public void testEventType()
    {
        EventType eventType = new EventType("TEST_TYPE", null);
        assertEquals(eventType, eventType);
        Object content = new SerializableObject();
        assertNotEquals(eventType, content);
        assertNotEquals(eventType, null);
        assertNotEquals(eventType, new EventType("TEST_TYPE2", null));
        assertEquals(eventType.getName(), "TEST_TYPE");
        assertEquals(eventType.toString(), "TEST_TYPE");
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new EventType("", null);
            }
        });
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new EventType(null, null);
            }
        });
    }

    /**
     * Test the Event.
     */
    @Test
    public void testEvent()
    {
        Serializable source = "source_id";
        Serializable source2 = new SerializableObject();
        EventType eventType = new EventType("TEST_TYPE", null);
        EventType eventType2 = new EventType("TEST_TYPE2", null);
        Serializable content = new SerializableObject();
        Serializable content2 = new SerializableObject();
        EventInterface event = new Event(eventType, source, content);
        assertEquals(event.getContent(), content);
        assertEquals(event.getSourceId(), source);
        assertEquals(event.getType(), eventType);

        assertEquals(event, event);
        assertEquals(event, new Event(eventType, source, content));
        assertNotEquals(event, source);
        assertNotEquals(event, null);

        assertNotEquals(event, new Event(eventType2, source, content));
        assertNotEquals(event, new Event(eventType, source2, content));
        assertNotEquals(event, new Event(eventType, source, content2));

        assertNotEquals(event, new Event(null, source, content));
        assertNotEquals(event, new Event(eventType, null, content));
        assertNotEquals(event, new Event(eventType, source, null));

        assertNotEquals(new Event(null, source, content), event);
        assertNotEquals(new Event(eventType, null, content), event);
        assertNotEquals(new Event(eventType, source, null), event);

        assertEquals(new Event(null, source, content), new Event(null, source, content));
        assertEquals(new Event(eventType, null, content), new Event(eventType, null, content));
        assertEquals(new Event(eventType, source, null), new Event(eventType, source, null));

        assertEquals(event.hashCode(), event.hashCode());
        assertEquals(event.hashCode(), new Event(eventType, source, content).hashCode());
        assertNotEquals(event.hashCode(), source.hashCode());

        assertNotEquals(event.hashCode(), new Event(eventType2, source, content).hashCode());
        assertNotEquals(event.hashCode(), new Event(eventType, source2, content).hashCode());
        assertNotEquals(event.hashCode(), new Event(eventType, source, content2).hashCode());

        assertNotEquals(event.hashCode(), new Event(null, source, content).hashCode());
        assertNotEquals(event.hashCode(), new Event(eventType, null, content).hashCode());
        assertNotEquals(event.hashCode(), new Event(eventType, source, null).hashCode());

        assertTrue(event.toString().contains("TEST_TYPE"));
    }

    /**
     * Test the TimedEvent.
     */
    @Test
    public void testTimedEvent()
    {
        Serializable source = "timed_source_id";
        Serializable source2 = new SerializableObject();
        EventType eventType = new EventType("TEST_TYPE", null);
        EventType eventType2 = new EventType("TEST_TYPE2", null);
        Serializable content = new SerializableObject();
        Serializable content2 = new SerializableObject();
        long time = 123L;
        long time2 = 456L;
        TimedEvent<Long> event = new TimedEvent<>(eventType, source, content, time);
        TimedEvent<Long> event2 = new TimedEvent<>(eventType2, source2, content2, time2);
        assertEquals(content, event.getContent());
        assertEquals(source, event.getSourceId());
        assertEquals(eventType, event.getType());
        assertEquals(time, event.getTimeStamp().longValue());

        assertEquals(event, event);
        assertEquals(new TimedEvent<Long>(eventType, source, content, time), event);
        assertNotEquals(event, source);
        assertNotEquals(event, null);

        assertNotEquals(event, new TimedEvent<Long>(eventType2, source, content, time));
        assertNotEquals(event, new TimedEvent<Long>(eventType, source2, content, time));
        assertNotEquals(event, new TimedEvent<Long>(eventType, source, content2, time));
        assertNotEquals(event, new TimedEvent<Long>(eventType, source, content, time2));

        assertNotEquals(event, new TimedEvent<Long>(null, source, content, time));
        assertNotEquals(event, new TimedEvent<Long>(eventType, null, content, time));
        assertNotEquals(event, new TimedEvent<Long>(eventType, source, null, time));
        assertNotEquals(event, new TimedEvent<Long>(eventType, source, content, null));

        assertNotEquals(new TimedEvent<Long>(null, source, content, time), event);
        assertNotEquals(new TimedEvent<Long>(eventType, null, content, time), event);
        assertNotEquals(new TimedEvent<Long>(eventType, source, null, time), event);
        assertNotEquals(new TimedEvent<Long>(eventType, source, content, null), event);

        assertEquals(new TimedEvent<Long>(null, source, content, time), new TimedEvent<Long>(null, source, content, time));
        assertEquals(new TimedEvent<Long>(eventType, null, content, time),
                new TimedEvent<Long>(eventType, null, content, time));
        assertEquals(new TimedEvent<Long>(eventType, source, null, time), new TimedEvent<Long>(eventType, source, null, time));
        assertEquals(new TimedEvent<Long>(eventType, source, content, null),
                new TimedEvent<Long>(eventType, source, content, null));

        assertEquals(event.hashCode(), event.hashCode());
        assertEquals(event.hashCode(), new TimedEvent<Long>(eventType, source, content, time).hashCode());
        assertNotEquals(event.hashCode(), source.hashCode());

        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType2, source, content, time).hashCode());
        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType, source2, content, time).hashCode());
        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType, source, content2, time).hashCode());
        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType, source, content, time2).hashCode());

        assertNotEquals(event.hashCode(), new TimedEvent<Long>(null, source, content, time).hashCode());
        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType, null, content, time).hashCode());
        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType, source, null, time).hashCode());
        assertNotEquals(event.hashCode(), new TimedEvent<Long>(eventType, source, content, null).hashCode());

        assertTrue(event.toString().contains("TEST_TYPE"));
        assertTrue(event.toString().contains("123"));

        assertTrue(event.compareTo(event) == 0);
        assertTrue(event.compareTo(event2) < 0);
        assertTrue(event2.compareTo(event) > 0);
    }

    /** Serializable object class. */
    class SerializableObject extends Object implements Serializable
    {
        /** */
        private static final long serialVersionUID = 1L;
    }
}
