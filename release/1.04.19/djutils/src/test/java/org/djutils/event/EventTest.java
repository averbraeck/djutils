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
        Object content = new SerializableObject();
        assertNotEquals(eventType, content);
        assertNotEquals(eventType, null);
        assertNotEquals(eventType, new EventType("TEST_TYPE2", MetaData.NO_META_DATA));
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

        // Check the deprecated constructor
        eventType = new EventType("event with unspecified meta data");
        assertEquals("Deprecated constructor uses NO_META_DATA for the meta data", MetaData.NO_META_DATA,
                eventType.getMetaData());
        assertEquals("Name is correctly used", "event with unspecified meta data", eventType.getName());
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
        EventType eventType = new EventType("TEST_TYPE", MetaData.NO_META_DATA);
        EventType eventType2 = new EventType("TEST_TYPE2", MetaData.NO_META_DATA);
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
