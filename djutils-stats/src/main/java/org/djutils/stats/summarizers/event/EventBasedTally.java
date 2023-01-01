package org.djutils.stats.summarizers.event;

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
        fireEvent(StatisticsEvents.INITIALIZED_EVENT);
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
            fireEvent(StatisticsEvents.OBSERVATION_ADDED_EVENT, value);
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
        fireEvent(StatisticsEvents.POPULATION_MEAN_EVENT, getPopulationMean());
        fireEvent(StatisticsEvents.POPULATION_VARIANCE_EVENT, getPopulationVariance());
        fireEvent(StatisticsEvents.POPULATION_SKEWNESS_EVENT, getPopulationSkewness());
        fireEvent(StatisticsEvents.POPULATION_KURTOSIS_EVENT, getPopulationKurtosis());
        fireEvent(StatisticsEvents.POPULATION_EXCESS_KURTOSIS_EVENT, getPopulationExcessKurtosis());
        fireEvent(StatisticsEvents.POPULATION_STDEV_EVENT, getPopulationStDev());
        fireEvent(StatisticsEvents.SUM_EVENT, getSum());
        fireEvent(StatisticsEvents.SAMPLE_MEAN_EVENT, getSampleMean());
        fireEvent(StatisticsEvents.SAMPLE_VARIANCE_EVENT, getSampleVariance());
        fireEvent(StatisticsEvents.SAMPLE_SKEWNESS_EVENT, getSampleSkewness());
        fireEvent(StatisticsEvents.SAMPLE_KURTOSIS_EVENT, getSampleKurtosis());
        fireEvent(StatisticsEvents.SAMPLE_EXCESS_KURTOSIS_EVENT, getSampleExcessKurtosis());
        fireEvent(StatisticsEvents.SAMPLE_STDEV_EVENT, getSampleStDev());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "EventBasedTally [wrappedTally=" + this.wrappedTally + "]";
    }

}
