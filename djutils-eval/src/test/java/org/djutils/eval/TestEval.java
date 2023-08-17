package org.djutils.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djunits.quantity.Quantity;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.base.Constants;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.junit.Test;

/**
 * TestEval.java.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestEval
{
    /**
     * Test some basic things
     */
    @Test
    public void testBasics()
    {
        try
        {
            Eval.evaluate(null, null);
            fail("null pointer for expression should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            Eval.evaluate("", null);
            fail("empty string for expression should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            Eval.evaluate("123)", null); // Unbalanced closing parentheses
            fail("Unbalanced closing parentheses should have thrown a RunT=timeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes position of problem", rte.getMessage().contains("position 3"));
        }

        // Unary operators with no argument
        for (String operation : new String[] {"-", "!"})
        {
            try
            {
                Eval.evaluate(operation, null);
                fail("Unary minus with no trailing operand should have thrown a RuntimException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing operand"));
            }
        }

        // Binary operators
        for (String operation : new String[] {"^", "*", "/", "+", "&&", "||", "<", "<=", ">", ">=", "==", "!="})
        {
            // With both operands missing
            try
            {
                Eval.evaluate(operation, null);
                fail("Binary operand without operands should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing left operand"));
            }

            // With left operand, but missing right operand
            try
            {
                Eval.evaluate("123" + operation, null);
                fail("Missing right operand should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing operand"));
            }
        }
        try
        {
            Eval.evaluate("123-", null);
            fail("Missing right operand should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing operand"));
        }

        for (String operation : new String[] {"&", "|"})
        {
            try
            {
                Eval.evaluate(operation, null);
                fail("Invalid partial operator should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("not a valid operator"));
            }
        }
    }

    /**
     * Test all binary operations.
     */
    @Test
    public void testBinaryOperations()
    {
        verifyValueAndUnit("Dimensionless + Dimensionless", Eval.evaluate("123+456", null), 579, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless - Dimensionless", Eval.evaluate("123-456", null), -333, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless * Dimensionless", Eval.evaluate("123*456", null), 56088, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless / Dimensionless", Eval.evaluate("123/456", null), 123.0 / 456, 0.00001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless ^ Dimensionless", Eval.evaluate("123^4.56", null), Math.pow(123, 4.56), 0.1,
                DimensionlessUnit.SI.getQuantity());
        verifyBoolean("Dimensionless > Dimensionless", Eval.evaluate("123>456", null), false);
        verifyBoolean("Dimensionless > Dimensionless", Eval.evaluate("456>123", null), true);
        verifyBoolean("Dimensionless >= Dimensionless", Eval.evaluate("123>=123", null), true);
        verifyBoolean("Dimensionless >= Dimensionless", Eval.evaluate("123>=123.01", null), false);
        verifyBoolean("Dimensionless < Dimensionless", Eval.evaluate("123<456", null), true);
        verifyBoolean("Dimensionless < Dimensionless", Eval.evaluate("456<123", null), false);
        verifyBoolean("Dimensionless <= Dimensionless", Eval.evaluate("123<=123", null), true);
        verifyBoolean("Dimensionless <= Dimensionless", Eval.evaluate("123<=122.99", null), false);
        verifyBoolean("Dimensionless == Dimensionless", Eval.evaluate("123==123", null), true);
        verifyBoolean("Dimensionless == Dimensionless", Eval.evaluate("123==124", null), false);
        verifyBoolean("Dimensionless != Dimensionless", Eval.evaluate("123!=123", null), false);
        verifyBoolean("Dimensionless != Dimensionless", Eval.evaluate("123!=124", null), true);
        verifyBoolean("not operator", Eval.evaluate("!2==2", null), false);
        verifyBoolean("not operator", Eval.evaluate("!2!=2", null), true);
        verifyBoolean("not operator", Eval.evaluate("!3==3", null), false);
        verifyBoolean("not operator", Eval.evaluate("!3!=3", null), true);
        try
        {
            Eval.evaluate("!5", null);
            fail("Applying logical not on non-logical value should have thrown a RuntimeException");
        }
        catch(RuntimeException rte)
        {
            assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("cannot apply unary not operator"));
        }
    }

    /**
     * Test the zero argument functions.
     */
    @Test
    public void testZeroArgumentFunctions()
    {
        try
        {
            Eval.evaluate("NONEXISTENTZEROARGUMENTFUNCTION()", null);
            fail("Calling a non existent zero-argument function should have thrown a RunTimeException");
        }
        catch (RuntimeException re)
        {
            assertTrue("exception is descriptive", re.getMessage().contains("Unknown"));
        }
        verifyValueAndUnit("Avogadro constant", Eval.evaluate("AVOGADRO()", null), Constants.AVOGADRO.si, 0.0,
                Constants.AVOGADRO.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Boltzmann constant", Eval.evaluate("BOLTZMANN()", null), Constants.BOLTZMANN.si, 0.0,
                Constants.BOLTZMANN.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Cesium 133 frequency constant", Eval.evaluate("CESIUM133_FREQUENCY()", null),
                Constants.CESIUM133_FREQUENCY.si, 0.0, Constants.CESIUM133_FREQUENCY.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Current time", Eval.evaluate("CURRENTTIME()", null), System.currentTimeMillis() / 1000, 1.0,
                TimeUnit.BASE_SECOND.getQuantity());
        verifyValueAndUnit("Base of natural logarithm", Eval.evaluate("E()", null), Constants.E.si, 0.0,
                Constants.E.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Electrical charge of an electron", Eval.evaluate("ELECTRONCHARGE()", null),
                Constants.ELECTRONCHARGE.si, 0.0, Constants.ELECTRONCHARGE.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Mass of an electrong", Eval.evaluate("ELECTRONMASS()", null), Constants.ELECTRONMASS.si, 0.0,
                Constants.ELECTRONMASS.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Gravitational constant at sea level", Eval.evaluate("G()", null), Constants.G.si, 0.0,
                Constants.G.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Speed of light in vacuum", Eval.evaluate("LIGHTSPEED()", null), Constants.LIGHTSPEED.si, 0.0,
                Constants.LIGHTSPEED.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Luminous efficacy Kcd of monochromatic radiation of frequency 540×10^12 Hz (540 THz). ",
                Eval.evaluate("LUMINOUS_EFFICACY_540THZ()", null), Constants.LUMINOUS_EFFICACY_540THZ.si, 0.0,
                Constants.LUMINOUS_EFFICACY_540THZ.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Mass of a neutron", Eval.evaluate("NEUTRONMASS()", null), Constants.NEUTRONMASS.si, 0.0,
                Constants.NEUTRONMASS.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Verify value of PI", Eval.evaluate("PI()", null), Constants.PI.si, 0.0,
                Constants.PI.getDisplayUnit().getQuantity());
        /*-- FIXME bug in Constants.java in djunits
        verifyValueAndUnit("Phi (the golden ratio)", Eval.evaluate("PHI()", null), Constants.PHI.si, 0.0,
                Constants.PHI.getDisplayUnit().getQuantity());
         */
        verifyValueAndUnit("Planck constant", Eval.evaluate("PLANCK()", null), Constants.PLANCK.si, 0.0,
                Constants.PLANCK.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Planck constant divided by 2 pi", Eval.evaluate("PLANKREDUCED()", null), Constants.PLANKREDUCED.si,
                0.0, Constants.PLANKREDUCED.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Electrical charge of a proton", Eval.evaluate("PROTONCHARGE()", null), Constants.PROTONCHARGE.si,
                0.0, Constants.PROTONCHARGE.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Mass of a proton", Eval.evaluate("PROTONMASS()", null), Constants.PROTONMASS.si, 0.0,
                Constants.PROTONMASS.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Tau (2 * pi)", Eval.evaluate("TAU()", null), Constants.TAU.si, 0.0,
                Constants.TAU.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Impedance of vacuum", Eval.evaluate("VACUUMIMPEDANCE()", null), Constants.VACUUMIMPEDANCE.si, 0.0,
                Constants.VACUUMIMPEDANCE.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Permeability of vacuum", Eval.evaluate("VACUUMPERMEABILITY()", null),
                Constants.VACUUMPERMEABILITY.si, 0.0, Constants.VACUUMPERMEABILITY.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Permittivity of vacuum", Eval.evaluate("VACUUMPERMITTIVITY()", null),
                Constants.VACUUMPERMITTIVITY.si, 0.0, Constants.VACUUMPERMITTIVITY.getDisplayUnit().getQuantity());
        verifyBoolean("Logical value true", Eval.evaluate("TRUE()", null), true);
        verifyBoolean("Logical value false", Eval.evaluate("FALSE()", null), false);
    }
    
    /**
     * Test the one argument functions (mostly math)
     */
    @Test
    public void testOneArgumentFunctions()
    {
        try
        {
            Eval.evaluate("nosuchoneargumentfunction(123)", null);
            fail("Nonexistant one-argument function should have thrown a RuntimeException");
        }
        catch(RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("unknown function"));
        }
        try
        {
            Eval.evaluate("sin(TRUE())", null);
            fail("Math function called on logical value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot be applied to"));
        }
        try
        {
            Eval.evaluate("sin(123 [m])", null);
            fail("Math function called on non-dimensionless value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot be applied to"));
        }
        verifyValueAndUnit("acos(-1)", Eval.evaluate("acos(-1)", null), Math.acos(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("acos(0.5)", Eval.evaluate("acos(0.5)", null), Math.acos(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("asin(-1)", Eval.evaluate("asin(-1)", null), Math.asin(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("asin(0.5)", Eval.evaluate("asin(0.5)", null), Math.asin(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("atan(-1)", Eval.evaluate("atan(-1)", null), Math.atan(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("atan(0.5)", Eval.evaluate("atan(0.5)", null), Math.atan(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cbrt(50)", Eval.evaluate("cbrt(50)", null), Math.cbrt(50), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cbrt(0.5)", Eval.evaluate("cbrt(0.5)", null), Math.cbrt(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cos(-1)", Eval.evaluate("cos(-1)", null), Math.cos(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cos(0.5)", Eval.evaluate("cos(0.5)", null), Math.cos(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cosh(-1)", Eval.evaluate("cosh(-1)", null), Math.cosh(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cosh(0.5)", Eval.evaluate("cosh(0.5)", null), Math.cosh(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("exp(-1)", Eval.evaluate("exp(-1)", null), Math.exp(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("exp(0.5)", Eval.evaluate("exp(0.5)", null), Math.exp(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("expm1(-1)", Eval.evaluate("expm1(-1)", null), Math.expm1(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("expm1(0.5)", Eval.evaluate("expm1(0.5)", null), Math.expm1(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log(50)", Eval.evaluate("log(50)", null), Math.log(50), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log(0.5)", Eval.evaluate("log(0.5)", null), Math.log(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log10(50)", Eval.evaluate("log10(50)", null), Math.log10(50), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log10(0.5)", Eval.evaluate("log10(0.5)", null), Math.log10(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log1p(50)", Eval.evaluate("log1p(50)", null), Math.log1p(50), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log1p(0.5)", Eval.evaluate("log1p(0.5)", null), Math.log1p(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("signum(0)", Eval.evaluate("signum(0)", null), Math.signum(0), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("signum(0.5)", Eval.evaluate("signum(0.5)", null), Math.signum(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("signum(-0.5)", Eval.evaluate("signum(-0.5)", null), Math.signum(-0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sin(-1)", Eval.evaluate("sin(-1)", null), Math.sin(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sin(0.5)", Eval.evaluate("sin(0.5)", null), Math.sin(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sinh(-1)", Eval.evaluate("sinh(-1)", null), Math.sinh(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sinh(0.5)", Eval.evaluate("sinh(0.5)", null), Math.sinh(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sqrt(50)", Eval.evaluate("sqrt(50)", null), Math.sqrt(50), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sqrt(0.5)", Eval.evaluate("sqrt(0.5)", null), Math.sqrt(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tan(-1)", Eval.evaluate("tan(-1)", null), Math.tan(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tan(0.5)", Eval.evaluate("tan(0.5)", null), Math.tan(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tanh(-1)", Eval.evaluate("tanh(-1)", null), Math.tanh(-1), 0.000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tanh(0.5)", Eval.evaluate("tanh(0.5)", null), Math.tanh(0.5), 0.000001, DimensionlessUnit.SI.getQuantity());
}

    /**
     * Verify the class, value and unit of a DoubleScalar value.
     * @param description String; description of the test
     * @param object Object; the DoubleScalar
     * @param expectedValue double; the expected SI value
     * @param tolerance double; the maximum error of the SI value
     * @param expectedQuantity Quantity&lt;?&gt;; the expected quantity
     */
    private void verifyValueAndUnit(final String description, final Object object, final double expectedValue,
            final double tolerance, final Quantity<?> expectedQuantity)
    {
        assertTrue(description, object instanceof DoubleScalar);
        DoubleScalar<?, ?> ds = (DoubleScalar<?, ?>) object;
        assertEquals(description, ds.si, expectedValue, tolerance);
        assertEquals(description, ds.getDisplayUnit().getQuantity(), expectedQuantity);
    }

    /**
     * Verify the class and value of a Boolean value
     * @param description String; description of the test
     * @param object Object; the Boolean
     * @param expectedValue boolean; the expected value
     */
    private void verifyBoolean(final String description, final Object object, final boolean expectedValue)
    {
        assertTrue(description, object instanceof Boolean);
        Boolean bv = (Boolean) object;
        assertEquals(description, bv, expectedValue);
    }
}