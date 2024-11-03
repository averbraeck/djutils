package org.djutils.draw.surface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.Point3d;
import org.junit.jupiter.api.Test;

/**
 * Surface3dTest.java; test the Surface3d class.
 * <p>
 * Copyright (c) 2021-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Surface3dTest
{
    /**
     * Test the constructor(s) of Surface3d.
     */
    @Test
    public void testConstructors()
    {
        try
        {
            new Surface3d(null);
            fail("null points should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {});
            fail("empty points should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {{new Point3d(1, 2, 3)}});
            fail("triangle with only one point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {{new Point3d(1, 2, 3), new Point3d(4, 5, 6)}});
            fail("triangle with only two points should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {
                    {new Point3d(1, 2, 3), new Point3d(4, 5, 6), new Point3d(7, 8, 9), new Point3d(10, 11, 12)}});
            fail("triangle with four points should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {{new Point3d(1, 2, 3), new Point3d(4, 5, 6), new Point3d(1, 2, 3)}});
            fail("triangle with duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {{new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(7, 8, 9)}});
            fail("triangle with duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Surface3d(new Point3d[][] {{new Point3d(1, 2, 3), new Point3d(4, 5, 6), new Point3d(4, 5, 6)}});
            fail("triangle with duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        Point3d[][] points = new Point3d[10][];
        for (int triangle = 0; triangle < points.length; triangle++)
        {
            points[triangle] = new Point3d[] {new Point3d(triangle, triangle + 1, triangle + 2),
                    new Point3d(triangle + 10, triangle + 11, triangle + 12),
                    new Point3d(triangle + 10, triangle + 11, triangle - 12)};
        }
        Surface3d surface3d = new Surface3d(points);
        assertEquals(points.length * 3, surface3d.size(), "size");
        Iterator<? extends Point3d> iterator = surface3d.getPoints();
        assertNotNull(iterator, "iterator is not null");
        for (int triangle = 0; triangle < points.length; triangle++)
        {
            for (int i = 0; i < 3; i++)
            {
                assertTrue(iterator.hasNext(), "iterator should not be exhaused yet");
                Object result = iterator.next();
                assertEquals(points[triangle][i], result, "iterator returned correct point");
            }
        }
        assertFalse(iterator.hasNext(), "iterator should now be exhaused");
        try
        {
            iterator.next();
            fail("exhausted iterator should have thrown a NoSuchElementException");
        }
        catch (NoSuchElementException nsee)
        {
            // Ignore expected exception
        }

        iterator = new Iterator<Point3d>()
        {
            private int triangleIndex = 0;

            private int pointIndex = 0;

            @Override
            public boolean hasNext()
            {
                return this.triangleIndex < points.length;
            }

            @Override
            public Point3d next()
            {
                Point3d result = points[this.triangleIndex][this.pointIndex++];
                if (this.pointIndex >= 3)
                {
                    this.triangleIndex++;
                    this.pointIndex = 0;
                }
                return result;
            }
        };
        Bounds3d bounds = new Bounds3d(iterator);
        assertEquals(bounds, surface3d.getBounds(), "Bounds match");

        // Triangulate a cube
        Point3d[][] cubePoints = new Point3d[12][];
        // bottom; minimal z
        cubePoints[0] = new Point3d[] {new Point3d(-1, -1, -1), new Point3d(-1, 1, -1), new Point3d(1, 1, -1)};
        cubePoints[1] = new Point3d[] {new Point3d(1, 1, -1), new Point3d(1, -1, -1), new Point3d(-1, -1, -1)};
        // left; minimal x
        cubePoints[2] = new Point3d[] {new Point3d(-1, -1, -1), new Point3d(-1, 1, -1), new Point3d(-1, 1, 1)};
        cubePoints[3] = new Point3d[] {new Point3d(-1, 1, 1), new Point3d(-1, -1, 1), new Point3d(-1, -1, -1)};
        // front; minimal y
        cubePoints[4] = new Point3d[] {new Point3d(-1, -1, -1), new Point3d(1, -1, -1), new Point3d(1, -1, 1)};
        cubePoints[5] = new Point3d[] {new Point3d(1, -1, 1), new Point3d(-1, -1, 1), new Point3d(-1, -1, -1)};
        // top; maximal z
        cubePoints[6] = new Point3d[] {new Point3d(-1, -1, 1), new Point3d(-1, 1, 1), new Point3d(1, 1, 1)};
        cubePoints[7] = new Point3d[] {new Point3d(1, 1, 1), new Point3d(1, -1, 1), new Point3d(-1, -1, 1)};
        // right; maximal x
        cubePoints[8] = new Point3d[] {new Point3d(1, -1, -1), new Point3d(1, 1, -1), new Point3d(1, 1, 1)};
        cubePoints[9] = new Point3d[] {new Point3d(1, 1, 1), new Point3d(1, -1, 1), new Point3d(1, -1, -1)};
        // rear; maximal y
        cubePoints[10] = new Point3d[] {new Point3d(-1, 1, -1), new Point3d(1, 1, -1), new Point3d(1, 1, 1)};
        cubePoints[11] = new Point3d[] {new Point3d(1, 1, 1), new Point3d(-1, 1, 1), new Point3d(-1, 1, -1)};
        surface3d = new Surface3d(cubePoints);
        assertEquals(36, surface3d.size(), "size (number of points in 12 triangles) is 36");
        assertEquals(new Bounds3d(-1, 1, -1, 1, -1, 1), surface3d.getBounds(), "bounds");

        try
        {
            surface3d.project();
            fail("should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        assertTrue(surface3d.toString().startsWith("Surface3d "),
                "toString results starts with class name (if not suppressed)");

        assertEquals(surface3d.toString(), surface3d.toString(false), "toString results with argument false is default");

        assertTrue(surface3d.toString().indexOf(surface3d.toString(true)) > 5,
                "toString result with argument true is substring of default result");

        assertEquals(surface3d.toString(), surface3d.toString("%f"), "toString result with argument \"%f\" is default");
    }

    /**
     * Test the hashCode and Equals methods.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testHashCodeAndEquals()
    {
        Point3d[][] referencePoints = new Point3d[][] {{new Point3d(1, 2, 3), new Point3d(4, 5, 6), new Point3d(7, 8, 9)},
                {new Point3d(11, 12, 13), new Point3d(14, 15, 16), new Point3d(17, 18, 19)}};
        Surface3d referenceSurface = new Surface3d(referencePoints);
        assertTrue(referenceSurface.equals(referenceSurface), "Equal to itself");
        assertFalse(referenceSurface.equals(null), "Not equal to null");
        assertFalse(referenceSurface.equals("some string"), "Not equal to some other object");
        // We could, in fact, patch the referencePoints array, but it is cleaner to work with a copy.
        Point3d[][] otherPoints =
                java.util.Arrays.stream(referencePoints).map(el -> el.clone()).toArray($ -> referencePoints.clone());
        assertTrue(referenceSurface.equals(new Surface3d(otherPoints)),
                "Equal to other Surface3d created from copy of referencePoints");
        assertEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode is same");
        // Now alter one element at a time and check that hashCode changes and equals returns false
        otherPoints[0][0] = new Point3d(1, 2, 3.5);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][0] = new Point3d(1, 2.5, 3);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][0] = new Point3d(1.5, 2, 3);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        otherPoints[0][0] = new Point3d(1, 2, 3);
        otherPoints[0][1] = new Point3d(4, 5, 6.5);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][1] = new Point3d(4, 5.5, 6);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][1] = new Point3d(4.5, 5, 6);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][1] = new Point3d(4, 5, 6);
        otherPoints[0][2] = new Point3d(7, 8, 9.5);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][2] = new Point3d(7, 8.5, 9);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        otherPoints[0][2] = new Point3d(7.5, 8, 9);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[0][2] = new Point3d(7, 8, 9);
        // Now we skip a few
        otherPoints[1][2] = new Point3d(17, 18, 19.5);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[1][2] = new Point3d(17, 18.5, 19);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        otherPoints[1][2] = new Point3d(17.5, 18, 19);
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
        // Now make one that uses the same set of points (and in the same order), but different indices
        otherPoints = new Point3d[3][];
        otherPoints[0] = referencePoints[0];
        otherPoints[1] = referencePoints[1];
        otherPoints[2] = referencePoints[1]; // A third triangle, using the same points as the second
        assertFalse(referenceSurface.equals(new Surface3d(otherPoints)),
                "Not equal to other Surface3d created from altered copy of referencePoints");
        assertNotEquals(referenceSurface.hashCode(), new Surface3d(otherPoints).hashCode(), "hashCode differs");
    }

}
