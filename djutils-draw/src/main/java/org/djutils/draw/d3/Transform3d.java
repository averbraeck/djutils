package org.djutils.draw.d3;

import java.util.Arrays;

import org.djutils.draw.bounds.BoundingBox;
import org.djutils.draw.d0.Point;
import org.djutils.draw.d0.Point3d;

/**
 * Transform3d contains a MUTABLE transformation object that can transform points (x,y,z) based on e.g, rotation and
 * translation. It uses an affine transform matrix that can be built up from different components (translation, rotation,
 * scaling, reflection, shearing).
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Transform3d
{
    /** The 4x4 transformation matrix, initialized as the Identity matrix. */
    double[] mat = new double[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };

    /**
     * Multiply a 4x4 matrix (stored as a 16-value array by row) with a 4-value vector.
     * @param m double[16]; the matrix
     * @param v double[4]; the vector
     * @return double[4]; the result of m x v
     */
    protected static double[] mulMatVec(double[] m, double[] v)
    {
        double[] result = new double[4];
        for (int i = 0; i < 4; i++)
        {
            result[i] = m[4 * i] * v[0] + m[4 * i + 1] * v[1] + m[4 * i + 2] * v[2] + m[4 * i + 3] * v[3];
        }
        return result;
    }

    /**
     * Multiply a 4x4 matrix (stored as a 16-value array by row) with a 3-value vector and a 1 for the 4th value.
     * @param m double[16]; the matrix
     * @param v double[3]; the vector
     * @return double[3]; the result of m x (v1, v2, v3, 1), with the last value left out
     */
    protected static double[] mulMatVec3(double[] m, double[] v)
    {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++)
        {
            result[i] = m[4 * i] * v[0] + m[4 * i + 1] * v[1] + m[4 * i + 2] * v[2] + m[4 * i + 3];
        }
        return result;
    }

    /**
     * Multiply a 4x4 matrix (stored as a 16-value array by row) with another 4x4-matrix.
     * @param m1 double[16]; the first matrix
     * @param m2 double[16]; the second matrix
     * @return double[16]; the result of m1 x m2
     */
    protected static double[] mulMatMat(double[] m1, double[] m2)
    {
        double[] result = new double[16];
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                result[4 * i + j] =
                        m1[4 * i] * m2[j] + m1[4 * i + 1] * m2[j + 4] + +m1[4 * i + 2] * m2[j + 8] + m1[4 * i + 3] * m2[j + 12];
            }
        }
        return result;
    }

    /**
     * Transform coordinates by a vector (tx, ty, tz).
     * @param tx double; the translation value for the x-coordinates
     * @param ty double; the translation value for the y-coordinates
     * @param tz double; the translation value for the z-coordinates
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d translate(final double tx, final double ty, final double tz)
    {
        if (tx == 0.0 && ty == 0.0 && tz == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] { 1, 0, 0, tx, 0, 1, 0, ty, 0, 0, 1, tz, 0, 0, 0, 1 });
        return this;
    }

    /**
     * Translate coordinates by a the x, y, and z values contained in a Point.
     * @param point Point; the point containing the x, y, and z translation values
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d translate(final Point point)
    {
        if (point.getX() == 0.0 && point.getY() == 0.0 && point.getZ() == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat,
                new double[] { 1, 0, 0, point.getX(), 0, 1, 0, point.getY(), 0, 0, 1, point.getZ(), 0, 0, 0, 1 });
        return this;
    }

    /**
     * Scale all coordinates with a factor for x, y, and z. A scale factor of 1 leaves the coordinate unchanged.
     * @param sx double; the scale factor for the x-coordinates
     * @param sy double; the scale factor for the y-coordinates
     * @param sz double; the scale factor for the z-coordinates
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d scale(final double sx, final double sy, final double sz)
    {
        if (sx == 1.0 && sy == 1.0 && sz == 1.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] { sx, 0, 0, 0, 0, sy, 0, 0, 0, 0, sz, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The Euler rotation around the x-axis with an angle in radians.
     * @param angle double; the angle to rotate the coordinates with with around the x-axis
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d rotX(final double angle)
    {
        if (angle == 0.0)
        {
            return this;
        }
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        this.mat = mulMatMat(this.mat, new double[] { 1, 0, 0, 0, 0, c, -s, 0, 0, s, c, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The Euler rotation around the y-axis with an angle in radians.
     * @param angle double; the angle to rotate the coordinates with with around the y-axis
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d rotY(final double angle)
    {
        if (angle == 0.0)
        {
            return this;
        }
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        this.mat = mulMatMat(this.mat, new double[] { c, 0, s, 0, 0, 1, 0, 0, -s, 0, c, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The Euler rotation around the z-axis with an angle in radians.
     * @param angle double; the angle to rotate the coordinates with with around the z-axis
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d rotZ(final double angle)
    {
        if (angle == 0.0)
        {
            return this;
        }
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        this.mat = mulMatMat(this.mat, new double[] { c, -s, 0, 0, s, c, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The xy-shear leaves the xy-coordinate plane for z=0 untouched. Coordinates on z=1 are translated by a vector (sx, sy, 0).
     * Coordinates for points with other z-values are translated by a vector (z.sx, z.sy, 0), where z is the z-coordinate of the
     * point.
     * @param sx double; the shear factor in the x-direction for z=1
     * @param sy double; the shear factor in the y-direction for z=1
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d shearXY(final double sx, final double sy)
    {
        if (sx == 0.0 && sy == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] { 1, 0, sx, 0, 0, 1, sy, 0, 0, 0, 1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The yz-shear leaves the yz-coordinate plain for x=0 untouched. Coordinates on x=1 are translated by a vector (0, sy, sz).
     * Coordinates for points with other x-values are translated by a vector (0, x.sy, x.sz), where x is the x-coordinate of the
     * point.
     * @param sy double; the shear factor in the y-direction for x=1
     * @param sz double; the shear factor in the z-direction for x=1
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d shearYZ(final double sy, final double sz)
    {
        if (sy == 0.0 && sz == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] { 1, 0, 0, 0, sy, 1, 0, 0, sz, 0, 1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The xz-shear leaves the xz-coordinate plain for y=0 untouched. Coordinates on y=1 are translated by a vector (sx, 0, sz).
     * Coordinates for points with other y-values are translated by a vector (y.sx, 0, y.sz), where y is the y-coordinate of the
     * point.
     * @param sx double; the shear factor in the y-direction for y=1
     * @param sz double; the shear factor in the z-direction for y=1
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d shearXZ(final double sx, final double sz)
    {
        if (sx == 0.0 && sz == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] { 1, sx, 0, 0, 0, 1, 0, 0, 0, sz, 1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The reflection of the x-coordinate, by mirroring it in the yz-plane (the plane with x=0).
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d reflectX()
    {
        this.mat = mulMatMat(this.mat, new double[] { -1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The reflection of the y-coordinate, by mirroring it in the xz-plane (the plane with y=0).
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d reflectY()
    {
        this.mat = mulMatMat(this.mat, new double[] { 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * The reflection of the z-coordinate, by mirroring it in the xy-plane (the plane with z=0).
     * @return Transform3d; the new transformation matrix after applying this transform
     */
    public Transform3d reflectZ()
    {
        this.mat = mulMatMat(this.mat, new double[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1 });
        return this;
    }

    /**
     * Apply the stored transform on the xyz-vector and return the transformed vector. For speed reasons, no checks on correct
     * size of the vector is done.
     * @param xyz double[3] the provided vector
     * @return double[3]; the transformed vector
     */
    public double[] transform(final double[] xyz)
    {
        return mulMatVec3(this.mat, xyz);
    }

    /**
     * Apply the stored transform on the provided point and return a point with the transformed coordinate.
     * @param point Point3d; the point to be transformed
     * @return Point3d; a point with the transformed coordinates
     */
    public Point3d transform(final Point3d point)
    {
        return new Point3d(mulMatVec3(this.mat, new double[] { point.getX(), point.getY(), point.getZ() }));
    }

    /**
     * Apply the stored transform on the provided BoundingBox and return a new BoundingBox with the bounds of the transformed
     * coordinates. All 8 corner points have to be transformed, since we do not know which of the 8 points will result in the
     * lowest and highest x, y, and z coordinates.
     * @param boundingBox BoundingBox; the bounds to be transformed
     * @return BoundingBox; the new bounds based on the transformed coordinates
     */
    public BoundingBox transform(final BoundingBox boundingBox)
    {
        return new BoundingBox(
                new Point3d[] { transform(new Point3d(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ())),
                        transform(new Point3d(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMaxZ())),
                        transform(new Point3d(boundingBox.getMinX(), boundingBox.getMaxY(), boundingBox.getMinZ())),
                        transform(new Point3d(boundingBox.getMinX(), boundingBox.getMaxY(), boundingBox.getMaxZ())),
                        transform(new Point3d(boundingBox.getMaxX(), boundingBox.getMinY(), boundingBox.getMinZ())),
                        transform(new Point3d(boundingBox.getMaxX(), boundingBox.getMinY(), boundingBox.getMaxZ())),
                        transform(new Point3d(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMinZ())),
                        transform(new Point3d(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ())) });
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Transform3d [mat=" + Arrays.toString(mat) + "]";
    }

}