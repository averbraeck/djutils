package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.Export;
import org.djutils.draw.point.Point3d;
import org.junit.jupiter.api.Test;

/**
 * Polygon3dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        double[] x = new double[] {1, 3, 5};
        double[] y = new double[] {2, 1, 10};
        double[] z = new double[] {2, 3, 4};

        Polygon3d polygon = new Polygon3d(x, y, z);
        checkPolygon("constructed from arrays", x, y, z, polygon);
        Polygon3d reversed = polygon.reverse();
        for (int index = 0; index < 3; index++)
        {
            Point3d p = reversed.get(x.length - 1 - index);
            assertEquals(x[index], p.x, 0.0001, "reversed x");
            assertEquals(y[index], p.y, 0.0001, "reversed y");
            assertEquals(z[index], p.z, 0.0001, "reversed z");
        }

        x = new double[] {1, 3, 5, 1};
        y = new double[] {2, 1, 10, 2};
        z = new double[] {2, 3, 4, 2};
        polygon = new Polygon3d(x, y, z); // Last point is duplicate of first point; should be handled gracefully
        assertTrue(polygon.toString().startsWith("Polygon3d"), "toString returns something descriptive");
        assertTrue(polygon.toString().indexOf(polygon.toString(true)) > 0, "toString can suppress the class name");
        checkPolygon("constructed from arrays", x, y, z, polygon);
        Polygon3d p2 = new Polygon3d(polygon);
        checkPolygon("constructed from existing polygon", x, y, z, p2);
        Polygon3d otherPolygon = new Polygon3d(polygon.get(0), polygon.get(1), polygon.get(2), polygon.get(0));
        assertEquals(polygon, otherPolygon, "polygon constructed from all points of existing polygon with first point "
                + "duplicated at end is equal to original");
        // Make a Polygon3d from Point3d where last point differs from first only in y
        new Polygon3d(polygon.get(0), polygon.get(1), polygon.get(2), new Point3d(polygon.get(0).x, 123, polygon.get(0).z));
        // Make a Polygon3d from Point3d where last point differs from first only in z
        new Polygon3d(polygon.get(0), polygon.get(1), polygon.get(2), new Point3d(polygon.get(0).x, polygon.get(0).y, 123));

        x = new double[] {1, 3, 1}; // x coordinate of last point matches that of first
        y = new double[] {2, 1, 10}; // not true for y coordinates
        z = new double[] {2, 3, 2}; // true for z
        polygon = new Polygon3d(x, y, z);
        // System.out.println(polygon);
        checkPolygon("constructed from arrays with first and last x equal", x, y, z, polygon);

        x = new double[] {1, 3, 1}; // x coordinate of last point matches that of first
        y = new double[] {2, 1, 2}; // same for y
        z = new double[] {2, 3, 4}; // not true for z
        polygon = new Polygon3d(x, y, z);
        // System.out.println(polygon);
        checkPolygon("constructed from arrays with first and last x equal", x, y, z, polygon);

        x = new double[] {1, 3, 5, 3};
        y = new double[] {2, 2, 10, 10};
        z = new double[] {4, 4, 4, 4};
        polygon = new Polygon3d(x, y, z);
        checkPolygon("constructed from arrays", x, y, z, polygon);
        // convert the points of polygon to an array of Point3d
        List<Point3d> list = new ArrayList<>();
        polygon.iterator().forEachRemaining(list::add);
        otherPolygon = new Polygon3d(list);
        assertEquals(polygon, otherPolygon, "Polygon created from polygon points is equal to original polygon");
        otherPolygon = new Polygon3d(list.get(0), list.get(1), list.get(2), list.get(3));
        assertEquals(polygon, otherPolygon, "Polygon created from all four points of existing polygon is equal to original");

        Point3d[] pointArray = list.toArray(new Point3d[0]);
        otherPolygon = new Polygon3d(pointArray);
        assertEquals(polygon, otherPolygon, "Polygon created from array of points of existing polygon is equal to original");

        list.add(list.get(0));
        otherPolygon = new Polygon3d(list.iterator());
        assertEquals(polygon, otherPolygon,
                "Polygon created from polygon points and duplicate of first point at end is equal to original polygon");

        otherPolygon = new Polygon3d(list);
        assertEquals(polygon, otherPolygon,
                "Polygon created from polygon points and duplicate of first point at end is equal to original polygon");
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
            new Polygon3d(new double[] {1, 2, 3}, new double[] {1, 2, 3}, new double[] {1, 2, 3, 4});
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] {1, 2, 3}, new double[] {1, 2, 3, 4}, new double[] {1, 2, 3});
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] {1, 2, 3, 4}, new double[] {1, 2, 3}, new double[] {1, 2, 3});
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(null, new double[] {1, 2, 3}, new double[] {1, 2, 3});
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] {1, 2, 3}, null, new double[] {1, 2, 3});
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] {1, 2, 3}, new double[] {1, 2, 3}, null);
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon3d(new double[] {1}, new double[] {1}, new double[] {1});
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
                    new Point3d[] {new Point3d(1, 2, 3), new Point3d(1, 2, 3)});
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

        assertEquals(2, new Polygon3d(true, points).size(), "After filtering; there are two points left");

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

        assertEquals(2, new Polygon3d(true, list).size(), "After filtering; there are two points left");
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
        assertEquals(2, r.size(), "result has size 2");
        assertEquals(p1, r.get(0), "point 0 is p1");
        assertEquals(p0, r.get(1), "point 1 is p0");

        Polygon3d l05 = new Polygon3d(p0, p1, p2, p3, p4, p5);
        r = l05.reverse();
        assertEquals(6, r.size(), "result has size 6");
        assertEquals(p5, r.get(0), "point 0 is p5");
        assertEquals(p4, r.get(1), "point 1 is p4");
        assertEquals(p3, r.get(2), "point 2 is p3");
        assertEquals(p2, r.get(3), "point 3 is p2");
        assertEquals(p1, r.get(4), "point 4 is p1");
        assertEquals(p0, r.get(5), "point 5 is p0");

        PolyLine2d l2d = l05.project();
        assertEquals(6, l2d.size(), "result has size 6");
        assertEquals(p0.project(), l2d.get(0), "point 0 is p5");
        assertEquals(p1.project(), l2d.get(1), "point 1 is p4");
        assertEquals(p2.project(), l2d.get(2), "point 2 is p3");
        assertEquals(p3.project(), l2d.get(3), "point 3 is p2");
        assertEquals(p4.project(), l2d.get(4), "point 4 is p1");
        assertEquals(p5.project(), l2d.get(5), "point 5 is p0");

        l05 = new Polygon3d(p0, p1, p2, p2x, p3, p4, p5);
        l2d = l05.project();
        assertEquals(6, l2d.size(), "result has size 6");
        assertEquals(p0.project(), l2d.get(0), "point 0 is p5");
        assertEquals(p1.project(), l2d.get(1), "point 1 is p4");
        assertEquals(p2.project(), l2d.get(2), "point 2 is p3");
        assertEquals(p3.project(), l2d.get(3), "point 3 is p2");
        assertEquals(p4.project(), l2d.get(4), "point 4 is p1");
        assertEquals(p5.project(), l2d.get(5), "point 5 is p0");

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
            assertEquals(x[index], polygon.getX(index), Math.ulp(x[index]), where + " x[index]");
            assertEquals(y[index], polygon.getY(index), Math.ulp(y[index]), where + " y[index]");
            assertEquals(z[index], polygon.getZ(index), Math.ulp(z[index]), where + " z[index]");
            LineSegment3d segment = polygon.getSegment(index);
            assertEquals(x[index], segment.startX, Math.ulp(x[index]), where + " segment start x");
            assertEquals(y[index], segment.startY, Math.ulp(y[index]), where + " segment start y");
            assertEquals(z[index], segment.startZ, Math.ulp(z[index]), where + " segment start z");
            int wrappedIndex = (index + 1) % polygon.size();
            assertEquals(x[wrappedIndex], segment.endX, Math.ulp(x[wrappedIndex]), where + " segment end x");
            assertEquals(y[wrappedIndex], segment.endY, Math.ulp(y[wrappedIndex]), where + " segment end y");
            assertEquals(z[wrappedIndex], segment.endZ, Math.ulp(z[wrappedIndex]), where + " segment end z");
            cumulativeLength += segment.getLength();
        }
        assertEquals(cumulativeLength, polygon.getLength(), polygon.size() * Math.ulp(cumulativeLength),
                where + " circumference");
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point3d[] points = new Point3d[] {new Point3d(123.456, 345.678, 901.234), new Point3d(234.567, 456.789, 12.345),
                new Point3d(-12.345, -34.567, 45.678)};
        Polygon3d pl = new Polygon3d(points);
        String[] out = Export.toTsv(pl).split("\\n");
        assertEquals(points.length + 1, out.length, "Excel output consists of one line per point plus one");
        for (int index = 0; index <= points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals(3, fields.length, "each line consists of three fields");
            try
            {
                double x = Double.parseDouble(fields[0].trim());
                assertEquals(points[index % pl.size()].x, x, 0.001, "x matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("First field " + fields[0] + " does not parse as a double");
            }
            try
            {
                double y = Double.parseDouble(fields[1].trim());
                assertEquals(points[index % pl.size()].y, y, 0.001, "y matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[1] + " does not parse as a double");
            }
            try
            {
                double z = Double.parseDouble(fields[2].trim());
                assertEquals(points[index % pl.size()].z, z, 0.001, "z matches");
            }
            catch (NumberFormatException nfe)
            {
                fail("Second field " + fields[2] + " does not parse as a double");
            }
        }
    }

}
