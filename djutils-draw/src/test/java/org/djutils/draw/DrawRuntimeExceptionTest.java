package org.djutils.draw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test the constructors for ValueException.
 * <p>
 * Copyright (c) 2013-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @version $Revision: 847 $, $LastChangedDate: 2020-01-17 15:57:08 +0100 (Fri, 17 Jan 2020) $, by $Author: pknoppers $, initial
 *          version 27 sep. 2015 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class DrawRuntimeExceptionTest
{
    /**
     * Test all constructors for DrawRuntimeException.
     */
    @Test
    public final void testDrawRuntimeException()
    {
        String message = "MessageString";
        Exception e = new DrawRuntimeException(message);
        assertTrue(null != e, "Exception should not be null");
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(null, e.getCause(), "cause should be null");
        e = new DrawRuntimeException();
        assertTrue(null != e, "Exception should not be null");
        assertEquals(null, e.getCause(), "cause should be null");
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new DrawRuntimeException(cause);
        assertTrue(null != e, "Exception should not be null");
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
        e = new DrawRuntimeException(message, cause);
        assertTrue(null != e, "Exception should not be null");
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
    }

    /**
     * Test all constructors for DrawRuntimeException.
     */
    @Test
    public final void drawRuntimeExceptionTest()
    {
        String message = "MessageString";
        Exception e = new DrawRuntimeException(message);
        assertTrue(null != e, "Exception should not be null");
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(null, e.getCause(), "cause should be null");
        e = new DrawRuntimeException();
        assertTrue(null != e, "Exception should not be null");
        assertEquals(null, e.getCause(), "cause should be null");
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new DrawRuntimeException(cause);
        assertTrue(null != e, "Exception should not be null");
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
        e = new DrawRuntimeException(message, cause);
        assertTrue(null != e, "Exception should not be null");
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
    }

}
