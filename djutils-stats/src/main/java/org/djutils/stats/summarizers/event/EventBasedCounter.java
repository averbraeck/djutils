package org.djutils.stats.summarizers.event;

import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventListenerMap;
import org.djutils.event.EventProducer;
import org.djutils.event.LocalEventProducer;
import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.Counter;

/**
 * The Counter class defines a statistics event counter. It embeds an EventProducer so it can keep listeners informed about new
 * observations, and it listens to external events to be able to receive observations, in addition to the register(...) method.
 * <p>
 * Copyright (c) 2002-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class EventBasedCounter extends Counter implements EventProducer, EventListener
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The embedded EventProducer. */
    private EventProducer eventProducer = null;

    /**
     * Construct a new EventBasedCounter.
     * @param description the description for this counter
     */
    public EventBasedCounter(final String description)
    {
        this(description, new LocalEventProducer());
    }

    /**
     * Construct a new EventBasedCounter with a specific EventProducer, e.g. a remote one.
     * @param description the description for this counter
     * @param eventProducer the EventProducer to embed and use in this statistic
     */
    public EventBasedCounter(final String description, final EventProducer eventProducer)
    {
        super(description);
        Throw.whenNull(eventProducer, "eventProducer cannot be null");
        this.eventProducer = eventProducer;
    }

    @Override
    public EventListenerMap getEventListenerMap() throws RemoteException
    {
        return this.eventProducer.getEventListenerMap();
    }

    @Override
    public void initialize()
    {
        super.initialize();
        if (this.eventProducer != null)
        {
            try
            {
                this.eventProducer.fireEvent(StatisticsEvents.INITIALIZED_EVENT);
            }
            catch (RemoteException exception)
            {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void notify(final Event event)
    {
        long value = 1;
        if (event.getContent() instanceof Number)
        {
            value = Math.round(((Number) event.getContent()).doubleValue());
        }
        else
        {
            throw new IllegalArgumentException("event content for counter not a number but of type " + event.getClass());
        }
        register(value);
    }

    @Override
    public long register(final long value)
    {
        super.register(value);
        try
        {
            if (hasListeners())
            {
                this.eventProducer.fireEvent(StatisticsEvents.OBSERVATION_ADDED_EVENT, value);
                fireEvents();
            }
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);
        }
        return value;
    }

    /**
     * Method that can be overridden to fire own events or additional events when registering an observation.
     * @throws RemoteException on network error
     */
    protected void fireEvents() throws RemoteException
    {
        this.eventProducer.fireEvent(StatisticsEvents.N_EVENT, getN());
        this.eventProducer.fireEvent(StatisticsEvents.COUNT_EVENT, getCount());
    }

}
