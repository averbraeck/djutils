package org.djutils.draw.line;

import org.djutils.draw.Directed;
import org.djutils.draw.point.Point;
import org.djutils.exceptions.Throw;

/**
 * A Ray is a half-line; it has one end point with non-infinite coordinates; the other end point is infinitely far away.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <R> The Ray type (2d or 3d)
 * @param <D> The Directed type (2d or 3d)
 * @param <P> The Point type (2d or 3d)
 */
public interface Ray<R extends Ray<R, D, P>, D extends Directed<D>, P extends Point<P>> extends Project<P>
{
    /**
     * Get the finite end point of this Ray.
     * @return the finite end point of this Ray
     */
    D getEndPoint();

    /**
     * Retrieve the angle from the positive X axis direction in radians.
     * @return the angle from the positive X axis direction in radians
     */
    double getDirZ();

    /**
     * Flip the direction of the Ray (creates and returns a new Ray instance).
     * @return Ray at the same location, but with <code>dirZ</code> (in case of a Ray3d) incremented by &pi; and
     *         <code>dirY</code> subtracted from &pi;
     */
    R flip();

    /**
     * Get the location at a position on the line, with its direction. Position must be a positive, finite value
     * @param position the position on the line for which to calculate the point on the line
     * @return a ray with the same direction as this ray (even if the direction of this ray is not normalized)
     * @throws ArithmeticException when <code>position</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>position</code> &lt; <code>0.0</code>, or infinite
     */
    default R getLocation(final double position)
    {
        Throw.whenNaN(position, "position");
        Throw.when(position < 0 || Double.isInfinite(position), IllegalArgumentException.class,
                "position must be finite and positive");
        return getLocationExtended(position);
    }

    /**
     * Get the location at a position on the line, with its direction. Position must be a finite value
     * @param position the position on the line for which to calculate the point on the line
     * @return a ray with the same direction as this ray
     * @throws ArithmeticException when <code>position</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>position</code> infinite
     */
    R getLocationExtended(double position);

    /**
     * Project a Point on a Ray. If the the projected points lies outside the ray, the start point of the ray is returned.
     * Otherwise the closest point on the ray is returned. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code published by Paul
     * Bourke</a>.
     * @param point the point to project onto the segment
     * @return either the start point, or DirectedPoint that lies somewhere on this Ray
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    P closestPointOnRay(P point);

}
