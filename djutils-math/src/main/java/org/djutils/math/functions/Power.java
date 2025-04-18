package org.djutils.math.functions;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.djutils.exceptions.Throw;

/**
 * MathFunctions that are a constant times some power of x; generally <code>f(x) &rarr; a * x^<sup>b</sup></code> where a &isin;
 * &#8477; and b &isin; &#8477;
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
public class Power implements MathFunction
{
    /** The weight (value at x == 0). */
    private final double weight;

    /** The power value. */
    private final double power;

    /** The function that yields x (may be null). */
    private final MathFunction chain;

    /** SuperScript writer. */
    private static final SuperScript SUPER_SCRIPT = new SuperScript();

    /**
     * Construct a new power function.
     * @param chain the MathFunction that yields the <code>x</code> for this power function
     * @param weight the value at <code>x == 1</code>
     * @param power the exponent of <code>x</code>
     */
    public Power(final MathFunction chain, final double weight, final double power)
    {
        this.weight = weight;
        this.power = weight == 0.0 ? 1.0 : power;
        this.chain = chain;
    }

    /**
     * Construct a new power function.
     * @param weight the value at <code>x == 1</code>
     * @param power the exponent of <code>x</code>
     */
    public Power(final double weight, final double power)
    {
        this(null, weight, power);
    }

    /**
     * Create a new power function with weight 1.0 and the supplied value as exponent.
     * @param chain the MathFunction that yields the <code>x</code> for this power function
     * @param power the exponent of <code>chain</code>
     */
    public Power(final MathFunction chain, final double power)
    {
        this(chain, 1.0, power);
    }

    /**
     * Create a new power function with weight 1.0 and the supplied value as exponent.
     * @param power the exponent of <code>x</code>
     */
    public Power(final double power)
    {
        this(1.0, power);
    }

    @Override
    public Double apply(final Double x)
    {
        if (this.weight == 0.0)
        {
            return 0.0; // Prevent result values like -0.0 that the Math.pow function can yield
        }
        // A few more short circuits
        if (this.power == 0.0)
        {
            return this.weight;
        }
        double xValue = this.chain == null ? x : this.chain.apply(x);
        if (this.power == 1.0)
        {
            return this.weight * xValue;
        }
        return this.weight * Math.pow(xValue, this.power);
    }

    @Override
    public MathFunction getDerivative()
    {
        if (this.weight == 0.0 || this.power == 0.0)
        {
            return Constant.ZERO;
        }
        if (this.power == 1.0)
        {
            if (this.weight == 1.0)
            {
                return (Constant.ONE);
            }
            return new Constant(this.weight);
        }
        Power myDerivative = new Power(this.chain, this.weight * this.power, this.power - 1.0);
        if (this.chain == null)
        {
            return myDerivative.simplify();
        }
        MathFunction myChainDerivative = new Power(this.chain, myDerivative.weight, myDerivative.power);
        return new Product(myChainDerivative, this.chain.getDerivative()).simplify();
    }

    @Override
    public MathFunction simplify()
    {
        if (this.weight == 0.0)
        {
            return Constant.ZERO;
        }
        if (this.power == 0.0)
        {
            if (this.weight == 1.0)
            {
                return Constant.ONE;
            }
            return new Constant(this.weight);
        }
        if (this.chain != null && this.chain instanceof Constant)
        {
            return new Constant(apply(0d)).simplify();
        }
        if (this.power == 1.0 && this.chain != null)
        {
            return this.chain.scaleBy(this.weight);
        }
        return this;
    }

    @Override
    public double getScale()
    {
        return this.weight;
    }

    @Override
    public MathFunction scaleBy(final double factor)
    {
        if (factor == 0.0)
        {
            return Constant.ZERO;
        }
        if (factor == 1.0)
        {
            return this;
        }
        return new Power(this.chain, factor * this.weight, this.power);
    }

    @Override
    public int sortPriority()
    {
        return 1;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Power), IllegalArgumentException.class, "other is of wrong type");
        Power otherPowerFunction = (Power) other;
        if (otherPowerFunction.power < this.power)
        {
            return -1;
        }
        if (otherPowerFunction.power > this.power)
        {
            return 1;
        }
        if (this.weight < otherPowerFunction.weight)
        {
            return 1;
        }
        if (this.weight > otherPowerFunction.weight)
        {
            return -1;
        }
        return compareChains(this.chain, otherPowerFunction.chain);
    }

    @Override
    public MathFunction mergeAdd(final MathFunction other)
    {
        if (other instanceof Power)
        {
            Power otherPowerFunction = (Power) other;
            if (this.power == otherPowerFunction.power && (this.chain == null && otherPowerFunction.chain == null
                    || (this.chain != null && this.chain.equals(otherPowerFunction.chain))))
            {
                return new Power(this.chain, this.weight + otherPowerFunction.weight, this.power);
            }
        }
        return null;
    }

    @Override
    public MathFunction mergeMultiply(final MathFunction other)
    {
        if (other instanceof Power)
        {
            Power otherPowerFunction = (Power) other;
            if (this.chain == null && otherPowerFunction.chain == null
                    || (this.chain != null && this.chain.equals(otherPowerFunction.chain)))
            {
                return new Power(this.chain, this.weight * otherPowerFunction.weight, this.power + otherPowerFunction.power);
            }
            else if (this.power == otherPowerFunction.power)
            {
                double resultWeight = this.weight * otherPowerFunction.weight;
                if (this.chain != null && otherPowerFunction.chain != null)
                {
                    return new Power(new Product(this.chain, otherPowerFunction.chain), resultWeight, this.power);
                }
                // The chain fields cannot both be null; therefore, exactly one is non-null
                return new Power(new Product(new Power(1, 1), this.chain == null ? otherPowerFunction.chain : this.chain),
                        resultWeight, this.power);
            }
        }
        return null;
    }

    @Override
    public MathFunction mergeDivide(final MathFunction other)
    {
        if (other instanceof Power)
        {
            Power otherPowerFunction = (Power) other;
            if (this.chain == null && otherPowerFunction.chain == null
                    || (this.chain != null && this.chain.equals(otherPowerFunction.chain)))
            {
                return new Power(this.chain, this.weight / otherPowerFunction.weight, this.power - otherPowerFunction.power);
            }
        }
        return null;
    }

    @Override
    public KnotReport getKnotReport(final Interval<?> interval)
    {
        boolean integerPower = this.power == Math.ceil(this.power);
        if (this.chain == null && (!integerPower) && interval.low() < 0.0)
        {
            return KnotReport.KNOWN_INFINITE; // cannot raise negative to non-integer power
        }
        if (this.chain != null && (!integerPower))
        {
            return KnotReport.UNKNOWN; // we cannot tell if this might raise negative to non-integer power
        }
        // Our domain is [-inf, +inf]
        return this.chain == null ? KnotReport.NONE : this.chain.getKnotReport(interval);
    }

    @Override
    public SortedSet<Double> getKnots(final Interval<?> interval)
    {
        boolean integerPower = this.power == Math.ceil(this.power);
        if (this.chain == null && (!integerPower) && interval.low() < 0.0)
        {
            throw new UnsupportedOperationException("There are infinitely many knots in " + interval);
        }
        if (this.chain != null && (!integerPower))
        {
            throw new UnsupportedOperationException("Cannot report knots in " + interval
                    + " because I do not know where the chained function is negative or zero");
        }
        return this.chain == null ? new TreeSet<Double>() : this.chain.getKnots(interval);
    }

    @Override
    public String toString()
    {
        if (this.weight == 0)
        {
            return ("0");
        }
        StringBuilder result = new StringBuilder();
        if (this.weight == -1.0)
        {
            result.append("-");
        }
        else if (this.weight != 1.0 || this.power == 0.0)
        {
            result.append(printValue(this.weight));
        }
        if (this.power != 0.0)
        {
            result.append(this.chain == null ? "x" : ("(" + this.chain.toString() + ")"));
            if (this.power != 1)
            {
                result.append(SUPER_SCRIPT.translate(printValue(this.power)));
            }
        }
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.chain, this.power, this.weight);
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
        Power other = (Power) obj;
        return Objects.equals(this.chain, other.chain)
                && Double.doubleToLongBits(this.power) == Double.doubleToLongBits(other.power)
                && Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(other.weight);
    }

}
