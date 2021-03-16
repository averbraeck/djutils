package org.djutils.complex;

/**
 * ComplexMath.java. Math with complex operands and results.
 * <p>
 * TODO: major rewrite; possibly based on https://people.freebsd.org/~stephen/catrig.c <br>
 * Also consider netbsd c-sources of libm/complex: http://cvsweb.netbsd.org/bsdweb.cgi/src/lib/libm/complex/
 * <br>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class ComplexMath
{

    /**
     * Do not instantiate.
     */
    private ComplexMath()
    {
        // Do not instantiate.
    }

    /**
     * Principal square root of a Complex operand. The principal square root of a complex number has a non-negative real
     * component.
     * @param z Complex; the operand
     * @return Complex; the principal square root of the operand
     */
    public static Complex sqrt(final Complex z)
    {
        double norm = z.norm();
        return new Complex(Math.sqrt((z.re + norm) / 2), (z.im >= 0 ? 1 : -1) * Math.sqrt((-z.re + norm) / 2));
    }

    /**
     * Exponential function of Complex operand.
     * @param z Complex; the operand
     * @return Complex; the result of the exponential function applied to the operand
     */
    public static Complex exp(final Complex z)
    {
        double factor = Math.exp(z.re);
        return new Complex(factor * Math.cos(z.im), factor * Math.sin(z.im));
    }

    /**
     * Principal value of the natural logarithm of Complex operand. See
     * <a href="https://en.wikipedia.org/wiki/Complex_logarithm">Wikipedia Complex logarithm</a>.
     * @param z Complex; the operand
     * @return Complex; the principal value of the natural logarithm of the Complex operand
     */
    public static Complex ln(final Complex z)
    {
        return new Complex(Math.log(z.norm()), z.phi());
    }

    /**
     * Sine function of Complex operand. See <a href="https://proofwiki.org/wiki/Sine_of_Complex_Number">ProofWiki Sine of
     * Complex Number</a>.
     * @param z Complex; the operand
     * @return Complex; the result of the sine function applied to the operand
     */
    public static Complex sin(final Complex z)
    {
        return new Complex(Math.sin(z.re) * Math.cosh(z.im), Math.cos(z.re) * Math.sinh(z.im));
    }

    /**
     * Cosine function of Complex operand. See <a href="https://proofwiki.org/wiki/Cosine_of_Complex_Number">ProofWiki Cosine of
     * Complex Number</a>.
     * @param z Complex; the operand
     * @return Complex; the result of the cosine function applied to the operand
     */
    public static Complex cos(final Complex z)
    {
        return new Complex(Math.cos(z.re) * Math.cosh(z.im), -Math.sin(z.re) * Math.sinh(z.im));
    }

    /**
     * Tangent function of Complex operand. See <a href="https://proofwiki.org/wiki/Tangent_of_Complex_Number">ProofWiki Tangent
     * of Complex Number</a>.
     * @param z Complex; the operand
     * @return Complex; the result of the tangent function applied to the operand
     */
    public static Complex tan(final Complex z)
    {
        // Using Formulation 4 of the reference as it appears to need the fewest trigonometric operations
        double divisor = Math.cos(2 * z.re) + Math.cosh(2 * z.im);
        return new Complex(Math.sin(2 * z.re) / divisor, Math.sinh(2 * z.im) / divisor);
    }

}
