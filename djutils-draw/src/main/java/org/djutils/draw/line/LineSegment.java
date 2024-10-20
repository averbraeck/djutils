package org.djutils.draw.line;

import java.io.Serializable;

import org.djutils.draw.Directed;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;
import org.djutils.draw.point.Point;
import org.djutils.exceptions.Throw;

/**
 * LineSegment is the interface for a line segment bound by 2 end points. A line segment stores the order in which it has been
 * created, so the end points are known as 'start' and 'end'.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type (2d or 3d)
 * @param <D> The directed type (2d or 3d)
 */
public interface LineSegment<P extends Point<P>, D extends Directed<D>>
         extends Drawable<P>, Serializable, Project<P>
{
    /**
     * Get the start point of this LineSegment.
     * @return P; the start point of the LineSegment
     */
    P getStartPoint();

    /**
     * Get the end point of this LineSegment.
     * @return P; the end point of the LineSegment
     */
    P getEndPoint();

    /**
     * Get the length (distance from start point to end point) of this LineSegment.
     * @return double; (distance from start point to end point) of this LineSegment
     */
    double getLength();

    /**
     * Project a Point on this LineSegment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param point P; the point to project onto the segment
     * @return P; either the start point, or the end point of the segment or a Point that lies somewhere in between those two.
     * @throws NullPointerException when point is null
     */
    P closestPointOnSegment(P point) throws NullPointerException;

    /**
     * Construct a new LineSegment with the points of this LineSegment in reverse order.
     * @return LineSegment; the new LineSegment
     */
    LineSegment<P, D> reverse();

    /**
     * Create a DirectedPoint at the specified position along this LineSegment.
     * @param position double; the distance from the start point of this LineSegment.
     * @return D; a DirectedPoint beginning at the specified position
     * @throws DrawRuntimeException when position is NaN, &lt; 0 or &gt; length of this LineSegment
     */
    default D getLocation(final double position) throws DrawRuntimeException
    {
        Throw.when(position < 0 || position > getLength(), DrawRuntimeException.class,
                "position must be positive and less than the length of this LineSegment");
        return getLocationExtended(position);
    }

    /**
     * Create a DirectedPoint at the specified position along this LineSegment.
     * @param position double; the distance from the start point of this LineSegment.
     * @return D; a DirectedPoint at the specified position
     * @throws DrawRuntimeException when position is NaN or infinite
     */
    D getLocationExtended(double position) throws DrawRuntimeException;

}
