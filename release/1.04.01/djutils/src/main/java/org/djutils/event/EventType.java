package org.djutils.event;

import java.io.Serializable;

/**
 * The EventType is a masker used for the subscription to asynchronous events. Eventtypes are used by EventProducers to show
 * which events they potentially fire. EventTypes should be defined as static final fields.
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
public final class EventType implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** the internal representation of the hashCode. */
    private final int hashcode;

    /** name refers to the name of the eventType. */
    private final String name;

    /**
     * constructs a new EventType.
     * @param name String; the name of this eventType. Two values are not appreciated : &lt;code&gt;null&lt;/code&gt; and
     *            &lt;code&gt;""&lt;/code&gt;.
     */
    public EventType(final String name)
    {
        super();
        if (name == null || name.equals(""))
        {
            throw new IllegalArgumentException("EventType name == null || EventType name == \"\"");
        }
        this.hashcode = name.hashCode();
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object)
    {
        if (!(object instanceof EventType))
        {
            return false;
        }
        return this.hashcode == ((EventType) object).hashcode;
    }

    /**
     * Returns a hash code for the name of the this eventType. The hash code for an <code>EventType</code> object is computed as
     * <blockquote>
     * 
     * <pre>
     *        s[0]*31&circ;(n-1) + s[1]*31&circ;(n-2) + ... + s[n-1]
     * </pre>
     * 
     * </blockquote> using <code>int</code> arithmetic, where <code>s[i]</code> is the <i>i </i>th character of the name of the
     * eventType, <code>n</code> is the length of the name, and <code>^</code> indicates exponentiation. This algorithm assures
     * JVM, host, time independence.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return this.hashcode;
    }

    /**
     * @return the event type name
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
