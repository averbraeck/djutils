package org.djutils.stats.summarizers.event;

import java.io.Serializable;
import java.util.Calendar;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.TimedEvent;
import org.djutils.stats.summarizers.TimestampTallyInterface;
import org.djutils.stats.summarizers.TimestampWeightedTally;

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
        fireEvent(new Event(StatisticsEvents.INITIALIZED_EVENT, this, null));
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
        fireEvents(timestamp, this.wrappedTimestampWeightedTally.getLastValue());
    }

    /** {@inheritDoc} */
    @Override
    public void endObservations(final Calendar timestamp)
    {
        this.wrappedTimestampWeightedTally.endObservations(timestamp);
        fireEvents(timestamp, this.wrappedTimestampWeightedTally.getLastValue());
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

    /**
     * Process one observed value.
     * @param timestamp Calendar; the Calendar object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    public double ingest(final Calendar timestamp, final double value)
    {
        this.wrappedTimestampWeightedTally.ingest(timestamp, value);
        fireEvents(timestamp, value);
        return value;
    }

    /**
     * Process one observed value.
     * @param timestamp Number; the object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    public double ingest(final Number timestamp, final double value)
    {
        this.wrappedTimestampWeightedTally.ingest(timestamp, value);
        fireEvents(timestamp, value);
        return value;
    }

    /**
     * Fire the events to potential listeners with the timestamp and value.
     * @param timestamp Object; Number or Calendar timestamp
     * @param value double; observation value
     */
    private void fireEvents(final Object timestamp, final double value)
    {
        if (hasListeners())
        {
            this.fireEvent(
                    new Event(StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT, this, new Object[] {timestamp, value}));
            fireEvent(new Event(StatisticsEvents.N_EVENT, this, getN()));
            fireEvent(new Event(StatisticsEvents.MIN_EVENT, this, getMin()));
            fireEvent(new Event(StatisticsEvents.MAX_EVENT, this, getMax()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_POPULATION_MEAN_EVENT, this, getWeightedPopulationMean()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_POPULATION_VARIANCE_EVENT, this, getWeightedPopulationVariance()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_POPULATION_STDEV_EVENT, this, getWeightedPopulationStDev()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_SUM_EVENT, this, getWeightedSum()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_SAMPLE_MEAN_EVENT, this, getWeightedSampleMean()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_SAMPLE_VARIANCE_EVENT, this, getWeightedSampleVariance()));
            fireEvent(new Event(StatisticsEvents.WEIGHTED_SAMPLE_STDEV_EVENT, this, getWeightedSampleStDev()));
        }
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
    public final double getWeightedPopulationStDev()
    {
        return this.wrappedTimestampWeightedTally.getWeightedPopulationStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleVariance()
    {
        return this.wrappedTimestampWeightedTally.getWeightedSampleVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedPopulationVariance()
    {
        return this.wrappedTimestampWeightedTally.getWeightedPopulationVariance();
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
