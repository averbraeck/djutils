package org.djutils.event.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RemoteEventListener class embodies a remote EventListener.
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
public abstract class RemoteEventListener implements RemoteEventListenerInterface
{
    /** */
    private static final long serialVersionUID = 20191230L;

    /**
     * Constructs a new RemoteListener.
     * @throws RemoteException in case of network error
     */
    public RemoteEventListener() throws RemoteException
    {
        super();
        // TODO: which port should the RemoteEventListener use? Probably this asks for a Factory
        UnicastRemoteObject.exportObject(this, 5555);
    }
}
