package org.djutils.stats.summarizers.quantileaccumulator;

import org.djutils.stats.summarizers.Tally;

import com.tdunning.math.stats.TDigest;

/**
 * TDigestAccumulator.java. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TDigestAccumulator implements QuantileAccumulator
{
    /** The TDigest that accumulates the ingested data into bins. */
    private TDigest tDigest;

    /** The compression used to create the TDigest (required to re-initialize). */
    private final int compression;
    
    /** The compression used by the parameter-less constructor. */
    public static final int DEFAULT_COMPRESSION = 100;

    /**
     * Construct a new TDigestAccumulator.
     * @param compression int; the number of bins to compress the data into
     */
    public TDigestAccumulator(final int compression)
    {
        this.compression = compression;
        initialize();
    }

    /**
     * Construct a new TDigestAccumulator with compression set to <code>DEFAULT_COMPRESSION</code>.
     */
    public TDigestAccumulator()
    {
        this(DEFAULT_COMPRESSION);
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final double value)
    {
        this.tDigest.add(value);
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public double getQuantile(final Tally tally, final double probability)
    {
        return this.tDigest.quantile(probability);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.tDigest = TDigest.createDigest(this.compression);
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "TDigestAccumulator [tDigest=" + tDigest + ", compression=" + compression + "]";
    }

}
