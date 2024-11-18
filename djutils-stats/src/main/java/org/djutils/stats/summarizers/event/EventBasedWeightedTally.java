package org.djutils.stats.summarizers.event;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventListenerMap;
import org.djutils.event.EventProducer;
import org.djutils.event.LocalEventProducer;
import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.WeightedTally;

/**
 * The EventBasedWeightedTally class defines a time-weighted tally that can be notified with weights and values using the
 * EventListener. It also produces events when values are tallied and when the tally is initialized. It embeds an EventProducer
 * so it can keep listeners informed about new observations.
 * <p>
 * Copyright (c) 2002-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class EventBasedWeightedTally extends WeightedTally implements EventProducer, EventListener
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The embedded EventProducer. */
    private EventProducer eventProducer = null;

    /**
     * Construct a new WeightedTally with a description.
     * @param description the description of this WeightedTally
     */
    public EventBasedWeightedTally(final String description)
    {
        this(description, new LocalEventProducer());
    }

    /**
     * Construct a new WeightedTally with a description.
     * @param description the description of this WeightedTally
     * @param eventProducer the EventProducer to embed and use in this statistic
     */
    public EventBasedWeightedTally(final String description, final EventProducer eventProducer)
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
        // metadata descriptor checks content
        Object[] content = (Object[]) event.getContent();
        double weight = ((Number) content[0]).doubleValue();
        double value = ((Number) content[1]).doubleValue();
        register(weight, value);
    }

    /**
     * Process one observed weighted value.
     * @param weight the weight of the value to process
     * @param value the value to process
     * @return the value
     */
    @Override
    public double register(final double weight, final double value)
    {
        super.register(weight, value);
        try
        {
            if (hasListeners())
            {
                this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_OBSERVATION_ADDED_EVENT,
                        new Serializable[] {weight, value});
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
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_POPULATION_MEAN_EVENT, getWeightedPopulationMean());
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_POPULATION_VARIANCE_EVENT, getWeightedPopulationVariance());
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_POPULATION_STDEV_EVENT, getWeightedPopulationStDev());
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_SUM_EVENT, getWeightedSum());
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_SAMPLE_MEAN_EVENT, getWeightedSampleMean());
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_SAMPLE_VARIANCE_EVENT, getWeightedSampleVariance());
        this.eventProducer.fireEvent(StatisticsEvents.WEIGHTED_SAMPLE_STDEV_EVENT, getWeightedSampleStDev());
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

}
