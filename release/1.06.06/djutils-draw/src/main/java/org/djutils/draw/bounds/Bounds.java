package org.djutils.draw.bounds;

import org.djutils.draw.Space;

/**
 * Bounds is the generic tagging interface that indicates the bounds for an object, where the simplest implementation is minX,
 * minY, maxX and maxY for 2D, and minX, minY, minZ and maxX, maxY and maxZ for 3D. Other bounds such as a BoundingCircle,
 * BoundingSphere or BoundingPolytope could also be defined.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <B> The bounds type
 * @param <S> The space type (2d or 3d)
 */
public interface Bounds<B extends Bounds<B, S>, S extends Space>
{
    /**
     * Return the lower bound for x.
     * @return double; the lower bound for x
     */
    double getMinX();

    /**
     * Return the upper bound for x.
     * @return double; the upper bound for x
     */
    double getMaxX();

    /**
     * Return the lower bound for y.
     * @return double; the lower bound for y
     */
    double getMinY();

    /**
     * Return the upper bound for y.
     * @return double; the upper bound for y
     */
    double getMaxY();

    /**
     * Return the extent of this Bounds2d in the x-direction.
     * @return double; the extent of this Bounds2d in the x-direction
     */
    default double getDeltaX()
    {
        return getMaxX() - getMinX();
    }

    /**
     * Return the extent of this Bounds2d in the y-direction.
     * @return double; the extent of this Bounds2d in the y-direction
     */
    default double getDeltaY()
    {
        return getMaxY() - getMinY();
    }

    /**
     * Check if this Bounds contains another Bounds. Covers returns true when one of the edges of the other Bounds (partly)
     * overlaps a border of this Bounds.
     * @param otherBounds Bounds; the Bounds for which to check if it is contained within this Bounds
     * @return boolean; whether this Bounds contains the provided Bounds, including overlapping borders
     * @throws NullPointerException when otherBounds is null
     */
    boolean covers(B otherBounds) throws NullPointerException;

    /**
     * Return whether this Bounds is disjoint from another Bounds. Only touching at an edge is considered disjoint.
     * @param otherBounds Bounds; the other Bounds
     * @return boolean; whether this Bounds is disjoint from another Bounds
     * @throws NullPointerException when bounds is null
     */
    boolean disjoint(B otherBounds) throws NullPointerException;

    /**
     * Return whether this Bounds intersects another Bounds. Only touching at an edge is not seen as intersecting.
     * @param otherBounds Bounds; the other Bounds
     * @return boolean; whether this bounding rectangle intersects the other Bounds
     * @throws NullPointerException when otherBounds is null
     */
    boolean intersects(B otherBounds);

    /**
     * Return the intersecting Bounds of this Bounds and another Bounds. Touching at the edge is not seen as intersecting. In
     * case there is no intersection, null is returned.
     * @param otherBounds Bounds; the other Bounds
     * @return Bounds; the intersecting Bounds of this Bounds and another Bounds. Touching at the edge is not seen as
     *         intersecting. If not intersecting; null is returned
     * @throws NullPointerException when otherBounds is null
     */
    B intersection(B otherBounds);

}
