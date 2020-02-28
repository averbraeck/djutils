package org.djutils.stats.summarizers;

import java.io.Serializable;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.QuantileAccumulator;

/**
 * The EventBasedTally class ingests a series of values and provides mean, standard deviation, etc. of the ingested values. It
 * extends an EventProducer so it can keep listeners informed about new observations, and it listens to external events to be
 * able to receive observations, in addition to the ingest(...) method.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class EventBasedTally extends EventProducer implements EventListenerInterface, TallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

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
    public final double getStdDev()
    {
        return this.wrappedTally.getStdDev();
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
    public final void initialize()
    {
        this.wrappedTally.initialize();
        fireEvent(INITIALIZED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final EventInterface event)
    {
        if (!(event.getContent() instanceof Number))
        {
            throw new IllegalArgumentException("Tally does not accept " + event);
        }
        double value = ((Number) event.getContent()).doubleValue();
        ingest(value);
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final double value)
    {
        this.wrappedTally.ingest(value);
        this.fireEvent(EventBasedTally.OBSERVATION_ADDED_EVENT, value);
        return value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.wrappedTally.toString();
    }

}
