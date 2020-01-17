package org.djutils.event.util;

import java.util.ListIterator;

import org.djutils.event.EventType;

/**
 * ListEventIterator provides an iterator embedding the ListIterator, which fires an event when an object has been removed. Note
 * that one does not have to subscribe specifically to the events of the EventIterator, as the EventProducing collection
 * subscribes to the ListEventIterator's remove events and fires these again to its subscribers.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <T> the type of elements to iterate on
 */
public class ListEventIterator<T> extends EventIterator<T> implements ListIterator<T>
{
    /** */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on adding of entries. */
    public static final EventType OBJECT_ADDED_EVENT = new EventType("OBJECT_ADDED_EVENT");

    /** OBJECT_CHANGED_EVENT is fired on changing of entries. */
    public static final EventType OBJECT_CHANGED_EVENT = new EventType("OBJECT_CHANGED_EVENT");

    /**
     * constructs a new ListEventIterator, embedding the parent ListIterator.
     * @param parent Iterator&lt;T&gt;; embedded iterator.
     */
    public ListEventIterator(final ListIterator<T> parent)
    {
        super(parent);
    }

    /** {@inheritDoc} */
    @Override
    public ListIterator<T> getParent()
    {
        return (ListIterator<T>) super.getParent();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean hasPrevious()
    {
        return getParent().hasPrevious();
    }

    /** {@inheritDoc} */
    @Override
    public T previous()
    {
        return getParent().previous();
    }

    /** {@inheritDoc} */
    @Override
    public int nextIndex()
    {
        return getParent().nextIndex();
    }

    /** {@inheritDoc} */
    @Override
    public int previousIndex()
    {
        return getParent().previousIndex();
    }

    /** {@inheritDoc} */
    @Override
    public void set(final T e)
    {
        getParent().set(e);
        fireEvent(OBJECT_CHANGED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    public void add(final T e)
    {
        getParent().add(e);
        fireEvent(OBJECT_ADDED_EVENT);
    }

}
