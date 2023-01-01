package org.djutils.stats.summarizers.event;

import java.io.Serializable;
import java.util.Calendar;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.LocalEventProducer;
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
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class EventBasedTimestampWeightedTally extends LocalEventProducer implements EventListener, TimestampTallyInterface
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
    public void initialize()
    {
        this.wrappedTimestampWeightedTally.initialize();
        fireEvent(StatisticsEvents.INITIALIZED_EVENT);
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
        fireEvents(timestamp.doubleValue(), this.wrappedTimestampWeightedTally.getLastValue());
    }

    /** {@inheritDoc} */
    @Override
    public void endObservations(final Calendar timestamp)
    {
        this.wrappedTimestampWeightedTally.endObservations(timestamp);
        fireEvents(timestamp.getTimeInMillis(), this.wrappedTimestampWeightedTally.getLastValue());
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final Event event)
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
                register(((Number) timestamp).doubleValue(), value);
            }
            else if (timestamp instanceof Calendar)
            {
                register(((Calendar) timestamp).getTimeInMillis(), value);
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
    public double register(final Calendar timestamp, final double value)
    {
        this.wrappedTimestampWeightedTally.register(timestamp, value);
        fireEvents(timestamp, value);
        return value;
    }

    /**
     * Process one observed value.
     * @param <T> a type for the timestamp that extends Number and is Comparable, e.g., a Double, a Long, or a djunits Time
     * @param timestamp T; the object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    public <T extends Number & Comparable<T>> double register(final T timestamp, final double value)
    {
        this.wrappedTimestampWeightedTally.register(timestamp, value);
        fireEvents(timestamp, value);
        return value;
    }

    /**
     * Fire the events to potential listeners with the timestamp and value.
     * @param <T> a type for the timestamp that is Serializable and Comparable
     * @param timestamp T; Number or Calendar timestamp
     * @param value double; observation value
     */
    private <T extends Serializable & Comparable<T>> void fireEvents(final T timestamp, final double value)
    {
        if (hasListeners())
        {
            this.fireEvent(StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT, new Serializable[] {timestamp, value});
            fireEvents(timestamp);
        }
    }

    /**
     * Method that can be overridden to fire own events or additional events when ingesting an observation.
     * @param <T> a type for the timestamp that is Serializable and Comparable
     * @param timestamp T; the timestamp to use in the TimedEvents
     */
    protected <T extends Serializable & Comparable<T>> void fireEvents(final T timestamp)
    {
        fireTimedEvent(StatisticsEvents.TIMED_N_EVENT, getN(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_MIN_EVENT, getMin(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_MAX_EVENT, getMax(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_POPULATION_MEAN_EVENT, getWeightedPopulationMean(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_POPULATION_VARIANCE_EVENT, getWeightedPopulationVariance(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_POPULATION_STDEV_EVENT, getWeightedPopulationStDev(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SUM_EVENT, getWeightedSum(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SAMPLE_MEAN_EVENT, getWeightedSampleMean(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT, getWeightedSampleVariance(), timestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SAMPLE_STDEV_EVENT, getWeightedSampleStDev(), timestamp);
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
