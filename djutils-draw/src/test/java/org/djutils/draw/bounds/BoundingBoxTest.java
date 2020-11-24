package org.djutils.draw.bounds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.djutils.draw.DrawException;
import org.djutils.draw.d0.Point;
import org.djutils.draw.d0.Point3d;
import org.djutils.draw.d1.Line3d;
import org.junit.Test;

/**
 * BoundingBoxText.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class BoundingBoxTest
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
            new BoundingBox(Double.NaN, 0, 0, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, Double.NaN, 0, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, Double.NaN, 0, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, 0, Double.NaN, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, 0, 0, Double.NaN, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, 0, 0, 0, Double.NaN);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(2, -2, 0, 0, 0, 0);
            fail("Negative x-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, 2, -2, 0, 0);
            fail("Negative y-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, 0, 0, 2, -2);
            fail("Negative z-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        BoundingBox bb = new BoundingBox(1, 2, 3, 4, 5, 6);
        assertEquals("minX", 1, bb.getMinX(), 0);
        assertEquals("maxX", 2, bb.getMaxX(), 0);
        assertEquals("minY", 3, bb.getMinY(), 0);
        assertEquals("maxY", 4, bb.getMaxY(), 0);
        assertEquals("minZ", 5, bb.getMinZ(), 0);
        assertEquals("maxZ", 6, bb.getMaxZ(), 0);

        try
        {
            new BoundingBox(Double.NaN, 0, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, Double.NaN, 0);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, Double.NaN);
            fail("Nan should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(-3, 0, 0);
            fail("Negative x-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, -3, 0);
            fail("Negative y-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        try
        {
            new BoundingBox(0, 0, -3);
            fail("Negative z-range should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        
        bb = new BoundingBox(20, 30, 40);
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
        
        Collection<Point> pointCollection = new ArrayList<>();
        try
        {
            new BoundingBox(pointCollection);
            fail("Empty point collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        pointCollection.add(new Point3d(10, 20, 30));
        bb = new BoundingBox(pointCollection);
        assertEquals("minX", 10, bb.getMinX(), 0);
        assertEquals("maxX", 10, bb.getMaxX(), 0);
        assertEquals("minY", 20, bb.getMinY(), 0);
        assertEquals("maxY", 20, bb.getMaxY(), 0);
        assertEquals("minZ", 30, bb.getMinZ(), 0);
        assertEquals("maxZ", 30, bb.getMaxZ(), 0);
        
        pointCollection.add(new Point3d(-5, -6, -7));
        bb = new BoundingBox(pointCollection);
        assertEquals("minX", -5, bb.getMinX(), 0);
        assertEquals("maxX", 10, bb.getMaxX(), 0);
        assertEquals("minY", -6, bb.getMinY(), 0);
        assertEquals("maxY", 20, bb.getMaxY(), 0);
        assertEquals("minZ", -7, bb.getMinZ(), 0);
        assertEquals("maxZ", 30, bb.getMaxZ(), 0);
        
        assertTrue("toString returns something descriptive", bb.toString().startsWith("BoundingBox "));
        assertFalse("bounding box is NOT the empty bounding box", bb.isEmpty());
        
        pointCollection.add(new Point3d(40, 50, 60));
        // This collection is an ArrayList, so the elements are stored in the order in which they were added
        bb = new BoundingBox(pointCollection);
        assertEquals("minX", -5, bb.getMinX(), 0);
        assertEquals("maxX", 40, bb.getMaxX(), 0);
        assertEquals("minY", -6, bb.getMinY(), 0);
        assertEquals("maxY", 50, bb.getMaxY(), 0);
        assertEquals("minZ", -7, bb.getMinZ(), 0);
        assertEquals("maxZ", 60, bb.getMaxZ(), 0);
        
        bb = new BoundingBox(pointCollection.toArray((new Point3d[0])));
        assertEquals("minX", -5, bb.getMinX(), 0);
        assertEquals("maxX", 40, bb.getMaxX(), 0);
        assertEquals("minY", -6, bb.getMinY(), 0);
        assertEquals("maxY", 50, bb.getMaxY(), 0);
        assertEquals("minZ", -7, bb.getMinZ(), 0);
        assertEquals("maxZ", 60, bb.getMaxZ(), 0);

        BoundingBox bb2 = new BoundingBox(-100, -90, -100, -90, -100, -90);
        BoundingBox bb3 = bb.intersection(bb2);
        assertEquals("empty bounding box", BoundingBox.EMPTY_BOUNDING_BOX, bb3);
        assertTrue("empty bounding box", bb3.isEmpty());
        
        Line3d line = new Line3d(new Point3d(1, 12, 23), new Point3d(3, 12, 21), new Point3d(2, 11, 23));
        bb = new BoundingBox(line);
        assertEquals("minX", 1, bb.getMinX(), 0);
        assertEquals("minY", 11, bb.getMinY(), 0);
        assertEquals("minZ", 21, bb.getMinZ(), 0);
        assertEquals("maxX", 3, bb.getMaxX(), 0);
        assertEquals("maxY", 12, bb.getMaxY(), 0);
        assertEquals("maxZ", 23, bb.getMaxZ(), 0);
        
        assertEquals("bounding box of reversed line", bb, new BoundingBox(line.reverse()));
    }
    
}
