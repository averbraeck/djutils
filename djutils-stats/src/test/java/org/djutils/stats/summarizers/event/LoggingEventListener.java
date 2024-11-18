package org.djutils.stats.summarizers.event;

import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventListener;

/**
 * LoggingEventListener logs the last event that was received and the number of received events so they can be checked. <br>
 * <br>
 * Copyright (c) 2020-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LoggingEventListener implements EventListener
{
    /** */
    private static final long serialVersionUID = 1L;

    /** Last received event. */
    private Event lastEvent;

    /** Nummber of received events. */
    private int numberOfEvents = 0;

    /** set the last event to null. */
    public void initialize()
    {
        this.lastEvent = null;
    }

    @Override
    public void notify(final Event event) throws RemoteException
    {
        this.lastEvent = event;
        this.numberOfEvents++;
    }

    /**
     * @return the last received event
     */
    public Event getLastEvent()
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
