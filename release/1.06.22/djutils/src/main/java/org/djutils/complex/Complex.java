package org.djutils.complex;

/**
 * Complex.java. Immutable complex numbers. This implementation stores the real and imaginary component as a double value. These
 * fields are directly accessible, or through a getter. There are also getters for the norm and phi, but this are a CPU
 * intensive.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Complex
{
    /** Real component. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double re;

    /** Imaginary component. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double im;

    /** The zero value in the complex number space. */
    public static final Complex ZERO = new Complex(0, 0);

    /** The (real) one value in the complex number space. */
    public static final Complex ONE = new Complex(1, 0);

    /** The imaginary unit value (i). */
    public static final Complex I = new Complex(0, 1);

    /**
     * Construct a new complex number.
     * @param re double; real component of the new complex number
     * @param im double; imaginary component of the new complex number
     */
    public Complex(final double re, final double im)
    {
        this.re = re;
        this.im = im;
    }

    /**
     * Construct a new complex number with specified real component and zero imaginary component.
     * @param re double; the real component of the new complex number
     */
    public Complex(final double re)
    {
        this.re = re;
        this.im = 0;
    }

    /**
     * Retrieve the real component of this complex number.
     * @return double; the real component of this complex number
     */
    public double getRe()
    {
        return this.re;
    }

    /**
     * Retrieve the imaginary component of this complex number.
     * @return double; the imaginary component of this complex number
     */
    public double getIm()
    {
        return this.im;
    }

    /**
     * Compute and return the norm, or radius of this complex number.
     * @return double; the norm, or radius of this complex number
     */
    public double norm()
    {
        return Math.hypot(this.re, this.im);
    }

    /**
     * Compute and return the phase or phi of this complex number.
     * @return double; the phase or phi of this complex number in Radians
     */
    public double phi()
    {
        return Math.atan2(this.im, this.re);
    }

    /**
     * Determine if this Complex has an imaginary component of zero.
     * @return boolean; true if the imaginary component of this Complex number is 0.0; false if the imaginary component of this
     *         Complex number is not 0.0.
     */
    public boolean isReal()
    {
        return this.im == 0.0;
    }

    /**
     * Determine if this Complex has a real component of zero.
     * @return boolean; true if the real component of this Complex number is 0.0; false if the real component of this Complex
     *         number is not 0.0
     */
    public boolean isImaginary()
    {
        return this.re == 0.0;
    }

    /**
     * Add this Complex and another Complex.
     * @param rightOperand Complex; the other Complex
     * @return Complex; the sum of this Complex and the other Complex
     */
    public Complex plus(final Complex rightOperand)
    {
        return new Complex(this.re + rightOperand.re, this.im + rightOperand.im);
    }

    /**
     * Add a scalar to this Complex.
     * @param rightOperand double; the scalar
     * @return Complex; the sum of this Complex and the scalar
     */
    public Complex plus(final double rightOperand)
    {
        return new Complex(this.re + rightOperand, this.im);
    }

    /**
     * Subtract another Complex from this Complex.
     * @param rightOperand Complex; the other Complex
     * @return Complex; the difference of this Complex and the other Complex
     */
    public Complex minus(final Complex rightOperand)
    {
        return new Complex(this.re - rightOperand.re, this.im - rightOperand.im);
    }

    /**
     * Subtract a scalar from this Complex.
     * @param rightOperand double; the scalar
     * @return Complex; the difference of this Complex and the scalar
     */
    public Complex minus(final double rightOperand)
    {
        return new Complex(this.re - rightOperand, this.im);
    }

    /**
     * Multiply this Complex with another Complex.
     * @param rightOperand Complex; the right hand side operand
     * @return Complex; the product of this Complex and the other Complex
     */
    public Complex times(final Complex rightOperand)
    {
        return new Complex(this.re * rightOperand.re - this.im * rightOperand.im,
                this.im * rightOperand.re + this.re * rightOperand.im);
    }

    /**
     * Multiply this Complex with a scalar.
     * @param rightOperand double; the right hand side operand
     * @return Complex; the product of this Complex and the scalar
     */
    public Complex times(final double rightOperand)
    {
        return new Complex(this.re * rightOperand, this.im * rightOperand);
    }

    /**
     * Compute the reciprocal of this Complex. If this is zero, the result will have the re field set to
     * Double.POSITIVE_INFINITY and the imaginary part set to NEGATIVE_INFINITY.
     * @return Complex; the reciprocal of this Complex (1 / this)
     */
    public Complex reciprocal()
    {
        double divisor = this.re * this.re + this.im * this.im;
        if (0.0 == divisor)
        {
            return new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        return new Complex(this.re / divisor, -this.im / divisor);
    }

    /**
     * Divide this Complex by another Complex. Division by ZERO yields a Complex with re set to Infinity if this.re != 0 and NaN
     * if this.re == 0 and im set to Infinity if this.im != 0 and NaN if this.im == 0.
     * @param rightOperand Complex; the right hand side operand
     * @return Complex; the ratio of this Complex and the right hand side operand
     */
    public Complex divideBy(final Complex rightOperand)
    {
        if (rightOperand.re == 0 && rightOperand.im == 0)
        {
            return new Complex(this.re / 0.0, this.im / 0.0);
        }
        return this.times(rightOperand.reciprocal());
    }

    /**
     * Divide this Complex by a scalar. Division by 0.0 yields a Complex with re set to Infinity if this.re != 0 and NaN
     * if this.re == 0 and im set to Infinity if this.im != 0 and NaN if this.im == 0.
     * @param rightOperand double; the scalar right hand side operand
     * @return Complex; the ratio of this Complex and the right hand side operand
     */
    public Complex divideBy(final double rightOperand)
    {
        return new Complex(this.re / rightOperand, this.im / rightOperand);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Complex [re=" + this.re + ", im=" + this.im + "]";
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.im);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.re);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:needbraces")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Complex other = (Complex) obj;
        if (Double.doubleToLongBits(this.im) != Double.doubleToLongBits(other.im))
            return false;
        if (Double.doubleToLongBits(this.re) != Double.doubleToLongBits(other.re))
            return false;
        return true;
    }

}