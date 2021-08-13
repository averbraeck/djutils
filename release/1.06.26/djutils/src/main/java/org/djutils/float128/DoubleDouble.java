package org.djutils.float128;

import java.io.Serializable;

/**
 * DoubleDouble stores a value as two double values, with a hi double value and a lo double value. The lo double value has a
 * value of around 1.0E13 lower magnitude than the hi double value. The value that is represented by the DoubleDouble has the
 * value hi+lo. DoubleDouble is immutable. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DoubleDouble extends Number implements Serializable, Comparable<DoubleDouble>
{
    /** the high part of the DoubleDouble value. */
    private final double hi;

    /** the low part of the DoubleDouble value. */
    private final double lo;

    /**
     * Construct a new DoubleDouble value consisting of a high (most significant) part and a low (least significant) part.
     * @param hi double; the high part of the DoubleDouble value
     * @param lo double; the low part of the DoubleDouble value
     */
    public DoubleDouble(final double hi, final double lo)
    {
        this.hi = hi;
        this.lo = lo;
    }

    /**
     * Construct a new DoubleDouble value using one double, where the lo part of the DoubleDouble will be 0.
     * @param value double; the high part of the DoubleDouble value
     */
    public DoubleDouble(final double value)
    {
        this(value, 0.0);
    }

    /**
     * Construct a DoubleDouble from a String value.
     * @param s String; the String value to be parsed 
     * @return a DoubleDouble value representing s with as many correct digits as possible
     */
    public static DoubleDouble valueOf(final String s)
    {
        int exp = 0;
        if (s.indexOf('e') >=0 || s.indexOf('E') >=0)
        {
            exp = Integer.valueOf(s.substring(0));
        }
        double hi = Double.valueOf(s);
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(final DoubleDouble o)
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public int intValue()
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public long longValue()
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public float floatValue()
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public double doubleValue()
    {
        return 0;
    }
}
