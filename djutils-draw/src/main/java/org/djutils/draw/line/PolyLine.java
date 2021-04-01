package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable;
import org.djutils.draw.Space;
import org.djutils.draw.point.Point;

/**
 * PolyLine is the interface for PolyLine2d and PolyLine3d implementations.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <L> the PolyLine type (2d or 3d)
 * @param <P> The matching Point type (2d or 3d)
 * @param <S> The matching Space type (2d, or 3d)
 * @param <R> The matching Ray type (2d or 3d)
 * @param <LS> The matching LineSegment type (2d or 3d)
 */
public interface PolyLine<L extends PolyLine<L, P, S, R, LS>, P extends Point<P, S>, S extends Space, R extends Ray<R, P, S>,
        LS extends LineSegment<P, R, S>> extends Drawable<P, S>, Project<P, S>
{
    /**
     * Constructor that can be accessed as a method (used to implement default methods in this interface).
     * @param pointList List&lt;P&gt;; a list of points
     * @return L; the new PolyLine
     * @throws NullPointerException when pointList is null
     * @throws DrawRuntimeException when pointList has fewer than two points or contains successive duplicate points
     */
    L instantiate(List<P> pointList) throws NullPointerException, DrawRuntimeException;

    /**
     * Construct a new PolyLine that is equal to this line except for segments that are shorter than the
     * <cite>noiseLevel</cite>. The result is guaranteed to start with the first point of this line and end with the last point
     * of this line.
     * @param noiseLevel double; the minimum segment length that is <b>not</b> removed
     * @return PolyLine2d; the filtered line
     */
    L noiseFilteredLine(double noiseLevel);

    /**
     * Return the length of this line. This is NOT the number of points; it is the sum of the lengths of the segments.
     * @return double; the length of this line
     */
    double getLength();

    /**
     * Return one of the points of this line.
     * @param index int; the index of the requested point
     * @return P; the point at the specified index
     * @throws IndexOutOfBoundsException when index &lt; 0 or index &gt;= size
     */
    P get(int index) throws IndexOutOfBoundsException;

    /**
     * Return the x-coordinate of a point of this PolyLine.
     * @param index int; the index of the requested x-coordinate
     * @return double; the x-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when index &lt; 0 or index &gt;= size()
     */
    double getX(int index) throws IndexOutOfBoundsException;

    /**
     * Return the y-coordinate of a point of this PolyLine.
     * @param index int; the index of the requested y-coordinate
     * @return double; the y-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when index &lt; 0 or index &gt;= size()
     */
    double getY(int index) throws IndexOutOfBoundsException;

    /**
     * Return the first point of this PolyLine.
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
     * Return the last point of this PolyLine.
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

    /**
     * Extract one LineSegment of this PolyLine, or Polygon.
     * @param index int; the rank number of the segment; must be in range 0..Size() - 2 for PolyLine, or 0.. Size() - 1 for
     *            Polygon.
     * @return LS; the LineSegment that connects point index to point index + 1
     */
    LS getSegment(int index);

    /**
     * Access the internal lengthIndexedLine. Return the cumulative length up to point <code>index</code> of this line
     * @param index int; the index
     * @return double; the cumulative length of this line up to point <code>index</code>
     * @throws IndexOutOfBoundsException when index &lt; 0 or index &gt;= size()
     */
    double lengthAtIndex(int index) throws IndexOutOfBoundsException;

    /**
     * Construct a new PolyLine with all points of this PolyLine in reverse order.
     * @return L; the new PolyLine
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
     * @param start double; fractional starting position, valid range [0..<cite>end</cite>)
     * @param end double; fractional ending position, valid range (<cite>start</cite>..1]
     * @return L; a new PolyLine covering the selected sub-section
     * @throws DrawRuntimeException when start &gt;= end, or start &lt; 0, or end &gt; 1
     */
    default L extractFractional(final double start, final double end) throws DrawRuntimeException
    {
        if (start < 0 || start >= end || end > 1)
        {
            throw new DrawRuntimeException(
                    "Bad interval (start=" + start + ", end=" + end + ", this is " + this.toString() + ")");
        }
        return extract(start * getLength(), end * getLength());
    }

    /**
     * Create a new PolyLine that covers a sub-section of this PolyLine.
     * @param start double; length along this PolyLine where the sub-section starts, valid range [0..<cite>end</cite>)
     * @param end double; length along this PolyLine where the sub-section ends, valid range
     *            (<cite>start</cite>..<cite>length</cite> (length is the length of this PolyLine)
     * @return L; a new PolyLine covering the selected sub-section
     * @throws DrawRuntimeException when start &gt;= end, or start &lt; 0, or end &gt; length
     */
    L extract(double start, double end) throws DrawRuntimeException;

    /**
     * Project a Point on this PolyLine. If the the projected points lies outside this PolyLine, the nearest end point of this
     * PolyLine is returned. Otherwise the returned point lies between the end points of this PolyLine. <br>
     * @param point P; the point to project onto this PolyLine
     * @return P; either the start point, or the end point of this PolyLine or a Point that lies somewhere along this PolyLine.
     * @throws NullPointerException when point is null
     */
    P closestPointOnPolyLine(P point) throws NullPointerException;

    /**
     * Get the location at a position on the line, with its direction. Position should be between 0.0 and line length.
     * @param position double; the position on the line for which to calculate the point on the line
     * @return R; a Ray at the position on the line, pointing in the direction of the line at that position. If the position is
     *         at (or very near) a point on this PolyLine, the direction is either the direction before, or the direction after
     *         that point
     * @throws DrawRuntimeException when position is NaN, less than 0.0, or more than line length.
     */
    R getLocation(double position) throws DrawRuntimeException;

    /**
     * Get the location at a position on the line, with its direction. Position can be below 0 or more than the line length. In
     * that case, the position will be extrapolated in the direction of the line at its start or end.
     * @param position double; the position on the line for which to calculate the point on, before, or after the line
     * @return R; a Ray at the position on the line, pointing in the direction of the line at that position. If the position is
     *         at (or very near) a point on this PolyLine, the direction is either the direction before, or the direction after
     *         that point. If the position is before the start point of this PolyLine, the direction is towards the start point.
     *         If the position is beyond the end of this PolyLine, the direction is the direction of the last segment of this
     *         PolyLine.
     */
    R getLocationExtended(double position);

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return R; a Ray at the position on the line, pointing in the direction of the line at that position. If the position is
     *         at (or very near) a point on this PolyLine, the direction is either the direction before, or the direction after
     *         that point
     * @throws DrawRuntimeException when fraction less than 0.0 or more than 1.0.
     */
    default R getLocationFraction(final double fraction) throws DrawRuntimeException
    {
        if (fraction < 0.0 || fraction > 1.0)
        {
            throw new DrawRuntimeException("getLocationFraction for line: fraction < 0.0 or > 1.0. fraction = " + fraction);
        }
        return getLocation(fraction * getLength());
    }

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @param tolerance double; the delta from 0.0 and 1.0 that will be forgiven
     * @return R; a Ray at the position on the line, pointing in the direction of the line at that position. If the position is
     *         at (or very near) a point on this PolyLine, the direction is either the direction before, or the direction after
     *         that point. If the position is before the start point of this PolyLine, the direction is towards the start point.
     *         If the position is beyond the end of this PolyLine, the direction is the direction of the last segment of this
     *         PolyLine.
     * @throws DrawRuntimeException when fraction less than 0.0 or more than 1.0.
     */
    default R getLocationFraction(final double fraction, final double tolerance) throws DrawRuntimeException
    {
        if (fraction < -tolerance || fraction > 1.0 + tolerance)
        {
            throw new DrawRuntimeException(
                    "getLocationFraction for line: fraction < 0.0 - tolerance or > 1.0 + tolerance; fraction = " + fraction);
        }
        double f = fraction < 0 ? 0.0 : fraction > 1.0 ? 1.0 : fraction;
        return getLocation(f * getLength());
    }

    /**
     * Get the location at a fraction of the line (or outside the line), with its direction.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return R; a Ray at the position on the line, pointing in the direction of the line at that position. If the position is
     *         at (or very near) a point on this PolyLine, the direction is either the direction before, or the direction after
     *         that point. If the position is before the start point of this PolyLine, the direction is towards the start point.
     *         If the position is beyond the end of this PolyLine, the direction is the direction of the last segment of this
     *         PolyLine.
     */
    default R getLocationFractionExtended(final double fraction)
    {
        return getLocationExtended(fraction * getLength());
    }

    /**
     * Truncate this PolyLine at the given length (less than the length of the line, and larger than zero) and return a new
     * line.
     * @param position double; the position along the line where to truncate the line
     * @return L; a new PolyLine that follows this PolyLine, but ends at the position where line.getLength() == lengthSI
     * @throws DrawRuntimeException when position less than 0.0 or more than line length.
     */
    L truncate(double position) throws DrawRuntimeException;

    /**
     * Binary search for a point index on this PolyLine that is at, or the the nearest one before a given position.
     * @param pos double; the position to look for
     * @return the index below the position; the position lies between points[index] and points[index+1]
     * @throws DrawRuntimeException when index could not be found
     */
    default int find(final double pos) throws DrawRuntimeException
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

    /**
     * Convert this PolyLine to something that MS-Excel can plot.
     * @return String MS-excel XY, or XYZ plottable output
     */
    String toExcel();

    /** Default precision of approximation of arcs in the offsetLine method. */
    double DEFAULT_CIRCLE_PRECISION = 0.001;

    /** By default, noise in the reference line of the offsetLine method less than this value is always filtered. */
    double DEFAULT_OFFSET_MINIMUM_FILTER_VALUE = 0.001;

    /** By default, noise in the reference line of the offsetLineMethod greater than this value is never filtered. */
    double DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE = 0.1;

    /**
     * By default, noise in the reference line of the offsetLineMethod less than <cite>offset / offsetFilterRatio</cite> is
     * filtered except when the resulting value exceeds <cite>offsetMaximumFilterValue</cite>.
     */
    double DEFAULT_OFFSET_FILTER_RATIO = 10;

    /** By default, the offsetLineMethod uses this offset precision. */
    double DEFAULT_OFFSET_PRECISION = 0.00001;

    /**
     * Construct an offset PolyLine. This is similar to what geographical specialists call buffering, except that this method
     * only construct a new line on one side of the reference line and does not add half disks (or miters) at the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <cite>this</cite> (the reference line) can cause major artifacts in the offset line. This method calls the underlying
     * method with default values for circlePrecision (<cite>DEFAULT_OFFSET</cite>), offsetMinimumFilterValue
     * (<cite>DEFAULT_OFFSET_MINIMUM_FILTER_VALUE</cite>), offsetMaximumFilterValue
     * (<cite>DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE</cite>), offsetFilterRatio (<cite>DEFAULT_OFFSET_FILTER_RATIO</cite>),
     * minimumOffset (<cite>DEFAULT_OFFSET_PRECISION</cite>). <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offset double; the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @return L; a PolyLine at the specified offset from the this PolyLine
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    default L offsetLine(double offset) throws DrawRuntimeException
    {
        return offsetLine(offset, DEFAULT_CIRCLE_PRECISION, DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, DEFAULT_OFFSET_FILTER_RATIO, DEFAULT_OFFSET_PRECISION);
    }

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks (or miters) around the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <cite>this</cite> (the reference line) can cause major artifacts in the offset line. <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offset double; the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @param circlePrecision double; precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue double; noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue double; noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio double; noise in the reference line less than <cite>offset / offsetFilterRatio</cite> is
     *            filtered except when the resulting value exceeds <cite>offsetMaximumFilterValue</cite>
     * @param minimumOffset double; an offset value less than this value is treated as 0.0
     * @return L; a PolyLine at the specified offset from the reference line
     * @throws IllegalArgumentException when offset is NaN, or circlePrecision, offsetMinimumFilterValue,
     *             offsetMaximumfilterValue, offsetFilterRatio, or minimumOffset is not positive, or NaN, or
     *             offsetMinimumFilterValue &gt;= offsetMaximumFilterValue
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    L offsetLine(double offset, double circlePrecision, double offsetMinimumFilterValue, double offsetMaximumFilterValue,
            double offsetFilterRatio, double minimumOffset) throws IllegalArgumentException, DrawRuntimeException;

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks (or miters) around the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <cite>this</cite> (the reference line) can cause major artifacts in the offset line. This method calls the underlying
     * method with default values for circlePrecision (<cite>DEFAULT_OFFSET</cite>), offsetMinimumFilterValue
     * (<cite>DEFAULT_OFFSET_MINIMUM_FILTER_VALUE</cite>), offsetMaximumFilterValue
     * (<cite>DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE</cite>), offsetFilterRatio (<cite>DEFAULT_OFFSET_FILTER_RATIO</cite>),
     * minimumOffset (<cite>DEFAULT_OFFSET_PRECISION</cite>). <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offsetAtStart double; the offset at the start of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param offsetAtEnd double; the offset at the end of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @return L; a PolyLine at the specified offset from the reference line
     * @throws IllegalArgumentException when offset is NaN, or circlePrecision, offsetMinimumFilterValue,
     *             offsetMaximumfilterValue, offsetFilterRatio, or minimumOffset is not positive, or NaN, or
     *             offsetMinimumFilterValue &gt;= offsetMaximumFilterValue
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    default L offsetLine(double offsetAtStart, double offsetAtEnd) throws IllegalArgumentException, DrawRuntimeException
    {
        return offsetLine(offsetAtStart, offsetAtEnd, DEFAULT_CIRCLE_PRECISION, DEFAULT_OFFSET_MINIMUM_FILTER_VALUE,
                DEFAULT_OFFSET_MAXIMUM_FILTER_VALUE, DEFAULT_OFFSET_FILTER_RATIO, DEFAULT_OFFSET_PRECISION);
    }

    /**
     * Construct an offset line. This is similar to what geographical specialists call buffering, except that this method only
     * construct a new line on one side of the reference line and does not add half disks (or miters) around the end points.
     * This method tries to strike a delicate balance between generating too few and too many points to approximate arcs. Noise
     * in <cite>this</cite> (the reference line) can cause major artifacts in the offset line. <br>
     * In the 3D version the offset is parallel to the X-Y plane.
     * @param offsetAtStart double; the offset at the start of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param offsetAtEnd double; the offset at the end of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param circlePrecision double; precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue double; noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue double; noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio double; noise in the reference line less than <cite>offset / offsetFilterRatio</cite> is
     *            filtered except when the resulting value exceeds <cite>offsetMaximumFilterValue</cite>
     * @param minimumOffset double; an offset value less than this value is treated as 0.0
     * @return L; a PolyLine at the specified offset from the reference line
     * @throws IllegalArgumentException when offset is NaN, or circlePrecision, offsetMinimumFilterValue,
     *             offsetMaximumfilterValue, offsetFilterRatio, or minimumOffset is not positive, or NaN, or
     *             offsetMinimumFilterValue &gt;= offsetMaximumFilterValue
     * @throws DrawRuntimeException Only if P is PolyLine3d and the line cannot be projected into 2d
     */
    L offsetLine(double offsetAtStart, double offsetAtEnd, double circlePrecision, double offsetMinimumFilterValue,
            double offsetMaximumFilterValue, double offsetFilterRatio, double minimumOffset)
            throws IllegalArgumentException, DrawRuntimeException;

    /**
     * Make a transition line from this PolyLine to another PolyLine using a user specified function.
     * @param endLine L; the other PolyLine
     * @param transition TransitionFunction; how the results changes from this line to the other line
     * @return L; a transition between this PolyLine and the other PolyLine
     * @throws DrawRuntimeException when construction of some point along the way fails. E.g. when the transition function
     *             returns NaN.
     */
    L transitionLine(L endLine, TransitionFunction transition) throws DrawRuntimeException;

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
