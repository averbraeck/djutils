package org.djutils.draw.curve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.djutils.base.AngleUtil;
import org.djutils.draw.curve.Clothoid2d;
import org.djutils.draw.curve.Flattener2d;
import org.djutils.draw.curve.OffsetFlattener2d;
import org.djutils.draw.curve.PieceWiseLinearOffset2d;
import org.djutils.draw.curve.Flattener2d.NumSegments;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * Tests the generation of clothoids with various input.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class ClothoidTest
{

    /** Number of segments for the clothoid lines to generated. */
    private static final int SEGMENTS = 64;

    /** Number of random runs per test. */
    private static final int RUNS = 10000; // this test was run 10.000.000 times, 10.000 is to check no change broke the logic

    /**
     * A reasonable S-shaped clothoid can make a total angle transition of 2 circles, one on the positive, and one on the
     * negative side of the clothoid. With small radii and a large A-value, many more circles might be required. Test that
     * involve checking theoretical angles with resulting angles of the line endpoints, should use reasonable total angles for
     * both S-shaped and C-shaped clothoids.
     */
    private static final double ANGLE_TOLERANCE = 4 * Math.PI / SEGMENTS;

    /** Allowable distance between resulting and theoretical endpoints of a clothoid. */
    private static final double DISTANCE_TOLERANCE = 1e-2;

    /**
     * Tests whether clothoid between two directed points are correct.
     */
    @Test
    public void testPoints()
    {
        Random r = new Random(3);
        for (int i = 0; i < RUNS; i++)
        {
            DirectedPoint2d start =
                    new DirectedPoint2d(r.nextDouble() * 10.0, r.nextDouble() * 10.0, (r.nextDouble() * 2 - 1) * Math.PI);
            DirectedPoint2d end =
                    new DirectedPoint2d(r.nextDouble() * 10.0, r.nextDouble() * 10.0, (r.nextDouble() * 2 - 1) * Math.PI);
            Clothoid2d clothoid = new Clothoid2d(start, end);
            PolyLine2d line = clothoid.toPolyLine(new Flattener2d.NumSegments(64));
            verifyLine(start, clothoid, line, null, null, null);
            assertTrue(clothoid.getAppliedShape().equals("Arc") || clothoid.getAppliedShape().equals("Clothoid"),
                    "Clothoid identifies itself correctly");
        }
    }

    /**
     * Tests whether clothoid between two directed points on a line, or just not on a line, are correct. This test is separate
     * from {@code TestPoints()} because the random procedure generates very few straight situations.
     */
    @Test
    public void testStraight()
    {
        Random r = new Random(3);
        double tolerance = 2.0 * Math.PI / 3600.0; // see ContinuousClothoid.ANGLE_TOLERANCE
        double startAng = -Math.PI;
        double dAng = Math.PI * 2 / 100;
        double sign = 1.0;
        for (double ang = startAng; ang < Math.PI; ang += dAng)
        {
            double x = Math.cos(ang);
            double y = Math.sin(ang);
            DirectedPoint2d start = new DirectedPoint2d(x, y, ang - tolerance + r.nextDouble() * tolerance * 2);
            DirectedPoint2d end = new DirectedPoint2d(3 * x, 3 * y, ang - tolerance + r.nextDouble() * tolerance * 2);

            Clothoid2d clothoid = new Clothoid2d(start, end);
            NumSegments numSegments64 = new NumSegments(64);
            PolyLine2d line = clothoid.toPolyLine(numSegments64);
            assertEquals(line.size(), 2, "Clothoid between point on line did not become a straight");
            assertTrue(clothoid.getAppliedShape().equals("Straight"), "Clothoid identifies itself correctly");

            start = new DirectedPoint2d(x, y, ang + sign * tolerance * 1.1);
            end = new DirectedPoint2d(3 * x, 3 * y, ang + sign * tolerance * 1.1);
            sign *= -1.0;
            clothoid = new Clothoid2d(start, end);
            line = clothoid.toPolyLine(numSegments64);
            assertTrue(line.size() > 2, "Clothoid between point just not on line should not become a straight");
            assertTrue(clothoid.getAppliedShape().equals("Clothoid"), "Clothoid identifies itself correctly");
        }
    }

    /**
     * Test clothoids created with curvatures and a length.
     */
    @Test
    public void testLength()
    {
        Random r = new Random(3);
        for (int i = 0; i < RUNS; i++)
        {
            DirectedPoint2d start =
                    new DirectedPoint2d(r.nextDouble() * 10.0, r.nextDouble() * 10.0, (r.nextDouble() * 2 - 1) * Math.PI);
            double length = 10.0 + r.nextDouble() * 500.0;
            double sign = r.nextBoolean() ? 1.0 : -1.0;
            double startCurvature = sign / (50.0 + r.nextDouble() * 1000.0);
            sign = r.nextBoolean() ? 1.0 : -1.0;
            double endCurvature = sign / (50.0 + r.nextDouble() * 1000.0);

            Clothoid2d clothoid = Clothoid2d.withLength(start, length, startCurvature, endCurvature);
            PolyLine2d line = clothoid.toPolyLine(new NumSegments(64));
            verifyLine(start, clothoid, line, startCurvature, endCurvature, null);
        }
    }

    /**
     * Test clothoids created with curvatures and an A-value.
     */
    @Test
    public void testA()
    {
        Random r = new Random(3);
        for (int i = 0; i < RUNS; i++)
        {
            DirectedPoint2d start =
                    new DirectedPoint2d(r.nextDouble() * 10.0, r.nextDouble() * 10.0, (r.nextDouble() * 2 - 1) * Math.PI);
            double sign = r.nextBoolean() ? 1.0 : -1.0;
            double startCurvature = sign / (50.0 + r.nextDouble() * 1000.0);
            sign = r.nextBoolean() ? 1.0 : -1.0;
            double endCurvature = sign / (50.0 + r.nextDouble() * 1000.0);
            double a = Math.sqrt((10.0 + r.nextDouble() * 500.0) / Math.abs(endCurvature - startCurvature));

            Clothoid2d clothoid = new Clothoid2d(start, a, startCurvature, endCurvature);
            PolyLine2d line = clothoid.toPolyLine(new NumSegments(64));
            verifyLine(start, clothoid, line, startCurvature, endCurvature, a);
            assertEquals(1 / startCurvature, clothoid.getStartRadius(), 0.001, "Start radius can be retrieved");
            assertEquals(1 / endCurvature, clothoid.getEndRadius(), 0.001, "End radius can be retrieved");
        }
    }

    /**
     * Verifies a line by comparing theoretical and numerical values.
     * @param start theoretical start point.
     * @param clothoid created clothoid.
     * @param line flattened line.
     * @param startCurvature Double; start curvature, may be {@code null} if no theoretical value available.
     * @param endCurvature Double; end curvature, may be {@code null} if no theoretical value available.
     * @param a Double; A-value, may be {@code null} if no theoretical value available.
     */
    private void verifyLine(final DirectedPoint2d start, final Clothoid2d clothoid, final PolyLine2d line,
            final Double startCurvature, final Double endCurvature, final Double a)
    {
        assertEquals(0.0, Math.hypot(start.x - line.get(0).x, start.y - line.get(0).y), DISTANCE_TOLERANCE,
                "Start location deviates");
        assertEquals(0.0, Math.hypot(clothoid.getEndPoint().x - line.get(line.size() - 1).x,
                clothoid.getEndPoint().y - line.get(line.size() - 1).y), DISTANCE_TOLERANCE, "End location deviates");
        assertEquals(0.0, AngleUtil.normalizeAroundZero(start.dirZ - getAngle(line, 0)), ANGLE_TOLERANCE,
                "Start direction deviates");
        assertEquals(0.0, AngleUtil.normalizeAroundZero(clothoid.getEndPoint().dirZ - getAngle(line, line.size() - 2)),
                ANGLE_TOLERANCE, "End direction deviates");
        assertEquals(0.0, start.distance(clothoid.getStartPoint()), DISTANCE_TOLERANCE, "Start location deviates");
        double lengthRatio = line.getLength() / clothoid.getLength();
        assertEquals(1.0, lengthRatio, 0.01, "Length is more than 1% shorter or longer than theoretical");
        if (startCurvature != null)
        {
            double curveatureRatio = clothoid.getStartCurvature() / startCurvature;
            assertEquals(1.0, curveatureRatio, 0.01, "Start curvature is more than 1% shorter or longer than theoretical");
        }
        if (endCurvature != null)
        {
            double curveatureRatio = clothoid.getEndCurvature() / endCurvature;
            assertEquals(1.0, curveatureRatio, 0.01, "End curvature is more than 1% shorter or longer than theoretical");
        }
        if (a != null)
        {
            double aRadius = clothoid.getA() / a;
            assertEquals(1.0, aRadius, 0.01, "A-value is more than 1% less or more than theoretical");
        }
        assertTrue(clothoid.toString().startsWith("Clothoid ["), "toString method returns something descriptive");
    }

    /**
     * Tests that a clothoid offset is on the right side and at the right direction, for clothoids that are reflected or not,
     * and clothoids that are opposite or not.
     */
    @Test
    public void testOffset()
    {
        Flattener2d flattener = new Flattener2d.NumSegments(32);
        OffsetFlattener2d offsetFlattener = new OffsetFlattener2d.NumSegments(32);
        // point A somewhere on y-axis
        for (double yA = -30.0; yA < 35.0; yA += 20.0)
        {
            // point B somewhere on x-axis
            for (double xB = -20.0; xB < 25.0; xB += 20.0 * 2.0 / 3.0)
            {
                // point A pointing left/right towards B
                DirectedPoint2d a = new DirectedPoint2d(0.0, yA, xB < 0.0 ? Math.PI : 0.0);
                // point B pointing up/down away from A
                DirectedPoint2d b = new DirectedPoint2d(xB, 0.0, yA < 0.0 ? Math.PI / 2 : -Math.PI / 2);
                Clothoid2d clothoid = new Clothoid2d(a, b);
                PolyLine2d flattened = clothoid.toPolyLine(flattener);
                assertEquals(a.x, flattened.getX(0), 0.0001, "start x");
                assertEquals(a.y, flattened.getY(0), 0.0001, "start y");
                assertEquals(b.x, flattened.getX(flattened.size() - 1), 0.0001, "end x");
                assertEquals(b.y, flattened.getY(flattened.size() - 1), 0.0001, "end y");
                // offset -2.0 or 2.0
                for (double offset = -2.0; offset < 3.0; offset += 4.0)
                {
                    flattened = clothoid.toPolyLine(offsetFlattener, new PieceWiseLinearOffset2d(0.0, offset, 1.0, offset));
                    Point2d start = flattened.get(0);
                    Point2d end = flattened.get(flattened.size() - 1);
                    assertEquals(0.0, start.x, 0.00001); // offset on y-axis
                    assertEquals(yA + (xB > 0.0 ? offset : -offset), start.y, 0.00001); // offset above or below
                    assertEquals(xB + (yA > 0.0 ? offset : -offset), end.x, 0.00001); // offset left or right
                    assertEquals(0.0, end.y, 0.00001); // offset on x-axis
                }
            }
        }
    }

    /**
     * Return the angle of a segment of a PolyLine2d.
     * @param line PolyLine2d; the PolyLine2d
     * @param segment int; the segment number
     * @return angle of the line segment
     */
    private static double getAngle(final PolyLine2d line, final int segment)
    {
        return Math.atan2(line.get(segment + 1).y - line.get(segment).y, line.get(segment + 1).x - line.get(segment).x);
    }

}
