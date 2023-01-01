package org.djutils.event.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.exceptions.Throw;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

/**
 * The Event producing map provides a map to which one can subscribe interest in entry changes. This class does not keep track
 * of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>. A listener
 * must subscribe to the iterator, key set, etc. individually.
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
 * @param <K> the key type
 * @param <V> the value type
 */
public class EventProducingMap<K, V> implements Map<K, V>, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT =
            new EventType("OBJECT_ADDED_EVENT", new MetaData("Size of the map after add", "Size of the map",
                    new ObjectDescriptor("Size of the map after add", "Size of the map", Integer.class)));

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT =
            new EventType("OBJECT_REMOVED_EVENT", new MetaData("Size of the map after remove", "Size of the map",
                    new ObjectDescriptor("Size of the map after remove", "Size of the map", Integer.class)));

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT =
            new EventType("OBJECT_CHANGED_EVENT", new MetaData("Size of the map after change", "Size of the map",
                    new ObjectDescriptor("Size of the map after change", "Size of the map", Integer.class)));

    /** the wrapped map. */
    private final Map<K, V> wrappedMap;

    /** the embedded event producer. */
    private final EventProducer eventProducer;

    /**
     * constructs a new EventProducingMap.
     * @param wrappedMap Map&lt;K,V&gt;; the embedded map.
     */
    public EventProducingMap(final Map<K, V> wrappedMap)
    {
        this(wrappedMap, new EventProducer());
    }

    /**
     * Constructs a new EventProducingMap.
     * @param wrappedMap Map&lt;K, V&gt;; the embedded map.
     * @param eventProducer EventProducer; the EventProducer to send events to the subscribers
     */
    public EventProducingMap(final Map<K, V> wrappedMap, final EventProducer eventProducer)
    {
        Throw.whenNull(wrappedMap, "wrappedMap cannot be null");
        Throw.whenNull(eventProducer, "eventProducer cannot be null");
        this.eventProducer = eventProducer;
        this.wrappedMap = wrappedMap;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.wrappedMap.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.wrappedMap.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsKey(final Object key)
    {
        return this.wrappedMap.containsKey(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(final Object value)
    {
        return this.wrappedMap.containsValue(value);
    }

    /** {@inheritDoc} */
    @Override
    public V get(final Object key)
    {
        return this.wrappedMap.get(key);
    }

    /** {@inheritDoc} */
    @Override
    public V put(final K key, final V value)
    {
        int nr = this.wrappedMap.size();
        V result = this.wrappedMap.put(key, value);
        if (nr != this.wrappedMap.size())
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedMap.size());
        }
        else
        {
            this.eventProducer.fireEvent(OBJECT_CHANGED_EVENT, this.wrappedMap.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public V remove(final Object key)
    {
        int nr = this.wrappedMap.size();
        V result = this.wrappedMap.remove(key);
        if (nr != this.wrappedMap.size())
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedMap.size());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void putAll(final Map<? extends K, ? extends V> map)
    {
        int nr = this.wrappedMap.size();
        this.wrappedMap.putAll(map);
        if (nr != this.wrappedMap.size())
        {
            this.eventProducer.fireEvent(OBJECT_ADDED_EVENT, this.wrappedMap.size());
        }
        else
        {
            if (!map.isEmpty())
            {
                this.eventProducer.fireEvent(OBJECT_CHANGED_EVENT, this.wrappedMap.size());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        int nr = this.wrappedMap.size();
        this.wrappedMap.clear();
        if (nr != this.wrappedMap.size())
        {
            this.eventProducer.fireEvent(OBJECT_REMOVED_EVENT, this.wrappedMap.size());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Set<K> keySet()
    {
        return this.wrappedMap.keySet();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<V> values()
    {
        return this.wrappedMap.values();
    }

    /** {@inheritDoc} */
    @Override
    public Set<Map.Entry<K, V>> entrySet()
    {
        return this.wrappedMap.entrySet();
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
