package org.djutils.stats.summarizers;

import org.djutils.exceptions.Throw;

/**
 * The WeightedTally class defines a statistical tally. A WeightedTally is a time-weighted tally. The WeightedTally used to
 * extend the Tally, but because the calculation method and method signatures are different, the WeightedTally has been made
 * self-contained.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class WeightedTally implements Statistic
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The sum of the weights of this WeightedTally. */
    private double sumOfWeights = 0;

    /** The mean of this WeightedTally. */
    private double weightedMean = 0;

    /** The sum of this WeightedTally. */
    private double weightedSum = 0;

    /** The total registered weight times the variance of this WeightedTally. */
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

    /**
     * Process one observed weighted value.
     * @param weight double; the weight of the value to process
     * @param value double; the value to process
     * @return double; the value
     */
    public double register(final double weight, final double value)
    {
        Throw.when(Double.isNaN(weight), IllegalArgumentException.class, "weight may not be NaN");
        Throw.when(weight < 0, IllegalArgumentException.class, "weight may not be negative");
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        if (0.0 == weight)
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
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the maximum value of any given observation, or NaN when no observations were registered.
     * @return double; the maximum value of any given observation
     */
    public double getMax()
    {
        return this.max;
    }

    /**
     * Returns the minimum value of any given observation, or NaN when no observations were registered.
     * @return double; the minimum value of any given observation
     */
    public double getMin()
    {
        return this.min;
    }

    /** {@inheritDoc} */
    @Override
    public long getN()
    {
        return this.n;
    }

    /**
     * Retrieve the current weighted sample mean of all observations since the initialization.
     * @return double; the current weighted sample mean
     */
    public double getWeightedSampleMean()
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

    /**
     * Retrieve the current weighted mean of all observations since the initialization.
     * @return double; the current weighted mean
     */
    public double getWeightedPopulationMean()
    {
        return getWeightedSampleMean();
    }

    /**
     * Retrieve the current weighted sample standard deviation of the observations.
     * @return double; the current weighted sample standard deviation
     */
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

    /**
     * Retrieve the current weighted standard deviation of the observations.
     * @return double; the current weighted standard deviation
     */
    public double getWeightedPopulationStDev()
    {
        synchronized (this.semaphore)
        {
            return Math.sqrt(getWeightedPopulationVariance());
        }
    }

    /**
     * Retrieve the current weighted sample variance of the observations.
     * @return double; the current weighted sample variance of the observations
     */
    public double getWeightedSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return getWeightedPopulationVariance() * this.n / (this.n - 1);
            }
            return Double.NaN;
        }
    }

    /**
     * Retrieve the current weighted variance of the observations.
     * @return double; the current weighted variance of the observations
     */
    public double getWeightedPopulationVariance()
    {
        synchronized (this.semaphore)
        {
            return this.weightTimesVariance / this.sumOfWeights;
        }
    }

    /**
     * Retrieve the current weighted sum of the values of the observations.
     * @return double; the current weighted sum of the values of the observations
     */
    public double getWeightedSum()
    {
        return this.weightedSum;
    }

    /** {@inheritDoc} */
    @Override
    public String reportHeader()
    {
        return "-".repeat(113) + String.format("\n| %-48.48s | %6.6s | %10.10s | %10.10s | %10.10s | %10.10s |\n",
                "Weighted Tally name", "n", "w.mean", "w.st.dev", "min obs", "max obs") + "-".repeat(113);
    }

    /** {@inheritDoc} */
    @Override
    public String reportLine()
    {
        return String.format("| %-48.48s | %6d | %s | %s | %s | %s |", getDescription(), getN(),
                formatFixed(getWeightedPopulationMean(), 10), formatFixed(getWeightedPopulationStDev(), 10),
                formatFixed(getMin(), 10), formatFixed(getMax(), 10));
    }

    /** {@inheritDoc} */
    @Override
    public String reportFooter()
    {
        return "-".repeat(113);
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
