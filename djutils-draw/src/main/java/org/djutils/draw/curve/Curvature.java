package org.djutils.draw.curve;

/**
 * Additional curve properties.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Curvature
{
    /**
     * Start curvature of this Curve2d.
     * @return start curvature of this Curve2d.
     */
    double getStartCurvature();

    /**
     * End curvature of this Curve2d..
     * @return end curvature of this Curve2d
     */
    double getEndCurvature();

    /**
     * Start radius of this Curve2d.
     * @return start radius of this Curve2d
     */
    default double getStartRadius()
    {
        return 1.0 / getStartCurvature();
    }

    /**
     * End radius of this Curve2d.
     * @return end radius of this Curve2d
     */
    default double getEndRadius()
    {
        return 1.0 / getEndCurvature();
    }

}
