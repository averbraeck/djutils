package org.djutils.math.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.SortedSet;

import org.junit.jupiter.api.Test;

/**
 * Test the Product class.
 * <p>
 * Copyright (c) 2024-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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

        Product p = new Product(new Sine(1, 2, 3), new Power(2, 3));
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
        assertEquals(123, f.apply(10) / p.apply(10), 0.0001, "scaleBy works");
        assertTrue(p == p.scaleBy(1.0), "scaleBy 1.0 return original");
        assertEquals(Constant.ZERO, p.scaleBy(0.0), "scaleBy 0.0 return ZERO");

        Product p2 = new Product(new Constant(5), p);
        assertEquals(5, p2.apply(10) / p.apply(10), 0.0001, "embedded product");

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

        p = new Product(new Sine(1, 2, 3), new Sine(1, 3, 1));
        p2 = new Product(new Sine(1, 2, 3));
        assertTrue(p.compareTo(p2) > 0);
        assertTrue(p2.compareTo(p) < 0);
        p2 = new Product(new Sine(1, 2, 3), new Power(2, 2));
        assertTrue(p.compareTo(p2) > 0);
        assertTrue(p2.compareTo(p) < 0);
        p2 = new Product(new Sine(1, 2, 3), new Sine(1, 3, 1));
        assertEquals(0, p.compareTo(p2));

        Product product = new Product(new Logarithm(), new Power(1, 2));
        assertEquals(KnotReport.NONE, product.getKnotReport(new Interval<String>(0.5, true, 10, true, "string")), "zero knots");
        assertEquals(KnotReport.KNOWN_FINITE, product.getKnotReport(new Interval<String>(0.0, true, 10, true, "string")),
                "one knot");
        assertEquals(1, product.getKnots(new Interval<String>(0.0, true, 10, true, "string")).size(), "one knot");
        assertEquals(0.0, product.getKnots(new Interval<String>(0.0, true, 10, true, "string")).first(), "one knot at 0.0");
        assertEquals(KnotReport.KNOWN_INFINITE, product.getKnotReport(new Interval<String>(-0.5, true, 10, true, "string")),
                "infinite");
        try
        {
            product.getKnots(new Interval<String>(-0.5, true, 10, true, "string"));
            fail("infinite set should throw an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
        product = new Product(new Logarithm(),
                new Concatenation(new Interval<MathFunction>(-1.0, true, 0.5, false, new Constant(1))));
        // System.out.println(sum); // Can't simplify this one
        assertEquals(KnotReport.KNOWN_FINITE, product.getKnotReport(new Interval<String>(0.0, true, 0.5, true, "string")),
                "two knots");
        SortedSet<Double> knots = product.getKnots(new Interval<String>(0.0, true, 0.5, true, "string"));
        // System.out.println(knots);
        assertEquals(2, knots.size(), "two knots");
        assertEquals(0.0, knots.first(), "first is at 0.0");
        assertEquals(0.5, knots.last(), "last is at 0.5");
        product.getKnotReport(new Interval<String>(-0.5, true, 0.5, true, "string"));
        assertEquals(KnotReport.KNOWN_INFINITE, product.getKnotReport(new Interval<String>(-0.5, true, 0.5, true, "string")),
                "infinite knots");
        try
        {
            product.getKnots(new Interval<String>(0.0, true, 1.0, true, "string"));
            fail("Infinitely many knots should have thrown an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
        assertEquals(KnotReport.KNOWN_INFINITE, product.getKnotReport(new Interval<String>(-1.0, true, 1.0, true, "string")),
                "infinite knots");
        try
        {
            product.getKnots(new Interval<String>(0.0, true, 1.0, true, "string"));
            fail("Infinitely many knots should have thrown an UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // Ignore expected exception
        }
    }
}
