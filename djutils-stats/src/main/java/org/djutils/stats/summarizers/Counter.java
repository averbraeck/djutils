package org.djutils.stats.summarizers;

/**
 * The Counter class defines a statistics event counter.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Counter implements CounterInterface
{
    /** */
    private static final long serialVersionUID = 20200228L;

    /** count represents the value of the counter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long count = 0;

    /** n represents the number of measurements. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long n = 0;

    /** description refers to the title of this counter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String description;

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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
    public long register(final long value)
    {
        synchronized (this.semaphore)
        {
            this.count += value;
            this.n++;
        }
        return value;
    }

    /** {@inheritDoc} */
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
    public String toString()
    {
        return this.description;
    }

}
