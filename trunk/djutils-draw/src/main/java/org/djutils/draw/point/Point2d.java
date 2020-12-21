package org.djutils.draw.point;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.Space2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.exceptions.Throw;

/**
 * A Point2d is an immutable Point with an x and y coordinate, stored with double precision. It differs from many Point
 * implementations by being immutable.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Point2d implements Drawable2d, Point<Point2d, Space2d>
{
    /** */
    private static final long serialVersionUID = 20201201L;

    /** The x-coordinate. */
    private final double x;

    /** The y-coordinate. */
    private final double y;

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @throws IllegalArgumentException when x or y is NaN
     */
    public Point2d(final double x, final double y) throws IllegalArgumentException
    {
        Throw.when(Double.isNaN(x) || Double.isNaN(y), IllegalArgumentException.class, "Coordinate must be a number (not NaN)");
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new Point with just an x and y coordinate, stored with double precision.
     * @param xy double[2]; the x and y coordinate
     * @throws NullPointerException when xy is null
     * @throws IllegalArgumentException when the dimension of xy is not 2, or a coordinate is NaN
     */
    public Point2d(final double[] xy) throws NullPointerException, IllegalArgumentException
    {
        this(checkLengthIsTwo(Throw.whenNull(xy, "xy-point cannot be null"))[0], xy[1]);
    }

    /**
     * Create an immutable point with just two values, x and y, stored with double precision from an AWT Point2D
     * @param point Point2D; an AWT Point2D
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when point has a NaN coordinate
     */
    public Point2d(final Point2D point) throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(point.getX()) || Double.isNaN(point.getY()), IllegalArgumentException.class,
                "Coordinate must be a number (not NaN)");
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Throw an IllegalArgumentException if the length of the provided array is not two.
     * @param xy double[]; the provided array
     * @return double[]; the provided array
     * @throws IllegalArgumentException when length of xy is not two
     */
    private static double[] checkLengthIsTwo(final double[] xy) throws IllegalArgumentException
    {
        Throw.when(xy.length != 2, IllegalArgumentException.class, "Length of xy-array must be 2");
        return xy;
    }

    /**
     * Return the x-coordinate.
     * @return double; the x-coordinate
     */
    public final double getX()
    {
        return this.x;
    }

    /**
     * Return the y-coordinate.
     * @return double; the y-coordinate
     */
    public final double getY()
    {
        return this.y;
    }

    /**
     * Return the distance to another point.
     * @param otherPoint Point&lt;?, S&gt;; the other point
     * @return double; the distance (2d or 3d as applicable) to the other point
     */
    public double distance(final Point2d otherPoint)
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        return Math.sqrt(distanceSquared(otherPoint));
    }

    /**
     * Return the squared distance between this point and the provided point.
     * @param otherPoint Point2d; the other point
     * @return double; the squared distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    public double distanceSquared(final Point2d otherPoint) throws NullPointerException
    {
        Throw.whenNull(otherPoint, "point cannot be null");
        double dx = getX() - otherPoint.getX();
        double dy = getY() - otherPoint.getY();
        return dx * dx + dy * dy;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point2d> getPoints()
    {
        return Arrays.stream(new Point2d[] { this }).iterator();
    }

    /**
     * Return a new Point with a translation by the provided dx and dy.
     * @param dx double; the horizontal translation
     * @param dy double; the vertical translation
     * @return P; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, or dy is NaN
     */
    public Point2d translate(final double dx, final double dy)
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy), IllegalArgumentException.class, "translation may not be NaN");
        return new Point2d(getX() + dx, getY() + dy);
    }

    /**
     * Return a new Point3d with a translation by the provided delta-x, delta-y and deltaZ.
     * @param dx double; the x translation
     * @param dy double; the y translation
     * @param dz double; the z translation
     * @return Point2d; a new point with the translated coordinates
     * @throws IllegalArgumentException when dx, dy, or dz is NaN
     */
    public Point3d translate(final double dx, final double dy, final double dz)
    {
        Throw.when(Double.isNaN(dx) || Double.isNaN(dy) || Double.isNaN(dz), IllegalArgumentException.class,
                "translation may not be NaN");
        return new Point3d(getX() + dx, getY() + dy, dz);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d scale(final double factor)
    {
        Throw.when(Double.isNaN(factor), IllegalArgumentException.class, "factor must be a number (not NaN)");
        return new Point2d(getX() * factor, getY() * factor);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d neg()
    {
        return scale(-1.0);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d abs()
    {
        return new Point2d(Math.abs(getX()), Math.abs(getY()));
    }

    /** {@inheritDoc} */
    @Override
    public Point2d normalize() throws DrawRuntimeException
    {
        double length = Math.sqrt(getX() * getX() + getY() * getY());
        Throw.when(length == 0.0, DrawRuntimeException.class, "cannot normalize (0.0, 0.0)");
        return this.scale(1.0 / length);
    }

    /**
     * Interpolate towards another Point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation.
     * @param point P; the other point
     * @param fraction the factor for interpolation towards the other point. When <code>fraction</code> is between 0 and 1, it
     *            is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0; <code>this</code> Point is
     *            returned; if <code>fraction</code> is 1, the other <code>point</code> is returned
     * @return P; the point that is <code>fraction</code> away on the line between this point and the other point
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    public Point2d interpolate(final Point2d point, final double fraction)
    {
        Throw.whenNull(point, "point cannot be null");
        Throw.when(Double.isNaN(fraction), IllegalArgumentException.class, "fraction must be a number (not NaN)");
        return new Point2d((1.0 - fraction) * getX() + fraction * point.getX(),
                (1.0 - fraction) * getY() + fraction * point.getY());
    }

    /**
     * A comparison with another point that returns true of each of the coordinates is less than epsilon apart.
     * @param other Point; the point to compare with
     * @param epsilon double; the upper bound of difference for one of the coordinates
     * @return boolean; true if both x, y and z (if a Point3d) are less than epsilon apart, otherwise false
     * @throws NullPointerException when point is null
     */
    public boolean epsilonEquals(final Point2d other, final double epsilon)
    {
        Throw.whenNull(other, "other point cannot be null");
        if (Math.abs(getX() - other.getX()) > epsilon)
        {
            return false;
        }
        if (Math.abs(getY() - other.getY()) > epsilon)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return new Bounds2d(this);
    }

    /**
     * Compute the 2D intersection of two line segments. Both line segments are defined by two points (that should be distinct).
     * @param line1P1 Point2d; first point of line segment 1
     * @param line1P2 Point2d; second point of line segment 1
     * @param line2P1 Point2d; first point of line segment 2
     * @param line2P2 Point2d; second point of line segment 2
     * @return Point2d; the intersection of the two lines, or null if the lines are (almost) parallel, or do not intersect
     */
    public static Point2d intersectionOfLineSegments(final Point2d line1P1, final Point2d line1P2, final Point2d line2P1,
            final Point2d line2P2)
    {
        double l1p1x = line1P1.getX();
        double l1p1y = line1P1.getY();
        double l1p2x = line1P2.getX() - l1p1x;
        double l1p2y = line1P2.getY() - l1p1y;
        double l2p1x = line2P1.getX() - l1p1x;
        double l2p1y = line2P1.getY() - l1p1y;
        double l2p2x = line2P2.getX() - l1p1x;
        double l2p2y = line2P2.getY() - l1p1y;
        double denominator = (l2p2y - l2p1y) * l1p2x - (l2p2x - l2p1x) * l1p2y;
        if (denominator == 0.0)
        {
            return null; // lines are parallel (they might even be on top of each other, but we don't check that)
        }
        double uA = ((l2p2x - l2p1x) * (-l2p1y) - (l2p2y - l2p1y) * (-l2p1x)) / denominator;
        // System.out.println("uA is " + uA);
        if ((uA < 0.0) || (uA > 1.0))
        {
            return null; // intersection outside line 1
        }
        double uB = (l1p2y * l2p1x - l1p2x * l2p1y) / denominator;
        // System.out.println("uB is " + uB);
        if (uB < 0.0 || uB > 1.0)
        {
            return null; // intersection outside line 2
        }
        return line1P1.interpolate(line1P2, uA);
        // return new Point2d(line1P1.x + uA * l1p2x, line1P1.y + uA * l1p2y);
    }

    /**
     * Project a point on a line segment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param segmentPoint1 Point2d; start of line segment
     * @param segmentPoint2 Point2d; end of line segment
     * @return Point2d; either <cite>segmentPoint1</cite>, or <cite>segmentPoint2</cite> or a new Point2d that lies somewhere in
     *         between those two.
     */
    public final Point2d closestPointOnSegment(final Point2d segmentPoint1, final Point2d segmentPoint2)
    {
        double dX = segmentPoint2.getX() - segmentPoint1.getX();
        double dY = segmentPoint2.getY() - segmentPoint1.getY();
        if ((0 == dX) && (0 == dY))
        {
            return segmentPoint1;
        }
        final double u = ((getX() - segmentPoint1.getX()) * dX + (getY() - segmentPoint1.y) * dY) / (dX * dX + dY * dY);
        if (u < 0)
        {
            return segmentPoint1;
        }
        else if (u > 1)
        {
            return segmentPoint2;
        }
        else
        {
            return segmentPoint1.interpolate(segmentPoint2, u);
        }
    }

    /**
     * Return the coordinates as an AWT Point2D.Double object.
     * @return Point2D; the coordinates as an AWT Point2D.Double object
     */
    public Point2D toPoint2D()
    {
        return new Point2D.Double(getX(), getY());
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final int fractionDigits)
    {
        int digits = fractionDigits < 0 ? 0 : fractionDigits;
        String format = String.format("(%%.%1$df,%%.%1$df)", digits);
        return String.format(format, getX(), getY());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("(%f,%f)", getX(), getY());
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
        Point2d other = (Point2d) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

}
