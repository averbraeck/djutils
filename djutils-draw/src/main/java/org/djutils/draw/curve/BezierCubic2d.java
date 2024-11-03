package org.djutils.draw.curve;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.djutils.draw.Transform2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.Ray2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;

/**
 * Continuous definition of a cubic B&eacute;zier curves in 2d. This extends from the more general {@code Bezier} as certain
 * methods are applied to calculate e.g. the roots, that are specific to cubic B&eacute;zier curves. With such information this
 * class can also specify information to be a {@code Curve}.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://pomax.github.io/bezierinfo/">B&eacute;zier info</a>
 */
public class BezierCubic2d extends Bezier2d implements Curve2d, OffsetFlattable2d
{

    /** Angle below which segments are seen as straight. */
    private static final double STRAIGHT = Math.PI / 36000; // 1/100th of a degree

    /** Start point with direction. */
    private final DirectedPoint2d startPoint;

    /** End point with direction. */
    private final DirectedPoint2d endPoint;

    /** Length. */
    private final double length;

    /**
     * Create a cubic B&eacute;zier curve.
     * @param start start point.
     * @param control1 first intermediate shape point.
     * @param control2 second intermediate shape point.
     * @param end end point.
     */
    public BezierCubic2d(final Point2d start, final Point2d control1, final Point2d control2, final Point2d end)
    {
        super(start, control1, control2, end);
        this.startPoint = new DirectedPoint2d(start, control1);
        this.endPoint = new DirectedPoint2d(end, control2.directionTo(end));
        this.length = length();
    }

    /**
     * Create a cubic B&eacute;zier curve.
     * @param points Point2d[]; array containing four Point2d objects
     */
    public BezierCubic2d(final Point2d[] points)
    {
        this(checkArray(points)[0], points[1], points[2], points[3]);

    }

    /**
     * Verify that a Point2d[] contains exactly 4 elements.
     * @param points Point2d[]; the array to check
     * @return Point2d[]; the provided array
     */
    private static Point2d[] checkArray(final Point2d[] points)
    {
        Throw.when(points.length != 4, IllegalArgumentException.class, "points must contain exactly 4 Point2d objects");
        return points;
    }

    /**
     * Construct a ContinuousBezierCubic from start to end with two generated control points at half the distance between start
     * and end. TODO Explain better.
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     */
    public BezierCubic2d(final Ray2d start, final Ray2d end)
    {
        this(start, end, 1.0);
    }

    /**
     * Construct a ContinuousBezierCubic from start to end with two generated control points at half the distance between start
     * and end.
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @param shape shape factor; 1 = control points at half the distance between start and end, &gt; 1 results in a pointier
     *            shape, &lt; 1 results in a flatter shape, value should be above 0 and finite
     */
    public BezierCubic2d(final Ray2d start, final Ray2d end, final double shape)
    {
        this(start, end, shape, false);
    }

    /**
     * Construct a ContinuousBezierCubic from start to end with two generated control points at half the distance between start
     * and end and .
     * @param start Ray2d; the start point and start direction of the B&eacute;zier curve
     * @param end Ray2d; the end point and end direction of the B&eacute;zier curve
     * @param shape double; the shape; higher values put the generated control points further away from end and result in a
     *            pointier B&eacute;zier curve
     * @param weighted boolean;
     */
    public BezierCubic2d(final Ray2d start, final Ray2d end, final double shape, final boolean weighted)
    {
        this(createControlPoints(start, end, shape, weighted));
    }

    /** Unit vector for transformations in createControlPoints. */
    private static final Point2d UNIT_VECTOR2D = new Point2d(1, 0);

