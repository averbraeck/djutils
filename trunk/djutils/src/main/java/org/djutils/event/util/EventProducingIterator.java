package org.djutils.event.util;

import java.io.Serializable;
import java.util.Iterator;

import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.IdProvider;
import org.djutils.exceptions.Throw;

/**
 * The EventProducingIterator provides an iterator embedding the Iterator, which fires an event when an object has been removed.
 * Note that one does not have to subscribe specifically to the events of the EventProducingIterator, as the EventProducing
 * collection subscribes to the EventProducingIterator's remove events and fires these again to its subscribers.
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
 * @param <T> the type of elements to iterate on
 */
public class EventProducingIterator<T> extends EventProducer implements Iterator<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT", null);

    /** our parent iterator. */
    private Iterator<T> parent = null;

    /** the function that produces the id by which the EventProducer can be identified. */
    private final IdProvider sourceIdProvider;

    /**
     * constructs a new EventProducingIterator, embedding the parent Iterator.
     * @param parent Iterator&lt;T&gt;; parent.
     * @param sourceId Serializable; the id by which the EventProducer can be identified by the EventListener
     */
    public EventProducingIterator(final Iterator<T> parent, final Serializable sourceId)
    {
        this(parent, new IdProvider()
        {
            /** */
            private static final long serialVersionUID = 20200119L;

            @Override
            public Serializable id()
            {
                return sourceId;
            }
        });
    }

    /**
     * Constructs a new EventProducingIterator, embedding the parent iterator.
     * @param parent Iterator&lt;T&gt;; the parent set.
     * @param sourceIdProvider IdProvider; the function that produces the id by which the EventProducer can be identified by the
     *            EventListener
     */
    public EventProducingIterator(final Iterator<T> parent, final IdProvider sourceIdProvider)
    {
        Throw.whenNull(parent, "parent cannot be null");
        Throw.whenNull(sourceIdProvider, "sourceIdprovider cannot be null");
        this.parent = parent;
        this.sourceIdProvider = sourceIdProvider;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.sourceIdProvider.id();
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