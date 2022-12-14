package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.OrientedPoint2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * Ray2dTest.java.
 * <p>
 * Copyright (c) 2021-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Ray2dTest
{
    /**
     * Test the various constructors of a Ray2d.
     */
    @Test
    public void testConstructors()
    {
        verifyRay("Constructor from x, y, phi", new Ray2d(1, 2, 3), 1, 2, 3);
        verifyRay("Constructor from Point2d, phi", new Ray2d(new Point2d(0.1, 0.2), -0.3), 0.1, 0.2, -0.3);
        verifyRay("Constructor from x, y, throughX, throughY", new Ray2d(1, 2, 3, 5), 1, 2, Math.atan2(3, 2));
        verifyRay("Constructor from x, y, throughX, throughY", new Ray2d(1, 2, 1, 5), 1, 2, Math.atan2(3, 0));
        verifyRay("Constructor from x, y, throughX, throughY", new Ray2d(1, 2, 3, 2), 1, 2, Math.atan2(0, 2));
        verifyRay("Constructor from Point2d, throughX, throughY", new Ray2d(new Point2d(1, 2), 3, 5), 1, 2, Math.atan2(3, 2));
        verifyRay("Constructor from Point2d, throughX, throughY", new Ray2d(new Point2d(1, 2), 1, 5), 1, 2, Math.atan2(3, 0));
        verifyRay("Constructor from Point2d, throughX, throughY", new Ray2d(new Point2d(1, 2), 3, 2), 1, 2, Math.atan2(0, 2));
        verifyRay("Constructor from x, y, Point2d", new Ray2d(1, 2, new Point2d(3, 5)), 1, 2, Math.atan2(3, 2));
        verifyRay("Constructor from x, y, Point2d", new Ray2d(1, 2, new Point2d(1, 5)), 1, 2, Math.atan2(3, 0));
        verifyRay("Constructor from x, y, Point2d", new Ray2d(1, 2, new Point2d(3, 2)), 1, 2, Math.atan2(0, 2));
        verifyRay("Constructor from Point2d, Point2d", new Ray2d(new Point2d(1, 2), new Point2d(3, 5)), 1, 2, Math.atan2(3, 2));
        verifyRay("Constructor from Point2d, Point2d", new Ray2d(new Point2d(1, 2), new Point2d(1, 5)), 1, 2, Math.atan2(3, 0));
        verifyRay("Constructor from Point2d, Point2d", new Ray2d(new Point2d(1, 2), new Point2d(3, 2)), 1, 2, Math.atan2(0, 2));

        try
        {
            new Ray2d(1, 2, Double.NaN);
            fail("NaN for phy should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(null, 1);
            fail("null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1, 2);
            fail("Same coordinates for through point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, new Point2d(1, 2));
            fail("Same coordinates for through point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(new Point2d(1, 2), 1, 2);
            fail("Same coordinates for through point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, null);
            fail("null for through point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(null, new Point2d(3, 4));
            fail("null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(new Point2d(1, 2), null);
            fail("null for through point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        Ray2d ray = new Ray2d(1, 2, 3);
        assertTrue("toString returns something descriptive", ray.toString().startsWith("Ray2d"));
        assertTrue("toString can suppress the class name", ray.toString().indexOf(ray.toString(true)) > 0);
    }

    /**
     * Verify all fields of a Ray2d with a tolerance of 0.0001.
     * @param description String; description of the test
     * @param ray Ray2d; the Ray2d
     * @param expectedX double; the expected x value
     * @param expectedY double; the expected y value
     * @param expectedPhi double; the expected phi value
     */
    private void verifyRay(final String description, final Ray2d ray, final double expectedX, final double expectedY,
            final double expectedPhi)
    {
        assertEquals(description + " getX", expectedX, ray.getX(), 0.0001);
        assertEquals(description + " x", expectedX, ray.x, 0.0001);
        assertEquals(description + " getY", expectedY, ray.getY(), 0.0001);
        assertEquals(description + " y", expectedY, ray.y, 0.0001);
        assertEquals(description + " getPhi", expectedPhi, ray.getPhi(), 0.0001);
        assertEquals(description + " phi", expectedPhi, ray.phi, 0.0001);
        Point2d startPoint = ray.getEndPoint();
        assertEquals(description + " getStartPoint x", expectedX, startPoint.x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedY, startPoint.y, 0.0001);
        Ray2d negated = ray.neg();
        assertEquals(description + " neg x", -expectedX, negated.x, 0.0001);
        assertEquals(description + " neg y", -expectedY, negated.y, 0.0001);
        assertEquals(description + " neg phi", expectedPhi + Math.PI, negated.phi, 0.0001);
        Ray2d flipped = ray.flip();
        assertEquals(description + " getX", expectedX, flipped.getX(), 0.0001);
        assertEquals(description + " x", expectedX, flipped.x, 0.0001);
        assertEquals(description + " getY", expectedY, flipped.getY(), 0.0001);
        assertEquals(description + " y", expectedY, flipped.y, 0.0001);
        assertEquals(description + " getPhi", expectedPhi + Math.PI, flipped.getPhi(), 0.0001);
        assertEquals(description + " phi", expectedPhi + Math.PI, flipped.phi, 0.0001);
        assertEquals(description + " size", 2, ray.size());
        Iterator<Point2d> iterator = ray.getPoints();
        // First result of iterator is the finite end point (but this is not a hard promise)
        assertTrue(iterator.hasNext());
        Point2d point = iterator.next();
        assertEquals(description + " iterator first point x", expectedX, point.x, 0.0001);
        assertEquals(description + " iterator first point y", expectedY, point.y, 0.0001);
        assertTrue(iterator.hasNext());
        point = iterator.next();
        // We only check that the point is infinite in at least one direction; the boundTest covers the rest
        assertTrue(description + " iterator second point is at infinity",
                Double.isInfinite(point.x) || Double.isInfinite(point.y));
        assertFalse(iterator.hasNext());
        try
        {
            iterator.next();
            fail("Should have thrown a NoSuchElementException");
        }
        catch (NoSuchElementException nsee)
        {
            // Ignore expected exception
        }
    }

    /**
     * Test the result of the getBounds method.
     */
    @Test
    public void boundsTest()
    {
        // X direction
        // Angle of 0 is exact; bounds should be infinite in only the positive X direction
        verifyBounds(new Ray2d(1, 2, 0).getBounds(), 1, 2, Double.POSITIVE_INFINITY, 2);

        // first quadrant
        verifyBounds(new Ray2d(1, 2, 0.2).getBounds(), 1, 2, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // Math.PI / 2 is in first quadrant due to finite precision of a double
        verifyBounds(new Ray2d(1, 2, Math.PI / 2).getBounds(), 1, 2, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // second quadrant
        verifyBounds(new Ray2d(1, 2, 2).getBounds(), Double.NEGATIVE_INFINITY, 2, 1, Double.POSITIVE_INFINITY);

        // Math.PI is in second quadrant due to finite precision of a double
        verifyBounds(new Ray2d(1, 2, Math.PI).getBounds(), Double.NEGATIVE_INFINITY, 2, 1, Double.POSITIVE_INFINITY);

        // third quadrant
        verifyBounds(new Ray2d(1, 2, 4).getBounds(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1, 2);

        // fourth quadrant
        verifyBounds(new Ray2d(1, 2, -1).getBounds(), 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 2);

        // -Math.PI / 2 is in fourth quadrant due to finite precision of a double
        verifyBounds(new Ray2d(1, 2, -Math.PI / 2).getBounds(), 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 2);

    }

    /**
     * Verify a Bounds object.
     * @param bounds Bounds2d; the Bounds object to verify
     * @param expectedMinX double; the expected minimum x value
     * @param expectedMinY double; the expected minimum y value
     * @param expectedMaxX double; the expected maximum x value
     * @param expectedMaxY double; the expected maximum y value
     */
    private void verifyBounds(final Bounds2d bounds, final double expectedMinX, final double expectedMinY,
            final double expectedMaxX, final double expectedMaxY)
    {
        assertEquals("Bounds minX", expectedMinX, bounds.getMinX(), 0.0001);
        assertEquals("Bounds minY", expectedMinY, bounds.getMinY(), 0.0001);
        assertEquals("Bounds maxX", expectedMaxX, bounds.getMaxX(), 0.0001);
        assertEquals("Bounds maxY", expectedMaxY, bounds.getMaxY(), 0.0001);
    }

    /**
     * Test the getLocation and getLocationExtended methods.
     */
    @Test
    public void testLocation()
    {
        try
        {
            new Ray2d(1, 2, 1).getLocation(Double.NaN);
            fail("NaN position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(-1);
            fail("Negative position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(Double.POSITIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(Double.NEGATIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocationExtended(Double.POSITIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocationExtended(Double.NEGATIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocationExtended(Double.NaN);
            fail("NaN position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        for (double phi : new double[] { 0, 1, 2, 3, 4, 5, -1, -2, Math.PI })
        {
            Ray2d ray = new Ray2d(1, 2, phi);
            for (double position : new double[] { 0, 10, 0.1, -2 })
            {
                Ray2d result = ray.getLocationExtended(position);
                assertEquals("result is position distance away from base of ray", Math.abs(position), ray.distance(result),
                        0.001);
                assertEquals("result has same phi as ray", ray.phi, result.phi, 0.00001);
                assertTrue("Reverse position on result yields ray",
                        ray.epsilonEquals(result.getLocationExtended(-position), 0.0001));
                if (position > 0)
                {
                    assertEquals("result lies in on ray", AngleUtil.normalizeAroundZero(ray.phi), ray.directionTo(result),
                            0.0001);
                }
                if (position < 0)
                {
                    assertEquals("ray lies on result", AngleUtil.normalizeAroundZero(result.phi), result.directionTo(ray),
                            0.0001);
                }
            }
        }
    }

    /**
     * Test the closestPointOnRay and the projectOrthogonal methods.
     */
    @Test
    public void testClosestPointAndProjectOrthogonal()
    {
        Ray2d ray = new Ray2d(1, 2, 1);
        try
        {
            ray.closestPointOnRay(null);
            fail("Null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        Point2d result = ray.closestPointOnRay(new Point2d(1, 0));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);
        result = ray.closestPointOnRay(new Point2d(0, 2));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);
        result = ray.closestPointOnRay(new Point2d(1, 2));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);

        assertNull("projection misses the ray", ray.projectOrthogonal(new Point2d(1, 0)));
        assertNull("projection misses the ray", ray.projectOrthogonal(new Point2d(0, 2)));
        assertEquals("projection hits start point of ray", new Point2d(1, 2), ray.projectOrthogonal(new Point2d(1, 2)));
        assertEquals("extended projection returns same point as projection on sufficiently long line segment", 0,
                new LineSegment2d(ray.getLocationExtended(-100), ray.getLocation(100)).closestPointOnSegment(new Point2d(1, 0))
                        .distance(ray.projectOrthogonalExtended(new Point2d(1, 0))),
                0.0001);

        Point2d projectingPoint = new Point2d(10, 10);
        result = ray.closestPointOnRay(projectingPoint); // Projects at a point along the ray
        double distance = result.distance(ray.getEndPoint());
        assertTrue("distance from start is > 0", distance > 0);
        // Angle startPoint-result-test-projectingPoint should be 90 degrees
        double angle = ray.getPhi() - result.directionTo(projectingPoint);
        assertEquals("angle should be about 90 degrees", Math.PI / 2, Math.abs(AngleUtil.normalizeAroundZero(angle)), 0.0001);
        assertEquals("projection hits closest point on the ray", 0, result.distance(ray.projectOrthogonal(projectingPoint)),
                0.0001);
        assertEquals("projectOrthogonalExtended returns same result as long as orthogonal projection exists", 0,
                result.distance(ray.projectOrthogonalExtended(projectingPoint)), 0.0001);
    }

    /**
     * Test the project methods.
     */
    @Test
    public void testProject()
    {
        Ray2d ray = new Ray2d(1, 2, 20, 10);
        assertTrue("projects outside", Double.isNaN(ray.projectOrthogonalFractional(new Point2d(1, 1))));
        assertTrue("projects before start", ray.projectOrthogonalFractionalExtended(new Point2d(1, 1)) < 0);
        assertEquals("projects at", -new Point2d(1 - 19 - 19, 2 - 8 - 8).distance(ray),
                ray.projectOrthogonalFractionalExtended(new Point2d(1 - 19 - 19 + 8, 2 - 8 - 8 - 19)), 0.0001);
        // Projection of projection is projection
        for (int x = -2; x < 5; x++)
        {
            for (int y = -2; y < 5; y++)
            {
                Point2d point = new Point2d(x, y);
                double fraction = ray.projectOrthogonalFractionalExtended(point);
                if (fraction < 0)
                {
                    assertTrue("non extended version yields NaN", Double.isNaN(ray.projectOrthogonalFractional(point)));
                    assertNull("non extended projectOrthogonal yields null", ray.projectOrthogonal(point));
                }
                else
                {
                    assertEquals("non extended version yields same", fraction, ray.projectOrthogonalFractional(point), 0.00001);
                    assertEquals("non extended version yields same as extended version", ray.projectOrthogonal(point),
                            ray.projectOrthogonalExtended(point));
                }
                Point2d projected = ray.projectOrthogonalExtended(point);
                assertEquals("projecting projected point yields same", fraction,
                        ray.projectOrthogonalFractionalExtended(projected), 0.00001);
            }
        }
    }

    /**
     * Test the epsilonEquals method.
     */
    @Test
    public void epsilonEqualsTest()
    {
        Ray2d ray = new Ray2d(1, 2, -1);
        try
        {
            ray.epsilonEquals((Ray2d) null, 1, 1);
            fail("Null pointer should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, -0.1, 1);
            fail("Negative epsilonCoordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException npe)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, 1, -0.1);
            fail("Negative epsilonDirection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException npe)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, Double.NaN, 1);
            fail("NaN epsilonCoordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException npe)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, 1, Double.NaN);
            fail("NaN epsilonDirection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException npe)
        {
            // Ignore expected exception
        }

        double[] deltas = new double[] { 0.0, -0.125, 0.125, -1, 1 }; // Use values that can be represented exactly in a double
        for (double dX : deltas)
        {
            for (double dY : deltas)
            {
                for (double dPhi : deltas)
                {
                    Ray2d other = new Ray2d(ray.x + dX, ray.y + dY, ray.phi + dPhi);
                    for (double epsilon : new double[] { 0, 0.125, 0.5, 0.9, 1.0, 1.1 })
                    {
                        // System.out.println(String.format("dX=%f, dY=%f, dPhi=%f, epsilon=%f", dX, dY, dPhi, epsilon));
                        boolean result = ray.epsilonEquals(other, epsilon, Double.POSITIVE_INFINITY);
                        boolean expected = Math.abs(dX) <= epsilon && Math.abs(dY) <= epsilon;
                        assertEquals("result of epsilonEquals checking x, y, z", expected, result);

                        result = ray.epsilonEquals(other, Double.POSITIVE_INFINITY, epsilon);
                        expected = Math.abs(dPhi) <= epsilon;
                        assertEquals("result of epsilonEquals checking phi", expected, result);
                    }
                }
            }
        }
    }

    /**
     * Test the equals and hasCode methods.
     */
    @Test
    public void equalsAndHashCodeTest()
    {
        Ray2d ray = new Ray2d(1, 2, 11, 12);
        assertEquals("equal to itself", ray, ray);
        assertNotEquals("not equal to null", ray, null);
        assertNotEquals("not equal to different object with same parent class", ray, new OrientedPoint2d(1, 2));
        assertNotEquals("not equal to ray with different direction", ray, new Ray2d(1, 2, 11, 10));
        assertNotEquals("not equal to ray with different start x", ray, new Ray2d(2, 2, 12, 12));
        assertNotEquals("not equal to ray with different start y", ray, new Ray2d(1, 3, 12, 13));
        assertEquals("equal to ray with same x, y and direction", ray, new Ray2d(1, 2, 21, 22));

        assertNotEquals("hashCode depends on x", ray.hashCode(), new Ray2d(2, 2, 12, 12));
        assertNotEquals("hashCode depends on y", ray.hashCode(), new Ray2d(1, 3, 11, 13));
        assertNotEquals("hashCode depends on phi", ray.hashCode(), new Ray2d(1, 2, 11, 10));
    }

}
