package org.djutils.draw.line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.djutils.draw.Export;
import org.djutils.draw.Transform2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * Polygon2dTest.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Polygon2dTest
{

    /**
     * Test the constructors.
     */
    @Test
    public void testConstructors()
    {
        double[] x = new double[] {1, 3, 5};
        double[] y = new double[] {2, 1, 10};
        double actualSurface = ((x[0] - x[2]) * (y[1] - y[0]) - (x[0] - x[1]) * (y[2] - y[0])) / 2;

        Polygon2d polygon = new Polygon2d(x, y);
        checkPolygon("constructed from arrays", x, y, polygon, actualSurface, true);
        Polygon2d p2 = new Polygon2d(polygon);
        checkPolygon("constructed from existing polygon", x, y, p2, actualSurface, true);
        Polygon2d reversed = polygon.reverse();
        assertEquals(-actualSurface, reversed.surface(), Math.ulp(-actualSurface), "surface of reversed polygon");
        assertTrue(reversed.isConvex(), "reversed polygon is also convex");

        x = new double[] {1, 3, 5, 1};
        y = new double[] {2, 1, 10, 2};
        polygon = new Polygon2d(x, y); // Last point is duplicate of first point; should be handled gracefully
        assertTrue(polygon.toString().startsWith("Polygon2d"), "toString returns something descriptive");
        assertTrue(polygon.toString().indexOf(polygon.toString(true)) > 0, "toString can suppress the class name");
        checkPolygon("constructed from arrays", x, y, polygon, actualSurface, true);
        assertEquals(-actualSurface, polygon.reverse().surface(), Math.ulp(-actualSurface), "surface of reversed polygon");
        Polygon2d otherPolygon = new Polygon2d(polygon.get(0), polygon.get(1), polygon.get(2), polygon.get(0));
        assertEquals(polygon, otherPolygon,
                "polygon constructed from all points of existing polygon with first point duplicated at end is equal "
                        + "to original");
        // Make a Polygon2d from Point2d where last point differs from first only in y
        new Polygon2d(polygon.get(0), polygon.get(1), polygon.get(2), new Point2d(polygon.get(0).x, 123));

        x = new double[] {1, 3, 1}; // x coordinate of last point matches that of first
        y = new double[] {2, 1, 10}; // not true for y coordinates
        polygon = new Polygon2d(x, y);
        // System.out.println(polygon);
        actualSurface = ((x[0] - x[2]) * (y[1] - y[0]) - (x[0] - x[1]) * (y[2] - y[0])) / 2;
        checkPolygon("constructed from arrays with first and last x equal", x, y, polygon, actualSurface, true);

        x = new double[] {1, 3, 5, 3};
        y = new double[] {2, 2, 10, 10};
        actualSurface = 2 * 8; // Parallelogram surface with two sides parallel to X-axis is easy
        polygon = new Polygon2d(x, y);
        checkPolygon("constructed from arrays", x, y, polygon, actualSurface, true);
        assertEquals(-actualSurface, polygon.reverse().surface(), Math.ulp(-actualSurface), "surface of reversed polygon");
        // convert the points of polygon to an array of Point2d
        List<Point2d> list = new ArrayList<>();
        polygon.iterator().forEachRemaining(list::add);
        otherPolygon = new Polygon2d(list);
        assertEquals(polygon, otherPolygon, "Polygon created from polygon points is equal to original polygon");
        otherPolygon = new Polygon2d(list.get(0), list.get(1), list.get(2), list.get(3));
        assertEquals(polygon, otherPolygon, "Polygon created from all four points of existing polygon is equal to original");

        Point2d[] pointArray = list.toArray(new Point2d[0]);
        otherPolygon = new Polygon2d(pointArray);
        assertEquals(polygon, otherPolygon, "Polygon created from array of points of existing polygon is equal to original");

        list.add(list.get(0));
        otherPolygon = new Polygon2d(list.iterator());
        assertEquals(polygon, otherPolygon,
                "Polygon created from polygon points and duplicate of first point at end is equal to original polygon");

        otherPolygon = new Polygon2d(list);
        assertEquals(polygon, otherPolygon,
                "Polygon created from polygon points and duplicate of first point at end is equal to original polygon");
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
            fail("last two points equal to first point should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        // Non convex polygon with unneeded points in horizontal and vertical side
        x = new double[] {0, 5, 10, 5, 10, 0, 0};
        y = new double[] {0, 0, 0, 5, 10, 10, 5};
        polygon = new Polygon2d(x, y);
        checkPolygon("non convex polygon", x, y, polygon, 100 - 25, false);
        assertFalse(polygon.reverse().isConvex(), "reversed non-convex polygon is also non-convex");

        try
        {
            polygon.getSegment(-1);
            fail("Negative index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e)
        {
            // Ignore expected exception
        }

        try
        {
            polygon.getSegment(polygon.size());
            fail("index equal to size (or more) should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] {1, 2, 3}, new double[] {1, 2, 3, 4});
            fail("unequal length of coordinate array should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] {1, 2, 3, 4}, new double[] {1, 2, 3});
            fail("unequal length of coordinate array should have thrown a IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(null, new double[] {1, 2, 3});
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] {1, 2, 3}, null);
            fail("null for x array hould have thrown a NullPointerException");
        }
        catch (NullPointerException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new double[] {1}, new double[] {1});
            fail("too short coordinate array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new Point2d(1, 2), new Point2d(1, 2), new Point2d[] {});
            fail("too short coordinate array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new Point2d(1, 2), new Point2d(1, 2), (Point2d[]) null);
            fail("too short coordinate array should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        try
        {
            new Polygon2d(new Point2d(1, 2), new Point2d(3, 2), new Point2d[] {new Point2d(1, 2), new Point2d(1, 2)});
            fail("two identical points at end, matching first point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        list.clear();
        list.add(new Point2d(1, 2));
        try
        {
            new Polygon2d(list);
            fail("too short list should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException dre)
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
            new Polygon2d(PolyLine.NO_FILTER, points);
            fail("duplicate point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        assertEquals(2, new Polygon2d(0.0, points).size(), "After filtering; there are two points left");

        List<Point2d> list = Arrays.asList(points);
        try
        {
            new Polygon2d(PolyLine.NO_FILTER, list);
            fail("duplicate point should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Ignore expected exception
        }

        assertEquals(2, new Polygon2d(0.0, list).size(), "After filtering; there are two points left");
    }

    /**
     * Verify the various properties of a Polygon2d.
     * @param where description of the test
     * @param x the expected x coordinates
     * @param y the expected y coordinates
     * @param polygon the Polygon2d
     * @param expectedSurface the expected surface of the polygon
     * @param isConvex the expected value returned by the isConvex method
     */
    private void checkPolygon(final String where, final double[] x, final double[] y, final Polygon2d polygon,
            final double expectedSurface, final boolean isConvex)
    {
        double cumulativeLength = 0;
        for (int index = 0; index < polygon.size(); index++)
        {
            assertEquals(x[index], polygon.getX(index), Math.ulp(x[index]), where + " x[index]");
            assertEquals(y[index], polygon.getY(index), Math.ulp(y[index]), where + " y[index]");
            LineSegment2d segment = polygon.getSegment(index);
            assertEquals(x[index], segment.startX, Math.ulp(x[index]), where + " segment start x");
            assertEquals(y[index], segment.startY, Math.ulp(y[index]), where + " segment start y");
            int wrappedIndex = (index + 1) % polygon.size();
            assertEquals(x[wrappedIndex], segment.endX, Math.ulp(x[wrappedIndex]), where + " segment end x");
            assertEquals(y[wrappedIndex], segment.endY, Math.ulp(y[wrappedIndex]), where + " segment end y");
            cumulativeLength += segment.getLength();
        }
        assertEquals(expectedSurface, polygon.surface(), Math.ulp(expectedSurface), where + " surface");
        assertEquals(cumulativeLength, polygon.getLength(), polygon.size() * Math.ulp(cumulativeLength),
                where + " circumference");
        assertEquals(isConvex, polygon.isConvex(), where + " is convex?");
    }

    /**
     * Test the contains method for a Point2D.
     */
    @Test
    public void containsPointTest()
    {
        // Parallelogram that nowhere crosses integer coordinates; so there is a clear result for all integer coordinates
        Polygon2d polygon = new Polygon2d(new double[] {4.8, 10.2, 15.2, 9.8}, new double[] {-10.1, -10.1, 0.1, 0.1});
        // System.out.print(polygon.toPlot() + " c1,0,0");
        for (int x = 0; x < 20; x += 1)
        {
            for (int y = -15; y < 5; y++)
            {
                boolean expected = pointInParallelogram(x, y);
                // System.out
                // .println(String.format("%s M%.1f,%.1f L%.1f,%.1f M%.1f,%.1f L%.1f,%.1f", expected ? "c1,0,0" : "c0,0,0",
                // x - 0.1, (double) y, x + 0.1, (double) y, (double) x, y - 0.1, (double) x, y + 0.1));
                assertEquals(expected, polygon.contains(x, y), "contains");
                assertEquals(expected, polygon.contains(new Point2d(x, y)), "contains");
            }
        }
    }

    /**
     * Test the contains method for a Bounds2d.
     */
    @Test
    public void containsBoundsTest()
    {
        // Parallelogram that nowhere crosses integer coordinates; so there is a clear result for all integer coordinates
        Polygon2d polygon = new Polygon2d(new double[] {4.8, 10.2, 15.2, 9.8}, new double[] {-10.1, -10.1, 0.1, 0.1});
        // System.out.print(polygon.toPlot() + " c1,0,0");
        for (int x = 0; x < 20; x += 1)
        {
            for (int y = -15; y < 5; y++)
            {
                for (int dX = 0; dX < 4; dX++)
                {
                    for (int dY = 0; dY < 4; dY++)
                    {
                        boolean expected = dX > 0 && dY > 0 && pointInParallelogram(x, y) && pointInParallelogram(x + dX, y)
                                && pointInParallelogram(x, y + dY) && pointInParallelogram(x + dX, y + dY);
                        Bounds2d bounds = new Bounds2d(new Point2d(x, y), new Point2d(x + dX, y + dY));
                        assertEquals(expected, polygon.contains(bounds), "contains");
                    }
                }
            }
        }
    }

    /**
     * Construct a list of Point2d spread out regularly over a circle.
     * @param centerX center X of the circle
     * @param centerY center Y of the circle
     * @param radius radius of the circle
     * @param size number of points in the polygon
     * @return the points that lie on a regular polygon
     */
    private List<Point2d> makePolygon(final double centerX, final double centerY, final double radius, final int size)
    {
        List<Point2d> points = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
        {
            double angle = Math.PI * 2 * i / size;
            points.add(new Point2d(centerX + radius * Math.cos(angle), centerY + radius * Math.sin(angle)));
        }
        return points;
    }

    /**
     * Test the intersects intersects method.
     */
    @Test
    public final void testIntersects()
    {
        double radius = 10;
        double cx = 5;
        double cy = -5;
        Polygon2d reference = new Polygon2d(makePolygon(cx, cy, radius, 18));
        for (int dx = -20; dx <= 20; dx++)
        {
            for (int dy = -20; dy <= 20; dy++)
            {
                boolean hit = true;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double radius2 = 2;
                if (distance > radius + radius2)
                {
                    hit = false;
                }
                else if (distance > radius + radius2 - 0.1)
                {
                    continue; // too close to be sure
                }
                Polygon2d other = new Polygon2d(makePolygon(cx + dx, cy + dy, radius2, 16));
                if (hit)
                {
                    assertTrue(reference.intersects(other), "shapes hit");
                }
                else
                {
                    assertFalse(reference.intersects(other), "shapes do not hit");
                }
            }
        }
        reference = new Polygon2d(new Point2d[] {new Point2d(0, 0), new Point2d(10, 0), new Point2d(10, 10)});
        // Make shapes that overlap along the X axis
        for (int dx = -20; dx <= 20; dx++)
        {
            Polygon2d other = new Polygon2d(new Point2d[] {new Point2d(dx, 0), new Point2d(dx + 5, 0), new Point2d(dx, -20)});
            boolean hit = dx >= -5 && dx <= 10;
            // System.out.println("hit="+hit+"\treference: " + reference + "\tother: "+ other);
            if (hit)
            {
                assertTrue(reference.intersects(other), "shapes hit");
            }
            else
            {
                assertFalse(reference.intersects(other), "shapes do not hit");
            }
        }
        // Make shapes that overlap along the Y axis
        for (int dy = -20; dy <= 20; dy++)
        {
            Polygon2d other = new Polygon2d(new Point2d[] {new Point2d(20, dy), new Point2d(10, dy), new Point2d(10, dy + 10)});
            boolean hit = dy >= -10 && dy <= 10;
            if (hit)
            {
                assertTrue(reference.intersects(other), "shapes hit");
            }
            else
            {
                assertFalse(reference.intersects(other), "shapes do not hit");
            }
        }
        // Make vertical and horizontal box
        Polygon2d vertical = new Polygon2d(new Point2d[] {new Point2d(-1, -10), new Point2d(1, -10), new Point2d(1, 10),
                new Point2d(-1, 10), new Point2d(-1, -10)});
        Polygon2d horizontal = new Polygon2d(new Point2d[] {new Point2d(-10, -1), new Point2d(10, -1), new Point2d(10, 1),
                new Point2d(-10, 1), new Point2d(-10, -1)});
        assertTrue(vertical.intersects(horizontal), "shapes hit");
        // Test all cases of co-linear edges
        Polygon2d one = new Polygon2d(new Point2d(3, 4), new Point2d(4, 3), new Point2d(4, -3), new Point2d(3, -4),
                new Point2d(-3, -4), new Point2d(-4, -3), new Point2d(-4, 3), new Point2d(-3, 4));
        Polygon2d two = new Polygon2d(new Transform2d().translate(8, 0).transform(one.iterator()));
        assertTrue(one.intersects(two), "shapes hit");
        two = new Polygon2d(new Transform2d().translate(8, 2).transform(one.iterator()));
        assertTrue(one.intersects(two), "shapes hit");
        two = new Polygon2d(new Transform2d().translate(8, 6).transform(one.iterator()));
        assertTrue(one.intersects(two), "shapes hit");
        two = new Polygon2d(new Transform2d().translate(7.75, 6.25).transform(one.iterator()));
        assertTrue(one.intersects(two), "shapes hit");
        two = new Polygon2d(new Transform2d().translate(7, 7).transform(one.iterator()));
        assertTrue(one.intersects(two), "shapes hit");

    }

    /**
     * Test code used in the contains tests. Only works for the parallelogram that is used in those tests.
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return true if (x,y) is inside the parallelogram; false if (x,y) is not inside the parallelogram
     */
    private boolean pointInParallelogram(final int x, final int y)
    {
        return y <= 0 && y >= -10 && x >= 10 + y * 0.5 && x <= 15 + y * 0.5;
    }

    /**
     * Test the debugging output methods.
     */
    @Test
    public void testExports()
    {
        Point2d[] points =
                new Point2d[] {new Point2d(123.456, 345.678), new Point2d(234.567, 456.789), new Point2d(-12.345, -34.567)};
        Polygon2d pl = new Polygon2d(points);
        String[] out = Export.toTsv(pl).split("\\n");
        assertEquals(points.length + 1, out.length, "Excel output consists of one line per point plus one");
        for (int index = 0; index <= points.length; index++)
        {
            String[] fields = out[index].split("\\t");
            assertEquals(2, fields.length, "each line consists of two fields");
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
        }

        out = Export.toPlot(pl).split(" L");
        assertEquals(points.length + 1, out.length, "Plotter output consists of one coordinate pair per point plus one");
        for (int index = 0; index < points.length; index++)
        {
            String[] fields = out[index].split(",");
            assertEquals(2, fields.length, "each line consists of two fields");
            if (index == 0)
            {
                assertTrue(fields[0].startsWith("M"));
                fields[0] = fields[0].substring(1);
            }
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
        }

        Path2D path = pl.toPath2D();
        int index = 0;
        for (PathIterator pi = path.getPathIterator(null); !pi.isDone(); pi.next())
        {
            double[] p = new double[6];
            int segType = pi.currentSegment(p);
            if (segType == PathIterator.SEG_MOVETO || segType == PathIterator.SEG_LINETO)
            {
                if (index == 0)
                {
                    assertEquals(PathIterator.SEG_MOVETO, segType, "First segment must be move");
                }
                else
                {
                    assertEquals(PathIterator.SEG_LINETO, segType, "Additional segments are line segments");
                }
                assertEquals(points[index].x, p[0], 0.00001, "X coordinate");
                assertEquals(points[index].y, p[1], 0.00001, "Y coordinate");
            }
            else if (index == points.length)
            {
                assertEquals(PathIterator.SEG_CLOSE, segType, "Last segment must be close");
            }
            else
            {
                fail("Unexpected segment type");
            }
            index++;
        }
    }

    /**
     * Test issue 15; simple polygon should contain a specified point, but the contains method says it does not.
     */
    @Test
    public void testIssue15()
    {
        Polygon2d polygon = new Polygon2d(new Point2d(0, -1), new Point2d(1000, 1), new Point2d(1000, -3), new Point2d(0, -5));
        Point2d point = new Point2d(1, -3);
        assertTrue(polygon.contains(point));
    }

}
