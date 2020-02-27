package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.djutils.event.Event;
import org.djutils.stats.summarizers.quantileaccumulator.FullStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.junit.Test;

/**
 * The TallyTest test the tally.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
        assertEquals(description, tally.toString());
        assertEquals(description, tally.getDescription());

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

        // We first fire a wrong event
        try
        {
            tally.notify(new Event(null, "ERROR", "ERROR"));
            fail("tally should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.1)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.2)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.3)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.4)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.5)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.6)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.7)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.8)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.9)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(2.0)));
            tally.notify(new Event(null, "TallyTest", Double.valueOf(1.0)));
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
        assertEquals(1.304, tally.getConfidenceInterval(0.05)[0], 1E-03 /* FIXME: was 1.0E-6 */);

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
        System.out.println("sigma=" + tally.getStdDev() + " mu=" + mu);
        // For loop below makes painfully clear where the getQuantile method fails
        for (double symmetricProbability : new double[] { 0.0, 0.682689492137086, 0.954499736103642, 0.997300203936740,
                0.999936657516334, 0.999999426696856, 0.999999998026825, 0.999999999997440 })
        {
            double probability = symmetricProbability + (1.0 - symmetricProbability) / 2;
            double x = tally.getQuantile(probability);
            System.out.println(String.format("probability=%20.18f 1-probability=%20.18f, x=%20.16f, sigmaCount=%20.17f",
                    probability, 1 - probability, x, (x - mu) / sigma));
        }
        // Test for the problem that Peter Knoppers had in Tritapt where really small rounding errors caused sqrt(-1e-14).
        double value = 166.0 / 25.0;
        tally.initialize();
        tally.ingest(value);
        tally.ingest(value);
        tally.ingest(value);
        tally.ingest(value);
        System.out.println(tally.getStdDev());
        tally.initialize();
        // Throw a lot of pseudo-randomly normally distributed values in and see if the expected mean and stddev come out
        double mean = 123.456;
        double stddev = 234.567;
        Random random = new Random(123456);
        for (int sample = 0; sample < 10000; sample++)
        {
            value = generateGaussianNoise(mean, stddev, random);
            // System.out.println(value);
            tally.ingest(value);
        }
        System.out.println(String.format("mean in: %10.15f out %20.15f, stddev in %20.15f, out %20.15f", mean,
                tally.getSampleMean(), stddev, tally.getStdDev()));
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
            // System.out.println(String.format("probability %5.2f: expected %20.15f, got %20.15f", probability, expected,
            // got));
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
        final double two_pi = Math.PI * 2;

        double u1, u2;
        do
        {
            u1 = random.nextDouble();
            u2 = random.nextDouble();
        }
        while (u1 <= epsilon);

        return mu + sigma * Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(two_pi * u2);
    }

}
