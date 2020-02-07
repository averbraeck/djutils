package org.djutils.immutablecollections;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableLinkedHashSet.java.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableLinkedHashSet
{

    /**
     * ...
     */
    @Test
    public final void testLinkedHashSet()
    {
        Set<Integer> intSet = new LinkedHashSet<>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        Set<Integer> set = new LinkedHashSet<Integer>(intSet);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(set, Immutable.WRAP), Immutable.WRAP);
        set = new LinkedHashSet<Integer>(intSet);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(set, Immutable.COPY), Immutable.COPY);
        set = new LinkedHashSet<Integer>(intSet);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(set), Immutable.COPY);
        set = new LinkedHashSet<Integer>(intSet);
        ImmutableLinkedHashSet<Integer> ihs = new ImmutableLinkedHashSet<Integer>(set);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(ihs), Immutable.COPY);

        set = new LinkedHashSet<Integer>(intSet);
        List<Integer> il = Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(il), Immutable.COPY);
        ImmutableLinkedHashSet<Integer> ilhs = new ImmutableLinkedHashSet<Integer>(set, Immutable.COPY);
        Assert.assertTrue("toString returns something descriptive", ilhs.toString().startsWith("ImmutableLinkedHashSet ["));

        ImmutableLinkedHashSet<Integer> wilhs = new ImmutableLinkedHashSet<Integer>(ilhs, Immutable.WRAP);
        Assert.assertEquals("wrapped immutable linked hash set is equal to that immutable hash set", wilhs, ilhs);
        ImmutableLinkedHashSet<Integer> cilhs = new ImmutableLinkedHashSet<Integer>(ilhs, Immutable.COPY);
        Assert.assertEquals("copied immutable linked hash set is equal to that immutable hash set", cilhs, ilhs);
    }

    /**
     * ...
     * @param set Set&lt;Integer&gt;; a set
     * @param imSet ImmutableSet&lt;Integer&gt;; an immutable set
     * @param copyOrWrap Immutable;
     */
    private void testIntSet(final Set<Integer> set, final ImmutableSet<Integer> imSet, final Immutable copyOrWrap)
    {
        Assert.assertTrue(set.size() == 10);
        Assert.assertTrue(imSet.size() == 10);
        for (int i = 0; i < 10; i++)
        {
            Assert.assertTrue(imSet.contains(i + 1));
        }
        Assert.assertFalse(imSet.isEmpty());
        Assert.assertFalse(imSet.contains(15));
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

}
