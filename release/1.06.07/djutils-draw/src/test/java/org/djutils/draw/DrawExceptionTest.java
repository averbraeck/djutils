package org.djutils.draw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test the constructors for ValueException.
 * <p>
 * Copyright (c) 2013-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @version $Revision: 847 $, $LastChangedDate: 2020-01-17 15:57:08 +0100 (Fri, 17 Jan 2020) $, by $Author: pknoppers $, initial
 *          version 27 sep. 2015 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class DrawExceptionTest
{
    /**
     * Test all constructors for DrawException.
     */
    @Test
    public final void drawExceptionTest()
    {
        String message = "MessageString";
        Exception e = new DrawException(message);
        assertTrue("Exception should not be null", null != e);
        assertEquals("message should be our message", message, e.getMessage());
        assertEquals("cause should be null", null, e.getCause());
        e = new DrawException();
        assertTrue("Exception should not be null", null != e);
        assertEquals("cause should be null", null, e.getCause());
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new DrawException(cause);
        assertTrue("Exception should not be null", null != e);
        assertEquals("cause should not be our cause", cause, e.getCause());
        assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
        e = new DrawException(message, cause);
        assertTrue("Exception should not be null", null != e);
        assertEquals("message should be our message", message, e.getMessage());
        assertEquals("cause should not be our cause", cause, e.getCause());
        assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
    }

    /**
     * Test all constructors for DrawRuntimeException.
     */
    @Test
    public final void DrawRuntimeExceptionTest()
    {
        String message = "MessageString";
        Exception e = new DrawRuntimeException(message);
        assertTrue("Exception should not be null", null != e);
        assertEquals("message should be our message", message, e.getMessage());
        assertEquals("cause should be null", null, e.getCause());
        e = new DrawRuntimeException();
        assertTrue("Exception should not be null", null != e);
        assertEquals("cause should be null", null, e.getCause());
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new DrawRuntimeException(cause);
        assertTrue("Exception should not be null", null != e);
        assertEquals("cause should not be our cause", cause, e.getCause());
        assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
        e = new DrawRuntimeException(message, cause);
        assertTrue("Exception should not be null", null != e);
        assertEquals("message should be our message", message, e.getMessage());
        assertEquals("cause should not be our cause", cause, e.getCause());
        assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
    }

}
