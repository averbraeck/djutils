package org.djutils.draw.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.djutils.draw.d0.Point;
import org.djutils.draw.d0.Point2d;
import org.djutils.draw.d0.Point3d;
import org.junit.Test;

/**
 * Transform3dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Transform3dTest
{
    /**
     * Test the matrix / vector multiplication.
     */
    @Test
    public void testMatrixMultiplication()
    {
        double[] mA = new double[] { 5, 7, 9, 10, 2, 3, 3, 8, 8, 10, 2, 3, 3, 3, 4, 8 };
        double[] mB = new double[] { 3, 10, 12, 18, 12, 1, 4, 9, 9, 10, 12, 2, 3, 12, 4, 10 };
        double[] mAmB = Transform3d.mulMatMat(mA, mB);
        double[] expected = new double[] { 210, 267, 236, 271, 93, 149, 104, 149, 171, 146, 172, 268, 105, 169, 128, 169 };
        for (int i = 0; i < 16; i++)
        {
            if (mAmB[i] != expected[i])
            {
                fail(String.format("difference MA x MB at %d: expected %f, was: %f", i, expected[i], mAmB[i]));
            }
        }

        double[] m = new double[] { 1, 0, 2, 0, 0, 3, 0, 4, 0, 0, 5, 0, 6, 0, 0, 7 };
        double[] v = new double[] { 2, 5, 1, 8 };
        double[] mv = Transform3d.mulMatVec(m, v);
        double[] ev = new double[] { 4, 47, 5, 68 };
        for (int i = 0; i < 4; i++)
        {
            if (mv[i] != ev[i])
            {
                fail(String.format("difference M x V at %d: expected %f, was: %f", i, ev[i], mv[i]));
            }
        }

        v = new double[] { 1, 2, 3 };
        mv = Transform3d.mulMatVec3(m, v);
        ev = new double[] { 7, 10, 15 };
        for (int i = 0; i < 3; i++)
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
        Transform3d t = new Transform3d();
        assertEquals("matrix contians 16 values", 16, t.mat.length);
        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                int e = row == col ? 1 : 0;
                assertEquals("Value in identity matrix matches", e, t.mat[4 * row + col], 0);
            }
        }
    }

    /**
     * Test the translate, scale, rotate, shear, reflectX and reflectY methods.
     */
    @Test
    public void testTranslateScaleRotateAndShear()
    {
        Transform3d t;
        // Test time grows (explodes) with the 4th power of the length of offsets.
        double[] offsets = new double[] { -100000, -100, -3, -1, -0.1, 0, 0.1, 1, 3, 100, 100000 };
        for (double dx : offsets)
        {
            for (double dy : offsets)
            {
                for (double dz : offsets)
                {
                    // Translate defined with a double[]
                    t = new Transform3d();
                    t.translate(dx, dy, dz);
                    for (double px : offsets)
                    {
                        for (double py : offsets)
                        {
                            for (double pz : offsets)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("transformed x matches", px + dx, p.getX(), 0.001);
                                assertEquals("transformed y matches", py + dy, p.getY(), 0.001);
                                assertEquals("transformed z matches", pz + dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("transformed x matches", px + dx, result[0], 0.001);
                                assertEquals("transformed y matches", py + dy, result[1], 0.001);
                                assertEquals("transformed z matches", pz + dz, result[2], 0.001);
                            }
                        }
                    }
                    // Translate defined with a Point
                    t = new Transform3d();
                    t.translate(new Point3d(dx, dy, dz));
                    for (double px : offsets)
                    {
                        for (double py : offsets)
                        {
                            for (double pz : offsets)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("transformed x matches", px + dx, p.getX(), 0.001);
                                assertEquals("transformed y matches", py + dy, p.getY(), 0.001);
                                assertEquals("transformed z matches", pz + dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("transformed x matches", px + dx, result[0], 0.001);
                                assertEquals("transformed y matches", py + dy, result[1], 0.001);
                                assertEquals("transformed z matches", pz + dz, result[2], 0.001);
                            }
                        }
                    }
                    // Scale
                    t = new Transform3d();
                    t.scale(dx, dy, dz);
                    for (double px : offsets)
                    {
                        for (double py : offsets)
                        {
                            for (double pz : offsets)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("scaled x matches", px * dx, p.getX(), 0.001);
                                assertEquals("scaled y matches", py * dy, p.getY(), 0.001);
                                assertEquals("scaled z matches", pz * dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("scaled x matches", px * dx, result[0], 0.001);
                                assertEquals("scaled y matches", py * dy, result[1], 0.001);
                                assertEquals("scaled z matches", pz * dz, result[2], 0.001);
                            }
                        }
                    }
                    // ShearXY
                    t = new Transform3d();
                    t.shearXY(dx, dy);
                    for (double px : offsets)
                    {
                        for (double py : offsets)
                        {
                            for (double pz : offsets)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("sheared x matches", px + pz * dx, p.getX(), 0.001);
                                assertEquals("sheared y matches", py + pz * dy, p.getY(), 0.001);
                                assertEquals("sheared z matches", pz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("sheared x matches", px + pz * dx, result[0], 0.001);
                                assertEquals("sheared y matches", py + pz * dy, result[1], 0.001);
                                assertEquals("sheared z matches", pz, result[2], 0.001);
                            }
                        }
                    }
                    // ShearXZ
                    t = new Transform3d();
                    t.shearXZ(dx, dz);
                    for (double px : offsets)
                    {
                        for (double py : offsets)
                        {
                            for (double pz : offsets)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("sheared x matches", px + py * dx, p.getX(), 0.001);
                                assertEquals("sheared y matches", py, p.getY(), 0.001);
                                assertEquals("sheared z matches", pz + py * dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("sheared x matches", px + py * dx, result[0], 0.001);
                                assertEquals("sheared y matches", py, result[1], 0.001);
                                assertEquals("sheared z matches", pz + py * dz, result[2], 0.001);
                            }
                        }
                    }
                    // ShearYZ
                    t = new Transform3d();
                    t.shearYZ(dy, dz);
                    for (double px : offsets)
                    {
                        for (double py : offsets)
                        {
                            for (double pz : offsets)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("sheared x matches", px, p.getX(), 0.001);
                                assertEquals("sheared y matches", py + px * dy, p.getY(), 0.001);
                                assertEquals("sheared z matches", pz + px * dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("sheared x matches", px, result[0], 0.001);
                                assertEquals("sheared y matches", py + px * dy, result[1], 0.001);
                                assertEquals("sheared z matches", pz + px * dz, result[2], 0.001);
                            }
                        }
                    }
                }
                // Rotate around Z (using dx as angle)
                t = new Transform3d();
                t.rotZ(dx);
                double sine = Math.sin(dx);
                double cosine = Math.cos(dx);
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        for (double pz : offsets)
                        {
                            Point p = t.transform(new Point3d(px, py, pz));
                            assertEquals("rotated x matches", px * cosine - py * sine, p.getX(), 0.001);
                            assertEquals("rotated y matches", py * cosine + px * sine, p.getY(), 0.001);
                            assertEquals("rotated z matches", pz, p.getZ(), 0.001);
                            double[] result = t.transform(new double[] { px, py, pz });
                            assertEquals("rotated x matches", px * cosine - py * sine, result[0], 0.001);
                            assertEquals("rotated z matches", py * cosine + px * sine, result[1], 0.001);
                            assertEquals("rotated z matches", pz, result[2], 0.001);
                        }
                    }
                }
                // Rotate around X (using dx as angle)
                t = new Transform3d();
                t.rotX(dx);
                sine = Math.sin(dx);
                cosine = Math.cos(dx);
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        for (double pz : offsets)
                        {
                            Point p = t.transform(new Point3d(px, py, pz));
                            assertEquals("rotated x matches", px, p.getX(), 0.001);
                            assertEquals("rotated y matches", py * cosine - pz * sine, p.getY(), 0.001);
                            assertEquals("rotated z matches", pz * cosine + py * sine, p.getZ(), 0.001);
                            double[] result = t.transform(new double[] { px, py, pz });
                            assertEquals("rotated x matches", px, result[0], 0.001);
                            assertEquals("rotated z matches", py * cosine - pz * sine, result[1], 0.001);
                            assertEquals("rotated z matches", pz * cosine + py * sine, result[2], 0.001);
                        }
                    }
                }
                // Rotate around Y (using dx as angle)
                t = new Transform3d();
                t.rotY(dx);
                sine = Math.sin(dx);
                cosine = Math.cos(dx);
                for (double px : offsets)
                {
                    for (double py : offsets)
                    {
                        for (double pz : offsets)
                        {
                            Point p = t.transform(new Point3d(px, py, pz));
                            assertEquals("rotated x matches", px * cosine + pz * sine, p.getX(), 0.001);
                            assertEquals("rotated y matches", py, p.getY(), 0.001);
                            assertEquals("rotated z matches", pz * cosine - px * sine, p.getZ(), 0.001);
                            double[] result = t.transform(new double[] { px, py, pz });
                            assertEquals("rotated x matches", px * cosine + pz * sine, result[0], 0.001);
                            assertEquals("rotated z matches", py, result[1], 0.001);
                            assertEquals("rotated z matches", pz * cosine - px * sine, result[2], 0.001);
                        }
                    }
                }
            }
        }
        // ReflectX
        t = new Transform3d();
        t.reflectX();
        for (double px : offsets)
        {
            for (double py : offsets)
            {
                for (double pz : offsets)
                {
                    Point p = t.transform(new Point3d(px, py, pz));
                    assertEquals("x-reflected x matches", -px, p.getX(), 0.001);
                    assertEquals("x-reflected y matches", py, p.getY(), 0.001);
                    assertEquals("x-reflected z matches", pz, p.getZ(), 0.001);
                    double[] result = t.transform(new double[] { px, py, pz });
                    assertEquals("x-reflected x matches", -px, result[0], 0.001);
                    assertEquals("x-reflected y matches", py, result[1], 0.001);
                    assertEquals("x-reflected z matches", pz, result[2], 0.001);
                }
            }
        }
        // ReflectY
        t = new Transform3d();
        t.reflectY();
        for (double px : offsets)
        {
            for (double py : offsets)
            {
                for (double pz : offsets)
                {
                    Point p = t.transform(new Point3d(px, py, pz));
                    assertEquals("y-reflected x matches", px, p.getX(), 0.001);
                    assertEquals("y-reflected y matches", -py, p.getY(), 0.001);
                    assertEquals("y-reflected z matches", pz, p.getZ(), 0.001);
                    double[] result = t.transform(new double[] { px, py, pz });
                    assertEquals("y-reflected x matches", px, result[0], 0.001);
                    assertEquals("y-reflected y matches", -py, result[1], 0.001);
                    assertEquals("y-reflected z matches", pz, result[2], 0.001);
                }
            }
        }
        // ReflectZ
        t = new Transform3d();
        t.reflectZ();
        for (double px : offsets)
        {
            for (double py : offsets)
            {
                for (double pz : offsets)
                {
                    Point p = t.transform(new Point3d(px, py, pz));
                    assertEquals("z-reflected x matches", px, p.getX(), 0.001);
                    assertEquals("z-reflected y matches", py, p.getY(), 0.001);
                    assertEquals("z-reflected z matches", -pz, p.getZ(), 0.001);
                    double[] result = t.transform(new double[] { px, py, pz });
                    assertEquals("z-reflected x matches", px, result[0], 0.001);
                    assertEquals("z-reflected y matches", py, result[1], 0.001);
                    assertEquals("z-reflected z matches", -pz, result[2], 0.001);
                }
            }
        }
    }

}
