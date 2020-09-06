package org.djutils.draw;

import java.rmi.RemoteException;

import org.djutils.draw.bounds.BoundingBox;
import org.djutils.draw.d0.DirectedPoint;

/**
 * The Locatable interface enforces implementation of information on position, direction and bounds of an object. Objects can
 * reside elsewhere on the network, so the methods in the Locatable interface can throw RemoteExceptions. The original Locatable
 * interface was part of the DSOL simulation library.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Locatable
{
    /**
     * returns the directed location of an object.
     * @return DirectedPoint the location
     * @throws RemoteException on network failure
     */
    DirectedPoint getLocation() throws RemoteException;

    /**
     * returns the bounds of the locatable object. The bounds is the not rotated bounds around [0;0;0]
     * @return BoundingBox with this.getLocation() as center of the box.
     * @throws RemoteException on network failure
     */
    BoundingBox getBounds() throws RemoteException;
}
