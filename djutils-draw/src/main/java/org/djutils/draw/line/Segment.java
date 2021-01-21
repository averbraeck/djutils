package org.djutils.draw.line;

import java.io.Serializable;

import org.djutils.draw.Drawable;
import org.djutils.draw.Space;
import org.djutils.draw.point.Point;

/**
 * Segment.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type (2d or 3d)
 * @param <S> The space type (2d or 3d)
 */
public interface Segment<P extends Point<P, S>, S extends Space> extends Drawable<P, S>, Serializable
{
    /**
     * Get the start point of the Segment.
     * @return P; the start point of the Segment
     */
    P getStartPoint();
    
    /**
     * Get the end point of the Segment.
     * @return P; the end point of the Segment
     */
    P getEndPoint();
    
    /**
     * Project a Point on a Segment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param point P; the point to project onto the segment
     * @return P; either the start point, or the end point of the segment or a Point2d that lies somewhere in
     *         between those two.
     */
    P closestPointOnSegment(P point);
    
}

