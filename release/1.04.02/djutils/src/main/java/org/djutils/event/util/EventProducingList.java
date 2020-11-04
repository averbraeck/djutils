package org.djutils.event.util;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;

/**
 * The Event producing list provides a list to which one can subscribe interest in entry changes. This class does not keep track
 * of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>. A listener
 * must subscribe to the iterator individually.
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
 * @param <E> the type of elements in the list
 */
public class EventProducingList<E> extends EventProducer implements EventListenerInterface, List<E>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT = new EventType("OBJECT_ADDED_EVENT");

    /** OBJECT_REMOVED_EVENT is fired on removel of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT = new EventType("OBJECT_CHANGED_EVENT");

    /** the parent list. */
    private List<E> parent = null;

    /**
     * constructs a new EventProducingList.
     * @param parent List&lt;T&gt;; the parent list.
     */
    public EventProducingList(final List<E> parent)
    {
        super();
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.parent.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.parent.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        int nr = this.parent.size();
        this.parent.clear();
        if (nr != this.parent.size())
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
    }

    /** {@inheritDoc} */
    @Override
    public void add(final int index, final E element)
    {
        this.parent.add(index, element);
        this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final E o)
    {
        boolean result = this.parent.add(o);
        if (result)
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        boolean result = this.parent.addAll(c);
        if (result)
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c)
    {
        boolean result = this.parent.addAll(index, c);
        if (result)
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o)
    {
        return this.parent.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return this.parent.containsAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public E get(final int index)
    {
        return this.parent.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public int indexOf(final Object o)
    {
        return this.parent.indexOf(o);
    }

    /** {@inheritDoc} */
    @Override
    public EventIterator<E> iterator()
    {
        EventIterator<E> iterator = new EventIterator<E>(this.parent.iterator());
        // WEAK reference as an iterator is usually local and should be eligible for garbage collection
        iterator.addListener(this, EventIterator.OBJECT_REMOVED_EVENT, ReferenceType.WEAK);
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public ListEventIterator<E> listIterator()
    {
        return listIterator(0);
    }

    /** {@inheritDoc} */
    @Override
    public ListEventIterator<E> listIterator(final int index)
    {
        ListEventIterator<E> iterator = new ListEventIterator<E>(this.parent.listIterator(index));
        // WEAK references as an iterator is usually local and should be eligible for garbage collection
        iterator.addListener(this, EventIterator.OBJECT_REMOVED_EVENT, ReferenceType.WEAK);
        iterator.addListener(this, ListEventIterator.OBJECT_ADDED_EVENT, ReferenceType.WEAK);
        iterator.addListener(this, ListEventIterator.OBJECT_CHANGED_EVENT, ReferenceType.WEAK);
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        // pass through the OBJECT_REMOVED_EVENT from the iterator
        if (event.getType().equals(EventIterator.OBJECT_REMOVED_EVENT))
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
        else if (event.getType().equals(ListEventIterator.OBJECT_ADDED_EVENT))
        {
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        }
        else if (event.getType().equals(ListEventIterator.OBJECT_CHANGED_EVENT))
        {
            this.fireEvent(OBJECT_CHANGED_EVENT, null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int lastIndexOf(final Object o)
    {
        return this.parent.lastIndexOf(o);
    }

    /** {@inheritDoc} */
    @Override
    public E remove(final int index)
    {
        E result = this.parent.remove(index);
        this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        boolean result = this.parent.remove(o);
        if (result)
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        boolean result = this.parent.removeAll(c);
        if (result)
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        boolean result = this.parent.retainAll(c);
        if (result)
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public E set(final int index, final E element)
    {
        E result = this.parent.set(index, element);
        this.fireEvent(OBJECT_CHANGED_EVENT, null);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<E> subList(final int fromIndex, final int toIndex)
    {
        return this.parent.subList(fromIndex, toIndex);
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return this.parent.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <T> T[] toArray(final T[] a)
    {
        return this.parent.toArray(a);
    }
}