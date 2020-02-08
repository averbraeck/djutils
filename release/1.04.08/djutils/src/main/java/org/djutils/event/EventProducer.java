package org.djutils.event;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;

/**
 * The EventProducer forms the reference implementation of the EventProducerInterface. Objects extending this class are provided
 * all the functionalities for registration and event firing. The storage of the listeners is done in a Map with the EventType
 * as the key, and a List of References (weak or strong) to the Listeners. The class uses a helper class EventProducerImpl to do
 * the real work, and avoid code duplication.
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
public abstract class EventProducer implements EventProducerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The EventProducer helper class with the actual implementation to avoid code duplication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final EventProducerImpl eventProducerImpl;

    /**
     * Constructs a new EventProducer and checks for duplicate values in event types.
     */
    public EventProducer()
    {
        this.eventProducerImpl = new EventProducerImpl(this);
    }

    /** {@inheritDoc} */
    @Override
    public abstract Serializable getSourceId(); // without RemoteException

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType)
    {
        return this.eventProducerImpl.addListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final ReferenceType referenceType)
    {
        return this.eventProducerImpl.addListener(listener, eventType, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final int position)
    {
        return this.eventProducerImpl.addListener(listener, eventType, position);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final int position, final ReferenceType referenceType)
    {
        return this.eventProducerImpl.addListener(listener, eventType, position, referenceType);
    }

    /**
     * Transmit an event to all interested listeners.
     * @param event EventInterface; the event
     * @return EventInterface; the event (for method chaining)
     */
    protected synchronized EventInterface fireEvent(final EventInterface event)
    {
        return this.eventProducerImpl.fireEvent(event);
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     * @return Serializable; the payload
     */
    protected Serializable fireEvent(final EventType eventType, final Serializable value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    protected void fireEvent(final EventType eventType)
    {
        this.eventProducerImpl.fireEvent(eventType);
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType EventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @return Serializable; the payload
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    protected <C extends Comparable<C> & Serializable> Serializable fireTimedEvent(final EventType eventType,
            final Serializable value, final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Transmit an event with a one byte payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value byte; the payload
     * @return byte; the payload
     */
    protected byte fireEvent(final EventType eventType, final byte value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit a time-stamped event with a one byte payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value byte; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return byte; the payload
     */
    protected <C extends Comparable<C> & Serializable> byte fireTimedEvent(final EventType eventType, final byte value,
            final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Transmit an event with a boolean payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value boolean; the payload
     * @return boolean; the payload
     */
    protected boolean fireEvent(final EventType eventType, final boolean value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit a time-stamped event with a boolean payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value boolean; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return boolean; the payload
     */
    protected <C extends Comparable<C> & Serializable> boolean fireTimedEvent(final EventType eventType, final boolean value,
            final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Transmit an event with a double value payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value double; the payload
     * @return double; the payload
     */
    protected double fireEvent(final EventType eventType, final double value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit a time-stamped event with a double value payload to interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value double; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return double; the payload
     */
    protected <C extends Comparable<C> & Serializable> double fireTimedEvent(final EventType eventType, final double value,
            final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Transmit an event with an integer payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value int; the payload
     * @return int; the payload
     */
    protected int fireEvent(final EventType eventType, final int value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit a time-stamped event with an integer payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value int; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return int; the payload
     */
    protected <C extends Comparable<C> & Serializable> int fireTimedEvent(final EventType eventType, final int value,
            final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Transmit an event with a long payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value long; the payload
     * @return long; the payload
     */
    protected long fireEvent(final EventType eventType, final long value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit a time-stamped event with a long payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value long; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return long; the payload
     */
    protected <C extends Comparable<C> & Serializable> long fireTimedEvent(final EventType eventType, final long value,
            final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Transmit an event with a short payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value short; the payload
     * @return short; the payload
     */
    protected short fireEvent(final EventType eventType, final short value)
    {
        return this.eventProducerImpl.fireEvent(eventType, value);
    }

    /**
     * Transmit a time-stamped event with a short payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value short; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return short; the payload
     */
    protected <C extends Comparable<C> & Serializable> short fireTimedEvent(final EventType eventType, final short value,
            final C time)
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time);
    }

    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types
     */
    protected synchronized int removeAllListeners()
    {
        return this.eventProducerImpl.removeAllListeners();
    }

    /**
     * Removes all the listeners of a class from this event producer.
     * @param ofClass Class&lt;?&gt;; the class or superclass
     * @return int; the number of removed listeners
     */
    protected synchronized int removeAllListeners(final Class<?> ofClass)
    {
        return this.eventProducerImpl.removeAllListeners(ofClass);

    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean removeListener(final EventListenerInterface listener, final EventType eventType)
    {
        return this.eventProducerImpl.removeListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners()
    {
        return this.eventProducerImpl.hasListeners();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int numberOfListeners(final EventType eventType)
    {
        return this.eventProducerImpl.numberOfListeners(eventType);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized Set<EventType> getEventTypesWithListeners()
    {
        return this.eventProducerImpl.getEventTypesWithListeners();
    }

    /**
     * Return a safe copy of the list of (soft or weak) references to the registered listeners for the provided event type, or
     * an empty list when nothing is registered for this event type. The method never returns a null pointer, so it is safe to
     * use the result directly in an iterator. The references to the listeners are the original references, so not safe copies.
     * @param eventType EventType; the event type to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the list of references to the listeners for this event type,
     *         or an empty list when the event type is not registered
     */
    protected List<Reference<EventListenerInterface>> getListenerReferences(final EventType eventType)
    {
        return this.eventProducerImpl.getListenerReferences(eventType);
    }

}
