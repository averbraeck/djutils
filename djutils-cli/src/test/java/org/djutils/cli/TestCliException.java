package org.djutils.cli;

import org.djutils.test.ExceptionTest;
import org.junit.jupiter.api.Test;

/**
 * TestCliException tests the CliException.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestCliException
{
    /**
     * Test the CliException.
     */
    @Test
    public void testCliException()
    {
        ExceptionTest.testExceptionClass(CliException.class);
    }
}
