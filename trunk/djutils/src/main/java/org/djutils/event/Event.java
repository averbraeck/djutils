package org.djutils.event;

/**
 * The Event class forms the reference implementation for the EventInterface.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    private final Object content;

    /** the source of an event. */
    private final Object source;

    /**
     * constructs a new Event.
     * @param type EventType; the name of the Event.
     * @param source Object; the source of the sender.
     * @param content Object; the content of the event.
     */
    public Event(final EventType type, final Object source, final Object content)
    {
        this.type = type;
        this.source = source;
        this.content = content;
    }

    /** {@inheritDoc} */
    @Override
    public final Object getSource()
    {
        return this.source;
    }

    /** {@inheritDoc} */
    @Override
    public final Object getContent()
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
