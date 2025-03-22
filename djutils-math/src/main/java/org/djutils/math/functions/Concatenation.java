package org.djutils.math.functions;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.djutils.exceptions.Throw;

/**
 * Concatenate FunctionInterface objects
 * <p>
 * Copyright (c) 2024-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    public Double apply(final Double x)
    {
        // TODO Use bisection to home in on the interval that covers x; for now use linear search
        for (var interval : this.functions)
        {
            if (interval.covers(x))
            {
                return interval.payload().apply(x);
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
            set.add(new Interval<MathFunction>(interval.low(), interval.lowInclusive(), interval.high(),
                    interval.highInclusive(), interval.payload().getDerivative()));
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
    public int sortPriority()
    {
        return 110;
    }

    /**
     * Construct a concatenation that is piecewise linear through a given set of points.
     * @param map mapping from domain to value at the inflection points
     * @return new Concatenation that is piecewise linear and connects the given points
     * @throws IllegalArgumentException when <code>map</code> contains fewer than 2 entries
     */
    public static Concatenation continuousPiecewiseLinear(final SortedMap<Double, Double> map)
    {
        SortedSet<Interval<MathFunction>> intervals = new TreeSet<>();
        Entry<Double, Double> prevEntry = null;
        for (Entry<Double, Double> nextEntry : map.entrySet())
        {
            if (prevEntry != null)
            {
                // create one linear section
                double slope = (nextEntry.getValue() - prevEntry.getValue()) / (nextEntry.getKey() - prevEntry.getKey());
                Power powerFunction = new Power(slope, 1);
                double constant = prevEntry.getValue() - powerFunction.apply(prevEntry.getKey());
                MathFunction function = new Sum(new Constant(constant), powerFunction);
                intervals.add(new Interval<MathFunction>(prevEntry.getKey(), intervals.isEmpty(), nextEntry.getKey(), true,
                        function));
            }
            prevEntry = nextEntry;
        }
        Throw.when(intervals.isEmpty(), IllegalArgumentException.class, "need at least two points");
        return new Concatenation(intervals);
    }

    /**
     * Construct a concatenation that is piecewise linear through a given set of input-output pairs.
     * @param arguments the input-output pairs; these specify the inflection points
     * @return new Concatenation that is piecewise linear and connects the given points
     * @throws IllegalArgumentException when <code>arguments</code> contains an odd number of entries, or fewer than 2 domain
     *             values, or duplicate domain values with differing function values
     */
    public static Concatenation continuousPiecewiseLinear(final double... arguments)
    {
        Throw.when(arguments.length % 2 != 0, IllegalArgumentException.class, "need an even number of arguments");
        SortedMap<Double, Double> map = new TreeMap<>();
        for (int i = 0; i < arguments.length; i += 2)
        {
            Throw.when(map.containsKey(arguments[i]) && arguments[i + 1] != map.get(arguments[i]),
                    IllegalArgumentException.class, "duplicate domain value with different function value is not permitted");
            map.put(arguments[i], arguments[i + 1]);
        }
        return continuousPiecewiseLinear(map);
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Concatenation), IllegalArgumentException.class, "other is of wrong type");
        return 0;
    }

    /**
     * Report all non-continuities and all points where <code>this</code> function is non differentiable, or non-evaluable. If
     * another <code>MathFunction</code> is chained, the transformation of that function, nor any discontinuities of that
     * <code>MathFunction</code> are taken into account as there is (currently) no way to figure out what values of the domain
     * of the chained function result in values that correspond to the discontinuities of <code>this</code> function.
     * @param interval the interval on which to report the discontinuities
     * @return iterator that will generate all discontinuities in the interval
     */
    public Iterator<Interval<Discontinuity>> discontinuities(final Interval<?> interval)
    {
        return new Iterator<Interval<Discontinuity>>()
        {
            /** The interval over which the discontinuities were requested. */
            private Interval<?> requestedInterval = interval;

            /** Iterator that visits all the internal intervals/functions of the Concatenation in sequence. */
            private Iterator<Interval<MathFunction>> internalIterator = Concatenation.this.functions.iterator();

            /** The current interval (made available by the hasNext method and cleared by the next method). */
            private Interval<MathFunction> currentInterval = null;

            @Override
            public boolean hasNext()
            {
                if (this.currentInterval == null && (!this.internalIterator.hasNext()))
                {
                    return false; // out of data
                }
                while (this.currentInterval == null && this.internalIterator.hasNext())
                {
                    this.currentInterval = this.internalIterator.next().intersection(this.requestedInterval);
                }
                return this.currentInterval != null;
            }

            @Override
            public Interval<Discontinuity> next()
            {
                Throw.when(this.currentInterval == null, NoSuchElementException.class, "Out of data");
                Interval<Discontinuity> result = this.currentInterval.payload() instanceof Nan
                        ? new Interval<>(this.currentInterval.low(), true, this.currentInterval.high(),
                                this.currentInterval.highInclusive(), Discontinuity.GAP)
                        : new Interval<>(this.currentInterval.low(), true, this.currentInterval.low(), true,
                                Discontinuity.KNOT);
                this.currentInterval = null;
                return result;
            }
        };
    }

    @Override
    public KnotReport getKnotReport(final Interval<?> interval)
    {
        KnotReport result = KnotReport.NONE;
        if (this.functions.first().low() > interval.low() || (this.functions.last().high() < interval.high()))
        {
            result = KnotReport.KNOWN_INFINITE;
        }
        for (Interval<MathFunction> i : this.functions)
        {
            Interval<MathFunction> intersection = i.intersection(interval);
            if (intersection != null)
            {
                if (interval.covers(i.low()))
                {
                    result = result.combineWith(KnotReport.KNOWN_FINITE);
                }
                if (interval.covers(i.high()))
                {
                    result = result.combineWith(KnotReport.KNOWN_FINITE);
                }
                result = result.combineWith(i.payload().getKnotReport(interval));
            }
        }
        return result;
    }

    @Override
    public SortedSet<Double> getKnots(final Interval<?> interval)
    {
        Throw.when(this.functions.first().low() > interval.low() || (this.functions.last().high() < interval.high()),
                UnsupportedOperationException.class, "Concatentation is undefined over (part of) " + interval);
        SortedSet<Double> result = new TreeSet<Double>();
        for (Interval<MathFunction> i : this.functions)
        {
            Interval<MathFunction> intersection = i.intersection(interval);
            if (intersection != null)
            {
                result.addAll(i.payload().getKnots(interval));
            }
            if (interval.covers(i.low()))
            {
                result.add(i.low());
            }
            if (interval.covers(i.high()))
            {
                result.add(i.high());
            }
        }
        return result;
    }

    @Override
    public String toString()
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
            stringBuilder.append(interval.toString());
            first = false;
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
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

    /** The various discontinuities reported by the <code>discontinuities</code> method. */
    enum Discontinuity
    {
        /** Continuous, but not differentiable. */
        KNOT,

        /** Not continuous (and, therefore, not differentiable). */
        DISCONTINUOUS,

        /** Function undefined in this interval; the <code>MathFunction</code> will yield <code>NaN</code> in this interval. */
        GAP;
    }

}
