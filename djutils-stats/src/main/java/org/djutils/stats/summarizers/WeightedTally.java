package org.djutils.stats.summarizers;

import org.djutils.exceptions.Throw;

/**
 * The WeightedTally class defines a statistical tally. A WeightedTally is a time-weighted tally. The WeightedTally used to
 * extend the Tally, but because the calculation method and method signatures are different, the WeightedTally has been made
 * self-contained.
 * <p>
 * Copyright (c) 2002-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class WeightedTally implements TallyStatistic
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The sum of the weights of this WeightedTally. */
    private double sumOfWeights;

    /** The mean of this WeightedTally. */
    private double weightedMean;

    /** The sum of this WeightedTally. */
    private double weightedSum;

    /** The total registered weight times the variance of this WeightedTally. */
    private double weightTimesVariance;

    /** The minimum observed value of this WeightedTally. */
    private double min;

    /** The maximum observed value of this WeightedTally. */
    private double max;

    /** The number of non-zero weight measurements of this WeightedTally. */
    private long n;

    /** The description of this WeightedTally. */
    private String description;

    /** The synchronization lock. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object semaphore = new Object();

    /**
     * Construct a new WeightedTally with a description.
     * @param description the description of this WeightedTally
     */
    public WeightedTally(final String description)
    {
        Throw.whenNull(description, "description cannot be null");
        this.description = description;
        initialize();
    }

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

    @Override
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * Process one observed weighted value.
     * @param weight the weight of the value to process
     * @param value the value to process
     * @return the value
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

    @Override
    public String getDescription()
    {
        return this.description;
    }

    @Override
    public double getMax()
    {
        return this.max;
    }

    @Override
    public double getMin()
    {
        return this.min;
    }

    @Override
    public long getN()
    {
        return this.n;
    }

    /**
     * Retrieve the current weighted sample mean of all observations since the initialization.
     * @return the current weighted sample mean
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
     * @return the current weighted mean
     */
    public double getWeightedPopulationMean()
    {
        return getWeightedSampleMean();
    }

    /**
     * Retrieve the current weighted sample standard deviation of the observations.
     * @return the current weighted sample standard deviation
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
     * @return the current weighted standard deviation
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
     * @return the current weighted sample variance of the observations
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
     * @return the current weighted variance of the observations
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
     * @return the current weighted sum of the values of the observations
     */
    public double getWeightedSum()
    {
        return this.weightedSum;
    }

    /**
     * Return a string representing a header for a textual table with a monospaced font that can contain multiple statistics.
     * @return header for the textual table.
     */
    public static String reportHeader()
    {
        return "-".repeat(113) + String.format("%n| %-48.48s | %6.6s | %10.10s | %10.10s | %10.10s | %10.10s |%n",
                "Weighted Tally name", "n", "w.mean", "w.st.dev", "min obs", "max obs") + "-".repeat(113);
    }

    @Override
    public String reportLine()
    {
        return String.format("| %-48.48s | %6d | %s | %s | %s | %s |", getDescription(), getN(),
                formatFixed(getWeightedPopulationMean(), 10), formatFixed(getWeightedPopulationStDev(), 10),
                formatFixed(getMin(), 10), formatFixed(getMax(), 10));
    }

    /**
     * Return a string representing a footer for a textual table with a monospaced font that can contain multiple statistics.
     * @return footer for the textual table
     */
    public static String reportFooter()
    {
        return "-".repeat(113);
    }

    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "WeightedTally [sumOfWeights=" + this.sumOfWeights + ", weightedMean=" + this.weightedMean + ", weightedSum="
                + this.weightedSum + ", weightTimesVariance=" + this.weightTimesVariance + ", min=" + this.min + ", max="
                + this.max + ", n=" + this.n + ", description=" + this.description + "]";
    }

}
