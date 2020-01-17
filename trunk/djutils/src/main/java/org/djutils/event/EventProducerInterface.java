package org.djutils.event;

import java.rmi.RemoteException;
import java.util.Set;

import org.djutils.event.ref.ReferenceType;

/**
 * The EventProducerInterface defines the registration operations of an event producer. This behavior includes adding and
 * removing listeners for a specific event type. The EventListener and EventProducer together form a combination of the
 * Publish-Subscribe design pattern and the Observer design pattern using the notify(event) method. See
 * <a href="https://en.wikipedia.org/wiki/Publish-subscribe_pattern" target=
 * "_blank">https://en.wikipedia.org/wiki/Publish-subscribe_pattern</a>,
 * <a href="https://en.wikipedia.org/wiki/Observer_pattern" target="_blank">https://en.wikipedia.org/wiki/Observer_pattern</a>,
 * and <a href="https://howtodoinjava.com/design-patterns/behavioral/observer-design-pattern/" target=
 * "_blank">https://howtodoinjava.com/design-patterns/behavioral/observer-design-pattern/</a>.
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
public interface EventProducerInterface
{
    /** The FIRST_POSITION in the queue. */
    int FIRST_POSITION = 0;

    /** The LAST_POSITION in the queue. */
    int LAST_POSITION = -1;

    /**
     * adds a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType.
     * @param eventType EventType; the events of interest.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType) throws RemoteException;

    /**
     * adds a listener to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType.
     * @param eventType EventType; the events of interest.
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, ReferenceType referenceType)
            throws RemoteException;

    /**
     * adds a listener as strong reference to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType.
     * @param eventType EventType; the events of interest.
     * @param position int; the position of the listener in the queue.
     * @return the success of adding the listener. If a listener was already added, or an illegal position is provided false is
     *         returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, int position) throws RemoteException;

    /**
     * adds a listener to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; which is interested at certain events,
     * @param eventType EventType; the events of interest.
     * @param position int; the position of the listener in the queue
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference.
     * @return the success of adding the listener. If a listener was already added or an illegal position is provided false is
     *         returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, int position, ReferenceType referenceType)
            throws RemoteException;

    /**
     * removes the subscription of a listener for a specific event.
     * @param listener EventListenerInterface; which is no longer interested.
     * @param eventType EventType; the event which is of no interest any more.
     * @return the success of removing the listener. If a listener was not subscribed false is returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean removeListener(EventListenerInterface listener, EventType eventType) throws RemoteException;
    
    /**
     * Return whether the EventProducer has listeners.
     * @return boolean; whether the EventProducer has listeners or not
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean hasListeners() throws RemoteException;
    
    /**
     * Return the number of listeners for the provided EventType.
     * @param eventType EventType; the event type to return the number of listeners for
     * @return boolean; whether the EventProducer has listeners or not
     * @throws RemoteException If a network connection failure occurs.
     */
    int numberOfListeners(EventType eventType) throws RemoteException;

    /**
     * Return the EventTypes for which the EventProducer has listeners.
     * @return Set&lt;EventType&gt;; the EventTypes for which the EventProducer has registered listeners 
     * @throws RemoteException If a network connection failure occurs.
     */
    Set<EventType> getEventTypesWithListeners() throws RemoteException;

}
