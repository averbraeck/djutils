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
 * Segment3d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LineSegment3d implements Drawable3d, LineSegment<Point3d, Ray3d, Space3d>
{
    /** ... */
    private static final long serialVersionUID = 20210121L;

    /** The start x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double fromX;

    /** The start y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double fromY;

    /** The start z-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double fromZ;

    /** The end x-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double toX;

    /** The end y-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double toY;

    /** The end z-coordinate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double toZ;

    /**
     * Construct a new Segment3d from six coordinates.
     * @param fromX double; the x-coordinate of the start point
     * @param fromY double; the y-coordinate of the start point
     * @param fromZ double; the z-coordinate of the start point
     * @param toX double; the x-coordinate of the end point
     * @param toY double; the y-coordinate of the end point
     * @param toZ double; the z-coordinate of the end point
     * @throws DrawRuntimeException when (fromX,fromY) is equals to (toX,toY)
     */
    public LineSegment3d(final double fromX, final double fromY, final double fromZ, final double toX, final double toY,
            final double toZ) throws DrawRuntimeException
    {
        Throw.when(fromX == toX && fromY == toY && fromZ == toZ, DrawRuntimeException.class, "From and to may not be equal");
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
    }

    /**
     * Construct a new Segment3d from a Point3d and three coordinates.
     * @param from Point3d; the start point
     * @param toX double; the x-coordinate of the end point
     * @param toY double; the y-coordinate of the end point
     * @param toZ double; the z-coordinate of the end point
     * @throws NullPointerException when from is null
     * @throws DrawRuntimeException when from has the exact coordinates toX, toY
     */
    public LineSegment3d(final Point3d from, final double toX, final double toY, final double toZ)
            throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(from, "from point may not be null").x, from.y, from.z, toX, toY, toZ);
    }

    /**
     * Construct a new Segment3d from three coordinates and a Point3d.
     * @param fromX double; the x-coordinate of the start point
     * @param fromY double; the y-coordinate of the start point
     * @param fromZ double; the z-coordinate of the start point
     * @param to Point3d; the end point
     * @throws NullPointerException when to is null
     * @throws DrawRuntimeException when to has the exact coordinates fromX, fromY
     */
    public LineSegment3d(final double fromX, final double fromY, final double fromZ, final Point3d to)
            throws NullPointerException, DrawRuntimeException
    {
        this(fromX, fromY, fromZ, Throw.whenNull(to, "to point may not be null").x, to.y, to.z);
    }

    /**
     * Construct a new Segment3d from two Point3d objects.
     * @param from Point3d; the start point
     * @param to Point3d; the end point
     * @throws NullPointerException when from is null
     * @throws DrawRuntimeException when from has the exact coordinates toX, toY
     */
    public LineSegment3d(final Point3d from, final Point3d to) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(from, "from point may not be null").x, from.y, from.z,
                Throw.whenNull(to, "to point may not be null").x, to.y, to.z);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d getStartPoint()
    {
        return new Point3d(this.fromX, this.fromY, this.fromZ);
    }

    /** {@inheritDoc} */
    @Override
    public Point3d getEndPoint()
    {
        return new Point3d(this.toX, this.toY, this.toZ);
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        // There is no varargs hypot function in Math
        double dX = this.toX - this.fromX;
        double dY = this.toY - this.fromY;
        double dZ = this.toZ - this.fromZ;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends Point3d> getPoints()
    {
        return Arrays.stream(new Point3d[] { getStartPoint(), getEndPoint() }).iterator();
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
        return new Bounds3d(Math.min(this.fromX, this.toX), Math.max(this.fromX, this.toX), Math.min(this.fromY, this.toY),
                Math.max(this.fromY, this.toY), Math.min(this.fromZ, this.toZ), Math.max(this.fromZ, this.toZ));
    }

    /** {@inheritDoc} */
    @Override
    public LineSegment2d project() throws DrawRuntimeException
    {
        return new LineSegment2d(this.fromX, this.fromY, this.toX, this.toY);
    }

    /** {@inheritDoc} */
    @Override
    public Ray3d getLocationExtended(final double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || Double.isInfinite(position), DrawRuntimeException.class,
                "position must be finite");
        double dX = this.toX - this.fromX;
        double dY = this.toY - this.fromY;
        double dZ = this.toZ - this.fromZ;
        double length = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        return new Ray3d(this.fromX + position * dX / length, this.fromY + position * dY / length,
                this.fromZ + position * dZ / length, Math.atan2(dY, dX), Math.atan2(dZ, Math.hypot(dX, dY)));
    }

    /** {@inheritDoc} */
    @Override
    public Point3d closestPointOnSegment(final Point3d point)
    {
        double dX = this.toX - this.fromX;
        double dY = this.toY - this.fromY;
        double dZ = this.toZ - this.fromZ;
        final double u = ((point.x - this.fromX) * dX + (point.y - this.fromY) * dY + (point.z - this.fromZ) * dZ)
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
            return new Point3d(this.fromX + u * dX, this.fromY + u * dY, this.fromZ + u * dZ);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Segment3d [fromX=" + this.fromX + ", fromY=" + this.fromY + ", fromZ=" + this.fromZ + ", toX=" + this.toX
                + ", toY=" + this.toY + ", toZ=" + this.toZ + "]";
    }

}
