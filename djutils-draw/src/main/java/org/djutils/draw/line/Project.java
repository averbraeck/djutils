package org.djutils.draw.line;

import org.djutils.draw.point.Point;

/**
 * Projection of points onto objects.
 * <p>
 * Copyright (c) 2021-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <P> the point type (2d or 3d)
 */
public interface Project<P extends Point<P>>
{
    /**
     * Project a point onto this object. For PolyLines and Polygons, there may be multiple valid solutions. In that case the
     * solution that lies on the closest segment is returned. If there is no valid solution on the closest segment,
     * <code>null</code> is returned.
     * @param point the point
     * @return the projection of the point (may be null if no sensible projection is possible). If the result is not
     *         <code>null</code>; the result lies somewhere on this object.
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    P projectOrthogonal(P point);

    /**
     * Project a point onto this object. For PolyLines and Polygons, there may be multiple valid solutions. In that case the
     * solution that lies on the closest segment is returned.
     * @param point the point
     * @return the projection of the point. This result cannot be <code>null</code>, but it may not lie on this object, but,
     *         in stead, lie on a line, or plane that extends this object
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    P projectOrthogonalExtended(P point);

    /**
     * Project a point onto this object. For PolyLines and Polygons, there may be multiple valid solutions. In that case the
     * solution that lies on the closest segment is returned. If there is no valid solution on the closest segment,
     * <code>NaN</code> is returned.
     * @param point the point
     * @return the fractional position of the projection of the point (may be <code>NaN</code> if no sensible projection
     *         is possible). If the result is not <code>NaN</code>; the result lies somewhere on this object.
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    double projectOrthogonalFractional(P point);

    /**
     * Project a point onto this object. For PolyLines and Polygons, there may be multiple valid solutions. In that case the
     * solution that lies on the closest segment is returned.
     * @param point the point
     * @return the fractional position of the projection of the point. This result cannot be <code>NaN</code>, but it
     *         may be outside the range 0.0 .. 1.0.
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    double projectOrthogonalFractionalExtended(P point);

}
