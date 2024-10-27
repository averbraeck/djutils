package org.djutils.stats.summarizers.event;

import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventListenerMap;
import org.djutils.event.EventProducer;
import org.djutils.event.LocalEventProducer;
import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.Tally;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.QuantileAccumulator;

/**
 * The EventBasedTally class registers a series of values and provides mean, standard deviation, etc. of the registered values.
 * It embeds an EventProducer so it can keep listeners informed about new observations, and it listens to external events to be
 * able to receive observations, in addition to the register(...) method.
 * <p>
 * Copyright (c) 2002-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class EventBasedTally extends Tally implements EventProducer, EventListener
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The embedded EventProducer. */
    private EventProducer eventProducer = null;

    /**
     * Constructs a new EventBasedTally.
     * @param description String; the description of this tally
     * @param quantileAccumulator QuantileAccumulator; the input series accumulator that can approximate or compute quantiles.
     */
    public EventBasedTally(final String description, final QuantileAccumulator quantileAccumulator)
    {
        this(description, quantileAccumulator, new LocalEventProducer());
    }

    /**
     * Convenience constructor that uses a NoStorageAccumulator to estimate quantiles.
     * @param description String; the description of this tally
     */
    public EventBasedTally(final String description)
    {
        this(description, new NoStorageAccumulator());
    }

    /**
     * Construct a new EventBasedCounter with a specific EventProducer, e.g. a remote one. The Tally uses uses a
     * NoStorageAccumulator to estimate quantiles.
     * @param description String; the description for this counter
     * @param eventProducer EventProducer; the EventProducer to embed and use in this statistic
     */
    public EventBasedTally(final String description, final EventProducer eventProducer)
    {
        this(description, new NoStorageAccumulator(), eventProducer);
    }

    /**
     * Construct a new EventBasedCounter with a specific EventProducer, e.g. a remote one.
     * @param description String; the description for this counter
     * @param quantileAccumulator QuantileAccumulator; the input series accumulator that can approximate or compute quantiles.
     * @param eventProducer EventProducer; the EventProducer to embed and use in this statistic
     */
    public EventBasedTally(final String description, final QuantileAccumulator quantileAccumulator,
            final EventProducer eventProducer)
    {
        super(description, quantileAccumulator);
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
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final Event event)
    {
        if (!(event.getContent() instanceof Number))
        {
            throw new IllegalArgumentException("Tally does not accept " + event);
        }
        double value = ((Number) event.getContent()).doubleValue();
        register(value);
    }

    @Override
    public double register(final double value)
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
        this.eventProducer.fireEvent(StatisticsEvents.MIN_EVENT, getMin());
        this.eventProducer.fireEvent(StatisticsEvents.MAX_EVENT, getMax());
        this.eventProducer.fireEvent(StatisticsEvents.POPULATION_MEAN_EVENT, getPopulationMean());
        this.eventProducer.fireEvent(StatisticsEvents.POPULATION_VARIANCE_EVENT, getPopulationVariance());
        this.eventProducer.fireEvent(StatisticsEvents.POPULATION_SKEWNESS_EVENT, getPopulationSkewness());
        this.eventProducer.fireEvent(StatisticsEvents.POPULATION_KURTOSIS_EVENT, getPopulationKurtosis());
        this.eventProducer.fireEvent(StatisticsEvents.POPULATION_EXCESS_KURTOSIS_EVENT, getPopulationExcessKurtosis());
        this.eventProducer.fireEvent(StatisticsEvents.POPULATION_STDEV_EVENT, getPopulationStDev());
        this.eventProducer.fireEvent(StatisticsEvents.SUM_EVENT, getSum());
        this.eventProducer.fireEvent(StatisticsEvents.SAMPLE_MEAN_EVENT, getSampleMean());
        this.eventProducer.fireEvent(StatisticsEvents.SAMPLE_VARIANCE_EVENT, getSampleVariance());
        this.eventProducer.fireEvent(StatisticsEvents.SAMPLE_SKEWNESS_EVENT, getSampleSkewness());
        this.eventProducer.fireEvent(StatisticsEvents.SAMPLE_KURTOSIS_EVENT, getSampleKurtosis());
        this.eventProducer.fireEvent(StatisticsEvents.SAMPLE_EXCESS_KURTOSIS_EVENT, getSampleExcessKurtosis());
        this.eventProducer.fireEvent(StatisticsEvents.SAMPLE_STDEV_EVENT, getSampleStDev());
    }

    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "EventBasedTally" + super.toString().substring(5);
    }

}
