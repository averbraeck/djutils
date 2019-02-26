package org.djutils.immutablecollections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Static methods operating on immutable collections, or a mix of an immutable collection and a mutable collection.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Feb 26, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class ImmutableCollections
{
    /**
     * There is never a need to instantiate an object of this class.
     */
    private ImmutableCollections()
    {
        // Do not instantiate
    }
    
    /**
     * Return an immutable empty set.
     * @return ImmutableSet&lt;T&gt;; an immutable empty set
     */
    public static <T extends Object> ImmutableSet<T> emptyImmutableSet()
    {
        return new ImmutableHashSet<T>(Collections.emptySet(), Immutable.WRAP);
    }

    /**
     * Return an immutable empty map.
     * @return ImmutableMap&lt;T, V&gt;; an immutable empty map
     */
    public static <T extends Object, V extends Object> ImmutableMap<T, V> emptyImmutableMap()
    {
        return new ImmutableHashMap<T, V>(Collections.emptyMap(), Immutable.WRAP);
    }

    /**
     * Return an immutable empty list.
     * @return ImmutableList&lt;T&gt;; an immutable empty list
     */
    public static <T extends Object> ImmutableList<T> emptyImmutableList()
    {
        return new ImmutableArrayList<T>(Collections.emptyList(), Immutable.WRAP);
    }

    /**
     * Search the immutable list for the specified object using the binary search algorithm. The list must be ordered according
     * to the natural ordering of its elements.
     * @param il ImmutableList&lt;?&gt;; the list (must be ordered according to the natural ordering of <code>T</code>
     * @param key T; the element to search for.
     * @return int; if <code>key</code> is present in the list, the index of <code>key</code>, or, when <code>key</code> is not
     *         present in the list <code>(-(<i>insertion point</i>) - 1)</code> where <code>insertion point</code> is the index
     *         of <code>key</code> if it were contained in the list.
     */
    public static <T> int binarySearch(ImmutableList<? extends Comparable<? super T>> il, T key)
    {
        return Collections.binarySearch(il.toList(), key);
    }

    /**
     * Search the immutable list for the specified object using the binary search algorithm. The list must be ordered according
     * to the comparator.
     * @param il ImmutableList&lt;?&gt;; the list (must be ordered according to the <code>comparator</code>
     * @param key T; the element to search for.
     * @param comparator Comparator&lt;? super T&gt;; a comparator for <code>T</code>
     * @return int; if <code>key</code> is present in the list, the index of <code>key</code>, or, when <code>key</code> is not
     *         present in the list <code>(-(<i>insertion point</i>) - 1)</code> where <code>insertion point</code> is the index
     *         of <code>key</code> if it were contained in the list.
     */
    public static <T> int binarySearch(ImmutableList<? extends T> il, T key, Comparator<? super T> comparator)
    {
        return Collections.binarySearch(il.toList(), key, comparator);
    }

    /**
     * Determine if two immutable collections have no common members.
     * @param ic1 ImmutableCollection&lt;?&gt; one immutable collection
     * @param ic2 ImmutableCollection&lt;?&gt; another immutable collection
     * @return boolean; true if the collections have no common members; false if the collections have at least one member in
     *         common
     */
    public static boolean disjoint(ImmutableCollection<?> ic1, ImmutableCollection<?> ic2)
    {
        return Collections.disjoint(ic1.toCollection(), ic2.toCollection());
    }

    /**
     * Determine if an immutable collection and a (non immutable) collection have no common members
     * @param ic1 ImmutableCollection&lt;?&gt; an immutable collection
     * @param c2 Collection&lt;?&gt; a (mutable) collection
     * @return boolean; true if the collections have no common members; false if the collections have at least one member in
     *         common
     */
    public static boolean disjoint(ImmutableCollection<?> ic1, Collection<?> c2)
    {
        return Collections.disjoint(ic1.toCollection(), c2);
    }

    /**
     * Determine if an immutable collection and a (non immutable) collection have no common members
     * @param c1 Collection&lt;?&gt; a (mutable) collection
     * @param ic2 ImmutableCollection&lt;?&gt; an immutable collection
     * @return boolean; true if the collections have no common members; false if the collections have at least one member in
     *         common
     */
    public static boolean disjoint(Collection<?> c1, ImmutableCollection<?> ic2)
    {
        return Collections.disjoint(c1, ic2.toCollection());
    }

    /**
     * Returns the starting position of the first occurrence of the specified target list within the specified source list, or
     * -1 if there is no such occurrence.
     * @param source ImmutableList&lt;?&gt;; the list in which to find the first occurrence of <code>target</code>
     * @param target ImmutableList&lt;?&gt;; the pattern to find in <code>source</code>
     * @return int; the index in <code>source</code> of the first occurrence of <code>target</code> or -1 if <code>target</code>
     *         does not occur anywhere in <code>source</code>
     */
    public static int indexOfSubList(ImmutableList<?> source, ImmutableList<?> target)
    {
        return Collections.indexOfSubList(source.toList(), target.toList());
    }

    /**
     * Returns the starting position of the first occurrence of the specified target list within the specified source list, or
     * -1 if there is no such occurrence.
     * @param source ImmutableList&lt;?&gt;; the list in which to find the first occurrence of <code>target</code>
     * @param target List&lt;?&gt;; the pattern to find in <code>source</code>
     * @return int; the index in <code>source</code> of the first occurrence of <code>target</code> or -1 if <code>target</code>
     *         does not occur anywhere in <code>source</code>
     */
    public static int indexOfSubList(ImmutableList<?> source, List<?> target)
    {
        return Collections.indexOfSubList(source.toList(), target);
    }

    /**
     * Returns the starting position of the first occurrence of the specified target list within the specified source list, or
     * -1 if there is no such occurrence.
     * @param source List&lt;?&gt;; the list in which to find the first occurrence of <code>target</code>
     * @param target ImmutableList&lt;?&gt;; the pattern to find in <code>source</code>
     * @return int; the index in <code>source</code> of the first occurrence of <code>target</code> or -1 if <code>target</code>
     *         does not occur anywhere in <code>source</code>
     */
    public static int indexOfSubList(List<?> source, ImmutableList<?> target)
    {
        return Collections.indexOfSubList(source, target.toList());
    }

    /**
     * Returns the starting position of the last occurrence of the specified target list within the specified source list, or -1
     * if there is no such occurrence.
     * @param source ImmutableList&lt;?&gt;; the list in which to find the last occurrence of <code>target</code>
     * @param target ImmutableList&lt;?&gt;; the pattern to find in <code>source</code>
     * @return int; the index in <code>source</code> of the last occurrence of <code>target</code> or -1 if <code>target</code>
     *         does not occur anywhere in <code>source</code>
     */
    public static int lastIndexOfSubList(ImmutableList<?> source, ImmutableList<?> target)
    {
        return Collections.lastIndexOfSubList(source.toList(), target.toList());
    }

    /**
     * Returns the starting position of the last occurrence of the specified target list within the specified source list, or -1
     * if there is no such occurrence.
     * @param source ImmutableList&lt;?&gt;; the list in which to find the last occurrence of <code>target</code>
     * @param target List&lt;?&gt;; the pattern to find in <code>source</code>
     * @return int; the index in <code>source</code> of the last occurrence of <code>target</code> or -1 if <code>target</code>
     *         does not occur anywhere in <code>source</code>
     */
    public static int lastIndexOfSubList(ImmutableList<?> source, List<?> target)
    {
        return Collections.lastIndexOfSubList(source.toList(), target);
    }

    /**
     * Returns the starting position of the last occurrence of the specified target list within the specified source list, or -1
     * if there is no such occurrence.
     * @param source List&lt;?&gt;; the list in which to find the last occurrence of <code>target</code>
     * @param target ImmutableList&lt;?&gt;; the pattern to find in <code>source</code>
     * @return int; the index in <code>source</code> of the last occurrence of <code>target</code> or -1 if <code>target</code>
     *         does not occur anywhere in <code>source</code>
     */
    public static int lastIndexOfSubList(List<?> source, ImmutableList<?> target)
    {
        return Collections.indexOfSubList(source, target.toList());
    }

    /**
     * Returns the maximum element of an immutable collection according to the <i>natural ordering</i> of its elements.
     * @param ic ImmutableCollection&ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the immutable collection
     * @return T &ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the maximum element in the immutable collection
     * @throws NoSuchElementException if the immutable collection is empty.
     */
    public static <T extends Object & Comparable<? super T>> T max(ImmutableCollection<? extends T> ic)
    {
        return Collections.max(ic.toCollection());
    }

    /**
     * Returns the maximum element of an immutable collection according to the <i>natural ordering</i> of its elements.
     * @param ic ImmutableCollection&ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the immutable collection
     * @param comparator Comparator&lt;? super T&gt;; a comparator for <code>T</code>
     * @return T &ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the maximum element in the immutable collection
     * @throws NoSuchElementException if the immutable collection is empty.
     */
    public static <T extends Object> T max(ImmutableCollection<? extends T> ic, Comparator<? super T> comparator)
    {
        return Collections.max(ic.toCollection(), comparator);
    }

    /**
     * Returns the minimum element of an immutable collection according to the <i>natural ordering</i> of its elements.
     * @param ic ImmutableCollection&ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the immutable collection
     * @return T &ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the minimum element in the immutable collection
     * @throws NoSuchElementException if the immutable collection is empty.
     */
    public static <T extends Object & Comparable<? super T>> T min(ImmutableCollection<? extends T> ic)
    {
        return Collections.min(ic.toCollection());
    }

    /**
     * Returns the minimum element of an immutable collection according to the <i>natural ordering</i> of its elements.
     * @param ic ImmutableCollection&ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the immutable collection
     * @param comparator Comparator&lt;? super T&gt;; a comparator for <code>T</code>
     * @return T &ltT extends Object &amp; Comparable&lt;? super T&gt;&gt;; the minimum element in the immutable collection
     * @throws NoSuchElementException if the immutable collection is empty.
     */
    public static <T extends Object> T min(ImmutableCollection<? extends T> ic, Comparator<? super T> comparator)
    {
        return Collections.min(ic.toCollection(), comparator);
    }
    
    /**
     * Return the number of occurrences of an object in an immutable collection.
     * @param ic ImmutableCollection; the immutable collection
     * @param o Object; the object to count the number occurrences of
     * @return int; the number of occurrences of <code>o</code> in <code>ic</code>
     */
    public static int frequency(ImmutableCollection<?> ic, Object o)
    {
        return Collections.frequency(ic.toCollection(), o);
    }

}
