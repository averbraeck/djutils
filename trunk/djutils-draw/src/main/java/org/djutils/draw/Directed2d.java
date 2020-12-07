package org.djutils.draw;

/**
 * Directed2d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Directed2d extends Directed<Directed2d>
{
    /**
     * Return the direction as a rotation around the z-axis in radians.
     * @return double; the rotation around the z-axis in radians
     */
    double getDirZ();

}

