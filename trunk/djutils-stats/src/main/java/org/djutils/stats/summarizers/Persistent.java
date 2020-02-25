package org.djutils.stats.summarizers;

import java.util.Calendar;

import org.djutils.event.EventInterface;
import org.djutils.event.EventType;
import org.djutils.event.TimedEvent;
import org.djutils.logger.CategoryLogger;

/**
 * The Persistent class defines a statistics event persistent. A Persistent is a time-averaged tally.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Persistent extends Tally
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT");

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT");

    /** startTime defines the time of the first event. */
    private double startTime = Double.NaN;

    /** elapsedTime tracks the elapsed time. */
    private double elapsedTime = Double.NaN;

    /** deltaTime defines the time between 2 events. */
    private double deltaTime = Double.NaN;

    /** lastvalue tracks the last value. */
    private double lastValue = Double.NaN;

    /**
     * constructs a new Persistent with a description.
     * @param description String; the description of this Persistent
     */
    public Persistent(final String description)
    {
        super(description);
    }

    /** {@inheritDoc} */
    @Override
    public double getStdDev()
    {
        synchronized (this.semaphore)
        {
            if (super.n > 1)
            {
                return Math.sqrt(super.varianceSum / (this.elapsedTime - this.deltaTime));
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getSampleVariance()
    {
        synchronized (this.semaphore)
        {
            if (super.n > 1)
            {
                return super.varianceSum / (this.elapsedTime - this.deltaTime);
            }
            return Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            super.initialize();
            this.deltaTime = 0.0;
            this.elapsedTime = 0.0;
            this.lastValue = 0.0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (!(event instanceof TimedEvent<?>) || !(event.getContent() instanceof Number))
        {
            throw new IllegalArgumentException("Persistent: event != TimedEvent || event.conent != Number ("
                    + event.getContent().getClass().toString() + ")");
        }
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
                CategoryLogger.always().warn("Persistent.notify: Content {} should be a Number", event.getContent());
            }

            synchronized (this.semaphore)
            {
                @SuppressWarnings({"rawtypes", "unchecked"})
                TimedEvent lastValueEvent =
                        new TimedEvent(Persistent.OBSERVATION_ADDED_EVENT, this, this.lastValue, timedEvent.getTimeStamp());
                fireEvent(lastValueEvent);
                @SuppressWarnings({"rawtypes", "unchecked"})
                TimedEvent valueEvent = new TimedEvent(Persistent.OBSERVATION_ADDED_EVENT, this, value, timedEvent.getTimeStamp());
                fireEvent(valueEvent);
                double timestamp = 0;
                if (timedEvent.getTimeStamp() instanceof Number)
                {
                    timestamp = ((Number) timedEvent.getTimeStamp()).doubleValue();
                }
                else if (timedEvent.getTimeStamp() instanceof Calendar)
                {
                    timestamp = ((Calendar) timedEvent.getTimeStamp()).getTimeInMillis();
                }
                else
                {
                    CategoryLogger.always().warn("Persistent.notify: Timestamp {} should be a Number or Calendar",
                            event.getContent());
                }
                if (this.n == 0)
                {
                    this.min = Double.MAX_VALUE;
                    this.max = -Double.MAX_VALUE;
                    this.startTime = timestamp;
                }
                else
                {
                    this.deltaTime = timestamp - (this.elapsedTime + this.startTime);
                    if (this.deltaTime > 0.0)
                    {
                        this.sum += value * 
                        double newAverage = ((super.sampleMean * (this.elapsedTime)) + (this.lastValue * this.deltaTime))
                                / (this.elapsedTime + this.deltaTime);
                        super.varianceSum +=
                                (this.lastValue - super.sampleMean) * (this.lastValue - newAverage) * this.deltaTime;
                        super.setSampleMean(newAverage);
                        this.elapsedTime = this.elapsedTime + this.deltaTime;
                    }
                }
                this.n++;
                if (value < this.min)
                {
                    this.min = value;
                }
                if (value > this.max)
                {
                    this.max = value;
                }
                // TODO this.quantileAccumulator.ingest(value);
                this.fireEvent(Tally.OBSERVATION_ADDED_EVENT, value);

                if (this.n > 1)
                {
                    super.fireEvent(Tally.STANDARD_DEVIATION_EVENT, this.getStdDev());
                    this.fireEvent(Tally.SAMPLE_VARIANCE_EVENT, this.getSampleVariance());
                }
                this.lastValue = value;
            }
        }
    }
}
