package org.djutils.event;

import org.djutils.metadata.MetaData;

/**
 * The EventType is the description of a topic used for the subscription to asynchronous events. Event types are used by
 * EventProducers to show which events they potentially fire. EventTypes are typically defined as static final fields. This
 * class only accepts when the producer fires events of type Event, and not a subclass of Event. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class EventType extends AbstractEventType
{
    /** */
    private static final long serialVersionUID = 20200505L;

    /**
     * Construct a new EventType. Only events of the type Event, and no subclasses of Event, can be used to fire events of this
     * type. This means that firing a TimedEvent of this type will result in an error.
     * @param name String; the name of the new eventType. Two values are not appreciated: null and the empty string.
     * @param metaData MetaData; describes the payload of events of the new EventType;
     */
    public EventType(final String name, final MetaData metaData)
    {
        super(name, metaData, Event.class);
    }

    /**
     * Construct a new EventType. The name of the metadata will function as the name of the event. Only events of the type
     * Event, and no subclasses of Event, can be used to fire events of this type. This means that firing a TimedEvent of this
     * type will result in an error.
     * @param metaData MetaData; describes the payload of events of the new EventType;
     */
    public EventType(final MetaData metaData)
    {
        super(metaData == null ? null : metaData.getName(), metaData, Event.class);
    }

    /**
     * Construct a new EventType with no meta data. Only events of the type Event, and no subclasses of Event, can be used to
     * fire events of this type. This means that firing a TimedEvent of this type will result in an error.
     * @param name String; the name of the new eventType. Two values are not appreciated: null and the empty string.
     */
    @Deprecated
    public EventType(final String name)
    {
        super(name, MetaData.NO_META_DATA, Event.class);
    }

}
