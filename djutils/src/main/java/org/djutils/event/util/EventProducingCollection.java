package org.djutils.event.util;

import java.rmi.RemoteException;
import java.util.Collection;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

/**
 * The Event producing collection provides a set to which one can subscribe interest in entry changes. This class does not keep
 * track of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>. A
 * listener must subscribe to the iterator individually.
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
 * @param <T> The type of the event producing Collection.
 */
public class EventProducingCollection<T> implements EventListener, Collection<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT =
            new EventType("OBJECT_ADDED_EVENT", new MetaData("Size of the collection after add", "Size of the collection",
                    new ObjectDescriptor("Size of the collection after add", "Size of the collection", Integer.class)));

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT =
            new EventType("OBJECT_REMOVED_EVENT", new MetaData("Size of the collection after remove", "Size of the collection",
                    new ObjectDescriptor("Size of the collection after remove", "Size of the collection", Integer.class)));

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT =
            new EventType("OBJECT_CHANGED_EVENT", new MetaData("Size of the collection after change", "Size of the collection",
                    new ObjectDescriptor("Size of the collection after change", "Size of the collection", Integer.class)));

    /** the wrapped collection. */
    private final Collection<T> wrappedCollection;

    /** the embedded event producer. */
    private final EventProducer eventProducer;

    /**
     * constructs a new EventProducingCollection with a local EventProducer.
     * @param wrappedCollection Collection&lt;T&gt;; the wrapped collection.
     */
    public EventProducingCollection(final Collection<T> wrappedCollection)
    {
        this(wrappedCollection, new EventProducer());
    }

    /**
     * constructs a new EventProducingCollection with a specific EventProducer.
     * @param wrappedCollection Collection&lt;T&gt;; the wrapped collection.
     * @param eventProducer EventProducer; the EventProducer to send events to the subscribers
     */
    public EventProducingCollection(final Collection<T> wrappedCollection, final EventProducer eventProducer)
    {
        Throw.whenNull(wrappedCollection, "wrappedCollection cannot be null");
        Throw.whenNull(eventProducer, "eventProducer cannot be null");
        this.eventProducer = eventProducer;
        this.wrappedCollection = wrappedCollection;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.wrappedCollection.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.wrappedCollection.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        int nr = this.wrappedCollection.size();
        this.wrappedCollection.clear();
        if (nr != this.wrappedCollection.size())
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedCollection.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final T o)
    {
        boolean changed = this.wrappedCollection.add(o);
        if (changed)
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedCollection.size());
        }
        else
        {
            this.eventProducer.fireEvent(OBJECT_CHANGED_EVENT, this.wrappedCollection.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends T> c)
    {
        boolean changed = this.wrappedCollection.addAll(c);
        if (changed)
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedCollection.size());
        }
        else
        {
            if (!c.isEmpty())
            {
                this.eventProducer.fireEvent(OBJECT_CHANGED_EVENT, this.wrappedCollection.size());
            }
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o)
    {
        return this.wrappedCollection.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return this.wrappedCollection.containsAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public EventProducingIterator<T> iterator()
    {
        EventProducingIterator<T> iterator = new EventProducingIterator<T>(this.wrappedCollection.iterator());
        // WEAK reference as an iterator is usually local and should be eligible for garbage collection
        iterator.getEventProducer().addListener(this, EventProducingIterator.OBJECT_REMOVED_EVENT, ReferenceType.WEAK);
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final Event event) throws RemoteException
    {
        // pass through the OBJECT_REMOVED_EVENT from the iterator
        if (event.getType().equals(EventProducingIterator.OBJECT_REMOVED_EVENT))
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedCollection.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        boolean changed = this.wrappedCollection.remove(o);
        if (changed)
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedCollection.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        boolean changed = this.wrappedCollection.removeAll(c);
        if (changed)
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedCollection.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        boolean changed = this.wrappedCollection.retainAll(c);
        if (changed)
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedCollection.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return this.wrappedCollection.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <E> E[] toArray(final E[] a)
    {
        return this.wrappedCollection.toArray(a);
    }

    /**
     * Return the embedded EventProducer.
     * @return EventProducer; the embedded EventProducer
     */
    public EventProducer getEventProducer()
    {
        return this.eventProducer;
    }

}
