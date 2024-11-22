package org.djutils.math.functions;

/**
 * Nan; MathFunction that returns NaN.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class Nan implements MathFunction
{
    /** MathFunction that always return NaN (Not A Number). */
    public static final Nan NAN = new Nan();

    /**
     * Utility class; do not instantiate.
     */
    private Nan()
    {
        // Do not instantiate
    }

    @Override
    public double get(final double x)
    {
        return Double.NaN;
    }

    @Override
    public MathFunction getDerivative()
    {
        return this; // same NaN value, same domain
    }
    
    @Override
    public MathFunction scaleBy(final double factor)
    {
        return this;
    }

    @Override
    public String getDescription()
    {
        return "NaN";
    }

    @Override
    public String getId()
    {
        return "NaN";
    }

    @Override
    public String toString()
    {
        return "NaN []";
    }

}
