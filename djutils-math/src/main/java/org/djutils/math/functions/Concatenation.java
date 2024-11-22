package org.djutils.math.functions;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.djutils.exceptions.Throw;

/**
 * Concatenate FunctionInterface objects
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
public class Concatenation implements MathFunction
{
    /** The wrapped functions. */
    private SortedSet<Interval<MathFunction>> functions;

    /**
     * Construct the concatenation of one or more MathFunction objects.
     * @param intervals the functions and the domains over which they should be active
     */
    @SafeVarargs
    public Concatenation(final Interval<MathFunction>... intervals)
    {
        this(convertToSortedSet(intervals));
    }

    /**
     * Convert an array of Interval&lt;FunctionInterface&gt; to a SortedSet.
     * @param intervals the intervals
     * @return sorted set
     */
    @SafeVarargs
    private static SortedSet<Interval<MathFunction>> convertToSortedSet(final Interval<MathFunction>... intervals)
    {
        SortedSet<Interval<MathFunction>> result = new TreeSet<>();
        for (var interval : intervals)
        {
            result.add(interval);
        }
        return result;
    }

    /**
     * Construct a Concatenation from a sorted set of Interval&lt;MathFunction&gt;.
     * @param set the sorted set of Interval with MathFunction payload
     */
    public Concatenation(final SortedSet<Interval<MathFunction>> set)
    {
        // Run the ordered list and check for overlaps and add NaN functions where there are gaps
        Interval<MathFunction> prevInterval = null;
        for (var interval : set)
        {
            Interval<MathFunction> thisInterval = interval;
            if (prevInterval != null)
            {
                Throw.when(!prevInterval.disjunct(thisInterval), IllegalArgumentException.class,
                        "Overlapping domains not permitted");
                if (prevInterval.high() < thisInterval.low() || (prevInterval.high() == thisInterval.low()
                        && (!prevInterval.highInclusive()) && (!thisInterval.lowInclusive())))
                {
                    // There is a gap; fill it with a NaN function
                    set.add(new Interval<MathFunction>(prevInterval.high(), !prevInterval.highInclusive(), thisInterval.low(),
                            !thisInterval.lowInclusive(), Nan.NAN));
                }
            }
            prevInterval = thisInterval;
        }
        Throw.when(set.size() < 1, IllegalArgumentException.class, "need at least one argument");
        this.functions = set;
    }

    @Override
    public double get(final double x)
    {
        // TODO Use bisection to home in on the interval that covers x; for now use linear search
        for (var interval : this.functions)
        {
            if (interval.covers(x))
            {
                return interval.payload().get(x);
            }
        }
        throw new IllegalArgumentException(String.format("x is outside the combined domain of this Concatenation", x));
    }

    @Override
    public Concatenation getDerivative()
    {
        SortedSet<Interval<MathFunction>> set = new TreeSet<>();
        for (var interval : this.functions)
        {
            set.add(new Interval<MathFunction>(interval.low(), interval.lowInclusive(), interval.high(), interval.highInclusive(),
                    interval.payload().getDerivative()));
        }
        return new Concatenation(set);
    }

    @Override
    public MathFunction scaleBy(final double factor)
    {
        if (factor == 1.0)
        {
            return this;
        }
        SortedSet<Interval<MathFunction>> result = new TreeSet<>();
        for (Interval<MathFunction> interval : this.functions)
        {
            result.add(new Interval<MathFunction>(interval.low(), interval.lowInclusive(), interval.high(),
                    interval.highInclusive(), interval.payload().scaleBy(factor)));
        }
        return new Concatenation(result);
    }

    @Override
    public String getDescription()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IntervalSet(");
        boolean first = true;
        for (var interval : this.functions)
        {
            if (!first)
            {
                stringBuilder.append(", ");
            }
            stringBuilder.append(interval.lowInclusive() ? "[" : "(");
            stringBuilder.append(printValue(interval.low()));
            stringBuilder.append(",");
            stringBuilder.append(printValue(interval.high()));
            stringBuilder.append(interval.highInclusive() ? "]" : ")");
            stringBuilder.append("\u2192");
            stringBuilder.append(interval.payload().getDescription());
            first = false;
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public String getId()
    {
        return "Concatenation";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.functions);
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
        Concatenation other = (Concatenation) obj;
        return Objects.equals(this.functions, other.functions);
    }

}
