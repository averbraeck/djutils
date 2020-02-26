package org.djutils.stats.summarizers;

import java.io.Serializable;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;

/**
 * The Counter class defines a statistics event counter.
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
public class Counter extends EventProducer implements EventListenerInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

    /** count represents the value of the counter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long count = 0;

    /** n represents the number of measurements. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long n = 0;

    /** description refers to the title of this counter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String description;

    /** the semaphore. */
    private Object semaphore = new Object();

    /**
     * constructs a new CounterTest.
     * @param description String; the description for this counter
     */
    public Counter(final String description)
    {
        this.description = description;
        initialize();
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /**
     * Returns the current counter value.
     * @return long the counter value
     */
    public long getCount()
    {
        return this.count;
    }

    /**
     * Returns the current number of observations.
     * @return long the number of observations
     */
    public long getN()
    {
        return this.n;
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

    /**
     * Process one observed value.
     * @param value long; the value to process
     * @return long; the value (for method chaining)
     */
    public long ingest(final long value)
    {
        synchronized (this.semaphore)
        {
            this.count += value;
            this.n++;
            if (hasListeners())
            {
                this.fireEvent(Counter.OBSERVATION_ADDED_EVENT, this.count);
            }
        }
        return value;
    }

    /**
     * initializes the counter.
     */
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.n = 0;
            this.count = 0;
            fireEvent(Counter.INITIALIZED_EVENT);
        }
    }

    /**
     * returns the description of the counter.
     * @return String the description
     */
    public String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.description;
    }

}
