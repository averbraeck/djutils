package org.djutils.draw;

import java.io.Serializable;
import java.util.Iterator;

import org.djutils.draw.point.Point;

/**
 * Drawable.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type (2d or 3d)
 */
public interface Drawable<P extends Point<P>> extends Serializable
{
    /**
     * Retrieve, or generate all points that make up the object.
     * @return Iterable&lt;Point2d&gt;; an iterator that generates all points that make up the object
     */
    Iterator<P> getPoints();
    
    /**
     * Retrieve the number of points that make up the object.
     * @return int; the number of points that make up the object
     */
    int size();
    
    /**
     * Retrieve the location of the object; the median point of the bounding box or bounding rectangle.
     * @return P; the location of the object
     */
    P getLocation();

}

