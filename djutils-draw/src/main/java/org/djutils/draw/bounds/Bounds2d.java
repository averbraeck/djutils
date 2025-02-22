package org.djutils.draw.bounds;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * A Bounds2d stores the rectangular 2D bounds of a 2d object, or a collection of 2d objects. The Bounds2d is an immutable
 * object.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Bounds2d implements Drawable2d, Bounds<Bounds2d, Point2d>
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
     * @param minX the lower bound for x
     * @param maxX the upper bound for x
     * @param minY the lower bound for y
     * @param maxY the upper bound for y
     * @throws ArithmeticException when <code>minX</code>, <code>maxX</code>, <code>minY</code>, or <code>maxY</code> is
     *             <code>NaN</code>
     * @throws IllegalArgumentException when <code>minX</code> &gt; <code>maxX</code>, or <code>minY</code> &gt;
     *             <code>maxY</code>
     */
    public Bounds2d(final double minX, final double maxX, final double minY, final double maxY)
            throws ArithmeticException, IllegalArgumentException
    {
        Throw.whenNaN(minX, "minX");
        Throw.whenNaN(maxX, "maxX");
        Throw.whenNaN(minY, "minY");
        Throw.whenNaN(maxY, "maxY");
        Throw.when(minX > maxX || minY > maxY, IllegalArgumentException.class,
                "lower bound for each dimension should be less than or equal to its upper bound");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Constructs a new Bounds2d around the origin (0, 0).
     * @param deltaX the deltaX value around the origin
     * @param deltaY the deltaY value around the origin
     * @throws ArithmeticException when <code>deltaX</code>, or <code>deltaY</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>deltaX &lt; 0.0</code>, or <code>deltaY &lt; 0.0</code>
     */
    public Bounds2d(final double deltaX, final double deltaY)
    {
        this(-0.5 * deltaX, 0.5 * deltaX, -0.5 * deltaY, 0.5 * deltaY);
    }

    /**
     * Construct a Bounds2d from some collection of points, finding the lowest and highest x and y coordinates.
     * @param points Iterator that will generate all the points for which to construct a
     *            Bounds2d
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when the <code>points</code> iterator provides zero points
     */
    public Bounds2d(final Iterator<? extends Point2d> points)
    {
        Throw.whenNull(points, "points");
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
     * @param points the points to construct a Bounds2d from
     * @throws NullPointerException when <code>points</code> is <code>null</code>
     * @throws IllegalArgumentException when zero points are provided
     */
    public Bounds2d(final Point2d[] points)
    {
        this(Arrays.stream(Throw.whenNull(points, "points")).iterator());
    }

    /**
     * Construct a Bounds2d for a Drawable2d.
     * @param drawable2d any object that implements the Drawable2d interface
     * @throws NullPointerException when <code>drawable2d</code> is <code>null</code>
     */
    public Bounds2d(final Drawable2d drawable2d)
    {
        this(Throw.whenNull(drawable2d, "drawable2d").iterator());
    }

    /**
     * Construct a Bounds2d for several Drawable2d objects.
     * @param drawable2d the Drawable2d objects
     * @throws NullPointerException when the <code>drawable2d</code> array is <code>null</code>, or contains a <code>null</code>
     *             value
     * @throws IllegalArgumentException when the length of the <code>drawable2d</code> array is 0
     */
    public Bounds2d(final Drawable2d... drawable2d)
    {
        this(pointsOf(drawable2d));
    }

    /**
     * Verify that the array contains at least one entry.
     * @param drawable2dArray array of Drawable2d objects
     * @return the array
     * @throws NullPointerException when <code>drawable2darray</code> is <code>null</code>
     * @throws IllegalArgumentException when the <code>drawable2dArray</code> contains 0 elements
     */
    static Drawable2d[] ensureHasOne(final Drawable2d[] drawable2dArray)
    {
        Throw.whenNull(drawable2dArray, "drawable2dArray");
        Throw.when(drawable2dArray.length == 0, IllegalArgumentException.class, "Array must contain at least one value");
        return drawable2dArray;
    }

    /**
     * Return an iterator that will return all points of one or more Drawable objects.
     * @param drawable2d the Drawable objects
     * @return iterator that will return all points of the Drawable objects
     * @throws NullPointerException when <code>drawable2d</code> is <code>null</code>, or contains a <code>null</code> value
     * @throws IllegalArgumentException when <code>drawable2d</code> is empty
     */
    public static Iterator<Point2d> pointsOf(final Drawable2d... drawable2d)
    {
        return new Iterator<Point2d>()
        {
            /** Index in the argument array. */
            private int nextArgument = 0;

            /** Iterator over the Point2d objects in the current Drawable2d. */
            private Iterator<? extends Point2d> currentIterator = ensureHasOne(drawable2d)[0].iterator();

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
                this.currentIterator = drawable2d[this.nextArgument].iterator();
                return this.currentIterator.next(); // Cannot fail because every Drawable has at least one point
            }
        };
    }

    /**
     * Construct a Bounds2d for a Collection of Drawable2d objects.
     * @param drawableCollection the collection
     * @throws NullPointerException when the <code>drawableCollection</code> is <code>null</code>, or contains a
     *             <code>null</code> value
     * @throws IllegalArgumentException when the <code>drawableCollection</code> is empty
     */
    public Bounds2d(final Collection<Drawable2d> drawableCollection)
    {
        this(pointsOf(drawableCollection));
    }

    /**
     * Return an iterator that will return all points of one or more Drawable2d objects.
     * @param drawableCollection the collection of Drawable2d objects
     * @return iterator that will return all points of the Drawable objects
     * @throws NullPointerException when the <code>drawableCollection</code> is <code>null</code>, or contains a
     *             <code>null</code> value
     * @throws IllegalArgumentException when the <code>drawableCollection</code> is empty
     */
    public static Iterator<Point2d> pointsOf(final Collection<Drawable2d> drawableCollection)
    {
        return new Iterator<Point2d>()
        {
            /** Iterator that iterates over the collection. */
            private Iterator<Drawable2d> collectionIterator = ensureHasOne(drawableCollection.iterator());

            /** Iterator that generates Point2d objects for the currently selected element of the collection. */
            private Iterator<? extends Point2d> currentIterator = this.collectionIterator.next().iterator();

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
                        this.currentIterator = this.collectionIterator.next().iterator();
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
     * @param iterator the iterator
     * @return the iterator
     * @throws NullPointerException when the <code>iterator</code> is <code>null</code>
     * @throws IllegalArgumentException when the <code>hasNext</code> method of the <code>iterator</code> returns
     *             <code>false</code> before even one <code>Drawable2d</code> was delivered
     */
    public static Iterator<Drawable2d> ensureHasOne(final Iterator<Drawable2d> iterator)
    {
        Throw.when(!iterator.hasNext(), IllegalArgumentException.class, "Collection may not be empty");
        return iterator;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Point2d> iterator()
    {
        Point2d[] array = new Point2d[] {new Point2d(this.minX, this.minY), new Point2d(this.minX, this.maxY),
                new Point2d(this.maxX, this.minY), new Point2d(this.maxX, this.maxY)};
        return Arrays.stream(array).iterator();
    }

    @Override
    public int size()
    {
        return 4;
    }

    /**
     * Check if this Bounds2d contains a point. Contains considers a point <b>on</b> the border of this Bounds2d to be outside.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return whether this Bounds2d contains the point
     * @throws ArithmeticException when <code>x</code>, or <code>y</code> is <code>NaN</code>
     */
    public boolean contains(final double x, final double y)
    {
        Throw.whenNaN(x, "x");
        Throw.whenNaN(y, "y");
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY;
    }

    @Override
    public boolean contains(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return contains(point.x, point.y);
    }

    @Override
    public boolean contains(final Bounds2d otherBounds) throws NullPointerException
    {
        Throw.whenNull(otherBounds, "otherBounds");
        return contains(otherBounds.minX, otherBounds.minY) && contains(otherBounds.maxX, otherBounds.maxY);
    }

    /**
     * Check if this Bounds2d covers a point. Covers returns <code>true</code> when the point is on, or inside this Bounds2d.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return whether this Bounds2d, including its borders, contains the point
     * @throws ArithmeticException when <code>x</code>, or <code>y</code> is <code>NaN</code>
     */
    public boolean covers(final double x, final double y)
    {
        Throw.whenNaN(x, "x");
        Throw.whenNaN(y, "y");
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY;
    }

    @Override
    public boolean covers(final Point2d point)
    {
        Throw.whenNull(point, "point");
        return covers(point.x, point.y);
    }

    @Override
    public boolean covers(final Bounds2d otherBounds)
    {
        Throw.whenNull(otherBounds, "otherBounds");
        return covers(otherBounds.minX, otherBounds.minY) && covers(otherBounds.maxX, otherBounds.maxY);
    }

    @Override
    public boolean disjoint(final Bounds2d otherBounds)
    {
        Throw.whenNull(otherBounds, "otherBounds");
        return otherBounds.minX > this.maxX || otherBounds.maxX < this.minX || otherBounds.minY > this.maxY
                || otherBounds.maxY < this.minY;
    }

    @Override
    public boolean intersects(final Bounds2d otherBounds2d)
    {
        return !disjoint(otherBounds2d);
    }

    @Override
    public Bounds2d intersection(final Bounds2d otherBounds2d)
    {
        Throw.whenNull(otherBounds2d, "otherBounds2d");
        if (disjoint(otherBounds2d))
        {
            return null;
        }
        return new Bounds2d(Math.max(this.minX, otherBounds2d.minX), Math.min(this.maxX, otherBounds2d.maxX),
                Math.max(this.minY, otherBounds2d.minY), Math.min(this.maxY, otherBounds2d.maxY));
    }

    /**
     * Return an AWT Rectangle2D that covers the same area as this Bounds2d.
     * @return java.awt.geom.Rectangle2D; the rectangle that covers the same area as this Bounds2d
     */
    public java.awt.geom.Rectangle2D toRectangle2D()
    {
        return new java.awt.geom.Rectangle2D.Double(this.minX, this.minY, this.maxX - this.minX, this.maxY - this.minY);
    }

    @Override
    public double getMinX()
    {
        return this.minX;
    }

    @Override
    public double getMaxX()
    {
        return this.maxX;
    }

    @Override
    public double getMinY()
    {
        return this.minY;
    }

    @Override
    public double getMaxY()
    {
        return this.maxY;
    }

    @Override
    public Point2d midPoint()
    {
        return new Point2d((this.minX + this.maxX) / 2, (this.minY + this.maxY) / 2);
    }

    /**
     * Return the area of this Bounds2d.
     * @return the area of this Bounds2d
     */
    public double getArea()
    {
        return getDeltaX() * getDeltaY();
    }

    @Override
    public Bounds2d getBounds()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return toString("%f");
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format =
                String.format("%1$s[x[%2$s : %2$s], y[%2$s : %2$s]]", doNotIncludeClassName ? "" : "Bounds2d ", doubleFormat);
        return String.format(Locale.US, format, this.minX, this.maxX, this.minY, this.maxY);
    }

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
