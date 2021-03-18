package org.djutils.complex;

import static org.junit.Assert.assertEquals;

import org.djutils.base.AngleUtil;
import org.junit.Test;

/**
 * TestComplexMath.java. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TestComplexMath
{

    /**
     * Test the square root function.
     */
    @Test
    public void testSqrt()
    {
        // Start by testing the bloody obvious
        assertEquals("square root of -1 is I", Complex.I, ComplexMath.sqrt(new Complex(-1)));
        Complex in = new Complex(0, -4);
        Complex c = ComplexMath.sqrt(in);
        assertEquals("square root of " + in + " norm", 2, c.norm(), 0.000001);
        assertEquals("square root of " + in + " phi", -Math.PI / 4, c.phi(), 0.000001);
        double[] values = new double[] { 0, 1, 0.01, 100, Math.PI, -Math.E };
        for (double re : values)
        {
            for (double im : values)
            {
                in = new Complex(re, im);
                c = ComplexMath.sqrt(in);
                assertEquals("square root of " + in + " norm", Math.sqrt(in.norm()), c.norm(), 0.0001);
                assertEquals("square root of " + in + " phi", in.phi() / 2, c.phi(), 0.0000001);
                Complex c2 = c.times(c);
                assertEquals("square of square root re", in.re, c2.re, 0.0001);
                assertEquals("square of square root im", in.im, c2.im, 0.0001);
            }
        }
    }

    /**
     * Test the cube root function.
     */
    @Test
    public void testCbrt()
    {
        double[] values = new double[] { 0, 1, 0.01, 100, Math.PI, -Math.E };
        for (double re : values)
        {
            for (double im : values)
            {
                Complex in = new Complex(re, im);
                Complex c = ComplexMath.cbrt(in);
                assertEquals("cube root of " + in + " norm", Math.cbrt(in.norm()), c.norm(), 0.0001);
                assertEquals("cube root of " + in + " phi", in.phi() / 3, c.phi(), 0.0000001);
                Complex c3 = c.times(c).times(c);
                assertEquals("cube of cube root re", in.re, c3.re, 0.0001);
                assertEquals("cube of cube root im", in.im, c3.im, 0.0001);
            }
        }
    }

    /**
     * Test the exponential function.
     */
    @Test
    public void testExp()
    {
        assertEquals("exp of 1 is e; re", Math.E, ComplexMath.exp(Complex.ONE).re, 0.000001);
        assertEquals("exp of 1 is e; im", 0, ComplexMath.exp(Complex.ONE).im, 0.000001);
        for (double re : new double[] { 0, 1, Math.PI, -Math.E, 10, -10 })
        {
            for (double im : new double[] { 0, 0.1, Math.PI / 2, Math.PI, 5, -1, -Math.E, -50 })
            {
                Complex in = new Complex(re, im);
                Complex out = ComplexMath.exp(in);
                assertEquals("exp(" + in + ") re", Math.exp(re) * Math.cos(im), out.re, 0.01);
                assertEquals("exp(" + in + ") im", Math.exp(re) * Math.sin(im), out.im, 0.01);
            }
        }
    }

    /**
     * Test the natural logarithm function.
     */
    @Test
    public void testLog()
    {
        assertEquals("ln(ONE) is ZERO", Complex.ZERO, ComplexMath.ln(Complex.ONE));
        Complex in = new Complex(Math.E);
        Complex out = ComplexMath.ln(in);
        assertEquals("ln(e) is ONE re", 1, out.re, 0.00000001);
        assertEquals("ln(e) is ONE im", 0, out.im, 0.00000001);
        for (double re : new double[] { 0, 1, Math.PI, 10, -Math.E, -10 })
        {
            for (double im : new double[] { 0, 0.1, Math.PI / 2, Math.PI, 5, -1, -Math.E, -50 })
            {
                in = new Complex(re, im);
                out = ComplexMath.ln(in);
                assertEquals("ln(" + in + ") re", Math.log(in.norm()), out.re, 0.01);
                assertEquals("ln(" + in + ") im", Math.atan2(im, re), out.im, 0.00001);
            }
        }
    }

    /**
     * Test the sine, cosine and tangent functions.
     */
    @Test
    public void testSinCosTan()
    {
        assertEquals("sin(ZERO) is ZERO", Complex.ZERO, ComplexMath.sin(Complex.ZERO));
        Complex c = ComplexMath.cos(Complex.ZERO);
        assertEquals("cos(ZERO) is ONE: re", 1, c.re, 0.00001);
        assertEquals("cos(ZERO) is ONE: im", 0, c.im, 0.00001);
        assertEquals("tan(ZERO) is ZERO", Complex.ZERO, ComplexMath.tan(Complex.ZERO));
        double[] values = new double[] { 0, 1, Math.PI, 10, -Math.E, -10 };
        for (double re : values)
        {
            for (double im : values)
            {
                Complex in = new Complex(re, im);
                Complex sin = ComplexMath.sin(in);
                assertEquals("sin(" + in + ") re", Math.sin(re) * Math.cosh(im), sin.re, 0.0001);
                assertEquals("sin(" + in + ") im", Math.cos(re) * Math.sinh(im), sin.im, 0.0001);
                Complex cos = ComplexMath.cos(in);
                assertEquals("cos(" + in + ") re", Math.cos(re) * Math.cosh(im), cos.re, 0.0001);
                assertEquals("cos(" + in + ") im", -Math.sin(re) * Math.sinh(im), cos.im, 0.0001);
                Complex tan = ComplexMath.tan(in);
                Complex div = sin.divideBy(cos);
                assertEquals("div norm", sin.norm() / cos.norm(), div.norm(), 0.00001);
                assertEquals("div phi", AngleUtil.normalizeAroundZero(sin.phi() - cos.phi()), div.phi(), 0.0001);
                assertEquals("tan(" + in + ") re", div.re, tan.re, 0.0001);
                assertEquals("tan(" + in + ") im", div.im, tan.im, 0.0001);
                Complex sin2plusCos2 = sin.times(sin).plus(cos.times(cos));
                assertEquals("sin^2 + cos^2 re", 1, sin2plusCos2.re, 0.00001);
                assertEquals("sin^2 + cos^2 im", 0, sin2plusCos2.im, 0.00001);
            }
        }
    }

    /**
     * Test the sinh, cosh, tanh functions.
     */
    @Test
    public void testSinhCoshTanH()
    {
        double[] values = new double[] { 0, 1, Math.PI, 10, -Math.E, -10 };
        for (double re : values)
        {
            for (double im : values)
            {
                Complex in = new Complex(re, im);
                Complex sinh = ComplexMath.sinh(in);
                Complex cosh = ComplexMath.cosh(in);
                Complex tanh = ComplexMath.tanh(in);

                // System.out.println(" in=" + printComplex(in) + "\ntanh=" + printComplex(tanh));
                assertEquals("sinh re", Math.sinh(re) * Math.cos(im), sinh.re, 0.0001);
                assertEquals("sinh im", Math.cosh(re) * Math.sin(im), sinh.im, 0.0001);
                assertEquals("cosh re", Math.cosh(re) * Math.cos(im), cosh.re, 0.0001);
                assertEquals("cosh im", Math.sinh(re) * Math.sin(im), cosh.im, 0.0001);
                assertEquals("tanh re", Math.sinh(2 * re) / (Math.cosh(2 * re) + Math.cos(2 * im)), tanh.re, 0.0001);
                assertEquals("tanh im", Math.sin(2 * im) / (Math.cosh(2 * re) + Math.cos(2 * im)), tanh.im, 0.0001);
                // Alternate way to compute tanh
                Complex alternateTanh = sinh.divideBy(cosh);
                assertEquals("alternate tanh re", tanh.re, alternateTanh.re, 0.0001);
                assertEquals("alternate tanh im", tanh.im, alternateTanh.im, 0.0001);
                if (im == 0)
                {
                    // Extra checks
                    assertEquals("sinh of real re", Math.sinh(re), sinh.re, 0.0001);
                    assertEquals("sinh of real im", 0, sinh.im, 0.0001);
                    assertEquals("cosh of real re", Math.cosh(re), cosh.re, 0.0001);
                    assertEquals("cosh of real im", 0, cosh.im, 0.0001);
                    assertEquals("tanh of real re", Math.tanh(re), tanh.re, 0.0001);
                    assertEquals("tahh of real im", 0, tanh.im, 0.0001);
                }
            }
        }
    }

    /**
     * Test the asin, acos and atan functions.
     */
    @Test
    public void testAsinAcosAtan()
    {
        double[] values = new double[] { 0, 0.2, 0, 8, 1, -1, -0.2, -0.8, Math.PI, 10, -Math.E, -10 };
        for (double re : values)
        {
            for (double im : values)
            {
                Complex in = new Complex(re, im);
                Complex asin = ComplexMath.asin(in);
                Complex acos = ComplexMath.acos(in);
                Complex atan = ComplexMath.atan(in);
                // This is a lousy test; we only verify that asin(sin(asin(z)) roughly equals asin(z)
                Complex asinOfSinOfAsin = ComplexMath.asin(ComplexMath.sin(asin));
                assertEquals("asin re", asinOfSinOfAsin.re, asin.re, 0.0001);
                assertEquals("asin im", asinOfSinOfAsin.im, asin.im, 0.0001);
                Complex acosOfCosOfAcos = ComplexMath.acos(ComplexMath.cos(acos));
                assertEquals("acos re", acosOfCosOfAcos.re, acos.re, 0.0001);
                assertEquals("acos im", acosOfCosOfAcos.im, acos.im, 0.0001);
                Complex atanOfTanOfAtan = ComplexMath.atan(ComplexMath.tan(atan));
                if (Math.abs(atan.re) < 100)
                {
                    assertEquals("atan re", atanOfTanOfAtan.re, atan.re, 0.0001);
                    assertEquals("atan im", atanOfTanOfAtan.im, atan.im, 0.0001);
                }
                if (im == 0 && re >= -1 && re <= 1)
                {
                    // Extra checks
                    assertEquals("asin of real in range -1, 1 re", Math.asin(re), asin.re, 0.00001);
                    assertEquals("asin of real in range -1, 1 im", 0, asin.im, 0.00001);
                    assertEquals("acos of real in range -1, 1 re", Math.acos(re), acos.re, 0.00001);
                    assertEquals("acos of real in range -1, 1 im", 0, acos.im, 0.00001);
                }
                else if (im == 0)
                {
                    assertEquals("atan of real re", Math.atan(re), atan.re, 0.00001);
                    assertEquals("atan of real, 1 im", 0, atan.im, 0.00001);

                }
            }
        }
    }

    /**
     * Test the asinh function.
     */
    @Test
    public void testAsinh()
    {
        double[] values = new double[] { 0, 0.2, 0, 8, 1, -1, -0.2, -0.8, Math.PI, 10, -Math.E, -10 };
        for (double re : values)
        {
            for (double im : values)
            {
                Complex in = new Complex(re, im);
                Complex asinh = ComplexMath.asinh(in);
                Complex acosh = ComplexMath.acosh(in);
                Complex atanh = ComplexMath.atanh(in);
                // This is a lousy test; we only verify that asinh(sinh(asinh(z)) roughly equals asinh(z)
                Complex asinhOfSinhOfAsinh = ComplexMath.asinh(ComplexMath.sinh(asinh));
                assertEquals("asinh re", asinhOfSinhOfAsinh.re, asinh.re, 0.0001);
                assertEquals("asinh im", asinhOfSinhOfAsinh.im, asinh.im, 0.0001);
                Complex acoshOfCoshOfAcosh = ComplexMath.acosh(ComplexMath.cosh(acosh));
                if (im != 0 || re > 1.0)
                {
                    // acosh is unstable around im == 0 && re <= 1.0; see <a
                    // href="https://mathworld.wolfram.com/InverseHyperbolicCosine.html">Wolfram mathWorld: Inverse Hyperbolic
                    // Cosine<//a> so we can't use this test there.
                    assertEquals("acosh re", acoshOfCoshOfAcosh.re, acosh.re, 0.0001);
                    assertEquals("acosh im", acoshOfCoshOfAcosh.im, acosh.im, 0.0001);
                }
                Complex atanhOfTanhOfAtanh = ComplexMath.atanh(ComplexMath.tanh(atanh));
                if (im != 0 || re > -1.0 && re < 1.0)
                {
                    // atanh is unstable around im == 0 && re <= -1 && re >= 1; see <a
                    // "https://mathworld.wolfram.com/InverseHyperbolicTangent.html">Wolfram mathWorld: Inverse Hyperbolic
                    // Tangent</a>, so we can't use this test there.
                    // System.out.println("   in=" + printComplex(in) + "\natanh=" + printComplex(atanh));
                    if (im != 1 && im != -1 || re != 0)
                    {
                        // Also unstable around i and minus i as the atan function is unstable around -1
                        assertEquals("atanh re", atanhOfTanhOfAtanh.re, atanh.re, 0.0001);
                        assertEquals("atanh im", atanhOfTanhOfAtanh.im, atanh.im, 0.0001);
                    }
                }

                if (im == 0)
                {
                    // Extra checks
                    assertEquals("asinh of real re", doubleAsinh(re), asinh.re, 0.00001);
                    assertEquals("asinh of real im", 0, asinh.im, 0.00001);
                    if (re >= 1.0)
                    {
                        assertEquals("acosh of real re", doubleAcosh(re), acosh.re, 0.00001);
                        assertEquals("acosh of real im", 0, acosh.im, 0.00001);
                    }
                    if (re > -1.0 && re < 1.0)
                    {
                        assertEquals("atanh of real re", (Math.log(1 + re) - Math.log(1 - re)) / 2, atanh.re, 0.00001);
                        assertEquals("acosh of real im", 0, atanh.im, 0.00001);
                    }
                }
            }
        }
    }

    /**
     * Copied from < href="https://forgetcode.com/java/1746-asinh-return-the-hyperbolic-sine-of-value-as-a-argument">Forget Code
     * asinh</a>.
     * @param x double; the argument
     * @return double; the inverse hyperbolic cosine of x
     */
    public static double doubleAsinh(final double x)
    {
        return Math.log(x + Math.sqrt(x * x + 1.0));
    }

    /**
     * Copied from < href="https://forgetcode.com/Java/1747-acosh-Return-the-hyperbolic-Cosine-of-value-as-a-Argument">Forget
     * Code acosh</a>.
     * @param x double; the argument
     * @return double; the inverse hyperbolic cosine of x
     */
    public static double doubleAcosh(final double x)
    {
        return Math.log(x + Math.sqrt(x * x - 1.0));
    }

    /**
     * @param c Complex;
     * @return String
     */
    public static String printComplex(final Complex c)
    {
        return String.format("re=%10.6f, im=%10.6f, norm=%10.6f, phi=%10.6f(=%10.6f\u00b0)", c.re, c.im, c.norm(), c.phi(),
                Math.toDegrees(c.phi()));
    }

}
