package org.djutils.stats.summarizers;

import java.io.Serializable;

import javax.swing.table.TableModel;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;

/**
 * The Counter class defines a statistics event counter.
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
public class Counter extends StatisticsObject implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** COUNT_EVENT is fired whenever setCount() is invoked. */
    public static final EventType COUNT_EVENT = new EventType("COUNT_EVENT");

    /** N_EVENT is fired on every new measurement. */
    public static final EventType N_EVENT = new EventType("N_EVENT");

    /** count represents the value of the counter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long count = Long.MIN_VALUE;

    /** n represents the number of measurements. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long n = Long.MIN_VALUE;

    /** description refers to the title of this counter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String description;

    /** the semaphore. */
    private Object semaphore = new Object();

    /**
     * constructs a new CounterTest.
     * @param description String; the description for this counter
     */
    public Counter(final String description)
    {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.description;
    }

    /**
     * Returns the current counter value.
     * @return long the counter value
     */
    public long getCount()
    {
        return this.count;
    }

    /**
     * Returns the current number of observations.
     * @return long the number of observations
     */
    public long getN()
    {
        return this.n;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        long value = 1;
        if (event.getContent() instanceof Number)
        {
            value = Math.round(((Number) event.getContent()).doubleValue());
        }
        else
        {
            throw new RuntimeException("event content for counter not a number but of type " + event.getClass());
        }

        synchronized (this.semaphore)
        {
            this.setCount(this.count + value);
            this.setN(this.n + 1);
            if (hasListeners())
            {
                this.fireEvent(Counter.COUNT_EVENT, this.count);
                this.fireEvent(Counter.N_EVENT, this.n);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.description;
    }

    /**
     * initializes the counter.
     */
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.setN(0);
            this.setCount(0);
        }
    }

    /**
     * is the counter initialized?
     * @return returns whether the counter is initialized
     */
    public boolean isInitialized()
    {
        return this.n != Long.MIN_VALUE;
    }

    /**
     * sets the count.
     * @param count long; the value
     */
    private void setCount(final long count)
    {
        this.count = count;
        this.fireEvent(new Event(COUNT_EVENT, getSourceId(), Long.valueOf(this.count)));
    }

    /**
     * sets n.
     * @param n long; the number of measurements.
     */
    private void setN(final long n)
    {
        this.n = n;
        this.fireEvent(new Event(N_EVENT, getSourceId(), Long.valueOf(this.n)));
    }

    /**
     * returns the description of the counter.
     * @return String the description
     */
    public String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public TableModel getTable()
    {
        String[] columnNames = {"field", "value"};
        EventType[] eventTypes = {null, Counter.N_EVENT, Counter.COUNT_EVENT};
        StatisticsTableModel result = new StatisticsTableModel(columnNames, eventTypes, 3);
        this.addListener(result, Counter.N_EVENT, ReferenceType.STRONG);
        this.addListener(result, Counter.COUNT_EVENT, ReferenceType.STRONG);

        result.setValueAt("name", 0, 0);
        result.setValueAt("n", 1, 0);
        result.setValueAt("count", 2, 0);
        result.setValueAt(this.description, 0, 1);
        result.setValueAt(Long.valueOf(this.n), 1, 1);
        result.setValueAt(Long.valueOf(this.count), 2, 1);
        return result;
    }
}
