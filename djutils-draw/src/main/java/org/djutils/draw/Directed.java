package org.djutils.draw;

/**
 * Directed is the interface to specify a Direction in the XY-plane (a vector without a length, pointing in a direction). This
 * is <b>not</b> the direction of the object as seen from the origin (0,0,0).
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Directed
{
    /**
     * Retrieve the angle from the positive x-axis. Positive rotates towards the positive y-axis (and beyond).
     * @return the angle from the positive x-axis
     */
    double getDirZ();

}
