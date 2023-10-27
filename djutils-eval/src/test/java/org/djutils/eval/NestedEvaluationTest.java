package org.djutils.eval;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.SIScalar;
import org.junit.jupiter.api.Test;

/**
 * NestedEvaluationTest.java.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class NestedEvaluationTest
{

    /**
     * Test recursive call to the evaluator.
     */
    @Test
    public void nestedEvaluation()
    {
        Eval eval = new Eval();
        Map<String, Object> map = Map.of("param1", "{param2}", "param2", Dimensionless.instantiateSI(0.3));
        eval.setRetrieveValue(new RetrieveValue()
        {
            @Override
            public Object lookup(final String name)
            {
                Object value = map.get(name);
                if (value instanceof String && value.toString().startsWith("{"))
                {
                    return eval.evaluate(value.toString().substring(1, value.toString().length() - 1));
                }
                return value;
            }
        });
        //System.out.println(eval.evaluate("1.0 - param2")); // 0,70000000
        //System.out.println(eval.evaluate("1.0 - param1")); // RuntimeException: Stack empty at position 6
        assertEquals(0.7,  ((SIScalar) eval.evaluate("1.0 - param1")).si, 0.000001, "Nested call succeeded");
    }
}
