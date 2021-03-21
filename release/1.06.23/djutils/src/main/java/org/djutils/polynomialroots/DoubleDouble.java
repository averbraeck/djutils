package org.djutils.polynomialroots;

/**
 * DoubleDouble is a class with exactly twice the precision of a double in terms of the fraction part. Internally, a
 * DoubleDouble is stored as two doubles, where the lowest significant double is 2^52 times smaller than the most significant
 * double. <br>
 * XXX: Code is in development.
 * <br>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DoubleDouble extends Number
{
    /** */
    private static final long serialVersionUID = 20210320L;

    /** the high valued double (most significant). */
    private final double dHi;
    
    /** the low valued double (least significant), with an exponent that is 2^52 smaller than dHi. */
    private final double dLo;
    
    /**
     * Construct a DoubleDouble on the basis of one double.
     * @param d double; a double number from which to construct the DoubleDouble
     */
    public DoubleDouble(final double d)
    {
        this.dHi = d;
        this.dLo = 0;
    }
    
    /**
     * Add a DoubleDouble to this value.
     * @param dd DoubleDOuble; the value to add
     * @return DoubleDouble; the sum of this DoubleDouble and the given value
     */
    public DoubleDouble plus(final DoubleDouble dd)
    {
        double retHi = this.dHi + dd.dHi;
        return new DoubleDouble(retHi);
    }
    
    /**
     * Add a double to this value.
     * @param d double; the value to add
     * @return DoubleDouble; the sum of this DoubleDouble and the given value
     */
    public DoubleDouble plus(final double d)
    {
        return plus(new DoubleDouble(d));
    }
    
    /** {@inheritDoc} */
    @Override
    public int intValue()
    {
        return (int) this.dHi;
    }

    /** {@inheritDoc} */
    @Override
    public long longValue()
    {
        return (long) this.dHi;
    }

    /** {@inheritDoc} */
    @Override
    public float floatValue()
    {
        return (float) this.dHi;
    }

    /** {@inheritDoc} */
    @Override
    public double doubleValue()
    {
        return this.dHi;
    }
    
}
