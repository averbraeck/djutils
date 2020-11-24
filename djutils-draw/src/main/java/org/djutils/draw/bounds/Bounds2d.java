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
public class Bounds2d implements Serializable
{
    /** */
    private static final long serialVersionUID = 20200829L;

    /** The lower bound for x, or NaN for an empty bounding box. */
    private final double minX;

    /** The lower bound for y, or NaN for an empty bounding rectangle. */
    private final double minY;

    /** The upper bound for x, or NaN for an empty bounding rectangle. */
    private final double maxX;

    /** The upper bound for y, or NaN for an empty bounding rectangle. */
    private final double maxY;

    /** The empty bounding rectangle for reuse. Since boundingRectangles are immmutable, only one instance is needed. */
    public static final Bounds2d EMPTY_BOUNDING_RECTANGLE = new Bounds2d();

    /**
     * Create an empty bounding rectangle, with NaN for all bounds.
     */
    private Bounds2d()
    {
        this.minX = Double.NaN;
        this.minY = Double.NaN;
        this.maxX = Double.NaN;
        this.maxY = Double.NaN;
    }

    /**
     * Construct a bounding rectangle by providing its lower and upper bounds.
     * @param minX double; the lower bound for x, or NaN for an empty bounding rectangle
     * @param maxX double; the upper bound for x, or NaN for an empty bounding rectangle
     * @param minY double; the lower bound for y, or NaN for an empty bounding rectangle
     * @param maxY double; the upper bound for y, or NaN for an empty bounding rectangle
     * @throws IllegalArgumentException when lower bounds are larger than upper boundingRectangle, or any of the bounds is NaN
     */
    public Bounds2d(final double minX, final double maxX, final double minY, final double maxY)
            throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(minX) || Double.isNaN(maxX) || Double.isNaN(minY) || Double.isNaN(maxY),
                IllegalArgumentException.class, "bounds must be numbers (not NaN)");
        Throw.when(minX > maxX || minY > maxY, IllegalArgumentException.class,
                "lower bound for a dimension should be less than or equal to its upper bound");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Constructs a new BoundingRectangle around (0, 0).
     * @param deltaX double; the deltaX value around the origin
     * @param deltaY double; the deltaY value around the origin
     * @throws IllegalArgumentException when one of the delta values is less than zero
     */
    public Bounds2d(final double deltaX, final double deltaY)
    {
        Throw.when(deltaX < 0.0 || deltaY < 0.0, IllegalArgumentException.class, "delta values sould be >= 0");
        Throw.when(Double.isNaN(deltaX) || Double.isNaN(deltaY), IllegalArgumentException.class, "Nan value not permitted");
        this.minX = -0.5 * deltaX;
        this.maxX = 0.5 * deltaX;
        this.minY = -0.5 * deltaY;
        this.maxY = 0.5 * deltaY;
    }

    /**
     * Construct a bounding rectangle from an array of points, finding the lowest and highest x, y, and z coordinates.
     * @param points Point[]; the array of points to construct a bounding rectangle from
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when zero points are provided
     */
    public Bounds2d(final Point[] points) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(points, "points may not be null");
        Throw.when(points.length == 0, IllegalArgumentException.class, "must have at least one point");
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMaxX = Double.NEGATIVE_INFINITY;
        double tempMaxY = Double.NEGATIVE_INFINITY;
        for (Point point : points)
        {
            double x = point.getX();
            double y = point.getY();
            if (x < tempMinX)
            {
                tempMinX = x;
            }
            if (x > tempMaxX)
            {
                tempMaxX = x;
            }
            if (y < tempMinY)
            {
                tempMinY = y;
            }
            if (y > tempMaxY)
            {
                tempMaxY = y;
            }
        }
        this.minX = tempMinX;
        this.minY = tempMinY;
        this.maxX = tempMaxX;
        this.maxY = tempMaxY;
    }

    /**
     * Construct a bounding rectangle from a collection of points, finding the lowest and highest x, y, and z coordinates.
     * @param points Collection&lt;Point&gt;; the collection of points to construct a bounding rectangle from
     * @throws NullPointerException when points is null
     * @throws IllegalArgumentException when zero points are provided
     */
    public Bounds2d(final Collection<Point> points) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(points, "points may not be null");
        Throw.when(points.isEmpty(), IllegalArgumentException.class, "must have at least one point");
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMaxX = Double.NEGATIVE_INFINITY;
        double tempMaxY = Double.NEGATIVE_INFINITY;
        for (Point point : points)
        {
            double x = point.getX();
            double y = point.getY();
            if (x < tempMinX)
            {
                tempMinX = x;
            }
            if (x > tempMaxX)
            {
                tempMaxX = x;
            }
            if (y < tempMinY)
            {
                tempMinY = y;
            }
            if (y > tempMaxY)
            {
                tempMaxY = y;
            }
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
    public Bounds2d(final Line line) throws NullPointerException
    {
        this(Throw.whenNull(line, "line may not be null").getPointArray());
    }

    /**
     * Construct a bounding rectangle based on the coordinates of an area.
     * @param area Area; the area
     * @throws NullPointerException when area is null
     */
    public Bounds2d(final Area area) throws NullPointerException
    {
        this(Throw.whenNull(area, "area may not be null").getBoundaryArray());
    }

    /**
     * Check if the bounding rectangle contains a point. Contains returns false when the point is on the border of the
     * rectangle.
     * @param point Point; the point
     * @return boolean; whether the bounding rectangle contains the point
     * @throws NullPointerException when point is null
     */
    public boolean contains(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return contains(point.getX(), point.getY());
    }

    /**
     * Check if the bounding rectangle contains a point. Contains returns false when the point is on the border of the
     * rectangle.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @return boolean; whether the bounding rectangle contains the point with the given coordinates
     */
    public boolean contains(final double x, final double y)
    {
        return (!isEmpty()) && x > this.minX && x < this.maxX && y > this.minY && y < this.maxY;
    }

    /**
     * Check if the bounding rectangle contains another bounding rectangle. Contains returns false when one of the edges of the
     * other bounding rectangle is overlapping with the border of this bounding rectangle.
     * @param boundingRectangle BoundingRectangle; the bounding rectangle for which to check if it is completely contained
     *            within this bounding rectangle
     * @return boolean; whether the bounding rectangle contains the provided bounding rectangle
     * @throws NullPointerException when boundingRectangle is null
     */
    public boolean contains(final Bounds2d boundingRectangle) throws NullPointerException
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        return (!boundingRectangle.isEmpty()) && contains(boundingRectangle.minX, boundingRectangle.minY)
                && contains(boundingRectangle.maxX, boundingRectangle.maxY);
    }

    /**
     * Return the centroid of this bounding rectangle.
     * @return Point; the centroid of this bounding rectangle
     * @throws NullPointerException when this BoundingRectanble is the EMPTY_BOUNDING_RECTANGLE
     */
    public Point centroid()
    {
        Throw.when(isEmpty(), NullPointerException.class, "The empty BoundingRectangle has no centroid");
        return new Point2d((this.maxX - this.minX) / 2.0, (this.maxY - this.minY) / 2.0);
    }

    /**
     * Check if the bounding rectangle contains a point. Covers returns true when the point is on the border of the rectangle.
     * @param point Point; the point
     * @return boolean; whether the bounding rectangle contains the point, including the borders
     * @throws NullPointerException when point is null
     */
    public boolean covers(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return covers(point.getX(), point.getY());
    }

    /**
     * Check if the bounding rectangle contains a point. Covers returns true when the point is on the border of the rectangle.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @return boolean; whether the bounding rectangle contains the point with the given coordinates, including the borders
     */
    public boolean covers(final double x, final double y)
    {
        return (!isEmpty()) && x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY;
    }

    /**
     * Check if the bounding rectangle contains another bounding rectangle. Covers returns true when one of the edges of the
     * other bounding rectangle is overlapping with the border of this bounding rectangle.
     * @param boundingRectangle BoundingRectangle; the bounding rectangle for which to check if it is contained within this
     *            bounding rectangle
     * @return boolean; whether the bounding rectangle contains the provided bounding rectangle, including overlapping borders
     * @throws NullPointerException when boundingRectangle is null
     */
    public boolean covers(final Bounds2d boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        return covers(boundingRectangle.minX, boundingRectangle.minY) && covers(boundingRectangle.maxX, boundingRectangle.maxY);
    }

    /**
     * Return whether this bounding rectangle is disjoint from another bounding rectangle. Touching at the edge is seen as
     * disjoint.
     * @param boundingRectangle BoundingRectangle; the other bounding rectangle
     * @return boolean; whether this bounding rectangle is disjoint from another bounding rectangle
     * @throws NullPointerException when boundingRectangle is null
     */
    public boolean disjoint(final Bounds2d boundingRectangle)
    {
        return !intersects(boundingRectangle);
    }

    /**
     * Return whether this bounding rectangle intersects with another bounding rectangle. Touching at the edge is not seen as
     * intersecting.
     * @param boundingRectangle BoundingRectangle; the other bounding rectangle
     * @return boolean; whether this bounding rectangle intersects with another bounding rectangle
     * @throws NullPointerException when boundingRectangle is null
     */
    public boolean intersects(final Bounds2d boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        return !(isEmpty() || boundingRectangle.isEmpty() || boundingRectangle.minX > this.maxX
                || boundingRectangle.maxX < this.minX || boundingRectangle.minY > this.maxY
                || boundingRectangle.maxY < this.minY);
    }

    /**
     * Return the extent of this bounding box in the x-direction.
     * @return double; the extent of this bounding box in the x-direction, or NaN if this is the EMPTY_BOUNDING_BOX
     */
    public double getDeltaX()
    {
        return getMaxX() - getMinX();
    }

    /**
     * Return the extent of this bounding box in the y-direction.
     * @return double; the extent of this bounding box in the y-direction, or NaN if this is the EMPTY_BOUNDING_BOX
     */
    public double getDeltaY()
    {
        return getMaxY() - getMinY();
    }

    /**
     * Return the intersecting bounding rectangle of this bounding rectangle and another bounding rectangle. Touching at the
     * edge is not seen as intersecting. In case there is no intersection, the empty bounding rectangle is returned.
     * @param boundingRectangle BoundingRectangle; the other bounding rectangle
     * @return BoundingRectangle; the intersecting bounding rectangle of this bounding rectangle and another bounding rectangle
     *         or the empty bounding rectangle in case there is no intersection
     * @throws NullPointerException when boundingRectangle is null
     */
    public Bounds2d intersection(final Bounds2d boundingRectangle)
    {
        Throw.whenNull(boundingRectangle, "boundingRectangle cannot be null");
        if (isEmpty() || boundingRectangle.isEmpty() || !intersects(boundingRectangle))
        {
            return EMPTY_BOUNDING_RECTANGLE;
        }
        double tempMinX = this.minX > boundingRectangle.minX ? this.minX : boundingRectangle.minX;
        double tempMinY = this.minY > boundingRectangle.minY ? this.minY : boundingRectangle.minY;
        double tempMaxX = this.maxX < boundingRectangle.maxX ? this.maxX : boundingRectangle.maxX;
        double tempMaxY = this.maxY < boundingRectangle.maxY ? this.maxY : boundingRectangle.maxY;
        return new Bounds2d(tempMinX, tempMaxX, tempMinY, tempMaxY);
    }

    /**
     * Return whether the boundingRectangle is empty (indicated by one or more bounds containing NaN).
     * @return boolean; whether the boundingRectangle is empty
     */
    public boolean isEmpty()
    {
        return this == EMPTY_BOUNDING_RECTANGLE;
    }

    /**
     * Return the rectangle as an AWT Rectangle2D.
     * @return Rectangle2D; the rectangle as an AWT Rectangle2D
     */
    public Rectangle2D toRectangle2D()
    {
        return new Rectangle2D.Double(this.minX, this.minY, this.maxX - this.minX, this.maxY - this.minY);
    }

    /**
     * Return the lower bound for x, or NaN for an empty boundingRectangle
     * @return double; the lower bound for x, or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getMinX()
    {
        return this.minX;
    }

    /**
     * Return the upper bound for x, or NaN for an empty boundingRectangle
     * @return double; the upper bound for x, or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getMaxX()
    {
        return this.maxX;
    }

    /**
     * Return the lower bound for y, or NaN for an empty boundingRectangle
     * @return double; the lower bound for y, or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getMinY()
    {
        return this.minY;
    }

    /**
     * Return the upper bound for y, or NaN for an empty boundingRectangle
     * @return double; the upper bound for y, or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getMaxY()
    {
        return this.maxY;
    }

    /**
     * Return the width of the bounding rectangle (x-direction).
     * @return double; the width of the bounding rectangle or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getWidth()
    {
        return getMaxX() - getMinX();
    }

    /**
     * Return the height of the bounding rectangle (y-direction).
     * @return double; the height of the bounding rectangle or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getHeight()
    {
        return getMaxY() - getMinY();
    }

    /**
     * Return the area of the bounding rectangle.
     * @return double; the area of the bounding rectangle or NaN for the EMPTY_BOUNDING_RECTANGLE
     */
    public double getArea()
    {
        return getWidth() * getHeight();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "BoundingRectangle [x[" + this.minX + " : " + this.maxX + "], y[" + this.minY + " : " + this.maxY + "]]";
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
