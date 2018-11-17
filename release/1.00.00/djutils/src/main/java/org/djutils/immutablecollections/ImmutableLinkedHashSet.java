package org.djutils.immutablecollections;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An immutable wrapper for a HashSet.
 * <p>
 * Copyright (c) 2016-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck
 * $, initial version May 7, 2016 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <E> the type of content of this Set
 */
public class ImmutableLinkedHashSet<E> extends ImmutableAbstractSet<E>
{
    /** */
    private static final long serialVersionUID = 20160507L;

    /**
     * @param collection Collection&lt;? extends E&gt;; the collection to use for the immutable set.
     */
    public ImmutableLinkedHashSet(final Collection<? extends E> collection)
    {
        super(new LinkedHashSet<E>(collection), Immutable.COPY);
    }

    /**
     * @param collection Set&lt;E&gt;; the collection to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableLinkedHashSet(final Set<E> collection, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new LinkedHashSet<E>(collection) : collection, copyOrWrap);
    }

    /**
     * @param collection ImmutableAbstractCollection&lt;? extends E&gt;; the collection to use for the immutable set.
     */
    public ImmutableLinkedHashSet(final ImmutableAbstractCollection<? extends E> collection)
    {
        super(new LinkedHashSet<E>(collection.getCollection()), Immutable.COPY);
    }

    /**
     * @param set ImmutableAbstractSet&lt;E&gt;; the collection to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableLinkedHashSet(final ImmutableAbstractSet<E> set, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new LinkedHashSet<E>(set.getCollection()) : set.getCollection(),
                copyOrWrap);
    }

    /** {@inheritDoc} */
    @Override
    protected Set<E> getCollection()
    {
        return super.getCollection();
    }

    /** {@inheritDoc} */
    @Override
    public final Set<E> toSet()
    {
        return new LinkedHashSet<E>(getCollection());
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        Set<E> set = getCollection();
        if (null == set)
        {
            return "ImmutableLinkedHashSet []";
        }
        return "ImmutableLinkedHashSet [" + set.toString() + "]";
    }

}
