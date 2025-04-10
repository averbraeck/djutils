package org.djutils.math.functions;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.djutils.exceptions.Throw;

/**
 * Logarithms.
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
public class Logarithm implements MathFunction
{
    /**
     * The reciprocal of the natural logarithm of the base of this logarithm. The scale factor to apply to the natural
     * logarithm.
     */
    private final double logBaseRecip;

    /** The function that yields x (may be null). */
    private final MathFunction chain;

    /**
     * Private constructor that offers direct control of the <code>logBase</code> value.
     * @param notUsed not used
     * @param chain the chained <code>MathFunction</code>
     * @param logBaseRecip the reciprocal of the base of the new logarithm
     */
    private Logarithm(final boolean notUsed, final MathFunction chain, final double logBaseRecip)
    {
        this.logBaseRecip = logBaseRecip;
        this.chain = chain;
    }

    /**
     * Natural logarithm; logarithm with base <code>e</code>.
     */
    public Logarithm()
    {
        this(Math.E);
    }

    /**
     * Logarithm with user specified base. The base is the value where the logarithm function has the value <code>1.0</code>.
     * @param base the base of the new logarithm
     */
    public Logarithm(final double base)
    {
        this(null, base);
    }

    /**
     * Natural logarithm of chained <code>MathFunction</code>.
     * @param chain the chained <code>MathFunction</code>
     */
    public Logarithm(final MathFunction chain)
    {
        this(chain, Math.E);
    }

    /**
     * Logarithm of chained function and user specified base. The base is the value where the logarithm function has the value
     * <code>1.0</code>.
     * @param chain the chained <code>MathFunction</code>
     * @param base the base
     */
    public Logarithm(final MathFunction chain, final double base)
    {
        this.logBaseRecip = base == Math.E ? 1.0 : (1.0 / Math.log(base));
        this.chain = chain;
    }

    @Override
    public Double apply(final Double x)
    {
        double xValue = this.chain == null ? x : this.chain.apply(x);
        return Math.log(xValue) * this.logBaseRecip;
    }

    @Override
    public MathFunction getDerivative()
    {
        if (this.chain == null)
        {
            return new Power(this.logBaseRecip, -1); // d/dx c * ln(x) === c / x
        }
        // d/dx c * ln(f(x)) === c * f'(x) / f(x)
        MathFunction result = new Quotient(this.chain.getDerivative().scaleBy(this.logBaseRecip), this.chain);
        return result.simplify();
    }

    @Override
    public MathFunction simplify()
    {
        if (this.logBaseRecip == 0.0)
        {
            return Constant.ZERO;
        }
        if (this.chain != null && this.chain instanceof Constant)
        {
            return new Constant(apply(0d)).simplify();
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
        return new Logarithm(true, this.chain, this.logBaseRecip * scaleFactor);
    }

    @Override
    public int sortPriority()
    {
        return 6;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Logarithm), IllegalArgumentException.class, "other is of wrong type");
        Logarithm otherLog = (Logarithm) other;
        if (this.logBaseRecip > otherLog.logBaseRecip)
        {
            return 1;
        }
        if (this.logBaseRecip < otherLog.logBaseRecip)
        {
            return -1;
        }
        return compareChains(this.chain, otherLog.chain);
    }

    @Override
    public MathFunction mergeAdd(final MathFunction other)
    {
        if (other instanceof Logarithm)
        {
            Logarithm otherLog = (Logarithm) other;
            if (this.chain == null && otherLog.chain == null || this.chain != null && this.chain.equals(otherLog.chain))
            {
                return new Logarithm(true, this.chain, this.logBaseRecip + otherLog.logBaseRecip);
            }
        }
        return null;
    }

    @Override
    public KnotReport getKnotReport(final Interval<?> interval)
    {
        if (this.chain != null)
        {
            return KnotReport.UNKNOWN;
        }
        return interval.low() > 0.0 ? KnotReport.NONE
                : interval.low() == 0.0 ? KnotReport.KNOWN_FINITE : KnotReport.KNOWN_INFINITE;
    }

    @Override
    public SortedSet<Double> getKnots(final Interval<?> interval)
    {
        if (this.chain != null)
        {
            throw new UnsupportedOperationException("Cannot report knots in " + interval
                    + " because I do not know where the chained function is negative or zero");
        }
        if (interval.low() > 0.0)
        {
            return new TreeSet<Double>();
        }
        if (interval.low() == 0.0)
        {
            SortedSet<Double> result = new TreeSet<>();
            result.add(0.0);
            return result;
        }
        throw new UnsupportedOperationException("There are infinitely many knots in " + interval);
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        if (this.logBaseRecip != 1.0)
        {
            result.append(printValue(this.logBaseRecip));
        }
        if (this.logBaseRecip != 0.0)
        {
            result.append("ln(");
            result.append(this.chain == null ? "x" : this.chain.toString());
            result.append(")");
        }
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.chain, this.logBaseRecip);
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
        Logarithm other = (Logarithm) obj;
        return Objects.equals(this.chain, other.chain)
                && Double.doubleToLongBits(this.logBaseRecip) == Double.doubleToLongBits(other.logBaseRecip);
    }

}
