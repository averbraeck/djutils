package org.djutils.immutablecollections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Test;

/**
 * Test immutable list.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Feb 26, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestImmutableList
{
    /**
     * Test the immutable list class.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testImmutableList()
    {
        Integer[] values = new Integer[] { 2, 5, 1, 2, 4, 9 };
        ImmutableList<Integer> il = new ImmutableArrayList<>(Arrays.asList(values));
        assertTrue("default is to copy", il.isCopy());
        assertFalse("default is not to wrap", il.isWrap());
        ImmutableList<Integer> il2 = new ImmutableArrayList<>(Arrays.asList(values), Immutable.COPY);
        assertTrue("COPY means copy", il2.isCopy());
        il2 = new ImmutableArrayList<>(Arrays.asList(values), Immutable.WRAP);
        assertTrue("COPY means copy", il2.isWrap());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractCollection<Integer>)il);
        assertTrue("default is to copy", il2.isCopy());
        assertEquals("has same size", il.size(), il2.size());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractList<Integer>)il);
        assertTrue("default is to copy", il2.isCopy());
        assertEquals("has same size", il.size(), il2.size());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractList<Integer>)il, Immutable.COPY);
        assertTrue("COPY means copy", il2.isCopy());
        assertEquals("has same size", il.size(), il2.size());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractList<Integer>)il, Immutable.WRAP);
        assertTrue("WRAP means wrap", il2.isWrap());
        assertEquals("has same size", il.size(), il2.size());
        il2 = il.subList(2, 4);
        assertEquals("sublist has length 2", 2, il2.size());
        for (int index = 0; index < il2.size(); index++)
        {
            assertEquals("sub list element matches", values[index + 2], il2.get(index));
        }
        assertEquals("position of first 2", 0, il.indexOf(new Integer(2)));
        assertEquals("position of first (and only) 4", 4, il.indexOf(new Integer(4)));
        assertEquals("position of last 2", 3, il.lastIndexOf(new Integer(2)));
        assertTrue("contains 1", il.contains(new Integer(1)));
        assertFalse("does not contain 123", il.contains(new Integer(123)));
        Object[] outObject = il.toArray();
        assertEquals("length of toArray matches size of what went in", values.length, outObject.length);
        for (int index = 0; index < outObject.length; index++)
        {
            assertEquals("objects in out match what went in", values[index], outObject[index]);
        }
        Integer[] outInteger = il.toArray(new Integer[0]);
        assertEquals("length of toArray matches size of what went in", values.length, outInteger.length);
        for (int index = 0; index < outInteger.length; index++)
        {
            assertEquals("objects in out match what went in", values[index], outInteger[index]);
        }
        for (int index = 0; index < values.length; index++)
        {
            assertEquals("values can be retrieved one by one", values[index], il.get(index));
        }
        ImmutableIterator<Integer> ii = il.iterator();
        for (int index = 0; index < values.length; index++)
        {
            assertTrue(ii.hasNext());
            Integer got = ii.next();
            assertEquals("iterator returned next value", values[index], got);
        }
        assertFalse("iterator has run out", ii.hasNext());
        this.sum = 0;
        il.forEach(new Consumer<Integer>()
        {

            @Override
            public void accept(Integer t)
            {
                TestImmutableList.this.sum += t;
            }
        });
        // compute the result the old fashioned way
        int expectedSum = 0;
        for (int index = 0; index < values.length; index++)
        {
            expectedSum += values[index];
        }
        assertEquals("sum matches", expectedSum, this.sum);
        assertTrue("contains all", il.containsAll(Arrays.asList(values)));
        assertFalse("not contains all", il.containsAll(Arrays.asList(new Integer[] { 1, 2, 3 })));
        assertTrue("contains all", il.containsAll(new ImmutableArrayList<Integer>(Arrays.asList(values))));
        assertFalse("not contains all",
                il.containsAll(new ImmutableArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3 }))));
        outObject = il.stream().toArray();
        assertEquals("length of toArray matches size of what went in", values.length, outObject.length);
        for (int index = 0; index < outObject.length; index++)
        {
            assertEquals("objects in out match what went in", values[index], outObject[index]);
        }
        assertTrue("toString returns something descriptive", il.toString().startsWith("ImmutableArrayList ["));
        assertEquals("size returns correct value", values.length, il.size());
        assertFalse("list is not empty", il.isEmpty());
        assertTrue("emty list reports it is empty",
                new ImmutableArrayList<Integer>(Arrays.asList(new Integer[] {})).isEmpty());
        // Testing the spliterator and parallelstream will have to wait until I understand how to write a unit test for that
    }

    /** Accumulator for forEach test. */
    int sum;
}
