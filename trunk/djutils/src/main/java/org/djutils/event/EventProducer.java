package org.djutils.event;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
import org.djutils.logger.CategoryLogger;

/**
 * The EventProducer forms the reference implementation of the EventProducerInterface. Objects extending this class are provided
 * all the functionalities for registration and event firing. The storage of the listeners is done in a Map with the EventType
 * as the key, and a List of References (weak or strong) to the Listeners.
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
 */
public abstract class EventProducer implements EventProducerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The collection of interested listeners. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected EventListenerMap listeners = new EventListenerMap();

    /** Cache to prevent continuous reflection. */
    private static final transient Map<Class<? extends EventProducer>, EventType[]> EVENTTYPE_CACHE = new LinkedHashMap<>();

    /**
     * Checks that no duplicate short values are assigned to the producer. An event producer produces events of a certain
     * eventType. This eventType functions as a marker for registration. If the eventProducer defines two non-private, static
     * EventTypes with an equal value, the marker function is lost. This method checks for this particular problem. Note that
     * this will also return true when a superclass defines the same non-private, static EventType as a subclass.
     * @return boolean; true if every non-private, static EventType in this class is unique; false if a duplicate exists.
     */
    private boolean checkNoDuplicateEventTypes()
    {
        EventType[] events = getStaticEventTypes(getClass());
        for (int i = 0; i < events.length; i++)
        {
            for (int j = i + 1; j < events.length; j++) // assumes symmetric "equals"
            {
                if (events[i].equals(events[j]))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Constructs a new EventProducer and checks for duplicate values in event types.
     */
    public EventProducer()
    {
        if (!this.checkNoDuplicateEventTypes())
        {
            throw new RuntimeException("EventProducer failed: " + "more events have the same class + name combination");
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
    protected EventInterface fireEvent(final EventListenerInterface listener, final EventInterface event) throws RemoteException
    {
        listener.notify(event);
        return event;
    }

    /**
     * Transmit an event to all interested listeners.
     * @param event EventInterface; the event
     * @return the event
     */
    protected synchronized EventInterface fireEvent(final EventInterface event)
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
    protected Serializable fireEvent(final EventType eventType, final Serializable value)
    {
        this.fireEvent(new Event(eventType, this, value));
        return value;
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    protected void fireEvent(final EventType eventType)
    {
        this.fireEvent(new Event(eventType, this, null));
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
        Throw.whenNull(time, "time may not be null");
        this.fireEvent(new TimedEvent<C>(eventType, this, value, time));
        return value;
    }

    /**
     * Transmit an event with a one byte payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value byte; the payload
     * @return byte; the payload
     */
    protected byte fireEvent(final EventType eventType, final byte value)
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
    protected <C extends Comparable<C> & Serializable> byte fireTimedEvent(final EventType eventType, final byte value,
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
    protected boolean fireEvent(final EventType eventType, final boolean value)
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
    protected <C extends Comparable<C> & Serializable> boolean fireTimedEvent(final EventType eventType, final boolean value,
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
    protected double fireEvent(final EventType eventType, final double value)
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
    protected <C extends Comparable<C> & Serializable> double fireTimedEvent(final EventType eventType, final double value,
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
    protected int fireEvent(final EventType eventType, final int value)
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
    protected <C extends Comparable<C> & Serializable> int fireTimedEvent(final EventType eventType, final int value,
            final C time)
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
    protected long fireEvent(final EventType eventType, final long value)
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
    protected <C extends Comparable<C> & Serializable> long fireTimedEvent(final EventType eventType, final long value,
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
    protected short fireEvent(final EventType eventType, final short value)
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
    protected <C extends Comparable<C> & Serializable> short fireTimedEvent(final EventType eventType, final short value,
            final C time)
    {
        this.fireTimedEvent(eventType, Short.valueOf(value), time);
        return value;
    }

    /**
     * Return the non-private, static EventType fields for the provided class, and add them to the EventProducer cache.
     * @param checkClass Class&lt;? extends EventProducer&gt;; the class to return the non-private, static EventTypes for
     * @return EventType[]; the non-private, static event types of this EventProducer
     */
    private static EventType[] getStaticEventTypes(final Class<? extends EventProducer> checkClass)
    {
        Throw.whenNull(checkClass, "checkClass may not be null");
        if (EVENTTYPE_CACHE.containsKey(checkClass))
        {
            return EVENTTYPE_CACHE.get(checkClass);
        }
        List<Field> fieldList = new ArrayList<Field>();
        Class<?> clazz = checkClass;
        while (clazz != null)
        {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++)
            {
                fieldList.add(declaredFields[i]);
            }
            clazz = clazz.getSuperclass();
        }
        List<EventType> eventTypeList = new ArrayList<>();
        for (Field field : fieldList)
        {
            if (field.getType().equals(EventType.class) && !Modifier.isPrivate(field.getModifiers())
                    && Modifier.isStatic(field.getModifiers()))
            {
                try
                {
                    eventTypeList.add((EventType) field.get(null)); // only static fields
                }
                catch (Exception exception)
                {
                    // should not happen
                    CategoryLogger.always().error(exception);
                }
            }
        }
        EVENTTYPE_CACHE.put(checkClass, eventTypeList.toArray(new EventType[eventTypeList.size()]));
        return EVENTTYPE_CACHE.get(checkClass);
    }

    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types
     */
    protected synchronized int removeAllListeners()
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
    protected synchronized int removeAllListeners(final Class<?> ofClass)
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

    /** {@inheritDoc} */
    @Override
    public synchronized Set<EventType> getEventTypesWithListeners()
    {
        return this.listeners.keySet(); // is already a safe copy
    }

}
