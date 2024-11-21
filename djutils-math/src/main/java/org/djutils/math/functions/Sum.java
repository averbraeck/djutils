package org.djutils.math.functions;

import java.util.Arrays;

import org.djutils.exceptions.Throw;

/**
 * Add up one or more FunctionInterface objects.
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
public class Sum implements Function
{
    /** The functions whose values will be summed. */
    private final Function[] terms;

    /**
     * Construct the sum of one or more functions.
     * @param functions the functions that this Sum will add together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Sum(final Function... functions)
    {
        Throw.when(functions.length == 0, IllegalArgumentException.class, "Plus needs at least one object to sum");
        this.terms = simplify(functions);
    }

    /**
     * Simplify a set of terms that must be added together.
     * @param functions the terms that must be added together
     * @return minimal array with the remaining terms
     */
    private Function[] simplify(final Function[] functions)
    {
        // Aggregate all Constant functions together and accumulate their values
        int termCount = 0;
        double totalConstant = 0.0;
        for (Function function : functions)
        {
            if (function instanceof Constant)
            {
                totalConstant += function.get(0.0);
            }
            else
            {
                termCount++;
            }
        }
        Function constantPart = null;
        if (totalConstant == 1.0)
        {
            constantPart = Constant.ONE;
        }
        if (totalConstant != 0.0)
        {
            constantPart = new Constant(totalConstant);
        }
        else if (termCount == 0)
        {
            constantPart = Constant.ZERO;
        }
        if (constantPart != null)
        {
            termCount++; // reserve a spot for the Constant term in the result
        }
        Function[] result = new Function[termCount];
        // Aggregate all non-Constant functions
        int index = 0;
        for (Function function : functions)
        {
            if (!(function instanceof Constant))
            {
                result[index++] = function;
            }
        }
        if (constantPart != null)
        {
            result[index] = constantPart;
        }
        return result;
    }

    @Override
    public double get(final double x)
    {
        double result = 0.0;
        for (Function fi : this.terms)
        {
            result += fi.get(x);
        }
        return result;
    }

    @Override
    public Function getDerivative()
    {
        Function[] derivatives = new Function[this.terms.length];
        for (int i = 0; i < this.terms.length; i++)
        {
            derivatives[i] = this.terms[i].getDerivative();
        }
        return new Sum(derivatives).simplify();
    }

    @Override
    public Function simplify()
    {
        Function[] simplifiedTerms = simplify(this.terms);
        if (simplifiedTerms.length == 1)
        {
            return simplifiedTerms[0];
        }
        if (Arrays.equals(simplifiedTerms, this.terms))
        {
            return this;
        }
        return new Sum(simplifiedTerms);
    }

    @Override
    public String getDescription()
    {
        StringBuilder result = new StringBuilder();
        result.append("\u03A3(");
        for (int i = 0; i < this.terms.length; i++)
        {
            if (i > 0)
            {
                result.append(", ");
            }
            result.append(this.terms[i].getDescription());
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public String getId()
    {
        return "Sum";
    }

    @Override
    public String toString()
    {
        return "Sum [terms=" + Arrays.toString(this.terms) + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.terms);
        return result;
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
        Sum other = (Sum) obj;
        return Arrays.equals(this.terms, other.terms);
    }

}
