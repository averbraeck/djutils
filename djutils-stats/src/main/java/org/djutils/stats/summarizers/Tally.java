package org.djutils.stats.summarizers;

import org.djutils.exceptions.Throw;
import org.djutils.stats.ConfidenceInterval;
import org.djutils.stats.DistNormalTable;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.QuantileAccumulator;

/**
 * The Tally class registers a series of values and provides mean, standard deviation, etc. of the registered values.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class Tally implements Statistic
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The sum of this tally. */
    private double sum = 0;

    /** The mean of this tally. */
    private double m1 = 0;

    /** The summation for the second moment (variance). */
    private double m2 = 0;

    /** The summation for the third moment (skewness). */
    private double m3 = 0;

    /** The summation for the fourth moment (kurtosis). */
    private double m4 = 0;

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
     * Convenience constructor that uses a NoStorageAccumulator to estimate quantiles.
     * @param description String; the description of this tally
     */
    public Tally(final String description)
    {
        this(description, new NoStorageAccumulator());
    }

    /**
     * Constructs a new Tally.
     * @param description String; the description of this tally
     * @param quantileAccumulator QuantileAccumulator; the input series accumulator that can approximate or compute quantiles.
     */
    public Tally(final String description, final QuantileAccumulator quantileAccumulator)
    {
        Throw.whenNull(description, "description cannot be null");
        Throw.whenNull(quantileAccumulator, "quantileAccumulator cannot be null");
        this.description = description;
        this.quantileAccumulator = quantileAccumulator;
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
            this.sum = 0.0;
            this.m1 = 0.0;
            this.m2 = 0.0;
            this.m3 = 0;
            this.m4 = 0;
            this.quantileAccumulator.initialize();
        }
    }

    /**
     * Ingest an array of values.
     * @param values double...; the values to register
     */
    public void register(final double... values)
    {
        for (double value : values)
        {
            register(value);
        }
    }

    /**
     * Process one observed value.
     * @param value double; the value to process
     * @return double; the value
     */
    public double register(final double value)
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
            double delta = value - this.m1;
            double oldm2 = this.m2;
            double oldm3 = this.m3;
            // Eq 4 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            // Eq 1.1 in https://prod-ng.sandia.gov/techlib-noauth/access-control.cgi/2008/086212.pdf
            this.m1 += delta / this.n;
            // Eq 44 in https://fanf2.user.srcf.net/hermes/doc/antiforgery/stats.pdf
            // Eq 1.2 in https://prod-ng.sandia.gov/techlib-noauth/access-control.cgi/2008/086212.pdf
            this.m2 += delta * (value - this.m1);
            // Eq 2.13 in https://prod-ng.sandia.gov/techlib-noauth/access-control.cgi/2008/086212.pdf
            this.m3 += -3 * oldm2 * delta / this.n + (this.n - 1) * (this.n - 2) * delta * delta * delta / this.n / this.n;
            // Eq 2.16 in https://prod-ng.sandia.gov/techlib-noauth/access-control.cgi/2008/086212.pdf
            this.m4 += -4 * oldm3 * delta / this.n + 6 * oldm2 * delta * delta / this.n / this.n + (this.n - 1)
                    * (this.n * this.n - 3 * this.n + 3) * delta * delta * delta * delta / this.n / this.n / this.n;
            this.sum += value;
            if (value < this.min)
            {
                this.min = value;
            }
            if (value > this.max)
            {
                this.max = value;
            }
            this.quantileAccumulator.register(value);
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
     * Return the sum of the values of the observations.
     * @return double; the sum of the values of the observations
     */
    public double getSum()
    {
        return this.sum;
    }

    /**
     * Returns the sample mean of all observations since the initialization.
     * @return double; the sample mean
     */
    public double getSampleMean()
    {
        if (this.n > 0)
        {
            return this.m1;
        }
        return Double.NaN;
    }

    /**
     * Returns the population mean of all observations since the initialization.
     * @return double; the population mean
     */
    public double getPopulationMean()
    {
        return getSampleMean();
    }

    /**
     * Returns the current (unbiased) sample standard deviation of all observations since the initialization. The sample
     * standard deviation is defined as the square root of the sample variance.
     * @return double; the sample standard deviation
     */
    public double getSampleStDev()
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
     * Returns the current (biased) population standard deviation of all observations since the initialization. The population
     * standard deviation is defined as the square root of the population variance.
     * @return double; the population standard deviation
     */
    public double getPopulationStDev()
    {
        synchronized (this.semaphore)
        {
            return Math.sqrt(getPopulationVariance());
        }
    }

    /**
     * Returns the current (unbiased) sample variance of all observations since the initialization. The calculation of the
     * sample variance in relation to the population variance is undisputed. The formula is:<br>
     * &nbsp;&nbsp;<i>S<sup>2</sup> = (1 / (n - 1)) * [ &Sigma;x<sup>2</sup> - (&Sigma;x)<sup>2</sup> / n ] </i><br>
     * which can be calculated on the basis of the calculated population variance <i>&sigma;<sup>2</sup></i> as follows:<br>
     * &nbsp;&nbsp;<i>S<sup>2</sup> = &sigma;<sup>2</sup> * n / (n - 1)</i><br>
     * @return double; the current sample variance of this tally
     */
    public double getSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return this.m2 / (this.n - 1);
            }
            return Double.NaN;
        }
    }

    /**
     * Returns the current (biased) population variance of all observations since the initialization. The population variance is
     * defined as:<br>
     * <i>&sigma;<sup>2</sup> = (1 / n) * [ &Sigma;x<sup>2</sup> - (&Sigma;x)<sup>2</sup> / n ] </i>
     * @return double; the current population variance of this tally
     */
    public double getPopulationVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 0)
            {
                return this.m2 / this.n;
            }
            return Double.NaN;
        }
    }

    /**
     * Return the (unbiased) sample skewness of the registered data. There are different formulas to calculate the unbiased
     * (sample) skewness from the biased (population) skewness. Minitab, for instance calculates unbiased skewness as:<br>
     * &nbsp;&nbsp;<i>Skew<sub>unbiased</sub> = Skew<sub>biased</sub> [ ( n - 1) / n ]<sup> 3/2</sup></i> <br>
     * whereas SAS, SPSS and Excel calculate it as:<br>
     * &nbsp;&nbsp;<i>Skew<sub>unbiased</sub> = Skew<sub>biased</sub> &radic;[ n ( n - 1)] / (n - 2)</i> <br>
     * Here we follow the last mentioned formula. All formulas converge to the same value with larger n.
     * @return double; the sample skewness of the registered data
     */
    public double getSampleSkewness()
    {
        if (this.n > 2)
        {
            return getPopulationSkewness() * Math.sqrt(this.n * (this.n - 1)) / (this.n - 2);
        }
        return Double.NaN;
    }

    /**
     * Return the (biased) population skewness of the registered data. The population skewness is defined as:<br>
     * &nbsp;&nbsp;<i>Skew<sub>biased</sub> = [ &Sigma; ( x - &mu; ) <sup>3</sup> ] / [ n . S<sup>3</sup> ]</i><br>
     * where <i>S<sup>2</sup></i> is the <b>sample</b> variance. So the denominator is equal to <i>[ n .
     * sample_var<sup>3/2</sup> ]</i> .
     * @return double; the skewness of the registered data
     */
    public double getPopulationSkewness()
    {
        if (this.n > 1)
        {
            return (this.m3 / this.n) / Math.pow(getPopulationVariance(), 1.5);
        }
        return Double.NaN;
    }

    /**
     * Return the sample kurtosis of the registered data. The sample kurtosis can be defined in multiple ways. Here, we choose the
     * following formula:<br>
     * &nbsp;&nbsp;<i>Kurt<sub>unbiased</sub> = [ &Sigma; ( x - &mu; ) <sup>4</sup> ] / [ ( n - 1 ) . S<sup>4</sup> ]</i><br>
     * where <i>S<sup>2</sup></i> is the <u>sample</u> variance. So the denominator is equal to <i>[ ( n - 1 ) .
     * sample_var<sup>2</sup> ]</i> .
     * @return double; the sample kurtosis of the registered data
     */
    public double getSampleKurtosis()
    {
        if (this.n > 3)
        {
            double sVar = getSampleVariance();
            return this.m4 / (this.n - 1) / sVar / sVar;
        }
        return Double.NaN;
    }

    /**
     * Return the (biased) population kurtosis of the registered data. The population kurtosis is defined as:<br>
     * &nbsp;&nbsp;<i>Kurt<sub>biased</sub> = [ &Sigma; ( x - &mu; ) <sup>4</sup> ] / [ n . &sigma;<sup>4</sup> ]</i><br>
     * where <i>&sigma;<sup>2</sup></i> is the <u>population</u> variance. So the denominator is equal to <i>[ n .
     * pop_var<sup>2</sup> ]</i> .
     * @return double; the population kurtosis of the registered data
     */
    public double getPopulationKurtosis()
    {
        if (this.n > 2)
        {
            return (this.m4 / this.n) / (this.m2 / this.n) / (this.m2 / this.n);
        }
        return Double.NaN;
    }

    /**
     * Return the sample excess kurtosis of the registered data. The sample excess kurtosis is the sample-corrected value of the
     * excess kurtosis. Several formulas exist to calculate the sample excess kurtosis from the population kurtosis. Here we
     * use:<br>
     * &nbsp;&nbsp;<i>ExcessKurt<sub>unbiased</sub> = ( n - 1 ) / [( n - 2 ) * ( n - 3 )] [ ( n + 1 ) *
     * ExcessKurt<sub>biased</sub> + 6]</i> <br>
     * This is the excess kurtosis that is calculated by, for instance, SAS, SPSS and Excel.
     * @return double; the sample excess kurtosis of the registered data
     */
    public double getSampleExcessKurtosis()
    {
        if (this.n > 3)
        {
            double g2 = getPopulationExcessKurtosis();
            return (1.0 * (this.n - 1) / (this.n - 2) / (this.n - 3)) * ((this.n + 1) * g2 + 6.0);
        }
        return Double.NaN;
    }

    /**
     * Return the population excess kurtosis of the registered data. The kurtosis value of the normal distribution is 3. The
     * excess kurtosis is the kurtosis value shifted by -3 to be 0 for the normal distribution.
     * @return double; the population excess kurtosis of the registered data
     */
    public double getPopulationExcessKurtosis()
    {
        if (this.n > 2)
        {
            // convert kurtosis to excess kurtosis, shift by -3
            return getPopulationKurtosis() - 3.0;
        }
        return Double.NaN;
    }

    /**
     * Compute the quantile for the given probability.
     * @param probability double; the probability for which the quantile is to be computed. The value should be between 0 and 1,
     *            inclusive.
     * @return double; the quantile for the probability
     * @throws IllegalArgumentException when the probability is less than 0 or larger than 1
     */
    public double getQuantile(final double probability)
    {
        return this.quantileAccumulator.getQuantile(this, probability);
    }

    /**
     * Get, or estimate fraction of registered values between -infinity up to and including a given quantile.
     * @param quantile double; the given quantile
     * @return double; the estimated or observed fraction of registered values between -infinity up to and including the given
     *         quantile. When this TallyInterface has registered zero values; this method shall return NaN.
     * @throws IllegalArgumentException when quantile is NaN
     */
    public double getCumulativeProbability(final double quantile)
    {
        return this.quantileAccumulator.getCumulativeProbability(this, quantile);
    }

    /**
     * returns the confidence interval on either side of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @return double[]; the confidence interval of this tally
     * @throws IllegalArgumentException when alpha is less than 0 or larger than 1
     */
    public double[] getConfidenceInterval(final double alpha)
    {
        return this.getConfidenceInterval(alpha, ConfidenceInterval.BOTH_SIDE_CONFIDENCE);
    }

    /**
     * returns the confidence interval based of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @param side ConfidenceInterval; the side of the confidence interval with respect to the mean
     * @return double[]; the confidence interval of this tally
     * @throws IllegalArgumentException when alpha is less than 0 or larger than 1
     * @throws NullPointerException when side is null
     */
    public double[] getConfidenceInterval(final double alpha, final ConfidenceInterval side)
    {
        Throw.whenNull(side, "type of confidence level cannot be null");
        Throw.when(alpha < 0 || alpha > 1, IllegalArgumentException.class,
                "confidenceLevel should be between 0 and 1 (inclusive)");
        synchronized (this.semaphore)
        {
            double sampleMean = getSampleMean();
            if (Double.isNaN(sampleMean) || Double.valueOf(this.getSampleStDev()).isNaN())
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
            double[] result = {sampleMean - confidence, sampleMean + confidence};
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

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Tally [sum=" + this.sum + ", m1=" + this.m1 + ", m2=" + this.m2 + ", m3=" + this.m3 + ", m4=" + this.m4
                + ", min=" + this.min + ", max=" + this.max + ", n=" + this.n + ", description=" + this.description
                + ", quantileAccumulator=" + this.quantileAccumulator + "]";
    }

    /** {@inheritDoc} */
    @Override
    public String reportHeader()
    {
        return "-".repeat(113) + String.format("\n| %-48.48s | %6.6s | %10.10s | %10.10s | %10.10s | %10.10s |\n", "Tally name",
                "n", "mean", "st.dev", "minimum", "maximum") + "-".repeat(113);
    }

    /** {@inheritDoc} */
    @Override
    public String reportLine()
    {
        return String.format("| %-48.48s | %6d | %s | %s | %s | %s |", getDescription(), getN(),
                formatFixed(getPopulationMean(), 10), formatFixed(getPopulationStDev(), 10), formatFixed(getMin(), 10),
                formatFixed(getMax(), 10));
    }

    /** {@inheritDoc} */
    @Override
    public String reportFooter()
    {
        return "-".repeat(113);
    }
}
