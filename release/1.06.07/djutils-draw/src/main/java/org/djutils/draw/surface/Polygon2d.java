package org.djutils.draw.surface;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Polygon2d.java. Closed PolyLine2d. The actual closing point (which is the same as the starting point) is NOT included in the
 * super PolyLine2d.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Polygon2d extends PolyLine2d
{

    /**
     * Construct a new Polygon2d.
     * @param point1 Point2d; the first point of the new Polygon2d
     * @param point2 Point2d; the second point of the new Polygon2d
     * @param otherPoints Point2d[]; all remaining points of the new Polygon2d (may be null)
     * @throws DrawRuntimeException when point2 is equal to the last point of otherPoints, or any two successive points are
     *             equal
     */
    public Polygon2d(final Point2d point1, final Point2d point2, final Point2d[] otherPoints) throws DrawRuntimeException
    {
        super(checkClosingPoint(point1, otherPoints), point2, otherPoints);
    }

    /**
     * Ensure that the last point of otherPoints is not equal to point1.
     * @param point1 Point2d; the first point of a new Polygon2d
     * @param otherPoints Point2d[]; the remaining points of a new Polygon2d (may be null)
     * @return Point2d; point1; the first point for a new Polygon2d
     * @throws DrawRuntimeException when point1 is equal to the last point of otherPoints
     */
    private static Point2d checkClosingPoint(final Point2d point1, final Point2d[] otherPoints) throws DrawRuntimeException
    {
        Throw.when(otherPoints != null && otherPoints[otherPoints.length - 1].equals(point1), DrawRuntimeException.class,
                "point1 must not be equal to last point of otherPoints");
        return point1;
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Determine if a point is inside this Polygon.
     * @param point Point2d; the point
     * @return boolean; true if the point is inside this polygon, false if the point is outside this polygon. Results are
     *         ill-defined for points on the edges of this Polygon.
     */
    public final boolean contains(final Point2d point)
    {
        return false; // TODO
    }

    /**
     * Subtract the overlap with another Polygon2d from this Polygon2d and return the result as a new Polygon2d.
     * @param otherPolygon Polygon2d; the other Polygon2d
     * @return Polygon2d; the asymmetrical difference; or null if there otherPolygon completely covers this Polygon2d
     */
    public Polygon2d difference(final Polygon2d otherPolygon)
    {
        return null; // TODO
    }

}