    /**
     * Create control points for a cubic B&eacute;zier curve defined by two Rays.
     * @param start Ray2d; the start point (and direction)
     * @param end Ray2d; the end point (and direction)
     * @param shape double; the shape; higher values put the generated control points further away from end and result in a
     *            pointier B&eacute;zier curve
     * @param weighted boolean;
     * @return Point2d[]; an array of four Point2d elements: start, the first control point, the second control point, end.
     */
    private static Point2d[] createControlPoints(final Ray2d start, final Ray2d end, final double shape, final boolean weighted)
    {
        Throw.whenNull(start, "start");
        Throw.whenNull(end, "end");
        Throw.when(start.distanceSquared(end) == 0, IllegalArgumentException.class,
                "Cannot create control points if start and end points coincide");
        Throw.when(Double.isNaN(shape) || shape <= 0 || Double.isInfinite(shape), IllegalArgumentException.class,
                "shape must be a finite, positive value");

        Point2d control1;
        Point2d control2;
        if (weighted)
        {
            // each control point is 'w' * the distance between the end-points away from the respective end point
            // 'w' is a weight given by the distance from the end point to the extended line of the other end point
            double distance = shape * start.distance(end);
            double dStart = start.distance(end.projectOrthogonalExtended(start));
            double dEnd = end.distance(start.projectOrthogonalExtended(end));
            double wStart = dStart / (dStart + dEnd);
            double wEnd = dEnd / (dStart + dEnd);
            control1 = new Transform2d().translate(start).rotation(start.dirZ).scale(distance * wStart, distance * wStart)
                    .transform(UNIT_VECTOR2D);
            // - (minus) as the angle is where the line leaves, i.e. from shape point to end
            control2 = new Transform2d().translate(end).rotation(end.dirZ + Math.PI).scale(distance * wEnd, distance * wEnd)
                    .transform(UNIT_VECTOR2D);
        }
        else
        {
            // each control point is half the distance between the end-points away from the respective end point
            double distance = shape * start.distance(end) / 2.0;
            control1 = start.getLocation(distance);
            // new Transform2d().translate(start).rotation(start.phi).scale(distance, distance).transform(UNIT_VECTOR2D);
            control2 = end.getLocationExtended(-distance);
            // new Transform2d().translate(end).rotation(end.phi + Math.PI).scale(distance, distance).transform(UNIT_VECTOR2D);
        }
        return new Point2d[] {start, control1, control2, end};
    }

    @Override
    public Point2d getPoint(final double t)
    {
        return new Point2d(B3(t, this.x), B3(t, this.y));
    }

    /**
     * Calculate the cubic B&eacute;zier point with B(t) = (1 - t)<sup>3</sup>P<sub>0</sub> + 3t(1 - t)<sup>2</sup>
     * P<sub>1</sub> + 3t<sup>2</sup> (1 - t) P<sub>2</sub> + t<sup>3</sup> P<sub>3</sub>.
     * @param t double; the fraction
     * @param p double[]; the four control values of this dimension of this curve
     * @return the cubic B&eacute;zier value B(t)
     */
    @SuppressWarnings("checkstyle:methodname")
    private static double B3(final double t, final double[] p)
    {
        double t2 = t * t;
        double t3 = t2 * t;
        double m = (1.0 - t);
        double m2 = m * m;
        double m3 = m2 * m;
        return m3 * p[0] + 3.0 * t * m2 * p[1] + 3.0 * t2 * m * p[2] + t3 * p[3];
    }

    @Override
    public DirectedPoint2d getStartPoint()
    {
        return this.startPoint;
    }

    @Override
    public DirectedPoint2d getEndPoint()
    {
        return this.endPoint;
    }

    @Override
    public double getStartCurvature()
    {
        return curvature(0.0);
    }

    @Override
    public double getEndCurvature()
    {
        return curvature(1.0);
    }

    /**
     * Returns the root t values, where each of the sub-components derivative for x and y are 0.0.
     * @return set of root t values, sorted and in the range (0, 1).
     */
    private SortedSet<Double> getRoots()
    {
        // Uses quadratic B&eacute;zier formulation
        double ax = 3.0 * (-getX(0) + 3.0 * getX(1) - 3.0 * getX(2) + getX(3));
        double ay = 3.0 * (-getY(0) + 3.0 * getY(1) - 3.0 * getY(2) + getY(3));
        double bx = 6.0 * (getX(0) - 2.0 * getX(1) + getX(2));
        double by = 6.0 * (getY(0) - 2.0 * getY(1) + getY(2));
        double cx = 3.0 * (getX(1) - getX(0));
        double cy = 3.0 * (getY(1) - getY(0));

        // ABC formula
        TreeSet<Double> roots = new TreeSet<>();
        double discriminant = bx * bx - 4.0 * ax * cx;
        if (discriminant > 0)
        {
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double ax2 = 2.0 * ax;
            roots.add((-bx + sqrtDiscriminant) / ax2);
            roots.add((-bx - sqrtDiscriminant) / ax2);
        }
        discriminant = by * by - 4.0 * ay * cy;
        if (discriminant > 0)
        {
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double ay2 = 2.0 * ay;
            roots.add((-by + sqrtDiscriminant) / ay2);
            roots.add((-by - sqrtDiscriminant) / ay2);
        }

        // System.out.println("Roots in " + this);
        // for (Double root : roots)
        // {
        // System.out.println("\t" + root + " " + getPoint(root));
        // }

        // Only roots in range (0.0 ... 1.0) are valid and useful
        return roots.subSet(0.0, false, 1.0, false);
    }

