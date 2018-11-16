package org.djutils.immutablecollections;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutable.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * www.simulation.tudelft.nl</a>. The source code and binary code of this software is proprietary information of Delft
 * University of Technology.
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
