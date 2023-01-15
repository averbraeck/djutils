package org.djutils.stats.summarizers;

/**
 * The Counter class defines a statistics event counter.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Counter implements Statistic
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** count represents the value of the counter. */
    private long count = 0;

    /** n represents the number of measurements. */
    private long n = 0;

    /** description refers to the title of this counter. */
    private String description;

    /** the semaphore. */
    private Object semaphore = new Object();

    /**
     * Constructs a new Counter.
     * @param description String; the description for this counter
     */
    public Counter(final String description)
    {
        this.description = description;
        initialize();
    }

    /**
     * Returns the current counter value.
     * @return long; the counter value
     */
    public long getCount()
    {
        return this.count;
    }

    /** {@inheritDoc} */
    @Override
    public long getN()
    {
        return this.n;
    }

    /**
     * Process one observed value.
     * @param value long; the value to process
     * @return long; the value
     */
    public long register(final long value)
    {
        synchronized (this.semaphore)
        {
            this.count += value;
            this.n++;
        }
        return value;
    }

    /**
     * Initialize the counter.
     */
    @Override
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.n = 0;
            this.count = 0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public String reportHeader()
    {
        return "-".repeat(72) + String.format("\n| %-48.48s | %6.6s | %8.8s |\n", "Counter name", "n", "count")
                + "-".repeat(72);
    }

    /** {@inheritDoc} */
    @Override
    public String reportLine()
    {
        return String.format("| %-48.48s | %6d | %8d |", getDescription(), getN(), getCount());
    }

    /** {@inheritDoc} */
    @Override
    public String reportFooter()
    {
        return "-".repeat(72);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Counter [description=" + this.description + ", n=" + this.n + ", count=" + this.count + "]";
    }
    
}
