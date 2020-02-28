package org.djutils.stats.summarizers;

import java.io.Serializable;

import org.djunits.Throw;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.QuantileAccumulator;

/**
 * The Tally class ingests a series of values and provides mean, standard deviation, etc. of the ingested values.
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
public class Tally extends EventProducer implements EventListenerInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

    /** The sum of this tally. */
    private double sum = 0;

    /** The mean of this tally. */
    private double mean = 0;

    /** The variance of this tally times the number of samples. */
    private double varianceTImesN = 0;

    /** The minimum observed value of this tally. */
    private double min = Double.NaN;

    /** The maximum observed value of this tally. */
    private double max = Double.NaN;

    /** The number of measurements of this tally. */
    private long n = 0;

    /** The description of this tally. */
    private final String description;

    /** The quantile accumulator. */
    private final QuantileAccumulator quantileAccumulator;

    /** the synchronized lock. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object semaphore = new Object();

    /**
     * Constructs a new Tally.
     * @param description String; the description of this tally
     * @param quantileAccumulator QuantileAccumulator; the input series accumulator that can approximate or compute quantiles.
     */
    public Tally(final String description, final QuantileAccumulator quantileAccumulator)
    {
        this.description = description;
        this.quantileAccumulator = quantileAccumulator;
        initialize();
    }

    /**
     * Convenience constructor that uses a NoStorageAccumulator to estimate quantiles.
     * @param description String; the description of this tally
     */
    public Tally(final String description)
    {
        this(description, new NoStorageAccumulator());
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /**
     * Returns the sampleMean of all observations since the initialization.
     * @return double the sampleMean
     */
    public final double getSampleMean()
    {
        if (this.n > 0)
        {
            return this.mean;
        }
        return Double.NaN;
    }

    /**
     * Compute a quantile.
     * @param probability double; the probability for which the quantile is to be computed
     * @return double; the quantile for the probability
     */
    public final double getQuantile(final double probability)
    {
        return this.quantileAccumulator.getQuantile(this, probability);
    }

    /**
     * returns the confidence interval on either side of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @return double[] the confidence interval of this tally
     */
    public final double[] getConfidenceInterval(final double alpha)
    {
        return this.getConfidenceInterval(alpha, ConfidenceInterval.BOTH_SIDE_CONFIDENCE);
    }

    /**
     * returns the confidence interval based of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @param side short; the side of the confidence interval with respect to the mean
     * @return double[] the confidence interval of this tally
     */
    public final double[] getConfidenceInterval(final double alpha, final ConfidenceInterval side)
    {
        Throw.whenNull(side, "type of confidence level cannot be null");
        Throw.when(alpha < 0 || alpha > 1, IllegalArgumentException.class,
                "confidenceLevel should be between 0 and 1 (inclusive)");
        synchronized (this.semaphore)
        {
            double sampleMean = getSampleMean();
            if (Double.isNaN(sampleMean) || Double.valueOf(this.getStdDev()).isNaN())
            {
                return null; // TODO throw something
            }
            double level = 1 - alpha;
            if (side.equals(ConfidenceInterval.BOTH_SIDE_CONFIDENCE))
            {
                level = 1 - alpha / 2.0;
            }
            double z = DistNormalTable.getInverseCumulativeProbability(0.0, 1.0, level);
            double confidence = z * Math.sqrt(this.getSampleVariance() / this.n);
            double[] result = { sampleMean - confidence, sampleMean + confidence };
            if (side.equals(ConfidenceInterval.LEFT_SIDE_CONFIDENCE))
            {
                result[1] = sampleMean;
            }
            if (side.equals(ConfidenceInterval.RIGHT_SIDE_CONFIDENCE))
            {
                result[0] = sampleMean;
            }
            result[0] = Math.max(result[0], this.min);
            result[1] = Math.min(result[1], this.max);
            return result;
        }
    }

    /**
     * returns the description of this tally.
     * @return Sting description
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the max.
     * @return double
     */
    public final double getMax()
    {
        return this.max;
    }

    /**
     * Returns the min.
     * @return double
     */
    public final double getMin()
    {
        return this.min;
    }

    /**
     * Returns the number of observations.
     * @return long n
     */
    public final long getN()
    {
        return this.n;
    }

    /**
     * Returns the current tally standard deviation.
     * @return double the standard deviation
     */
    public final double getStdDev()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return Math.sqrt(getSampleVariance());
            }
            return Double.NaN;
        }
    }

    /**
     * returns the sum of the values of the observations.
     * @return double sum
     */
    public final double getSum()
    {
        return this.sum;
    }

    /**
     * Returns the current variance of this tally.
     * @return double; the current variance of this tally
     */
    public final double getSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return this.varianceTImesN / (this.n - 1);
            }
            return Double.NaN;
        }
    }

    /**
     * initializes the Tally. This methods sets the max, min, n, sum and variance values to their initial values.
     */
    public final void initialize()
    {
        synchronized (this.semaphore)
        {
            this.min = Double.NaN;
            this.max = Double.NaN;
            this.n = 0;
            this.sum = 0.0;
            this.mean = 0.0;
            this.varianceTImesN = 0.0;
            this.quantileAccumulator.initialize();
            fireEvent(INITIALIZED_EVENT);
        }
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

    /**
     * Process one observed value.
     * @param value double; the value to process
     * @return double; the value (for method chaining)
     */
    public double ingest(final double value)
    {
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        synchronized (this.semaphore)
        {
            if (this.n == 0)
            {
                this.min = Double.MAX_VALUE;
                this.max = -Double.MAX_VALUE;
            }
            this.n++;
            double prevMean = this.mean;
            // Eq 4 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            this.mean = prevMean + (value - prevMean) / this.n;
            // Eq 44 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            this.varianceTImesN = this.varianceTImesN + (value - prevMean) * (value - this.mean);
            this.sum += value;
            if (value < this.min)
            {
                this.min = value;
            }
            if (value > this.max)
            {
                this.max = value;
            }
            this.quantileAccumulator.ingest(value);
            this.fireEvent(Tally.OBSERVATION_ADDED_EVENT, value);
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.description;
    }

}
