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
 * The EventProducer forms the reference implementation of the publish side of the pub/sub design pattern. The storage of the
 * listeners is done in a Map with the EventType as the key, and a List of References (weak or strong) to the Listeners.
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
public class LocalEventProducer implements EventProducerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20200207;

    /** The collection of interested listeners. */
    private EventListenerMap listeners = new EventListenerMap();

    /** The source id for identifying the sender of the event. */
    private final Serializable sourceId;
    
    /**
     * Construct a new EventProducer.
     * @param sourceId Serializable; the sourceId of the event producer to identify the event publisher
     * 
     */
    public LocalEventProducer(final Serializable sourceId)
    {
        this.sourceId = sourceId;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.sourceId;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListener listener, final EventType eventType)
    {
        return this.addListener(listener, eventType, EventProducerInterface.FIRST_POSITION);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListener listener, final EventType eventType,
            final ReferenceType referenceType)
    {
        return this.addListener(listener, eventType, EventProducerInterface.FIRST_POSITION, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListener listener, final EventType eventType, final int position)
    {
        return this.addListener(listener, eventType, position, ReferenceType.STRONG);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListener listener, final EventType eventType, final int position,
            final ReferenceType referenceType)
    {
        Throw.whenNull(listener, "listener cannot be null");
        Throw.whenNull(eventType, "eventType cannot be null");
        Throw.whenNull(referenceType, "referenceType cannot be null");
        if (position < EventProducerInterface.LAST_POSITION)
        {
            return false;
        }
        Reference<EventListener> reference = null;
        if (referenceType.isStrong())
        {
            reference = new StrongReference<EventListener>(listener);
        }
        else
        {
            reference = new WeakReference<EventListener>(listener);
        }
        if (this.listeners.containsKey(eventType))
        {
            for (Reference<EventListener> entry : this.listeners.get(eventType))
            {
                if (listener.equals(entry.get()))
                {
                    return false;
                }
            }
            List<Reference<EventListener>> entries = this.listeners.get(eventType);
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
            List<Reference<EventListener>> entries = new ArrayList<>();
            entries.add(reference);
            this.listeners.put(eventType, entries);
        }
        return true;
    }

    /**
     * Transmit an event to a listener. This method is a hook method. The default implementation simply invokes the notify on
     * the listener. In specific cases (filtering, storing, queuing, this method can be overwritten.
     * @param listener EventListenerInterface; the listener for this event
     * @param event Event; the event to fire
     * @throws RemoteException on network failure
     */
    public void fireEvent(final EventListener listener, final Event event) throws RemoteException
    {
        listener.notify(event);
    }

    /**
     * Transmit an event to all interested listeners.
     * @param event Event; the event
     */
    public synchronized void fireEvent(final Event event)
    {
        Throw.whenNull(event, "event may not be null");
        Throw.whenNull(event.getType(), "event type may not be null");
        if (this.listeners.containsKey(event.getType()))
        {
            // make a safe copy because of possible removeListener() in notify() method during fireEvent
            List<Reference<EventListener>> listenerList = new ArrayList<>(this.listeners.get(event.getType()));
            for (Reference<EventListener> reference : listenerList)
            {
                EventListener listener = reference.get();
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
    }

    /**
     * Transmit a time-stamped event to all interested listeners.
     * @param event TimedEvent&lt;C&gt;; the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> void fireTimedEvent(final TimedEvent<C> event)
    {
        fireEvent(event);
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    public void fireEvent(final EventType eventType)
    {
        this.fireEvent(new Event(eventType, getSourceId(), null, true));
    }

    /**
     * Transmit a time-stamped event with a no payload object to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event.
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> void fireTimedEvent(final TimedEventType eventType, final C time)
    {
        this.fireEvent(new TimedEvent<C>(eventType, getSourceId(), null, time, true));
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     * @return Serializable; the payload
     */
    public Serializable fireEvent(final EventType eventType, final Serializable value)
    {
        this.fireEvent(new Event(eventType, getSourceId(), value, true));
        return value;
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @return Serializable; the payload
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> Serializable fireTimedEvent(final TimedEventType eventType,
            final Serializable value, final C time)
    {
        this.fireEvent(new TimedEvent<C>(eventType, getSourceId(), value, time, true));
        return value;
    }

    /**
     * Transmit an event with a one byte payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value byte; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return byte; the payload
     */
    public byte fireEvent(final EventType eventType, final byte value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Byte.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a one byte payload to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value byte; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return byte; the payload
     */
    public <C extends Comparable<C> & Serializable> byte fireTimedEvent(final TimedEventType eventType, final byte value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Byte.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with a one char payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value char; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return char; the payload
     */
    public char fireEvent(final EventType eventType, final char value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Character.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a one char payload to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value char; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return char; the payload
     */
    public <C extends Comparable<C> & Serializable> char fireTimedEvent(final TimedEventType eventType, final char value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Character.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with a boolean payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value boolean; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return boolean; the payload
     */
    public boolean fireEvent(final EventType eventType, final boolean value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Boolean.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a boolean payload to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value boolean; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return boolean; the payload
     */
    public <C extends Comparable<C> & Serializable> boolean fireTimedEvent(final TimedEventType eventType, final boolean value,
            final C time, final boolean verifyMetaData)
    {
        fireTimedEvent(eventType, Boolean.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with a double value payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value double; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return double; the payload
     */
    public double fireEvent(final EventType eventType, final double value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Double.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a double value payload to interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value double; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return double; the payload
     */
    public <C extends Comparable<C> & Serializable> double fireTimedEvent(final TimedEventType eventType, final double value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Double.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with an integer payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value int; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return int; the payload
     */
    public int fireEvent(final EventType eventType, final int value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Integer.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with an integer payload to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value int; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return int; the payload
     */
    public <C extends Comparable<C> & Serializable> int fireTimedEvent(final TimedEventType eventType, final int value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Integer.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with a long payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value long; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return long; the payload
     */
    public long fireEvent(final EventType eventType, final long value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Long.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a long payload to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value long; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return long; the payload
     */
    public <C extends Comparable<C> & Serializable> long fireTimedEvent(final TimedEventType eventType, final long value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Long.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with a short payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value short; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return short; the payload
     */
    public short fireEvent(final EventType eventType, final short value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Short.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a short payload to all interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value short; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return short; the payload
     */
    public <C extends Comparable<C> & Serializable> short fireTimedEvent(final TimedEventType eventType, final short value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Short.valueOf(value), time, verifyMetaData);
        return value;
    }

    /**
     * Transmit an event with a float value payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value float; the payload
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @return float; the payload
     */
    public float fireEvent(final EventType eventType, final float value, final boolean verifyMetaData)
    {
        this.fireEvent(eventType, Float.valueOf(value), verifyMetaData);
        return value;
    }

    /**
     * Transmit a time-stamped event with a float value payload to interested listeners.
     * @param eventType TimedEventType; the eventType of the event
     * @param value float; the payload
     * @param time C; a time stamp for the event
     * @param verifyMetaData boolean; whether to verify the compliance with metadata or not
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return float; the payload
     */
    public <C extends Comparable<C> & Serializable> float fireTimedEvent(final TimedEventType eventType, final float value,
            final C time, final boolean verifyMetaData)
    {
        this.fireTimedEvent(eventType, Float.valueOf(value), time, verifyMetaData);
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
        Map<EventType, Reference<EventListener>> removeMap = new LinkedHashMap<>();
        for (EventType type : this.listeners.keySet())
        {
            for (Iterator<Reference<EventListener>> ii = this.listeners.get(type).iterator(); ii.hasNext();)
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

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean removeListener(final EventListener listener, final EventType eventType)
    {
        Throw.whenNull(listener, "listener may not be null");
        Throw.whenNull(eventType, "eventType may not be null");
        if (!this.listeners.containsKey(eventType))
        {
            return false;
        }
        boolean result = false;
        for (Iterator<Reference<EventListener>> i = this.listeners.get(eventType).iterator(); i.hasNext();)
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
    private synchronized boolean removeListener(final Reference<EventListener> reference, final EventType eventType)
    {
        Throw.whenNull(reference, "reference may not be null");
        Throw.whenNull(eventType, "eventType may not be null");
        boolean success = false;
        for (Iterator<Reference<EventListener>> i = this.listeners.get(eventType).iterator(); i.hasNext();)
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
     * Return a safe copy of the list of (strong or weak) references to the registered listeners for the provided event type, or
     * an empty list when nothing is registered for this event type. The method never returns a null pointer, so it is safe to
     * use the result directly in an iterator. The references to the listeners are the original references, so not safe copies.
     * @param eventType EventType; the event type to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the list of references to the listeners for this event type,
     *         or an empty list when the event type is not registered
     */
    public List<Reference<EventListener>> getListenerReferences(final EventType eventType)
    {
        List<Reference<EventListener>> result = new ArrayList<>();
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
