package org.djutils.draw.surface;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Surface3d.java. Triangulated surface in 3D space.
 * <p>
 * Copyright (c) 2021-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Surface3d implements Drawable3d
{
    /** */
    private static final long serialVersionUID = 20210706L;

    /** X-coordinates of all points used to define the triangulated surface. */
    private final double[] x;

    /** Y-coordinates of all points used to define the triangulated surface. */
    private final double[] y;

    /** Z-coordinates of all points used to define the triangulated surface. */
    private final double[] z;

    /** Indices into the points array for each triangle in succession. */
    private final int[] indices;

    /** The bounds of this Surface3d. */
    private final Bounds3d bounds;

    /**
     * Construct a new Surface3d.
     * @param points Point3d[][]; two dimensional array of points. The first index iterates over the individual triangles; the
     *            second index iterates over the points of a single triangle. The range of the second index must be 3. It is
     *            expected that all points appear multiple times in the points array, but never within the same sub-array.
     * @throws NullPointerException when points is null, or any element in points is null
     * @throws DrawRuntimeException when points is empty, or any element in points does not contain exactly three different
     *             points
     */
    public Surface3d(final Point3d[][] points) throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(points, "points");
        Throw.when(points.length == 0, DrawRuntimeException.class, "points must have at least one element");
        this.indices = new int[points.length * 3];
        // Figure out how many points are unique
        Map<Point3d, Integer> indexMap = new LinkedHashMap<>();
        for (int triangle = 0; triangle < points.length; triangle++)
        {
            Point3d[] trianglePoints = points[triangle];
            Throw.whenNull(trianglePoints, "Element in trianglePoints may not be null");
            Throw.when(trianglePoints.length != 3, DrawRuntimeException.class,
                    "Triangle %d contain wrong number of points (should be 3, got %d", triangle, trianglePoints.length);
            Point3d prevPoint = trianglePoints[2];
            for (int i = 0; i < 3; i++)
            {
                Point3d point = trianglePoints[i];
                // The next check is not good enough when this constructor can be fed a subclass of Point3d
                Throw.when(point.equals(prevPoint), DrawRuntimeException.class, "Triangle %d contains duplicate point",
                        triangle);
                Integer currentIndex = indexMap.get(point);
                if (currentIndex == null)
                {
                    indexMap.put(point, indexMap.size());
                }
                prevPoint = point;
            }
        }
        this.x = new double[indexMap.size()];
        this.y = new double[indexMap.size()];
        this.z = new double[indexMap.size()];
        for (int triangle = 0; triangle < points.length; triangle++)
        {
            Point3d[] trianglePoints = points[triangle];
            for (int i = 0; i < 3; i++)
            {
                Point3d point3d = trianglePoints[i];
                Integer index = indexMap.get(point3d);
                this.indices[triangle * 3 + i] = index;
                this.x[index] = point3d.x;
                this.y[index] = point3d.y;
                this.z[index] = point3d.z;
            }
        }
        this.bounds = new Bounds3d(getPoints());
    }

    @Override
    public Iterator<? extends Point3d> getPoints()
    {
        return new Iterator<Point3d>()
        {
            private int current = 0;

            @Override
            public boolean hasNext()
            {
                return this.current < size();
            }

            @Override
            public Point3d next()
            {
                Throw.when(this.current >= size(), NoSuchElementException.class, "Iterator has exhausted the input");
                int index = this.current++;
                return new Point3d(Surface3d.this.x[Surface3d.this.indices[index]],
                        Surface3d.this.y[Surface3d.this.indices[index]], Surface3d.this.z[Surface3d.this.indices[index]]);
            }
        };
    }

    @Override
    public int size()
    {
        return this.indices.length;
    }

    @Override
    public Bounds3d getBounds()
    {
        return this.bounds;
    }

    @Override
    public Drawable2d project() throws DrawRuntimeException
    {
        throw new DrawRuntimeException("Project not implemented because we do not have a class that represents a multitude "
                + "of triangles in the 2D plane");
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        StringBuilder result = new StringBuilder();
        if (!doNotIncludeClassName)
        {
            result.append("Surface3d ");
        }
        String format = String.format("%%sx=%1$s, y=%1$s, z=%1$s", doubleFormat);
        for (int index = 0; index < this.indices.length; index++)
        {
            if (index % 3 == 0)
            {
                result.append("[");
            }
            int i = this.indices[index];
            result.append(String.format(Locale.US, format, index == 0 ? "[" : ", ", this.x[i], this.y[i], this.z[i]));
            if (index % 3 == 2)
            {
                result.append("]");
            }
        }
        result.append("]");
        return result.toString();
    }

    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.indices);
        result = prime * result + Arrays.hashCode(this.x);
        result = prime * result + Arrays.hashCode(this.y);
        result = prime * result + Arrays.hashCode(this.z);
        return result;
    }

    @SuppressWarnings({"checkstyle:designforextension", "checkstyle:needbraces"})
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Surface3d other = (Surface3d) obj;
        if (!Arrays.equals(this.indices, other.indices))
            return false;
        if (!Arrays.equals(this.x, other.x))
            return false;
        if (!Arrays.equals(this.y, other.y))
            return false;
        if (!Arrays.equals(this.z, other.z))
            return false;
        return true;
    }

}
