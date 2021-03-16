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
                assertEquals("square of square root im", in.re, c2.re, 0.0001);
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
                assertEquals("cube of cube root im", in.re, c3.re, 0.0001);
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
     * @param c Complex;
     * @return String
     */
    public static String printComplex(final Complex c)
    {
        return String.format("re=%10.6f, im=%10.6f, norm=%10.6f, phi=%10.6f(=%10.6f\u00b0)", c.re, c.im, c.norm(), c.phi(),
                Math.toDegrees(c.phi()));
    }

}
