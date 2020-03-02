package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.djutils.stats.summarizers.quantileaccumulator.FullStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.TDigestAccumulator;
import org.junit.Test;

/**
 * The TallyTest test the tally.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    @Test
    public void testTally()
    {
        String description = "THIS TALLY IS TESTED";
        Tally tally = new Tally(description);

        // check the description
        assertTrue(tally.toString().contains(description));
        assertEquals(description, tally.getDescription());
        assertTrue(tally.toString().startsWith("Tally"));

        // now we check the initial values
        assertTrue(Double.valueOf(tally.getMin()).isNaN());
        assertTrue(Double.valueOf(tally.getMax()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(tally.getStdDev()).isNaN());
        assertEquals(0, tally.getSum(), 0);
        assertEquals(0L, tally.getN());
        assertNull(tally.getConfidenceInterval(0.95));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.LEFT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.RIGHT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.BOTH_SIDE_CONFIDENCE));

        // Now we ingest some values
        try
        {
            tally.ingest(1.1);
            tally.ingest(1.2);
            tally.ingest(1.3);
            tally.ingest(1.4);
            tally.ingest(1.5);
            tally.ingest(1.6);
            tally.ingest(1.7);
            tally.ingest(1.8);
            tally.ingest(1.9);
            tally.ingest(2.0);
            tally.ingest(1.0);
        }
        catch (Exception exception)
        {
            fail(exception.getMessage());
        }

        // Now we check the tally
        assertEquals(2.0, tally.getMax(), 1.0E-6);
        assertEquals(1.0, tally.getMin(), 1.0E-6);
        assertEquals(11, tally.getN());
        assertEquals(16.5, tally.getSum(), 1.0E-6);
        assertEquals(1.5, tally.getSampleMean(), 1.0E-6);
        assertEquals(0.110000, tally.getSampleVariance(), 1.0E-6);
        assertEquals(0.331662, tally.getStdDev(), 1.0E-6);

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
        try
        {
            tally.getConfidenceInterval(0.95, null);
            fail("null is not defined as side of confidence level");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(NullPointerException.class));
        }
        try
        {
            assertNull(tally.getConfidenceInterval(-0.95));
            fail("should have reacted on wrong confidence level -0.95");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }
        try
        {
            assertNull(tally.getConfidenceInterval(1.14));
            fail("should have reacted on wrong confidence level 1.14");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }

        assertTrue(Math.abs(tally.getSampleMean() - 1.5) < 10E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, tally.getSampleVariance(), 1.0E-6);
        assertEquals(stDev, tally.getStdDev(), 1.0E-6);

        // test confidence interval failure on uninitialized tally
        Tally t = new Tally("unused");
        assertNull(t.getConfidenceInterval(0.95));
        t.ingest(1.0);
        assertNull(t.getConfidenceInterval(0.95));

    }

    /**
     * Test Tally with the NoStorageAccumulator.
     */
    @Test
    public void testNoStorageAccumulator()
    {
        Tally tally = new Tally("test with the NoStorageAccumulator", new NoStorageAccumulator());
        assertTrue("mean of no data is NaN", Double.isNaN(tally.getSampleMean()));
        try
        {
            tally.getQuantile(0.5);
            fail("getQuantile of no data should have resulted in an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        tally.ingest(90.0);
        assertEquals("mean of one value is that value", 90.0, tally.getSampleMean(), 0);
        try
        {
            tally.getQuantile(0.5);
            fail("getQuantile of one value should have resulted in an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        tally.ingest(110.0);
        assertEquals("mean of two value", 100.0, tally.getSampleMean(), 0);
        assertEquals("50% quantile", 100.0, tally.getQuantile(0.5), 0);
        double sigma = tally.getStdDev();
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

        assertEquals("84% is about one sigma", 1, DistNormalTable.getInverseCumulativeProbability(0, 1, 0.84), 0.01);
        assertEquals("16% is about minus one sigma", -1, DistNormalTable.getInverseCumulativeProbability(0, 1, 0.16), 0.01);

        // Test for the problem that Peter Knoppers had in Tritapt where really small rounding errors caused sqrt(-1e-14).
        double value = 166.0 / 25.0;
        tally.initialize();
        tally.ingest(value);
        tally.ingest(value);
        tally.ingest(value);
        tally.ingest(value);
        tally.initialize();
        // Throw a lot of pseudo-randomly normally distributed values in and see if the expected mean and stddev come out
        double mean = 123.456;
        double stddev = 234.567;
        Random random = new Random(123456);
        for (int sample = 0; sample < 10000; sample++)
        {
            value = generateGaussianNoise(mean, stddev, random);
            tally.ingest(value);
        }
        assertEquals("mean should approximately match", mean, tally.getSampleMean(), stddev / 10);
        assertEquals("stddev should approximately match", stddev, tally.getStdDev(), stddev / 10);
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
            tally.ingest(1.0 * step);
        }
        for (double probability : new double[] { 0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0 })
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            assertEquals("quantile should match", expected, got, 0.00001);
        }
        try
        {
            tally.getQuantile(-0.01);
            fail("negative probability should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            tally.getQuantile(1.01);
            fail("Probability > 1 should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertTrue("toString returns something descriptive",
                new FullStorageAccumulator().toString().startsWith("FullStorageAccumulator"));
    }

    /**
     * Generate normally distributed values. Derived from https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
     * @param mu double; mean
     * @param sigma double; standard deviation
     * @param random Random; entropy source
     * @return double; one pseudo random value
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
            tally.ingest(1.0 * step);
        }
        for (double probability : new double[] { 0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0 })
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            System.out.println(String.format("probability %10.8f, expected %10.8f, got %10.8f", probability, expected, got));
            assertEquals("quantile should match", expected, got, 1.0); // With 100 bins the error should be below 1%
        }
        tally.initialize();
        // Insert values from 0.0 .. 100.0 (step 0.0001)
        for (int step = 0; step <= 1000000; step++)
        {
            tally.ingest(0.0001 * step);
        }
        for (double probability : new double[] { 0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0 })
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            System.out.println(String.format("probability %10.8f, expected %10.8f, got %10.8f", probability, expected, got));
            assertEquals("quantile should match", expected, got, 0.01); // Uniformly distributed data yields very good estimates
        }
        tally = new Tally("Tally for TDigestAccumulator test", new TDigestAccumulator(4));
        // Insert values from 0.0 .. 100.0 (step 0.0001)
        for (int step = 0; step <= 1000000; step++)
        {
            tally.ingest(0.0001 * step);
        }
        for (double probability : new double[] { 0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0 })
        {
            double expected = 100 * probability;
            double got = tally.getQuantile(probability);
            System.out.println(String.format("probability %10.8f, expected %10.8f, got %10.8f", probability, expected, got));
            assertEquals("quantile should match", expected, got, 0.01); // Uniformly distributed data yields very good estimates
        }
        try
        {
            tally.getQuantile(-0.01);
            fail("negative probability should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            tally.getQuantile(1.01);
            fail("Probability > 1 should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        assertTrue("toString returns something descriptive",
                new TDigestAccumulator().toString().startsWith("TDigestAccumulator"));
        tally = new Tally("Tally for TDigestAccumulator test", new TDigestAccumulator());
        // Throw a lot of pseudo-randomly normally distributed values in and see if the accumulator gets a good approximation
        // of the distribution
        double mean = 123.456;
        double stddev = 234.567;
        Random random = new Random(123456);
        for (int sample = 0; sample < 10000; sample++)
        {
            double value = generateGaussianNoise(mean, stddev, random);
            tally.ingest(value);
        }
        // Test that tally reports cumulative probabilities that roughly follow that of normal distribution
        for (double probability : new double[] { 0.01, 0.1, 0.25, 0.49, 0.5, 0.51, 0.75, 0.9, 0.99 })
        {
            double expected = DistNormalTable.getInverseCumulativeProbability(mean, stddev, probability);
            double got = tally.getQuantile(probability);
            double margin = mean / 10 / Math.sqrt(Math.min(probability, 1 - probability));
            System.out.println(String.format("probability %12.7f, expected %12.7f, reasonable margin %12.7f, got %12.7f",
                    probability, expected, margin, got));
            assertEquals("quantile should match", expected, got, margin);
        }
    }

}