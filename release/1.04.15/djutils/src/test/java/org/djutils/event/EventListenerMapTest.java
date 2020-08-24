package org.djutils.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.djutils.event.ref.Reference;
import org.djutils.event.ref.StrongReference;
import org.djutils.event.ref.WeakReference;
import org.djutils.event.remote.RemoteEventListener;
import org.djutils.rmi.RMIUtils;
import org.junit.Test;

/**
 * Test the EventListenerMap.
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
public class EventListenerMapTest implements Serializable
{
    /** */
    private static final long serialVersionUID = 20191230L;

    /**
     * Test the EventListenerMap.
     */
    @SuppressWarnings("checkstyle:methodlength")
    @Test
    public void testEventListenerMap()
    {
        EventType eventType1 = new EventType("EVENT_TYPE1", null);
        EventType eventType2 = new EventType("EVENT_TYPE2", null);
        EventListenerInterface el1 = new TestEventListener();
        Reference<EventListenerInterface> sref1 = new StrongReference<>(el1);
        EventListenerInterface el2 = new TestEventListener();
        Reference<EventListenerInterface> sref2 = new StrongReference<>(el2);
        EventListenerInterface el3 = new TestEventListener();
        Reference<EventListenerInterface> wref3 = new WeakReference<>(el3);
        EventListenerInterface el4 = new TestEventListener();
        Reference<EventListenerInterface> wref4 = new WeakReference<>(el4);
        Reference<EventListenerInterface> sref4 = new StrongReference<>(el4);

        // test size(), isEmpty(), put()
        EventListenerMap elm = new EventListenerMap();
        assertEquals(0, elm.size());
        assertTrue(elm.isEmpty());
        assertFalse(elm.containsKey(eventType1));
        List<Reference<EventListenerInterface>> list1 = new ArrayList<>();
        list1.add(sref1);
        List<Reference<EventListenerInterface>> putResult = elm.put(eventType1, list1);
        assertNull(putResult);
        assertEquals(1, elm.size());
        assertFalse(elm.isEmpty());
        putResult = elm.put(eventType1, list1);
        assertEquals(1, elm.size());
        assertEquals(putResult, list1);
        List<Reference<EventListenerInterface>> list2 = new ArrayList<>();
        list2.add(sref2);
        list2.add(wref3);
        elm.put(eventType2, list2);
        assertEquals(2, elm.size());

        // test keySet()
        Set<EventType> keySet = elm.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(eventType1));
        assertTrue(keySet.contains(eventType2));
        assertFalse(keySet.contains(new EventType("EVENT_TYPE3", null)));

        // test containsKey()
        assertTrue(elm.containsKey(eventType1));
        assertTrue(elm.containsKey(eventType2));
        assertFalse(elm.containsKey(new EventType("EVENT_TYPE3", null)));

        // test containsValue() for Reference and Listener
        assertTrue(elm.containsValue(el1));
        assertTrue(elm.containsValue(el2));
        assertTrue(elm.containsValue(el3));
        assertFalse(elm.containsValue(el4));
        assertTrue(elm.containsValue(sref1));
        assertTrue(elm.containsValue(sref2));
        assertTrue(elm.containsValue(wref3));
        assertFalse(elm.containsValue(sref4));
        assertFalse(elm.containsValue(wref4));

        // test values()
        Collection<List<Reference<EventListenerInterface>>> values = elm.values();
        assertEquals(2, values.size());
        Iterator<List<Reference<EventListenerInterface>>> vit = values.iterator();
        List<Reference<EventListenerInterface>> v1 = vit.next();
        List<Reference<EventListenerInterface>> v2 = vit.next();
        assertTrue((v1.size() == 1 && v2.size() == 2) || (v1.size() == 2 && v2.size() == 1));

        // test entrySet()
        Set<Map.Entry<EventType, List<Reference<EventListenerInterface>>>> entrySet = elm.entrySet();
        assertEquals(2, entrySet.size());

        // test putAll()
        EventListenerMap elm2 = new EventListenerMap();
        elm2.putAll(elm);
        assertEquals(elm.size(), elm2.size());
        assertEquals(elm.keySet(), elm2.keySet());

        // change something in the underlying list and see if the map remains unaffected
        list1.remove(0);
        assertTrue((v1.size() == 1 && v2.size() == 2) || (v1.size() == 2 && v2.size() == 1));
        list1.add(sref1);

        // test readObject() and writeObject()
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(elm);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            EventListenerMap elm3 = (EventListenerMap) ois.readObject();
            assertEquals(elm.size(), elm3.size());
            Set<String> names0 = new HashSet<>();
            elm.keySet().forEach((e) -> names0.add(e.getName()));
            Set<String> names3 = new HashSet<>();
            elm3.keySet().forEach((e) -> names3.add(e.getName()));
            assertEquals(names0, names3);
            Collection<List<Reference<EventListenerInterface>>> values3 = elm3.values();
            assertEquals(2, values3.size());
            Iterator<List<Reference<EventListenerInterface>>> vit3 = values3.iterator();
            List<Reference<EventListenerInterface>> v31 = vit3.next();
            List<Reference<EventListenerInterface>> v32 = vit3.next();
            assertTrue((v31.size() == 1 && v32.size() == 2) || (v31.size() == 2 && v32.size() == 1));
            baos.close();
            oos.close();
            bais.close();
            ois.close();
        }
        catch (IOException | ClassNotFoundException exception)
        {
            fail(exception.getMessage());
        }

        // test readObject() and writeObject() with one RemoteEventListener (that should not be copied)
        try
        {
            EventType remoteEventType = new EventType("REMOTE_EVENT_TYPE", null);
            List<Reference<EventListenerInterface>> remoteList = new ArrayList<>();
            TestRemoteEventListener remoteEventListener = new TestRemoteEventListener();
            remoteList.add(new WeakReference<EventListenerInterface>(remoteEventListener));
            elm.put(remoteEventType, remoteList);
            assertEquals(3, elm.size());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(elm);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            EventListenerMap elm3 = (EventListenerMap) ois.readObject();
            assertEquals(2, elm3.size());
            assertEquals(3, elm.size());
            elm.remove(remoteEventType);
            Set<String> names0 = new HashSet<>();
            elm.keySet().forEach((e) -> names0.add(e.getName()));
            Set<String> names3 = new HashSet<>();
            elm3.keySet().forEach((e) -> names3.add(e.getName()));
            assertEquals(names0, names3);
            Collection<List<Reference<EventListenerInterface>>> values3 = elm3.values();
            assertEquals(2, values3.size());
            Iterator<List<Reference<EventListenerInterface>>> vit3 = values3.iterator();
            List<Reference<EventListenerInterface>> v31 = vit3.next();
            List<Reference<EventListenerInterface>> v32 = vit3.next();
            assertTrue((v31.size() == 1 && v32.size() == 2) || (v31.size() == 2 && v32.size() == 1));
            baos.close();
            oos.close();
            bais.close();
            ois.close();
            RMIUtils.closeRegistry(remoteEventListener.getRegistry());
        }
        catch (IOException | ClassNotFoundException | AlreadyBoundException exception)
        {
            fail(exception.getMessage());
        }

        // test get()
        List<Reference<EventListenerInterface>> getList = elm.get(eventType2);
        assertEquals(2, elm.size());
        assertEquals(2, getList.size());
        getList = elm.get(eventType1);
        assertEquals(2, elm.size());
        assertEquals(1, getList.size());
        getList = elm.get(new EventType("EVENT_TYPE3", null));
        assertNull(getList);

        // test remove() and see if the underlying and copied data structures remain unaffected
        List<Reference<EventListenerInterface>> removedList = elm.remove(eventType2);
        assertEquals(1, elm.size());
        assertEquals(2, removedList.size());
        assertEquals(list2, removedList);
        assertEquals(2, elm2.size());
        assertEquals(2, values.size());
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(eventType1));
        assertTrue(keySet.contains(eventType2));
        assertTrue(elm.keySet().contains(eventType1));
        assertFalse(elm.keySet().contains(eventType2));
        // the entrySet should be affected as it is the only infrastructure that is not a safe copy
        assertEquals(1, entrySet.size());

        // test clear() and see if the underlying and copied data structures remain unaffected
        elm.clear();
        assertEquals(0, elm.size());
        assertEquals(2, elm2.size());
        assertEquals(2, values.size());
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(eventType1));
        assertTrue(keySet.contains(eventType2));
        assertFalse(elm.keySet().contains(eventType1));
        assertFalse(elm.keySet().contains(eventType2));
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

    /** */
    protected static class TestRemoteEventListener extends RemoteEventListener
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /**
         * @throws RemoteException on error
         * @throws AlreadyBoundException on error
         */
        public TestRemoteEventListener() throws RemoteException, AlreadyBoundException
        {
            super("localhost", 1099, "testListener");
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event) throws RemoteException
        {
            // tagging method
        }
    }

}