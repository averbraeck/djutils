package org.djutils.draw.point;

import java.io.Serializable;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;
import org.djutils.draw.Space;

/**
 * Point is the interface for the Point2d and Point3d implementations, standardizing as many of the methods as possible.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type
 * @param <S> The space type (2d or 3d)
 */
public interface Point<P extends Point<P, S>, S extends Space> extends Drawable<P, S>, Serializable
{
    /**
     * Return a new Point with the coordinates of this point scaled by the provided factor.
     * @param factor double; the scale factor
     * @return Point; a new point with the coordinates of this point scaled by the provided factor
     * @throws IllegalArgumentException when factor is NaN
     */
    P scale(double factor) throws IllegalArgumentException;

    /**
     * Return a new Point with negated coordinate values.
     * @return Point; a new point with negated coordinate values
     */
    P neg();

    /**
     * Return a new Point with absolute coordinate values.
     * @return Point; a new point with absolute coordinate values
     */
    P abs();

    /**
     * Return a new Point with a distance of 1 to the origin.
     * @return Point; the normalized point
     * @throws DrawRuntimeException when point is the origin, and no length can be established for scaling
     */
    P normalize() throws DrawRuntimeException;

    /**
     * Return a string representation of the point with a certain number of fraction digits for the coordinates.
     * @param fractionDigits int; the number of fraction digits. Should be 0 or a positive number
     * @return String; a formatted string with a certain number of fraction digits for the coordinates
     */
    String toString(int fractionDigits);

    /**
     * Return a string representation of the point.
     * @return String; a formatted string of the point
     */
    @Override
    String toString();

}
