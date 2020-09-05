package org.djutils.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * AngleUtilTest tests the angle normalization methods in AngleUtil. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class AngleUtilTest
{
    /**
     * Test angle normalization.
     */
    @Test
    public void testAngleNormalization()
    {
        final double pi = Math.PI;
        final double pi05 = 0.5 * pi;
        final double pi15 = 1.5 * pi;
        final double pi20 = 2.0 * pi;
        final double pi100 = 10.0 * pi;

        // center
        assertEquals(pi, AngleUtil.normalizeAroundPi(pi), 1E-6);
        assertEquals(0.0, AngleUtil.normalizeAroundZero(0.0), 1E-6);

        assertEquals(pi, AngleUtil.normalizeAroundPi(pi + pi20), 1E-6);
        assertEquals(0.0, AngleUtil.normalizeAroundZero(0.0 + pi20), 1E-6);
        assertEquals(pi, AngleUtil.normalizeAroundPi(pi - pi20), 1E-6);
        assertEquals(0.0, AngleUtil.normalizeAroundZero(0.0 - pi20), 1E-6);

        assertEquals(pi, AngleUtil.normalizeAroundPi(pi + pi100), 1E-6);
        assertEquals(0.0, AngleUtil.normalizeAroundZero(0.0 + pi100), 1E-6);
        assertEquals(pi, AngleUtil.normalizeAroundPi(pi - pi100), 1E-6);
        assertEquals(0.0, AngleUtil.normalizeAroundZero(0.0 - pi100), 1E-6);

        // quart
        assertEquals(pi05, AngleUtil.normalizeAroundPi(pi05), 1E-6);
        assertEquals(pi15, AngleUtil.normalizeAroundPi(pi15), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundZero(pi05), 1E-6);
        assertEquals(-pi05, AngleUtil.normalizeAroundZero(-pi05), 1E-6);

        assertEquals(pi05, AngleUtil.normalizeAroundPi(pi05 + pi20), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundPi(pi05 - pi20), 1E-6);
        assertEquals(pi15, AngleUtil.normalizeAroundPi(pi15 + pi20), 1E-6);
        assertEquals(pi15, AngleUtil.normalizeAroundPi(pi15 - pi20), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundZero(pi05 + pi20), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundZero(pi05 - pi20), 1E-6);
        assertEquals(-pi05, AngleUtil.normalizeAroundZero(pi20 - pi05), 1E-6);
        assertEquals(-pi05, AngleUtil.normalizeAroundZero(-pi20 - pi05), 1E-6);

        assertEquals(pi05, AngleUtil.normalizeAroundPi(pi05 + pi100), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundPi(pi05 - pi100), 1E-6);
        assertEquals(pi15, AngleUtil.normalizeAroundPi(pi15 + pi100), 1E-6);
        assertEquals(pi15, AngleUtil.normalizeAroundPi(pi15 - pi100), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundZero(pi05 + pi100), 1E-6);
        assertEquals(pi05, AngleUtil.normalizeAroundZero(pi05 - pi100), 1E-6);
        assertEquals(-pi05, AngleUtil.normalizeAroundZero(pi100 - pi05), 1E-6);
        assertEquals(-pi05, AngleUtil.normalizeAroundZero(-pi100 - pi05), 1E-6);
        
        // edges plus or minus a small number
        final double eps = 1E-8;
        assertEquals(pi, AngleUtil.normalizeAroundZero(pi - eps), 1E-6);
        assertEquals(-pi, AngleUtil.normalizeAroundZero(pi + eps), 1E-6);
        assertEquals(-pi, AngleUtil.normalizeAroundZero(-pi + eps), 1E-6);
        assertEquals(pi, AngleUtil.normalizeAroundZero(-pi - eps), 1E-6);

        assertEquals(0.0, AngleUtil.normalizeAroundPi(eps), 1E-6);
        assertEquals(pi20, AngleUtil.normalizeAroundPi(-eps), 1E-6);
        assertEquals(pi20, AngleUtil.normalizeAroundPi(pi20 - eps), 1E-6);
        assertEquals(0.0, AngleUtil.normalizeAroundPi(pi20 + eps), 1E-6);

        // NaN and Infinity should result in NaN
        assertTrue(Double.isNaN(AngleUtil.normalizeAroundPi(Double.NaN)));
        assertTrue(Double.isNaN(AngleUtil.normalizeAroundZero(Double.NaN)));
        assertTrue(Double.isNaN(AngleUtil.normalizeAroundPi(Double.POSITIVE_INFINITY)));
        assertTrue(Double.isNaN(AngleUtil.normalizeAroundZero(Double.POSITIVE_INFINITY)));
        assertTrue(Double.isNaN(AngleUtil.normalizeAroundPi(Double.NEGATIVE_INFINITY)));
        assertTrue(Double.isNaN(AngleUtil.normalizeAroundZero(Double.NEGATIVE_INFINITY)));
    }
}
