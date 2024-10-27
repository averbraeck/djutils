package org.djutils.draw;

/**
 * Directed3d.java.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * There are two naming conventions for phi and theta. Djutils draw uses neither to stay clear of this confusion. The angle from
 * the positive z-axis to the projection of the direction on the x-y-plane is named <cite>dirY</cite>. The angle from the
 * positive x-axis to the projection of the direction in the x-y-plane is named <cite>dirZ</cite>.
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
     * @return double; dirZ
     */
    double getDirZ();

    /**
     * Retrieve the angle from the positive z axis to the direction. Normally these are values between [0:&pi;]. Angles less
     * than &pi;/2 indicate above the x-y-plane; positive slope, angles &gt; &pi;/2 indicate angles below this plane; negative
     * slope.
     * @return double; dirY
     */
    double getDirY();

}
