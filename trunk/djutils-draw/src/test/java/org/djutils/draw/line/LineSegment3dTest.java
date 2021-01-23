package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
     * @param expectedFromX double; the expected x value for the start of the segment
     * @param expectedFromY double; the expected y value for the start of the segment
     * @param expectedFromZ double; the expected z value for the start of the segment
     * @param expectedToX double; the expected x value for the end of the segment
     * @param expectedToY double; the expected y value for the end of the segment
     * @param expectedToZ double; the expected y value for the end of the segment
     */
    public void verifySegment(final String description, final LineSegment3d segment, final double expectedFromX,
            final double expectedFromY, final double expectedFromZ, final double expectedToX, final double expectedToY,
            final double expectedToZ)
    {
        assertEquals(description + " fromX", expectedFromX, segment.fromX, 0.0001);
        assertEquals(description + " fromY", expectedFromY, segment.fromY, 0.0001);
        assertEquals(description + " fromZ", expectedFromZ, segment.fromZ, 0.0001);
        assertEquals(description + " toX", expectedToX, segment.toX, 0.0001);
        assertEquals(description + " toY", expectedToY, segment.toY, 0.0001);
        assertEquals(description + " toZ", expectedToZ, segment.toZ, 0.0001);
        assertEquals(description + " getStartPoint x", expectedFromX, segment.getStartPoint().x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedFromY, segment.getStartPoint().y, 0.0001);
        assertEquals(description + " getStartPoint z", expectedFromZ, segment.getStartPoint().z, 0.0001);
        assertEquals(description + " getEndPoint x", expectedToX, segment.getEndPoint().x, 0.0001);
        assertEquals(description + " getEndPoint y", expectedToY, segment.getEndPoint().y, 0.0001);
        assertEquals(description + " getEndPoint z", expectedToZ, segment.getEndPoint().z, 0.0001);
        assertEquals(description + " length",
                Math.hypot(Math.hypot(expectedToX - expectedFromX, expectedToY - expectedFromY), expectedToZ - expectedFromZ),
                segment.getLength(), 0.0001);
        assertEquals(description + " size is 2", 2, segment.size());
        Iterator<? extends Point3d> iterator = segment.getPoints();
        assertTrue(description + " iterator has data", iterator.hasNext());
        Point3d point = iterator.next();
        assertEquals(description + " iterator first point x", expectedFromX, point.x, 0.0001);
        assertEquals(description + " iterator first point y", expectedFromY, point.y, 0.0001);
        assertEquals(description + " iterator first point z", expectedFromZ, point.z, 0.0001);
        assertTrue(description + " iterator has more data", iterator.hasNext());
        point = iterator.next();
        assertEquals(description + " iterator second point x", expectedToX, point.x, 0.0001);
        assertEquals(description + " iterator second point y", expectedToY, point.y, 0.0001);
        assertEquals(description + " iterator second point z", expectedToZ, point.z, 0.0001);
        assertFalse(description + " iterator has no more data", iterator.hasNext());
        Bounds3d bounds = segment.getBounds();
        assertEquals(description + " bounds minX", Math.min(expectedFromX, expectedToX), bounds.getMinX(), 0.0001);
        assertEquals(description + " bounds maxX", Math.max(expectedFromX, expectedToX), bounds.getMaxX(), 0.0001);
        assertEquals(description + " bounds minY", Math.min(expectedFromY, expectedToY), bounds.getMinY(), 0.0001);
        assertEquals(description + " bounds maxY", Math.max(expectedFromY, expectedToY), bounds.getMaxY(), 0.0001);
        assertEquals(description + " bounds minZ", Math.min(expectedFromZ, expectedToZ), bounds.getMinZ(), 0.0001);
        assertEquals(description + " bounds maxZ", Math.max(expectedFromZ, expectedToZ), bounds.getMaxZ(), 0.0001);
        assertTrue(description + " toString returns something descriptive", segment.toString().startsWith("Segment3d"));
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
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(1, 0, 3));
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(0, 2, 3));
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(1, 2, 3));
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);

        Point3d projectingPoint = new Point3d(10, 10, 10);
        result = segment.closestPointOnSegment(projectingPoint); // Projects at a point along the segment
        double distanceFromStart = result.distance(segment.getStartPoint());
        assertTrue("distance from start is > 0", distanceFromStart > 0);
        double distanceToEnd = result.distance(segment.getEndPoint());
        System.out.println(segment + " projectingPoint=" + projectingPoint + ", result=" + result);
        assertTrue("distance to end point is > 0", distanceToEnd > 0);
        assertEquals("sum of distances is length of segment", segment.getLength(), distanceFromStart + distanceToEnd, 0.0001);

        Point3d doubleProjected = segment.closestPointOnSegment(result);
        assertEquals("projecting the projection yields the projection", 0, doubleProjected.distance(result), 0.0001);

        result = segment.closestPointOnSegment(new Point3d(21, 10, 15));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(20, 11, 15));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(20, 10, 16));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
        result = segment.closestPointOnSegment(new Point3d(20, 10, 15));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
    }

}
