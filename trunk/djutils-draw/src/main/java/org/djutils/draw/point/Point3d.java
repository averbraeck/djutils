package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.Space3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.exceptions.Throw;

/**
 * A Point3d is an immutable point with an x, y, and z coordinate, stored with double precision. It differs from many Point
 * implementations by being immutable.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point3d implements Drawable3d, Point<Point3d, Space3d>
{
    /** */
    private static final long serialVersionUID = 20201201L;

    /** The x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double x;

    /** The y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double y;

    /** The z-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double z;

    /**
     * Create a new Point with just an x, y and z coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public Point3d(final double x, final double y, final double z)
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new Point with just an x, y and z coordinate, stored with double precision.
     * @param xyz double[]; the x, y and z coordinate
     * @throws NullPointerException when xyz is null
     * @throws IllegalArgumentException when the dimension of xyz is not 3, or a coordinate is NaN
     */
    public Point3d(final double[] xyz) throws NullPointerException, IllegalArgumentException
    {
        this(checkLengthIsThree(Throw.whenNull(xyz, "xyz-point cannot be null"))[0], xyz[1], xyz[2]);
    }

    /**
     * Create an immutable point with just three values, x, y and z, stored with double precision from a Point2d and z.
     * @param point Point2d; a Point2d
     * @param z double; the z coordinate
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when z is NaN
     */
    public Point3d(final Point2d point, final double z) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(z), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = point.x;
        this.y = point.y;
        this.z = z;
    }

    /**
     * Create an immutable point with just three values, x, y and z, stored with double precision from an AWT Point2D and z.
     * @param point Point2D; an AWT Point2D
     * @param z double; the z coordinate
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate, or z is NaN
     */
    public Point3d(final Point2D point, final double z) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(point.getX()) || Double.isNaN(point.getY()), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        Throw.when(Double.isNaN(z), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = point.getX();
        this.y = point.getY();
        this.z = z;
    }

    /**
     * Throw an IllegalArgumentException if the length of the provided array is not three.
     * @param xyz double[]; the provided array
     * @return double[]; the provided array
     * @throws IllegalArgumentException when length of xyz is not three
     */
    private static double[] checkLengthIsThree(final double[] xyz) throws IllegalArgumentException
    {
        Throw.when(xyz.length != 3, IllegalArgumentException.class, "Length of xy-array must be 2");
        return xyz;
    }

    /** {@inheritDoc} */
    @Override
    public final double getX()
    {
        return this.x;
    }

    /** {@inheritDoc} */
    @Override
    public final double getY()
    {
        return this.y;
    }

    /**
     * Return the z-coordinate.
     * @return double; the z-coordinate
     */
    public final double getZ()
    {
        return this.z;
    }

    /** {@inheritDoc} */
    @Override
    public double distanceSquared(final Point3d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        double dx = this.x - otherPoint.x;
        double dy = this.y - otherPoint.y;
        double dz = this.z - otherPoint.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /** {@inheritDoc} */
    @Override
    public double distance(final Point3d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        return Math.sqrt(distanceSquared(otherPoint));
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point3d> getPoints()
    {
        return Arrays.stream(new Point3d[] {this}).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Point2d project() throws DrawRuntimeException
    {
        return new Point2d(this.x, this.y);
    }

    /**
     * Return a new Point with a translation by the provided dx and dy.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return Point3D; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, or dy is NaN
     */
    public Point3d translate(final double dx, final double dy) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class,
                "Translation must be number (not NaN)");
        return new Point3d(this.x + dx, this.y + dy, this.z);
    }

    /**
     * Return a new Point3d with a translation by the provided dx, dy and dz.
     * @param dx double; the x translation
     * @param dy double; the y translation
     * @param dz double; the z translation
     * @return Point3d; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, dy, or dz is NaN
     */
    public Point3d translate(final double dx, final double dy, final double dz) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "dx, dy and dz must be numbers (not NaN)");
        return new Point3d(this.x + dx, this.y + dy, this.z + dz);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d scale(final double factor) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new Point3d(this.x * factor, this.y * factor, this.z * factor);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d neg()
    {
        return scale(-1.0);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d abs()
    {
        return new Point3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    /** {@inheritDoc} */
    @Override
    public Point3d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d interpolate(final Point3d point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new Point3d((1.0 - fraction) * this.x + fraction * point.x, (1.0 - fraction) * this.y + fraction * point.y,
                (1.0 - fraction) * this.z + fraction * point.z);

    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final Point3d other, final double epsilon)
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(this.x - other.x) > epsilon)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilon)
        {
            return false;
        }
        if (Math.abs(this.z - other.z) > epsilon)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return new Bounds3d(this);
    }

    /** {@inheritDoc} */
    @Override
    public final Point3d closestPointOnSegment(final Point3d segmentPoint1, final Point3d segmentPoint2)
    {
        Throw.whenNull(segmentPoint1, "segmentPoint1 may not be null");
        Throw.whenNull(segmentPoint2, "segmentPoint2 may not be null");
        return closestPointOnSegment(segmentPoint1.x, segmentPoint1.y, segmentPoint1.z, segmentPoint2.x, segmentPoint2.y,
                segmentPoint2.z);
    }

    /**
     * Compute the closest point on a line with optional limiting of the result on either end.
     * @param p1X double; the x coordinate of the first point on the line
     * @param p1Y double; the y coordinate of the first point on the line
     * @param p1Z double; the z coordinate of the first point on the line
     * @param p2X double; the x coordinate of the second point on the line
     * @param p2Y double; the y coordinate of the second point on the line
     * @param p2Z double; the z coordinate of the second point on the line
     * @param lowLimitHandling Boolean; controls handling of results that lie before the first point of the line. If null; this
     *            method returns null; else if true; this method returns (p1X,p1Y); else (lowLimitHandling is false); this
     *            method will return the closest point on the line
     * @param highLimitHandling Boolean; controls the handling of results that lie beyond the second point of the line. If null;
     *            this method returns null; else if true; this method returns (p2X,p2Y); else (highLimitHandling is false); this
     *            method will return the closest point on the line
     * @return Point3d; the closest point on the line after applying the indicated limit handling; so the result can be null
     * @throws DrawRuntimeException when any of the arguments is NaN
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public Point3d closestPointOnLine(final double p1X, final double p1Y, final double p1Z, final double p2X, final double p2Y,
            final double p2Z, final Boolean lowLimitHandling, final Boolean highLimitHandling) throws DrawRuntimeException
    {
        double fraction = fractionalPositionOnLine(p1X, p1Y, p1Z, p2X, p2Y, p2Z, lowLimitHandling, highLimitHandling);
        if (Double.isNaN(fraction))
        {
            return null;
        }
        if (fraction == 1.0)
        {
            return new Point3d(p2X, p2Y, p2Z); // Maximize precision in case fraction == 1.0
        }
        return new Point3d(p1X + fraction * (p2X - p1X), p1Y + fraction * (p2Y - p1Y), p1Z + fraction * (p2Z - p1Z));
    }

    /**
     * Compute the fractional position of the closest point on a line with optional limiting of the result on either end. If the
     * line has length 0; this method returns 0.0.
     * @param p1X double; the x coordinate of the first point on the line
     * @param p1Y double; the y coordinate of the first point on the line
     * @param p1Z double; the z coordinate of the first point on the line
     * @param p2X double; the x coordinate of the second point on the line
     * @param p2Y double; the y coordinate of the second point on the line
     * @param p2Z double; the z coordinate of the second point on the line
     * @param lowLimitHandling Boolean; controls handling of results that lie before the first point of the line. If null; this
     *            method returns NaN; else if true; this method returns 0.0; else (lowLimitHandling is false); this results &lt;
     *            0.0 are returned
     * @param highLimitHandling Boolean; controls the handling of results that lie beyond the second point of the line. If null;
     *            this method returns NaN; else if true; this method returns 1.0; else (highLimitHandling is false); results
     *            &gt; 1.0 are returned
     * @return double; the fractional position of the closest point on the line. Results within the range 0.0 .. 1.0 are always
     *         returned as is.. A result &lt; 0.0 is subject to lowLimitHandling. A result &gt; 1.0 is subject to
     *         highLimitHandling
     * @throws DrawRuntimeException when any of the arguments is NaN
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public double fractionalPositionOnLine(final double p1X, final double p1Y, final double p1Z, final double p2X,
            final double p2Y, final double p2Z, final Boolean lowLimitHandling, final Boolean highLimitHandling)
            throws DrawRuntimeException
    {
        double dX = p2X - p1X;
        double dY = p2Y - p1Y;
        double dZ = p2Z - p1Z;
        Throw.when(Double.isNaN(dX) || Double.isNaN(dY) || Double.isNaN(dZ), DrawRuntimeException.class,
                "NaN values not permitted");
        if (0 == dX && 0 == dY && 0 == dZ)
        {
            return 0.0;
        }
        double fraction = ((this.x - p1X) * dX + (this.y - p1Y) * dY + (this.z - p1Z) * dZ) / (dX * dX + dY * dY + dZ * dZ);
        if (fraction < 0.0)
        {
            if (lowLimitHandling == null)
            {
                return Double.NaN;
            }
            if (lowLimitHandling)
            {
                fraction = 0.0;
            }
        }
        else if (fraction > 1.0)
        {
            if (highLimitHandling == null)
            {
                return Double.NaN;
            }
            if (highLimitHandling)
            {
                fraction = 1.0;
            }
        }
        return fraction;
    }

    /**
     * Project a point on a line segment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param p1X double; the x coordinate of the start point of the line segment
     * @param p1Y double; the y coordinate of the start point of the line segment
     * @param p1Z double; the z coordinate of the start point of the line segment
     * @param p2X double; the x coordinate of the end point of the line segment
     * @param p2Y double; the y coordinate of the end point of the line segment
     * @param p2Z double; the y coordinate of the end point of the line segment
     * @return P; either <cite>segmentPoint1</cite>, or <cite>segmentPoint2</cite> or a new Point2d that lies somewhere in
     *         between those two.
     * @throws DrawRuntimeException when any of the parameters is NaN
     */
    public final Point3d closestPointOnSegment(final double p1X, final double p1Y, final double p1Z, final double p2X,
            final double p2Y, final double p2Z) throws DrawRuntimeException
    {
        return closestPointOnLine(p1X, p1Y, p1Z, p2X, p2Y, p2Z, true, true);
    }

    /** {@inheritDoc} */
    @Override
    public final Point3d closestPointOnLine(final Point3d linePoint1, final Point3d linePoint2)
            throws NullPointerException, DrawRuntimeException
    {
        Throw.whenNull(linePoint1, "linePoint1 may not be null");
        Throw.whenNull(linePoint2, "linePoint2 may not be null");
        return closestPointOnLine(linePoint1.x, linePoint1.y, linePoint1.z, linePoint2.x, linePoint2.y, linePoint2.z);
    }

    /**
     * Project a point on a line. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param p1X double; the x coordinate of a point of the line
     * @param p1Y double; the y coordinate of a point of the line
     * @param p1Z double; the z coordinate of a point on the line
     * @param p2X double; the x coordinate of another point on the line
     * @param p2Y double; the y coordinate of another point on the line
     * @param p2Z double; the z coordinate of another point on the line
     * @return Point3d; a point on the line that goes through the points
     * @throws DrawRuntimeException when the points on the line are identical
     */
    public final Point3d closestPointOnLine(final double p1X, final double p1Y, final double p1Z, final double p2X,
            final double p2Y, final double p2Z) throws DrawRuntimeException
    {
        Throw.when(p1X == p2X && p1Y == p2Y && p1Z == p2Z, DrawRuntimeException.class, "degenerate line not allowed");
        return closestPointOnLine(p1X, p1Y, p1Z, p2X, p2Y, p2Z, false, false);
    }

    /**
     * Return the direction of the point in radians with respect to the origin, ignoring the z-coordinate.
     * @return double; the direction of the projection of the point in the x-y plane with respect to the origin, in radians
     */
    final double horizontalDirection()
    {
        return Math.atan2(this.y, this.x);
    }

    /**
     * Return the direction to another point, in radians, ignoring the z-coordinate.
     * @param point Point3d; the other point
     * @return double; the direction of the projection of the point in the x-y plane to another point, in radians
     * @throws NullPointerException when <code>point</code> is null
     */
    final double horizontalDirection(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point cannot be null");
        return Math.atan2(point.y - this.y, point.x - this.x);
    }

    /**
     * Return the direction with respect to the Z axis to another point, in radians.
     * @param point Point3d; the other point
     * @return double; the direction with respect to the Z axis to another point, in radians
     * @throws NullPointerException when <code>point</code> is null
     */
    final double verticalDirection(final Point3d point) throws NullPointerException
    {
        Throw.whenNull(point, "point cannot be null");
        return Math.atan2(Math.hypot(point.y - this.y, point.x - this.x), point.z - this.z);
    }

    /**
     * Return the squared distance between the coordinates of this point and the provided point, ignoring the z-coordinate.
     * @param point Point3d; the other point
     * @return double; the squared distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    final double horizontalDistanceSquared(final Point3d point)
    {
        Throw.whenNull(point, "point cannot be null");
        double dx = this.x - point.x;
        double dy = this.y - point.y;
        return dx * dx + dy * dy;
    }

    /**
     * Return the Euclidean distance between this point and the provided point, ignoring the z-coordinate.
     * @param point Point3d; the other point
     * @return double; the Euclidean distance between this point and the other point, ignoring the z-coordinate
     * @throws NullPointerException when point is null
     */
    final double horizontalDistance(final Point3d point)
    {
        return Math.sqrt(horizontalDistanceSquared(point));
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return toString("%f");
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2$s]", doNotIncludeClassName ? "" : "Point3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.z);
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
        Point3d other = (Point3d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

}
