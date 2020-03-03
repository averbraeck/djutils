package org.djutils.stats.summarizers;

import org.djutils.exceptions.Throw;

/**
 * The WeightedTally class defines a statistical tally. A WeightedTally is a time-weighted tally. The WeightedTally used to
 * extend the Tally, but because the calculation method and method signatures are different, the WeightedTally has been made
 * self-contained.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class WeightedTally implements WeightedTallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

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
    public final String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public final double getMax()
    {
        return this.max;
    }

    /** {@inheritDoc} */
    @Override
    public final double getMin()
    {
        return this.min;
    }

    /** {@inheritDoc} */
    @Override
    public final long getN()
    {
        return this.n;
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSampleMean()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 0)
            {
                return this.weightedMean;
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedSampleStDev()
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

    /** {@inheritDoc} */
    @Override
    public double getWeightedStDev()
    {
        synchronized (this.semaphore)
        {
            return Math.sqrt(getWeightedVariance());
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return getWeightedVariance() * this.n / (this.n - 1);
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedVariance()
    {
        synchronized (this.semaphore)
        {
            return this.weightTimesVariance / this.sumOfWeights;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final double getWeightedSum()
    {
        return this.weightedSum;
    }

    /** {@inheritDoc} */
    @Override
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
        }
    }

    /** {@inheritDoc} */
    @Override
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
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "WeightedTally [sumOfWeights=" + this.sumOfWeights + ", weightedMean=" + this.weightedMean + ", weightedSum="
                + this.weightedSum + ", weightTimesVariance=" + this.weightTimesVariance + ", min=" + this.min + ", max="
                + this.max + ", n=" + this.n + ", description=" + this.description + "]";
    }

}