    /**
     * Returns the inflection t values, where curvature changes sign.
     * @return set of inflection t values, sorted and in the range (0, 1)
     */
    private SortedSet<Double> getInflections()
    {
        // Align: translate so first point is (0, 0), rotate so last point is on x=axis (y = 0)
        Point2d[] aligned = new Point2d[4];
        double ang = -Math.atan2(getY(3) - getY(0), getX(3) - getX(0));
        double cosAng = Math.cos(ang);
        double sinAng = Math.sin(ang);
        for (int i = 0; i < 4; i++)
        {
            aligned[i] = new Point2d(cosAng * (getX(i) - getX(0)) - sinAng * (getY(i) - getY(0)),
                    sinAng * (getX(i) - getX(0)) + cosAng * (getY(i) - getY(0)));
        }

        // Inflection as curvature = 0, using:
        // curvature = x'(t)*y''(t) + y'(t)*x''(t) = 0
        // (this is highly simplified due to the alignment, removing many terms)
        double a = aligned[2].x * aligned[1].y;
        double b = aligned[3].x * aligned[1].y;
        double c = aligned[1].x * aligned[2].y;
        double d = aligned[3].x * aligned[2].y;

        double x = -3.0 * a + 2.0 * b + 3.0 * c - d;
        double y = 3.0 * a - b - 3.0 * c;
        double z = c - a;

        // ABC formula (on x, y, z)
        TreeSet<Double> inflections = new TreeSet<>();
        if (Math.abs(x) < 1.0e-6)
        {
            if (Math.abs(y) >= 1.0e-12)
            {
                inflections.add(-z / y);
            }
        }
        else
        {
            double det = y * y - 4.0 * x * z;
            double sq = Math.sqrt(det);
            double d2 = 2 * x;
            if (det >= 0.0 && Math.abs(d2) >= 1e-12)
            {
                inflections.add(-(y + sq) / d2);
                inflections.add((sq - y) / d2);
            }
        }

        // Only inflections in range (0.0 ... 1.0) are valid and useful
        return inflections.subSet(0.0, false, 1.0, false);
    }

    /**
     * Returns the offset t values.
     * @param fractions length fractions at which offsets are defined.
     * @return set of offset t values, sorted and in the range (0, 1), exclusive.
     */
    private SortedSet<Double> getOffsetT(final Set<Double> fractions)
    {
        TreeSet<Double> crossSections = new TreeSet<>();
        double lenTot = length();
        for (Double f : fractions)
        {
            if (f > 0.0 && f < 1.0)
            {
                crossSections.add(getT(f * lenTot));
            }
        }
        return crossSections;
    }

    /**
     * Returns the t value at the provided length along the B&eacute;zier curve. This method uses an iterative approach with a
     * precision of 1e-6.
     * @param position double; position along the B&eacute;zier curve.
     * @return t value at the provided length along the B&eacute;zier curve.
     */
    @Override
    public double getT(final double position)
    {
        if (0.0 == position)
        {
            return 0.0;
        }
        if (this.length == position)
        {
            return 1.0;
        }
        // start at 0.0 and 1.0, cut in half, see which half to use next
        double t0 = 0.0;
        double t2 = 1.0;
        double t1 = 0.5;
        while (t2 > t0 + 1.0e-6)
        {
            t1 = (t2 + t0) / 2.0;
            SplitBeziers parts = split(t1);
            double len1 = parts.first.length();
            if (len1 < position)
            {
                t0 = t1;
            }
            else
            {
                t2 = t1;
            }
        }
        return t1;
    }

    /**
     * Wrapper for two ContinuousBezierCubic2d.
     * @param first ContinuousBezierCubic2d; the part before the split point
     * @param remainder ContinuousBezierCubic2d; the part after the split point
     */
    private record SplitBeziers(BezierCubic2d first, BezierCubic2d remainder)
    {
    };

