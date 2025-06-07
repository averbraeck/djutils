package org.djutils.draw;

import org.djutils.test.ExceptionTest;
import org.junit.jupiter.api.Test;

/**
 * Test the constructors for the djutils-draw Exception classes.
 * <p>
 * Copyright (c) 2013-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @version $Revision: 847 $, $LastChangedDate: 2020-01-17 15:57:08 +0100 (Fri, 17 Jan 2020) $, by $Author: pknoppers $, initial
 *          version 27 sep. 2015 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
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

}
