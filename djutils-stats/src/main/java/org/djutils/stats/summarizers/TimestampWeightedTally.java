package org.djutils.stats.summarizers;

import java.util.Calendar;

import org.djutils.exceptions.Throw;

/**
 * The TimestampWeightedTally class defines a time-weighted tally based on timestamped data. The difference with a normal
 * time-weighed tally is that the weight of a value is only known at the occurrence of the next timestamp. Furthermore, a last
 * timestamp needs to be specified to determine the weight of the last value. Timestamps can be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TimestampWeightedTally extends WeightedTally
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** startTime defines the time of the first observation. Often equals to 0.0, but can also have other value. */
    private double startTime;

    /** lastTimestamp stores the time of the last observation. Stored separately to avoid ulp rounding errors and allow ==. */
    private double lastTimestamp;

    /** lastValue tracks the last value. */
    private double lastValue;

    /** indicate whether the statistic is active or not (false before first event and after last event). */
    private boolean active;

    /**
     * constructs a new TimestampWeightedTally with a description.
     * @param description the description of this TimestampWeightedTally
     */
    public TimestampWeightedTally(final String description)
    {
        super(description);
    }

    @Override
    public void initialize()
    {
        synchronized (super.semaphore)
        {
            super.initialize();
            this.startTime = Double.NaN;
            this.lastTimestamp = Double.NaN;
            this.lastValue = 0.0;
            this.active = true;
        }
    }

    /**
     * Return whether the statistic is active (accepting observations) or not.
     * @return whether the statistic is active (accepting observations) or not
     */
    public boolean isActive()
    {
        return this.active;
    }

    /**
     * End the observations and closes the last interval of observations. After ending, no more observations will be accepted.
     * Calling this method will create an extra observation, and corresponding events for the EventBased implementations of this
     * interface will be called.
     * @param timestamp the Number object representing the final timestamp
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
     * @param timestamp the Calendar object representing the final timestamp
     */
    public void endObservations(final Calendar timestamp)
    {
        register(timestamp, this.lastValue);
        this.active = false;
    }

    /**
     * Return the last observed value.
     * @return the last observed value
     */
    public double getLastValue()
    {
        return this.lastValue;
    }

    /**
     * Process one observed Calender-based value. The time used will be the Calendar's time in milliseconds. Silently ignore
     * when a value is registered, but tally is not active, i.e. when endObservations() has been called.
     * @param timestamp the Calendar object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    public double register(final Calendar timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        return registerValue(timestamp.getTimeInMillis(), value);
    }

    /**
     * Process one observed Number-based value. Silently ignore when a value is registered, but tally is not active, i.e. when
     * endObservations() has been called.
     * @param timestamp the object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN or timestamp is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    public double register(final Number timestamp, final double value)
    {
        return registerValue(timestamp, value);
    }

    /**
     * Explicit;y override the double value method signature of WeightedTally to call the right method.<br>
     * Process one observed double value. Silently ignore when a value is registered, but tally is not active, i.e. when
     * endObservations() has been called.
     * @param timestamp the object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN or timestamp is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    @Override
    public double register(final double timestamp, final double value)
    {
        return registerValue(timestamp, value);
    }

    /**
     * Process one observed Number-based value. Silently ignore when a value is registered, but tally is not active, i.e. when
     * endObservations() has been called.
     * @param timestamp the object representing the timestamp
     * @param value the value to process
     * @return the value
     * @throws NullPointerException when timestamp is null
     * @throws IllegalArgumentException when value is NaN or timestamp is NaN
     * @throws IllegalArgumentException when given timestamp is before last timestamp
     */
    protected double registerValue(final Number timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        double timestampDouble = timestamp.doubleValue();
        Throw.when(Double.isNaN(timestampDouble), IllegalArgumentException.class, "timestamp may not be NaN");
        Throw.when(timestampDouble < this.lastTimestamp, IllegalArgumentException.class,
                "times not offered in ascending order. Last time was " + this.lastTimestamp + ", new timestamp was "
                        + timestampDouble);

        synchronized (super.semaphore)
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
                    super.register(deltaTime, this.lastValue);
                }
                this.lastTimestamp = timestampDouble;
            }
            this.lastValue = value;
            return value;
        }
    }

    /**
     * Return a string representing a header for a textual table with a monospaced font that can contain multiple statistics.
     * @return header for the textual table.
     */
    public static String reportHeader()
    {
        return "-".repeat(126)
                + String.format("%n| %-48.48s | %6.6s | %10.10s | %10.10s | %10.10s | %10.10s | %10.10s |%n",
                        "Timestamp-based weighted Tally name", "n", "interval", "w.mean", "w.st.dev", "min obs", "max obs")
                + "-".repeat(126);
    }

    @Override
    public String reportLine()
    {
        return String.format("| %-48.48s | %6d | %s | %s | %s | %s | %s |", getDescription(), getN(),
                formatFixed(this.lastTimestamp - this.startTime, 10), formatFixed(getWeightedPopulationMean(), 10),
                formatFixed(getWeightedPopulationStDev(), 10), formatFixed(getMin(), 10), formatFixed(getMax(), 10));
    }

    /**
     * Return a string representing a footer for a textual table with a monospaced font that can contain multiple statistics.
     * @return footer for the textual table
     */
    public static String reportFooter()
    {
        return "-".repeat(126);
    }

    @Override
    public String toString()
    {
        return "TimestampWeightedTally" + super.toString().substring("WeightedTally".length());
    }

}
