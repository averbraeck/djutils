package org.djutils.immutablecollections;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * An immutable wrapper for a TreeSet.
 * <p>
 * Copyright (c) 2016-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
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
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
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
        super(new TreeSet<E>(immutableSortedSet.getCollection()), Immutable.COPY);
    }

    /**
     * @param immutableTreeSet ImmutableTreeSet&lt;E&gt;; the collection to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableTreeSet(final ImmutableTreeSet<E> immutableTreeSet, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new TreeSet<E>(immutableTreeSet.getCollection())
                : immutableTreeSet.getCollection(), copyOrWrap);
    }

    /** {@inheritDoc} */
    @Override
    public final NavigableSet<E> toSet()
    {
        return new TreeSet<E>(getCollection());
    }

    /** {@inheritDoc} */
    @Override
    protected NavigableSet<E> getCollection()
    {
        return (NavigableSet<E>) super.getCollection();
    }

    /** {@inheritDoc} */
    @Override
    public final Comparator<? super E> comparator()
    {
        return getCollection().comparator();
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSortedSet<E> subSet(final E fromElement, final E toElement)
    {
        return new ImmutableTreeSet<E>(getCollection().subSet(fromElement, toElement));
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSortedSet<E> headSet(final E toElement)
    {
        return new ImmutableTreeSet<E>(getCollection().headSet(toElement));
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSortedSet<E> tailSet(final E fromElement)
    {
        return new ImmutableTreeSet<E>(getCollection().tailSet(fromElement));
    }

    /** {@inheritDoc} */
    @Override
    public final E first()
    {
        return getCollection().first();
    }

    /** {@inheritDoc} */
    @Override
    public final E last()
    {
        return getCollection().last();
    }

    /** {@inheritDoc} */
    @Override
    public final E lower(final E e)
    {
        return getCollection().lower(e);
    }

    /** {@inheritDoc} */
    @Override
    public final E floor(final E e)
    {
        return getCollection().floor(e);
    }

    /** {@inheritDoc} */
    @Override
    public final E ceiling(final E e)
    {
        return getCollection().ceiling(e);
    }

    /** {@inheritDoc} */
    @Override
    public final E higher(final E e)
    {
        return getCollection().higher(e);
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> descendingSet()
    {
        return new ImmutableTreeSet<E>(getCollection().descendingSet());
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableIterator<E> descendingIterator()
    {
        return new ImmutableIterator<E>(getCollection().descendingIterator());
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement,
            final boolean toInclusive)
    {
        return new ImmutableTreeSet<E>(getCollection().subSet(fromElement, fromInclusive, toElement, toInclusive));
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> headSet(final E toElement, final boolean inclusive)
    {
        return new ImmutableTreeSet<E>(getCollection().headSet(toElement, inclusive));
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableNavigableSet<E> tailSet(final E fromElement, final boolean inclusive)
    {
        return new ImmutableTreeSet<E>(getCollection().tailSet(fromElement, inclusive));
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        NavigableSet<E> set = getCollection();
        if (null == set)
        {
            return "ImmutableTreeSet []";
        }
        return "ImmutableTreeSet [" + set.toString() + "]";
    }

}
