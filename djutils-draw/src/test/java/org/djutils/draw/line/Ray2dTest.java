package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.djutils.base.AngleUtil;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * Ray2dTest.java.
 * <p>
 * Copyright (c) 2021-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Ray2dTest
{
    /**
     * Test the various constructors of a Ray2d.
     */
    @Test
    public void testConstructors()
    {
        verifyRay("Constructor from x, y, dirZ", new Ray2d(1, 2, 3), 1, 2, 3);
        verifyRay("Constructor from [x, y], dirZ", new Ray2d(new double[] {1, 2}, 3), 1, 2, 3);
        verifyRay("Constructor from Point2D.Double(x, y), dirZ", new Ray2d(new Point2D.Double(1, 2), 3), 1, 2, 3);
        verifyRay("Constructor from Point2d, dirZ", new Ray2d(new Point2d(0.1, 0.2), -0.3), 0.1, 0.2, -0.3);
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
            fail("NaN for phy should have thrown an ArithmeticException");
        }
        catch (ArithmeticException ae)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d((Point2d) null, 1);
            fail("null for point should have thrown a NullPointerException");
        }
        catch (NullPointerException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1, 2);
            fail("Same coordinates for through point should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, new Point2d(1, 2));
            fail("Same coordinates for through point should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(new Point2d(1, 2), 1, 2);
            fail("Same coordinates for through point should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
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
        assertTrue(ray.toString().startsWith("Ray2d"), "toString returns something descriptive");
        assertTrue(ray.toString().indexOf(ray.toString(true)) > 0, "toString can suppress the class name");
    }

    /**
     * Verify all fields of a Ray2d with a tolerance of 0.0001.
     * @param description String; description of the test
     * @param ray Ray2d; the Ray2d
     * @param expectedX double; the expected x value
     * @param expectedY double; the expected y value
     * @param expectedDirZ double; the expected dirZ value
     */
    private void verifyRay(final String description, final Ray2d ray, final double expectedX, final double expectedY,
            final double expectedDirZ)
    {
        assertEquals(expectedX, ray.getX(), 0.0001, description + " getX");
        assertEquals(expectedX, ray.x, 0.0001, description + " x");
        assertEquals(expectedY, ray.getY(), 0.0001, description + " getY");
        assertEquals(expectedY, ray.y, 0.0001, description + " y");
        assertEquals(expectedDirZ, ray.getDirZ(), 0.0001, description + " getDirZ");
        assertEquals(expectedDirZ, ray.dirZ, 0.0001, description + " dirZ");
        Point2d startPoint = ray.getEndPoint();
        assertEquals(expectedX, startPoint.x, 0.0001, description + " getStartPoint x");
        assertEquals(expectedY, startPoint.y, 0.0001, description + " getStartPoint y");
        Ray2d negated = ray.neg();
        assertEquals(-expectedX, negated.x, 0.0001, description + " neg x");
        assertEquals(-expectedY, negated.y, 0.0001, description + " neg y");
        assertEquals(AngleUtil.normalizeAroundZero(expectedDirZ + Math.PI), negated.dirZ, 0.0001, description + " neg dirZ");
        Ray2d flipped = ray.flip();
        assertEquals(expectedX, flipped.getX(), 0.0001, description + " getX");
        assertEquals(expectedX, flipped.x, 0.0001, description + " x");
        assertEquals(expectedY, flipped.getY(), 0.0001, description + " getY");
        assertEquals(expectedY, flipped.y, 0.0001, description + " y");
        assertEquals(AngleUtil.normalizeAroundZero(expectedDirZ + Math.PI), flipped.getDirZ(), 0.0001,
                description + " getDirZ");
        assertEquals(AngleUtil.normalizeAroundZero(expectedDirZ + Math.PI), flipped.dirZ, 0.0001, description + " dirZ");
        assertEquals(2, ray.size(), description + " size");
        Iterator<Point2d> iterator = ray.iterator();
        // First result of iterator is the finite end point (but this is not a hard promise)
        assertTrue(iterator.hasNext());
        Point2d point = iterator.next();
        assertEquals(expectedX, point.x, 0.0001, description + " iterator first point x");
        assertEquals(expectedY, point.y, 0.0001, description + " iterator first point y");
        assertTrue(iterator.hasNext());
        point = iterator.next();
        // We only check that the point is infinite in at least one direction; the boundTest covers the rest
        assertTrue(Double.isInfinite(point.x) || Double.isInfinite(point.y),
                description + " iterator second point is at infinity");
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
        assertEquals(expectedMinX, bounds.getMinX(), 0.0001, "Bounds minX");
        assertEquals(expectedMinY, bounds.getMinY(), 0.0001, "Bounds minY");
        assertEquals(expectedMaxX, bounds.getMaxX(), 0.0001, "Bounds maxX");
        assertEquals(expectedMaxY, bounds.getMaxY(), 0.0001, "Bounds maxY");
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
            fail("NaN position should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(-1);
            fail("Negative position should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(Double.POSITIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocation(Double.NEGATIVE_INFINITY);
            fail("Infinite position should have thrown a DrawRuntimeException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocationExtended(Double.POSITIVE_INFINITY);
            fail("Infinite position should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocationExtended(Double.NEGATIVE_INFINITY);
            fail("Infinite position should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Ray2d(1, 2, 1).getLocationExtended(Double.NaN);
            fail("NaN position should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        for (double dirZ : new double[] {0, 1, 2, 3, 4, 5, -1, -2, Math.PI})
        {
            Ray2d ray = new Ray2d(1, 2, dirZ);
            for (double position : new double[] {0, 10, 0.1, -2})
            {
                Ray2d result = ray.getLocationExtended(position);
                assertEquals(Math.abs(position), ray.distance(result), 0.001,
                        "result is position distance away from base of ray");
                assertEquals(ray.dirZ, result.dirZ, 0.00001, "result has same dirZ as ray");
                assertTrue(ray.epsilonEquals(result.getLocationExtended(-position), 0.0001),
                        "Reverse position on result yields ray");
                if (position > 0)
                {
                    assertEquals(AngleUtil.normalizeAroundZero(ray.dirZ), ray.directionTo(result), 0.0001,
                            "result lies in on ray");
                }
                if (position < 0)
                {
                    assertEquals(AngleUtil.normalizeAroundZero(result.dirZ), result.directionTo(ray), 0.0001,
                            "ray lies on result");
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
        assertEquals(ray.x, result.x, 0, "result is start point");
        assertEquals(ray.y, result.y, 0, "result is start point");
        result = ray.closestPointOnRay(new Point2d(0, 2));
        assertEquals(ray.x, result.x, 0, "result is start point");
        assertEquals(ray.y, result.y, 0, "result is start point");
        result = ray.closestPointOnRay(new Point2d(1, 2));
        assertEquals(ray.x, result.x, 0, "result is start point");
        assertEquals(ray.y, result.y, 0, "result is start point");

        assertNull(ray.projectOrthogonal(new Point2d(1, 0)), "projection misses the ray");
        assertNull(ray.projectOrthogonal(new Point2d(0, 2)), "projection misses the ray");
        assertEquals(new Point2d(1, 2), ray.projectOrthogonal(new Point2d(1, 2)), "projection hits start point of ray");
        assertEquals(0,
                new LineSegment2d(ray.getLocationExtended(-100), ray.getLocation(100)).closestPointOnSegment(new Point2d(1, 0))
                        .distance(ray.projectOrthogonalExtended(new Point2d(1, 0))),
                0.0001, "extended projection returns same point as projection on sufficiently long line segment");

        Point2d projectingPoint = new Point2d(10, 10);
        result = ray.closestPointOnRay(projectingPoint); // Projects at a point along the ray
        double distance = result.distance(ray.getEndPoint());
        assertTrue(distance > 0, "distance from start is > 0");
        // Angle startPoint-result-test-projectingPoint should be 90 degrees
        double angle = ray.getDirZ() - result.directionTo(projectingPoint);
        assertEquals(Math.PI / 2, Math.abs(AngleUtil.normalizeAroundZero(angle)), 0.0001, "angle should be about 90 degrees");
        assertEquals(0, result.distance(ray.projectOrthogonal(projectingPoint)), 0.0001,
                "projection hits closest point on the ray");
        assertEquals(0, result.distance(ray.projectOrthogonalExtended(projectingPoint)), 0.0001,
                "projectOrthogonalExtended returns same result as long as orthogonal projection exists");
    }

    /**
     * Test the project methods.
     */
    @Test
    public void testProject()
    {
        Ray2d ray = new Ray2d(1, 2, 20, 10);
        assertTrue(Double.isNaN(ray.projectOrthogonalFractional(new Point2d(1, 1))), "projects outside");
        assertTrue(ray.projectOrthogonalFractionalExtended(new Point2d(1, 1)) < 0, "projects before start");
        assertEquals(-new Point2d(1 - 19 - 19, 2 - 8 - 8).distance(ray),
                ray.projectOrthogonalFractionalExtended(new Point2d(1 - 19 - 19 + 8, 2 - 8 - 8 - 19)), 0.0001, "projects at");
        // Projection of projection is projection
        for (int x = -2; x < 5; x++)
        {
            for (int y = -2; y < 5; y++)
            {
                Point2d point = new Point2d(x, y);
                double fraction = ray.projectOrthogonalFractionalExtended(point);
                if (fraction < 0)
                {
                    assertTrue(Double.isNaN(ray.projectOrthogonalFractional(point)), "non extended version yields NaN");
                    assertNull(ray.projectOrthogonal(point), "non extended projectOrthogonal yields null");
                }
                else
                {
                    assertEquals(fraction, ray.projectOrthogonalFractional(point), 0.00001, "non extended version yields same");
                    assertEquals(ray.projectOrthogonal(point), ray.projectOrthogonalExtended(point),
                            "non extended version yields same as extended version");
                }
                Point2d projected = ray.projectOrthogonalExtended(point);
                assertEquals(fraction, ray.projectOrthogonalFractionalExtended(projected), 0.00001,
                        "projecting projected point yields same");
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
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, -0.1, 1);
            fail("Negative epsilonCoordinate should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, 1, -0.1);
            fail("Negative epsilonDirection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, Double.NaN, 1);
            fail("NaN epsilonCoordinate should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        try
        {
            ray.epsilonEquals(ray, 1, Double.NaN);
            fail("NaN epsilonDirection should have thrown an ArithmeticException");
        }
        catch (ArithmeticException e)
        {
            // Ignore expected exception
        }

        double[] deltas = new double[] {0.0, -0.125, 0.125, -1, 1}; // Use values that can be represented exactly in a double
        for (double dX : deltas)
        {
            for (double dY : deltas)
            {
                for (double dDirZ : deltas)
                {
                    Ray2d other = new Ray2d(ray.x + dX, ray.y + dY, ray.dirZ + dDirZ);
                    for (double epsilon : new double[] {0, 0.125, 0.5, 0.9, 1.0, 1.1})
                    {
                        // System.out.println(String.format("dX=%f, dY=%f, dDirZ=%f, epsilon=%f", dX, dY, dDirZ, epsilon));
                        boolean result = ray.epsilonEquals(other, epsilon, Double.POSITIVE_INFINITY);
                        boolean expected = Math.abs(dX) <= epsilon && Math.abs(dY) <= epsilon;
                        assertEquals(expected, result, "result of epsilonEquals checking x, y, z");

                        result = ray.epsilonEquals(other, Double.POSITIVE_INFINITY, epsilon);
                        expected = Math.abs(dDirZ) <= epsilon;
                        if (result != expected)
                        {
                            System.out.println("epsilongEquals rotation mismatch: ray=" + ray + ", other=" + other
                                    + ", epsilongRotation=" + epsilon + ", result=" + result);
                            ray.epsilonEquals(other, Double.POSITIVE_INFINITY, epsilon);
                        }
                        assertEquals(expected, result, "result of epsilonEquals checking dirZ");
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
        assertEquals(ray, ray, "equal to itself");
        assertNotEquals(ray, null, "not equal to null");
        assertNotEquals(ray, new DirectedPoint2d(1, 2, 3), "not equal to different object with same parent class");
        assertNotEquals(ray, new Ray2d(1, 2, 11, 10), "not equal to ray with different direction");
        assertNotEquals(ray, new Ray2d(2, 2, 12, 12), "not equal to ray with different start x");
        assertNotEquals(ray, new Ray2d(1, 3, 12, 13), "not equal to ray with different start y");
        assertEquals(ray, new Ray2d(1, 2, 21, 22), "equal to ray with same x, y and direction");

        assertNotEquals(ray.hashCode(), new Ray2d(2, 2, 12, 12), "hashCode depends on x");
        assertNotEquals(ray.hashCode(), new Ray2d(1, 3, 11, 13), "hashCode depends on y");
        assertNotEquals(ray.hashCode(), new Ray2d(1, 2, 11, 10), "hashCode depends on dirZ");
    }

}
