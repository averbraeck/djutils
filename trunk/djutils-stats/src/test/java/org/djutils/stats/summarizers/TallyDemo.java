package org.djutils.stats.summarizers;

import java.util.Random;

import org.djutils.stats.summarizers.quantileaccumulator.FullStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.TDigestAccumulator;

/**
 * TallyDemo.java.
 * <br><br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <br>
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
        System.out.println("minimum:                    " + tally.getMin());
        System.out.println("maximum:                    " + tally.getMax());
        System.out.println("count:                      " + tally.getN());
        System.out.println("sum:                        " + tally.getSum());
        System.out.println("sample mean:                " + tally.getSampleMean());
        System.out.println("sample variance:            " + tally.getSampleVariance());
        System.out.println("sample skewness:            " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:            " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:     " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:            " + tally.getPopulationMean());
        System.out.println("population variance:        " + tally.getPopulationVariance());
        System.out.println("population skewness:        " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:        " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis: " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:             " + tally.getQuantile(0.25));
        System.out.println("median:                     " + tally.getQuantile(0.5));
        System.out.println("third quartile:             " + tally.getQuantile(0.75));

        System.out.println();
        tally = new Tally("Example tally with full storage accumulator", new FullStorageAccumulator());
        random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                    " + tally.getMin());
        System.out.println("maximum:                    " + tally.getMax());
        System.out.println("count:                      " + tally.getN());
        System.out.println("sum:                        " + tally.getSum());
        System.out.println("sample mean:                " + tally.getSampleMean());
        System.out.println("sample variance:            " + tally.getSampleVariance());
        System.out.println("sample skewness:            " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:            " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:     " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:            " + tally.getPopulationMean());
        System.out.println("population variance:        " + tally.getPopulationVariance());
        System.out.println("population skewness:        " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:        " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis: " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:             " + tally.getQuantile(0.25));
        System.out.println("median:                     " + tally.getQuantile(0.5));
        System.out.println("third quartile:             " + tally.getQuantile(0.75));

        System.out.println();
        tally = new Tally("Example tally with TDigest accumulator", new TDigestAccumulator());
        random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                    " + tally.getMin());
        System.out.println("maximum:                    " + tally.getMax());
        System.out.println("count:                      " + tally.getN());
        System.out.println("sum:                        " + tally.getSum());
        System.out.println("sample mean:                " + tally.getSampleMean());
        System.out.println("sample variance:            " + tally.getSampleVariance());
        System.out.println("sample skewness:            " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:            " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:     " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:            " + tally.getPopulationMean());
        System.out.println("population variance:        " + tally.getPopulationVariance());
        System.out.println("population skewness:        " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:        " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis: " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:             " + tally.getQuantile(0.25));
        System.out.println("median:                     " + tally.getQuantile(0.5));
        System.out.println("third quartile:             " + tally.getQuantile(0.75));

        System.out.println();
        tally = new Tally("Example tally with TDigest accumulator with higher precision", new TDigestAccumulator(1000));
        random = new Random(1234);
        for (int i = 0; i < 1000; i++)
        {
            tally.ingest(random.nextDouble());
        }
        System.out.println("minimum:                    " + tally.getMin());
        System.out.println("maximum:                    " + tally.getMax());
        System.out.println("count:                      " + tally.getN());
        System.out.println("sum:                        " + tally.getSum());
        System.out.println("sample mean:                " + tally.getSampleMean());
        System.out.println("sample variance:            " + tally.getSampleVariance());
        System.out.println("sample skewness:            " + tally.getSampleSkewness());
        System.out.println("sample kurtosis:            " + tally.getSampleKurtosis());
        System.out.println("sample excess kurtosis:     " + tally.getSampleExcessKurtosis());
        System.out.println("population mean:            " + tally.getPopulationMean());
        System.out.println("population variance:        " + tally.getPopulationVariance());
        System.out.println("population skewness:        " + tally.getPopulationSkewness());
        System.out.println("population kurtosis:        " + tally.getPopulationKurtosis());
        System.out.println("population excess kurtosis: " + tally.getPopulationExcessKurtosis());
        System.out.println("first quartile:             " + tally.getQuantile(0.25));
        System.out.println("median:                     " + tally.getQuantile(0.5));
        System.out.println("third quartile:             " + tally.getQuantile(0.75));

    }

}

