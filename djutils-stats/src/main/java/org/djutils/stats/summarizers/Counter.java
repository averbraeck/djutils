package org.djutils.stats.summarizers;

import org.djutils.exceptions.Throw;

/**
 * The Counter class defines a statistics event counter.
 * <p>
 * Copyright (c) 2002-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * @param description the description for this counter
     */
    public Counter(final String description)
    {
        Throw.whenNull(description, "description cannot be null");
        this.description = description;
        initialize();
    }

    /**
     * Returns the current counter value.
     * @return the counter value
     */
    public long getCount()
    {
        return this.count;
    }

    @Override
    public long getN()
    {
        return this.n;
    }

    /**
     * Process one observed value.
     * @param value the value to process
     * @return the value
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

    @Override
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Return a string representing a header for a textual table with a monospaced font that can contain multiple statistics.
     * @return header for the textual table.
     */
    public static String reportHeader()
    {
        return "-".repeat(72) + String.format("%n| %-48.48s | %6.6s | %8.8s |%n", "Counter name", "n", "count")
                + "-".repeat(72);
    }

    @Override
    public String reportLine()
    {
        return String.format("| %-48.48s | %6d | %8d |", getDescription(), getN(), getCount());
    }

    /**
     * Return a string representing a footer for a textual table with a monospaced font that can contain multiple statistics.
     * @return footer for the textual table
     */
    public static String reportFooter()
    {
        return "-".repeat(72);
    }

    @Override
    public String toString()
    {
        return "Counter [description=" + this.description + ", n=" + this.n + ", count=" + this.count + "]";
    }
    
}
