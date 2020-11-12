package org.djutils.draw.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.draw.bounds.BoundingBox;
import org.djutils.draw.d0.Point;
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
     * Test the translate, scale, rotate, shear and reflect methods.
     */
    @Test
    public void testTranslateScaleRotateShearAndReflect()
    {
        Transform3d t;
        // Test time grows (explodes) with the 6th power of the length of values.
        double[] values = new double[] { -100000, -100, -3, -1, -0.1, 0, 0.1, 1, 3, 100, 100000 };
        for (double dx : values)
        {
            for (double dy : values)
            {
                for (double dz : values)
                {
                    // Translate defined with a double[]
                    t = new Transform3d();
                    t.translate(dx, dy, dz);
                    for (double px : values)
                    {
                        for (double py : values)
                        {
                            for (double pz : values)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("translated x matches", px + dx, p.getX(), 0.001);
                                assertEquals("translated y matches", py + dy, p.getY(), 0.001);
                                assertEquals("translated z matches", pz + dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("translated x matches", px + dx, result[0], 0.001);
                                assertEquals("translated y matches", py + dy, result[1], 0.001);
                                assertEquals("translated z matches", pz + dz, result[2], 0.001);
                            }
                        }
                    }
                    // Translate defined with a Point
                    t = new Transform3d();
                    t.translate(new Point3d(dx, dy, dz));
                    for (double px : values)
                    {
                        for (double py : values)
                        {
                            for (double pz : values)
                            {
                                Point p = t.transform(new Point3d(px, py, pz));
                                assertEquals("translated x matches", px + dx, p.getX(), 0.001);
                                assertEquals("translated y matches", py + dy, p.getY(), 0.001);
                                assertEquals("translated z matches", pz + dz, p.getZ(), 0.001);
                                double[] result = t.transform(new double[] { px, py, pz });
                                assertEquals("translated x matches", px + dx, result[0], 0.001);
                                assertEquals("translated y matches", py + dy, result[1], 0.001);
                                assertEquals("translated z matches", pz + dz, result[2], 0.001);
                            }
                        }
                    }
                    // Scale
                    t = new Transform3d();
                    t.scale(dx, dy, dz);
                    for (double px : values)
                    {
                        for (double py : values)
                        {
                            for (double pz : values)
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
                    for (double px : values)
                    {
                        for (double py : values)
                        {
                            for (double pz : values)
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
                    for (double px : values)
                    {
                        for (double py : values)
                        {
                            for (double pz : values)
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
                    for (double px : values)
                    {
                        for (double py : values)
                        {
                            for (double pz : values)
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
                for (double px : values)
                {
                    for (double py : values)
                    {
                        for (double pz : values)
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
                for (double px : values)
                {
                    for (double py : values)
                    {
                        for (double pz : values)
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
                for (double px : values)
                {
                    for (double py : values)
                    {
                        for (double pz : values)
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
        for (double px : values)
        {
            for (double py : values)
            {
                for (double pz : values)
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
        for (double px : values)
        {
            for (double py : values)
            {
                for (double pz : values)
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
        for (double px : values)
        {
            for (double py : values)
            {
                for (double pz : values)
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

    /**
     * Test the transform method.
     */
    @Test
    public void transformTest()
    {
        Transform3d reflectionX = new Transform3d().reflectX();
        Transform3d reflectionY = new Transform3d().reflectY();
        Transform3d reflectionZ = new Transform3d().reflectZ();
        // Test time explodes with the 6th power of the length of this array
        double[] values = new double[] { -30, 0, 0.07, 25 };
        for (double translateX : values)
        {
            for (double translateY : values)
            {
                for (double translateZ : values)
                {
                    Transform3d translation = new Transform3d().translate(translateX, translateY, translateZ);
                    for (double scaleX : values)
                    {
                        for (double scaleY : values)
                        {
                            for (double scaleZ : values)
                            {
                                Transform3d scaling = new Transform3d().scale(scaleX, scaleY, scaleZ);
                                for (double angle : new double[] { -2, 0, 0.5 })
                                {
                                    Transform3d rotationX = new Transform3d().rotX(angle);
                                    Transform3d rotationY = new Transform3d().rotY(angle);
                                    Transform3d rotationZ = new Transform3d().rotZ(angle);
                                    for (double shearA : values)
                                    {
                                        for (double shearB : values)
                                        {
                                            Transform3d t = new Transform3d().translate(translateX, translateY, translateZ)
                                                    .scale(scaleX, scaleY, scaleZ).rotZ(angle).shearXY(shearA, shearB);
                                            Transform3d shearXY = new Transform3d().shearXY(shearA, shearB);
                                            Transform3d tReflectX =
                                                    new Transform3d().reflectX().translate(translateX, translateY, translateZ)
                                                            .scale(scaleX, scaleY, scaleZ).rotY(angle).shearYZ(shearA, shearB);
                                            Transform3d shearYZ = new Transform3d().shearYZ(shearA, shearB);
                                            Transform3d tReflectY =
                                                    new Transform3d().reflectY().translate(translateX, translateY, translateZ)
                                                            .scale(scaleX, scaleY, scaleZ).rotZ(angle).shearXZ(shearA, shearB);
                                            Transform3d shearXZ = new Transform3d().shearXZ(shearA, shearB);
                                            Transform3d tReflectZ =
                                                    new Transform3d().reflectZ().translate(translateX, translateY, translateZ)
                                                            .scale(scaleX, scaleY, scaleZ).rotX(angle).shearXY(shearA, shearB);
                                            for (double px : values)
                                            {
                                                for (double py : values)
                                                {
                                                    for (double pz : values)
                                                    {
                                                        Point3d p = new Point3d(px, py, pz);
                                                        Point3d tp = t.transform(p);
                                                        Point3d chainP = translation.transform(
                                                                scaling.transform(rotationZ.transform(shearXY.transform(p))));
                                                        assertEquals("X", chainP.getX(), tp.getX(), 0.0000001);
                                                        assertEquals("Y", chainP.getY(), tp.getY(), 0.0000001);
                                                        assertEquals("Z", chainP.getZ(), tp.getZ(), 0.0000001);
                                                        tp = tReflectX.transform(p);
                                                        Point3d chainPReflectX = reflectionX.transform(translation.transform(
                                                                scaling.transform(rotationY.transform(shearYZ.transform(p)))));
                                                        assertEquals("RX X", chainPReflectX.getX(), tp.getX(), 0.0000001);
                                                        assertEquals("RX Y", chainPReflectX.getY(), tp.getY(), 0.0000001);
                                                        assertEquals("RX Z", chainPReflectX.getZ(), tp.getZ(), 0.0000001);
                                                        tp = tReflectY.transform(p);
                                                        Point3d chainPReflectY = reflectionY.transform(translation.transform(
                                                                scaling.transform(rotationZ.transform(shearXZ.transform(p)))));
                                                        assertEquals("RY X", chainPReflectY.getX(), tp.getX(), 0.0000001);
                                                        assertEquals("RY Y", chainPReflectY.getY(), tp.getY(), 0.0000001);
                                                        assertEquals("RY Z", chainPReflectY.getZ(), tp.getZ(), 0.0000001);
                                                        tp = tReflectZ.transform(p);
                                                        Point3d chainPReflectZ = reflectionZ.transform(translation.transform(
                                                                scaling.transform(rotationX.transform(shearXY.transform(p)))));
                                                        assertEquals("RZ X", chainPReflectZ.getX(), tp.getX(), 0.0000001);
                                                        assertEquals("RZ Y", chainPReflectZ.getY(), tp.getY(), 0.0000001);
                                                        assertEquals("RZ Z", chainPReflectZ.getZ(), tp.getZ(), 0.0000001);
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
            }
        }
    }

    /**
     * Test transformation of a bounding box.
     */
    @Test
    public void transformBoundingBoxTest()
    {
        double[] values = new double[] { -100, 0.1, 0, 0.1, 100 };
        double[] sizes = new double[] { 0, 10, 100 };
        Transform3d t = new Transform3d().rotX(0.4).rotZ(0.8).rotY(-1.2).reflectX().scale(0.5, 1.5, 2.5).shearXY(2, 3)
                .translate(123, 456, 789);
        // System.out.println(t);
        for (double x : values)
        {
            for (double y : values)
            {
                for (double z : values)
                {
                    for (double xSize : sizes)
                    {
                        for (double ySize : sizes)
                        {
                            for (double zSize : sizes)
                            {
                                BoundingBox bb = new BoundingBox(x, x + xSize, y, y + ySize, z, z + zSize);
                                Point3d[] points = new Point3d[] { new Point3d(x, y, z), new Point3d(x + xSize, y, z),
                                        new Point3d(x, y + ySize, z), new Point3d(x + xSize, y + ySize, z),
                                        new Point3d(x, y, z + zSize), new Point3d(x + xSize, y, z + zSize),
                                        new Point3d(x, y + ySize, z + zSize), new Point3d(x + xSize, y + ySize, z + zSize) };
                                Point3d[] transformedPoints = new Point3d[8];
                                for (int i = 0; i < points.length; i++)
                                {
                                    transformedPoints[i] = t.transform(points[i]);
                                }
                                BoundingBox expected = new BoundingBox(transformedPoints);
                                BoundingBox got = t.transform(bb);
                                assertEquals("bb minX", expected.getMinX(), got.getMinX(), 0.0001);
                                assertEquals("bb maxX", expected.getMaxX(), got.getMaxX(), 0.0001);
                                assertEquals("bb minY", expected.getMinY(), got.getMinY(), 0.0001);
                                assertEquals("bb maxY", expected.getMaxY(), got.getMaxY(), 0.0001);
                                assertEquals("bb minZ", expected.getMinZ(), got.getMinZ(), 0.0001);
                                assertEquals("bb maxZ", expected.getMaxZ(), got.getMaxZ(), 0.0001);
                            }
                        }
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
        assertTrue("toString returns something descriptive", new Transform3d().toString().startsWith("Transform3d "));
    }

}
