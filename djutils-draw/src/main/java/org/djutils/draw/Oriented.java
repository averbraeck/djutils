package org.djutils.draw;

/**
 * Directed is an interface to indicate an object has a direction.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <O> The Oriented type (2d or 3d)
 */
public interface Oriented<O extends Oriented<O>>
{
    /**
     * Return a new DirectedPoint2d with negated coordinate values. Add 180 degrees (pi radians) to the rotation(s).
     * @return D; a new D with negated coordinate values and a rotation in the opposite direction
     */
    O neg();

    /**
     * Return the rotation around the z-axis in radians.
     * @return double; the rotation around the z-axis in radians
     */
    double getDirZ();
}
