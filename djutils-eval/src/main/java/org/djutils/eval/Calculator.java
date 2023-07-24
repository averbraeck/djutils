package org.djutils.eval;

import org.djunits.value.vdouble.scalar.base.DoubleScalar;

/**
 * Calculator.java. Demonstrates use of the Eval class.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Calculator
{
    /**
     * Demonstrate the Eval class. Program entry point.
     * @param args String ...; the command line arguments
     */
    public static void main(final String... args)
    {
        doEval(" 23.45 * 10 ");
        doEval("PI()*AVOGADRO()");
        doEval("PI()");
        doEval("PI()/PI()");
        doEval("CURRENTTIME()");
        doEval("PHI()");
        System.out.println();
        doEval("PI()");
        doEval("PI()/PI()");
        System.out.println();
        doEval("NEUTRONMASS()-ELECTRONMASS()");
        doEval("NEUTRONMASS()-PI()");
        doEval("12 [ms-1] / 123[/s]"); 
    }

    /**
     * Print expression followed by the result.
     * @param expression String; expression to evaluate
     */
    public static void doEval(final String expression)
    {
        try
        {
            DoubleScalar<?, ?> result = Eval.evaluate(expression, null);
            System.out.println(expression + ": " + result);
        }
        catch (RuntimeException e)
        {
            System.out.println(expression + ": ERROR: "+ e.getMessage());
        }
    }
}
