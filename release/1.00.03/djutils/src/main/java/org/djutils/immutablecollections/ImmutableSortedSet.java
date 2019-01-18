package org.djutils.immutablecollections;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

/**
 * A SortedSet interface without the methods that can change it. The return values of subSet, tailSet and headSet are
 * all ImmutableSortedSets.
 * <p>
 * Copyright (c) 2016-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <E> the type of content of this Set
 */
public interface ImmutableSortedSet<E> extends ImmutableSet<E>
{
    /**
     * Returns a modifiable copy of this immutable set.
     * @return a modifiable copy of this immutable set.
     */
    @Override
    SortedSet<E> toSet();

    /**
     * Returns the comparator used to order the elements in this immutable set, or <tt>null</tt> if this immutable set
     * uses the {@linkplain Comparable natural ordering} of its elements.
     * @return the comparator used to order the elements in this immutable set, or <tt>null</tt> if this immutable set
     *         uses the natural ordering of its elements
     */
    Comparator<? super E> comparator();

    /**
     * Returns a view of the portion of this immutable set whose elements range from <tt>fromElement</tt>, inclusive, to
     * <tt>toElement</tt>, exclusive. (If <tt>fromElement</tt> and <tt>toElement</tt> are equal, the returned immutable
     * set is empty.)
     * <p>
     * The result of this method is a new, immutable sorted set.
     * @param fromElement E; low endpoint (inclusive) of the returned immutable set
     * @param toElement E; high endpoint (exclusive) of the returned immutable set
     * @return a new, immutable sorted set of the portion of this immutable set whose elements range from
     *         <tt>fromElement</tt>, inclusive, to <tt>toElement</tt>, exclusive
     * @throws ClassCastException if <tt>fromElement</tt> and <tt>toElement</tt> cannot be compared to one another using
     *             this immutable set's comparator (or, if the immutable set has no comparator, using natural ordering).
     *             Implementations may, but are not required to, throw this exception if <tt>fromElement</tt> or
     *             <tt>toElement</tt> cannot be compared to elements currently in the immutable set.
     * @throws NullPointerException if <tt>fromElement</tt> or <tt>toElement</tt> is null and this immutable set does
     *             not permit null elements
     * @throws IllegalArgumentException if <tt>fromElement</tt> is greater than <tt>toElement</tt>; or if this immutable
     *             set itself has a restricted range, and <tt>fromElement</tt> or <tt>toElement</tt> lies outside the
     *             bounds of the range
     */
    ImmutableSortedSet<E> subSet(E fromElement, E toElement);

    /**
     * Returns a view of the portion of this immutable set whose elements are strictly less than <tt>toElement</tt>. The
     * returned immutable set is backed by this immutable set, so changes in the returned immutable set are reflected in
     * this immutable set, and vice-versa. The returned immutable set supports all optional immutable set operations
     * that this immutable set supports.
     * <p>
     * The result of this method is a new, immutable sorted set.
     * @param toElement E; high endpoint (exclusive) of the returned immutable set
     * @return a view of the portion of this immutable set whose elements are strictly less than <tt>toElement</tt>
     * @throws ClassCastException if <tt>toElement</tt> is not compatible with this immutable set's comparator (or, if
     *             the immutable set has no comparator, if <tt>toElement</tt> does not implement {@link Comparable}).
     *             Implementations may, but are not required to, throw this exception if <tt>toElement</tt> cannot be
     *             compared to elements currently in the immutable set.
     * @throws NullPointerException if <tt>toElement</tt> is null and this immutable set does not permit null elements
     * @throws IllegalArgumentException if this immutable set itself has a restricted range, and <tt>toElement</tt> lies
     *             outside the bounds of the range
     */
    ImmutableSortedSet<E> headSet(E toElement);

    /**
     * Returns a view of the portion of this immutable set whose elements are greater than or equal to
     * <tt>fromElement</tt>. The returned immutable set is backed by this immutable set, so changes in the returned
     * immutable set are reflected in this immutable set, and vice-versa. The returned immutable set supports all
     * optional immutable set operations that this immutable set supports.
     * <p>
     * The result of this method is a new, immutable sorted set.
     * @param fromElement E; low endpoint (inclusive) of the returned immutable set
     * @return a view of the portion of this immutable set whose elements are greater than or equal to
     *         <tt>fromElement</tt>
     * @throws ClassCastException if <tt>fromElement</tt> is not compatible with this immutable set's comparator (or, if
     *             the immutable set has no comparator, if <tt>fromElement</tt> does not implement {@link Comparable}).
     *             Implementations may, but are not required to, throw this exception if <tt>fromElement</tt> cannot be
     *             compared to elements currently in the immutable set.
     * @throws NullPointerException if <tt>fromElement</tt> is null and this immutable set does not permit null elements
     * @throws IllegalArgumentException if this immutable set itself has a restricted range, and <tt>fromElement</tt>
     *             lies outside the bounds of the range
     */
    ImmutableSortedSet<E> tailSet(E fromElement);

    /**
     * Returns the first (lowest) element currently in this immutable set.
     * @return the first (lowest) element currently in this immutable set
     * @throws NoSuchElementException if this immutable set is empty
     */
    E first();

    /**
     * Returns the last (highest) element currently in this immutable set.
     * @return the last (highest) element currently in this immutable set
     * @throws NoSuchElementException if this immutable set is empty
     */
    E last();

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

}
