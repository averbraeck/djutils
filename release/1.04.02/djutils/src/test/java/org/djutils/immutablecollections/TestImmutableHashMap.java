package org.djutils.immutablecollections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import org.djutils.immutablecollections.ImmutableMap.ImmutableEntry;
import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableHashMap.java.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableHashMap
{

    /**
     * Test most of the equals and hashCode methods and the forEach method of the ImmutableAbstractMap class.
     */
    @SuppressWarnings({ "unlikely-arg-type" })
    @Test
    public final void testMapEqualsAndHashCode()
    {
        Integer[] keys = new Integer[] { 10, 20, 30, 40 };
        Map<Integer, Double> mutableMap1 = new HashMap<>();
        Map<Integer, Double> mutableMap2 = new HashMap<>();
        for (Integer key : keys)
        {
            mutableMap1.put(key, Math.PI + key);
            mutableMap2.put(key, Math.PI + key);
        }
        assertEquals("maps with same content should be equal", mutableMap1, mutableMap2);
        assertEquals("maps with same content should have same hash code", mutableMap1.hashCode(), mutableMap2.hashCode());
        // No see that the same logic holds for our immutable maps
        ImmutableMap<Integer, Double> im1 = new ImmutableHashMap<>(mutableMap1, Immutable.WRAP);
        assertFalse(im1.isCopy());
        ImmutableMap<Integer, Double> im2 = new ImmutableHashMap<>(mutableMap2, Immutable.WRAP);
        assertEquals("immutable maps with same content should be equal", im1, im2);
        assertEquals("immutable maps with same content should have same hash code", im1.hashCode(), im2.hashCode());
        im2 = new ImmutableHashMap<>(mutableMap2, Immutable.COPY);
        assertEquals("immutable maps with same content should be equal", im1, im2);
        assertEquals("immutable maps with same content should be equal", im2, im1);
        im1 = new ImmutableHashMap<>(mutableMap1, Immutable.COPY);
        assertTrue(im1.isCopy());
        assertEquals("immutable maps with same content should be equal", im1, im2);
        assertEquals("immutable maps with same content should be equal", im2, im1);
        // test the short cut path in equals
        assertEquals("immutable map is equal to itself", im1, im1);
        assertFalse("immutable map is not equal to null", im1.equals(null));
        assertFalse("immutable map is not equal to some totally different object", im1.equals("abc"));
        mutableMap2.put(keys[0], Math.E);
        assertFalse("altered mutable map differs", mutableMap1.equals(mutableMap2));
        assertEquals("immutable map holding copy is not altered", im1, im2);
        ImmutableMap<Integer, Double> im1Wrap = new ImmutableHashMap<>(mutableMap1, Immutable.WRAP);
        assertEquals("another immutable map from the same collection is equal", im1, im1Wrap);
        assertEquals("another immutable map from the same collection has same hash code", im1.hashCode(), im1Wrap.hashCode());
        mutableMap1.put(keys[0], -Math.PI);
        assertFalse("wrapped immutable map re-checks content", im1.equals(im1Wrap));
        assertFalse("wrapped immutable map re-checks content", im1Wrap.equals(im1));
        assertFalse("wrapped immutable map re-computes hash code", im1.hashCode() == im1Wrap.hashCode());
        assertFalse("wrapped immutable map re-computes hash code", im1Wrap.hashCode() == im1.hashCode());
        // Test the get method
        assertNull("result of get for non-existent key returns null", im1.get(-123));
        for (Integer key : keys)
        {
            assertEquals("Immutable map returns same as underlying mutable map", mutableMap1.get(key), im1Wrap.get(key));
        }
        ImmutableMap<Integer, Double> map3 =
                new ImmutableHashMap<>((ImmutableAbstractMap<Integer, Double>) im1Wrap, Immutable.WRAP);
        assertEquals("immutable map constructed by wrapping another immutable map is equals", im1Wrap, map3);
        map3 = new ImmutableHashMap<>((ImmutableAbstractMap<Integer, Double>) im1Wrap, Immutable.COPY);
        assertEquals("immutable map constructed by copyinig another immutable map is equals", im1Wrap, map3);
        assertTrue("toString returns something descriptive", map3.toString().startsWith("ImmutableHashMap ["));
        assertEquals("get with default returns value for key when it exists", mutableMap1.get(keys[0]),
                map3.getOrDefault(keys[0], Math.asin(2.0)));
        assertEquals("get with default returns default for key when it does not exist", Math.asin(2.0),
                map3.getOrDefault(-123, Math.asin(2.0)), 0.00001);
        final ImmutableMap<Integer, Double> map4 =
                new ImmutableHashMap<Integer, Double>((ImmutableAbstractMap<Integer, Double>) im1Wrap, Immutable.WRAP);
        boolean[] tested = new boolean[keys.length];
        map3.forEach(new BiConsumer<Integer, Double>()
        {
            @Override
            public void accept(Integer t, Double u)
            {
                assertEquals("accept got a value that matches the key", u, map4.get(t), 0.0001);
                int index = -1;
                for (int i = 0; i < keys.length; i++)
                {
                    if (keys[i] == t)
                    {
                        index = i;
                    }
                }
                assertTrue("key is contained in keys", index >= 0);
                assertFalse("key has not appeared before", tested[index]);
                tested[index] = true;
            }
        });
        for (int index = 0; index < tested.length; index++)
        {
            assertTrue("each index got tested", tested[index]);
        }
    }

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
            Assert.assertTrue(imMap.size() == 10);
        else
            Assert.assertTrue(imMap.size() == 11);
    }

    private boolean sameContent(Collection<?> a, Collection<?> b)
    {
        return a.containsAll(b) && b.containsAll(b);
    }

    private boolean checkEntrySets(Set<Entry<Integer, Integer>> es, Set<ImmutableEntry<Integer, Integer>> ies)
    {
        if (es.size() != ies.size())
            return false;
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
                return false;
        }
        return true;
    }

}
