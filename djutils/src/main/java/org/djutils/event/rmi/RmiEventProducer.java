package org.djutils.event.rmi;

import java.io.Serializable;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import org.djutils.event.LocalEventProducer;
import org.djutils.rmi.RmiObject;

/**
 * The RmiEventProducer provides a remote implementation of the eventProducer using the RMI protocol.
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
public abstract class RmiEventProducer extends LocalEventProducer implements Remote
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** The embedded RmiObject class for the remote firing of events. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    private final RmiObject rmiObject;

    /**
     * Create a remote event listener and register the listener in the RMI registry. When the RMI registry does not exist yet,
     * it will be created, but <b>only</b> on the local host. Remote creation of a registry on another computer is not possible.
     * Any attempt to do so will cause an AccessException to be fired.
     * @param host String; the host where the RMI registry resides or will be created. Creation is only possible on localhost.
     * @param port int; the port where the RMI registry can be found or will be created
     * @param bindingKey String; the key under which this object will be bound in the RMI registry
     * @param sourceId Serializable; the sourceId of the event producer to identify the event publisher
     * @throws RemoteException when there is a problem with the RMI registry
     * @throws AlreadyBoundException when there is already another object bound to the bindingKey
     * @throws NullPointerException when host, path, or bindingKey is null
     * @throws IllegalArgumentException when port &lt; 0 or port &gt; 65535
     * @throws AccessException when there is an attempt to create a registry on a remote host
     */
    public RmiEventProducer(final String host, final int port, final String bindingKey, final Serializable sourceId)
            throws RemoteException, AlreadyBoundException
    {
        super(sourceId);
        this.rmiObject = new RmiObject(host, port, bindingKey);
    }

    /**
     * Create a remote event listener and register the listener in the RMI registry. When the host has not been specified in the
     * URL, 127.0.0.1 will be used. When the port has not been specified in the URL, the default RMI port 1099 will be used.
     * When the RMI registry does not exist yet, it will be created, but <b>only</b> on the local host. Remote creation of a
     * registry on another computer is not possible. Any attempt to do so will cause an AccessException to be fired.
     * @param registryURL URL; the URL of the registry, e.g., "http://localhost:1099" or "http://130.161.185.14:28452"
     * @param bindingKey String; the key under which this object will be bound in the RMI registry
     * @param sourceId Serializable; the sourceId of the event producer to identify the event publisher
     * @throws RemoteException when there is a problem with the RMI registry
     * @throws AlreadyBoundException when there is already another object bound to the bindingKey
     * @throws NullPointerException when registryURL or bindingKey is null
     * @throws AccessException when there is an attempt to create a registry on a remote host
     */
    public RmiEventProducer(final URL registryURL, final String bindingKey, final Serializable sourceId)
            throws RemoteException, AlreadyBoundException
    {
        super(sourceId);
        this.rmiObject = new RmiObject(registryURL, bindingKey);
    }

    /**
     * Returns the registry in which this object has been bound, e.g., to look up other objects in the registry.
     * @return Registry; the registry in which this object has been bound
     */
    public Registry getRegistry()
    {
        return this.rmiObject.getRegistry();
    }
}
