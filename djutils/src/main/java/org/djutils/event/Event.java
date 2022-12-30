package org.djutils.event;

import java.io.Serializable;

import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;

/**
 * The Event class forms the reference implementation for the Event. Because events are often sent over the network,
 * the interface demands that source of the event and its content are serializable. It is the responsibility of the programmer,
 * though, that the <b>fields</b> of the sourceId and content are serializable as well.<br>
 * <br>
 * In contrast with earlier implementations of the Event package, a <b>sourceId</b> is sent over the network rather than a
 * pointer to the source itself. This has several advantages:
 * <ol>
 * <li>The object extending the EventProducer does not have to be Serializable itself</li>
 * <li>There is no risk that the entire EventProducer object gets serialized (including subclasses) and is sent over the network
 * <li>There is no risk that the receiver of an event gets a pointer to the sending object, while still being able to identify
 * the sending object
 * </ol>
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
 */
public class Event implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140826L;

    /** The type of the event. */
    private final EventType type;

    /** The content of the event. */
    private final Serializable content;

    /** The source id of an event. */
    private final Serializable sourceId;

    /**
     * Construct a new Event, where compliance with the metadata is verified.
     * @param type EventType; the name of the Event.
     * @param sourceId Serializable; the source id of the sender
     * @param content Serializable; the content of the event
     */
    public Event(final EventType type, final Serializable sourceId, final Serializable content)
    {
        this(type, sourceId, content, true);
    }

    /**
     * Construct a new Event, with a choice to verify compliance with metadata.
     * @param type EventType; the name of the Event.
     * @param sourceId Serializable; the source id of the sender
     * @param content Serializable; the content of the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     */
    public Event(final EventType type, final Serializable sourceId, final Serializable content, final boolean verifyMetaData)
    {
        Throw.whenNull(type, "type cannot be null");
        Throw.whenNull(sourceId, "sourceId cannot be null");
        this.type = type;
        this.sourceId = sourceId;
        this.content = content;
        if (verifyMetaData)
        {
            MetaData metaData = type.getMetaData();
            if (null != metaData)
            {
                if ((null != content) && !(content instanceof Object[]))
                {
                    metaData.verifyComposition(content);
                }
                else
                {
                    metaData.verifyComposition((Object[]) content);
                }
            }
        }
    }

    /**
     * Return the id of the source of the event. The source is, or identifies the sender of the event
     * @return Serializable; the id of the source of the event
     */
    public final Serializable getSourceId()
    {
        return this.sourceId;
    }

    /**
     * Return the content (payload) of this event.
     * @return Serializable; the content (payload) of this event
     */
    public final Serializable getContent()
    {
        return this.content;
    }

    /**
     * Return the type of the event.
     * @return EventType; the type of the event
     */
    public EventType getType()
    {
        return this.type;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
        result = prime * result + ((this.sourceId == null) ? 0 : this.sourceId.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (this.content == null)
        {
            if (other.content != null)
                return false;
        }
        else if (!this.content.equals(other.content))
            return false;
        if (this.sourceId == null)
        {
            if (other.sourceId != null)
                return false;
        }
        else if (!this.sourceId.equals(other.sourceId))
            return false;
        if (this.type == null)
        {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        return true;
    }

}
