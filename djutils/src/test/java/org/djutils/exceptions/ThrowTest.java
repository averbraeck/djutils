package org.djutils.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * The JUNIT Test for <code>Throw</code> class.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class ThrowTest
{
    /**
     * Test the Throw class.
     */
    @SuppressWarnings({"checkstyle:methodlength", "cast"})
    @Test
    public void testThrow()
    {
        Object object = new Object();
        Object objectNull = null;
        int i = 10;
        Double d = Double.valueOf(20.0);
        String s = "argument";
        int hex = 26; // 1A

        //
        // Throw.whenNull(...)
        //

        String message = "Throw error has occurred. Correct";
        try
        {
            Throw.whenNull(objectNull, message);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message, e.getMessage().endsWith(message));
        }

        String message1arg = "Throw error has occurred for %s. Correct";
        String message1 = "Throw error has occurred for argument. Correct";
        try
        {
            Throw.whenNull(objectNull, message1arg, s);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message1, e.getMessage().endsWith(message1));
        }

        String message2arg = "Throw error %d has occurred for %s. Correct";
        String message2 = "Throw error 10 has occurred for argument. Correct";
        try
        {
            Throw.whenNull(objectNull, message2arg, i, s);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message2, e.getMessage().endsWith(message2));
        }

        String message3arg = "Throw error %4.1f has occurred for %s, value %d. Correct";
        String message3 = "Throw error 20.0 has occurred for argument, value 10. Correct";
        try
        {
            Throw.whenNull(objectNull, message3arg, d, s, i);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message3, e.getMessage().endsWith(message3));
        }

        String message4arg = "Throw error %4.1f has occurred for %s, hex=%h, value %d. Correct";
        String message4 = "Throw error 20.0 has occurred for argument, hex=1a, value 10. Correct";
        try
        {
            Throw.whenNull(objectNull, message4arg, d, s, hex, i);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message4, e.getMessage().endsWith(message4));
        }

        // this should be okay
        Throw.whenNull(object, "object is not null -- this should not be triggered");
        Throw.whenNull(object, message1arg, s);
        Throw.whenNull(object, message2arg, i, s);
        Throw.whenNull(object, message3arg, d, s, i);
        Throw.whenNull(object, message4arg, d, s, hex, i);

        //
        // Throw.when(...)
        //

        try
        {
            Throw.when(i == 10, Exception.class, message);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message, e.getMessage().endsWith(message));
        }

        try
        {
            Throw.when(i == 10, Exception.class, message1arg, s);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message1, e.getMessage().endsWith(message1));
        }

        try
        {
            Throw.when(i == 10, Exception.class, message2arg, i, s);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message2, e.getMessage().endsWith(message2));
        }

        try
        {
            Throw.when(i == 10, Exception.class, message3arg, d, s, i);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message3, e.getMessage().endsWith(message3));
        }

        try
        {
            Throw.when(i == 10, Exception.class, message4arg, d, s, hex, i);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage() + " / " + message4, e.getMessage().endsWith(message4));
        }

        // this should be okay
        Throw.when(false, RuntimeException.class, "object is not null -- this should not be triggered");
        Throw.when(false, RuntimeException.class, message1arg, s);
        Throw.when(false, RuntimeException.class, message2arg, i, s);
        Throw.when(false, RuntimeException.class, message3arg, d, s, i);
        Throw.when(false, RuntimeException.class, message4arg, d, s, hex, i);

        assertEquals(d, Throw.when(d, false, RuntimeException.class, "object is not null -- this should not be triggered"));
        assertEquals(d, Throw.when(d, false, RuntimeException.class, message1arg, s));
        assertEquals(d, Throw.when(d, false, RuntimeException.class, message2arg, i, s));
        assertEquals(d, Throw.when(d, false, RuntimeException.class, message3arg, d, s, i));
        assertEquals(d, Throw.when(d, false, RuntimeException.class, message4arg, d, s, hex, i));

        try
        {
            Throw.when(true, RuntimeException.class, message);
            fail("Throw.when(true, ...) should have thrown an exception");
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("message is present", rte.getMessage().contains(message));
        }

        String badFormatString = "FormatString with bad placeholder %f";
        String notAFloatOrDouble = "this is not a float or double";
        try
        {
            Throw.when(true, RuntimeException.class, badFormatString, notAFloatOrDouble);
            fail("Throw.when(true, ...) should have thrown an exception");
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("description is descriptive, despite error in format string",
                    rte.getMessage().contains(badFormatString));
            assertTrue("description is descriptive, despite error in format string",
                    rte.getMessage().contains(notAFloatOrDouble));
        }

        try
        {
            Throw.when("result (which will not be returned because there will be no return)", true, RuntimeException.class,
                    badFormatString, notAFloatOrDouble);
            fail("Throw.when(true, ...) should have thrown an exception");
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("description is descriptive, despite error in format string",
                    rte.getMessage().contains(badFormatString));
            assertTrue("description is descriptive, despite error in format string",
                    rte.getMessage().contains(notAFloatOrDouble));
        }

        try
        {
            Throw.when("result (which will not be returned because there will be no return)", true, RuntimeException.class,
                    message);
            fail("Throw.when(true, ...) should have thrown an exception");
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("description is descriptive", rte.getMessage().contains(message));
        }

        Object result = Double.valueOf(123 / 456);
        String formatStringWith2PlaceHolders = "message %s %s";
        String arg1 = "arg1";
        String arg2 = "arg2";
        try
        {
            Throw.when(result, true, RuntimeException.class, formatStringWith2PlaceHolders, arg1, arg2);
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("description is descriptive", rte.getMessage().contains("message"));
            assertTrue("description is descriptive", rte.getMessage().contains(arg1));
            assertTrue("description is descriptive", rte.getMessage().contains(arg2));
        }

        String formatStringWith3PlaceHolders = "message %s %s %s";
        String arg3 = "arg3";
        try
        {
            Throw.when(result, true, RuntimeException.class, formatStringWith3PlaceHolders, arg1, arg2, arg3);
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("description is descriptive", rte.getMessage().contains("message"));
            assertTrue("description is descriptive", rte.getMessage().contains(arg1));
            assertTrue("description is descriptive", rte.getMessage().contains(arg2));
            assertTrue("description is descriptive", rte.getMessage().contains(arg3));
        }

        String formatStringWith4PlaceHolders = "message %s %s %s %s";
        String arg4 = "arg4";
        try
        {
            Throw.when(result, true, RuntimeException.class, formatStringWith4PlaceHolders, arg1, arg2, arg3, arg4);
        }
        catch (RuntimeException rte)
        {
            assertTrue("exception is a RuntimeException", rte instanceof RuntimeException);
            assertTrue("description is descriptive", rte.getMessage().contains("message"));
            assertTrue("description is descriptive", rte.getMessage().contains(arg1));
            assertTrue("description is descriptive", rte.getMessage().contains(arg2));
            assertTrue("description is descriptive", rte.getMessage().contains(arg3));
            assertTrue("description is descriptive", rte.getMessage().contains(arg4));
        }
    }

}
