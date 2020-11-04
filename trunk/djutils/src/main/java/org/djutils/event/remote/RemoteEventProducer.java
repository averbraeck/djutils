package org.djutils.event.remote;

import java.io.Serializable;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducerImpl;
import org.djutils.event.EventTypeInterface;
import org.djutils.event.TimedEventInterface;
import org.djutils.event.TimedEventTypeInterface;
import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;
import org.djutils.rmi.RMIObject;

/**
 * The RemoteEventProducer provides a remote implementation of the eventProducer.
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
public abstract class RemoteEventProducer extends RMIObject implements RemoteEventProducerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The EventProducer helper class with the actual implementation to avoid code duplication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final EventProducerImpl eventProducerImpl;

    /**
     * Create a remote event listener and register the listener in the RMI registry. When the RMI registry does not exist yet,
     * it will be created, but <b>only</b> on the local host. Remote creation of a registry on another computer is not possible.
     * Any attempt to do so will cause an AccessException to be fired.
     * @param host String; the host where the RMI registry resides or will be created. Creation is only possible on localhost.
     * @param port int; the port where the RMI registry can be found or will be created
     * @param bindingKey String; the key under which this object will be bound in the RMI registry
     * @throws RemoteException when there is a problem with the RMI registry
     * @throws AlreadyBoundException when there is already another object bound to the bindingKey
     * @throws NullPointerException when host, path, or bindingKey is null
     * @throws IllegalArgumentException when port &lt; 0 or port &gt; 65535
     * @throws AccessException when there is an attempt to create a registry on a remote host
     */
    public RemoteEventProducer(final String host, final int port, final String bindingKey)
            throws RemoteException, AlreadyBoundException
    {
        super(host, port, bindingKey);
        this.eventProducerImpl = new EventProducerImpl(this);
    }

    /**
     * Create a remote event listener and register the listener in the RMI registry. When the host has not been specified in the
     * URL, 127.0.0.1 will be used. When the port has not been specified in the URL, the default RMI port 1099 will be used.
     * When the RMI registry does not exist yet, it will be created, but <b>only</b> on the local host. Remote creation of a
     * registry on another computer is not possible. Any attempt to do so will cause an AccessException to be fired.
     * @param registryURL URL; the URL of the registry, e.g., "http://localhost:1099" or "http://130.161.185.14:28452"
     * @param bindingKey String; the key under which this object will be bound in the RMI registry
     * @throws RemoteException when there is a problem with the RMI registry
     * @throws AlreadyBoundException when there is already another object bound to the bindingKey
     * @throws NullPointerException when registryURL or bindingKey is null
     * @throws AccessException when there is an attempt to create a registry on a remote host
     */
    public RemoteEventProducer(final URL registryURL, final String bindingKey) throws RemoteException, AlreadyBoundException
    {
        super(registryURL, bindingKey);
        this.eventProducerImpl = new EventProducerImpl(this);
    }

    /** {@inheritDoc} */
    @Override
    public abstract Serializable getSourceId(); // without RemoteException

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventTypeInterface eventType)
            throws RemoteException
    {
        return this.eventProducerImpl.addListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventTypeInterface eventType,
            final ReferenceType referenceType) throws RemoteException
    {
        return this.eventProducerImpl.addListener(listener, eventType, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventTypeInterface eventType,
            final int position) throws RemoteException
    {
        return this.eventProducerImpl.addListener(listener, eventType, position);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventTypeInterface eventType,
            final int position, final ReferenceType referenceType) throws RemoteException
    {
        return this.eventProducerImpl.addListener(listener, eventType, position, referenceType);
    }

    /**
     * Remove all the listeners from this event producer.
     * @return int; the number of removed event types
     * @throws RemoteException on network failure
     */
    protected synchronized int removeAllListeners() throws RemoteException
    {
        return this.eventProducerImpl.removeAllListeners();
    }

    /**
     * Removes all the listeners of a class from this event producer.
     * @param ofClass Class&lt;?&gt;; the class or superclass
     * @return int; the number of removed listeners
     * @throws RemoteException on network failure
     */
    protected synchronized int removeAllListeners(final Class<?> ofClass) throws RemoteException
    {
        return this.eventProducerImpl.removeAllListeners(ofClass);

    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean removeListener(final EventListenerInterface listener, final EventTypeInterface eventType)
            throws RemoteException
    {
        return this.eventProducerImpl.removeListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners() throws RemoteException
    {
        return this.eventProducerImpl.hasListeners();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int numberOfListeners(final EventTypeInterface eventType) throws RemoteException
    {
        return this.eventProducerImpl.numberOfListeners(eventType);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized Set<EventTypeInterface> getEventTypesWithListeners() throws RemoteException
    {
        return this.eventProducerImpl.getEventTypesWithListeners();
    }

    /**
     * Return a safe copy of the list of (strong or weak) references to the registered listeners for the provided event type, or
     * an empty list when nothing is registered for this event type. The method never returns a null pointer, so it is safe to
     * use the result directly in an iterator. The references to the listeners are the original references, so not safe copies.
     * @param eventType EventTypeInterface; the event type to look up the listeners for
     * @return List&lt;Reference&lt;EventListenerInterface&gt;&gt;; the list of references to the listeners for this event type,
     *         or an empty list when the event type is not registered
     */
    protected List<Reference<EventListenerInterface>> getListenerReferences(final EventTypeInterface eventType)
    {
        return this.eventProducerImpl.getListenerReferences(eventType);
    }

    /* ********************************************************************************************************* */
    /* ******************** FIREEVENT AND FIRETIMEDEVENT WITH METADATA VERIFICATION ************************** */
    /* ********************************************************************************************************* */

    /**
     * Transmit an event to all interested listeners.
     * @param event EventInterface; the event
     * @throws RemoteException on network failure
     */
    protected synchronized void fireEvent(final EventInterface event) throws RemoteException
    {
        this.eventProducerImpl.fireEvent(event, true);
    }

    /**
     * Transmit a timed event to all interested listeners.
     * @param event TimedEventInterface&lt;C&gt;; the timed event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> void fireTimedEvent(final TimedEventInterface<C> event)
            throws RemoteException
    {
        this.eventProducerImpl.fireTimedEvent(event, true);
    }

    /**
     * Transmit an event with a serializable object as payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value Serializable; the object sent with the event
     * @return Serializable; the payload
     * @throws RemoteException on network failure
     */
    protected Serializable fireEvent(final EventTypeInterface eventType, final Serializable value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit an event with no payload object to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @throws RemoteException on network failure
     */
    protected void fireEvent(final EventTypeInterface eventType) throws RemoteException
    {
        this.eventProducerImpl.fireEvent(eventType, true);
    }

    /**
     * Transmit a time-stamped event with a Serializable object (payload) to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @return Serializable; the payload
     * @param <C> the comparable type to indicate the time when the event is fired
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> Serializable fireTimedEvent(final TimedEventTypeInterface eventType,
            final Serializable value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a one byte payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value byte; the payload
     * @return byte; the payload
     * @throws RemoteException on network failure
     */
    protected byte fireEvent(final EventTypeInterface eventType, final byte value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a one byte payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value byte; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return byte; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> byte fireTimedEvent(final TimedEventTypeInterface eventType,
            final byte value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a one char payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value char; the payload
     * @return char; the payload
     * @throws RemoteException on network failure
     */
    protected char fireEvent(final EventTypeInterface eventType, final char value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a one char payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value char; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return char; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> char fireTimedEvent(final TimedEventTypeInterface eventType,
            final char value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a boolean payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value boolean; the payload
     * @return boolean; the payload
     * @throws RemoteException on network failure
     */
    protected boolean fireEvent(final EventTypeInterface eventType, final boolean value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a boolean payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value boolean; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return boolean; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> boolean fireTimedEvent(final TimedEventTypeInterface eventType,
            final boolean value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a double value payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value double; the payload
     * @return double; the payload
     * @throws RemoteException on network failure
     */
    protected double fireEvent(final EventTypeInterface eventType, final double value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a double value payload to interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value double; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return double; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> double fireTimedEvent(final TimedEventTypeInterface eventType,
            final double value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with an integer payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value int; the payload
     * @return int; the payload
     * @throws RemoteException on network failure
     */
    protected int fireEvent(final EventTypeInterface eventType, final int value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with an integer payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value int; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return int; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> int fireTimedEvent(final TimedEventTypeInterface eventType,
            final int value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a long payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value long; the payload
     * @return long; the payload
     * @throws RemoteException on network failure
     */
    protected long fireEvent(final EventTypeInterface eventType, final long value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a long payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value long; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return long; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> long fireTimedEvent(final TimedEventTypeInterface eventType,
            final long value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a short payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value short; the payload
     * @return short; the payload
     * @throws RemoteException on network failure
     */
    protected short fireEvent(final EventTypeInterface eventType, final short value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a short payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value short; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return short; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> short fireTimedEvent(final TimedEventTypeInterface eventType,
            final short value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /**
     * Transmit an event with a float payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value float; the payload
     * @return float; the payload
     * @throws RemoteException on network failure
     */
    protected float fireEvent(final EventTypeInterface eventType, final float value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, true);
    }

    /**
     * Transmit a time-stamped event with a float payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value float; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return float; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> float fireTimedEvent(final TimedEventTypeInterface eventType,
            final float value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, true);
    }

    /* ********************************************************************************************************* */
    /* ******************* FIREEVENT AND FIRETIMEDEVENT WITHOUT METADATA VERIFICATION ************************ */
    /* ********************************************************************************************************* */

    /**
     * Transmit an event to all interested listeners.
     * @param event EventInterface; the event
     * @throws RemoteException on network failure
     */
    protected void fireUnverifiedEvent(final EventInterface event) throws RemoteException
    {
        this.eventProducerImpl.fireEvent(event, false);
    }

    /**
     * Transmit a timed event to all interested listeners.
     * @param event TimedEventInterface&lt;C&gt;; the timed event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> void fireUnverifiedTimedEvent(final TimedEventInterface<C> event)
            throws RemoteException
    {
        this.eventProducerImpl.fireTimedEvent(event, false);
    }

    /**
     * Transmit an event that is not verified with no payload object to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @throws RemoteException on network failure
     */
    protected void fireUnverifiedEvent(final EventTypeInterface eventType) throws RemoteException
    {
        this.eventProducerImpl.fireEvent(eventType, false);
    }

    /**
     * Transmit a timed event that is not verified with no payload object to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> void fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final C time) throws RemoteException
    {
        this.eventProducerImpl.fireTimedEvent(eventType, time, false);
    }

    /**
     * Transmit an event that is not verified with a serializable object as payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value Serializable; the object sent with the event
     * @return Serializable; the payload
     * @throws RemoteException on network failure
     */
    protected Serializable fireUnverifiedEvent(final EventTypeInterface eventType, final Serializable value)
            throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a Serializable object (payload) to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event.
     * @param value Serializable; the payload sent with the event
     * @param time C; a time stamp for the event
     * @return Serializable; the payload
     * @param <C> the comparable type to indicate the time when the event is fired
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> Serializable fireUnverifiedTimedEvent(
            final TimedEventTypeInterface eventType, final Serializable value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a one byte payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value byte; the payload
     * @return byte; the payload
     * @throws RemoteException on network failure
     */
    protected byte fireUnverifiedEvent(final EventTypeInterface eventType, final byte value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a one byte payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value byte; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return byte; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> byte fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final byte value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a one char payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value char; the payload
     * @return char; the payload
     * @throws RemoteException on network failure
     */
    protected char fireUnverifiedEvent(final EventTypeInterface eventType, final char value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a one char payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value char; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return char; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> char fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final char value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a boolean payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value boolean; the payload
     * @return boolean; the payload
     * @throws RemoteException on network failure
     */
    protected boolean fireUnverifiedEvent(final EventTypeInterface eventType, final boolean value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a boolean payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value boolean; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return boolean; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> boolean fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final boolean value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a double value payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value double; the payload
     * @return double; the payload
     * @throws RemoteException on network failure
     */
    protected double fireUnverifiedEvent(final EventTypeInterface eventType, final double value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a double value payload to interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value double; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return double; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> double fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final double value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with an integer payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value int; the payload
     * @return int; the payload
     * @throws RemoteException on network failure
     */
    protected int fireUnverifiedEvent(final EventTypeInterface eventType, final int value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with an integer payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value int; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return int; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> int fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final int value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a long payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value long; the payload
     * @return long; the payload
     * @throws RemoteException on network failure
     */
    protected long fireUnverifiedEvent(final EventTypeInterface eventType, final long value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a long payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value long; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return long; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> long fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final long value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a short payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value short; the payload
     * @return short; the payload
     * @throws RemoteException on network failure
     */
    protected short fireUnverifiedEvent(final EventTypeInterface eventType, final short value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a short payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value short; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return short; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> short fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final short value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

    /**
     * Transmit an event that is not verified with a float payload to all interested listeners.
     * @param eventType EventTypeInterface; the eventType of the event
     * @param value float; the payload
     * @return float; the payload
     * @throws RemoteException on network failure
     */
    protected float fireUnverifiedEvent(final EventTypeInterface eventType, final float value) throws RemoteException
    {
        return this.eventProducerImpl.fireEvent(eventType, value, false);
    }

    /**
     * Transmit a time-stamped event that is not verified with a float payload to all interested listeners.
     * @param eventType TimedEventTypeInterface; the eventType of the event
     * @param value float; the payload
     * @param time C; a time stamp for the event
     * @param <C> the comparable type to indicate the time when the event is fired
     * @return float; the payload
     * @throws RemoteException on network failure
     */
    protected <C extends Comparable<C> & Serializable> float fireUnverifiedTimedEvent(final TimedEventTypeInterface eventType,
            final float value, final C time) throws RemoteException
    {
        return this.eventProducerImpl.fireTimedEvent(eventType, value, time, false);
    }

}
