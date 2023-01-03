package org.djutils.event;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.djutils.event.reference.Reference;
import org.djutils.event.reference.ReferenceType;
import org.djutils.exceptions.Throw;

/**
 * EventProducer is the interface that exposes a few of the methods of the implementation of an EventProducer to the outside
 * world: the ability to add and remove listeners.
 * <p>
 * Copyright (c) 2022-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface EventProducer extends Serializable, Remote
{
    /** The FIRST_POSITION in the queue. */
    int FIRST_POSITION = 0;

    /** The LAST_POSITION in the queue. */
    int LAST_POSITION = -1;

    /**
     * Add a listener to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; which is interested at certain events
     * @param eventType EventType; the events of interest
     * @param position int; the position of the listener in the queue
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference
     * @return the success of adding the listener. If a listener was already added or an illegal position is provided false is
     *         returned
     * @throws RemoteException on network error
     * @see org.djutils.event.reference.WeakReference
     */
    boolean addListener(EventListener listener, EventType eventType, int position, ReferenceType referenceType)
            throws RemoteException;

    /**
     * Add a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @return the success of adding the listener. If a listener was already added false is returned
     * @throws RemoteException on network error
     */
    default boolean addListener(final EventListener listener, final EventType eventType) throws RemoteException
    {
        return addListener(listener, eventType, FIRST_POSITION);
    }

    /**
     * Add a listener to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference
     * @return the success of adding the listener. If a listener was already added false is returned
     * @throws RemoteException on network error
     * @see org.djutils.event.reference.WeakReference
     */
    default boolean addListener(final EventListener listener, final EventType eventType, final ReferenceType referenceType)
            throws RemoteException
    {
        return addListener(listener, eventType, FIRST_POSITION, referenceType);
    }

    /**
     * Add a listener as strong reference to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @param position int; the position of the listener in the queue
     * @return the success of adding the listener. If a listener was already added, or an illegal position is provided false is
     *         returned
     * @throws RemoteException on network error
     */
    default boolean addListener(final EventListener listener, final EventType eventType, final int position)
            throws RemoteException
    {
        return addListener(listener, eventType, position, ReferenceType.STRONG);
    }

    /**
     * Return the map with the EventListener entries and the reference types.
     * @return EventListenerMap; the map with the EventListener entries and the reference types
     * @throws RemoteException on netowrk error
     */
    EventListenerMap getEventListenerMap() throws RemoteException;
    
    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types for which listeners existed
     * @throws RemoteException on network error
     */
    default int removeAllListeners() throws RemoteException
    {
        int result = getEventListenerMap().size();
        getEventListenerMap().clear();
        return result;
    }

    /**
     * Removes all the listeners of a class from this event producer.
     * @param ofClass Class&lt;?&gt;; the class or superclass
     * @return int; the number of removed listeners
     * @throws RemoteException on network error
     */
    default int removeAllListeners(final Class<?> ofClass) throws RemoteException
    {
        Throw.whenNull(ofClass, "ofClass may not be null");
        int result = 0;
        Map<EventType, Reference<EventListener>> removeMap = new LinkedHashMap<>();
        for (EventType type : getEventListenerMap().keySet())
        {
            for (Iterator<Reference<EventListener>> ii = getEventListenerMap().get(type).iterator(); ii.hasNext();)
            {
                Reference<EventListener> listener = ii.next();
                if (listener.get().getClass().isAssignableFrom(ofClass))
                {
                    removeMap.put(type, listener);
                    result++;
                }
            }
        }
        for (EventType type : removeMap.keySet())
        {
            removeListener(removeMap.get(type).get(), type);
        }
        return result;
    }

    /**
     * Remove the subscription of a listener for a specific event.
     * @param listener EventListenerInterface; which is no longer interested
     * @param eventType EventType; the event which is of no interest any more
     * @return the success of removing the listener. If a listener was not subscribed false is returned
     * @throws RemoteException on network error
     */
    default boolean removeListener(final EventListener listener, final EventType eventType) throws RemoteException
    {
        Throw.whenNull(listener, "listener may not be null");
        Throw.whenNull(eventType, "eventType may not be null");
        EventListenerMap eventListenerMap = getEventListenerMap();
        if (!eventListenerMap.containsKey(eventType))
        {
            return false;
        }
        boolean result = false;
        for (Iterator<Reference<EventListener>> i = eventListenerMap.get(eventType).iterator(); i.hasNext();)
        {
            Reference<EventListener> reference = i.next();
            EventListener entry = reference.get();
            if (entry == null)
            {
                i.remove();
            }
            else
            {
                if (listener.equals(entry))
                {
                    i.remove();
                    result = true;
                }
            }
            if (eventListenerMap.get(eventType).size() == 0)
            {
                eventListenerMap.remove(eventType);
            }
        }
        return result;
    }

    /**
     * Return whether the EventProducer has listeners.
     * @return boolean; whether the EventProducer has listeners or not
     * @throws RemoteException on network error
     */
    default boolean hasListeners() throws RemoteException
    {
        return !getEventListenerMap().isEmpty();
    }

    /**
     * Return the number of listeners for the provided EventType.
     * @param eventType EventType; the event type to return the number of listeners for
     * @return boolean; whether the EventProducer has listeners or not
     * @throws RemoteException on network error
     */
    default int numberOfListeners(final EventType eventType) throws RemoteException
    {
        if (getEventListenerMap().containsKey(eventType))
        {
            return getEventListenerMap().get(eventType).size();
        }
        return 0;
    }

    /**
     * Return a safe copy of the list of (strong or weak) references to the registered listeners for the provided event type, or
     * an empty list when nothing is registered for this event type. The method never returns a null pointer, so it is safe to
     * use the result directly in an iterator. The references to the listeners are the original references, so not safe copies.
     * @param eventType EventType; the event type to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the list of references to the listeners for this event type,
     *         or an empty list when the event type is not registered
     * @throws RemoteException on network error
     */
    default List<Reference<EventListener>> getListenerReferences(final EventType eventType) throws RemoteException
    {
        List<Reference<EventListener>> result = new ArrayList<>();
        if (getEventListenerMap().get(eventType) != null)
        {
            result.addAll(getEventListenerMap().get(eventType));
        }
        return result;
    }

    /**
     * Return the EventTypes for which the EventProducer has listeners.
     * @return Set&lt;EventType&gt;; the EventTypes for which the EventProducer has registered listeners
     * @throws RemoteException on netowrk error
     */
    default Set<EventType> getEventTypesWithListeners() throws RemoteException
    {
        return getEventListenerMap().keySet(); // is already a safe copy
    }

}
