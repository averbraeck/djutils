package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * LineSegment3d is a line segment bound by 2 end points in 3D-space. A line segment stores the order in which it has been
 * created, so the end points are known as 'start' and 'end'.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class LineSegment3d implements Drawable3d, LineSegment<Point3d, DirectedPoint3d>
{
    /** */
    private static final long serialVersionUID = 20210121L;

    /** The start x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double startX;

    /** The start y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double startY;

    /** The start z-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double startZ;

    /** The end x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double endX;

    /** The end y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double endY;

    /** The end z-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double endZ;

    /**
     * Construct a new LineSegment3d from six coordinates.
     * @param startX the x-coordinate of the start point
     * @param startY the y-coordinate of the start point
     * @param startZ the z-coordinate of the start point
     * @param endX the x-coordinate of the end point
     * @param endY the y-coordinate of the end point
     * @param endZ the z-coordinate of the end point
     * @throws IllegalArgumentException when <code>(startX,startY,startZ)</code> is equal to <code>(endX,endY,endZ)</code>
     */
    public LineSegment3d(final double startX, final double startY, final double startZ, final double endX, final double endY,
            final double endZ)
    {
        Throw.when(startX == endX && startY == endY && startZ == endZ, IllegalArgumentException.class,
                "Start and end may not be equal");
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    /**
     * Construct a new LineSegment3d from a Point3d and three coordinates.
     * @param start the start point
     * @param endX the x-coordinate of the end point
     * @param endY the y-coordinate of the end point
     * @param endZ the z-coordinate of the end point
     * @throws NullPointerException when <code>start</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>start</code> has the exact coordinates <code>(endX,endY,endZ)</code>
     */
    public LineSegment3d(final Point3d start, final double endX, final double endY, final double endZ)
    {
        this(Throw.whenNull(start, "start").x, start.y, start.z, endX, endY, endZ);
    }

    /**
     * Construct a new LineSegment3d from three coordinates and a Point3d.
     * @param startX the x-coordinate of the start point
     * @param startY the y-coordinate of the start point
     * @param startZ the z-coordinate of the start point
     * @param end the end point
     * @throws NullPointerException when <code>end</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>end</code> has the exact coordinates <code>(startX,startY,startZ</code>
     */
    public LineSegment3d(final double startX, final double startY, final double startZ, final Point3d end)
    {
        this(startX, startY, startZ, Throw.whenNull(end, "end").x, end.y, end.z);
    }

    /**
     * Construct a new LineSegment3d from two Point3d objects.
     * @param start the start point
     * @param end the end point
     * @throws NullPointerException when <code>start</code>, or <code>end</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>start</code> has the same coordinates as <code>end</code>
     */
    public LineSegment3d(final Point3d start, final Point3d end)
    {
        this(Throw.whenNull(start, "start point may not be null").x, start.y, start.z,
                Throw.whenNull(end, "end point may not be null").x, end.y, end.z);
    }

    @Override
    public Point3d getStartPoint()
    {
        return new Point3d(this.startX, this.startY, this.startZ);
    }

    @Override
    public Point3d getEndPoint()
    {
        return new Point3d(this.endX, this.endY, this.endZ);
    }

    @Override
    public double getLength()
    {
        // There is no varargs hypot function in Math
        double dX = this.endX - this.startX;
        double dY = this.endY - this.startY;
        double dZ = this.endZ - this.startZ;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    @Override
    public Iterator<Point3d> iterator()
    {
        return Arrays.stream(new Point3d[] {getStartPoint(), getEndPoint()}).iterator();
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public Bounds3d getBounds()
    {
        return new Bounds3d(Math.min(this.startX, this.endX), Math.max(this.startX, this.endX),
                Math.min(this.startY, this.endY), Math.max(this.startY, this.endY), Math.min(this.startZ, this.endZ),
                Math.max(this.startZ, this.endZ));
    }

    @Override
    public LineSegment2d project() throws DrawRuntimeException
    {
        return new LineSegment2d(this.startX, this.startY, this.endX, this.endY);
    }

    @Override
    public DirectedPoint3d getLocationExtended(final double position) throws IllegalArgumentException
    {
        Throw.whenNaN(position, "position");
        Throw.when(Double.isInfinite(position), IllegalArgumentException.class, "position must be finite");
        double dX = this.endX - this.startX;
        double dY = this.endY - this.startY;
        double dZ = this.endZ - this.startZ;
        double length = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        return new DirectedPoint3d(this.startX + position * dX / length, this.startY + position * dY / length,
                this.startZ + position * dZ / length, Math.atan2(dZ, Math.hypot(dX, dY)), Math.atan2(dY, dX));
    }

    @Override
    public Point3d closestPointOnSegment(final Point3d point)
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ, true, true);
    }

    @Override
    public LineSegment3d reverse()
    {
        return new LineSegment3d(this.endX, this.endY, this.endZ, this.startX, this.startY, this.startZ);
    }

    @Override
    public Point3d projectOrthogonal(final Point3d point)
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ, null, null);
    }

    @Override
    public Point3d projectOrthogonalExtended(final Point3d point)
    {
        Throw.whenNull(point, "point");
        return point.closestPointOnLine(this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ);
    }

    @Override
    public double projectOrthogonalFractional(final Point3d point)
    {
        Throw.whenNull(point, "point");
        return point.fractionalPositionOnLine(this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ, null,
                null);
    }

    @Override
    public double projectOrthogonalFractionalExtended(final Point3d point)
    {
        Throw.whenNull(point, "point");
        return point.fractionalPositionOnLine(this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ, false,
                false);
    }

    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[startX=%2$s, startY=%2$s, startZ=%2$s - endX=%2%s, endY=%2$s, endZ=%2$s]",
                doNotIncludeClassName ? "" : "LineSegment3d ", doubleFormat);
        return String.format(Locale.US, format, this.startX, this.startY, this.startZ, this.endX, this.endY, this.endZ);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.endX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.endY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.endZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.startX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.startY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.startZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LineSegment3d other = (LineSegment3d) obj;
        if (Double.doubleToLongBits(this.endX) != Double.doubleToLongBits(other.endX))
            return false;
        if (Double.doubleToLongBits(this.endY) != Double.doubleToLongBits(other.endY))
            return false;
        if (Double.doubleToLongBits(this.endZ) != Double.doubleToLongBits(other.endZ))
            return false;
        if (Double.doubleToLongBits(this.startX) != Double.doubleToLongBits(other.startX))
            return false;
        if (Double.doubleToLongBits(this.startY) != Double.doubleToLongBits(other.startY))
            return false;
        if (Double.doubleToLongBits(this.startZ) != Double.doubleToLongBits(other.startZ))
            return false;
        return true;
    }

}
