package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.math.AngleUtil;

/**
 * Sine function.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Sine implements Function
{
    /** Size multiplier. */
    private final double factor;

    /** Time scale multiplier. */
    private final double omega;

    /** Time scale shift. */
    private final double shift;

    /**
     * Construct a new Sine function <code>factor * sin(omega * x + shift)</code>.
     * @param amplitude multiplication factor for the output
     * @param omega radial frequency; multiplication factor for the input
     * @param shift time shift for the input (applied <b>after</b> the <code>omega</code> factor)
     */
    public Sine(final double amplitude, final double omega, final double shift)
    {
        this.factor = amplitude;
        this.omega = omega;
        this.shift = shift;
    }

    @Override
    public double get(final double x)
    {
        if (this.factor == 0.0)
        {
            return 0.0;
        }
        return this.factor * Math.sin(this.omega * x + this.shift);
    }

    @Override
    public Function getDerivative()
    {
        return new Sine(this.factor * this.omega, this.omega, AngleUtil.normalizeAroundZero(this.shift + Math.PI / 2));
    }
    
    @Override
    public Function simplify()
    {
        if (this.factor == 0.0)
        {
            return Constant.ZERO;
        }
        return this;
    }
    
    @Override
    public Function scaleBy(final double scaleFactor)
    {
        if (scaleFactor == 0.0)
        {
            return Constant.ZERO;
        }
        if (scaleFactor == 1.0)
        {
            return this;
        }
        return new Sine(scaleFactor * this.factor, this.omega, this.shift);
    }

    @Override
    public String getDescription()
    {
        StringBuilder result = new StringBuilder();
        if (this.factor != 1.0)
        {
            result.append(printValue(this.factor));
        }
        result.append("sin(");
        if (this.omega != 1.0)
        {
            result.append(printValue(this.omega));
        }
        result.append("x");
        if (this.shift != 0.0)
        {
            if (this.shift >= 0.0)
            {
                result.append("+");
            }
            result.append(printValue(this.shift));
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public String getId()
    {
        return ("sin");
    }

    @Override
    public String toString()
    {
        return "Sine [factor=" + printValue(this.factor) + ", omega=" + printValue(this.omega) + ", shift="
                + printValue(this.shift) + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.factor, this.omega, this.shift);
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
        Sine other = (Sine) obj;
        return Double.doubleToLongBits(this.factor) == Double.doubleToLongBits(other.factor)
                && Double.doubleToLongBits(this.omega) == Double.doubleToLongBits(other.omega)
                && Double.doubleToLongBits(this.shift) == Double.doubleToLongBits(other.shift);
    }

}
