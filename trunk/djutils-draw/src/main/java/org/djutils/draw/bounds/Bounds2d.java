package org.djutils.draw.bounds;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.Space2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * A Bounds2d stores the rectangular 2D bounds of a 2d object, or a collection of 2d objects. The Bounds2d is an immutable
 * object.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds2d implements Drawable2d, Bounds<Bounds2d, Point2d, Space2d>
{
    /** */
    private static final long serialVersionUID = 20200829L;

    /** The lower bound for x. */
    private final double minX;

    /** The lower bound for y. */
    private final double minY;

    /** The upper bound for x. */
    private final double maxX;

    /** The upper bound for y. */
    private final double maxY;

    /**
     * Construct a Bounds2d by providing its lower and upper bounds in both dimensions.
     * @param minX double; the lower bound for x
     * @param maxX double; the upper bound for x
     * @param minY double; the lower bound for y
     * @param maxY double; the upper bound for y
     * @throws IllegalArgumentException when a lower bound is larger than the corresponding upper bound, or any of the bounds is
     *             NaN
     */
    public Bounds2d(final double minX, final double maxX, final double minY, final double maxY) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(minX) || Double.isNaN(maxX) || Double.isNaN(minY) || Double.isNaN(maxY),
                IllegalArgumentException.class, "bounds must be numbers (not NaN)");
        Throw.when(minX > maxX || minY > maxY, IllegalArgumentException.class,
                "lower bound for each dimension should be less than or equal to its upper bound");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Constructs a new Bounds2d around the origin (0, 0).
     * @param deltaX double; the deltaX value around the origin
     * @param deltaY double; the deltaY value around the origin
     * @throws IllegalArgumentException when one of the delta values is less than zero
     */
    public Bounds2d(final double deltaX, final double deltaY)
    {
        this(-0.5 * deltaX, 0.5 * deltaX, -0.5 * deltaY, 0.5 * deltaY);
    }

    /**
     * Construct a Bounds2d from some collection of points, finding the lowest and highest x and y coordinates.
     * @param points Iterator&lt;? extends Point2d&gt;; Iterator that will generate all the points for which to construct a
     *            Bounds2d
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when the iterator provides zero points
     */
    public Bounds2d(final Iterator<? extends Point2d> points)
    {
        Throw.whenNull(points, "points may not be null");
        Throw.when(!points.hasNext(), IllegalArgumentException.class, "need at least one point");
        Point2d point = points.next();
        double tempMinX = point.x;
        double tempMaxX = point.x;
        double tempMinY = point.y;
        double tempMaxY = point.y;
        while (points.hasNext())
        {
            point = points.next();
            tempMinX = Math.min(tempMinX, point.x);
            tempMaxX = Math.max(tempMaxX, point.x);
            tempMinY = Math.min(tempMinY, point.y);
            tempMaxY = Math.max(tempMaxY, point.y);
        }
        this.minX = tempMinX;
        this.maxX = tempMaxX;
        this.minY = tempMinY;
        this.maxY = tempMaxY;
    }

    /**
     * Construct a Bounds2d from an array of Point2d, finding the lowest and highest x and y coordinates.
     * @param points Point2d[]; the points to construct a Bounds2d from
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when zero points are provided
     */
    public Bounds2d(final Point2d[] points) throws NullPointerException, IllegalArgumentException
    {
        this(Arrays.stream(Throw.whenNull(points, "points may not be null")).iterator());
    }

    /**
     * Construct a Bounds2d for a Drawable2d.
     * @param drawable2d Drawable2d; any object that implements the Drawable2d interface
     * @throws NullPointerException when drawable2d is null
     */
    public Bounds2d(final Drawable2d drawable2d) throws NullPointerException
    {
        this(Throw.whenNull(drawable2d, "drawable2d may not be null").getPoints());
    }

    /**
     * Construct a Bounds2d for several Drawable2d objects.
     * @param drawable2d Drawable2d...; the Drawable2d objects
     * @throws NullPointerException when the array is null, or contains a null value
     * @throws IllegalArgumentException when the length of the array is 0
     */
    public Bounds2d(final Drawable2d... drawable2d) throws NullPointerException, IllegalArgumentException
    {
        this(new Iterator<Point2d>()
        {
            /** Index in the argument array. */
            private int nextArgument = 0;

            /** Iterator over the Point2d objects in the current Drawable2d. */
            private Iterator<? extends Point2d> currentIterator = ensureHasOne(drawable2d)[0].getPoints();

            @Override
            public boolean hasNext()
            {
                return this.nextArgument < drawable2d.length - 1 || this.currentIterator.hasNext();
            }

            @Override
            public Point2d next()
            {
                if (this.currentIterator.hasNext())
                {
                    return this.currentIterator.next();
                }
                // Move to next Drawable2d
                this.nextArgument++;
                this.currentIterator = drawable2d[this.nextArgument].getPoints();
                return this.currentIterator.next(); // Cannot fail because every Drawable has at least one point
            }
        });
    }

    /**
     * Verify that the array contains at lease one entry.
     * @param drawable2dArray Drawable2d[]; array of Drawable2d objects
     * @return Drawable2d[]; the array
     * @throws NullPointerException when the array is null
     * @throws IllegalArgumentException when the array contains 0 elements
     */
    static Drawable2d[] ensureHasOne(final Drawable2d[] drawable2dArray) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(drawable2dArray, "Array may not be null");
        Throw.when(drawable2dArray.length == 0, IllegalArgumentException.class, "Array must contain at least one value");
        return drawable2dArray;
    }

    /**
     * Construct a Bounds2d for a Collection of Drawable2d objects.
     * @param drawableCollection Collection&lt;Drawable2d&gt;; the collection
     * @throws NullPointerException when the collection is null, or contains null values
     * @throws IllegalArgumentException when the collection is empty
     */
    public Bounds2d(final Collection<Drawable2d> drawableCollection) throws NullPointerException, IllegalArgumentException
    {
        this(new Iterator<Point2d>()
        {
            /** Iterator that iterates over the collection. */
            private Iterator<Drawable2d> collectionIterator = ensureHasOne(drawableCollection.iterator());

            /** Iterator that generates Point2d objects for the currently selected element of the collection. */
            private Iterator<? extends Point2d> currentIterator = this.collectionIterator.next().getPoints();

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
            public Point2d next()
            {
                Point2d result = this.currentIterator.next();
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
        });
    }

    /**
     * Verify that the iterator has something to return.
     * @param iterator Iterator&lt;Drawable2d&gt;; the iterator
     * @return Iterator&lt;Drawable2d&gt;; the iterator
     * @throws NullPointerException when the iterator is null
     * @throws IllegalArgumentException when the hasNext method of the iterator returns false
     */
    static Iterator<Drawable2d> ensureHasOne(final Iterator<Drawable2d> iterator)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.when(!iterator.hasNext(), IllegalArgumentException.class, "Collection may not be empty");
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point2d> getPoints()
    {
        Point2d[] array = new Point2d[] { new Point2d(this.minX, this.minY), new Point2d(this.minX, this.maxY),
                new Point2d(this.maxX, this.minY), new Point2d(this.maxX, this.maxY) };
        return Arrays.stream(array).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 4;
    }

    /**
     * Check if this Bounds2d contains a given point. Contains considers a point <b>on</b> the border of this Bounds2d to be
     * outside.
     * @param point Point2d; the point
     * @return boolean; true this Bounds2d contains the point; false if this Bounds2d does <b>not</b> contain the point
     * @throws NullPointerException when point is null
     */
    public boolean contains(final Point2d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return contains(point.x, point.y);
    }

    /**
     * Check if this Bounds2d contains a point. Contains considers a point <b>on</b> the border of this Bounds2d to be outside.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @return boolean; whether this Bounds2d contains the point
     * @throws IllegalArgumentException when any of the coordinates is NaN
     */
    public boolean contains(final double x, final double y) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y), IllegalArgumentException.class, "coordinates must be numbers (not NaN)");
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY;
    }

    /**
     * Check if this Bounds2d completely contains a Drawable2d.
     * @param drawable Drawable2d; the object for which to check if it is completely contained within this Bounds2d.
     * @return boolean; false if any point of the Drawable2d is on or outside one of the borders of this Bounds2d; true when all
     *         points of the Drawable2d are contained within this Bounds2d.
     * @throws NullPointerException when drawable2d is null
     */
    public boolean contains(final Drawable2d drawable) throws NullPointerException
    {
        Throw.whenNull(drawable, "drawable cannot be null");
        for (Iterator<? extends Point2d> iterator = drawable.getPoints(); iterator.hasNext();)
        {
            if (!contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if this Bounds2d contains a point. Covers returns true when the point is on, or within the border of this Bounds2d.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @return boolean; whether this Bounds2d, including its borders, contains the point
     */
    public boolean covers(final double x, final double y)
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y), IllegalArgumentException.class, "coordinates must be numbers (not NaN)");
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY;
    }

    /**
     * Check if this Bounds2d contains a point. Covers returns true when the point is on, or within the border of this Bounds2d.
     * @param point Point2d; the point
     * @return boolean; whether this Bounds2d, including its borders, contains the point
     * @throws NullPointerException when point is null
     */
    public boolean covers(final Point2d point)
    {
        Throw.whenNull(point, "point cannot be null");
        return covers(point.x, point.y);
    }

    /** {@inheritDoc} */
    @Override
    public boolean covers(final Bounds2d otherBounds2d) throws NullPointerException
    {
        Throw.whenNull(otherBounds2d, "otherBounds2d cannot be null");
        return covers(otherBounds2d.minX, otherBounds2d.minY) && covers(otherBounds2d.maxX, otherBounds2d.maxY);
    }

    /** {@inheritDoc} */
    @Override
    public boolean disjoint(final Bounds2d otherBounds2d) throws NullPointerException
    {
        Throw.whenNull(otherBounds2d, "otherBounds2d cannot be null");
        return otherBounds2d.minX >= this.maxX || otherBounds2d.maxX <= this.minX || otherBounds2d.minY >= this.maxY
                || otherBounds2d.maxY <= this.minY;
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(final Bounds2d otherBounds2d) throws NullPointerException
    {
        return !disjoint(otherBounds2d);
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d intersection(final Bounds2d otherBounds2d)
    {
        Throw.whenNull(otherBounds2d, "otherBounds2d cannot be null");
        if (disjoint(otherBounds2d))
        {
            return null;
        }
        return new Bounds2d(Math.max(this.minX, otherBounds2d.minX), Math.min(this.maxX, otherBounds2d.maxX),
                Math.max(this.minY, otherBounds2d.minY), Math.min(this.maxY, otherBounds2d.maxY));
    }

    /**
     * Return an AWT Rectangle2D that covers the same area as this Bounds2d.
     * @return Rectangle2D; the rectangle that covers the same area as this Bounds2d
     */
    public Rectangle2D toRectangle2D()
    {
        return new Rectangle2D.Double(this.minX, this.minY, this.maxX - this.minX, this.maxY - this.minY);
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

    /** {@inheritDoc} */
    @Override
    public Point2d midPoint()
    {
        return new Point2d((this.minX + this.maxX) / 2, (this.minY + this.maxY) / 2);
    }

    /**
     * Return the area of this Bounds2d.
     * @return double; the area of this Bounds2d
     */
    public double getArea()
    {
        return getDeltaX() * getDeltaY();
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return toString("%f");
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[absoluteX[%2$s : %2$s], absoluteY[%2$s : %2$s]]",
                doNotIncludeClassName ? "" : "Bounds2d ", doubleFormat);
        return String.format(Locale.US, format, this.minX, this.maxX, this.minY, this.maxY);
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
        temp = Double.doubleToLongBits(this.minX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minY);
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
        Bounds2d other = (Bounds2d) obj;
        if (Double.doubleToLongBits(this.maxX) != Double.doubleToLongBits(other.maxX))
            return false;
        if (Double.doubleToLongBits(this.maxY) != Double.doubleToLongBits(other.maxY))
            return false;
        if (Double.doubleToLongBits(this.minX) != Double.doubleToLongBits(other.minX))
            return false;
        if (Double.doubleToLongBits(this.minY) != Double.doubleToLongBits(other.minY))
            return false;
        return true;
    }

}
