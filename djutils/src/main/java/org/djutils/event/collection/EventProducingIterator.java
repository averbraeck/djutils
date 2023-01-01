package org.djutils.event.collection;

import java.io.Serializable;
import java.util.Iterator;

import org.djutils.event.EventListener;
import org.djutils.event.LocalEventProducer;
import org.djutils.event.EventProducingObject;
import org.djutils.event.EventType;
import org.djutils.event.reference.ReferenceType;
import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;

/**
 * The EventProducingIterator provides an iterator embedding the Iterator, which fires an event when an object has been removed.
 * Note that one does not have to subscribe specifically to the events of the EventProducingIterator, as the EventProducing
 * collection subscribes to the EventProducingIterator's remove events and fires these again to its subscribers.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
public class EventProducingIterator<T> implements EventProducingObject, Iterator<T>, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT", MetaData.NO_META_DATA);

    /** our parent iterator. */
    private Iterator<T> wrappedIterator = null;

    /** the embedded event producer. */
    private final LocalEventProducer eventProducer;

    /**
     * constructs a new EventProducingIterator, embedding the parent Iterator.
     * @param wrappedIterator Iterator&lt;T&gt;; parent.
     */
    public EventProducingIterator(final Iterator<T> wrappedIterator)
    {
        this(wrappedIterator, new LocalEventProducer());
    }

    /**
     * Constructs a new EventProducingIterator, embedding the parent iterator.
     * @param wrappedIterator Iterator&lt;T&gt;; the wrapped iterator.
     * @param eventProducer EventProducer; the EventProducer to send events to the subscribers
     */
    public EventProducingIterator(final Iterator<T> wrappedIterator, final LocalEventProducer eventProducer)
    {
        Throw.whenNull(wrappedIterator, "parent cannot be null");
        Throw.whenNull(eventProducer, "eventProducer cannot be null");
        this.eventProducer = eventProducer;
        this.wrappedIterator = wrappedIterator;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext()
    {
        return getWrappedIterator().hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public T next()
    {
        return getWrappedIterator().next();
    }

    /** {@inheritDoc} */
    @Override
    public void remove()
    {
        getWrappedIterator().remove();
        this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT);
    }

    /**
     * Return the embedded iterator.
     * @return parent Iterator&lt;T&gt;; the embedded iterator
     */
    protected Iterator<T> getWrappedIterator()
    {
        return this.wrappedIterator;
    }

    /**
     * Return the embedded EventProducer.
     * @return EventProducer; the embedded EventProducer
     */
    public LocalEventProducer getEventProducer()
    {
        return this.eventProducer;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(final EventListener listener, final EventType eventType, final int position,
            final ReferenceType referenceType)
    {
        return getEventProducer().addListener(listener, eventType, position, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeListener(final EventListener listener, final EventType eventType)
    {
        return getEventProducer().removeListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners()
    {
        return getEventProducer().removeAllListeners();
    }

}
