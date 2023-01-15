package org.djutils.stats.summarizers;

import java.util.Calendar;

import org.djutils.exceptions.Throw;

/**
 * The TimestampWeightedTally class defines a time-weighted tally based on timestamped data. The difference with a normal
 * time-weighed tally is that the weight of a value is only known at the occurrence of the next timestamp. Furthermore, a last
 * timestamp needs to be specified to determine the weight of the last value. Timestamps can be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TimestampWeightedTally implements Statistic
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** the wrapped weighted tally. */
    private WeightedTally wrappedWeightedTally;

    /** startTime defines the time of the first observation. Often equals to 0.0, but can also have other value. */
    private double startTime = Double.NaN;

    /** lastTimestamp stores the time of the last observation. Stored separately to avoid ulp rounding errors and allow ==. */
    private double lastTimestamp = Double.NaN;

    /** lastValue tracks the last value. */
    private double lastValue = Double.NaN;

    /** indicate whether the statistic is active or not (false before first event and after last event). */
    private boolean active = false;

    /**
     * constructs a new TimestampWeightedTally with a description.
     * @param description String; the description of this TimestampWeightedTally
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
            this.lastTimestamp = Double.NaN;
            this.lastValue = 0.0;
            this.active = true;
        }
    }

    /**
     * Return whether the statistic is active (accepting observations) or not.
     * @return boolean; whether the statistic is active (accepting observations) or not
     */
    public boolean isActive()
    {
        return this.active;
    }

    /**
     * End the observations and closes the last interval of observations. After ending, no more observations will be accepted.
     * Calling this method will create an extra observation, and corresponding events for the EventBased implementations of this
     * interface will be called.
     * @param timestamp Number; the Number object representing the final timestamp
     */
    public void endObservations(final Number timestamp)
    {
        register(timestamp, this.lastValue);
        this.active = false;
    }

    /**
     * End the observations and closes the last interval of observations. After ending, no more observations will be accepted.
     * Calling this method will create an extra observation, and corresponding events for the EventBased implementations of this
     * interface will be called.
     * @param timestamp Calendar; the Calendar object representing the final timestamp
     */
    public void endObservations(final Calendar timestamp)
    {
        endObservations(timestamp.getTimeInMillis());
    }

    /**
     * Return the last observed value.
     * @return double; the last observed value
     */
    public double getLastValue()
    {
        return this.lastValue;
    }

    /**
     * Process one observed value.
     * @param timestamp Calendar; the Calendar object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    public double register(final Calendar timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        return register(timestamp.getTimeInMillis(), value);
    }

    /**
     * Process one observed value.
     * @param timestamp Number; the object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    public double register(final Number timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        double timestampDouble = timestamp.doubleValue();
        Throw.when(Double.isNaN(timestampDouble), IllegalArgumentException.class, "timestamp may not be NaN");
        Throw.when(timestampDouble < this.lastTimestamp, IllegalArgumentException.class,
                "times not offered in ascending order. Last time was " + this.lastTimestamp + ", new timestamp was "
                        + timestampDouble);

        synchronized (this.wrappedWeightedTally.semaphore)
        {
            // only calculate anything when the time interval is larger than 0, and when the TimestampWeightedTally is active
            if ((Double.isNaN(this.lastTimestamp) || timestampDouble > this.lastTimestamp) && this.active)
            {
                if (Double.isNaN(this.startTime))
                {
                    this.startTime = timestampDouble;
                }
                else
                {
                    double deltaTime = Math.max(0.0, timestampDouble - this.lastTimestamp);
                    this.wrappedWeightedTally.register(deltaTime, this.lastValue);
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

    /**
     * Returns the maximum value of any given observation, or NaN when no observations were registered.
     * @return double; the maximum value of any given observation
     */
    public double getMax()
    {
        return this.wrappedWeightedTally.getMax();
    }

    /**
     * Returns the minimum value of any given observation, or NaN when no observations were registered.
     * @return double; the minimum value of any given observation
     */
    public double getMin()
    {
        return this.wrappedWeightedTally.getMin();
    }

    /**
     * Retrieve the current weighted sample mean of all observations since the initialization.
     * @return double; the current weighted sample mean
     */
    public double getWeightedSampleMean()
    {
        return this.wrappedWeightedTally.getWeightedSampleMean();
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
        return this.wrappedWeightedTally.getWeightedSampleStDev();
    }

    /**
     * Retrieve the current weighted standard deviation of the observations.
     * @return double; the current weighted standard deviation
     */
    public double getWeightedPopulationStDev()
    {
        return this.wrappedWeightedTally.getWeightedPopulationStDev();
    }

    /**
     * Retrieve the current weighted sample variance of the observations.
     * @return double; the current weighted sample variance of the observations
     */
    public double getWeightedSampleVariance()
    {
        return this.wrappedWeightedTally.getWeightedSampleVariance();
    }

    /**
     * Retrieve the current weighted variance of the observations.
     * @return double; the current weighted variance of the observations
     */
    public double getWeightedPopulationVariance()
    {
        return this.wrappedWeightedTally.getWeightedPopulationVariance();
    }

    /**
     * Retrieve the current weighted sum of the values of the observations.
     * @return double; the current weighted sum of the values of the observations
     */
    public double getWeightedSum()
    {
        return this.wrappedWeightedTally.getWeightedSum();
    }

    /** {@inheritDoc} */
    @Override
    public String reportHeader()
    {
        return "-".repeat(113)
                + String.format("%n| %-48.48s | %6.6s | %10.10s | %10.10s | %10.10s | %10.10s |%n",
                        "Timestamp-based weighted Tally name", "n", "w.mean", "w.st.dev", "min obs", "max obs")
                + "-".repeat(113);
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
    public String toString()
    {
        return this.wrappedWeightedTally.toString();
    }

}
