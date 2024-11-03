package org.djutils.draw;

/**
 * Directed is the interface to specify a Direction (a vector without a length, pointing in a direction). This is <b>not</b> the
 * direction of the object as seen from the origin (0,0,0).
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <D> the Directed type
 */
public interface Directed<D extends Directed<D>>
{
    /**
     * Return a new D with negated coordinate values. Adds 180 degrees (pi radians) to the rotation(s) and normalizes them.
     * @return D; a new D with negated coordinate values and a rotation in the opposite direction
     */
    D neg();

    /**
     * Compare this Directed with another Directed with specified tolerances in the coordinates and the angles.
     * @param other D; the Directed to compare to
     * @param epsilonCoordinate double; the upper bound of difference for one of the coordinates; use Double.POSITIVE_INFINITY
     *            if you do not want to check the coordinates
     * @param epsilonDirection double; the upper bound of difference for the direction(s); use Double.POSITIVE_INFINITY if you
     *            do not want to check the angles
     * @return boolean; true if x, y, and z are less than epsilonCoordinate apart, and rotX, rotY and rotZ are less than
     *         epsilonRotation apart, otherwise false
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException epsilonCoordinate or epsilonRotation is NaN or negative
     */
    boolean epsilonEquals(D other, double epsilonCoordinate, double epsilonDirection)
            throws NullPointerException, IllegalArgumentException;

}
