package org.djutils.draw;

import org.djutils.test.ExceptionTest;
import org.junit.jupiter.api.Test;

/**
 * Test the constructors for the djutils-draw Exception classes.
 * <p>
 * Copyright (c) 2013-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class DrawExceptionTest
{
    /**
     * Test all constructors for InvalidProjectionException.
     */
    @Test
    public final void testInvalidProjectionException()
    {
        ExceptionTest.testExceptionClass(InvalidProjectionException.class);
    }

    /**
     * Test all constructors for InternalCalculationException.
     */
    @Test
    public final void testInternalCalculationException()
    {
        ExceptionTest.testExceptionClass(InternalCalculationException.class);
    }

}
