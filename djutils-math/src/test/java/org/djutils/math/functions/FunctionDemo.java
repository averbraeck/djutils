package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

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
        MathFunction function =
                new Sum(new PowerFunction(2, 3), new PowerFunction(5, 2), new PowerFunction(8, 1), new PowerFunction(3, 0));
        MathFunction deriv1 = function.getDerivative();
        MathFunction deriv2 = deriv1.getDerivative();
        MathFunction deriv3 = deriv2.getDerivative();
        MathFunction deriv4 = deriv3.getDerivative();
        MathFunction deriv5 = deriv4.getDerivative();
        System.out.println("f      " + function);
        System.out.println("f'     " + deriv1);
        System.out.println("f''    " + deriv2);
        System.out.println("f'''   " + deriv3);
        System.out.println("f''''  " + deriv4);
        System.out.println("f''''' " + deriv5); // Is identical to deriv4
        System.out.println("Check if f'''' is equal to f''''' : " + deriv4.equals(deriv5));
        for (int step = -10; step <= 10; step++)
        {
            double x = 0.5 * step;
            System.out.println(String.format(
                    "x=%5.2f: f(x)=%8.2f; f'(x)=%10.2f; " + "f''(x)=%8.2f; f'''(x)=%8.2f; " + "f''''(x)=%8.2f; f'''''(x)=%8.2f",
                    x, function.get(x), deriv1.get(x), deriv2.get(x), deriv3.get(x), deriv4.get(x), deriv5.get(x)));
        }

        System.out.println("\nBuild a continuous piecewise linear function; each linear piece is a power function");
        MathFunction part1 = new Sum(new Constant(1.0), new PowerFunction(0.5, 1));
        MathFunction part2 = new Sum(new Constant(1.1 - 0.4), new PowerFunction(2.0, 1));
        MathFunction part3 = new PowerFunction(1.7, 0);
        MathFunction concatenation = new Concatenation(new Interval<MathFunction>(0.0, true, 0.2, false, part1),
                new Interval<MathFunction>(0.2, true, 0.5, false, part2),
                new Interval<MathFunction>(0.75, true, 1.0, true, part3));
        System.out.println("part 1 " + part1);
        System.out.println("part 2 " + part2);
        System.out.println("part 3 " + part3);
        System.out.println("f  " + concatenation);
        MathFunction derivative = concatenation.getDerivative();
        System.out.println("f' " + derivative);
        for (int step = 0; step <= 50; step++)
        {
            double x = 0.02 * step;
            System.out.println(String.format("x=%5.2f: f=%10.2f; f'=%10.2f", x, concatenation.get(x), derivative.get(x)));
        }
        System.out.println("Build the same concatentation the easy way (without the NaN section)");
        SortedMap<Double, Double> map = new TreeMap<>();
        map.put(0.0, 1.0);
        map.put(0.2, 1.1);
        map.put(0.5,  1.7);
        map.put(1.0, 1.7);
        concatenation = Concatenation.continuousPiecewiseLinear(map);
        System.out.println(concatenation);

        System.out.println("\nBuild the product of two simple polynomials");
        MathFunction p1 = new Sum(new PowerFunction(3, 2), new PowerFunction(2, 1), new Constant(5)); // 3*x^2+2*x+5
        System.out.println("polynomial 1: " + p1);
        MathFunction p2 = new Sum(new PowerFunction(2, 2), new PowerFunction(4, 1), new Constant(7)); // 2*x^2+4*x+7
        System.out.println("polynomial 2: " + p2);
        MathFunction product = new Product(p1, p2);
        System.out.println("product (f): " + product);
        deriv1 = product.getDerivative();
        System.out.println("f':          " + deriv1);
        product.getDerivative();
        deriv2 = deriv1.getDerivative();
        System.out.println("f'':         " + deriv2);
        deriv3 = deriv2.getDerivative();
        System.out.println("f''':        " + deriv3);
        deriv4 = deriv3.getDerivative();
        System.out.println("f'''':       " + deriv4);
        deriv5 = deriv4.getDerivative();
        System.out.println("f''''':      " + deriv5);
        MathFunction deriv6 = deriv5.getDerivative();
        System.out.println("f'''''':     " + deriv6);

        System.out.println("\nDifferentiate a sine function");
        MathFunction f = new Sine(2, 3, 0);
        System.out.println("f:           " + f);
        deriv1 = f.getDerivative();
        System.out.println("f':          " + deriv1);
        product.getDerivative();
        deriv2 = deriv1.getDerivative();
        System.out.println("f'':         " + deriv2);
        deriv3 = deriv2.getDerivative();
        System.out.println("f''':        " + deriv3);
        deriv4 = deriv3.getDerivative();
        System.out.println("f'''':       " + deriv4);
        deriv5 = deriv4.getDerivative();
        System.out.println("f''''':      " + deriv5);
        deriv6 = deriv5.getDerivative();
        System.out.println("f'''''':     " + deriv6);

        System.out.println("\nDifferentiate a simpler sine function");
        f = new Sine(2, 1, 0);
        System.out.println("f:           " + f);
        deriv1 = f.getDerivative();
        System.out.println("f':          " + deriv1);
        product.getDerivative();
        deriv2 = deriv1.getDerivative();
        System.out.println("f'':         " + deriv2);
        deriv3 = deriv2.getDerivative();
        System.out.println("f''':        " + deriv3);
        deriv4 = deriv3.getDerivative();
        System.out.println("f'''':       " + deriv4);
        deriv5 = deriv4.getDerivative();
        System.out.println("f''''':      " + deriv5);
        System.out.println("f and f'''' do test equal (there is no resulting rounding error)");
        assertEquals(f, deriv4);

        System.out.println("\nIncorporate a factor in a PowerFunction");
        p1 = new Constant(5);
        System.out.println("part 1:     " + p1);
        p2 = new PowerFunction(3, 4);
        System.out.println("part 2:     " + p2);
        f = new Product(p1, p2);
        System.out.println("combined:   " + f);
        f = f.simplify();
        System.out.println("simplified: " + f);

        System.out.println("\nadd together two polynomials");
        p1 = new Sum(new PowerFunction(3, 3), new PowerFunction(2, 2), new PowerFunction(1, 1), new PowerFunction(6, 0));
        System.out.println(p1);
        p2 = new Sum(new PowerFunction(2, 3), new PowerFunction(1, 2), new PowerFunction(5, 1), new PowerFunction(8, 0));
        System.out.println(p2);
        f = new Sum(p1, p2);
        System.out.println(f);

        System.out.println("\nmultiply some powers");
        p1 = new PowerFunction(3, 4);
        System.out.println(p1);
        p2 = new PowerFunction(2, 3);
        System.out.println(p2);
        f = new Product(p1, p2);
        System.out.println(f);
        f = f.simplify();
        System.out.println("simplified: " + f);

        System.out.println("\nadd two sines with the same frequency but different amplitude and 90 degrees different phase");
        p1 = new Sine(3, 10, 0);
        System.out.println("sine 1:     " + p1);
        p2 = new Sine(4, 10, Math.PI / 2);
        System.out.println("sine 2:     " + p2);
        f = new Sum(p1, p2);
        System.out.println("sum:        " + f);
        f = f.simplify();
        System.out.println("simplified: " + f);

        System.out.println("\nadd two sines with the same frequency but different amplitude and 180 degrees different phase");
        p1 = new Sine(3, 10, 0);
        System.out.println("sine 1:     " + p1);
        p2 = new Sine(4, 10, Math.PI);
        System.out.println("sine 2:     " + p2);
        f = new Sum(p1, p2);
        System.out.println("sum:        " + f);
        f = f.simplify();
        System.out.println("simplified: " + f);

        System.out.println("\nadd two sines with the same frequency same amplitude and 180 degrees different phase");
        p1 = new Sine(3, 10, 0);
        System.out.println("sine 1:     " + p1);
        p2 = new Sine(3, 10, Math.PI);
        System.out.println("sine 2:     " + p2);
        f = new Sum(p1, p2);
        System.out.println("sum:        " + f);
        f = f.simplify();
        System.out.println("simplified: " + f);
        System.out.println("obviously; these don't quite cancel out...");

        System.out.println("\nchain sine as argument of square function");
        p1 = new Sine(1, 1, 0);
        System.out.println("sine: " + p1);
        p2 = new PowerFunction(p1, 1, 2);
        System.out.println("f:    " + p2);
        deriv1 = p2.getDerivative();
        System.out.println("f':   " + deriv1);
        deriv2 = deriv1.getDerivative();
        System.out.println("f''   " + deriv2);
        deriv3 = deriv2.getDerivative();
        System.out.println("f'''  " + deriv3);
        for (int step = -10; step <= 10; step++)
        {
            double x = 0.2 * step;
            System.out.println(String.format("x=%5.1f: f(x)=%10.5f; f'(x)=%10.5f; f''(x)=%10.5f; f'''(x)=%10.5f", x, p2.get(x),
                    deriv1.get(x), deriv2.get(x), deriv3.get(x)));
        }
    }

}
