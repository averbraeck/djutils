package org.djutils.immutablecollections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableTreeSet.java.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableTreeSet
{

    /**
     * Test the tree set.
     */
    @Test
    public final void testTreeSet()
    {
        Set<Integer> intSet = new TreeSet<>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        NavigableSet<Integer> sortedSet = new TreeSet<Integer>(intSet);
        testIntSet(sortedSet, new ImmutableTreeSet<Integer>(sortedSet, Immutable.WRAP), Immutable.WRAP);
        sortedSet = new TreeSet<Integer>(intSet);
        testIntSet(sortedSet, new ImmutableTreeSet<Integer>(sortedSet, Immutable.COPY), Immutable.COPY);
        sortedSet = new TreeSet<Integer>(intSet);
        testIntSet(sortedSet, new ImmutableTreeSet<Integer>(sortedSet), Immutable.COPY);
        sortedSet = new TreeSet<Integer>(intSet);
        ImmutableTreeSet<Integer> ihs = new ImmutableTreeSet<Integer>(sortedSet);
        testIntSet(sortedSet, new ImmutableTreeSet<Integer>(ihs), Immutable.COPY);

        sortedSet = new TreeSet<Integer>(intSet);
        List<Integer> il = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        testIntSet(sortedSet, new ImmutableTreeSet<Integer>(il), Immutable.COPY);
        ImmutableTreeSet<Integer> its = new ImmutableTreeSet<Integer>(sortedSet);
        Assert.assertTrue("toString returns something descriptive", its.toString().startsWith("ImmutableTreeSet ["));

        ImmutableTreeSet<Integer> wrapped = new ImmutableTreeSet<Integer>(its, Immutable.WRAP);
        Assert.assertEquals("wrapped is equal wrapped-wrapped", its, wrapped);
        ImmutableTreeSet<Integer> copied = new ImmutableTreeSet<Integer>(its, Immutable.COPY);
        Assert.assertEquals("wrapped is equal to copy-wrapped", its, copied);
        Assert.assertEquals("copy-wrapped is equal to wrapped", copied, its);
    }

    /**
     * ...
     * @param set NavigableSet&lt;Integer&gt;; set
     * @param imSet ImmutableTreeSet&lt;Integer&gt;; immutable set
     * @param copyOrWrap Immutable;
     */
    private void testIntSet(final NavigableSet<Integer> set, final ImmutableTreeSet<Integer> imSet, final Immutable copyOrWrap)
    {
        Assert.assertTrue(set.size() == 10);
        Assert.assertTrue(imSet.size() == 10);
        for (int i = 0; i < 10; i++)
        {
            Assert.assertTrue(imSet.contains(i + 1));
        }
        Assert.assertFalse(imSet.isEmpty());
        Assert.assertFalse(imSet.contains(15));

        Assert.assertTrue(imSet.first() == 1);
        Assert.assertTrue(imSet.last() == 10);

        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imSet.isCopy());
            Assert.assertTrue(imSet.toSet().equals(set));
            Assert.assertFalse(imSet.toSet() == set);
        }
        else
        {
            Assert.assertTrue(imSet.isWrap());
            Assert.assertTrue(imSet.toSet().equals(set));
            Assert.assertFalse(imSet.toSet() == set); // this WRAP method returns a NEW list
        }

        Set<Integer> to = imSet.toSet();
        Assert.assertTrue(set.equals(to));

        Integer[] arr = imSet.toArray(new Integer[] {});
        Integer[] sar = set.toArray(new Integer[] {});
        Assert.assertArrayEquals(arr, sar);

        // modify the underlying data structure
        set.add(11);
        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imSet.size() == 10);
        }
        else
        {
            Assert.assertTrue(imSet.size() == 11);
        }
    }

    /**
     * Test the comparator of the ImmutableTreeSet.
     */
    @Test
    public void testComparator()
    {
        Integer[] values = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Set<Integer> intSet = new TreeSet<>(Arrays.asList(values));
        NavigableSet<Integer> sortedSet = new TreeSet<Integer>(intSet);
        assertNull("Sorted set uses default compare; not an explicit comparator", sortedSet.comparator());
        Comparator<Integer> reverseIntegerComparator = new Comparator<Integer>()
        {
            @Override
            public int compare(final Integer o1, final Integer o2)
            {
                return -Integer.compare(o1, o2);
            }

            @Override
            public String toString()
            {
                return "Reversing comparator";
            }
        };
        sortedSet = new TreeSet<Integer>(reverseIntegerComparator);
        sortedSet.addAll(intSet);
        ImmutableTreeSet<Integer> its = new ImmutableTreeSet<>(sortedSet, Immutable.WRAP);
        assertEquals("custom comparator is returned", reverseIntegerComparator, its.comparator());
        // Let's check that the custom comparator actually worked
        assertEquals("size must match", values.length, its.size());
        Integer prev = null;
        for (Integer value : its)
        {
            // System.out.println(value);
            if (prev != null)
            {
                assertTrue("Values must be in non-increasing order", value <= prev);
            }
            prev = value;
        }
        ImmutableSortedSet<Integer> subSet = its.subSet(7, 3);
        prev = null;
        boolean seen3 = false;
        boolean seen7 = false;
        for (Integer value : subSet)
        {
            // System.out.println(value);
            assertTrue("value must be in range", value <= 7 && value >= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertFalse("3 must not have been returned", seen3);
        assertTrue("7 must have been returned", seen7);

        subSet = its.subSet(7, false, 3, false);
        prev = null;
        seen3 = false;
        seen7 = false;
        for (Integer value : subSet)
        {
            // System.out.println(value);
            assertTrue("value must be in range", value <= 7 && value >= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertFalse("3 must not have been returned", seen3);
        assertFalse("7 must not have been returned", seen7);

        subSet = its.subSet(7, true, 3, false);
        prev = null;
        seen3 = false;
        seen7 = false;
        for (Integer value : subSet)
        {
            // System.out.println(value);
            assertTrue("value must be in range", value <= 7 && value >= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertFalse("3 must not have been returned", seen3);
        assertTrue("7 must have been returned", seen7);

        subSet = its.subSet(7, false, 3, true);
        prev = null;
        seen3 = false;
        seen7 = false;
        for (Integer value : subSet)
        {
            // System.out.println(value);
            assertTrue("value must be in range", value <= 7 && value >= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertTrue("3 must have been returned", seen3);
        assertFalse("7 must not have been returned", seen7);

        subSet = its.subSet(7, true, 3, true);
        prev = null;
        seen3 = false;
        seen7 = false;
        for (Integer value : subSet)
        {
            // System.out.println(value);
            assertTrue("value must be in range", value <= 7 && value >= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertTrue("3 must have been returned", seen3);
        assertTrue("7 must have been returned", seen7);

        ImmutableSortedSet<Integer> headSet = its.headSet(7);
        prev = null;
        seen7 = false;
        for (Integer value : headSet)
        {
            assertTrue("value must be in range", value >= 7);
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertFalse("7 must not have been returned", seen7);
        
        headSet = its.headSet(7, true);
        prev = null;
        seen7 = false;
        for (Integer value : headSet)
        {
            assertTrue("value must be in range", value >= 7);
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertTrue("7 must have been returned", seen7);
        
        headSet = its.headSet(7, false);
        prev = null;
        seen7 = false;
        for (Integer value : headSet)
        {
            assertTrue("value must be in range", value >= 7);
            if (7 == value)
            {
                seen7 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertFalse("7 must not have been returned", seen7);
        
        ImmutableSortedSet<Integer> tailSet = its.tailSet(3);
        prev = null;
        seen3 = false;
        for (Integer value : tailSet)
        {
            assertTrue("value must be in range", value <= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertTrue("3 must have been returned", seen3);
        
        tailSet = its.tailSet(3, true);
        prev = null;
        seen3 = false;
        for (Integer value : tailSet)
        {
            assertTrue("value must be in range", value <= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertTrue("3 must have been returned", seen3);
        
        tailSet = its.tailSet(3, false);
        prev = null;
        seen3 = false;
        for (Integer value : tailSet)
        {
            assertTrue("value must be in range", value <= 3);
            if (3 == value)
            {
                seen3 = true;
            }
            if (prev != null)
            {
                assertTrue("Values are in decreasing order", value <= prev);
            }
            prev = value;
        }
        assertFalse("3 must not have been returned", seen3);
        
        for (int index = 0; index < values.length; index++)
        {
            Integer lower = its.lower(values[index]);
            if (index == values.length - 1)
            {
                assertNull("lower of last element should have returned null", lower);
            }
            else
            {
                assertEquals("lower should have returned next higher value", values[index + 1], lower);
            }
            Integer higher = its.higher(values[index]);
            if (index == 0)
            {
                assertNull("higher should have returned null", higher);
            }
            else
            {
                assertEquals("higher should have returned next lower value", values[index - 1], higher);
            }
            Integer floor = its.floor(values[index]);
            assertEquals("floor of element in set returns that element", values[index], floor);
            Integer ceil = its.floor(values[index]);
            assertEquals("ceil of element in set returns that element", values[index], ceil);
        }
        assertNull("floor of value higher than any in set returns null", its.floor(11));
        assertNull("ceil of value lower than any in set returns null", its.ceiling(0));
        assertEquals("floor of value lower than any in set is lowest in set", 1, its.floor(0), 0);
        assertEquals("ceiling of value higher than any in set is highest in set", 10, its.ceiling(11), 0);
        ImmutableSet<Integer> descendingSet = its.descendingSet();
        assertEquals("descendingSet has correct size", values.length, descendingSet.size());
        prev = null;
        for (Integer value : descendingSet)
        {
            if (null != prev)
            {
                assertTrue("descendingSet has value in ascending order", value >= prev);
            }
            prev = value;
        }
        ImmutableIterator<Integer> ii = its.descendingIterator();
        prev = null;
        while (ii.hasNext())
        {
            Integer next = ii.next();
            if (null != prev)
            {
                assertTrue("descendingSet has value in ascending order", next >= prev);
            }
            prev = next;
        }
        try
        {
            ii.next();
            fail("next should have thrown an Exception");
        }
        catch (NoSuchElementException nsee)
        {
            // Ignore expected exception
        }
    }

}
