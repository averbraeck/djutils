package org.djutils.draw.line;

import org.djutils.draw.point.Point;

/**
 * Project.java.
 * <p>
 * Copyright (c) 2021-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> the point type (2d or 3d)
 */
public interface Project<P extends Point<P>>
{
    /**
     * Project a point onto this object. For PolyLines, there may be multiple valid solutions. In that case the solution that
     * lies on the closest segment is returned. If there is no valid solution on the closest segment, null is returned.
     * @param point P; the point
     * @return P; the projection of the point (may be null if no sensible projection is possible). If the result is not null;
     *         the result lies somewhere on this object.
     * @throws NullPointerException when point is null;
     */
    P projectOrthogonal(P point) throws NullPointerException;

    /**
     * Project a point onto this object. For PolyLines, there may be multiple valid solutions. In that case the solution that
     * lies on the closest segment is returned.
     * @param point P; the point
     * @return P; the projection of the point. This result cannot be null, but it may not lie on this object, but, in stead, lie
     *         on a line, or plane that extends this object
     * @throws NullPointerException when point is null;
     */
    P projectOrthogonalExtended(P point) throws NullPointerException;

    /**
     * Project a point onto this object. For PolyLines, there may be multiple valid solutions. In that case the solution that
     * lies on the closest segment is returned. If there is no valid solution on the closest segment, null is returned.
     * @param point P; the point
     * @return double; the fractional position of the projection of the point (may be NaN if no sensible projection is
     *         possible). If the result is not null; the result lies somewhere on this object.
     * @throws NullPointerException when point is null;
     */
    double projectOrthogonalFractional(P point) throws NullPointerException;

    /**
     * Project a point onto this object. For PolyLines, there may be multiple valid solutions. In that case the solution that
     * lies on the closest segment is returned.
     * @param point P; the point
     * @return double; the fractional position of the projection of the point. This result cannot be NaN, but it may be outside
     *         the range 0.0 .. 1.0.
     * @throws NullPointerException when point is null;
     */
    double projectOrthogonalFractionalExtended(P point) throws NullPointerException;

}
