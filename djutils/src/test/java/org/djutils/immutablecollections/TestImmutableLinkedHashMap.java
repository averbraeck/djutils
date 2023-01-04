package org.djutils.immutablecollections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.djutils.immutablecollections.ImmutableMap.ImmutableEntry;
import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableLinkedHashMap.java.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableLinkedHashMap
{

    /**
     * ...
     */
    @Test
    public final void testImmutableLinkedHashMap()
    {
        Map<Integer, Integer> isMap = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++)
        {
            isMap.put(i, 100 * i);
        }
        Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableLinkedHashMap<Integer, Integer>(map, Immutable.WRAP), Immutable.WRAP);
        map = new LinkedHashMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableLinkedHashMap<Integer, Integer>(map, Immutable.COPY), Immutable.COPY);
        map = new LinkedHashMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableLinkedHashMap<Integer, Integer>(map), Immutable.COPY);
        map = new LinkedHashMap<Integer, Integer>(isMap);
        ImmutableLinkedHashMap<Integer, Integer> ihs = new ImmutableLinkedHashMap<Integer, Integer>(map);
        testIntMap(map, new ImmutableLinkedHashMap<Integer, Integer>(ihs), Immutable.COPY);
        ImmutableLinkedHashMap<Integer, Integer> ilhm = new ImmutableLinkedHashMap<>(ihs, Immutable.COPY);
        Assert.assertTrue("toString returns something descriptive", ilhm.toString().startsWith("ImmutableLinkedHashMap ["));
    }

    /**
     * ...
     * @param map Map&lt;Integer, Integer&gt;; map
     * @param imMap ImmutableMap&lt;Integer, Integer&gt;; immutable map
     * @param copyOrWrap Immutable;
     */
    private void testIntMap(final Map<Integer, Integer> map, final ImmutableMap<Integer, Integer> imMap,
            final Immutable copyOrWrap)
    {
        Assert.assertTrue(map.size() == 10);
        Assert.assertTrue(imMap.size() == 10);
        for (int i = 0; i < 10; i++)
        {
            Assert.assertTrue(imMap.containsKey(i + 1));
        }
        for (int i = 0; i < 10; i++)
        {
            Assert.assertTrue(imMap.containsValue(100 * (i + 1)));
        }
        Assert.assertFalse(imMap.isEmpty());
        Assert.assertFalse(imMap.containsKey(15));
        Assert.assertFalse(imMap.containsValue(1500));

        Assert.assertTrue(imMap.keySet().size() == 10);
        Assert.assertTrue(imMap.values().size() == 10);

        Assert.assertTrue(sameContent(map.keySet(), imMap.keySet().toSet()));
        Assert.assertTrue(sameContent(map.values(), imMap.values().toCollection()));
        Assert.assertTrue(sameContent(map.keySet(), imMap.keySet().toSet())); // cached
        Assert.assertTrue(sameContent(map.values(), imMap.values().toCollection()));

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
        {
            Assert.assertTrue(imMap.size() == 10);
        }
        else
        {
            Assert.assertTrue(imMap.size() == 11);
        }
    }

    /**
     * Determine if two collections contain the same objects.
     * @param a Collection&lt;?&gt;; collection
     * @param b Collection&lt;?&gt;; another collection
     * @return boolean; true if the collections contain the same objects
     */
    private boolean sameContent(final Collection<?> a, final Collection<?> b)
    {
        return a.containsAll(b) && b.containsAll(a); // Oops: second half was b.containsAll(b)
    }

    /**
     * Determine if two entry sets contain the same entries.
     * @param es Set&lt;Engry&gt;Integer, Integer&gt;&gt;; entry set
     * @param ies Set&lt;ImmutableEngry&gt;Integer, Integer&gt;&gt;; immutable entry set
     * @return boolean; true if the entry sets contain the same set of keys, each with the same values
     */
    private boolean checkEntrySets(final Set<Entry<Integer, Integer>> es, final Set<ImmutableEntry<Integer, Integer>> ies)
    {
        if (es.size() != ies.size())
        {
            return false;
        }
        for (Entry<Integer, Integer> entry : es)
        {
            boolean found = false;
            for (ImmutableEntry<Integer, Integer> immEntry : ies)
            {
                if (entry.getKey().equals(immEntry.getKey()) && entry.getValue().equals(immEntry.getValue()))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                return false;
            }
        }
        return true;
    }

}
