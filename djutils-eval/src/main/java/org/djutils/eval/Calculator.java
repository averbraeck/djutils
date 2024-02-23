package org.djutils.eval;

import java.util.HashMap;
import java.util.Map;

import org.djunits.value.vdouble.scalar.Position;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;

/**
 * Calculator.java. Demonstrates use of the Eval class.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Calculator
{
    /**
     * Demonstrate the Eval class. Program entry point.
     * @param args String ...; the command line arguments
     */
    public static void main(final String... args)
    {
        doEval("3[kg.ms-2]");
        doEval("23.45*10");
        doEval(" 23.45 * 10 ");
        doEval("PI()*AVOGADRO()");
        doEval("PI()");
        doEval("PI()/PI()");
        doEval("CURRENTTIME()");
        doEval("PHI()");
        System.out.println();
        doEval("NEUTRONMASS()-ELECTRONMASS()");
        doEval("NEUTRONMASS()-PI()");
        doEval("12 [ms-1] / 123[/s]");
        Map<String, DoubleScalar<?,?>> valuePool = new HashMap<>();
        valuePool.put("position", Position.valueOf("100 m")); // Absolute
        doEval("position-10[m]", new RetrieveValue() {

            @Override
            public DoubleScalar<?, ?> lookup(final String name)
            {
                return valuePool.get(name);
            }});
        doEval("position+10[m]", new RetrieveValue() {

            @Override
            public DoubleScalar<?, ?> lookup(final String name)
            {
                return valuePool.get(name);
            }});
        doEval("position-position", new RetrieveValue() {

            @Override
            public DoubleScalar<?, ?> lookup(final String name)
            {
                return valuePool.get(name);
            }});
        doEval("position+position", new RetrieveValue() {

            @Override
            public DoubleScalar<?, ?> lookup(final String name)
            {
                return valuePool.get(name);
            }});
        doEval("5^3");
        doEval("5<5");
        doEval("!5<5");
        doEval("12 [m/s] > 7 [m]");
        doEval("TRUE()");
        doEval("FALSE()");
        doEval("TRUE()!=5");
        doEval("TRUE()==5");
        doEval("FALSE()==5");
        doEval("(2>3)?5:1+100");
        doEval("PHI()");
        doEval("-0==0");//FIXME
        doEval("2^-2");
        doEval("3e+5");
        doEval("12>4?TRUE():(9+5)");
        doEval("3==5!");
        doEval("3[km/h]");
        doEval("5*2[s]/5[s]");
    }

    /**
     * Print expression followed by the result.
     * @param expression String; expression to evaluate
     */
    public static void doEval(final String expression)
    {
        doEval(expression, null);
    }
    
    /**
     * Print expression followed by the result.
     * @param expression String; the expression to evaluate
     * @param valuePool RetrieveValue; variable pool
     */
    public static void doEval(final String expression, final RetrieveValue valuePool)
    {
        try
        {
            Object result = new Eval().setRetrieveValue(valuePool).evaluate(expression);
            System.out.println(expression + ": " + result);
        }
        catch (RuntimeException e)
        {
            System.out.println(expression + ": ERROR: "+ e.getMessage());
        }
    }
}
