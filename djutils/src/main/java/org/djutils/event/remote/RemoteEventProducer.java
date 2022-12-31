package org.djutils.event.remote;

import java.io.Serializable;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.LocalEventProducer;
import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;
import org.djutils.rmi.RMIRegisteredObject;

/**
 * The RemoteEventProducer provides a remote implementation of the eventProducer.
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
public abstract class RemoteEventProducer extends RMIRegisteredObject implements EventProducer, Remote
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The EventProducer helper class with the actual implementation to avoid code duplication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final LocalEventProducer embeddedEventProducer;

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
        this.embeddedEventProducer = new LocalEventProducer(this);
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
        this.embeddedEventProducer = new LocalEventProducer(this);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.embeddedEventProducer.getSourceId();
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(final EventListener listener, final EventType eventType, final int position,
            final ReferenceType referenceType)
    {
        return this.embeddedEventProducer.addListener(listener, eventType, position, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeListener(final EventListener listener, final EventType eventType)
    {
        return this.embeddedEventProducer.removeListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners()
    {
        return this.embeddedEventProducer.removeAllListeners();
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners(final Class<?> ofClass)
    {
        return this.embeddedEventProducer.removeAllListeners(ofClass);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners()
    {
        return this.embeddedEventProducer.hasListeners();
    }

    /** {@inheritDoc} */
    @Override
    public int numberOfListeners(final EventType eventType)
    {
        return this.embeddedEventProducer.numberOfListeners(eventType);
    }

    /** {@inheritDoc} */
    @Override
    public List<Reference<EventListener>> getListenerReferences(final EventType eventType)
    {
        return this.embeddedEventProducer.getListenerReferences(eventType);
    }

    /** {@inheritDoc} */
    @Override
    public Set<EventType> getEventTypesWithListeners()
    {
        return this.embeddedEventProducer.getEventTypesWithListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void fireEvent(final Event event)
    {
        this.embeddedEventProducer.fireEvent(event);
    }

}
