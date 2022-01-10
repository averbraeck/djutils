package org.djutils.stats.summarizers.quantileaccumulator;

import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.Tally;

/**
 * FixedBinsAccumulator.java. <br>
 * This accumulator is created with a caller prescribes set of bins. All bins have the same width.
 * <br>
 * Copyright (c) 2021-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class FixedBinsAccumulator implements QuantileAccumulator
{
    /** Center value of minimum bin. */
    private final double minimumBinCenter;

    /** Width of each bin. */
    private final double binWidth;

    /** Storage for the accumulated values. */
    private long[] accumulator;

    /** Cumulative counts. */
    private long[] cumulatives = null;

    /** Total number of ingested values. */
    private long totalCount = 0;

    /** Count number of ingested items that fall below the range. */
    private long belowCount = 0;

    /** Count number of ingested items that fall above the range. */
    private long aboveCount = 0;

    /**
     * Construct a new FullStorageAccumulator.
     * @param minimumBinCenter double; center value of bin for minimum value of range (minimum value in range is
     *            <code>minimumBinCenter - binWidth / 2</code>, maximum value in range is
     *            <code>minimumBinCenter + binWidth * (binCount - 0.5)</code>)
     * @param binWidth double; width of each bin
     * @param binCount int; number of bins
     */
    public FixedBinsAccumulator(final double minimumBinCenter, final double binWidth, final int binCount)
    {
        Throw.when(!Double.isFinite(minimumBinCenter), IllegalArgumentException.class, "minimumBinCenter must be finite");
        Throw.when(!Double.isFinite(binWidth), IllegalArgumentException.class, "binWidth must be finite");
        Throw.when(binWidth <= 0, IllegalArgumentException.class, "binWidth must be positive");
        Throw.when(binCount < 1, IllegalArgumentException.class, "binCount must be > 0");
        this.minimumBinCenter = minimumBinCenter;
        this.binWidth = binWidth;
        this.belowCount = 0;
        this.aboveCount = 0;
        this.accumulator = new long[binCount];
        this.cumulatives = null;
        this.totalCount = 0;
    }

    /**
     * Retrieve the bin width.
     * @return double; the bin width
     */
    public double getBinWidth()
    {
        return this.binWidth;
    }

    /**
     * Retrieve the bin count.
     * @return int; the bin count
     */
    public int getBinCount()
    {
        return this.accumulator.length;
    }

    /**
     * Retrieve the total number of ingested values.
     * @return long; the total number of ingested values
     */
    public long getN()
    {
        return this.totalCount;
    }

    /**
     * Retrieve the number of ingested values that were below the range of this FixedBinsAccumulator.
     * @return long; the number of ingested values that were below the range of this FixedBinsAccumulator
     */
    public long getBelowCount()
    {
        return this.belowCount;
    }

    /**
     * Retrieve the number of ingested values that were above the range of this FixedBinsAccumulator.
     * @return long; the number of ingested values that were above the range of this FixedBinsAccumulator
     */
    public long getAboveCount()
    {
        return this.aboveCount;
    }

    /**
     * Return the center of a particular bin.
     * @param bin int the bin number
     * @return double; the center of requested bin
     */
    public double getBinCenter(final int bin)
    {
        Throw.when(bin < 0 || bin >= this.accumulator.length, IllegalArgumentException.class,
                "bin must be in range 0..$1; got $2", this.accumulator.length - 1, bin);
        return this.minimumBinCenter + bin * this.binWidth;
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final double value)
    {
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "accumulator can not accumlate NaN value");
        this.cumulatives = null;
        double floatBin = (value - this.minimumBinCenter) / binWidth;
        int bin = (int) Math.rint(floatBin);
        if (bin < 0)
        {
            this.belowCount++;
        }
        else if (bin >= this.accumulator.length)
        {
            this.aboveCount++;
        }
        else
        {
            this.accumulator[bin]++;
        }
        this.totalCount++;
        return value;
    }
    
    /**
     * Compute the cumulative values if not already available.
     */
    private void ensureCumulatives()
    {
        if (null == this.cumulatives)
        {
            long count = 0;
            this.cumulatives = new long[this.accumulator.length];
            for (int bin = 0; bin < this.accumulator.length; bin++)
            {
                count += this.accumulator[bin];
                this.cumulatives[bin] = count;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getQuantile(final Tally tally, final double probability)
    {
        Throw.when(!Double.isFinite(probability) || probability < 0.0 || probability > 1.0, IllegalArgumentException.class,
                "probability must be a value between 0 and 1");
        ensureCumulatives();
        // TODO do something clever with belowCount and aboveCount (could involve the tally as well)
        long usableCount = this.totalCount - this.belowCount - this.aboveCount;
        if (usableCount == 0)
        {
            return Double.NaN;
        }
        double value = (usableCount) * probability;
        // TODO use bisection to home in
        for (int bin = 0; bin < this.cumulatives.length; bin++)
        {
            if (this.cumulatives[bin] >= value)
            {
                return bin * this.binWidth + this.minimumBinCenter;
            }
        }
        return 0; // cannot happen
    }

    /** {@inheritDoc} */
    @Override
    public double getCumulativeProbability(final Tally tally, final double quantile) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(quantile), IllegalArgumentException.class, "quantile may not be NaN");
        // TODO do something clever with belowCount and aboveCount (could involve the tally as well)
        if (this.totalCount == 0)
        {
            return Double.NaN;
        }
        double floatBin = (quantile - this.minimumBinCenter) / binWidth;
        int bin = (int) Math.rint(floatBin);
        if (bin < 0)
        {
            return 0.0;
        }
        if (bin >= this.accumulator.length)
        {
            return 1.0;
        }
        ensureCumulatives();
        return 1.0 * this.cumulatives[bin] / this.totalCount + (floatBin - bin - 0.5) * this.accumulator[bin] / this.totalCount;
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.belowCount = 0;
        this.aboveCount = 0;
        this.accumulator = new long[this.accumulator.length];
        this.cumulatives = null;
        this.totalCount = 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "FixedBinsAccumulator [minimumBinCenter=" + minimumBinCenter + ", binWidth=" + binWidth + ", totalCount="
                + totalCount + ", belowCount=" + belowCount + ", aboveCount=" + aboveCount + "]";
    }

}
