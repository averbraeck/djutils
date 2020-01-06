package org.djutils.immutablecollections;

import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableTreeSet.java.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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

        Integer[] arr = (Integer[]) imSet.toArray(new Integer[] {});
        Integer[] sar = (Integer[]) set.toArray(new Integer[] {});
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
}
