package org.djutils.draw.line;

import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.DrawException;
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
 * @param <L> the Line type (2d or 3d)
 * @param <P> The Point type (2d or 3d)
 * @param <S> The Space type (2d, or 3d)
 * @param <R> The matching Ray type (2d or 3d)
 */
public interface PolyLine<L extends PolyLine<L, P, S, R>, P extends Point<P, S>, S extends Space, R> extends Drawable<P, S>
{
    /**
     * Constructor that can be accessed as a method (used to implement default methods in this interface).
     * @param pointList List&lt;P&gt;; a list of points
     * @return L; the new line
     * @throws NullPointerException when pointList is null
     * @throws DrawRuntimeException when pointList has fewer than two points or contains successive duplicate points
     */
    L instantiate(List<P> pointList) throws NullPointerException, DrawRuntimeException;

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
     * @throws IndexOutOfBoundsException
     */
    double getX(int index) throws IndexOutOfBoundsException;

    /**
     * Return the y-coordinate of a point of this PolyLine.
     * @param index int; the index of the requested y-coordinate
     * @return double; the y-coordinate of the requested point of this PolyLine
     * @throws IndexOutOfBoundsException
     */
    double getY(int index) throws IndexOutOfBoundsException;

    /**
     * Return the first point of this line.
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
     * Return the last point of this line.
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
     * Access the internal lengthIndexedLine. Return the cumulative length up to point <code>index</code> of this line
     * @param index int; the index
     * @return double; the cumulative length of this line up to point <code>index</code>
     * @throws IndexOutOfBoundsException when index &lt; 0 or index &gt;= size()
     */
    double lengthAtIndex(int index) throws IndexOutOfBoundsException;

    /**
     * Construct a new Line with all points of this Line in reverse order.
     * @return L; the new Line
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
     * Construct a new L covering the indicated fraction of this L.
     * @param start double; starting point, valid range [0..<cite>end</cite>)
     * @param end double; ending point, valid range (<cite>start</cite>..1]
     * @return Line3d; the new L
     * @throws DrawException when start &gt;= end, or start &lt; 0, or end &gt; 1
     */
    default L extractFractional(final double start, final double end) throws DrawException
    {
        if (start < 0 || start >= end || end > 1)
        {
            throw new DrawException("Bad interval (start=" + start + ", end=" + end + ", this is " + this.toString() + ")");
        }
        return extract(start * getLength(), end * getLength());
    }

    /**
     * Create a new L that covers a sub-section of this L.
     * @param start double; length along this Line3d where the sub-section starts, valid range [0..<cite>end</cite>)
     * @param end double; length along this Line3d where the sub-section ends, valid range
     *            (<cite>start</cite>..<cite>length</cite> (length is the length of this L)
     * @return L; the selected sub-section
     * @throws DrawException when start &gt;= end, or start &lt; 0, or end &gt; length
     */
    L extract(double start, double end) throws DrawException;

    /**
     * Get the location at a position on the line, with its direction. Position should be between 0.0 and line length.
     * @param position double; the position on the line for which to calculate the point on the line
     * @return OP; an oriented point
     * @throws DrawException when position is NaN, less than 0.0, or more than line length.
     */
    R getLocation(double position) throws DrawException;

    /**
     * Get the location at a position on the line, with its direction. Position can be below 0 or more than the line length. In
     * that case, the position will be extrapolated in the direction of the line at its start or end.
     * @param position double; the position on the line for which to calculate the point on, before, or after the line
     * @return OP; an oriented point
     */
    R getLocationExtended(double position);

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return OP; an oriented point
     * @throws DrawException when fraction less than 0.0 or more than 1.0.
     */
    default R getLocationFraction(final double fraction) throws DrawException
    {
        if (fraction < 0.0 || fraction > 1.0)
        {
            throw new DrawException("getLocationFraction for line: fraction < 0.0 or > 1.0. fraction = " + fraction);
        }
        return getLocation(fraction * getLength());
    }

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @param tolerance double; the delta from 0.0 and 1.0 that will be forgiven
     * @return OrientedPoint3d, or OrientedPoint2d (in accordance with OP, P, L and S)
     * @throws DrawException when fraction less than 0.0 or more than 1.0.
     */
    default R getLocationFraction(final double fraction, final double tolerance) throws DrawException
    {
        if (fraction < -tolerance || fraction > 1.0 + tolerance)
        {
            throw new DrawException(
                    "getLocationFraction for line: fraction < 0.0 - tolerance or > 1.0 + tolerance; fraction = " + fraction);
        }
        double f = fraction < 0 ? 0.0 : fraction > 1.0 ? 1.0 : fraction;
        return getLocation(f * getLength());
    }

    /**
     * Get the location at a fraction of the line (or outside the line), with its direction.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return OrientedPoint3d, or OrientedPoint2d (in accordance with OP, P, L and S)
     */
    default R getLocationFractionExtended(final double fraction)
    {
        return getLocationExtended(fraction * getLength());
    }

    /**
     * Truncate this Line at the given length (less than the length of the line, and larger than zero) and return a new line.
     * @param position double; the position along the line where to truncate the line
     * @return L; a new Line that follows this line, but ends at the position where line.getLength() == lengthSI
     * @throws DrawException when position less than 0.0 or more than line length.
     */
    L truncate(double position) throws DrawException;

    /**
     * Binary search for a position on the line.
     * @param pos double; the position to look for
     * @return the index below the position; the position is between points[index] and points[index+1]
     * @throws DrawException when index could not be found
     */
    default int find(final double pos) throws DrawException
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
        throw new DrawException("Could not find position " + pos + " on line with length: " + getLength());
    }

}
