package org.djutils.stats.summarizers.event;

import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventListener;

/**
 * A listener that throws a RemoteException on notify().
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RmiErrorEventListener implements EventListener
{
    /** */
    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public void notify(final Event event) throws RemoteException
    {
        throw new RemoteException("planned");
    }
}
