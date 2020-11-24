package org.djutils.draw.bounds;

import java.io.Serializable;
import java.util.Collection;

import org.djutils.draw.line.Line;
import org.djutils.draw.point.Point;
import org.djutils.draw.point.Point3d;
import org.djutils.draw.surface.Surface;
import org.djutils.draw.volume.Volume3d;
import org.djutils.exceptions.Throw;

/**
 * Bounds3d is the generic class for the 3D extent of an object to determine whether it must be drawn or not.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds3d implements Serializable
{
    /** */
    private static final long serialVersionUID = 2020829L;

    /** The lower bound for x, or NaN for an empty bounding box. */
    private final double minX;

    /** The lower bound for y, or NaN for an empty bounding box. */
    private final double minY;

    /** The lower bound for z, or NaN for an empty bounding box. */
    private final double minZ;

    /** The upper bound for x, or NaN for an empty bounding box. */
    private final double maxX;

    /** The upper bound for y, or NaN for an empty bounding box. */
    private final double maxY;

    /** The upper bound for z, or NaN for an empty bounding box. */
    private final double maxZ;

    /** The empty bounding box for reuse. Since bounding boxes are immutable, only one instance is needed. */
    public static final Bounds3d EMPTY_BOUNDING_BOX = new Bounds3d();

    /**
     * Create an empty bounding box, with NaN for all bounds.
     */
    private Bounds3d()
    {
        this.minX = Double.NaN;
        this.maxX = Double.NaN;
        this.minY = Double.NaN;
        this.maxY = Double.NaN;
        this.minZ = Double.NaN;
        this.maxZ = Double.NaN;
    }

    /**
     * Construct a bounding box by providing all lower and upper bounds.
     * @param minX double; the lower bound for x, or NaN for an empty bounding box
     * @param maxX double; the upper bound for x, or NaN for an empty bounding box
     * @param minY double; the lower bound for y, or NaN for an empty bounding box
     * @param maxY double; the upper bound for y, or NaN for an empty bounding box
     * @param minZ double; the lower bound for z, or NaN for an empty bounding box
     * @param maxZ double; the upper bound for z, or NaN for an empty bounding box
     * @throws IllegalArgumentException when lower bounds are larger than upper boundingBox or any bound is NaN
     */
    public Bounds3d(final double minX, final double maxX, final double minY, final double maxY, final double minZ,
            final double maxZ)
    {
        Throw.when(minX > maxX || minY > maxY || minZ > maxZ, IllegalArgumentException.class,
                "lower bound for a dimension should be less than or equal to its upper bound");
        Throw.when(Double.isNaN(minX) || Double.isNaN(maxX) || Double.isNaN(minY) || Double.isNaN(maxY) || Double.isNaN(minZ)
                || Double.isNaN(maxZ), IllegalArgumentException.class, "Nan boundary value not permitted");
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    /**
     * Constructs a new Bounds3d around (0, 0, 0).
     * @param deltaX double; the deltaX value around the origin
     * @param deltaY double; the deltaY value around the origin
     * @param deltaZ double; the deltaZ value around the origin
     * @throws IllegalArgumentException when one of the delta values is less than zero
     */
    public Bounds3d(final double deltaX, final double deltaY, final double deltaZ)
    {
        Throw.when(deltaX < 0.0 || deltaY < 0.0 || deltaZ < 0.0, IllegalArgumentException.class, "delta values sould be >= 0");
        Throw.when(Double.isNaN(deltaX) || Double.isNaN(deltaY) || Double.isNaN(deltaZ), IllegalArgumentException.class,
                "Nan value not permitted");
        this.minX = -0.5 * deltaX;
        this.maxX = 0.5 * deltaX;
        this.minY = -0.5 * deltaY;
        this.maxY = 0.5 * deltaY;
        this.minZ = -0.5 * deltaZ;
        this.maxZ = 0.5 * deltaZ;
    }

    /**
     * Report if this Bounds3d is the special EMPTY_BOUNDING_BOX object.
     * @return boolean; true if this Bounds3d is the EMPTY_BOUNDING_BOX object; false if this Bounds3d is not the
     *         EMPTY_BOUNDING_BOX object
     */
    public boolean isEmpty()
    {
        return this == EMPTY_BOUNDING_BOX;
    }

    /**
     * Construct a bounding box from an array of points, finding the lowest and highest x, y, and z coordinates.
     * @param points Point[]; the array of points to construct a bounding box from
     */
    public Bounds3d(final Point[] points)
    {
        Throw.when(points.length == 0, IllegalArgumentException.class, "points may not be empty");
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMinZ = Double.POSITIVE_INFINITY;
        double tempMaxX = Double.NEGATIVE_INFINITY;
        double tempMaxY = Double.NEGATIVE_INFINITY;
        double tempMaxZ = Double.NEGATIVE_INFINITY;
        for (Point point : points)
        {
            double x = point.getX();
            double y = point.getY();
            double z = point.getZ();
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
            if (z < tempMinZ)
            {
                tempMinZ = z;
            }
            if (z > tempMaxZ)
            {
                tempMaxZ = z;
            }
        }
        this.minX = tempMinX;
        this.minY = tempMinY;
        this.minZ = tempMinZ;
        this.maxX = tempMaxX;
        this.maxY = tempMaxY;
        this.maxZ = tempMaxZ;
    }

    /**
     * Construct a bounding box from a collection of points, finding the lowest and highest x, y, and z coordinates.
     * @param points Collection&lt;Point&gt;; the collection of points to construct a bounding box from
     */
    public Bounds3d(final Collection<Point> points)
    {
        Throw.when(points.size() == 0, IllegalArgumentException.class, "points may not be empty");
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMinZ = Double.POSITIVE_INFINITY;
        double tempMaxX = Double.NEGATIVE_INFINITY;
        double tempMaxY = Double.NEGATIVE_INFINITY;
        double tempMaxZ = Double.NEGATIVE_INFINITY;
        for (Point point : points)
        {
            double x = point.getX();
            double y = point.getY();
            double z = point.getZ();
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
            if (z < tempMinZ)
            {
                tempMinZ = z;
            }
            if (z > tempMaxZ)
            {
                tempMaxZ = z;
            }
        }
        this.minX = tempMinX;
        this.minY = tempMinY;
        this.minZ = tempMinZ;
        this.maxX = tempMaxX;
        this.maxY = tempMaxY;
        this.maxZ = tempMaxZ;
    }

    /**
     * Construct a bounding box based on the coordinates of a line.
     * @param line Line; the line
     * @throws NullPointerException when line is null
     */
    public Bounds3d(final Line line)
    {
        this(Throw.whenNull(line, "line cannot be null").getPointArray());
    }

    /**
     * Construct a bounding box based on the coordinates of an area.
     * @param area Area; the area
     * @throws NullPointerException when area is null
     */
    public Bounds3d(final Surface area)
    {
        this(Throw.whenNull(area, "area cannot be null").getBoundaryArray());
    }

    /**
     * Construct a bounding box based on the coordinates of a volume.
     * @param volume Volume3d; the volume
     * @throws NullPointerException when volume is null
     */
    public Bounds3d(final Volume3d volume)
    {
        Throw.whenNull(volume, "volume cannot be null");
        double tempMinX = Double.POSITIVE_INFINITY;
        double tempMinY = Double.POSITIVE_INFINITY;
        double tempMinZ = Double.POSITIVE_INFINITY;
        double tempMaxX = Double.NEGATIVE_INFINITY;
        double tempMaxY = Double.NEGATIVE_INFINITY;
        double tempMaxZ = Double.NEGATIVE_INFINITY;
        for (Line line : volume.getWireframeLines())
        {
            for (Point point : line.getPointArray())
            {
                double x = point.getX();
                double y = point.getY();
                double z = point.getZ();
                Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                        "NaN value not permitted");
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
                if (z < tempMinZ)
                {
                    tempMinZ = z;
                }
                if (z > tempMaxZ)
                {
                    tempMaxZ = z;
                }
            }
        }
        this.minX = tempMinX;
        this.minY = tempMinY;
        this.minZ = tempMinZ;
        this.maxX = tempMaxX;
        this.maxY = tempMaxY;
        this.maxZ = tempMaxZ;
    }

    /**
     * Check if the bounding box contains a point. Contains returns false when the point is on the border of the box.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @param z double; the z-coordinate of the point
     * @return boolean; whether the bounding box contains the point with the given coordinates
     * @throws IllegalArgumentException when any of the coordinates is NaN
     */
    public boolean contains(final double x, final double y, final double z) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "coordinates must be numbers (not NaN)");
        return (!isEmpty()) && x > this.minX && x < this.maxX && y > this.minY && y < this.maxY && z > this.minZ
                && z < this.maxZ;
    }

    /**
     * Check if the bounding box contains a point. Contains returns false when the point is on the border of the box.
     * @param point Point; the point
     * @return boolean; whether the bounding box contains the point
     * @throws NullPointerException when point is null
     */
    public boolean contains(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return (!isEmpty()) && contains(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Check if the bounding box contains another bounding box. Contains returns false when one of the edges of the other
     * bounding box is overlapping with the border of this bounding box.
     * @param boundingBox Bounds3d; the bounding box for which to check if it is completely contained within this bounding
     *            box
     * @return boolean; whether the bounding box contains the provided bounding box
     * @throws NullPointerException when boundingBox is null
     */
    public boolean contains(final Bounds3d boundingBox)
    {
        Throw.whenNull(boundingBox, "boundingBox cannot be null");
        return contains(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ())
                && contains(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
    }

    /**
     * Return the centroid of this bounding box.
     * @return Point; the centroid of this bounding box
     * @throws NullPointerException when this Bounds3d is the EMPTY_BOUNDING_BOX
     */
    public Point centroid() throws NullPointerException
    {
        Throw.when(isEmpty(), NullPointerException.class, "The empty Bounds3d has no centroid");
        return new Point3d((this.getMaxX() - this.getMinX()) / 2.0, (this.getMaxY() - this.getMinY()) / 2.0,
                (this.getMaxZ() - this.getMinZ()) / 2.0);
    }

    /**
     * Check if the bounding box contains a point. Covers returns true when the point is on the border of the box.
     * @param x double; the x-coordinate of the point
     * @param y double; the y-coordinate of the point
     * @param z double; the z-coordinate of the point
     * @return boolean; whether the bounding box contains the point with the given coordinates, including the borders
     * @throws IllegalArgumentException when any of the coordinates is NaN
     */
    public boolean covers(final double x, final double y, final double z) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "coordinates must be numbers (not NaN)");
        return (!this.isEmpty()) && x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ
                && z <= this.maxZ;
    }

    /**
     * Check if the bounding box contains a point. Covers returns true when the point is on the border of the box.
     * @param point Point; the point
     * @return boolean; whether the bounding box contains the point, including the borders
     * @throws NullPointerException when point is null
     */
    public boolean covers(final Point point)
    {
        Throw.whenNull(point, "point cannot be null");
        return covers(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Check if the bounding box contains another bounding box. Covers returns true when one of the edges of the other bounding
     * box is overlapping with the border of this bounding box.
     * @param boundingBox Bounds3d; the bounding box for which to check if it is contained within this bounding box
     * @return boolean; whether the bounding box contains the provided bounding box, including overlapping borders
     * @throws NullPointerException when boundingBox is null
     */
    public boolean covers(final Bounds3d boundingBox)
    {
        Throw.whenNull(boundingBox, "boundingBox cannot be null");
        if (isEmpty() || boundingBox.isEmpty())
        {
            return false;
        }
        return covers(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ())
                && covers(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
    }

    /**
     * Return whether this bounding box intersects with another bounding box. Touching at the edge is not seen as intersecting.
     * @param boundingBox Bounds3d; the other bounding box
     * @return boolean; whether this bounding box intersects with another bounding box
     * @throws NullPointerException when boundingBox is null
     */
    public boolean intersects(final Bounds3d boundingBox)
    {
        Throw.whenNull(boundingBox, "boundingBox cannot be null");
        return !(isEmpty() || boundingBox.isEmpty() || boundingBox.minX > this.maxX || boundingBox.maxX < this.minX
                || boundingBox.minY > this.maxY || boundingBox.maxY < this.minY || boundingBox.minZ > this.maxZ
                || boundingBox.maxZ < this.minZ);
    }

    /**
     * Return the intersecting bounding box of this bounding box and another bounding box. Touching at the edge is not seen as
     * intersecting. In case there is no intersection, the empty bounding box is returned.
     * @param boundingBox Bounds3d; the other bounding box
     * @return Bounds3d; the intersecting bounding box of this bounding box and another bounding box or the empty bounding
     *         box in case there is no intersection
     * @throws NullPointerException when boundingBox is null
     */
    public Bounds3d intersection(final Bounds3d boundingBox)
    {
        Throw.whenNull(boundingBox, "boundingBox cannot be null");
        if (disjoint(boundingBox))
        {
            return EMPTY_BOUNDING_BOX;
        }
        return new Bounds3d(Math.max(this.minX, boundingBox.minX), Math.min(this.maxX, boundingBox.maxX),
                Math.max(this.minY, boundingBox.minY), Math.min(this.maxY, boundingBox.maxY),
                Math.max(this.minZ, boundingBox.minZ), Math.min(this.maxZ, boundingBox.maxZ));
    }

    /**
     * Return whether this bounding box is disjoint from another bounding box. Touching at the edge is seen as disjoint.
     * @param boundingBox Bounds3d; the other bounding box
     * @return boolean; whether this bounding box is disjoint from another bounding box
     * @throws NullPointerException when boundingBox is null
     */
    public boolean disjoint(Bounds3d boundingBox)
    {
        return !intersects(boundingBox);
    }

    /**
     * Return the 2d envelope of this Bounds3d.
     * @return Envelope; the 2d envelope of this bounding box
     * @throws NullPointerException when this is the EMPTY_BOUNDING_BOX
     */
    public Bounds2d envelope() throws NullPointerException
    {
        Throw.when(isEmpty(), NullPointerException.class, "The empty Bounds3d has not envelope");
        return new Bounds2d(this.minX, this.maxX, this.minY, this.maxY);
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
     * Return the extent of this bounding box in the z-direction.
     * @return double; the extent of this bounding box in the z-direction, or NaN if this is the EMPTY_BOUNDING_BOX
     */
    public double getDeltaZ()
    {
        return getMaxZ() - getMinZ();
    }

    /**
     * Return the volume of this bounding box.
     * @return double; the volume of this bounding box, or NaN if this is the EMPTY_BOUNDING_BOX
     */
    public double getVolume()
    {
        return getDeltaX() * getDeltaY() * getDeltaZ();
    }

    /**
     * Return the lower bound for x, or NaN for an empty bounding box
     * @return double; the lower bound for x, or NaN for the EMPTY_BOUNDING_BOX
     */
    public double getMinX()
    {
        return this.minX;
    }

    /**
     * Return the upper bound for x, or NaN for an empty bounding box
     * @return double; the upper bound for x, or NaN for the EMPTY_BOUNDING_BOX
     */
    public double getMaxX()
    {
        return this.maxX;
    }

    /**
     * Return the lower bound for y, or NaN for an empty bounding box
     * @return double; the lower bound for y, or NaN for the EMPTY_BOUNDING_BOX
     */
    public double getMinY()
    {
        return this.minY;
    }

    /**
     * Return the upper bound for y, or NaN for an empty bounding box
     * @return double; the upper bound for y, or NaN for the EMPTY_BOUNDING_BOX
     */
    public double getMaxY()
    {
        return this.maxY;
    }

    /**
     * Return the lower bound for z, or NaN for an empty bounding box
     * @return double; the lower bound for z, or NaN for the EMPTY_BOUNDING_BOX
     */
    public double getMinZ()
    {
        return this.minZ;
    }

    /**
     * Return the upper bound for z, or NaN for an empty bounding box
     * @return double; the upper bound for z, or NaN for the EMPTY_BOUNDING_BOX
     */
    public double getMaxZ()
    {
        return this.maxZ;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Bounds3d [x[" + this.minX + " : " + this.maxX + "], y[" + this.minY + " : " + this.maxY + "], z[" + this.minZ
                + " : " + this.maxZ + "]]";
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
    @Override
    public boolean equals(Object obj)
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
