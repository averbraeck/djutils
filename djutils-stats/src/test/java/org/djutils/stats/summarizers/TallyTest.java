package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
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
 * @since 1.5
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
        assertTrue(Double.valueOf(tally.getSum()).isNaN());
        assertEquals(Long.MIN_VALUE, tally.getN());
        assertNull(tally.getConfidenceInterval(0.95));
        assertNull(tally.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, Tally.RIGHT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, Tally.BOTH_SIDE_CONFIDENCE));

        // now we initialize the tally
        assertFalse(tally.isInitialized());
        tally.initialize();
        assertTrue(tally.isInitialized());

        // now we check wether all the properties are correct
        assertTrue(tally.getMin() == Double.MAX_VALUE);
        assertTrue(tally.getMax() == -Double.MAX_VALUE);
        assertTrue(Double.valueOf(tally.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(tally.getStdDev()).isNaN());
        assertEquals(0.0, tally.getSum(), 1.0E-6);
        assertEquals(0, tally.getN());
        assertNull(tally.getConfidenceInterval(0.95));
        assertNull(tally.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, Tally.RIGHT_SIDE_CONFIDENCE));
        assertNull(tally.getConfidenceInterval(0.95, Tally.BOTH_SIDE_CONFIDENCE));

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
        assertEquals(0.11, tally.getSampleVariance(), 1.0E-6);
        assertEquals(0.332, tally.getStdDev(), 1.0E-3);
        assertEquals(1.304, tally.getConfidenceInterval(0.05)[0], 1.0E-6);

        // we check the input of the confidence interval
        try
        {
            tally.getConfidenceInterval(0.95, (short) 14);
            fail("14 is not defined as side of confidence level");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
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
}
