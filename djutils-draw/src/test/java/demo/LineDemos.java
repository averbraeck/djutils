package demo;

import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.DrawRuntimeException;
import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;

/**
 * LineDemos.java.
 * <p>
 * Copyright (c) 2021-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class LineDemos
{
    /**
     * Do not instantiate.
     */
    private LineDemos()
    {
        // Do not instantiate
    }

    /**
     * Demonstrate the line classes.
     * @param args String[]; the command line arguments (not used)
     * @throws DrawRuntimeException ...
     */
    public static void main(final String[] args) throws DrawRuntimeException
    {
        PolyLine2d pl1 = new PolyLine2d(new Point2d(1, 2), new Point2d(3, 4), new Point2d(20, -5));
        System.out.println(pl1);
        Point2d[] pointArray = new Point2d[] {new Point2d(1, 2), new Point2d(3, 4), new Point2d(20, -5)};
        PolyLine2d pl2 = new PolyLine2d(pointArray);
        System.out.println(pl2);
        double[] x = new double[] {1, 3, 20};
        double[] y = new double[] {2, 4, -5};
        PolyLine2d pl3 = new PolyLine2d(x, y);
        System.out.println(pl3);
        List<Point2d> pointList = new ArrayList<>();
        pointList.add(new Point2d(1, 2));
        pointList.add(new Point2d(3, 4));
        pointList.add(new Point2d(20, -5));
        PolyLine2d pl4 = new PolyLine2d(pointList);
        System.out.println(pl4);

        PolyLine2d polyLine2d = new PolyLine2d(new Point2d(1, 1), new Point2d(5, 1), new Point2d(5, 2), new Point2d(9, 5));
        System.out.println("PolyLine: " + polyLine2d);
        System.out.println("length: " + polyLine2d.getLength());
        System.out.println("fragment: " + polyLine2d.extract(2.0, 9.0));
        System.out.println("fragment: " + polyLine2d.extractFractional(0.2, 0.9));

        System.out.println("PolyLine: " + polyLine2d);
        System.out.println("location at distance 7.0: " + polyLine2d.getLocation(7.0));
        System.out.println("extended location at distance 15: " + polyLine2d.getLocationExtended(15.0));
        System.out.println("extended location at distance -8: " + polyLine2d.getLocationExtended(-8.0));

        System.out.println("PolyLine: " + polyLine2d);
        for (int index = 0; index < polyLine2d.size() - 1; index++)
        {
            System.out.println("segment " + index + ": " + polyLine2d.getSegment(index));
        }

        System.out.print(polyLine2d.toPlot());

        System.out.println("PolyLine: " + polyLine2d);
        System.out.println("closest point to (0,1): " + polyLine2d.closestPointOnPolyLine(new Point2d(0, 1)));
        System.out.println("closest point to (6,0): " + polyLine2d.closestPointOnPolyLine(new Point2d(6, 0)));
        System.out.println("closest point to (10,0): " + polyLine2d.closestPointOnPolyLine(new Point2d(10, 0)));
        System.out.println("closest point to (50,0): " + polyLine2d.closestPointOnPolyLine(new Point2d(50, 0)));
        System.out.println("project (0,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(0, 0)));
        System.out.println("project (4,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(4, 0)));
        System.out.println("project (5,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(5, 0)));
        System.out.println("project (6,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(6, 0)));
        System.out.println("project (10,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(10, 0)));
        System.out.println("project (50,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(50, 0)));
        System.out.println("project (50,0) orthogonal extended: " + polyLine2d.projectOrthogonalExtended(new Point2d(50, 0)));

        LineSegment2d ls1 = new LineSegment2d(new Point2d(1, 2), new Point2d(5, 0));
        System.out.println("ls1: " + ls1);
        LineSegment2d ls2 = new LineSegment2d(new Point2d(1, 2), 5, 0);
        System.out.println("ls2: " + ls2);
        LineSegment2d ls3 = new LineSegment2d(1, 2, new Point2d(5, 0));
        System.out.println("ls3: " + ls3);
        LineSegment2d ls4 = new LineSegment2d(1, 2, 5, 0);
        System.out.println("ls4: " + ls4);
    }
}
