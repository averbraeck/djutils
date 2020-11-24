package org.djutils.draw.line;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.djutils.logger.CategoryLogger;

/**
 * Line3d.java. Implementation of Line for 3D space.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Line3d implements Line
{
    /** */
    private static final long serialVersionUID = 20200911L;

    /** The points of the line. */
    private final Point3d[] points;

    /** The cumulative length of the line at point 'i'. */
    private final double[] lengthIndexedLine;

    /** The cached length. */
    private final double length;

    /** The cached centroid for the Locatable interface. */
    private final Point3d centroid;

    /** Bounding rectangle of this Line3d. */
    private final Bounds2d boundingRectangle;
    // TODO Peter thinks that the Line3d need not store the bounding rectangle. Can be quickly derived from bounding box.

    /** Bounding box of this Line3d around the centroid. */
    private final Bounds3d bounds;

    /**
     * Construct a new Line3d and initialize its length indexed line, bounds, centroid and length.
     * @param points Point3d...; the array of points to construct this Line3d from.
     * @throws DrawException when the provided points do not constitute a valid line (too few points or identical adjacent
     *             points)
     */
    public Line3d(final Point3d... points) throws DrawException
    {
        if (points.length < 2)
        {
            throw new DrawException("Degenerate Line3d; has " + points.length + " point" + (points.length != 1 ? "s" : ""));
        }
        double minX = points[0].getX();
        double minY = points[0].getY();
        double minZ = points[0].getZ();
        ;
        double maxX = points[0].getX();
        double maxY = points[0].getY();
        double maxZ = points[0].getZ();
        this.lengthIndexedLine = new double[points.length];
        this.lengthIndexedLine[0] = 0.0;
        for (int i = 1; i < points.length; i++)
        {
            Point3d point = points[i];
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            minZ = Math.min(minZ, point.getZ());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
            maxZ = Math.max(maxZ, point.getZ());
            if (points[i - 1].getX() == point.getX() && points[i - 1].getY() == point.getY()
                    && points[i - 1].getZ() == point.getZ())
            {
                throw new DrawException("Degenerate Line3d; point " + (i - 1) + " has the same x, y and z as point " + i);
            }
            this.lengthIndexedLine[i] = this.lengthIndexedLine[i - 1] + points[i - 1].distance(point);
        }
        this.points = points; // XXX Absolutely no need to make a deep copy of this one?
        this.length = this.lengthIndexedLine[this.lengthIndexedLine.length - 1];
        this.centroid = new DirectedPoint3d((maxX + minX) / 2, (maxY + minY) / 2, (maxZ + minZ) / 2);
        double deltaX = maxX - minX;
        double deltaY = maxY - minY;
        double deltaZ = maxZ - minZ;
        this.bounds = new Bounds3d(-deltaX / 2.0, deltaX / 2, -deltaY / 2.0, deltaY / 2, -deltaZ / 2.0, deltaZ / 2);
        this.boundingRectangle = new Bounds2d(minX, maxX, minY, maxY);
    }

    /**
     * Construct a new Line3d from a List&lt;Point3d&gt;.
     * @param pointList List&lt;Point3d&gt;; the list of points to construct this Line3d from.
     * @throws DrawException when the provided points do not constitute a valid line (too few points or identical adjacent
     *             points)
     */
    public Line3d(final List<Point3d> pointList) throws DrawException
    {
        this(pointList.toArray(new Point3d[pointList.size()]));
    }

    /**
     * Construct a new OTSShape (closed shape) from a Path2D.
     * @param path Path2D; the Path2D to construct this Line3d from.
     * @throws DrawException when the provided points do not constitute a valid line (too few points or identical adjacent
     *             points)
     */
    public Line3d(final Path2D path) throws DrawException
    {
        this(path2DtoArray(path));
    }

    /**
     * Convert a path2D to a Point3d[] array to construct the line.
     * @param path Path2D; the path to convert
     * @return Point3d[]; an array of points based on MOVETO and LINETO elements of the Path2D
     */
    private static Point3d[] path2DtoArray(final Path2D path)
    {
        List<Point3d> result = new ArrayList<>();
        for (PathIterator pi = path.getPathIterator(null); !pi.isDone(); pi.next())
        {
            double[] p = new double[6];
            int segType = pi.currentSegment(p);
            if (segType == PathIterator.SEG_MOVETO || segType == PathIterator.SEG_LINETO)
            {
                result.add(new Point3d(p[0], p[1], 0.0));
            }
            else if (segType == PathIterator.SEG_CLOSE)
            {
                if (!result.get(0).equals(result.get(result.size() - 1)))
                {
                    result.add(new Point3d(result.get(0).getX(), result.get(0).getY(), 0.0));
                }
                break;
            }
        }
        return result.toArray(new Point3d[result.size() - 1]);
    }

    /**
     * Construct a new Line3d that is equal to this line except for segments that are shorter than the <cite>noiseLevel</cite>.
     * The result is guaranteed to start with the first point of this line and end with the last point of this line.
     * @param noiseLevel double; the minimum segment length that is <b>not</b> removed
     * @return Line3d; the filtered line
     */
    public final Line3d noiseFilteredLine(final double noiseLevel)
    {
        if (this.size() <= 2)
        {
            return this; // Except for some cached fields; an Line3d is immutable; so safe to return
        }
        Point3d prevPoint = null;
        List<Point3d> list = null;
        for (int index = 0; index < this.size(); index++)
        {
            Point3d currentPoint = this.points[index];
            if (null != prevPoint && prevPoint.distance(currentPoint) < noiseLevel)
            {
                if (null == list)
                {
                    // Found something to filter; copy this up to (and including) prevPoint
                    list = new ArrayList<>();
                    for (int i = 0; i < index; i++)
                    {
                        list.add(this.points[i]);
                    }
                }
                if (index == this.size() - 1)
                {
                    if (list.size() > 1)
                    {
                        // Replace the last point of the result by the last point of this Line3d
                        list.set(list.size() - 1, currentPoint);
                    }
                    else
                    {
                        // Append the last point of this even though it is close to the first point than the noise value to
                        // comply with the requirement that first and last point of this are ALWAYS included in the result.
                        list.add(currentPoint);
                    }
                }
                continue; // Do not replace prevPoint by currentPoint
            }
            else if (null != list)
            {
                list.add(currentPoint);
            }
            prevPoint = currentPoint;
        }
        if (null == list)
        {
            return this;
        }
        if (list.size() == 2 && list.get(0).equals(list.get(1)))
        {
            // Find something to insert along the way
            for (int index = 1; index < this.size() - 1; index++)
            {
                if (!this.points[index].equals(list.get(0)))
                {
                    list.add(1, this.points[index]);
                    break;
                }
            }
        }
        try
        {
            return new Line3d(list);
        }
        catch (DrawException exception)
        {
            CategoryLogger.always().error(exception);
            throw new Error(exception);
        }
    }

    /**
     * Concatenate several Line3d instances.
     * @param lines Line3d...; Line3d... one or more Line3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match
     *            the first of the second, etc.
     * @return Line3d
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static Line3d concatenate(final Line3d... lines) throws DrawException
    {
        return concatenate(0.0, lines);
    }

    /**
     * Concatenate two Line3d instances. This method is separate for efficiency reasons.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param line1 Line3d; first line
     * @param line2 Line3d; second line
     * @return Line3d; the concatenation of the two lines
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static Line3d concatenate(final double tolerance, final Line3d line1, final Line3d line2) throws DrawException
    {
        if (line1.getLast().distance(line2.getFirst()) > tolerance)
        {
            throw new DrawException("Lines are not connected: " + line1.getLast() + " to " + line2.getFirst() + " distance is "
                    + line1.getLast().distance(line2.getFirst()) + " > " + tolerance);
        }
        int size = line1.size() + line2.size() - 1;
        Point3d[] points = new Point3d[size];
        int nextIndex = 0;
        for (int j = 0; j < line1.size(); j++)
        {
            points[nextIndex++] = line1.get(j);
        }
        for (int j = 1; j < line2.size(); j++)
        {
            points[nextIndex++] = line2.get(j);
        }
        return new Line3d(points);
    }

    /**
     * Concatenate several Line3d instances.
     * @param tolerance double; the tolerance between the end point of a line and the first point of the next line
     * @param lines Line3d...; Line3d... one or more Line3d. The last point of the first &lt;strong&gt;must&lt;/strong&gt; match
     *            the first of the second, etc.
     * @return Line3d; the concatenation of the lines
     * @throws DrawException if zero lines are given, or when there is a gap between consecutive lines
     */
    public static Line3d concatenate(final double tolerance, final Line3d... lines) throws DrawException
    {
        if (0 == lines.length)
        {
            throw new DrawException("Empty argument list");
        }
        else if (1 == lines.length)
        {
            return lines[0];
        }
        int size = lines[0].size();
        for (int i = 1; i < lines.length; i++)
        {
            if (lines[i - 1].getLast().distance(lines[i].getFirst()) > tolerance)
            {
                throw new DrawException("Lines are not connected: " + lines[i - 1].getLast() + " to " + lines[i].getFirst()
                        + " distance is " + lines[i - 1].getLast().distance(lines[i].getFirst()) + " > " + tolerance);
            }
            size += lines[i].size() - 1;
        }
        Point3d[] points = new Point3d[size];
        int nextIndex = 0;
        for (int i = 0; i < lines.length; i++)
        {
            Line3d line = lines[i];
            for (int j = 0 == i ? 0 : 1; j < line.size(); j++)
            {
                points[nextIndex++] = line.get(j);
            }
        }
        return new Line3d(points);
    }

    /**
     * Construct a new Line3d with all points of this Line3d in reverse order.
     * @return Line3d; the new Line3d
     */
    public final Line3d reverse()
    {
        Point3d[] resultPoints = new Point3d[size()];
        int nextIndex = size();
        for (Point3d p : getPointArray())
        {
            resultPoints[--nextIndex] = p;
        }
        try
        {
            return new Line3d(resultPoints);
        }
        catch (DrawException exception)
        {
            // Cannot happen
            throw new RuntimeException(exception);
        }
    }

    /**
     * Construct a new Line3d covering the indicated fraction of this Line3d.
     * @param start double; starting point, valid range [0..<cite>end</cite>)
     * @param end double; ending point, valid range (<cite>start</cite>..1]
     * @return Line3d; the new Line3d
     * @throws DrawException when start &gt;= end, or start &lt; 0, or end &gt; 1
     */
    public final Line3d extractFractional(final double start, final double end) throws DrawException
    {
        if (start < 0 || start >= end || end > 1)
        {
            throw new DrawException("Bad interval (start=" + start + ", end=" + end + ", this is " + this.toString() + ")");
        }
        return extract(start * this.length, end * this.length);
    }

    /**
     * Create a new Line3d that covers a sub-section of this Line3d.
     * @param start double; length along this Line3d where the sub-section starts, valid range [0..<cite>end</cite>)
     * @param end double; length along this Line3d where the sub-section ends, valid range
     *            (<cite>start</cite>..<cite>length</cite> (length is the length of this Line3d)
     * @return Line3d; the selected sub-section
     * @throws DrawException when start &gt;= end, or start &lt; 0, or end &gt; length
     */
    public final Line3d extract(final double start, final double end) throws DrawException
    {
        if (Double.isNaN(start) || Double.isNaN(end) || start < 0 || start >= end || end > getLength())
        {
            throw new DrawException(
                    "Bad interval (" + start + ".." + end + "; length of this Line3d is " + this.getLength() + ")");
        }
        double cumulativeLength = 0;
        double nextCumulativeLength = 0;
        double segmentLength = 0;
        int index = 0;
        List<Point3d> pointList = new ArrayList<>();
        while (start > cumulativeLength)
        {
            Point3d fromPoint = this.points[index];
            index++;
            Point3d toPoint = this.points[index];
            segmentLength = fromPoint.distance(toPoint);
            cumulativeLength = nextCumulativeLength;
            nextCumulativeLength = cumulativeLength + segmentLength;
            if (nextCumulativeLength >= start)
            {
                break;
            }
        }
        if (start == nextCumulativeLength)
        {
            pointList.add(this.points[index]);
        }
        else
        {
            pointList.add(this.points[index - 1].interpolate(this.points[index], (start - cumulativeLength) / segmentLength));
            if (end > nextCumulativeLength)
            {
                pointList.add(this.points[index]);
            }
        }
        while (end > nextCumulativeLength)
        {
            Point3d fromPoint = this.points[index];
            index++;
            if (index >= this.points.length)
            {
                break; // rounding error
            }
            Point3d toPoint = this.points[index];
            segmentLength = fromPoint.distance(toPoint);
            cumulativeLength = nextCumulativeLength;
            nextCumulativeLength = cumulativeLength + segmentLength;
            if (nextCumulativeLength >= end)
            {
                break;
            }
            pointList.add(toPoint);
        }
        if (end == nextCumulativeLength)
        {
            pointList.add(this.points[index]);
        }
        else
        {
            Point3d point = this.points[index - 1].interpolate(this.points[index], (end - cumulativeLength) / segmentLength);
            // can be the same due to rounding
            if (!point.equals(pointList.get(pointList.size() - 1)))
            {
                pointList.add(point);
            }
        }
        try
        {
            return new Line3d(pointList);
        }
        catch (DrawException exception)
        {
            CategoryLogger.always().error(exception, "interval " + start + ".." + end + " too short");
            throw new DrawException("interval " + start + ".." + end + "too short");
        }
    }

    /**
     * Create a new Line3d, filtering out repeating successive points.
     * @param points Point3d...; the coordinates of the line as Point3d
     * @return the line
     * @throws DrawException when number of points &lt; 2
     */
    public static Line3d createAndCleanLine3d(final Point3d... points) throws DrawException
    {
        if (points.length < 2)
        {
            throw new DrawException("Degenerate Line3d; has " + points.length + " point" + (points.length != 1 ? "s" : ""));
        }
        return createAndCleanLine3d(new ArrayList<>(Arrays.asList(points)));
    }

    /**
     * Create an Line3d, while filtering out repeating successive points.
     * @param pointList List&lt;Point3d&gt;; list of the coordinates of the line as Point3d; any duplicate points in this list
     *            are removed (this method may modify the provided list)
     * @return Line3d; the line
     * @throws DrawException when number of non-equal points &lt; 2
     */
    public static Line3d createAndCleanLine3d(final List<Point3d> pointList) throws DrawException
    {
        // TODO avoid modifying the input list.
        // clean successive equal points
        int i = 1;
        while (i < pointList.size())
        {
            if (pointList.get(i - 1).equals(pointList.get(i)))
            {
                pointList.remove(i);
            }
            else
            {
                i++;
            }
        }
        return new Line3d(pointList);
    }

    /**
     * Return the number of points in this Line3d.
     * @return the number of points on the line
     */
    public final int size()
    {
        return this.points.length;
    }

    /**
     * Return the first Point of this Line3d.
     * @return the first point on the line
     */
    public final Point3d getFirst()
    {
        return this.points[0];
    }

    /**
     * Return the last Point of this Line3d.
     * @return the last point on the line
     */
    public final Point3d getLast()
    {
        return this.points[size() - 1];
    }

    /**
     * Return one Point of this Line3d.
     * @param i int; the index of the point to retrieve
     * @return OTSPoint3d; the i<sup>th</sup> point of the line
     * @throws DrawException when i &lt; 0 or i &gt; the number of points
     */
    public final Point3d get(final int i) throws DrawException
    {
        if (i < 0 || i > size() - 1)
        {
            throw new DrawException("Line3d.get(i=" + i + "); i<0 or i>=size(), which is " + size());
        }
        return this.points[i];
    }

    /**
     * Get the location at a position on the line, with its direction. Position can be below 0 or more than the line length. In
     * that case, the position will be extrapolated in the direction of the line at its start or end.
     * @param position double; the position on the line for which to calculate the point on, before, or after the line
     * @return a directed point
     */
    public final DirectedPoint3d getLocationExtended(final double position)
    {
        if (position >= 0.0 && position <= getLength())
        {
            try
            {
                return getLocation(position);
            }
            catch (DrawException exception)
            {
                // cannot happen
            }
        }

        // position before start point -- extrapolate using direction from first point to second point of this Line3d
        if (position < 0.0)
        {
            double len = position;
            double fraction = len / (this.lengthIndexedLine[1] - this.lengthIndexedLine[0]);
            Point3d p1 = this.points[0];
            Point3d p2 = this.points[1];
            return new DirectedPoint3d(p1.getX() + fraction * (p2.getX() - p1.getX()),
                    p1.getY() + fraction * (p2.getY() - p1.getY()), p1.getZ() + fraction * (p2.getZ() - p1.getZ()), 0.0, 0.0,
                    Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        }

        // position beyond end point -- extrapolate using the direction from the before last point to the last point of this
        // Line3d
        int n1 = this.lengthIndexedLine.length - 1;
        int n2 = this.lengthIndexedLine.length - 2;
        double len = position - getLength();
        double fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        while (Double.isInfinite(fraction))
        {
            if (--n2 < 0)
            {
                CategoryLogger.always().error("lengthIndexedLine of {} is invalid", this);
                Point3d p = this.points[n1];
                return new DirectedPoint3d(p.getX(), p.getY(), p.getZ(), 0.0, 0.0, 0.0); // Bogus direction
            }
            fraction = len / (this.lengthIndexedLine[n1] - this.lengthIndexedLine[n2]);
        }
        Point3d p1 = this.points[n2];
        Point3d p2 = this.points[n1];
        return new DirectedPoint3d(p2.getX() + fraction * (p2.getX() - p1.getX()),
                p2.getY() + fraction * (p2.getY() - p1.getY()), p2.getZ() + fraction * (p2.getZ() - p1.getZ()), 0.0, 0.0,
                Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
    }

    /**
     * Get the location at a fraction of the line, with its direction. Fraction should be between 0.0 and 1.0.
     * @param fraction double; the fraction for which to calculate the point on the line
     * @return DirectedPoint3d
     * @throws DrawException when fraction less than 0.0 or more than 1.0.
     */
    public final DirectedPoint3d getLocationFraction(final double fraction) throws DrawException
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
     * @return DirectedPoint3d
     * @throws DrawException when fraction less than 0.0 or more than 1.0.
     */
    public final DirectedPoint3d getLocationFraction(final double fraction, final double tolerance) throws DrawException
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
     * @return DirectedPoint3d
     */
    public final DirectedPoint3d getLocationFractionExtended(final double fraction)
    {
        return getLocationExtended(fraction * getLength());
    }

    /**
     * Return the length of this OTSLine3D as a double value. (If the coordinates of the points constituting this line are
     * expressed in meters, the returned length will be in meters.)
     * @return the length of the line in SI units
     */
    public final double getLength()
    {
        return this.length;
    }

    /**
     * Binary search for a position on the line.
     * @param pos double; the position to look for
     * @return the index below the position; the position is between points[index] and points[index+1]
     * @throws DrawException when index could not be found
     */
    private int find(final double pos) throws DrawException
    {
        if (pos == 0)
        {
            return 0;
        }

        int lo = 0;
        int hi = this.lengthIndexedLine.length - 1;
        while (lo <= hi)
        {
            if (hi == lo)
            {
                return lo;
            }
            int mid = lo + (hi - lo) / 2;
            if (pos < this.lengthIndexedLine[mid])
            {
                hi = mid - 1;
            }
            else if (pos > this.lengthIndexedLine[mid + 1])
            {
                lo = mid + 1;
            }
            else
            {
                return mid;
            }
        }
        throw new DrawException(
                "Could not find position " + pos + " on line with length indexes: " + Arrays.toString(this.lengthIndexedLine));
    }

    /**
     * Get the location at a position on the line, with its direction. Position should be between 0.0 and line length.
     * @param position double; the position on the line for which to calculate the point on the line
     * @return a directed point
     * @throws DrawException when position less than 0.0 or more than line length.
     */
    public final DirectedPoint3d getLocation(final double position) throws DrawException
    {
        if (position < 0.0 || position > getLength())
        {
            throw new DrawException("getLocationSI for line: position < 0.0 or > line length. Position = " + position
                    + " m. Length = " + getLength() + " m.");
        }

        // handle special cases: position == 0.0, or position == length
        if (position == 0.0)
        {
            Point3d p1 = this.points[0];
            Point3d p2 = this.points[1];
            return new DirectedPoint3d(p1.getX(), p1.getY(), p1.getZ(), 0.0, 0.0,
                    Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        }
        if (position == getLength())
        {
            Point3d p1 = this.points[this.points.length - 2];
            Point3d p2 = this.points[this.points.length - 1];
            return new DirectedPoint3d(p2.getX(), p2.getY(), p2.getZ(), 0.0, 0.0,
                    Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        }

        // find the index of the line segment, use binary search
        int index = find(position);
        double remainder = position - this.lengthIndexedLine[index];
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        Point3d p1 = this.points[index];
        Point3d p2 = this.points[index + 1];
        return new DirectedPoint3d(p1.getX() + fraction * (p2.getX() - p1.getX()),
                p1.getY() + fraction * (p2.getY() - p1.getY()), p1.getZ() + fraction * (p2.getZ() - p1.getZ()), 0.0, 0.0,
                Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
    }

    /**
     * Truncate a line at the given length (less than the length of the line, and larger than zero) and return a new line.
     * @param lengthSI double; the location where to truncate the line
     * @return a new Line3d that follows this line, but ends at the position where line.getLength() == lengthSI
     * @throws DrawException when position less than 0.0 or more than line length.
     */
    public final Line3d truncate(final double lengthSI) throws DrawException
    {
        if (lengthSI <= 0.0 || lengthSI > getLength())
        {
            throw new DrawException("truncate for line: position <= 0.0 or > line length. Position = " + lengthSI
                    + " m. Length = " + getLength() + " m.");
        }

        // handle special case: position == length
        if (lengthSI == getLength())
        {
            return new Line3d(getPointArray());
        }

        // find the index of the line segment
        int index = find(lengthSI);
        double remainder = lengthSI - this.lengthIndexedLine[index];
        double fraction = remainder / (this.lengthIndexedLine[index + 1] - this.lengthIndexedLine[index]);
        Point3d p1 = this.points[index];
        Point3d lastPoint;
        if (0.0 == fraction)
        {
            index--;
            lastPoint = p1;
        }
        else
        {
            Point3d p2 = this.points[index + 1];
            lastPoint = new Point3d(p1.getX() + fraction * (p2.getX() - p1.getX()),
                    p1.getY() + fraction * (p2.getY() - p1.getY()), p1.getZ() + fraction * (p2.getZ() - p1.getZ()));

        }
        Point3d[] coords = new Point3d[index + 2];
        for (int i = 0; i <= index; i++)
        {
            coords[i] = this.points[i];
        }
        coords[index + 1] = lastPoint;
        return new Line3d(coords);
    }

    /**
     * Returns the fractional position along this line of the orthogonal projection of point (x, y) on this line. If the point
     * is not orthogonal to the closest line segment, the nearest point is selected.
     * @param x double; x-coordinate of point to project
     * @param y double; y-coordinate of point to project
     * @return fractional position along this line of the orthogonal projection on this line of a point
     */
    public final double projectOrthogonal(final double x, final double y)
    {
        // prepare
        double minDistance = Double.POSITIVE_INFINITY;
        double minSegmentFraction = 0;
        int minSegment = -1;

        // code based on Line2D.ptSegDistSq(...)
        for (int i = 0; i < size() - 1; i++)
        {
            double dx = this.points[i + 1].getX() - this.points[i].getX();
            double dy = this.points[i + 1].getY() - this.points[i].getY();
            // vector relative to (x(i), y(i))
            double px = x - this.points[i].getX();
            double py = y - this.points[i].getY();
            // dot product
            double dot1 = px * dx + py * dy;
            double f;
            double distance;
            if (dot1 > 0)
            {
                // vector relative to (x(i+1), y(i+1))
                px = dx - px;
                py = dy - py;
                // dot product
                double dot2 = px * dx + py * dy;
                if (dot2 > 0)
                {
                    // projection on line segment
                    double len2 = dx * dx + dy * dy;
                    double proj = dot2 * dot2 / len2;
                    f = dot1 / len2;
                    distance = px * px + py * py - proj;
                }
                else
                {
                    // dot<=0 projection 'after' line segment
                    f = 1;
                    distance = px * px + py * py;
                }
            }
            else
            {
                // dot<=0 projection 'before' line segment
                f = 0;
                distance = px * px + py * py;
            }
            // check if closer than previous
            if (distance < minDistance)
            {
                minDistance = distance;
                minSegmentFraction = f;
                minSegment = i;
            }
        }

        // return
        double segLen = this.lengthIndexedLine[minSegment + 1] - this.lengthIndexedLine[minSegment];
        return (this.lengthIndexedLine[minSegment] + segLen * minSegmentFraction) / getLength();
    }

    /**
     * Retrieve the centroid of this Line3d.
     * @return Point3d; the centroid of this Line3d
     */
    public final Point3d getCentroid()
    {
        return this.centroid;
    }

    /**
     * Get the bounding rectangle of this Line3d.
     * @return Bounds2d; the bounding rectangle of this Line3d
     */
    public final Bounds2d getBounds2d()
    {
        return this.boundingRectangle;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d getLocation() throws RemoteException
    {
        return new DirectedPoint3d(this.centroid.getX(), this.centroid.getY(), this.centroid.getZ());
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds() throws RemoteException
    {
        return this.bounds;
    }

    /** {@inheritDoc} */
    @Override
    public Point3d[] getPointArray()
    {
        return this.points;
        // XXX This enables the caller to modify our immutable Point array. 
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return Arrays.toString(this.points);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.points);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "checkstyle:designforextension", "checkstyle:needbraces" })
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Line3d other = (Line3d) obj;
        if (!Arrays.equals(this.points, other.points))
            return false;
        return true;
    }

    /**
     * Convert the 2D projection of this Line3d to something that MS-Excel can plot.
     * @return excel XY plottable output
     */
    public final String toExcel()
    {
        StringBuffer s = new StringBuffer();
        for (Point3d p : this.points)
        {
            s.append(p.getX() + "\t" + p.getY() + "\n");
        }
        return s.toString();
    }

}
