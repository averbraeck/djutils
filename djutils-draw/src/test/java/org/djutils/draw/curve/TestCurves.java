package org.djutils.draw.curve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.djutils.base.AngleUtil;
import org.djutils.draw.Export;
import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.Ray2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.immutablecollections.ImmutableSet;
import org.junit.jupiter.api.Test;

/**
 * TestCurves.java.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * TODO also with maxAngle flattener and non-declared knot
 * <p>
 * TODO test flattener with curve that has a knot
 * </p>
 * <p>
 * TODO test Bezier3d
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class TestCurves
{

    /**
     * Test the ContinuousStraight class.
     */
    @Test
    public void testStraight()
    {
        NavigableMap<Double, Double> transition = new TreeMap<>();
        transition.put(0.0, 0.0);
        transition.put(0.2, 1.0);
        transition.put(1.0, 2.0);
        int steps = 100;
        for (double x : new double[] {-10, -1, -0.1, 0, 0.1, 1, 10})
        {
            for (double y : new double[] {-30, -3, -0.3, 0, 0.3, 3, 30})
            {
                for (double dirZ : new double[] {-3, -2, -1, 0, 1, 2, 3})
                {
                    DirectedPoint2d dp = new DirectedPoint2d(x, y, dirZ);
                    for (double length : new double[] {0.3, 3, 30})
                    {
                        Straight2d cs = new Straight2d(dp, length);
                        assertEquals(x, cs.getStartPoint().x, 0.0, "start x");
                        assertEquals(y, cs.getStartPoint().y, 0.0, "start y");
                        assertEquals(dirZ, cs.getStartPoint().dirZ, 0.0, "start dirZ");
                        assertEquals(cs.getStartCurvature(), 0, 0, "start curvature");
                        assertEquals(x + Math.cos(dirZ) * length, cs.getEndPoint().x, 0.00001, "end x");
                        assertEquals(y + Math.sin(dirZ) * length, cs.getEndPoint().y, 0.00001, "end y");
                        assertEquals(dirZ, cs.getEndPoint().dirZ, 0.00001, "end dirZ");
                        assertEquals(cs.getEndCurvature(), 0, 0, "end curvature");
                        assertEquals(length, cs.getLength(), 0.00001, "length");
                        PolyLine2d flattened = cs.toPolyLine(null);
                        assertEquals(2, flattened.size(), "size of flattened is 2 points");
                        assertEquals(x, flattened.get(0).x, 0, "start of flattened x");
                        assertEquals(y, flattened.get(0).y, 0, "start of flattened y");
                        assertEquals(x + Math.cos(dirZ) * length, flattened.get(1).x, 0.00001, "end of flattened x");
                        assertEquals(y + Math.sin(dirZ) * length, flattened.get(1).y, 0.00001, "end of flattened y");
                        for (int step = 0; step <= steps; step++)
                        {
                            double fraction = 1.0 * step / steps;
                            Point2d position = cs.getPoint(fraction);
                            double direction = cs.getDirection(fraction);
                            Point2d closest = flattened.closestPointOnPolyLine(position);
                            assertEquals(0, position.distance(closest), 0.01, "Point at fraction lies on flattened line");
                            assertEquals(flattened.get(0).directionTo(flattened.get(1)), direction, 0.00001,
                                    "Direction matches");
                        }
                        PieceWiseLinearOffset2d fld = new PieceWiseLinearOffset2d(transition);
                        flattened = cs.toPolyLine(null, fld);
                        assertEquals(3, flattened.size(),
                                "size of flattened line with one offset knot along the way is 3 points");
                        assertEquals(x, flattened.get(0).x, 0, "start of flattened x");
                        assertEquals(y, flattened.get(0).y, 0, "start of flattened y");
                        assertEquals(x + length * 0.2 * Math.cos(dirZ) - fld.get(0.2) * Math.sin(dirZ), flattened.getX(1),
                                0.0001, "x of intermediate point");
                        assertEquals(y + length * 0.2 * Math.sin(dirZ) + fld.get(0.2) * Math.cos(dirZ), flattened.getY(1),
                                0.0001, "x of intermediate point");
                        assertEquals(x + length * 1.0 * Math.cos(dirZ) - fld.get(1.0) * Math.sin(dirZ), flattened.getX(2),
                                0.0001, "x of intermediate point");
                        assertEquals(y + length * 1.0 * Math.sin(dirZ) + fld.get(1.0) * Math.cos(dirZ), flattened.getY(2),
                                0.0001, "x of intermediate point");
                        for (int step = 0; step <= steps; step++)
                        {
                            double fraction = 1.0 * step / steps;
                            Point2d position = cs.getPoint(fraction, fld);
                            double direction = cs.getDirection(fraction, fld);
                            Point2d closest = flattened.closestPointOnPolyLine(position);
                            assertEquals(0, position.distance(closest), 0.01, "Point at fraction lies on flattened line");
                            if (fraction < 0.2)
                            {
                                assertEquals(flattened.get(0).directionTo(flattened.get(1)), direction, 0.00001,
                                        "Direction matches");
                            }
                            if (fraction > 0.2)
                            {
                                assertEquals(flattened.get(1).directionTo(flattened.get(2)), direction, 0.00001,
                                        "Direction matches");
                            }
                        }
                    }
                }
            }
        }
        try
        {
            new Straight2d(new DirectedPoint2d(1, 2, 3), -0.2);
            fail("negative length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            new Straight2d(new DirectedPoint2d(1, 2, 3), 0.0);
            fail("zero length should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        assertTrue(new Straight2d(new DirectedPoint2d(2, 5, 1), 3).toString().startsWith("Straight ["),
                "toString returns something descriptive");
    }

    /**
     * Test the ContinuousArc class.
     */
    @Test
    public void testArc()
    {
        NavigableMap<Double, Double> transition = new TreeMap<>();
        transition.put(0.0, 0.0);
        transition.put(0.2, 1.0);
        transition.put(1.0, 2.0);
        for (double x : new double[] {0, -10, -1, -0.1, 0.1, 1, 10})
        {
            for (double y : new double[] {0, -30, -3, -0.3, 0.3, 3, 30})
            {
                for (double dirZ : new double[] {-3, -2, -1, 0, Math.PI / 2, 1, 2, 3})
                {
                    DirectedPoint2d dp = new DirectedPoint2d(x, y, dirZ);
                    for (double radius : new double[] {3.0, 0.3, 30})
                    {
                        for (boolean left : new Boolean[] {false, true})
                        {
                            for (double a : new double[] {1, 0.1, 2, 5})
                            {
                                Arc2d ca = new Arc2d(dp, radius, left, a);
                                assertEquals(x, ca.getStartPoint().x, 0, "start x");
                                assertEquals(y, ca.getStartPoint().y, 0, "start y");
                                assertEquals(dirZ, ca.getStartPoint().dirZ, 0, "start dirZ");
                                assertEquals(radius, ca.getStartRadius(), 0.000001, "start radius");
                                assertEquals(radius, ca.getEndRadius(), 0.000001, "end radius");
                                assertEquals(1 / radius, ca.getStartCurvature(), 0.00001, "start curvature");
                                assertEquals(1 / radius, ca.getEndCurvature(), 0.00001, "end curvature");
                                assertEquals(dirZ, ca.getStartDirection(), 0, "start direction");
                                assertEquals(AngleUtil.normalizeAroundZero(dirZ + (left ? a : -a)), ca.getEndDirection(),
                                        0.00001, "end direction");
                                int sign = left ? 1 : -1;
                                Point2d center =
                                        new Point2d(x - Math.sin(dirZ) * radius * sign, y + Math.cos(dirZ) * radius * sign);
                                DirectedPoint2d expectedEnd =
                                        new DirectedPoint2d(center.x + Math.sin(dirZ + a * sign) * radius * sign,
                                                center.y - Math.cos(dirZ + a * sign) * radius * sign,
                                                AngleUtil.normalizeAroundZero(dirZ + a * sign));
                                assertTrue(expectedEnd.epsilonEquals(ca.getEndPoint(), 0.001, 0.00001), " end point");
                                assertEquals(radius * a, ca.getLength(), 0.00001, "length");
                                // Test the NumSegments flattener without offsets
                                PolyLine2d flattened = ca.toPolyLine(new Flattener2d.NumSegments(20));
                                verifyNumSegments(ca, flattened, 20);
                                // Test the MaxDeviation flattener without offsets
                                double precision = 0.1;
                                flattened = ca.toPolyLine(new Flattener2d.MaxDeviation(precision));
                                verifyMaxDeviation(ca, flattened, precision);
                                // Test the MaxAngle flattener without offsets
                                double anglePrecision = 0.01;
                                flattened = ca.toPolyLine(new Flattener2d.MaxAngle(anglePrecision));
                                verifyMaxAngleDeviation(flattened, ca, anglePrecision);
                                // Test the MaxDeviationAndAngle flattener without offsets
                                flattened = ca.toPolyLine(new Flattener2d.MaxDeviationAndAngle(precision, anglePrecision));
                                verifyMaxDeviation(ca, flattened, precision);
                                verifyMaxAngleDeviation(flattened, ca, anglePrecision);
                                // Only check transitions for radius of arc > 2 and length of arc > 2
                                if (radius > 2 && ca.getLength() > 2)
                                {
                                    PieceWiseLinearOffset2d fld = new PieceWiseLinearOffset2d(transition);
                                    // Test the NumSegments flattener with offsets
                                    flattened = ca.toPolyLine(new OffsetFlattener2d.NumSegments(30), fld);
                                    verifyNumSegments(ca, fld, flattened, 30);
                                    // Test the MaxDeviation flattener with offsets
                                    flattened = ca.toPolyLine(new OffsetFlattener2d.MaxDeviation(precision), fld);
                                    verifyMaxDeviation(ca, fld, flattened, precision);
                                    // Test the MaxAngle flattener with offsets
                                    flattened = ca.toPolyLine(new OffsetFlattener2d.MaxAngle(anglePrecision), fld);
                                    verifyMaxAngleDeviation(flattened, ca, fld, anglePrecision);
                                    // Test the MaxDeviationAndAngle flattener with offsets
                                    flattened = ca.toPolyLine(
                                            new OffsetFlattener2d.MaxDeviationAndAngle(precision, anglePrecision), fld);
                                    verifyMaxDeviation(ca, fld, flattened, precision);
                                    verifyMaxAngleDeviation(flattened, ca, fld, anglePrecision);
                                }
                            }
                        }
                    }
                }
            }
        }
        try
        {
            new Arc2d(new DirectedPoint2d(1, 2, 3), -0.01, true, 1);
            fail("negative radius should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        new Arc2d(new DirectedPoint2d(1, 2, 3), 0, true, 1); // is allowed
        try
        {
            new Arc2d(new DirectedPoint2d(1, 2, 3), 10, true, -0.1);
            fail("negative angle should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        new Arc2d(new DirectedPoint2d(1, 2, 3), 10, true, 0); // is allowed
        assertTrue(new Arc2d(new DirectedPoint2d(1, 2, 3), 10, true, 1).toString().startsWith("Arc ["),
                "toString returns something descriptive");
    }

    /**
     * Verify the number of segments and the location of the points on a flattened FlattableLine2d.
     * @param curve FlattableLine2d
     * @param flattened PolyLine2d
     * @param numSegments int; the number of segments that the flattened FlattableLine2d should have
     */
    private static void verifyNumSegments(final Curve2d curve, final PolyLine2d flattened, final int numSegments)
    {
        assertEquals(numSegments, flattened.size() - 1, "Number of segments");
        for (int i = 0; i <= numSegments; i++)
        {
            double fraction = i * 1.0 / numSegments;
            Point2d expectedPoint = curve.getPoint(fraction);
            // TODO this can't work for bezier
            Point2d actualPoint = flattened.get(i);
            assertEquals(expectedPoint, actualPoint, "Point in flattened line matches point generated by the continuous arc");
        }
    }

    /**
     * Verify the number of segments and the location of the points on a flattened OffsetFlattableLine2d.
     * @param curve OffsetFlattableLine2d
     * @param fld FractionalLengthData (may be null)
     * @param flattened PolyLine2d
     * @param numSegments int; the number of segments that the flattened OffsetFlattableLine2d should have
     */
    private static void verifyNumSegments(final OffsetCurve2d curve, final PieceWiseLinearOffset2d fld,
            final PolyLine2d flattened, final int numSegments)
    {
        assertEquals(numSegments, flattened.size() - 1, "Number of segments");
        for (int i = 0; i <= numSegments; i++)
        {
            double fraction = i * 1.0 / numSegments;
            Point2d expectedPoint = curve.getPoint(fraction, fld);
            Point2d actualPoint = flattened.get(i);
            assertEquals(expectedPoint, actualPoint, "Point in flattened line matches point generated by the continuous arc");
        }
    }

    /** Maximum permissible exceeding of precision. Needed due to the simple-minded way that the Flattener works. */
    public static final double FUDGE_FACTOR = 1.4;

    /**
     * Verify the lateral precision of a flattened FlattableLine2d.
     * @param curve FlattableLine2d
     * @param flattened PolyLine2d
     * @param precision double
     */
    private static void verifyMaxDeviation(final Curve2d curve, final PolyLine2d flattened, final double precision)
    {
        int steps = 100;
        for (int step = 0; step <= steps; step++)
        {
            double fraction = 1.0 * step / steps;
            Point2d curvePoint = curve.getPoint(fraction);
            Point2d polyLinePoint = flattened.closestPointOnPolyLine(curvePoint);
            if (curvePoint.distance(polyLinePoint) > precision * FUDGE_FACTOR)
            {
                printSituation(-1, 0.5, flattened, curve, fraction, null);
                curve.toPolyLine(new Flattener2d.MaxDeviation(precision));
            }
            assertEquals(0, curvePoint.distance(polyLinePoint), precision * FUDGE_FACTOR,
                    "point on FlattableLine2d is close to PolyLine2d");
        }
    }

    /**
     * Verify the lateral precision of a flattened continuous FlattableLine2d.
     * @param curve FlattableLine2d
     * @param fld FractionalLengthData
     * @param flattened PolyLine2d
     * @param precision double
     */
    private static void verifyMaxDeviation(final OffsetCurve2d curve, final PieceWiseLinearOffset2d fld,
            final PolyLine2d flattened, final double precision)
    {
        double fraction = 0.0;
        int steps = flattened.size() - 1;
        for (int step = 0; step < steps; step++)
        {
            double fractionAtStartOfSegment = Double.NaN;
            double fractionAtEndOfSegment = Double.NaN;
            LineSegment2d lineSegment = flattened.getSegment(step);
            // Bisect to find fraction for start and end of segment and use middle of those fractions as THE fraction
            for (double positionOnSegment : new double[] {0.01, 0.99})
            {
                Point2d pointOnSegment = lineSegment.getLocation(positionOnSegment * lineSegment.getLength());
                double flattenedDir = lineSegment.getStartPoint().directionTo(lineSegment.getEndPoint());
                // Find a fraction on fa2d that results in a point very close to flattenPoint
                double veryClose = 0.1 / flattened.size() / 5; // Don't know why that / 5 was needed
                // Use bisection to encroach on the fraction
                double highFraction = Math.min(1.0, fraction + Math.min(20.0 / steps, 0.5));
                while (highFraction - fraction > veryClose)
                {
                    double midFraction = (fraction + highFraction) / 2;
                    Point2d midPoint = curve.getPoint(midFraction, fld);
                    double dir = midPoint.directionTo(pointOnSegment);
                    double dirDifference = Math.abs(AngleUtil.normalizeAroundZero(flattenedDir - dir));
                    if (dirDifference < Math.PI / 4)
                    {
                        fraction = midFraction;
                    }
                    else
                    {
                        highFraction = midFraction;
                    }
                }
                if (positionOnSegment < 0.5)
                {
                    fractionAtStartOfSegment = fraction;
                }
                else
                {
                    fractionAtEndOfSegment = fraction;
                }
            }
            fraction = (fractionAtStartOfSegment + fractionAtEndOfSegment) / 2; // Take the middle
            Point2d curvePoint = curve.getPoint(fraction, fld);
            double actualDistance = curvePoint.distance(lineSegment.closestPointOnSegment(curvePoint));
            if (actualDistance > precision * FUDGE_FACTOR)
            {
                printSituation(step, 0.5, flattened, curve, fraction, fld);
                curve.toPolyLine(new OffsetFlattener2d.MaxDeviation(precision), fld);
            }
            assertEquals(0, actualDistance, precision * FUDGE_FACTOR, "point on OffsetFlattableLine2d is close to PolyLine2d");
            fraction = fractionAtEndOfSegment;
        }
    }

    /**
     * Print things for debugging.
     * @param segment int; the step along the curve or the polyLine2d
     * @param positionOnSegment double
     * @param flattened PolyLine2d
     * @param line Object
     * @param fraction double
     * @param fld FractionalLengthData (may be null)
     */
    public static void printSituation(final int segment, final double positionOnSegment, final PolyLine2d flattened,
            final Object line, final double fraction, final PieceWiseLinearOffset2d fld)
    {
        System.out.println("# " + line);
        System.out.print(Export.toPlot(flattened));
        if (null != fld)
        {
            System.out.print("c0,1,0 "
                    + Export.toPlot(((OffsetCurve2d) line).toPolyLine(new OffsetFlattener2d.MaxDeviation(0.01), fld)));
        }
        System.out.print("c0,1,1 " + Export.toPlot(((Curve2d) line).toPolyLine(new Flattener2d.MaxDeviation(0.01))));
        Point2d pointAtFraction =
                null != fld ? ((OffsetCurve2d) line).getPoint(fraction, fld) : ((Curve2d) line).getPoint(fraction);
        double faDir = null != fld ? ((OffsetCurve2d) line).getDirection(fraction, fld)
                : ((Curve2d) line).getDirection(fraction);
        System.out.print("# curveDirection=" + faDir);
        if (segment >= 0)
        {
            double flattenedDir = flattened.get(segment).directionTo(flattened.get(segment + 1));
            System.out.println(", segment direction=" + flattenedDir + " directionDifference=" + (flattenedDir - faDir));
            System.out.println("# segment=" + segment + ", positionOnSegment=" + positionOnSegment);
            System.out.println("sw0.1c1,0.6,0.6 " + Export.toPlot(flattened.getSegment(segment)) + " r");
            LineSegment2d lineSegment = flattened.getSegment(segment);
            Point2d closestPointOnSegment = lineSegment.closestPointOnSegment(pointAtFraction);
            System.out.println("# closestPointOnSegment=" + closestPointOnSegment + " distance from pointAtFraction to segment="
                    + pointAtFraction.distance(closestPointOnSegment));
        }
        else
        {
            System.out.println();
        }
        Point2d closestPointOnFlattened = flattened.closestPointOnPolyLine(pointAtFraction);
        System.out.println("# fraction=" + fraction + " pointAtFraction=" + pointAtFraction + ", closestPointOnFlattened="
                + closestPointOnFlattened + ", distance=" + pointAtFraction.distance(closestPointOnFlattened));
        System.out.print("# segments: ");
        for (int i = 0; i < flattened.size() - 1; i++)
        {
            System.out.print(String.format("%s%3d%s ", i == segment ? "##" : "  ", i, i == segment ? "##" : "  "));
        }
        System.out.print("\n# angles:  ");
        for (int i = 0; i < flattened.size() - 1; i++)
        {
            System.out.print(String.format(" %7.4f", flattened.get(i).directionTo(flattened.get(i + 1))));
        }
        System.out.print("\n# lengths: ");
        for (int i = 0; i < flattened.size() - 1; i++)
        {
            System.out.print(String.format(" %7.4f", flattened.get(i).distance(flattened.get(i + 1))));
        }
        System.out.print("\n# x:    ");
        for (int i = 0; i < flattened.size(); i++)
        {
            System.out.print(String.format(" %7.4f", flattened.get(i).x));
        }
        System.out.print("\n# y:    ");
        for (int i = 0; i < flattened.size(); i++)
        {
            System.out.print(String.format(" %7.4f", flattened.get(i).y));
        }
        System.out.println("\nc0,0,1 M0,0L " + pointAtFraction.x + "," + pointAtFraction.y);
        if (null != fld)
        {
            System.out.print("# Knots in ofl2d domain:");
            OffsetCurve2d ofl2d = (OffsetCurve2d) line;
            for (Iterator<Double> iterator = fld.iterator(); iterator.hasNext();)
            {
                double knot = iterator.next();
                if (knot != 0.0 && knot != 1.0)
                {
                    double t = ofl2d.getT(knot * ofl2d.getLength());
                    System.out.println("\tknot at " + knot + " -> fraction " + t + " point " + ofl2d.getPoint(t, fld));
                }
            }
        }

        System.out.println("break here");
    }

    /**
     * Verify that a flattened FlattableLine2d has matches direction with the flattableLine2d.
     * @param flattened PolyLine2d
     * @param curve FlattableLine2d
     * @param anglePrecision double
     */
    public static void verifyMaxAngleDeviation(final PolyLine2d flattened, final Curve2d curve, final double anglePrecision)
    {
        double fraction = 0.0;
        for (int step = 0; step < flattened.size() - 1; step++)
        {
            double flattenedDir = flattened.get(step).directionTo(flattened.get(step + 1));
            for (double positionOnSegment : new double[] {0.1, 0.9})
            {
                Point2d flattenPoint = flattened.get(step).interpolate(flattened.get(step + 1), positionOnSegment);
                // Find a fraction on fa2d that results in a point very close to flattenPoint
                double veryClose = 0.1 / flattened.size() / 5; // Don't know why that / 5 was needed
                // Use bisection to encroach on the fraction
                double highFraction = Math.min(1.0, fraction + Math.min(20.0 / flattened.size(), 0.5));
                while (highFraction - fraction > veryClose)
                {
                    double midFraction = (fraction + highFraction) / 2;
                    Point2d midPoint = curve.getPoint(midFraction);
                    double dir = flattenPoint.directionTo(midPoint);
                    double dirDifference = Math.abs(AngleUtil.normalizeAroundZero(flattenedDir - dir));
                    if (dirDifference < Math.PI / 2)
                    {
                        highFraction = midFraction;
                    }
                    else
                    {
                        fraction = midFraction;
                    }
                }
                double faDir = curve.getDirection(fraction);
                if (Math.abs(AngleUtil.normalizeAroundZero(flattenedDir - faDir)) > anglePrecision * FUDGE_FACTOR)
                {
                    printSituation(step, positionOnSegment, flattened, curve, fraction, null);
                    curve.toPolyLine(new Flattener2d.MaxAngle(anglePrecision));
                }
                assertEquals(0, AngleUtil.normalizeAroundZero(flattenedDir - faDir), anglePrecision * FUDGE_FACTOR,
                        "direction difference should be less than anglePrecision");
            }
        }
    }

    /**
     * Verify that a flattened FlattableLine2d has no knots sharper than specified, except at the boundary points in the
     * FractionalLengthData.
     * @param flattened PolyLine2d
     * @param curve OffsetFlattableLine2d
     * @param fld FractionalLengthData
     * @param anglePrecision double
     */
    public static void verifyMaxAngleDeviation(final PolyLine2d flattened, final OffsetCurve2d curve,
            final PieceWiseLinearOffset2d fld, final double anglePrecision)
    {
        double fraction = 0.0;
        for (int step = 0; step < flattened.size() - 1; step++)
        {
            double flattenedDir = flattened.get(step).directionTo(flattened.get(step + 1));
            for (double positionOnSegment : new double[] {0.1, 0.9})
            {
                Point2d flattenPoint = flattened.get(step).interpolate(flattened.get(step + 1), positionOnSegment);
                // Find a fraction on fa2d that results in a point very close to flattenPoint
                double veryClose = 0.1 / flattened.size() / 5; // Don't know why that / 5 was needed
                // Use bisection to encroach on the fraction
                double highFraction = Math.min(1.0, fraction + 0.1);
                while (highFraction - fraction > veryClose)
                {
                    double midFraction = (fraction + highFraction) / 2;
                    Point2d midPoint = curve.getPoint(midFraction, fld);
                    double dir = flattenPoint.directionTo(midPoint);
                    double dirDifference = Math.abs(AngleUtil.normalizeAroundZero(flattenedDir - dir));
                    if (dirDifference < Math.PI / 2)
                    {
                        highFraction = midFraction;
                    }
                    else
                    {
                        fraction = midFraction;
                    }
                }
                // Check if there is a knot very close to fraction
                Double knot = null;
                for (Iterator<Double> iterator = fld.iterator(); iterator.hasNext();)
                {
                    knot = iterator.next();
                    if (knot != 0.0 && knot != 1.0 && Math.abs(curve.getT(knot * curve.getLength()) - fraction) <= veryClose)
                    {
                        break;
                    }
                    knot = null;
                }
                if (knot == null)
                {
                    double faDir = curve.getDirection(fraction, fld);
                    if (Math.abs(AngleUtil.normalizeAroundZero(flattenedDir - faDir)) > anglePrecision * FUDGE_FACTOR)
                    {
                        printSituation(step, positionOnSegment, flattened, curve, fraction, fld);
                        curve.getDirection(fraction, fld);
                        curve.toPolyLine(new OffsetFlattener2d.MaxAngle(anglePrecision), fld);
                    }
                    assertEquals(0, AngleUtil.normalizeAroundZero(flattenedDir - faDir), anglePrecision * FUDGE_FACTOR,
                            "direction difference should be less than anglePrecision");
                }
            }
        }
    }

    /**
     * Test the ContinuousBezier2d and ContinuousBezierCubic2d classes.
     */
    @Test
    public void testBezier2d()
    {
        NavigableMap<Double, Double> transition = new TreeMap<>();
        transition.put(0.0, 0.0);
        transition.put(0.2, 1.0);
        transition.put(1.0, 2.0);
        for (double x : new double[] {0, -1, -0.1, 10})
        {
            for (double y : new double[] {0, -3, -0.3, 30})
            {
                for (double dirZ : new double[] {-3, -2, -1, 0, Math.PI / 2, 1, 2, 3})
                {
                    Ray2d dp = new Ray2d(x, y, dirZ);
                    for (double x2 : new double[] {10.5, 30})
                    {
                        for (double y2 : new double[] {30.5, 70})
                        {
                            for (double dirZ2 : new double[] {1, 0.1, 2.5, 5})
                            {
                                Ray2d dp2 = new Ray2d(x2, y2, dirZ2);
                                BezierCubic2d cbc = new BezierCubic2d(dp, dp2);
                                assertEquals(x, cbc.getStartPoint().x, 0, "start x");
                                assertEquals(y, cbc.getStartPoint().y, 0, "start y");
                                assertEquals(dirZ, cbc.getStartPoint().dirZ, 0.00001, "start dirZ");
                                assertEquals(dirZ, cbc.getStartDirection(), 0.00001, "start direction");
                                assertEquals(x2, cbc.getEndPoint().x, 0.000001, "end x");
                                assertEquals(y2, cbc.getEndPoint().y, 0.000001, "end y");
                                assertEquals(AngleUtil.normalizeAroundZero(dirZ2), cbc.getEndPoint().dirZ, 0.00001, "end dirZ");
                                assertEquals(AngleUtil.normalizeAroundZero(dirZ2), cbc.getEndDirection(), 0.00001,
                                        "end direction");
                                // Test the NumSegments flattener without offsets
                                PolyLine2d flattened = cbc.toPolyLine(new Flattener2d.NumSegments(20));
                                verifyNumSegments(cbc, flattened, 20);
                                // Test the MaxDeviation flattener without offsets
                                double precision = 0.1;
                                flattened = cbc.toPolyLine(new Flattener2d.MaxDeviation(precision));
                                verifyMaxDeviation(cbc, flattened, precision);
                                double anglePrecision = 0.01;
                                double meanDir = dp.directionTo(dp2);
                                if (Math.abs(AngleUtil.normalizeAroundZero(meanDir - dirZ)) < 2.5
                                        && Math.abs(AngleUtil.normalizeAroundZero(meanDir - dirZ2)) < 2.5)
                                {
                                    // Test the MaxAngle flattener without offsets
                                    flattened = cbc.toPolyLine(new Flattener2d.MaxAngle(anglePrecision));
                                    verifyMaxAngleDeviation(flattened, cbc, anglePrecision);
                                    // Test the MaxDeviationAndAngle flattener without offsets
                                    flattened = cbc.toPolyLine(new Flattener2d.MaxDeviationAndAngle(precision, anglePrecision));
                                    verifyMaxDeviation(cbc, flattened, precision);
                                    verifyMaxAngleDeviation(flattened, cbc, anglePrecision);
                                }
                                // Only check transitions for radius of arc > 2 and length of arc > 2
                                if (cbc.getStartRadius() > 2 && cbc.getLength() > 2)
                                {
                                    PieceWiseLinearOffset2d fld = new PieceWiseLinearOffset2d(transition);
                                    // Test the NumSegments flattener with offsets
                                    flattened = cbc.toPolyLine(new OffsetFlattener2d.NumSegments(30), fld);
                                    verifyNumSegments(cbc, fld, flattened, 30);
                                    // Test the MaxDeviation flattener with offsets
                                    flattened = cbc.toPolyLine(new OffsetFlattener2d.MaxDeviation(precision), fld);
                                    if (Math.abs(AngleUtil.normalizeAroundZero(meanDir - dirZ)) < 2
                                            && Math.abs(AngleUtil.normalizeAroundZero(meanDir - dirZ2)) < 2)
                                    {
                                        // Test with offsets only for shapes that we expect to be smooth
                                        verifyMaxDeviation(cbc, fld, flattened, precision);
                                        flattened = cbc.toPolyLine(new OffsetFlattener2d.MaxAngle(anglePrecision), fld);
                                        verifyMaxAngleDeviation(flattened, cbc, fld, anglePrecision);
                                        // Test the MaxDeviationAndAngle flattener with offsets
                                        flattened = cbc.toPolyLine(
                                                new OffsetFlattener2d.MaxDeviationAndAngle(precision, anglePrecision), fld);
                                        verifyMaxDeviation(cbc, fld, flattened, precision);
                                        verifyMaxAngleDeviation(flattened, cbc, fld, anglePrecision);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Check the startRadius and endRadius of CubicBezier2d and getT.
     */
    @Test
    public void testCubicbezierRadiusAndSome()
    {
        // Check that the curvature functions return something sensible
        // https://stackoverflow.com/questions/1734745/how-to-create-circle-with-b%C3%A9zier-curves
        double controlDistance = (4.0 / 3) * Math.tan(Math.PI / 8);
        BezierCubic2d bcb = new BezierCubic2d(new Point2d(1, 0), new Point2d(1, controlDistance),
                new Point2d(controlDistance, 1), new Point2d(0, 1));
        assertEquals(1.0, bcb.getStartRadius(), 0.03, "start radius of cubic bezier approximation of unit circle");
        assertEquals(1.0, bcb.getEndRadius(), 0.03, "end radius of cubic bezier approximation of unit circle");
        bcb = new BezierCubic2d(new Point2d(1, 0), new Point2d(1, -controlDistance),
                new Point2d(controlDistance, -1), new Point2d(0, -1));
        assertEquals(-1.0, bcb.getStartRadius(), 0.03, "start radius of cubic bezier approximation of unit circle");
        assertEquals(-1.0, bcb.getEndRadius(), 0.03, "end radius of cubic bezier approximation of unit circle");
        assertEquals(0.0, bcb.getT(0.0), 0.0, "getT is exact at 0.0");
        assertEquals(0.0, bcb.getT(0.001 * bcb.getLength()), 0.1, "getT is close to 0.0 for small input");
        assertEquals(0.5, bcb.getT(0.5 * bcb.getLength()), 0.01, "getT is close to 0.5 halfway on symmetrical Bezier");
        assertEquals(1.0, bcb.getT(0.999 * bcb.getLength()), 0.1, "getT is close to 1.0 for input close to 1.0");
        assertEquals(1.0, bcb.getT(bcb.getLength()), 0.0, "getT is exact at 1.0");
        
        try
        {
            bcb.split(-0.0001);
            fail("Negative split point should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        try
        {
            bcb.split(1.0001);
            fail("Split point beyond 1.0 should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
    }

    /**
     * Test the FractionalLengthData class.
     */
    @Test
    public void testFractionalLengthData()
    {
        testFldConstructors("Zero values should have thrown an IllegalArgumentException");
        testFldConstructors("Odd number of values should have thrown an IllegalArgumentException", 0.1);
        testFldConstructors("Odd number of values should have thrown an IllegalArgumentException", 0.1, 5.0, 0.3);
        testFldConstructors("Negative fractional position should have thrown an IllegalArgumentException", -0.1, 2.0);
        testFldConstructors("Fractional position > 1.0 should have thrown an IllegalArgumentException", 1.1, 2.0);
        testFldConstructors("-0.0 fractional position should have thrown an IllegalArgumentException", -0.0, 2.0);
        testFldConstructors("value must be finite", 0.2, Double.NaN);
        testFldConstructors("value must be finite", 0.2, Double.NEGATIVE_INFINITY);
        testFldConstructors("value must be finite", 0.2, Double.POSITIVE_INFINITY);

        try
        {
            new PieceWiseLinearOffset2d((Map<Double, Double>) null);
            fail("null map should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        Map<Double, Double> map = new HashMap<>();
        map.put(null, 3.0);
        try
        {
            new PieceWiseLinearOffset2d(map);
            fail("null key in map should have thrown an IllegalArgumentException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        PieceWiseLinearOffset2d fld = new PieceWiseLinearOffset2d(0.1, 2, 0.7, 5);
        assertEquals(2, fld.get(0.0), 0.000001, "get for data with key lower than first entry returns first value");
        assertEquals(5, fld.get(1.0), 0.000001, "get for at first key higher than last entry returns first value");
        assertEquals(2, fld.get(0.1), 0.000001, "get at first key returns first value");
        assertEquals(5, fld.get(0.7), 0.000001, "get at last key returns first value");
        assertEquals(2 + (5 - 2) * (0.3 - 0.1) / (0.7 - 0.1), fld.get(0.3), 0.00001, "Value between point interpolates");
        assertEquals(3 / 0.6, fld.getDerivative(0.11), 0.00001, "get derivative works between the entries");
        assertEquals(3 / 0.6, fld.getDerivative(0.69), 0.00001, "get derivative works between the entries");
        assertEquals(0, fld.getDerivative(0.09), 0, "get derivative returns 0 outside the range of entries");
        assertEquals(0, fld.getDerivative(0.71), 0, "get derivative returns 0 outside the range of entries");
        assertEquals(2, fld.size(), "size is returned");
        ImmutableSet<Double> returnedValues = fld.getValues();
        assertEquals(2, returnedValues.size(), "getValues returned set of correct size");
        assertTrue(returnedValues.contains(2.0), "value 2.0 is among returned values");
        assertTrue(returnedValues.contains(5.0), "value 2.0 is among returned values");

        fld = new PieceWiseLinearOffset2d(0.0, 0.0, 0.2, 1.0, 1.0, 2.0);
        int index = 0;
        for (Double value : fld)
        {
            assertEquals(index == 0 ? 0.0 : index == 1 ? 0.2 : 1.0, value, 0.0, "offset is returned");
            index++;
        }

        Iterator<Double> iterator = fld.iterator();
        assertTrue(iterator.hasNext(), "first offset is available");
        assertEquals(0.0, iterator.next(), "first offset is returned");
        assertTrue(iterator.hasNext(), "second offset is available");
        assertEquals(0.2, iterator.next(), "second offset is returned");
        assertTrue(iterator.hasNext(), "third offset is available");
        assertEquals(1.0, iterator.next(), "third offset is returned");
        assertFalse(iterator.hasNext(), "iterator is now exhausted");
        try
        {
            iterator.next();
            fail("exhausted iterator should have thrown a NoSuchElementException");
        }
        catch (NoSuchElementException nsee)
        {
            // Ignore expected exception
        }

        fld = PieceWiseLinearOffset2d.of(0.3, 6);
        assertEquals(1, fld.size(), "size matches");
        returnedValues = fld.getValues();
        assertEquals(1, returnedValues.size(), "getValues returned set of correct size");
        assertTrue(returnedValues.contains(6.0), "returned set contains expected value");
    }

    /**
     * Test the various constructors of FractionalLengthData.
     * @param problem String; description of the problem with the input data that the test should detect
     * @param in double... input for the constructors
     */
    static void testFldConstructors(final String problem, final double... in)
    {
        try
        {
            new PieceWiseLinearOffset2d(in);
            fail(problem);
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        Map<Double, Double> map = new HashMap<>();
        for (int i = 0; i < in.length; i += 2)
        {
            map.put(in[i], i + 1 < in.length ? in[i + 1] : null);
        }
        if (in.length % 2 == 0)
        {
            try
            {
                new PieceWiseLinearOffset2d(map);
                fail(problem);
            }
            catch (IllegalArgumentException iae)
            {
                // Ignore expected exception
            }
        }
        else
        {
            try
            {
                new PieceWiseLinearOffset2d(map);
                fail(problem);
            }
            catch (NullPointerException npe)
            {
                // Ignore expected exception
            }
        }
        try
        {
            PieceWiseLinearOffset2d.of(in);
            fail(problem);
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
    }

}
