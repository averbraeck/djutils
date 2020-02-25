package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.event.Event;
import org.djutils.event.TimedEvent;
import org.junit.Test;

/**
 * The PersistentTest test the persistent
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
public class PersistentTest
{
    /** Test the persistent. */
    @Test
    public void testPersistent()
    {
        String description = "THIS PERSISTENT IS TESTED";
        Persistent persistent = new Persistent(description);

        // check the description
        assertTrue(persistent.toString().equals(description));

        // now we check the initial values
        assertTrue(Double.valueOf(persistent.getMin()).isNaN());
        assertTrue(Double.valueOf(persistent.getMax()).isNaN());
        assertTrue(Double.valueOf(persistent.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(persistent.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(persistent.getStdDev()).isNaN());
        assertTrue(Double.valueOf(persistent.getSum()).isNaN());
        assertEquals(Long.MIN_VALUE, persistent.getN());
        assertNull(persistent.getConfidenceInterval(0.95));
        assertNull(persistent.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE));
        assertNull(persistent.getConfidenceInterval(0.95, Tally.RIGHT_SIDE_CONFIDENCE));
        assertNull(persistent.getConfidenceInterval(0.95, Tally.BOTH_SIDE_CONFIDENCE));

        // now we initialize the persistent
        assertFalse(persistent.isInitialized());
        persistent.initialize();
        assertTrue(persistent.isInitialized());

        // now we check wether all the properties are correct
        assertTrue(persistent.getMin() == Double.MAX_VALUE);
        assertTrue(persistent.getMax() == -Double.MAX_VALUE);
        assertTrue(Double.valueOf(persistent.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(persistent.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(persistent.getStdDev()).isNaN());
        assertEquals(0.0, persistent.getSum(), 1E-6);
        assertEquals(0, persistent.getN());
        assertNull(persistent.getConfidenceInterval(0.95));
        assertNull(persistent.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE));
        assertNull(persistent.getConfidenceInterval(0.95, Tally.RIGHT_SIDE_CONFIDENCE));
        assertNull(persistent.getConfidenceInterval(0.95, Tally.BOTH_SIDE_CONFIDENCE));

        // We first fire a wrong event
        try
        {
            persistent.notify(new Event(null, "ERROR", "ERROR"));
            fail("persistent should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.0), 0.0));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.1), 0.1));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.2), 0.2));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.3), 0.3));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.4), 0.4));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.5), 0.5));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.6), 0.6));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.7), 0.7));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.8), 0.8));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(1.9), 0.9));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(2.0), 1.0));
            persistent.notify(new TimedEvent<Double>(null, "PersistentTest", Double.valueOf(2.1), 1.1));
        }
        catch (Exception exception)
        {
            fail(exception.getMessage());
        }

        // Now we check the persistent
        assertEquals(2.1, persistent.getMax(), 1.0E-6);
        assertEquals(1.0, persistent.getMin(), 1.0E-6);
        assertEquals(12, persistent.getN());
        assertEquals(18.6, persistent.getSum(), 1.0E-6);
        assertEquals(1.5, persistent.getSampleMean(), 1.0E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, persistent.getSampleVariance(), 1.0E-6);
        assertEquals(stDev, persistent.getStdDev(), 1.0E-6);
    }
}
