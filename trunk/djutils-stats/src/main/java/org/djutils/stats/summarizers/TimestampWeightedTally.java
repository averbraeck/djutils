package org.djutils.stats.summarizers;

import java.util.Calendar;

import org.djunits.Throw;

/**
 * The TimestampWeightedTally class defines a time-weighted tally based on timestamped data. The difference with a normal
 * time-weighed tally is that the weight of a value is only known at the occurrence of the next timestamp. Furthermore, a last
 * timestamp needs to be specified to determine the weight of the last value. Timestamps can be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TimestampWeightedTally implements TimestampTallyInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** the wrapped weighted tally. */
    private WeightedTally wrappedWeightedTally;

    /** startTime defines the time of the first observation. Often equals to 0.0, but can also have other value. */
    private double startTime = Double.NaN;

    /** elapsedTime tracks the time during which we have calculated observations. This excludes the start time. */
    private double elapsedTime = Double.NaN;

    /** lastTimestamp stores the time of the last observation. Stored separately to avoid ulp rounding errors and allow ==. */
    private double lastTimestamp = Double.NaN;

    /** lastValue tracks the last value. */
    private double lastValue = Double.NaN;

    /** indicate whether the statistic is active or not (false before first event and after last event). */
    private boolean active = false;

    /**
     * constructs a new TimestampWeightedTally with a description.
     * @param description String; the description of this Persistent
     */
    public TimestampWeightedTally(final String description)
    {
        this.wrappedWeightedTally = new WeightedTally(description);
        initialize();
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        synchronized (this.wrappedWeightedTally.semaphore)
        {
            this.wrappedWeightedTally.initialize();
            this.startTime = Double.NaN;
            this.elapsedTime = 0.0;
            this.lastTimestamp = Double.NaN;
            this.lastValue = 0.0;
            this.active = true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive()
    {
        return this.active;
    }

    /** {@inheritDoc} */
    @Override
    public void endObservations(final Number timestamp)
    {
        ingest(timestamp, this.lastValue);
        this.active = false;
    }

    /** {@inheritDoc} */
    @Override
    public void endObservations(final Calendar timestamp)
    {
        endObservations(timestamp.getTimeInMillis());
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final Calendar timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        return ingest(timestamp.getTimeInMillis(), value);
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final Number timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        double timestampDouble = timestamp.doubleValue();
        Throw.when(Double.isNaN(timestampDouble), IllegalArgumentException.class, "timestamp may not be NaN");
        Throw.when(timestampDouble < (this.elapsedTime + this.startTime), IllegalArgumentException.class,
                "times not offered in ascending order. Last time was " + (this.elapsedTime + this.startTime)
                        + ", new timestamp was " + timestampDouble);

        synchronized (this.wrappedWeightedTally.semaphore)
        {
            // only calculate anything when the time interval is larger than 0, and when the persistent is active
            if ((Double.isNaN(this.lastTimestamp) || timestampDouble > this.lastTimestamp) && this.active)
            {
                if (Double.isNaN(this.startTime))
                {
                    this.startTime = timestampDouble;
                    this.elapsedTime = 0.0;
                }
                else
                {
                    double deltaTime = timestampDouble - this.lastTimestamp;
                    this.elapsedTime += deltaTime;
                    this.wrappedWeightedTally.ingest(deltaTime, this.lastValue);
                }
                this.lastTimestamp = timestampDouble;
            }
            this.lastValue = value;
            return value;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return this.wrappedWeightedTally.getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public long getN()
    {
        return this.wrappedWeightedTally.getN();
    }

    /** {@inheritDoc} */
    @Override
    public double getMax()
    {
        return this.wrappedWeightedTally.getMax();
    }

    /** {@inheritDoc} */
    @Override
    public double getMin()
    {
        return this.wrappedWeightedTally.getMin();
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedSampleMean()
    {
        return this.wrappedWeightedTally.getWeightedSampleMean();
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedSampleStdDev()
    {
        return this.wrappedWeightedTally.getWeightedSampleStdDev();
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedSampleVariance()
    {
        return this.wrappedWeightedTally.getWeightedSampleVariance();
    }

    /** {@inheritDoc} */
    @Override
    public double getWeightedSum()
    {
        return this.wrappedWeightedTally.getWeightedSum();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.wrappedWeightedTally.toString();
    }

}
