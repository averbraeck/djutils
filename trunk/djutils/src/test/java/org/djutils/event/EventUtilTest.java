package org.djutils.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.djutils.event.util.EventIterator;
import org.djutils.event.util.EventProducingCollection;
import org.djutils.event.util.EventProducingList;
import org.djutils.event.util.EventProducingSet;
import org.junit.Test;

/**
 * Test the EventProducingCollection, EventProducingList, EventProducingMap, EventProducingSet and EventIterator.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EventUtilTest implements Serializable
{
    /** */
    private static final long serialVersionUID = 20191230L;

    /**
     * Test the EventProducingCollection.
     */
    @Test
    public void testEventProducingCollection()
    {
        EventProducingCollection<String> epc = new EventProducingCollection<>(new LinkedHashSet<>());
        TestEventListener listener = new TestEventListener();
        epc.addListener(listener, EventProducingCollection.OBJECT_ADDED_EVENT);
        epc.addListener(listener, EventProducingCollection.OBJECT_REMOVED_EVENT);
        epc.addListener(listener, EventProducingCollection.OBJECT_CHANGED_EVENT);

        // test add
        listener.setExpectingNotification(true);
        assertTrue(epc.isEmpty());
        boolean ok = epc.add("abc");
        assertTrue(ok);
        assertEquals(epc, listener.getReceivedEvent().getSource());
        assertEquals(EventProducingCollection.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(1), listener.getReceivedEvent().getContent());
        assertFalse(epc.isEmpty());
        ok = epc.add("abc");
        assertFalse(ok);
        assertEquals(epc, listener.getReceivedEvent().getSource());
        assertEquals(EventProducingCollection.OBJECT_CHANGED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(1), listener.getReceivedEvent().getContent());

        // test remove
        ok = epc.remove("abc");
        assertTrue(ok);
        assertEquals(EventProducingCollection.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(0), listener.getReceivedEvent().getContent());
        assertTrue(epc.isEmpty());
        listener.setExpectingNotification(false);
        ok = epc.remove("def");
        assertFalse(ok);
        listener.setExpectingNotification(true);

        // test addAll, size
        ok = epc.addAll(Arrays.asList("a", "b", "c", "d", "e"));
        assertTrue(ok);
        assertEquals(EventProducingCollection.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        assertEquals(5, epc.size());
        ok = epc.addAll(Arrays.asList("b", "e"));
        assertFalse(ok);
        assertEquals(EventProducingCollection.OBJECT_CHANGED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        ok = epc.addAll(Arrays.asList());
        assertFalse(ok);
        listener.setExpectingNotification(true);

        // test removeAll
        epc.removeAll(Arrays.asList("b", "c"));
        assertEquals(EventProducingCollection.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(3), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        epc.removeAll(Arrays.asList());
        listener.setExpectingNotification(true);

        // test retainAll
        epc.retainAll(Arrays.asList("c", "d", "e"));
        assertEquals(EventProducingCollection.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(2), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        epc.retainAll(Arrays.asList("d", "e"));
        listener.setExpectingNotification(true);

        // test contains, containsAll
        assertTrue(epc.contains("d"));
        assertFalse(epc.contains("a"));
        assertTrue(epc.containsAll(Arrays.asList("d", "e")));

        // test toArray
        Object[] arr = epc.toArray();
        String[] stringArr = epc.toArray(new String[] {});
        assertEquals(2, arr.length);
        assertTrue(arr[0].equals("d") || arr[0].equals("e"));
        assertTrue(arr[1].equals("d") || arr[1].equals("e"));
        assertNotEquals(arr[0], arr[1]);
        assertEquals(2, stringArr.length);
        assertTrue(stringArr[0].equals("d") || stringArr[0].equals("e"));
        assertTrue(stringArr[1].equals("d") || stringArr[1].equals("e"));
        assertNotEquals(stringArr[0], stringArr[1]);

        // test clear
        epc.clear();
        assertEquals(EventProducingCollection.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(0), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        epc.clear();
        listener.setExpectingNotification(true);

        // test iterator
        ok = epc.addAll(Arrays.asList("a", "b", "c", "d", "e"));
        assertTrue(ok);
        assertEquals(EventProducingCollection.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        assertEquals(5, epc.size());
        EventIterator<String> eit = epc.iterator();
        assertNotNull(eit);
        assertTrue(eit.hasNext());
        String firstString = eit.next();
        assertTrue(eit.hasNext());
        String secondString = eit.next();
        eit.remove();
        assertEquals(EventProducingCollection.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(4), listener.getReceivedEvent().getContent());
        assertEquals(4, epc.size());
        assertTrue(epc.contains(firstString));
        assertFalse(epc.contains(secondString));

        // clear the collection and remove the listeners
        epc.removeAllListeners();
        epc.clear();
    }

    /**
     * Test the EventProducingSet.
     */
    @Test
    public void testEventProducingSet()
    {
        EventProducingSet<String> eps = new EventProducingSet<>(new LinkedHashSet<>());
        TestEventListener listener = new TestEventListener();
        eps.addListener(listener, EventProducingSet.OBJECT_ADDED_EVENT);
        eps.addListener(listener, EventProducingSet.OBJECT_REMOVED_EVENT);
        eps.addListener(listener, EventProducingSet.OBJECT_CHANGED_EVENT);

        // test add
        listener.setExpectingNotification(true);
        assertTrue(eps.isEmpty());
        boolean ok = eps.add("abc");
        assertTrue(ok);
        assertEquals(eps, listener.getReceivedEvent().getSource());
        assertEquals(EventProducingSet.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(1), listener.getReceivedEvent().getContent());
        assertFalse(eps.isEmpty());
        ok = eps.add("abc");
        assertFalse(ok);
        assertEquals(eps, listener.getReceivedEvent().getSource());
        assertEquals(EventProducingSet.OBJECT_CHANGED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(1), listener.getReceivedEvent().getContent());

        // test remove
        ok = eps.remove("abc");
        assertTrue(ok);
        assertEquals(EventProducingSet.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(0), listener.getReceivedEvent().getContent());
        assertTrue(eps.isEmpty());
        listener.setExpectingNotification(false);
        ok = eps.remove("def");
        assertFalse(ok);
        listener.setExpectingNotification(true);

        // test addAll, size
        ok = eps.addAll(Arrays.asList("a", "b", "c", "d", "e"));
        assertTrue(ok);
        assertEquals(EventProducingSet.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        assertEquals(5, eps.size());
        ok = eps.addAll(Arrays.asList("b", "e"));
        assertFalse(ok);
        assertEquals(EventProducingSet.OBJECT_CHANGED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        ok = eps.addAll(Arrays.asList());
        assertFalse(ok);
        listener.setExpectingNotification(true);

        // test removeAll
        eps.removeAll(Arrays.asList("b", "c"));
        assertEquals(EventProducingSet.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(3), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        eps.removeAll(Arrays.asList());
        listener.setExpectingNotification(true);

        // test retainAll
        eps.retainAll(Arrays.asList("c", "d", "e"));
        assertEquals(EventProducingSet.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(2), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        eps.retainAll(Arrays.asList("d", "e"));
        listener.setExpectingNotification(true);

        // test contains, containsAll
        assertTrue(eps.contains("d"));
        assertFalse(eps.contains("a"));
        assertTrue(eps.containsAll(Arrays.asList("d", "e")));

        // test toArray
        Object[] arr = eps.toArray();
        String[] stringArr = eps.toArray(new String[] {});
        assertEquals(2, arr.length);
        assertTrue(arr[0].equals("d") || arr[0].equals("e"));
        assertTrue(arr[1].equals("d") || arr[1].equals("e"));
        assertNotEquals(arr[0], arr[1]);
        assertEquals(2, stringArr.length);
        assertTrue(stringArr[0].equals("d") || stringArr[0].equals("e"));
        assertTrue(stringArr[1].equals("d") || stringArr[1].equals("e"));
        assertNotEquals(stringArr[0], stringArr[1]);

        // test clear
        eps.clear();
        assertEquals(EventProducingSet.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(0), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        eps.clear();
        listener.setExpectingNotification(true);

        // test iterator
        ok = eps.addAll(Arrays.asList("a", "b", "c", "d", "e"));
        assertTrue(ok);
        assertEquals(EventProducingSet.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        assertEquals(5, eps.size());
        EventIterator<String> eit = eps.iterator();
        assertNotNull(eit);
        assertTrue(eit.hasNext());
        String firstString = eit.next();
        assertTrue(eit.hasNext());
        String secondString = eit.next();
        eit.remove();
        assertEquals(EventProducingSet.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(4), listener.getReceivedEvent().getContent());
        assertEquals(4, eps.size());
        assertTrue(eps.contains(firstString));
        assertFalse(eps.contains(secondString));

        // clear the collection and remove the listeners
        eps.removeAllListeners();
        eps.clear();
    }

    /**
     * Test the EventProducingList.
     */
    @Test
    public void testEventProducingList()
    {
        EventProducingList<String> epl = new EventProducingList<>(new ArrayList<>());
        TestEventListener listener = new TestEventListener();
        epl.addListener(listener, EventProducingList.OBJECT_ADDED_EVENT);
        epl.addListener(listener, EventProducingList.OBJECT_REMOVED_EVENT);
        epl.addListener(listener, EventProducingList.OBJECT_CHANGED_EVENT);

        // test add, remove(int)
        listener.setExpectingNotification(true);
        assertTrue(epl.isEmpty());
        assertEquals(0, epl.size());
        boolean ok = epl.add("abc");
        assertTrue(ok);
        assertEquals(epl, listener.getReceivedEvent().getSource());
        assertEquals(EventProducingList.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(1), listener.getReceivedEvent().getContent());
        assertFalse(epl.isEmpty());
        ok = epl.add("abc");
        assertTrue(ok); // duplicates allowed in list
        assertEquals(epl, listener.getReceivedEvent().getSource());
        assertEquals(EventProducingList.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(2), listener.getReceivedEvent().getContent());
        epl.remove(1);
        assertEquals(EventProducingList.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(1), listener.getReceivedEvent().getContent());
        assertEquals(1, epl.size());
        
        // test remove(object)
        ok = epl.remove("abc");
        assertTrue(ok);
        assertEquals(EventProducingList.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(0), listener.getReceivedEvent().getContent());
        assertTrue(epl.isEmpty());
        listener.setExpectingNotification(false);
        ok = epl.remove("def");
        assertFalse(ok);
        listener.setExpectingNotification(true);

        // test addAll, set, size
        ok = epl.addAll(Arrays.asList("a", "b", "c", "d", "e"));
        assertTrue(ok);
        assertEquals(EventProducingList.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        assertEquals(5, epl.size());
        String old = epl.set(0, "aa");
        assertEquals("a", old);
        assertEquals(EventProducingList.OBJECT_CHANGED_EVENT, listener.getReceivedEvent().getType());
        listener.setExpectingNotification(false);
        ok = epl.addAll(Arrays.asList());
        assertFalse(ok);
        listener.setExpectingNotification(true);

        // test removeAll
        epl.removeAll(Arrays.asList("b", "c"));
        assertEquals(EventProducingList.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(3), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        epl.removeAll(Arrays.asList());
        listener.setExpectingNotification(true);

        // test retainAll
        epl.retainAll(Arrays.asList("c", "d", "e"));
        assertEquals(EventProducingList.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(2), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        epl.retainAll(Arrays.asList("d", "e"));
        listener.setExpectingNotification(true);

        // test contains, containsAll
        assertTrue(epl.contains("d"));
        assertFalse(epl.contains("a"));
        assertTrue(epl.containsAll(Arrays.asList("d", "e")));

        // test toArray
        Object[] arr = epl.toArray();
        String[] stringArr = epl.toArray(new String[] {});
        assertEquals(2, arr.length);
        assertTrue(arr[0].equals("d") || arr[0].equals("e"));
        assertTrue(arr[1].equals("d") || arr[1].equals("e"));
        assertNotEquals(arr[0], arr[1]);
        assertEquals(2, stringArr.length);
        assertTrue(stringArr[0].equals("d") || stringArr[0].equals("e"));
        assertTrue(stringArr[1].equals("d") || stringArr[1].equals("e"));
        assertNotEquals(stringArr[0], stringArr[1]);

        // test clear
        epl.clear();
        assertEquals(EventProducingList.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(0), listener.getReceivedEvent().getContent());
        listener.setExpectingNotification(false);
        epl.clear();
        listener.setExpectingNotification(true);

        // test iterator
        ok = epl.addAll(Arrays.asList("a", "b", "c", "d", "e"));
        assertTrue(ok);
        assertEquals(EventProducingList.OBJECT_ADDED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(5), listener.getReceivedEvent().getContent());
        assertEquals(5, epl.size());
        EventIterator<String> eit = epl.iterator();
        assertNotNull(eit);
        assertTrue(eit.hasNext());
        String firstString = eit.next();
        assertTrue(eit.hasNext());
        String secondString = eit.next();
        eit.remove();
        assertEquals(EventProducingList.OBJECT_REMOVED_EVENT, listener.getReceivedEvent().getType());
        assertEquals(Integer.valueOf(4), listener.getReceivedEvent().getContent());
        assertEquals(4, epl.size());
        assertTrue(epl.contains(firstString));
        assertFalse(epl.contains(secondString));

        // clear the collection and remove the listeners
        epl.removeAllListeners();
        epl.clear();
    }

    /** */
    protected static class TestEventListener implements EventListenerInterface
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** expect notification or not. */
        private boolean expectingNotification = true;

        /** received event. */
        private EventInterface receivedEvent;

        /**
         * @param expectingNotification set expectingNotification
         */
        public void setExpectingNotification(boolean expectingNotification)
        {
            this.expectingNotification = expectingNotification;
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
        }
    }

}
