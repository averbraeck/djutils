package org.djutils.draw.bounds;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.djutils.draw.Drawable3d;
import org.djutils.draw.Space3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Bounds3d is the generic class for the 3D extent of an object. It is an immutable object.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds3d implements Serializable, Drawable3d, Bounds<Bounds3d, Point3d, Space3d>
{
    /** */
    private static final long serialVersionUID = 2020829L;

    /** The lower bound for x. */
    private final double minAbsoluteX;

    /** The lower bound for y. */
    private final double minAbsoluteY;

    /** The lower bound for z. */
    private final double minAbsoluteZ;

    /** The upper bound for x. */
    private final double maxAbsoluteX;

    /** The upper bound for y. */
    private final double maxAbsoluteY;

    /** The upper bound for z. */
    private final double maxAbsoluteZ;

    /**
     * Construct a Bounds3d by providing all lower and upper bounds.
     * @param minAbsoluteX double; the lower bound for x
     * @param maxAbsoluteX double; the upper bound for x
     * @param minAbsoluteY double; the lower bound for y
     * @param maxAbsoluteY double; the upper bound for y
     * @param minAbsoluteZ double; the lower bound for z
     * @param maxAbsoluteZ double; the upper bound for z
     * @throws IllegalArgumentException when lower bounds are larger than upper boundingBox or any bound is NaN
     */
    public Bounds3d(final double minAbsoluteX, final double maxAbsoluteX, final double minAbsoluteY, final double maxAbsoluteY,
            final double minAbsoluteZ, final double maxAbsoluteZ)
    {
        Throw.when(
                Double.isNaN(minAbsoluteX) || Double.isNaN(maxAbsoluteX) || Double.isNaN(minAbsoluteY)
                        || Double.isNaN(maxAbsoluteY) || Double.isNaN(minAbsoluteZ) || Double.isNaN(maxAbsoluteZ),
                IllegalArgumentException.class, "Nan boundary value not permitted");
        Throw.when(minAbsoluteX > maxAbsoluteX || minAbsoluteY > maxAbsoluteY || minAbsoluteZ > maxAbsoluteZ,
                IllegalArgumentException.class,
                "lower bound for each dimension should be less than or equal to its upper bound");
        this.minAbsoluteX = minAbsoluteX;
        this.minAbsoluteY = minAbsoluteY;
        this.minAbsoluteZ = minAbsoluteZ;
        this.maxAbsoluteX = maxAbsoluteX;
        this.maxAbsoluteY = maxAbsoluteY;
        this.maxAbsoluteZ = maxAbsoluteZ;
    }

    /**
     * Constructs a new Bounds3d around the origin (0, 0, 0).
     * @param deltaX double; the deltaX value around the origin
     * @param deltaY double; the deltaY value around the origin
     * @param deltaZ double; the deltaZ value around the origin
     * @throws IllegalArgumentException when one of the delta values is less than zero
     */
    public Bounds3d(final double deltaX, final double deltaY, final double deltaZ)
    {
        this(-0.5 * deltaX, 0.5 * deltaX, -0.5 * deltaY, 0.5 * deltaY, -0.5 * deltaZ, 0.5 * deltaZ);
    }

    /**
     * Construct a Bounds3d from some collection of points, finding the lowest and highest x and y coordinates.
     * @param points Iterator&lt;? extends Point3d&gt;; the array of points to construct a Bounds3d from
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when zero points are provided
     */
    public Bounds3d(final Iterator<? extends Point3d> points)
    {
        Throw.whenNull(points, "points may not be null");
        Throw.when(!points.hasNext(), IllegalArgumentException.class, "need at least one point");
        Point3d point = points.next();
        double tempMinX = point.x;
        double tempMaxX = point.x;
        double tempMinY = point.y;
        double tempMaxY = point.y;
        double tempMinZ = point.z;
        double tempMaxZ = point.z;
        while (points.hasNext())
        {
            point = points.next();
            tempMinX = Math.min(tempMinX, point.x);
            tempMaxX = Math.max(tempMaxX, point.x);
            tempMinY = Math.min(tempMinY, point.y);
            tempMaxY = Math.max(tempMaxY, point.y);
            tempMinZ = Math.min(tempMinZ, point.z);
            tempMaxZ = Math.max(tempMaxZ, point.z);
        }
        this.minAbsoluteX = tempMinX;
        this.maxAbsoluteX = tempMaxX;
        this.minAbsoluteY = tempMinY;
        this.maxAbsoluteY = tempMaxY;
        this.minAbsoluteZ = tempMinZ;
        this.maxAbsoluteZ = tempMaxZ;
    }

    /**
     * Construct a Bounds3d from an array of Point3d, finding the lowest and highest x, y and z coordinates.
     * @param points Point3d[]; the points to construct a Bounds3d from
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when zero points are provided
     */
    public Bounds3d(final Point3d[] points) throws NullPointerException, IllegalArgumentException
    {
        this(Arrays.stream(Throw.whenNull(points, "points may not be null")).iterator());
    }

    /**
     * Construct a Bounds3d for a Drawable3d.
     * @param drawable3d Drawable3d; any object that implements the Drawable2d interface
     * @throws NullPointerException when area is null
     */
    public Bounds3d(final Drawable3d drawable3d) throws NullPointerException
    {
        this(Throw.whenNull(drawable3d, "drawable3d may not be null").getPoints());
    }

    /**
     * Construct a Bounds3d from a collection of Point3d, finding the lowest and highest x and y coordinates.
     * @param points Collection&lt;Point3d&gt;; the collection of points to construct a Bounds2d from
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when the collection is empty
     */
    public Bounds3d(final Collection<Point3d> points)
    {
        this(Throw.whenNull(points, "points may not be null").iterator());
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point3d> getPoints()
    {
        Point3d[] array = new Point3d[] { new Point3d(this.minAbsoluteX, this.minAbsoluteY, this.minAbsoluteZ),
                new Point3d(this.minAbsoluteX, this.minAbsoluteY, this.maxAbsoluteZ),
                new Point3d(this.minAbsoluteX, this.maxAbsoluteY, this.minAbsoluteZ),
                new Point3d(this.minAbsoluteX, this.maxAbsoluteY, this.maxAbsoluteZ),
                new Point3d(this.maxAbsoluteX, this.minAbsoluteY, this.minAbsoluteZ),
                new Point3d(this.maxAbsoluteX, this.minAbsoluteY, this.maxAbsoluteZ),
                new Point3d(this.maxAbsoluteX, this.maxAbsoluteY, this.minAbsoluteZ),
                new Point3d(this.maxAbsoluteX, this.maxAbsoluteY, this.maxAbsoluteZ) };
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 8;
    }

    /**
     * Check if the Bounds3d contains a point. Contains returns false when the point is on the surface of this Bounds3d.
     * @param point Point3d; the point
     * @return boolean; whether the bounding box contains the point
     * @throws NullPointerException when point is null
     */
    public boolean contains(final Point3d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return contains(point.x, point.y, point.z);
    }

    /**
     * Check if this Bounds3d contains a point. Contains returns false when the point is on the surface of this Bounds3d.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @param z double; the z-coordinate of the point
     * @return boolean; whether this Bounds3d contains the point
     * @throws IllegalArgumentException when any of the coordinates is NaN
     */
    public boolean contains(final double x, final double y, final double z) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "coordinates must be numbers (not NaN)");
        return x > this.minAbsoluteX && x < this.maxAbsoluteX && y > this.minAbsoluteY && y < this.maxAbsoluteY
                && z > this.minAbsoluteZ && z < this.maxAbsoluteZ;
    }

    /**
     * Check if the Bounds3d contains another Bounds3d. Contains returns false when one of the faces of the other Bounds3d is
     * overlapping with the face of this Bounds3d.
     * @param drawable Drawable3d; the Bounds3d for which to check if it is completely contained within this Bounds3d
     * @return boolean; whether the bounding box contains the provided bounding box
     * @throws NullPointerException when otherBounds3d is null
     */
    public boolean contains(final Drawable3d drawable)
    {
        Throw.whenNull(drawable, "drawable cannot be null");
        for (Iterator<? extends Point3d> iterator = drawable.getPoints(); iterator.hasNext();)
        {
            if (!contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d project()
    {
        return new Bounds2d(this.minAbsoluteX, this.maxAbsoluteX, this.minAbsoluteY, this.maxAbsoluteY);
    }

    /**
     * Check if this Bounds3d contains a point. Covers returns true when the point is on a face of this Bounds3d.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @param z double; the z-coordinate of the point
     * @return boolean; whether the bounding box contains the point, including the faces
     * @throws IllegalArgumentException when any of the coordinates is NaN
     */
    public boolean covers(final double x, final double y, final double z) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "coordinates must be numbers (not NaN)");
        return x >= this.minAbsoluteX && x <= this.maxAbsoluteX && y >= this.minAbsoluteY && y <= this.maxAbsoluteY
                && z >= this.minAbsoluteZ && z <= this.maxAbsoluteZ;
    }

    /**
     * Check if this Bounds3d contains a point. Covers returns true when the point is on a face of this Bounds3d.
     * @param point Point3d; the point
     * @return boolean; whether the bounding box contains the point, including the faces
     * @throws NullPointerException when point is null
     */
    public boolean covers(final Point3d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return covers(point.x, point.y, point.z);
    }

    /** {@inheritDoc} */
    @Override
    public boolean covers(final Bounds3d otherBounds3d)
    {
        Throw.whenNull(otherBounds3d, "otherBounds3d cannot be null");
        return covers(otherBounds3d.minAbsoluteX, otherBounds3d.minAbsoluteY, otherBounds3d.minAbsoluteZ)
                && covers(otherBounds3d.maxAbsoluteX, otherBounds3d.maxAbsoluteY, otherBounds3d.maxAbsoluteZ);
    }

    /** {@inheritDoc} */
    @Override
    public boolean disjoint(final Bounds3d otherBounds3d)
    {
        Throw.whenNull(otherBounds3d, "otherBounds3d cannot be null");
        return otherBounds3d.minAbsoluteX >= this.maxAbsoluteX || otherBounds3d.maxAbsoluteX <= this.minAbsoluteX
                || otherBounds3d.minAbsoluteY >= this.maxAbsoluteY || otherBounds3d.maxAbsoluteY <= this.minAbsoluteY
                || otherBounds3d.minAbsoluteZ >= this.maxAbsoluteZ || otherBounds3d.maxAbsoluteZ <= this.minAbsoluteZ;
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(final Bounds3d otherBounds3d)
    {
        return !disjoint(otherBounds3d);
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d intersection(final Bounds3d otherBounds3d)
    {
        Throw.whenNull(otherBounds3d, "otherBounds3d cannot be null");
        if (disjoint(otherBounds3d))
        {
            return null;
        }
        return new Bounds3d(Math.max(this.minAbsoluteX, otherBounds3d.minAbsoluteX),
                Math.min(this.maxAbsoluteX, otherBounds3d.maxAbsoluteX),
                Math.max(this.minAbsoluteY, otherBounds3d.minAbsoluteY),
                Math.min(this.maxAbsoluteY, otherBounds3d.maxAbsoluteY),
                Math.max(this.minAbsoluteZ, otherBounds3d.minAbsoluteZ),
                Math.min(this.maxAbsoluteZ, otherBounds3d.maxAbsoluteZ));
    }

    /**
     * Return the extent of this Bounds3d in the z-direction.
     * @return double; the extent of this Bounds3d in the z-direction
     */
    public double getDeltaZ()
    {
        return getAbsoluteMaxZ() - getAbsoluteMinZ();
    }

    /**
     * Return the volume of this Bounds3d.
     * @return double; the volume of this Bounds3d
     */
    public double getVolume()
    {
        return getDeltaX() * getDeltaY() * getDeltaZ();
    }

    /** {@inheritDoc} */
    @Override
    public double getAbsoluteMinX()
    {
        return this.minAbsoluteX;
    }

    /** {@inheritDoc} */
    @Override
    public double getAbsoluteMaxX()
    {
        return this.maxAbsoluteX;
    }

    /** {@inheritDoc} */
    @Override
    public double getAbsoluteMinY()
    {
        return this.minAbsoluteY;
    }

    /** {@inheritDoc} */
    @Override
    public double getAbsoluteMaxY()
    {
        return this.maxAbsoluteY;
    }

    /**
     * Return the lower bound for z.
     * @return double; the lower bound for z
     */
    public double getAbsoluteMinZ()
    {
        return this.minAbsoluteZ;
    }

    /**
     * Return the upper bound for z.
     * @return double; the upper bound for z
     */
    public double getAbsoluteMaxZ()
    {
        return this.maxAbsoluteZ;
    }

    /**
     * Return the relative lower bound for z (relative to the centroid).
     * @return double; the relative lower bound for z
     */
    public double getMinZ()
    {
        return -getDeltaZ() / 2;
    }

    /**
     * Return the relative upper bound for z (relative to the centroid).
     * @return double; the relative upper bound for z
     */
    public double getMaxZ()
    {
        return getDeltaZ() / 2;
    }

    /** {@inheritDoc} */
    @Override
    public Point3d midPoint()
    {
        return new Point3d((this.minAbsoluteX + this.maxAbsoluteX) / 2, (this.minAbsoluteY + this.maxAbsoluteY) / 2,
                (this.minAbsoluteZ + this.maxAbsoluteZ) / 2);
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[absoluteX[%2$s : %2$s], absoluteY[%2$s : %2$s, absoluteZ[%2$s : %2$s]]",
                doNotIncludeClassName ? "" : "Bounds3d ", doubleFormat);
        return String.format(format, this.minAbsoluteX, this.maxAbsoluteX, this.minAbsoluteY, this.maxAbsoluteY,
                this.minAbsoluteZ, this.maxAbsoluteZ);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.maxAbsoluteX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxAbsoluteY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxAbsoluteZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minAbsoluteX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minAbsoluteY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minAbsoluteZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:needbraces")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bounds3d other = (Bounds3d) obj;
        if (Double.doubleToLongBits(this.maxAbsoluteX) != Double.doubleToLongBits(other.maxAbsoluteX))
            return false;
        if (Double.doubleToLongBits(this.maxAbsoluteY) != Double.doubleToLongBits(other.maxAbsoluteY))
            return false;
        if (Double.doubleToLongBits(this.maxAbsoluteZ) != Double.doubleToLongBits(other.maxAbsoluteZ))
            return false;
        if (Double.doubleToLongBits(this.minAbsoluteX) != Double.doubleToLongBits(other.minAbsoluteX))
            return false;
        if (Double.doubleToLongBits(this.minAbsoluteY) != Double.doubleToLongBits(other.minAbsoluteY))
            return false;
        if (Double.doubleToLongBits(this.minAbsoluteZ) != Double.doubleToLongBits(other.minAbsoluteZ))
            return false;
        return true;
    }

}
