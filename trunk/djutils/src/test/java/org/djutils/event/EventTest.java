package org.djutils.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;

import org.djutils.exceptions.Try;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.junit.Test;

/**
 * EventTest tests the EventType, Event, and TimedEvent.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * Test the NO_META_DATA object.
     */
    @Test
    public void testNoMetaData()
    {
        EventType noMetaDataEventType = new EventType("No Meta Data", MetaData.NO_META_DATA);
        new Event(noMetaDataEventType, "sender", new Object[] {"abc", 123, 0.6}); // should not fail

        EventType withMetaDataEventType =
                new EventType("With Meta Data", new MetaData("Almost identical to NO_META_DATA", "Any Object is accepted",
                        new ObjectDescriptor("Almost identical to NO_META_DATA", "Any Object is accepted", Object.class)));
        try
        {
            new Event(withMetaDataEventType, "sender", new Object[] {"abc", 123, 0.6});
            fail("imitation of NO_META_DATA does not work for Object[] payload");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

    }

    /**
     * Test the constructor without a name.
     */
    @Test
    public void testNoName()
    {
        MetaData metaData =
                new MetaData("INT_EVENT", "event with integer payload", new ObjectDescriptor("int", "integer", Integer.class));
        EventType eventType = new EventType(metaData);
        assertEquals(eventType, eventType);
        assertEquals(Event.class, eventType.getValidEventType());
        Object content = new SerializableObject();
        assertNotEquals(eventType, content);
        assertNotEquals(eventType, null);
        assertEquals(eventType, new EventType(metaData));
        assertEquals(eventType.getName(), "INT_EVENT");
        assertEquals(eventType.getMetaData().getName(), "INT_EVENT");
        assertEquals(eventType.getMetaData().getDescription(), "event with integer payload");
        assertEquals(eventType.toString(), "INT_EVENT");
        assertEquals(1, eventType.getMetaData().getObjectDescriptors().length);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new EventType((MetaData) null);
            }
        }, "Constructing EventType with null metadata should have failed");

        new Event(eventType, "source", 5);
        new Event(eventType, "source", null);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Event(eventType, "source", 1.2);
            }
        }, "Constructing Integer Event with double content should have failed");
        new Event(eventType, "source", 1.2, false); // but without checking it should succeed

        EventType eventType2 = new EventType(MetaData.EMPTY);
        assertEquals(eventType2, eventType2);
        assertNotEquals(eventType, eventType2);
        Object content2 = new SerializableObject();
        assertNotEquals(eventType2, content2);
        assertNotEquals(eventType2, null);
        assertEquals(eventType2, new EventType(MetaData.EMPTY));
        assertEquals(eventType2.getName(), "No data");
        assertEquals(eventType2.getMetaData().getName(), "No data");
        assertEquals(eventType2.getMetaData().getDescription(), "No data");
        assertEquals(eventType2.toString(), "No data");
        assertEquals(1, eventType.getMetaData().getObjectDescriptors().length);

        new Event(eventType2, "source", null);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new Event(eventType2, "source", 1.2);
            }
        }, "Constructing EMPTY Event with double content should have failed");
    }

    /**
     * Test the EventType.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testEventType()
    {
        EventType eventType = new EventType("TEST_TYPE", MetaData.NO_META_DATA);
        assertEquals(eventType, eventType);
        assertEquals(Event.class, eventType.getValidEventType());
        Object content = new SerializableObject();
        assertNotEquals(eventType, content);
        assertNotEquals(eventType, null);
        assertNotEquals(eventType, new EventType("TEST_TYPE2", MetaData.NO_META_DATA));
        assertEquals(eventType.getName(), "TEST_TYPE");
        assertEquals(eventType.toString(), "TEST_TYPE");
        TimedEventType timedEventType = new TimedEventType("TEST_TYPE", MetaData.NO_META_DATA);
        assertEquals(TimedEvent.class, timedEventType.getValidEventType());

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
                new EventType((String) null, (MetaData) null);
            }
        });

        // Check the deprecated constructor
        eventType = new EventType("event with unspecified meta data");
        assertEquals("Deprecated constructor uses NO_META_DATA for the meta data", MetaData.NO_META_DATA,
                eventType.getMetaData());
        assertEquals("Name is correctly used", "event with unspecified meta data", eventType.getName());
        assertEquals(Event.class, eventType.getValidEventType());
    }

    /**
     * Test the Event.
     */
    @Test
    public void testEvent()
    {
        Serializable source = "source_id";
        Serializable source2 = new SerializableObject();
        EventType eventType = new EventType("TEST_TYPE", MetaData.NO_META_DATA);
        EventType eventType2 = new EventType("TEST_TYPE2", MetaData.NO_META_DATA);
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
        TimedEventType timedEventType = new TimedEventType("TEST_TYPE", MetaData.NO_META_DATA);
        TimedEventType timedEventType2 = new TimedEventType("TEST_TYPE2", MetaData.NO_META_DATA);
        Serializable content = new SerializableObject();
        Serializable content2 = new SerializableObject();
        long time = 123L;
        long time2 = 456L;
        TimedEvent<Long> timedEvent = new TimedEvent<>(timedEventType, source, content, time);
        TimedEvent<Long> timedEvent2 = new TimedEvent<>(timedEventType2, source2, content2, time2);
        assertEquals(content, timedEvent.getContent());
        assertEquals(source, timedEvent.getSourceId());
        assertEquals(timedEventType, timedEvent.getType());
        assertEquals(time, timedEvent.getTimeStamp().longValue());

        assertEquals(timedEvent, timedEvent);
        assertEquals(new TimedEvent<Long>(timedEventType, source, content, time), timedEvent);
        assertNotEquals(timedEvent, source);
        assertNotEquals(timedEvent, null);

        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType2, source, content, time));
        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType, source2, content, time));
        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType, source, content2, time));
        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType, source, content, time2));

        assertNotEquals(timedEvent, new TimedEvent<Long>(null, source, content, time));
        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType, null, content, time));
        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType, source, null, time));
        assertNotEquals(timedEvent, new TimedEvent<Long>(timedEventType, source, content, null));

        assertNotEquals(new TimedEvent<Long>(null, source, content, time), timedEvent);
        assertNotEquals(new TimedEvent<Long>(timedEventType, null, content, time), timedEvent);
        assertNotEquals(new TimedEvent<Long>(timedEventType, source, null, time), timedEvent);
        assertNotEquals(new TimedEvent<Long>(timedEventType, source, content, null), timedEvent);

        assertEquals(new TimedEvent<Long>(null, source, content, time), new TimedEvent<Long>(null, source, content, time));
        assertEquals(new TimedEvent<Long>(timedEventType, null, content, time),
                new TimedEvent<Long>(timedEventType, null, content, time));
        assertEquals(new TimedEvent<Long>(timedEventType, source, null, time),
                new TimedEvent<Long>(timedEventType, source, null, time));
        assertEquals(new TimedEvent<Long>(timedEventType, source, content, null),
                new TimedEvent<Long>(timedEventType, source, content, null));

        assertEquals(timedEvent.hashCode(), timedEvent.hashCode());
        assertEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, source, content, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), source.hashCode());

        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType2, source, content, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, source2, content, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, source, content2, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, source, content, time2).hashCode());

        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(null, source, content, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, null, content, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, source, null, time).hashCode());
        assertNotEquals(timedEvent.hashCode(), new TimedEvent<Long>(timedEventType, source, content, null).hashCode());

        assertTrue(timedEvent.toString().contains("TEST_TYPE"));
        assertTrue(timedEvent.toString().contains("123"));

        assertTrue(timedEvent.compareTo(timedEvent) == 0);
        assertTrue(timedEvent.compareTo(timedEvent2) < 0);
        assertTrue(timedEvent2.compareTo(timedEvent) > 0);

        MetaData metaData =
                new MetaData("INT_EVENT", "event with integer payload", new ObjectDescriptor("int", "integer", Integer.class));
        TimedEventType intEventType = new TimedEventType(metaData);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new TimedEvent<Double>(intEventType, "source", 1.2, 3.4);
            }
        }, "Constructing Integer TimedEvent with double content should have failed");
        new TimedEvent<Double>(intEventType, "source", 1.2, 3.4, false); // but without checking it should succeed
    }

    /** Serializable object class. */
    class SerializableObject extends Object implements Serializable
    {
        /** */
        private static final long serialVersionUID = 1L;
    }
}
