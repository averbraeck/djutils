package org.djutils.draw;

/**
 * Directed3d is an interface to indicate an object has a direction in three dimensions.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Directed3d extends Directed2d
{
    /**
     * Return the direction as a rotation around the x-axis in radians.
     * @return double; the rotation around the x-axis in radians
     */
    double getDirX();

    /**
     * Return the direction as a rotation around the y-axis in radians.
     * @return double; the rotation around the y-axis in radians
     */
    double getDirY();

}
