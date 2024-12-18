package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test the KnotReport enum.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class KnotReportTest
{
    /**
     * Test the KnotReport enum.
     */
    @Test
    public void testKnotReport()
    {
        assertTrue(KnotReport.KNOWN_FINITE.isFinite(), "is finite");
        assertFalse(KnotReport.KNOWN_INFINITE.isFinite(), "infinite");
        assertTrue(KnotReport.NONE.isFinite(), "is zero (therefore finite)");
        assertFalse(KnotReport.UNKNOWN.isFinite(), "not known is not finite");
        
        assertEquals(KnotReport.NONE, KnotReport.NONE.combineWith(KnotReport.NONE));
        assertEquals(KnotReport.KNOWN_FINITE, KnotReport.NONE.combineWith(KnotReport.KNOWN_FINITE));
        assertEquals(KnotReport.KNOWN_INFINITE, KnotReport.NONE.combineWith(KnotReport.KNOWN_INFINITE));
        assertEquals(KnotReport.UNKNOWN, KnotReport.NONE.combineWith(KnotReport.UNKNOWN));
        assertEquals(KnotReport.KNOWN_FINITE, KnotReport.KNOWN_FINITE.combineWith(KnotReport.NONE));
        assertEquals(KnotReport.KNOWN_FINITE, KnotReport.KNOWN_FINITE.combineWith(KnotReport.KNOWN_FINITE));
        assertEquals(KnotReport.KNOWN_INFINITE, KnotReport.KNOWN_FINITE.combineWith(KnotReport.KNOWN_INFINITE));
        assertEquals(KnotReport.UNKNOWN, KnotReport.KNOWN_FINITE.combineWith(KnotReport.UNKNOWN));
        assertEquals(KnotReport.KNOWN_INFINITE, KnotReport.KNOWN_INFINITE.combineWith(KnotReport.NONE));
        assertEquals(KnotReport.KNOWN_INFINITE, KnotReport.KNOWN_INFINITE.combineWith(KnotReport.KNOWN_FINITE));
        assertEquals(KnotReport.KNOWN_INFINITE, KnotReport.KNOWN_INFINITE.combineWith(KnotReport.KNOWN_INFINITE));
        assertEquals(KnotReport.UNKNOWN, KnotReport.KNOWN_INFINITE.combineWith(KnotReport.UNKNOWN));
        assertEquals(KnotReport.UNKNOWN, KnotReport.UNKNOWN.combineWith(KnotReport.NONE));
        assertEquals(KnotReport.UNKNOWN, KnotReport.UNKNOWN.combineWith(KnotReport.KNOWN_FINITE));
        assertEquals(KnotReport.UNKNOWN, KnotReport.UNKNOWN.combineWith(KnotReport.KNOWN_INFINITE));
        assertEquals(KnotReport.UNKNOWN, KnotReport.UNKNOWN.combineWith(KnotReport.UNKNOWN));
    }
}
