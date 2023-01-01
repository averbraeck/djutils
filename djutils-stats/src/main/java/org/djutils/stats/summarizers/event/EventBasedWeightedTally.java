package org.djutils.stats.summarizers.event;

import java.io.Serializable;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.LocalEventProducer;
import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.WeightedTally;
import org.djutils.stats.summarizers.WeightedTallyInterface;

/**
 * The EventBasedWeightedTally class defines a time-weighted tally that can be notified with weights and values using the
 * EventListenerInterface. It also produces events when values are tallied and when the tally is initialized.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class EventBasedWeightedTally extends LocalEventProducer implements EventListener, WeightedTallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** the wrapped WeightedTally. */
    private final WeightedTally wrappedWeightedTally;

    /**
     * Construct a new WeightedTally with a description.
     * @param description String; the description of this WeightedTally
     */
    public EventBasedWeightedTally(final String description)
    {
        this.wrappedWeightedTally = new WeightedTally(description);
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription()
    {
        return this.wrappedWeightedTally.getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public final double getMax()
    {
        return this.wrappedWeightedTally.getMax();
    }

    /** {@inheritDoc} */
    @Override
    public final double getMin()
    {
        return this.wrappedWeightedTally.getMin();
    }

    /** {@inheritDoc} */
    @Override
    public final long getN()
    {
        return this.wrappedWeightedTally.getN();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleMean()
    {
        return this.wrappedWeightedTally.getWeightedSampleMean();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleStDev()
    {
        return this.wrappedWeightedTally.getWeightedSampleStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedPopulationStDev()
    {
        return this.wrappedWeightedTally.getWeightedPopulationStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleVariance()
    {
        return this.wrappedWeightedTally.getWeightedSampleVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedPopulationVariance()
    {
        return this.wrappedWeightedTally.getWeightedPopulationVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSum()
    {
        return this.wrappedWeightedTally.getWeightedSum();
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.wrappedWeightedTally.initialize();
        fireEvent(StatisticsEvents.INITIALIZED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final Event event)
    {
        Throw.when(!(event.getContent() instanceof Object[]), IllegalArgumentException.class,
                "WeightedTally.notify: Content should be Object[] {weight, value}");
        Object[] content = (Object[]) event.getContent();
        Throw.when(content.length != 2, IllegalArgumentException.class,
                "WeightedTally.notify: Content should be Object[] {weight, value}");
        Throw.when(!(content[0] instanceof Number), IllegalArgumentException.class,
                "WeightedTally.notify: Weight (Content[0]) should be a Number");
        Throw.when(!(content[1] instanceof Number), IllegalArgumentException.class,
                "WeightedTally.notify: Value (Content[1]) should be a Number");
        double weight = ((Number) content[0]).doubleValue();
        double value = ((Number) content[1]).doubleValue();
        register(weight, value);
    }

    /**
     * Process one observed weighted value.
     * @param weight double; the weight of the value to process
     * @param value double; the value to process
     * @return double; the value
     */
    public double register(final double weight, final double value)
    {
        this.wrappedWeightedTally.register(weight, value);
        if (hasListeners())
        {
            fireEvent(StatisticsEvents.WEIGHTED_OBSERVATION_ADDED_EVENT, new Serializable[] {weight, value});
            fireEvents();
        }
        return value;
    }

    /**
     * Method that can be overridden to fire own events or additional events when ingesting an observation.
     */
    protected void fireEvents()
    {
        fireEvent(StatisticsEvents.N_EVENT, getN());
        fireEvent(StatisticsEvents.MIN_EVENT, getMin());
        fireEvent(StatisticsEvents.MAX_EVENT, getMax());
        fireEvent(StatisticsEvents.WEIGHTED_POPULATION_MEAN_EVENT, getWeightedPopulationMean());
        fireEvent(StatisticsEvents.WEIGHTED_POPULATION_VARIANCE_EVENT, getWeightedPopulationVariance());
        fireEvent(StatisticsEvents.WEIGHTED_POPULATION_STDEV_EVENT, getWeightedPopulationStDev());
        fireEvent(StatisticsEvents.WEIGHTED_SUM_EVENT, getWeightedSum());
        fireEvent(StatisticsEvents.WEIGHTED_SAMPLE_MEAN_EVENT, getWeightedSampleMean());
        fireEvent(StatisticsEvents.WEIGHTED_SAMPLE_VARIANCE_EVENT, getWeightedSampleVariance());
        fireEvent(StatisticsEvents.WEIGHTED_SAMPLE_STDEV_EVENT, getWeightedSampleStDev());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.wrappedWeightedTally.toString();
    }

}
