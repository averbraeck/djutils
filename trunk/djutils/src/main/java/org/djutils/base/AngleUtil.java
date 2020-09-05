package org.djutils.base;

/**
 * AngleUtil has some base methods to deal with angles, such as normalization between -PI and PI, and between 0 and 2*PI. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class AngleUtil
{
    /** Utility construcor. */
    private AngleUtil()
    {
        // Utility constructor
    }

    /** the valie 2&pi;. */
    public static final double PI2 = 2.0 * Math.PI;

    /**
     * Normalize an angle in a 2&pi; wide interval around &pi;, resulting in angles in the 0 to 2&pi; interval. An angle value
     * of NaN or Infinity returns NaN.
     * @param angle double; the angle to normalize
     * @return double; the normalized angle
     */
    public static double normalizeAroundPi(final double angle)
    {
        return angle - PI2 * Math.floor(angle / PI2);
    }

    /**
     * Normalize an angle in a 2&pi; wide interval around 0, resulting in angles in the -&pi; to &pi; interval. An angle value
     * of NaN or Infinity returns NaN.
     * @param angle double; the angle to normalize
     * @return double; the normalized angle
     */
    public static double normalizeAroundZero(final double angle)
    {
        return angle - PI2 * Math.floor((angle + Math.PI) / PI2);
    }

}
