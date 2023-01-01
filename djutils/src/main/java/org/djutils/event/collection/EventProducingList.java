package org.djutils.event.collection;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventProducer;
import org.djutils.event.EventProducingObject;
import org.djutils.event.EventType;
import org.djutils.event.reference.ReferenceType;
import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

/**
 * The Event producing list provides a list to which one can subscribe interest in entry changes. This class does not keep track
 * of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>. A listener
 * must subscribe to the iterator individually.
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
 * @param <E> the type of elements in the list
 */
public class EventProducingList<E> implements EventProducingObject, EventListener, List<E>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT =
            new EventType("OBJECT_ADDED_EVENT", new MetaData("Size of the list after add", "Size of the list",
                    new ObjectDescriptor("Size of the list after add", "Size of the list", Integer.class)));

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT =
            new EventType("OBJECT_REMOVED_EVENT", new MetaData("Size of the list after remove", "Size of the list",
                    new ObjectDescriptor("Size of the list after remove", "Size of the list", Integer.class)));

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT =
            new EventType("OBJECT_CHANGED_EVENT", new MetaData("Size of the list after change", "Size of the list",
                    new ObjectDescriptor("Size of the list after change", "Size of the list", Integer.class)));

    /** the wrapped list. */
    private List<E> wrappedList = null;

    /** the embedded event producer. */
    private final EventProducer eventProducer;

    /**
     * constructs a new EventProducingList.
     * @param wrappedList List&lt;E&gt;; the embedded list.
     */
    public EventProducingList(final List<E> wrappedList)
    {
        this(wrappedList, new EventProducer());
    }

    /**
     * Constructs a new EventProducingList.
     * @param wrappedList List&lt;E&gt;; the embedded list.
     * @param eventProducer EventProducer; the EventProducer to send events to the subscribers
     */
    public EventProducingList(final List<E> wrappedList, final EventProducer eventProducer)
    {
        Throw.whenNull(wrappedList, "wrappedList cannot be null");
        Throw.whenNull(eventProducer, "eventProducer cannot be null");
        this.eventProducer = eventProducer;
        this.wrappedList = wrappedList;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.wrappedList.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.wrappedList.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        int nr = this.wrappedList.size();
        this.wrappedList.clear();
        if (nr != this.wrappedList.size())
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedList.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void add(final int index, final E element)
    {
        this.wrappedList.add(index, element);
        this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedList.size());
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final E o)
    {
        boolean result = this.wrappedList.add(o);
        if (result)
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedList.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        boolean result = this.wrappedList.addAll(c);
        if (result)
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedList.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c)
    {
        boolean result = this.wrappedList.addAll(index, c);
        if (result)
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedList.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o)
    {
        return this.wrappedList.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return this.wrappedList.containsAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public E get(final int index)
    {
        return this.wrappedList.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public int indexOf(final Object o)
    {
        return this.wrappedList.indexOf(o);
    }

    /** {@inheritDoc} */
    @Override
    public EventProducingIterator<E> iterator()
    {
        EventProducingIterator<E> iterator = new EventProducingIterator<E>(this.wrappedList.iterator());
        // WEAK reference as an iterator is usually local and should be eligible for garbage collection
        iterator.getEventProducer().addListener(this, EventProducingIterator.OBJECT_REMOVED_EVENT, ReferenceType.WEAK);
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public EventProducingListIterator<E> listIterator()
    {
        return listIterator(0);
    }

    /** {@inheritDoc} */
    @Override
    public EventProducingListIterator<E> listIterator(final int index)
    {
        EventProducingListIterator<E> iterator = new EventProducingListIterator<E>(this.wrappedList.listIterator(index));
        // WEAK references as an iterator is usually local and should be eligible for garbage collection
        iterator.getEventProducer().addListener(this, EventProducingIterator.OBJECT_REMOVED_EVENT, ReferenceType.WEAK);
        iterator.getEventProducer().addListener(this, EventProducingListIterator.OBJECT_ADDED_EVENT, ReferenceType.WEAK);
        iterator.getEventProducer().addListener(this, EventProducingListIterator.OBJECT_CHANGED_EVENT, ReferenceType.WEAK);
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final Event event) throws RemoteException
    {
        // pass through the OBJECT_REMOVED_EVENT from the iterator
        if (event.getType().equals(EventProducingIterator.OBJECT_REMOVED_EVENT))
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedList.size());
        }
        else if (event.getType().equals(EventProducingListIterator.OBJECT_ADDED_EVENT))
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedList.size());
        }
        else if (event.getType().equals(EventProducingListIterator.OBJECT_CHANGED_EVENT))
        {
            this.eventProducer.fireEvent(OBJECT_CHANGED_EVENT, this.wrappedList.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public int lastIndexOf(final Object o)
    {
        return this.wrappedList.lastIndexOf(o);
    }

    /** {@inheritDoc} */
    @Override
    public E remove(final int index)
    {
        E result = this.wrappedList.remove(index);
        this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedList.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        boolean result = this.wrappedList.remove(o);
        if (result)
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedList.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        boolean result = this.wrappedList.removeAll(c);
        if (result)
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedList.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        boolean result = this.wrappedList.retainAll(c);
        if (result)
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedList.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public E set(final int index, final E element)
    {
        E result = this.wrappedList.set(index, element);
        this.eventProducer.fireEvent(OBJECT_CHANGED_EVENT, this.wrappedList.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<E> subList(final int fromIndex, final int toIndex)
    {
        return this.wrappedList.subList(fromIndex, toIndex);
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return this.wrappedList.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <T> T[] toArray(final T[] a)
    {
        return this.wrappedList.toArray(a);
    }

    /**
     * Return the embedded EventProducer.
     * @return EventProducer; the embedded EventProducer
     */
    public EventProducer getEventProducer()
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
