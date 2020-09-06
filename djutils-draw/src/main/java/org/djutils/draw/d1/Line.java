package org.djutils.draw.d1;

import org.djutils.draw.d0.Point;

/**
 * Line.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Line
{
    /**
     * Return the points of this line as an array.
     * @return Point[]; the points of this line as an array
     */
    Point[] getPointArray();
}
