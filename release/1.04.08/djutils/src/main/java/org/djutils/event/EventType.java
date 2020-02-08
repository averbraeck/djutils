package org.djutils.event;

import java.io.Serializable;

/**
 * The EventType is a masker used for the subscription to asynchronous events. Eventtypes are used by EventProducers to show
 * which events they potentially fire. EventTypes should be defined as static final fields. The EventType does not have a
 * hashCode based on its name anymore, because in that case an expensive check should be done to search for name clashes in the
 * class hierarchy. By using the 'address' of the object, different EventTypes have by definition a unique hashCode. 
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
     * Construct a new EventType.
     * @param name String; the name of this eventType. Two values are not appreciated : &lt;code&gt;null&lt;/code&gt; and
     *            &lt;code&gt;""&lt;/code&gt;.
     */
    public EventType(final String name)
    {
        if (name == null || name.equals(""))
        {
            throw new IllegalArgumentException("EventType name == null || EventType name == \"\"");
        }
        this.name = name;
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
}
