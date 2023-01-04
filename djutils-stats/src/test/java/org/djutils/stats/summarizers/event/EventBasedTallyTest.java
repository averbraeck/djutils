package org.djutils.stats.summarizers.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.djutils.event.EventType;
import org.djutils.metadata.MetaData;
import org.djutils.stats.ConfidenceInterval;
import org.djutils.stats.DistNormalTable;
import org.djutils.stats.summarizers.quantileaccumulator.FullStorageAccumulator;
import org.djutils.stats.summarizers.quantileaccumulator.NoStorageAccumulator;
import org.junit.Test;

/**
 * The TallyTest test the tally.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class EventBasedTallyTest
{
    /** an event to fire. */
    private static final EventType VALUE_EVENT = new EventType("VALUE_EVENT", MetaData.NO_META_DATA);

    /** Test the event based tally. */
    @SuppressWarnings("checkstyle:methodlength")
    @Test
    public void testEventBasedTally()
    {
        String description = "THIS TALLY IS TESTED";
        EventBasedTally tally = new EventBasedTally(description);

        // check the description
        assertTrue(tally.toString().contains(description));
        assertEquals(description, tally.getDescription());
        assertTrue(tally.toString().startsWith("EventBasedTally"));

        // now we check the initial values
        assertTrue(Double.valueOf(tally.getMin()).isNaN());
        assertTrue(Double.valueOf(tally.getMax()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleMean()).isNaN());
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

        // We first fire a wrong event
        try
        {
            tally.notify(new Event(VALUE_EVENT, "ERROR"));
            fail("tally should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            tally.notify(new Event(VALUE_EVENT, 1.1));
            assertFalse("mean is now available", Double.isNaN(tally.getSampleMean()));
            assertTrue("sample variance is not available", Double.isNaN(tally.getSampleVariance()));
            assertFalse("variance is not available", Double.isNaN(tally.getPopulationVariance()));
            assertTrue("skewness is not available", Double.isNaN(tally.getPopulationSkewness()));
            tally.notify(new Event(VALUE_EVENT, 1.2));
            assertFalse("sample variance is now available", Double.isNaN(tally.getSampleVariance()));
            assertTrue("sample skewness is not available", Double.isNaN(tally.getSampleSkewness()));
            assertFalse("skewness is available", Double.isNaN(tally.getPopulationSkewness()));
            assertTrue("kurtosis is not available", Double.isNaN(tally.getPopulationKurtosis()));
            tally.notify(new Event(VALUE_EVENT, 1.3));
            assertFalse("skewness is now available", Double.isNaN(tally.getSampleSkewness()));
            assertFalse("kurtosis is now available", Double.isNaN(tally.getPopulationKurtosis()));
            assertTrue("sample kurtosis is not available", Double.isNaN(tally.getSampleKurtosis()));
            tally.notify(new Event(VALUE_EVENT, 1.4));
            assertFalse("sample kurtosis is now available", Double.isNaN(tally.getSampleKurtosis()));
            tally.notify(new Event(VALUE_EVENT, 1.5));
            tally.notify(new Event(VALUE_EVENT, 1.6));
            tally.notify(new Event(VALUE_EVENT, 1.7));
            tally.notify(new Event(VALUE_EVENT, 1.8));
            tally.notify(new Event(VALUE_EVENT, 1.9));
            tally.notify(new Event(VALUE_EVENT, 2.0));
            tally.notify(new Event(VALUE_EVENT, 1.0));
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
        assertEquals(0.331662, tally.getSampleStDev(), 1.0E-6);

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
        double varianceAccumulator = 0;
        for (int i = 0; i < 11; i++)
        {
            varianceAccumulator = Math.pow(1.5 - (1.0 + i / 10.0), 2) + varianceAccumulator;
        }

        assertEquals(varianceAccumulator / 10.0, tally.getSampleVariance(), 1.0E-6);
        assertEquals(Math.sqrt(varianceAccumulator / 10.0), tally.getSampleStDev(), 1.0E-6);

        assertEquals(varianceAccumulator / 11.0, tally.getPopulationVariance(), 1.0E-6);
        assertEquals(Math.sqrt(varianceAccumulator / 11.0), tally.getPopulationStDev(), 1.0E-6);
    }

    /**
     * Test produced events by EventBasedTally.
     */
    @Test
    public void testTallyEventProduction()
    {
        EventBasedTally tally = new EventBasedTally("testTally");
        ObservationEventListener oel = new ObservationEventListener();
        tally.addListener(oel, StatisticsEvents.OBSERVATION_ADDED_EVENT);
        assertEquals(0, oel.getObservationEvents());

        EventType[] types = new EventType[] {StatisticsEvents.N_EVENT, StatisticsEvents.MIN_EVENT, StatisticsEvents.MAX_EVENT,
                StatisticsEvents.POPULATION_MEAN_EVENT, StatisticsEvents.POPULATION_VARIANCE_EVENT,
                StatisticsEvents.POPULATION_SKEWNESS_EVENT, StatisticsEvents.POPULATION_KURTOSIS_EVENT,
                StatisticsEvents.POPULATION_STDEV_EVENT, StatisticsEvents.SUM_EVENT, StatisticsEvents.SAMPLE_MEAN_EVENT,
                StatisticsEvents.SAMPLE_VARIANCE_EVENT, StatisticsEvents.SAMPLE_SKEWNESS_EVENT,
                StatisticsEvents.SAMPLE_KURTOSIS_EVENT, StatisticsEvents.SAMPLE_STDEV_EVENT};
        LoggingEventListener[] listeners = new LoggingEventListener[types.length];
        for (int i = 0; i < types.length; i++)
        {
            listeners[i] = new LoggingEventListener();
            tally.addListener(listeners[i], types[i]);
        }

        for (int i = 1; i <= 10; i++)
        {
            tally.register(10 * i);
        }

        assertEquals(10, oel.getObservationEvents());

        // values from: https://atozmath.com/StatsUG.aspx
        Object[] expectedValues =
                new Object[] {10L, 10.0, 100.0, 55.0, 825.0, 0.0, 1.7758, 28.7228, 550.0, 55.0, 916.6667, 0.0, 1.5982, 30.2765};
        for (int i = 0; i < types.length; i++)
        {
            assertEquals("Number of events for listener " + types[i], 10, listeners[i].getNumberOfEvents());
            assertEquals("Event type for listener " + types[i], types[i], listeners[i].getLastEvent().getType());
            if (expectedValues[i] instanceof Long)
            {
                assertEquals("Final value for listener " + types[i], expectedValues[i],
                        listeners[i].getLastEvent().getContent());
            }
            else
            {
                double e = ((Double) expectedValues[i]).doubleValue();
                double c = ((Double) listeners[i].getLastEvent().getContent()).doubleValue();
                assertEquals("Final value for listener " + types[i], e, c, 0.001);
            }
        }
    }

    /**
     * Test EventBasedTally with the NoStorageAccumulator.
     */
    @Test
    public void testNoStorageAccumulator()
    {
        EventBasedTally tally = new EventBasedTally("test with the NoStorageAccumulator", new NoStorageAccumulator());
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

        tally.notify(new Event(VALUE_EVENT, 90.0));
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

        tally.notify(new Event(VALUE_EVENT, 110.0));
        assertEquals("mean of two value", 100.0, tally.getSampleMean(), 0);
        assertEquals("50% quantile", 100.0, tally.getQuantile(0.5), 0);

        /*-
        double sigma = tally.getSampleStDev();
        double mu = tally.getSampleMean();
        // For loop below makes painfully clear where the getQuantile method fails
        // Values are from last table in https://en.wikipedia.org/wiki/Standard_normal_table
        for (double probability : new double[] {1 - 5.00000E-1, 1 - 1.58655E-1, 1 - 2.27501E-2, 1 - 1.34990E-3, 1 - 3.16712E-5,
                1 - 2.86652E-7, 1 - 9.86588E-10, 1 - 1.27981E-12, 1 - 6.22096E-16, 1 - 1.12859E-19, 1 - 7.61985E-24})
        {
            double x = tally.getQuantile(probability);
            System.out.println(String.format("probability=%19.16f 1-probability=%19.16f, x=%19.14f, sigmaCount=%19.16f",
                    probability, 1 - probability, x, (x - mu) / sigma));
        }
        // Output shows that the inverse cumulative probability function works fine up to about 8 sigma
         */

        assertEquals("84% is about one sigma", 1, DistNormalTable.getInverseCumulativeProbability(0, 1, 0.84), 0.01);
        assertEquals("16% is about minus one sigma", -1, DistNormalTable.getInverseCumulativeProbability(0, 1, 0.16), 0.01);

        // Test for the problem that Peter Knoppers had in Tritapt where really small rounding errors caused sqrt(-1e-14).
        double value = 166.0 / 25.0;
        tally.initialize();
        tally.notify(new Event(VALUE_EVENT, value));
        tally.notify(new Event(VALUE_EVENT, value));
        tally.notify(new Event(VALUE_EVENT, value));
        tally.notify(new Event(VALUE_EVENT, value));
        tally.initialize();
        // Throw a lot of pseudo-randomly normally distributed values in and see if the expected mean and stddev come out
        double mean = 123.456;
        double stddev = 234.567;
        Random random = new Random(123456);
        for (int sample = 0; sample < 10000; sample++)
        {
            value = generateGaussianNoise(mean, stddev, random);
            tally.notify(new Event(VALUE_EVENT, value));
        }
        assertEquals("mean should approximately match", mean, tally.getSampleMean(), stddev / 10);
        assertEquals("stddev should approximately match", stddev, tally.getSampleStDev(), stddev / 10);
    }

    /**
     * Test the Event based tally with the FullStorageAccumulator.
     */
    @Test
    public void testFullStorageAccumulator()
    {
        EventBasedTally tally =
                new EventBasedTally("EventBasedTally for FullStorageAccumulator test", new FullStorageAccumulator());
        // Insert values from 0.0 .. 100.0 (step 1.0)
        for (int step = 0; step <= 100; step++)
        {
            tally.notify(new Event(VALUE_EVENT, 1.0 * step));
        }
        for (double probability : new double[] {0.0, 0.01, 0.1, 0.49, 0.5, 0.51, 0.9, 0.99, 1.0})
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

    /** The listener that counts the OBSERVATION_ADDED_EVENT events and checks correctness. */
    class ObservationEventListener implements EventListener
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** counter for the event. */
        private int observationEvents = 0;

        @Override
        public void notify(final Event event)
        {
            assertTrue(event.getType().equals(StatisticsEvents.OBSERVATION_ADDED_EVENT));
            assertTrue("Content of the event has a wrong type, not DOuble: " + event.getContent().getClass(),
                    event.getContent() instanceof Double);
            this.observationEvents++;
        }

        /**
         * @return countEvents
         */
        public int getObservationEvents()
        {
            return this.observationEvents;
        }
    }

}
