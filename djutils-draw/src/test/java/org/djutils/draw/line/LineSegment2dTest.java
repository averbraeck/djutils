package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.djutils.draw.Export;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.math.AngleUtil;
import org.junit.jupiter.api.Test;

/**
 * Segment2dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class LineSegment2dTest
{
    /**
     * Test the constructors.
     */
    @Test
    public void constructorTest()
    {
        verifySegment("Segment from four coordinates", new LineSegment2d(1, 2, 3, 4), 1, 2, 3, 4);
        verifySegment("Segment from two coordinates and a Point2d", new LineSegment2d(1, 2, new Point2d(3, 4)), 1, 2, 3, 4);
        verifySegment("Segment from a Point2d and two coordinates", new LineSegment2d(new Point2d(1, 2), 3, 4), 1, 2, 3, 4);
        verifySegment("Segment from two Point2d objects", new LineSegment2d(new Point2d(1, 2), new Point2d(3, 4)), 1, 2, 3, 4);

        try
        {
            new LineSegment2d(1, 2, 1, 2);
            fail("idential start and end should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        new LineSegment2d(1, 2, 1, 3);
        new LineSegment2d(1, 2, 0, 2);
    }

    /**
     * Check that a segment has all the right values.
     * @param description String; description of the test
     * @param segment Segment2d; the segment
     * @param expectedStartX double; the expected x value for the start of the segment
     * @param expectedStartY double; the expected y value for the start of the segment
     * @param expectedEndX double; the expected x value for the end of the segment
     * @param expectedEndY double; the expected y value for the end of the segment
     */
    public void verifySegment(final String description, final LineSegment2d segment, final double expectedStartX,
            final double expectedStartY, final double expectedEndX, final double expectedEndY)
    {
        assertEquals(expectedStartX, segment.startX, 0.0001, description + " startX");
        assertEquals(expectedStartY, segment.startY, 0.0001, description + " startY");
        assertEquals(expectedEndX, segment.endX, 0.0001, description + " endX");
        assertEquals(expectedEndY, segment.endY, 0.0001, description + " endY");
        assertEquals(expectedStartX, segment.getStartPoint().x, 0.0001, description + " getStartPoint x");
        assertEquals(expectedStartY, segment.getStartPoint().y, 0.0001, description + " getStartPoint y");
        assertEquals(expectedEndX, segment.getEndPoint().x, 0.0001, description + " getEndPoint x");
        assertEquals(expectedEndY, segment.getEndPoint().y, 0.0001, description + " getEndPoint y");
        assertEquals(Math.hypot(expectedEndX - expectedStartX, expectedEndY - expectedStartY), segment.getLength(), 0.0001,
                description + " length");
        assertEquals(2, segment.size(), description + " size is 2");
        Iterator<Point2d> iterator = segment.iterator();
        assertTrue(iterator.hasNext(), description + " iterator has data");
        Point2d point = iterator.next();
        assertEquals(expectedStartX, point.x, 0.0001, description + " iterator first point x");
        assertEquals(expectedStartY, point.y, 0.0001, description + " iterator first point y");
        assertTrue(iterator.hasNext(), description + " iterator has more data");
        point = iterator.next();
        assertEquals(expectedEndX, point.x, 0.0001, description + " iterator second point x");
        assertEquals(expectedEndY, point.y, 0.0001, description + " iterator second point y");
        assertFalse(iterator.hasNext(), description + " iterator has no more data");
        Bounds2d bounds = segment.getBounds();
        assertEquals(Math.min(expectedStartX, expectedEndX), bounds.getMinX(), 0.0001, description + " bounds minX");
        assertEquals(Math.max(expectedStartX, expectedEndX), bounds.getMaxX(), 0.0001, description + " bounds maxX");
        assertEquals(Math.min(expectedStartY, expectedEndY), bounds.getMinY(), 0.0001, description + " bounds minY");
        assertEquals(Math.max(expectedStartY, expectedEndY), bounds.getMaxY(), 0.0001, description + " bounds maxY");
        assertTrue(segment.toString().startsWith("LineSegment2d "), description + " toString returns something descriptive");
        assertTrue(segment.toString().indexOf(segment.toString(true)) > 0,
                description + " toString can suppress the class name");
    }

    /**
     * Test the getLocation methods.
     */
    @Test
    public void locationTest()
    {
        Point2d startPoint = new Point2d(3, 4);
        Point2d endPoint = new Point2d(9, 20);
        LineSegment2d segment = new LineSegment2d(startPoint, endPoint);
        try
        {
            segment.getLocation(Double.NaN);
            fail("NaN position should have thrown an ArithmeticException");
        }
        catch (ArithmeticException ae)
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
                DirectedPoint2d dp = segment.getLocation(position);
                assertEquals(position, dp.distance(startPoint), 0.0001, "distance from start point");
                assertEquals(segment.getLength() - position, dp.distance(endPoint), 0.0001, "distance from end point");
                assertEquals(startPoint.directionTo(endPoint), dp.dirZ, 0.0001, "direction of ray");
            }
            DirectedPoint2d dp = segment.getLocationExtended(position);
            assertEquals(Math.abs(position), dp.distance(startPoint), 0.0001, "distance from start point");
            assertEquals(Math.abs(segment.getLength() - position), dp.distance(endPoint), 0.0001, "distance from end point");
            assertEquals(startPoint.directionTo(endPoint), dp.dirZ, 0.0001, "direction of ray");
        }
    }

    /**
     * Test the closestPointOnSegment method.
     */
    @Test
    public void closestPointOnSegmentTest()
    {
        LineSegment2d segment = new LineSegment2d(1, 2, 20, 10);
        try
        {
            segment.closestPointOnSegment(null);
            fail("Null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        Point2d result = segment.closestPointOnSegment(new Point2d(1, 0));
        assertEquals(segment.startX, result.x, 0, "result is start point");
        assertEquals(segment.startY, result.y, 0, "result is start point");
        result = segment.closestPointOnSegment(new Point2d(0, 2));
        assertEquals(segment.startX, result.x, 0, "result is start point");
        assertEquals(segment.startY, result.y, 0, "result is start point");
        result = segment.closestPointOnSegment(new Point2d(1, 2));
        assertEquals(segment.startX, result.x, 0, "result is start point");
        assertEquals(segment.startY, result.y, 0, "result is start point");

        Point2d projectingPoint = new Point2d(10, 10);
        result = segment.closestPointOnSegment(projectingPoint); // Projects at a point along the segment
        double distanceFromStart = result.distance(segment.getStartPoint());
        assertTrue(distanceFromStart > 0, "distance from start is > 0");
        double distanceToEnd = result.distance(segment.getEndPoint());
        assertTrue(distanceToEnd > 0, "distance to end point is > 0");
        assertEquals(segment.getLength(), distanceFromStart + distanceToEnd, 0.0001, "sum of distances is length of segment");
        // Angle startPoint-result-test-projectingPoint should be 90 degrees
        double angle = segment.getStartPoint().directionTo(segment.getEndPoint()) - result.directionTo(projectingPoint);
        assertEquals(Math.PI / 2, Math.abs(AngleUtil.normalizeAroundZero(angle)), 0.0001, "angle should be about 90 degrees");

        result = segment.closestPointOnSegment(new Point2d(21, 10));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
        result = segment.closestPointOnSegment(new Point2d(20, 11));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
        result = segment.closestPointOnSegment(new Point2d(20, 10));
        assertEquals(segment.endX, result.x, 0, "result is end point");
        assertEquals(segment.endY, result.y, 0, "result is end point");
    }

    /**
     * Test the reverse operation.
     */
    @Test
    public void testReverse()
    {
        LineSegment2d segment = new LineSegment2d(1, 2, 20, 10);
        LineSegment2d reversed = segment.reverse();
        verifySegment("reversed", reversed, 20, 10, 1, 2);
        assertEquals(segment, reversed.reverse(), "reversed reversed equals original");
    }

    /**
     * Test the project methods.
     */
    @Test
    public void testProject()
    {
        LineSegment2d segment = new LineSegment2d(1, 2, 20, 10);
        assertTrue(Double.isNaN(segment.projectOrthogonalFractional(new Point2d(1, 1))), "projects outside");
        assertTrue(segment.projectOrthogonalFractionalExtended(new Point2d(1, 1)) < 0, "projects before start");
        assertEquals(-2, segment.projectOrthogonalFractionalExtended(new Point2d(1 - 19 - 19 + 8, 2 - 8 - 8 - 19)), 0.0001,
                "projects at -2");
        assertEquals(0.5, segment.projectOrthogonalFractional(new Point2d(11, 1)), 0.1,
                "point near half way (not on segment) project at about half way");
        assertTrue(Double.isNaN(segment.projectOrthogonalFractional(new Point2d(25, 15))), "projects outside");
        assertTrue(segment.projectOrthogonalFractionalExtended(new Point2d(25, 15)) > 1, "projects after end");
        assertEquals(2, segment.projectOrthogonalFractionalExtended(new Point2d(1 + 19 + 19 - 8, 2 + 8 + 8 + 19)), 0.0001,
                "projects at 2");
    }

    /**
     * Test the toExcel method.
     * @throws NumberFormatException if that happens, this test has failed
     */
    @Test
    public void testExports() throws NumberFormatException
    {
        LineSegment2d segment = new LineSegment2d(1, 2, 20, 10);
        String result = Export.toTsv(segment);
        String[] lines = result.split("\n");
        assertEquals(2, lines.length, "result is two lines");
        for (int lineNo = 0; lineNo < lines.length; lineNo++)
        {
            String[] fields = lines[lineNo].trim().split("\t");
            assertEquals(2, fields.length, "Line consists of two fields");
            for (int fieldNo = 0; fieldNo < fields.length; fieldNo++)
            {
                double value = Double.parseDouble(fields[fieldNo]);
                double expectedValue = lineNo == 0 ? (fieldNo == 0 ? segment.startX : segment.startY)
                        : (fieldNo == 0 ? segment.endX : segment.endY);
                assertEquals(expectedValue, value, 0.0001, "field contains the correct value");
            }
        }
        result = Export.toPlot(segment);
        assertEquals(1, result.split("\n").length, "result is one line");
        int valuesSeen = 0;
        int pos = 0;
        while (pos < result.length())
        {
            if (valuesSeen % 2 == 0)
            {
                assertEquals(valuesSeen == 0 ? "M" : "L", result.substring(pos, pos + 1), "command is M");
            }
            else
            {
                assertEquals(",", result.substring(pos, pos + 1), "coordinates are separated by a comma");
            }
            pos++;
            int endPos = pos;
            while (endPos < result.length())
            {
                if ("0123456789.".indexOf(result.substring(endPos, endPos + 1)) >= 0)
                {
                    endPos++;
                }
                else
                {
                    break;
                }
            }
            double value = Double.NaN;
            double expectedValue = valuesSeen == 0 ? segment.startX
                    : valuesSeen == 1 ? segment.startY : valuesSeen == 2 ? segment.endX : segment.endY;
            try
            {
                value = Double.parseDouble(result.substring(pos, endPos));
                assertEquals(expectedValue, value, 0, "value matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("sub string should have been a parsable double value");
            }
            valuesSeen++;
            pos = endPos;
            if (valuesSeen == 4)
            {
                assertEquals("\n", result.substring(pos), "line terminator at end");
                pos++;
            }
        }
    }

    /**
     * Test the equals and hasCode methods.
     */
    @Test
    public void equalsAndHashCodeTest()
    {
        LineSegment2d segment = new LineSegment2d(1, 2, -3, -4);
        assertEquals(segment, segment, "equal to itself");
        assertNotEquals(segment, null, "not equal to null");
        assertNotEquals(segment, "no way", "not equal to a totally different object");
        assertNotEquals(segment, new LineSegment2d(2, 2, -3, -4), "not equal to line segment with different start x");
        assertNotEquals(segment, new LineSegment2d(1, 3, -3, -4), "not equal to line segment with different start y");
        assertNotEquals(segment, new LineSegment2d(1, 2, -4, -4), "not equal to line segment with different end x");
        assertNotEquals(segment, new LineSegment2d(1, 2, -3, -5), "not equal to line segment with different end y");
        assertEquals(segment, new LineSegment2d(1, 2, -3, -4), "equal to another line segment with same start and end x, y");

        assertNotEquals(segment.hashCode(), new LineSegment2d(2, 2, -3, -4), "hashCode depends on start x");
        assertNotEquals(segment.hashCode(), new LineSegment2d(1, 3, -3, -4), "hashCode depends on start y");
        assertNotEquals(segment.hashCode(), new LineSegment2d(1, 2, -4, -4), "hashCode depends on end x");
        assertNotEquals(segment.hashCode(), new LineSegment2d(1, 2, -4, -5), "hashCode depends on end y");
    }

}
