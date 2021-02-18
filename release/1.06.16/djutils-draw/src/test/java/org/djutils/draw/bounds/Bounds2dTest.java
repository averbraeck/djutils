package org.djutils.draw.bounds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import org.djutils.draw.DrawException;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * Bounds2dTest.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds2dTest
{

    /**
     * Test the bounding rectangle constructor.
     * @throws DrawException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException if that happens uncaught; this test has failed
     */
    @Test
    public void constructorTest() throws IllegalArgumentException, DrawException
    {
        try
        {
            new Bounds2d(Double.NaN, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(0, Double.NaN, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(0, 0, Double.NaN, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(0, 0, 0, Double.NaN);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(2, -2, 0, 0);
            fail("Negative x-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(0, 0, 2, -2);
            fail("Negative y-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        Bounds2d br = new Bounds2d(1, 2, 3, 6);
        assertEquals("minAbsoluteX", 1, br.getAbsoluteMinX(), 0);
        assertEquals("maxAbsoluteX", 2, br.getAbsoluteMaxX(), 0);
        assertEquals("minAbsoluteY", 3, br.getAbsoluteMinY(), 0);
        assertEquals("maxAbsoluteY", 6, br.getAbsoluteMaxY(), 0);
        assertEquals("minX", -0.5, br.getMinX(), 0);
        assertEquals("maxX", 0.5, br.getMaxX(), 0);
        assertEquals("minY", -1.5, br.getMinY(), 0);
        assertEquals("maxY", 1.5, br.getMaxY(), 0);

        try
        {
            new Bounds2d(Double.NaN, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(0, Double.NaN);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(-3, 0);
            fail("Negative x-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(0, -3);
            fail("Negative y-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        br = new Bounds2d(20, 30);
        assertEquals("minX", -10, br.getMinX(), 0);
        assertEquals("maxX", 10, br.getMaxX(), 0);
        assertEquals("minY", -15, br.getMinY(), 0);
        assertEquals("maxY", 15, br.getMaxY(), 0);
        assertEquals("deltaX", 20, br.getDeltaX(), 0);
        assertEquals("deltaY", 30, br.getDeltaY(), 0);
        assertEquals("volume", 20 * 30, br.getArea(), 0);
        assertFalse("contains does not include boundaries", br.contains(-10, 0));
        assertFalse("contains does not include boundaries", br.contains(10, 0));
        assertFalse("contains does not include boundaries", br.contains(0, -15));
        assertFalse("contains does not include boundaries", br.contains(0, 15));
        assertTrue("contains", br.contains(-0.999, 0));
        assertTrue("contains", br.contains(0.999, 0));
        assertTrue("contains", br.contains(0, -14.999));
        assertTrue("contains", br.contains(0, 14.999));
        assertTrue("covers includes boundaries", br.covers(-10, 0));
        assertTrue("covers includes boundaries", br.covers(10, 0));
        assertTrue("covers includes boundaries", br.covers(0, -15));
        assertTrue("covers includes boundaries", br.covers(0, 15));
        assertFalse("covers", br.covers(-10.001, 0));
        assertFalse("covers", br.covers(10.001, 0));
        assertFalse("covers", br.covers(0, -15.001));
        assertFalse("covers", br.covers(0, 15.001));

        Collection<Point2d> pointCollection = new ArrayList<>();
        try
        {
            new Bounds2d(pointCollection);
            fail("Empty point collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        pointCollection.add(new Point2d(10, 20));
        br = new Bounds2d(pointCollection);
        assertEquals("minX", 10, br.getAbsoluteMinX(), 0);
        assertEquals("maxX", 10, br.getAbsoluteMaxX(), 0);
        assertEquals("minY", 20, br.getAbsoluteMinY(), 0);
        assertEquals("maxY", 20, br.getAbsoluteMaxY(), 0);

        pointCollection.add(new Point2d(-5, -6));
        br = new Bounds2d(pointCollection);
        assertEquals("minX", -5, br.getAbsoluteMinX(), 0);
        assertEquals("maxX", 10, br.getAbsoluteMaxX(), 0);
        assertEquals("minY", -6, br.getAbsoluteMinY(), 0);
        assertEquals("maxY", 20, br.getAbsoluteMaxY(), 0);

        assertTrue("toString returns something descriptive", br.toString().startsWith("Bounds2d "));
        assertEquals("toString with false argument produces same as toString with no argument", br.toString(),
                br.toString(false));
        assertTrue("toString with true argument produces rhs of toString with no argument",
                br.toString().indexOf(br.toString(true)) > 0);

        pointCollection.add(new Point2d(40, 50));
        // This collection is an ArrayList, so the elements are stored in the order in which they were added
        br = new Bounds2d(pointCollection);
        assertEquals("minX", -5, br.getAbsoluteMinX(), 0);
        assertEquals("maxX", 40, br.getAbsoluteMaxX(), 0);
        assertEquals("minY", -6, br.getAbsoluteMinY(), 0);
        assertEquals("maxY", 50, br.getAbsoluteMaxY(), 0);

        br = new Bounds2d(pointCollection.toArray((new Point2d[0])));
        assertEquals("minX", -5, br.getAbsoluteMinX(), 0);
        assertEquals("maxX", 40, br.getAbsoluteMaxX(), 0);
        assertEquals("minY", -6, br.getAbsoluteMinY(), 0);
        assertEquals("maxY", 50, br.getAbsoluteMaxY(), 0);

        PolyLine2d line = new PolyLine2d(new Point2d(1, 12), new Point2d(3, 12), new Point2d(2, 11));
        br = new Bounds2d(line);
        assertEquals("minX", 1, br.getAbsoluteMinX(), 0);
        assertEquals("minY", 11, br.getAbsoluteMinY(), 0);
        assertEquals("maxX", 3, br.getAbsoluteMaxX(), 0);
        assertEquals("maxY", 12, br.getAbsoluteMaxY(), 0);

        assertEquals("bounding box of reversed line", br, new Bounds2d(line.reverse()));

        Point2d p2d = new Point2d(123, 456);
        br = new Bounds2d(p2d);
        assertEquals("minX", 123, br.getAbsoluteMinX(), 0);
        assertEquals("maxX", 123, br.getAbsoluteMaxX(), 0);
        assertEquals("minY", 456, br.getAbsoluteMinY(), 0);
        assertEquals("maxY", 456, br.getAbsoluteMaxY(), 0);
        assertFalse("contains does not include boundaries", br.contains(p2d));
        assertTrue("covers includes boundaries", br.covers(p2d));

        try
        {
            new Bounds2d((Point2d) null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        assertEquals("Size of a Bounds2d is always 4", 4, br.size());
    }

    /**
     * Test various methods of a Bounds2d.
     * @throws DrawException when that happens uncaught; this test has failed
     * @throws IllegalArgumentException when that happens uncaught; this test has failed
     * @throws NullPointerException when that happens uncaught; this test has failed
     */
    @Test
    @SuppressWarnings("unlikely-arg-type")
    public void methodTest() throws NullPointerException, IllegalArgumentException, DrawException
    {
        PolyLine2d l2d = new PolyLine2d(new Point2d(10, 10), new Point2d(30, -20), new Point2d(-40, 100));
        Bounds2d br = new Bounds2d(l2d);
        assertEquals("minX", -40, br.getAbsoluteMinX(), 0);
        assertEquals("maxX", 30, br.getAbsoluteMaxX(), 0);
        assertEquals("minY", -20, br.getAbsoluteMinY(), 0);
        assertEquals("maxY", 100, br.getAbsoluteMaxY(), 0);

        Point2d midPoint = br.midPoint();
        assertEquals("midPoint x", (br.getAbsoluteMinX() + br.getAbsoluteMaxX()) / 2, midPoint.x, 0);
        assertEquals("midPoint y", (br.getAbsoluteMinY() + br.getAbsoluteMaxY()) / 2, midPoint.y, 0);
        assertEquals("midPoint of bounds of point is point", midPoint, new Bounds2d(midPoint).midPoint());

        try
        {
            br.contains(Double.NaN, 0);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            br.contains(0, Double.NaN);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertFalse("boundingbox does not contain itself", br.contains(br));
        Bounds2d br2 = new Bounds2d(br.getAbsoluteMinX() - 0.0001, br.getAbsoluteMaxX() + 0.0001, br.getAbsoluteMinY() - 0.0001,
                br.getAbsoluteMaxY() + 0.0001);
        assertTrue("Slightly enlarged bounding box contains non-enlarged version", br2.contains(br));

        try
        {
            br.covers((Bounds2d) null);
            fail("Should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            br.covers(Double.NaN, 0);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            br.covers(0, Double.NaN);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertTrue("Bounds2d covers itself", br.covers(br));
        assertFalse("Bounds2d does not cover slightly enlarged version of itself", br.covers(br2));
        br2 = new Bounds2d(br.getAbsoluteMinX() + 0.0001, br.getAbsoluteMaxX() + 0.0001, br.getAbsoluteMinY() + 0.0001,
                br.getAbsoluteMaxY() + 0.0001);
        assertFalse("Bounds2d does not cover slightly moved version of itself", br.covers(br2));

        assertFalse("Overlapping Bounds2d is not disjoint", br.disjoint(br2));
        assertTrue("Overlapping Bounds2d is not disjoint", br.intersects(br2));

        br2 = new Bounds2d(br.getAbsoluteMinX() + 1000, br.getAbsoluteMaxX() + 1000, br.getAbsoluteMinY() + 1000,
                br.getAbsoluteMaxY() + 1000);
        assertFalse("No intersection", br.intersects(br2));
        assertTrue("Disjoint", br.disjoint(br2));
        br2 = new Bounds2d(br.getAbsoluteMaxX(), br.getAbsoluteMaxX() + 0.0001, br.getAbsoluteMinY() + 0.0001,
                br.getAbsoluteMaxY() + 0.0001);
        assertTrue("Only touching at vertical line is disjoint", br.disjoint(br2));
        assertTrue("Only touching at vertical line is disjoint", br2.disjoint(br));

        try
        {
            br.intersection(null);
            fail("Should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        double[] shifts = new double[] { -200, -5, 0, 5, 200 };
        for (double dx : shifts)
        {
            for (double dy : shifts)
            {
                br2 = new Bounds2d(br.getAbsoluteMinX() + dx, br.getAbsoluteMaxX() + dx, br.getAbsoluteMinY() + dy,
                        br.getAbsoluteMaxY() + dy);
                Bounds2d intersection = br.intersection(br2);
                if (Math.abs(dx) >= 200 || Math.abs(dy) >= 200)
                {
                    assertNull("intersection is null", intersection);
                }
                else
                {
                    assertEquals("min x", Math.max(br.getAbsoluteMinX(), br2.getAbsoluteMinX()), intersection.getAbsoluteMinX(),
                            0);
                    assertEquals("max x", Math.min(br.getAbsoluteMaxX(), br2.getAbsoluteMaxX()), intersection.getAbsoluteMaxX(),
                            0);
                    assertEquals("min y", Math.max(br.getAbsoluteMinY(), br2.getAbsoluteMinY()), intersection.getAbsoluteMinY(),
                            0);
                    assertEquals("max y", Math.min(br.getAbsoluteMaxY(), br2.getAbsoluteMaxY()), intersection.getAbsoluteMaxY(),
                            0);
                }
            }
        }
        Rectangle2D r2D = br.toRectangle2D();
        assertEquals("x", r2D.getX(), br.getAbsoluteMinX(), 0);
        assertEquals("y", r2D.getY(), br.getAbsoluteMinY(), 0);
        assertEquals("w", r2D.getWidth(), br.getDeltaX(), 0.000001);
        assertEquals("h", r2D.getHeight(), br.getDeltaY(), 0.000001);
        assertEquals("getBounds returns this", br, br.getBounds());
        assertNotEquals("HashCode uses minX", br.hashCode(),
                new Bounds2d(br.getAbsoluteMinX() + 1, br.getAbsoluteMaxX(), br.getAbsoluteMinY(), br.getAbsoluteMaxY()));
        assertNotEquals("HashCode uses maxX", br.hashCode(),
                new Bounds2d(br.getAbsoluteMinX(), br.getAbsoluteMaxX() + 1, br.getAbsoluteMinY(), br.getAbsoluteMaxY()));
        assertNotEquals("HashCode uses minY", br.hashCode(),
                new Bounds2d(br.getAbsoluteMinX(), br.getAbsoluteMaxX(), br.getAbsoluteMinY() + 1, br.getAbsoluteMaxY()));
        assertNotEquals("HashCode uses maxY", br.hashCode(),
                new Bounds2d(br.getAbsoluteMinX(), br.getAbsoluteMaxX(), br.getAbsoluteMinY(), br.getAbsoluteMaxY() + 1));

        assertFalse("equals checks for null", br.equals(null));
        assertFalse("equals checks for different kind of object", br.equals("string"));
        assertFalse("equals checks minX", br.equals(
                new Bounds2d(br.getAbsoluteMinX() + 1, br.getAbsoluteMaxX(), br.getAbsoluteMinY(), br.getAbsoluteMaxY())));
        assertFalse("equals checks maxX", br.equals(
                new Bounds2d(br.getAbsoluteMinX(), br.getAbsoluteMaxX() + 1, br.getAbsoluteMinY(), br.getAbsoluteMaxY())));
        assertFalse("equals checks minY", br.equals(
                new Bounds2d(br.getAbsoluteMinX(), br.getAbsoluteMaxX(), br.getAbsoluteMinY() + 1, br.getAbsoluteMaxY())));
        assertFalse("equals checks maxy", br.equals(
                new Bounds2d(br.getAbsoluteMinX(), br.getAbsoluteMaxX(), br.getAbsoluteMinY(), br.getAbsoluteMaxY() + 1)));
        assertTrue("equals to copy of itself", br.equals(new Bounds2d(br)));
    }

}
