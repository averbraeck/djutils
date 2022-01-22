package org.djutils.stats.summarizers.event;

import java.rmi.RemoteException;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;

/**
 * LoggingEventListener logs the last event that was received and the number of received events so they can be checked.
 * <br><br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LoggingEventListener implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;
    
    /** Last received event. */
    private EventInterface lastEvent;
    
    /** Nummber of received events. */
    private int numberOfEvents = 0;
    
    /** set the last event to null. */
    public void initialize()
    {
        this.lastEvent = null;
    }
    
    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        this.lastEvent = event;
        this.numberOfEvents++;
    }

    /**
     * @return EventInterface; the last received event
     */
    public EventInterface getLastEvent()
    {
        return this.lastEvent;
    }

    /**
     * @return received numberOfEvents
     */
    public int getNumberOfEvents()
    {
        return this.numberOfEvents;
    }
    
}

