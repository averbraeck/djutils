package org.djutils.draw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * Transform2dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        assertEquals(9, t.getMat().length, "matrix contians 9 values");
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 3; col++)
            {
                int e = row == col ? 1 : 0;
                assertEquals(e, t.getMat()[3 * row + col], 0, "Value in identity matrix matches");
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
                        assertEquals(px + dx, p.x, 0.001, "translated x matches");
                        assertEquals(py + dy, p.y, 0.001, "translated y matches");
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals(px + dx, result[0], 0.001, "translated x matches");
                        assertEquals(py + dy, result[1], 0.001, "translated y matches");
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
                        assertEquals(px + dx, p.x, 0.001, "transformed x matches");
                        assertEquals(py + dy, p.y, 0.001, "transformed y matches");
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals(px + dx, result[0], 0.001, "transformed x matches");
                        assertEquals(py + dy, result[1], 0.001, "transformed y matches");
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
                        assertEquals(px * dx, p.x, 0.001, "scaled x matches");
                        assertEquals(py * dy, p.y, 0.001, "scaled y matches");
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals(px * dx, result[0], 0.001, "scaled x matches");
                        assertEquals(py * dy, result[1], 0.001, "scaled y matches");
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
                        assertEquals(px + py * dx, p.x, 0.001, "sheared x matches");
                        assertEquals(py + px * dy, p.y, 0.001, "sheared y matches");
                        double[] result = t.transform(new double[] { px, py });
                        assertEquals(px + py * dx, result[0], 0.001, "sheared x matches");
                        assertEquals(py + px * dy, result[1], 0.001, "sheared y matches");
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
                    assertEquals(px * cosine - py * sine, p.x, 0.001, "rotated x matches");
                    assertEquals(py * cosine + px * sine, p.y, 0.001, "rotated y matches");
                    double[] result = t.transform(new double[] { px, py });
                    assertEquals(px * cosine - py * sine, result[0], 0.001, "rotated x matches");
                    assertEquals(py * cosine + px * sine, result[1], 0.001, "rotated y matches");
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
                assertEquals(-px, p.x, 0.001, "x-reflected x matches");
                assertEquals(py, p.y, 0.001, "x-reflected y  matches");
                double[] result = t.transform(new double[] { px, py });
                assertEquals(-px, result[0], 0.001, "x-reflected x  matches");
                assertEquals(py, result[1], 0.001, "x-reflected y  matches");
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
                assertEquals(px, p.x, 0.001, "y-reflected x matches");
                assertEquals(-py, p.y, 0.001, "y-reflected y  matches");
                double[] result = t.transform(new double[] { px, py });
                assertEquals(px, result[0], 0.001, "y-reflected x  matches");
                assertEquals(-py, result[1], 0.001, "y-reflected y  matches");
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
                                            assertEquals(chainP.x, tp.x, 0.0000001, "X");
                                            assertEquals(chainP.y, tp.y, 0.0000001, "Y");
                                            tp = tReflectX.transform(p);
                                            Point2d chainPReflectX = reflectionX.transform(chainP);
                                            assertEquals(chainPReflectX.x, tp.x, 0.0000001, "RX X");
                                            assertEquals(chainPReflectX.y, tp.y, 0.0000001, "RX Y");
                                            tp = tReflectY.transform(p);
                                            Point2d chainPReflectY = reflectionY.transform(chainP);
                                            assertEquals(chainPReflectY.x, tp.x, 0.0000001, "RY X");
                                            assertEquals(chainPReflectY.y, tp.y, 0.0000001, "RY Y");
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
                        assertEquals(expected.getMinX(), got.getMinX(), 0.0001, "bb minX");
                        assertEquals(expected.getMaxX(), got.getMaxX(), 0.0001, "bb maxX");
                        assertEquals(expected.getMinY(), got.getMinY(), 0.0001, "bb minY");
                        assertEquals(expected.getMaxY(), got.getMaxY(), 0.0001, "bb maxY");
                    }
                }
            }
        }
    }

    /**
     * Reproducible test of multiple transformations on a bounding rectangle.
     */
    @Test
    public void testBoundingRectangle2d()
    {
        Bounds2d bounds = new Bounds2d(-4, 4, -4, 4);

        // identical transformation
        Transform2d transform = new Transform2d();
        Bounds2d b = transform.transform(bounds);
        testBounds2d(b, -4, 4, -4, 4);

        // translate x, y
        transform = new Transform2d();
        transform.translate(20, 10);
        b = transform.transform(bounds);
        testBounds2d(b, 20 - 4, 20 + 4, 10 - 4, 10 + 4);

        // rotate 90 degrees (should be same)
        transform = new Transform2d();
        transform.rotation(Math.toRadians(90.0));
        b = transform.transform(bounds);
        testBounds2d(b, -4, 4, -4, 4);

        // rotate 45 degrees in the XY-plane
        transform = new Transform2d();
        transform.rotation(Math.toRadians(45.0));
        double d = 4.0 * Math.sqrt(2.0);
        b = transform.transform(bounds);
        testBounds2d(b, -d, d, -d, d);

        // rotate 45 degrees in the XY-plane and then translate to (10, 20)
        // note that to do FIRST rotation and THEN translation, the steps have to be built in the OPPOSITE order
        // since matrix multiplication operates from RIGHT to LEFT.
        transform = new Transform2d();
        transform.translate(10, 20);
        transform.rotation(Math.toRadians(45.0));
        b = transform.transform(bounds);
        testBounds2d(b, 10 - d, 10 + d, 20 - d, 20 + d);
    }

    /**
     * Check bounds values.
     * @param b Bounds2d; the box to test
     * @param minX double; expected value
     * @param maxX double; expected value
     * @param minY double; expected value
     * @param maxY double; expected value
     */
    private void testBounds2d(final Bounds2d b, final double minX, final double maxX, final double minY, final double maxY)
    {
        assertEquals(minX, b.getMinX(), 0.001);
        assertEquals(maxX, b.getMaxX(), 0.001);
        assertEquals(minY, b.getMinY(), 0.001);
        assertEquals(maxY, b.getMaxY(), 0.001);
    }

    /**
     * Check that toString returns something descriptive.
     */
    @Test
    public void toStringTest()
    {
        assertTrue(new Transform2d().toString().startsWith("Transform2d "), "toString returns something descriptive");
    }

    /**
     * Test the hashCode and equals methods.
     * @throws SecurityException if that happens uncaught; this test has failed
     * @throws NoSuchFieldException if that happens uncaught; this test has failed
     * @throws IllegalAccessException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException if that happens uncaught; this test has failed
     */
    @Test
    @SuppressWarnings({ "unlikely-arg-type" })
    public void testHashCodeAndEquals()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        // Difficult to write a complete test because we can't control the values of the internal fields directly.
        // We'll "solve" that using reflection.
        Transform2d reference = new Transform2d();
        assertEquals(reference, new Transform2d(), "Two different instances with same matrix do test equal");
        assertEquals(reference.hashCode(), new Transform2d().hashCode(),
                "Two different instances with same matrix have same hash code");
        for (int index = 0; index < 9; index++)
        {
            // Alter one element in the mat array at a time and expect the hash code to change and equals to return false.
            for (double alteration : new double[] { -100, -10, -Math.PI, -0.1, 0.3, Math.E, 123 })
            {
                Transform2d other = new Transform2d();
                Field matrix = other.getClass().getDeclaredField("mat");
                matrix.setAccessible(true);
                double[] matrixValues = (double[]) matrix.get(other);
                matrixValues[index] = alteration;
                assertNotEquals(reference, other, "Modified transform should not be equals");
                assertNotEquals(reference.hashCode(), other.hashCode(), "HashCode should be different (or it does not take all elements of the internal array "
                                + "into account");
            }
        }
        assertTrue(reference.equals(reference), "equal to itself");
        assertFalse(reference.equals(null), "not equal to null");
        assertFalse(reference.equals("nope"), "not equal to some other object");
    }

}
