package org.djutils.draw.line;

import org.djutils.draw.Drawable;
import org.djutils.draw.point.Point;

/**
 * Line is the interface for Line2d and Line3d implementations.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The Point type (2d or 3d)
 */
public interface Line<P extends Point<P>> extends Drawable<P>
{
    /**
     * Return the length of this line. This is NOT the number of points; it is the sum of the lengths of the segments.
     * @return double; the length of this line
     */
    double getLength();

    /**
     * Return one of the points of this line.
     * @param index int; the index of the requested point
     * @return P; the point at the specified index
     * @throws IndexOutOfBoundsException when index < 0 or index >= size
     */
    P get(int index) throws IndexOutOfBoundsException;

    /**
     * Return the first point of this line.
     * @return P; the first point of this line
     */
    default P getFirst()
    {
        try
        {
            return get(0);
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            throw new RuntimeException("cannot happen");
        }
    }

    /**
     * Return the last point of this line.
     * @return P; the last point of this line
     */
    default P getLast()
    {
        try
        {
            return get(size() - 1);
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            throw new RuntimeException("cannot happen");
        }
    }

}
