package org.djutils.immutablecollections;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutableVector.java.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableVector
{

    /**
     * Test vectors.
     */
    @Test
    public final void testVector()
    {
        Integer[] testData = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Vector<Integer> intVector = new Vector<>(Arrays.asList(testData));
        Vector<Integer> vector = new Vector<Integer>(intVector);
        testIntVector(vector, new ImmutableVector<Integer>(vector, Immutable.WRAP), Immutable.WRAP);
        vector = new Vector<Integer>(intVector);
        testIntVector(vector, new ImmutableVector<Integer>(vector, Immutable.COPY), Immutable.COPY);
        vector = new Vector<Integer>(intVector);
        testIntVector(vector, new ImmutableVector<Integer>(vector), Immutable.COPY);
        vector = new Vector<Integer>(intVector);
        ImmutableVector<Integer> ial = new ImmutableVector<Integer>(vector);
        testIntVector(vector, new ImmutableVector<Integer>(ial), Immutable.COPY);

        vector = new Vector<Integer>(intVector);
        Set<Integer> intSet = new HashSet<>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        testIntVector(vector, new ImmutableVector<Integer>(intSet), Immutable.COPY);
        ImmutableVector<Integer> iv = new ImmutableVector<Integer>(vector);
        Assert.assertTrue("toString returns something descriptive", iv.toString().startsWith("ImmutableVector ["));
        ImmutableVector<Integer> iv2 = new ImmutableVector<>(iv, Immutable.COPY);
        Assert.assertEquals("ImmutableVector with copy of other ImmutableVector tests equal to it", iv, iv2);
        Assert.assertEquals("ImmutableVector with copy of other ImmutableVector has same hash code", iv.hashCode(),
                iv2.hashCode());
        iv2 = new ImmutableVector<>(iv, Immutable.WRAP);
        Assert.assertEquals("ImmutableVector wrapping other ImmutableVector tests equal to it", iv, iv2);
        Assert.assertEquals("ImmutableVector wrapping other ImmutableVector has same hash code", iv.hashCode(), iv2.hashCode());
        // start anew as the testIntVector method modifies the underlying data.
        intVector = new Vector<>(Arrays.asList(testData));
        iv = new ImmutableVector<Integer>(intVector);
        ImmutableList<Integer> subList = iv.subList(2, 5);
        Assert.assertEquals("size of sub list is 3", 3, subList.size());
        for (int index = 0; index < subList.size(); index++)
        {
            Assert.assertEquals("value at index matches", iv.get(index + 2), subList.get(index));
        }
        try
        {
            iv.subList(-1, 3);
            Assert.fail("Negative from index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }
        try
        {
            iv.subList(1, iv.size() + 1);
            Assert.fail("To index bigger than size should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }
        try
        {
            iv.subList(5, 4);
            Assert.fail("negative range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        subList = iv.subList(4, 4);
        Assert.assertEquals("sub list should be empty", 0, subList.size());
        Integer[] justRight = new Integer[iv.size()];
        iv.copyInto(justRight);
        for (int index = 0; index < iv.size(); index++)
        {
            Assert.assertEquals("contents of array matches", iv.get(index), justRight[index]);
        }
        Integer[] bigger = new Integer[iv.size() + 3];
        bigger[bigger.length - 2] = -1;
        iv.copyInto(bigger);
        for (int index = 0; index < iv.size(); index++)
        {
            Assert.assertEquals("contents of array matches", iv.get(index), justRight[index]);
        }
        Assert.assertEquals("element after required length is still null", null, bigger[iv.size()]);
        Assert.assertEquals("element at length - 2 is still -1", -1, bigger[bigger.length - 2], 0);
        Assert.assertEquals("element at length - 1 is null", null, bigger[bigger.length - 1]);

        Integer[] tooShort = new Integer[iv.size() - 1];
        try
        {
            iv.copyInto(tooShort);
            Assert.fail("Too short target array should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }
        Assert.assertTrue("capacity returns capacity of the underlying collection", iv.capacity() >= testData.length);

        Enumeration<Integer> e = iv.elements();
        for (int index = 0; index < testData.length; index++)
        {
            Assert.assertTrue("There is another element to be had", e.hasMoreElements());
            Integer got = e.nextElement();
            Assert.assertEquals("element at index matches", testData[index], got);
        }
        Assert.assertFalse("there are no more elements to be had", e.hasMoreElements());
        for (int index = 0; index < testData.length; index++)
        {
            int indexOf = iv.indexOf(testData[index]);
            Assert.assertEquals("index matches", index, indexOf);
            Assert.assertEquals("value at index matches", testData[index], iv.get(indexOf));
            indexOf = iv.lastIndexOf(testData[index]);
            Assert.assertEquals("index matches", index, indexOf);
            int noIndex = iv.indexOf(testData[index], indexOf + 1);
            Assert.assertEquals("there is no later next index for this value", -1, noIndex);
            noIndex = iv.lastIndexOf(testData[index], indexOf - 1);
            Assert.assertEquals("there is no earlier next index for this value", -1, noIndex);
            Assert.assertEquals("get returns same as elementAt", iv.get(index), iv.elementAt(index));
        }
        Assert.assertEquals("firstElement returns first element", testData[0], iv.firstElement());
        Assert.assertEquals("lastElement returns last element", testData[testData.length - 1], iv.lastElement());
    }

    /**
     * ...
     * @param vector Vector&lt;Integer&gt;; a vector of Integer
     * @param imVector ImmutableVector&lt;Integer&gt;; an immutable vector of Integer
     * @param copyOrWrap Immutable
     */
    private void testIntVector(final Vector<Integer> vector, final ImmutableVector<Integer> imVector,
            final Immutable copyOrWrap)
    {
        Assert.assertTrue(vector.size() == 10);
        Assert.assertTrue(imVector.size() == 10);
        for (int i = 0; i < 10; i++)
        {
            Assert.assertTrue(imVector.get(i) == vector.get(i));
        }
        Assert.assertFalse(imVector.isEmpty());
        Assert.assertTrue(imVector.contains(5));
        Assert.assertFalse(imVector.contains(15));
        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imVector.isCopy());
            Assert.assertTrue(imVector.toList().equals(vector));
            Assert.assertFalse(imVector.toList() == vector);
        }
        else
        {
            Assert.assertTrue(imVector.isWrap());
            Assert.assertTrue(imVector.toList().equals(vector));
            Assert.assertFalse(imVector.toList() == vector); // this WRAP method returns a NEW list
        }

        Vector<Integer> to = imVector.toVector();
        Assert.assertTrue(vector.equals(to));

        Integer[] arr = imVector.toArray(new Integer[] {});
        Integer[] sar = vector.toArray(new Integer[] {});
        Assert.assertArrayEquals(arr, sar);

        // modify the underlying data structure
        vector.add(11);
        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imVector.size() == 10);
        }
        else
        {
            Assert.assertTrue(imVector.size() == 11);
        }
    }
}
