package org.djutils.eval;

/**
 * EvalDemo.java.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EvalDemo
{
    /**
     * Demonstrate the eval package.
     * @param args the command line arguments (not used)
     */
    public static void main(final String... args)
    {
        run("100[m/s] * 10 [s]");
        run("100[m/s] + 10 [s]");
        run("sqrt(-1)");
        run("1/0");
        
        RetrieveValue values = new RetrieveValue() {
            @Override
            public Object lookup(final String name)
            {
                if (name.equals("myVariable"))
                {
                    return 123.456;
                }
                return null;
            }};
        run("myVariable+20", values);
        run("2 * PI()");
        run("sin(1)");
        run("CURRENTTIME()+5");
        run("CURRENTTIME()/86400[s]"); // Does not work; cannot divide absolute values by anything
    }

    /**
     * Evaluate an expression and print the result. Does not use a value pool.
     * @param expression the expression
     */
    public static void run(final String expression)
    {
        run(expression, null);
    }
    
    /**
     * Evaluate an expression and print the result.
     * @param expression the expression
     * @param valuePool the value pool (may be null
     */
    public static void run(final String expression, final RetrieveValue valuePool)
    {
        System.out.print(expression + ": ");
        try
        {
            Object result = new Eval().setRetrieveValue(valuePool).evaluate(expression);
            System.out.println(result);
        }
        catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
