package org.djutils.immutablecollections;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * An immutable wrapper for a TreeSet.
 * <p>
 * Copyright (c) 2016-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <E> the type of content of this Set
 */
public class ImmutableTreeSet<E> extends ImmutableAbstractSet<E> implements ImmutableNavigableSet<E>
{
    /** */
    private static final long serialVersionUID = 20160507L;

    /**
     * @param sortedSet Collection&lt;? extends E&gt;; the collection to use for the immutable set.
     */
    public ImmutableTreeSet(final Collection<? extends E> sortedSet)
    {
        super(new TreeSet<E>(sortedSet), Immutable.COPY);
    }

    /**
     * @param treeSet NavigableSet&lt;E&gt;; the collection to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original collection
     */
    public ImmutableTreeSet(final NavigableSet<E> treeSet, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new TreeSet<E>(treeSet) : treeSet, copyOrWrap);
    }

    /**
     * @param immutableSortedSet ImmutableAbstractSet&lt;E&gt;; the collection to use for the immutable set.
     */
    public ImmutableTreeSet(final ImmutableAbstractSet<E> immutableSortedSet)
    {
        super(new TreeSet<E>(immutableSortedSet.getUnderlyingCollection()), Immutable.COPY);
    }

    /**
     * @param immutableTreeSet ImmutableTreeSet&lt;E&gt;; the collection to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original collection
     */
    public ImmutableTreeSet(final ImmutableTreeSet<E> immutableTreeSet, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new TreeSet<E>(immutableTreeSet.getUnderlyingCollection())
                : immutableTreeSet.getUnderlyingCollection(), copyOrWrap);
    }

    /** {@inheritDoc} */
    @Override
    public final NavigableSet<E> toSet()
    {
        return new TreeSet<E>(getUnderlyingCollection());
    }

    /** {@inheritDoc} */
    @Override
    protected NavigableSet<E> getUnderlyingCollection()
    {
        return (NavigableSet<E>) super.getUnderlyingCollection();
    }

    /** {@inheritDoc} */
    @Override
    public final Comparator<? super E> comparator()
    {
        return getUnderlyingCollection().comparator();
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSortedSet<E> subSet(final E fromElement, final E toElement)
    {
        return new ImmutableTreeSet<E>((TreeSet<E>) getUnderlyingCollection().subSet(fromElement, toElement), Immutable.WRAP);
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSortedSet<E> headSet(final E toElement)
    {
        return new ImmutableTreeSet<E>((TreeSet<E>) getUnderlyingCollection().headSet(toElement), Immutable.WRAP);
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSortedSet<E> tailSet(final E fromElement)
    {
        return new ImmutableTreeSet<E>((TreeSet<E>) getUnderlyingCollection().tailSet(fromElement), Immutable.WRAP);
    }

    /** {@inheritDoc} */
    @Override
    public final E first()
    {
        return getUnderlyingCollection().first();
    }

    /** {@inheritDoc} */
    @Override
    public final E last()
    {
        return getUnderlyingCollection().last();
    }

    /** {@inheritDoc} */
    @Override
    public final E lower(final E e)
    {
        return getUnderlyingCollection().lower(e);
    }

    /** {@inheritDoc} */
    @Override
    public final E floor(final E e)
    {
        return getUnderlyingCollection().floor(e);
    }

    /** {@inheritDoc} */
    @Override
    public final E ceiling(final E e)
    {
        return getUnderlyingCollection().ceiling(e);
    }

    /** {@inheritDoc} */
    @Override
    public final E higher(final E e)
    {
        return getUnderlyingCollection().higher(e);
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> descendingSet()
    {
        return new ImmutableTreeSet<E>(getUnderlyingCollection().descendingSet());
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableIterator<E> descendingIterator()
    {
        return new ImmutableIterator<E>(getUnderlyingCollection().descendingIterator());
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement,
            final boolean toInclusive)
    {
        return new ImmutableTreeSet<E>(getUnderlyingCollection().subSet(fromElement, fromInclusive, toElement, toInclusive),
                Immutable.WRAP);
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> headSet(final E toElement, final boolean inclusive)
    {
        return new ImmutableTreeSet<E>(getUnderlyingCollection().headSet(toElement, inclusive), Immutable.WRAP);
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> tailSet(final E fromElement, final boolean inclusive)
    {
        return new ImmutableTreeSet<E>(getUnderlyingCollection().tailSet(fromElement, inclusive), Immutable.WRAP);
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        NavigableSet<E> set = getUnderlyingCollection();
        if (null == set)
        {
            return "ImmutableTreeSet []";
        }
        return "ImmutableTreeSet [" + set.toString() + "]";
    }

}
