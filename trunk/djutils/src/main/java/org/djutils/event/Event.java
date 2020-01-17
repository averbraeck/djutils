package org.djutils.event;

import java.io.Serializable;

/**
 * The Event class forms the reference implementation for the EventInterface. Because events are often sent over the network,
 * the interface demands that the event and its source and content are serializable. It is the repsonsibility of the programmer,
 * though, that the <b>content</b> of the object is serializable as well.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Event implements EventInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140826L;

    /** type is the type of the event. */
    private final EventType type;

    /** content refers to the content of the event. */
    private final Serializable content;

    /** the source of an event. */
    private final Serializable source;

    /**
     * constructs a new Event.
     * @param type EventType; the name of the Event.
     * @param source Object; the source of the sender.
     * @param content Object; the content of the event.
     */
    public Event(final EventType type, final Serializable source, final Serializable content)
    {
        this.type = type;
        this.source = source;
        this.content = content;
    }

    /** {@inheritDoc} */
    @Override
    public final Serializable getSource()
    {
        return this.source;
    }

    /** {@inheritDoc} */
    @Override
    public final Serializable getContent()
    {
        return this.content;
    }

    /** {@inheritDoc} */
    @Override
    public final EventType getType()
    {
        return this.type;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[" + this.getClass().getName() + ";" + this.getType() + ";" + this.getSource() + ";" + this.getContent() + "]";
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
        result = prime * result + ((this.source == null) ? 0 : this.source.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        // content
        Event other = (Event) obj;
        if (this.content == null)
        {
            if (other.content != null)
            {
                return false;
            }
        }
        else if (!this.content.equals(other.content))
        {
            return false;
        }

        // source
        if (this.source == null)
        {
            if (other.source != null)
            {
                return false;
            }
        }
        else if (!this.source.equals(other.source))
        {
            return false;
        }

        // type
        if (this.type == null)
        {
            if (other.type != null)
            {
                return false;
            }
        }
        else if (!this.type.equals(other.type))
        {
            return false;
        }
        return true;
    }
}
