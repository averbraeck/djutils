package org.djutils.math.functions;

import java.util.Objects;

/**
 * Functions that are a constant times some power of x; generally <code>f(x) &rarr; a * x^<sup>b</sup></code> where a &isin;
 * &#8477 and b &isin; &#8477
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
public class PowerFunction implements Function
{
    /** The weight (value at x == 0). */
    private final double weight;

    /** The power value. */
    private final double power;

    /**
     * Construct a new power function.
     * @param weight the value at <code>x == 1</code>
     * @param power the power
     */
    public PowerFunction(final double weight, final double power)
    {
        this.weight = weight;
        this.power = weight == 0.0 ? 1.0 : power;
    }

    /**
     * Create a new power function with weight 1.0 and the supplied value as exponent.
     * @param power the power
     */
    public PowerFunction(final double power)
    {
        this(1.0, power);
    }

    @Override
    public double get(final double x)
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
        if (this.power == 1.0)
        {
            return this.weight * x;
        }
        return this.weight * Math.pow(x, this.power);
    }

    @Override
    public Function getDerivative()
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
        return new PowerFunction(this.weight * this.power, this.power - 1.0);
    }

    @Override
    public String getDescription()
    {
        if (this.weight == 0)
        {
            return ("0");
        }
        StringBuilder result = new StringBuilder();
        if (this.weight != 1.0 || this.power == 0.0)
        {
            result.append(printValue(this.weight));
        }
        if (this.power != 0.0)
        {
            result.append("x");
            if (this.power > 1 && this.power <= 9 && this.power % 1 == 0)
            {
                // Print single digit power using unicode superscript symbols
                int index = ((int) this.power) - 2;
                result.append("\u00B2\u00B3\u2074\u2075\u2076\u2077\u2078\u2079".substring(index, index + 1));
            }
            else if (this.power != 1)
            {
                result.append("^");
                result.append(printValue(this.power));
            }
        }
        return result.toString();
    }

    @Override
    public String getId()
    {
        return ("POW");
    }

    @Override
    public String toString()
    {
        return "PowerFunction [weight=" + printValue(this.weight) + ", power=" + printValue(this.power) + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.power, this.weight);
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
        PowerFunction other = (PowerFunction) obj;
        return Double.doubleToLongBits(this.power) == Double.doubleToLongBits(other.power)
                && Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(other.weight);
    }

}
