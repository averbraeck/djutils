package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.Directed;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;
import org.djutils.draw.point.Point;
import org.djutils.exceptions.Throw;

/**
 * PolyLine is the interface for PolyLine2d and PolyLine3d implementations.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <L> the PolyLine type (2d or 3d)
 * @param <P> The matching Point type (2d or 3d)
 * @param <D> The matching Directed type (2d or 3d)
 * @param <R> The matching Ray type (2d or 3d)
 * @param <LS> The matching LineSegment type (2d or 3d)
 */
public interface PolyLine<L extends PolyLine<L, P, R, D, LS>, P extends Point<P>, R extends Ray<R, D, P>, D extends Directed<D>,
        LS extends LineSegment<P, D>> extends Drawable<P>, Project<P>
{
    /**
     * Constructor that can be accessed as a method (used to implement default methods in this interface).
     * @param pointList List&lt;P&gt;; a list of points
     * @return L; the new PolyLine
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>pointList</code> has fewer than two points or contains successive duplicate
     *             points
     */
    L instantiate(List<P> pointList);

    /**
     * Construct a new PolyLine that is equal to this line except for segments that are shorter than the
     * <code>noiseLevel</code>. The result is guaranteed to start with the first point of this line and end with the last point
     * of this line.
     * @param noiseLevel double; the minimum segment length that is <b>not</b> removed
     * @return PolyLine2d; the filtered line
     */
    L noiseFilteredLine(double noiseLevel);

    /**
     * Return the length of this line. This is <b>not</b> the number of points; it is the sum of the lengths of the segments.
     * @return double; the length of this line
     */
    double getLength();

    /**
     * Return one of the points of this line.
     * @param index int; the index of the requested point
     * @return P; the point at the specified index
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    P get(int index);

    /**
     * Return the x-coordinate of a point of this PolyLine.
     * @param index int; the index of the requested x-coordinate
     * @return double; the x-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    double getX(int index);

    /**
     * Return the y-coordinate of a point of this PolyLine.
     * @param index int; the index of the requested y-coordinate
     * @return double; the y-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    double getY(int index);

    /**
     * Return the first point of this PolyLine.
     * @return P; the first point of this line
     */
    default P getFirst()
    {
        return get(0);
    }

    /**
     * Return the last point of this PolyLine.
     * @return P; the last point of this line
     */
    default P getLast()
    {
        return get(size() - 1);
    }

    /**
     * Extract one LineSegment of this PolyLine, or Polygon.
     * @param index int; the rank number of the segment; must be in range 0..Size() - 2 for PolyLine, or 0.. Size() - 1 for
     *            Polygon.
     * @return LS; the LineSegment that connects point index to point index + 1
     * @throws IndexOutOfBoundsException when <code>index</code> &lt; <code>0</code>, or <code>index &ge; size() -
     *             1</code> (in case of a PolyLine, or <code>index &ge; size()</code> in case of a Polygon
     */
    LS getSegment(int index);

    /**
     * Access the internal lengthIndexedLine. Return the cumulative length up to point <code>index</code> of this line
     * @param index int; the index
     * @return double; the cumulative length of this line up to point <code>index</code>
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    double lengthAtIndex(int index);

    /**
     * Construct a new PolyLine with all points of this PolyLine in reverse order.
     * @return L; the new <code>PolyLine</code>
     */
    default L reverse()
    {
        List<P> reversedPoints = new ArrayList<>(size());
        for (int index = size(); --index >= 0;)
        {
            reversedPoints.add(get(index));
        }
        return instantiate(reversedPoints);
    }

    /**
     * Construct a new PolyLine covering the indicated fraction of this PolyLine.
     * @param start double; fractional starting position, valid range [0..<code>end</code>)
     * @param end double; fractional ending position, valid range (<code>start</code>..1]
     * @return L; a new <code>PolyLine</code> covering the selected sub-section
     * @throws IllegalArgumentException when <code>start &ge; end</code>, or <code>start &lt; 0</code>, or
     *             <code>end &gt; 1</code>
     */
    default L extractFractional(final double start, final double end)
    {
        if (start < 0 || start >= end || end > 1)
        {
            throw new IllegalArgumentException(
                    "Bad interval (start=" + start + ", end=" + end + ", this is " + this.toString() + ")");
        }
        return extract(start * getLength(), end * getLength());
    }

    /**
     * Create a new PolyLine that covers a sub-section of this PolyLine.
     * @param start double; length along this PolyLine where the sub-section starts, valid range [0..<code>end</code>)
     * @param end double; length along this PolyLine where the sub-section ends, valid range
     *            (<code>start</code>..<code>length</code> (length is the length of this PolyLine)
     * @return L; a new <code>PolyLine</code> covering the selected sub-section
     * @throws IllegalArgumentException when <code>start &ge; end</code>, or <code>start &lt; 0</code>, or
     *             <code>end &gt; length</code>
     */
    L extract(double start, double end);

    /**
     * Project a Point on this PolyLine. If the the projected points lies outside this PolyLine, the nearest end point of this
     * PolyLine is returned. Otherwise the returned point lies between the end points of this PolyLine. <br>
     * @param point P; the point to project onto this PolyLine
     * @return P; either the start point, or the end point of this PolyLine or a Point that lies somewhere along this PolyLine
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    P closestPointOnPolyLine(P point);

    /**
     * Get the location at a position on the line, with its direction. Position should be between 0.0 and line length.
     * @param position double; the position on the line for which to calculate the point on the line
     * @return D; a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point
     * @throws ArithmeticException when position is <code>NaN</code>
     * @throws IllegalArgumentException when <code>position &lt; 0.0</code>, or <code>position &gt; getLength()</code>.
     */
    D getLocation(double position);

    /**
     * Get the location at a position on the line, with its direction. Position can be below 0 or more than the line length. In
     * that case, the position will be extrapolated in the direction of the line at its start or end.
     * @param position double; the position on the line for which to calculate the point on, before, or after the line
     * @return D; a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point. If the position is before the start point of this PolyLine, the direction is towards
     *         the start point. If the position is beyond the end of this PolyLine, the direction is the direction of the last
     *         segment of this PolyLine.
     */
    D getLocationExtended(double position);

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return D; a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point
     * @throws IllegalArgumentException when <code>fraction &lt; 0.0</code> or <code>fraction &gt;
     *             1.0</code>
     */
    default D getLocationFraction(final double fraction)
    {
        Throw.when(fraction < 0.0 || fraction > 1.0, IllegalArgumentException.class,
                "illegal fraction (got %f, should be in range [0.0, 1.0])", fraction);
        return getLocation(fraction * getLength());
    }

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @param tolerance double; the delta from 0.0 and 1.0 that will be forgiven
     * @return D; a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point. If the position is before the start point of this PolyLine, the direction is towards
     *         the start point. If the position is beyond the end of this PolyLine, the direction is the direction of the last
     *         segment of this PolyLine.
     * @throws IllegalArgumentException when <code>fraction &lt; -tolerance</code>, or <code>fraction &gt;
     *             1.0 + tolerance</code>
     */
    default D getLocationFraction(final double fraction, final double tolerance)
    {
        Throw.when(fraction < -tolerance || fraction > 1.0 + tolerance, IllegalArgumentException.class,
                "illegal fraction (got %f, should be within %f of [0.0, 1.0])", fraction, tolerance);
        double f = fraction < 0 ? 0.0 : fraction > 1.0 ? 1.0 : fraction;
        return getLocation(f * getLength());
    }

    /**
     * Get the location at a fraction of the line (or outside the line), with its direction.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return D; a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point. If the position is before the start point of this PolyLine, the direction is towards
     *         the start point. If the position is beyond the end of this PolyLine, the direction is the direction of the last
     *         segment of this PolyLine.
     */
    default D getLocationFractionExtended(final double fraction)
    {
        return getLocationExtended(fraction * getLength());
    }

    /**
     * Truncate this PolyLine at the given length (less than the length of the line, and larger than zero) and return a new
     * line.
     * @param position double; the position along the line where to truncate the line
     * @return L; a new PolyLine that follows this PolyLine, but ends at the position where line.getLength() == lengthSI
     * @throws IllegalArgumentException when <code>position &le; 0.0</code>, or <code>position &gt; getLength()</code>
     */
    L truncate(double position);

    /**
     * Binary search for a point index on this PolyLine that is at, or the the nearest one before a given position.
     * @param pos double; the position to look for
     * @return the index below the position; the position lies between points[index] and points[index+1]
     * @throws DrawRuntimeException when the point index could not be found (should never happen)
     */
    default int find(final double pos)
    {
        if (pos == 0)
        {
            return 0;
        }

        int lo = 0;
        int hi = size() - 1;
        while (lo <= hi)
        {
            if (hi == lo)
            {
                return lo;
            }
            int mid = lo + (hi - lo) / 2;
            if (pos < lengthAtIndex(mid))
            {
                hi = mid - 1;
            }
            else if (pos > lengthAtIndex(mid + 1))
            {
                lo = mid + 1;
            }
            else
            {
                return mid;
            }
        }
        throw new DrawRuntimeException("Could not find position " + pos + " on line with length: " + getLength());
    }

    /** Default precision of approximation of arcs in the offsetLine method. */
    double DEFAULT_CIRCLE_PRECISION = 0.001;

    /** By default, noise in the reference line of the offsetLine method less than this value is always filtered. */
    double DEFAULT_OFFSET_MINIMUM_FILTER_VALUE = 0.001;

    /** By default, noise in the reference line of the offsetLineMethod greater than this value is never filtered. */
    double DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE = 0.1;

    /**
     * By default, noise in the reference line of the offsetLineMethod less than <code>offset / offsetFilterRatio</code> is
     * filtered except when the resulting value exceeds <code>offsetMaximumFilterValue</code>.
     */
    double DEFAULT_OFFSET_FILTER_RATIO = 10;

    /** By default, the offsetLineMethod uses this offset precision. */
    double DEFAULT_OFFSET_PRECISION = 0.00001;

    /**
     * Construct an offset PolyLine. This is similar to what geographical specialists call buffering, except that this method
     * only construct a new line on one side of the reference line and does not add half disks (or miters) at the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <code>this</code> (the reference line) can cause major artifacts in the offset line. This method calls the underlying
     * method with default values for circlePrecision (<code>DEFAULT_OFFSET</code>), offsetMinimumFilterValue
     * (<code>DEFAULT_OFFSET_MINIMUM_FILTER_VALUE</code>), offsetMaximumFilterValue
     * (<code>DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE</code>), offsetFilterRatio (<code>DEFAULT_OFFSET_FILTER_RATIO</code>),
     * minimumOffset (<code>DEFAULT_OFFSET_PRECISION</code>). <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offset double; the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @return L; a PolyLine at the specified <code>offset</code> from the this PolyLine
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    default L offsetLine(final double offset) throws DrawRuntimeException
    {
        return offsetLine(offset, DEFAULT_CIRCLE_PRECISION, DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, DEFAULT_OFFSET_FILTER_RATIO, DEFAULT_OFFSET_PRECISION);
    }

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks (or miters) around the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <code>this</code> (the reference line) can cause major artifacts in the offset line. <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offset double; the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @param circlePrecision double; precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue double; noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue double; noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio double; noise in the reference line less than <code>offset / offsetFilterRatio</code> is
     *            filtered except when the resulting value exceeds <code>offsetMaximumFilterValue</code>
     * @param minimumOffset double; an offset value less than this value is treated as 0.0
     * @return L; a PolyLine at the specified offset from the reference line
     * @throws ArithmeticException when <code>offset</code>, or <code>circlePrecision</code>,
     *             <code>offsetMinimumFilterValue</code>, <code>offsetMaximumfilterValue</code>, <code>offsetFilterRatio</code>,
     *             or <code>minimumOffset</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>circlePrecision</code>, <code>offsetMinimumFilterValue</code>,
     *             <code>offsetMaximumfilterValue</code>, <code>offsetFilterRatio</code>, or <code>minimumOffset</code> is not
     *             positive, or <code>offsetMinimumFilterValue &ge; offsetMaximumFilterValue</code>
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    L offsetLine(double offset, double circlePrecision, double offsetMinimumFilterValue, double offsetMaximumFilterValue,
            double offsetFilterRatio, double minimumOffset);

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks (or miters) around the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <code>this</code> (the reference line) can cause major artifacts in the offset line. This method calls the underlying
     * method with default values for circlePrecision (<code>DEFAULT_OFFSET</code>), offsetMinimumFilterValue
     * (<code>DEFAULT_OFFSET_MINIMUM_FILTER_VALUE</code>), offsetMaximumFilterValue
     * (<code>DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE</code>), offsetFilterRatio (<code>DEFAULT_OFFSET_FILTER_RATIO</code>),
     * minimumOffset (<code>DEFAULT_OFFSET_PRECISION</code>). <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offsetAtStart double; the offset at the start of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param offsetAtEnd double; the offset at the end of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @return L; a PolyLine at the specified offset from the reference line
     * @throws ArithmeticException when <code>offset</code>, or <code>circlePrecision</code>,
     *             <code>offsetMinimumFilterValue</code>, <code>offsetMaximumfilterValue</code>, <code>offsetFilterRatio</code>,
     *             or <code>minimumOffset</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>circlePrecision</code>, <code>offsetMinimumFilterValue</code>,
     *             <code>offsetMaximumfilterValue</code>, <code>offsetFilterRatio</code>, or <code>minimumOffset</code> is not
     *             positive, or <code>offsetMinimumFilterValue &ge; offsetMaximumFilterValue</code>.
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    default L offsetLine(final double offsetAtStart, final double offsetAtEnd)
    {
        return offsetLine(offsetAtStart, offsetAtEnd, DEFAULT_CIRCLE_PRECISION, DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, DEFAULT_OFFSET_FILTER_RATIO, DEFAULT_OFFSET_PRECISION);
    }

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks (or miters) around the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <code>this</code> (the reference line) can cause major artifacts in the offset line. <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offsetAtStart double; the offset at the start of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param offsetAtEnd double; the offset at the end of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param circlePrecision double; precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue double; noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue double; noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio double; noise in the reference line less than <code>offset / offsetFilterRatio</code> is
     *            filtered except when the resulting value exceeds <code>offsetMaximumFilterValue</code>
     * @param minimumOffset double; an offset value less than this value is treated as 0.0
     * @return L; a PolyLine at the specified offset from the reference line
     * @throws ArithmeticException when <code>offset</code>, or <code>circlePrecision</code>,
     *             <code>offsetMinimumFilterValue</code>, <code>offsetMaximumfilterValue</code>, <code>offsetFilterRatio</code>,
     *             or <code>minimumOffset</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>circlePrecision</code>, <code>offsetMinimumFilterValue</code>,
     *             <code>offsetMaximumfilterValue</code>, <code>offsetFilterRatio</code>, or <code>minimumOffset</code> is not
     *             positive, or <code>offsetMinimumFilterValue &ge; offsetMaximumFilterValue</code>
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    L offsetLine(double offsetAtStart, double offsetAtEnd, double circlePrecision, double offsetMinimumFilterValue,
            double offsetMaximumFilterValue, double offsetFilterRatio, double minimumOffset);

    /**
     * Create a line at linearly varying offset from this line. The offset may change linearly from its initial value at the
     * start of the reference line via a number of intermediate offsets at intermediate positions to its final offset value at
     * the end of the reference line.
     * @param relativeFractions positional fractions for which the offsets have to be generated
     * @param offsets offsets at the relative positions (positive value is Left, negative value is Right)
     * @param offsetMinimumFilterValue double; noise in the reference line less than this value is filtered
     * @return the PolyLine2d of the line at multi-linearly changing offset of the reference line
     * @throws DrawRuntimeException when this method fails to create the offset line
     * @throws IllegalArgumentException when <code>relativeFractions</code> is too short, or differs in length from
     *             <code>offsets</code>
     */
    L offsetLine(double[] relativeFractions, double[] offsets, double offsetMinimumFilterValue);

    /**
     * Make a transition line from this PolyLine to another PolyLine using a user specified function.
     * @param endLine L; the other PolyLine
     * @param transition TransitionFunction; how the results changes from this line to the other line
     * @return L; a transition between this PolyLine and the other PolyLine
     * @throws ArithmeticException when the transition function returns <code>NaN</code> at some point
     * @throws DrawRuntimeException when construction of some point along the way fails
     */
    L transitionLine(L endLine, TransitionFunction transition);

    /**
     * Interface for transition function.
     */
    interface TransitionFunction
    {
        /**
         * Function that returns some value for inputs between 0.0 and 1.0. For a smooth transition, this function should return
         * 0.0 for input 0.0 and 1.0 for input 1.0 and be continuous and smooth.
         * @param fraction double; the input for the function
         * @return double; a ratio between 0.0 and 1.0 (values outside this domain are not an error, but will cause the
         *         transition line to go outside the range of the reference line and the other line)
         */
        double function(double fraction);
    }

}
