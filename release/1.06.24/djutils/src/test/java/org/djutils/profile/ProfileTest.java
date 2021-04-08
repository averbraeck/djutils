package org.djutils.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test the Profile class.
 */
public class ProfileTest
{

    /**
     * Test the Profile class using sine and cosine.
     */
    @Test
    public void profileTestUsingTrigFunctions()
    {
        System.gc();
        Profile.clear();
        // preload
        doSine(Math.toRadians(90));
        doCosine(Math.toRadians(90));
        Profile.setPrintInterval(0);
        Profile.print();
        Profile.reset();
        for (int step = 0; step <= 90; step++)
        {
            // System.out.println("step " + step);
            double angle = Math.toRadians(step);
            doCosine(angle);
            doSine(angle);
        }
        Profile.print();
    }

    /**
     * Perform a somewhat CPU time intensive function.
     * @param angle double; the argument
     */
    public void doSine(final double angle)
    {
        Profile.start();
        Math.sin(angle);
        Profile.end();
    }

    /**
     * Perform a somewhat CPU time intensive function.
     * @param angle double; the argument
     */
    public void doCosine(final double angle)
    {
        Profile.start();
        Math.cos(angle);
        Profile.end();
    }

    /**
     * Test the Profile class using busy waiting.
     * @throws InterruptedException if that happens; this test has failed
     */
    @Test
    public void profileTestUsingBusyWaiting() throws InterruptedException
    {
        System.gc();
        Profile.setPrintInterval(0);
        Profile.clear();
        // warm up
        delay(100000);
        Profile.reset();
        for (long sleepTime : new long[] {100000, 1000000, 10000000})
        {
            for (int calls : new int[] {1, 5, 10})
            {
                Profile.clear();
                System.out.print("sleep time " + sleepTime + "; calls " + calls + ": iterations:");
                for (int call = 0; call < calls; call++)
                {
                    System.out.print(" " + delay(sleepTime));
                }
                System.out.println();
                Profile.print();
                String stats = Profile.statistics("delay");
                // System.out.println(stats);
                stats = stats.substring(stats.indexOf("[") + 1);
                stats = stats.substring(0, stats.length() - 1);
                String[] pairs = stats.split(", ");
                for (String pair : pairs)
                {
                    String[] parts = pair.split("=");
                    // System.out.println("key=" + parts[0] + ", value=" + parts[1]);
                    String value = parts[1].substring(0, parts[1].length() - 1);
                    switch (parts[0])
                    {
                        case "name":
                            assertEquals("value of name should be \"delay\"", "delay", parts[1]);
                            break;
                        case "start":
                            assertEquals("value of start should be \"null\"", "null", parts[1]);
                            break;
                        case "total":
                            assertTrue("value of total should be at least ... ",
                                    calls * sleepTime <= Double.parseDouble(value) * 1000);
                        case "totalSquared":
                            assertTrue("value of totalSquared should be at least ... ",
                                    calls * sleepTime * calls * sleepTime <= Math.pow(Double.parseDouble(value), 2) * 1000);
                            break;
                        case "minTime":
                            assertTrue("value of minTime should be at least ... ",
                                    calls * sleepTime <= Double.parseDouble(value) * 1000);
                            break;
                        case "maxTime":
                            assertTrue("value of maxTime should be at least ... ",
                                    calls * sleepTime <= Double.parseDouble(value) * 1000);
                            break;
                        case "invocations":
                            assertEquals("value of invocations should be number of calls", calls, Long.parseLong(parts[1]));
                            break;

                        default:
                            fail("Unexpected key: " + parts[0]);
                    }
                }
            }
        }
    }

    /**
     * Delay for specified number of nano seconds. <br>
     * Derived from
     * https://stackoverflow.com/questions/11498585/how-to-suspend-a-java-thread-for-a-small-period-of-time-like-100-nanoseconds
     * @param nanoSeconds long; time to delay in nano seconds
     * @return long; number of iterations in the busy waiting loop
     * @throws InterruptedException If that happens; this test fails
     */
    public long delay(final long nanoSeconds) throws InterruptedException
    {
        Profile.start("delay");
        long start = System.nanoTime();
        long iteration = 0;
        while (start + nanoSeconds >= System.nanoTime())
        {
            iteration++;
        }
        Profile.end("delay");
        return iteration;
    }

    /**
     * Test handling of inappropriate calling sequences.
     */
    @Test
    public void testFailureModes()
    {
        Profile.setPrintInterval(0);
        Profile.clear();
        try
        {
            Profile.end();
            fail("end without start should have thrown an exception");
        }
        catch (IllegalStateException ise)
        {
            // Ignore expected exception
        }

        startOnly();
        try
        {
            startOnly();
            fail("start while already started should have thrown an exception");
        }
        catch (IllegalStateException ise)
        {
            // Ignore expected exception
        }

        Profile.start();
        Profile.end();
        try
        {
            Profile.end();
            fail("end without start should have thrown an exception");
        }
        catch (IllegalStateException ise)
        {
            // Ignore expected exception
        }

        assertNull("statistics for method that never called Profile.start should return null", Profile.statistics("Naaahhhh"));

        try
        {
            Profile.statistics(null);
            fail("calling statistics with a null pointer for the name should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }
    }

    /**
     * Calls start, but not end.
     */
    public void startOnly()
    {
        Profile.start();
    }

}
