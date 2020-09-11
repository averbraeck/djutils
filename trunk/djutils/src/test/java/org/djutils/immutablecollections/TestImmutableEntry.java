package org.djutils.immutablecollections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Map.Entry;

import org.djutils.immutablecollections.ImmutableMap.ImmutableEntry;
import org.junit.Test;

/**
 * Test the ImmutableEntry sub class.
 * <p>
 * Copyright (c) 2013-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 20, 2020 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestImmutableEntry
{

    /**
     * Test the ImmutableEntry sub class.
     */
    @Test
    public void testImmutableEntry()
    {
        MyEntry<String, String> entry = new MyEntry<>("Key", "Value");
        ImmutableEntry<String, String> ie = new ImmutableEntry<>(entry);
        assertEquals("key must be retrievable", entry.getKey(), ie.getKey());
        assertEquals("value must be retrievable", entry.getValue(), ie.getValue());
        assertEquals("ie is equal to itself", ie, ie);
        Comparator<ImmutableEntry<String, String>> keyComparator = ImmutableEntry.comparingByKey();
        Comparator<ImmutableEntry<String, String>> valueComparator = ImmutableEntry.comparingByValue();
        Comparator<String> reverseComparator = new Comparator<String>()
        {
            @Override
            public int compare(final String o1, final String o2)
            {
                return o2.compareTo(o1); // swapped arguments
            }
        };
        Comparator<ImmutableEntry<String, String>> ownKeyComparator = ImmutableEntry.comparingByKey(reverseComparator);
        Comparator<ImmutableEntry<String, String>> ownValueComparator = ImmutableEntry.comparingByValue(reverseComparator);
        assertEquals("keyComparator returns 0 when comparing ie to itself", 0, keyComparator.compare(ie, ie));
        assertEquals("valueComparator returns 0 when comparing ie to itself", 0, valueComparator.compare(ie, ie));
        assertEquals("ownKeyComparator returns 0 when comparing ie to itself", 0, ownKeyComparator.compare(ie, ie));
        assertEquals("ownValueComparator returns 0 when comparing ie to itself", 0, ownValueComparator.compare(ie, ie));
        ImmutableEntry<String, String> ie2 = new ImmutableEntry<>(entry);
        assertEquals("ie has same hashCode as ie2 (which wraps the same MyEntry)", ie.hashCode(), ie2.hashCode());
        assertEquals("ie is equal to another ie embedding the same entry", ie, ie2);
        assertNotEquals("ie is not equal to null", ie, null);
        assertNotEquals("ie is not equal to some unrelated object", ie, "Hello");
        ie2 = new ImmutableEntry<>(null);
        assertNotEquals("ie is not equal to ie embedding null", ie, ie2);
        assertNotEquals("ie embedding null is not equal to ie embedding non-null", ie2, ie);
        MyEntry<String, String> entry2 = new MyEntry<>("Key", "DifferentValue");
        ie2 = new ImmutableEntry<>(entry2);
        assertNotEquals("ie is not equal to other ie embedding same key but different value", ie, ie2);
        assertEquals("comparator returns 0 when comparing ie to other that has same key but different value", 0,
                keyComparator.compare(ie, ie2));
        entry2 = new MyEntry<>("Key2", "Value2");
        ie2 = new ImmutableEntry<>(entry2);
        System.out.println(ie + " " + ie2 + " " + keyComparator.compare(ie, ie2));
        assertTrue("keyComparator returns < 0 when comparing objects that are in natural order",
                keyComparator.compare(ie, ie2) < 0);
        assertTrue("keyComparator returns > 0 when comparing objects that are in reverse natural order",
                keyComparator.compare(ie2, ie) > 0);
        assertTrue("ownKeyComparator returns > 0 when comparing objects that are in natural order",
                ownKeyComparator.compare(ie, ie2) > 0);
        assertTrue("ownKeyComparator returns < 0 when comparing objects that are in reverse natural order",
                ownKeyComparator.compare(ie2, ie) < 0);
        assertTrue("valueComparator returns < 0 when comparing objects that are in natural order",
                valueComparator.compare(ie, ie2) < 0);
        assertTrue("valueComparator returns > 0 when comparing objects that are in reverse natural order",
                valueComparator.compare(ie2, ie) > 0);
        assertTrue("ownValueComparator returns > 0 when comparing objects that are in natural order",
                ownValueComparator.compare(ie, ie2) > 0);
        assertTrue("ownValueComparator returns > 0 when comparing objects that are in reverse natural order",
                ownValueComparator.compare(ie2, ie) < 0);
        ie = new ImmutableEntry<>(null);
        ie2 = new ImmutableEntry<>(null);
        assertEquals("ie embedding null is equal to another that also embeds null", ie, ie2);
    }

    /**
     * Simple implementation of Entry interface.
     * @param <K> type of the key
     * @param <V> type of the value
     */
    public static class MyEntry<K, V> implements Entry<K, V>
    {
        /** The key. */
        private final K key;

        /** The value. */
        private V value;

        /**
         * Construct a new MyEntry object.
         * @param key K; key of the entry
         * @param value V; value of the entry
         */
        MyEntry(final K key, final V value)
        {
            this.key = key;
            this.value = value;
        }

        /** {@inheritDoc} */
        @Override
        public final K getKey()
        {
            return this.key;
        }

        /** {@inheritDoc} */
        @Override
        public final V getValue()
        {
            return this.value;
        }

        /** {@inheritDoc} */
        @Override
        public final V setValue(final V newValue)
        {
            this.value = newValue;
            return this.value;
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return "MyEntry [key=" + this.key + ", value=" + this.value + "]";
        }

    }

}
