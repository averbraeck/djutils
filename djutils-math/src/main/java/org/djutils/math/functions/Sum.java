package org.djutils.math.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private final List<Function> terms;

    /**
     * Construct the sum of one or more functions.
     * @param functions the functions that this Sum will add together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Sum(final Function... functions)
    {
        this(Arrays.asList(functions));
    }

    /**
     * Construct the sum of one or more functions.
     * @param functions the functions that this Sum will add together.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Sum(final List<Function> functions)
    {
        Throw.when(functions.size() == 0, IllegalArgumentException.class, "Sum needs at least one object to sum");
        this.terms = simplify(functions);
    }

    /**
     * Simplify a set of terms that must be added together.
     * @param functions the terms that must be added together
     * @return minimal array with the remaining terms
     */
    private List<Function> simplify(final List<Function> functions)
    {
        List<Function> result = new ArrayList<>(functions);

        // Aggregate all Constant functions together and accumulate their values
        double totalConstant = 0.0;
        for (int index = 0; index < result.size(); index++)
        {
            Function function = result.get(index);
            if (function instanceof Constant)
            {
                // Remove this Constant and accumulate its value in our running total
                totalConstant += function.get(0.0);
                result.remove(index);
                index--;
            }
            else if (function instanceof Sum)
            {
                // Replace an embedded Sum by all elements of that Sum
                result.remove(index);
                index--;
                result.addAll(((Sum) function).terms);
            }
        }
        if (totalConstant == 1.0)
        {
            result.add(Constant.ONE);
        }
        else if (totalConstant != 0.0)
        {
            result.add(new Constant(totalConstant));
        }
        if (result.isEmpty())
        {
            result.add(Constant.ZERO); // We may not return an empty list
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
        List<Function> derivatives = new ArrayList<>();
        for (Function term : this.terms)
        {
            derivatives.add(term.getDerivative());
        }
        return new Sum(derivatives).simplify();
    }

    @Override
    public Function simplify()
    {
        List<Function> simplifiedTerms = simplify(this.terms);
        if (simplifiedTerms.size() == 1)
        {
            return simplifiedTerms.get(0);
        }
        if (this.terms.equals(simplifiedTerms))
        {
            return this;
        }
        return new Sum(simplifiedTerms);
    }

    @Override
    public String getDescription()
    {
        StringBuilder result = new StringBuilder();
        result.append("\u03A3("); // Capital sigma (Σ)
        for (int i = 0; i < this.terms.size(); i++)
        {
            if (i > 0)
            {
                result.append(", ");
            }
            result.append(this.terms.get(i).getDescription());
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
        return "Sum [terms=" + this.terms + "]";
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
