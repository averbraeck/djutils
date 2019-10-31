package org.djutils.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test the methods in the Try class.
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class TryTest
{

    /**
     * Test the methods in the Try class.
     * @throws RuntimeException if that happens (uncaught); this test has failed.
     */
    @Test
    public void tryTest() throws RuntimeException
    {
        String nullPointer = null;
        String initialValueOfResult = "initial value of result";
        String result = initialValueOfResult;
        String formatWithoutArg = "format";
        String arg1 = "arg1";
        String formatWithOneArg = "format %s";
        String arg2 = "arg2";
        String formatWith2Args = "format %s %s";
        String arg3 = "arg3";
        String formatWith3Args = "format %s %s %s";
        String arg4 = "arg4";
        String formatWith4Args = "format %s %s %s %s";

        result = Try.assign(() -> String.format(formatWithoutArg), "Should not fail");
        assertEquals("assign should have succeeded", formatWithoutArg, result);
        result = initialValueOfResult;

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), formatWithoutArg);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), formatWithOneArg, arg1);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), formatWith2Args, arg1, arg2);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), formatWith3Args, arg1, arg2, arg3);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), formatWith4Args, arg1, arg2, arg3, arg4);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
            assertTrue("message contains arg4", rte.getMessage().contains(arg4));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        result = Try.assign(() -> String.format(formatWithoutArg), RuntimeException.class, "Should not fail");
        assertEquals("assign should have succeeded", formatWithoutArg, result);
        result = initialValueOfResult;

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWithoutArg);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWithOneArg,
                    arg1);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWith2Args,
                    arg1, arg2);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWith3Args,
                    arg1, arg2, arg3);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.assign(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWith4Args,
                    arg1, arg2, arg3, arg4);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
            assertTrue("message contains arg4", rte.getMessage().contains(arg4));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWithoutArg);
            fail("String.format with nullPointer should have thrown a NullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWithOneArg, arg1);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWith2Args, arg1,
                    arg2);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWith3Args, arg1,
                    arg2, arg3);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), RuntimeException.class, formatWith4Args, arg1,
                    arg2, arg3, arg4);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
            assertTrue("message contains arg4", rte.getMessage().contains(arg4));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), formatWithoutArg);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), formatWithOneArg, arg1);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), formatWith2Args, arg1, arg2);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), formatWith3Args, arg1, arg2, arg3);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            Try.execute(() -> String.format(nullPointer, "unused argument"), formatWith4Args, arg1, arg2, arg3, arg4);
            fail("String.format with nullPointer should have thrown a nullPointerException");
        }
        catch (RuntimeException rte)
        {
            System.out.println("Cause: " + rte.getCause().toString());
            assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                    rte.getCause().toString().contains("NullPointerException"));
            assertTrue("message contains format", rte.getMessage().contains(formatWithoutArg));
            assertTrue("message contains arg1", rte.getMessage().contains(arg1));
            assertTrue("message contains arg2", rte.getMessage().contains(arg2));
            assertTrue("message contains arg3", rte.getMessage().contains(arg3));
            assertTrue("message contains arg4", rte.getMessage().contains(arg4));
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        try
        {
            result = Try.testFail(() -> String.format(formatWithoutArg), NullPointerException.class);
            fail("testFail should have thrown an exception because no NullPointerException was thrown in the assignment");
        }
        catch (Error e)
        {
            // Ignore expected error
        }
        assertEquals("Result has not changed", initialValueOfResult, result);

        result = Try.testFail(() -> String.format(nullPointer), NullPointerException.class);
        assertNull("Result has changed to null", result);
        result = initialValueOfResult;

        result = Try.testSucceed(() -> String.format(formatWithoutArg), NullPointerException.class);
        assertEquals("result should be changed", formatWithoutArg, result);
        result = initialValueOfResult;

    }

    /**
     * Can we get our intermittent test result to show up without the JUnit test harness?
     * @param args String[]; the command line arguments
     */
    public static void main(final String[] args)
    {
        String nullPointer = null;
        String arg1 = "arg1";
        String arg2 = "arg2";
        String arg3 = "arg3";
        String arg4 = "arg4";
        String formatWith4Args = "format %s %s %s %s";

        for (int iteration = 1; true; iteration++)
        {
            System.out.println("Starting iteration " + iteration);
            try
            {
                Try.execute(() -> String.format(nullPointer, "unused argument"), formatWith4Args, arg1, arg2, arg3, arg4);
                fail("String.format with nullPointer should have thrown a nullPointerException");
            }
            catch (RuntimeException rte)
            {
                if (!rte.getCause().toString().contains("NullPointerException"))
                {
                    System.out.println("Expected NullPointerException; got " + rte.getCause().toString());
                }
                assertTrue("message cause is NullPointerException, instead got: " + rte.getCause().toString(),
                        rte.getCause().toString().contains("NullPointerException"));
            }
        }
    }

}
