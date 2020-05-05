package org.djutils.event;

import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;

/**
 * Reference implementation of the EventType. The AbstractEventType is the description of a topic used for the subscription to
 * asynchronous events. Event types are used by EventProducers to show which events they potentially fire. EventTypes are
 * typically defined as static final fields. In order to prevent name clashes for the EventType, the full name of the class from
 * which the EventType was defined (usually in the &lt;clinit&gt;) is added to the equals() and hashCode() methods of the
 * EventType. In that way, EventTypes that are the same will be unique, but EventTypes with just the same name but defined in
 * different classes will be different. This is the abstract class that can be tailored to any event type. Subclasses that
 * extend the AbstractEventType are the EventType for regular events, and TimedEventType for timed events.<br>
 * <br>
 * Note: the reason why this is important is because <b>remote events</b> that use EventTypes can have <i>multiple versions</i>
 * of the same public static final EventType: one the is defined in the client, and one that is defined via the network. These
 * will have <i>different addresses in memory</i> but they share the same class and name info, so equals() will yield true.
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
public abstract class AbstractEventType implements EventTypeInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The name of the eventType. */
    private final String name;

    /** Meta data (describes the payload). */
    private final MetaData metaData;

    /** The class of a valid event, e.g., to indicate that a TimedEvent is expected. */
    private final Class<? extends EventInterface> validEventType;

    /**
     * The class name from which the event type construction was called; together with the event name this should lead to a
     * unique hash, even when the same name is used in different classes.
     */
    private String definingClassName;

    /**
     * Construct a new EventType with an explicit type of event that is considered to be a valid event when the event is
     * verified. This can, for instance, be used to verify that a TimedEvent is fired instead of an ordinary event.
     * @param name String; the name of the new eventType. Two values are not appreciated: null and the empty string.
     * @param metaData MetaData; describes the payload of events of the new EventType;
     * @param validEventType Class&lt;? extends EventInterface&gt;; the valid class of events
     */
    public AbstractEventType(final String name, final MetaData metaData, final Class<? extends EventInterface> validEventType)
    {
        Throw.when(name == null || name.equals(""), IllegalArgumentException.class,
                "EventType name == null || EventType name == \"\"");
        Throw.whenNull(metaData,
                "Meta data may not be null (but you could provide the NO_META_DATA value if the payload will be varying)");
        this.name = name;
        Throw.whenNull(validEventType, "validEventType may not be null");
        this.validEventType = validEventType;
        StackTraceElement[] steArray = new Throwable().getStackTrace();
        for (StackTraceElement ste : steArray)
        {
            if (!(ste.getClassName().endsWith("EventType")))
            {
                this.definingClassName = ste.getClassName();
                break;
            }
        }
        Throw.whenNull(this.definingClassName, "no defining class name found that is not an EventType");
        this.metaData = metaData;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public MetaData getMetaData()
    {
        return this.metaData;
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends EventInterface> getValidEventType()
    {
        return this.validEventType;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.definingClassName.hashCode();
        result = prime * result + this.name.hashCode();
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
        AbstractEventType other = (AbstractEventType) obj;
        if (!this.name.equals(other.name))
            return false;
        if (!this.definingClassName.equals(other.definingClassName))
            return false;
        return true;
    }

}
