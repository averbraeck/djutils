package org.djutils.complex.demo;

import org.djutils.complex.Complex;

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
        Complex.hypot(1.2, 3.4);
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
        System.out.println(String.format("             Math.atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
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
        System.out.println(String.format("             Math.hypot: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            PerformanceTests.hypotA(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("PerformanceTests.hypotA: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Complex.hypot(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("PerformanceTests.hypotB: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000000.0;
            Math.sin(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("          Complex.hypot: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            PerformanceTests.hypotB(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("               Math.sin: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000000.0;
            Math.cos(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos;
        System.out.println(String.format("               Math.cos: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
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
        System.out.println(String.format("             Math.atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
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
        System.out.println(String.format("     Math.sqrt(x*x+y*y): %d invocations in %.6f s (%.1f ns/invocation)",
                iterations, durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
    }

    /** 2^450. */
    private static final double TWO_POW_450 = Double.longBitsToDouble(0x5C10000000000000L);

    /** 2^-450. */
    private static final double TWO_POW_N450 = Double.longBitsToDouble(0x23D0000000000000L);

    /** 2^750? */
    private static final double TWO_POW_750 = Double.longBitsToDouble(0x6ED0000000000000L);

    /** 2^-750? */
    private static final double TWO_POW_N750 = Double.longBitsToDouble(0x1110000000000000L);

    /**
     * Implementation of hypot taken from https://stackoverflow.com/questions/3764978/why-hypot-function-is-so-slow .
     * @param x double; x
     * @param y double; y
     * @return double; the hypot of x, and y
     */
    public static double hypotA(double x, double y)
    {
        x = Math.abs(x);
        y = Math.abs(y);
        if (y < x)
        {
            double a = x;
            x = y;
            y = a;
        }
        else if (!(y >= x))
        { // Testing if we have some NaN.
            if ((x == Double.POSITIVE_INFINITY) || (y == Double.POSITIVE_INFINITY))
            {
                return Double.POSITIVE_INFINITY;
            }
            else
            {
                return Double.NaN;
            }
        }
        if (y - x == y)
        { // x too small to substract from y
            return y;
        }
        else
        {
            double factor;
            if (x > TWO_POW_450)
            { // 2^450 < x < y
                x *= TWO_POW_N750;
                y *= TWO_POW_N750;
                factor = TWO_POW_750;
            }
            else if (y < TWO_POW_N450)
            { // x < y < 2^-450
                x *= TWO_POW_750;
                y *= TWO_POW_750;
                factor = TWO_POW_N750;
            }
            else
            {
                factor = 1.0;
            }
            return factor * Math.sqrt(x * x + y * y);
        }
    }

    /**
     * <b>hypot</b>.
     * @param x double; x
     * @param y double; y
     * @return sqrt(x*x +y*y) without intermediate overflow or underflow.
     * @Note {@link Math#hypot} is unnecessarily slow. This returns the identical result to Math.hypot with reasonable run times
     *       (~40 nsec vs. 800 nsec).
     *       <p>
     *       The logic for computing z is copied from "Freely Distributable Math Library" fdlibm's e_hypot.c. This minimizes
     *       rounding error to provide 1 ulb accuracy.
     */
    public static double hypotB(double x, double y)
    {
        if (Double.isInfinite(x) || Double.isInfinite(y))
        {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(x) || Double.isNaN(y))
        {
            return Double.NaN;
        }

        x = Math.abs(x);
        y = Math.abs(y);

        if (x < y)
        {
            double d = x;
            x = y;
            y = d;
        }

        int xi = Math.getExponent(x);
        int yi = Math.getExponent(y);

        if (xi > yi + 27)
        {
            return x;
        }

        int bias = 0;
        if (xi > 510 || xi < -511)
        {
            bias = xi;
            x = Math.scalb(x, -bias);
            y = Math.scalb(y, -bias);
        }

        // translated from "Freely Distributable Math Library" e_hypot.c to minimize rounding errors
        double z = 0;
        if (x > 2 * y)
        {
            double x1 = Double.longBitsToDouble(Double.doubleToLongBits(x) & 0xffffffff00000000L);
            double x2 = x - x1;
            z = Math.sqrt(x1 * x1 + (y * y + x2 * (x + x1)));
        }
        else
        {
            double t = 2 * x;
            double t1 = Double.longBitsToDouble(Double.doubleToLongBits(t) & 0xffffffff00000000L);
            double t2 = t - t1;
            double y1 = Double.longBitsToDouble(Double.doubleToLongBits(y) & 0xffffffff00000000L);
            double y2 = y - y1;
            double xMinusY = x - y;
            z = Math.sqrt(t1 * y1 + (xMinusY * xMinusY + (t1 * y2 + t2 * y))); // Note: 2*x*y <= x*x + y*y
        }

        if (bias == 0)
        {
            return z;
        }
        else
        {
            return Math.scalb(z, bias);
        }
    }
}
