package org.djutils.stats.summarizers;

import java.util.Random;

import org.djutils.stats.summarizers.quantileaccumulator.FixedBinsAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.FullStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.TDigestAccumulator;

/**
 * TallyDemo.java. <br>
 * <br>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class TallyDemo
{
    /** Do not instantiate. */
    private TallyDemo()
    {
        // Do not instantiate
    }

    /**
     * Demonstrate the basic use of a Tally.
     * @param args String[]; the command line arguments (not used)
     */
    public static void main(final String[] args)
    {
        Tally tally = new Tally("Example tally");
        Random random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                       " + tally.getMin());
        System.out.println("maximum:                       " + tally.getMax());
        System.out.println("count:                         " + tally.getN());
        System.out.println("sum:                           " + tally.getSum());
        System.out.println("sample mean:                   " + tally.getSampleMean());
        System.out.println("sample variance:               " + tally.getSampleVariance());
        System.out.println("sample standard deviation:     " + tally.getSampleStDev());
        System.out.println("sample skewness:               " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:               " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:        " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:               " + tally.getPopulationMean());
        System.out.println("population variance:           " + tally.getPopulationVariance());
        System.out.println("population standard deviation: " + tally.getPopulationStDev());
        System.out.println("population skewness:           " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:           " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis:    " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:                " + tally.getQuantile(0.25));
        System.out.println("median:                        " + tally.getQuantile(0.5));
        System.out.println("third quartile:                " + tally.getQuantile(0.75));
        for (int bin = 0; bin <= 10; bin++)
        {
            double value = bin / 10.0;
            System.out.println("Cumulative probability at " + value + "  " + tally.getCumulativeProbability(value));
        }

        System.out.println("End of NoStorageAccumulator output.\n");
        tally = new Tally("Example tally with full storage accumulator", new FullStorageAccumulator());
        random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                       " + tally.getMin());
        System.out.println("maximum:                       " + tally.getMax());
        System.out.println("count:                         " + tally.getN());
        System.out.println("sum:                           " + tally.getSum());
        System.out.println("sample mean:                   " + tally.getSampleMean());
        System.out.println("sample variance:               " + tally.getSampleVariance());
        System.out.println("sample standard deviation:     " + tally.getSampleStDev());
        System.out.println("sample skewness:               " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:               " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:        " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:               " + tally.getPopulationMean());
        System.out.println("population variance:           " + tally.getPopulationVariance());
        System.out.println("population standard deviation: " + tally.getPopulationStDev());
        System.out.println("population skewness:           " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:           " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis:    " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:                " + tally.getQuantile(0.25));
        System.out.println("median:                        " + tally.getQuantile(0.5));
        System.out.println("third quartile:                " + tally.getQuantile(0.75));
        for (int bin = 0; bin <= 10; bin++)
        {
            double value = bin / 10.0;
            System.out.println("Cumulative probability at " + value + "  " + tally.getCumulativeProbability(value));
        }

        System.out.println("End of FullStorageAccumulator output.\n");
        tally = new Tally("Example tally with TDigest accumulator", new TDigestAccumulator());
        random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                       " + tally.getMin());
        System.out.println("maximum:                       " + tally.getMax());
        System.out.println("count:                         " + tally.getN());
        System.out.println("sum:                           " + tally.getSum());
        System.out.println("sample mean:                   " + tally.getSampleMean());
        System.out.println("sample variance:               " + tally.getSampleVariance());
        System.out.println("sample standard deviation:     " + tally.getSampleStDev());
        System.out.println("sample skewness:               " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:               " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:        " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:               " + tally.getPopulationMean());
        System.out.println("population variance:           " + tally.getPopulationVariance());
        System.out.println("population standard deviation: " + tally.getPopulationStDev());
        System.out.println("population skewness:           " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:           " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis:    " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:                " + tally.getQuantile(0.25));
        System.out.println("median:                        " + tally.getQuantile(0.5));
        System.out.println("third quartile:                " + tally.getQuantile(0.75));
        for (int bin = 0; bin <= 10; bin++)
        {
            double value = bin / 10.0;
            System.out.println("Cumulative probability at " + value + "  " + tally.getCumulativeProbability(value));
        }

        System.out.println();
        tally = new Tally("Example tally with TDigest accumulator with higher precision", new TDigestAccumulator(1000));
        random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                       " + tally.getMin());
        System.out.println("maximum:                       " + tally.getMax());
        System.out.println("count:                         " + tally.getN());
        System.out.println("sum:                           " + tally.getSum());
        System.out.println("sample mean:                   " + tally.getSampleMean());
        System.out.println("sample variance:               " + tally.getSampleVariance());
        System.out.println("sample standard deviation:     " + tally.getSampleStDev());
        System.out.println("sample skewness:               " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:               " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:        " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:               " + tally.getPopulationMean());
        System.out.println("population variance:           " + tally.getPopulationVariance());
        System.out.println("population standard deviation: " + tally.getPopulationStDev());
        System.out.println("population skewness:           " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:           " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis:    " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:                " + tally.getQuantile(0.25));
        System.out.println("median:                        " + tally.getQuantile(0.5));
        System.out.println("third quartile:                " + tally.getQuantile(0.75));
        for (int bin = 0; bin <= 10; bin++)
        {
            double value = bin / 10.0;
            System.out.println("Cumulative probability at " + value + "  " + tally.getCumulativeProbability(value));
        }

        System.out.println("End of TDigestAccumulator output.\n");

        tally = new Tally("Example tally with FixedBinsAccumulator using 1000 bins",
                new FixedBinsAccumulator(0.0005, 0.001, 1000));
        random = new Random(1234);
        for (int i = 0; i < 1000000; i++)
        {
            double d = random.nextDouble();
            tally.ingest(d);
        }
        System.out.println("0% quantile (should be close to 0.0):            " + tally.getQuantile(0.0));
        System.out.println("25% quantile (should be close to 0.25):          " + tally.getQuantile(0.25));
        System.out.println("50% quantile (should be close to 0.5):           " + tally.getQuantile(0.50));
        System.out.println("100% quantile (should be close to 1.0):          " + tally.getQuantile(1.0));
        for (int bin = 0; bin < 10; bin++) // only print the first 10 bin counts
        {
            System.out.println("Number of values in bin " + bin + "                        " + (int) Math.rint(tally.getN()
                    * (tally.getCumulativeProbability((bin + 1) / 1000.0) - tally.getCumulativeProbability(bin / 1000.0))));
        }

        System.out.println("");

        tally = new Tally("Example tally with FixedBinsAccumulator using 1001 bins",
                new FixedBinsAccumulator(1.0, (Math.E - 1.0) / 1000, 1001));
        // Feed the tally perfectly exponentially distributed values in the interval [1.0,e)
        random = new Random(1234);
        for (int i = 0; i <= 1000000; i++)
        {
            tally.ingest(Math.exp(1.0 * i / 1000000));
        }
        System.out.println("0% quantile (should be 1.0):                     " + tally.getQuantile(0.0));
        System.out.println("25% quantile (should be close to sqrt(sqrt(e))): " + tally.getQuantile(0.25));
        System.out.println("50% quantile (should be close to sqrt(e)):       " + tally.getQuantile(0.50));
        System.out.println("100% quantile (should be close to e):            " + tally.getQuantile(1.0));
        for (int bin = 0; bin < 10; bin++)
        {
            double value = 1 + bin / 10.0 * (Math.E - 1);
            System.out.println(String.format("Cumulative probability at %8f               ", value)
                    + tally.getCumulativeProbability(value));
        }

        System.out.println("End of FixedBinsAccumulator output.\n");
    }

}
