package org.djutils.event.util;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.IdProvider;
import org.djutils.event.ref.ReferenceType;
import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

/**
 * The Event producing set provides a set to which one can subscribe interest in entry changes. This class does not keep track
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
 * @param <E> the type of elements in the set
 */
public class EventProducingSet<E> extends EventProducer implements EventListenerInterface, Set<E>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT =
            new EventType("OBJECT_ADDED_EVENT", new MetaData("Size of the set after add", "Size of the set",
                    new ObjectDescriptor("Size of the set after add", "Size of the set", Integer.class)));

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT =
            new EventType("OBJECT_REMOVED_EVENT", new MetaData("Size of the set after remove", "Size of the set",
                    new ObjectDescriptor("Size of the set after remove", "Size of the set", Integer.class)));

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT =
            new EventType("OBJECT_CHANGED_EVENT", new MetaData("Size of the set after change", "Size of the set",
                    new ObjectDescriptor("Size of the set after change", "Size of the set", Integer.class)));

    /** the parent set. */
    private Set<E> parent = null;

    /** the function that produces the id by which the EventProducer can be identified. */
    private final IdProvider sourceIdProvider;

    /**
     * Constructs a new EventProducingSet.
     * @param parent Set&lt;E&gt;; the parent set.
     * @param sourceId Serializable; the id by which the EventProducer can be identified by the EventListener
     */
    public EventProducingSet(final Set<E> parent, final Serializable sourceId)
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
     * Constructs a new EventProducingSet.
     * @param parent Set&lt;E&gt;; the parent set.
     * @param sourceIdProvider IdProvider; the function that produces the id by which the EventProducer can be identified by the
     *            EventListener
     */
    public EventProducingSet(final Set<E> parent, final IdProvider sourceIdProvider)
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
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final E o)
    {
        boolean changed = this.parent.add(o);
        if (changed)
        {
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        }
        else
        {
            this.fireEvent(OBJECT_CHANGED_EVENT, this.parent.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        boolean changed = this.parent.addAll(c);
        if (changed)
        {
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        }
        else
        {
            if (!c.isEmpty())
            {
                this.fireEvent(OBJECT_CHANGED_EVENT, this.parent.size());
            }
        }
        return changed;
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
    public EventProducingIterator<E> iterator()
    {
        EventProducingIterator<E> iterator = new EventProducingIterator<E>(this.parent.iterator(), this.sourceIdProvider);
        // WEAK reference as an iterator is usually local and should be eligible for garbage collection
        iterator.addListener(this, EventProducingIterator.OBJECT_REMOVED_EVENT, ReferenceType.WEAK);
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        // pass through the OBJECT_REMOVED_EVENT from the iterator
        if (event.getType().equals(EventProducingIterator.OBJECT_REMOVED_EVENT))
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        boolean changed = this.parent.remove(o);
        if (changed)
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        boolean changed = this.parent.removeAll(c);
        if (changed)
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        boolean changed = this.parent.retainAll(c);
        if (changed)
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
        return changed;
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
