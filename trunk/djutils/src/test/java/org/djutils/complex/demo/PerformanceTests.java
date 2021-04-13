package org.djutils.complex.demo;

/**
 * PerformanceTests.java. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class PerformanceTests
{
    /**
     * Do not instantiate.
     */
    private PerformanceTests()
    {
        // Do not instantiate
    }

    /**
     * Measure performance of atan2, hypot, sine and cosine.
     * @param args String[]; the command line arguments (not used)
     */
    public static void main(final String[] args)
    {
        // Ensure that all classes are loaded before measuring things
        Math.atan2(0.5, 1.5);
        Math.hypot(1.2, 3.4);
        Math.sin(0.8);
        Math.cos(0.6);
        Math.sqrt(2.3);

        int iterations = 100000000;
        long startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.atan2(y, x);
        }
        long nowNanos = System.nanoTime();
        long durationNanos = nowNanos - startNanos;
        System.out.println(String.format("atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.hypot(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("hypot: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000000.0;
            Math.sin(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("  sin: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000000.0;
            Math.cos(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("  cos: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.atan2(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.sqrt(x * x + y * y);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("sqrt(x*x+y*y): %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
    }

}
