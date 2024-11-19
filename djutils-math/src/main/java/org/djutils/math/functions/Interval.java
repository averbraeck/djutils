package org.djutils.math.functions;

import java.util.Objects;

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
record Interval<T>(double low, boolean lowInclusive, double high, boolean highInclusive, T payload)
        implements Comparable<Interval<?>>
{
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
        if (this.high == other.low && (!this.highInclusive) || (!other.lowInclusive))
        {
            return true;
        }
        if (this.low == other.high && !(this.lowInclusive) || (!other.highInclusive))
        {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(final Interval<?> other)
    {
        if (this.low < other.low || this.low == other.low && this.lowInclusive && (!other.lowInclusive))
        {
            return -1;
        }
        if (this.low > other.low || this.low == other.low && other.lowInclusive && (!this.lowInclusive))
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

    @Override
    public String toString()
    {
        return "Interval " + (this.lowInclusive ? "[" : ")") + this.low + ", " + this.high
                + (this.highInclusive ? "]" : ")" + ", payload=" + this.payload + "]");
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
