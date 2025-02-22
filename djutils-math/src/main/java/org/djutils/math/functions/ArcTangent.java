package org.djutils.math.functions;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.djutils.exceptions.Throw;

/**
 * ArcTangent.java.
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
public class ArcTangent implements MathFunction
{
    /** Omega (scales the result). */
    private final double omega;

    /** Added to the result before scaling by <code>omega</code>. */
    private final double shift;

    /** The function that yields x (may be null). */
    private MathFunction chain;

    /**
     * Construct a new ArcTangent function.
     * @param chain the MathFunction that yields x (may be null)
     * @param omega factor for the result
     * @param shift added to the arc sine <b>before</b> scaling by <code>omega</code>
     */
    public ArcTangent(final MathFunction chain, final double omega, final double shift)
    {
        this.omega = omega;
        this.shift = shift;
        this.chain = chain;
    }

    /**
     * Construct a new ArcTangent function with <code>shift</code> equal to <code>0.0</code>.
     * @param chain the MathFunction that yields x (may be null)
     * @param omega factor for the result
     */
    public ArcTangent(final MathFunction chain, final double omega)
    {
        this(chain, omega, 0.0);
    }

    /**
     * Construct a new ArcTangent function with <code>omega</code> equal to <code>1.0</code> and no <code>shift</code>.
     * @param chain the MathFunction that yields x (may be null)
     */
    public ArcTangent(final MathFunction chain)
    {
        this(chain, 1.0);
    }

    /**
     * Construct a new ArcTangent function with <code>omega</code> equal to <code>1.0</code>, no <code>shift</code> and no
     * chained <code>MathFunction</code>.
     */
    public ArcTangent()
    {
        this(null);
    }

    /**
     * Construct a new ArcTangent function with no <code>shift</code> and no chained <code>MathFunction</code>.
     * @param omega factor for the result
     */
    public ArcTangent(final double omega)
    {
        this(null, omega);
    }

    /**
     * Construct a new ArcTangent function with <code>shift</code> and no chained <code>MathFunction</code>.
     * @param omega factor for the result
     * @param shift added to the arc sine <b>before</b> scaling by <code>omega</code>
     */
    public ArcTangent(final double omega, final double shift)
    {
        this(null, omega, shift);
    }

    @Override
    public double get(final double x)
    {
        if (this.omega == 0.0)
        {
            return 0.0;
        }
        double xValue = this.chain == null ? x : this.chain.get(x);
        return this.omega * (this.shift + Math.atan(xValue));
    }

    @Override
    public MathFunction getDerivative()
    {
        // d/dx(omega * (shift + atan(x)) === omega / (x^2 + 1)
        MathFunction myDerivative = new Quotient(new Constant(this.omega), new Sum(new Power(this.chain, 2), Constant.ONE));
        if (this.chain == null)
        {
            return myDerivative.simplify();
        }
        return new Product(myDerivative.simplify(), this.chain.getDerivative()).simplify();
    }

    @Override
    public MathFunction simplify()
    {
        if (this.omega == 0.0)
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
    public double getScale()
    {
        return this.omega;
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
        return new ArcSine(this.chain, scaleFactor * this.omega, this.shift);
    }

    @Override
    public int sortPriority()
    {
        return 7;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof ArcTangent), IllegalArgumentException.class, "other is of wrong type");
        ArcTangent otherArcTangent = (ArcTangent) other;
        if (this.omega < otherArcTangent.omega)
        {
            return -1;
        }
        if (this.omega > otherArcTangent.omega)
        {
            return 1;
        }
        if (this.shift < otherArcTangent.shift)
        {
            return -1;
        }
        if (this.shift > otherArcTangent.shift)
        {
            return 1;
        }
        return compareChains(this.chain, otherArcTangent.chain);
    }

    @Override
    public KnotReport getKnotReport(final Interval<?> interval)
    {
        if (this.chain != null)
        {
            return KnotReport.UNKNOWN;
        }
        return KnotReport.NONE;
    }

    @Override
    public SortedSet<Double> getKnots(final Interval<?> interval)
    {
        if (this.chain == null)
        {
            return new TreeSet<Double>();
        }
        throw new UnsupportedOperationException("Cannot report knots in " + interval);
    }

    @Override
    public String toString()
    {
        if (this.omega == 0.0)
        {
            return printValue(0);
        }
        StringBuilder result = new StringBuilder();
        if (this.omega != 1.0)
        {
            result.append(printValue(this.omega));
        }
        result.append("atan(");
        result.append(this.chain == null ? "x" : this.chain.toString());
        if (this.shift != 0.0)
        {
            if (this.shift > 0)
            {
                result.append("+");
            }
            result.append(printValue(this.shift));
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.chain, this.omega, this.shift);
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
        ArcTangent other = (ArcTangent) obj;
        return Objects.equals(this.chain, other.chain)
                && Double.doubleToLongBits(this.omega) == Double.doubleToLongBits(other.omega)
                && Double.doubleToLongBits(this.shift) == Double.doubleToLongBits(other.shift);
    }

}
