package org.djutils.event.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducerInterface;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;
import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;
import org.djutils.exceptions.Try;
import org.djutils.rmi.RMIUtils;
import org.junit.Test;

/**
 * RemoteEventTest makes some very basic tests for the RemoteEventListener and RemoteEventProducer.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class RemoteEventPubSubTest
{
    /**
     * Test the construction of the RemoteEventListsner and RemoteEventProducer.
     * @throws RemoteException on remote error
     * @throws AlreadyBoundException when producer or listener is already bound in the RMI registry
     * @throws MalformedURLException on URL error
     */
    @Test
    public void testRemoteEventListenerProducer() throws RemoteException, AlreadyBoundException, MalformedURLException
    {
        TestRemoteEventProducer producer = new TestRemoteEventProducer();
        try
        {
            TestRemoteEventListener listener = new TestRemoteEventListener("listener");
            assertFalse(producer.hasListeners());
            assertEquals(0, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
            assertEquals(0, producer.getEventTypesWithListeners().size());
            assertEquals(0, producer.getListenerReferences(TestRemoteEventProducer.REMOTE_EVENT_1).size());
            boolean addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1);
            assertTrue(addListenerOK);
            assertTrue(producer.hasListeners());
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
            assertEquals(1, producer.getEventTypesWithListeners().size());
            assertEquals(1, producer.getListenerReferences(TestRemoteEventProducer.REMOTE_EVENT_1).size());
            assertEquals(listener, producer.getListenerReferences(TestRemoteEventProducer.REMOTE_EVENT_1).get(0).get());

            String string = "abc123";
            listener.setExpectedObject(string);
            producer.fireEvent(new Event(TestRemoteEventProducer.REMOTE_EVENT_1, producer, string));
            assertEquals(string, listener.getReceivedEvent().getContent());
            assertEquals(TestRemoteEventProducer.REMOTE_EVENT_1, listener.getReceivedEvent().getType());
            assertEquals(producer, listener.getReceivedEvent().getSourceId());

            listener.setExpectedObject(Boolean.valueOf(true));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, true);
            listener.setExpectedObject(Boolean.valueOf(false));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, false);

            listener.setExpectedObject(Byte.valueOf((byte) 87));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, (byte) 87);

            listener.setExpectedObject(Short.valueOf((short) -234));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, (short) -234);

            listener.setExpectedObject(Double.valueOf(123.456d));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 123.456d);

            listener.setExpectedObject(Integer.valueOf(12345));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 12345);

            listener.setExpectedObject(Long.valueOf(123456L));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 123456L);

            listener.setExpectedObject("abcde");
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, "abcde");

            listener.setExpectedObject(null);
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1);

            // remove listener tests
            producer.removeListener(listener, TestRemoteEventProducer.REMOTE_EVENT_2);
            listener.setExpectedObject(Byte.valueOf((byte) 87));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, (byte) 87);

            producer.removeListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1);
            listener.setExpectingNotification(false);
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 12345);

            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_2);
            assertTrue(addListenerOK);
            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1);
            assertTrue(addListenerOK);
            listener.setExpectingNotification(true);
            listener.setExpectedObject(Double.valueOf(123.456d));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 123.456d);
            assertEquals(TestRemoteEventProducer.REMOTE_EVENT_1, listener.getReceivedEvent().getType());
            listener.setExpectedObject(Double.valueOf(234.567d));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_2, 234.567d);
            assertEquals(TestRemoteEventProducer.REMOTE_EVENT_2, listener.getReceivedEvent().getType());

            int nrRemovedListeners = producer.removeAllListeners();
            assertEquals(2, nrRemovedListeners);
            listener.setExpectingNotification(false);
            listener.setExpectedObject(Byte.valueOf((byte) 87));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, (byte) 87);
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_2, 12345);

            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_2);
            assertTrue(addListenerOK);
            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1);
            assertTrue(addListenerOK);
            listener.setExpectingNotification(true);
            listener.setExpectedObject(Double.valueOf(123.456d));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 123.456d);
            assertEquals(TestRemoteEventProducer.REMOTE_EVENT_1, listener.getReceivedEvent().getType());
            listener.setExpectedObject(Double.valueOf(234.567d));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_2, 234.567d);
            assertEquals(TestRemoteEventProducer.REMOTE_EVENT_2, listener.getReceivedEvent().getType());

            TestTimedRemoteEventListener<Double> timedListener = new TestTimedRemoteEventListener<>("timedListener");
            addListenerOK = producer.addListener(timedListener, TestRemoteEventProducer.REMOTE_EVENT_1);
            assertTrue(addListenerOK);
            timedListener.setExpectingNotification(true);
            timedListener.setExpectedObject(Double.valueOf(12.34d));
            listener.setExpectedObject(Double.valueOf(12.34d));
            producer.fireEvent(
                    new TimedEvent<Double>(TestRemoteEventProducer.REMOTE_EVENT_1, producer, Double.valueOf(12.34d), 12.01d));
            assertEquals(12.01, timedListener.getReceivedEvent().getTimeStamp(), 0.001);

            nrRemovedListeners = producer.removeAllListeners(TestRemoteEventListener.class);
            assertEquals(2, nrRemovedListeners);
            listener.setExpectingNotification(false);
            timedListener.setExpectingNotification(true);
            timedListener.setExpectedObject(Byte.valueOf((byte) 87));
            producer.fireTimedEvent(TestRemoteEventProducer.REMOTE_EVENT_1, (byte) 87, Double.valueOf(13.02d));
            assertEquals(13.02, timedListener.getReceivedEvent().getTimeStamp(), 0.001);

            nrRemovedListeners = producer.removeAllListeners();
            assertEquals(1, nrRemovedListeners);
        }
        catch (RemoteException | AlreadyBoundException exception)
        {
            throw exception;
        }
        finally
        {
            // clean up the registry
            RMIUtils.closeRegistry(producer.getRegistry());
        }
    }

    /**
     * Test the construction of the RemoteEventListsner and RemoteEventProducer.
     * @throws RemoteException on remote error
     * @throws AlreadyBoundException when producer or listener is already bound in the RMI registry
     * @throws MalformedURLException on URL error
     */
    @Test
    public void testTimedRemoteEventListenerProducer() throws RemoteException, AlreadyBoundException, MalformedURLException
    {
        TestRemoteEventProducer producer = new TestRemoteEventProducer();
        try
        {
            TestTimedRemoteEventListener<Double> timedListener = new TestTimedRemoteEventListener<>("timedListener");
            EventType eventType = new EventType("TEST_TYPE", null);

            boolean addListenerOK = producer.addListener(timedListener, eventType);
            assertTrue(addListenerOK);

            String string = "abc123";
            timedListener.setExpectedObject(string);
            producer.fireEvent(new TimedEvent<Double>(eventType, producer, string, 12.01d));
            assertEquals(string, timedListener.getReceivedEvent().getContent());
            assertEquals(eventType, timedListener.getReceivedEvent().getType());
            assertEquals(producer, timedListener.getReceivedEvent().getSourceId());
            assertEquals(12.01d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(Boolean.valueOf(true));
            producer.fireTimedEvent(eventType, true, Double.valueOf(12.02d));
            assertEquals(12.02d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);
            timedListener.setExpectedObject(Boolean.valueOf(false));
            producer.fireTimedEvent(eventType, false, Double.valueOf(12.03d));
            assertEquals(12.03d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(Byte.valueOf((byte) 87));
            producer.fireTimedEvent(eventType, (byte) 87, Double.valueOf(12.04d));
            assertEquals(12.04d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(Short.valueOf((short) -234));
            producer.fireTimedEvent(eventType, (short) -234, Double.valueOf(12.05d));
            assertEquals(12.05d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(Double.valueOf(123.456d));
            producer.fireTimedEvent(eventType, 123.456d, Double.valueOf(12.06d));
            assertEquals(12.06d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(Integer.valueOf(12345));
            producer.fireTimedEvent(eventType, 12345, Double.valueOf(12.07d));
            assertEquals(12.07d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(Long.valueOf(123456L));
            producer.fireTimedEvent(eventType, 123456L, Double.valueOf(12.08d));
            assertEquals(12.08d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);

            timedListener.setExpectedObject(null);
            producer.fireTimedEvent(eventType, null, Double.valueOf(12.09d));
            assertEquals(12.09d, timedListener.getReceivedEvent().getTimeStamp().doubleValue(), 0.001);
        }
        catch (RemoteException | AlreadyBoundException | MalformedURLException exception)
        {
            throw exception;
        }
        finally
        {
            // clean up the registry
            RMIUtils.closeRegistry(producer.getRegistry());
        }
    }

    /**
     * Test the EventProducer for strong and weak references, and for position information.
     * @throws SecurityException on error retrieving listener map
     * @throws NoSuchFieldException on error retrieving listener map
     * @throws IllegalAccessException on error retrieving listener map
     * @throws IllegalArgumentException on error retrieving listener map
     * @throws RemoteException on network exception
     * @throws AlreadyBoundException when RMI registry not cleaned
     */
    @Test
    public void testEventStrongWeakPos() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
            SecurityException, RemoteException, AlreadyBoundException
    {
        TestRemoteEventProducer producer = new TestRemoteEventProducer();
        try
        {
            TestRemoteEventListener listener = new TestRemoteEventListener("listener");

            // test illegal parameters and null pointer exceptions in adding a listener
            try
            {
                producer.addListener(null, TestRemoteEventProducer.REMOTE_EVENT_1);
                fail("null listener should have thrown an exception");
            }
            catch (NullPointerException npe)
            {
                // Ignore expected exception
            }
            boolean addListenerOK =
                    producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1, -10, ReferenceType.STRONG);
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
                    producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1, null);
                }
            }, "expected NullPointerException", NullPointerException.class);
            Try.testFail(new Try.Execution()
            {
                @Override
                public void execute() throws Throwable
                {
                    producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1, 0, null);
                }
            }, "expected NullPointerException", NullPointerException.class);

            // test whether weak and strong calls to addListener work, and whether positions can be provided
            assertFalse(producer.hasListeners());
            assertEquals(0, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
            assertEquals(new LinkedHashSet<EventType>(), producer.getEventTypesWithListeners());

            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1);
            assertTrue(addListenerOK);
            assertTrue(producer.hasListeners());
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
            assertEquals(new LinkedHashSet<EventType>(Arrays.asList(TestRemoteEventProducer.REMOTE_EVENT_1)),
                    producer.getEventTypesWithListeners());

            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_2, ReferenceType.WEAK);
            assertTrue(addListenerOK);
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_2));
            assertEquals(
                    new LinkedHashSet<EventType>(
                            Arrays.asList(TestRemoteEventProducer.REMOTE_EVENT_1, TestRemoteEventProducer.REMOTE_EVENT_2)),
                    producer.getEventTypesWithListeners());

            // check false for adding same listener second time
            addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_2, ReferenceType.WEAK);
            assertFalse(addListenerOK);
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_2));
            assertEquals(
                    new LinkedHashSet<EventType>(
                            Arrays.asList(TestRemoteEventProducer.REMOTE_EVENT_1, TestRemoteEventProducer.REMOTE_EVENT_2)),
                    producer.getEventTypesWithListeners());

            // check LAST_POSITION and FIRST_POSITION
            TestRemoteEventListener listener2 = new TestRemoteEventListener("listener2");
            TestRemoteEventListener listener3 = new TestRemoteEventListener("listener3");
            addListenerOK = producer.addListener(listener2, TestRemoteEventProducer.REMOTE_EVENT_2,
                    EventProducerInterface.LAST_POSITION);
            addListenerOK = producer.addListener(listener3, TestRemoteEventProducer.REMOTE_EVENT_2,
                    EventProducerInterface.FIRST_POSITION);
            assertEquals(3, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_2));

            // check whether positions have been inserted okay: listener3 - listener - listener2
            List<Reference<EventListenerInterface>> listenerList =
                    producer.getListenerReferences(TestRemoteEventProducer.REMOTE_EVENT_2);
            assertEquals(3, listenerList.size());
            assertEquals(listener3, listenerList.get(0).get());
            assertEquals(listener, listenerList.get(1).get());
            assertEquals(listener2, listenerList.get(2).get());
        }
        catch (RemoteException | AlreadyBoundException exception)
        {
            throw exception;
        }
        finally
        {
            // clean up the registry
            RMIUtils.closeRegistry(producer.getRegistry());
        }
    }

    /**
     * Test the EventProducer for a weak reference that is removed by the garbage collector.
     * @throws SecurityException on error retrieving listener map
     * @throws NoSuchFieldException on error retrieving listener map
     * @throws IllegalAccessException on error retrieving listener map
     * @throws IllegalArgumentException on error retrieving listener map
     * @throws RemoteException on network exception
     * @throws AlreadyBoundException when RMI registry not cleaned
     */
    @Test
    public void testEventProducerWeakRemoval() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
            SecurityException, RemoteException, AlreadyBoundException
    {
        TestRemoteEventProducer producer = new TestRemoteEventProducer();
        try
        {
            TestRemoteEventListener listener = new TestRemoteEventListener("listener");
            boolean addListenerOK = producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1, ReferenceType.WEAK);
            assertTrue(addListenerOK);
            assertTrue(producer.hasListeners());
            assertEquals(1, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));

            // fire an event -- should arrive
            listener.setExpectingNotification(true);
            listener.setExpectedObject(Integer.valueOf(12));
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 12);

            List<Reference<EventListenerInterface>> listenerList =
                    producer.getListenerReferences(TestRemoteEventProducer.REMOTE_EVENT_1);
            assertEquals(1, listenerList.size());
            Reference<EventListenerInterface> ref = listenerList.get(0);
            // simulate clearing by GC (the list is a safe copy, but the reference is original)
            Field referent = ref.getClass().getDeclaredField("referent");
            referent.setAccessible(true);
            referent.set(ref, new java.lang.ref.WeakReference<EventListenerInterface>(null));
            referent.setAccessible(false);

            // fire an event -- should not arrive
            listener.setExpectingNotification(false);
            producer.fireEvent(TestRemoteEventProducer.REMOTE_EVENT_1, 34);
            assertFalse(producer.hasListeners());
            assertEquals(0, producer.numberOfListeners(TestRemoteEventProducer.REMOTE_EVENT_1));
        }
        catch (RemoteException | AlreadyBoundException exception)
        {
            throw exception;
        }
        finally
        {
            // clean up the registry
            RMIUtils.closeRegistry(producer.getRegistry());
        }
    }

    /** */
    protected static class TestRemoteEventProducer extends RemoteEventProducer
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** */
        public static final EventType REMOTE_EVENT_1 = new EventType("REMOTE_EVENT_1", null);

        /** */
        public static final EventType REMOTE_EVENT_2 = new EventType("REMOTE_EVENT_2", null);

        /**
         * Construct a RemoteEventProducer.
         * @throws RemoteException on error
         * @throws AlreadyBoundException on error
         */
        public TestRemoteEventProducer() throws RemoteException, AlreadyBoundException
        {
            super("127.0.0.1", 1099, "producer");
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId()
        {
            return "producer";
        }
    }

    /** */
    protected static class TestRemoteEventListener extends RemoteEventListener
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
         * @param key String; the key under which the listener will be registered in RMI
         * @throws RemoteException on error
         * @throws AlreadyBoundException on error
         */
        public TestRemoteEventListener(final String key) throws RemoteException, AlreadyBoundException
        {
            super("localhost", 1099, key);
        }

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
     * TimedRemoteEventListener.
     * @param <C> the comparable time type
     */
    protected static class TestTimedRemoteEventListener<C extends Comparable<C> & Serializable> extends RemoteEventListener
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
         * @param key String; key used to bind to the RMI registry
         * @throws RemoteException on error
         * @throws AlreadyBoundException on error
         * @throws MalformedURLException on URL error
         */
        public TestTimedRemoteEventListener(final String key)
                throws RemoteException, AlreadyBoundException, MalformedURLException
        {
            super(new URL("http://127.0.0.1:1099"), key);
        }

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
