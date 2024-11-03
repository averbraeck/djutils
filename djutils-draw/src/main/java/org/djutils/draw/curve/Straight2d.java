package org.djutils.draw.curve;

import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.Ray2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Continuous curve implementation of a straight in 2d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Straight2d implements Curve2d, OffsetFlattable2d
{

    /** Start point with direction. */
    private final DirectedPoint2d startPoint;

    /** End point with direction. */
    private final DirectedPoint2d endPoint;

    /** Length. */
    private final double length;

    /**
     * Constructor.
     * @param startPoint start point.
     * @param length length.
     */
    public Straight2d(final DirectedPoint2d startPoint, final double length)
    {
        Throw.whenNull(startPoint, "Start point may not be null.");
        Throw.when(length <= 0.0, IllegalArgumentException.class, "Length must be above 0.");
        this.startPoint = startPoint;
        this.endPoint = new Ray2d(startPoint).getLocation(length);
        this.length = length;
    }

    @Override
    public DirectedPoint2d getStartPoint()
    {
        return this.startPoint;
    }

    @Override
    public DirectedPoint2d getEndPoint()
    {
        return this.endPoint;
    }

    @Override
    public double getStartCurvature()
    {
        return 0.0;
    }

    @Override
    public double getEndCurvature()
    {
        return 0.0;
    }

    /**
     * Return the point at the given fraction and the given lateral offset.
     * @param fraction double; fraction along the line
     * @param offset double; lateral offset (to the left)
     * @return Point2d; the point at the given fraction and lateral offset
     */
    private Point2d getPoint(final double fraction, final double offset)
    {
        if (offset == 0.0)
        {
            return new Ray2d(this.startPoint).getLocation(fraction * this.length);
        }
        else
        {
            double cos = Math.cos(this.startPoint.dirZ);
            double sin = Math.sin(this.startPoint.dirZ);
            double x = this.startPoint.x - offset * sin + cos * fraction * this.length;
            double y = this.startPoint.y + offset * cos + sin * fraction * this.length;
            return new Point2d(x, y);
        }
    }

    @Override
    public Point2d getPoint(final double fraction)
    {
        return getPoint(fraction, 0);
    }

    @Override
    public Point2d getPoint(final double fraction, final PieceWiseLinearOffset2d fld)
    {
        return getPoint(fraction, fld.get(fraction));
    }

    @Override
    public PolyLine2d toPolyLine(final Flattener2d flattener)
    {
        return new PolyLine2d(new Point2d(this.startPoint.x, this.startPoint.y), new Point2d(this.endPoint.x, this.endPoint.y));
    }

    /**
     * Offset a PolyLine2d based on variable offset. A straight uses no segments, other than for varying offset.
     * @param offsets offsets, should contain keys 0.0 and 1.0.
     * @return offset poly-line.
     */
    public PolyLine2d offset(final PieceWiseLinearOffset2d offsets)
    {
        return toPolyLine(null).offsetLine(offsets.getFractionalLengthsAsArray(), offsets.getValuesAsArray(), 0.0);
    }

    @Override
    public PolyLine2d toPolyLine(final OffsetFlattener2d flattener, final PieceWiseLinearOffset2d offsets)
    {
        return offset(offsets);
    }

    @Override
    public double getLength()
    {
        return this.length;
    }

    @Override
    public String toString()
    {
        return "ContinuousStraight [startPoint=" + this.startPoint + ", endPoint=" + this.endPoint + ", length=" + this.length
                + "]";
    }

}
