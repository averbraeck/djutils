package org.djutils.draw.point;

import org.djutils.draw.Drawable;

/**
 * Point is the interface for the Point2d and Point3d implementations, standardizing as many of the methods as possible.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <P> The point type
 */
public interface Point<P extends Point<P>> extends Drawable<P>
{
    /**
     * Return the x-coordinate.
     * @return the x-coordinate
     */
    double getX();

    /**
     * Return the y-coordinate.
     * @return the y-coordinate
     */
    double getY();

    /**
     * Return a new Point with the coordinates of this point scaled by the provided factor.
     * @param factor the scale factor
     * @return a new point with the coordinates of this point scaled by the provided factor
     * @throws ArithmeticException when <code>factor</code> is <code>NaN</code>
     */
    P scale(double factor);

    /**
     * Return a new Point with negated coordinate values. If this is a <code>DirectedPoint</code>, <code>dirY</code> (in case
     * this is a <code>DirectedPoint3d</code> and <code>dirZ</code> are negated.
     * @return a new point with negated coordinate values
     */
    P neg();

    /**
     * Return a new Point with absolute coordinate values. If this is a <code>DirectedPoint</code>, <code>dirY</code> (in case
     * this is a <code>DirectedPoint3d</code> and <code>dirZ</code> are copied unchanged.
     * @return a new point with absolute coordinate values
     */
    P abs();

    /**
     * Return a new Point with a distance of 1 to the origin.
     * @return the normalized point
     * @throws IllegalArgumentException when point is the origin, and no length can be established for scaling
     */
    P normalize() throws IllegalArgumentException;

    /**
     * Return the distance to another point.
     * @param otherPoint P the other point
     * @return the distance (2d or 3d as applicable) to the other point
     */
    double distance(P otherPoint);

    /**
     * Return the squared distance between this point and the provided point.
     * @param otherPoint the other point
     * @return the squared distance between this point and the other point
     * @throws NullPointerException when <code>otherPoint</code> is <code>null</code>
     */
    double distanceSquared(P otherPoint);

    /**
     * Interpolate towards another Point with a fraction. It is allowed for fraction to be less than zero or larger than 1. In
     * that case the interpolation turns into an extrapolation.
     * @param otherPoint the other point
     * @param fraction the factor for interpolation towards the other point. When &lt;code&gt;fraction&lt;/code&gt; is between 0
     *            and 1, it is an interpolation, otherwise an extrapolation. If <code>fraction</code> is 0; <code>this</code>
     *            Point is returned; if <code>fraction</code> is 1, the other <code>point</code> is returned
     * @return the point that is <code>fraction</code> away on the line between this point and the other point
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     * @throws ArithmeticException when <code>fraction</code> is <code>NaN</code>
     */
    P interpolate(P otherPoint, double fraction);

    /**
     * Project a point on a line segment. If the the projected points lies outside the line segment, the nearest end point of
     * the line segment is returned. Otherwise the returned point lies between the end points of the line segment. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param segmentPoint1 start of line segment
     * @param segmentPoint2 end of line segment
     * @return either <code>segmentPoint1</code>, or <code>segmentPoint2</code> or a new Point2d that lies somewhere in between
     *         those two.
     * @throws NullPointerException when <code>segmentPoint2</code>, or <code>segmentPoint2</code> is <code>null</code>
     */
    P closestPointOnSegment(P segmentPoint1, P segmentPoint2);

    /**
     * Project a point on a line. <br>
     * Adapted from <a href="http://paulbourke.net/geometry/pointlineplane/DistancePoint.java">example code provided by Paul
     * Bourke</a>.
     * @param linePoint1 point on the line
     * @param linePoint2 another point on the line
     * @return a point on the line that goes through <code>linePoint1</code> and <code>linePoint2</code>
     * @throws NullPointerException when <code>linePoint1</code> is <code>null</code>, or <code>linePoint2</code> is
     *             <code>null</code>
     * @throws IllegalArgumentException when <code>linePoint1</code> is at the same location as <code>linePoint2</code>
     */
    P closestPointOnLine(P linePoint1, P linePoint2);

    /**
     * A comparison with another point that returns<code>true</code> of each of the coordinates is less than epsilon apart.
     * @param otherPoint the point to compare with
     * @param epsilon the upper bound of difference for one of the coordinates
     * @return boolean;<code>true</code> if both x, y and z (if a Point3d) are less than epsilon apart, otherwise
     *         <code>false</code>
     * @throws NullPointerException when <code>otherPoint</code> is <code>null</code>
     * @throws ArithmeticException when <code>epsilon</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>epsilon</code> &lt; <code>0.0</code>
     */
    boolean epsilonEquals(P otherPoint, double epsilon);

}
