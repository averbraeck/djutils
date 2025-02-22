package org.djutils.eval;

import org.djutils.base.Identifiable;
import org.djutils.metadata.MetaData;

/**
 * Function.java. Description and implementation of a function that can be registered in and then executed by the Eval evaluator.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Function extends Identifiable
{   
    /**
     * Return the name of the function.
     * @return the name of the function 
     */
    default String getName()
    {
        return getMetaData().getName();
    }
    /**
     * Return a textual description of the function.
     * @return description of the function (may use html tags).
     */
    default String getDescription()
    {
        return getMetaData().getDescription();
    }
    
    /**
     * Specifies the types of the arguments expected by the function.
     * @return specification of the arguments expected by the function
     */
    MetaData getMetaData();
    
    /**
     * The function itself.
     * @param arguments the arguments of the function
     * @return the result of the function (must currently be either some type of DoubleScalar or Boolean).
     * @throws RuntimeException thrown when the function is unable to produce a result
     */
    Object function(Object[] arguments) throws RuntimeException;
    
}
