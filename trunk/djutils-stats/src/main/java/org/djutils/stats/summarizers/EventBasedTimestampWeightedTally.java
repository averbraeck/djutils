package org.djutils.stats.summarizers;

import java.io.Serializable;
import java.util.Calendar;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;

/**
 * The TimestampWeightedTally class defines a time-weighted tally based on timestamped data. The difference with a normal
 * time-weighed tally is that the weight of a value is only known at the occurrence of the next timestamp. Furthermore, a last
 * timestamp needs to be specified to determine the weight of the last value. This EventBased version of the tally can be
 * notified with timestamps and values using the EventListenerInterface. It also produces events when values are tallied and
 * when the tally is initialized. Timestamps can be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class EventBasedTimestampWeightedTally extends EventProducer implements EventListenerInterface, TimestampTallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

    /** the wrapped timestamp weighted tally. */
    private TimestampWeightedTally wrappedTimestampWeightedTally;

    /**
     * constructs a new EventBasefdTimestampWeightedTally with a description.
     * @param description String; the description of this EventBasedTimestampWeightedTally
     */
    public EventBasedTimestampWeightedTally(final String description)
    {
        this.wrappedTimestampWeightedTally = new TimestampWeightedTally(description);
        initialize();
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.wrappedTimestampWeightedTally.initialize();
        fireEvent(INITIALIZED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isActive()
    {
        return this.wrappedTimestampWeightedTally.isActive();
    }

    /** {@inheritDoc} */
    @Override
    public void endObservations(final Number timestamp)
    {
        this.wrappedTimestampWeightedTally.endObservations(timestamp);
    }

    /** {@inheritDoc} */
    @Override
    public void endObservations(final Calendar timestamp)
    {
        this.wrappedTimestampWeightedTally.endObservations(timestamp);
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event instanceof TimedEvent<?>)
        {
            TimedEvent<?> timedEvent = (TimedEvent<?>) event;
            double value = 0.0;
            if (event.getContent() instanceof Number)
            {
                value = ((Number) event.getContent()).doubleValue();
            }
            else
            {
                throw new IllegalArgumentException(
                        "EventBasedTimestampWeightedTally.notify: Content " + event.getContent() + " should be a Number");
            }
            Object timestamp = timedEvent.getTimeStamp();
            if (timestamp instanceof Number)
            {
                ingest((Number) timestamp, value);
            }
            else if (timestamp instanceof Calendar)
            {
                ingest((Calendar) timestamp, value);
            }
            else
            {
                throw new IllegalArgumentException(
                        "EventBasedTimestampWeightedTally.notify: timestamp should be a Number or Calendar");
            }
        }
        else
        {
            throw new IllegalArgumentException("EventBasedTimestampWeightedTally.notify: Event should be a TimedEvent");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final Calendar timestamp, final double value)
    {
        this.wrappedTimestampWeightedTally.ingest(timestamp, value);
        fireEvent(OBSERVATION_ADDED_EVENT, this);
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final Number timestampNumber, final double value)
    {
        this.wrappedTimestampWeightedTally.ingest(timestampNumber, value);
        fireEvent(OBSERVATION_ADDED_EVENT, this);
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription()
    {
        return this.wrappedTimestampWeightedTally.getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public final long getN()
    {
        return this.wrappedTimestampWeightedTally.getN();
    }

    /** {@inheritDoc} */
    @Override
    public final double getMax()
    {
        return this.wrappedTimestampWeightedTally.getMax();
    }

    /** {@inheritDoc} */
    @Override
    public final double getMin()
    {
        return this.wrappedTimestampWeightedTally.getMin();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleMean()
    {
        return this.wrappedTimestampWeightedTally.getWeightedSampleMean();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleStDev()
    {
        return this.wrappedTimestampWeightedTally.getWeightedSampleStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedStDev()
    {
        return this.wrappedTimestampWeightedTally.getWeightedStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleVariance()
    {
        return this.wrappedTimestampWeightedTally.getWeightedSampleVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedVariance()
    {
        return this.wrappedTimestampWeightedTally.getWeightedVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSum()
    {
        return this.wrappedTimestampWeightedTally.getWeightedSum();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.wrappedTimestampWeightedTally.toString();
    }

}
