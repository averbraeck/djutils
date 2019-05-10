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
