package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * ArcSine function.
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
public class ArcSine implements MathFunction
{
    /** Omega (scales the result). */
    private final double omega;

    /** Added to the result before scaling by <code>omega</code>. */
    private final double shift;

    /** The function that yields x (may be null). */
    private MathFunction chain;

    /**
     * Construct a new ArcSine function.
     * @param chain the MathFunction that yields x (may be null)
     * @param omega factor for the result
     * @param shift added to the arc sine <b>before</b> scaling by <code>omega</code>
     */
    public ArcSine(final MathFunction chain, final double omega, final double shift)
    {
        this.omega = omega;
        this.shift = shift;
        this.chain = chain;
    }

    /**
     * Construct a new ArcSine function with <code>shift</code> equal to <code>0.0</code>.
     * @param chain the MathFunction that yields x (may be null)
     * @param omega factor for the result
     */
    public ArcSine(final MathFunction chain, final double omega)
    {
        this(chain, omega, 0.0);
    }

    /**
     * Construct a new ArcSine function with <code>omega</code> equal to <code>1.0</code> and no <code>shift</code>.
     * @param chain the MathFunction that yields x (may be null)
     */
    public ArcSine(final MathFunction chain)
    {
        this(chain, 1.0);
    }

    /**
     * Construct a new ArcSine function with <code>omega</code> equal to <code>1.0</code>, no <code>shift</code> and no chained
     * <code>MathFunction</code>.
     */
    public ArcSine()
    {
        this(null);
    }

    /**
     * Construct a new ArcSine function with no <code>shift</code> and no chained <code>MathFunction</code>.
     * @param omega factor for the result
     */
    public ArcSine(final double omega)
    {
        this(null, omega);
    }

    /**
     * Construct a new ArcSine function with no <code>shift</code> and no chained <code>MathFunction</code>.
     * @param omega factor for the result
     * @param shift added to the arc sine <b>before</b> scaling by <code>omega</code>
     */
    public ArcSine(final double omega, final double shift)
    {
        this(null, omega, shift);
    }

    /**
     * Construct an arc cosine function using the equation <code>acos(x) === &pi;/2-asin(x)</code>.
     * @param chain the MathFunction that yields x (may be null)
     * @param omega factor for the result
     * @return a ArcSine object that is set up to actually yield the arc cosine function
     */
    public static ArcSine arcCosine(final MathFunction chain, final double omega)
    {
        return new ArcSine(chain, -omega, -Math.PI / 2);
    }

    @Override
    public double get(final double x)
    {
        if (this.omega == 0.0)
        {
            return 0.0;
        }
        double xValue = this.chain == null ? x : this.chain.get(x);
        return this.omega * (this.shift + Math.asin(xValue));
    }

    @Override
    public MathFunction getDerivative()
    {
        // d/dx(omega * asin(x + shift)) === omega * (-x^2 - 2 * shift * x + 1 - shift^2)^-0.5
        MathFunction myDerivative = new Power(
                new Sum(new Power(this.chain, -1, 2), new Power(-2 * this.shift, 1), new Constant(1 - this.shift * this.shift)),
                1, -0.5).scaleBy(this.omega);
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
        return 5;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof ArcSine), IllegalArgumentException.class, "other is of wrong type");
        ArcSine otherArcSine = (ArcSine) other;
        if (this.omega < otherArcSine.omega)
        {
            return -1;
        }
        if (this.omega > otherArcSine.omega)
        {
            return 1;
        }
        if (this.shift < otherArcSine.shift)
        {
            return -1;
        }
        if (this.shift > otherArcSine.shift)
        {
            return 1;
        }
        return compareChains(this.chain, otherArcSine.chain);
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
        result.append("asin(");
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
        ArcSine other = (ArcSine) obj;
        return Objects.equals(this.chain, other.chain)
                && Double.doubleToLongBits(this.omega) == Double.doubleToLongBits(other.omega)
                && Double.doubleToLongBits(this.shift) == Double.doubleToLongBits(other.shift);
    }

}
