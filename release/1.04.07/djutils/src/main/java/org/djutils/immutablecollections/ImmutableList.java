package org.djutils.immutablecollections;

import java.util.List;

/**
 * A List interface without the methods that can change it. The constructor of the ImmutableList needs to be given an initial
 * List.
 * <p>
 * Copyright (c) 2016-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <E> the type of content of this List
 */
public interface ImmutableList<E> extends ImmutableCollection<E>
{
    /**
     * Returns the element at the specified position in this immutable list.
     * @param index int; index of the element to return
     * @return the element at the specified position in this immutable list
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    E get(int index);

    /**
     * Returns the index of the first occurrence of the specified element in this immutable list, or -1 if this immutable list
     * does not contain the element. More formally, returns the lowest index <code>i</code> such that
     * <code>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</code>, or -1 if there is no such index.
     * @param o Object; element to search for
     * @return the index of the first occurrence of the specified element in this immutable list, or -1 if this immutable list
     *         does not contain the element
     * @throws ClassCastException if the type of the specified element is incompatible with this immutable list
     * @throws NullPointerException if the specified element is null and this immutable list does not permit null elements
     */
    int indexOf(Object o);

    /**
     * Returns the index of the last occurrence of the specified element in this immutable list, or -1 if this immutable list
     * does not contain the element. More formally, returns the highest index <code>i</code> such that
     * <code>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</code>, or -1 if there is no such index.
     * @param o Object; element to search for
     * @return the index of the last occurrence of the specified element in this immutable list, or -1 if this immutable list
     *         does not contain the element
     * @throws ClassCastException if the type of the specified element is incompatible with this immutable list
     * @throws NullPointerException if the specified element is null and this immutable list does not permit null elements
     */
    int lastIndexOf(Object o);

    /**
     * Returns a safe, immutable copy of the portion of this immutable list between the specified <code>fromIndex</code>,
     * inclusive, and <code>toIndex</code>, exclusive. (If <code>fromIndex</code> and <code>toIndex</code> are equal, the
     * returned immutable list is empty).
     * @param fromIndex int; low endpoint (inclusive) of the subList
     * @param toIndex int; high endpoint (exclusive) of the subList
     * @return a view of the specified range within this immutable list
     * @throws IndexOutOfBoundsException for an illegal endpoint index value (<code>fromIndex &lt; 0 || toIndex &gt; size ||
     *         fromIndex &gt; toIndex</code>)
     */
    ImmutableList<E> subList(int fromIndex, int toIndex);

    /**
     * Returns a modifiable copy of this immutable list.
     * @return a modifiable copy of this immutable list.
     */
    List<E> toList();

    /**
     * Force to redefine equals for the implementations of immutable collection classes.
     * @param obj Object; the object to compare this collection with
     * @return whether the objects are equal
     */
    @Override
    boolean equals(Object obj);

    /**
     * Force to redefine hashCode for the implementations of immutable collection classes.
     * @return the calculated hashCode
     */
    @Override
    int hashCode();

    /**
     * Force to redefine toString.
     * @return String; a description of this immutable list
     */
    @Override
    String toString();

}