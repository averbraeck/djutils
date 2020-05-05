package org.djutils.event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.djutils.event.ref.Reference;
import org.djutils.event.remote.RemoteEventListenerInterface;
import org.djutils.exceptions.Throw;

/**
 * The EventListenerMap maps EventTypes on lists of References to EventListeners. The References can be Weak or Strong. The Map
 * can be serialized. When serializing, the References to RemoteEventListeners are not written as they are fully dependent on a
 * volatile network state that will almost certainly not be the same when the serialized map is read back.
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
 */
public final class EventListenerMap implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The hasMap we map on. */
    private Map<EventTypeInterface, List<Reference<EventListenerInterface>>> map =
            Collections.synchronizedMap(new LinkedHashMap<>());

    /**
     * Return the size of the EventListenerMap, i.e. the number of EventTypes that are registered.
     * @return int; the size of the EventListenerMap, i.e. the number of EventTypes that are registered
     */
    public int size()
    {
        return this.map.size();
    }

    /**
     * Clears the EventListenerMap.
     */
    public void clear()
    {
        this.map.clear();
    }

    /**
     * Return whether the EventListenerMap is empty.
     * @return boolean; whether the EventListenerMap is empty
     */
    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    /**
     * Return whether the EventListenerMap contains the EventType as a key.
     * @param eventType EventType; the EventType key to search for
     * @return boolean; whether the EventListenerMap contains the EventType as a key
     */
    public boolean containsKey(final EventTypeInterface eventType)
    {
        Throw.whenNull(eventType, "Cannot search for a null EventType");
        return this.map.containsKey(eventType);
    }

    /**
     * Return whether the EventListenerMap contains the eventListener as one of the subscribers.
     * @param eventListener EventListenerInterface; the EventListener value to search for
     * @return boolean; true if the EventListenerMap contains the eventListener as one of the subscribers; false otherwise
     */
    public boolean containsValue(final EventListenerInterface eventListener)
    {
        Throw.whenNull(eventListener, "Cannot search for a null EventListener");
        for (List<Reference<EventListenerInterface>> refList : this.map.values())
        {
            for (Reference<EventListenerInterface> ref : refList)
            {
                if (eventListener.equals(ref.get()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the EventListenerMap contains the reference to the eventListener as one of the subscribers.
     * @param reference EventListenerInterface; the reference pointer an EventListener to search for
     * @return boolean; true if the EventListenerMap contains the reference to the eventListener as one of the subscribers;
     *         false otherwise
     */
    public boolean containsValue(final Reference<EventListenerInterface> reference)
    {
        Throw.whenNull(reference, "Cannot search for a null reference");
        for (List<Reference<EventListenerInterface>> refList : this.map.values())
        {
            for (Reference<EventListenerInterface> ref : refList)
            {
                if (reference.equals(ref))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a safe copy of the collection of lists of references to EventListeners, i.e. all the listeners registered in the
     * map
     * @return Collection&lt;List&lt;Reference&lt;EventListenerInterface&gt;&gt;&gt;; a safe copy of the collection of lists of
     *         references to EventListeners, i.e. all the listeners registered in the map
     */
    public Collection<List<Reference<EventListenerInterface>>> values()
    {
        Collection<List<Reference<EventListenerInterface>>> result = new LinkedHashSet<>();
        for (List<Reference<EventListenerInterface>> list : this.map.values())
        {
            result.add(new ArrayList<>(list));
        }
        return result;
    }

    /**
     * Add all entries of the map to the EventListenerMap. The lists of listeners are added as a safe copy, so the list will not
     * be changed when the entries from copied map will be changed.
     * @param m EventListenerMap; the map with references to event listeners to add to the current EventListenerMap
     */
    public void putAll(final EventListenerMap m)
    {
        Throw.whenNull(m, "Cannot use putAll for a null map");
        for (Map.Entry<EventTypeInterface, List<Reference<EventListenerInterface>>> entry : m.entrySet())
        {
            put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    /**
     * Returns the Set of Entry types holding pairs of a key (EventTypeInterface) and a value (List of references to
     * EventListeners for that EventTypeInterface). Note: this is a map with the real values, so not a safe copy. This entrySet
     * can be used to change the underlying map.
     * @return Set&lt;Map.Entry&lt;EventTypeInterface, List&lt;Reference&lt;EventListenerInterface&gt;&gt;&gt;&gt;;the Set of
     *         Entry types holding pairs of a key (EventTypeInterface) and a value (List of references to EventListeners for that
     *         EventTypeInterface). Note: this is <b>not</b> a safe copy!
     */
    public Set<Map.Entry<EventTypeInterface, List<Reference<EventListenerInterface>>>> entrySet()
    {
        return this.map.entrySet();
    }

    /**
     * Returns a safe copy of the Set of EventTypeInterfaces for which listeners are registered.
     * @return Set&lt;EventTypeInterface&gt;; a safe copy of the Set of EventTypeInterface keys for which listeners are registered
     */
    public Set<EventTypeInterface> keySet()
    {
        return new LinkedHashSet<EventTypeInterface>(this.map.keySet());
    }

    /**
     * Returns the original List of references to EventListeners for the given EventTypeInterface. Note: this is <b>not</b> a
     * safe copy, so the list is backed by the original data structure and will change when listeners are added or removed. The
     * method will return null when the EventTypeInterface is not found.
     * @param key EventTypeInterface; the eventType to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;; the List of references to EventListeners for the given
     *         EventTypeInterface, or null when the EventTypeInterface is not found. Note: this is <b>not</b> a safe copy.
     */
    public List<Reference<EventListenerInterface>> get(final EventTypeInterface key)
    {
        Throw.whenNull(key, "Cannot use get for a null EventType key");
        return this.map.get(key);
    }

    /**
     * Remove the List of references to EventListeners for the given EventTypeInterface.
     * @param key EventTypeInterface; the eventType to remove the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the removed List of references to EventListeners for the
     *         given EventTypeInterface
     */
    public List<Reference<EventListenerInterface>> remove(final EventTypeInterface key)
    {
        Throw.whenNull(key, "Cannot use remove for a null EventTypeInterface key");
        return this.map.remove(key);
    }

    /**
     * Add the List of references to EventListeners for the given EventTypeInterface to the underlying Map. A safe copy will be
     * added, so the original list will not be affected when listeners are removed or added, nor will the underlying map be
     * affected when the provided list is changed.
     * @param key EventTypeInterface; the eventType to store the listeners for
     * @param value List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the references to EventListeners to store for the given
     *            EventTypeInterface
     * @return List&lt;Reference&lt;EventListenerInterface&gt;; the previous List of references to EventListeners for the given
     *         EventTypeInterface, or null when there was no previous mapping
     */
    public List<Reference<EventListenerInterface>> put(final EventTypeInterface key,
            final List<Reference<EventListenerInterface>> value)
    {
        Throw.whenNull(key, "Cannot use put with a null EventType key");
        Throw.whenNull(value, "Cannot use put with a null List as value");
        return this.map.put(key, new ArrayList<>(value));
    }

    /**
     * Write the EventListenerMap to a stream. RemoteEventListeners are not written, as they are fully dependent on the state of
     * the network, which might not be the same when the EventListenerMap is read back. Weak references and strong references
     * are both written to the stream.
     * @param out ObjectOutputStream; the output stream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        Map<EventTypeInterface, List<Reference<EventListenerInterface>>> outMap = new LinkedHashMap<>();
        for (Map.Entry<EventTypeInterface, List<Reference<EventListenerInterface>>> entry : this.map.entrySet())
        {
            outMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        for (Entry<EventTypeInterface, List<Reference<EventListenerInterface>>> entry : this.map.entrySet())
        {
            for (Reference<EventListenerInterface> reference : entry.getValue())
            {
                if (reference.get() instanceof RemoteEventListenerInterface)
                {
                    outMap.get(entry.getKey()).remove(reference);
                }
            }
            if (outMap.get(entry.getKey()).isEmpty())
            {
                outMap.remove(entry.getKey());
            }
        }
        out.writeObject(outMap);
    }

    /**
     * Read an EventListenerMap from a stream and use it to replace the internal map.
     * @param in java.io.ObjectInputStream; the input stream
     * @throws IOException on IOException
     * @throws ClassNotFoundException on ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.map = (LinkedHashMap<EventTypeInterface, List<Reference<EventListenerInterface>>>) in.readObject();
    }

}
