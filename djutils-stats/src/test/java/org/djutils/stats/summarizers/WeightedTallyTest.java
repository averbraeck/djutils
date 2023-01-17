package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.djutils.exceptions.Try;
import org.junit.Test;

/**
 * The WeightedTallyTest tests the WeightedTally.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class WeightedTallyTest
{
    /** Test the WeightedTally. */
    @Test
    public void testWeightedTally()
    {
        String description = "THIS WEIGHTED TALLY IS TESTED";
        WeightedTally wt = new WeightedTally(description);
        assertEquals(description, wt.getDescription());

        assertTrue(wt.toString().startsWith("WeightedTally"));
        // check the description
        assertTrue(wt.toString().contains(description));

        // now we check the initial values
        assertTrue(Double.isNaN(wt.getMin()));
        assertTrue(Double.isNaN(wt.getMax()));
        assertTrue(Double.isNaN(wt.getWeightedSampleMean()));
        assertTrue(Double.isNaN(wt.getWeightedPopulationMean()));
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        assertEquals(0.0, wt.getWeightedSum(), 0.0);
        assertEquals(0L, wt.getN());

        wt.register(0.1, 1.1);
        assertEquals(1.1, wt.getWeightedSampleMean(), 0.000001);
        assertEquals(1.1, wt.getWeightedPopulationMean(), 0.000001);
        assertTrue(Double.isNaN(wt.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(wt.getWeightedSampleStDev()));
        wt.register(0.1, 1.2);
        assertFalse(Double.isNaN(wt.getWeightedSampleVariance()));
        assertFalse(Double.isNaN(wt.getWeightedSampleStDev()));
        wt.register(0.1, 1.3);
        wt.register(0.1, 1.4);
        wt.register(0.1, 1.5);
        wt.register(0.1, 1.6);
        wt.register(0.1, 1.7);
        wt.register(0.1, 1.8);
        wt.register(0.1, 1.9);
        wt.register(0.1, 2.0);
        wt.register(0.1, 1.0);

        // check the report functions
        int len = WeightedTally.reportFooter().length();
        assertEquals(len, WeightedTally.reportHeader().split("\\R")[0].length());
        assertEquals(len, WeightedTally.reportHeader().split("\\R")[1].length());
        assertEquals(len, WeightedTally.reportHeader().split("\\R")[2].length());
        assertEquals(len, wt.reportLine().length());
        assertEquals(len, new WeightedTally("empty tally").reportLine().length());
        WeightedTally tallyX = new WeightedTally("1 value");
        tallyX.register(2.0, 1E10);
        assertEquals(len, tallyX.reportLine().length());
        WeightedTally tallyY = new WeightedTally("1 very small value");
        tallyY.register(0.1, -0.00000000002);
        assertEquals(len, tallyY.reportLine().length());

        // Now we check the WeightedTally
        assertEquals(2.0, wt.getMax(), 1.0E-6);
        assertEquals(1.0, wt.getMin(), 1.0E-6);
        assertEquals(11, wt.getN());
        assertEquals(1.5 * 0.1 * 11, wt.getWeightedSum(), 1.0E-6);
        assertEquals(1.5, wt.getWeightedSampleMean(), 1.0E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance += Math.pow(1.5 - (1.0 + i / 10.0), 2);
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, wt.getWeightedSampleVariance(), 1.0E-6);
        assertEquals(stDev, wt.getWeightedSampleStDev(), 1.0E-6);

        Try.testFail(() -> wt.register(-0.1, 123.456), "negative weight should have thrown an exception",
                IllegalArgumentException.class);
    }

    /** Test the WeightedTally on a simple example. */
    @Test
    public void testWeightedTallySimple()
    {
        // From: https://sciencing.com/calculate-time-decimals-5962681.html
        WeightedTally wt = new WeightedTally("simple WeightedTally statistic");
        wt.initialize();
        wt.register(13.0, 86.0);
        wt.register(23.0, 26.0);
        wt.register(4.0, 0.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN());

        // When we have observations with duration 0, we should get the same answers
        wt = new WeightedTally("simple WeightedTally statistic");
        wt.initialize();
        wt.register(13.0, 86.0);
        wt.register(0.0, 86.0);
        wt.register(23.0, 26.0);
        wt.register(4.0, 0.0);
        wt.register(0.0, 0.0);

        assertEquals(1716.0, wt.getWeightedSum(), 0.001);
        assertEquals(42.9, wt.getWeightedSampleMean(), 0.001);
        assertEquals(3, wt.getN()); // non-zero values only

        // Example from NIST: https://www.itl.nist.gov/div898/software/dataplot/refman2/ch2/weightsd.pdf
        wt = new WeightedTally("NIST");
        wt.register(1, 2);
        wt.register(1, 3);
        wt.register(0, 5);
        wt.register(0, 7);
        wt.register(4, 11);
        wt.register(1, 13);
        wt.register(2, 17);
        wt.register(1, 19);
        wt.register(0, 23);

        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedSampleMean(), 0.001);
        assertEquals((2 + 3 + 4 * 11 + 13 + 2 * 17 + 19) / 10.0, wt.getWeightedPopulationMean(), 0.001);

        assertEquals(5.82, wt.getWeightedSampleStDev(), 0.01);
        // System.out.println("sample variance " + wt.getWeightedSampleVariance());
        // System.out.println("sample stdev " + wt.getWeightedSampleStDev());
        // System.out.println("sample stdev^2 " + wt.getWeightedSampleStDev() * wt.getWeightedSampleStDev());
        // System.out.println(" variance " + wt.getWeightedVariance());
        // System.out.println(" stdev " + wt.getWeightedStDev());
        // System.out.println(" stdev^2 " + wt.getWeightedStDev() * wt.getWeightedStDev());
        assertEquals(5.32, wt.getWeightedPopulationStDev(), 0.01); // Computed with Excel sheet
        assertEquals(28.25, wt.getWeightedPopulationVariance(), 0.01); // Computed with Excel sheet
    }
}
