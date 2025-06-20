package org.djutils.stats.summarizers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.djutils.exceptions.Try;
import org.djutils.stats.ConfidenceInterval;
import org.djutils.stats.DistNormalTable;
import org.djutils.stats.summarizers.quantileaccumulator.FullStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.TDigestAccumulator;
import org.junit.jupiter.api.Test;

/**
 * The TallyTest test the tally.
 * <p>
 * Copyright (c) 2002-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class TallyTest
{
    /** Test the tally. */
    @SuppressWarnings("checkstyle:methodlength")
    @Test
    public void testTally()
    {
        String description = "THIS TALLY IS TESTED";
        Tally tally = new Tally(description);

        // check the description
        assertTrue(tally.toString().contains(description));
        assertEquals(description, tally.getDescription());
        assertTrue(tally.toString().startsWith("Tally"));

        // change the description
        tally.setDescription("new description");
        assertEquals("new description", tally.getDescription());
        tally.setDescription(description);
        assertEquals(description, tally.getDescription());

        // now we check the initial values
        assertTrue(Double.valueOf(tally.getMin()).isNaN());
        assertTrue(Double.valueOf(tally.getMax()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(tally.getPopulationMean()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(tally.getPopulationVariance()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleStDev()).isNaN());
        assertTrue(Double.valueOf(tally.getPopulationStDev()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleSkewness()).isNaN());
        assertTrue(Double.valueOf(tally.getPopulationSkewness()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleKurtosis()).isNaN());
        assertTrue(Double.valueOf(tally.getPopulationKurtosis()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleExcessKurtosis()).isNaN());
        assertTrue(Double.valueOf(tally.getPopulationExcessKurtosis()).isNaN());
        assertEquals(0, tally.getSum(), 0);
        assertEquals(0L, tally.getN());
        assertNull(tally.getConfidenceInterval(0.95));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.LEFT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.BOTH_SIDE_CONFIDENCE));

        // Now we register some values
        tally.register(1.1);
        assertFalse(Double.isNaN(tally.getSampleMean()), "sample mean is now available");
        assertFalse(Double.isNaN(tally.getPopulationMean()), "mean is now available");
        assertEquals(1.1, tally.getSampleMean(), 0.0000001, "smaple mean is 1.1");
        assertEquals(1.1, tally.getPopulationMean(), 0.0000001, "mean is 1.1");
        assertTrue(Double.isNaN(tally.getSampleVariance()), "sample variance is not available");
        assertFalse(Double.isNaN(tally.getPopulationVariance()), "variance is not available");
        assertTrue(Double.isNaN(tally.getPopulationSkewness()), "skewness is not available");
        tally.register(1.2);
        assertFalse(Double.isNaN(tally.getSampleVariance()), "sample variance is now available");
        assertTrue(Double.isNaN(tally.getSampleSkewness()), "sample skewness is not available");
        assertTrue(Double.isNaN(tally.getSampleKurtosis()), "sample kurtosis is not available");
        assertFalse(Double.isNaN(tally.getPopulationSkewness()), "skewness is available");
        assertTrue(Double.isNaN(tally.getPopulationKurtosis()), "kurtosis is not available");
        tally.register(1.3);
        assertFalse(Double.isNaN(tally.getSampleSkewness()), "skewness is now available");
        assertFalse(Double.isNaN(tally.getPopulationKurtosis()), "kurtosis is now available");
        assertTrue(Double.isNaN(tally.getSampleKurtosis()), "sample kurtosis is not available");
        tally.register(1.4);
        assertFalse(Double.isNaN(tally.getSampleKurtosis()), "sample kurtosis is now available");
        tally.register(1.5);
        tally.register(1.6);
        tally.register(1.7);
        tally.register(1.8);
        tally.register(1.9);
        tally.register(2.0);
        tally.register(1.0);

        // check the report functions
        int len = Tally.reportFooter().length();
        assertEquals(len, Tally.reportHeader().split("\\R")[0].length());
        assertEquals(len, Tally.reportHeader().split("\\R")[1].length());
        assertEquals(len, Tally.reportHeader().split("\\R")[2].length());
        assertEquals(len, tally.reportLine().length());
        assertEquals(len, new Tally("empty tally").reportLine().length());
        Tally tallyX = new Tally("1 value");
        tallyX.register(1E10);
        assertEquals(len, tallyX.reportLine().length());
        Tally tallyY = new Tally("1 very small value");
        tallyY.register(-0.000000000002);
        assertEquals(len, tallyY.reportLine().length());

        // Now we check the tally
        assertEquals(2.0, tally.getMax(), 1.0E-6);
        assertEquals(1.0, tally.getMin(), 1.0E-6);
        assertEquals(11, tally.getN());
        assertEquals(16.5, tally.getSum(), 1.0E-6);
        assertEquals(1.5, tally.getSampleMean(), 1.0E-6);
        assertEquals(0.110000, tally.getSampleVariance(), 1.0E-6);
        assertEquals(0.331662, tally.getSampleStDev(), 1.0E-6);
        // From: https://atozmath.com/StatsUG.aspx
        assertEquals(0.1, tally.getPopulationVariance(), 1.0E-6);
        assertEquals(Math.sqrt(0.1), tally.getPopulationStDev(), 1.0E-6);
        assertEquals(0.0, tally.getPopulationSkewness(), 1.0E-6);
        assertEquals(1.78, tally.getPopulationKurtosis(), 1.0E-6);
        assertEquals(-1.22, tally.getPopulationExcessKurtosis(), 1.0E-6);
        assertEquals(0.0, tally.getSampleSkewness(), 1.0E-6);
        assertEquals(-1.2, tally.getSampleExcessKurtosis(), 1.0E-6);
        assertEquals(1.618182, tally.getSampleKurtosis(), 1.0E-6);

        assertEquals(1.304003602, tally.getConfidenceInterval(0.05)[0], 1E-05);
        assertEquals(1.695996398, tally.getConfidenceInterval(0.05)[1], 1E-05);
        assertEquals(1.335514637, tally.getConfidenceInterval(0.10)[0], 1E-05);
        assertEquals(1.664485363, tally.getConfidenceInterval(0.10)[1], 1E-05);
        assertEquals(1.356046853, tally.getConfidenceInterval(0.15)[0], 1E-05);
        assertEquals(1.643953147, tally.getConfidenceInterval(0.15)[1], 1E-05);
        assertEquals(1.432551025, tally.getConfidenceInterval(0.50)[0], 1E-05);
        assertEquals(1.567448975, tally.getConfidenceInterval(0.50)[1], 1E-05);
        assertEquals(1.474665290, tally.getConfidenceInterval(0.80)[0], 1E-05);
        assertEquals(1.525334710, tally.getConfidenceInterval(0.80)[1], 1E-05);
        assertEquals(1.493729322, tally.getConfidenceInterval(0.95)[0], 1E-05);
        assertEquals(1.506270678, tally.getConfidenceInterval(0.95)[1], 1E-05);

        assertEquals(1.304003602, tally.getConfidenceInterval(0.05, ConfidenceInterval.BOTH_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.695996398, tally.getConfidenceInterval(0.05, ConfidenceInterval.BOTH_SIDE_CONFIDENCE)[1], 1E-05);
        assertEquals(1.432551025, tally.getConfidenceInterval(0.50, ConfidenceInterval.BOTH_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.567448975, tally.getConfidenceInterval(0.50, ConfidenceInterval.BOTH_SIDE_CONFIDENCE)[1], 1E-05);
        assertEquals(1.493729322, tally.getConfidenceInterval(0.95, ConfidenceInterval.BOTH_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.506270678, tally.getConfidenceInterval(0.95, ConfidenceInterval.BOTH_SIDE_CONFIDENCE)[1], 1E-05);

        assertEquals(1.304003602, tally.getConfidenceInterval(0.025, ConfidenceInterval.LEFT_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.500000000, tally.getConfidenceInterval(0.025, ConfidenceInterval.LEFT_SIDE_CONFIDENCE)[1], 1E-05);
        assertEquals(1.432551025, tally.getConfidenceInterval(0.25, ConfidenceInterval.LEFT_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.500000000, tally.getConfidenceInterval(0.25, ConfidenceInterval.LEFT_SIDE_CONFIDENCE)[1], 1E-05);
        assertEquals(1.474665290, tally.getConfidenceInterval(0.40, ConfidenceInterval.LEFT_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.500000000, tally.getConfidenceInterval(0.40, ConfidenceInterval.LEFT_SIDE_CONFIDENCE)[1], 1E-05);

        assertEquals(1.500000000, tally.getConfidenceInterval(0.025, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.695996398, tally.getConfidenceInterval(0.025, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE)[1], 1E-05);
        assertEquals(1.500000000, tally.getConfidenceInterval(0.25, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.567448975, tally.getConfidenceInterval(0.25, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE)[1], 1E-05);
        assertEquals(1.500000000, tally.getConfidenceInterval(0.40, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE)[0], 1E-05);
        assertEquals(1.525334710, tally.getConfidenceInterval(0.40, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE)[1], 1E-05);

        // we check the input of the confidence interval
        Try.testFail(() -> tally.getConfidenceInterval(0.95, null), "null is not defined as side of confidence level",
                NullPointerException.class);
        Try.testFail(() -> tally.getConfidenceInterval(-0.95), "should have reacted on wrong confidence level -0.95",
                IllegalArgumentException.class);
        Try.testFail(() -> tally.getConfidenceInterval(1.14), "should have reacted on wrong confidence level 1.14",
                IllegalArgumentException.class);

        assertTrue(Math.abs(tally.getSampleMean() - 1.5) < 10E-6);

        // Let's compute the standard deviation
        double varianceAccumulator = 0;
        for (int i = 0; i < 11; i++)
        {
            varianceAccumulator = Math.pow(1.5 - (1.0 + i / 10.0), 2) + varianceAccumulator;
        }

        assertEquals(varianceAccumulator / 10.0, tally.getSampleVariance(), 1.0E-6);
        assertEquals(Math.sqrt(varianceAccumulator / 10.0), tally.getSampleStDev(), 1.0E-6);

        assertEquals(varianceAccumulator / 11.0, tally.getPopulationVariance(), 1.0E-6);
        assertEquals(Math.sqrt(varianceAccumulator / 11.0), tally.getPopulationStDev(), 1.0E-6);

        // test confidence interval failure on uninitialized tally
        Tally t = new Tally("unused");
        assertNull(t.getConfidenceInterval(0.95));
        t.register(1.0);
        assertNull(t.getConfidenceInterval(0.95));
    }

    /**
     * Test the kurtosis example from https://en.wikipedia.org/wiki/Kurtosis where we assumed that the example for sample
     * kurtosis actually calculates population kurtosis (!).
     */
    @Test
    public void testKurtWikipedia()
    {
        Tally tally = new Tally("Wikipedia");
        tally.register(0, 3, 4, 1, 2, 3, 0, 2, 1, 3, 2, 0, 2, 2, 3, 2, 5, 2, 3, 999);
        assertEquals(18.05, tally.getPopulationKurtosis(), 0.01);
        assertEquals(15.05, tally.getPopulationExcessKurtosis(), 0.01);
    }

    /**
     * Test skewness and kurtosis based on two Excel samples.
     */
    @Test
    public void testSkewKurtExcel()
    {
        Tally tally1 = new Tally("Excel1");
        tally1.register(1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2);
        assertEquals(-1.2, tally1.getSampleExcessKurtosis(), 0.01);
        Tally tally2 = new Tally("Excel2");
        tally2.register(2, 4, 6, 3, 2, 1, 2, 3, 4, 5, 9);
        assertEquals(3.7272, tally2.getPopulationMean(), 0.01);
        assertEquals(4.7438, tally2.getPopulationVariance(), 0.01);
        assertEquals(5.2182, tally2.getSampleVariance(), 0.01);
        assertEquals(2.1780, tally2.getPopulationStDev(), 0.01);
        assertEquals(2.2843, tally2.getSampleStDev(), 0.01);
        assertEquals(1.0903, tally2.getPopulationSkewness(), 0.01);
        assertEquals(1.2706, tally2.getSampleSkewness(), 0.01);
        assertEquals(1.7908, tally2.getSampleExcessKurtosis(), 0.01);
    }

    /**
     * Test Tally with the NoStorageAccumulator.
     */
    @Test
    public void testNoStorageAccumulator()
    {
        Tally tally = new Tally("test with the NoStorageAccumulator", new NoStorageAccumulator());
        assertTrue(Double.isNaN(tally.getSampleMean()), "mean of no data is NaN");
        Try.testFail(() -> tally.getQuantile(0.5), "getQuantile of no data should have resulted in an IllegalArgumentException",
                IllegalArgumentException.class);
        tally.register(90.0);
        assertEquals(90.0, tally.getSampleMean(), 0, "mean of one value is that value");
        Try.testFail(() -> tally.getQuantile(0.5),
                "getQuantile of one value should have resulted in an IllegalArgumentException", IllegalArgumentException.class);

        tally.register(110.0);
        assertEquals(100.0, tally.getSampleMean(), 0, "mean of two value");
        assertEquals(100.0, tally.getQuantile(0.5), 0, "50% quantile");
        /*-
        double sigma = tally.getSampleStDev();
        double mu = tally.getSampleMean();
        // For loop below makes painfully clear where the getQuantile method fails
        // Values are from last table in https://en.wikipedia.org/wiki/Standard_normal_table
        for (double probability : new double[] { 1 - 5.00000E-1, 1 - 1.58655E-1, 1 - 2.27501E-2, 1 - 1.34990E-3, 1 - 3.16712E-5,
                1 - 2.86652E-7, 1 - 9.86588E-10, 1 - 1.27981E-12, 1 - 6.22096E-16, 1 - 1.12859E-19, 1 - 7.61985E-24 })
        {
            double x = tally.getQuantile(probability);
            System.out.println(String.format("probability=%19.16f 1-probability=%19.16f, x=%19.14f, sigmaCount=%19.16f",
                    probability, 1 - probability, x, (x - mu) / sigma));
        }
        // Output shows that the inverse cumulative probability function works fine up to about 8 sigma
         */

        assertEquals(1, DistNormalTable.getInverseCumulativeProbability(0, 1, 0.84), 0.01, "84% is about one sigma");
        assertEquals(-1, DistNormalTable.getInverseCumulativeProbability(0, 1, 0.16), 0.01, "16% is about minus one sigma");

        // Test for the problem that Peter Knoppers had in Tritapt where really small rounding errors caused sqrt(-1e-14).
        double value = 166.0 / 25.0;
        tally.initialize();
        tally.register(value);
        tally.register(value);
        tally.register(value);
        tally.register(value);
        tally.initialize();
        // Throw a lot of pseudo-randomly normally distributed values in and see if the expected mean and stddev come out
        double mean = 123.456;
        double stddev = 234.567;
        Random random = new Random(123456);
        for (int sample = 0; sample < 10000; sample++)
        {
            value = generateGaussianNoise(mean, stddev, random);
            tally.register(value);
        }
        assertEquals(mean, tally.getSampleMean(), stddev / 10, "mean should approximately match");
        assertEquals(stddev, tally.getSampleStDev(), stddev / 10, "stddev should approximately match");
    }

    /**
     * Test the FullStorageAccumulator.
     */
    @Test
    public void testFullStorageAccumulator()
    {
        Tally tally = new Tally("Tally for FullStorageAccumulator test", new FullStorageAccumulator());
        // Insert values from 0.0 .. 100.0 (step 1.0)
        for (int step = 0; step <= 100; step++)
        {
            tally.register(1.0 * step);
        }
        for (double probability : new double[] {0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0})
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            assertEquals(expected, got, 0.00001, "quantile should match");
        }
        Try.testFail(() -> tally.getQuantile(-0.01), "negative probability should have thrown an exception",
                IllegalArgumentException.class);
        Try.testFail(() -> tally.getQuantile(1.01), "Probability > 1 should have thrown an exception",
                IllegalArgumentException.class);

        assertTrue(new FullStorageAccumulator().toString().startsWith("FullStorageAccumulator"),
                "toString returns something descriptive");
    }

    /**
     * Generate normally distributed values. Derived from https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
     * @param mu mean
     * @param sigma standard deviation
     * @param random entropy source
     * @return one pseudo random value
     */
    double generateGaussianNoise(final double mu, final double sigma, final Random random)
    {
        final double epsilon = Double.MIN_VALUE;
        final double twoPi = Math.PI * 2;

        double u1, u2;
        do
        {
            u1 = random.nextDouble();
            u2 = random.nextDouble();
        }
        while (u1 <= epsilon);

        return mu + sigma * Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(twoPi * u2);
    }

    /**
     * Test the T-Digest accumulator.
     */
    @Test
    public void testTDigestAccumulator()
    {
        Tally tally = new Tally("Tally for TDigestAccumulator test", new TDigestAccumulator());
        // Insert values from 0.0 .. 100.0 (step 1.0)
        for (int step = 0; step <= 100; step++)
        {
            tally.register(1.0 * step);
        }
        for (double probability : new double[] {0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0})
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            // System.out.println(String.format("probability %10.8f, expected %10.8f, got %10.8f", probability, expected, got));
            assertEquals(expected, got, 1.0, "quantile should match"); // With 100 bins the error should be below 1%
        }
        // https://en.wikipedia.org/wiki/Uniform_distribution_(continuous)
        assertEquals(0, tally.getSampleSkewness(), 0.0001, "sample skewness should be 0");
        assertEquals(0, tally.getPopulationSkewness(), 0.0001, "skewness should be 0");
        assertEquals(-1.2, tally.getSampleExcessKurtosis(), 0.0001, "sample excess kurtosis should be -1.2");
        assertEquals(-1.2, tally.getPopulationExcessKurtosis(), 0.01, "population excess kurtosis should be -1.2");
        // System.out.println(String.format("%d uniformly distributed values: skewness %20.15f, kurtosis %20.15f", tally.getN(),
        // tally.getSampleSkewness(), tally.getSampleKurtosis()));
        tally.initialize();
        // Insert values from 0.0 .. 100.0 (step 0.0001)
        for (int step = 0; step <= 1000000; step++)
        {
            tally.register(0.0001 * step);
        }
        for (double probability : new double[] {0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0})
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            // System.out.println(String.format("probability %10.8f, expected %10.8f, got %10.8f", probability, expected, got));
            assertEquals(expected, got, 0.01, "quantile should match"); // Uniformly distributed data yields very good estimates
        }
        // System.out.println(String.format("%d uniformly distributed values: skewness %20.15f, kurtosis %20.15f", tally.getN(),
        // tally.getSampleSkewness(), tally.getSampleKurtosis()));
        assertEquals(0, tally.getSampleSkewness(), 0.0001, "sample skewness should be 0");
        assertEquals(0, tally.getPopulationSkewness(), 0.0001, "skewness should be 0");
        assertEquals(-1.2, tally.getSampleExcessKurtosis(), 0.0001, "sample kurtosis should be -1.2");
        assertEquals(-1.2, tally.getPopulationExcessKurtosis(), 0.0001, "population excess kurtosis should be -1.2");
        tally = new Tally("Tally for TDigestAccumulator test", new TDigestAccumulator(4));
        // Insert values from 0.0 .. 100.0 (step 0.0001)
        for (int step = 0; step <= 1000000; step++)
        {
            tally.register(0.0001 * step);
        }
        for (double probability : new double[] {0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0})
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            // System.out.println(String.format("probability %10.8f, expected %10.8f, got %10.8f", probability, expected, got));
            assertEquals(expected, got, 0.01, "quantile should match"); // Uniformly distributed data yields very good estimates
        }

        final Tally tally1 = tally;
        Try.testFail(() -> tally1.getQuantile(-0.01), "negative probability should have thrown an exception",
                IllegalArgumentException.class);
        Try.testFail(() -> tally1.getQuantile(1.01), "Probability > 1 should have thrown an exception",
                IllegalArgumentException.class);

        assertTrue(new TDigestAccumulator().toString().startsWith("TDigestAccumulator"),
                "toString returns something descriptive");
        tally = new Tally("Tally for TDigestAccumulator test", new TDigestAccumulator());
        // Throw a lot of pseudo-randomly normally distributed values in and see if the accumulator gets a good approximation
        // of the distribution
        double mean = 123.456;
        double stddev = 234.567;
        Random random = new Random(123456);
        for (int sample = 0; sample < 10000; sample++)
        {
            double value = generateGaussianNoise(mean, stddev, random);
            tally.register(value);
        }
        // Test that tally reports cumulative probabilities that roughly follow that of normal distribution
        for (double probability : new double[] {0.01, 0.1, 0.25, 0.49, 0.5, 0.51, 0.75, 0.9, 0.99})
        {
            double expected = DistNormalTable.getInverseCumulativeProbability(mean, stddev, probability);
            double got = tally.getQuantile(probability);
            double margin = mean / 10 / Math.sqrt(Math.min(probability, 1 - probability));
            // System.out.println(String.format("probability %12.7f, expected %12.7f, reasonable margin %12.7f, got %12.7f",
            // probability, expected, margin, got));
            assertEquals(expected, got, margin, "quantile should match");
        }
    }

    /**
     * Test skewness and kurtosis. Test data from http://web.ipac.caltech.edu/staff/fmasci/home/astro_refs/SkewStatSignif.pdf
     */
    @Test
    public void testSkewnessAndKurtosis()
    {
        List<Double> testValues = new ArrayList<>();
        for (int i = 5; --i >= 0;)
        {
            testValues.add(61.0);
        }
        for (int i = 18; --i >= 0;)
        {
            testValues.add(64.0);
        }
        for (int i = 42; --i >= 0;)
        {
            testValues.add(67.0);
        }
        for (int i = 27; --i >= 0;)
        {
            testValues.add(70.0);
        }
        for (int i = 8; --i >= 0;)
        {
            testValues.add(73.0);
        }
        int count = testValues.size();
        Tally tally = new Tally("");
        for (double value : testValues)
        {
            tally.register(value);
        }
        // System.out.println(tally);
        // System.out.println(String.format("count %d mean %20.15f variance %20.15f skew %20.15f kurtosis %20.15f", count,
        // tally.getSampleMean(), tally.getSampleVariance(), tally.getSampleSkewness(), tally.getSampleKurtosis()));
        // Do the math the "classic" way (i.e. using two passes; the first pass gets the mean)
        double mean = tally.getSampleMean();
        double m2 = 0;
        double m3 = 0;
        double m4 = 0;
        for (double value : testValues)
        {
            double delta = value - mean;
            m2 += delta * delta;
            m3 += delta * delta * delta;
            m4 += delta * delta * delta * delta;
        }
        m2 /= count;
        m3 /= count;
        m4 /= count;
        double g1 = m3 / Math.pow(m2, 1.5);
        double sg1 = g1 * Math.sqrt(count * (count - 1)) / (count - 2);
        double a4 = m4 / m2 / m2;
        // System.out.println(String.format("m2 %20.15f, m3 %20.15f, m4 %20.15f", m2, m3, m4));
        double g2 = a4 - 3;
        double sg2 = 1.0 * (count - 1) / (count - 2) / (count - 3) * ((count + 1) * g2 + 6);
        // System.out.println(String.format("g1 %20.15f sampleSkewness %20.15f, a4 %20.15f g2 %20.15f sampleKurtosis %20.15f",
        // g1, sg1, a4, g2, sg2));
        assertEquals(sg1, tally.getSampleSkewness(), 0.0001, "skew should match");
        assertEquals(sg2, tally.getSampleExcessKurtosis(), 0.0001, "kurtosis should match");
    }

}
