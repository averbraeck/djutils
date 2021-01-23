package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.Space3d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * LineSegment3d is a line segment bound by 2 end points in 3D-space. A line segment stores the order in which it has been
 * created, so the end points are known as 'start' and 'end'.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LineSegment3d implements Drawable3d, LineSegment<Point3d, Ray3d, Space3d>
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
     * Construct a new LineSegment3d start six coordinates.
     * @param startX double; the x-coordinate of the start point
     * @param startY double; the y-coordinate of the start point
     * @param startZ double; the z-coordinate of the start point
     * @param endX double; the x-coordinate of the end point
     * @param endY double; the y-coordinate of the end point
     * @param endZ double; the z-coordinate of the end point
     * @throws DrawRuntimeException when (startX,startY) is equals end (endX,endY)
     */
    public LineSegment3d(final double startX, final double startY, final double startZ, final double endX, final double endY,
            final double endZ) throws DrawRuntimeException
    {
        Throw.when(startX == endX && startY == endY && startZ == endZ, DrawRuntimeException.class,
                "Start and end may not be equal");
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    /**
     * Construct a new LineSegment3d start a Point3d and three coordinates.
     * @param start Point3d; the start point
     * @param endX double; the x-coordinate of the end point
     * @param endY double; the y-coordinate of the end point
     * @param endZ double; the z-coordinate of the end point
     * @throws NullPointerException when start is null
     * @throws DrawRuntimeException when start has the exact coordinates endX, endY
     */
    public LineSegment3d(final Point3d start, final double endX, final double endY, final double endZ)
            throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(start, "start point may not be null").x, start.y, start.z, endX, endY, endZ);
    }

    /**
     * Construct a new LineSegment3d start three coordinates and a Point3d.
     * @param startX double; the x-coordinate of the start point
     * @param startY double; the y-coordinate of the start point
     * @param startZ double; the z-coordinate of the start point
     * @param end Point3d; the end point
     * @throws NullPointerException when end is null
     * @throws DrawRuntimeException when end has the exact coordinates startX, startY
     */
    public LineSegment3d(final double startX, final double startY, final double startZ, final Point3d end)
            throws NullPointerException, DrawRuntimeException
    {
        this(startX, startY, startZ, Throw.whenNull(end, "end point may not be null").x, end.y, end.z);
    }

    /**
     * Construct a new LineSegment3d start two Point3d objects.
     * @param start Point3d; the start point
     * @param end Point3d; the end point
     * @throws NullPointerException when start is null
     * @throws DrawRuntimeException when start has the exact coordinates endX, endY
     */
    public LineSegment3d(final Point3d start, final Point3d end) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(start, "start point may not be null").x, start.y, start.z,
                Throw.whenNull(end, "end point may not be null").x, end.y, end.z);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d getStartPoint()
    {
        return new Point3d(this.startX, this.startY, this.startZ);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d getEndPoint()
    {
        return new Point3d(this.endX, this.endY, this.endZ);
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        // There is no varargs hypot function in Math
        double dX = this.endX - this.startX;
        double dY = this.endY - this.startY;
        double dZ = this.endZ - this.startZ;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point3d> getPoints()
    {
        return Arrays.stream(new Point3d[] {getStartPoint(), getEndPoint()}).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 2;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return new Bounds3d(Math.min(this.startX, this.endX), Math.max(this.startX, this.endX),
                Math.min(this.startY, this.endY), Math.max(this.startY, this.endY), Math.min(this.startZ, this.endZ),
                Math.max(this.startZ, this.endZ));
    }

    /** {@inheritDoc} */
    @Override
    public LineSegment2d project() throws DrawRuntimeException
    {
        return new LineSegment2d(this.startX, this.startY, this.endX, this.endY);
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        double dX = this.endX - this.startX;
        double dY = this.endY - this.startY;
        double dZ = this.endZ - this.startZ;
        double length = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        return new Ray3d(this.startX + position * dX / length, this.startY + position * dY / length,
                this.startZ + position * dZ / length, Math.atan2(dY, dX), Math.atan2(dZ, Math.hypot(dX, dY)));
    }

    /** {@inheritDoc} */
    @Override
    public Point3d closestPointOnSegment(final Point3d point)
    {
        double dX = this.endX - this.startX;
        double dY = this.endY - this.startY;
        double dZ = this.endZ - this.startZ;
        final double u = ((point.x - this.startX) * dX + (point.y - this.startY) * dY + (point.z - this.startZ) * dZ)
                / (dX * dX + dY * dY + dZ * dZ);
        if (u < 0)
        {
            return getStartPoint();
        }
        else if (u > 1)
        {
            return getEndPoint();
        }
        else
        {
            return new Point3d(this.startX + u * dX, this.startY + u * dY, this.startZ + u * dZ);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Segment3d [startX=" + this.startX + ", startY=" + this.startY + ", startZ=" + this.startZ + ", endX="
                + this.endX + ", endY=" + this.endY + ", endZ=" + this.endZ + "]";
    }

}
