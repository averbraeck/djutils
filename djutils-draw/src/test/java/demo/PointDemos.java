package demo;

import org.djutils.draw.point.Point2d;

/**
 * PointDemos.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class PointDemos
{
    /**
     * Do not instantiate.
     */
    private PointDemos()
    {
        // Do not instantiate
    }

    /**
     * Demonstrate the Point classes.
     * @param args the command line arguments; not used
     */
    public static void main(final String[] args)
    {
        Point2d point1 = new Point2d(123.45, 234.56);
        System.out.println(point1);
        double[] coordinates = new double[] {123.45, 234.56};
        Point2d point2 = new Point2d(coordinates);
        System.out.println(point2);
        java.awt.geom.Point2D awtPoint2D = new java.awt.geom.Point2D.Double(123.45, 234.56);
        Point2d point3 = new Point2d(awtPoint2D);
        System.out.println(point3);
        java.awt.geom.Point2D awtPoint2DCopy = point3.toPoint2D();
        System.out.println(awtPoint2DCopy);

        Point2d secondPoint2d = new Point2d(234.56, 345.78);
        System.out.println("Direction to: " + point1.directionTo(secondPoint2d));
        System.out.println("Distance: " + point1.distance(secondPoint2d));
        System.out.println("Distance squared " + point1.distanceSquared(secondPoint2d));
        System.out.println("Interpolation at fraction 0.3: " + point1.interpolate(secondPoint2d, 0.3));

        System.out.println("Almost equals to another Point2d: " + point1.epsilonEquals(new Point2d(123, 235), 0.5));

        System.out.println("The point: " + point1);
        System.out.println("The point translated over 10, -20: " + point1.translate(10, -20));
        System.out.println("The point scaled by 2: " + point1.scale(2.0));
        System.out.println("The point negated: " + point1.neg());
        System.out.println("The point normalized: " + point1.normalize());
        System.out.println("The point with absolute values: " + point1.abs());
        System.out.println("The point negated, then absolute: " + point1.neg().abs());

    }
}
