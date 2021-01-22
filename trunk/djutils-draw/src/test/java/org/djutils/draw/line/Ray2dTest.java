package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djutils.base.AngleUtil;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * Ray2dTest.java.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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

        assertTrue("toString returns something descriptive", new Ray2d(1, 2, 3).toString().startsWith("Ray2d"));
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
        Point2d startPoint = ray.getStartPoint();
        assertEquals(description + " getStartPoint x", expectedX, startPoint.x, 0.0001);
        assertEquals(description + " getStartPoint y", expectedY, startPoint.y, 0.0001);
        Ray2d negated = ray.neg();
        assertEquals(description + " neg x", -expectedX, negated.x, 0.0001);
        assertEquals(description + " neg y", -expectedY, negated.y, 0.0001);
        assertEquals(description + " neg phi", expectedPhi + Math.PI, negated.phi, 0.0001);
    }

    /**
     * Test the result of the getBounds method.
     */
    @Test
    public void boundsTest()
    {
        // X direction
        Bounds2d b = new Ray2d(1, 2, 0).getBounds();
        // Angle of 0 is exact; bounds should be infinite in only the positive X direction
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

        // first quadrant
        b = new Ray2d(1, 2, 0.2).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);

        // Math.PI / 2 is in first quadrant due to finite precision of a double
        b = new Ray2d(1, 2, Math.PI / 2).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);

        // second quadrant
        b = new Ray2d(1, 2, 2).getBounds();
        assertEquals("Bounds minX", Double.NEGATIVE_INFINITY, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.maxX", 1, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);

        // Math.PI is in second quadrant due to finite precision of a double
        b = new Ray2d(1, 2, Math.PI).getBounds();
        assertEquals("Bounds minX", Double.NEGATIVE_INFINITY, b.getMinX(), 0);
        assertEquals("Bounds.minY", 2, b.getMinY(), 0);
        assertEquals("Bounds.maxX", 1, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", Double.POSITIVE_INFINITY, b.getMaxY(), 0);

        // third quadrant
        b = new Ray2d(1, 2, 4).getBounds();
        assertEquals("Bounds minX", Double.NEGATIVE_INFINITY, b.getMinX(), 0);
        assertEquals("Bounds.minY", Double.NEGATIVE_INFINITY, b.getMinY(), 0);
        assertEquals("Bounds.maxX", 1, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

        // fourth quadrant
        b = new Ray2d(1, 2, -1).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", Double.NEGATIVE_INFINITY, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

        // -Math.PI / 2 is in fourth quadrant due to finite precision of a double
        b = new Ray2d(1, 2, -Math.PI / 2).getBounds();
        assertEquals("Bounds minX", 1, b.getMinX(), 0);
        assertEquals("Bounds.minY", Double.NEGATIVE_INFINITY, b.getMinY(), 0);
        assertEquals("Bounds.maxX", Double.POSITIVE_INFINITY, b.getMaxX(), 0);
        assertEquals("Bounds.maxY", 2, b.getMaxY(), 0);

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
            fail("Infited position should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(Double.NEGATIVE_INFINITY);
            fail("Infinte position should have thrown a DrawRuntimeException");
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
     * Test the closestPointOnRay method.
     */
    @Test
    public void testClosestPoint()
    {
        Ray2d ray = new Ray2d(1, 2, 1);
        try
        {
            ray.closestPointOnLine(null);
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
        
        Point2d projectingPoint = new Point2d(10, 10);
        result = ray.closestPointOnRay(projectingPoint); // Projects at a point along the ray
        double distance = result.distance(ray.getStartPoint());
        assertTrue("distance from start is > 0", distance > 0);
        // Angle startPoint-result-test-projectingPoint should be 90 degrees
        double angle = ray.getPhi() - result.directionTo(projectingPoint);
        assertEquals("angle should be about 90 degrees", Math.PI / 2, Math.abs(AngleUtil.normalizeAroundZero(angle)), 0.0001);
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

}
