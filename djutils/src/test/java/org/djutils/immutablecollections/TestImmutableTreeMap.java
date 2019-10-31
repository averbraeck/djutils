package org.djutils.immutablecollections;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.djutils.immutablecollections.ImmutableMap.ImmutableEntry;
import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableTreeMap.java.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableTreeMap
{

    @SuppressWarnings({ "unlikely-arg-type" })
    @Test
    public final void testTreeMap()
    {
        NavigableMap<Integer, Integer> isMap = new TreeMap<>();
        for (int i = 1; i <= 10; i++)
            isMap.put(i, 100 * i);
        NavigableMap<Integer, Integer> map = new TreeMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableTreeMap<Integer, Integer>(map, Immutable.WRAP), Immutable.WRAP);
        map = new TreeMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableTreeMap<Integer, Integer>(map, Immutable.COPY), Immutable.COPY);
        map = new TreeMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableTreeMap<Integer, Integer>(map), Immutable.COPY);
        map = new TreeMap<Integer, Integer>(isMap);
        ImmutableTreeMap<Integer, Integer> ihs = new ImmutableTreeMap<Integer, Integer>(map);
        testIntMap(map, new ImmutableTreeMap<Integer, Integer>(ihs), Immutable.COPY);

        ImmutableTreeMap<Integer, Integer> itm = new ImmutableTreeMap<>(isMap, Immutable.WRAP);
        ImmutableTreeMap<Integer, Integer> itmw = new ImmutableTreeMap<>(itm, Immutable.WRAP);
        Assert.assertEquals("wrapper is equal to wrapped", itm, itmw);
        itmw = new ImmutableTreeMap<>(itm, Immutable.COPY);
        Assert.assertEquals("copied is equal to wrapped", itm, itmw);
        Assert.assertEquals("wrapped is equal to copied", itmw, itm);
        Assert.assertTrue("toString returns something descriptive", itm.toString().startsWith("ImmutableTreeMap ["));
        ImmutableTreeMap<Integer, Integer> subMap = (ImmutableTreeMap<Integer, Integer>) itm.subMap(3, 5);
        Assert.assertEquals("size is 2", 2, subMap.size());
        Assert.assertEquals("first key is 3", 3, subMap.firstKey(), 0);
        Assert.assertEquals("last key is 4", 4, subMap.lastKey(), 0);
        Assert.assertNull("sub map has no value for key 2", subMap.get(2));
        Assert.assertNull("sub map has no value for key 5", subMap.get(5));
        Assert.assertEquals("value for key 3 is 300", 300, subMap.get(3), 0);
        Assert.assertEquals("value for key 4 is 400", 400, subMap.get(4), 0);
        subMap = (ImmutableTreeMap<Integer, Integer>) itm.subMap(2, true, 6, true);
        Assert.assertEquals("first key is 2", 2, subMap.firstKey(), 0);
        Assert.assertEquals("last key is 6", 6, subMap.lastKey(), 0);
        ImmutableTreeMap<Integer, Integer> headMap = (ImmutableTreeMap<Integer, Integer>) itm.headMap(5);
        Assert.assertEquals("headMap has 4 entries", 4, headMap.size());
        Assert.assertEquals("first key is 1", 1, headMap.firstKey(), 0);
        Assert.assertEquals("last key is 4", 4, headMap.lastKey(), 0);
        headMap = (ImmutableTreeMap<Integer, Integer>) itm.headMap(5, true);
        Assert.assertEquals("headMap has 5 entries", 5, headMap.size());
        Assert.assertEquals("first key is 1", 1, headMap.firstKey(), 0);
        Assert.assertEquals("last key is 5", 5, headMap.lastKey(), 0);
        ImmutableTreeMap<Integer, Integer> tailMap = (ImmutableTreeMap<Integer, Integer>) itm.tailMap(5);
        Assert.assertEquals("tailMap has 6 entries", 6, tailMap.size());
        Assert.assertEquals("first key is 5", 5, tailMap.firstKey(), 0);
        tailMap = (ImmutableTreeMap<Integer, Integer>) itm.tailMap(5, false);
        Assert.assertEquals("tailMap has 5 entries", 5, tailMap.size());
        Assert.assertEquals("first key is 6", 6, tailMap.firstKey(), 0);
        Assert.assertNull("there is no lower key than 1", itm.lowerKey(1));
        Assert.assertEquals("highest key lower than 5 is 4", 4, itm.lowerKey(5), 0);
        Assert.assertEquals("highest key lower than 999 is 10", 10, itm.lowerKey(999), 0);
        Assert.assertEquals("highest key lower than, or equal to 5 is s", 5, itm.floorKey(5), 0);
        Assert.assertNull("highest key lower than, or equal to 0 does not exist", itm.floorKey(0));
        Assert.assertEquals("lowest key equal or bigger than 3 is 3", 3, itm.ceilingKey(3), 0);
        Assert.assertNull("lowest key equal or bigger than 11 does not exist", itm.ceilingKey(11));
        Assert.assertEquals("lowest key bigger than -10 is 1", 1, itm.higherKey(-10), 0);
        Assert.assertEquals("lowest key bigger than 5 is 6", 6, itm.higherKey(5), 0);
        Assert.assertNull("lowest key bigger than 10 does not exist", itm.higherKey(10));
        ImmutableTreeMap<Integer, Integer> descending = (ImmutableTreeMap<Integer, Integer>) itm.descendingMap();
        Assert.assertEquals("descending map has same size", itm.size(), descending.size());
        ImmutableSet<ImmutableEntry<Integer, Integer>> entrySet = itm.entrySet();
        Iterator<ImmutableEntry<Integer, Integer>> iterator = entrySet.iterator();
        ImmutableEntry<Integer, Integer> sampleEntry = iterator.next();
        Assert.assertEquals("ImmutableEntry is equal to itself", sampleEntry, sampleEntry);
        Assert.assertFalse("entry is not equal to null", sampleEntry.equals(null));
        Assert.assertFalse("entry is not equal to some other object", sampleEntry.equals("ABC"));
        ImmutableEntry<Integer, Integer> differentEntry = iterator.next();
        Assert.assertFalse("entry is not equal to the next entry", sampleEntry.equals(differentEntry));
        ImmutableEntry<Integer, Integer> copy = new ImmutableEntry<Integer, Integer>(isMap.firstEntry());
        Assert.assertEquals("wrapped entry is equal to self made entry containing same entry", sampleEntry, copy);
        ImmutableEntry<Integer, Integer> containsNull = new ImmutableEntry<Integer, Integer>(null);
        Assert.assertFalse("wrapped entry is not equal to self made entry contaning null", sampleEntry.equals(containsNull));
        Assert.assertFalse("Self made entry containing null is not equal to sampleEntry", containsNull.equals(sampleEntry));
        ImmutableEntry<Integer, Integer> otherContainsNull = new ImmutableEntry<Integer, Integer>(null);
        Assert.assertEquals("entry containing null is equal to another entry containing null", containsNull, otherContainsNull);
        
        
    }

    private void testIntMap(final NavigableMap<Integer, Integer> map, final ImmutableTreeMap<Integer, Integer> imMap,
            final Immutable copyOrWrap)
    {
        Assert.assertTrue(map.size() == 10);
        Assert.assertTrue(imMap.size() == 10);
        for (int i = 0; i < 10; i++)
            Assert.assertTrue(imMap.containsKey(i + 1));
        for (int i = 0; i < 10; i++)
            Assert.assertTrue(imMap.containsValue(100 * (i + 1)));
        Assert.assertFalse(imMap.isEmpty());
        Assert.assertFalse(imMap.containsKey(15));
        Assert.assertFalse(imMap.containsValue(1500));

        Assert.assertTrue(imMap.keySet().size() == 10);
        Assert.assertTrue(imMap.values().size() == 10);
        Assert.assertTrue(imMap.keySet().first() == 1);
        Assert.assertTrue(imMap.keySet().last() == 10);
        Assert.assertTrue(imMap.values().contains(200));

        Assert.assertArrayEquals(map.keySet().toArray(), imMap.keySet().toSet().toArray());
        Assert.assertArrayEquals(map.values().toArray(), imMap.values().toSet().toArray());
        Assert.assertArrayEquals(map.keySet().toArray(), imMap.keySet().toSet().toArray()); // cached
        Assert.assertArrayEquals(map.values().toArray(), imMap.values().toSet().toArray());

        Assert.assertTrue(checkEntrySets(map.entrySet(), imMap.entrySet().toSet()));
        Assert.assertTrue(checkEntrySets(map.entrySet(), imMap.entrySet().toSet())); // cached

        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imMap.isCopy());
            Assert.assertTrue(imMap.toMap().equals(map));
            Assert.assertFalse(imMap.toMap() == map);
        }
        else
        {
            Assert.assertTrue(imMap.isWrap());
            Assert.assertTrue(imMap.toMap().equals(map));
            Assert.assertFalse(imMap.toMap() == map); // this WRAP method returns a NEW list
        }

        Map<Integer, Integer> to = imMap.toMap();
        Assert.assertTrue(map.equals(to));

        // modify the underlying data structure
        map.put(11, 1100);
        if (copyOrWrap == Immutable.COPY)
            Assert.assertTrue(imMap.size() == 10);
        else
            Assert.assertTrue(imMap.size() == 11);
    }

    private boolean checkEntrySets(Set<Entry<Integer, Integer>> es, Set<ImmutableEntry<Integer, Integer>> ies)
    {
        if (es.size() != ies.size())
            return false;
        Iterator<Entry<Integer, Integer>> entryIt = es.iterator();
        Iterator<ImmutableEntry<Integer, Integer>> immEntryIt = ies.iterator();
        while (entryIt.hasNext())
        {
            Entry<Integer, Integer> e1 = entryIt.next();
            ImmutableEntry<Integer, Integer> e2 = immEntryIt.next();
            if (!e1.getKey().equals(e2.getKey()) || !e1.getValue().equals(e2.getValue()))
                return false;
        }
        return true;
    }
}
