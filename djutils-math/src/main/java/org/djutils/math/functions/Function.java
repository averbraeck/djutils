package org.djutils.math.functions;

import org.djutils.base.Describable;

/**
 * Function interface
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface Function extends Describable
{

    /**
     * Returns the data at given fractional length. If only data beyond the fractional length is available, the first available
     * value is returned. If only data before the fractional length is available, the last available value is returned.
     * Otherwise data is linearly interpolated.
     * @param fraction fractional length, may be outside range <code>[0.0, 1.0]</code>, but below 0.0 and above 1.1 the result
     *            will be constant
     * @return interpolated or extended value, at knots, or discontinuities, the result will be <code>NaN</code>
     */
    double get(double fraction);

    /**
     * Returns the derivative of the data with respect to fractional length.
     * @return derivative of this Function
     */
    Function getDerivative();
    
    /**
     * Attempts to find a simplified version of this Function (e.g. replace <code>1 - 5</code> by <code>-4</code>). 
     * @return <code>this</code>, or a simplified version thereof
     */
    default Function simplify()
    {
        return this;
    }
    
    /**
     * Incorporate a multiplication factor to this Function. 
     * @param factor the factor to incorporate
     * @return a new Function that yields the same result as the original function multiplied by the <code>factor</code>
     */
    Function scaleBy(double factor);

    /**
     * Format a numerical value. If the value is integer, format it without decimal point. If the value is not integer, use a
     * reasonable format.
     * @param value the value to format
     * @return the formatted value
     */
    default String printValue(final double value)
    {
        if (value <= Long.MAX_VALUE && value >= Long.MIN_VALUE && value == Math.ceil(value))
        {
            return String.format("%d", (long) value);
        }
        return "" + value;
    }

    /**
     * Wrapper for one domain and function value pair.
     * @param s double; value in the domain of the function
     * @param t double; value of the function for <code>s</code>
     */
    record TupleSt(double s, double t)
    {
    }

}
