package org.djutils.stats.summarizers;

import java.io.Serializable;
import java.util.Calendar;

import org.djunits.Throw;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;

/**
 * The Persistent class defines a statistics event persistent. A Persistent is a time-weighted tally. The persistent used to
 * extend the Tally, but because the calculation method and method signatures are different, the Persistent has been made
 * self-contained.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class WeightedTally extends EventProducer implements EventListenerInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

    /** sum refers to the sum of the time-persistent statistic. */
    private double weightedSum = 0;

    /** min refers to the min of the time-persistent statistic. */
    private double min = Double.NaN;

    /** max refers to the max of the time-persistent statistic. */
    private double max = Double.NaN;

    /** varianceSum refers to the varianceSum of the time-persistent statistic. */
    private double varianceSum = 0;

    /** n refers to the number of non-zero duration measurements. */
    private long n = 0;

    /** startTime defines the time of the first observation. Often equals to 0.0, but can also have other value. */
    private double startTime = Double.NaN;

    /** elapsedTime tracks the time during which we have calculated observations. This excludes the start time. */
    private double elapsedTime = Double.NaN;

    /** lastTimestamp stores the time of the last observation. Stored separately to avoid ulp rounding errors and allow ==. */
    private double lastTimestamp = Double.NaN;

    /** lastValue tracks the last value. */
    private double lastValue = Double.NaN;

    /** description refers to the description of this time-persistent statistic. */
    private final String description;

    /** whether the persistent is active or not. */
    private boolean active;

    /** the synchronized lock. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object semaphore = new Object();

    /**
     * constructs a new Persistent with a description.
     * @param description String; the description of this Persistent
     */
    public WeightedTally(final String description)
    {
        this.description = description;
        initialize();
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this;
    }

    /**
     * returns the description of this tally.
     * @return Sting description
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the max.
     * @return double
     */
    public final double getMax()
    {
        return this.max;
    }

    /**
     * Returns the min.
     * @return double
     */
    public final double getMin()
    {
        return this.min;
    }

    /**
     * Returns the number of observations.
     * @return long n
     */
    public final long getN()
    {
        return this.n;
    }

    /**
     * Returns the sampleMean of all observations since the initialization.
     * @return double; weighted sampleMean
     */
    public final double getWeightedSampleMean()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return this.weightedSum / this.elapsedTime;
            }
            return Double.NaN;
        }
    }

    /**
     * Returns the current standard deviation of the time-weighted observations.
     * @return double; weighted standard deviation
     */
    public double getWeightedStdDev()
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
     * Returns the current variance of the time-weighted observations. See
     * <a href="https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf" target= "_blank">
     * https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf</a> for some of the definitions and a
     * calculation example.
     * @return double; weighted sample variance
     */
    public double getWeightedSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (this.n > 1)
            {
                return (this.varianceSum - this.weightedSum * this.weightedSum / this.elapsedTime)
                        / (((this.n - 1.0) / (1.0 * this.n)) * this.elapsedTime);
            }
            return Double.NaN;
        }
    }

    /**
     * returns the sum of the values of the observations multiplied by their duration.
     * @return double; weighted sum
     */
    public final double getWeightedSum()
    {
        return this.weightedSum;
    }

    /**
     * initializes the Persistent statistic. This methods sets the max, min, n, sum and variance values to their initial values.
     */
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.active = true;
            this.min = Double.NaN;
            this.max = Double.NaN;
            this.n = 0;
            this.weightedSum = 0.0;
            this.varianceSum = 0.0;

            this.startTime = Double.NaN;
            this.elapsedTime = 0.0;
            this.lastTimestamp = Double.NaN;
            this.lastValue = 0.0;

            fireEvent(INITIALIZED_EVENT);
        }
    }

    /**
     * @return whether the time-persistent statistic is active (accepting observations) or not.
     */
    public boolean isActive()
    {
        return this.active;
    }

    /**
     * End the observations. After ending, no more observations will be accepted.
     * @param timestamp Number; the Number object representing the final timestamp
     */
    public void endObservations(final Number timestamp)
    {
        ingest(timestamp, this.lastValue);
        this.active = false;
    }

    /**
     * End the observations. After ending, no more observations will be accepted.
     * @param timestamp Calendar; the Calendar object representing the final timestamp
     */
    public void endObservations(final Calendar timestamp)
    {
        endObservations(timestamp.getTimeInMillis());
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event instanceof TimedEvent<?>)
        {
            TimedEvent<?> timedEvent = (TimedEvent<?>) event;
            double value = 0.0;
            if (event.getContent() instanceof Number)
            {
                value = ((Number) event.getContent()).doubleValue();
            }
            else
            {
                throw new IllegalArgumentException("Persistent.notify: Content " + event.getContent() + " should be a Number");
            }
            Object timestamp = timedEvent.getTimeStamp();
            if (timestamp instanceof Number)
            {
                ingest((Number) timestamp, value);
            }
            else if (timestamp instanceof Calendar)
            {
                ingest((Calendar) timestamp, value);
            }
            else
            {
                throw new IllegalArgumentException("Persistent.notify: timestamp should be a Number or Calendar");
            }
        }
        else
        {
            throw new IllegalArgumentException("Persistent.notify: Event should be a TimedEvent");
        }
    }

    /**
     * Process one observed value.
     * @param timestamp Calendar; the Calendar object representing the timestamp
     * @param value double; the value to process
     * @return double; the value (for method chaining)
     */
    public double ingest(final Calendar timestamp, final double value)
    {
        Throw.whenNull(timestamp, "timestamp object may not be null");
        return ingest(timestamp.getTimeInMillis(), value);
    }

    /**
     * Process one observed value.
     * @param timestampNumber Number; the object representing the timestamp
     * @param value double; the value to process
     * @return double; the value (for method chaining)
     */
    public double ingest(final Number timestampNumber, final double value)
    {
        Throw.whenNull(timestampNumber, "timestamp object may not be null");
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "value may not be NaN");
        double timestamp = timestampNumber.doubleValue();
        Throw.when(timestamp < (this.elapsedTime + this.startTime), IllegalArgumentException.class,
                "times not offered in ascending order. Last time was " + (this.elapsedTime + this.startTime)
                        + ", new timestamp was " + timestamp);

        synchronized (this.semaphore)
        {
            // only calculate anything when the time interval is larger than 0, and when the persistent is active
            if ((Double.isNaN(this.lastTimestamp) || timestamp > this.lastTimestamp) && this.active)
            {
                if (this.n == 0)
                {
                    this.min = Double.MAX_VALUE;
                    this.max = -Double.MAX_VALUE;
                    this.startTime = timestamp;
                    this.elapsedTime = 0.0;
                }
                else
                {
                    double deltaTime = timestamp - this.lastTimestamp;
                    double timeValue = this.lastValue * deltaTime;
                    this.weightedSum += timeValue;
                    this.varianceSum += timeValue * timeValue;
                    this.elapsedTime += deltaTime;
                }

                this.n++;
                this.lastTimestamp = timestamp;
                if (value < this.min)
                {
                    this.min = value;
                }
                if (value > this.max)
                {
                    this.max = value;
                }
                this.fireEvent(Tally.OBSERVATION_ADDED_EVENT, value);
            }

            this.lastValue = value;
            return value;
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.description;
    }

}
