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
    /** Use this value for <code>epsilon</code> to disable all filtering. */
    double NO_FILTER = -1.0;

    /**
     * Constructor that can be accessed as a method (used to implement default methods in this interface).
     * @param pointList a list of points
     * @return the new PolyLine
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>pointList</code> has fewer than two points or contains successive duplicate
     *             points
     */
    default L instantiate(final List<P> pointList)
    {
        return instantiate(NO_FILTER, pointList);
    }

    /**
     * Constructor that can be accessed as a method (used to implement default methods in this interface).
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out)
     * @param pointList a list of points
     * @return the new PolyLine
     * @throws NullPointerException when <code>pointList</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>pointList</code> has fewer than two points or contains successive duplicate
     *             points
     */
    L instantiate(double epsilon, List<P> pointList);

    /**
     * Construct a new PolyLine that is equal to this line except for segments that are shorter than the
     * <code>noiseLevel</code>. The result is guaranteed to start with the first point of this line and end with the last point
     * of this line.
     * @param noiseLevel the minimum segment length that is <b>not</b> removed
     * @return the filtered line
     */
    L noiseFilteredLine(double noiseLevel);

    /**
     * Return the length of this line. This is <b>not</b> the number of points; it is the sum of the lengths of the segments.
     * @return the length of this line
     */
    double getLength();

    /**
     * Return one of the points of this line.
     * @param index the index of the requested point
     * @return the point at the specified index
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    P get(int index);

    /**
     * Return the x-coordinate of a point of this PolyLine.
     * @param index the index of the requested x-coordinate
     * @return the x-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    double getX(int index);

    /**
     * Return the y-coordinate of a point of this PolyLine.
     * @param index the index of the requested y-coordinate
     * @return the y-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    double getY(int index);

    /**
     * Return the first point of this PolyLine.
     * @return the first point of this line
     */
    default P getFirst()
    {
        return get(0);
    }

    /**
     * Return the last point of this PolyLine.
     * @return the last point of this line
     */
    default P getLast()
    {
        return get(size() - 1);
    }

    /**
     * Extract one LineSegment of this PolyLine, or Polygon.
     * @param index the rank number of the segment; must be in range 0..Size() - 2 for PolyLine, or 0.. Size() - 1 for
     *            Polygon.
     * @return the LineSegment that connects point index to point index + 1
     * @throws IndexOutOfBoundsException when <code>index</code> &lt; <code>0</code>, or <code>index &ge; size() -
     *             1</code> (in case of a PolyLine, or <code>index &ge; size()</code> in case of a Polygon
     */
    LS getSegment(int index);

    /**
     * Access the internal lengthIndexedLine. Return the cumulative length up to point <code>index</code> of this line
     * @param index the index
     * @return the cumulative length of this line up to point <code>index</code>
     * @throws IndexOutOfBoundsException when <code>index &lt; 0</code>, or <code>index &ge; size()</code>
     */
    double lengthAtIndex(int index);

    /**
     * Construct a new PolyLine with all points of this PolyLine in reverse order.
     * @return the new <code>PolyLine</code>
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
     * @param start fractional starting position, valid range [0..<code>end</code>)
     * @param end fractional ending position, valid range (<code>start</code>..1]
     * @return a new <code>PolyLine</code> covering the selected sub-section
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
     * @param start length along this PolyLine where the sub-section starts, valid range [0..<code>end</code>)
     * @param end length along this PolyLine where the sub-section ends, valid range
     *            (<code>start</code>..<code>length</code> (length is the length of this PolyLine)
     * @return a new <code>PolyLine</code> covering the selected sub-section
     * @throws IllegalArgumentException when <code>start &ge; end</code>, or <code>start &lt; 0</code>, or
     *             <code>end &gt; length</code>
     */
    L extract(double start, double end);

    /**
     * Project a Point on this PolyLine. If the the projected points lies outside this PolyLine, the nearest end point of this
     * PolyLine is returned. Otherwise the returned point lies between the end points of this PolyLine. <br>
     * @param point the point to project onto this PolyLine
     * @return either the start point, or the end point of this PolyLine or a Point that lies somewhere along this PolyLine
     * @throws NullPointerException when <code>point</code> is <code>null</code>
     */
    P closestPointOnPolyLine(P point);

    /**
     * Get the location at a position on the line, with its direction. Position should be between 0.0 and line length.
     * @param position the position on the line for which to calculate the point on the line
     * @return a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point
     * @throws ArithmeticException when position is <code>NaN</code>
     * @throws IllegalArgumentException when <code>position &lt; 0.0</code>, or <code>position &gt; getLength()</code>.
     */
    D getLocation(double position);

    /**
     * Get the location at a position on the line, with its direction. Position can be below 0 or more than the line length. In
     * that case, the position will be extrapolated in the direction of the line at its start or end.
     * @param position the position on the line for which to calculate the point on, before, or after the line
     * @return a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
     *         position is at (or very near) a point on this PolyLine, the direction is either the direction before, or the
     *         direction after that point. If the position is before the start point of this PolyLine, the direction is towards
     *         the start point. If the position is beyond the end of this PolyLine, the direction is the direction of the last
     *         segment of this PolyLine.
     */
    D getLocationExtended(double position);

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction the fraction for which to calculate the point on the line
     * @return a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
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
     * @param fraction the fraction for which to calculate the point on the line
     * @param tolerance the delta from 0.0 and 1.0 that will be forgiven
     * @return a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
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
     * @param fraction the fraction for which to calculate the point on the line
     * @return a DirectedPoint at the position on the line, pointing in the direction of the line at that position. If the
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
     * @param position the position along the line where to truncate the line
     * @return a new PolyLine that follows this PolyLine, but ends at the position where line.getLength() == lengthSI
     * @throws IllegalArgumentException when <code>position &le; 0.0</code>, or <code>position &gt; getLength()</code>
     */
    L truncate(double position);

    /**
     * Binary search for a point index on this PolyLine that is at, or the the nearest one before a given position.
     * @param pos the position to look for
     * @return the position lies between points[index] and points[index+1]
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
     * @param offset the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @return a PolyLine at the specified <code>offset</code> from the this PolyLine
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
     * @param offset the offset; positive values indicate left of the reference line, negative values indicate right of
     *            the reference line
     * @param circlePrecision precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio noise in the reference line less than <code>offset / offsetFilterRatio</code> is
     *            filtered except when the resulting value exceeds <code>offsetMaximumFilterValue</code>
     * @param minimumOffset an offset value less than this value is treated as 0.0
     * @return a PolyLine at the specified offset from the reference line
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
     * @param offsetAtStart the offset at the start of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param offsetAtEnd the offset at the end of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @return a PolyLine at the specified offset from the reference line
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
     * @param offsetAtStart the offset at the start of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param offsetAtEnd the offset at the end of this line; positive values indicate left of the reference line,
     *            negative values indicate right of the reference line
     * @param circlePrecision precision of approximation of arcs; the line segments that are used to approximate an arc
     *            will not deviate from the exact arc by more than this value
     * @param offsetMinimumFilterValue noise in the reference line less than this value is always filtered
     * @param offsetMaximumFilterValue noise in the reference line greater than this value is never filtered
     * @param offsetFilterRatio noise in the reference line less than <code>offset / offsetFilterRatio</code> is
     *            filtered except when the resulting value exceeds <code>offsetMaximumFilterValue</code>
     * @param minimumOffset an offset value less than this value is treated as 0.0
     * @return a PolyLine at the specified offset from the reference line
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
     * @param offsetMinimumFilterValue noise in the reference line less than this value is filtered
     * @return the PolyLine2d of the line at multi-linearly changing offset of the reference line
     * @throws DrawRuntimeException when this method fails to create the offset line
     * @throws IllegalArgumentException when <code>relativeFractions</code> is too short, or differs in length from
     *             <code>offsets</code>
     */
    L offsetLine(double[] relativeFractions, double[] offsets, double offsetMinimumFilterValue);

    /**
     * Make a transition line from this PolyLine to another PolyLine using a user specified function.
     * @param endLine the other PolyLine
     * @param transition defines how the results changes from this <code>line</code> to the <code>endLine</code>
     * @return a transition between this PolyLine and the other PolyLine
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
         * @param fraction the input for the function
         * @return a ratio between 0.0 and 1.0 (values outside this domain are not an error, but will cause the transition line
         *         to go outside the range of the reference line and the other line)
         */
        double function(double fraction);
    }

    /**
     * Filter adjacent points that are (near) duplicates. Works for any number of dimensions.
     * @param epsilon minimum distance between points to be considered different (these will <b>not</b> be filtered out). To
     *            filter out only exactly identical points, specify <code>0.0</code>. To disable all filtering, specify a
     *            <code>NO_FILTER</code> value for <code>epsilon</code>
     * @param coordinates double[][] the coordinates of the points. First index is the dimension (0 for x, etc), second index is
     *            the rank of the point
     * @return filtered coordinates of the points in the same format as <code>coordinates</code>
     */
    default double[][] filterNearDuplicates(final double epsilon, final double[]... coordinates)
    {
        if (NO_FILTER == epsilon)
        {
            return coordinates;
        }
        Throw.when(epsilon < 0, IllegalArgumentException.class,
                "epsilon may not be < 0 (except -1.0 to indicate no filtering)");
        double epsilonSquared = epsilon * epsilon;
        // count the number of points that will be preserved
        int count = 1;
        int prevIndex = 0;
        for (int index = 1; index < coordinates[0].length; index++)
        {
            double distanceSquared = 0.0;
            for (int dimension = 0; dimension < coordinates.length; dimension++)
            {
                double distance = coordinates[dimension][index] - coordinates[dimension][prevIndex];
                distanceSquared += distance * distance;
            }
            if (distanceSquared > epsilonSquared)
            {
                prevIndex = index;
                count++;
            }
        }
        if (count == coordinates[0].length || coordinates[0].length == 2)
        {
            return coordinates; // Nothing to filter
        }
        // Found (near) duplicates to filter
        double[][] result = new double[coordinates.length][count];
        // Copy the first set of coordinates
        for (int dimension = 0; dimension < coordinates.length; dimension++)
        {
            result[dimension][0] = coordinates[dimension][0];
        }
        int next = 1;
        prevIndex = 0;
        for (int index = 1; index < coordinates[0].length; index++)
        {
            double distanceSquared = 0.0;
            for (int dimension = 0; dimension < coordinates.length; dimension++)
            {
                double distance = coordinates[dimension][index] - coordinates[dimension][prevIndex];
                distanceSquared += distance * distance;
            }
            if (distanceSquared > epsilonSquared)
            {
                for (int dimension = 0; dimension < coordinates.length; dimension++)
                {
                    result[dimension][next] = coordinates[dimension][index];
                }
                prevIndex = index;
                next++;
            }
        }
        return result;
    }

}
