package org.djutils.draw.point;

import java.io.Serializable;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;

/**
 * Point is the interface for the Point2d and Point3d implementations, standardizing as many of the methods as possible.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <P> The point type
 */
public interface Point<P extends Point<P>> extends Drawable<P>, Serializable
{
    /**
     * Return the x-coordinate. When the point is not in Cartesian space, a calculation to Cartesian space has to be made.
     * @return double; the x-coordinate
     */
    double getX();

    /**
     * Return the y-coordinate. When the point is not in Cartesian space, a calculation to Cartesian space has to be made.
     * @return double; the y-coordinate
     */
    double getY();

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
     * Return the distance to another point.
     * @param otherPoint P; P the other point
     * @return double; the distance (2d or 3d as applicable) to the other point
     */
    double distance(P otherPoint);

    /**
     * Return the squared distance between this point and the provided point.
     * @param otherPoint P; the other point
     * @return double; the squared distance between this point and the other point
     * @throws NullPointerException when otherPoint is null
     */
    double distanceSquared(P otherPoint) throws NullPointerException;

    /**
     * Interpolate towards another Point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation.
     * @param point P; the other point
     * @param fraction double; the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is
     *            between 0 and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0;
     *            <code>this</code> Point is returned; if <code>fraction</code> is 1, the other <code>point</code> is returned
     * @return P; the point that is <code>fraction</code> away on the line between this point and the other point
     * @throws NullPointerException when point is null
     * @throws IllegalArgumentException when fraction is NaN
     */
    P interpolate(P point, double fraction);

    /**
     * Project a point on a line segment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param segmentPoint1 P; start of line segment
     * @param segmentPoint2 P; end of line segment
     * @return P; either <cite>segmentPoint1</cite>, or <cite>segmentPoint2</cite> or a new Point2d that lies somewhere in
     *         between those two.
     * @throws NullPointerException when segmentPoint2, or segmentPoint2 is null
     */
    P closestPointOnSegment(P segmentPoint1, P segmentPoint2) throws NullPointerException;

    /**
     * Project a point on a line. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param linePoint1 P; point on the line
     * @param linePoint2 P; another point on the line
     * @return Point2d; a point on the line that goes through <cite>linePoint1</cite> and <cite>linePoint2</cite>
     * @throws NullPointerException when linePoint1 is null, or linePoint2 is null
     * @throws DrawRuntimeException when <cite>linePoint1</cite> is at the same location as <cite>linePoint2</cite>
     */
    P closestPointOnLine(P linePoint1, P linePoint2) throws NullPointerException, DrawRuntimeException;

    /**
     * A comparison with another point that returns true of each of the coordinates is less than epsilon apart.
     * @param other P; the point to compare with
     * @param epsilon double; the upper bound of difference for one of the coordinates
     * @return boolean; true if both x, y and z (if a Point3d) are less than epsilon apart, otherwise false
     * @throws NullPointerException when other is null
     */
    boolean epsilonEquals(P other, double epsilon) throws NullPointerException;

}
