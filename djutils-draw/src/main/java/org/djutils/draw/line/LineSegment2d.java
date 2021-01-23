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
 * Segment2d.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LineSegment2d implements Drawable2d, LineSegment<Point2d, Ray2d, Space2d>
{
    /** ... */
    private static final long serialVersionUID = 20210121L;

    /** The start x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double fromX;

    /** The start y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double fromY;

    /** The end x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double toX;

    /** The end y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double toY;

    /**
     * Construct a new Segment2d from four coordinates.
     * @param fromX double; the x-coordinate of the start point
     * @param fromY double; the y-coordinate of the start point
     * @param toX double; the x-coordinate of the end point
     * @param toY double; the y-coordinate of the end point
     * @throws DrawRuntimeException when (fromX,fromY) is equals to (toX,toY)
     */
    public LineSegment2d(final double fromX, final double fromY, final double toX, final double toY) throws DrawRuntimeException
    {
        Throw.when(fromX == toX && fromY == toY, DrawRuntimeException.class, "From and to may not be equal");
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    /**
     * Construct a new Segment2d from a Point2d and two coordinates.
     * @param from Point2d; the start point
     * @param toX double; the x-coordinate of the end point
     * @param toY double; the y-coordinate of the end point
     * @throws NullPointerException when from is null
     * @throws DrawRuntimeException when from has the exact coordinates toX, toY
     */
    public LineSegment2d(final Point2d from, final double toX, final double toY)
            throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(from, "from point may not be null").x, from.y, toX, toY);
    }

    /**
     * Construct a new Segment2d from two coordinates and a Point2d.
     * @param fromX double; the x-coordinate of the start point
     * @param fromY double; the y-coordinate of the start point
     * @param to Point2d; the end point
     * @throws NullPointerException when to is null
     * @throws DrawRuntimeException when to has the exact coordinates fromX, fromY
     */
    public LineSegment2d(final double fromX, final double fromY, final Point2d to)
            throws NullPointerException, DrawRuntimeException
    {
        this(fromX, fromY, Throw.whenNull(to, "to point may not be null").x, to.y);
    }

    /**
     * Construct a new Segment2d from two Point2d objects.
     * @param from Point2d; the start point
     * @param to Point2d; the end point
     * @throws NullPointerException when from is null
     * @throws DrawRuntimeException when from has the exact coordinates toX, toY
     */
    public LineSegment2d(final Point2d from, final Point2d to) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(from, "from point may not be null").x, from.y, Throw.whenNull(to, "to point may not be null").x,
                to.y);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getStartPoint()
    {
        return new Point2d(this.fromX, this.fromY);
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getEndPoint()
    {
        return new Point2d(this.toX, this.toY);
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        return Math.hypot(this.toX - this.fromX, this.toY - this.fromY);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point2d> getPoints()
    {
        return Arrays.stream(new Point2d[] {getStartPoint(), getEndPoint()}).iterator();
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
        return new Bounds2d(Math.min(this.fromX, this.toX), Math.max(this.fromX, this.toX), Math.min(this.fromY, this.toY),
                Math.max(this.fromY, this.toY));
    }

    /** {@inheritDoc} */
    @Override
    public Ray2d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        double dX = this.toX - this.fromX;
        double dY = this.toY - this.fromY;
        double length = Math.hypot(dX, dY);
        return new Ray2d(this.fromX + position * dX / length, this.fromY + position * dY / length, Math.atan2(dY, dX));
    }

    /** {@inheritDoc} */
    @Override
    public Point2d closestPointOnSegment(final Point2d point)
    {
        double dX = this.toX - this.fromX;
        double dY = this.toY - this.fromY;
        final double u = ((point.x - this.fromX) * dX + (point.y - this.fromY) * dY) / (dX * dX + dY * dY);
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
            return new Point2d((1.0 - u) * this.fromX + u * this.toX, (1.0 - u) * this.fromY + u * this.toY);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Segment2d [fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + "]";
    }

}
