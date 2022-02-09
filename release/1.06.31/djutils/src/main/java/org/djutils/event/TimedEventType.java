package org.djutils.event;

import org.djutils.metadata.MetaData;

/**
 * The TimedEventType is the description of a topic used for the subscription to asynchronous events with a time stamp. Event
 * types are used by EventProducers to show which events they potentially fire. EventTypes are typically defined as static final
 * fields. This class only accepts when the producer fires events of type TimedEvent, and not a generic Event or subclass of
 * TimedEvent. <br>
 * <br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TimedEventType extends AbstractEventType implements TimedEventTypeInterface
{
    /** */
    private static final long serialVersionUID = 20200505L;

    /**
     * Construct a new TimedEventType. Only events of the type TimedEvent, but not classes of Event or subclasses of TimedEvent,
     * can be used to fire events of this type. This means that firing an ordinary Event using this EventType will result in an
     * error.
     * @param name String; the name of the new eventType. Two values are not appreciated: null and the empty string.
     * @param metaData MetaData; describes the payload of events of the new EventType;
     */
    public TimedEventType(final String name, final MetaData metaData)
    {
        super(name, metaData, TimedEvent.class);
    }

    /**
     * Construct a new TimedEventType. The name of the metadata will function as the name of the event. Only events of the type
     * TimedEvent, but not classes of Event or subclasses of TimedEvent, can be used to fire events of this type. This means
     * that firing an ordinary Event using this EventType will result in an error.
     * @param metaData MetaData; describes the payload of events of the new EventType;
     */
    public TimedEventType(final MetaData metaData)
    {
        super(metaData == null ? null : metaData.getName(), metaData, TimedEvent.class);
    }

    /**
     * Construct a new TimedEventType with no meta data. Only events of the type TimedEvent, but not classes of Event or
     * subclasses of TimedEvent, can be used to fire events of this type. This means that firing an ordinary Event using this
     * EventType will result in an error.
     * @param name String; the name of the new eventType. Two values are not appreciated: null and the empty string.
     */
    @Deprecated
    public TimedEventType(final String name)
    {
        super(name, MetaData.NO_META_DATA, TimedEvent.class);
    }

}
