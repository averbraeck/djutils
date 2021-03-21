package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

/**
 * Polygon2dTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Polygon2dTest
{

    /**
     * Test the constructors.
     */
    @Test
    public void testConstructors()
    {
        double[] x = new double[] { 1, 3, 5 };
        double[] y = new double[] { 2, 1, 10 };
        double actualSurface = ((x[0] - x[2]) * (y[1] - y[0]) - (x[0] - x[1]) * (y[2] - y[0])) / 2;

        Polygon2d polygon = new Polygon2d(x, y);
        checkPolygon("constructed from arrays", x, y, polygon, actualSurface, true);
        Polygon2d reversed = polygon.reverse();
        assertEquals("surface of reversed polygon", -actualSurface, reversed.surface(), Math.ulp(-actualSurface));
        assertTrue("reversed polygon is also convex", reversed.isConvex());

        x = new double[] { 1, 3, 5, 1 };
        y = new double[] { 2, 1, 10, 2 };
        polygon = new Polygon2d(x, y); // Last point is duplicate of first point; should be handled gracefully
        assertTrue("toString returns something descriptive", polygon.toString().startsWith("Polygon2d"));
        assertTrue("toString can suppress the class name", polygon.toString().indexOf(polygon.toString(true)) > 0);
        checkPolygon("constructed from arrays", x, y, polygon, actualSurface, true);
        assertEquals("surface of reversed polygon", -actualSurface, polygon.reverse().surface(), Math.ulp(-actualSurface));
        Polygon2d otherPolygon = new Polygon2d(polygon.get(0), polygon.get(1), polygon.get(2), polygon.get(0));
        assertEquals("polygon constructed from all points of existing polygon with first point duplicated at end is equal "
                + "to original", polygon, otherPolygon);
        // Make a Polygon2d from Point2d where last point differs from first only in y
        new Polygon2d(polygon.get(0), polygon.get(1), polygon.get(2), new Point2d(polygon.get(0).x, 123));

        x = new double[] { 1, 3, 1 }; // x coordinate of last point matches that of first
        y = new double[] { 2, 1, 10 }; // not true for y coordinates
        polygon = new Polygon2d(x, y);
        // System.out.println(polygon);
        actualSurface = ((x[0] - x[2]) * (y[1] - y[0]) - (x[0] - x[1]) * (y[2] - y[0])) / 2;
        checkPolygon("constructed from arrays with first and last x equal", x, y, polygon, actualSurface, true);

        x = new double[] { 1, 3, 5, 3 };
        y = new double[] { 2, 2, 10, 10 };
        actualSurface = 2 * 8; // Parallelogram surface with two sides parallel to X-axis is easy
        polygon = new Polygon2d(x, y);
        checkPolygon("constructed from arrays", x, y, polygon, actualSurface, true);
        assertEquals("surface of reversed polygon", -actualSurface, polygon.reverse().surface(), Math.ulp(-actualSurface));
        // convert the points of polygon to an array of Point2d
        List<Point2d> list = new ArrayList<>();
        polygon.getPoints().forEachRemaining(list::add);
        otherPolygon = new Polygon2d(list);
        assertEquals("Polygon created from polygon points is equal to original polygon", polygon, otherPolygon);
        otherPolygon = new Polygon2d(list.get(0), list.get(1), list.get(2), list.get(3));
        assertEquals("Polygon created from all four points of existing polygon is equal to original", polygon, otherPolygon);
        
        Point2d[] pointArray = list.toArray(new Point2d[0]);
        otherPolygon = new Polygon2d(pointArray);
        assertEquals("Polygon created from array of points of existing polygon is equal to original", polygon, otherPolygon);

        list.add(list.get(0));
        otherPolygon = new Polygon2d(list.iterator());
        assertEquals("Polygon created from polygon points and duplicate of first point at end is equal to original polygon",
                polygon, otherPolygon);

        otherPolygon = new Polygon2d(list);
        assertEquals("Polygon created from polygon points and duplicate of first point at end is equal to original polygon",
                polygon, otherPolygon);
        // Add a point that only differs in y
        list.add(new Point2d(list.get(0).x, 123));
        new Polygon2d(list.iterator());
        list.add(list.get(0));
        new Polygon2d(list.iterator());
        
        // Make last TWO points duplicate of first point
        list.add(list.get(0));
        try
        {
            new Polygon2d(list);
            fail("last two points equal to first point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        
        // Non convex polygon with unneeded points in horizontal and vertical side
        x = new double[] {0, 5, 10, 5, 10, 0, 0};
        y = new double[] {0, 0, 0, 5, 10, 10, 5};
        polygon = new Polygon2d(x, y);
        checkPolygon("non convex polygon", x, y, polygon, 100 - 25, false);
        assertFalse("reversed non-convex polygon is also non-convex", polygon.reverse().isConvex());

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
            new Polygon2d(new double[] { 1, 2, 3 }, new double[] { 1, 2, 3, 4 });
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] { 1, 2, 3, 4 }, new double[] { 1, 2, 3 });
            fail("unequal length of coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(null, new double[] { 1, 2, 3 });
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] { 1, 2, 3 }, null);
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] { 1 }, new double[] { 1 });
            fail("too short coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new Point2d(1, 2), new Point2d(1, 2), new Point2d[] {});
            fail("too short coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new Point2d(1, 2), new Point2d(1, 2), (Point2d[]) null);
            fail("too short coordinate array should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new Point2d(1, 2), new Point2d(3, 2), new Point2d[] { new Point2d(1, 2), new Point2d(1, 2) });
            fail("two identical points at end, matching first point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }

        list.clear();
        list.add(new Point2d(1, 2));
        try
        {
            new Polygon2d(list);
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
        Point2d[] points = new Point2d[] {new Point2d(1, 2), new Point2d(1, 2), new Point2d(4, 5)};
        try
        {
            new Polygon2d(false, points);
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        
        assertEquals("After filtering; there are two points left", 2, new Polygon2d(true, points).size()); 
        
        List<Point2d> list = Arrays.asList(points);
        try
        {
            new Polygon2d(false, list);
            fail("duplicate point should have thrown a DrawRuntimeException");
        }
        catch (DrawRuntimeException dre)
        {
            // Ignore expected exception
        }
        
        assertEquals("After filtering; there are two points left", 2, new Polygon2d(true, list).size()); 
    }

    /**
     * Verify the various properties of a Polygon2d.
     * @param where String; description of the test
     * @param x double[]; the expected x coordinates
     * @param y double[]; the expected y coordinates
     * @param polygon Polygon2d; the Polygon2d
     * @param expectedSurface double; the expected surface of the polygon
     * @param isConvex boolean; the expected value returned by the isConvex method
     */
    private void checkPolygon(final String where, final double[] x, final double[] y, final Polygon2d polygon,
            final double expectedSurface, final boolean isConvex)
    {
        double cumulativeLength = 0;
        for (int index = 0; index < polygon.size(); index++)
        {
            assertEquals(where + " x[index]", x[index], polygon.getX(index), Math.ulp(x[index]));
            assertEquals(where + " y[index]", y[index], polygon.getY(index), Math.ulp(y[index]));
            LineSegment2d segment = polygon.getSegment(index);
            assertEquals(where + " segment start x", x[index], segment.startX, Math.ulp(x[index]));
            assertEquals(where + " segment start y", y[index], segment.startY, Math.ulp(y[index]));
            int wrappedIndex = (index + 1) % polygon.size();
            assertEquals(where + " segment end x", x[wrappedIndex], segment.endX, Math.ulp(x[wrappedIndex]));
            assertEquals(where + " segment end y", y[wrappedIndex], segment.endY, Math.ulp(y[wrappedIndex]));
            cumulativeLength += segment.getLength();
        }
        assertEquals(where + " surface", expectedSurface, polygon.surface(), Math.ulp(expectedSurface));
        assertEquals(where + " circumference", cumulativeLength, polygon.getLength(),
                polygon.size() * Math.ulp(cumulativeLength));
        assertEquals(where + " is convex?", isConvex, polygon.isConvex());
    }

    /**
     * Test the contains method.
     */
    @Test
    public void containsTest()
    {
        // Parallelogram that nowhere crosses integer coordinates; so there is a clear result for all integer coordinates
        Polygon2d polygon = new Polygon2d(new double[] { 4.8, 10.2, 15.2, 9.8 }, new double[] { -10.1, -10.1, 0.1, 0.1 });
        // System.out.print(polygon.toPlot() + " c1,0,0");
        for (int x = 0; x < 20; x += 1)
        {
            for (int y = -15; y < 5; y++)
            {
                boolean expected = y <= 0 && y >= -10 && x >= 10 + y * 0.5 && x <= 15 + y * 0.5;
                // System.out
                // .println(String.format("%s M%.1f,%.1f L%.1f,%.1f M%.1f,%.1f L%.1f,%.1f", expected ? "c1,0,0" : "c0,0,0",
                // x - 0.1, (double) y, x + 0.1, (double) y, (double) x, y - 0.1, (double) x, y + 0.1));
                assertEquals("contains", expected, polygon.contains(x, y));
                assertEquals("contains", expected, polygon.contains(new Point2d(x, y)));
            }
        }
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point2d[] points =
                new Point2d[] { new Point2d(123.456, 345.678), new Point2d(234.567, 456.789), new Point2d(-12.345, -34.567) };
        Polygon2d pl = new Polygon2d(points);
        String[] out = pl.toExcel().split("\\n");
        assertEquals("Excel output consists of one line per point plus one", points.length + 1, out.length);
        for (int index = 0; index <= points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals("each line consists of two fields", 2, fields.length);
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
        }

        out = pl.toPlot().split(" L");
        assertEquals("Plotter output consists of one coordinate pair per point plus one", points.length + 1, out.length);
        for (int index = 0; index < points.length; index++)
        {
            String[] fields = out[index].split(",");
            assertEquals("each line consists of two fields", 2, fields.length);
            if (index == 0)
            {
                assertTrue(fields[0].startsWith("M"));
                fields[0] = fields[0].substring(1);
            }
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
        }

    }

}
