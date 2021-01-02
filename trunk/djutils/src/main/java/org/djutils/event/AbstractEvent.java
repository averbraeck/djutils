package org.djutils.event;

import java.io.Serializable;

import org.djutils.metadata.MetaData;

/**
 * The Event class forms the reference implementation for the EventInterface. Because events are often sent over the network,
 * the interface demands that source of the event and its content are serializable. It is the responsibility of the programmer,
 * though, that the <b>fields</b> of the sourceId and content are serializable as well.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class AbstractEvent implements EventInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140826L;

    /** The type of the event. */
    private final EventTypeInterface type;

    /** The content of the event. */
    private final Serializable content;

    /** The source id of an event. */
    private final Serializable sourceId;

    /**
     * Construct a new Event, where compliance with the metadata is verified.
     * @param type EventTypeInterface; the name of the Event.
     * @param sourceId Serializable; the source id of the sender
     * @param content Serializable; the content of the event
     */
    public AbstractEvent(final EventTypeInterface type, final Serializable sourceId, final Serializable content)
    {
        this(type, sourceId, content, true);
    }

    /**
     * Construct a new Event, with a choice to verify compliance with metadata.
     * @param type EventTypeInterface; the name of the Event.
     * @param sourceId Serializable; the source id of the sender
     * @param content Serializable; the content of the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     */
    public AbstractEvent(final EventTypeInterface type, final Serializable sourceId, final Serializable content,
            final boolean verifyMetaData)
    {
        this.type = type;
        this.sourceId = sourceId;
        this.content = content;
        if (verifyMetaData && null != this.type)
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

    /** {@inheritDoc} */
    @Override
    public final Serializable getSourceId()
    {
        return this.sourceId;
    }

    /** {@inheritDoc} */
    @Override
    public final Serializable getContent()
    {
        return this.content;
    }

    /** {@inheritDoc} */
    @Override
    public EventTypeInterface getType()
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
        AbstractEvent other = (AbstractEvent) obj;
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
