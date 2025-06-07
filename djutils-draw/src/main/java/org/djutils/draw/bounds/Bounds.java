package org.djutils.draw.bounds;

import java.util.Iterator;

import org.djutils.draw.Drawable;
import org.djutils.draw.point.Point;

/**
 * Bounds is the generic tagging interface that indicates the bounds for an object, where the simplest implementation is minX,
 * minY, maxX and maxY for 2D, and minX, minY, minZ and maxX, maxY and maxZ for 3D. Other bounds such as a BoundingCircle,
 * BoundingSphere or BoundingPolytope could also be defined.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <B> The bounds type (2d or 3d)
 * @param <P> The point type (2d or 3d)
 */
public interface Bounds<B extends Bounds<B, P>, P extends Point<P>> extends Drawable<P>
{
    /**
     * Return the absolute lower bound for x.
     * @return the absolute lower bound for <code>x</code>
     */
    double getMinX();

    /**
     * Return the absolute upper bound for x.
     * @return the absolute upper bound for <code>x</code>
     */
    double getMaxX();

    /**
     * Return the absolute lower bound for y.
     * @return the absolute lower bound for <code>y</code>
     */
    double getMinY();

    /**
     * Return the absolute upper bound for y.
     * @return the absolute upper bound for <code>y</code>
     */
    double getMaxY();

    /**
     * Return the extent of this Bounds2d in the x-direction.
     * @return the extent of this Bounds2d in the x-direction
     */
    default double getDeltaX()
    {
        return getMaxX() - getMinX();
    }

    /**
     * Return the extent of this Bounds2d in the y-direction.
     * @return the extent of this Bounds2d in the y-direction
     */
    default double getDeltaY()
    {
        return getMaxY() - getMinY();
    }

    /**
     * Return the mid point of this Bounds object.
     * @return the mid point of this <code>Bounds</code> object
     */
    P midPoint();

    /**
     * Check if a point is contained in this Bounds.
     * @param point the point
     * @return <code>true</code> if the point is within this Bounds; <code>false</code> if the point is not within this Bounds,
     *         or on an edge of this Bounds
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    boolean contains(P point);

    /**
     * Check if this Bounds completely contains another Bounds. If any point of the other Bounds lies on an edge (2d) or surface
     * (3d) of this Bounds, this method returns <code>false</code>.
     * @param otherBounds the Bounds to check for complete containment within this Bounds.
     * @return <code>false</code> if any point of D is on or outside one of the borders of this Bounds; <code>true</code> when
     *         all points of D are contained within this Bounds.
     * @throws NullPointerException when <code>otherBounds</code> is <code>null</code>
     */
    boolean contains(B otherBounds);

    /**
     * Check if this Bounds covers or touches a point.
     * @param point the Point for which to check if it is covered/touched by this Bounds
     * @return whether this Bounds covers or touches the point
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    boolean covers(P point);

    /**
     * Check if no part of a Bounds is outside this Bounds. The edges/surfaces of this Bounds are considered inside.
     * @param otherBounds the Bounds for which to check if it is covered by this Bounds
     * @return whether this Bounds contains the provided Bounds, including overlapping borders
     * @throws NullPointerException when <code>otherBounds</code> is <code>null</code>
     */
    boolean covers(B otherBounds);

    /**
     * Return whether a Bounds is disjoint from this Bounds. Touching at an edge is <b>not</b> considered disjoint. A Bounds
     * that completely surrounds this Bounds is <b>not</b> disjoint.
     * @param otherBounds the other Bounds
     * @return <code>true</code> if the drawable is disjoint from this Bounds, or only touches an edge; <code>false</code> if
     *         any point of the other Bounds is inside this Bounds, or the other Bounds surrounds this Bounds
     * @throws NullPointerException when <code>otherBounds</code> is <code>null</code>
     */
    boolean disjoint(B otherBounds);

    /**
     * Return whether this Bounds intersects another Bounds. Touching at an edge is considered intersecting.
     * @param otherBounds the other Bounds
     * @return whether this bounding box/rectangle intersects the other Bounds
     * @throws NullPointerException when <code>otherBounds</code> is <code>null</code>
     */
    boolean intersects(B otherBounds);

    /**
     * Return the intersecting Bounds of this Bounds and another Bounds. Touching at an edge is considered intersecting. In case
     * there is no intersection, null is returned.
     * @param otherBounds the other Bounds
     * @return the intersecting Bounds of this Bounds and another Bounds. Touching at the edge is not seen as intersecting. If
     *         not intersecting; <code>null</code> is returned
     * @throws NullPointerException when <code>otherBounds</code> is <code>null</code>
     */
    B intersection(B otherBounds);

    /**
     * After version 2.3.1, the order of the points returned by the iterator of a Bounds object is documented (and changed
     * compared with earlier versions). The returned points now form a Hamiltonian circuit through all the vertices of the
     * bounding box, all steps follow an edge (in the 3D case, not all edges are visited).
     */
    @Override
    Iterator<P> iterator();

}
