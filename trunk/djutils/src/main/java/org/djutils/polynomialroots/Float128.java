package org.djutils.polynomialroots;

import java.math.BigInteger;

/**
 * Float128 stores immutable floating point values, with a 16 bits signed exponent, 125 bits fraction, and one sign bit. It has
 * arithmetic for addition, subtraction, multiplication and division, as well as several Math operators such as signum and abs.
 * The fraction follows the implementation of the IEEE-754 standard, which means that the initial '1' is not stored in the
 * fraction.<br>
 * XXX: Code is in development.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Float128 extends Number
{
    /** */
    private static final long serialVersionUID = 20210320L;

    /**
     * sign, infinity, NaN byte; sign in bit 0 where 1 means negative, NaN in bit 1, Infinity in bit 2, underflow in bit 3,
     * signed zero in bit 4.
     */
    private byte sign;

    /** exponent = 16 bits, stored as a regular int. */
    private int exponent;

    /** fraction = 125 bits; hi (most significant) part. */
    private long fractionHi;

    /** fraction = 125 bits; lo (least significant) part. */
    private long fractionLo;

    /** byte constant 0. */
    private static final byte B0 = (byte) 0x00;

    /** SIGN bit in position 0. */
    private static final byte SIGN = (byte) 0x01;

    /** NAN bit in position 0. */
    private static final byte NAN = (byte) 0x02;

    /** INF bit in position 0. */
    private static final byte INF = (byte) 0x04;

    /** UNDERFLOW bit in position 0. */
    private static final byte UNDERFLOW = (byte) 0x08;

    /** UNDERFLOW bit in position 0. */
    private static final byte ZERO = (byte) 0x10;

    /** MASK[n] contains n '1' bits from the right side and '0' in the other positions. */
    private static final long[] MASK = new long[64];

    /** BIG_LO[n] contains 2^n as a BigInteger. */
    private static final BigInteger[] BIG_LO = new BigInteger[62];

    /** BIG_HI[n] contains 2^(n+62) as a BigInteger. */
    private static final BigInteger[] BIG_HI = new BigInteger[62];

    /** Fill the MASK bits and the BigInteger constants for hi and lo. */
    static
    {
        MASK[0] = 0;
        for (int i = 1; i < 64; i++)
        {
            MASK[i] = (MASK[i - 1] << 1) | 1L;
        }

        final BigInteger bigTWO = new BigInteger("2");
        BIG_LO[0] = new BigInteger("1");
        for (int n = 1; n < 62; n++)
        {
            BIG_LO[n] = BIG_LO[n - 1].multiply(bigTWO);
        }

        BIG_HI[0] = BIG_LO[61];
        for (int n = 1; n < 62; n++)
        {
            BIG_HI[n] = BIG_HI[n - 1].multiply(bigTWO);
        }
    }

    /**
     * Create a Float128 by specifying all the fields.
     * @param sign byte; sign, infinity, NaN byte; sign in bit 0, NaN in bit 1, Infinity in bit 2, underflow in bit 3, signed
     *            zero in bit 4.
     * @param fractionHi long; fraction = 125 bits; hi (most significant) part
     * @param fractionLo long; fraction = 125 bits; lo (least significant) part
     * @param exponent int; 16 bits, stored as a regular int
     */
    private Float128(final byte sign, final long fractionHi, final long fractionLo, final int exponent)
    {
        this.sign = sign;
        this.fractionHi = fractionHi;
        this.fractionLo = fractionLo;
        this.exponent = exponent;
    }

    /**
     * Create a Float128 based on a double.
     * @param d double; the double to store
     */
    public Float128(final double d)
    {
        this.sign = d >= 0 ? B0 : SIGN;
        long dl = Double.doubleToRawLongBits(d);
        this.fractionHi = dl & 0x000FFFFFFFFFFFFFL;
        this.fractionLo = 0L;
        int exp = (int) ((dl & 0x7FF0000000000000L) >>> 52);
        if (exp == 0)
        {
            // signed zero (if F == 0) and underflow (if F != 0)
            this.sign |= this.fractionHi == 0L ? ZERO : UNDERFLOW;
        }
        else if (exp == 0x7FF)
        {
            // infinity (if F==0) and NaN (if F != 0)
            this.sign |= this.fractionHi == 0L ? INF : NAN;
        }
        else
        {
            // regular exponent. note that IEEE-754 exponents are stored in a shifted manner
            this.exponent = exp - 1022;
        }
    }

    /**
     * Add a Float128 value to this value. Addition works as follows: suppose you add 10 and 100 (decimal).<br>
     * v1 = 10 = 0x(1)01000000p3 and v2 = 0x(1)100100000p6. These are the numbers behind the initial '1'.<br>
     * Shift the lowest value (including the leading 1) 3 bits to the right, and add:
     * 
     * <pre>
     * 0x(0)0010100000p6
     * 0x(1)1001000000p6
     * -----------------+
     * 0x(1)1011100000p6
     * </pre>
     * 
     * The last number indeed represents the value 110.
     * @param value Float128; the value to add
     * @return Float128; the sum of this Float128 and the given value
     */
    public Float128 plus(final Float128 value)
    {
        // shift the fraction of the lowest exponent in the direction of the highest exponent
        int expDelta = this.exponent - value.exponent;
        int exp = Math.max(this.exponent, value.exponent);
        long[] tf = {this.fractionHi, this.fractionLo};
        long[] vf = {value.fractionHi, value.fractionLo};
        if (expDelta > 0)
        {
            // this.exponent > value.exponent; shift value.fraction 'up'
            shift(vf, expDelta);
        }
        else if (expDelta < 0)
        {
            // value.exponent > this.exponent; shift this.fraction 'up'
            shift(tf, -expDelta);
        }
        long[] ret = new long[2];
        ret[1] = tf[1] + vf[1];
        long carry = (ret[1] & 0x4000000000000000L) == 0 ? 0L : 1L;
        ret[0] = tf[0] + vf[0] + carry;
        if ((ret[0] & 0x4000000000000000L) != 0)
        {
            shift(ret, 1);
            exp += 1;
        }
        return new Float128(this.sign, ret[0], ret[1], exp);
    }

    /**
     * Shift the bits to the right for the variable v.
     * @param v long[]; the variable stored as two longs
     * @param bits int; the number of bits to shift 'down'
     */
    private void shift(final long[] v, final int bits)
    {
        v[1] = v[1] >>> bits;
        long carry = (v[0] & MASK[bits]) << (62 - bits);
        v[1] |= carry;
        v[0] = ((v[0] >>> 1) | 0x2000000000000000L) >>> (bits - 1);
    }

    /**
     * Add a double value to this value.
     * @param value double; the value to add
     * @return Float128; the sum of this Float128 and the given value
     */
    public Float128 plus(final double value)
    {
        return plus(new Float128(value));
    }

    /** {@inheritDoc} */
    @Override
    public int intValue()
    {
        return (int) doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public long longValue()
    {
        return (long) doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public float floatValue()
    {
        return (float) doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public double doubleValue()
    {
        if ((this.sign & NAN) != 0)
        {
            return Double.NaN;
        }
        if (this.exponent > 1023 || (this.sign & INF) != 0)
        {
            return ((this.sign & SIGN) == 0) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        if (this.exponent < -1022)
        {
            return 0.0; // underflow
        }
        long dl = (this.sign & SIGN) == 0 ? 0L : 0x8000000000000000L;
        dl |= this.fractionHi & 0x000FFFFFFFFFFFFFL;
        dl |= (this.exponent + 1022L) << 52;
        return Double.longBitsToDouble(dl);
    }

    /**
     * Return whether the stored value is zero.
     * @return boolean; whether the stored value is zero
     */
    public boolean isZero()
    {
        return (this.sign & ZERO) != 0;
    }

    /**
     * Return whether the stored value is NaN.
     * @return boolean; whether the stored value is NaN
     */
    public boolean isNaN()
    {
        return (this.sign & NAN) != 0;
    }

    /**
     * Return whether the stored value is infinite.
     * @return boolean; whether the stored value is infinite
     */
    public boolean isInfinite()
    {
        return (this.sign & INF) != 0;
    }

    /**
     * Return whether the stored value is finite.
     * @return boolean; whether the stored value is finite
     */
    public boolean isFinite()
    {
        return (this.sign & INF) == 0;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.exponent;
        result = prime * result + (int) (this.fractionHi ^ (this.fractionHi >>> 32));
        result = prime * result + (int) (this.fractionLo ^ (this.fractionLo >>> 32));
        result = prime * result + this.sign;
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Float128 other = (Float128) obj;
        if (this.exponent != other.exponent)
            return false;
        if (this.fractionHi != other.fractionHi)
            return false;
        if (this.fractionLo != other.fractionLo)
            return false;
        if (this.sign != other.sign)
            return false;
        return true;
    }

    /**
     * Return the binary string representation of this Float128 value.
     * @return String; the binary string representation of this Float128 value
     */
    public String toBinaryString()
    {
        // create the String representation of the fraction
        StringBuffer s = new StringBuffer();
        s.append(isZero() ? "0x0." : "0x1.");
        for (int i = 62; i >= 0; --i)
        {
            s.append((this.fractionHi & (1 << i)) == 0 ? '0' : '1');
        }
        for (int i = 62; i >= 0; --i)
        {
            s.append((this.fractionLo & (1 << i)) == 0 ? '0' : '1');
        }
        if ((this.sign & SIGN) != 0)
        {
            s.insert(0, '-');
        }
        s.append("p");
        s.append(this.exponent);
        return s.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        // create the String representation of the fraction
        StringBuffer s = new StringBuffer();
        for (int i = 62; i >= 0; --i)
        {
            s.append((this.fractionHi & (1 << i)) == 0 ? '0' : '1');
        }
        for (int i = 62; i >= 0; --i)
        {
            s.append((this.fractionLo & (1 << i)) == 0 ? '0' : '1');
        }
        if ((this.sign & SIGN) != 0)
        {
            s.insert(0, '-');
        }
        BigInteger big = new BigInteger(s.toString(), 2);
        String ret = isZero() ? "0." : "1.";
        return ret + big.toString() + "p" + this.exponent;
    }

    /**
     * test code.
     * @param args String[] not used
     */
    public static void main(final String[] args)
    {
        System.out.println(new Float128(100.0).doubleValue());
        System.out.println(new Float128(100.0).toBinaryString());
        System.out.println(new Float128(100.0).toString());
        Float128 ff = new Float128(100.0).plus(10.0);
        System.out.println(ff.doubleValue());
    }
}
