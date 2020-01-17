package org.djutils.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;
import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * Test the EventProducer and EventListener.
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
public class EventPubSubTest implements Serializable
{
    /** */
    private static final long serialVersionUID = 20191230L;

    /**
     * Test the EventProducer and EventListener.
     */
    @Test
    public void testEventPubSub()
    {
        TestEventProducer producer = new TestEventProducer();
        TestEventListener listener = new TestEventListener();
        EventType eventType = new EventType("TEST_TYPE");

        boolean addListenerOK = producer.addListener(listener, eventType);
        assertTrue(addListenerOK);

        String string = "abc123";
        listener.setExpectedObject(string);
        producer.fireEvent(new Event(eventType, producer, string));
        assertEquals(string, listener.getReceivedEvent().getContent());
        assertEquals(eventType, listener.getReceivedEvent().getType());
        assertEquals(producer, listener.getReceivedEvent().getSource());

        listener.setExpectedObject(Boolean.valueOf(true));
        producer.fireEvent(eventType, true);
        listener.setExpectedObject(Boolean.valueOf(false));
        producer.fireEvent(eventType, false);

        listener.setExpectedObject(Byte.valueOf((byte) 87));
        producer.fireEvent(eventType, (byte) 87);

        listener.setExpectedObject(Short.valueOf((short) -234));
        producer.fireEvent(eventType, (short) -234);

        listener.setExpectedObject(Double.valueOf(123.456d));
        producer.fireEvent(eventType, 123.456d);

        listener.setExpectedObject(Integer.valueOf(12345));
        producer.fireEvent(eventType, 12345);

        listener.setExpectedObject(Long.valueOf(123456L));
        producer.fireEvent(eventType, 123456L);

        listener.setExpectedObject(null);
        producer.fireEvent(eventType);

        // remove listener tests
        producer.removeListener(listener, TestEventProducer.PRODUCER_EVENT_1);
        listener.setExpectedObject(Byte.valueOf((byte) 87));
        producer.fireEvent(eventType, (byte) 87);

        producer.removeListener(listener, eventType);
        listener.setExpectingNotification(false);
        producer.fireEvent(eventType, 12345);

        addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1);
        assertTrue(addListenerOK);
        addListenerOK = producer.addListener(listener, eventType);
        assertTrue(addListenerOK);
        listener.setExpectingNotification(true);
        listener.setExpectedObject(Double.valueOf(123.456d));
        producer.fireEvent(eventType, 123.456d);
        assertEquals(eventType, listener.getReceivedEvent().getType());
        listener.setExpectedObject(Double.valueOf(234.567d));
        producer.fireEvent(TestEventProducer.PRODUCER_EVENT_1, 234.567d);
        assertEquals(TestEventProducer.PRODUCER_EVENT_1, listener.getReceivedEvent().getType());

        int nrRemovedListeners = producer.removeAllListeners();
        assertEquals(2, nrRemovedListeners);
        listener.setExpectingNotification(false);
        listener.setExpectedObject(Byte.valueOf((byte) 87));
        producer.fireEvent(eventType, (byte) 87);
        producer.fireEvent(TestEventProducer.PRODUCER_EVENT_1, 12345);

        addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1);
        assertTrue(addListenerOK);
        addListenerOK = producer.addListener(listener, eventType);
        assertTrue(addListenerOK);
        listener.setExpectingNotification(true);
        listener.setExpectedObject(Double.valueOf(123.456d));
        producer.fireEvent(eventType, 123.456d);
        assertEquals(eventType, listener.getReceivedEvent().getType());
        listener.setExpectedObject(Double.valueOf(234.567d));
        producer.fireEvent(TestEventProducer.PRODUCER_EVENT_1, 234.567d);
        assertEquals(TestEventProducer.PRODUCER_EVENT_1, listener.getReceivedEvent().getType());
        TestTimedEventListener<Double> timedListener = new TestTimedEventListener<>();
        addListenerOK = producer.addListener(timedListener, eventType);
        assertTrue(addListenerOK);
        timedListener.setExpectingNotification(true);
        timedListener.setExpectedObject(Double.valueOf(12.34d));
        listener.setExpectedObject(Double.valueOf(12.34d));
        producer.fireEvent(new TimedEvent<Double>(eventType, producer, Double.valueOf(12.34d), 12.01d));
        assertEquals(12.01, timedListener.getReceivedEvent().getTimeStamp(), 0.001);

        nrRemovedListeners = producer.removeAllListeners(TestEventListener.class);
        assertEquals(2, nrRemovedListeners);
        listener.setExpectingNotification(false);
        timedListener.setExpectingNotification(true);
        timedListener.setExpectedObject(Byte.valueOf((byte) 87));
        producer.fireTimedEvent(eventType, (byte) 87, Double.valueOf(13.02d));
        assertEquals(13.02, timedListener.getReceivedEvent().getTimeStamp(), 0.001);

        nrRemovedListeners = producer.removeAllListeners();
        assertEquals(1, nrRemovedListeners);
    }

    /**
     * Test the EventProducer and EventListener for TimedEvents.
     */
    @Test
    public void testTimedEventPubSub()
    {
        TestEventProducer producer = new TestEventProducer();
        TestTimedEventListener<Double> listener = new TestTimedEventListener<>();
        EventType eventType = new EventType("TEST_TYPE");

        boolean addListenerOK = producer.addListener(listener, eventType);
        assertTrue(addListenerOK);

        String string = "abc123";
        listener.setExpectedObject(string);
        producer.fireEvent(new TimedEvent<Double>(eventType, producer, string, 12.01d));
        assertEquals(string, listener.getReceivedEvent().getContent());
        assertEquals(eventType, listener.getReceivedEvent().getType());
        assertEquals(producer, listener.getReceivedEvent().getSource());
        assertEquals(12.01d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(Boolean.valueOf(true));
        producer.fireTimedEvent(eventType, true, Double.valueOf(12.02d));
        assertEquals(12.02d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);
        listener.setExpectedObject(Boolean.valueOf(false));
        producer.fireTimedEvent(eventType, false, Double.valueOf(12.03d));
        assertEquals(12.03d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(Byte.valueOf((byte) 87));
        producer.fireTimedEvent(eventType, (byte) 87, Double.valueOf(12.04d));
        assertEquals(12.04d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(Short.valueOf((short) -234));
        producer.fireTimedEvent(eventType, (short) -234, Double.valueOf(12.05d));
        assertEquals(12.05d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(Double.valueOf(123.456d));
        producer.fireTimedEvent(eventType, 123.456d, Double.valueOf(12.06d));
        assertEquals(12.06d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(Integer.valueOf(12345));
        producer.fireTimedEvent(eventType, 12345, Double.valueOf(12.07d));
        assertEquals(12.07d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(Long.valueOf(123456L));
        producer.fireTimedEvent(eventType, 123456L, Double.valueOf(12.08d));
        assertEquals(12.08d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

        listener.setExpectedObject(null);
        producer.fireTimedEvent(eventType, null, Double.valueOf(12.09d));
        assertEquals(12.09d, listener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);
    }

    /**
     * Test the EventProducer and EventListener for TimedEvents.
     */
    @Test
    public void testIllegalEventProducer()
    {
        try
        {
            EventProducerInterface illegal = new TestIllegalEventProducer();
            System.err.println("EventProducer with identical events constructed: " + illegal.toString());
            fail("Construction of EventProducer with identical events should have triggered a RuntimeException");
        }
        catch (RuntimeException e)
        {
            // ok
        }
    }

    /**
     * Test the EventProducer for strong and weak references, and for position information.
     * @throws SecurityException on error retrieving listener map
     * @throws NoSuchFieldException on error retrieving listener map
     * @throws IllegalAccessException on error retrieving listener map
     * @throws IllegalArgumentException on error retrieving listener map
     */
    @Test
    public void testEventStrongWeakPos()
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
    {
        TestEventProducer producer = new TestEventProducer();
        TestEventListener listener = new TestEventListener();

        // test illegal parameters and null pointer exceptions in adding a listener
        try
        {
            producer.addListener(null, TestEventProducer.PRODUCER_EVENT_1);
            fail("null listener should have thrown an exception");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }
        boolean addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1, -10, ReferenceType.STRONG);
        assertFalse(addListenerOK);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                producer.addListener(listener, null);
            }
        }, "expected NullPointerException", NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1, null);
            }
        }, "expected NullPointerException", NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1, 0, null);
            }
        }, "expected NullPointerException", NullPointerException.class);

        // test whether weak and strong calls to addListener work, and whether positions can be provided
        assertFalse(producer.hasListeners());
        assertEquals(0, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_1));
        assertEquals(new LinkedHashSet<EventType>(), producer.getEventTypesWithListeners());

        addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1);
        assertTrue(addListenerOK);
        assertTrue(producer.hasListeners());
        assertEquals(1, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_1));
        assertEquals(new LinkedHashSet<EventType>(Arrays.asList(TestEventProducer.PRODUCER_EVENT_1)),
                producer.getEventTypesWithListeners());

        addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_2, ReferenceType.WEAK);
        assertTrue(addListenerOK);
        assertEquals(1, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_1));
        assertEquals(1, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_2));
        assertEquals(
                new LinkedHashSet<EventType>(
                        Arrays.asList(TestEventProducer.PRODUCER_EVENT_1, TestEventProducer.PRODUCER_EVENT_2)),
                producer.getEventTypesWithListeners());

        // check false for adding same listener second time
        addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_2, ReferenceType.WEAK);
        assertFalse(addListenerOK);
        assertEquals(1, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_1));
        assertEquals(1, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_2));
        assertEquals(
                new LinkedHashSet<EventType>(
                        Arrays.asList(TestEventProducer.PRODUCER_EVENT_1, TestEventProducer.PRODUCER_EVENT_2)),
                producer.getEventTypesWithListeners());

        // check LAST_POSITION and FIRST_POSITION
        TestEventListener listener2 = new TestEventListener();
        TestEventListener listener3 = new TestEventListener();
        addListenerOK =
                producer.addListener(listener2, TestEventProducer.PRODUCER_EVENT_2, EventProducerInterface.LAST_POSITION);
        addListenerOK =
                producer.addListener(listener3, TestEventProducer.PRODUCER_EVENT_2, EventProducerInterface.FIRST_POSITION);
        assertEquals(3, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_2));

        // get the underlying listener map
        EventListenerMap map =
                (EventListenerMap) producer.getClass().getSuperclass().getDeclaredField("listeners").get(producer);
        // check whether positions have been inserted okay: listener3 - listener - listener2
        List<Reference<EventListenerInterface>> listenerList = map.get(TestEventProducer.PRODUCER_EVENT_2);
        assertEquals(3, listenerList.size());
        assertEquals(listener3, listenerList.get(0).get());
        assertEquals(listener, listenerList.get(1).get());
        assertEquals(listener2, listenerList.get(2).get());
    }

    /**
     * Test the EventProducer for a weak reference that is removed by the garbage collector.
     * @throws SecurityException on error retrieving listener map
     * @throws NoSuchFieldException on error retrieving listener map
     * @throws IllegalAccessException on error retrieving listener map
     * @throws IllegalArgumentException on error retrieving listener map
     */
    @Test
    public void testEventProducerWeakRemoval()
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
    {
        TestEventProducer producer = new TestEventProducer();
        TestEventListener listener = new TestEventListener();
        boolean addListenerOK = producer.addListener(listener, TestEventProducer.PRODUCER_EVENT_1, ReferenceType.WEAK);
        assertTrue(addListenerOK);
        assertTrue(producer.hasListeners());
        assertEquals(1, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_1));

        // fire an event -- should arrive
        listener.setExpectingNotification(true);
        listener.setExpectedObject(Integer.valueOf(12));
        producer.fireEvent(TestEventProducer.PRODUCER_EVENT_1, 12);

        // get the underlying listener map
        EventListenerMap map =
                (EventListenerMap) producer.getClass().getSuperclass().getDeclaredField("listeners").get(producer);
        List<Reference<EventListenerInterface>> listenerList = map.get(TestEventProducer.PRODUCER_EVENT_1);
        assertEquals(1, listenerList.size());
        Reference<EventListenerInterface> ref = listenerList.get(0);
        // simulate clearing by GC
        Field referent = ref.getClass().getDeclaredField("referent");
        referent.setAccessible(true);
        referent.set(ref, new java.lang.ref.WeakReference<EventListenerInterface>(null));
        referent.setAccessible(false);

        // fire an event -- should not arrive
        listener.setExpectingNotification(false);
        producer.fireEvent(TestEventProducer.PRODUCER_EVENT_1, 34);
        assertFalse(producer.hasListeners());
        assertEquals(0, producer.numberOfListeners(TestEventProducer.PRODUCER_EVENT_1));
    }

    /** */
    protected static class TestEventProducer extends EventProducer
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** */
        public static final EventType PRODUCER_EVENT_1 = new EventType("PRODUCER_EVENT_1");

        /** */
        public static final EventType PRODUCER_EVENT_2 = new EventType("PRODUCER_EVENT_2");

        /** this should be okay. */
        @SuppressWarnings("unused")
        private static final EventType PRODUCER_EVENT_3 = new EventType("PRODUCER_EVENT_1");

        /** this should be okay. */
        @SuppressWarnings("unused")
        private static final EventType PRODUCER_EVENT_4 = new EventType("PRODUCER_EVENT_1");
    }

    /** */
    protected static class TestIllegalEventProducer extends EventProducer
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** */
        public static final EventType PRODUCER_EVENT_1 = new EventType("PRODUCER_EVENT_1");

        /** duplicate static non-private EventType should give error on class construction. */
        public static final EventType PRODUCER_EVENT_2 = new EventType("PRODUCER_EVENT_1");
    }

    /** */
    protected static class TestEventListener implements EventListenerInterface
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** expect notification or not. */
        private boolean expectingNotification = true;

        /** expected object in notify. */
        private Object expectedObject;

        /** received event. */
        private EventInterface receivedEvent;

        /**
         * @param expectingNotification set expectingNotification
         */
        public void setExpectingNotification(final boolean expectingNotification)
        {
            this.expectingNotification = expectingNotification;
        }

        /**
         * @param expectedObject set expectedObject
         */
        public void setExpectedObject(final Object expectedObject)
        {
            this.expectedObject = expectedObject;
        }

        /**
         * @return receivedEvent
         */
        public EventInterface getReceivedEvent()
        {
            return this.receivedEvent;
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event) throws RemoteException
        {
            if (!this.expectingNotification)
            {
                fail("Received event " + event + " unexpectedly");
            }
            this.receivedEvent = event;
            assertEquals(this.expectedObject, event.getContent());
        }

    }

    /**
     * TimedEventListener.
     * @param <C> the comparable time type
     */
    protected static class TestTimedEventListener<C extends Comparable<C> & Serializable> implements EventListenerInterface
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** expect notification or not. */
        private boolean expectingNotification = true;

        /** expected object in notify. */
        private Object expectedObject;

        /** received event. */
        private TimedEvent<C> receivedEvent;

        /**
         * @param expectingNotification set expectingNotification
         */
        public void setExpectingNotification(final boolean expectingNotification)
        {
            this.expectingNotification = expectingNotification;
        }

        /**
         * @param expectedObject set expectedObject
         */
        public void setExpectedObject(final Object expectedObject)
        {
            this.expectedObject = expectedObject;
        }

        /**
         * @return receivedEvent
         */
        public TimedEvent<C> getReceivedEvent()
        {
            return this.receivedEvent;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public void notify(final EventInterface event) throws RemoteException
        {
            if (!this.expectingNotification)
            {
                fail("Received event " + event + " unexpectedly");
            }
            if (!(event instanceof TimedEvent))
            {
                fail("Received event " + event + " is not a TimedEvent");
            }
            this.receivedEvent = (TimedEvent<C>) event;
            assertEquals(this.expectedObject, event.getContent());
        }

    }

}
