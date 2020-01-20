package org.djutils.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.djutils.event.remote.RemoteEventListener;
import org.djutils.event.remote.RemoteEventListenerInterface;
import org.djutils.event.remote.RemoteEventProducer;
import org.djutils.event.remote.RemoteEventProducerInterface;
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
public class RemoteEventTest
{
    /**
     * Test the construction of the RemoteEventListsner and RemoteEventProducer.
     * @throws RemoteException on remote error
     * @throws AlreadyBoundException when producer or listener is already bound in the RMI registry
     */
    @Test
    public void testRemoteEventListenerProducer() throws RemoteException, AlreadyBoundException
    {
        RemoteEventProducerInterface producer = new TestRemoteEventProducer();
        RemoteEventListenerInterface listener = new TestRemoteEventListener();
        assertFalse(producer.hasListeners());
        producer.addListener(listener, TestRemoteEventProducer.REMOTE_EVENT_1);
        assertTrue(producer.hasListeners());
        assertEquals(1, producer.getEventTypesWithListeners().size());

        // clean up the registry
        RMIUtils.closeRegistry(((TestRemoteEventProducer) producer).getRegistry());
    }

    /** */
    protected static class TestRemoteEventProducer extends RemoteEventProducer
    {
        /** */
        private static final long serialVersionUID = 20191230L;

        /** */
        public static final EventType REMOTE_EVENT_1 = new EventType("REMOTE_EVENT_1");

        /** */
        public static final EventType REMOTE_EVENT_2 = new EventType("REMOTE_EVENT_2");

        /**
         * Construct a RemoteEventProducer.
         * @throws RemoteException on error
         * @throws AlreadyBoundException on error
         */
        public TestRemoteEventProducer() throws RemoteException, AlreadyBoundException
        {
            super("localhost", 1099, "producer");
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId() throws RemoteException
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

        /** received event. */
        private EventInterface receivedEvent;

        /**
         * @throws RemoteException on error
         * @throws AlreadyBoundException on error
         */
        public TestRemoteEventListener() throws RemoteException, AlreadyBoundException
        {
            super("localhost", 1099, "listener");
        }

        /**
         * @param expectingNotification set expectingNotification
         */
        public void setExpectingNotification(final boolean expectingNotification)
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
