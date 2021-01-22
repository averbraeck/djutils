package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * Ray3dTest.java.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        verifyRay("Constructor from x, y, phi", new Ray3d(1, 2, 3, 4, 5), 1, 2, 3, 4, 5);
        verifyRay("Constructor from Point3d, phi", new Ray3d(new Point3d(0.1, 0.2, 0.3), -0.4, -0.5), 0.1, 0.2, 0.3, -0.4,
                -0.5);
        verifyRay("Constructor from x, y, z, throughX, throughY, throughZ", new Ray3d(1, 2, 3, 4, 6, 15), 1, 2, 3,
                Math.atan2(4, 3), Math.atan2(12, 5));
        verifyRay("Constructor from x, y, z, throughX, throughY, throughZ", new Ray3d(1, 2, 3, 1, 6, 15), 1, 2, 3,
                Math.atan2(4, 0), Math.atan2(12, 4));
        verifyRay("Constructor from x, y, z, throughX, throughY, throughZ", new Ray3d(1, 2, 3, 1, 2, 15), 1, 2, 3,
                Math.atan2(0, 0), Math.atan2(12, 0));
        verifyRay("Constructor from Point3d, throughX, throughY, throughZ", new Ray3d(new Point3d(1, 2, 3), 4, 6, 15), 1, 2, 3,
                Math.atan2(4, 3), Math.atan2(12, 5));
        verifyRay("Constructor from Point3d, throughX, throughY, throughZ", new Ray3d(new Point3d(1, 2, 3), 1, 6, 15), 1, 2, 3,
                Math.atan2(4, 0), Math.atan2(12, 4));
        verifyRay("Constructor from Point3d, throughX, throughY, throughZ", new Ray3d(new Point3d(1, 2, 3), 1, 2, 15), 1, 2, 3,
                Math.atan2(0, 0), Math.atan2(12, 0));
        verifyRay("Constructor from x, y, z, Point3d", new Ray3d(1, 2, 3, new Point3d(4, 6, 15)), 1, 2, 3, Math.atan2(4, 3),
                Math.atan2(12, 5));
        verifyRay("Constructor from x, y, z, Point3d", new Ray3d(1, 2, 3, new Point3d(1, 6, 15)), 1, 2, 3, Math.atan2(4, 0),
                Math.atan2(12, 4));
        verifyRay("Constructor from x, y, z, Point3d", new Ray3d(1, 2, 3, new Point3d(1, 2, 15)), 1, 2, 3, Math.atan2(0, 0),
                Math.atan2(12, 0));
        verifyRay("Constructor from Point3d, Point3d", new Ray3d(new Point3d(1, 2, 3), new Point3d(4, 6, 15)), 1, 2, 3,
                Math.atan2(4, 3), Math.atan2(12, 5));
        verifyRay("Constructor from Point3d, Point3d", new Ray3d(new Point3d(1, 2, 3), new Point3d(1, 6, 15)), 1, 2, 3,
                Math.atan2(4, 0), Math.atan2(12, 4));
        verifyRay("Constructor from Point3d, Point3d", new Ray3d(new Point3d(1, 2, 3), new Point3d(1, 2, 15)), 1, 2, 3,
                Math.atan2(0, 0), Math.atan2(12, 0));

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

        assertTrue("toString returns something descriptive", new Ray3d(1, 2, 3, 0.2, 0.3).toString().startsWith("Ray3d"));
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
        Point3d startPoint = ray.getStartPoint();
        assertEquals(description + " getStartPoint x", expectedX, startPoint.x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedY, startPoint.y, 0.0001);
        assertEquals(description + " getStartPoint z", expectedZ, startPoint.z, 0.0001);
        Ray3d negated = ray.neg();
        assertEquals(description + " neg x", -expectedX, negated.x, 0.0001);
        assertEquals(description + " neg y", -expectedY, negated.y, 0.0001);
        assertEquals(description + " neg z", -expectedZ, negated.z, 0.0001);
        assertEquals(description + " neg phi", expectedPhi + Math.PI, negated.phi, 0.0001);
        assertEquals(description + " neg theta", expectedTheta + Math.PI, negated.theta, 0.0001);
    }

    /**
     * Test the result of the getBounds method.
     */
    @Test
    public void boundsTest()
    {
        // X direction
        Bounds3d b = new Ray3d(1, 2, 3, 0, 0).getBounds();
        // Angle of 0 is exact; bounds should be infinite in only the positive X direction
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds minZ", 3, b.getMinZ(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);
        assertEquals("Bounds.maxZ", 3, b.getMinZ(), 0);

        // first quadrant in XY, pointing up (positive Z)
        b = new Ray3d(1, 2, 3, 0.2, 1.1).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.minZ", 3, b.getMinZ(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);
        assertEquals("Bounds.maxZ", Double.POSITIVE_INFINITY, b.getMaxZ(), 0);

        // Math.PI / 2 is in first quadrant due to finite precision of a double
        b = new Ray3d(1, 2, 3, Math.PI / 2, 0).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds minZ", 3, b.getMinZ(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);
        assertEquals("Bounds.maxZ", 3, b.getMinZ(), 0);

        // second quadrant in XY, pointing down (negative Z)
        b = new Ray3d(1, 2, 3, 2, -1).getBounds();
        assertEquals("Bounds minX", Double.NEGATIVE_INFINITY, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.minZ", Double.NEGATIVE_INFINITY, b.getMinZ(), 0);
        assertEquals("Bounds.maxX", 1, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);
        assertEquals("Bounds.maxZ", 3, b.getMaxZ(), 0);

        // Math.PI is in second quadrant due to finite precision of a double
        b = new Ray3d(1, 2, 3, Math.PI, 0).getBounds();
        assertEquals("Bounds minX", Double.NEGATIVE_INFINITY, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.maxX", 1, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);

        // third quadrant
        b = new Ray3d(1, 2, 3, 4, 0).getBounds();
        assertEquals("Bounds minX", Double.NEGATIVE_INFINITY, b.getMinX(), 0);
        assertEquals("Bounds.minY", Double.NEGATIVE_INFINITY, b.getMinY(), 0);
        assertEquals("Bounds.maxX", 1, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

        // fourth quadrant
        b = new Ray3d(1, 2, 3, -1, 0).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", Double.NEGATIVE_INFINITY, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

        // -Math.PI / 2 is in fourth quadrant due to finite precision of a double
        b = new Ray3d(1, 2, 3, -Math.PI / 2, 0).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", Double.NEGATIVE_INFINITY, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

        // TODO theta values at boundaries and outside of [0..PI/2]
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
     * Test the closestPointOnRay method.
     */
    @Test
    public void testClosestPoint()
    {
        Ray3d ray = new Ray3d(1, 2, 3, 0.4, 0.5);
        try
        {
            ray.closestPointOnLine(null);
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

        Point3d projectingPoint = new Point3d(10, 10, 10);
        result = ray.closestPointOnRay(projectingPoint); // Projects at a point along the ray
        double distance = result.distance(ray.getStartPoint());
        assertTrue("distance from start is > 0", distance > 0);
        // Check that points on the ray slightly closer to start point or slightly further are indeed further from
        // projectingPoint
        assertTrue("Point on ray closer than result is further from projectingPoint",
                ray.getLocation(distance - 0.1).distance(projectingPoint) < distance);
        assertTrue("Point on ray further than result is further from projectingPoint",
                ray.getLocation(distance + 0.1).distance(projectingPoint) < distance);
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
                            }
                        }
                    }
                }
            }
        }
    }

}
