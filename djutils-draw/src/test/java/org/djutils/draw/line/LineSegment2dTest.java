package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * Segment2dTest.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
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
            fail("idential start and end should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
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
     * @param expectedFromX double; the expected x value for the start of the segment
     * @param expectedFromY double; the expected y value for the start of the segment
     * @param expectedToX double; the expected x value for the end of the segment
     * @param expectedToY double; the expected y value for the end of the segment
     */
    public void verifySegment(final String description, final LineSegment2d segment, final double expectedFromX,
            final double expectedFromY, final double expectedToX, final double expectedToY)
    {
        assertEquals(description + " fromX", expectedFromX, segment.fromX, 0.0001);
        assertEquals(description + " fromY", expectedFromY, segment.fromY, 0.0001);
        assertEquals(description + " toX", expectedToX, segment.toX, 0.0001);
        assertEquals(description + " toY", expectedToY, segment.toY, 0.0001);
        assertEquals(description + " getStartPoint x", expectedFromX, segment.getStartPoint().x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedFromY, segment.getStartPoint().y, 0.0001);
        assertEquals(description + " getEndPoint x", expectedToX, segment.getEndPoint().x, 0.0001);
        assertEquals(description + " getEndPoint y", expectedToY, segment.getEndPoint().y, 0.0001);
        assertEquals(description + " length", Math.hypot(expectedToX - expectedFromX, expectedToY - expectedFromY),
                segment.getLength(), 0.0001);
        assertEquals(description + " size is 2", 2, segment.size());
        Iterator<? extends Point2d> iterator = segment.getPoints();
        assertTrue(description + " iterator has data", iterator.hasNext());
        Point2d point = iterator.next();
        assertEquals(description + " iterator first point x", expectedFromX, point.x, 0.0001);
        assertEquals(description + " iterator first point y", expectedFromY, point.y, 0.0001);
        assertTrue(description + " iterator has more data", iterator.hasNext());
        point = iterator.next();
        assertEquals(description + " iterator second point x", expectedToX, point.x, 0.0001);
        assertEquals(description + " iterator second point y", expectedToY, point.y, 0.0001);
        assertFalse(description + " iterator has no more data", iterator.hasNext());
        Bounds2d bounds = segment.getBounds();
        assertEquals(description + " bounds minX", Math.min(expectedFromX, expectedToX), bounds.getMinX(), 0.0001);
        assertEquals(description + " bounds maxX", Math.max(expectedFromX, expectedToX), bounds.getMaxX(), 0.0001);
        assertEquals(description + " bounds minY", Math.min(expectedFromY, expectedToY), bounds.getMinY(), 0.0001);
        assertEquals(description + " bounds maxY", Math.max(expectedFromY, expectedToY), bounds.getMaxY(), 0.0001);
        assertTrue(description + " toString returns something descriptive", segment.toString().startsWith("Segment2d"));
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

        for (double position : new double[] { -3, -0.5, 0, 1, 10, 100 })
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
                Ray2d ray = segment.getLocation(position);
                assertEquals("distance from start point", position, ray.distance(startPoint), 0.0001);
                assertEquals("distance from end point", segment.getLength() - position, ray.distance(endPoint), 0.0001);
                assertEquals("direction of ray", startPoint.directionTo(endPoint), ray.phi, 0.0001);
            }
            Ray2d ray = segment.getLocationExtended(position);
            assertEquals("distance from start point", Math.abs(position), ray.distance(startPoint), 0.0001);
            assertEquals("distance from end point", Math.abs(segment.getLength() - position), ray.distance(endPoint), 0.0001);
            assertEquals("direction of ray", startPoint.directionTo(endPoint), ray.phi, 0.0001);
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
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);
        result = segment.closestPointOnSegment(new Point2d(0, 2));
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);
        result = segment.closestPointOnSegment(new Point2d(1, 2));
        assertEquals("result is start point", segment.fromX, result.x, 0);
        assertEquals("result is start point", segment.fromY, result.y, 0);

        Point2d projectingPoint = new Point2d(10, 10);
        result = segment.closestPointOnSegment(projectingPoint); // Projects at a point along the segment
        double distanceFromStart = result.distance(segment.getStartPoint());
        assertTrue("distance from start is > 0", distanceFromStart > 0);
        double distanceToEnd = result.distance(segment.getEndPoint());
        assertTrue("distance to end point is > 0", distanceToEnd > 0);
        assertEquals("sum of distances is length of segment", segment.getLength(), distanceFromStart + distanceToEnd, 0.0001);
        // Angle startPoint-result-test-projectingPoint should be 90 degrees
        double angle = segment.getStartPoint().directionTo(segment.getEndPoint()) - result.directionTo(projectingPoint);
        assertEquals("angle should be about 90 degrees", Math.PI / 2, Math.abs(AngleUtil.normalizeAroundZero(angle)), 0.0001);
        
        result = segment.closestPointOnSegment(new Point2d(21, 10));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
        result = segment.closestPointOnSegment(new Point2d(20, 11));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
        result = segment.closestPointOnSegment(new Point2d(20, 10));
        assertEquals("result is end point", segment.toX, result.x, 0);
        assertEquals("result is end point", segment.toY, result.y, 0);
    }

}
