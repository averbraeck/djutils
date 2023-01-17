package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test the Counter class.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * <br>
 * @author <a href="https://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CounterTest
{
    /** Test the counter. */
    @Test
    public void testCounter()
    {
        String description = "counter description";
        Counter counter = new Counter(description);
        assertTrue(counter.toString().contains(description));
        assertTrue(counter.toString().startsWith("Counter"));
        assertEquals(description, counter.getDescription());

        assertEquals(0L, counter.getN());
        assertEquals(0L, counter.getCount());

        counter.register(2);
        assertEquals(1L, counter.getN());
        assertEquals(2L, counter.getCount());

        counter.initialize();
        assertEquals(0L, counter.getN());
        assertEquals(0L, counter.getCount());

        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.register(2 * i);
            value += 2 * i;
        }
        assertEquals(100, counter.getN());
        assertEquals(value, counter.getCount());
        
        Counter counterLong = new Counter("A very " + "long ".repeat(20) + " description");
        counterLong.register(1000);

        // check the report functions
        int len = Counter.reportFooter().length();
        assertEquals(len, Counter.reportHeader().split("\\R")[0].length());
        assertEquals(len, Counter.reportHeader().split("\\R")[1].length());
        assertEquals(len, Counter.reportHeader().split("\\R")[2].length());
        assertEquals(len, counter.reportLine().length());
        Counter counterEmpty = new Counter("Long description ".repeat(100));
        assertEquals(len, counterEmpty.reportLine().length());

    }
}
