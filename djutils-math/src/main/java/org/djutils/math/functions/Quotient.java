package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * Quotient.java.
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
public class Quotient implements MathFunction
{
    /** The numerator of this Quotient. */
    private final MathFunction numerator;

    /** The denominator of this Quotient. */
    private final MathFunction denominator;

    /**
     * Construct a new Quotient; division of two <code>MathFunction</code>s, generally <code>numerator / denominator</code>.
     * @param numerator the numerator part of the division (the part above the division line)
     * @param denominator the denominator of the devision (the part below the division line)
     */
    public Quotient(final MathFunction numerator, final MathFunction denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public double get(final double x)
    {
        return this.numerator.get(x) / this.denominator.get(x);
    }

    @Override
    public MathFunction getDerivative()
    {
        return new Quotient(
                new Sum(new Product(this.numerator.getDerivative(), this.denominator),
                        new Product(this.numerator.scaleBy(-1), this.denominator.getDerivative())),
                new Product(this.denominator, this.denominator)).simplify();
    }
    
    @Override
    public MathFunction scaleBy(final double factor)
    {
        return new Quotient(this.numerator.scaleBy(factor), this.denominator);
    }

    @Override
    public int sortPriority()
    {
        return 102;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Quotient), IllegalArgumentException.class, "other is of wrong type");
        Quotient otherQuotient = (Quotient) other;
        return this.denominator.sortPriority() - otherQuotient.sortPriority();
    }

    @Override
    public String getDescription()
    {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(this.numerator.getDescription());
        result.append(")/(");
        result.append(this.denominator.getDescription());
        result.append(")");
        return result.toString();
    }
    
    @Override
    public String getId()
    {
        return "Quotient";
    }

    @Override
    public String toString()
    {
        return "Quotient [numerator=" + this.numerator + ", denominator=" + this.denominator + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.denominator, this.numerator);
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
        Quotient other = (Quotient) obj;
        return Objects.equals(this.denominator, other.denominator) && Objects.equals(this.numerator, other.numerator);
    }
    
}
