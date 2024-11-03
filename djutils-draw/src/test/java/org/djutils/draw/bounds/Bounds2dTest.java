package org.djutils.draw.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable2d;
import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * Bounds2dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Bounds2dTest
{

    /**
     * Test the bounding rectangle constructor.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException if that happens uncaught; this test has failed
     */
    @Test
    public void constructorTest() throws IllegalArgumentException, DrawRuntimeException
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

        try
        {
            new Bounds2d(new Drawable2d[] {});
            fail("Empty array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        Bounds2d br = new Bounds2d(1, 2, 3, 6);
        assertEquals(1, br.getMinX(), 0, "minX");
        assertEquals(2, br.getMaxX(), 0, "maxX");
        assertEquals(3, br.getMinY(), 0, "minY");
        assertEquals(6, br.getMaxY(), 0, "maxY");

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
        assertEquals(20, br.getDeltaX(), 0, "deltaX");
        assertEquals(30, br.getDeltaY(), 0, "deltaY");
        assertEquals(20 * 30, br.getArea(), 0, "volume");
        assertFalse(br.contains(-10, 0), "contains does not include boundaries");
        assertFalse(br.contains(10, 0), "contains does not include boundaries");
        assertFalse(br.contains(0, -15), "contains does not include boundaries");
        assertFalse(br.contains(0, 15), "contains does not include boundaries");
        assertTrue(br.contains(-0.999, 0), "contains");
        assertTrue(br.contains(0.999, 0), "contains");
        assertTrue(br.contains(0, -14.999), "contains");
        assertTrue(br.contains(0, 14.999), "contains");
        assertTrue(br.covers(-10, 0), "covers includes boundaries");
        assertTrue(br.covers(10, 0), "covers includes boundaries");
        assertTrue(br.covers(0, -15), "covers includes boundaries");
        assertTrue(br.covers(0, 15), "covers includes boundaries");
        assertFalse(br.covers(-10.001, 0), "covers");
        assertFalse(br.covers(10.001, 0), "covers");
        assertFalse(br.covers(0, -15.001), "covers");
        assertFalse(br.covers(0, 15.001), "covers");

        Collection<Drawable2d> drawable2dCollection = new ArrayList<>();
        try
        {
            new Bounds2d(drawable2dCollection);
            fail("Empty drawable collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        drawable2dCollection.add(null);
        try
        {
            new Bounds2d(drawable2dCollection);
            fail("null element in collection should have thrown an NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }
        drawable2dCollection.clear();

        drawable2dCollection.add(new Point2d(10, 20));
        br = new Bounds2d(drawable2dCollection);
        assertEquals(10, br.getMinX(), 0, "minX");
        assertEquals(10, br.getMaxX(), 0, "maxX");
        assertEquals(20, br.getMinY(), 0, "minY");
        assertEquals(20, br.getMaxY(), 0, "maxY");

        drawable2dCollection.add(new Point2d(-5, -6));
        br = new Bounds2d(drawable2dCollection);
        assertEquals(-5, br.getMinX(), 0, "minX");
        assertEquals(10, br.getMaxX(), 0, "maxX");
        assertEquals(-6, br.getMinY(), 0, "minY");
        assertEquals(20, br.getMaxY(), 0, "maxY");

        drawable2dCollection.add(new LineSegment2d(20, 30, 40, 50));
        br = new Bounds2d(drawable2dCollection);
        assertEquals(-5, br.getMinX(), 0, "minX");
        assertEquals(40, br.getMaxX(), 0, "maxX");
        assertEquals(-6, br.getMinY(), 0, "minY");
        assertEquals(50, br.getMaxY(), 0, "maxY");

        assertTrue(br.toString().startsWith("Bounds2d "), "toString returns something descriptive");
        assertEquals(br.toString(), br.toString(false),
                "toString with false argument produces same as toString with no argument");
        assertTrue(br.toString().indexOf(br.toString(true)) > 0,
                "toString with true argument produces rhs of toString with no argument");

        drawable2dCollection.add(new Point2d(40, 50));
        // This collection is an ArrayList, so the elements are stored in the order in which they were added
        br = new Bounds2d(drawable2dCollection);
        assertEquals(-5, br.getMinX(), 0, "minX");
        assertEquals(40, br.getMaxX(), 0, "maxX");
        assertEquals(-6, br.getMinY(), 0, "minY");
        assertEquals(50, br.getMaxY(), 0, "maxY");

        br = new Bounds2d(drawable2dCollection.toArray((new Drawable2d[0])));
        assertEquals(-5, br.getMinX(), 0, "minX");
        assertEquals(40, br.getMaxX(), 0, "maxX");
        assertEquals(-6, br.getMinY(), 0, "minY");
        assertEquals(50, br.getMaxY(), 0, "maxY");

        drawable2dCollection.add(null);
        try
        {
            new Bounds2d(drawable2dCollection);
            fail("null element in collection should have thrown an NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        PolyLine2d line = new PolyLine2d(new Point2d(1, 12), new Point2d(3, 12), new Point2d(2, 11));
        br = new Bounds2d(line);
        assertEquals(1, br.getMinX(), 0, "minX");
        assertEquals(11, br.getMinY(), 0, "minY");
        assertEquals(3, br.getMaxX(), 0, "maxX");
        assertEquals(12, br.getMaxY(), 0, "maxY");

        assertEquals(br, new Bounds2d(line.reverse()), "bounding box of reversed line");

        Point2d p2d = new Point2d(123, 456);
        br = new Bounds2d(p2d);
        assertEquals(123, br.getMinX(), 0, "minX");
        assertEquals(123, br.getMaxX(), 0, "maxX");
        assertEquals(456, br.getMinY(), 0, "minY");
        assertEquals(456, br.getMaxY(), 0, "maxY");
        assertFalse(br.contains(p2d), "contains does not include boundaries");
        assertTrue(br.covers(p2d), "covers includes boundaries");

        try
        {
            new Bounds2d((Point2d) null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        assertEquals(4, br.size(), "Size of a Bounds2d is always 4");

        br = new Bounds2d(line, p2d);
        assertEquals(1, br.getMinX(), 0, "minX");
        assertEquals(11, br.getMinY(), 0, "minY");
        assertEquals(123, br.getMaxX(), 0, "maxX");
        assertEquals(456, br.getMaxY(), 0, "maxY");

        br = new Bounds2d(p2d, line);
        assertEquals(1, br.getMinX(), 0, "minX");
        assertEquals(11, br.getMinY(), 0, "minY");
        assertEquals(123, br.getMaxX(), 0, "maxX");
        assertEquals(456, br.getMaxY(), 0, "maxY");

        br = new Bounds2d(line, line);
        assertEquals(1, br.getMinX(), 0, "minX");
        assertEquals(11, br.getMinY(), 0, "minY");
        assertEquals(3, br.getMaxX(), 0, "maxX");
        assertEquals(12, br.getMaxY(), 0, "maxY");

        try
        {
            new Bounds2d(line, p2d, null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds2d(new Iterator<Point2d>()
            {

                @Override
                public boolean hasNext()
                {
                    return false;
                }

                @Override
                public Point2d next()
                {
                    return null;
                }
            });
            fail("iterator that yields zero points should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

    }

    /**
     * Test various methods of a Bounds2d.
     * @throws DrawRuntimeException when that happens uncaught; this test has failed
     * @throws IllegalArgumentException when that happens uncaught; this test has failed
     * @throws NullPointerException when that happens uncaught; this test has failed
     */
    @Test
    @SuppressWarnings("unlikely-arg-type")
    public void methodTest() throws NullPointerException, IllegalArgumentException, DrawRuntimeException
    {
        PolyLine2d l2d = new PolyLine2d(new Point2d(10, 10), new Point2d(30, -20), new Point2d(-40, 100));
        Bounds2d br = new Bounds2d(l2d);
        assertEquals(-40, br.getMinX(), 0, "minX");
        assertEquals(30, br.getMaxX(), 0, "maxX");
        assertEquals(-20, br.getMinY(), 0, "minY");
        assertEquals(100, br.getMaxY(), 0, "maxY");

        Point2d midPoint = br.midPoint();
        assertEquals((br.getMinX() + br.getMaxX()) / 2, midPoint.x, 0, "midPoint x");
        assertEquals((br.getMinY() + br.getMaxY()) / 2, midPoint.y, 0, "midPoint y");
        assertEquals(midPoint, new Bounds2d(midPoint).midPoint(), "midPoint of bounds of point is point");

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

        assertFalse(br.contains(br), "boundingbox does not contain itself");
        Bounds2d br2 = new Bounds2d(br.getMinX() - 0.0001, br.getMaxX() + 0.0001, br.getMinY() - 0.0001, br.getMaxY() + 0.0001);
        assertTrue(br2.contains(br), "Slightly enlarged bounding box contains non-enlarged version");

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

        assertTrue(br.covers(br), "Bounds2d covers itself");
        assertFalse(br.covers(br2), "Bounds2d does not cover slightly enlarged version of itself");
        br2 = new Bounds2d(br.getMinX() + 0.0001, br.getMaxX() + 0.0001, br.getMinY() + 0.0001, br.getMaxY() + 0.0001);
        assertFalse(br.covers(br2), "Bounds2d does not cover slightly moved version of itself");

        assertFalse(br.disjoint(br2), "Overlapping Bounds2d is not disjoint");
        assertTrue(br.intersects(br2), "Overlapping Bounds2d is not disjoint");

        br2 = new Bounds2d(br.getMinX() + 1000, br.getMaxX() + 1000, br.getMinY() + 1000, br.getMaxY() + 1000);
        assertFalse(br.intersects(br2), "No intersection");
        assertTrue(br.disjoint(br2), "Disjoint");
        br2 = new Bounds2d(br.getMaxX(), br.getMaxX() + 0.0001, br.getMinY() + 0.0001, br.getMaxY() + 0.0001);
        assertFalse(br.disjoint(br2), "Only touching at vertical line is not disjoint");
        assertFalse(br2.disjoint(br), "Only touching at vertical line is not disjoint");

        try
        {
            br.intersection(null);
            fail("Should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        double[] shifts = new double[] {-200, -5, 0, 5, 200};
        for (double dx : shifts)
        {
            for (double dy : shifts)
            {
                br2 = new Bounds2d(br.getMinX() + dx, br.getMaxX() + dx, br.getMinY() + dy, br.getMaxY() + dy);
                Bounds2d intersection = br.intersection(br2);
                if (Math.abs(dx) >= 200 || Math.abs(dy) >= 200)
                {
                    assertNull(intersection, "intersection is null");
                }
                else
                {
                    assertEquals(Math.max(br.getMinX(), br2.getMinX()), intersection.getMinX(), 0, "min x");
                    assertEquals(Math.min(br.getMaxX(), br2.getMaxX()), intersection.getMaxX(), 0, "max x");
                    assertEquals(Math.max(br.getMinY(), br2.getMinY()), intersection.getMinY(), 0, "min y");
                    assertEquals(Math.min(br.getMaxY(), br2.getMaxY()), intersection.getMaxY(), 0, "max y");
                }
            }
        }
        Rectangle2D r2D = br.toRectangle2D();
        assertEquals(r2D.getX(), br.getMinX(), 0, "x");
        assertEquals(r2D.getY(), br.getMinY(), 0, "y");
        assertEquals(r2D.getWidth(), br.getDeltaX(), 0.000001, "w");
        assertEquals(r2D.getHeight(), br.getDeltaY(), 0.000001, "h");
        assertEquals(br, br.getBounds(), "getBounds returns this");
        assertNotEquals(br.hashCode(), new Bounds2d(br.getMinX() + 1, br.getMaxX(), br.getMinY(), br.getMaxY()),
                "HashCode uses minX");
        assertNotEquals(br.hashCode(), new Bounds2d(br.getMinX(), br.getMaxX() + 1, br.getMinY(), br.getMaxY()),
                "HashCode uses maxX");
        assertNotEquals(br.hashCode(), new Bounds2d(br.getMinX(), br.getMaxX(), br.getMinY() + 1, br.getMaxY()),
                "HashCode uses minY");
        assertNotEquals(br.hashCode(), new Bounds2d(br.getMinX(), br.getMaxX(), br.getMinY(), br.getMaxY() + 1),
                "HashCode uses maxY");

        assertFalse(br.equals(null), "equals checks for null");
        assertFalse(br.equals("string"), "equals checks for different kind of object");
        assertFalse(br.equals(new Bounds2d(br.getMinX() + 1, br.getMaxX(), br.getMinY(), br.getMaxY())), "equals checks minX");
        assertFalse(br.equals(new Bounds2d(br.getMinX(), br.getMaxX() + 1, br.getMinY(), br.getMaxY())), "equals checks maxX");
        assertFalse(br.equals(new Bounds2d(br.getMinX(), br.getMaxX(), br.getMinY() + 1, br.getMaxY())), "equals checks minY");
        assertFalse(br.equals(new Bounds2d(br.getMinX(), br.getMaxX(), br.getMinY(), br.getMaxY() + 1)), "equals checks maxy");
        assertTrue(br.equals(new Bounds2d(br)), "equals to copy of itself");
    }

}
