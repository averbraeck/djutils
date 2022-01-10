package org.djutils.event;

import java.io.Serializable;

/**
 * The Event class forms the reference implementation for the EventInterface. Because events are often sent over the network,
 * the interface demands that source of the event and its content are serializable. It is the responsibility of the programmer,
 * though, that the <b>fields</b> of the sourceId and content are serializable as well.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Event extends AbstractEvent
{
    /** */
    private static final long serialVersionUID = 20200505L;

    /**
     * Construct a new Event, where compliance with the metadata is verified.
     * @param type EventTypeInterface; the name of the Event.
     * @param sourceId Serializable; the source id of the sender
     * @param content Serializable; the content of the event
     */
    public Event(final EventTypeInterface type, final Serializable sourceId, final Serializable content)
    {
        super(type, sourceId, content);
    }

    /**
     * Construct a new Event, with a choice to verify compliance with metadata.
     * @param type EventTypeInterface; the name of the Event.
     * @param sourceId Serializable; the source id of the sender
     * @param content Serializable; the content of the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     */
    public Event(final EventTypeInterface type, final Serializable sourceId, final Serializable content,
            final boolean verifyMetaData)
    {
        super(type, sourceId, content, verifyMetaData);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[" + this.getClass().getName() + ";" + this.getType() + ";" + this.getSourceId() + ";" + this.getContent()
                + "]";
    }

}