    /**
     * Splits the B&eacute;zier in two B&eacute;zier curves of the same order.
     * @param t t value along the B&eacute;zier curve to apply the split.
     * @return SplitBeziers; the B&eacute;zier curve before t, and the B&eacute;zier curve after t.
     */
    public SplitBeziers split(final double t)
    {
        Throw.when(t < 0.0 || t > 1.0, IllegalArgumentException.class, "t value should be in the range [0.0 ... 1.0].");
        // System.out.println("Splitting at " + t + ": " + this);
        List<Point2d> p1 = new ArrayList<>();
        List<Point2d> p2 = new ArrayList<>();
        List<Point2d> all = new ArrayList<>();
        for (int i = 0; i < size(); i++)
        {
            all.add(new Point2d(getX(i), getY(i)));
        }
        split0(t, all, p1, p2);
        SplitBeziers result = new SplitBeziers(new BezierCubic2d(p1.get(0), p1.get(1), p1.get(2), p1.get(3)),
                new BezierCubic2d(p2.get(3), p2.get(2), p2.get(1), p2.get(0)));
        // System.out.println("\t1 " + result.first + "\n\t2 " + result.remainder);
        // if (Math.abs(AngleUtil.normalizeAroundZero(result.first.endPoint.dirZ - result.remainder.startPoint.dirZ)) > 0.1)
        // {
        // System.out.println("DIRECTION MISMATCH");
        // }
        return result;
    }

    /**
     * Performs the iterative algorithm of Casteljau to derive the split B&eacute;zier curves.
     * @param t t value along the B&eacute;zier to apply the split.
     * @param p shape points of B&eacute;zier still to split.
     * @param p1 shape points of first part, accumulated in the recursion.
     * @param p2 shape points of second part, accumulated in the recursion.
     */
    private void split0(final double t, final List<Point2d> p, final List<Point2d> p1, final List<Point2d> p2)
    {
        if (p.size() == 1)
        {
            p1.add(p.get(0));
            p2.add(p.get(0));
        }
        else
        {
            List<Point2d> pNew = new ArrayList<>();
            for (int i = 0; i < p.size() - 1; i++)
            {
                if (i == 0)
                {
                    p1.add(p.get(i));
                }
                if (i == p.size() - 2)
                {
                    p2.add(p.get(i + 1));
                }
                double t1 = 1.0 - t;
                pNew.add(new Point2d(t1 * p.get(i).x + t * p.get(i + 1).x, t1 * p.get(i).y + t * p.get(i + 1).y));
            }
            split0(t, pNew, p1, p2);
        }
    }

    /** The derivative B&eacute;zier used to computer the direction at some t value. */
    private Bezier2d derivative = null;

    @Override
    public PolyLine2d toPolyLine(final Flattener2d flattener)
    {
        Throw.whenNull(flattener, "Flattener may not be null.");
        if (null == this.derivative)
        {
            this.derivative = derivative();
        }
        return flattener.flatten(this);
    }

    /**
     * A B&eacute;zier curve does not have a trivial offset. Hence, we split the B&eacute;zier along points of 3 types. 1)
     * roots, where the derivative of either the x-component or y-component is 0, such that we obtain C-shaped scalable
     * segments, 2) inflections, where the curvature changes sign and the offset and offset angle need to flip sign, and 3)
     * offset fractions so that the intended offset segments can be adhered to. Note that C-shaped segments can be scaled
     * similar to a circle arc, whereas S-shaped segments have no trivial scaling and are thus split.
     */
    private NavigableMap<Double, BezierCubic2d> segments;

    /** The FractionalLengthData for which segments were created. */
    private PieceWiseLinearOffset2d fldForSegments = null;

