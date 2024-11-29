package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Test the Product class.
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
public class ProductTest
{
    /**
     * Test the Product class.
     */
    @Test
    public void productTest()
    {
        try
        {
            new Product();
            fail("empty argument list should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        Product p = new Product(new Sine(1, 2, 3), new PowerFunction(2, 3));
        try
        {
            p.compareWithinSubType(Constant.ONE);
            fail("compareWithinSubType should throw an IllegalArgumentException for incompatible sub type");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        assertEquals(100, p.sortPriority(), "sorting priority of Product is 100");
        MathFunction f = p.scaleBy(123);
        assertEquals(123, f.get(10) / p.get(10), 0.0001, "scaleBy works");
        assertTrue(p == p.scaleBy(1.0), "scaleBy 1.0 return original");
        assertEquals(Constant.ZERO, p.scaleBy(0.0), "scaleBy 0.0 return ZERO");

        Product p2 = new Product(new Constant(5), p);
        assertEquals(5, p2.get(10) / p.get(10), 0.0001, "embedded product");

        p2 = new Product(Constant.ZERO, p);
        f = p2.simplify();
        assertEquals(Constant.ZERO, f, "product with ZERO in it should simplify to ZERO");
        
        p = new Product(new Constant(3), new Constant(6));
        f = p.simplify();
        assertEquals(new Constant(18), f, "product of constants should simplify to a single constant");
        p = new Product(new Constant(2), new Constant(0.5));
        f = p.simplify();
        assertEquals(Constant.ONE, f, "product of constants should simplify to a single constant");
        p = new Product(new Constant(2));
        p2 = new Product(new Constant(4));
        assertNotEquals(p.hashCode(), p2.hashCode(), "hash code takes the factor(s) into account");
        assertFalse(p.equals(null), "not equal to null");
    }
}
