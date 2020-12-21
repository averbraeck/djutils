package org.djutils.draw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
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
     * Test the translate, scale, rotate, shear and reflect methods.
     */
    @Test
    public void testTranslateScaleRotateShearAndReflect()
    {
        Transform2d t;
        // Test time grows (explodes) with the 4th power of the length of values.
        double[] values = new double[] { -100000, -100, -10, -3, -1, -0.3, -0.1, 0, 0.1, 0.3, 1, 3, 10, 100, 100000 };
        for (double dx : values)
        {
            for (double dy : values)
            {
                // Translate defined with a double[]
                t = new Transform2d();
                t.translate(dx, dy);
                for (double px : values)
                {
                    for (double py : values)
                    {
                        Point2d p = t.transform(new Point2d(px, py));
                        assertEquals("translated x matches", px + dx, p.x, 0.001);
                        assertEquals("translated y matches", py + dy, p.y, 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("translated x matches", px + dx, result[0], 0.001);
                        assertEquals("translated y matches", py + dy, result[1], 0.001);
                    }
                }
                // Translate defined with a Point
                t = new Transform2d();
                t.translate(new Point2d(dx, dy));
                for (double px : values)
                {
                    for (double py : values)
                    {
                        Point2d p = t.transform(new Point2d(px, py));
                        assertEquals("transformed x matches", px + dx, p.x, 0.001);
                        assertEquals("transformed y matches", py + dy, p.y, 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("transformed x matches", px + dx, result[0], 0.001);
                        assertEquals("transformed y matches", py + dy, result[1], 0.001);
                    }
                }
                // Scale
                t = new Transform2d();
                t.scale(dx, dy);
                for (double px : values)
                {
                    for (double py : values)
                    {
                        Point2d p = t.transform(new Point2d(px, py));
                        assertEquals("scaled x matches", px * dx, p.x, 0.001);
                        assertEquals("scaled y matches", py * dy, p.y, 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("scaled x matches", px * dx, result[0], 0.001);
                        assertEquals("scaled y matches", py * dy, result[1], 0.001);
                    }
                }
                // Shear
                t = new Transform2d();
                t.shear(dx, dy);
                for (double px : values)
                {
                    for (double py : values)
                    {
                        Point2d p = t.transform(new Point2d(px, py));
                        assertEquals("sheared x matches", px + py * dx, p.x, 0.001);
                        assertEquals("sheared y matches", py + px * dy, p.y, 0.001);
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals("sheared x matches", px + py * dx, result[0], 0.001);
                        assertEquals("sheared y matches", py + px * dy, result[1], 0.001);
                    }
                }
            }
            // Rotate (using dx as angle)
            t = new Transform2d();
            t.rotation(dx);
            double sine = Math.sin(dx);
            double cosine = Math.cos(dx);
            for (double px : values)
            {
                for (double py : values)
                {
                    Point2d p = t.transform(new Point2d(px, py));
                    assertEquals("rotated x matches", px * cosine - py * sine, p.x, 0.001);
                    assertEquals("rotated y matches", py * cosine + px * sine, p.y, 0.001);
                    double[] result = t.transform(new double[] { px, py });
                    assertEquals("rotated x matches", px * cosine - py * sine, result[0], 0.001);
                    assertEquals("rotated y matches", py * cosine + px * sine, result[1], 0.001);
                }
            }
        }
        // ReflectX
        t = new Transform2d();
        t.reflectX();
        for (double px : values)
        {
            for (double py : values)
            {
                Point2d p = t.transform(new Point2d(px, py));
                assertEquals("x-reflected x matches", -px, p.x, 0.001);
                assertEquals("x-reflected y  matches", py, p.y, 0.001);
                double[] result = t.transform(new double[] { px, py });
                assertEquals("x-reflected x  matches", -px, result[0], 0.001);
                assertEquals("x-reflected y  matches", py, result[1], 0.001);
            }
        }
        // ReflectY
        t = new Transform2d();
        t.reflectY();
        for (double px : values)
        {
            for (double py : values)
            {
                Point2d p = t.transform(new Point2d(px, py));
                assertEquals("y-reflected x matches", px, p.x, 0.001);
                assertEquals("y-reflected y  matches", -py, p.y, 0.001);
                double[] result = t.transform(new double[] { px, py });
                assertEquals("y-reflected x  matches", px, result[0], 0.001);
                assertEquals("y-reflected y  matches", -py, result[1], 0.001);
            }
        }
    }

    /**
     * Test the transform method.
     */
    @Test
    public void transformTest()
    {
        Transform2d reflectionX = new Transform2d().reflectX();
        Transform2d reflectionY = new Transform2d().reflectY();
        // Test time explodes with the 6th power of the length of this array
        double[] values = new double[] { -100, -0.1, 0, 0.01, 1, 100 };
        for (double translateX : values)
        {
            for (double translateY : values)
            {
                Transform2d translation = new Transform2d().translate(translateX, translateY);
                for (double scaleX : values)
                {
                    for (double scaleY : values)
                    {
                        Transform2d scaling = new Transform2d().scale(scaleX, scaleY);
                        for (double angle : new double[] { -2, 0, 0.5 })
                        {
                            Transform2d rotation = new Transform2d().rotation(angle);
                            for (double shearX : values)
                            {
                                for (double shearY : values)
                                {
                                    Transform2d t = new Transform2d().translate(translateX, translateY).scale(scaleX, scaleY)
                                            .rotation(angle).shear(shearX, shearY);
                                    Transform2d tReflectX = new Transform2d().reflectX().translate(translateX, translateY)
                                            .scale(scaleX, scaleY).rotation(angle).shear(shearX, shearY);
                                    Transform2d tReflectY = new Transform2d().reflectY().translate(translateX, translateY)
                                            .scale(scaleX, scaleY).rotation(angle).shear(shearX, shearY);
                                    Transform2d shearing = new Transform2d().shear(shearX, shearY);
                                    for (double px : values)
                                    {
                                        for (double py : values)
                                        {
                                            Point2d p = new Point2d(px, py);
                                            Point2d tp = t.transform(p);
                                            Point2d chainP = translation
                                                    .transform(scaling.transform(rotation.transform(shearing.transform(p))));
                                            assertEquals("X", chainP.x, tp.x, 0.0000001);
                                            assertEquals("Y", chainP.y, tp.y, 0.0000001);
                                            tp = tReflectX.transform(p);
                                            Point2d chainPReflectX = reflectionX.transform(chainP);
                                            assertEquals("RX X", chainPReflectX.x, tp.x, 0.0000001);
                                            assertEquals("RX Y", chainPReflectX.y, tp.y, 0.0000001);
                                            tp = tReflectY.transform(p);
                                            Point2d chainPReflectY = reflectionY.transform(chainP);
                                            assertEquals("RY X", chainPReflectY.x, tp.x, 0.0000001);
                                            assertEquals("RY Y", chainPReflectY.y, tp.y, 0.0000001);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Test transformation of a bounding rectangle.
     */
    @Test
    public void transformBounds2dTest()
    {
        double[] values = new double[] { -100, 0.1, 0, 0.1, 100 };
        double[] sizes = new double[] { 0, 10, 100 };
        Transform2d t = new Transform2d().rotation(0.4).reflectX().scale(0.5, 1.5).shear(2, 3).translate(123, 456);
        // System.out.println(t);
        for (double x : values)
        {
            for (double y : values)
            {
                for (double xSize : sizes)
                {
                    for (double ySize : sizes)
                    {
                        Bounds2d bb = new Bounds2d(x, x + xSize, y, y + ySize);
                        Point2d[] points = new Point2d[] { new Point2d(x, y), new Point2d(x + xSize, y),
                                new Point2d(x, y + ySize), new Point2d(x + xSize, y + ySize) };
                        Point2d[] transformedPoints = new Point2d[4];
                        for (int i = 0; i < points.length; i++)
                        {
                            transformedPoints[i] = t.transform(points[i]);
                        }
                        Bounds2d expected = new Bounds2d(Arrays.stream(transformedPoints).iterator());
                        Bounds2d got = t.transform(bb);
                        assertEquals("bb minX", expected.getMinX(), got.getMinX(), 0.0001);
                        assertEquals("bb maxX", expected.getMaxX(), got.getMaxX(), 0.0001);
                        assertEquals("bb minY", expected.getMinY(), got.getMinY(), 0.0001);
                        assertEquals("bb maxY", expected.getMaxY(), got.getMaxY(), 0.0001);
                    }
                }
            }
        }
    }

    /**
     * Check that toString returns something descriptive.
     */
    @Test
    public void toStringTest()
    {
        assertTrue("toString returns something descriptive", new Transform2d().toString().startsWith("Transform2d "));
    }

}
