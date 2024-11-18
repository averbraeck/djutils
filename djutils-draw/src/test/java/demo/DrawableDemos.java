package demo;

import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;

/**
 * DrawableDemos.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class DrawableDemos
{
    /**
     * Do not instantiate.
     */
    private DrawableDemos()
    {
        // Do not instantiate
    }

    /**
     * Demonstrate the Drawable interface.
     * @param args the command line arguments; not used
     */
    public static void main(final String... args)
    {
        Point2d p = new Point2d(12.34, 23.45);
        System.out.println("The iterator of a point yields one point (the point itself):");
        for (Point2d pp : p)
        {
            System.out.println(pp);
        }
        LineSegment2d ls = new LineSegment2d(12.34, 23.45, 34.56, 45.67);
        System.out.println("The iterator of a line segment yields two points (the start point and the end point):");
        for (Point2d pp : ls)
        {
            System.out.println(pp);
        }
        Bounds3d b = new Bounds3d(12.34, 23.45, 34.56, 45.67, 56.78, 67.89);
        System.out.println("The iterator of a bounds3d yields eight points (the vertices of the bounds):");
        for (Point3d pp : b)
        {
            System.out.println(pp);
        }

        Point2d point2d = new Point2d(12.34, 23.45);
        System.out.println("2D object has two dimensions: " + point2d.getDimensions());
        Point3d point3d = new Point3d(12.34, 23.45, 34.56);
        System.out.println("3D object has three dimensions: " + point3d.getDimensions());

    }
}
