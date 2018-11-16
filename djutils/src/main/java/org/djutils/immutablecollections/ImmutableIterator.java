package org.djutils.immutablecollections;

import java.util.Iterator;

/**
 * An immutable iterator over elements, wrapping a "mutable" iterator. The default remove method from the interface will
 * throw an exception.
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
 * @param <E> the element type
 */
public class ImmutableIterator<E> implements Iterator<E>
{
    /** the wrapped iterator. */
    private final Iterator<E> iterator;

    /**
     * @param iterator Iterator&lt;E&gt;; the iterator to wrap as an immutable iterator.
     */
    public ImmutableIterator(final Iterator<E> iterator)
    {
        super();
        this.iterator = iterator;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public final E next()
    {
        return this.iterator.next();
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "ImmutableIterator [iterator=" + this.iterator + "]";
    }

}
