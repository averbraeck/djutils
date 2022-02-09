package org.djutils.event;

import java.io.Serializable;

/**
 * The TimedEvent is the reference implementation for a timed event. Because events are often sent over the network, the
 * interface demands that the event, its sourceId, content and timestamp are serializable. It is the repsonsibility of the
 * programmer, though, that the <b>fields</b> of the sourceId, content and timestamp are serializable as well.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <T> the Comparable type that represents time
 */
public class TimedEvent<T extends Comparable<T> & Serializable> extends AbstractEvent
        implements Comparable<TimedEvent<T>>, TimedEventInterface<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140826L;

    /** Time stamp of this TimedEvent. */
    private final T timeStamp;

    /**
     * Construct a new timed event, where compliance with the metadata is verified.
     * @param type TimedEventTypeInterface; the eventType of the event.
     * @param sourceId Serializable; the source of the event.
     * @param content Serializable; the content of the event.
     * @param timeStamp T; the timeStamp.
     */
    public TimedEvent(final TimedEventTypeInterface type, final Serializable sourceId, final Serializable content,
            final T timeStamp)
    {
        this(type, sourceId, content, timeStamp, true);
    }

    /**
     * Construct a new timed event, with a choice to verify compliance with metadata.
     * @param type TimedEventTypeInterface; the eventType of the event.
     * @param sourceId Serializable; the source of the event.
     * @param content Serializable; the content of the event.
     * @param timeStamp T; the timeStamp.
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     */
    public TimedEvent(final TimedEventTypeInterface type, final Serializable sourceId, final Serializable content,
            final T timeStamp, final boolean verifyMetaData)
    {
        super(type, sourceId, content, verifyMetaData);
        this.timeStamp = timeStamp;
    }

    /** {@inheritDoc} */
    @Override
    public T getTimeStamp()
    {
        return this.timeStamp;
    }

    /** {@inheritDoc} */
    @Override
    public TimedEventTypeInterface getType()
    {
        return (TimedEventTypeInterface) super.getType();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.timeStamp == null) ? 0 : this.timeStamp.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        TimedEvent<?> other = (TimedEvent<?>) obj;
        if (this.timeStamp == null)
        {
            if (other.timeStamp != null)
                return false;
        }
        else if (!this.timeStamp.equals(other.timeStamp))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(final TimedEvent<T> o)
    {
        return this.timeStamp.compareTo(o.getTimeStamp());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[" + this.getClass().getName() + ";" + this.getType() + ";" + this.getSourceId() + ";" + this.getContent() + ";"
                + this.getTimeStamp() + "]";
    }

}