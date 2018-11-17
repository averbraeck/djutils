package org.djutils.immutablecollections;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableHashMap.java. 
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableHashMap
{

    @Test
    public final void testHashMap()
    {
        Map<Integer, Integer> isMap = new HashMap<>();
        for (int i = 1; i <= 10; i++)
            isMap.put(i, 100 * i);
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableHashMap<Integer, Integer>(map, Immutable.WRAP), Immutable.WRAP);
        map = new HashMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableHashMap<Integer, Integer>(map, Immutable.COPY), Immutable.COPY);
        map = new HashMap<Integer, Integer>(isMap);
        testIntMap(map, new ImmutableHashMap<Integer, Integer>(map), Immutable.COPY);
        map = new HashMap<Integer, Integer>(isMap);
        ImmutableHashMap<Integer, Integer> ihs = new ImmutableHashMap<Integer, Integer>(map);
        testIntMap(map, new ImmutableHashMap<Integer, Integer>(ihs), Immutable.COPY);
    }

    private void testIntMap(final Map<Integer, Integer> map, final ImmutableMap<Integer, Integer> imMap,
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
}