    /**
     * Check if the current segment map matches the provided FractionalLengthData. If not; rebuild the segments to match.
     * @param fld FractionalLengthData;
     */
    private void updateSegments(final PieceWiseLinearOffset2d fld)
    {
        Throw.whenNull(fld, "Offsets may not be null.");
        if (fld.equals(this.fldForSegments))
        {
            return;
        }
        // System.out.println("Updating segments for " + this);
        // Detect straight line
        double ang1 = Math.atan2(getY(1) - getY(0), getX(1) - getX(0));
        double ang2 = Math.atan2(getY(3) - getY(1), getX(3) - getX(1));
        double ang3 = Math.atan2(getY(3) - getY(2), getX(3) - getX(2));
        boolean straight =
                Math.abs(ang1 - ang2) < STRAIGHT && Math.abs(ang2 - ang3) < STRAIGHT && Math.abs(ang3 - ang1) < STRAIGHT;

        this.segments = new TreeMap<>();

        // Gather all points to split segments, and their types (Root, Inflection, or Kink in offsets)
        NavigableMap<Double, Boundary> splits0 = new TreeMap<>(); // splits0 & splits because splits0 must be effectively final
        if (!straight)
        {
            getRoots().forEach((t) -> splits0.put(t, Boundary.ROOT));
            getInflections().forEach((t) -> splits0.put(t, Boundary.INFLECTION));
        }
        getOffsetT(fld.getFractionalLengths().toSet()).forEach((t) -> splits0.put(t, Boundary.KINK));
        NavigableMap<Double, Boundary> splits = splits0.subMap(1e-6, false, 1.0 - 1e-6, false);

        // Initialize loop variables
        // Work on a copy of the offset fractions, so we can remove each we use.
        // Skip t == 0.0 while collecting the split points -on- this B&eacute;zier
        NavigableSet<Double> fCrossSectionRemain = fld.getFractionalLengths().toSet().tailSet(0.0, false);
        double lengthTotal = length();
        BezierCubic2d currentBezier = this;
        // System.out.println("Current bezier is " + this);
        double lengthSoFar = 0.0;
        // curvature and angle sign, flips at each inflection, start based on initial curve
        double sig = Math.signum((getY(1) - getY(0)) * (getX(2) - getX(0)) - (getX(1) - getX(0)) * (getY(2) - getY(0)));

        Iterator<Double> typeIterator = splits.navigableKeySet().iterator();
        double tStart = 0.0;
        if (splits.isEmpty())
        {
            this.segments.put(tStart, currentBezier.offset(fld, lengthSoFar, lengthTotal, sig, true));
        }
        while (typeIterator.hasNext())
        {
            double tInFull = typeIterator.next();
            Boundary type = splits.get(tInFull);
            double t;
            // Note: as we split the B&eacute;zier curve and work with the remainder in each loop, the resulting t value is not
            // the same as on the full B&eacute;zier curve. Therefore we need to refind the roots, or inflections, or at least
            // one cross-section.
            if (type == Boundary.ROOT)
            {
                t = currentBezier.getRoots().first();
            }
            else if (type == Boundary.INFLECTION)
            {
                t = currentBezier.getInflections().first();
            }
            else
            {
                NavigableSet<Double> fCrossSection = new TreeSet<>();
                double fSoFar = lengthSoFar / lengthTotal;
                double fFirst = fCrossSectionRemain.pollFirst(); // fraction in total B&eacute;zier curve
                fCrossSection.add((fFirst - fSoFar) / (1.0 - fSoFar)); // add fraction in remaining B&eacute;zier curve
                SortedSet<Double> offsets = currentBezier.getOffsetT(fCrossSection);
                t = offsets.first();
            }
            if (t < 1e-10)
            {
                continue;
            }

            // Split B&eacute;zier curve, and add offset of first part
            SplitBeziers parts = currentBezier.split(t);
            BezierCubic2d segment = parts.first.offset(fld, lengthSoFar, lengthTotal, sig, false);
            // System.out.println("Offset segment at " + tStart + ": " + segment);
            this.segments.put(tStart, segment);

            // Update loop variables
            lengthSoFar += parts.first.getLength();
            if (type == Boundary.INFLECTION)
            {
                sig = -sig;
            }
            tStart = tInFull;

            // Append last segment, or loop again with remainder
            if (!typeIterator.hasNext())
            {
                BezierCubic2d lastSegment = parts.remainder.offset(fld, lengthSoFar, lengthTotal, sig, true);
                // System.out.println("Offset last segment at " + tStart + ": " + lastSegment);
                this.segments.put(tStart, lastSegment);
            }
            else
            {
                currentBezier = parts.remainder;
            }
        }
        this.segments.put(1.0, null); // so we can interpolate t values along segments
        this.fldForSegments = fld;
        // double lastDirection = Double.NaN;
        // for (Double fraction = this.segments.firstKey(); fraction != null; fraction = this.segments.higherKey(fraction))
        // {
        // ContinuousBezierCubic2d cbc = this.segments.get(fraction);
        // System.out.print(String.format("%20.18f: %10.10s ", fraction, splits.get(fraction)));
        // if (cbc != null)
        // {
        // System.out.print(cbc.getStartPoint() + " " + cbc.getEndPoint() + " " + cbc.length);
        // if ((!Double.isNaN(lastDirection))
        // && (Math.abs(AngleUtil.normalizeAroundZero(lastDirection - cbc.getStartPoint().dirZ))) > 0.01)
        // {
        // System.out.print(" DIRECTION MISMATCH");
        // }
        // System.out.println();
        // lastDirection = cbc.getEndPoint().dirZ;
        // }
        // else
        // {
        // System.out.println("null");
        // }
        // }
        // System.out.println("finished updating segments");
    }

