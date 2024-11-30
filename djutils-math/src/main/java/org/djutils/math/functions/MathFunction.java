package org.djutils.math.functions;

/**
 * MathFunction interface.
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
public interface MathFunction extends Comparable<MathFunction>
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
     * @return derivative of this MathFunction
     */
    MathFunction getDerivative();

    /**
     * Attempts to find a simplified version of this MathFunction (e.g. replace <code>1 - 5</code> by <code>-4</code>).
     * @return <code>this</code>, or a simplified version thereof
     */
    default MathFunction simplify()
    {
        return this;
    }

    /**
     * Get the scale factor of this MathFunction.
     * @return the scale factor of this MathFunction
     */
    default double getScale()
    {
        return 1.0;
    }

    /**
     * Incorporate a multiplication factor to this MathFunction.
     * @param factor the factor to incorporate
     * @return a new MathFunction that yields the same result as the original function multiplied by the <code>factor</code>
     */
    MathFunction scaleBy(double factor);

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
     * Sorting priority of this type of MathFunction (low values shall sort before higher).
     * @return sorting priority of this type of MathFunction
     */
    int sortPriority();

    /**
     * Determine sorting order among instances of a particular sub type of <code>MathFunction</code>. The sorting order should
     * sort <code>MathFunction</code>s that may be combined next to one another. Because <code>MathFunction</code>s are also
     * used in <code>SortedSet</code>s, this comparator may return 0 <b>if and only if</b> this and other are entirely equal!
     * @param other the other <code>MathFunction</code> that must be of the same type
     * @return int; &lt; 0 when this sorts before other; &gt; 0 when this sorts after other; 0 when this and other are identical
     */
    int compareWithinSubType(MathFunction other);

    /**
     * This MathFunction is added to another; try to replace both by a combined <code>MathFunction</code>.
     * @param other the other <code>MathFunction</code>
     * @return combined MathFunction, or null when the two could not be combined
     */
    default MathFunction mergeAdd(final MathFunction other)
    {
        return null;
    }

    /**
     * This MathFunction is multiplied by another; try to replace both by a combined MathFunction.
     * @param other the other MathFunction
     * @return combined MathFunction, or null when the two could not be combined
     */
    default MathFunction mergeMultiply(final MathFunction other)
    {
        return null;
    }

    @Override
    default int compareTo(final MathFunction other)
    {
        int result = this.sortPriority() - other.sortPriority();
        if (result == 0)
        {
            return compareWithinSubType(other);
        }
        return result;
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
