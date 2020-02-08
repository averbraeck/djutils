package org.djutils.event;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;
import org.djutils.event.ref.StrongReference;
import org.djutils.event.ref.WeakReference;
import org.djutils.exceptions.Throw;

/**
 * The EventProducerImpl forms the reference implementation of the EventProducerInterface, and acts as a "helper" class for the
 * different types of EvenProducer implementations. The storage of the listeners is done in a Map with the EventType as the key,
 * and a List of References (weak or strong) to the Listeners.
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
public class EventProducerImpl implements EventProducerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20200207;

    /** The collection of interested listeners. */
    private EventListenerMap listeners = new EventListenerMap();

    /** The embedding event producer that uses this helper class. */
    private final EventProducerInterface embeddingEventProducer;

    /**
     * Construct the helper class to execute the work for registering listeners and firing events.
     * @param embeddingEventProducer EventProducerInterface; the embedding event producer class
     */
    public EventProducerImpl(final EventProducerInterface embeddingEventProducer)
    {
        super();
        this.embeddingEventProducer = embeddingEventProducer;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId() // without RemoteException
    {
        try
        {
            return this.embeddingEventProducer.getSourceId();
        }
        catch (RemoteException rme)
        {
            throw new RuntimeException(rme);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType)
    {
        return this.addListener(listener, eventType, EventProducerInterface.FIRST_POSITION);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final ReferenceType referenceType)
    {
        return this.addListener(listener, eventType, EventProducerInterface.FIRST_POSITION, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final int position)
    {
        return this.addListener(listener, eventType, position, ReferenceType.STRONG);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final int position, final ReferenceType referenceType)
    {
        Throw.whenNull(listener, "listener cannot be null");
        Throw.whenNull(eventType, "eventType cannot be null");
        Throw.whenNull(referenceType, "referenceType cannot be null");
        if (position < EventProducerInterface.LAST_POSITION)
        {
            return false;
        }
        Reference<EventListenerInterface> reference = null;
        if (referenceType.isStrong())
        {
            reference = new StrongReference<EventListenerInterface>(listener);
        }
        else
        {
            reference = new WeakReference<EventListenerInterface>(listener);
        }
        if (this.listeners.containsKey(eventType))
        {
            for (Reference<EventListenerInterface> entry : this.listeners.get(eventType))
            {
                if (listener.equals(entry.get()))
                {
                    return false;
                }
            }
            List<Reference<EventListenerInterface>> entries = this.listeners.get(eventType);
            if (position == EventProducerInterface.LAST_POSITION)
            {
                entries.add(reference);
            }
            else
            {
                entries.add(position, reference);
            }
        }
        else
        {
            List<Reference<EventListenerInterface>> entries = new ArrayList<>();
            entries.add(reference);
            this.listeners.put(eventType, entries);
        }
        return true;
    }

    /**
     * Transmit an event to a listener. This method is a hook method. The default implementation simply invokes the notify on
     * the listener. In specific cases (filtering, storing, queuing, this method can be overwritten.
     * @param listener EventListenerInterface; the listener for this event
     * @param event EventInterface; the event to fire
     * @return EventInterface; the event
     * @throws RemoteException on network failure
     */
    public EventInterface fireEvent(final EventListenerInterface listener, final EventInterface event) throws RemoteException
    {
        listener.notify(event);
        return event;
    }

    /**
     * Transmit an event to all interested listeners.
     * @param event EventInterface; the event
     * @return EventInterface; the event (for method chaining)
     */
    public synchronized EventInterface fireEvent(final EventInterface event)
    {
        Throw.whenNull(event, "event may not be null");
        Throw.whenNull(event.getType(), "event type may not be null");
        if (this.listeners.containsKey(event.getType()))
        {
            // make a safe copy because of possible removeListener() in notify() method during fireEvent
            List<Reference<EventListenerInterface>> listenerList = new ArrayList<>(this.listeners.get(event.getType()));
            for (Reference<EventListenerInterface> reference : listenerList)
            {
                EventListenerInterface listener = reference.get();
                try
                {
                    if (listener != null)
                    {
                        // The garbage collection has not cleaned the referent
                        this.fireEvent(listener, event);
                    }
                    else
                    {
                        // The garbage collection cleaned the referent;
                        // there is no need to keep the subscription
                        this.removeListener(reference, event.getType());
                    }
                }
                catch (RemoteException remoteException)
                {
                    // A network failure prevented the delivery,
                    // subscription is removed.
                    this.removeListener(reference, event.getType());
                }
            }
        }
        return event;
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     * @return Serializable; the payload
     */
    public Serializable fireEvent(final EventType eventType, final Serializable value)
    {
        this.fireEvent(new Event(eventType, getSourceId(), value));
        return value;
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    public void fireEvent(final EventType eventType)
    {
        this.fireEvent(new Event(eventType, getSourceId(), null));
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType EventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @return Serializable; the payload
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> Serializable fireTimedEvent(final EventType eventType,
            final Serializable value, final C time)
    {
        Throw.whenNull(time, "time may not be null");
        this.fireEvent(new TimedEvent<C>(eventType, getSourceId(), value, time));
        return value;
    }

    /**
     * Transmit an event with a one byte payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value byte; the payload
     * @return byte; the payload
     */
    public byte fireEvent(final EventType eventType, final byte value)
    {
        this.fireEvent(eventType, Byte.valueOf(value));
        return value;
    }

    /**
     * Transmit a time-stamped event with a one byte payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value byte; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return byte; the payload
     */
    public <C extends Comparable<C> & Serializable> byte fireTimedEvent(final EventType eventType, final byte value,
            final C time)
    {
        this.fireTimedEvent(eventType, Byte.valueOf(value), time);
        return value;
    }

    /**
     * Transmit an event with a boolean payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value boolean; the payload
     * @return boolean; the payload
     */
    public boolean fireEvent(final EventType eventType, final boolean value)
    {
        this.fireEvent(eventType, Boolean.valueOf(value));
        return value;
    }

    /**
     * Transmit a time-stamped event with a boolean payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value boolean; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return boolean; the payload
     */
    public <C extends Comparable<C> & Serializable> boolean fireTimedEvent(final EventType eventType, final boolean value,
            final C time)
    {
        fireTimedEvent(eventType, Boolean.valueOf(value), time);
        return value;
    }

    /**
     * Transmit an event with a double value payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value double; the payload
     * @return double; the payload
     */
    public double fireEvent(final EventType eventType, final double value)
    {
        this.fireEvent(eventType, Double.valueOf(value));
        return value;
    }

    /**
     * Transmit a time-stamped event with a double value payload to interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value double; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return double; the payload
     */
    public <C extends Comparable<C> & Serializable> double fireTimedEvent(final EventType eventType, final double value,
            final C time)
    {
        this.fireTimedEvent(eventType, Double.valueOf(value), time);
        return value;
    }

    /**
     * Transmit an event with an integer payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value int; the payload
     * @return int; the payload
     */
    public int fireEvent(final EventType eventType, final int value)
    {
        this.fireEvent(eventType, Integer.valueOf(value));
        return value;
    }

    /**
     * Transmit a time-stamped event with an integer payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value int; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return int; the payload
     */
    public <C extends Comparable<C> & Serializable> int fireTimedEvent(final EventType eventType, final int value, final C time)
    {
        this.fireTimedEvent(eventType, Integer.valueOf(value), time);
        return value;
    }

    /**
     * Transmit an event with a long payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value long; the payload
     * @return long; the payload
     */
    public long fireEvent(final EventType eventType, final long value)
    {
        this.fireEvent(eventType, Long.valueOf(value));
        return value;
    }

    /**
     * Transmit a time-stamped event with a long payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value long; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return long; the payload
     */
    public <C extends Comparable<C> & Serializable> long fireTimedEvent(final EventType eventType, final long value,
            final C time)
    {
        this.fireTimedEvent(eventType, Long.valueOf(value), time);
        return value;
    }

    /**
     * Transmit an event with a short payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value short; the payload
     * @return short; the payload
     */
    public short fireEvent(final EventType eventType, final short value)
    {
        this.fireEvent(eventType, Short.valueOf(value));
        return value;
    }

    /**
     * Transmit a time-stamped event with a short payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value short; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return short; the payload
     */
    public <C extends Comparable<C> & Serializable> short fireTimedEvent(final EventType eventType, final short value,
            final C time)
    {
        this.fireTimedEvent(eventType, Short.valueOf(value), time);
        return value;
    }

    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types
     */
    public synchronized int removeAllListeners()
    {
        int result = this.listeners.size();
        this.listeners = null;
        this.listeners = new EventListenerMap();
        return result;
    }

    /**
     * Removes all the listeners of a class from this event producer.
     * @param ofClass Class&lt;?&gt;; the class or superclass
     * @return int; the number of removed listeners
     */
    public synchronized int removeAllListeners(final Class<?> ofClass)
    {
        Throw.whenNull(ofClass, "ofClass may not be null");
        int result = 0;
        Map<EventType, Reference<EventListenerInterface>> removeMap = new LinkedHashMap<>();
        for (EventType type : this.listeners.keySet())
        {
            for (Iterator<Reference<EventListenerInterface>> ii = this.listeners.get(type).iterator(); ii.hasNext();)
            {
                Reference<EventListenerInterface> listener = ii.next();
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

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean removeListener(final EventListenerInterface listener, final EventType eventType)
    {
        Throw.whenNull(listener, "listener may not be null");
        Throw.whenNull(eventType, "eventType may not be null");
        if (!this.listeners.containsKey(eventType))
        {
            return false;
        }
        boolean result = false;
        for (Iterator<Reference<EventListenerInterface>> i = this.listeners.get(eventType).iterator(); i.hasNext();)
        {
            Reference<EventListenerInterface> reference = i.next();
            EventListenerInterface entry = reference.get();
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
            if (this.listeners.get(eventType).size() == 0)
            {
                this.listeners.remove(eventType);
            }
        }
        return result;
    }

    /**
     * Remove one reference from the subscription list.
     * @param reference Reference&lt;EventListenerInterface&gt;; the (strong or weak) reference to remove
     * @param eventType EventType; the eventType for which reference must be removed
     * @return boolean; true if the reference was removed; otherwise false
     */
    private synchronized boolean removeListener(final Reference<EventListenerInterface> reference, final EventType eventType)
    {
        Throw.whenNull(reference, "reference may not be null");
        Throw.whenNull(eventType, "eventType may not be null");
        boolean success = false;
        for (Iterator<Reference<EventListenerInterface>> i = this.listeners.get(eventType).iterator(); i.hasNext();)
        {
            if (i.next().equals(reference))
            {
                i.remove();
                success = true;
            }
        }
        if (this.listeners.get(eventType).size() == 0)
        {
            this.listeners.remove(eventType);
        }
        return success;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners()
    {
        return !this.listeners.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int numberOfListeners(final EventType eventType)
    {
        if (this.listeners.containsKey(eventType))
        {
            return this.listeners.get(eventType).size();
        }
        return 0;
    }

    /**
     * Return a safe copy of the list of (soft or weak) references to the registered listeners for the provided event type, or
     * an empty list when nothing is registered for this event type. The method never returns a null pointer, so it is safe to
     * use the result directly in an iterator. The references to the listeners are the original references, so not safe copies.
     * @param eventType EventType; the event type to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the list of references to the listeners for this event type,
     *         or an empty list when the event type is not registered
     */
    public List<Reference<EventListenerInterface>> getListenerReferences(final EventType eventType)
    {
        List<Reference<EventListenerInterface>> result = new ArrayList<>();
        if (this.listeners.get(eventType) != null)
        {
            result.addAll(this.listeners.get(eventType));
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized Set<EventType> getEventTypesWithListeners()
    {
        return this.listeners.keySet(); // is already a safe copy
    }

}
