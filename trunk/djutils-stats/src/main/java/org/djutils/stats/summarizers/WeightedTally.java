package org.djutils.stats.summarizers;

import java.io.Serializable;

import org.djunits.Throw;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;

/**
 * The WeightedTally class defines a statistical tally. A WeightedTally is a time-weighted tally. The WeightedTally used to
 * extend the Tally, but because the calculation method and method signatures are different, the WeightedTally has been made
 * self-contained.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class WeightedTally extends EventProducer implements EventListenerInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

    /** The sum of the weights of this WeightedTally. */
    private double sumOfWeights = 0;

    /** The mean of this WeightedTally. */
    private double weightedMean = 0;

    /** The sum of this WeightedTally. */
    private double weightedSum = 0;

    /** The total ingested weight times the variance of this WeightedTally. */
    private double weightTimesVariance = 0;

    /** The minimum observed value of this WeightedTally. */
    private double min = Double.NaN;

    /** The maximum observed value of this WeightedTally. */
    private double max = Double.NaN;

    /** The number of non-zero weight measurements of this WeightedTally. */
    private long n = 0;

    /** The description of this WeightedTally. */
    private final String description;

    /** The synchronization lock. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object semaphore = new Object();

    /**
     * Construct a new WeightedTally with a description.
     * @param description String; the description of this WeightedTally
     */
    public WeightedTally(final String description)
    {
        this.description = description;
        initialize();
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /**
     * Retrieve the description of this WeightedTally.
     * @return String; description of this WeightedTally
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Retrieve the highest observed value (that did not come with zero weight).
     * @return double; the highest observed value (that did not come with zero weight)
     */
    public final double getMax()
    {
        return this.max;
    }

    /**
     * Retrieve the lowest observed value (that did not come with zero weight).
     * @return double; the lowest observed value (that did not come with zero weight)
     */
    public final double getMin()
    {
        return this.min;
    }

    /**
     * Returns the number of observations.
     * @return long; the number of observations
     */
    public final long getN()
    {
        return this.n;
    }

    /**
     * Retrieve the current weighted sampleMean of all observations since the initialization.
     * @return double; the current weighted sampleMean
     */
    public final double getWeightedSampleMean()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return this.weightedMean;
            }
            return Double.NaN;
        }
    }

    /**
     * Retrieve the current weighted standard deviation of the observations.
     * @return double; the current weighted standard deviation
     */
    public double getWeightedSampleStdDev()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return Math.sqrt(getWeightedSampleVariance());
            }
            return Double.NaN;
        }
    }

    /**
     * Retrieve the current weighted variance of the observations.
     * @return double; the current weighted variance of the observations
     */
    public double getWeightedSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return this.weightTimesVariance / this.sumOfWeights * this.n / (this.n - 1);
            }
            return Double.NaN;
        }
    }

    /**
     * Retrieve the current weighted sum of the values of the observations.
     * @return double; the current weighted sum of the values of the observations
     */
    public final double getWeightedSum()
    {
        return this.weightedSum;
    }

    /**
     * Initialize this WeightedTally. This methods sets the max, min, n, sum and variance values to their initial values.
     */
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.min = Double.NaN;
            this.max = Double.NaN;
            this.n = 0;
            this.sumOfWeights = 0.0;
            this.weightedMean = 0.0;
            this.weightTimesVariance = 0.0;
            this.weightedSum = 0.0;

            fireEvent(INITIALIZED_EVENT);
        }
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
                throw new IllegalArgumentException("WeightedTally.notify: Content " + event.getContent() + " should be a Number");
            }
            Object timestamp = timedEvent.getTimeStamp();
            if (timestamp instanceof Number)
            {
                ingest(1, value); // FIXME must compute weight as duration since previous event.
            }
            // else if (timestamp instanceof Calendar)
            // {
            // ingest((Calendar) timestamp, value);
            // }
            else
            {
                throw new IllegalArgumentException("WeightedTally.notify: timestamp should be a Number or Calendar");
            }
        }
        else
        {
            throw new IllegalArgumentException("WeightedTally.notify: Event should be a TimedEvent");
        }
    }

    /**
     * Process one observed weighted value.
     * @param weight double; the weight of the value to process
     * @param value double; the value to process
     * @return double; the value
     */
    public double ingest(final double weight, final double value)
    {
        Throw.when(Double.isNaN(weight), IllegalArgumentException.class, "weight may not be NaN");
        Throw.when(weight < 0, IllegalArgumentException.class, "weight may not be negative");
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        if (0 == weight)
        {
            return value;
        }
        synchronized (this.semaphore)
        {
            if (this.n == 0)
            {
                this.min = value;
                this.max = value;
            }
            this.n++;
            // Eq 47 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            this.sumOfWeights += weight;
            double prevWeightedMean = this.weightedMean;
            // Eq 53 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            this.weightedMean += weight / this.sumOfWeights * (value - prevWeightedMean);
            // Eq 68 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            this.weightTimesVariance += weight * (value - prevWeightedMean) * (value - this.weightedMean);
            this.weightedSum += weight * value;
            if (value < this.min)
            {
                this.min = value;
            }
            if (value > this.max)
            {
                this.max = value;
            }
            this.fireEvent(Tally.OBSERVATION_ADDED_EVENT, value);
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "WeightedTally [sumOfWeights=" + sumOfWeights + ", weightedMean=" + weightedMean + ", weightedSum=" + weightedSum
                + ", weightTimesVariance=" + weightTimesVariance + ", min=" + min + ", max=" + max + ", n=" + n
                + ", description=" + description + "]";
    }

}
