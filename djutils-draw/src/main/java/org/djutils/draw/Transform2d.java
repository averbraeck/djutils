package org.djutils.draw;

import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;

/**
 * Transform2d contains a MUTABLE transformation object that can transform points (x,y) based on e.g, rotation and translation.
 * It uses an affine transform matrix that can be built up from different components (translation, rotation, scaling,
 * reflection, shearing).
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Transform2d implements Cloneable
{
    /** The 3x3 transformation matrix, initialized as the Identity matrix. */
    private double[] mat = new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1};

    /**
     * Multiply a 3x3 matrix (stored as a 9-value array by row) with a 4-value vector.
     * @param m double[]; the matrix
     * @param v double[]; the vector
     * @return double[3]; the result of m x v
     */
    protected static double[] mulMatVec(final double[] m, final double[] v)
    {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++)
        {
            result[i] = m[3 * i] * v[0] + m[3 * i + 1] * v[1] + m[3 * i + 2] * v[2];
        }
        return result;
    }

    /**
     * Multiply a 3x3 matrix (stored as a 9-value array by row) with a 3-value vector and a 1 for the 3rd value.
     * @param m double[]; the matrix
     * @param v double[]; the vector
     * @return double[2]; the result of m x (v1, v2, 1), with the last value left out
     */
    protected static double[] mulMatVec2(final double[] m, final double[] v)
    {
        double[] result = new double[2];
        for (int i = 0; i < 2; i++)
        {
            result[i] = m[3 * i] * v[0] + m[3 * i + 1] * v[1] + m[3 * i + 2];
        }
        return result;
    }

    /**
     * Multiply a 3x3 matrix (stored as a 9-value array by row) with another 3x3-matrix.
     * @param m1 double[]; the first matrix
     * @param m2 double[]; the second matrix
     * @return double[9]; the result of m1 x m2
     */
    protected static double[] mulMatMat(final double[] m1, final double[] m2)
    {
        double[] result = new double[9];
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                result[3 * i + j] = m1[3 * i] * m2[j] + m1[3 * i + 1] * m2[j + 3] + +m1[3 * i + 2] * m2[j + 6];
            }
        }
        return result;
    }

    /**
     * Get a safe copy of the affine transformation matrix.
     * @return double[]; a safe copy of the affine transformation matrix
     */
    public double[] getMat()
    {
        return this.mat.clone();
    }

    /**
     * Transform coordinates by a vector (tx, ty). Note that to carry out multiple operations, the steps have to be built in the
     * OPPOSITE order since matrix multiplication operates from RIGHT to LEFT.
     * @param tx double; the translation value for the x-coordinates
     * @param ty double; the translation value for the y-coordinates
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d translate(final double tx, final double ty)
    {
        if (tx == 0.0 && ty == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] {1, 0, tx, 0, 1, ty, 0, 0, 1});
        return this;
    }

    /**
     * Translate coordinates by a the x and y values contained in a Point2d. Note that to carry out multiple operations, the
     * steps have to be built in the OPPOSITE order since matrix multiplication operates from RIGHT to LEFT.
     * @param point Point2d; the point containing the x and y translation values
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d translate(final Point2d point)
    {
        if (point.x == 0.0 && point.y == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] {1, 0, point.x, 0, 1, point.y, 0, 0, 1});
        return this;
    }

    /**
     * Scale all coordinates with a factor for x, and y. A scale factor of 1 leaves the coordinate unchanged. Note that to carry
     * out multiple operations, the steps have to be built in the OPPOSITE order since matrix multiplication operates from RIGHT
     * to LEFT.
     * @param sx double; the scale factor for the x-coordinates
     * @param sy double; the scale factor for the y-coordinates
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d scale(final double sx, final double sy)
    {
        if (sx == 1.0 && sy == 1.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] {sx, 0, 0, 0, sy, 0, 0, 0, 1});
        return this;
    }

    /**
     * The rotation around the origin with an angle in radians. Note that to carry out multiple operations, the steps have to be
     * built in the OPPOSITE order since matrix multiplication operates from RIGHT to LEFT.
     * @param angle double; the angle to rotate the coordinates with with around the origin
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d rotation(final double angle)
    {
        if (angle == 0.0)
        {
            return this;
        }
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        this.mat = mulMatMat(this.mat, new double[] {c, -s, 0, s, c, 0, 0, 0, 1});
        return this;
    }

    /**
     * The 2d shear leaves the xy-coordinate plane for z=0 untouched. An x-coordinate with a value of 1 is translated by sx, and
     * an x-coordinate with another value is translated by x*sx. Similarly, a y-coordinate with a value of 1 is translated by xy
     * and a y-coordinate with another value is translated by y*sy. Note that to carry out multiple operations, the steps have
     * to be built in the OPPOSITE order since matrix multiplication operates from RIGHT to LEFT.
     * @param sx double; the shear factor in the x-direction
     * @param sy double; the shear factor in the y-direction
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d shear(final double sx, final double sy)
    {
        if (sx == 0.0 && sy == 0.0)
        {
            return this;
        }
        this.mat = mulMatMat(this.mat, new double[] {1, sx, 0, sy, 1, 0, 0, 0, 1});
        return this;
    }

    /**
     * The reflection of the x-coordinate, by mirroring it in the yz-plane (the plane with x=0). Note that to carry out multiple
     * operations, the steps have to be built in the OPPOSITE order since matrix multiplication operates from RIGHT to LEFT.
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d reflectX()
    {
        this.mat = mulMatMat(this.mat, new double[] {-1, 0, 0, 0, 1, 0, 0, 0, 1});
        return this;
    }

    /**
     * The reflection of the y-coordinate, by mirroring it in the xz-plane (the plane with y=0). Note that to carry out multiple
     * operations, the steps have to be built in the OPPOSITE order since matrix multiplication operates from RIGHT to LEFT.
     * @return Transform2d; the new transformation matrix after applying this transform
     */
    public Transform2d reflectY()
    {
        this.mat = mulMatMat(this.mat, new double[] {1, 0, 0, 0, -1, 0, 0, 0, 1});
        return this;
    }

    /**
     * Apply the stored transform on the xy-vector and return the transformed vector. For speed reasons, no checks on correct
     * size of the vector is done.
     * @param xy double[]; double[2] the provided vector
     * @return double[2]; the transformed vector
     */
    public double[] transform(final double[] xy)
    {
        return mulMatVec2(this.mat, xy);
    }

    /**
     * Apply the stored transform on the provided point and return a point with the transformed coordinate.
     * @param point Point2d; the point to be transformed
     * @return Point2d; a point with the transformed coordinates
     */
    public Point2d transform(final Point2d point)
    {
        return new Point2d(mulMatVec2(this.mat, new double[] {point.x, point.y}));
    }

    /**
     * Apply the stored transform on the points generated by the provided pointIterator.
     * @param pointIterator Iterator&lt;Point2d&gt;; generates the points to be transformed
     * @return Iterator&lt;Point2d&gt;; an iterator that will generator all transformed points
     */
    public Iterator<Point2d> transform(final Iterator<Point2d> pointIterator)
    {
        return new Iterator<Point2d>()
        {

            @Override
            public boolean hasNext()
            {
                return pointIterator.hasNext();
            }

            @Override
            public Point2d next()
            {
                return transform(pointIterator.next());
            }
        };
    }

    /**
     * Apply the stored transform on the provided Bounds2d and return a new Bounds2d with the bounds of the transformed
     * coordinates. All 4 corner points have to be transformed, since we do not know which of the 4 points will result in the
     * lowest and highest x and y coordinates.
     * @param boundingRectangle Bounds2d; the bounds to be transformed
     * @return Bounds2d; the new bounds based on the transformed coordinates
     */
    public Bounds2d transform(final Bounds2d boundingRectangle)
    {
        return new Bounds2d(transform(boundingRectangle.getPoints()));
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.mat);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transform2d other = (Transform2d) obj;
        if (!Arrays.equals(this.mat, other.mat))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Transform2d [mat=" + Arrays.toString(this.mat) + "]";
    }

}
