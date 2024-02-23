package org.djutils.draw;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.point.Point;
import org.djutils.exceptions.Throw;

/**
 * Drawable is an interface to indicate zero or more points can be retrieved to draw the object.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
    Iterator<? extends P> getPoints();

    /**
     * Create a list of all points that make up this Drawable. This method is expensive as a new list is constructed on each
     * invocation.
     * @return List&lt;? extends P&gt;; a list containing all points of this Drawable
     */
    default List<P> getPointList()
    {
        List<P> result = new ArrayList<>(size());
        getPoints().forEachRemaining(result::add);
        return result;
    }

    /**
     * Retrieve the number of points that make up the object.
     * @return int; the number of points that make up the object
     */
    int size();

    /**
     * Return the number of dimensions.
     * @return int; the number of dimensions
     */
    int getDimensions();

    /**
     * Produce a string describing the Drawable using default conversion for the (double) coordinate values. Regrettably, it is
     * not allowed to provide a default implementation here.
     * @return String; a string describing the Drawable
     */
    @Override
    String toString();

    /**
     * Produce a String describing the Drawable.
     * @param doubleFormat String; a format string (something like "%6.3f") which will be used to render every coordinate value)
     * @param doNotIncludeClassName boolean; if true; the output of toString is <b>not</b> prefixed by the class name. This is
     *            useful for concatenating the textual representation of lots of Drawables (e.g. an array, or a List).
     * @return String; textual representation of the Drawable
     */
    String toString(String doubleFormat, boolean doNotIncludeClassName);

    /**
     * Produce a String describing the Drawable.
     * @param doubleFormat String; a format string (something like "%6.3f") which will be used to render every coordinate value)
     * @return String; textual representation of the Drawable
     */
    default String toString(final String doubleFormat)
    {
        return toString(doubleFormat, false);
    }

    /**
     * Produce a String describing the Drawable.
     * @param doNotIncludeClassName boolean; if true; the output of toString is <b>not</b> prefixed by the class name. This is
     *            useful for concatenating the textual representation of lots of Drawables (e.g. an array, or a List).
     * @return String; textual representation of the Drawable
     */
    default String toString(final boolean doNotIncludeClassName)
    {
        return toString("%f", doNotIncludeClassName);
    }

}
