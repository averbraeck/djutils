package org.djutils.math.functions;

/**
 * Knowledge about knots (discontinuities) of a MathFunction in some interval.
 * <p>
 * Copyright (c) 2024-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public enum KnotReport
{
    /** It is certain that there are no knots in the interval. */
    NONE,
    /** There are a limited number of knots in the interval. */
    KNOWN_FINITE,
    /** There are infinitely many knots in the interval. */
    KNOWN_INFINITE,
    /** The presence, or number of knots in the interval is not known. */
    UNKNOWN;

    /**
     * Report if the number of knots is finite.
     * @return <code>true</code> for <code>NONE</code>, or <code>KNOWN_FINITE</code>; <code>false</code> for
     *         <code>KNOWN_INFINITE</code>, or <code>UNKNOWN</code>
     */
    public boolean isFinite()
    {
        switch (this)
        {
            case KNOWN_FINITE:
                return true;
            case KNOWN_INFINITE:
                return false;
            case NONE:
                return true;
            case UNKNOWN:
                return false;
            default:
                throw new IllegalStateException("Cannot happen");
        }
    }
    
    /**
     * Combine two <code>KnotReport</code>s and return the combined knowledge.
     * @param other the other <code>KnotReport</code>
     * @return the combined <code>KnotReport</code> knowledge
     */
    public KnotReport combineWith(final KnotReport other)
    {
        if (this == UNKNOWN || other == UNKNOWN)
        {
            return UNKNOWN;
        }
        if (this == KNOWN_INFINITE || other == KNOWN_INFINITE)
        {
            return KNOWN_INFINITE;
        }
        if (this == KNOWN_FINITE || other == KNOWN_FINITE)
        {
            return KNOWN_FINITE; 
        }
        return NONE;
    }
}
