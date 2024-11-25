package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test that derivative of a function matches the local slope.
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
public class DerivativeTest
{
    /** The tests. */
    private static Scenario[] tests = new Scenario[] {new Scenario(new Sine(1, 1, 0), -30, +30, 61, 0.0001, 0.0001),
            new Scenario(new Sine(2, 3, 4), -10, 10, 61, 0.0001, 0.001),
            new Scenario(new PowerFunction(3, 5), -10, 10, 61, 0.0001, 5),
            new Scenario(new PowerFunction(2, 0.5), 1, 20, 20, 0.0001, 0.0001),
            new Scenario(new PowerFunction(2, -0.5), 1, 20, 20, 0.0001, 0.0001),
            new Scenario(new Sum(new PowerFunction(1, 2), new PowerFunction(5, 1)), -20, 20, 41, 0.0001, 0.001),
            new Scenario(new Product(new Sine(2, 2, 1), new PowerFunction(1, 2)), -20, 20, 41, 0.0001, 0.2)};

    /**
     * Test scenario.
     * @param mathFunction the MathFunction to run the tests on
     * @param minimum lowest <code>x</code> value of to test
     * @param maximum highest <code>x</code> value of to test
     * @param steps number of points along the range <code>[lowest, highest]</code> to perform the test
     * @param delta range around <code>x</code> test point to verify derivative
     * @param epsilon range around <code>f(x)</code> test point to verify derivative
     */
    record Scenario(MathFunction mathFunction, double minimum, double maximum, int steps, double delta, double epsilon)
    {
    }

    /**
     * Run all the scenarios.
     */
    @Test
    public void runScenarios()
    {
        for (Scenario scenario : tests)
        {
            int steps = scenario.steps();
            MathFunction function = scenario.mathFunction();
            // System.out.println(function.getDescription());
            MathFunction derivative = function.getDerivative();
            // System.out.println(derivative.getDescription());
            String functionName = function.toString();
            for (int step = 0; step < scenario.steps(); step++)
            {
                double x = steps == 1 ? scenario.minimum()
                        : (scenario.minimum() + (scenario.maximum() - scenario.minimum()) * 1.0 * step / (steps - 1));
                double fX = function.get(x);
                for (int side : new int[] {1, -1})
                {
                    if (side > 0 && step < steps - 1 || side < 0 && step > 0)
                    {
                        double dX = x + scenario.delta() * side;
                        double fDX = function.get(dX);
                        double actualSlope = (fDX - fX) / (dX - x);
                        double expectedSlope = derivative.get(x);

                        if (Math.abs(actualSlope - expectedSlope) >= scenario.epsilon())
                        {
                            System.out.println(functionName + " (deriv " + derivative.toString() + ") f(" + dX + ")=" + fX
                                    + " - f(" + x + ")=" + fX + " = " + (fDX - fX) + "; actual slope=" + actualSlope
                                    + ", expected slope=" + expectedSlope);
                        }
                        assertEquals(expectedSlope, actualSlope, scenario.epsilon(), functionName);
                    }
                }
            }
        }
    }
}
