package org.djutils.float128;

/**
 * PerformanceTests.java. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class TestHypotAtan
{
    /**
     * Do not instantiate.
     */
    private TestHypotAtan()
    {
        // Do not instantiate
    }

    /**
     * Measure performance of atan2, hypot, sine and cosine.
     * @param args String[]; the command line arguments (not used)
     */
    public static void main(final String[] args)
    {
        // Ensure that all classes are loaded before measuring things
        Math.atan2(0.5, 1.5);
        Math.hypot(1.2, 3.4);
        Math.sin(0.8);
        Math.cos(0.6);
        Math.sqrt(2.3);

        int iterations = 100000000;

        long startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            @SuppressWarnings("unused")
            double x = 0.1 + i / 1000.0;
            @SuppressWarnings("unused")
            double y = -0.5 + i / 2000.0;
        }
        long nowNanos = System.nanoTime();
        long baseNanos = nowNanos - startNanos;
        System.out.println(String.format("base loop: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                baseNanos / 1000000000.0, 1.0 * baseNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.atan2(y, x);
        }
        nowNanos = System.nanoTime();
        long durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            TestHypotAtan.atan2(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("PerformanceTests.atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            TestHypotAtan.fastAtan(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("PerformanceTests.fastAtan: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.hypot(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("hypot: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            TestHypotAtan.hypotA(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("PerformanceTests.hypotA: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            TestHypotAtan.hypotB(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("PerformanceTests.hypotB: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            TestHypotAtan.hypotC(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("PerformanceTests.hypotC: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000000.0;
            Math.sin(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("  sin: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000000.0;
            Math.cos(x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("  cos: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.atan2(y, x);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("atan2: %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));

        startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++)
        {
            double x = 0.1 + i / 1000.0;
            double y = -0.5 + i / 2000.0;
            Math.sqrt(x * x + y * y);
        }
        nowNanos = System.nanoTime();
        durationNanos = nowNanos - startNanos - baseNanos;
        System.out.println(String.format("sqrt(x*x+y*y): %d invocations in %.6f s (%.1f ns/invocation)", iterations,
                durationNanos / 1000000000.0, 1.0 * durationNanos / iterations));
    }

    /** 2^450. */
    private static final double TWO_POW_450 = Double.longBitsToDouble(0x5C10000000000000L);

    /** 2^-450. */
    private static final double TWO_POW_N450 = Double.longBitsToDouble(0x23D0000000000000L);

    /** 2^750? */
    private static final double TWO_POW_750 = Double.longBitsToDouble(0x6ED0000000000000L);

    /** 2^-750? */
    private static final double TWO_POW_N750 = Double.longBitsToDouble(0x1110000000000000L);

    /**
     * Implementation of hypot taken from https://stackoverflow.com/questions/3764978/why-hypot-function-is-so-slow.
     * @param x double; x
     * @param y double; y
     * @return double; the hypot of x, and y
     */
    public static double hypotA(double x, double y)
    {
        x = Math.abs(x);
        y = Math.abs(y);
        if (y < x)
        {
            double a = x;
            x = y;
            y = a;
        }
        else if (!(y >= x))
        { // Testing if we have some NaN.
            if ((x == Double.POSITIVE_INFINITY) || (y == Double.POSITIVE_INFINITY))
            {
                return Double.POSITIVE_INFINITY;
            }
            else
            {
                return Double.NaN;
            }
        }
        if (y - x == y)
        { // x too small to substract from y
            return y;
        }
        else
        {
            double factor;
            if (x > TWO_POW_450)
            { // 2^450 < x < y
                x *= TWO_POW_N750;
                y *= TWO_POW_N750;
                factor = TWO_POW_750;
            }
            else if (y < TWO_POW_N450)
            { // x < y < 2^-450
                x *= TWO_POW_750;
                y *= TWO_POW_750;
                factor = TWO_POW_N750;
            }
            else
            {
                factor = 1.0;
            }
            return factor * Math.sqrt(x * x + y * y);
        }
    }

    /**
     * <b>hypot</b>.
     * @param x double; x
     * @param y double; y
     * @return sqrt(x*x +y*y) without intermediate overflow or underflow.
     * @Note {@link Math#hypot} is unnecessarily slow. This returns the identical result to Math.hypot with reasonable run times
     *       (~40 nsec vs. 800 nsec).
     *       <p>
     *       The logic for computing z is copied from "Freely Distributable Math Library" fdlibm's e_hypot.c. This minimizes
     *       rounding error to provide 1 ulb accuracy.
     */
    public static double hypotB(double x, double y)
    {
        if (Double.isInfinite(x) || Double.isInfinite(y))
        {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(x) || Double.isNaN(y))
        {
            return Double.NaN;
        }

        x = Math.abs(x);
        y = Math.abs(y);

        if (x < y)
        {
            double d = x;
            x = y;
            y = d;
        }

        int xi = Math.getExponent(x);
        int yi = Math.getExponent(y);

        if (xi > yi + 27)
        {
            return x;
        }

        int bias = 0;
        if (xi > 510 || xi < -511)
        {
            bias = xi;
            x = Math.scalb(x, -bias);
            y = Math.scalb(y, -bias);
        }

        // translated from "Freely Distributable Math Library" e_hypot.c to minimize rounding errors
        double z = 0;
        if (x > 2 * y)
        {
            double x1 = Double.longBitsToDouble(Double.doubleToLongBits(x) & 0xffffffff00000000L);
            double x2 = x - x1;
            z = Math.sqrt(x1 * x1 + (y * y + x2 * (x + x1)));
        }
        else
        {
            double t = 2 * x;
            double t1 = Double.longBitsToDouble(Double.doubleToLongBits(t) & 0xffffffff00000000L);
            double t2 = t - t1;
            double y1 = Double.longBitsToDouble(Double.doubleToLongBits(y) & 0xffffffff00000000L);
            double y2 = y - y1;
            double xMinusY = x - y;
            z = Math.sqrt(t1 * y1 + (xMinusY * xMinusY + (t1 * y2 + t2 * y))); // Note: 2*x*y <= x*x + y*y
        }

        if (bias == 0)
        {
            return z;
        }
        else
        {
            return Math.scalb(z, bias);
        }
    }

    /**
     * <b>hypot</b>. C.F. Borges, An Improved Algorithm for hypot(a, b). arXiv:1904.09481v6 [math.NA] 14 Jun 2019.
     * @param x double; x
     * @param y double; y
     * @return sqrt(x*x +y*y) without intermediate overflow or underflow.
     */
    public static double hypotC(final double x, final double y)
    {
        if (Double.isInfinite(x) || Double.isInfinite(y))
        {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(x) || Double.isNaN(y))
        {
            return Double.NaN;
        }

        // scaling if necessary to avoid overflow of x*x or y*y

        double h = Math.sqrt(Math.fma(x, x, y * y));
        double hsq = h * h;
        double xsq = x * x;
        double a = Math.fma(-y, y, hsq - xsq) + Math.fma(h, h, -hsq) - Math.fma(x, x, -xsq);
        return h - a / (2.0 * h);
    }

    /* @(#)e_atan2.c 1.3 95/01/18 */
    /*-
     * ====================================================
     * Copyright (C) 1993 by Sun Microsystems, Inc. All rights reserved.
     *
     * Developed at SunSoft, a Sun Microsystems, Inc. business.
     * Permission to use, copy, modify, and distribute this
     * software is freely granted, provided that this notice 
     * is preserved.
     * ====================================================
     *
     */

    /*- __ieee754_atan2(y,x)
     * Method :
     *  1. Reduce y to positive by atan2(y,x)=-atan2(-y,x).
     *  2. Reduce x to positive by (if x and y are unexceptional): 
     *      ARG (x+iy) = arctan(y/x)       ... if x > 0,
     *      ARG (x+iy) = pi - arctan[y/(-x)]   ... if x < 0,
     *
     * Special cases:
     *
     *  ATAN2((anything), NaN ) is NaN;
     *  ATAN2(NAN , (anything) ) is NaN;
     *  ATAN2(+-0, +(anything but NaN)) is +-0  ;
     *  ATAN2(+-0, -(anything but NaN)) is +-pi ;
     *  ATAN2(+-(anything but 0 and NaN), 0) is +-pi/2;
     *  ATAN2(+-(anything but INF and NaN), +INF) is +-0 ;
     *  ATAN2(+-(anything but INF and NaN), -INF) is +-pi;
     *  ATAN2(+-INF,+INF ) is +-pi/4 ;
     *  ATAN2(+-INF,-INF ) is +-3pi/4;
     *  ATAN2(+-INF, (anything but,0,NaN, and INF)) is +-pi/2;
     *
     * Constants:
     * The hexadecimal values are the intended ones for the following 
     * constants. The decimal values may be used, provided that the 
     * compiler will convert from decimal to binary accurately enough 
     * to produce the hexadecimal values shown.
     */

    /** */
    private static double tiny = 1.0e-300;

    /** */
    private static double zero = 0.0;

    /** */
    private static double pi_o_4 = 7.8539816339744827900E-01; /* 0x3FE921FB, 0x54442D18 */

    /** */
    private static double pi_o_2 = 1.5707963267948965580E+00; /* 0x3FF921FB, 0x54442D18 */

    /** */
    private static double pi = 3.1415926535897931160E+00; /* 0x400921FB, 0x54442D18 */

    /** */
    private static double pi_lo = 1.2246467991473531772E-16; /* 0x3CA1A626, 0x33145C07 */

    /**
     * atan.c implementation of Sun in fdlibm: http://www.netlib.org/fdlibm/.
     * @param y double
     * @param x double
     * @return atan2(y, x)
     */
    @SuppressWarnings("checkstyle:needbraces")
    private static double atan2(double y, double x)
    {
        double z;
        int k, m, hx, hy, ix, iy;
        long lx, ly;

        hx = __HI(x);
        ix = hx & 0x7fffffff;
        lx = __LO(x);
        hy = __HI(y);
        iy = hy & 0x7fffffff;
        ly = __LO(y);
        if (((ix | ((lx | -lx) >> 31)) > 0x7ff00000) || ((iy | ((ly | -ly) >> 31)) > 0x7ff00000)) /* x or y is NaN */
            return x + y;
        if ((hx - 0x3ff00000 | lx) == 0)
            return atan(y); /* x=1.0 */
        m = ((hy >> 31) & 1) | ((hx >> 30) & 2); /* 2*sign(x)+sign(y) */

        /* when y = 0 */
        if ((iy | ly) == 0)
        {
            switch (m)
            {
                case 0:
                case 1:
                    return y; /* atan(+-0,+anything)=+-0 */
                case 2:
                    return pi + tiny; /* atan(+0,-anything) = pi */
                case 3:
                    return -pi - tiny; /* atan(-0,-anything) =-pi */
            }
        }
        /* when x = 0 */
        if ((ix | lx) == 0)
            return (hy < 0) ? -pi_o_2 - tiny : pi_o_2 + tiny;

        /* when x is INF */
        if (ix == 0x7ff00000)
        {
            if (iy == 0x7ff00000)
            {
                switch (m)
                {
                    case 0:
                        return pi_o_4 + tiny; /* atan(+INF,+INF) */
                    case 1:
                        return -pi_o_4 - tiny; /* atan(-INF,+INF) */
                    case 2:
                        return 3.0 * pi_o_4 + tiny; /* atan(+INF,-INF) */
                    case 3:
                        return -3.0 * pi_o_4 - tiny; /* atan(-INF,-INF) */
                }
            }
            else
            {
                switch (m)
                {
                    case 0:
                        return zero; /* atan(+...,+INF) */
                    case 1:
                        return -zero; /* atan(-...,+INF) */
                    case 2:
                        return pi + tiny; /* atan(+...,-INF) */
                    case 3:
                        return -pi - tiny; /* atan(-...,-INF) */
                }
            }
        }
        /* when y is INF */
        if (iy == 0x7ff00000)
            return (hy < 0) ? -pi_o_2 - tiny : pi_o_2 + tiny;

        /* compute y/x */
        k = (iy - ix) >> 20;
        if (k > 60)
            z = pi_o_2 + 0.5 * pi_lo; /* |y/x| > 2**60 */
        else if (hx < 0 && k < -60)
            z = 0.0; /* |y|/x < -2**60 */
        else
            z = atan(Math.abs(y / x)); /* safe to do y/x */
        switch (m)
        {
            case 0:
                return z; /* atan(+,+) */
            case 1:
                // __HI(z) ^= 0x80000000;
                z = __HI(z, __HI(z) ^ 0x80000000);
                return z; /* atan(-,+) */
            case 2:
                return pi - (z - pi_lo); /* atan(+,-) */
            default: /* case 3 */
                return (z - pi_lo) - pi; /* atan(-,-) */
        }
    }

    /*- @(#)s_atan.c 1.3 95/01/18 */
    /*
     * ==================================================== Copyright (C) 1993 by Sun Microsystems, Inc. All rights reserved.
     * Developed at SunSoft, a Sun Microsystems, Inc. business. Permission to use, copy, modify, and distribute this software is
     * freely granted, provided that this notice is preserved. ====================================================
     */

    /*- atan(x)
     * Method
     *   1. Reduce x to positive by atan(x) = -atan(-x).
     *   2. According to the integer k=4t+0.25 chopped, t=x, the argument
     *      is further reduced to one of the following intervals and the
     *      arctangent of t is evaluated by the corresponding formula:
     *
     *      [0,7/16]      atan(x) = t-t^3*(a1+t^2*(a2+...(a10+t^2*a11)...)
     *      [7/16,11/16]  atan(x) = atan(1/2) + atan( (t-0.5)/(1+t/2) )
     *      [11/16.19/16] atan(x) = atan( 1 ) + atan( (t-1)/(1+t) )
     *      [19/16,39/16] atan(x) = atan(3/2) + atan( (t-1.5)/(1+1.5t) )
     *      [39/16,INF]   atan(x) = atan(INF) + atan( -1/t )
     *
     * Constants:
     * The hexadecimal values are the intended ones for the following 
     * constants. The decimal values may be used, provided that the 
     * compiler will convert from decimal to binary accurately enough 
     * to produce the hexadecimal values shown.
     */

    /** */
    private static double[] atanhi = {4.63647609000806093515e-01, /* atan(0.5)hi 0x3FDDAC67, 0x0561BB4F */
            7.85398163397448278999e-01, /* atan(1.0)hi 0x3FE921FB, 0x54442D18 */
            9.82793723247329054082e-01, /* atan(1.5)hi 0x3FEF730B, 0xD281F69B */
            1.57079632679489655800e+00, /* atan(inf)hi 0x3FF921FB, 0x54442D18 */
    };

    /** */
    private static double[] atanlo = {2.26987774529616870924e-17, /* atan(0.5)lo 0x3C7A2B7F, 0x222F65E2 */
            3.06161699786838301793e-17, /* atan(1.0)lo 0x3C81A626, 0x33145C07 */
            1.39033110312309984516e-17, /* atan(1.5)lo 0x3C700788, 0x7AF0CBBD */
            6.12323399573676603587e-17, /* atan(inf)lo 0x3C91A626, 0x33145C07 */
    };

    /** */
    private static double[] aT = {3.33333333333329318027e-01, /* 0x3FD55555, 0x5555550D */
            -1.99999999998764832476e-01, /* 0xBFC99999, 0x9998EBC4 */
            1.42857142725034663711e-01, /* 0x3FC24924, 0x920083FF */
            -1.11111104054623557880e-01, /* 0xBFBC71C6, 0xFE231671 */
            9.09088713343650656196e-02, /* 0x3FB745CD, 0xC54C206E */
            -7.69187620504482999495e-02, /* 0xBFB3B0F2, 0xAF749A6D */
            6.66107313738753120669e-02, /* 0x3FB10D66, 0xA0D03D51 */
            -5.83357013379057348645e-02, /* 0xBFADDE2D, 0x52DEFD9A */
            4.97687799461593236017e-02, /* 0x3FA97B4B, 0x24760DEB */
            -3.65315727442169155270e-02, /* 0xBFA2B444, 0x2C6A6C2F */
            1.62858201153657823623e-02, /* 0x3F90AD3A, 0xE322DA11 */
    };

    /** */
    private static double one = 1.0;

    /** */
    private static double huge = 1.0e300;

    /**
     * atan from http://www.netlib.org/fdlibm/.
     * @param x double
     * @return atan(x)
     */
    @SuppressWarnings("checkstyle:needbraces")
    static double atan(double x)
    {
        double w, s1, s2, z;
        int ix, hx, id;

        hx = __HI(x);
        ix = hx & 0x7fffffff;
        if (ix >= 0x44100000)
        { /* if |x| >= 2^66 */
            if (ix > 0x7ff00000 || (ix == 0x7ff00000 && (__LO(x) != 0)))
                return x + x; /* NaN */
            if (hx > 0)
                return atanhi[3] + atanlo[3];
            else
                return -atanhi[3] - atanlo[3];
        }
        if (ix < 0x3fdc0000)
        { /* |x| < 0.4375 */
            if (ix < 0x3e200000)
            { /* |x| < 2^-29 */
                if (huge + x > one)
                    return x; /* raise inexact */
            }
            id = -1;
        }
        else
        {
            x = Math.abs(x);
            if (ix < 0x3ff30000)
            { /* |x| < 1.1875 */
                if (ix < 0x3fe60000)
                { /* 7/16 <=|x|<11/16 */
                    id = 0;
                    x = (2.0 * x - one) / (2.0 + x);
                }
                else
                { /* 11/16<=|x|< 19/16 */
                    id = 1;
                    x = (x - one) / (x + one);
                }
            }
            else
            {
                if (ix < 0x40038000)
                { /* |x| < 2.4375 */
                    id = 2;
                    x = (x - 1.5) / (one + 1.5 * x);
                }
                else
                { /* 2.4375 <= |x| < 2^66 */
                    id = 3;
                    x = -1.0 / x;
                }
            }
        }
        /* end of argument reduction */
        z = x * x;
        w = z * z;
        /* break sum from i=0 to 10 aT[i]z**(i+1) into odd and even poly */
        s1 = z * (aT[0] + w * (aT[2] + w * (aT[4] + w * (aT[6] + w * (aT[8] + w * aT[10])))));
        s2 = w * (aT[1] + w * (aT[3] + w * (aT[5] + w * (aT[7] + w * aT[9]))));
        if (id < 0)
            return x - x * (s1 + s2);
        else
        {
            z = atanhi[id] - ((x * (s1 + s2) - atanlo[id]) - x);
            return (hx < 0) ? -z : z;
        }
    }

    /**
     * Return the low-order 32 bits of the double argument as an int.
     * @param x x
     * @return __LO
     */
    private static int __LO(double x)
    {
        long transducer = Double.doubleToRawLongBits(x);
        return (int) transducer;
    }

    /**
     * Return a double with its low-order bits of the second argument and the high-order bits of the first argument..
     * @param x x
     * @param low lo
     * @return __LO
     */
    private static double __LO(double x, int low)
    {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0xFFFF_FFFF_0000_0000L) | (low & 0x0000_0000_FFFF_FFFFL));
    }

    /**
     * Return the high-order 32 bits of the double argument as an int.
     * @param x x
     * @return __HI
     */
    private static int __HI(double x)
    {
        long transducer = Double.doubleToRawLongBits(x);
        return (int) (transducer >> 32);
    }

    /**
     * Return a double with its high-order bits of the second argument and the low-order bits of the first argument..
     * @param x x
     * @param high hi
     * @return __HI
     */
    private static double __HI(double x, int high)
    {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0x0000_0000_FFFF_FFFFL) | (((long) high)) << 32);
    }

    /**
     * @param x param
     * @return atan(x)
     */
    static double fastAtan(double x)
    {
        double u = 1.3654703620217001424e-2;
        u = u * x + -1.0046251337641932974e-1;
        u = u * x + 2.7313356251360220792e-1;
        u = u * x + -3.2614626783181052668e-1;
        u = u * x + 7.5150001258356942267e-2;
        u = u * x + 1.8054329923285069577e-1;
        u = u * x + 3.1227633392937778104e-3;
        u = u * x + -3.3362526818403318876e-1;
        u = u * x + 1.3994641595913163446e-5;
        u = u * x + 9.9999973830765721236e-1;
        return u * x + 8.0891638103222688273e-10;
    }

}
