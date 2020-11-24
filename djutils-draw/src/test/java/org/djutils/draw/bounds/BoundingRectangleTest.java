package org.djutils.draw.bounds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.djutils.draw.d0.Point;
import org.djutils.draw.d0.Point2d;
import org.junit.Test;

/**
 * Bounds2dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Bounds2dTest
{

    /**
     * Test the bounding rectangle constructor.
     */
    @Test
    public void constructorTest()
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

        Bounds2d br = new Bounds2d(1, 2, 3, 4);
        assertEquals("minX", 1, br.getMinX(), 0);
        assertEquals("maxX", 2, br.getMaxX(), 0);
        assertEquals("minY", 3, br.getMinY(), 0);
        assertEquals("maxY", 4, br.getMaxY(), 0);

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

        Collection<Point> pointCollection = new ArrayList<>();
        try
        {
            new Bounds3d(pointCollection);
            fail("Empty point collection should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        pointCollection.add(new Point2d(10, 20));
        br = new Bounds2d(pointCollection);
        assertEquals("minX", 10, br.getMinX(), 0);
        assertEquals("maxX", 10, br.getMaxX(), 0);
        assertEquals("minY", 20, br.getMinY(), 0);
        assertEquals("maxY", 20, br.getMaxY(), 0);

        pointCollection.add(new Point2d(-5, -6));
        br = new Bounds2d(pointCollection);
        assertEquals("minX", -5, br.getMinX(), 0);
        assertEquals("maxX", 10, br.getMaxX(), 0);
        assertEquals("minY", -6, br.getMinY(), 0);
        assertEquals("maxY", 20, br.getMaxY(), 0);

        assertTrue("toString returns something descriptive", br.toString().startsWith("Bounds2d "));
        assertFalse("bounding box is NOT the empty bounding box", br.isEmpty());

        pointCollection.add(new Point2d(40, 50));
        // This collection is an ArrayList, so the elements are stored in the order in which they were added
        br = new Bounds2d(pointCollection);
        assertEquals("minX", -5, br.getMinX(), 0);
        assertEquals("maxX", 40, br.getMaxX(), 0);
        assertEquals("minY", -6, br.getMinY(), 0);
        assertEquals("maxY", 50, br.getMaxY(), 0);

        br = new Bounds2d(pointCollection.toArray((new Point2d[0])));
        assertEquals("minX", -5, br.getMinX(), 0);
        assertEquals("maxX", 40, br.getMaxX(), 0);
        assertEquals("minY", -6, br.getMinY(), 0);
        assertEquals("maxY", 50, br.getMaxY(), 0);

        Bounds2d bb2 = new Bounds2d(-100, -90, -100, -90);
        Bounds2d bb3 = br.intersection(bb2);
        assertEquals("empty bounding box", Bounds2d.EMPTY_BOUNDING_RECTANGLE, bb3);
        assertTrue("empty bounding box", bb3.isEmpty());
        
    }
    
}