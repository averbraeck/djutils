package org.djutils.eval;

import org.djutils.metadata.MetaData;

/**
 * F1.java. Minimal implementation of one-argument Function with description.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
class F1 implements Function
{
    /** Id of the function. */
    final String id;

    /** Type of the parameters of this function (also contains name and description). */
    final MetaData metaData;

    /** Type of the result of this function. */
    final Class<?> resultClass;

    /** The zero argument function. */
    final OneArgumentFunction f1;

    /**
     * Construct a new one-argument function.
     * @param id name of the function as it must be written in expressions
     * @param resultClass the type of the result of the function
     * @param metaData meta data of the function
     * @param f1 one argument function
     */
    F1(final String id, final Class<?> resultClass, final MetaData metaData, final OneArgumentFunction f1)
    {
        this.id = id;
        this.resultClass = resultClass;
        this.metaData = metaData;
        this.f1 = f1;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public MetaData getMetaData()
    {
        return this.metaData;
    }

    @Override
    public Object function(final Object[] arguments) throws RuntimeException
    {
        return this.f1.execute(this, arguments[0]);
    }

    /**
     * Interface for one argument functions.
     */
    interface OneArgumentFunction
    {
        /**
         * Prototype of the one-argument function
         * @param functionData meta data of the function
         * @param argument the argument of the function
         * @return the result type of the function
         */
        Object execute(Function functionData, Object argument);

    }
}
