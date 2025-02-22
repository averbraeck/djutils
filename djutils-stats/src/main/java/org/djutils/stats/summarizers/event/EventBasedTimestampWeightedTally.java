package org.djutils.stats.summarizers.event;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventListenerMap;
import org.djutils.event.EventProducer;
import org.djutils.event.LocalEventProducer;
import org.djutils.event.TimedEvent;
import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.TimestampWeightedTally;

/**
 * The TimestampWeightedTally class defines a time-weighted tally based on timestamped data. The difference with a normal
 * time-weighed tally is that the weight of a value is only known at the occurrence of the next timestamp. Furthermore, a last
 * timestamp needs to be specified to determine the weight of the last value. This EventBased version of the tally can be
 * notified with timestamps and values using the EventListenerInterface. It also produces events when values are tallied and
 * when the tally is initialized. Timestamps can be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class EventBasedTimestampWeightedTally extends TimestampWeightedTally implements EventProducer, EventListener
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The embedded EventProducer. */
    private EventProducer eventProducer = null;

    /**
     * constructs a new EventBasedTimestampWeightedTally with a description.
     * @param description the description of this EventBasedTimestampWeightedTally
     */
    public EventBasedTimestampWeightedTally(final String description)
    {
        this(description, new LocalEventProducer());
    }

    /**
     * Construct a new EventBasedTimestampWeightedTally with a description.
     * @param description the description of this WeightedTally
     * @param eventProducer the EventProducer to embed and use in this statistic
     */
    public EventBasedTimestampWeightedTally(final String description, final EventProducer eventProducer)
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
     * Process one observed Calender-based value. The time used will be the Calendar's time in milliseconds. Silently ignore
     * when a value is registered, but tally is not active, i.e. when endObservations() has been called.
     * @param timestamp the Calendar object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    @Override
    public double register(final Calendar timestamp, final double value)
    {
        super.register(timestamp, value);
        try
        {
            if (hasListeners())
            {
                this.eventProducer.fireEvent(StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT,
                        new Serializable[] {timestamp, value});
                fireEvents(timestamp);
            }
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);
        }
        return value;
    }

    /**
     * Process one observed Number-based value. Silently ignore when a value is registered, but tally is not active, i.e. when
     * endObservations() has been called.
     * @param timestamp the object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN or timestamp is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    @Override
    public double register(final Number timestamp, final double value)
    {
        super.register(timestamp, value);
        try
        {
            if (hasListeners())
            {
                this.eventProducer.fireEvent(StatisticsEvents.TIMESTAMPED_OBSERVATION_ADDED_EVENT,
                        new Serializable[] {timestamp, value});
                fireEvents(timestamp);
            }
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);
        }
        return value;
    }

    /**
     * Explicit;y override the double value method signature of WeightedTally to call the right method.<br>
     * Process one observed double value. Silently ignore when a value is registered, but tally is not active, i.e. when
     * endObservations() has been called.
     * @param timestamp the object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN or timestamp is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    @Override
    public double register(final double timestamp, final double value)
    {
        return register((Number) Double.valueOf(timestamp), value);
    }

    /**
     * Method that can be overridden to fire own events or additional events when registering an observation.
     * @param <T> a type for the timestamp that is Serializable and Comparable
     * @param timestamp the timestamp to use in the TimedEvents
     * @throws RemoteException on network error
     */
    protected <T extends Serializable & Comparable<T>> void fireEvents(final Serializable timestamp) throws RemoteException
    {
        // Note that All implementations of Number are Comparable and (by default) Serializable. So is Calendar.
        @SuppressWarnings("unchecked")
        T castedTimestamp = (T) timestamp;
        fireTimedEvent(StatisticsEvents.TIMED_N_EVENT, getN(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_MIN_EVENT, getMin(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_MAX_EVENT, getMax(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_POPULATION_MEAN_EVENT, getWeightedPopulationMean(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_POPULATION_VARIANCE_EVENT, getWeightedPopulationVariance(),
                castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_POPULATION_STDEV_EVENT, getWeightedPopulationStDev(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SUM_EVENT, getWeightedSum(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SAMPLE_MEAN_EVENT, getWeightedSampleMean(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT, getWeightedSampleVariance(), castedTimestamp);
        fireTimedEvent(StatisticsEvents.TIMED_WEIGHTED_SAMPLE_STDEV_EVENT, getWeightedSampleStDev(), castedTimestamp);
    }

    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "EventBasedWeightedTally" + super.toString().substring("WeightedTally".length());
    }

}
