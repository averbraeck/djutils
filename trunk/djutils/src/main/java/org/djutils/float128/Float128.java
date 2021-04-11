package org.djutils.float128;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Float128 stores immutable floating point values, with a 16 bits signed exponent, 120 bits fraction, and one sign bit. It has
 * arithmetic for addition, subtraction, multiplication and division, as well as several Math operators such as signum and abs.
 * The fraction follows the implementation of the IEEE-754 standard, which means that the initial '1' is not stored in the
 * fraction.<br>
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

    /** fraction = 120 bits; hi 60 bits (most significant) part. */
    private long fractionHi;

    /** fraction = 120 bits; lo 60 bits (least significant) part. */
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

    /** ZERO bit in position 0. */
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
     * @param fractionHi long; fraction = 120 bits; hi (most significant) 60 bits. Bit 60 is 1 to represent the initial '1' in
     *            the fraction before the decimal point. That makes addition, subtraction, ans shifting the value a lot easier
     * @param fractionLo long; fraction = 120 bits; lo (least significant) 60 bits
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
     * Create a Float128 based on a double. The IEEE-754 double is built up as follows:
     * <ul>
     * <li>bit 63 [0x8000_0000_0000_0000L]: sign bit(1-bit)</li>
     * <li>bits 62-52 [0x7ff0_0000_0000_0000L]: exponent (11-bit), stored as a the 2-exponent value + 1022.</li>
     * <li>- exponent 000 and fraction == 0: signed zero</li>
     * <li>- exponent 000 and fraction != 0: underflow</li>
     * <li>- exponent 111 and fraction == 0: infinity</li>
     * <li>- exponent 111 and fraction != 0: NaN</li>
     * <li>bits 51-0 [0x000f_ffff_ffff_ffffL]: fraction (52-bit)
     * </ul>
     * @param d double; the double to store
     */
    public Float128(final double d)
    {
        this.sign = d >= 0 ? B0 : SIGN;
        long dl = Double.doubleToRawLongBits(d);
        this.fractionHi = (dl << 8) & 0x0FFF_FFFF_FFFF_FFFFL | 0x1000_0000_0000_0000L;
        this.fractionLo = 0L;
        int exp = (int) (dl >>> 52) & 0x7FF;
        if (exp == 0)
        {
            // signed zero (if F == 0) and underflow (if F != 0)
            this.sign |= this.fractionHi == 0L ? ZERO : UNDERFLOW;
        }
        else if (exp == 0x7FF)
        {
            // infinity (if F==0) and NaN (if F != 0)
            this.sign |= (dl & 0x000F_FFFF_FFFF_FFFFL) == 0L ? INF : NAN;
        }
        else
        {
            // regular exponent. note that IEEE-754 exponents are stored in a shifted manner
            this.exponent = exp - 1023;
        }
    }

    /**
     * Add a Float128 value to this value. Addition works as follows: suppose you add 10 and 100 (decimal).<br>
     * v1 = 10 = 0x(1)01000000p3 and v2 = 0x(1)100100000p6. These are the numbers behind the initial (1) before the decimal
     * point that is part of the Float128 in bit 60.<br>
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
        long carry = 0L;
        if ((ret[1] & 0x1000_0000_0000_0000L) != 0)
        {
            carry = 1L;
            ret[1] &= 0x0FFF_FFFF_FFFF_FFFFL;
        }
        ret[0] = tf[0] + vf[0] + carry;
        if ((ret[0] & 0x2000_0000_0000_0000L) != 0)
        {
            shift(ret, 1);
            exp += 1;
        }
        return new Float128(this.sign, ret[0], ret[1], exp);
    }

    /**
     * Shift the bits to the right for the variable v.
     * @param v long[]; the variable stored as two longs
     * @param bits int; the number of bits to shift 'down'. bits HAS to be &gt;= 0.
     */
    protected void shift(final long[] v, final int bits)
    {
        v[1] >>>= bits;
        long carry = (v[0] & MASK[bits]) << (60 - bits);
        v[1] |= carry;
        v[0] >>>= bits;
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

    /**
     * Return binary representation of l, all 64 bits.
     * @param l long; the value
     * @return the binary string representation with 64 bits
     */
    private static String printLong(final long l)
    {
        String s = String.format("%64s", Long.toUnsignedString(l, 2)).replaceAll(" ", "0");
        s = s.substring(0, 1) + " " + s.substring(1, 12) + " (1.)" + s.substring(12);
        return s;
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
        if ((this.sign & UNDERFLOW) != 0 || this.exponent < -1022)
        {
            return 0.0; // underflow
        }
        long dl = (this.sign & SIGN) == 0 ? 0L : 0x8000_0000_0000_0000L;
        dl |= (this.fractionHi >>> 8) & 0x000F_FFFF_FFFF_FFFFL;
        dl |= (this.exponent + 1023L) << 52;
        return Double.longBitsToDouble(dl);
    }

    /**
     * Return whether the stored value is a signed zero.
     * @return boolean; whether the stored value is signed zero
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

    /**
     * Return whether the stored value is positive.
     * @return boolean; whether the stored value is positive
     */
    public boolean isPositive()
    {
        return (this.sign & SIGN) == 0;
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
    public String toPaddedBinaryString()
    {
        if (isZero())
        {
            return isPositive() ? "0x0.p0" : "-0x0.p0";
        }
        if (isNaN())
        {
            return "NaN";
        }
        if (isInfinite())
        {
            return isPositive() ? "+INF" : "-INF";
        }

        // create the String representation of the fraction
        StringBuffer s = new StringBuffer();
        if ((this.sign & SIGN) != 0)
        {
            s.append('-');
        }
        s.append("0x1.");
        for (int i = 59; i >= 0; i--)
        {
            s.append((this.fractionHi & (1L << i)) == 0 ? '0' : '1');
        }
        for (int i = 59; i >= 0; i--)
        {
            s.append((this.fractionLo & (1L << i)) == 0 ? '0' : '1');
        }
        s.append("p");
        s.append(this.exponent);
        return s.toString();
    }

    /**
     * Return the binary string representation of this Float128 value.
     * @return String; the binary string representation of this Float128 value
     */
    public String toBinaryString()
    {
        if (isZero())
        {
            return isPositive() ? "0x0.p0" : "-0x0.p0";
        }
        if (isNaN())
        {
            return "NaN";
        }
        if (isInfinite())
        {
            return isPositive() ? "+INF" : "-INF";
        }

        // create the String representation of the fraction
        StringBuffer s = new StringBuffer();
        if ((this.sign & SIGN) != 0)
        {
            s.append('-');
        }
        s.append("0x1.");
        for (int i = 59; i > 0; i--)
        {
            s.append((this.fractionHi & (1L << i)) == 0 ? '0' : '1');
        }
        if (this.fractionLo != 0)
        {
            for (int i = 59; i > 0; i--)
            {
                s.append((this.fractionLo & (1L << i)) == 0 ? '0' : '1');
            }
        }
        while (s.charAt(s.length() - 1) == '0')
        {
            s.deleteCharAt(s.length() - 1);
        }

        s.append("p");
        s.append(this.exponent);
        return s.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.toBinaryString();
    }

    /** */
    private static byte[][] DOUBLE_DECIMAL_TABLE;

    /** */
    private static int[] DOUBLE_DECIMAL_EXP;

    /** */
    private static final BigDecimal DEC_TWO = new BigDecimal("2");

    /**
     * Calculate double decimal table. The table runs from -1024..1023 for the exponent and every set of decimal digits is 20
     * digits long. The DOUBLE_DECIMAL_EXP table contains the exponent for the 1-digit based number. So 1.0xp0 is coded in row
     * 1024 with a value of 1, which means 1.000000000 and an exponent of 0. In theory table could be stored in nibbles, but
     * bytes is a lot more convenient.
     */
    private static void calcDoubleDecimalTable()
    {
        DOUBLE_DECIMAL_TABLE = new byte[2048][];
        DOUBLE_DECIMAL_EXP = new int[2048];
        BigInteger big = BigInteger.ONE;
        for (int i = 0; i < 1024; i++)
        {
            DOUBLE_DECIMAL_TABLE[i + 1024] = new byte[20];
            String bigStr = big.toString();
            DOUBLE_DECIMAL_EXP[i + 1024] = bigStr.length() - 1;
            for (int j = 0; j < 20 && j < bigStr.length(); j++)
            {
                DOUBLE_DECIMAL_TABLE[i + 1024][j] = (byte) (bigStr.charAt(j) - '0');
            }
            big = big.multiply(BigInteger.TWO);
        }
        BigDecimal dec = BigDecimal.ONE;
        int exp = -1;
        for (int i = 1; i <= 1024; i++)
        {
            DOUBLE_DECIMAL_TABLE[1024 - i] = new byte[20];
            dec = dec.divide(DEC_TWO, 50, RoundingMode.HALF_UP);
            String bigStr = dec.toString();
            if (bigStr.startsWith("0.0"))
            {
                dec = dec.multiply(BigDecimal.TEN);
                bigStr = dec.toString();
                exp = exp - 1;
            }
            DOUBLE_DECIMAL_EXP[1024 - i] = exp;
            for (int j = 0; j < 20 && j < bigStr.length() - 2; j++)
            {
                DOUBLE_DECIMAL_TABLE[1024 - i][j] = (byte) (bigStr.charAt(j + 2) - '0');
            }
        }
    }

    /**
     * A test for a toString() method for a double.
     * @param d double; the value
     * @return String; the decimal 17-digit scientific notation String representation of the double
     */
    public static String doubleTotring(final double d)
    {
        if (DOUBLE_DECIMAL_TABLE == null)
        {
            calcDoubleDecimalTable();
        }
        long dl = Double.doubleToRawLongBits(d);
        long fraction = dl & 0x000FFFFFFFFFFFFFL;
        int exp = (int) ((dl & 0x7FF0000000000000L) >>> 52);
        if (exp == 0)
        {
            // signed zero (if F == 0) and underflow (if F != 0)
            return fraction == 0L ? "0.0" : "UNDERFLOW";
        }
        else if (exp == 0x7FF)
        {
            // infinity (if F==0) and NaN (if F != 0)
            return fraction == 0L ? "Infinity" : "NaN";
        }
        else
        {
            // regular exponent. note that IEEE-754 exponents are stored in a shifted manner
            exp -= 1022;
        }

        // fill and add
        int[] digits = new int[20];
        int startDecExp = DOUBLE_DECIMAL_EXP[exp + 1024];
        for (int i = 0; i < 52; i++)
        {
            byte[] p = DOUBLE_DECIMAL_TABLE[i + exp + 1024];
            int shift = DOUBLE_DECIMAL_EXP[i + exp + 1024] - startDecExp;
            for (int j = shift; j < 20; j++)
            {
                if ((dl & (1 << (52 - i))) != 0)
                {
                    digits[j] += p[j - shift];
                }
            }
        }

        // normalize
        for (int i = 19; i > 0; --i)
        {
            int v = digits[i] % 10;
            int c = digits[i] / 10;
            digits[i - 1] += c;
            digits[i] = v;
        }

        StringBuffer s = new StringBuffer();
        for (int i = 0; i < 20; i++)
        {
            s.append(digits[i]);
        }
        return s.toString();
    }

    /**
     * test code.
     * @param args String[] not used
     */
    public static void main(final String[] args)
    {
        double mv = Math.PI; // .MIN_VALUE;
        long lmv = Double.doubleToRawLongBits(mv);
        System.out.println(printLong(lmv));
        String s = "0x1.";
        for (int i = 1; i <= 120; i++)
        {
            s += (char) ('0' + (i % 10));
        }
        System.out.println(s);

        Float128 fmv = new Float128(mv);
        System.out.println(fmv.toPaddedBinaryString());
        System.out.println(printLong(Double.doubleToRawLongBits(fmv.doubleValue())));

        Float128 pi = new Float128(3.141592653589).plus(7.932384626433E-12); //.plus(8.3279502884197E-25);
        System.out.println(
                "0x1.1001001000011111101101010100010001000010110100011000010001101001100010011000110011000101000101110000000110111p1");
        System.out.println(pi.toPaddedBinaryString());
    }
}
