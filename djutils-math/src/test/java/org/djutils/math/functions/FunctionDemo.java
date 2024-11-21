package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Demonstrate various classed in function package.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class FunctionDemo
{
    /**
     * Utility class - do not instantiate.
     */
    private FunctionDemo()
    {
        // Do not instantiate.
    }

    /**
     * Demonstrate the Function classes.
     * @param args the command line arguments (not used)
     */
    public static void main(final String... args)
    {
        System.out.println("Build a cubic polynomial as a sum of four power functions");
        Function function =
                new Sum(new PowerFunction(2, 3), new PowerFunction(5, 2), new PowerFunction(8, 1), new PowerFunction(3, 0));
        Function deriv = function.getDerivative();
        Function deriv2 = deriv.getDerivative();
        Function deriv3 = deriv2.getDerivative();
        Function deriv4 = deriv3.getDerivative();
        Function deriv5 = deriv4.getDerivative();
        System.out.println("f      " + function.getDescription());
        System.out.println("f'     " + deriv.getDescription());
        System.out.println("f''    " + deriv2.getDescription());
        System.out.println("f'''   " + deriv3.getDescription());
        System.out.println("f''''  " + deriv4.getDescription());
        System.out.println("f''''' " + deriv5.getDescription()); // Is identical to deriv4
        System.out.println("Check if f'''' is equal to f''''' : " + deriv4.equals(deriv5));
        for (int step = -10; step <= 10; step++)
        {
            double x = 0.5 * step;
            System.out.println(String.format(
                    "x=%5.2f: f(x)=%8.2f; f'(x)=%10.2f; " + "f''(x)=%8.2f; f'''(x)=%8.2f; " + "f''''(x)=%8.2f; f'''''(x)=%8.2f",
                    x, function.get(x), deriv.get(x), deriv2.get(x), deriv3.get(x), deriv4.get(x), deriv5.get(x)));
        }

        System.out.println("\nBuild a continuous piecewise linear function; each linear piece is a power function");
        Function part1 = new Sum(new PowerFunction(1.0, 0), new PowerFunction(0.5, 1));
        Function part2 = new Sum(new PowerFunction(1.1 - 0.4, 0), new PowerFunction(2.0, 1));
        Function part3 = new PowerFunction(1.7, 0);
        Function concatenation = new Concatenation(new Interval<Function>(0.0, true, 0.2, false, part1),
                new Interval<Function>(0.2, true, 0.5, false, part2), new Interval<Function>(0.5, true, 1.0, true, part3));
        System.out.println("part 1 " + part1.getDescription());
        System.out.println("part 2 " + part2.getDescription());
        System.out.println("part 3 " + part3.getDescription());
        System.out.println("f  " + concatenation.getDescription());
        Function derivative = concatenation.getDerivative();
        System.out.println("f' " + derivative.getDescription());
        for (int step = 0; step <= 50; step++)
        {
            double x = 0.02 * step;
            System.out.println(String.format("x=%5.2f: f=%10.2f; f'=%10.2f", x, concatenation.get(x), derivative.get(x)));
        }

        System.out.println("\nBuild the product of two simple polynomials");
        Function p1 = new Sum(new PowerFunction(3, 2), new PowerFunction(2, 1), new Constant(5)); // 3*x^2+2*x+5
        System.out.println("polynomial 1: " + p1.getDescription());
        Function p2 = new Sum(new PowerFunction(2, 2), new PowerFunction(4, 1), new Constant(7)); // 2*x^2+4*x+7
        System.out.println("polynomial 2: " + p2.getDescription());
        Function product = new Product(p1, p2);
        System.out.println("product (f): " + product.getDescription());
        deriv = product.getDerivative();
        System.out.println("f':          " + deriv.getDescription());
        product.getDerivative();
        deriv2 = deriv.getDerivative();
        System.out.println("f'':         " + deriv2.getDescription());
        deriv3 = deriv2.getDerivative();
        System.out.println("f''':        " + deriv3.getDescription());
        deriv4 = deriv3.getDerivative();
        System.out.println("f'''':       " + deriv4.getDescription());
        deriv5 = deriv4.getDerivative();
        System.out.println("f''''':      " + deriv5.getDescription());
        Function deriv6 = deriv5.getDerivative();
        System.out.println("f'''''':     " + deriv6.getDescription());

        System.out.println("\nDifferentiate a sine function");
        Function f = new Sine(2, 3, 0);
        System.out.println("f:           " + f.getDescription());
        deriv = f.getDerivative();
        System.out.println("f':          " + deriv.getDescription());
        product.getDerivative();
        deriv2 = deriv.getDerivative();
        System.out.println("f'':         " + deriv2.getDescription());
        deriv3 = deriv2.getDerivative();
        System.out.println("f''':        " + deriv3.getDescription());
        deriv4 = deriv3.getDerivative();
        System.out.println("f'''':       " + deriv4.getDescription());
        deriv5 = deriv4.getDerivative();
        System.out.println("f''''':      " + deriv5.getDescription());
        deriv6 = deriv5.getDerivative();
        System.out.println("f'''''':     " + deriv6.getDescription());

        System.out.println("\nDifferentiate a simpler sine function");
        f = new Sine(2, 1, 0);
        System.out.println("f:           " + f.getDescription());
        deriv = f.getDerivative();
        System.out.println("f':          " + deriv.getDescription());
        product.getDerivative();
        deriv2 = deriv.getDerivative();
        System.out.println("f'':         " + deriv2.getDescription());
        deriv3 = deriv2.getDerivative();
        System.out.println("f''':        " + deriv3.getDescription());
        deriv4 = deriv3.getDerivative();
        System.out.println("f'''':       " + deriv4.getDescription());
        deriv5 = deriv4.getDerivative();
        System.out.println("f''''':      " + deriv5.getDescription());
        System.out.println("f and f'''' do test equal (there is no resulting rounding error)");
        assertEquals(f, deriv4);

        System.out.println("\nIncorporate a factor in a PowerFunction");
        p1 = new Constant(5);
        System.out.println("part 1:     " + p1.getDescription());
        p2 = new PowerFunction(3, 4);
        System.out.println("part 2:     " + p2.getDescription());
        f = new Product(p1, p2);
        System.out.println("combined:   " + f.getDescription());
        f = f.simplify();
        System.out.println("simplified: " + f.getDescription());

    }

}
