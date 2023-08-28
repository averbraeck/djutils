package org.djutils.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.djunits.quantity.Quantity;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.ForceUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.PositionUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Position;
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
 * @author Peter Knoppers</a>
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
            new Eval().evaluate(null);
            fail("null pointer for expression should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Eval().evaluate("");
            fail("empty string for expression should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Eval().evaluate("123)"); // Unbalanced closing parentheses
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
                new Eval().evaluate(operation);
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
                new Eval().evaluate(operation);
                fail("Binary operand without operands should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing left operand"));
            }

            // With left operand, but missing right operand
            try
            {
                new Eval().evaluate("123" + operation);
                fail("Missing right operand should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing operand"));
            }
        }
        try
        {
            new Eval().evaluate("123-");
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
                new Eval().evaluate(operation);
                fail("Invalid partial operator should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("not a valid operator"));
            }
        }

        try
        {
            new Eval().evaluate("-TRUE()");
            fail("Unary minus cannot be applied to logical value");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot apply unary minus"));
        }

        try
        {
            new Eval().evaluate("TRUE()&");
            fail("Incomplete operator should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("single \'&\' is not a valid operator"));
        }

        try
        {
            new Eval().evaluate("TRUE()&TRUE()");
            fail("Incomplete operator should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("single \'&\' is not a valid operator"));
        }

        try
        {
            new Eval().evaluate("TRUE()|");
            fail("Incomplete operator should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("single \'|\' is not a valid operator"));
        }

        try
        {
            new Eval().evaluate("TRUE()|TRUE()");
            fail("Incomplete operator should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("single \'|\' is not a valid operator"));
        }

        try
        {
            new Eval().evaluate("(123+456");
            fail("Unclosed parenthesis should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing closing parenthesis"));
        }

        try
        {
            new Eval().evaluate("(123+456(");
            fail("Unclosed parenthesis should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("operator expected"));
        }

        try
        {
            new Eval().evaluate("+123");
            fail("Double (unary) plus should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing left operand"));
        }

        try
        {
            new Eval().evaluate("-+123");
            fail("Double (unary) plus should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing left operand"));
        }

        try
        {
            new Eval().evaluate("123 [m/s");
            fail("Missing closing bracket of unit should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("missing closing bracket (\']\')"));
        }

        try
        {
            new Eval().evaluate("123 [m/s*");
            fail("Missing closing bracket of unit should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("bad symbol in si unit string"));
        }

        try
        {
            new Eval().evaluate("abc+123");
            fail("Variable without RetrieveValue object should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot resolve variable "));
        }

        try
        {
            new Eval().evaluate("abc.def_pqr+123");
            fail("Variable without RetrieveValue object should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot resolve variable "));
        }

        try
        {
            new Eval().evaluate("abc");
            fail("Variable without RetrieveValue object should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot resolve variable "));
        }

        try
        {
            new Eval().evaluate("abc(");
            fail("Variable without RetrieveValue object should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("missing closing parenthesis "));
        }

        try
        {
            new Eval().evaluate("123+456%");
            fail("Bad character where operator is expected should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("operator expected "));
        }

        verifyValueAndUnit("dot in unit", new Eval().evaluateExpression("20[kg.m/s2]"), 20, 0, ForceUnit.SI.getQuantity());
    }

    /**
     * Test all binary operations.
     */
    @Test
    public void testBinaryOperations()
    {
        verifyValueAndUnit("Dimensionless + Dimensionless", new Eval().evaluate("123+456"), 579, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless - Dimensionless", new Eval().evaluate("123-456"), -333, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless * Dimensionless", new Eval().evaluate("123*456"), 56088, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless / Dimensionless", new Eval().evaluate("123/456"), 123.0 / 456, 0.00001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("Dimensionless ^ Dimensionless", new Eval().evaluate("123^4.56"), Math.pow(123, 4.56), 0.1,
                DimensionlessUnit.SI.getQuantity());
        verifyBoolean("Dimensionless > Dimensionless", new Eval().evaluate("123>456"), false);
        verifyBoolean("Dimensionless > Dimensionless", new Eval().evaluate("456>123"), true);
        verifyBoolean("Dimensionless >= Dimensionless", new Eval().evaluate("123>=123"), true);
        verifyBoolean("Dimensionless >= Dimensionless", new Eval().evaluate("123>=123.01"), false);
        verifyBoolean("Dimensionless < Dimensionless", new Eval().evaluate("123<456"), true);
        verifyBoolean("Dimensionless < Dimensionless", new Eval().evaluate("456<123"), false);
        verifyBoolean("Dimensionless <= Dimensionless", new Eval().evaluate("123<=123"), true);
        verifyBoolean("Dimensionless <= Dimensionless", new Eval().evaluate("123<=122.99"), false);
        verifyBoolean("Dimensionless == Dimensionless", new Eval().evaluate("123==123"), true);
        verifyBoolean("Dimensionless == Dimensionless", new Eval().evaluate("123==124"), false);
        verifyBoolean("Dimensionless != Dimensionless", new Eval().evaluate("123!=123"), false);
        verifyBoolean("Dimensionless != Dimensionless", new Eval().evaluate("123!=124"), true);
        verifyBoolean("not operator", new Eval().evaluate("!2==2"), false);
        verifyBoolean("not operator", new Eval().evaluate("!2!=2"), true);
        verifyBoolean("not operator", new Eval().evaluate("!3==3"), false);
        verifyBoolean("not operator", new Eval().evaluate("!3!=3"), true);
        verifyBoolean("true && true", new Eval().evaluate("TRUE()&&TRUE()"), true);
        verifyBoolean("true && false", new Eval().evaluate("TRUE()&&FALSE()"), false);
        verifyBoolean("false && true", new Eval().evaluate("FALSE()&&TRUE()"), false);
        verifyBoolean("false && false", new Eval().evaluate("FALSE()&&FALSE()"), false);
        verifyBoolean("true || true", new Eval().evaluate("TRUE()||TRUE()"), true);
        verifyBoolean("true || false", new Eval().evaluate("TRUE()||FALSE()"), true);
        verifyBoolean("false || true", new Eval().evaluate("FALSE()||TRUE()"), true);
        verifyBoolean("false || false", new Eval().evaluate("FALSE()||FALSE()"), false);
        for (String operator : new String[] {"^", "*", "/", "+", "-", "<", "<=", ">", ">=", "&&", "||"})
        {
            try
            {
                new Eval().evaluate("123" + operator + "TRUE()");
                fail("Illegal operand type should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("right operand ")
                        || rte.getMessage().toLowerCase().contains("cannot "));
            }

            try
            {
                new Eval().evaluate("TRUE()" + operator + "123");
                fail("Illegal operand type should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("left operand ")
                        || rte.getMessage().toLowerCase().contains("cannot "));
            }

        }

        for (String operator : new String[] {"^", "+", "-", "<", "<=", ">", ">=", "&&", "||"})
        {
            try
            {
                new Eval().evaluate("123" + operator + "345 [m/s]");
                fail("Non-compatible operand types should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot "));
            }
        }
    }

    /**
     * Test some illegal operators
     */
    @Test
    public void testIllegalOperators()
    {
        try
        {
            new Eval().evaluate("2=5");
            fail("Single \'=\' should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("single \'=\' is not a valid operator"));
        }

        try
        {
            new Eval().evaluate("TRUE()!FALSE()");
            fail("Single \'!\' should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("single \'!\' is not a valid operator"));
        }

    }

    /**
     * Test handling of binary operator at the end of the expression.
     */
    @Test
    public void testBinaryOperatorAtEndOfExpression()
    {
        for (String operation : new String[] {"^", "*", "/", "+", "&&", "||", "<", "<=", ">", ">=", "==", "!="})
        {
            try
            {
                new Eval().evaluate("123" + operation);
                fail("Binary operator at end of expression should have thrown a RuntimeException");
            }
            catch (RuntimeException rte)
            {
                assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing operand"));
            }
        }
    }

    /**
     * Test the number parser
     */
    @Test
    public void testNumberParser()
    {
        verifyValueAndUnit("E notation", new Eval().evaluate("2E6"), 2e6, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("E notation", new Eval().evaluate("2E-6"), 2e-6, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("e notation", new Eval().evaluate("2e6"), 2e6, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("e notation", new Eval().evaluate("2e-6"), 2e-6, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("E notation", new Eval().evaluate("2E+6"), 2e6, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("e notation", new Eval().evaluate("2e+6"), 2e6, 0, DimensionlessUnit.SI.getQuantity());
        try
        {
            new Eval().evaluate("123e45e6");
            fail("Multiple e letters in a number should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("too many"));
        }

        try
        {
            new Eval().evaluate("123e4.5");
            fail("Decimal symbol after e should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("not allowed after"));
        }

        try
        {
            new Eval().evaluate("123.456.789");
            fail("Multiple radix symbols should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("too many"));
        }

        try
        {
            new Eval().evaluate("123e--4");
            fail("Multiple minus signs in exponent should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("too many"));
        }

        try
        {
            new Eval().evaluate("123e-+4");
            fail("Multiple signs in exponent should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("too many"));
        }

        try
        {
            new Eval().evaluate("123..56");
            fail("Multiple radix symbols in mantissa should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("too many"));
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
            new Eval().evaluate("NONEXISTENTZEROARGUMENTFUNCTION()");
            fail("Calling a non existent zero-argument function should have thrown a RunTimeException");
        }
        catch (RuntimeException re)
        {
            assertTrue("exception is descriptive", re.getMessage().contains("Unknown"));
        }
        verifyValueAndUnit("Avogadro constant", new Eval().evaluate("AVOGADRO()"), Constants.AVOGADRO.si, 0.0,
                Constants.AVOGADRO.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Boltzmann constant", new Eval().evaluate("BOLTZMANN()"), Constants.BOLTZMANN.si, 0.0,
                Constants.BOLTZMANN.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Cesium 133 frequency constant", new Eval().evaluate("CESIUM133_FREQUENCY()"),
                Constants.CESIUM133_FREQUENCY.si, 0.0, Constants.CESIUM133_FREQUENCY.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Current time", new Eval().evaluate("CURRENTTIME()"), System.currentTimeMillis() / 1000, 1.0,
                TimeUnit.BASE_SECOND.getQuantity());
        verifyValueAndUnit("Base of natural logarithm", new Eval().evaluate("E()"), Constants.E.si, 0.0,
                Constants.E.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Electrical charge of an electron", new Eval().evaluate("ELECTRONCHARGE()"),
                Constants.ELECTRONCHARGE.si, 0.0, Constants.ELECTRONCHARGE.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Mass of an electrong", new Eval().evaluate("ELECTRONMASS()"), Constants.ELECTRONMASS.si, 0.0,
                Constants.ELECTRONMASS.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Gravitational constant at sea level", new Eval().evaluate("G()"), Constants.G.si, 0.0,
                Constants.G.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Speed of light in vacuum", new Eval().evaluate("LIGHTSPEED()"), Constants.LIGHTSPEED.si, 0.0,
                Constants.LIGHTSPEED.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Luminous efficacy Kcd of monochromatic radiation of frequency 540×10^12 Hz (540 THz). ",
                new Eval().evaluate("LUMINOUS_EFFICACY_540THZ()"), Constants.LUMINOUS_EFFICACY_540THZ.si, 0.0,
                Constants.LUMINOUS_EFFICACY_540THZ.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Mass of a neutron", new Eval().evaluate("NEUTRONMASS()"), Constants.NEUTRONMASS.si, 0.0,
                Constants.NEUTRONMASS.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Verify value of PI", new Eval().evaluate("PI()"), Constants.PI.si, 0.0,
                Constants.PI.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Phi (the golden ratio)", new Eval().evaluate("PHI()"), Constants.PHI.si, 0.0,
                Constants.PHI.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Planck constant", new Eval().evaluate("PLANCK()"), Constants.PLANCK.si, 0.0,
                Constants.PLANCK.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Planck constant divided by 2 pi", new Eval().evaluate("PLANCKREDUCED()"), Constants.PLANKREDUCED.si,
                0.0, Constants.PLANKREDUCED.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Electrical charge of a proton", new Eval().evaluate("PROTONCHARGE()"), Constants.PROTONCHARGE.si,
                0.0, Constants.PROTONCHARGE.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Mass of a proton", new Eval().evaluate("PROTONMASS()"), Constants.PROTONMASS.si, 0.0,
                Constants.PROTONMASS.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Tau (2 * pi)", new Eval().evaluate("TAU()"), Constants.TAU.si, 0.0,
                Constants.TAU.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Impedance of vacuum", new Eval().evaluate("VACUUMIMPEDANCE()"), Constants.VACUUMIMPEDANCE.si, 0.0,
                Constants.VACUUMIMPEDANCE.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Permeability of vacuum", new Eval().evaluate("VACUUMPERMEABILITY()"),
                Constants.VACUUMPERMEABILITY.si, 0.0, Constants.VACUUMPERMEABILITY.getDisplayUnit().getQuantity());
        verifyValueAndUnit("Permittivity of vacuum", new Eval().evaluate("VACUUMPERMITTIVITY()"),
                Constants.VACUUMPERMITTIVITY.si, 0.0, Constants.VACUUMPERMITTIVITY.getDisplayUnit().getQuantity());
        verifyBoolean("Logical value true", new Eval().evaluate("TRUE()"), true);
        verifyBoolean("Logical value false", new Eval().evaluate("FALSE()"), false);
        verifyValueAndUnit("Number that starts with radix symbol", new Eval().evaluate(".345"), .345, 0,
                DimensionlessUnit.SI.getQuantity());
    }

    /**
     * Test the unary operators
     */
    @Test
    public void testUnaryOperators()
    {
        try
        {
            new Eval().evaluate("!5");
            fail("Applying logical not on non-logical value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("cannot apply unary not operator"));
        }

        try
        {
            new Eval().evaluate("123 456"); // Missing binary operator
            fail("Two operands with no operator between them should have thown a RuntimException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("operator expected"));
        }

        try
        {
            new Eval().evaluate("-TRUE()"); // Unary minus on logical value
            fail("Unary minus on logical value should have thrown an RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("cannot apply unary minus"));
        }

        try
        {
            new Eval().evaluate("!5"); // Logical not operator on non logical operand
            fail("Unary not operator on non logical operand should have thrown an RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message is descriptive", rte.getMessage().toLowerCase().contains("cannot apply unary not"));
        }
    }

    /**
     * Test the one argument functions (mostly math)
     */
    @Test
    public void testOneArgumentFunctions()
    {
        try
        {
            new Eval().evaluate("nosuchoneargumentfunction(123)");
            fail("Nonexistant one-argument function should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("unknown function"));
        }
        try
        {
            new Eval().evaluate("sin(TRUE())");
            fail("Math function called on logical value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("does not take "));
        }
        try
        {
            new Eval().evaluate("sin(123 [m])");
            fail("Math function called on non-dimensionless value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("incompatible quantity"));
        }
        verifyValueAndUnit("acos(-1)", new Eval().evaluate("acos(-1)"), Math.acos(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("acos(0.5)", new Eval().evaluate("acos(0.5)"), Math.acos(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("asin(-1)", new Eval().evaluate("asin(-1)"), Math.asin(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("asin(0.5)", new Eval().evaluate("asin(0.5)"), Math.asin(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("atan(-1)", new Eval().evaluate("atan(-1)"), Math.atan(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("atan(0.5)", new Eval().evaluate("atan(0.5)"), Math.atan(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cbrt(50)", new Eval().evaluate("cbrt(50)"), Math.cbrt(50), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cbrt(0.5)", new Eval().evaluate("cbrt(0.5)"), Math.cbrt(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cos(-1)", new Eval().evaluate("cos(-1)"), Math.cos(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cos(0.5)", new Eval().evaluate("cos(0.5)"), Math.cos(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cosh(-1)", new Eval().evaluate("cosh(-1)"), Math.cosh(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("cosh(0.5)", new Eval().evaluate("cosh(0.5)"), Math.cosh(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("exp(-1)", new Eval().evaluate("exp(-1)"), Math.exp(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("exp(0.5)", new Eval().evaluate("exp(0.5)"), Math.exp(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("expm1(-1)", new Eval().evaluate("expm1(-1)"), Math.expm1(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("expm1(0.5)", new Eval().evaluate("expm1(0.5)"), Math.expm1(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log(50)", new Eval().evaluate("log(50)"), Math.log(50), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log(0.5)", new Eval().evaluate("log(0.5)"), Math.log(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log10(50)", new Eval().evaluate("log10(50)"), Math.log10(50), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log10(0.5)", new Eval().evaluate("log10(0.5)"), Math.log10(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log1p(50)", new Eval().evaluate("log1p(50)"), Math.log1p(50), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("log1p(0.5)", new Eval().evaluate("log1p(0.5)"), Math.log1p(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("signum(0)", new Eval().evaluate("signum(0)"), Math.signum(0), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("signum(0.5)", new Eval().evaluate("signum(0.5)"), Math.signum(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("signum(-0.5)", new Eval().evaluate("signum(-0.5)"), Math.signum(-0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sin(-1)", new Eval().evaluate("sin(-1)"), Math.sin(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sin(0.5)", new Eval().evaluate("sin(0.5)"), Math.sin(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sinh(-1)", new Eval().evaluate("sinh(-1)"), Math.sinh(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sinh(0.5)", new Eval().evaluate("sinh(0.5)"), Math.sinh(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sqrt(50)", new Eval().evaluate("sqrt(50)"), Math.sqrt(50), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("sqrt(0.5)", new Eval().evaluate("sqrt(0.5)"), Math.sqrt(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tan(-1)", new Eval().evaluate("tan(-1)"), Math.tan(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tan(0.5)", new Eval().evaluate("tan(0.5)"), Math.tan(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tanh(-1)", new Eval().evaluate("tanh(-1)"), Math.tanh(-1), 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("tanh(0.5)", new Eval().evaluate("tanh(0.5)"), Math.tanh(0.5), 0.000001,
                DimensionlessUnit.SI.getQuantity());
    }

    /**
     * Test the two-parameter functions.
     */
    @Test
    public void testTwoArgumentFunctions()
    {
        verifyValueAndUnit("pow(3.4,5.2)", new Eval().evaluate("pow(3.4,5.2)"), Math.pow(3.4, 5.2), 0.1,
                DimensionlessUnit.SI.getQuantity());

        try
        {
            new Eval().evaluate("12[m]^3");
            fail("Power operator on non-dimensionless should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot raise "));
        }

        try
        {
            new Eval().evaluate("pow(TRUE(),5.2)");
            fail("Attempt to raise a logical value to some power should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("does not take "));
        }

        try
        {
            new Eval().evaluate("pow(3.4,TRUE())");
            fail("Attempt to raise a value to a logical value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("does not take "));
        }

        try
        {
            new Eval().evaluate("pow(3.4,5.2[m])");
            fail("Attempt to raise a value to a non Dimensionless value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("incompatible quantity"));
        }

        try
        {
            new Eval().evaluate("pow(3.4[s],5.2)");
            fail("Attempt to raise a value to a non Dimensionless value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("incompatible quantity"));
        }

        try
        {
            new Eval().evaluate("TRUE()^5.2");
            fail("Attempt to raise a logical value to some power should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot raise "));
        }

        try
        {
            new Eval().evaluate("3.4^TRUE()");
            fail("Attempt to raise a value to a logical value should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot raise "));
        }

        verifyValueAndUnit("atan2(1,2)", new Eval().evaluate("atan2(1,2)"), Math.atan2(1, 2), 0.00001,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("atan2(-2,-1)", new Eval().evaluate("atan2(-2,-1)"), Math.atan2(-2, -1), 0.00001,
                DimensionlessUnit.SI.getQuantity());
        try
        {
            new Eval().evaluate("atan2(TRUE(),1)");
            fail("Attempt to use a logical operand in atan2 should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("atan2 does not take "));
        }

        try
        {
            new Eval().evaluate("atan2(2,FALSE())");
            fail("Attempt to use a logical operand in atan2 should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("atan2 does not take "));
        }

        verifyValueAndUnit("atan2(1[m],2[m])", new Eval().evaluate("atan2(1[m],2[m])"), Math.atan2(1, 2), 0.00001,
                DimensionlessUnit.SI.getQuantity());

        try
        {
            new Eval().evaluate("atan2(1[m],2[s])");
            fail("Attempt to use atan2 with parameters of different types should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot compute atan2 of "));
        }

        try
        {
            new Eval().evaluate("nosuchtwoparameterfunction(123,456)");
            fail("Attempt to call an non-existant two-parameter function should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("unknown function "));
        }
    }

    /**
     * Test division by zero
     */
    @Test
    public void testDivisionByZero()
    {
        try
        {
            new Eval().evaluate("1/0");
            fail("Division by zero should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("division by 0"));
        }
    }

    /**
     * Test the order in which operators are applied.
     */
    @Test
    public void testEvaluationOrder()
    {
        verifyValueAndUnit("13+17-19+23-31", new Eval().evaluate("13+17-19+23-31"), 13 + 17 - 19 + 23 - 31, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("13+17-19/23-31", new Eval().evaluate("13+17-19/23-31"), 13 + 17 - 19.0 / 23 - 31, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("13+17-19/23^3-31", new Eval().evaluate("13+17-19/23^3-31"), 13 + 17 - 19.0 / Math.pow(23, 3) - 31,
                0.0000001, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("13*17/19*23/31", new Eval().evaluate("13*17/19*23/31"), 13.0 * 17 / 19 * 23 / 31, 0.000001,
                DimensionlessUnit.SI.getQuantity());
        verifyBoolean("TRUE()&&TRUE()&&TRUE()", new Eval().evaluate("TRUE()&&TRUE()&&TRUE()"), true);
        verifyBoolean("TRUE()&&TRUE()&&FALSE()", new Eval().evaluate("TRUE()&&TRUE()&&FALSE()"), false);
        verifyBoolean("TRUE()&&FALSE()&&TRUE()", new Eval().evaluate("TRUE()&&FALSE()&&TRUE()"), false);
        verifyBoolean("TRUE()&&FALSE()&&FALSE()", new Eval().evaluate("TRUE()&&FALSE()&&FALSE()"), false);
        verifyBoolean("FALSE()&&TRUE()&&TRUE()", new Eval().evaluate("FALSE()&&TRUE()&&TRUE()"), false);
        verifyBoolean("FALSE()&&TRUE()&&FALSE()", new Eval().evaluate("FALSE()&&TRUE()&&FALSE()"), false);
        verifyBoolean("FALSE()&&FALSE()&&TRUE()", new Eval().evaluate("FALSE()&&TRUE()&&TRUE()"), false);
        verifyBoolean("FALSE()&&FALSE()&&FALSE()", new Eval().evaluate("FALSE()&&FALSE()&&FALSE()"), false);

        verifyBoolean("TRUE()||TRUE()&&TRUE()", new Eval().evaluate("TRUE()||TRUE()&&TRUE()"), true);
        verifyBoolean("TRUE()||TRUE()&&FALSE()", new Eval().evaluate("TRUE()||TRUE()&&FALSE()"), true);
        verifyBoolean("TRUE()||FALSE()&&TRUE()", new Eval().evaluate("TRUE()||FALSE()&&TRUE()"), true);
        verifyBoolean("TRUE()||FALSE()&&FALSE()", new Eval().evaluate("TRUE()||FALSE()&&FALSE()"), true);
        verifyBoolean("FALSE()||TRUE()&&TRUE()", new Eval().evaluate("FALSE()||TRUE()&&TRUE()"), true);
        verifyBoolean("FALSE()||TRUE()&&FALSE()", new Eval().evaluate("FALSE()||TRUE()&&FALSE()"), false);
        verifyBoolean("FALSE()||FALSE()&&TRUE()", new Eval().evaluate("FALSE()||FALSE()&&TRUE()"), false);
        verifyBoolean("FALSE()||FALSE()&&FALSE()", new Eval().evaluate("FALSE()||FALSE()&&FALSE()"), false);

        verifyBoolean("TRUE()&&TRUE()||TRUE()", new Eval().evaluate("TRUE()&&TRUE()||TRUE()"), true);
        verifyBoolean("TRUE()&&TRUE()||FALSE()", new Eval().evaluate("TRUE()&&TRUE()||FALSE()"), true);
        verifyBoolean("TRUE()&&FALSE()||TRUE()", new Eval().evaluate("TRUE()&&FALSE()||TRUE()"), true);
        verifyBoolean("TRUE()&&FALSE()||FALSE()", new Eval().evaluate("TRUE()&&FALSE()||FALSE()"), false);
        verifyBoolean("FALSE()&&TRUE()||TRUE()", new Eval().evaluate("FALSE()&&TRUE()||TRUE()"), true);
        verifyBoolean("FALSE()&&TRUE()||FALSE()", new Eval().evaluate("FALSE()&&TRUE()||FALSE()"), false);
        verifyBoolean("FALSE()&&FALSE()||TRUE()", new Eval().evaluate("FALSE()&&TRUE()||TRUE()"), true);
        verifyBoolean("FALSE()&&FALSE()||FALSE()", new Eval().evaluate("FALSE()&&FALSE()||FALSE()"), false);

        verifyBoolean("TRUE()||TRUE()||TRUE()", new Eval().evaluate("TRUE()||TRUE()||TRUE()"), true);
        verifyBoolean("TRUE()||TRUE()||FALSE()", new Eval().evaluate("TRUE()||TRUE()||FALSE()"), true);
        verifyBoolean("TRUE()||FALSE()||TRUE()", new Eval().evaluate("TRUE()||FALSE()||TRUE()"), true);
        verifyBoolean("TRUE()||FALSE()||FALSE()", new Eval().evaluate("TRUE()||FALSE()||FALSE()"), true);
        verifyBoolean("FALSE()||TRUE()||TRUE()", new Eval().evaluate("FALSE()||TRUE()||TRUE()"), true);
        verifyBoolean("FALSE()||TRUE()||FALSE()", new Eval().evaluate("FALSE()||TRUE()||FALSE()"), true);
        verifyBoolean("FALSE()||FALSE()||TRUE()", new Eval().evaluate("FALSE()||FALSE()||TRUE()"), true);
        verifyBoolean("FALSE()||FALSE()||FALSE()", new Eval().evaluate("FALSE()||FALSE()||FALSE()"), false);

        verifyValueAndUnit("2^3^5", new Eval().evaluate("2^3^5"), Math.pow(2, Math.pow(3, 5)), 1,
                DimensionlessUnit.SI.getQuantity());
    }

    /**
     * Test conditional expressions
     */
    @Test
    public void testConditionalExpressions()
    {
        verifyValueAndUnit("TRUE()?3:(1/0)", new Eval().evaluate("TRUE()?3:(1/0)"), 3, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?1/0:3", new Eval().evaluate("FALSE()?1/0:3"), 3, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?3:((1/0))", new Eval().evaluate("TRUE()?3:(1/0)"), 3, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?((1/0)):3", new Eval().evaluate("FALSE()?1/0:3"), 3, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?TRUE()?1:2:3", new Eval().evaluate("TRUE()?TRUE()?1:2:3"), 1, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?FALSE()?1:2:3", new Eval().evaluate("TRUE()?FALSE()?1:2:3"), 2, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?TRUE()?1:2:3", new Eval().evaluate("FALSE()?TRUE()?1:2:3"), 3, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?FALSE()?1:2:3", new Eval().evaluate("FALSE()?FALSE()?1:2:3"), 3, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?1:TRUE()?2:3", new Eval().evaluate("TRUE()?1:TRUE()?2:3"), 1, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?1:FALSE()?2:3", new Eval().evaluate("TRUE()?1:FALSE()?2:3"), 1, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?1:TRUE()?2:3", new Eval().evaluate("FALSE()?1:TRUE()?2:3"), 2, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?1:FALSE()?2:3", new Eval().evaluate("FALSE()?1:FALSE()?2:3"), 3, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?(((3))):(((1/0)))", new Eval().evaluate("TRUE()?(((3))):(((1/0)))"), 3, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?(((1/0))):(((5)))", new Eval().evaluate("FALSE()?(((1/0))):(((5)))"), 5, 0,
                DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("FALSE()?1:2+3", new Eval().evaluate("FALSE()?1:2+3"), 5, 0, DimensionlessUnit.SI.getQuantity());
        verifyValueAndUnit("TRUE()?1:2+3", new Eval().evaluate("TRUE()?1:2+3"), 4, 0, DimensionlessUnit.SI.getQuantity());
        try
        {
            new Eval().evaluate("TRUE()?1:(");
            fail("Missing closing parentheses should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing closing parenthesis"));
        }

        try
        {
            new Eval().evaluate("FALSE()?TRUE()");
            fail("Incomplete conditional expression should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            // System.out.println(rte.getMessage());
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("nonterminated conditional expression"));
        }

        try
        {
            new Eval().evaluate("TRUE()?TRUE()");
            fail("Incomplete conditional expression should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("missing \':\' of conditional expression"));
        }

        try
        {
            new Eval().evaluate("TRUE()?TRUE()?");
            fail("Incomplete conditional expression should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("missing operand"));
        }

        try
        {
            new Eval().evaluate("123?4:5");
            fail("Conditional expression depending on non-logical should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("condition does not evaluate to a logical value"));
        }

        verifyBoolean("Binding strength of else part is minimum", new Eval().evaluate("12>16?3:FALSE()||TRUE()"), true);
        verifyBoolean("Binding strength of else part is minimum", new Eval().evaluate("12>16?3:5>4"), true);
    }

    /**
     * Test the mechanism for retrieving the values from a value store by name and the handling of absolute operands.
     */
    @Test
    public void testNamedVariablesAndHandlingOfAbsoluteOperands()
    {
        try
        {
            new Eval().evaluate("abc");
            fail("Attempt to retrieve value with no value store should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot resolve variable "));
        }

        Map<String, Object> map = new HashMap<>();
        ValueStore valueStore = new ValueStore(map);

        try
        {
            new Eval().setRetrieveValue(valueStore).evaluate("abc");
            fail("Attempt to retrieve value with empty value store should throw a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem", rte.getMessage().toLowerCase().contains("cannot resolve variable "));
        }

        map.put("booleanTrue", Boolean.TRUE);
        Position position = new Position(456, PositionUnit.INCH);
        map.put("position", position);
        Position otherPosition = new Position(135, PositionUnit.YARD);
        map.put("otherPosition", otherPosition);
        verifyBoolean("Retrieve a logical value from the value store",
                new Eval().setRetrieveValue(valueStore).evaluate("booleanTrue"), true);
        verifyValueAndUnit("Retrieve a Position from the value store",
                new Eval().setRetrieveValue(valueStore).evaluate("position"), new Position(456, PositionUnit.INCH).si, 0.0001,
                PositionUnit.INCH.getQuantity());

        verifyValueAndUnit("Abs+Rel->Abs", new Eval().setRetrieveValue(valueStore).evaluate("position+12[m]"),
                new Position(456, PositionUnit.INCH).si + 12, 0.0001, PositionUnit.BASE);

        try
        {
            new Eval().setRetrieveValue(valueStore).evaluate("456[m]+position");
            fail("Using an absolute as RHS for addition should have thrown a RuntimeException");
        }
        catch (RuntimeException rte)
        {
            assertTrue("Message describes the problem",
                    rte.getMessage().toLowerCase().contains("cannot add an absolute value to some other value"));
        }

        verifyValueAndUnit("Abs-Abs->Rel", new Eval().setRetrieveValue(valueStore).evaluate("position-otherPosition"),
                position.minus(otherPosition).si, 0.00001, LengthUnit.SI.getQuantity());
        verifyValueAndUnit("Abs-Rel->Abs", new Eval().setRetrieveValue(valueStore).evaluate("position-200[m]"),
                position.si - 200, 0.0001, PositionUnit.BASE);
    }

    /**
     * Value store for testing the value retrieval system.
     */
    class ValueStore implements RetrieveValue
    {
        /** The mapping from name to value. */
        private final Map<String, Object> values;

        /**
         * Create a new ValueStore.
         * @param map Map&lt;String,Object&gt;; map that translates names to value (not deep-copied; therefore, if this map is
         *            changed at a later time that will affect subsequent lookup results).
         */
        ValueStore(final Map<String, Object> map)
        {
            this.values = map;
        }

        /** {@inheritDoc} */
        @Override
        public Object lookup(final String name)
        {
            return this.values.get(name);
        }

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
        if (!(object instanceof DoubleScalar))
        {
            System.out.println("object: " + object.getClass().getCanonicalName());
        }
        assertTrue(description, object instanceof DoubleScalar);
        DoubleScalar<?, ?> ds = (DoubleScalar<?, ?>) object;
        assertEquals(description, ds.si, expectedValue, tolerance);
        assertEquals(description, ds.getDisplayUnit().getQuantity().getSiDimensions(), expectedQuantity.getSiDimensions());
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
