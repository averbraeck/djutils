package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * Exponential function.
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
public class Exponential implements MathFunction
{
    /** Value of the exponential function at <code>x == 0.0</code>. */
    private final double factor;

    /** The chained function (may be null). */
    private final MathFunction chain;

    /** SuperScript writer. */
    private static final SuperScript SUPER_SCRIPT = new SuperScript();

    /**
     * Construct a new Exponential of a <code>MathFunction</code>.
     * @param chain the <code>MathFunction</code> that yields the exponent
     * @param factor the value of the Exponential function when <Code>MathFunction</code> yields <code>0.0</code>
     */
    public Exponential(final MathFunction chain, final double factor)
    {
        this.chain = chain;
        this.factor = factor;
    }

    /**
     * Construct a new Exponential of a <code>MathFunction</code>.
     * @param chain the <code>MathFunction</code> that yields the exponent
     */
    public Exponential(final MathFunction chain)
    {
        this(chain, 1.0);
    }

    /**
     * Construct a new Exponential.
     * @param factor the value of the Exponential at <code>x == 0.0</code>
     */
    public Exponential(final double factor)
    {
        this(null, factor);
    }

    /**
     * Construct the <code>e<sup>x</sup></code> function.
     */
    public Exponential()
    {
        this(1.0);
    }

    @Override
    public double get(final double x)
    {
        return this.factor * Math.exp(this.chain == null ? x : this.chain.get(x));
    }

    @Override
    public MathFunction getDerivative()
    {
        if (this.chain == null)
        {
            return this;
        }
        return new Product(this.chain.getDerivative(), this);
    }

    @Override
    public MathFunction simplify()
    {
        if (this.factor == 0.0)
        {
            return Constant.ZERO;
        }
        if (this.chain != null && this.chain instanceof Constant)
        {
            return new Constant(get(0)).simplify();
        }
        return this;
    }

    @Override
    public MathFunction scaleBy(final double scaleFactor)
    {
        if (scaleFactor == 0.0)
        {
            return Constant.ZERO;
        }
        if (scaleFactor == 1.0)
        {
            return this;
        }
        return new Exponential(this.chain, scaleFactor * this.factor);
    }

    @Override
    public int sortPriority()
    {
        return 7;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Exponential), IllegalArgumentException.class, "other is of wrong type");
        Exponential otherExp = (Exponential) other;
        if (this.factor > otherExp.factor)
        {
            return 1;
        }
        if (this.factor < otherExp.factor)
        {
            return -1;
        }
        return compareChains(this.chain, otherExp.chain);
    }

    @Override
    public MathFunction mergeAdd(final MathFunction other)
    {
        if (other instanceof Exponential)
        {
            Exponential otherExp = (Exponential) other;
            if (this.chain == null && otherExp.chain == null || this.chain != null && this.chain.equals(otherExp.chain))
            {
                return new Exponential(this.chain, this.factor + otherExp.factor);
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        if (this.factor != 1.0)
        {
            result.append(printValue(this.factor));
        }
        if (this.factor != 0.0)
        {
            if (this.chain == null)
            {
                result.append("e");
                result.append(SUPER_SCRIPT.translate("x"));
            }
            else
            {
                result.append("exp(");
                result.append(this.chain.toString());
                result.append(")");
            }
        }
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.chain, this.factor);
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
        Exponential other = (Exponential) obj;
        return Objects.equals(this.chain, other.chain)
                && Double.doubleToLongBits(this.factor) == Double.doubleToLongBits(other.factor);
    }
    
}
