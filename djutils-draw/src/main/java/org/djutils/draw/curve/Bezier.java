package org.djutils.draw.curve;

/**
 * Common code used to generated B&eacute;zier curves.
 * <p>
 * Copyright (c) 2013-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class Bezier
{
    /** T values of numerical approach of Legendre-Gauss to determine B&eacute;zier length. */
    static final double[] T =
            new double[] {-0.0640568928626056260850430826247450385909, 0.0640568928626056260850430826247450385909,
                    -0.1911188674736163091586398207570696318404, 0.1911188674736163091586398207570696318404,
                    -0.3150426796961633743867932913198102407864, 0.3150426796961633743867932913198102407864,
                    -0.4337935076260451384870842319133497124524, 0.4337935076260451384870842319133497124524,
                    -0.5454214713888395356583756172183723700107, 0.5454214713888395356583756172183723700107,
                    -0.6480936519369755692524957869107476266696, 0.6480936519369755692524957869107476266696,
                    -0.7401241915785543642438281030999784255232, 0.7401241915785543642438281030999784255232,
                    -0.8200019859739029219539498726697452080761, 0.8200019859739029219539498726697452080761,
                    -0.8864155270044010342131543419821967550873, 0.8864155270044010342131543419821967550873,
                    -0.9382745520027327585236490017087214496548, 0.9382745520027327585236490017087214496548,
                    -0.9747285559713094981983919930081690617411, 0.9747285559713094981983919930081690617411,
                    -0.9951872199970213601799974097007368118745, 0.9951872199970213601799974097007368118745};

    /** C values of numerical approach of Legendre-Gauss to determine B&eacute;zier length. */
    static final double[] C =
            new double[] {0.1279381953467521569740561652246953718517, 0.1279381953467521569740561652246953718517,
                    0.1258374563468282961213753825111836887264, 0.1258374563468282961213753825111836887264,
                    0.121670472927803391204463153476262425607, 0.121670472927803391204463153476262425607,
                    0.1155056680537256013533444839067835598622, 0.1155056680537256013533444839067835598622,
                    0.1074442701159656347825773424466062227946, 0.1074442701159656347825773424466062227946,
                    0.0976186521041138882698806644642471544279, 0.0976186521041138882698806644642471544279,
                    0.086190161531953275917185202983742667185, 0.086190161531953275917185202983742667185,
                    0.0733464814110803057340336152531165181193, 0.0733464814110803057340336152531165181193,
                    0.0592985849154367807463677585001085845412, 0.0592985849154367807463677585001085845412,
                    0.0442774388174198061686027482113382288593, 0.0442774388174198061686027482113382288593,
                    0.0285313886289336631813078159518782864491, 0.0285313886289336631813078159518782864491,
                    0.0123412297999871995468056670700372915759, 0.0123412297999871995468056670700372915759};

    /** The default number of points to use to construct a B&eacute;zier curve. */
    public static final int DEFAULT_BEZIER_SIZE = 64;

    /** Cached factorial values. */
    private static long[] fact = new long[] {1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L,
            479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L,
            121645100408832000L, 2432902008176640000L};

    /** Utility class. */
    private Bezier()
    {
        // do not instantiate
    }

    /**
     * Calculate the B&eacute;zier point of degree n, with B(t) = &Sigma;(i = 0..n) [C(n, i) * (1 - t)<sup>n-i</sup>
     * t<sup>i</sup> P<sub>i</sub>], where C(n, k) is the binomial coefficient defined by n! / ( k! (n-k)! ), ! being the
     * factorial operator.
     * @param t double; the fraction
     * @param p double...; the points of the curve, where the first and last are begin and end point, and all intermediate ones
     *            are control points
     * @return the B&eacute;zier value B(t) of degree n, where n is the number of points in the <code>p</code> array
     */
    @SuppressWarnings("checkstyle:methodname")
    static double Bn(final double t, final double... p)
    {
        if (p.length == 0)
        {
            return 0.0;
        }
        double b = 0.0;
        double m = (1.0 - t);
        int n = p.length - 1;
        double fn = factorial(n);
        for (int i = 0; i <= n; i++)
        {
            double c = fn / (factorial(i) * (factorial(n - i)));
            b += c * Math.pow(m, n - i) * Math.pow(t, i) * p[i];
        }
        return b;
    }

    /**
     * Calculate factorial(k), which is k * (k-1) * (k-2) * ... * 1. For factorials up to 20, a lookup table is used.
     * @param k int; the parameter
     * @return k!
     */
    private static double factorial(final int k)
    {
        if (k < fact.length)
        {
            return fact[k];
        }
        double f = 1;
        for (int i = 2; i <= k; i++)
        {
            f = f * i;
        }
        return f;
    }

    /**
     * Returns the derivative for one dimension of a B&eacute;zier, which is a B&eacute;zier of 1 order lower.
     * @param in double[]; coefficients of one dimension of a B&eacute;zier
     * @return double[]; coefficients of one dimension of the derivative B&eacute;zier
     */
    public static double[] derivative(final double[] in)
    {
        if (in.length == 0) // Derivative of a zero order B&eacute;zier is a zero order B&eacute;zier
        {
            return in; //  No need to create a new one
        }
        int n = in.length - 1;
        double[] result = new double[n];
        for (int i = 0; i < n; i++)
        {
            result[i] = n * (in[i + 1] - in[i]);
        }
        return result;
    }

}
