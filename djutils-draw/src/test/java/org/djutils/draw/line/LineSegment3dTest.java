package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * Segment3dTest.java.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
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
            fail("idential start and end should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        new LineSegment3d(1, 2, 3, 1, 2, 4);
        new LineSegment3d(1, 2, 3, 1, 3, 3);
        new LineSegment3d(1, 2, 3, 2, 2, 3);

    }

    /**
     * Check that a segment has all the right values.
     * @param description String; description of the test
     * @param segment Segment3d; the segment
     * @param expectedStartX double; the expected x value for the start of the segment
     * @param expectedStartY double; the expected y value for the start of the segment
     * @param expectedStartZ double; the expected z value for the start of the segment
     * @param expectedEndX double; the expected x value for the end of the segment
     * @param expectedEndY double; the expected y value for the end of the segment
     * @param expectedEndZ double; the expected y value for the end of the segment
     */
    public void verifySegment(final String description, final LineSegment3d segment, final double expectedStartX,
            final double expectedStartY, final double expectedStartZ, final double expectedEndX, final double expectedEndY,
            final double expectedEndZ)
    {
        assertEquals(description + " startX", expectedStartX, segment.startX, 0.0001);
        assertEquals(description + " startY", expectedStartY, segment.startY, 0.0001);
        assertEquals(description + " startZ", expectedStartZ, segment.startZ, 0.0001);
        assertEquals(description + " toX", expectedEndX, segment.endX, 0.0001);
        assertEquals(description + " endY", expectedEndY, segment.endY, 0.0001);
        assertEquals(description + " endZ", expectedEndZ, segment.endZ, 0.0001);
        assertEquals(description + " getStartPoint x", expectedStartX, segment.getStartPoint().x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedStartY, segment.getStartPoint().y, 0.0001);
        assertEquals(description + " getStartPoint z", expectedStartZ, segment.getStartPoint().z, 0.0001);
        assertEquals(description + " getEndPoint x", expectedEndX, segment.getEndPoint().x, 0.0001);
        assertEquals(description + " getEndPoint y", expectedEndY, segment.getEndPoint().y, 0.0001);
        assertEquals(description + " getEndPoint z", expectedEndZ, segment.getEndPoint().z, 0.0001);
        assertEquals(description + " length", Math
                .hypot(Math.hypot(expectedEndX - expectedStartX, expectedEndY - expectedStartY), expectedEndZ - expectedStartZ),
                segment.getLength(), 0.0001);
        assertEquals(description + " size is 2", 2, segment.size());
        Iterator<? extends Point3d> iterator = segment.getPoints();
        assertTrue(description + " iterator has data", iterator.hasNext());
        Point3d point = iterator.next();
        assertEquals(description + " iterator first point x", expectedStartX, point.x, 0.0001);
        assertEquals(description + " iterator first point y", expectedStartY, point.y, 0.0001);
        assertEquals(description + " iterator first point z", expectedStartZ, point.z, 0.0001);
        assertTrue(description + " iterator has more data", iterator.hasNext());
        point = iterator.next();
        assertEquals(description + " iterator second point x", expectedEndX, point.x, 0.0001);
        assertEquals(description + " iterator second point y", expectedEndY, point.y, 0.0001);
        assertEquals(description + " iterator second point z", expectedEndZ, point.z, 0.0001);
        assertFalse(description + " iterator has no more data", iterator.hasNext());
        Bounds3d bounds = segment.getBounds();
        assertEquals(description + " bounds minX", Math.min(expectedStartX, expectedEndX), bounds.getMinX(), 0.0001);
        assertEquals(description + " bounds maxX", Math.max(expectedStartX, expectedEndX), bounds.getMaxX(), 0.0001);
        assertEquals(description + " bounds minY", Math.min(expectedStartY, expectedEndY), bounds.getMinY(), 0.0001);
        assertEquals(description + " bounds maxY", Math.max(expectedStartY, expectedEndY), bounds.getMaxY(), 0.0001);
        assertEquals(description + " bounds minZ", Math.min(expectedStartZ, expectedEndZ), bounds.getMinZ(), 0.0001);
        assertEquals(description + " bounds maxZ", Math.max(expectedStartZ, expectedEndZ), bounds.getMaxZ(), 0.0001);
        assertTrue(description + " toString returns something descriptive", segment.toString().startsWith("LineSegment3d "));
        assertTrue(description + " toString can suppress the class name",
                segment.toString().indexOf(segment.toString(true)) > 0);
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
            fail("NaN position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            segment.getLocationExtended(Double.POSITIVE_INFINITY);
            fail("Infinity position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            segment.getLocationExtended(Double.NEGATIVE_INFINITY);
            fail("Infinity position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
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
                    fail("position out of bounds should have thrown a DrawRuntimeException");
                }
                catch (DrawRuntimeException dre)
                {
                    // Ignore expected exception
                }
            }
            else
            {
                Ray3d ray = segment.getLocation(position);
                assertEquals("distance from start point", position, ray.distance(startPoint), 0.0001);
                assertEquals("distance from end point", segment.getLength() - position, ray.distance(endPoint), 0.0001);
                assertEquals("direction of ray phi", startPoint.project().directionTo(endPoint.project()), ray.phi, 0.0001);
                assertEquals("direction of ray theta", Math.atan2(endPoint.z - startPoint.z, segment.project().getLength()),
                        ray.theta, 0.0001);
            }
            Ray3d ray = segment.getLocationExtended(position);
            assertEquals("distance from start point", Math.abs(position), ray.distance(startPoint), 0.0001);
            assertEquals("distance from end point", Math.abs(segment.getLength() - position), ray.distance(endPoint), 0.0001);
            assertEquals("direction of ray phi", startPoint.project().directionTo(endPoint.project()), ray.phi, 0.0001);
            assertEquals("direction of ray theta", Math.atan2(endPoint.z - startPoint.z, segment.project().getLength()),
                    ray.theta, 0.0001);
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
        assertEquals("result is start point x", segment.startX, result.x, 0);
        assertEquals("result is start point y", segment.startY, result.y, 0);
        assertEquals("result is start point z", segment.startZ, result.z, 0);
        result = segment.closestPointOnSegment(new Point3d(1, 0, 3));
        assertEquals("result is start point x", segment.startX, result.x, 0);
        assertEquals("result is start point y", segment.startY, result.y, 0);
        assertEquals("result is start point z", segment.startZ, result.z, 0);
        result = segment.closestPointOnSegment(new Point3d(0, 2, 3));
        assertEquals("result is start point x", segment.startX, result.x, 0);
        assertEquals("result is start point y", segment.startY, result.y, 0);
        assertEquals("result is start point z", segment.startZ, result.z, 0);
        result = segment.closestPointOnSegment(new Point3d(1, 2, 3));
        assertEquals("result is start point x", segment.startX, result.x, 0);
        assertEquals("result is start point y", segment.startY, result.y, 0);
        assertEquals("result is start point z", segment.startZ, result.z, 0);

        Point3d projectingPoint = new Point3d(10, 10, 10);
        result = segment.closestPointOnSegment(projectingPoint); // Projects at a point along the segment
        double distanceFromStart = result.distance(segment.getStartPoint());
        assertTrue("distance from start is > 0", distanceFromStart > 0);
        double distanceToEnd = result.distance(segment.getEndPoint());
        // System.out.println(segment + " projectingPoint=" + projectingPoint + ", result=" + result);
        assertTrue("distance to end point is > 0", distanceToEnd > 0);
        assertEquals("sum of distances is length of segment", segment.getLength(), distanceFromStart + distanceToEnd, 0.0001);

        Point3d doubleProjected = segment.closestPointOnSegment(result);
        assertEquals("projecting the projection yields the projection", 0, doubleProjected.distance(result), 0.0001);

        result = segment.closestPointOnSegment(new Point3d(21, 10, 15));
        assertEquals("result is end point", segment.endX, result.x, 0);
        assertEquals("result is end point", segment.endY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(20, 11, 15));
        assertEquals("result is end point", segment.endX, result.x, 0);
        assertEquals("result is end point", segment.endY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(20, 10, 16));
        assertEquals("result is end point", segment.endX, result.x, 0);
        assertEquals("result is end point", segment.endY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(20, 10, 15));
        assertEquals("result is end point", segment.endX, result.x, 0);
        assertEquals("result is end point", segment.endY, result.y, 0);
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
        assertEquals("reversed reversed equals original", segment, reversed.reverse());
    }
    
    /**
     * Test the project methods.
     */
    @Test
    public void testProject()
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, 20, 10, 5);
        assertTrue("projects outside", Double.isNaN(segment.projectOrthogonalFractional(new Point3d(1, 1, 1))));
        assertTrue("projects before start", segment.projectOrthogonalFractionalExtended(new Point3d(1, 1, 1)) < 0);
        assertEquals("projects at -2", -2,
                segment.projectOrthogonalFractionalExtended(new Point3d(1 - 19 - 19 + 8, 2 - 8 - 8 - 19, 3 - 2 - 2)), 0.0001);
        assertEquals("point near half way (not on segment) project at about half way", 0.5,
                segment.projectOrthogonalFractional(new Point3d(11, 1, 4)), 0.1);
        assertTrue("projects outside", Double.isNaN(segment.projectOrthogonalFractional(new Point3d(25, 15, 6))));
        assertTrue("projects after end", segment.projectOrthogonalFractionalExtended(new Point3d(25, 15, 6)) > 1);
        assertEquals("projects at 2", 2,
                segment.projectOrthogonalFractionalExtended(new Point3d(1 + 19 + 19 - 8, 2 + 8 + 8 + 19, 3 + 2 + 2)), 0.0001);
    }

    /**
     * Test the toExcel method.
     * @throws NumberFormatException if that happens, this test has failed
     */
    @Test
    public void testToExcel() throws NumberFormatException
    {
        LineSegment3d segment = new LineSegment3d(1, 2, 3, 20, 10, 5);
        String result = segment.toExcel();
        String[] lines = result.split("\n");
        assertEquals("result is two lines", 2, lines.length);
        for (int lineNo = 0; lineNo < lines.length; lineNo++)
        {
            String[] fields = lines[lineNo].trim().split("\t");
            assertEquals("Line consists of three fields", 3, fields.length);
            for (int fieldNo = 0; fieldNo < fields.length; fieldNo++)
            {
                double value = Double.parseDouble(fields[fieldNo]);
                double expectedValue =
                        lineNo == 0 ? (fieldNo == 0 ? segment.startX : fieldNo == 1 ? segment.startY : segment.startZ)
                                : (fieldNo == 0 ? segment.endX : fieldNo == 1 ? segment.endY : segment.endZ);
                assertEquals("field contains the correct value", expectedValue, value, 0.0001);
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
        assertEquals("equal to itself", segment, segment);
        assertNotEquals("not equal to null", segment, null);
        assertNotEquals("not equal to a totally different object", segment, "no way");
        assertNotEquals("not equal to line segment with different start x", segment, new LineSegment3d(2, 2, 3, -3, -4, -5));
        assertNotEquals("not equal to line segment with different start y", segment, new LineSegment3d(1, 3, 3, -3, -4, -5));
        assertNotEquals("not equal to line segment with different start z", segment, new LineSegment3d(1, 2, 4, -3, -4, -5));
        assertNotEquals("not equal to line segment with different end x", segment, new LineSegment3d(1, 2, 3, -4, -4, -5));
        assertNotEquals("not equal to line segment with different end y", segment, new LineSegment3d(1, 2, 3, -3, -5, -5));
        assertNotEquals("not equal to line segment with different end y", segment, new LineSegment3d(1, 2, 3, -3, -4, -6));
        assertEquals("equal to another line segment with same start and end x, y, z", segment,
                new LineSegment3d(1, 2, 3, -3, -4, -5));

        assertNotEquals("hashCode depends on start x", segment.hashCode(), new LineSegment3d(2, 2, 3, -3, -4, -5));
        assertNotEquals("hashCode depends on start y", segment.hashCode(), new LineSegment3d(1, 3, 3, -3, -4, -5));
        assertNotEquals("hashCode depends on start z", segment.hashCode(), new LineSegment3d(1, 3, 4, -3, -4, -5));
        assertNotEquals("hashCode depends on end x", segment.hashCode(), new LineSegment3d(1, 2, 3, -4, -4, -5));
        assertNotEquals("hashCode depends on end y", segment.hashCode(), new LineSegment3d(1, 2, 3, -4, -5, -5));
        assertNotEquals("hashCode depends on end z", segment.hashCode(), new LineSegment3d(1, 2, 3, -4, -5, -6));
    }

}
