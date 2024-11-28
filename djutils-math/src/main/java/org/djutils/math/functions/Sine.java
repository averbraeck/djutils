package org.djutils.math.functions;

import java.util.Objects;

import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * Sine function.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Sine implements MathFunction
{
    /** Size multiplier. */
    private final double amplitude;

    /** Time scale multiplier. */
    private final double omega;

    /** Time scale shift. */
    private final double shift;

    /** The function that yields x (may be null). */
    private final MathFunction chain;

    /**
     * Construct a new Sine function <code>factor * sin(omega * x + shift)</code>.
     * @param chain the MathFunction that yields the <code>x</code> for this power function
     * @param amplitude multiplication factor for the output
     * @param omega radial frequency; multiplication factor for the input
     * @param shift time shift for the input (applied <b>after</b> the <code>omega</code> factor)
     */
    public Sine(final MathFunction chain, final double amplitude, final double omega, final double shift)
    {
        this.amplitude = amplitude;
        this.omega = omega;
        this.shift = shift;
        this.chain = chain;
    }

    /**
     * Construct a new Sine function <code>factor * sin(omega * x + shift)</code>.
     * @param amplitude multiplication factor for the output
     * @param omega radial frequency; multiplication factor for the input
     * @param shift time shift for the input (applied <b>after</b> the <code>omega</code> factor)
     */
    public Sine(final double amplitude, final double omega, final double shift)
    {
        this.amplitude = amplitude;
        this.omega = omega;
        this.shift = shift;
        this.chain = null;
    }

    /**
     * Construct a cosine function <code>factor * cos(omega * x + shift)</code>. The result is actually a Sine with the
     * correctly adjusted <code>shift</code>.
     * @param amplitude multiplication factor for the output
     * @param omega radial frequency; multiplication factor for the input
     * @param shift time shift for the input (applied <b>after</b> the <code>omega</code> factor)
     * @return Sine with the requested <code>amplitude</code>, <code>omega</code> and adjusted <code>shift</code>
     */
    public static Sine cosine(final double amplitude, final double omega, final double shift)
    {
        return new Sine(amplitude, omega, AngleUtil.normalizeAroundZero(shift + Math.PI / 2));
    }

    @Override
    public double get(final double x)
    {
        if (this.amplitude == 0.0)
        {
            return 0.0;
        }
        double xValue = this.chain == null ? x : this.chain.get(x);
        return this.amplitude * Math.sin(this.omega * xValue + this.shift);
    }

    @Override
    public MathFunction getDerivative()
    {
        Sine myDerivative = new Sine(this.chain, this.amplitude * this.omega, this.omega,
                AngleUtil.normalizeAroundZero(this.shift + Math.PI / 2));
        if (this.chain == null)
        {
            return myDerivative.simplify();
        }
        MathFunction myChainDerivative = new Sine(this.chain, myDerivative.amplitude, myDerivative.omega, myDerivative.shift);
        return new Product(myChainDerivative, this.chain.getDerivative()).simplify();
    }

    @Override
    public MathFunction simplify()
    {
        if (this.amplitude == 0.0)
        {
            return Constant.ZERO;
        }
        return this;
    }

    @Override
    public double getScale()
    {
        return this.amplitude;
    }

    @Override
    public MathFunction scaleBy(final double scaleFactor)
    {
        if (scaleFactor == 0.0)
        {
            return Constant.ZERO;
        }
        if (scaleFactor == 1.0)
        {
            return this;
        }
        return new Sine(this.chain, scaleFactor * this.amplitude, this.omega, this.shift);
    }

    @Override
    public int sortPriority()
    {
        return 4;
    }

    @Override
    public int compareWithinSubType(final MathFunction other)
    {
        Throw.when(!(other instanceof Sine), IllegalArgumentException.class, "other is of wrong type");
        Sine otherSine = (Sine) other;
        if (otherSine.omega > this.omega)
        {
            return -1;
        }
        if (otherSine.omega < this.omega)
        {
            return 1;
        }
        return 0;
    }

    /**
     * Compute the sum of two sines. The sines must have the same <code>omega</code> but have their own amplitude and shift.
     * @param chain the chained MathFunction
     * @param amplitude1 amplitude of the first sine
     * @param amplitude2 amplitude of the second sine
     * @param omega angular frequency of both sines
     * @param shift1 phase shift of first sine
     * @param shift2 phase shift of second sine
     * @return a new Sine that represents the sum of the supplied sines
     */
    private static Sine sumSines(final MathFunction chain, final double amplitude1, final double amplitude2, final double omega,
            final double shift1, final double shift2)
    {
        // There is probably a way to calculate the result that takes fewer CPU cycles.
        double re = amplitude1 * Math.cos(shift1) + amplitude2 * Math.cos(shift2);
        double im = amplitude1 * Math.sin(shift1) + amplitude2 * Math.sin(shift2);
        double resultAmplitude = Math.hypot(re, im);
        double resultShift = Math.atan2(im, re);
        return new Sine(chain, resultAmplitude, omega, resultShift);
    }

    @Override
    public MathFunction mergeAdd(final MathFunction other)
    {
        if (other instanceof Sine)
        {
            Sine otherSine = (Sine) other;
            if (this.omega == otherSine.omega && this.chain == otherSine.chain)
            {
                return sumSines(this.chain, this.amplitude, otherSine.amplitude, this.omega, this.shift, otherSine.shift);
            }
        }
        return null;
    }

    @Override
    public MathFunction mergeMultiply(final MathFunction other)
    {
        if (other instanceof Sine)
        {
            Sine otherSine = (Sine) other;
            if (this.omega == otherSine.omega && this.chain == otherSine.chain)
            {
                /*-
                 * a * sin(x + theta) * b * sin(x + phi)
                 * == (cos(x + theta - x - phi) - cos(x + theta + x + phi))               * a * b / 2
                 * == (cos(theta - phi)         - cos(2 * x + theta + phi))               * a * b / 2
                 * == (cos(theta - phi)         - sin(pi/2 + 2 * x + theta + phi))        * a * b / 2
                 * == (cos(theta - phi)         + sin(pi + pi / 2 + 2 * x + theta + phi)) * a * b / 2
                 * == (cos(theta - phi)         + sin(2 * x + theta + phi + pi + pi / 2)) * a * b / 2
                 */
                double scale = this.amplitude * otherSine.amplitude / 2;
                double phaseShift = AngleUtil.normalizeAroundZero(this.shift + otherSine.shift + Math.PI + Math.PI / 2);
                return new Sum(new Constant(Math.cos(this.shift - otherSine.shift) * scale),
                        new Sine(this.chain, scale, this.omega * 2, phaseShift));
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        double quadrant = AngleUtil.normalizeAroundPi(this.shift) / (Math.PI / 2);
        int roundedQuadrant = (int) Math.round(quadrant);
        double deviation = Math.abs(roundedQuadrant - quadrant);
        boolean closeTo90 = deviation < 10 * Math.ulp(Math.PI);
        boolean useCosine = closeTo90 && roundedQuadrant % 2 == 1;
        boolean useSine = closeTo90 && roundedQuadrant % 2 == 0;
        //System.out.println("roundedQuadrant=" + roundedQuadrant);
        boolean parenthesizeNegative = (useSine || useCosine) && ((roundedQuadrant >= 2) != (this.amplitude < 0));

        StringBuilder result = new StringBuilder();
        if (parenthesizeNegative)
        {
            result.append("(-");
        }
        if (this.amplitude != 1.0)
        {
            result.append(printValue(Math.abs(this.amplitude)));
        }
        result.append(useCosine ? "cos(" : "sin(");
        if (this.omega != 1.0)
        {
            result.append(printValue(this.omega));
        }
        result.append(this.chain == null ? "x" : ("(" + this.chain.toString() + ")"));
        if (this.shift != 0.0)
        {
            if (!closeTo90)
            {
                if (this.shift >= 0.0)
                {
                    result.append("+");
                }
                result.append(printValue(this.shift));
            }
        }
        result.append(")");
        if (parenthesizeNegative)
        {
            result.append(")");
        }
        return result.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.amplitude, this.chain, this.omega, this.shift);
    }

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
        Sine other = (Sine) obj;
        return Double.doubleToLongBits(this.amplitude) == Double.doubleToLongBits(other.amplitude)
                && Objects.equals(this.chain, other.chain)
                && Double.doubleToLongBits(this.omega) == Double.doubleToLongBits(other.omega)
                && Double.doubleToLongBits(this.shift) == Double.doubleToLongBits(other.shift);
    }

}
