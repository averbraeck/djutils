package org.djutils.immutablecollections;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedMap;

/**
 * A SortedMap interface without the methods that can change it. The return values of subMap, tailMap and headMap are all
 * ImmutableSortedMaps.
 * <p>
 * Copyright (c) 2016-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <K> the key type of content of this Map
 * @param <V> the value type of content of this Map
 */
public interface ImmutableSortedMap<K, V> extends ImmutableMap<K, V>
{
    /**
     * Returns a modifiable copy of this immutable map.
     * @return a modifiable copy of this immutable map.
     */
    @Override
    SortedMap<K, V> toMap();

    /**
     * Returns the comparator used to order the keys in this immutable map, or <code>null</code> if this immutable map uses the
     * {@linkplain Comparable natural ordering} of its keys.
     * @return the comparator used to order the keys in this immutable map, or <code>null</code> if this immutable map uses the
     *         natural ordering of its keys
     */
    Comparator<? super K> comparator();

    /**
     * Returns a view of the portion of this immutable map whose keys range from <code>fromKey</code>, inclusive, to
     * <code>toKey</code>, exclusive. (If <code>fromKey</code> and <code>toKey</code> are equal, the returned immutable map is
     * empty.)
     * <p>
     * The result of this method is a new, immutable sorted map.
     * @param fromKey K; low endpoint (inclusive) of the returned immutable map
     * @param toKey K; high endpoint (exclusive) of the returned immutable map
     * @return a new, immutable sorted map of the portion of this immutable map whose keys range from <code>fromKey</code>,
     *         inclusive, to <code>toKey</code>, exclusive
     * @throws ClassCastException if <code>fromKey</code> and <code>toKey</code> cannot be compared to one another using this
     *             immutable map's comparator (or, if the immutable map has no comparator, using natural ordering).
     *             Implementations may, but are not required to, throw this exception if <code>fromKey</code> or
     *             <code>toKey</code> cannot be compared to keys currently in the immutable map.
     * @throws NullPointerException if <code>fromKey</code> or <code>toKey</code> is null and this immutable map does not permit
     *             null keys
     * @throws IllegalArgumentException if <code>fromKey</code> is greater than <code>toKey</code>; or if this immutable map
     *             itself has a restricted range, and <code>fromKey</code> or <code>toKey</code> lies outside the bounds of the
     *             range
     */
    ImmutableSortedMap<K, V> subMap(K fromKey, K toKey);

    /**
     * Returns a view of the portion of this immutable map whose keys are strictly less than <code>toKey</code>. The returned
     * immutable map is backed by this immutable map, so changes in the returned immutable map are reflected in this immutable
     * map, and vice-versa. The returned immutable map supports all optional immutable map operations that this immutable map
     * supports.
     * <p>
     * The result of this method is a new, immutable sorted map.
     * @param toKey K; high endpoint (exclusive) of the returned immutable map
     * @return a view of the portion of this immutable map whose keys are strictly less than <code>toKey</code>
     * @throws ClassCastException if <code>toKey</code> is not compatible with this immutable map's comparator (or, if the
     *             immutable map has no comparator, if <code>toKey</code> does not implement {@link Comparable}).
     *             Implementations may, but are not required to, throw this exception if <code>toKey</code> cannot be compared
     *             to keys currently in the immutable map.
     * @throws NullPointerException if <code>toKey</code> is null and this immutable map does not permit null keys
     * @throws IllegalArgumentException if this immutable map itself has a restricted range, and <code>toKey</code> lies outside
     *             the bounds of the range
     */
    ImmutableSortedMap<K, V> headMap(K toKey);

    /**
     * Returns a view of the portion of this immutable map whose keys are greater than or equal to <code>fromKey</code>. The
     * returned immutable map is backed by this immutable map, so changes in the returned immutable map are reflected in this
     * immutable map, and vice-versa. The returned immutable map supports all optional immutable map operations that this
     * immutable map supports.
     * <p>
     * The result of this method is a new, immutable sorted map.
     * @param fromKey K; low endpoint (inclusive) of the returned immutable map
     * @return a view of the portion of this immutable map whose keys are greater than or equal to <code>fromKey</code>
     * @throws ClassCastException if <code>fromKey</code> is not compatible with this immutable map's comparator (or, if the
     *             immutable map has no comparator, if <code>fromKey</code> does not implement {@link Comparable}).
     *             Implementations may, but are not required to, throw this exception if <code>fromKey</code> cannot be compared
     *             to keys currently in the immutable map.
     * @throws NullPointerException if <code>fromKey</code> is null and this immutable map does not permit null keys
     * @throws IllegalArgumentException if this immutable map itself has a restricted range, and <code>fromKey</code> lies
     *             outside the bounds of the range
     */
    ImmutableSortedMap<K, V> tailMap(K fromKey);

    /**
     * Returns the first (lowest) key currently in this immutable map.
     * @return the first (lowest) key currently in this immutable map
     * @throws NoSuchElementException if this immutable map is empty
     */
    K firstKey();

    /**
     * Returns the last (highest) key currently in this immutable map.
     * @return the last (highest) key currently in this immutable map
     * @throws NoSuchElementException if this immutable map is empty
     */
    K lastKey();

    /**
     * Return an ImmutableSortedSet view of the keys contained in this immutable map.
     * @return an ImmutableSortedSet view of the keys contained in this immutable map
     */
    @Override
    ImmutableSortedSet<K> keySet();

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
