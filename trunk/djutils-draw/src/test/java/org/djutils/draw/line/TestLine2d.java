package org.djutils.draw.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.DrawException;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * TestLine2d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class TestLine2d
{

    /**
     * Test all the constructors of Point3d.
     * @param points Point3d[]; array of Point3d to test with
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    private void runConstructors(final Point2d[] points) throws DrawException
    {
        verifyPoints(new Line2d(points), points);
        List<Point2d> list = new ArrayList<>();
        for (int i = 0; i < points.length; i++)
        {
            list.add(points[i]);
        }
        Line2d line = new Line2d(list);
        verifyPoints(line, points);
        verifyPoints(new Line2d(line.getPoints()), points);
        double length = 0;
        for (int i = 1; i < points.length; i++)
        {
            length += Math.sqrt(Math.pow(points[i].getX() - points[i - 1].getX(), 2)
                    + Math.pow(points[i].getY() - points[i - 1].getY(), 2));
        }
        assertEquals("length", length, line.getLength(), 10 * Math.ulp(length));

        // Construct a Path3D.Double that contains the horizontal moveto or lineto
        int horizontalMoves = 0;
        Path2D path = new Path2D.Double();
        path.moveTo(points[0].getX(), points[0].getY());
        // System.out.print("path is "); printPath2D(path);
        for (int i = 1; i < points.length; i++)
        {
            // Path2D is corrupt if same point is added twice in succession
            if (points[i].getX() != points[i - 1].getX() || points[i].getY() != points[i - 1].getY())
            {
                path.lineTo(points[i].getX(), points[i].getY());
                horizontalMoves++;
            }
        }
        try
        {
            line = new Line2d(path);
            if (0 == horizontalMoves)
            {
                fail("Construction of Line3d from path with degenerate projection should have failed");
            }
            // This new Line3d has z=0 for all points so veryfyPoints won't work
            assertEquals("number of points should match", horizontalMoves + 1, line.size());
            int indexInLine = 0;
            for (int i = 0; i < points.length; i++)
            {
                if (i > 0 && (points[i].getX() != points[i - 1].getX() || points[i].getY() != points[i - 1].getY()))
                {
                    indexInLine++;
                }
                assertEquals("x in line", points[i].getX(), line.get(indexInLine).getX(), 0.001);
                assertEquals("y in line", points[i].getY(), line.get(indexInLine).getY(), 0.001);
            }
        }
        catch (DrawException e)
        {
            if (0 != horizontalMoves)
            {
                fail("Construction of Line3d from path with non-degenerate projection should not have failed");
            }
        }
    }

    /**
     * Test construction of a Line3d from a Path2D with SEG_CLOSE.
     * @throws DrawException on unexpected error
     */
    @Test
    public void testPathWithClose() throws DrawException
    {
        Path2D path = new Path2D.Double();
        path.moveTo(1, 2);
        path.lineTo(4, 5);
        path.lineTo(4, 8);
        path.closePath();
        Line2d line = new Line2d(path);
        assertEquals("line has 4 points", 4, line.size());
        assertEquals("first point equals last point", line.getFirst(), line.getLast());
        // Now the case that the path was already closed
        path = new Path2D.Double();
        path.moveTo(1, 2);
        path.lineTo(4, 5);
        path.lineTo(1, 2);
        path.closePath();
        line = new Line2d(path);
        assertEquals("line has 4 points", 3, line.size());
        assertEquals("first point equals last point", line.getFirst(), line.getLast());

    }

    /**
     * Verify that a Line3d contains the same points as an array of Point3d.
     * @param line Line3d; the OTS line
     * @param points Point3d[]; the OTSPoint array
     * @throws DrawException should not happen; this test has failed if it does happen
     */
    private void verifyPoints(final Line2d line, final Point2d[] points) throws DrawException
    {
        assertEquals("Line should have same number of points as point array", line.size(), points.length);
        for (int i = 0; i < points.length; i++)
        {
            assertEquals("x of point i should match", points[i].getX(), line.get(i).getX(), Math.ulp(points[i].getX()));
            assertEquals("y of point i should match", points[i].getY(), line.get(i).getY(), Math.ulp(points[i].getY()));
        }
    }

}