    @Override
    public Point2d getPoint(final double fraction, final PieceWiseLinearOffset2d fld)
    {
        updateSegments(fld);
        Entry<Double, BezierCubic2d> entry;
        double nextT;
        if (fraction == 1.0)
        {
            entry = BezierCubic2d.this.segments.lowerEntry(fraction);
            nextT = fraction;
        }
        else
        {
            entry = BezierCubic2d.this.segments.floorEntry(fraction);
            nextT = BezierCubic2d.this.segments.higherKey(fraction);
        }
        double t = (fraction - entry.getKey()) / (nextT - entry.getKey());
        return entry.getValue().getPoint(t);
    }

    @Override
    public double getDirection(final double fraction, final PieceWiseLinearOffset2d fld)
    {
        updateSegments(fld);
        Entry<Double, BezierCubic2d> entry = BezierCubic2d.this.segments.floorEntry(fraction);
        if (entry.getValue() == null)
        {
            // end of line
            entry = BezierCubic2d.this.segments.lowerEntry(fraction);
            Point2d derivativeBezier = entry.getValue().derivative().getPoint(1.0);
            return Math.atan2(derivativeBezier.y, derivativeBezier.x);
        }
        Double nextT = BezierCubic2d.this.segments.higherKey(fraction);
        if (nextT == null)
        {
            nextT = 1.0;
        }
        double t = (fraction - entry.getKey()) / (nextT - entry.getKey());
        Point2d derivativeBezier = entry.getValue().derivative().getPoint(t);
        return Math.atan2(derivativeBezier.y, derivativeBezier.x);
    }

    @Override
    public PolyLine2d toPolyLine(final OffsetFlattener2d flattener, final PieceWiseLinearOffset2d fld)
    {
        Throw.whenNull(fld, "Offsets may not be null.");
        Throw.whenNull(flattener, "Flattener may not be null.");

        return flattener.flatten(this, fld);
    }

