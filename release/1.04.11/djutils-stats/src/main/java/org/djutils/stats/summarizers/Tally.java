package org.djutils.stats.summarizers;

import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.QuantileAccumulator;

/**
 * The Tally class ingests a series of values and provides mean, standard deviation, etc. of the ingested values.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class Tally implements TallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** The sum of this tally. */
    private double sum = 0;

    /** The mean of this tally. */
    private double m1 = 0;

    /** The summation for the second moment (variance). */
    private double m2 = 0;

    /** The summation for the third moment (skew). */
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
    public final double getSampleMean()
    {
        if (this.n > 0)
        {
            return this.m1;
        }
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public final double getQuantile(final double probability)
    {
        return this.quantileAccumulator.getQuantile(this, probability);
    }

    /** {@inheritDoc} */
    @Override
    public final double[] getConfidenceInterval(final double alpha)
    {
        return this.getConfidenceInterval(alpha, ConfidenceInterval.BOTH_SIDE_CONFIDENCE);
    }

    /** {@inheritDoc} */
    @Override
    public final double[] getConfidenceInterval(final double alpha, final ConfidenceInterval side)
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
    public final double getSampleStDev()
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

    /** {@inheritDoc} */
    @Override
    public final double getStDev()
    {
        synchronized (this.semaphore)
        {
            return Math.sqrt(getVariance());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final double getSum()
    {
        return this.sum;
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleVariance()
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

    /** {@inheritDoc} */
    @Override
    public final double getVariance()
    {
        synchronized (this.semaphore)
        {
            return this.m2 / this.n;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleSkewness()
    {
        if (this.n > 2)
        {
            return getSkewness() * Math.sqrt(this.n * (this.n - 1)) / (this.n - 2);
        }
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public final double getSkewness()
    {
        if (this.n > 1)
        {
            return this.m3 / this.n / Math.pow(this.m2 / this.n, 1.5);
        }
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public final double getSampleKurtosis()
    {
        if (this.n > 3)
        {
            double g2 = getKurtosis();
            return 1.0 * (this.n - 1) / (this.n - 2) / (this.n - 3) * ((this.n + 1) * g2 + 6);
        }
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public final double getKurtosis()
    {
        if (this.n > 2)
        {
            double a4 = this.m4 / this.n / (this.m2 / this.n) / (this.m2 / this.n);
            return a4 - 3; // convert kurtosis to excess kurtosis
        }
        return Double.NaN;
    }

    /** {@inheritDoc} */
    @Override
    public final void initialize()
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

    /** {@inheritDoc} */
    @Override
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
            this.quantileAccumulator.ingest(value);
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "Tally [sum=" + this.sum + ", m1=" + this.m1 + ", m2=" + this.m2 + ", m3=" + this.m3 + ", m4=" + this.m4
                + ", min=" + this.min + ", max=" + this.max + ", n=" + this.n + ", description=" + this.description
                + ", quantileAccumulator=" + this.quantileAccumulator + "]";
    }

}
