package org.djutils.event;

import org.djutils.event.reference.ReferenceType;

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
public interface EventProducer
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
     * @see org.djutils.event.reference.WeakReference
     */
    boolean addListener(EventListener listener, EventType eventType, int position, ReferenceType referenceType);

    /**
     * Add a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @return the success of adding the listener. If a listener was already added false is returned
     */
    default boolean addListener(final EventListener listener, final EventType eventType)
    {
        return addListener(listener, eventType, LocalEventProducer.FIRST_POSITION);
    }

    /**
     * Add a listener to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference
     * @return the success of adding the listener. If a listener was already added false is returned
     * @see org.djutils.event.reference.WeakReference
     */
    default boolean addListener(final EventListener listener, final EventType eventType, final ReferenceType referenceType)
    {
        return addListener(listener, eventType, LocalEventProducer.FIRST_POSITION, referenceType);
    }

    /**
     * Add a listener as strong reference to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @param position int; the position of the listener in the queue
     * @return the success of adding the listener. If a listener was already added, or an illegal position is provided false is
     *         returned
     */
    default boolean addListener(final EventListener listener, final EventType eventType, final int position)
    {
        return addListener(listener, eventType, position, ReferenceType.STRONG);
    }

    /**
     * Remove the subscription of a listener for a specific event.
     * @param listener EventListenerInterface; which is no longer interested
     * @param eventType EventType; the event which is of no interest any more
     * @return the success of removing the listener. If a listener was not subscribed false is returned
     */
    boolean removeListener(EventListener listener, EventType eventType);

    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types for which listeners existed
     */
    int removeAllListeners();

}