    /**
     * Creates the offset B&eacute;zier curve of a B&eacute;zier segment. These segments are part of the offset procedure.
     * @param offsets offsets as defined for the entire B&eacute;zier.
     * @param lengthSoFar cumulative length of all previously split off segments.
     * @param lengthTotal length of full B&eacute;zier.
     * @param sig sign of offset and offset slope
     * @param last {@code true} for the last B&eacute;zier segment.
     * @return offset B&eacute;zier.
     */
    private BezierCubic2d offset(final PieceWiseLinearOffset2d offsets, final double lengthSoFar, final double lengthTotal,
            final double sig, final boolean last)
    {
        double offsetStart = sig * offsets.get(lengthSoFar / lengthTotal);
        double offsetEnd = sig * offsets.get((lengthSoFar + getLength()) / lengthTotal);

        Point2d p1 = new Point2d(getX(0) - (getY(1) - getY(0)), getY(0) + (getX(1) - getX(0)));
        Point2d p2 = new Point2d(getX(3) - (getY(2) - getY(3)), getY(3) + (getX(2) - getX(3)));
        Point2d center = Point2d.intersectionOfLines(new Point2d(getX(0), getY(0)), p1, p2, new Point2d(getX(3), getY(3)));

        if (center == null)
        {
            // Start and end have same direction, offset first two by same amount, and last two by same amount to maintain
            // directions
            Point2d[] newBezierPoints = new Point2d[4];
            double ang = Math.atan2(p1.y - getY(0), p1.x - getX(0));
            double dxStart = Math.cos(ang) * offsetStart;
            double dyStart = -Math.sin(ang) * offsetStart;
            newBezierPoints[0] = new Point2d(getX(0) + dxStart, getY(0) + dyStart);
            newBezierPoints[1] = new Point2d(getX(1) + dxStart, getY(1) + dyStart);
            double dxEnd = Math.cos(ang) * offsetEnd;
            double dyEnd = -Math.sin(ang) * offsetEnd;
            newBezierPoints[2] = new Point2d(getX(2) + dxEnd, getY(2) + dyEnd);
            newBezierPoints[3] = new Point2d(getX(3) + dxEnd, getY(3) + dyEnd);
            return new BezierCubic2d(newBezierPoints[0], newBezierPoints[1], newBezierPoints[2], newBezierPoints[3]);
        }

        // move 1st and 4th point their respective offsets away from the center
        Point2d[] newBezierPoints = new Point2d[4];
        double off = offsetStart;
        for (int i = 0; i < 4; i = i + 3)
        {
            double dy = getY(i) - center.y;
            double dx = getX(i) - center.x;
            double ang = Math.atan2(dy, dx);
            double len = Math.hypot(dx, dy) + off;
            newBezierPoints[i] = new Point2d(center.x + len * Math.cos(ang), center.y + len * Math.sin(ang));
            off = offsetEnd;
        }

        // find tangent unit vectors that account for slope in offset
        double ang = sig * Math.atan((offsetEnd - offsetStart) / getLength());
        double cosAng = Math.cos(ang);
        double sinAng = Math.sin(ang);
        double dx = getX(1) - getX(0);
        double dy = getY(1) - getY(0);
        double dx1;
        double dy1;
        if (0.0 == lengthSoFar)
        {
            // force same start angle
            dx1 = dx;
            dy1 = dy;
        }
        else
        {
            // shift angle by 'ang'
            dx1 = cosAng * dx - sinAng * dy;
            dy1 = sinAng * dx + cosAng * dy;
        }
        dx = getX(2) - getX(3);
        dy = getY(2) - getY(3);
        double dx2;
        double dy2;
        if (last)
        {
            // force same end angle
            dx2 = dx;
            dy2 = dy;
        }
        else
        {
            // shift angle by 'ang'
            dx2 = cosAng * dx - sinAng * dy;
            dy2 = sinAng * dx + cosAng * dy;
        }

        // control points 2 and 3 as intersections between tangent unit vectors and line through center and original point 2 and
        // 3 in original B&eacute;zier
        Point2d cp2 = new Point2d(newBezierPoints[0].x + dx1, newBezierPoints[0].y + dy1);
        newBezierPoints[1] = Point2d.intersectionOfLines(newBezierPoints[0], cp2, center, new Point2d(getX(1), getY(1)));
        Point2d cp3 = new Point2d(newBezierPoints[3].x + dx2, newBezierPoints[3].y + dy2);
        newBezierPoints[2] = Point2d.intersectionOfLines(newBezierPoints[3], cp3, center, new Point2d(getX(2), getY(2)));

        // create offset B&eacute;zier
        return new BezierCubic2d(newBezierPoints[0], newBezierPoints[1], newBezierPoints[2], newBezierPoints[3]);
    }

    @Override
    public double getLength()
    {
        return this.length;
    }

    @Override
    public String toString()
    {
        return "ContinuousBezierCubic2d [startPoint=" + this.startPoint + ", endPoint=" + this.endPoint + ", controlPoint1="
                + new Point2d(getX(1), getY(1)) + ", controlPoint2=" + new Point2d(getX(2), getY(2)) + ", length=" + this.length
                + ", startDirZ=" + getStartPoint().dirZ + " (" + getPoint(0).directionTo(getPoint(1)) + "), endir="
                + getEndPoint().dirZ + " (" + getPoint(2).directionTo(getPoint(3)) + ")]";
    }

    /**
     * The various discontinuities/boundaries of a B&eacute;zier curve.
     */
    private enum Boundary
    {
        /** Root of B&eacute;zier curve. */
        ROOT,

        /** Inflection point of B&eacute;zier curve. */
        INFLECTION,

        /** Kink in offsets. */
        KINK
    }

}
