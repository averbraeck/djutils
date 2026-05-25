package org.djutils.draw;

/**
 * Oriented3d is an interface to indicate an object has a direction in three dimensions.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public interface Oriented3d extends Directed3d
{
    /**
     * Return the rotation around the x-axis in radians.
     * @return the rotation around the x-axis in radians
     */
    double getDirX();

}
