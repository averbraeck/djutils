package org.djutils.math.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

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
public class Product implements MathFunction
{
    /** The functions whose values will be summed. */
    private final List<MathFunction> factors;

    /**
     * Construct the product of one or more functions.
     * @param functions the functions that this Product will multiply together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Product(final MathFunction... functions)
    {
        this(Arrays.asList(functions));
    }

    /**
     * Construct the product of one or more functions.
     * @param functions the functions that this Product will multiply together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Product(final List<MathFunction> functions)
    {
        Throw.when(functions.size() == 0, IllegalArgumentException.class, "Product needs at least one object to multiply");
        this.factors = simplify(functions);
    }

    /**
     * Simplify a set of factors that must be multiplied together.
     * @param functions the factors that must be multiplied together
     * @return minimal array with the remaining factors
     */
    private List<MathFunction> simplify(final List<MathFunction> functions)
    {
        List<MathFunction> result = new ArrayList<>(functions);

        // Pull up all Products that are directly embedded in this Product
        for (int index = 0; index < result.size(); index++)
        {
            MathFunction function = result.get(index);
            Throw.whenNull(function, "function");
            if (function instanceof Product)
            {
                // Replace any embedded Product by all factors that comprise that Product
                result.remove(index);
                index--;
                result.addAll(((Product) function).factors);
            }
        }
        // Optimize all elements
        for (int index = 0; index < result.size(); index++)
        {
            MathFunction function = result.get(index);
            MathFunction optimized = function.simplify();
            if (!function.equals(optimized))
            {
                result.remove(index);
                result.add(index, optimized);
            }
        }
        Collections.sort(result);
        // Aggregate all Constant functions and scale factors together and multiply their values
        // Merge all functions that can be merged 
        for (int index = 0; index < result.size(); index++)
        {
            MathFunction function = result.get(index);
            if (index < result.size() - 1)
            {
                MathFunction nextFunction = result.get(index + 1);
                MathFunction merged = function.mergeMultiply(nextFunction);
                if (merged != null)
                {
                    result.remove(index);
                    result.remove(index);
                    result.add(index, merged);
                    index--; // try to merge it with yet one more MathFunction
                }
            }
        }
        double productOfConstants = 1.0;
        for (int index = 0; index < result.size(); index++)
        {
            MathFunction function = result.get(index);
            double scale = function.getScale();
            if (function instanceof Constant)
            {
                if (0.0 == scale)
                {
                    // This may have to be revised to work in the presence of infinity or NaN values
                    result.clear();
                    result.add(Constant.ZERO);
                    return result;
                }
                // Remove this Constant and accumulate its value in our running total
                productOfConstants *= scale;
                result.remove(index);
                index--;
            }
            else if (scale != 1.0)
            {
                // Accumulate this scale factor in our running total
                productOfConstants *= scale;
                function = function.scaleBy(1.0 / scale); // remove it from the function and replace the function
                result.remove(index);
                result.add(index, function);
            }
        }
        if (productOfConstants != 1.0)
        {
            if (result.size() > 0)
            {
                // Incorporate the scale factor in the first item of the result
                MathFunction function = result.get(0);
                result.remove(0);
                function = function.scaleBy(productOfConstants);
                result.add(0, function);
            }
            else // result list is empty
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
        for (MathFunction fi : this.factors)
        {
            result *= fi.get(x);
        }
        return result;
    }

    @Override
    public MathFunction getDerivative()
    {
        List<MathFunction> result = new ArrayList<>();
        for (int i = 0; i < this.factors.size(); i++)
        {
            List<MathFunction> termFactors = new ArrayList<>(this.factors.size());
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
    public MathFunction simplify()
    {
        List<MathFunction> simplifiedFactors = simplify(this.factors);
        if (simplifiedFactors.size() == 1)
        {
            return simplifiedFactors.get(0);
        }
        return this; // Other simplification were already done in the constructor
    }

    @Override
    public int sortPriority()
    {
        return 100;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Product), IllegalArgumentException.class, "other is of wrong type");
        Product otherProduct = (Product) other;
        for (int index = 0; index < this.factors.size(); index++)
        {
            if (index >= otherProduct.factors.size())
            {
                return 1;
            }
            int result = this.factors.get(index).compareTo(otherProduct.factors.get(index));
            if (result != 0)
            {
                return result;
            }
        }
        if (otherProduct.factors.size() > this.factors.size())
        {
            return -1;
        }
        return 0;
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
        List<MathFunction> result = new ArrayList<>(this.factors);
        MathFunction scaledFactor = result.get(0).scaleBy(scaleFactor);
        result.remove(0);
        result.add(0, scaledFactor);
        return new Product(result);
    }

    @Override
    public KnotReport getKnotReport(final Interval<?> interval)
    {
        KnotReport result = KnotReport.NONE;
        for (MathFunction factor : this.factors)
        {
            result = result.combineWith(factor.getKnotReport(interval));
        }
        return result;
    }
    
    @Override
    public SortedSet<Double> getKnots(final Interval<?> interval)
    {
        SortedSet<Double> result = new TreeSet<>(); 
        for (MathFunction factor : this.factors)
        {
            result.addAll(factor.getKnots(interval));
        }
        return result;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("\u03A0("); // Capital pi (Π)
        for (int i = 0; i < this.factors.size(); i++)
        {
            if (i > 0)
            {
                result.append(", ");
            }
            result.append(this.factors.get(i).toString());
        }
        result.append(")");
        return result.toString();
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
