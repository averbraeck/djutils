package org.djutils.math.functions;

/**
 * Nan; FunctionInterface that returns NaN.
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
public final class NanFunction implements Function
{
    /** Function that always return NaN (Not A Number). */
    public static final NanFunction NAN = new NanFunction();

    /**
     * Utility class; do not instantiate.
     */
    private NanFunction()
    {
        // Do not instantiate
    }

    @Override
    public double get(final double x)
    {
        return Double.NaN;
    }

    @Override
    public Function getDerivative()
    {
        return this; // same NaN value, same domain
    }

    @Override
    public String getDescription()
    {
        return "NaN";
    }

    @Override
    public String getId()
    {
        return ("NaN");
    }

}
