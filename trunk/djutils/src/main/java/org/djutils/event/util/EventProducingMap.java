package org.djutils.event.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.IdProvider;
import org.djutils.exceptions.Throw;

/**
 * The Event producing map provides a map to which one can subscribe interest in entry changes. This class does not keep track
 * of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>. A listener
 * must subscribe to the iterator, key set, etc. individually.
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
 * @param <K> the key type
 * @param <V> the value type
 */
public class EventProducingMap<K, V> extends EventProducer implements Map<K, V>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT = new EventType("OBJECT_ADDED_EVENT", null);

    /** OBJECT_REMOVED_EVENT is fired on removel of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT", null);

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT = new EventType("OBJECT_CHANGED_EVENT", null);

    /** the parent map. */
    private final Map<K, V> parent;

    /** the function that produces the id by which the EventProducer can be identified. */
    private final IdProvider sourceIdProvider;

    /**
     * constructs a new EventProducingMap.
     * @param parent Map&lt;K,V&gt;; the embedded map.
     * @param sourceId Serializable; the id by which the EventProducer can be identified by the EventListener
     */
    public EventProducingMap(final Map<K, V> parent, final Serializable sourceId)
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
     * Constructs a new EventProducingMap.
     * @param parent Map&lt;K, V&gt;; the parent map.
     * @param sourceIdProvider IdProvider; the function that produces the id by which the EventProducer can be identified by the
     *            EventListener
     */
    public EventProducingMap(final Map<K, V> parent, final IdProvider sourceIdProvider)
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
    public boolean containsKey(final Object key)
    {
        return this.parent.containsKey(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(final Object value)
    {
        return this.parent.containsValue(value);
    }

    /** {@inheritDoc} */
    @Override
    public V get(final Object key)
    {
        return this.parent.get(key);
    }

    /** {@inheritDoc} */
    @Override
    public V put(final K key, final V value)
    {
        int nr = this.parent.size();
        V result = this.parent.put(key, value);
        if (nr != this.parent.size())
        {
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        }
        else
        {
            this.fireEvent(OBJECT_CHANGED_EVENT, null);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public V remove(final Object key)
    {
        int nr = this.parent.size();
        V result = this.parent.remove(key);
        if (nr != this.parent.size())
        {
            this.fireEvent(OBJECT_REMOVED_EVENT, this.parent.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void putAll(final Map<? extends K, ? extends V> map)
    {
        int nr = this.parent.size();
        this.parent.putAll(map);
        if (nr != this.parent.size())
        {
            this.fireEvent(OBJECT_ADDED_EVENT, this.parent.size());
        }
        else
        {
            if (!map.isEmpty())
            {
                this.fireEvent(OBJECT_CHANGED_EVENT, null);
            }
        }
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
    public Set<K> keySet()
    {
        return this.parent.keySet();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<V> values()
    {
        return this.parent.values();
    }

    /** {@inheritDoc} */
    @Override
    public Set<Map.Entry<K, V>> entrySet()
    {
        return this.parent.entrySet();
    }
}
