package org.djutils.draw;

/**
 * Directed3d.java.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * There are two naming conventions for phi and theta. Djutils draw uses phi to denote the angle from the positive x-axis to the
 * projection of the direction in the x-y-plane. On the
 * <a href="https://en.wikipedia.org/wiki/Spherical_coordinate_system">wikepedia page</a> this is named the <i>physics
 * convention</i>. In the <i>mathematics convention</i>, the meanings of phi and theta are swapped. Our preference for the
 * <i>physics convention</i> is based on the fact that phi in the 2d system corresponds to the projection on the x-y-plane of
 * phi in the 3d system.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <D> the Directed type
 */
public interface Directed3d<D extends Directed<D>> extends Directed<D>
{
    /**
     * Retrieve the angle from the positive x-axis to the projection of the direction in the x-y-plane. Positive values rotate
     * towards the positive y-axis (and beyond).
     * @return double; phi
     */
    double getDirZ();

    /**
     * Retrieve the angle from the positive z axis to the direction. Normally these are values between [0:&pi;]. Angles less
     * than &pi;/2 indicate above the x-y-plane; positive slope, angles > &pi;/2 indicate angles below this plane; negative
     * slope.
     * @return double; dirY
     */
    double getDirY();

}
