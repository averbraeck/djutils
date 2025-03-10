package org.djutils.eval;

/**
 * RetrieveValue.java.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface RetrieveValue
{
    /**
     * Look up a variable and return its value. Numeric values should be returned as a DoubleScalar of some kind. Logical values
     * should be returned as a Boolean.
     * @param name name of the variable
     * @return the value of the variable
     */
    Object lookup(final String name);
    
}
