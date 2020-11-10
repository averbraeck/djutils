package org.djutils.draw.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.djutils.draw.d0.Point;
import org.djutils.draw.d0.Point2d;
import org.junit.Test;

/**
 * Transform2dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Transform2dTest
{
    /**
     * Test the matrix / vector multiplication.
     */
    @Test
    public void testMatrixMultiplication()
    {
        double[] mA = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        double[] mB = new double[] { 2, 1, 0, 2, 4, 3, 3, 1, 2 };
        double[] mAmB = Transform2d.mulMatMat(mA, mB);
        double[] expected = new double[] { 15, 12, 12, 36, 30, 27, 57, 48, 42 };
        for (int i = 0; i < 9; i++)
        {
            if (mAmB[i] != expected[i])
            {
                fail(String.format("difference MA x MB at %d: expected %f, was: %f", i, expected[i], mAmB[i]));
            }
        }

        double[] m = new double[] { 1, 4, 2, 5, 3, 1, 4, 2, 5 };
        double[] v = new double[] { 2, 5, 1 };
        double[] mv = Transform2d.mulMatVec(m, v);
        double[] ev = new double[] { 24, 26, 23 };
        for (int i = 0; i < 3; i++)
        {
            if (mv[i] != ev[i])
            {
                fail(String.format("difference M x V at %d: expected %f, was: %f", i, ev[i], mv[i]));
            }
        }

        v = new double[] { 1, 2 };
        mv = Transform2d.mulMatVec2(m, v);
        ev = new double[] { 11, 12 };
        for (int i = 0; i < 2; i++)
        {
            if (mv[i] != ev[i])
            {
                fail(String.format("difference M x V3 at %d: expected %f, was: %f", i, ev[i], mv[i]));
            }
        }
    }

    /**
     * Test that the constructor creates an Identity matrix.
     */
    @Test
    public void testConstructor()
    {
        // TODO: decide whether the internal (flattened) matrix should be visible at all, or add a getter
        Transform2d t = new Transform2d();
        assertEquals("matrix contians 9 values", 9, t.mat.length);
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 3; col++)
            {
                int e = row == col ? 1 : 0;
                assertEquals("Value in identity matrix matches", e, t.mat[3 * row + col], 0);
            }
        }
    }

    /**
     * Test the translate, scale, rotate, shear, reflectX and reflectY methods.
     */
    @Test
    public void testTranslateScaleRotateAndShear()
    {
        Transform2d t;
        // Test time grows (explodes) with the 4th power of the length of offsets.
        double[] offsets = new double[] { -100000, -100, -10, -3, -1, -0.3, -0.1, 0, 0.1, 0.3, 1, 3, 10, 100, 100000 };
        for (double dx : offsets)
        {
            for (double dy : offsets)
            {
                // Translate defined with a double[]
                t = new Transform2d();
                t.translate(dx, dy);
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        Point p = t.transform(new Point2d(px, py));
                        assertEquals("transformed x matches", px + dx, p.getX(), 0.001);
                        assertEquals("transformed y matches", py + dy, p.getY(), 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("transformed x matches", px + dx, result[0], 0.001);
                        assertEquals("transformed y matches", py + dy, result[1], 0.001);
                    }
                }
                // Translate defined with a Point
                t = new Transform2d();
                t.translate(new Point2d(dx, dy));
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        Point p = t.transform(new Point2d(px, py));
                        assertEquals("transformed x matches", px + dx, p.getX(), 0.001);
                        assertEquals("transformed y matches", py + dy, p.getY(), 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("transformed x matches", px + dx, result[0], 0.001);
                        assertEquals("transformed y matches", py + dy, result[1], 0.001);
                    }
                }
                // Scale
                t = new Transform2d();
                t.scale(dx, dy);
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        Point p = t.transform(new Point2d(px, py));
                        assertEquals("scaled x matches", px * dx, p.getX(), 0.001);
                        assertEquals("scaled y matches", py * dy, p.getY(), 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("scaled x matches", px * dx, result[0], 0.001);
                        assertEquals("scaled y matches", py * dy, result[1], 0.001);
                    }
                }
                // Shear
                t = new Transform2d();
                t.shear(dx, dy);
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        Point p = t.transform(new Point2d(px, py));
                        assertEquals("sheared x matches", px + py * dx, p.getX(), 0.001);
                        assertEquals("sheared y matches", py + px * dy, p.getY(), 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("sheared x matches", px + py * dx, result[0], 0.001);
                        assertEquals("sheared y matches", py + px * dy, result[1], 0.001);
                    }
                }
            }
            // Translate defined with a double[]
            t = new Transform2d();
            t.rotate(dx);
            double sine = Math.sin(dx);
            double cosine = Math.cos(dx);
            for (double px : offsets)
            {
                for (double py : offsets)
                {
                    Point p = t.transform(new Point2d(px, py));
                    assertEquals("transformed x matches", px * cosine - py * sine, p.getX(), 0.001);
                    assertEquals("transformed y matches", py * cosine + px * sine, p.getY(), 0.001);
                    double[] result = t.transform(new double[] { px, py });
                    assertEquals("transformed x matches", px * cosine - py * sine, result[0], 0.001);
                    assertEquals("transformed y matches", py * cosine + px * sine, result[1], 0.001);
                }
            }
        }
        // ReflectX
        t = new Transform2d();
        t.reflectX();
        for (double px : offsets)
        {
            for (double py : offsets)
            {
                Point p = t.transform(new Point2d(px, py));
                assertEquals("x-reflected x matches", -px, p.getX(), 0.001);
                assertEquals("x-reflected y  matches", py, p.getY(), 0.001);
                double[] result = t.transform(new double[] { px, py });
                assertEquals("x-reflected x  matches", -px, result[0], 0.001);
                assertEquals("x-reflected y  matches", py, result[1], 0.001);
            }
        }
        // ReflectY
        t = new Transform2d();
        t.reflectY();
        for (double px : offsets)
        {
            for (double py : offsets)
            {
                Point p = t.transform(new Point2d(px, py));
                assertEquals("y-reflected x matches", px, p.getX(), 0.001);
                assertEquals("y-reflected y  matches", -py, p.getY(), 0.001);
                double[] result = t.transform(new double[] { px, py });
                assertEquals("y-reflected x  matches", px, result[0], 0.001);
                assertEquals("y-reflected y  matches", -py, result[1], 0.001);
            }
        }
    }

}
