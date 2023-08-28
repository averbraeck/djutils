package org.djutils.eval;

import org.djutils.metadata.MetaData;

/**
 * F2.java. Minimal implementation of two-argument Function with description.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author Peter Knoppers</a>
 */
class F2 implements Function
{
    /** Id of the function. */
    final String id;

    /** Type of the parameters of this function (also contains name and description). */
    final MetaData metaData;

    /** The zero argument function. */
    final TwoArgumentFunction f2;

    /**
     * Construct a new two-argument function.
     * @param id String; name of the function as it must be written in expressions
     * @param metaData MetaData; meta data of the function
     * @param f2 TwoArgumentFunction; two argument function
     */
    F2(final String id, final MetaData metaData, final TwoArgumentFunction f2)
    {
        this.id = id;
        this.metaData = metaData;
        this.f2 = f2;
    }

    /** {@inheritDoc} */
    @Override
    public String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public MetaData getMetaData()
    {
        return this.metaData;
    }

    /** {@inheritDoc} */
    @Override
    public Object function(final Object[] arguments) throws RuntimeException
    {
        return this.f2.execute(this, arguments[0], arguments[1]);
    }

    /**
     * Interface for two-argument functions.
     */
    interface TwoArgumentFunction
    {
        /**
         * Prototype of the two-argument function
         * @param functionData Function; meta data of the function
         * @param argument1 Object; the first argument of the function
         * @param argument2 Object; the second argument of the function
         * @return Object; the result type of the function
         */
        Object execute(Function functionData, Object argument1, Object argument2);

    }
}
