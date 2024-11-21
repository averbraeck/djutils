package org.djutils.math.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private final List<Function> factors;

    /**
     * Construct the product of one or more functions.
     * @param functions the functions that this Product will multiply together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Product(final Function... functions)
    {
        this(Arrays.asList(functions));
    }

    /**
     * Construct the product of one or more functions.
     * @param functions the functions that this Product will multiply together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Product(final List<Function> functions)
    {
        Throw.when(functions.size() == 0, IllegalArgumentException.class, "Product needs at least one object to multiply");
        this.factors = simplify(functions);
    }

    /**
     * Simplify a set of factors that must be multiplied together.
     * @param functions the factors that must be multiplied together
     * @return minimal array with the remaining factors
     */
    private List<Function> simplify(final List<Function> functions)
    {
        List<Function> result = new ArrayList<>(functions);

        // Aggregate all Constant functions together and multiply their values
        double productOfConstants = 1.0;
        for (int index = 0; index < result.size(); index++)
        {
            Function function = result.get(index);
            if (function instanceof Constant)
            {
                double value = function.get(0.0);
                if (0.0 == value)
                {
                    result.clear();
                    result.add(Constant.ZERO);
                    return result;
                }
                // Remove this Constant and accumulate its value in our running total
                productOfConstants *= value;
                result.remove(index);
                index--;
            }
            else if (function instanceof Product)
            {
                // Replace an embedded Product by all factors of that Product
                result.remove(index);
                index--;
                result.addAll(((Product) function).factors);
            }
        }
        if (productOfConstants != 1.0)
        {
            if (result.size() > 0)
            {
                List<Function> newList = new ArrayList<>(result.size());
                for (Function function : result)
                {
                    newList.add(function.scaleBy(productOfConstants));
                }
                result = newList;
            }
            else
            {
                result.add(new Constant(productOfConstants));
            }
        }
        if (result.size() == 0)
        {
            result.add(Constant.ONE);
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
        List<Function> result = new ArrayList<>();
        for (int i = 0; i < this.factors.size(); i++)
        {
            List<Function> termFactors = new ArrayList<>(this.factors.size());
            for (int j = 0; j < this.factors.size(); j++)
            {
                if (j == i)
                {
                    termFactors.add(this.factors.get(j).getDerivative());
                }
                else
                {
                    termFactors.add(this.factors.get(j));
                }
            }
            result.add(new Product(termFactors).simplify());
        }
        return new Sum(result).simplify();
    }

    @Override
    public Function simplify()
    {
        List<Function> simplifiedFactors = simplify(this.factors);
        if (simplifiedFactors.size() == 1)
        {
            return simplifiedFactors.get(0);
        }
        if (this.factors.equals(simplifiedFactors))
        {
            return this;
        }
        return new Product(simplifiedFactors);

    }

    @Override
    public String getDescription()
    {
        StringBuilder result = new StringBuilder();
        result.append("\u03A0("); // Capital pi (Π)
        for (int i = 0; i < this.factors.size(); i++)
        {
            if (i > 0)
            {
                result.append(", ");
            }
            result.append(this.factors.get(i).getDescription());
        }
        result.append(")");
        return result.toString();
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
        List<Function> result = new ArrayList<>(this.factors.size());
        for (Function factor : this.factors)
        {
            result.add(factor.scaleBy(scaleFactor));
        }
        return new Product(result);
    }

    @Override
    public String getId()
    {
        return "Sum";
    }

    @Override
    public String toString()
    {
        return "Product [factors=" + this.factors + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.factors);
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
        return Objects.equals(this.factors, other.factors);
    }

}
