package org.djutils.math.functions;

import java.util.Objects;

/**
 * Constant value function. Could be implemented with PowerFunction, but this is much more efficient.
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
public class Constant implements Function
{
    /** The value of this constant function. */
    private final double value;

    /** The constant value function that is always zero and has infinite domain. */
    public static final Constant ZERO = new Constant(0.0);

    /** The constant value function that is always one and has infinite domain. */
    public static final Constant ONE = new Constant(1.0);

    /**
     * Create a new constant value function.
     * @param value the value at any <code>x</code>
     */
    public Constant(final double value)
    {
        this.value = value;
    }

    @Override
    public double get(final double x)
    {
        return this.value;
    }

    @Override
    public Constant getDerivative()
    {
        return ZERO;
    }

    @Override
    public String getDescription()
    {
        return printValue(this.value);
    }

    @Override
    public String getId()
    {
        return "C";
    }

    @Override
    public String toString()
    {
        return "Constant [value=" + printValue(this.value) + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.value);
    }

    @SuppressWarnings("checkstyle:needbraces")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Constant other = (Constant) obj;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }

}
