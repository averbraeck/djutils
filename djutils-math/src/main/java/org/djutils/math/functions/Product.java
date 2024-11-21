package org.djutils.math.functions;

import java.util.Arrays;

import org.djutils.exceptions.Throw;

/**
 * Multiply functions.
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
public class Product implements Function
{
    /** The functions whose values will be summed. */
    private final Function[] factors;

    /**
     * Construct the product of one or more functions.
     * @param functions the functions that this Product will multiply together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Product(final Function... functions)
    {
        Throw.when(functions.length == 0, IllegalArgumentException.class, "Plus needs at least one object to sum");
        this.factors = simplify(functions);
    }

    /**
     * Simplify a set of factors that must be multiplied together.
     * @param functions the factors that must be multiplied together
     * @return minimal array with the remaining factors
     */
    private Function[] simplify(final Function[] functions)
    {
        // Aggregate all Constant functions together and multiply their values
        int factorCount = 0;
        double totalConstant = 1.0;
        for (Function function : functions)
        {
            if (function instanceof Constant)
            {
                totalConstant *= function.get(0.0);
            }
            else
            {
                factorCount++;
            }
        }
        if (totalConstant == 0.0)
        {
            return new Function[] {Constant.ZERO};
        }
        Function constantPart = null;
        if (factorCount == 0)
        {
            constantPart = Constant.ONE;
        }
        if (totalConstant != 1.0)
        {
            constantPart = new Constant(totalConstant);
        }
        if (constantPart != null)
        {
            factorCount++;
        }
        // Aggregate all non-Constant functions
        Function[] result = new Function[factorCount];
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
        double result = 1.0;
        for (Function fi : this.factors)
        {
            result *= fi.get(x);
        }
        return result;
    }

    @Override
    public Function getDerivative()
    {
        Function[] terms = new Function[this.factors.length];
        for (int i = 0; i < this.factors.length; i++)
        {
            Function[] termFactors = new Function[this.factors.length];
            boolean cancel = false;
            for (int j = 0; j < this.factors.length; j++)
            {
                if (j == i)
                {
                    termFactors[j] = this.factors[j].getDerivative();
                }
                else
                {
                    termFactors[j] = this.factors[j];
                }
                if (termFactors[j].equals(Constant.ZERO))
                {
                    cancel = true;
                }
            }
            terms[i] = cancel ? Constant.ZERO : new Product(termFactors).simplify();
        }
        return new Sum(terms).simplify();
    }

    @Override
    public Function simplify()
    {
        Function[] simplifiedFactors = simplify(this.factors);
        if (simplifiedFactors.length == 1)
        {
            return simplifiedFactors[0];
        }
        if (Arrays.equals(simplifiedFactors, this.factors))
        {
            return this;
        }
        return new Product(simplifiedFactors);

    }

    @Override
    public String getDescription()
    {
        StringBuilder result = new StringBuilder();
        result.append("\u03A0(");
        for (int i = 0; i < this.factors.length; i++)
        {
            if (i > 0)
            {
                result.append(", ");
            }
            result.append(this.factors[i].getDescription());
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
        return "Product [factors=" + Arrays.toString(this.factors) + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.factors);
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
        Product other = (Product) obj;
        return Arrays.equals(this.factors, other.factors);
    }

}
