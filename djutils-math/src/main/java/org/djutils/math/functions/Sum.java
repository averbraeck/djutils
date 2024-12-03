package org.djutils.math.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * Add up one or more MathFunction objects.
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
public class Sum implements MathFunction
{
    /** The functions whose values will be summed. */
    private final List<MathFunction> terms;

    /**
     * Construct the sum of one or more functions.
     * @param functions the functions that this Sum will add together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Sum(final MathFunction... functions)
    {
        this(Arrays.asList(functions));
    }

    /**
     * Construct the sum of one or more functions.
     * @param functions the functions that this Sum will add together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Sum(final List<MathFunction> functions)
    {
        Throw.when(functions.size() == 0, IllegalArgumentException.class, "Sum needs at least one object to sum");
        this.terms = simplify(functions);
    }

    /**
     * Simplify a set of terms that must be added together.
     * @param functions the terms that must be added together
     * @return minimal array with the remaining terms
     */
    private List<MathFunction> simplify(final List<MathFunction> functions)
    {
        List<MathFunction> result = new ArrayList<>(functions);

        // Pull up all Sums that are embedded in this Sum
        for (int index = 0; index < result.size(); index++)
        {
            MathFunction function = result.get(index);
            Throw.whenNull(function, "function");
            if (function instanceof Sum)
            {
                // Replace any embedded Sum by all terms that comprise that Sum
                result.remove(index);
                index--;
                result.addAll(((Sum) function).terms);
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
        // Merge all functions that can be merged
        for (int index = 0; index < result.size(); index++)
        {
            MathFunction function = result.get(index);
            if (function.equals(Constant.ZERO))
            {
                result.remove(index);
                index--;
            }
            else if (index < result.size() - 1)
            {
                MathFunction nextFunction = result.get(index + 1);
                MathFunction merged = function.mergeAdd(nextFunction);
                if (merged != null)
                {
                    result.remove(index);
                    result.remove(index);
                    result.add(index, merged);
                    index--; // try to merge it with yet one more MathFunction
                }
            }
        }
        if (result.size() == 0)
        {
            result.add(Constant.ZERO);
        }
        return result;
    }

    @Override
    public double get(final double x)
    {
        double result = 0.0;
        for (MathFunction fi : this.terms)
        {
            result += fi.get(x);
        }
        return result;
    }

    @Override
    public MathFunction getDerivative()
    {
        List<MathFunction> derivatives = new ArrayList<>();
        for (MathFunction term : this.terms)
        {
            derivatives.add(term.getDerivative());
        }
        return new Sum(derivatives).simplify();
    }

    @Override
    public MathFunction simplify()
    {
        List<MathFunction> simplifiedTerms = simplify(this.terms);
        if (simplifiedTerms.size() == 1)
        {
            return simplifiedTerms.get(0);
        }
        return this;
    }

    @Override
    public MathFunction scaleBy(final double factor)
    {
        if (factor == 0.0)
        {
            return Constant.ZERO;
        }
        if (factor == 1.0)
        {
            return this;
        }
        List<MathFunction> result = new ArrayList<>(this.terms.size());
        for (MathFunction function : this.terms)
        {
            result.add(function.scaleBy(factor));
        }
        return new Sum(result);
    }

    @Override
    public int sortPriority()
    {
        return 101;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Sum), IllegalArgumentException.class, "other is of wrong type");
        Sum otherSum = (Sum) other;
        for (int index = 0; index < this.terms.size(); index++)
        {
            if (index >= otherSum.terms.size())
            {
                return 1;
            }
            int result = this.terms.get(index).compareTo(otherSum.terms.get(index));
            if (result != 0)
            {
                return result;
            }
        }
        if (otherSum.terms.size() > this.terms.size())
        {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("\u03A3("); // Capital sigma (Σ)
        for (int i = 0; i < this.terms.size(); i++)
        {
            if (i > 0)
            {
                result.append(", ");
            }
            result.append(this.terms.get(i).toString());
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.terms);
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
        return Objects.equals(this.terms, other.terms);
    }

}
