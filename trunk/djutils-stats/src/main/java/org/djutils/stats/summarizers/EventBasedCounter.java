package org.djutils.stats.summarizers;

import java.io.Serializable;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;

/**
 * The Counter class defines a statistics event counter. It extends an EventProducer so it can keep listeners informed about new
 * observations, and it listens to external events to be able to receive observations, in addition to the ingest(...) method.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class EventBasedCounter extends EventProducer implements EventListenerInterface, CounterInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

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
            this.fireEvent(EventBasedCounter.OBSERVATION_ADDED_EVENT, this);
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.wrappedCounter.initialize();
        fireEvent(EventBasedCounter.INITIALIZED_EVENT);
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
