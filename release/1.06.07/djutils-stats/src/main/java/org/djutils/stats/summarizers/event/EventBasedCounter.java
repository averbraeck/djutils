package org.djutils.stats.summarizers.event;

import java.io.Serializable;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.stats.summarizers.Counter;
import org.djutils.stats.summarizers.CounterInterface;

/**
 * The Counter class defines a statistics event counter. It extends an EventProducer so it can keep listeners informed about new
 * observations, and it listens to external events to be able to receive observations, in addition to the ingest(...) method.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class EventBasedCounter extends EventProducer implements EventListenerInterface, CounterInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The wrapped Counter. */
    private final Counter wrappedCounter;

    /**
     * Construct a new EventBasedCounter.
     * @param description String; the description for this counter
     */
    public EventBasedCounter(final String description)
    {
        this.wrappedCounter = new Counter(description);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public long getCount()
    {
        return this.wrappedCounter.getCount();
    }

    /** {@inheritDoc} */
    @Override
    public long getN()
    {
        return this.wrappedCounter.getN();
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
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
        ingest(value);
    }

    /** {@inheritDoc} */
    @Override
    public long ingest(final long value)
    {
        this.wrappedCounter.ingest(value);
        if (hasListeners())
        {
            fireEvent(new Event(StatisticsEvents.OBSERVATION_ADDED_EVENT, this, value));
            fireEvents();
        }
        return value;
    }

    /**
     * Method that can be overridden to fire own events or additional events when ingesting an observation.
     */
    protected void fireEvents()
    {
        fireEvent(new Event(StatisticsEvents.N_EVENT, this, getN()));
        fireEvent(new Event(StatisticsEvents.COUNT_EVENT, this, getCount()));
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.wrappedCounter.initialize();
        fireEvent(new Event(StatisticsEvents.INITIALIZED_EVENT, this, null));
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return this.wrappedCounter.getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.wrappedCounter.toString();
    }

}
