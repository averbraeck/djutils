package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * Constant value function; <code>f(x) &rarr; c</code> where <code>c &isin; &#8477</code>. Can also be implemented with
 * PowerFunction, but this is much more readable and efficient.
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
public class Constant implements MathFunction
{
    /** The value of this constant function. */
    private final double value;

    /** The constant value function that is always zero and has infinite domain. */
    public static final Constant ZERO = new Constant(0.0);

    /** The constant value function that is always one and has infinite domain. */
    public static final Constant ONE = new Constant(1.0);

    /**
     * Create a new constant value function F(x).
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
    public MathFunction simplify()
    {
        if (this.value == ZERO.value)
        {
            return ZERO;
        }
        if (this.value == ONE.value)
        {
            return ONE;
        }
        return this;
    }

    @Override
    public double getScale()
    {
        return this.value;
    }

    @Override
    public MathFunction scaleBy(final double factor)
    {
        return new Constant(factor * this.value).simplify();
    }

    @Override
    public int sortPriority()
    {
        return 2;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Constant), IllegalArgumentException.class, "other is of wrong type");
        return 0;
    }

    @Override
    public MathFunction mergeAdd(final MathFunction other)
    {
        if (other instanceof Constant)
        {
            double total = this.value + ((Constant) other).value;
            return new Constant(total).simplify();
        }
        return null;
    }

    @Override
    public MathFunction mergeMultiply(final MathFunction other)
    {
        if (other instanceof Constant)
        {
            double product = this.value * ((Constant) other).value;
            return new Constant(product).simplify();
        }
        return null;
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
