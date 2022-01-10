package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.OrientedPoint3d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * Ray3dTest.java.
 * <p>
 * Copyright (c) 2021-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Ray3dTest
{
    /**
     * Test the various constructors of a Ray3d.
     */
    @Test
    public void testConstructors()
    {
        // Verify theta and phi for the six basic directions.
        verifyRay("positive x", new Ray3d(0, 0, 0, 1, 0, 0), 0, 0, 0, 0, Math.PI / 2);
        verifyRay("positive y", new Ray3d(0, 0, 0, 0, 1, 0), 0, 0, 0, Math.PI / 2, Math.PI / 2);
        verifyRay("positive z", new Ray3d(0, 0, 0, 0, 0, 1), 0, 0, 0, 0, 0);
        verifyRay("negative x", new Ray3d(0, 0, 0, -1, 0, 0), 0, 0, 0, Math.PI, Math.PI / 2);
        verifyRay("negative y", new Ray3d(0, 0, 0, 0, -1, 0), 0, 0, 0, -Math.PI / 2, Math.PI / 2);
        verifyRay("negative z", new Ray3d(0, 0, 0, 0, 0, -1), 0, 0, 0, 0, Math.PI);
        verifyRay("Constructor from x, y, phi, theta", new Ray3d(1, 2, 3, 4, 5), 1, 2, 3, 4, 5);
        verifyRay("Constructor from Point3d, phi, theta", new Ray3d(new Point3d(0.1, 0.2, 0.3), -0.4, -0.5), 0.1, 0.2, 0.3,
                -0.4, -0.5);
        verifyRay("Constructor from x, y, z, throughX, throughY, throughZ", new Ray3d(1, 2, 3, 4, 6, 15), 1, 2, 3,
                Math.atan2(4, 3), Math.atan2(5, 12));
        verifyRay("Constructor from x, y, z, throughX, throughY, throughZ", new Ray3d(1, 2, 3, 1, 6, 15), 1, 2, 3,
                Math.atan2(4, 0), Math.atan2(4, 12));
        verifyRay("Constructor from x, y, z, throughX, throughY, throughZ", new Ray3d(1, 2, 3, 1, 2, 15), 1, 2, 3,
                Math.atan2(0, 0), Math.atan2(0, 12));
        verifyRay("Constructor from Point3d, throughX, throughY, throughZ", new Ray3d(new Point3d(1, 2, 3), 4, 6, 15), 1, 2, 3,
                Math.atan2(4, 3), Math.atan2(5, 12));
        verifyRay("Constructor from Point3d, throughX, throughY, throughZ", new Ray3d(new Point3d(1, 2, 3), 1, 6, 15), 1, 2, 3,
                Math.atan2(4, 0), Math.atan2(4, 12));
        verifyRay("Constructor from Point3d, throughX, throughY, throughZ", new Ray3d(new Point3d(1, 2, 3), 1, 2, 15), 1, 2, 3,
                Math.atan2(0, 0), Math.atan2(0, 12));
        verifyRay("Constructor from x, y, z, Point3d", new Ray3d(1, 2, 3, new Point3d(4, 6, 15)), 1, 2, 3, Math.atan2(4, 3),
                Math.atan2(5, 12));
        verifyRay("Constructor from x, y, z, Point3d", new Ray3d(1, 2, 3, new Point3d(1, 6, 15)), 1, 2, 3, Math.atan2(4, 0),
                Math.atan2(4, 12));
        verifyRay("Constructor from x, y, z, Point3d", new Ray3d(1, 2, 3, new Point3d(1, 2, 15)), 1, 2, 3, Math.atan2(0, 0),
                Math.atan2(0, 12));
        verifyRay("Constructor from Point3d, Point3d", new Ray3d(new Point3d(1, 2, 3), new Point3d(4, 6, 15)), 1, 2, 3,
                Math.atan2(4, 3), Math.atan2(5, 12));
        verifyRay("Constructor from Point3d, Point3d", new Ray3d(new Point3d(1, 2, 3), new Point3d(1, 6, 15)), 1, 2, 3,
                Math.atan2(4, 0), Math.atan2(4, 12));
        verifyRay("Constructor from Point3d, Point3d", new Ray3d(new Point3d(1, 2, 3), new Point3d(1, 2, 15)), 1, 2, 3,
                Math.atan2(0, 0), Math.atan2(0, 12));

        try
        {
            new Ray3d(1, 2, 3, Double.NaN, 0);
            fail("NaN for phy should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 0, Double.NaN);
            fail("NaN for theta should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(null, 1, 2);
            fail("null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 2, 3);
            fail("Same coordinates for through point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, new Point3d(1, 2, 3));
            fail("Same coordinates for through point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(new Point3d(1, 2, 3), 1, 2, 3);
            fail("Same coordinates for through point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, null);
            fail("null for through point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(null, new Point3d(4, 5, 6));
            fail("null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(new Point3d(1, 2, 3), null);
            fail("null for through point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        Ray3d ray = new Ray3d(1, 2, 3, 0.2, 0.3);
        assertTrue("toString returns something descriptive", ray.toString().startsWith("Ray3d"));
        assertTrue("toString can suppress the class name", ray.toString().indexOf(ray.toString(true)) > 0);
    }

    /**
     * Verify all fields of a Ray3d with a tolerance of 0.0001.
     * @param description String; description of the test
     * @param ray Ray3d; the Ray3d
     * @param expectedX double; the expected x value
     * @param expectedY double; the expected y value
     * @param expectedZ double; the expected z value
     * @param expectedPhi double; the expected phi value
     * @param expectedTheta double; the expected theta value
     */
    private void verifyRay(final String description, final Ray3d ray, final double expectedX, final double expectedY,
            final double expectedZ, final double expectedPhi, final double expectedTheta)
    {
        assertEquals(description + " getX", expectedX, ray.getX(), 0.0001);
        assertEquals(description + " x", expectedX, ray.x, 0.0001);
        assertEquals(description + " getY", expectedY, ray.getY(), 0.0001);
        assertEquals(description + " y", expectedY, ray.y, 0.0001);
        assertEquals(description + " getZ", expectedZ, ray.getZ(), 0.0001);
        assertEquals(description + " z", expectedZ, ray.z, 0.0001);
        assertEquals(description + " getPhi", expectedPhi, ray.getPhi(), 0.0001);
        assertEquals(description + " phi", expectedPhi, ray.phi, 0.0001);
        assertEquals(description + " getTheta", expectedTheta, ray.getTheta(), 0.0001);
        assertEquals(description + " theta", expectedTheta, ray.theta, 0.0001);
        Point3d startPoint = ray.getEndPoint();
        assertEquals(description + " getStartPoint x", expectedX, startPoint.x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedY, startPoint.y, 0.0001);
        assertEquals(description + " getStartPoint z", expectedZ, startPoint.z, 0.0001);
        Ray3d negated = ray.neg();
        assertEquals(description + " neg x", -expectedX, negated.x, 0.0001);
        assertEquals(description + " neg y", -expectedY, negated.y, 0.0001);
        assertEquals(description + " neg z", -expectedZ, negated.z, 0.0001);
        assertEquals(description + " neg phi", expectedPhi + Math.PI, negated.phi, 0.0001);
        assertEquals(description + " neg theta", expectedTheta + Math.PI, negated.theta, 0.0001);
        Ray3d flipped = ray.flip();
        assertEquals(description + " getX", expectedX, flipped.getX(), 0.0001);
        assertEquals(description + " x", expectedX, flipped.x, 0.0001);
        assertEquals(description + " getY", expectedY, flipped.getY(), 0.0001);
        assertEquals(description + " y", expectedY, flipped.y, 0.0001);
        assertEquals(description + " getZ", expectedZ, flipped.getZ(), 0.0001);
        assertEquals(description + " z", expectedZ, flipped.z, 0.0001);
        assertEquals(description + " getPhi", expectedPhi + Math.PI, flipped.getPhi(), 0.0001);
        assertEquals(description + " phi", expectedPhi + Math.PI, flipped.phi, 0.0001);
        assertEquals(description + " getTheta", Math.PI - expectedTheta, flipped.getTheta(), 0.0001);
        assertEquals(description + " theta", Math.PI - expectedTheta, flipped.theta, 0.0001);
        assertEquals(description + " size", 2, ray.size());
        Iterator<Point3d> iterator = ray.getPoints();
        // First result of iterator is the finite end point (but this is not a hard promise)
        assertTrue(iterator.hasNext());
        Point3d point = iterator.next();
        assertEquals(description + " iterator first point x", expectedX, point.x, 0.0001);
        assertEquals(description + " iterator first point y", expectedY, point.y, 0.0001);
        assertEquals(description + " iterator first point z", expectedZ, point.z, 0.0001);
        assertTrue(iterator.hasNext());
        point = iterator.next();
        // We only check that the point is infinite in at least one direction; the boundTest covers the rest
        assertTrue(description + " iterator second point is at infinity",
                Double.isInfinite(point.x) || Double.isInfinite(point.y) || Double.isInfinite(point.z));
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
        verifyBounds(new Ray3d(1, 2, 3, 0, 1).getBounds(), 1, 2, 3, Double.POSITIVE_INFINITY, 2, Double.POSITIVE_INFINITY);

        // Z direction
        // Angle of 0 is exact; bounds should be infinite in only the positive X direction
        verifyBounds(new Ray3d(1, 2, 3, 0, 0).getBounds(), 1, 2, 3, 1, 2, Double.POSITIVE_INFINITY);

        // first quadrant in XY, pointing up (positive Z)
        verifyBounds(new Ray3d(1, 2, 3, 0.2, 1.1).getBounds(), 1, 2, 3, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);

        // Math.PI / 2 is in first quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, Math.PI / 2, 1).getBounds(), 1, 2, 3, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // second quadrant in XY, pointing up
        verifyBounds(new Ray3d(1, 2, 3, 2, 1).getBounds(), Double.NEGATIVE_INFINITY, 2, 3, 1, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);

        // Math.PI is in second quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, Math.PI, 1).getBounds(), Double.NEGATIVE_INFINITY, 2, 3, 1, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);

        // third quadrant
        verifyBounds(new Ray3d(1, 2, 3, 4, 1).getBounds(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 3, 1, 2,
                Double.POSITIVE_INFINITY);

        // fourth quadrant
        verifyBounds(new Ray3d(1, 2, 3, -1, 1).getBounds(), 1, Double.NEGATIVE_INFINITY, 3, Double.POSITIVE_INFINITY, 2,
                Double.POSITIVE_INFINITY);

        // -Math.PI / 2 is in fourth quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, -Math.PI / 2, 1).getBounds(), 1, Double.NEGATIVE_INFINITY, 3, Double.POSITIVE_INFINITY,
                2, Double.POSITIVE_INFINITY);

        // first quadrant in XY, pointing down (negative Z)
        verifyBounds(new Ray3d(1, 2, 3, 0.2, 3).getBounds(), 1, 2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, 3);

        // second quadrant in XY, pointing down
        verifyBounds(new Ray3d(1, 2, 3, 2, 3).getBounds(), Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY, 1,
                Double.POSITIVE_INFINITY, 3);

        // Math.PI is in second quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, Math.PI, 3).getBounds(), Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY, 1,
                Double.POSITIVE_INFINITY, 3);

        // third quadrant
        verifyBounds(new Ray3d(1, 2, 3, 4, 3).getBounds(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY, 1, 2, 3);

        // fourth quadrant
        verifyBounds(new Ray3d(1, 2, 3, -1, 3).getBounds(), 1, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, 2, 3);

        // -Math.PI / 2 is in fourth quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, -Math.PI / 2, 3).getBounds(), 1, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, 2, 3);

        // first quadrant in XY, pointing up (positive Z)
        verifyBounds(new Ray3d(1, 2, 3, 0.2, -1.1).getBounds(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 3, 1, 2,
                Double.POSITIVE_INFINITY);

        // Math.PI / 2 is in first quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, Math.PI / 2, -1).getBounds(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 3, 1,
                2, Double.POSITIVE_INFINITY);

        // second quadrant in XY, pointing up
        verifyBounds(new Ray3d(1, 2, 3, 2, -1).getBounds(), 1, Double.NEGATIVE_INFINITY, 3, Double.POSITIVE_INFINITY, 2,
                Double.POSITIVE_INFINITY);

        // Math.PI is in second quadrant due to finite precision of a double
        verifyBounds(new Ray3d(1, 2, 3, Math.PI, -1).getBounds(), 1, Double.NEGATIVE_INFINITY, 3, Double.POSITIVE_INFINITY, 2,
                Double.POSITIVE_INFINITY);

        // third quadrant
        verifyBounds(new Ray3d(1, 2, 3, 4, -1).getBounds(), 1, 2, 3, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);

        // fourth quadrant
        verifyBounds(new Ray3d(1, 2, 3, -1, -1).getBounds(), Double.NEGATIVE_INFINITY, 2, 3, 1, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);

        // TODO theta values at boundaries and outside of [0..PI/2]
    }

    /**
     * Verify a Bounds object.
     * @param bounds Bounds3d; the Bounds object to verify
     * @param expectedMinX double; the expected minimum x value
     * @param expectedMinY double; the expected minimum y value
     * @param expectedMinZ double; the expected minimum z value
     * @param expectedMaxX double; the expected maximum x value
     * @param expectedMaxY double; the expected maximum y value
     * @param expectedMaxZ double; the expected maximum z value
     */
    private void verifyBounds(final Bounds3d bounds, final double expectedMinX, final double expectedMinY,
            final double expectedMinZ, final double expectedMaxX, final double expectedMaxY, final double expectedMaxZ)
    {
        assertEquals("Bounds minX", expectedMinX, bounds.getMinX(), 0.0001);
        assertEquals("Bounds minY", expectedMinY, bounds.getMinY(), 0.0001);
        assertEquals("Bounds minZ", expectedMinZ, bounds.getMinZ(), 0.0001);
        assertEquals("Bounds maxX", expectedMaxX, bounds.getMaxX(), 0.0001);
        assertEquals("Bounds maxY", expectedMaxY, bounds.getMaxY(), 0.0001);
        assertEquals("Bounds maxZ", expectedMaxZ, bounds.getMaxZ(), 0.0001);
    }

    /**
     * Test the getLocation and getLocationExtended methods.
     */
    @Test
    public void testLocation()
    {
        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocation(Double.NaN);
            fail("NaN position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocation(-1);
            fail("Negative position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocation(Double.POSITIVE_INFINITY);
            fail("Infited position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocation(Double.NEGATIVE_INFINITY);
            fail("Infinte position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocationExtended(Double.POSITIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocationExtended(Double.NEGATIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray3d(1, 2, 3, 1, 0.5).getLocationExtended(Double.NaN);
            fail("NaN position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        for (double phi : new double[] { 0, 1, 2, 3, 4, 5, -1, -2, Math.PI })
        {
            for (double theta : new double[] { 0, 1, 2, 3, 4, 5, -1, -2, Math.PI })
            {
                Ray3d ray = new Ray3d(1, 2, 3, phi, theta);
                for (double position : new double[] { 0, 10, 0.1, -2 })
                {
                    Ray3d result = ray.getLocationExtended(position);
                    assertEquals("result is position distance away from base of ray", Math.abs(position), ray.distance(result),
                            0.001);
                    assertEquals("result has same phi as ray", ray.phi, result.phi, 0.00001);
                    assertTrue("Reverse position on result yields ray",
                            ray.epsilonEquals(result.getLocationExtended(-position), 0.0001));
                    if (position > 0)
                    {
                        // TODO verify that it is on positive side of ray
                        assertEquals("result lies in on ray (phi)", ray.phi, result.phi, 0.0001);
                        assertEquals("result lies on ray (theta)", ray.theta, result.theta, 0.0001);
                    }
                    if (position < 0)
                    {
                        assertEquals("ray lies on result (phi)", result.phi, ray.phi, 0.0001);
                        assertEquals("ray lies on result (theta)", result.theta, ray.theta, 0.0001);
                    }
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
        Ray3d ray = new Ray3d(1, 2, 3, 0.4, 0.5);
        try
        {
            ray.closestPointOnRay(null);
            fail("Null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        Point3d result = ray.closestPointOnRay(new Point3d(1, 2, 0));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);
        assertEquals("result is start point", ray.z, result.z, 0);
        result = ray.closestPointOnRay(new Point3d(1, 2, 0));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);
        assertEquals("result is start point", ray.z, result.z, 0);
        result = ray.closestPointOnRay(new Point3d(0, 2, 3));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);
        assertEquals("result is start point", ray.z, result.z, 0);
        result = ray.closestPointOnRay(new Point3d(1, 2, 3));
        assertEquals("result is start point", ray.x, result.x, 0);
        assertEquals("result is start point", ray.y, result.y, 0);
        assertEquals("result is start point", ray.z, result.z, 0);

        assertNull("projection misses the ray", ray.projectOrthogonal(new Point3d(1, 0, 3)));
        assertNull("projection misses the ray", ray.projectOrthogonal(new Point3d(0, 2, 3)));
        assertNull("projection misses the ray", ray.projectOrthogonal(new Point3d(1, 2, 2)));
        assertEquals("projection hits start point of ray", new Point3d(1, 2, 3), ray.projectOrthogonal(new Point3d(1, 2, 3)));
        assertEquals("extended projection returns same point as projection on sufficiently long line segment", 0,
                new LineSegment3d(ray.getLocationExtended(-100), ray.getLocation(100))
                        .closestPointOnSegment(new Point3d(1, 0, -1))
                        .distance(ray.projectOrthogonalExtended(new Point3d(1, 0, -1))),
                0.0001);

        Point3d projectingPoint = new Point3d(10, 10, 10);
        result = ray.closestPointOnRay(projectingPoint); // Projects at a point along the ray
        double distance = result.distance(ray.getEndPoint());
        assertTrue("distance from start is > 0", distance > 0);
        // Check that points on the ray slightly closer to start point or slightly further are indeed further from
        // projectingPoint
        assertTrue("Point on ray closer than result is further from projectingPoint",
                ray.getLocation(distance - 0.1).distance(projectingPoint) < distance);
        assertTrue("Point on ray further than result is further from projectingPoint",
                ray.getLocation(distance + 0.1).distance(projectingPoint) < distance);
        assertEquals("projectOrthogonalExtended returns same result as long as orthogonal projection exists", 0,
                result.distance(ray.projectOrthogonalExtended(projectingPoint)), 0.0001);
    }

    /**
     * Test the project methods.
     */
    @Test
    public void testProject()
    {
        Ray3d ray = new Ray3d(1, 2, 3, 20, 10, 5);
        assertTrue("projects outside", Double.isNaN(ray.projectOrthogonalFractional(new Point3d(1, 1, 1))));
        assertTrue("projects before start", ray.projectOrthogonalFractionalExtended(new Point3d(1, 1, 1)) < 0);
        assertEquals("projects at", -new Point3d(1 - 19 - 19, 2 - 8 - 8, 3 - 2 - 2).distance(ray),
                ray.projectOrthogonalFractionalExtended(new Point3d(1 - 19 - 19 + 8, 2 - 8 - 8 - 19, 3 - 2 - 2)), 0.0001);
        // Projection of projection is projection
        for (int x = -2; x < 5; x++)
        {
            for (int y = -2; y < 5; y++)
            {
                for (int z = -2; z < 5; z++)
                {
                    Point3d point = new Point3d(x, y, z);
                    double fraction = ray.projectOrthogonalFractionalExtended(point);
                    if (fraction < 0)
                    {
                        assertTrue("non extended version yields NaN", Double.isNaN(ray.projectOrthogonalFractional(point)));
                        assertNull("non extended projectOrthogonal yields null", ray.projectOrthogonal(point));
                    }
                    else
                    {
                        assertEquals("non extended version yields same", fraction, ray.projectOrthogonalFractional(point),
                                0.00001);
                        assertEquals("non extended version yields same as extended version", ray.projectOrthogonal(point),
                                ray.projectOrthogonalExtended(point));
                    }
                    Point3d projected = ray.projectOrthogonalExtended(point);
                    assertEquals("projecting projected point yields same", fraction,
                            ray.projectOrthogonalFractionalExtended(projected), 0.00001);
                }
            }
        }
    }

    /**
     * Test the epsilonEquals method.
     */
    @Test
    public void epsilonEqualsTest()
    {
        Ray3d ray = new Ray3d(1, 2, 3, 0.5, -0.5);
        try
        {
            ray.epsilonEquals((Ray3d) null, 1, 1);
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
                for (double dZ : deltas)
                {
                    for (double dPhi : deltas)
                    {
                        for (double dTheta : deltas)
                        {
                            for (double epsilon : new double[] { 0, 0.125, 0.5, 0.9, 1.0, 1.1 })
                            {
                                Ray3d other = new Ray3d(ray.x + dX, ray.y + dY, ray.z + dZ, ray.phi + dPhi, ray.theta + dTheta);
                                // System.out.println(String.format("dX=%f, dY=%f, dZ=%f, dPhi=%f, dTheta=%f, epsilon=%f", dX,
                                // dY,
                                // dZ, dPhi, dTheta, epsilon));
                                boolean result = ray.epsilonEquals(other, epsilon, Double.POSITIVE_INFINITY);
                                boolean expected =
                                        Math.abs(dX) <= epsilon && Math.abs(dY) <= epsilon && Math.abs(dZ) <= epsilon;
                                assertEquals("result of epsilonEquals checking x, y, z", expected, result);

                                result = ray.epsilonEquals(other, Double.POSITIVE_INFINITY, epsilon);
                                expected = Math.abs(dPhi) <= epsilon && Math.abs(dTheta) <= epsilon;
                                assertEquals("result of epsilonEquals checking phi and theta", expected, result);
                                // Create an equivalent alternative ray
                                other = new Ray3d(ray.x + dX, ray.y + dY, ray.z + dZ, Math.PI + ray.phi + dPhi,
                                        Math.PI - ray.theta + dTheta);
                                result = ray.epsilonEquals(other, epsilon, Double.POSITIVE_INFINITY);
                                expected = Math.abs(dX) <= epsilon && Math.abs(dY) <= epsilon && Math.abs(dZ) <= epsilon;
                                assertEquals("result of epsilonEquals checking x, y, z", expected, result);

                                result = ray.epsilonEquals(other, Double.POSITIVE_INFINITY, epsilon);
                                expected = Math.abs(dPhi) <= epsilon && Math.abs(dTheta) <= epsilon;
                                assertEquals("result of epsilonEquals checking phi and theta", expected, result);
                            }
                        }
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
        Ray3d ray = new Ray3d(1, 2, 3, 11, 12, 13);
        assertEquals("equal to itself", ray, ray);
        assertNotEquals("not equal to null", ray, null);
        assertNotEquals("not equal to different object with same parent class", ray, new OrientedPoint3d(1, 2, 3));
        assertNotEquals("not equal to ray with different phi", ray, new Ray3d(1, 2, 3, 11, 10, 13));
        assertNotEquals("not equal to ray with different theta", ray, new Ray3d(1, 2, 3, 11, 12, 10));
        assertNotEquals("not equal to ray with different start x", ray, new Ray3d(2, 2, 3, 12, 12, 13));
        assertNotEquals("not equal to ray with different start y", ray, new Ray3d(1, 3, 3, 11, 13, 13));
        assertEquals("equal to ray with same x, y and direction", ray, new Ray3d(1, 2, 3, 21, 22, 23));

        assertNotEquals("hashCode depends on x", ray.hashCode(), new Ray3d(2, 2, 3, 12, 12, 13));
        assertNotEquals("hashCode depends on y", ray.hashCode(), new Ray3d(1, 3, 3, 11, 13, 13));
        assertNotEquals("hashCode depends on y", ray.hashCode(), new Ray3d(1, 2, 4, 11, 12, 14));
        assertNotEquals("hashCode depends on phi", ray.hashCode(), new Ray3d(1, 2, 3, 11, 10, 13));
        assertNotEquals("hashCode depends on theta", ray.hashCode(), new Ray3d(1, 2, 3, 11, 12, 10));
    }

}
