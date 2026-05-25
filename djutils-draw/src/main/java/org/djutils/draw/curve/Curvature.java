package org.djutils.draw.curve;

/**
 * Additional curve properties.
 * <p>
 * Copyright (c) 2023-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
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
