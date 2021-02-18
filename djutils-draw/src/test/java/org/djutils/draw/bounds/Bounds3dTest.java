package org.djutils.draw.bounds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.djutils.draw.DrawException;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * Bounds3dText.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds3dTest
{

    /**
     * Test the bounding box constructor.
     * @throws DrawException if that happens uncaught; this test has failed
     * @throws IllegalArgumentException on unexpected error
     */
    @Test
    public void constructorTest() throws IllegalArgumentException, DrawException
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

        Bounds3d bb = new Bounds3d(1, 2, 3, 6, 5, 10);
        assertEquals("minX", -0.5, bb.getMinX(), 0);
        assertEquals("maxX", 0.5, bb.getMaxX(), 0);
        assertEquals("minY", -1.5, bb.getMinY(), 0);
        assertEquals("maxY", 1.5, bb.getMaxY(), 0);
        assertEquals("minZ", -2.5, bb.getMinZ(), 0);
        assertEquals("maxZ", 2.5, bb.getMaxZ(), 0);

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
        assertEquals("minX", -10, bb.getMinX(), 0);
        assertEquals("maxX", 10, bb.getMaxX(), 0);
        assertEquals("minY", -15, bb.getMinY(), 0);
        assertEquals("maxY", 15, bb.getMaxY(), 0);
        assertEquals("minZ", -20, bb.getMinZ(), 0);
        assertEquals("maxZ", 20, bb.getMaxZ(), 0);
        assertEquals("deltaX", 20, bb.getDeltaX(), 0);
        assertEquals("deltaY", 30, bb.getDeltaY(), 0);
        assertEquals("deltaZ", 40, bb.getDeltaZ(), 0);
        assertEquals("volume", 20 * 30 * 40, bb.getVolume(), 0);
        assertFalse("contains does not include boundaries", bb.contains(-10, 0, 0));
        assertFalse("contains does not include boundaries", bb.contains(10, 0, 0));
        assertFalse("contains does not include boundaries", bb.contains(0, -15, 0));
        assertFalse("contains does not include boundaries", bb.contains(0, 15, 0));
        assertFalse("contains does not include boundaries", bb.contains(0, 0, -20));
        assertFalse("contains does not include boundaries", bb.contains(0, 0, 20));
        assertTrue("contains", bb.contains(-0.999, 0, 0));
        assertTrue("contains", bb.contains(0.999, 0, 0));
        assertTrue("contains", bb.contains(0, -14.999, 0));
        assertTrue("contains", bb.contains(0, 14.999, 0));
        assertTrue("contains", bb.contains(0, 0, -19.999));
        assertTrue("contains", bb.contains(0, 0, 19.999));
        assertTrue("covers includes boundaries", bb.covers(-10, 0, 0));
        assertTrue("covers includes boundaries", bb.covers(10, 0, 0));
        assertTrue("covers includes boundaries", bb.covers(0, -15, 0));
        assertTrue("covers includes boundaries", bb.covers(0, 15, 0));
        assertTrue("covers includes boundaries", bb.covers(0, 0, -20));
        assertTrue("covers includes boundaries", bb.covers(0, 0, 20));
        assertFalse("covers", bb.covers(-10.001, 0, 0));
        assertFalse("covers", bb.covers(10.001, 0, 0));
        assertFalse("covers", bb.covers(0, -15.001, 0));
        assertFalse("covers", bb.covers(0, 15.001, 0));
        assertFalse("covers", bb.covers(0, 0, -20.001));
        assertFalse("covers", bb.covers(0, 0, 20.001));

        Collection<Point3d> pointCollection = new ArrayList<>();
        try
        {
            new Bounds3d(pointCollection);
            fail("Empty point collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        pointCollection.add(new Point3d(10, 20, 30));
        bb = new Bounds3d(pointCollection);
        assertEquals("minX", 10, bb.getAbsoluteMinX(), 0);
        assertEquals("maxX", 10, bb.getAbsoluteMaxX(), 0);
        assertEquals("minY", 20, bb.getAbsoluteMinY(), 0);
        assertEquals("maxY", 20, bb.getAbsoluteMaxY(), 0);
        assertEquals("minZ", 30, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxZ", 30, bb.getAbsoluteMaxZ(), 0);

        pointCollection.add(new Point3d(-5, -6, -7));
        bb = new Bounds3d(pointCollection);
        assertEquals("minX", -5, bb.getAbsoluteMinX(), 0);
        assertEquals("maxX", 10, bb.getAbsoluteMaxX(), 0);
        assertEquals("minY", -6, bb.getAbsoluteMinY(), 0);
        assertEquals("maxY", 20, bb.getAbsoluteMaxY(), 0);
        assertEquals("minZ", -7, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxZ", 30, bb.getAbsoluteMaxZ(), 0);

        assertTrue("toString returns something descriptive", bb.toString().startsWith("Bounds3d "));
        assertEquals("toString with false argument produces same as toString with no argument", bb.toString(),
                bb.toString(false));
        assertTrue("toString with true argument produces rhs of toString with no argument",
                bb.toString().indexOf(bb.toString(true)) > 0);

        pointCollection.add(new Point3d(40, 50, 60));
        // This collection is an ArrayList, so the elements are stored in the order in which they were added
        bb = new Bounds3d(pointCollection);
        assertEquals("minX", -5, bb.getAbsoluteMinX(), 0);
        assertEquals("maxX", 40, bb.getAbsoluteMaxX(), 0);
        assertEquals("minY", -6, bb.getAbsoluteMinY(), 0);
        assertEquals("maxY", 50, bb.getAbsoluteMaxY(), 0);
        assertEquals("minZ", -7, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxZ", 60, bb.getAbsoluteMaxZ(), 0);

        bb = new Bounds3d(pointCollection.toArray((new Point3d[0])));
        assertEquals("minX", -5, bb.getAbsoluteMinX(), 0);
        assertEquals("maxX", 40, bb.getAbsoluteMaxX(), 0);
        assertEquals("minY", -6, bb.getAbsoluteMinY(), 0);
        assertEquals("maxY", 50, bb.getAbsoluteMaxY(), 0);
        assertEquals("minZ", -7, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxZ", 60, bb.getAbsoluteMaxZ(), 0);

        Bounds3d bb2 = new Bounds3d(-100, -90, -100, -90, -100, -90);
        assertNull("empty bounding box", bb.intersection(bb2));

        PolyLine3d line = new PolyLine3d(new Point3d(1, 12, 23), new Point3d(3, 12, 21), new Point3d(2, 11, 23));
        bb = new Bounds3d(line);
        assertEquals("minX", 1, bb.getAbsoluteMinX(), 0);
        assertEquals("minY", 11, bb.getAbsoluteMinY(), 0);
        assertEquals("minZ", 21, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxX", 3, bb.getAbsoluteMaxX(), 0);
        assertEquals("maxY", 12, bb.getAbsoluteMaxY(), 0);
        assertEquals("maxZ", 23, bb.getAbsoluteMaxZ(), 0);

        assertEquals("bounding box of reversed line", bb, new Bounds3d(line.reverse()));

        Point3d p3d = new Point3d(123, 456, 789);
        bb = new Bounds3d(p3d);
        assertEquals("minX", 123, bb.getAbsoluteMinX(), 0);
        assertEquals("maxX", 123, bb.getAbsoluteMaxX(), 0);
        assertEquals("minY", 456, bb.getAbsoluteMinY(), 0);
        assertEquals("maxY", 456, bb.getAbsoluteMaxY(), 0);
        assertEquals("minZ", 789, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxZ", 789, bb.getAbsoluteMaxZ(), 0);
        assertFalse("contains does not include boundaries", bb.contains(p3d));
        assertTrue("covers includes boundaries", bb.covers(p3d));

        assertEquals("size of a Bounds3d is always 8", 8, bb.size());
    }

    /**
     * Test various methods of a Bounds2d.
     * @throws DrawException when that happens uncaught; this test has failed
     * @throws IllegalArgumentException when that happens uncaught; this test has failed
     * @throws NullPointerException when that happens uncaught; this test has failed
     */
    @Test
    public void methodTest() throws NullPointerException, IllegalArgumentException, DrawException
    {
        PolyLine3d l3d = new PolyLine3d(new Point3d(10, 10, 10), new Point3d(30, -20, 40), new Point3d(-40, 100, 0));
        Bounds3d bb = new Bounds3d(l3d);
        assertEquals("minX", -40, bb.getAbsoluteMinX(), 0);
        assertEquals("maxX", 30, bb.getAbsoluteMaxX(), 0);
        assertEquals("minY", -20, bb.getAbsoluteMinY(), 0);
        assertEquals("maxY", 100, bb.getAbsoluteMaxY(), 0);
        assertEquals("minZ", 0, bb.getAbsoluteMinZ(), 0);
        assertEquals("maxZ", 40, bb.getAbsoluteMaxZ(), 0);

        Point3d midPoint = bb.midPoint();
        assertEquals("midPoint x", (bb.getAbsoluteMinX() + bb.getAbsoluteMaxX()) / 2, midPoint.x, 0);
        assertEquals("midPoint y", (bb.getAbsoluteMinY() + bb.getAbsoluteMaxY()) / 2, midPoint.y, 0);
        assertEquals("midPoint z", (bb.getAbsoluteMinZ() + bb.getAbsoluteMaxZ()) / 2, midPoint.z, 0);
        assertEquals("midPoint of bounds of point is point", midPoint, new Bounds3d(midPoint).midPoint());

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

        assertFalse("boundingbox does not contain itself", bb.contains(bb));
        Bounds3d bb2 = new Bounds3d(bb.getAbsoluteMinX() - 0.0001, bb.getAbsoluteMaxX() + 0.0001, bb.getAbsoluteMinY() - 0.0001,
                bb.getAbsoluteMaxY() + 0.0001, bb.getAbsoluteMinZ() - 0.0001, bb.getAbsoluteMaxZ() + 0.0001);
        assertTrue("Slightly enlarged bounding box contains non-enlarged version", bb2.contains(bb));

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

        assertTrue("Bounds2d covers itself", bb.covers(bb));
        assertFalse("Bounds2d does not cover slightly enlarged version of itself", bb.covers(bb2));
        bb2 = new Bounds3d(bb.getAbsoluteMinX() + 0.0001, bb.getAbsoluteMaxX() + 0.0001, bb.getAbsoluteMinY() + 0.0001,
                bb.getAbsoluteMaxY() + 0.0001, bb.getAbsoluteMinZ() + 0.0001, bb.getAbsoluteMaxZ() + 0.0001);
        assertFalse("Bounds2d does not cover slightly moved version of itself", bb.covers(bb2));

        assertFalse("Overlapping Bounds2d is not disjoint", bb.disjoint(bb2));
        assertTrue("Overlapping Bounds2d is not disjoint", bb.intersects(bb2));
        bb2 = new Bounds3d(bb.getAbsoluteMinX() + 1000, bb.getAbsoluteMaxX() + 1000, bb.getAbsoluteMinY() + 1000,
                bb.getAbsoluteMaxY() + 1000, bb.getAbsoluteMinZ() + 1000, bb.getAbsoluteMaxZ() + 1000);
        assertFalse("No intersection", bb.intersects(bb2));
        assertTrue("Disjoint", bb.disjoint(bb2));
        bb2 = new Bounds3d(bb.getAbsoluteMaxX(), bb.getAbsoluteMaxX() + 0.0001, bb.getAbsoluteMinY() + 0.0001,
                bb.getAbsoluteMaxY() + 0.0001, bb.getAbsoluteMinZ() + 0.0001, bb.getAbsoluteMaxZ() + 0.0001);
        assertTrue("Only touching at vertical line is disjoint", bb.disjoint(bb2));
        assertTrue("Only touching at vertical line is disjoint", bb2.disjoint(bb));

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
                    bb2 = new Bounds3d(bb.getAbsoluteMinX() + dx, bb.getAbsoluteMaxX() + dx, bb.getAbsoluteMinY() + dy,
                            bb.getAbsoluteMaxY() + dy, bb.getAbsoluteMinZ() + dz, bb.getAbsoluteMaxZ() + dz);
                    Bounds3d intersection = bb.intersection(bb2);
                    if (Math.abs(dx) >= 200 || Math.abs(dy) >= 200 || Math.abs(dz) >= 200)
                    {
                        assertNull("intersection is null", intersection);
                    }
                    else
                    {
                        assertEquals("min x", Math.max(bb.getAbsoluteMinX(), bb2.getAbsoluteMinX()),
                                intersection.getAbsoluteMinX(), 0);
                        assertEquals("max x", Math.min(bb.getAbsoluteMaxX(), bb2.getAbsoluteMaxX()),
                                intersection.getAbsoluteMaxX(), 0);
                        assertEquals("min y", Math.max(bb.getAbsoluteMinY(), bb2.getAbsoluteMinY()),
                                intersection.getAbsoluteMinY(), 0);
                        assertEquals("max y", Math.min(bb.getAbsoluteMaxY(), bb2.getAbsoluteMaxY()),
                                intersection.getAbsoluteMaxY(), 0);
                        assertEquals("min z", Math.max(bb.getAbsoluteMinZ(), bb2.getAbsoluteMinZ()),
                                intersection.getAbsoluteMinZ(), 0);
                        assertEquals("max z", Math.min(bb.getAbsoluteMaxZ(), bb2.getAbsoluteMaxZ()),
                                intersection.getAbsoluteMaxZ(), 0);
                    }
                }
            }
        }
        assertEquals("getBounds returns this", bb, bb.getBounds());
        assertNotEquals("HashCode uses minX", bb.hashCode(), new Bounds3d(bb.getAbsoluteMinX() + 1, bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ()));
        assertNotEquals("HashCode uses maxX", bb.hashCode(), new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX() + 1,
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ()));
        assertNotEquals("HashCode uses minY", bb.hashCode(), new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY() + 1, bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ()));
        assertNotEquals("HashCode uses maxY", bb.hashCode(), new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY() + 1, bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ()));
        assertNotEquals("HashCode uses minZ", bb.hashCode(), new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ() + 1, bb.getAbsoluteMaxZ()));
        assertNotEquals("HashCode uses maxZ", bb.hashCode(), new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY() + 1, bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ() + 1));

        assertFalse("equals checks for null", bb.equals(null));
        assertFalse("equals checks for different kind of object", bb.equals("string"));
        assertFalse("equals checks minX", bb.equals(new Bounds3d(bb.getAbsoluteMinX() + 1, bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ())));
        assertFalse("equals checks maxX", bb.equals(new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX() + 1,
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ())));
        assertFalse("equals checks minY", bb.equals(new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY() + 1, bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ())));
        assertFalse("equals checks maxy", bb.equals(new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY() + 1, bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ())));
        assertFalse("equals checks minZ", bb.equals(new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ() + 1, bb.getAbsoluteMaxZ())));
        assertFalse("equals checks maxZ", bb.equals(new Bounds3d(bb.getAbsoluteMinX(), bb.getAbsoluteMaxX(),
                bb.getAbsoluteMinY(), bb.getAbsoluteMaxY(), bb.getAbsoluteMinZ(), bb.getAbsoluteMaxZ() + 1)));
        assertTrue("equals to copy of itself", bb.equals(new Bounds3d(bb)));

        Bounds2d projection = bb.project();
        assertEquals("projection minX", projection.getAbsoluteMinX(), bb.getAbsoluteMinX(), 0);
        assertEquals("projection maxX", projection.getAbsoluteMaxX(), bb.getAbsoluteMaxX(), 0);
        assertEquals("projection minY", projection.getAbsoluteMinY(), bb.getAbsoluteMinY(), 0);
        assertEquals("projection maxY", projection.getAbsoluteMaxY(), bb.getAbsoluteMaxY(), 0);
    }

}
