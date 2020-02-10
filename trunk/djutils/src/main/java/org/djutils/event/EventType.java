package org.djutils.event;

import java.io.Serializable;

/**
 * The EventType is a masker used for the subscription to asynchronous events. Eventtypes are used by EventProducers to show
 * which events they potentially fire. EventTypes should be defined as static final fields. In order to prevent name clashes for
 * the EventType, the full name of the class from which the EventType was defined (usually in the &lt;clinit&gt;) is added to
 * the equals() and hashCode() methods of the EventType. In that way, EventTypes that are the same will be unique, but
 * EventTypes with just the same name but defined in different classes will be different. <br>
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
public final class EventType implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The name of the eventType. */
    private final String name;

    /**
     * The class name from which the event type construction was called; together with the event name this should lead to a
     * unique hash, even when the same name is used in different classes.
     */
    private final String definingClassName;

    /**
     * Construct a new EventType.
     * @param name String; the name of this eventType. Two values are not appreciated: null and the empty string.
     */
    public EventType(final String name)
    {
        if (name == null || name.equals(""))
        {
            throw new IllegalArgumentException("EventType name == null || EventType name == \"\"");
        }
        this.name = name;
        StackTraceElement[] steArray = new Throwable().getStackTrace();
        this.definingClassName = steArray[1].getClassName();
    }

    /**
     * Return the event type name.
     * @return String; the event type name
     */
    public String getName()
    {
        return this.name;
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
        EventType other = (EventType) obj;
        if (!this.name.equals(other.name))
            return false;
        if (!this.definingClassName.equals(other.definingClassName))
            return false;
        return true;
    }

}
