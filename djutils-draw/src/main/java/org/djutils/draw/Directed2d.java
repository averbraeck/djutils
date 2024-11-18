package org.djutils.draw;

/**
 * Interface for objects that have a direction in the XY-plane.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <D> the Directed type
 */
public interface Directed2d<D extends Directed<D>> extends Directed<D>
{
    /**
     * Retrieve the angle from the positive x-axis. Positive rotates towards the positive y-axis (and beyond).
     * @return dirZ
     */
    double getDirZ();

}
