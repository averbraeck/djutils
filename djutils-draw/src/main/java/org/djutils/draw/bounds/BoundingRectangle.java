package org.djutils.draw.bounds;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;

import org.djutils.draw.d0.Point;
import org.djutils.draw.d0.Point2d;
import org.djutils.draw.d1.Line;
import org.djutils.draw.d2.Area;
import org.djutils.exceptions.Throw;

/**
 * ABoundingRectangle contains the rectangular 2D bounds of an object, ignoring the z-coordinate. The bounding rectangle is
 * implemented as an immutable object. An empty version to denote, e.g., a non-intersection, can be created with the empty
 * constructor. The boundary values will be NaN in that case.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class BoundingRectangle implements Serializable
{
    /** */
    private static final long serialVersionUID = 20200829L;

    /** the lower bound for x, or NaN for an empty bounding box. */
    private final double minX;

    /** the lower bound for y, or NaN for an empty bounding box. */
    private final double minY;

    /** the upper bound for x, or NaN for an empty bounding box. */
    private final double maxX;

    /** the upper bound for y, or NaN for an empty bounding box. */
    private final double maxY;

    /** the empty bounding rectangle for reuse. Since boundingRectangles are immmutable, only one instance is needed. */
    public static final BoundingRectangle EMPTY_ENVELOPE = new BoundingRectangle();

    /**
     * Create an empty bounding rectangle, with NaN for all bounds.
     */
    private BoundingRectangle()
    {
        this.minX = Double.NaN;
        this.minY = Double.NaN;
        this.maxX = Double.NaN;
        this.maxY = Double.NaN;
    }

    /**
     * Create a bounding rectangle by providing its lower and upper bounds.
     * @param minX double; the lower bound for x, or NaN for an empty bounding box
     * @param maxX double; the upper bound for x, or NaN for an empty bounding box
     * @param minY double; the lower bound for y, or NaN for an empty bounding box
     * @param maxY double; the upper bound for y, or NaN for an empty bounding box
     * @throws IllegalArgumentException when lower bounds are larger than upper boundingBox
     */
    public BoundingRectangle(final double minX, final double maxX, final double minY, final double maxY)
    {
        Throw.when(minX > maxX || minY > maxY, IllegalArgumentException.class,
                "lower bound for a dimension should be less than or equal to its upper bound");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Create a bounding rectangle from an array of points, finding the lowest and highest x, y, and z coordinates.
     * @param points Point[]; the array of points to construct a bounding rectangle from
     */
    public BoundingRectangle(final Point[] points)
    {
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMaxX = -Double.NEGATIVE_INFINITY;
        double tempMaxY = -Double.NEGATIVE_INFINITY;
        for (Point point : points)
        {
            double x = point.getX();
            double y = point.getY();
            if (x < tempMinX)
                tempMinX = x;
            else if (x > tempMaxX)
                tempMaxX = x;
            if (y < tempMinY)
                tempMinY = y;
            else if (y > tempMaxY)
                tempMaxY = y;
        }
        this.minX = tempMinX;
        this.minY = tempMinY;
        this.maxX = tempMaxX;
        this.maxY = tempMaxY;
    }

    /**
     * Create a bounding rectangle from a collection of points, finding the lowest and highest x, y, and z coordinates.
     * @param points Collection&lt;Point&gt;; the collection of points to construct a bounding rectangle from
     */
    public BoundingRectangle(final Collection<Point> points)
    {
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMaxX = -Double.NEGATIVE_INFINITY;
        double tempMaxY = -Double.NEGATIVE_INFINITY;
        for (Point point : points)
        {
            double x = point.getX();
            double y = point.getY();
            if (x < tempMinX)
                tempMinX = x;
            else if (x > tempMaxX)
                tempMaxX = x;
            if (y < tempMinY)
                tempMinY = y;
            else if (y > tempMaxY)
                tempMaxY = y;
        }
        this.minX = tempMinX;
        this.minY = tempMinY;
        this.maxX = tempMaxX;
        this.maxY = tempMaxY;
    }

    /**
     * Construct a bounding rectangle based on the coordinates of a line.
     * @param line Line; the line
     * @throws NullPointerException when line is null
     */
    public BoundingRectangle(final Line line)
    {
        this(line.getPointArray());
    }

    /**
     * Construct a bounding rectangle based on the coordinates of an area.
     * @param area Area; the area
     * @throws NullPointerException when area is null
     */
    public BoundingRectangle(final Area area)
    {
        this(area.getBoundaryArray());
    }

    public boolean contains(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return contains(point.getX(), point.getY());
    }

    public boolean contains(final double x, final double y)
    {
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY;
    }

    public boolean contains(final BoundingRectangle boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        return contains(boundingRectangle.minX, boundingRectangle.minY)
                && contains(boundingRectangle.maxX, boundingRectangle.maxY);
    }

    public Point centroid()
    {
        return new Point2d((this.maxX - this.minX) / 2.0, (this.maxY - this.minY) / 2.0);
    }

    public boolean covers(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return covers(point.getX(), point.getY());
    }

    public boolean covers(final double x, final double y)
    {
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY;
    }

    public boolean covers(final BoundingRectangle boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        return covers(boundingRectangle.minX, boundingRectangle.minY) && covers(boundingRectangle.maxX, boundingRectangle.maxY);
    }

    public boolean disjoint(final BoundingRectangle boundingRectangle)
    {
        return !intersects(boundingRectangle);
    }

    public boolean intersects(final BoundingRectangle boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        return !(boundingRectangle.minX > this.maxX || boundingRectangle.maxX < this.minX || boundingRectangle.minY > this.maxY
                || boundingRectangle.maxY < this.minY);
    }

    public BoundingRectangle intersection(final BoundingRectangle boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        if (isEmpty() || boundingRectangle.isEmpty() || !intersects(boundingRectangle))
        {
            return EMPTY_ENVELOPE;
        }
        double tempMinX = this.minX > boundingRectangle.minX ? this.minX : boundingRectangle.minX;
        double tempMinY = this.minY > boundingRectangle.minY ? this.minY : boundingRectangle.minY;
        double tempMaxX = this.maxX < boundingRectangle.maxX ? this.maxX : boundingRectangle.maxX;
        double tempMaxY = this.maxY < boundingRectangle.maxY ? this.maxY : boundingRectangle.maxY;
        return new BoundingRectangle(tempMinX, tempMaxX, tempMinY, tempMaxY);
    }

    /**
     * Return whether the boundingRectangle is empty (indicated by one or more bounds containing NaN).
     * @return boolean; whether the boundingRectangle is empty
     */
    public boolean isEmpty()
    {
        return Double.isNaN(this.minX) || Double.isNaN(this.maxX) || Double.isNaN(this.minY) || Double.isNaN(this.maxY);
    }

    public Rectangle2D toRectangle2D()
    {
        return new Rectangle2D.Double(this.minX, this.minY, this.maxX - this.minX, this.maxY - this.minY);
    }

    /**
     * Return the lower bound for x, or NaN for an empty boundingRectangle
     * @return double; the lower bound for x, or NaN for an empty boundingRectangle
     */
    public double getMinX()
    {
        return this.minX;
    }

    /**
     * Return the upper bound for x, or NaN for an empty boundingRectangle
     * @return double; the upper bound for x, or NaN for an empty boundingRectangle
     */
    public double getMaxX()
    {
        return this.maxX;
    }

    /**
     * Return the lower bound for y, or NaN for an empty boundingRectangle
     * @return double; the lower bound for y, or NaN for an empty boundingRectangle
     */
    public double getMinY()
    {
        return this.minY;
    }

    /**
     * Return the upper bound for y, or NaN for an empty boundingRectangle
     * @return double; the upper bound for y, or NaN for an empty boundingRectangle
     */
    public double getMaxY()
    {
        return this.maxY;
    }

    public double getWidth()
    {
        return getMaxX() - getMinX();
    }

    public double getHeight()
    {
        return getMaxY() - getMinY();
    }

    public double getArea()
    {
        return getWidth() * getHeight();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Envelope[x[" + this.minX + " : " + this.maxX + "], y[" + this.minY + " : " + this.maxY + "]]";
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
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoundingRectangle other = (BoundingRectangle) obj;
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
