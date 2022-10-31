package org.djutils.draw.bounds;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.Drawable3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Bounds3d is the generic class for the 3D extent of an object. It is an immutable object.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds3d implements Serializable, Drawable3d, Bounds<Bounds3d, Point3d, Drawable3d>
{
    /** */
    private static final long serialVersionUID = 2020829L;

    /** The lower bound for x. */
    private final double minX;

    /** The lower bound for y. */
    private final double minY;

    /** The lower bound for z. */
    private final double minZ;

    /** The upper bound for x. */
    private final double maxX;

    /** The upper bound for y. */
    private final double maxY;

    /** The upper bound for z. */
    private final double maxZ;

    /**
     * Construct a Bounds3d by providing all lower and upper bounds.
     * @param minX double; the lower bound for x
     * @param maxX double; the upper bound for x
     * @param minY double; the lower bound for y
     * @param maxY double; the upper bound for y
     * @param minZ double; the lower bound for z
     * @param maxZ double; the upper bound for z
     * @throws IllegalArgumentException when lower bounds are larger than upper boundingBox or any bound is NaN
     */
    public Bounds3d(final double minX, final double maxX, final double minY, final double maxY, final double minZ,
            final double maxZ)
    {
        Throw.when(Double.isNaN(minX) || Double.isNaN(maxX) || Double.isNaN(minY) || Double.isNaN(maxY) || Double.isNaN(minZ)
                || Double.isNaN(maxZ), IllegalArgumentException.class, "Nan boundary value not permitted");
        Throw.when(minX > maxX || minY > maxY || minZ > maxZ, IllegalArgumentException.class,
                "lower bound for each dimension should be less than or equal to its upper bound");
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
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
        this.minX = tempMinX;
        this.maxX = tempMaxX;
        this.minY = tempMinY;
        this.maxY = tempMaxY;
        this.minZ = tempMinZ;
        this.maxZ = tempMaxZ;
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
     * Construct a Bounds3d for several Drawable2d objects.
     * @param drawable3d Drawable3d...; the Drawable2d objects
     * @throws NullPointerException when the array is null, or contains a null value
     * @throws IllegalArgumentException when the length of the array is 0
     */
    public Bounds3d(final Drawable3d... drawable3d) throws NullPointerException, IllegalArgumentException
    {
        this(pointsOf(drawable3d));
    }

    /**
     * Return an iterator that will return all points of one or more Drawable objects.
     * @param drawable3d Drawable3d...; the Drawable objects
     * @return Iterator&lt;P&gt;; iterator that will return all points of the Drawable objects
     * @throws NullPointerException when drawable is null, or contains a null value
     * @throws IllegalArgumentException when drawable is empty
     */
    public static Iterator<Point3d> pointsOf(final Drawable3d... drawable3d)
    {
        return new Iterator<Point3d>()
        {
            /** Index in the argument array. */
            private int nextArgument = 0;

            /** Iterator over the Point2d objects in the current Drawable2d. */
            private Iterator<? extends Point3d> currentIterator = ensureHasOne(drawable3d)[0].getPoints();

            @Override
            public boolean hasNext()
            {
                return this.nextArgument < drawable3d.length - 1 || this.currentIterator.hasNext();
            }

            @Override
            public Point3d next()
            {
                if (this.currentIterator.hasNext())
                {
                    return this.currentIterator.next();
                }
                // Move to next Drawable2d
                this.nextArgument++;
                this.currentIterator = drawable3d[this.nextArgument].getPoints();
                return this.currentIterator.next(); // Cannot fail because every Drawable has at least one point
            }
        };
    }

    /**
     * Verify that the array contains at least one entry.
     * @param drawable3dArray Drawable3d[]; array of Drawable2d objects
     * @return Drawable3d[]; the array
     * @throws NullPointerException when the array is null
     * @throws IllegalArgumentException when the array contains 0 elements
     */
    static Drawable3d[] ensureHasOne(final Drawable3d[] drawable3dArray) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(drawable3dArray, "Array may not be null");
        Throw.when(drawable3dArray.length == 0, IllegalArgumentException.class, "Array must contain at least one value");
        return drawable3dArray;
    }

    /**
     * Construct a Bounds3d for a Collection of Drawable2d objects.
     * @param drawableCollection Collection&lt;Drawable2d&gt;; the collection
     * @throws NullPointerException when the collection is null, or contains null values
     * @throws IllegalArgumentException when the collection is empty
     */
    public Bounds3d(final Collection<Drawable3d> drawableCollection) throws NullPointerException, IllegalArgumentException
    {
        this(pointsOf(drawableCollection));
    }

    /**
     * Return an iterator that will return all points of one or more Drawable3d objects.
     * @param drawableCollection Collection&lt;Drawable3d&gt;; the collection of Drawable2d objects
     * @return Iterator&lt;P&gt;; iterator that will return all points of the Drawable objects
     * @throws NullPointerException when drawableCollection is null, or contains a null value
     * @throws IllegalArgumentException when drawableCollection is empty
     */
    public static Iterator<Point3d> pointsOf(final Collection<Drawable3d> drawableCollection)
            throws NullPointerException, IllegalArgumentException
    {
        return new Iterator<Point3d>()
        {
            /** Iterator that iterates over the collection. */
            private Iterator<Drawable3d> collectionIterator = ensureHasOne(drawableCollection.iterator());

            /** Iterator that generates Point2d objects for the currently selected element of the collection. */
            private Iterator<? extends Point3d> currentIterator = this.collectionIterator.next().getPoints();

            @Override
            public boolean hasNext()
            {
                if (this.currentIterator == null)
                {
                    return false;
                }
                return this.currentIterator.hasNext();
            }

            @Override
            public Point3d next()
            {
                Point3d result = this.currentIterator.next();
                if (!this.currentIterator.hasNext())
                {
                    if (this.collectionIterator.hasNext())
                    {
                        this.currentIterator = this.collectionIterator.next().getPoints();
                    }
                    else
                    {
                        this.currentIterator = null;
                    }
                }
                return result;
            }
        };
    }

    /**
     * Verify that the iterator has something to return.
     * @param iterator Iterator&lt;Drawable2d&gt;; the iterator
     * @return Iterator&lt;Drawable3d&gt;; the iterator
     * @throws NullPointerException when the iterator is null
     * @throws IllegalArgumentException when the hasNext method of the iterator returns false
     */
    static Iterator<Drawable3d> ensureHasOne(final Iterator<Drawable3d> iterator)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.when(!iterator.hasNext(), IllegalArgumentException.class, "Collection may not be empty");
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point3d> getPoints()
    {
        Point3d[] array =
                new Point3d[] { new Point3d(this.minX, this.minY, this.minZ), new Point3d(this.minX, this.minY, this.maxZ),
                        new Point3d(this.minX, this.maxY, this.minZ), new Point3d(this.minX, this.maxY, this.maxZ),
                        new Point3d(this.maxX, this.minY, this.minZ), new Point3d(this.maxX, this.minY, this.maxZ),
                        new Point3d(this.maxX, this.maxY, this.minZ), new Point3d(this.maxX, this.maxY, this.maxZ) };
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 8;
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
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY && z > this.minZ && z < this.maxZ;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Point3d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return contains(point.x, point.y, point.z);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Drawable3d drawable) throws NullPointerException
    {
        Throw.whenNull(drawable, "drawable cannot be null");
        Bounds3d bounds = drawable.getBounds();
        return contains(bounds.minX, bounds.minY, bounds.minZ) && contains(bounds.maxX, bounds.maxY, bounds.maxZ);
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
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
    }

    /** {@inheritDoc} */
    @Override
    public boolean covers(final Point3d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return covers(point.x, point.y, point.z);
    }

    /** {@inheritDoc} */
    @Override
    public boolean covers(final Drawable3d drawable)
    {
        Throw.whenNull(drawable, "drawable cannot be null");
        Bounds3d bounds = drawable.getBounds();
        return covers(bounds.minX, bounds.minY, bounds.minZ) && covers(bounds.maxX, bounds.maxY, bounds.maxZ);
    }

    /** {@inheritDoc} */
    @Override
    public boolean disjoint(final Drawable3d drawable)
    {
        Throw.whenNull(drawable, "drawable cannot be null");
        Bounds3d bounds = drawable.getBounds();
        return bounds.minX > this.maxX || bounds.maxX < this.minX || bounds.minY > this.maxY || bounds.maxY < this.minY
                || bounds.minZ > this.maxZ || bounds.maxZ < this.minZ;
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
        return new Bounds3d(Math.max(this.minX, otherBounds3d.minX), Math.min(this.maxX, otherBounds3d.maxX),
                Math.max(this.minY, otherBounds3d.minY), Math.min(this.maxY, otherBounds3d.maxY),
                Math.max(this.minZ, otherBounds3d.minZ), Math.min(this.maxZ, otherBounds3d.maxZ));
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d project()
    {
        return new Bounds2d(this.minX, this.maxX, this.minY, this.maxY);
    }

    /**
     * Return the extent of this Bounds3d in the z-direction.
     * @return double; the extent of this Bounds3d in the z-direction
     */
    public double getDeltaZ()
    {
        return getMaxZ() - getMinZ();
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
    public double getMinX()
    {
        return this.minX;
    }

    /** {@inheritDoc} */
    @Override
    public double getMaxX()
    {
        return this.maxX;
    }

    /** {@inheritDoc} */
    @Override
    public double getMinY()
    {
        return this.minY;
    }

    /** {@inheritDoc} */
    @Override
    public double getMaxY()
    {
        return this.maxY;
    }

    /**
     * Return the lower bound for z.
     * @return double; the lower bound for z
     */
    public double getMinZ()
    {
        return this.minZ;
    }

    /**
     * Return the upper bound for z.
     * @return double; the upper bound for z
     */
    public double getMaxZ()
    {
        return this.maxZ;
    }

    /** {@inheritDoc} */
    @Override
    public Point3d midPoint()
    {
        return new Point3d((this.minX + this.maxX) / 2, (this.minY + this.maxY) / 2, (this.minZ + this.maxZ) / 2);
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
        String format = String.format("%1$s[x[%2$s : %2$s], y[%2$s : %2$s, z[%2$s : %2$s]]",
                doNotIncludeClassName ? "" : "Bounds3d ", doubleFormat);
        return String.format(Locale.US, format, this.minX, this.maxX, this.minY, this.maxY, this.minZ, this.maxZ);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.maxX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minZ);
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
        if (Double.doubleToLongBits(this.maxX) != Double.doubleToLongBits(other.maxX))
            return false;
        if (Double.doubleToLongBits(this.maxY) != Double.doubleToLongBits(other.maxY))
            return false;
        if (Double.doubleToLongBits(this.maxZ) != Double.doubleToLongBits(other.maxZ))
            return false;
        if (Double.doubleToLongBits(this.minX) != Double.doubleToLongBits(other.minX))
            return false;
        if (Double.doubleToLongBits(this.minY) != Double.doubleToLongBits(other.minY))
            return false;
        if (Double.doubleToLongBits(this.minZ) != Double.doubleToLongBits(other.minZ))
            return false;
        return true;
    }

}
