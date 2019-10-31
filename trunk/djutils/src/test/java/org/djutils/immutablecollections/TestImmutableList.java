package org.djutils.immutablecollections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Test;

/**
 * Test immutable list.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Feb 26, 2019 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestImmutableList
{
    /** Accumulator for forEach test. */
    int sum;

    /**
     * Test the immutable list class.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testImmutableList()
    {
        Integer[] values = new Integer[] {2, 5, 1, 2, 4, 9};
        ImmutableList<Integer> il = new ImmutableArrayList<>(Arrays.asList(values));
        assertTrue("toString returns something descriptive", il.toString().startsWith("ImmutableArrayList ["));
        assertTrue("default is to copy", il.isCopy());
        assertFalse("default is not to wrap", il.isWrap());
        ImmutableList<Integer> il2 = new ImmutableArrayList<>(Arrays.asList(values), Immutable.COPY);
        assertTrue("COPY means copy", il2.isCopy());
        il2 = new ImmutableArrayList<>(Arrays.asList(values), Immutable.WRAP);
        assertTrue("COPY means copy", il2.isWrap());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractCollection<Integer>) il);
        assertTrue("default is to copy", il2.isCopy());
        assertEquals("has same size", il.size(), il2.size());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractList<Integer>) il);
        assertTrue("default is to copy", il2.isCopy());
        assertEquals("has same size", il.size(), il2.size());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractList<Integer>) il, Immutable.COPY);
        assertTrue("COPY means copy", il2.isCopy());
        assertEquals("has same size", il.size(), il2.size());
        il2 = new ImmutableArrayList<Integer>((ImmutableAbstractList<Integer>) il, Immutable.WRAP);
        assertTrue("WRAP means wrap", il2.isWrap());
        assertFalse("WRAP is not copy", il2.isCopy());
        assertTrue("Wrap is wrap", il2.isWrap());
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
        assertTrue("toString method of iterator returns something descriptive", ii.toString().startsWith("ImmutableIterator ["));
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
        assertFalse("not contains all", il.containsAll(Arrays.asList(new Integer[] {1, 2, 3})));
        assertTrue("contains all", il.containsAll(new ImmutableArrayList<Integer>(Arrays.asList(values))));
        assertFalse("not contains all",
                il.containsAll(new ImmutableArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3}))));
        outObject = il.stream().toArray();
        assertEquals("length of toArray matches size of what went in", values.length, outObject.length);
        for (int index = 0; index < outObject.length; index++)
        {
            assertEquals("objects in out match what went in", values[index], outObject[index]);
        }
        assertTrue("toString returns something descriptive", il.toString().startsWith("ImmutableArrayList ["));
        assertEquals("size returns correct value", values.length, il.size());
        assertFalse("list is not empty", il.isEmpty());
        assertTrue("emty list reports it is empty", new ImmutableArrayList<Integer>(Arrays.asList(new Integer[] {})).isEmpty());
        assertTrue("equal to itself", il.equals(il));
        assertFalse("not equal to null", il.equals(null));
        assertFalse("not equal to some string", il.equals("abc"));
        assertFalse("not equal to a (shorter) sub list of itself", il.equals(il2));
        il2 = new ImmutableArrayList<Integer>(Arrays.asList(values));
        assertTrue("equal to another one that has the exact same contents", il.equals(il2));
        assertEquals("hashcodes should match", il.hashCode(), il2.hashCode());

        // Testing the spliterator and parallelstream will have to wait until I understand how to write a unit test for that
    }

    /**
     * Test the ImmutableHashMap class.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testImmutableHashSet()
    {
        Integer[] values = new Integer[] {2, 5, 1, 12, 4, 9}; // all different
        ImmutableSet<Integer> is = new ImmutableHashSet<>(Arrays.asList(values));
        assertTrue("default is to copy", is.isCopy());
        assertFalse("default is not to wrap", is.isWrap());
        ImmutableSet<Integer> is2 = new ImmutableHashSet<>(new HashSet<Integer>(Arrays.asList(values)), Immutable.COPY);
        assertTrue("COPY means copy", is2.isCopy());
        is2 = new ImmutableHashSet<>(new HashSet<Integer>(Arrays.asList(values)), Immutable.WRAP);
        assertTrue("COPY means copy", is2.isWrap());
        is2 = new ImmutableHashSet<Integer>((ImmutableAbstractCollection<Integer>) is);
        assertTrue("default is to copy", is2.isCopy());
        assertEquals("has same size", is.size(), is2.size());
        is2 = new ImmutableHashSet<Integer>((ImmutableAbstractSet<Integer>) is);
        assertTrue("default is to copy", is2.isCopy());
        assertEquals("has same size", is.size(), is2.size());
        is2 = new ImmutableHashSet<Integer>((ImmutableAbstractSet<Integer>) is, Immutable.COPY);
        assertTrue("COPY means copy", is2.isCopy());
        assertEquals("has same size", is.size(), is2.size());
        is2 = new ImmutableHashSet<Integer>((ImmutableAbstractSet<Integer>) is, Immutable.WRAP);
        assertTrue("WRAP means wrap", is2.isWrap());
        assertEquals("has same size", is.size(), is2.size());
        assertTrue("contains 1", is.contains(new Integer(1)));
        assertFalse("does not contain 123", is.contains(new Integer(123)));
        Object[] outObject = is.toArray();
        assertEquals("length of toArray matches size of what went in", values.length, outObject.length);
        Set<Integer> verify = new HashSet<>(Arrays.asList(values));
        for (int index = 0; index < outObject.length; index++)
        {
            assertTrue("Each object matches an object that went in", verify.remove(outObject[index]));
        }
        assertTrue("All objects were matched", verify.isEmpty());
        Integer[] outInteger = is.toArray(new Integer[0]);
        assertEquals("length of toArray matches size of what went in", values.length, outInteger.length);
        verify = new HashSet<>(Arrays.asList(values));
        for (int index = 0; index < outInteger.length; index++)
        {
            assertTrue("Each object matches an object that went in", verify.remove(outInteger[index]));
        }
        assertTrue("All objects were matched", verify.isEmpty());
        verify = new HashSet<>(Arrays.asList(values));
        ImmutableIterator<Integer> ii = is.iterator();
        for (int index = 0; index < values.length; index++)
        {
            assertTrue(ii.hasNext());
            Integer got = ii.next();
            assertTrue("Each object matches an object that went in", verify.remove(got));
        }
        assertFalse("iterator has run out", ii.hasNext());
        assertTrue("All objects were matched", verify.isEmpty());
        this.sum = 0;
        is.forEach(new Consumer<Integer>()
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
        assertTrue("contains all", is.containsAll(Arrays.asList(values)));
        assertFalse("not contains all", is.containsAll(Arrays.asList(new Integer[] {1, 2, 3})));
        assertTrue("contains all", is.containsAll(new ImmutableArrayList<Integer>(Arrays.asList(values))));
        assertFalse("not contains all",
                is.containsAll(new ImmutableArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3}))));
        outObject = is.stream().toArray();
        assertEquals("length of toArray matches size of what went in", values.length, outObject.length);
        verify = new HashSet<>(Arrays.asList(values));
        for (int index = 0; index < outObject.length; index++)
        {
            assertTrue("Each object matches an object that went in", verify.remove(outObject[index]));
        }
        assertTrue("All objects were matched", verify.isEmpty());
        assertTrue("toString returns something descriptive", is.toString().startsWith("ImmutableHashSet ["));
        assertEquals("size returns correct value", values.length, is.size());
        assertFalse("list is not empty", is.isEmpty());
        assertTrue("emty list reports it is empty", new ImmutableArrayList<Integer>(Arrays.asList(new Integer[] {})).isEmpty());
        assertTrue("equal to itself", is.equals(is));
        assertFalse("not equal to null", is.equals(null));
        assertFalse("not equal to some string", is.equals("abc"));
        is2 = new ImmutableHashSet<Integer>(Arrays.asList(Arrays.copyOfRange(values, 2, 4)));
        assertFalse("not equal to a (smaller) sub set of itself", is.equals(is2));
        is2 = new ImmutableHashSet<Integer>(new HashSet<>(Arrays.asList(values)));
        assertTrue("equal to another one that has the exact same contents", is.equals(is2));
        assertEquals("hashcodes should match", is.hashCode(), is2.hashCode());
        Collection<Integer> collection = is.toCollection();
        assertEquals("to collection result has correct number of values", is.size(), collection.size());
        verify = new HashSet<>(Arrays.asList(values));
        Iterator<Integer> i = collection.iterator();
        while (i.hasNext())
        {
            assertTrue("Each object matches an object that went in", verify.remove(i.next()));
        }
        assertTrue("All objects were matched", verify.isEmpty());

        // Testing the spliterator and parallelstream will have to wait until I understand how to write a unit test for that
    }

}
