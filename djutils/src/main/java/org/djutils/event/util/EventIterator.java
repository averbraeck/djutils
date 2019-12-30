package org.djutils.event.util;

import java.util.Iterator;

import org.djutils.event.EventProducer;
import org.djutils.event.EventType;

/**
 * The Event producing iterator provides a set to which one can subscribe interest in entry changes. Note that one does not have
 * to subscribe specifically to the events of the EventIterator, as the EventProducing collection subscribes to the
 * EventIterator's remove events and fires these again to its subscribers.
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
 * @param <T> the type of elements to iterate on
 */
public class EventIterator<T> extends EventProducer implements Iterator<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** our parent iterator. */
    private Iterator<T> parent = null;

    /**
     * constructs a new EventIterator, embedding the parent Iterator.
     * @param parent Iterator&lt;T&gt;; parent.
     */
    public EventIterator(final Iterator<T> parent)
    {
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext()
    {
        return getParent().hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public T next()
    {
        return getParent().next();
    }

    /** {@inheritDoc} */
    @Override
    public void remove()
    {
        getParent().remove();
        this.fireEvent(OBJECT_REMOVED_EVENT);
    }

    /**
     * Return the embedded iterator.
     * @return parent Iterator&lt;T&gt;; the embedded iterator
     */
    protected Iterator<T> getParent()
    {
        return this.parent;
    }
    
}
