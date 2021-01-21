package org.djutils.draw.line;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * Ray.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <R> The Ray type (2d or 3d)
 */
public interface Ray<R extends Ray<R>>
{
    /**
     * Get the location at a position on the line, with its direction. Position must be a positive, finite value
     * @param position double; the position on the line for which to calculate the point on the line
     * @return R; a ray with the same direction as this ray (even if the direction of this ray is not normalized)
     * @throws DrawRuntimeException when position less than 0.0, infinite, or NaN.
     */
    default R getLocation(double position) throws DrawRuntimeException
    {
        Throw.when(Double.isNaN(position) || position < 0, DrawRuntimeException.class,
                "position must be finite and positive");
        return getLocationExtended(position);
    }

    /**
     * Get the location at a position on the line, with its direction. Position must be a positive, finite value
     * @param position double; the position on the line for which to calculate the point on the line
     * @return R; a ray with the same direction as this ray
     * @throws DrawRuntimeException when position infinite, or NaN.
     */
    R getLocationExtended(double position) throws DrawRuntimeException;

    /**
     * Compare this Ray2d with another Ray3d and return true when each of the coordinates is less than epsilonCoordinate apart,
     * and the directions (normalized) differ less than epsilonRotation.
     * @param other Ray2d; the point to compare with
     * @param epsilonCoordinate double; the upper bound of difference for one of the coordinates
     * @param epsilonDirection double; the upper bound of difference for the direction(s)
     * @return boolean; true if the locations are less than epsilonCoordinate apart, and the phi differs less than
     *         epsilonRotation and, for 3d theta differs less than epsilonRotation apart, otherwise false
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException epsilonCoordinate or epsilonDirection is NaN or < 0
     */
    boolean epsilonEquals(R other, double epsilonCoordinate, double epsilonDirection)
            throws NullPointerException, IllegalArgumentException;

}
