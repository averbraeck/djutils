package org.djutils.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.djutils.base.AngleUtil;
import org.junit.Test;

/**
 * TestComplex.java. <br>
 * <br>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TestComplex
{

    /**
     * Test the various constructors of Complex.
     */
    @Test
    public void testConstructors()
    {
        double[] testValues = new double[] { 0, 1, 100, -1, -10000, Math.PI };

        for (double re : testValues)
        {
            for (double im : testValues)
            {
                Complex complex = new Complex(re, im);
                assertEquals("re", re, complex.re, 0.0001);
                assertEquals("im", im, complex.im, 0.0001);
                assertEquals("getRe", re, complex.getRe(), 0.0001);
                assertEquals("getIm", im, complex.getIm(), 0.0001);
                assertEquals("norm", Math.hypot(re, im), complex.norm(), 0.0001);
                if (re != 0 || im != 0)
                {
                    assertEquals("phi", Math.atan2(im, re), complex.phi(), 0.000001);
                }
                if (im == 0)
                {
                    assertTrue("If imaginary part is 0; complex is pure real", complex.isReal());
                    complex = new Complex(re);
                    assertEquals("re", re, complex.re, 0.0001);
                    assertEquals("im", im, complex.im, 0.0001);
                    assertEquals("norm", Math.hypot(re, im), complex.norm(), 0.0001);
                    if (re != 0)
                    {
                        assertEquals("phi", Math.atan2(im, re), complex.phi(), 0.000001);
                    }
                }
                else
                {
                    assertFalse("If imaginary part is not null; complex is not pure real", complex.isReal());
                }
                if (re == 0)
                {
                    assertTrue("If real part is 0; complex is imaginary", complex.isImaginary());
                }
                else
                {
                    assertFalse("If real part is not 0; comples is not imaginary", complex.isImaginary());
                }
                Complex conjugate = complex.conjugate(); // Loss less operation; we can test for exact equality
                assertEquals("Conjugate re", complex.re, conjugate.re, 0);
                assertEquals("Conjugate im", -complex.im, conjugate.im, 0);
            }
        }
    }

    /**
     * Test the constants defined by the Complex class.
     */
    @Test
    public void testConstants()
    {
        assertEquals("real component of ZERO", 0, Complex.ZERO.re, 0);
        assertEquals("imaginary component of ZERO", 0, Complex.ZERO.im, 0);
        assertEquals("real component of ONE", 1, Complex.ONE.re, 0);
        assertEquals("imaginary component of ONE", 0, Complex.ONE.im, 0);
        assertEquals("real component of MINUS_ONE", -1, Complex.MINUS_ONE.re, 0);
        assertEquals("imaginary component of MINUS_ONE", 0, Complex.MINUS_ONE.im, 0);
        assertEquals("real component of I", 0, Complex.I.re, 0);
        assertEquals("imaginary component of I", 1, Complex.I.im, 0);
        assertEquals("real component of MINUS_I", 0, Complex.MINUS_I.re, 0);
        assertEquals("imaginary component of MINUS_I", -1, Complex.MINUS_I.im, 0);
    }

    /**
     * Test the methods.
     */
    @Test
    public void testOperations()
    {
        Complex a = new Complex(12, -34);
        Complex b = new Complex(-23, 45);
        Complex c = a.plus(b);
        assertEquals("sum re", a.re + b.re, c.re, 0.00001);
        assertEquals("sum im", a.im + b.im, c.im, 0.00001);
        c = a.plus(123);
        assertEquals("sum re", a.re + 123, c.re, 0.00001);
        assertEquals("sum im", a.im, c.im, 0.00001);
        c = a.minus(b);
        assertEquals("difference re", a.re - b.re, c.re, 0.00001);
        assertEquals("difference im", a.im - b.im, c.im, 0.00001);
        c = a.minus(123);
        assertEquals("difference re", a.re - 123, c.re, 0.00001);
        assertEquals("difference im", a.im, c.im, 0.00001);
        c = a.times(b);
        assertEquals("product norm", a.norm() * b.norm(), c.norm(), 0.0001);
        assertEquals("product phi", a.phi() + b.phi(), c.phi(), 0.000001);
        c = a.times(123);
        assertEquals("product norm", a.norm() * 123, c.norm(), 0.0001);
        assertEquals("product phi", a.phi(), c.phi(), 0.000001);
        c = a.reciprocal();
        assertEquals("norm of reciprocal", a.norm(), 1 / c.norm(), 0.00001);
        assertEquals("phi of reciprocal", -a.phi(), c.phi(), 0.000001);
        c = a.times(c);
        assertEquals("a * a.reciprocal re", 1, c.re, 0.00001);
        assertEquals("a * a.reciprocal im", 0, c.im, 0.00001);
        for (double angle : new double[] {0, 0.1, 1, Math.E, Math.PI, 5, 10, -1, -5})
        {
            c = a.rotate(angle);
            assertEquals("rotated a norm", a.norm(), c.norm(), 0.00001);
            assertEquals("rotation difference", AngleUtil.normalizeAroundZero(a.phi() + angle), c.phi(), 0.000001);
        }
        c = a.divideBy(b);
        assertEquals("norm of division", a.norm() / b.norm(), c.norm(), 0.0000001);
        assertEquals("phi of division", AngleUtil.normalizeAroundZero(a.phi() - b.phi()), c.phi(), 0.000001);
        c = a.divideBy(123);
        assertEquals("dividend re", a.re / 123, c.re, 0.0000001);
        assertEquals("dividend im", a.im / 123, c.im, 0.0000001);
        c = Complex.ZERO.divideBy(Complex.ZERO);
        assertTrue("ZERO / ZERO re is NaN", Double.isNaN(c.re));
        assertTrue("ZERO / ZERO im is NaN", Double.isNaN(c.im));
        c = Complex.ONE.divideBy(Complex.ZERO);
        assertTrue("ONE / ZERO re is positive Infinity", Double.isInfinite(c.re) && c.re > 0);
        assertTrue("ONE / ZERO im is NaN", Double.isNaN(c.im));
        c = Complex.ZERO.minus(Complex.ONE).divideBy(Complex.ZERO);
        assertTrue("minus ONE / ZERO re is negative Infinity", Double.isInfinite(c.re) && c.re < 0);
        assertTrue("minus ONE / ZERO im is NaN", Double.isNaN(c.im));
        c = Complex.I.divideBy(Complex.ZERO);
        assertTrue("I / ZERO re is NaN", Double.isNaN(c.re));
        assertTrue("I / ZERO im is positive Infinity", Double.isInfinite(c.im) && c.im > 0);
        c = Complex.ZERO.minus(Complex.I).divideBy(Complex.ZERO);
        assertTrue("minus I / ZERO re is NaN", Double.isNaN(c.re));
        assertTrue("minus I / ZERO im is positive Infinity", Double.isInfinite(c.im) && c.im < 0);
        c = Complex.ZERO.reciprocal();
        assertTrue("reciprocal of ZERO re is positive Infinity ", Double.isInfinite(c.re) && c.re > 0);
        assertTrue("reciprocal of ZERO im is positive Infinity ", Double.isInfinite(c.im) && c.im > 0);
        c = Complex.ONE.divideBy(Complex.I);
        assertEquals("ONE / I re is 0", 0, c.re, 0);
        assertEquals("ONE / I im is -1", -1, c.im, 0);
        c = Complex.ZERO.divideBy(0.0);
        assertTrue("ZERO / 0.0 re is NaN", Double.isNaN(c.re));
        assertTrue("ZERO / 0.0 im is NaN", Double.isNaN(c.im));
        c = Complex.ONE.divideBy(0.0);
        assertTrue("ONE / 0.0 re is positive Infinity", Double.isInfinite(c.re) && c.re > 0);
        assertTrue("ONE / 0.0 im is NaN", Double.isNaN(c.im));
        c = Complex.ZERO.minus(Complex.ONE).divideBy(0.0);
        assertTrue("minus ONE / 0.0 re is negative Infinity", Double.isInfinite(c.re) && c.re < 0);
        assertTrue("minus ONE / 0.0 im is NaN", Double.isNaN(c.im));
        c = Complex.I.divideBy(0.0);
        assertTrue("I / 0.0 re is NaN", Double.isNaN(c.re));
        assertTrue("I / 0.0 im is positive Infinity", Double.isInfinite(c.im) && c.im > 0);
        c = Complex.ZERO.minus(Complex.I).divideBy(0.0);
        assertTrue("minus I / 0.0 re is NaN", Double.isNaN(c.re));
        assertTrue("minus I / 0.0 im is positive Infinity", Double.isInfinite(c.im) && c.im < 0);
    }

    /**
     * Test other methods.
     */
    @SuppressWarnings({ "unlikely-arg-type" })
    @Test
    public void testOthers()
    {
        Complex a = new Complex(12, 34);
        assertTrue("toString returns something descriptive", a.toString().startsWith("Complex "));
        assertNotEquals("hashCode takes re into account", a.hashCode(), a.plus(Complex.ONE).hashCode());
        assertNotEquals("hashCode takes im into account", a.hashCode(), a.plus(Complex.I).hashCode());
        assertTrue(a.equals(a));
        assertFalse(a.equals(null));
        assertFalse(a.equals("This is a String; not a complex"));
        assertFalse(a.equals(new Complex(12, 35)));
        assertFalse(a.equals(new Complex(13, 34)));
        assertEquals(a, new Complex(12, 34));
    }

}
