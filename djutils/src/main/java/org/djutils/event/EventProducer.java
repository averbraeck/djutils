package org.djutils.event;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;

/**
 * The EventProducer defines the registration operations of an event producer. This behavior includes adding and removing
 * listeners for a specific event type. The EventListener and EventProducer together form a combination of the Publish-Subscribe
 * design pattern and the Observer design pattern using the notify(event) method. See
 * <a href="https://en.wikipedia.org/wiki/Publish-subscribe_pattern" target=
 * "_blank">https://en.wikipedia.org/wiki/Publish-subscribe_pattern</a>,
 * <a href="https://en.wikipedia.org/wiki/Observer_pattern" target="_blank">https://en.wikipedia.org/wiki/Observer_pattern</a>,
 * and <a href="https://howtodoinjava.com/design-patterns/behavioral/observer-design-pattern/" target=
 * "_blank">https://howtodoinjava.com/design-patterns/behavioral/observer-design-pattern/</a>.
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
 */
public interface EventProducer extends Serializable 
{
    /** The FIRST_POSITION in the queue. */
    int FIRST_POSITION = 0;

    /** The LAST_POSITION in the queue. */
    int LAST_POSITION = -1;

    /**
     * Provide the sourceId that will be transmitted with the fired Event.
     * @return Serializable; the sourceId that will be transmitted with the fired Event
     */
    Serializable getSourceId();

    /**
     * Add a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @return the success of adding the listener. If a listener was already added false is returned
     */
    default boolean addListener(final EventListener listener, final EventType eventType)
    {
        return addListener(listener, eventType, EventProducer.FIRST_POSITION);
    }

    /**
     * Add a listener to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType
     * @param eventType EventType; the events of interest
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference
     * @return the success of adding the listener. If a listener was already added false is returned
     * @see org.djutils.event.ref.WeakReference
     */
    default boolean addListener(final EventListener listener, final EventType eventType, final ReferenceType referenceType)
    {
        return addListener(listener, eventType, EventProducer.FIRST_POSITION, referenceType);
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
     * Add a listener to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; which is interested at certain events
     * @param eventType EventType; the events of interest
     * @param position int; the position of the listener in the queue
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference
     * @return the success of adding the listener. If a listener was already added or an illegal position is provided false is
     *         returned
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListener listener, EventType eventType, int position, ReferenceType referenceType);

    /**
     * Remove the subscription of a listener for a specific event.
     * @param listener EventListenerInterface; which is no longer interested
     * @param eventType EventType; the event which is of no interest any more
     * @return the success of removing the listener. If a listener was not subscribed false is returned
     */
    boolean removeListener(EventListener listener, EventType eventType);

    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types
     */
    int removeAllListeners();

    /**
     * Removes all the listeners of a class from this event producer.
     * @param ofClass Class&lt;?&gt;; the class or superclass
     * @return int; the number of removed listeners
     */
    int removeAllListeners(Class<?> ofClass);
    
    /**
     * Return whether the EventProducer has listeners.
     * @return boolean; whether the EventProducer has listeners or not
     */
    boolean hasListeners();

    /**
     * Return the number of listeners for the provided EventType.
     * @param eventType EventType; the event type to return the number of listeners for
     * @return boolean; whether the EventProducer has listeners or not
     */
    int numberOfListeners(EventType eventType);

    /**
     * Return a safe copy of the list of (strong or weak) references to the registered listeners for the provided event type, or
     * an empty list when nothing is registered for this event type. The method never returns a null pointer, so it is safe to
     * use the result directly in an iterator. The references to the listeners are the original references, so not safe copies.
     * @param eventType EventType; the event type to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the list of references to the listeners for this event type,
     *         or an empty list when the event type is not registered
     */
    List<Reference<EventListener>> getListenerReferences(EventType eventType);

    /**
     * Return the EventTypes for which the EventProducer has listeners.
     * @return Set&lt;EventType&gt;; the EventTypes for which the EventProducer has registered listeners
     */
    Set<EventType> getEventTypesWithListeners();

    /**
     * Transmit an event to all subscribed listeners.
     * @param event Event; the event
     */
    void fireEvent(Event event);

    /**
     * Transmit a time-stamped event to all interested listeners.
     * @param event TimedEvent&lt;C&gt;; the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    default <C extends Comparable<C> & Serializable> void fireTimedEvent(final TimedEvent<C> event)
    {
        fireEvent(event);
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    default void fireEvent(final EventType eventType)
    {
        fireEvent(new Event(eventType, getSourceId(), null, true));
    }

    /**
     * Transmit a time-stamped event with a no payload object to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event.
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    default <C extends Comparable<C> & Serializable> void fireTimedEvent(final TimedEventType eventType, final C time)

    {
        fireEvent(new TimedEvent<C>(eventType, getSourceId(), null, time, true));
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     */
    default void fireEvent(final EventType eventType, final Serializable value)
    {
        fireEvent(new Event(eventType, getSourceId(), value, true));
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    default <C extends Comparable<C> & Serializable> void fireTimedEvent(final TimedEventType eventType,
            final Serializable value, final C time)
    {
        fireEvent(new TimedEvent<C>(eventType, getSourceId(), value, time, true));
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    default void fireUnverifiedEvent(final EventType eventType)
    {
        fireEvent(new Event(eventType, getSourceId(), null, false));
    }

    /**
     * Transmit a time-stamped event with a no payload object to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event.
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    default <C extends Comparable<C> & Serializable> void fireUnverifiedTimedEvent(final TimedEventType eventType, final C time)
    {
        fireEvent(new TimedEvent<C>(eventType, getSourceId(), null, time, false));
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     */
    default void fireUnverifiedEvent(final EventType eventType, final Serializable value)
    {
        fireEvent(new Event(eventType, getSourceId(), value, false));
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    default <C extends Comparable<C> & Serializable> void fireUnverifiedTimedEvent(final TimedEventType eventType,
            final Serializable value, final C time)
    {
        fireEvent(new TimedEvent<C>(eventType, getSourceId(), value, time, false));
    }

}
