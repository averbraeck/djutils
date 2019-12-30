package org.djutils.event.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.djutils.event.EventProducer;
import org.djutils.event.EventType;

/**
 * The Event producing map provides a map to which one can subscribe interest in entry changes. This class does not keep track
 * of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>. A listener
 * must subscribe to the iterator, key set, etc. individually.
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
 * @param <K> the key type
 * @param <V> the value type
 */
public class EventProducingMap<K, V> extends EventProducer implements Map<K, V>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20191230L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT = new EventType("OBJECT_ADDED_EVENT");

    /** OBJECT_REMOVED_EVENT is fired on removel of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** OBJECT_CHANGED_EVENT is fired on change of one or more entries. */
    public static final EventType OBJECT_CHANGED_EVENT = new EventType("OBJECT_CHANGED_EVENT");

    /** the parent map. */
    private Map<K, V> parent = null;

    /**
     * constructs a new EventProducingMap.
     * @param parent Map&lt;K,V&gt;; the parent map.
     */
    public EventProducingMap(final Map<K, V> parent)
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
    public boolean containsKey(final Object arg0)
    {
        return this.parent.containsKey(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(final Object arg0)
    {
        return this.parent.containsValue(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public V get(final Object arg0)
    {
        return this.parent.get(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public V put(final K arg0, final V arg1)
    {
        V result = this.parent.put(arg0, arg1);
        this.fireEvent(OBJECT_ADDED_EVENT, null);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public V remove(final Object arg0)
    {
        V result = this.parent.remove(arg0);
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void putAll(final Map<? extends K, ? extends V> arg0)
    {
        this.parent.putAll(arg0);
        this.fireEvent(OBJECT_ADDED_EVENT, null);
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        this.parent.clear();
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
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
