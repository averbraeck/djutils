package org.djutils.immutablecollections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An immutable wrapper for an ArrayList.
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
 * @param <E> the type of content of this List
 */
public class ImmutableArrayList<E> extends ImmutableAbstractList<E>
{
    /** */
    private static final long serialVersionUID = 20160507L;

    /**
     * @param collection Collection&lt;? extends E&gt;; the collection to use for the immutable list.
     */
    public ImmutableArrayList(final Collection<? extends E> collection)
    {
        super(new ArrayList<E>(collection), Immutable.COPY);
    }

    /**
     * @param list List&lt;E&gt;; the list to use for the immutable list.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableArrayList(final List<E> list, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new ArrayList<E>(list) : list, copyOrWrap);
    }

    /**
     * @param collection ImmutableAbstractCollection&lt;? extends E&gt;; the collection to use for the immutable list.
     */
    public ImmutableArrayList(final ImmutableAbstractCollection<? extends E> collection)
    {
        super(new ArrayList<E>(collection.getCollection()), Immutable.COPY);
    }

    /**
     * @param list ImmutableAbstractList&lt;E&gt;; the list to use for the immutable list.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableArrayList(final ImmutableAbstractList<E> list, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new ArrayList<E>(list.getCollection()) : list.getCollection(), copyOrWrap);
    }

    /** {@inheritDoc} */
    @Override
    public final ArrayList<E> toList()
    {
        return new ArrayList<E>(getCollection());
    }

    /** {@inheritDoc} */
    @Override
    protected List<E> getCollection()
    {
        return super.getCollection();
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableList<E> subList(final int fromIndex, final int toIndex)
    {
        return new ImmutableArrayList<>(getCollection().subList(fromIndex, toIndex));
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        List<E> list = getCollection();
        if (null == list)
        {
            return "ImmutableArrayList []";
        }
        return "ImmutableArrayList [" + list.toString() + "]";
    }

}
