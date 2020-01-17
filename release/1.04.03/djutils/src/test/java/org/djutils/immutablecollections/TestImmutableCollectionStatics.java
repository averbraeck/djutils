package org.djutils.immutablecollections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * <p>
 * Copyright (c) 2013-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Feb 26, 2019 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestImmutableCollectionStatics
{

    /**
     * Test the empty constructors.
     */
    @Test
    public void testEmptyConstructors()
    {
        assertEquals("empty immutable set is empty", 0, ImmutableCollections.emptyImmutableSet().size());
        assertEquals("empty immutable list is empty", 0, ImmutableCollections.emptyImmutableList().size());
        assertEquals("empty immutable map is empty", 0, ImmutableCollections.emptyImmutableMap().size());
    }

    /**
     * Test the various methods that return an index.
     */
    @Test
    public void testSearchers()
    {
        final Integer[] values = new Integer[] {10, 2, -5, 2, 2, 6};
        ImmutableList<Integer> il = new ImmutableArrayList<>(new ArrayList<Integer>(Arrays.asList(values)), Immutable.WRAP);
        assertEquals("max", Integer.valueOf(10), ImmutableCollections.max(il));
        assertEquals("min", Integer.valueOf(-5), ImmutableCollections.min(il));
        Comparator<Integer> indexComparator = new Comparator<Integer>()
        {
            // This crazy comparator compares position of the given values in the array values
            @Override
            public int compare(final Integer o1, final Integer o2)
            {
                return Arrays.binarySearch(values, o1) - Arrays.binarySearch(values, o2);
            }
        };
        assertEquals("custom comparator max", Integer.valueOf(6), ImmutableCollections.max(il, indexComparator));
        assertEquals("custom comparator min", Integer.valueOf(10), ImmutableCollections.min(il, indexComparator));
        assertEquals("number of 10s", 1, ImmutableCollections.frequency(il, Integer.valueOf(10)));
        assertEquals("number of 100s", 0, ImmutableCollections.frequency(il, Integer.valueOf(100)));
        assertEquals("number of 2s", 3, ImmutableCollections.frequency(il, Integer.valueOf(2)));
        Integer[] subList = new Integer[] {2, 2};
        ImmutableList<Integer> isl = new ImmutableArrayList<Integer>(new ArrayList<>(Arrays.asList(subList)));
        assertEquals("position of sub list", 3, ImmutableCollections.indexOfSubList(il, isl));
        assertEquals("position of non-sub list", -1, ImmutableCollections.indexOfSubList(isl, il));
        assertEquals("last position of sub list", 3, ImmutableCollections.lastIndexOfSubList(il, isl));
        assertEquals("last position of non-sub list", -1, ImmutableCollections.lastIndexOfSubList(isl, il));
        List<Integer> msl = new ArrayList<>(Arrays.asList(subList));
        assertEquals("position of sub list", 3, ImmutableCollections.indexOfSubList(il, msl));
        assertEquals("position of non-sub list", -1, ImmutableCollections.indexOfSubList(msl, il));
        assertEquals("last position of sub list", 3, ImmutableCollections.lastIndexOfSubList(il, msl));
        assertEquals("last position of non-sub list", -1, ImmutableCollections.lastIndexOfSubList(msl, il));

        Arrays.sort(values); // this modifies the contents of our array
        il = new ImmutableArrayList<>(new ArrayList<Integer>(Arrays.asList(values)));
        assertEquals("position of 6", 4, ImmutableCollections.binarySearch(il, Integer.valueOf(6)));
        assertEquals("position where 5 would be if it were present", -5, ImmutableCollections.binarySearch(il, Integer.valueOf(5)));
        final Integer[] uniqueValues = new Integer[] {10, 2, -5, 6};
        ImmutableList<Integer> il2 = new ImmutableArrayList<>(new ArrayList<Integer>(Arrays.asList(uniqueValues)));
        indexComparator = new Comparator<Integer>()
        {
            // This crazy comparator compares position of the given values in the array values
            @Override
            public int compare(final Integer o1, final Integer o2)
            {
                return Arrays.binarySearch(uniqueValues, o1) - Arrays.binarySearch(uniqueValues, o2);
            }
        };
        assertEquals("position of 2 binary search with crazy comparator", 1,
                ImmutableCollections.binarySearch(il2, Integer.valueOf(2), indexComparator));
        assertEquals("position of 10 binary search with crazy comparator", 0,
                ImmutableCollections.binarySearch(il2, Integer.valueOf(10), indexComparator));
        assertEquals("position of 6 binary search with crazy comparator", 3,
                ImmutableCollections.binarySearch(il2, Integer.valueOf(6), indexComparator));
        assertFalse("The collections are not disjoint", ImmutableCollections.disjoint(il, il2));
        ImmutableList<Integer> il3 = new ImmutableArrayList<>(new ArrayList<Integer>(Arrays.asList(new Integer[] {99, 999})));
        assertTrue("The collections are disjoint", ImmutableCollections.disjoint(il, il3));
        List<Integer> mutableList = new ArrayList<>(Arrays.asList(uniqueValues));
        assertFalse("The collections are not disjoint", ImmutableCollections.disjoint(il, mutableList));
        assertFalse("The collections are not disjoint", ImmutableCollections.disjoint(mutableList, il));
        mutableList = new ArrayList<Integer>(Arrays.asList(new Integer[] {99, 999}));
        assertTrue("The collections are disjoint", ImmutableCollections.disjoint(il, mutableList));
        assertTrue("The collections are disjoint", ImmutableCollections.disjoint(mutableList, il));

    }
}
