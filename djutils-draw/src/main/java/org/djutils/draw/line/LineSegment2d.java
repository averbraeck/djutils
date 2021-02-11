package org.djutils.draw.line;

import java.util.Arrays;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.Space2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * LineSegment2d is a line segment bound by 2 end points in 2D-space. A line segment stores the order in which it has been
 * created, so the end points are known as 'start' and 'end'.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LineSegment2d implements Drawable2d, LineSegment<Point2d, Ray2d, Space2d>
{
    /** */
    private static final long serialVersionUID = 20210121L;

    /** The start x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double startX;

    /** The start y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double startY;

    /** The end x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double endX;

    /** The end y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double endY;

    /**
     * Construct a new LineSegment2d start four coordinates.
     * @param startX double; the x-coordinate of the start point
     * @param startY double; the y-coordinate of the start point
     * @param endX double; the x-coordinate of the end point
     * @param endY double; the y-coordinate of the end point
     * @throws DrawRuntimeException when (startX,startY) is equals end (endX,endY)
     */
    public LineSegment2d(final double startX, final double startY, final double endX, final double endY)
            throws DrawRuntimeException
    {
        Throw.when(startX == endX && startY == endY, DrawRuntimeException.class, "Start and end may not be equal");
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Construct a new LineSegment2d start a Point2d and two coordinates.
     * @param start Point2d; the start point
     * @param endX double; the x-coordinate of the end point
     * @param endY double; the y-coordinate of the end point
     * @throws NullPointerException when start is null
     * @throws DrawRuntimeException when start has the exact coordinates endX, endY
     */
    public LineSegment2d(final Point2d start, final double endX, final double endY)
            throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(start, "start point may not be null").x, start.y, endX, endY);
    }

    /**
     * Construct a new LineSegment2d start two coordinates and a Point2d.
     * @param startX double; the x-coordinate of the start point
     * @param startY double; the y-coordinate of the start point
     * @param end Point2d; the end point
     * @throws NullPointerException when end is null
     * @throws DrawRuntimeException when end has the exact coordinates startX, startY
     */
    public LineSegment2d(final double startX, final double startY, final Point2d end)
            throws NullPointerException, DrawRuntimeException
    {
        this(startX, startY, Throw.whenNull(end, "end point may not be null").x, end.y);
    }

    /**
     * Construct a new LineSegment2d start two Point2d objects.
     * @param start Point2d; the start point
     * @param end Point2d; the end point
     * @throws NullPointerException when start is null
     * @throws DrawRuntimeException when start has the exact coordinates endX, endY
     */
    public LineSegment2d(final Point2d start, final Point2d end) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(start, "start point may not be null").x, start.y,
                Throw.whenNull(end, "end point may not be null").x, end.y);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getStartPoint()
    {
        return new Point2d(this.startX, this.startY);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getEndPoint()
    {
        return new Point2d(this.endX, this.endY);
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        return Math.hypot(this.endX - this.startX, this.endY - this.startY);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point2d> getPoints()
    {
        return Arrays.stream(new Point2d[] { getStartPoint(), getEndPoint() }).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 2;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return new Bounds2d(Math.min(this.startX, this.endX), Math.max(this.startX, this.endX),
                Math.min(this.startY, this.endY), Math.max(this.startY, this.endY));
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        double dX = this.endX - this.startX;
        double dY = this.endY - this.startY;
        double length = Math.hypot(dX, dY);
        return new Ray2d(this.startX + position * dX / length, this.startY + position * dY / length, Math.atan2(dY, dX));
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnSegment(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.closestPointOnLine(this.startX, this.startY, this.endX, this.endY, true, true);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonal(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.closestPointOnLine(this.startX, this.startY, this.endX, this.endY, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d projectOrthogonalExtended(final Point2d point)
    {
        Throw.whenNull(point, "point may not be null");
        return point.closestPointOnLine(this.startX, this.startY, this.endX, this.endY, false, false);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractional(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.fractionalPositionOnLine(this.startX, this.startY, this.endX, this.endY, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public double projectOrthogonalFractionalExtended(final Point2d point) throws NullPointerException
    {
        Throw.whenNull(point, "point may not be null");
        return point.fractionalPositionOnLine(this.startX, this.startY, this.endX, this.endY, false, false);
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
        String format = String.format("%1$s[startX=%2$s, startY=%2$s - endX=%2%s, endY=%2$s]",
                doNotIncludeClassName ? "" : "LineSegment2d ", doubleFormat);
        return String.format(format, this.startX, this.startY, this.endX, this.endY);
    }

    /** {@inheritDoc} */
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
        temp = Double.doubleToLongBits(this.startX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.startY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
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
        LineSegment2d other = (LineSegment2d) obj;
        if (Double.doubleToLongBits(this.endX) != Double.doubleToLongBits(other.endX))
            return false;
        if (Double.doubleToLongBits(this.endY) != Double.doubleToLongBits(other.endY))
            return false;
        if (Double.doubleToLongBits(this.startX) != Double.doubleToLongBits(other.startX))
            return false;
        if (Double.doubleToLongBits(this.startY) != Double.doubleToLongBits(other.startY))
            return false;
        return true;
    }

}
