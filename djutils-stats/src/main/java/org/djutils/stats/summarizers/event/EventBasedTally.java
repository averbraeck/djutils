package org.djutils.stats.summarizers.event;

import java.io.Serializable;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.LocalEventProducer;
import org.djutils.stats.ConfidenceInterval;
import org.djutils.stats.summarizers.Tally;
import org.djutils.stats.summarizers.TallyInterface;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.QuantileAccumulator;

/**
 * The EventBasedTally class ingests a series of values and provides mean, standard deviation, etc. of the ingested values. It
 * extends an EventProducer so it can keep listeners informed about new observations, and it listens to external events to be
 * able to receive observations, in addition to the register(...) method.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class EventBasedTally extends LocalEventProducer implements EventListener, TallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** the wrapped Tally. */
    private final Tally wrappedTally;

    /**
     * Constructs a new EventBasedTally.
     * @param description String; the description of this tally
     * @param quantileAccumulator QuantileAccumulator; the input series accumulator that can approximate or compute quantiles.
     */
    public EventBasedTally(final String description, final QuantileAccumulator quantileAccumulator)
    {
        super(description);
        this.wrappedTally = new Tally(description, quantileAccumulator);
    }

    /**
     * Convenience constructor that uses a NoStorageAccumulator to estimate quantiles.
     * @param description String; the description of this tally
     */
    public EventBasedTally(final String description)
    {
        this(description, new NoStorageAccumulator());
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleMean()
    {
        return this.wrappedTally.getSampleMean();
    }

    /** {@inheritDoc} */
    @Override
    public final double getQuantile(final double probability)
    {
        return this.wrappedTally.getQuantile(probability);
    }

    /** {@inheritDoc} */
    @Override
    public double getCumulativeProbability(final double quantile) throws IllegalArgumentException
    {
        return this.wrappedTally.getCumulativeProbability(quantile);
    }

    /** {@inheritDoc} */
    @Override
    public final double[] getConfidenceInterval(final double alpha)
    {
        return this.wrappedTally.getConfidenceInterval(alpha);
    }

    /** {@inheritDoc} */
    @Override
    public final double[] getConfidenceInterval(final double alpha, final ConfidenceInterval side)
    {
        return this.wrappedTally.getConfidenceInterval(alpha, side);
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription()
    {
        return this.wrappedTally.getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public final double getMax()
    {
        return this.wrappedTally.getMax();
    }

    /** {@inheritDoc} */
    @Override
    public final double getMin()
    {
        return this.wrappedTally.getMin();
    }

    /** {@inheritDoc} */
    @Override
    public final long getN()
    {
        return this.wrappedTally.getN();
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleStDev()
    {
        return this.wrappedTally.getSampleStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getPopulationStDev()
    {
        return this.wrappedTally.getPopulationStDev();
    }

    /** {@inheritDoc} */
    @Override
    public final double getSum()
    {
        return this.wrappedTally.getSum();
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleVariance()
    {
        return this.wrappedTally.getSampleVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getPopulationVariance()
    {
        return this.wrappedTally.getPopulationVariance();
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleSkewness()
    {
        return this.wrappedTally.getSampleSkewness();
    }

    /** {@inheritDoc} */
    @Override
    public final double getPopulationSkewness()
    {
        return this.wrappedTally.getPopulationSkewness();
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleKurtosis()
    {
        return this.wrappedTally.getSampleKurtosis();
    }

    /** {@inheritDoc} */
    @Override
    public final double getPopulationKurtosis()
    {
        return this.wrappedTally.getPopulationKurtosis();
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleExcessKurtosis()
    {
        return this.wrappedTally.getSampleExcessKurtosis();
    }

    /** {@inheritDoc} */
    @Override
    public final double getPopulationExcessKurtosis()
    {
        return this.wrappedTally.getPopulationExcessKurtosis();
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.wrappedTally.initialize();
        fireEvent(new Event(StatisticsEvents.INITIALIZED_EVENT, this, null));
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public double register(final double value)
    {
        this.wrappedTally.register(value);
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
        fireEvent(new Event(StatisticsEvents.MIN_EVENT, this, getMin()));
        fireEvent(new Event(StatisticsEvents.MAX_EVENT, this, getMax()));
        fireEvent(new Event(StatisticsEvents.POPULATION_MEAN_EVENT, this, getPopulationMean()));
        fireEvent(new Event(StatisticsEvents.POPULATION_VARIANCE_EVENT, this, getPopulationVariance()));
        fireEvent(new Event(StatisticsEvents.POPULATION_SKEWNESS_EVENT, this, getPopulationSkewness()));
        fireEvent(new Event(StatisticsEvents.POPULATION_KURTOSIS_EVENT, this, getPopulationKurtosis()));
        fireEvent(new Event(StatisticsEvents.POPULATION_EXCESS_KURTOSIS_EVENT, this, getPopulationExcessKurtosis()));
        fireEvent(new Event(StatisticsEvents.POPULATION_STDEV_EVENT, this, getPopulationStDev()));
        fireEvent(new Event(StatisticsEvents.SUM_EVENT, this, getSum()));
        fireEvent(new Event(StatisticsEvents.SAMPLE_MEAN_EVENT, this, getSampleMean()));
        fireEvent(new Event(StatisticsEvents.SAMPLE_VARIANCE_EVENT, this, getSampleVariance()));
        fireEvent(new Event(StatisticsEvents.SAMPLE_SKEWNESS_EVENT, this, getSampleSkewness()));
        fireEvent(new Event(StatisticsEvents.SAMPLE_KURTOSIS_EVENT, this, getSampleKurtosis()));
        fireEvent(new Event(StatisticsEvents.SAMPLE_EXCESS_KURTOSIS_EVENT, this, getSampleExcessKurtosis()));
        fireEvent(new Event(StatisticsEvents.SAMPLE_STDEV_EVENT, this, getSampleStDev()));
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "EventBasedTally [wrappedTally=" + this.wrappedTally + "]";
    }

}
