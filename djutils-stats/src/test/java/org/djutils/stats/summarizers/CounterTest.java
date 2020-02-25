package org.djutils.stats.summarizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.swing.table.TableModel;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;
import org.junit.Test;

/**
 * Test the Counter class.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CounterTest
{
    /** the event to fire. */
    private static final EventType COUNT_EVENT = new EventType("CountEvent");
    
    /** Test the counter. */
    @Test
    public void test()
    {
        String description = "counter description";
        Counter counter = new Counter(description);
        assertEquals(description, counter.toString());
        assertEquals(description, counter.getDescription());

        assertEquals(Long.MIN_VALUE, counter.getN());
        assertEquals(Long.MIN_VALUE, counter.getCount());

        assertFalse(counter.isInitialized());
        counter.initialize();
        assertTrue(counter.isInitialized());

        counter.addListener(new EventListenerInterface()
        {
            /** */
            private static final long serialVersionUID = 1L;

            @Override
            public void notify(final EventInterface event)
            {
                assertTrue(event.getType().equals(Counter.COUNT_EVENT));
                assertTrue(event.getContent().getClass().equals(Long.class));
            }
        }, Counter.COUNT_EVENT);

        // test wrong event
        try
        {
            counter.notify(new Event(COUNT_EVENT, "CounterTest", "abc"));
            fail("Wrong payload to Counter should have triggreed an exception");
        }
        catch (RuntimeException exception)
        {
            // ok, should have given error
        }
        
        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.notify(new Event(Counter.COUNT_EVENT, "CounterTest", Long.valueOf(2 * i)));
            value += 2 * i;
        }
        assertEquals(100, counter.getN());
        assertEquals(value, counter.getCount());
        
        // get the TableModel
        TableModel table = counter.getTable();
        assertEquals(2, table.getColumnCount());
        assertEquals(3, table.getRowCount());
        assertEquals("n", table.getValueAt(1, 0));
        assertEquals(100L, table.getValueAt(1, 1));
    }
}
