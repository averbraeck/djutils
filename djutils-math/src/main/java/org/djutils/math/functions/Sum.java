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
    private final Function[] functions;

    /**
     * Construct the sum of one or more functions.
     * @param functions the functions that this Plus will sum.
     * @throws IllegalArgumentException when zero parameters are provided
     * @throws NullPointerException when a <code>null</code> value is among the arguments
     */
    public Sum(final Function... functions)
    {
        Throw.when(functions.length == 0, IllegalArgumentException.class, "Plus needs at least one object to sum");
        // Count ZERO functions
        int count = 0;
        for (Function functionInterface : functions)
        {
            if (!functionInterface.equals(Constant.ZERO))
            {
                count++;
            }
        }
        if (count == 0)
        {
            // All function arguments are ZERO; we need to use one
            this.functions = new Function[] {Constant.ZERO};
            return; // regretfully, we can't just return Constant.ZERO instead of returning this...
        }
        // Aggregate all non-ZERO functions
        this.functions = new Function[count];
        int index = 0;
        for (Function functionInterface : functions)
        {
            if (!functionInterface.equals(Constant.ZERO))
            {
                this.functions[index++] = functionInterface;
            }
        }
    }

    @Override
    public double get(final double x)
    {
        double result = 0.0;
        for (Function fi : this.functions)
        {
            result += fi.get(x);
        }
        return result;
    }

    @Override
    public Function getDerivative()
    {
        Function[] derivatives = new Function[this.functions.length];
        for (int i = 0; i < this.functions.length; i++)
        {
            derivatives[i] = this.functions[i].getDerivative();
        }
        Sum result = new Sum(derivatives);
        if (result.functions.length == 1)
        {
            return result.functions[0]; // Simplify a 1-element sum to just the element.
        }
        return result;
    }

    @Override
    public String getDescription()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (int i = 0; i < this.functions.length; i++)
        {
            if (i > 0)
            {
                stringBuilder.append("+");
            }
            stringBuilder.append(this.functions[i].getDescription());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public String getId()
    {
        return "Sum";
    }

    @Override
    public String toString()
    {
        return "Sum [functions=" + Arrays.toString(this.functions) + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.functions);
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
        return Arrays.equals(this.functions, other.functions);
    }

}
