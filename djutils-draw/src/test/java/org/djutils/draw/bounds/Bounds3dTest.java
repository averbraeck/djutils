package org.djutils.draw.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Drawable3d;
import org.djutils.draw.line.LineSegment3d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.Point3d;
import org.junit.jupiter.api.Test;

/**
 * Bounds3dText.java.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds3dTest
{

    /**
     * Test the bounding box constructor.
     * @throws DrawRuntimeException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException on unexpected error
     */
    @Test
    public void constructorTest() throws IllegalArgumentException, DrawRuntimeException
    {
        try
        {
            new Bounds3d(Double.NaN, 0, 0, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, Double.NaN, 0, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, Double.NaN, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, 0, Double.NaN, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, 0, 0, Double.NaN, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, 0, 0, 0, Double.NaN);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(2, -2, 0, 0, 0, 0);
            fail("Negative x-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, 2, -2, 0, 0);
            fail("Negative y-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, 0, 0, 2, -2);
            fail("Negative z-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(new Drawable3d[] {});
            fail("Empty array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        Bounds3d bb = new Bounds3d(1, 2, 3, 6, 5, 10);
        assertEquals(1, bb.getMinX(), 0, "minX");
        assertEquals(2, bb.getMaxX(), 0, "maxX");
        assertEquals(3, bb.getMinY(), 0, "minY");
        assertEquals(6, bb.getMaxY(), 0, "maxY");
        assertEquals(5, bb.getMinZ(), 0, "minZ");
        assertEquals(10, bb.getMaxZ(), 0, "maxZ");

        try
        {
            new Bounds3d(Double.NaN, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, Double.NaN, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, Double.NaN);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(-3, 0, 0);
            fail("Negative x-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, -3, 0);
            fail("Negative y-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(0, 0, -3);
            fail("Negative z-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        bb = new Bounds3d(20, 30, 40);
        assertEquals(-10, bb.getMinX(), 0, "minX");
        assertEquals(10, bb.getMaxX(), 0, "maxX");
        assertEquals(-15, bb.getMinY(), 0, "minY");
        assertEquals(15, bb.getMaxY(), 0, "maxY");
        assertEquals(-20, bb.getMinZ(), 0, "minZ");
        assertEquals(20, bb.getMaxZ(), 0, "maxZ");
        assertEquals(20, bb.getDeltaX(), 0, "deltaX");
        assertEquals(30, bb.getDeltaY(), 0, "deltaY");
        assertEquals(40, bb.getDeltaZ(), 0, "deltaZ");
        assertEquals(20 * 30 * 40, bb.getVolume(), 0, "volume");
        assertFalse(bb.contains(-10, 0, 0), "contains does not include boundaries");
        assertFalse(bb.contains(10, 0, 0), "contains does not include boundaries");
        assertFalse(bb.contains(0, -15, 0), "contains does not include boundaries");
        assertFalse(bb.contains(0, 15, 0), "contains does not include boundaries");
        assertFalse(bb.contains(0, 0, -20), "contains does not include boundaries");
        assertFalse(bb.contains(0, 0, 20), "contains does not include boundaries");
        assertTrue(bb.contains(-0.999, 0, 0), "contains");
        assertTrue(bb.contains(0.999, 0, 0), "contains");
        assertTrue(bb.contains(0, -14.999, 0), "contains");
        assertTrue(bb.contains(0, 14.999, 0), "contains");
        assertTrue(bb.contains(0, 0, -19.999), "contains");
        assertTrue(bb.contains(0, 0, 19.999), "contains");
        assertTrue(bb.covers(-10, 0, 0), "covers includes boundaries");
        assertTrue(bb.covers(10, 0, 0), "covers includes boundaries");
        assertTrue(bb.covers(0, -15, 0), "covers includes boundaries");
        assertTrue(bb.covers(0, 15, 0), "covers includes boundaries");
        assertTrue(bb.covers(0, 0, -20), "covers includes boundaries");
        assertTrue(bb.covers(0, 0, 20), "covers includes boundaries");
        assertFalse(bb.covers(-10.001, 0, 0), "covers");
        assertFalse(bb.covers(10.001, 0, 0), "covers");
        assertFalse(bb.covers(0, -15.001, 0), "covers");
        assertFalse(bb.covers(0, 15.001, 0), "covers");
        assertFalse(bb.covers(0, 0, -20.001), "covers");
        assertFalse(bb.covers(0, 0, 20.001), "covers");

        Collection<Drawable3d> drawable3dCollection = new ArrayList<>();
        try
        {
            new Bounds3d(drawable3dCollection);
            fail("Empty point collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        drawable3dCollection.add(new Point3d(10, 20, 30));
        bb = new Bounds3d(drawable3dCollection);
        assertEquals(10, bb.getMinX(), 0, "minX");
        assertEquals(10, bb.getMaxX(), 0, "maxX");
        assertEquals(20, bb.getMinY(), 0, "minY");
        assertEquals(20, bb.getMaxY(), 0, "maxY");
        assertEquals(30, bb.getMinZ(), 0, "minZ");
        assertEquals(30, bb.getMaxZ(), 0, "maxZ");

        drawable3dCollection.add(new Point3d(-5, -6, -7));
        bb = new Bounds3d(drawable3dCollection);
        assertEquals(-5, bb.getMinX(), 0, "minX");
        assertEquals(10, bb.getMaxX(), 0, "maxX");
        assertEquals(-6, bb.getMinY(), 0, "minY");
        assertEquals(20, bb.getMaxY(), 0, "maxY");
        assertEquals(-7, bb.getMinZ(), 0, "minZ");
        assertEquals(30, bb.getMaxZ(), 0, "maxZ");

        drawable3dCollection.add(new LineSegment3d(20, 30, 40, 40, 50, 60));
        bb = new Bounds3d(drawable3dCollection);
        assertEquals(-5, bb.getMinX(), 0, "minX");
        assertEquals(40, bb.getMaxX(), 0, "maxX");
        assertEquals(-6, bb.getMinY(), 0, "minY");
        assertEquals(50, bb.getMaxY(), 0, "maxY");
        assertEquals(-7, bb.getMinZ(), 0, "minZ");
        assertEquals(60, bb.getMaxZ(), 0, "maxZ");

        assertTrue(bb.toString().startsWith("Bounds3d "), "toString returns something descriptive");
        assertEquals(bb.toString(), bb.toString(false),
                "toString with false argument produces same as toString with no argument");
        assertTrue(bb.toString().indexOf(bb.toString(true)) > 0,
                "toString with true argument produces rhs of toString with no argument");

        drawable3dCollection.add(new Point3d(40, 50, 60));
        // This collection is an ArrayList, so the elements are stored in the order in which they were added
        bb = new Bounds3d(drawable3dCollection);
        assertEquals(-5, bb.getMinX(), 0, "minX");
        assertEquals(40, bb.getMaxX(), 0, "maxX");
        assertEquals(-6, bb.getMinY(), 0, "minY");
        assertEquals(50, bb.getMaxY(), 0, "maxY");
        assertEquals(-7, bb.getMinZ(), 0, "minZ");
        assertEquals(60, bb.getMaxZ(), 0, "maxZ");

        bb = new Bounds3d(drawable3dCollection.toArray((new Drawable3d[0])));
        assertEquals(-5, bb.getMinX(), 0, "minX");
        assertEquals(40, bb.getMaxX(), 0, "maxX");
        assertEquals(-6, bb.getMinY(), 0, "minY");
        assertEquals(50, bb.getMaxY(), 0, "maxY");
        assertEquals(-7, bb.getMinZ(), 0, "minZ");
        assertEquals(60, bb.getMaxZ(), 0, "maxZ");

        drawable3dCollection.add(null);
        try
        {
            new Bounds3d(drawable3dCollection);
            fail("null element in collection should have thrown an NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        Bounds3d bb2 = new Bounds3d(-100, -90, -100, -90, -100, -90);
        assertNull(bb.intersection(bb2), "empty bounding box");

        PolyLine3d line = new PolyLine3d(new Point3d(1, 12, 23), new Point3d(3, 12, 21), new Point3d(2, 11, 23));
        bb = new Bounds3d(line);
        assertEquals(1, bb.getMinX(), 0, "minX");
        assertEquals(11, bb.getMinY(), 0, "minY");
        assertEquals(21, bb.getMinZ(), 0, "minZ");
        assertEquals(3, bb.getMaxX(), 0, "maxX");
        assertEquals(12, bb.getMaxY(), 0, "maxY");
        assertEquals(23, bb.getMaxZ(), 0, "maxZ");

        assertEquals(bb, new Bounds3d(line.reverse()), "bounding box of reversed line");

        Point3d p3d = new Point3d(123, 456, 789);
        bb = new Bounds3d(p3d);
        assertEquals(123, bb.getMinX(), 0, "minX");
        assertEquals(123, bb.getMaxX(), 0, "maxX");
        assertEquals(456, bb.getMinY(), 0, "minY");
        assertEquals(456, bb.getMaxY(), 0, "maxY");
        assertEquals(789, bb.getMinZ(), 0, "minZ");
        assertEquals(789, bb.getMaxZ(), 0, "maxZ");
        assertFalse(bb.contains(p3d), "contains does not include boundaries");
        assertTrue(bb.covers(p3d), "covers includes boundaries");

        try
        {
            new Bounds3d((Point3d) null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        assertEquals(8, bb.size(), "size of a Bounds3d is always 8");

        bb = new Bounds3d(line, p3d);
        assertEquals(1, bb.getMinX(), 0, "minX");
        assertEquals(11, bb.getMinY(), 0, "minY");
        assertEquals(123, bb.getMaxX(), 0, "maxX");
        assertEquals(456, bb.getMaxY(), 0, "maxY");
        assertEquals(21, bb.getMinZ(), 0, "minZ");
        assertEquals(789, bb.getMaxZ(), 0, "maxZ");

        bb = new Bounds3d(p3d, line);
        assertEquals(1, bb.getMinX(), 0, "minX");
        assertEquals(11, bb.getMinY(), 0, "minY");
        assertEquals(123, bb.getMaxX(), 0, "maxX");
        assertEquals(456, bb.getMaxY(), 0, "maxY");
        assertEquals(21, bb.getMinZ(), 0, "minZ");
        assertEquals(789, bb.getMaxZ(), 0, "maxZ");

        bb = new Bounds3d(line, line);
        assertEquals(1, bb.getMinX(), 0, "minX");
        assertEquals(11, bb.getMinY(), 0, "minY");
        assertEquals(3, bb.getMaxX(), 0, "maxX");
        assertEquals(12, bb.getMaxY(), 0, "maxY");
        assertEquals(21, bb.getMinZ(), 0, "minZ");
        assertEquals(23, bb.getMaxZ(), 0, "maxZ");

        try
        {
            new Bounds3d(line, p3d, null);
            fail("Null parameter should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Bounds3d(new Iterator<Point3d>()
            {

                @Override
                public boolean hasNext()
                {
                    return false;
                }

                @Override
                public Point3d next()
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
    public void methodTest() throws NullPointerException, IllegalArgumentException, DrawRuntimeException
    {
        PolyLine3d l3d = new PolyLine3d(new Point3d(10, 10, 10), new Point3d(30, -20, 40), new Point3d(-40, 100, 0));
        Bounds3d bb = new Bounds3d(l3d);
        assertEquals(-40, bb.getMinX(), 0, "minX");
        assertEquals(30, bb.getMaxX(), 0, "maxX");
        assertEquals(-20, bb.getMinY(), 0, "minY");
        assertEquals(100, bb.getMaxY(), 0, "maxY");
        assertEquals(0, bb.getMinZ(), 0, "minZ");
        assertEquals(40, bb.getMaxZ(), 0, "maxZ");

        Point3d midPoint = bb.midPoint();
        assertEquals((bb.getMinX() + bb.getMaxX()) / 2, midPoint.x, 0, "midPoint x");
        assertEquals((bb.getMinY() + bb.getMaxY()) / 2, midPoint.y, 0, "midPoint y");
        assertEquals((bb.getMinZ() + bb.getMaxZ()) / 2, midPoint.z, 0, "midPoint z");
        assertEquals(midPoint, new Bounds3d(midPoint).midPoint(), "midPoint of bounds of point is point");

        try
        {
            bb.contains(Double.NaN, 0, 0);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            bb.contains(0, Double.NaN, 0);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            bb.contains(0, 0, Double.NaN);
            fail("NaN should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertFalse(bb.contains(bb), "boundingbox does not contain itself");
        Bounds3d bb2 = new Bounds3d(bb.getMinX() - 0.0001, bb.getMaxX() + 0.0001, bb.getMinY() - 0.0001, bb.getMaxY() + 0.0001,
                bb.getMinZ() - 0.0001, bb.getMaxZ() + 0.0001);
        assertTrue(bb2.contains(bb), "Slightly enlarged bounding box contains non-enlarged version");

        try
        {
            bb.covers((Bounds3d) null);
            fail("Should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            bb.covers(Double.NaN, 0, 0);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            bb.covers(0, Double.NaN, 0);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            bb.covers(0, 0, Double.NaN);
            fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        assertTrue(bb.covers(bb), "Bounds2d covers itself");
        assertFalse(bb.covers(bb2), "Bounds2d does not cover slightly enlarged version of itself");
        bb2 = new Bounds3d(bb.getMinX() + 0.0001, bb.getMaxX() + 0.0001, bb.getMinY() + 0.0001, bb.getMaxY() + 0.0001,
                bb.getMinZ() + 0.0001, bb.getMaxZ() + 0.0001);
        assertFalse(bb.covers(bb2), "Bounds2d does not cover slightly moved version of itself");

        assertFalse(bb.disjoint(bb2), "Overlapping Bounds2d is not disjoint");
        assertTrue(bb.intersects(bb2), "Overlapping Bounds2d is not disjoint");
        bb2 = new Bounds3d(bb.getMinX() + 1000, bb.getMaxX() + 1000, bb.getMinY() + 1000, bb.getMaxY() + 1000,
                bb.getMinZ() + 1000, bb.getMaxZ() + 1000);
        assertFalse(bb.intersects(bb2), "No intersection");
        assertTrue(bb.disjoint(bb2), "Disjoint");
        bb2 = new Bounds3d(bb.getMaxX(), bb.getMaxX() + 0.0001, bb.getMinY() + 0.0001, bb.getMaxY() + 0.0001,
                bb.getMinZ() + 0.0001, bb.getMaxZ() + 0.0001);
        assertFalse(bb.disjoint(bb2), "Only touching at vertical line is not disjoint");
        assertFalse(bb2.disjoint(bb), "Only touching at vertical line is not disjoint");

        try
        {
            bb.intersection(null);
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
                for (double dz : shifts)
                {
                    bb2 = new Bounds3d(bb.getMinX() + dx, bb.getMaxX() + dx, bb.getMinY() + dy, bb.getMaxY() + dy,
                            bb.getMinZ() + dz, bb.getMaxZ() + dz);
                    Bounds3d intersection = bb.intersection(bb2);
                    if (Math.abs(dx) >= 200 || Math.abs(dy) >= 200 || Math.abs(dz) >= 200)
                    {
                        assertNull(intersection, "intersection is null");
                    }
                    else
                    {
                        assertEquals(Math.max(bb.getMinX(), bb2.getMinX()), intersection.getMinX(), 0, "min x");
                        assertEquals(Math.min(bb.getMaxX(), bb2.getMaxX()), intersection.getMaxX(), 0, "max x");
                        assertEquals(Math.max(bb.getMinY(), bb2.getMinY()), intersection.getMinY(), 0, "min y");
                        assertEquals(Math.min(bb.getMaxY(), bb2.getMaxY()), intersection.getMaxY(), 0, "max y");
                        assertEquals(Math.max(bb.getMinZ(), bb2.getMinZ()), intersection.getMinZ(), 0, "min z");
                        assertEquals(Math.min(bb.getMaxZ(), bb2.getMaxZ()), intersection.getMaxZ(), 0, "max z");
                    }
                }
            }
        }
        assertEquals(bb, bb.getBounds(), "getBounds returns this");
        assertNotEquals(bb.hashCode(), new Bounds3d(bb.getMinX() + 1, bb.getMaxX(), bb.getMinY(), bb.getMaxY(), bb.getMinZ(), bb.getMaxZ()),
                "HashCode uses minX");
        assertNotEquals(bb.hashCode(), new Bounds3d(bb.getMinX(), bb.getMaxX() + 1, bb.getMinY(), bb.getMaxY(), bb.getMinZ(), bb.getMaxZ()),
                "HashCode uses maxX");
        assertNotEquals(bb.hashCode(), new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY() + 1, bb.getMaxY(), bb.getMinZ(), bb.getMaxZ()),
                "HashCode uses minY");
        assertNotEquals(bb.hashCode(), new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY() + 1, bb.getMinZ(), bb.getMaxZ()),
                "HashCode uses maxY");
        assertNotEquals(bb.hashCode(), new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY(), bb.getMinZ() + 1, bb.getMaxZ()),
                "HashCode uses minZ");
        assertNotEquals(bb.hashCode(), new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY() + 1, bb.getMinZ(), bb.getMaxZ() + 1),
                "HashCode uses maxZ");

        assertFalse(bb.equals(null), "equals checks for null");
        assertFalse(bb.equals("string"), "equals checks for different kind of object");
        assertFalse(bb
                .equals(new Bounds3d(bb.getMinX() + 1, bb.getMaxX(), bb.getMinY(), bb.getMaxY(), bb.getMinZ(), bb.getMaxZ())), "equals checks minX");
        assertFalse(bb
                .equals(new Bounds3d(bb.getMinX(), bb.getMaxX() + 1, bb.getMinY(), bb.getMaxY(), bb.getMinZ(), bb.getMaxZ())), "equals checks maxX");
        assertFalse(bb
                .equals(new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY() + 1, bb.getMaxY(), bb.getMinZ(), bb.getMaxZ())), "equals checks minY");
        assertFalse(bb
                .equals(new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY() + 1, bb.getMinZ(), bb.getMaxZ())), "equals checks maxy");
        assertFalse(bb
                .equals(new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY(), bb.getMinZ() + 1, bb.getMaxZ())), "equals checks minZ");
        assertFalse(bb
                .equals(new Bounds3d(bb.getMinX(), bb.getMaxX(), bb.getMinY(), bb.getMaxY(), bb.getMinZ(), bb.getMaxZ() + 1)), "equals checks maxZ");
        assertTrue(bb.equals(new Bounds3d(bb)), "equals to copy of itself");

        Bounds2d projection = bb.project();
        assertEquals(projection.getMinX(), bb.getMinX(), 0, "projection minX");
        assertEquals(projection.getMaxX(), bb.getMaxX(), 0, "projection maxX");
        assertEquals(projection.getMinY(), bb.getMinY(), 0, "projection minY");
        assertEquals(projection.getMaxY(), bb.getMaxY(), 0, "projection maxY");
    }

}
