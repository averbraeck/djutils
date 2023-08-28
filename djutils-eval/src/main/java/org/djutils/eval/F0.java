package org.djutils.eval;

import org.djutils.metadata.MetaData;

/**
 * F0.java. Minimal implementation of zero-argument Function with description.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author Peter Knoppers</a>
 */
class F0 implements Function
{
    /** Id of the function. */
    final String id;

    /** Type of the parameters of this function (also contains name and description). */
    final MetaData metaData;

    /** Type of the result of this function. */
    final Class<?> resultClass;

    /** The zero argument function. */
    final ZeroArgumentFunction f0;

    /**
     * Construct a new zero argument function.
     * @param id String; name of the function as it must be written in expressions
     * @param resultClass Class&lt;?&gt;; the type of the result of the function
     * @param metaData MetaData; meta data of the function
     * @param f0 ZeroArgumentFunction; zero argument function
     */
    F0(final String id, final Class<?> resultClass, final MetaData metaData, final ZeroArgumentFunction f0)
    {
        this.id = id;
        this.resultClass = resultClass;
        this.metaData = metaData;
        this.f0 = f0;
    }

    /**
     * Construct a new zero-argument function with constant result.
     * @param id String; name of the function as it must be written in expressions
     * @param constantResult Object; the result of the zero argument function
     * @param metaData MetaData; meta data of the function
     */
    F0(final String id, final Object constantResult, final MetaData metaData)
    {
        this(id, constantResult.getClass(), metaData, (f) -> constantResult);
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
        return this.f0.execute(this);
    }
    
    /**
     * Interface for zero argument functions.
     */
    interface ZeroArgumentFunction
    {
        /**
         * Prototype of the zero-argument function
         * @param functionData Function; meta data of the function
         * @return Object; the result type of the function
         */
        Object execute(Function functionData);
    }

}
