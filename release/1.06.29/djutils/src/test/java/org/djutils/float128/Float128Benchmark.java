package org.djutils.float128;

import java.util.Random;

/**
 * Float128Benchmark tests the efficiency of the Float128 class. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class Float128Benchmark
{
    /** Utility class. */
    private Float128Benchmark()
    {
        // utility class
    }

    /**
     * Benchmark the different Float128 operators and compare with regular double values.
     * @param args not used
     */
    @SuppressWarnings("unused")
    public static void main(final String[] args)
    {
        Random rand = new Random(1L);
        double[] d1 = new double[4096];
        double[] d2 = new double[d1.length];
        for (int i = 0; i < d1.length; i++)
        {
            d1[i] = 1.0E6 * rand.nextDouble();
            d2[i] = 1.0E6 * rand.nextDouble();
        }
        System.out.println(String.format("%-16s     %16s     %16s", "operator", "double (ms)", "Float128 (ms)"));
        long ms = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++)
        {
            int j = i & 0xFFF;
        }
        long xtime = System.currentTimeMillis() - ms;

        // plus()
        ms = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++)
        {
            double d = d1[i & 0xFFF] + d2[i & 0xFFF];
        }
        long dtime = System.currentTimeMillis() - ms - xtime;

        ms = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++)
        {
            new Float128(d1[i & 0xFFF]).plus(new Float128(d2[i & 0xFFF]));
        }
        long ftime = System.currentTimeMillis() - ms - xtime;
        System.out.println(String.format("%-16s     %16d     %16d", "plus (10M)", dtime, ftime));
    }
}
