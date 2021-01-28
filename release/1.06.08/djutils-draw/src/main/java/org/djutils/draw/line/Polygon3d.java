package org.djutils.draw.line;

import java.util.Arrays;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.Point3d;
import org.djutils.exceptions.Throw;

/**
 * Polygon3d.java. Closed PolyLine3d. The actual closing point (which is the same as the starting point) is NOT included in the
 * super PolyLine3d.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Polygon3d extends PolyLine3d
{
    /** */
    private static final long serialVersionUID = 20210126L;

    /**
     * Construct a new Polygon3d.
     * @param point1 Point3d; the first point of the new Polygon3d
     * @param point2 Point3d; the second point of the new Polygon3d
     * @param otherPoints Point3d[]; all remaining points of the new Polygon3d (may be null)
     * @throws DrawRuntimeException when point1 is equal to the last point of otherPoints, or any two successive points are
     *             equal
     */
    public Polygon3d(final Point3d point1, final Point3d point2, final Point3d[] otherPoints) throws DrawRuntimeException
    {
        super(point1, point2, fixClosingPoint(point1, otherPoints));
    }

    /**
     * Ensure that the last point of otherPoints is not equal to point1. Remove the last point if necessary.
     * @param point1 Point3d; the first point of a new Polygon3d
     * @param otherPoints Point3d[]; the remaining points of a new Polygon3d (may be null)
     * @return Point3d[]; otherPoints (possibly a copy thereof with the last entry removed)
     */
    private static Point3d[] fixClosingPoint(final Point3d point1, final Point3d[] otherPoints)
    {
        if (otherPoints == null || otherPoints.length == 0)
        {
            return otherPoints;
        }
        Point3d lastPoint = otherPoints[otherPoints.length - 1];
        if (point1.x == lastPoint.x && point1.y == lastPoint.y)
        {
            return Arrays.copyOf(otherPoints, otherPoints.length - 1);
        }
        return otherPoints;
    }

    /**
     * Compute the surface of this Polygon3d. Sign of the result reflects the winding-ness of this this Polygon3d. If this
     * Polygon3d self-intersects, the result is bogus.
     * @return double; the surface of this Polygon3d
     */
    public double surface()
    {
        double result = 0;
        // We initialize previous point to the last point of this Polygon3d to avoid wrapping problems
        double prevX = getX(size() - 1);
        double prevY = getY(size() - 1);
        for (int i = 0; i < size(); i++)
        {
            double thisX = getX(i);
            double thisY = getY(i);
            result += prevX * thisY - thisX * prevY;
            prevX = thisX;
            prevY = thisY;
        }
        return result / 2;
    }

    /** {@inheritDoc} */
    @Override
    public double getLength()
    {
        // Length a polygon is computed by taking the length of the PolyLine and adding the length of the closing segment
        return super.getLength() + Math.hypot(getX(size() - 1) - getX(0), getY(size() - 1) - getY(0));
    }

    /** {@inheritDoc} */
    @Override
    public LineSegment3d getSegment(final int index)
    {
        if (index < size() - 1)
        {
            return super.getSegment(index);
        }
        Throw.when(index != size() - 1, DrawRuntimeException.class, "index must be in range 0..size() - 1");
        return new LineSegment3d(getX(index), getY(index), getZ(index), getX(0), getY(0), getZ(0));
    }

}
