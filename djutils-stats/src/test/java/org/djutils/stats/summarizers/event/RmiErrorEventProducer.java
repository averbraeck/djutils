package org.djutils.stats.summarizers.event;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.djutils.event.EventListenerMap;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;

/**
 * EventProducer that throws an error on removeListener().
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RmiErrorEventProducer implements EventProducer
{
    /** */
    private static final long serialVersionUID = 1L;

    /** The collection of interested listeners. */
    private EventListenerMap eventListenerMap = new EventListenerMap();

    @Override
    public EventListenerMap getEventListenerMap()
    {
        return this.eventListenerMap;
    }

    @Override
    public void fireEvent(final EventType eventType) throws RemoteException
    {
        throw new RemoteException("planned");
    }

    @Override
    public void fireEvent(final EventType eventType, final Serializable value) throws RemoteException
    {
        throw new RemoteException("planned");
    }

}
