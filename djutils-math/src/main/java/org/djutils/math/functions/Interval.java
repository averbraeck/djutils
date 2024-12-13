package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * Immutable double interval, optionally including none, one, or both boundary values.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <T> the payload type
 * @param low low limit of the domain
 * @param lowInclusive if true; the low limit is included; if false, the low limit is not included
 * @param high high limit of the domain (inclusive)
 * @param highInclusive if true; the high limit is included; if false; the high limit is not included
 * @param payload the payload of this Interval
 */
record Interval<T extends Comparable<T>>(double low, boolean lowInclusive, double high, boolean highInclusive, T payload)
        implements Comparable<Interval<?>>
{
    /**
     * Construct a new Interval.
     * @param low low limit of the domain
     * @param lowInclusive if true; the low limit is included; if false, the low limit is not included
     * @param high high limit of the domain (inclusive)
     * @param highInclusive if true; the high limit is included; if false; the high limit is not included
     * @param payload the payload of this Interval
     */
    Interval(final double low, final boolean lowInclusive, final double high, final boolean highInclusive, final T payload)
    {
        Throw.when(low > high, IllegalArgumentException.class, "low may not be higher than high");
        Throw.when(low == high && (!lowInclusive) && (!highInclusive), IllegalArgumentException.class,
                "zero width interval must include at least one of its boundaries");
        this.low = low;
        this.lowInclusive = lowInclusive;
        this.high = high;
        this.highInclusive = highInclusive;
        this.payload = payload;
    }

    /**
     * Check if this Interval completely covers some other Interval.
     * @param other the other Interval (not necessarily carrying a similarly typed pay load)
     * @return boolean; true if this Interval completely covers the other Interval; false if any part of other Interval (which
     *         may be infinitesimally small) is outside this Interval
     */
    public boolean covers(final Interval<?> other)
    {
        if (this.low > other.low || this.high < other.high)
        {
            return false;
        }
        if (this.low == other.low && (!this.lowInclusive) && other.lowInclusive)
        {
            return false;
        }
        if (this.high == other.high && (!this.highInclusive) && other.highInclusive)
        {
            return false;
        }
        return true;
    }

    /**
     * Check if this Interval is completely disjunct of some other Interval.
     * @param other the other Interval (not necessarily carrying a similarly typed pay load)
     * @return boolean; true if this Interval is completely disjunct of the other Interval; false if any part of this Interval
     *         (which may be infinitesimally small) covers the other Interval
     */
    public boolean disjunct(final Interval<?> other)
    {
        if (this.high < other.low || this.low > other.high)
        {
            return true;
        }
        if (this.high == other.low && ((!this.highInclusive) || (!other.lowInclusive)))
        {
            return true;
        }
        if (this.low == other.high && ((!this.lowInclusive) || (!other.highInclusive)))
        {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(final Interval<?> other)
    {
        // compare the low boundary
        if (this.low < other.low || this.low == other.low && this.lowInclusive && (!other.lowInclusive))
        {
            return -1;
        }
        if (this.low > other.low || this.low == other.low && other.lowInclusive && (!this.lowInclusive))
        {
            return 1;
        }
        // low and lowInclusive are the same; compare the high boundary
        if (this.high < other.high || this.high == other.high && (!this.highInclusive) && other.highInclusive)
        {
            return -1;
        }
        if (this.high > other.high || this.high == other.high && this.highInclusive && (!other.highInclusive))
        {
            return 1;
        }
        // boundaries are exactly the same; compare the payload
        if (this.payload != null)
        {
            if (other.payload == null)
            {
                return -1;
            }
            return this.payload.compareTo((T) other.payload);
        }
        if (other.payload != null)
        {
            return 1;
        }
        return 0;
    }

    /**
     * Check if a value falls on this Interval.
     * @param x the value to check
     * @return true if <code>x</code> lies on this Interval
     */
    public boolean covers(final double x)
    {
        return ((this.low < x || (this.low == x && this.lowInclusive))
                && (this.high > x || (this.high == x && this.highInclusive)));
    }

    /**
     * Compute the intersection of this <code>Interval</code> and some other <code>Interval</code>. The other Interval need not
     * have the same type of payload.
     * @param other the other <code>Interval</code>
     * @return the intersection of the intervals (can be <code>null</code>). If not null, the payload is the payload of
     *         <code>this</code> interval
     */
    public Interval<T> intersection(final Interval<?> other)
    {
        if (this.disjunct(other))
        {
            return null;
        }
        if (other.covers(this))
        {
            return this;
        }
        if (this.covers(other))
        {
            return new Interval<T>(other.low, other.lowInclusive, other.high, other.highInclusive, this.payload);
        }
        boolean includeLow = this.low > other.low && this.lowInclusive || this.low < other.low && other.lowInclusive
                || this.low == other.low && this.lowInclusive && other.lowInclusive;
        boolean includeHigh = this.high < other.high && this.highInclusive || this.high > other.high && other.highInclusive
                || this.high == other.high && this.highInclusive && other.highInclusive;
        return new Interval<T>(Math.max(this.low, other.low), includeLow, Math.min(this.high, other.high), includeHigh,
                this.payload);
    }

    @Override
    public String toString()
    {
        return (this.lowInclusive ? "[" : "(") + this.low + ", " + this.high + (this.highInclusive ? "]" : ")") + "\u2192"
                + this.payload;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.high, this.highInclusive, this.low, this.lowInclusive, this.payload);
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
        Interval<?> other = (Interval<?>) obj;
        return Double.doubleToLongBits(this.high) == Double.doubleToLongBits(other.high)
                && this.highInclusive == other.highInclusive
                && Double.doubleToLongBits(this.low) == Double.doubleToLongBits(other.low)
                && this.lowInclusive == other.lowInclusive && Objects.equals(this.payload, other.payload);
    }

}
