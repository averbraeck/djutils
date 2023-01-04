package org.djutils.multikeymap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * Test the MultiKeyMap class.
 * <p>
 * Copyright (c) 2013-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TestMultiKeyMap
{

    /**
     * Test the MultiKeyMap class.
     */
    @Test
    public void testMultiKeyMap()
    {
        try
        {
            new MultiKeyMap<>();
            fail("empty key list should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        MultiKeyMap<String> mkm = new MultiKeyMap<>(String.class);
        try
        {
            mkm.get(123.456);
            fail("key of wrong type should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        assertNull("non existent key return null", mkm.get("abc"));
        mkm.put("value1", "key1");
        assertEquals("existing key return value for that  key", "value1", mkm.get("key1"));
        mkm.put("value2", "key2");
        assertEquals("existing key return value for that  key", "value1", mkm.get("key1"));
        assertEquals("existing key return value for that  key", "value2", mkm.get("key2"));
        mkm.clear("value1");
        assertNull("no longer existent key return null", mkm.get("value1"));
        assertEquals("existing key return value for that  key", "value2", mkm.get("key2"));
        String result = mkm.get(new Supplier<String>()
        {
            @Override
            public String get()
            {
                return "newValue3";
            }
        }, "key3");
        assertEquals("result is new value", "newValue3", result);
        assertEquals("existing key return value for that  key", "newValue3", mkm.get("key3"));
        String oldValue = mkm.put("newValue3", "key3");
        assertEquals("existing key returns new value for that  key", "newValue3", mkm.get("key3"));
        assertEquals("put has returned old value", "newValue3", oldValue);
        result = mkm.get(new Supplier<String>()
        {
            @Override
            public String get()
            {
                fail("get method in Supplier should not have been called");
                return "newNewalue3";
            }
        }, "key3");
        assertEquals("result is unchanged", "newValue3", result);

        try
        {
            mkm.get("k", "l", "m");
            fail("Wrong number of keys should have thrown an exeption");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            mkm.get();
            fail("Wrong number of keys should have thrown an exeption");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        mkm = new MultiKeyMap<>(String.class, Double.class);
        result = mkm.get("k", 123.456);
        assertNull("result should be null", result);
        mkm.put("dummy", "1", 123.456);
        assertEquals("two step key works", "dummy", mkm.get("1", 123.456));
        assertNull("two step key works", mkm.get("1", 123.457));
        try
        {
            mkm.get("1", "2");
            fail("Wrong type of last key should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        Set<Object> keySet = mkm.getKeys();
        assertEquals("there is one key at level 0", 1, keySet.size());
        assertEquals("type of key is String", String.class, keySet.iterator().next().getClass());
        assertEquals("object is string with value \"1\"", "1", keySet.iterator().next());
        keySet = mkm.getKeys("1");
        assertEquals("there is one key at level 1", 1, keySet.size());
        assertEquals("type of key is Double", Double.class, keySet.iterator().next().getClass());
        assertEquals("object is Double with value 123.456", 123.456, keySet.iterator().next());
        try
        {
            mkm.getKeys("1", 123.456, "3");
            fail("too many keys should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        assertNull("Clearing non-existant sub map returns null", mkm.clear("2", 123.4));
        Object o = mkm.clear("1", 123.456);
        assertEquals("result of clear is removed object", "dummy", o);
        assertNull("dummy is no longer in the map", mkm.get("1", 123.456));
        mkm.put("dummy", "1", 123.456);
        assertEquals("dummy is back", "dummy", mkm.get("1", 123.456));
        mkm.put("dummy2", "2", 23.456);
        MultiKeyMap<String> subMap = mkm.getSubMap();
        assertEquals("Top level sub map ", subMap, mkm);
        subMap = mkm.getSubMap("1");
        assertEquals("level one sub map contains dummy", "dummy", subMap.get(123.456));
        try
        {
            mkm.getSubMap("1", 123.456);
            fail("Too many arguments should have thrown an exception");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        mkm.clear("1");
        assertNull("dummy was removed", mkm.get("1", 123.456));
        assertEquals("dummy2 is still there", "dummy2", mkm.get("2", 23.456));
        mkm.clear();
        assertEquals("result of clear at top level clears the entire map", 0, mkm.getKeys().size());

        assertTrue("toString returns something descriptive", mkm.toString().startsWith("MultiKeyMap ["));
    }
}
