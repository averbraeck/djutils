package org.djutils.event;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.djutils.event.reference.Reference;
import org.djutils.event.reference.ReferenceType;
import org.djutils.event.reference.StrongReference;
import org.djutils.event.reference.WeakReference;
import org.djutils.exceptions.Throw;

/**
 * The LocalEventProducer defines the registration and fireEvent operations of an event producer. This behavior includes adding
 * and removing listeners for a specific event type, and firing events for all kinds of different payloads. The EventListener
 * and EventProducer together form a combination of the Publish-Subscribe design pattern and the Observer design pattern using
 * the notify(event) method. See <a href="https://en.wikipedia.org/wiki/Publish-subscribe_pattern" target=
 * "_blank">https://en.wikipedia.org/wiki/Publish-subscribe_pattern</a>,
 * <a href="https://en.wikipedia.org/wiki/Observer_pattern" target="_blank">https://en.wikipedia.org/wiki/Observer_pattern</a>,
 * and <a href="https://howtodoinjava.com/design-patterns/behavioral/observer-design-pattern/" target=
 * "_blank">https://howtodoinjava.com/design-patterns/behavioral/observer-design-pattern/</a>.
 * <p>
 * The EventProducer forms the reference implementation of the publish side of the pub/sub design pattern. The storage of the
 * listeners is done in a Map with the EventType as the key, and a List of References (weak or strong) to the Listeners. Note
 * that the term 'Local' used in the class name is opposed to remote event producers such as the RmiEventProducer.
 * </p>
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
public class LocalEventProducer implements EventProducer, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20200207;

    /** The collection of interested listeners. */
    private EventListenerMap eventListenerMap = new EventListenerMap();

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListener listener, final EventType eventType, final int position,
            final ReferenceType referenceType)
    {
        Throw.whenNull(listener, "listener cannot be null");
        Throw.whenNull(eventType, "eventType cannot be null");
        Throw.whenNull(referenceType, "referenceType cannot be null");
        if (position < LAST_POSITION)
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
        if (this.eventListenerMap.containsKey(eventType))
        {
            for (Reference<EventListener> entry : this.eventListenerMap.get(eventType))
            {
                if (listener.equals(entry.get()))
                {
                    return false;
                }
            }
            List<Reference<EventListener>> entries = this.eventListenerMap.get(eventType);
            if (position == LAST_POSITION)
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
            this.eventListenerMap.put(eventType, entries);
        }
        return true;
    }

    /**
     * Transmit an event to a listener. This method is a hook method. The default implementation simply invokes the notify on
     * the listener. In specific cases (filtering, storing, queueing, this method can be overwritten.
     * @param listener EventListenerInterface; the listener for this event
     * @param event Event; the event to fire
     * @throws RemoteException on network failure
     */
    protected void fireEvent(final EventListener listener, final Event event) throws RemoteException
    {
        listener.notify(event);
    }

    /**
     * Transmit an event to all subscribed listeners.
     * @param event Event; the event
     */
    public synchronized void fireEvent(final Event event)
    {
        Throw.whenNull(event, "event may not be null");
        if (this.eventListenerMap.containsKey(event.getType()))
        {
            // make a safe copy because of possible removeListener() in notify() method during fireEvent
            List<Reference<EventListener>> listenerList = new ArrayList<>(this.eventListenerMap.get(event.getType()));
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
        for (Iterator<Reference<EventListener>> i = this.eventListenerMap.get(eventType).iterator(); i.hasNext();)
        {
            if (i.next().equals(reference))
            {
                i.remove();
                success = true;
            }
        }
        if (this.eventListenerMap.get(eventType).size() == 0)
        {
            this.eventListenerMap.remove(eventType);
        }
        return success;
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
        fireEvent(new Event(eventType, null, true));
    }

    /**
     * Transmit a time-stamped event with a no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event.
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> void fireTimedEvent(final EventType eventType, final C time)

    {
        fireEvent(new TimedEvent<C>(eventType, null, time, true));
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     */
    public void fireEvent(final EventType eventType, final Serializable value)
    {
        fireEvent(new Event(eventType, value, true));
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType EventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> void fireTimedEvent(final EventType eventType, final Serializable value,
            final C time)
    {
        fireEvent(new TimedEvent<C>(eventType, value, time, true));
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event
     */
    public void fireUnverifiedEvent(final EventType eventType)
    {
        fireEvent(new Event(eventType, null, false));
    }

    /**
     * Transmit a time-stamped event with a no payload object to all interested listeners.
     * @param eventType EventType; the eventType of the event.
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> void fireUnverifiedTimedEvent(final EventType eventType, final C time)
    {
        fireEvent(new TimedEvent<C>(eventType, null, time, false));
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventType; the eventType of the event
     * @param value Serializable; the object sent with the event
     */
    public void fireUnverifiedEvent(final EventType eventType, final Serializable value)
    {
        fireEvent(new Event(eventType, value, false));
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType EventType; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     */
    public <C extends Comparable<C> & Serializable> void fireUnverifiedTimedEvent(final EventType eventType,
            final Serializable value, final C time)
    {
        fireEvent(new TimedEvent<C>(eventType, value, time, false));
    }

    /* **************************************************************************************************** */
    /* *********************** (RE) IMPLEMENTATION OF METHODS WITHOUT REMOTEEXCEPTION ********************* */
    /* **************************************************************************************************** */

    /** {@inheritDoc} */
    @Override
    public EventListenerMap getEventListenerMap()
    {
        return this.eventListenerMap;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(final EventListener listener, final EventType eventType)
    {
        try
        {
            return EventProducer.super.addListener(listener, eventType);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(final EventListener listener, final EventType eventType, final ReferenceType referenceType)
    {
        try
        {
            return EventProducer.super.addListener(listener, eventType, referenceType);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(final EventListener listener, final EventType eventType, final int position)
    {
        try
        {
            return EventProducer.super.addListener(listener, eventType, position);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners()
    {
        try
        {
            return EventProducer.super.removeAllListeners();
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners(final Class<?> ofClass)
    {
        try
        {
            return EventProducer.super.removeAllListeners(ofClass);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeListener(final EventListener listener, final EventType eventType)
    {
        try
        {
            return EventProducer.super.removeListener(listener, eventType);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners()
    {
        try
        {
            return EventProducer.super.hasListeners();
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public int numberOfListeners(final EventType eventType)
    {
        try
        {
            return EventProducer.super.numberOfListeners(eventType);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Reference<EventListener>> getListenerReferences(final EventType eventType)
    {
        try
        {
            return EventProducer.super.getListenerReferences(eventType);
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

    /** {@inheritDoc} */
    @Override
    public Set<EventType> getEventTypesWithListeners()
    {
        try
        {
            return EventProducer.super.getEventTypesWithListeners();
        }
        catch (RemoteException exception)
        {
            throw new RuntimeException(exception);

        }
    }

}
