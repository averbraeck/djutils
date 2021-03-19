package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * Polygon3dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Polygon3dTest
{

    /**
     * Test the constructors.
     */
    @Test
    public void testConstructors()
    {
        double[] x = new double[] { 1, 3, 5 };
        double[] y = new double[] { 2, 1, 10 };
        double[] z = new double[] { 2, 3, 4 };

        Polygon3d polygon = new Polygon3d(x, y, z);
        checkPolygon("constructed from arrays", x, y, z, polygon);
        Polygon3d reversed = polygon.reverse();
        for (int index = 0; index < 3; index++)
        {
            Point3d p = reversed.get(x.length - 1 - index);
            assertEquals("reversed x", x[index], p.x, 0.0001);
            assertEquals("reversed y", y[index], p.y, 0.0001);
            assertEquals("reversed z", z[index], p.z, 0.0001);
        }

        x = new double[] { 1, 3, 5, 1 };
        y = new double[] { 2, 1, 10, 2 };
        z = new double[] { 2, 3, 4, 2 };
        polygon = new Polygon3d(x, y, z); // Last point is duplicate of first point; should be handled gracefully
        assertTrue("toString returns something descriptive", polygon.toString().startsWith("Polygon3d"));
        assertTrue("toString can suppress the class name", polygon.toString().indexOf(polygon.toString(true)) > 0);
        checkPolygon("constructed from arrays", x, y, z, polygon);
        Polygon3d otherPolygon = new Polygon3d(polygon.get(0), polygon.get(1), polygon.get(2), polygon.get(0));
        assertEquals("polygon constructed from all points of existing polygon with first point duplicated at end is equal "
                + "to original", polygon, otherPolygon);
        // Make a Polygon3d from Point3d where last point differs from first only in y
        new Polygon3d(polygon.get(0), polygon.get(1), polygon.get(2), new Point3d(polygon.get(0).x, 123, polygon.get(0).z));
        // Make a Polygon3d from Point3d where last point differs from first only in z
        new Polygon3d(polygon.get(0), polygon.get(1), polygon.get(2), new Point3d(polygon.get(0).x, polygon.get(0).y, 123));

        x = new double[] { 1, 3, 1 }; // x coordinate of last point matches that of first
        y = new double[] { 2, 1, 10 }; // not true for y coordinates
        z = new double[] { 2, 3, 2 }; // true for z
        polygon = new Polygon3d(x, y, z);
        // System.out.println(polygon);
        checkPolygon("constructed from arrays with first and last x equal", x, y, z, polygon);

        x = new double[] { 1, 3, 1 }; // x coordinate of last point matches that of first
        y = new double[] { 2, 1, 2 }; // same for y
        z = new double[] { 2, 3, 4 }; // not true for z
        polygon = new Polygon3d(x, y, z);
        // System.out.println(polygon);
        checkPolygon("constructed from arrays with first and last x equal", x, y, z, polygon);

        x = new double[] { 1, 3, 5, 3 };
        y = new double[] { 2, 2, 10, 10 };
        z = new double[] { 4, 4, 4, 4 };
        polygon = new Polygon3d(x, y, z);
        checkPolygon("constructed from arrays", x, y, z, polygon);
        // convert the points of polygon to an array of Point3d
        List<Point3d> list = new ArrayList<>();
        polygon.getPoints().forEachRemaining(list::add);
        otherPolygon = new Polygon3d(list);
        assertEquals("Polygon created from polygon points is equal to original polygon", polygon, otherPolygon);
        otherPolygon = new Polygon3d(list.get(0), list.get(1), list.get(2), list.get(3));
        assertEquals("Polygon created from all four points of existing polygon is equal to original", polygon, otherPolygon);

        Point3d[] pointArray = list.toArray(new Point3d[0]);
        otherPolygon = new Polygon3d(pointArray);
        assertEquals("Polygon created from array of points of existing polygon is equal to original", polygon, otherPolygon);

        list.add(list.get(0));
        otherPolygon = new Polygon3d(list.iterator());
        assertEquals("Polygon created from polygon points and duplicate of first point at end is equal to original polygon",
                polygon, otherPolygon);

        otherPolygon = new Polygon3d(list);
        assertEquals("Polygon created from polygon points and duplicate of first point at end is equal to original polygon",
                polygon, otherPolygon);
        // Add a point that only differs in y
        list.add(new Point3d(list.get(0).x, 123, list.get(0).z));
        new Polygon3d(list.iterator());
        list.add(list.get(0));
        new Polygon3d(list.iterator());

        // Add a point that only differs in z
        list.add(new Point3d(list.get(0).x, list.get(0).y, 123));
        new Polygon3d(list.iterator());
        list.add(list.get(0));
        new Polygon3d(list.iterator());

        // Make last TWO points duplicate of first point
        list.add(list.get(0));
        try
        {
            new Polygon3d(list);
            fail("last two points equal to first point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            polygon.getSegment(-1);
            fail("Negative index should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            polygon.getSegment(polygon.size());
            fail("index equal to size (or more) should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] { 1, 2, 3 }, new double[] { 1, 2, 3 }, new double[] { 1, 2, 3, 4 });
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] { 1, 2, 3 }, new double[] { 1, 2, 3, 4 }, new double[] { 1, 2, 3 });
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] { 1, 2, 3, 4 }, new double[] { 1, 2, 3 }, new double[] { 1, 2, 3 });
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(null, new double[] { 1, 2, 3 }, new double[] { 1, 2, 3 });
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] { 1, 2, 3 }, null, new double[] { 1, 2, 3 });
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] { 1, 2, 3 }, new double[] { 1, 2, 3 }, null);
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] { 1 }, new double[] { 1 }, new double[] { 1 });
            fail("too short coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d[] {});
            fail("too short coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new Point3d(1, 2, 3), new Point3d(1, 2, 3), (Point3d[]) null);
            fail("too short coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new Point3d(1, 2, 3), new Point3d(3, 2, 5),
                    new Point3d[] { new Point3d(1, 2, 3), new Point3d(1, 2, 3) });
            fail("two identical points at end, matching first point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        list.clear();
        list.add(new Point3d(1, 2, 3));
        try
        {
            new Polygon3d(list);
            fail("too short list should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

    }
    
    /**
     * Test the filtering constructors.
     */
    @Test
    public void filterTest()
    {
        Point3d[] points = new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3), new Point3d(4, 5, 6)};
        try
        {
            new Polygon3d(false, points);
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        
        assertEquals("After filtering; there are two points left", 2, new Polygon3d(true, points).size()); 
        
        List<Point3d> list = Arrays.asList(points);
        try
        {
            new Polygon3d(false, list);
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        
        assertEquals("After filtering; there are two points left", 2, new Polygon3d(true, list).size()); 
    }

    /**
     * Test the reverse and project methods.
     * @throws DrawRuntimeException should not happen; this test has failed if it does happen
     */
    @Test
    public final void reverseAndProjectTest() throws DrawRuntimeException
    {
        Point3d p0 = new Point3d(1.1, 2.21, 3.1);
        Point3d p1 = new Point3d(2.1, 2.22, 3.2);
        Point3d p2 = new Point3d(2.1, 2.23, 3.3);
        Point3d p2x = new Point3d(p2.x, p2.y, p2.z + 1);
        Point3d p3 = new Point3d(4.1, 2.24, 3.4);
        Point3d p4 = new Point3d(5.1, 2.25, 3.5);
        Point3d p5 = new Point3d(6.1, 2.26, 3.6);

        Polygon3d l01 = new Polygon3d(p0, p1);
        Polygon3d r = l01.reverse();
        assertEquals("result has size 2", 2, r.size());
        assertEquals("point 0 is p1", p1, r.get(0));
        assertEquals("point 1 is p0", p0, r.get(1));

        Polygon3d l05 = new Polygon3d(p0, p1, p2, p3, p4, p5);
        r = l05.reverse();
        assertEquals("result has size 6", 6, r.size());
        assertEquals("point 0 is p5", p5, r.get(0));
        assertEquals("point 1 is p4", p4, r.get(1));
        assertEquals("point 2 is p3", p3, r.get(2));
        assertEquals("point 3 is p2", p2, r.get(3));
        assertEquals("point 4 is p1", p1, r.get(4));
        assertEquals("point 5 is p0", p0, r.get(5));

        PolyLine2d l2d = l05.project();
        assertEquals("result has size 6", 6, l2d.size());
        assertEquals("point 0 is p5", p0.project(), l2d.get(0));
        assertEquals("point 1 is p4", p1.project(), l2d.get(1));
        assertEquals("point 2 is p3", p2.project(), l2d.get(2));
        assertEquals("point 3 is p2", p3.project(), l2d.get(3));
        assertEquals("point 4 is p1", p4.project(), l2d.get(4));
        assertEquals("point 5 is p0", p5.project(), l2d.get(5));

        l05 = new Polygon3d(p0, p1, p2, p2x, p3, p4, p5);
        l2d = l05.project();
        assertEquals("result has size 6", 6, l2d.size());
        assertEquals("point 0 is p5", p0.project(), l2d.get(0));
        assertEquals("point 1 is p4", p1.project(), l2d.get(1));
        assertEquals("point 2 is p3", p2.project(), l2d.get(2));
        assertEquals("point 3 is p2", p3.project(), l2d.get(3));
        assertEquals("point 4 is p1", p4.project(), l2d.get(4));
        assertEquals("point 5 is p0", p5.project(), l2d.get(5));

        Polygon3d l22x = new Polygon3d(p2, p2x);
        try
        {
            l22x.project();
            fail("Projecting a polygon3d that entirely projects to one point should have thrown an exception");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
    }

    /**
     * Verify the various properties of a Polygon3d.
     * @param where String; description of the test
     * @param x double[]; the expected x coordinates
     * @param y double[]; the expected y coordinates
     * @param z double[]; the expected z coordinates
     * @param polygon Polygon3d; the Polygon3d
     */
    private void checkPolygon(final String where, final double[] x, final double[] y, final double[] z, final Polygon3d polygon)
    {
        double cumulativeLength = 0;
        for (int index = 0; index < polygon.size(); index++)
        {
            assertEquals(where + " x[index]", x[index], polygon.getX(index), Math.ulp(x[index]));
            assertEquals(where + " y[index]", y[index], polygon.getY(index), Math.ulp(y[index]));
            assertEquals(where + " z[index]", z[index], polygon.getZ(index), Math.ulp(z[index]));
            LineSegment3d segment = polygon.getSegment(index);
            assertEquals(where + " segment start x", x[index], segment.startX, Math.ulp(x[index]));
            assertEquals(where + " segment start y", y[index], segment.startY, Math.ulp(y[index]));
            assertEquals(where + " segment start z", z[index], segment.startZ, Math.ulp(z[index]));
            int wrappedIndex = (index + 1) % polygon.size();
            assertEquals(where + " segment end x", x[wrappedIndex], segment.endX, Math.ulp(x[wrappedIndex]));
            assertEquals(where + " segment end y", y[wrappedIndex], segment.endY, Math.ulp(y[wrappedIndex]));
            assertEquals(where + " segment end z", z[wrappedIndex], segment.endZ, Math.ulp(z[wrappedIndex]));
            cumulativeLength += segment.getLength();
        }
        assertEquals(where + " circumference", cumulativeLength, polygon.getLength(),
                polygon.size() * Math.ulp(cumulativeLength));
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point3d[] points = new Point3d[] { new Point3d(123.456, 345.678, 901.234), new Point3d(234.567, 456.789, 12.345),
                new Point3d(-12.345, -34.567, 45.678) };
        Polygon3d pl = new Polygon3d(points);
        String[] out = pl.toExcel().split("\\n");
        assertEquals("Excel output consists of one line per point plus one", points.length + 1, out.length);
        for (int index = 0; index <= points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals("each line consists of three fields", 3, fields.length);
            try
            {
                double x = Double.parseDouble(fields[0].trim());
                assertEquals("x matches", points[index % pl.size()].x, x, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("First field " + fields[0] + " does not parse as a double");
            }
            try
            {
                double y = Double.parseDouble(fields[1].trim());
                assertEquals("y matches", points[index % pl.size()].y, y, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[1] + " does not parse as a double");
            }
            try
            {
                double z = Double.parseDouble(fields[2].trim());
                assertEquals("z matches", points[index % pl.size()].z, z, 0.001);
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[2] + " does not parse as a double");
            }
        }
    }

}
