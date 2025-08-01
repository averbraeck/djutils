package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.djutils.draw.Export;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;
import org.junit.jupiter.api.Test;

/**
 * Segment3dTest.java.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class LineSegment3dTest
{
    /**
     * Test the constructors.
     */
    @Test
    public void constructorTest()
    {
        verifySegment("Segment from four coordinates", new LineSegment3d(1, 2, 3, 4, 5, 6), 1, 2, 3, 4, 5, 6);
        verifySegment("Segment from two coordinates and a Point3d", new LineSegment3d(1, 2, 3, new Point3d(4, 5, 6)), 1, 2, 3,
                4, 5, 6);
        verifySegment("Segment from a Point3d and two coordinates", new LineSegment3d(new Point3d(1, 2, 3), 4, 5, 6), 1, 2, 3,
                4, 5, 6);
        verifySegment("Segment from two Point3d objects", new LineSegment3d(new Point3d(1, 2, 3), new Point3d(4, 5, 6)), 1, 2,
                3, 4, 5, 6);

        try
        {
            new LineSegment3d(1, 2, 3, 1, 2, 3);
            fail("idential start and end should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        new LineSegment3d(1, 2, 3, 1, 2, 4);
        new LineSegment3d(1, 2, 3, 1, 3, 3);
        new LineSegment3d(1, 2, 3, 2, 2, 3);

    }

    /**
     * Check that a segment has all the right values.
     * @param description description of the test
     * @param segment the segment
     * @param expectedStartX the expected x value for the start of the segment
     * @param expectedStartY the expected y value for the start of the segment
     * @param expectedStartZ the expected z value for the start of the segment
     * @param expectedEndX the expected x value for the end of the segment
     * @param expectedEndY the expected y value for the end of the segment
     * @param expectedEndZ the expected y value for the end of the segment
     */
    public void verifySegment(final String description, final LineSegment3d segment, final double expectedStartX,
            final double expectedStartY, final double expectedStartZ, final double expectedEndX, final double expectedEndY,
            final double expectedEndZ)
    {
        assertEquals(expectedStartX, segment.startX, 0.0001, description + " startX");
        assertEquals(expectedStartY, segment.startY, 0.0001, description + " startY");
        assertEquals(expectedStartZ, segment.startZ, 0.0001, description + " startZ");
        assertEquals(expectedEndX, segment.endX, 0.0001, description + " toX");
        assertEquals(expectedEndY, segment.endY, 0.0001, description + " endY");
        assertEquals(expectedEndZ, segment.endZ, 0.0001, description + " endZ");
        assertEquals(expectedStartX, segment.getStartPoint().x, 0.0001, description + " getStartPoint x");
        assertEquals(expectedStartY, segment.getStartPoint().y, 0.0001, description + " getStartPoint y");
        assertEquals(expectedStartZ, segment.getStartPoint().z, 0.0001, description + " getStartPoint z");
        assertEquals(expectedEndX, segment.getEndPoint().x, 0.0001, description + " getEndPoint x");
        assertEquals(expectedEndY, segment.getEndPoint().y, 0.0001, description + " getEndPoint y");
        assertEquals(expectedEndZ, segment.getEndPoint().z, 0.0001, description + " getEndPoint z");
        assertEquals(Math.hypot(Math.hypot(expectedEndX - expectedStartX, expectedEndY - expectedStartY),
                expectedEndZ - expectedStartZ), segment.getLength(), 0.0001, description + " length");
        assertEquals(2, segment.size(), description + " size is 2");
        Iterator<Point3d> iterator = segment.iterator();
        assertTrue(iterator.hasNext(), description + " iterator has data");
        Point3d point = iterator.next();
        assertEquals(expectedStartX, point.x, 0.0001, description + " iterator first point x");
        assertEquals(expectedStartY, point.y, 0.0001, description + " iterator first point y");
        assertEquals(expectedStartZ, point.z, 0.0001, description + " iterator first point z");
        assertTrue(iterator.hasNext(), description + " iterator has more data");
        point = iterator.next();
        assertEquals(expectedEndX, point.x, 0.0001, description + " iterator second point x");
        assertEquals(expectedEndY, point.y, 0.0001, description + " iterator second point y");
        assertEquals(expectedEndZ, point.z, 0.0001, description + " iterator second point z");
        assertFalse(iterator.hasNext(), description + " iterator has no more data");
        Bounds3d bounds = segment.getAbsoluteBounds();
        assertEquals(Math.min(expectedStartX, expectedEndX), bounds.getMinX(), 0.0001, description + " bounds minX");
        assertEquals(Math.max(expectedStartX, expectedEndX), bounds.getMaxX(), 0.0001, description + " bounds maxX");
        assertEquals(Math.min(expectedStartY, expectedEndY), bounds.getMinY(), 0.0001, description + " bounds minY");
        assertEquals(Math.max(expectedStartY, expectedEndY), bounds.getMaxY(), 0.0001, description + " bounds maxY");
        assertEquals(Math.min(expectedStartZ, expectedEndZ), bounds.getMinZ(), 0.0001, description + " bounds minZ");
        assertEquals(Math.max(expectedStartZ, expectedEndZ), bounds.getMaxZ(), 0.0001, description + " bounds maxZ");
        assertTrue(segment.toString().startsWith("LineSegment3d "), description + " toString returns something descriptive");
        assertTrue(segment.toString().indexOf(segment.toString(true)) > 0,
                description + " toString can suppress the class name");
    }

    /**
     * Test the getLocation methods.
     */
    @Test
    public void locationTest()
    {
        Point3d startPoint = new Point3d(3, 4, 5);
        Point3d endPoint = new Point3d(9, 20, 15);
        LineSegment3d segment = new LineSegment3d(startPoint, endPoint);
        try
        {
            segment.getLocation(Double.NaN);
            fail("NaN position should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            segment.getLocationExtended(Double.POSITIVE_INFINITY);
            fail("Infinity position should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            segment.getLocationExtended(Double.NEGATIVE_INFINITY);
            fail("Infinity position should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        for (double position : new double[] {-3, -0.5, 0, 1, 10, 100})
        {
            if (position < 0 || position > segment.getLength())
            {
                try
                {
                    segment.getLocation(position);
                    fail("position out of bounds should have thrown a IllegalArgumentException");
                }
                catch (IllegalArgumentException e)
                {
                    // Ignore expected exception
                }
            }
            else
            {
                DirectedPoint3d dp = segment.getLocation(position);
                assertEquals(position, dp.distance(startPoint), 0.0001, "distance from start point");
                assertEquals(segment.getLength() - position, dp.distance(endPoint), 0.0001, "distance from end point");
                assertEquals(startPoint.project().directionTo(endPoint.project()), dp.dirZ, 0.0001,
                        "direction of directedPoint dirZ");
                assertEquals(Math.atan2(endPoint.z - startPoint.z, segment.project().getLength()), dp.dirY, 0.0001,
                        "direction of directedPoint dirY");
            }
            DirectedPoint3d dp = segment.getLocationExtended(position);
            assertEquals(Math.abs(position), dp.distance(startPoint), 0.0001, "distance from start point");
            assertEquals(Math.abs(segment.getLength() - position), dp.distance(endPoint), 0.0001, "distance from end point");
            assertEquals(startPoint.project().directionTo(endPoint.project()), dp.dirZ, 0.0001, "direction of ray dirZ");
            assertEquals(Math.atan2(endPoint.z - startPoint.z, segment.project().getLength()), dp.dirY, 0.0001,
                    "direction of ray dirY");
        }
    }

    /**
     * Test the closestPointOnSegment method.
     */
    @Test
    public void closestPointOnSegmentTest()
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, 20, 10, 15);
        try
        {
            segment.closestPointOnSegment(null);
            fail("Null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        Point3d result = segment.closestPointOnSegment(new Point3d(1, 2, 0));
        assertEquals(segment.startX, result.x, 0, "result is start point x");
        assertEquals(segment.startY, result.y, 0, "result is start point y");
        assertEquals(segment.startZ, result.z, 0, "result is start point z");
        result = segment.closestPointOnSegment(new Point3d(1, 0, 3));
        assertEquals(segment.startX, result.x, 0, "result is start point x");
        assertEquals(segment.startY, result.y, 0, "result is start point y");
        assertEquals(segment.startZ, result.z, 0, "result is start point z");
        result = segment.closestPointOnSegment(new Point3d(0, 2, 3));
        assertEquals(segment.startX, result.x, 0, "result is start point x");
        assertEquals(segment.startY, result.y, 0, "result is start point y");
        assertEquals(segment.startZ, result.z, 0, "result is start point z");
        result = segment.closestPointOnSegment(new Point3d(1, 2, 3));
        assertEquals(segment.startX, result.x, 0, "result is start point x");
        assertEquals(segment.startY, result.y, 0, "result is start point y");
        assertEquals(segment.startZ, result.z, 0, "result is start point z");

        Point3d projectingPoint = new Point3d(10, 10, 10);
        result = segment.closestPointOnSegment(projectingPoint); // Projects at a point along the segment
        double distanceFromStart = result.distance(segment.getStartPoint());
        assertTrue(distanceFromStart > 0, "distance from start is > 0");
        double distanceToEnd = result.distance(segment.getEndPoint());
        // System.out.println(segment + " projectingPoint=" + projectingPoint + ", result=" + result);
        assertTrue(distanceToEnd > 0, "distance to end point is > 0");
        assertEquals(segment.getLength(), distanceFromStart + distanceToEnd, 0.0001, "sum of distances is length of segment");

        Point3d doubleProjected = segment.closestPointOnSegment(result);
        assertEquals(0, doubleProjected.distance(result), 0.0001, "projecting the projection yields the projection");

        result = segment.closestPointOnSegment(new Point3d(21, 10, 15));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
        result = segment.closestPointOnSegment(new Point3d(20, 11, 15));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
        result = segment.closestPointOnSegment(new Point3d(20, 10, 16));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
        result = segment.closestPointOnSegment(new Point3d(20, 10, 15));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
    }

    /**
     * Test the reverse operation.
     */
    @Test
    public void testReverse()
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, 20, 10, 5);
        LineSegment3d reversed = segment.reverse();
        verifySegment("reversed", reversed, 20, 10, 5, 1, 2, 3);
        assertEquals(segment, reversed.reverse(), "reversed reversed equals original");
    }

    /**
     * Test the project methods.
     */
    @Test
    public void testProject()
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, 20, 10, 5);
        assertTrue(Double.isNaN(segment.projectOrthogonalFractional(new Point3d(1, 1, 1))), "projects outside");
        assertTrue(segment.projectOrthogonalFractionalExtended(new Point3d(1, 1, 1)) < 0, "projects before start");
        assertEquals(-2, segment.projectOrthogonalFractionalExtended(new Point3d(1 - 19 - 19 + 8, 2 - 8 - 8 - 19, 3 - 2 - 2)),
                0.0001, "projects at -2");
        assertEquals(0.5, segment.projectOrthogonalFractional(new Point3d(11, 1, 4)), 0.1,
                "point near half way (not on segment) project at about half way");
        assertTrue(Double.isNaN(segment.projectOrthogonalFractional(new Point3d(25, 15, 6))), "projects outside");
        assertTrue(segment.projectOrthogonalFractionalExtended(new Point3d(25, 15, 6)) > 1, "projects after end");
        assertEquals(2, segment.projectOrthogonalFractionalExtended(new Point3d(1 + 19 + 19 - 8, 2 + 8 + 8 + 19, 3 + 2 + 2)),
                0.0001, "projects at 2");
    }

    /**
     * Test the toExcel method.
     * @throws NumberFormatException if that happens, this test has failed
     */
    @Test
    public void testToExcel() throws NumberFormatException
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, 20, 10, 5);
        String result = Export.toTsv(segment);
        String[] lines = result.split("\n");
        assertEquals(2, lines.length, "result is two lines");
        for (int lineNo = 0; lineNo < lines.length; lineNo++)
        {
            String[] fields = lines[lineNo].trim().split("\t");
            assertEquals(3, fields.length, "Line consists of three fields");
            for (int fieldNo = 0; fieldNo < fields.length; fieldNo++)
            {
                double value = Double.parseDouble(fields[fieldNo]);
                double expectedValue =
                        lineNo == 0 ? (fieldNo == 0 ? segment.startX : fieldNo == 1 ? segment.startY : segment.startZ)
                                : (fieldNo == 0 ? segment.endX : fieldNo == 1 ? segment.endY : segment.endZ);
                assertEquals(expectedValue, value, 0.0001, "field contains the correct value");
            }
        }
    }

    /**
     * Test the equals and hasCode methods.
     */
    @Test
    public void equalsAndHashCodeTest()
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, -3, -4, -5);
        assertEquals(segment, segment, "equal to itself");
        assertNotEquals(segment, null, "not equal to null");
        assertNotEquals(segment, "no way", "not equal to a totally different object");
        assertNotEquals(segment, new LineSegment3d(2, 2, 3, -3, -4, -5), "not equal to line segment with different start x");
        assertNotEquals(segment, new LineSegment3d(1, 3, 3, -3, -4, -5), "not equal to line segment with different start y");
        assertNotEquals(segment, new LineSegment3d(1, 2, 4, -3, -4, -5), "not equal to line segment with different start z");
        assertNotEquals(segment, new LineSegment3d(1, 2, 3, -4, -4, -5), "not equal to line segment with different end x");
        assertNotEquals(segment, new LineSegment3d(1, 2, 3, -3, -5, -5), "not equal to line segment with different end y");
        assertNotEquals(segment, new LineSegment3d(1, 2, 3, -3, -4, -6), "not equal to line segment with different end y");
        assertEquals(segment, new LineSegment3d(1, 2, 3, -3, -4, -5),
                "equal to another line segment with same start and end x, y, z");

        assertNotEquals(segment.hashCode(), new LineSegment3d(2, 2, 3, -3, -4, -5), "hashCode depends on start x");
        assertNotEquals(segment.hashCode(), new LineSegment3d(1, 3, 3, -3, -4, -5), "hashCode depends on start y");
        assertNotEquals(segment.hashCode(), new LineSegment3d(1, 3, 4, -3, -4, -5), "hashCode depends on start z");
        assertNotEquals(segment.hashCode(), new LineSegment3d(1, 2, 3, -4, -4, -5), "hashCode depends on end x");
        assertNotEquals(segment.hashCode(), new LineSegment3d(1, 2, 3, -4, -5, -5), "hashCode depends on end y");
        assertNotEquals(segment.hashCode(), new LineSegment3d(1, 2, 3, -4, -5, -6), "hashCode depends on end z");
    }

}
