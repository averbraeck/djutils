package demo;


import org.djutils.draw.point.Point2d;

/**
 * PointDemos.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
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
     * @param args String[]; the command line arguments; not used
     */
    public static void main(final String[] args)
    {
        Point2d point2d = new Point2d(123.45, 234.56);
        System.out.println(point2d);
        double[] coordinates = new double[] { 123.45, 234.56 };
        point2d = new Point2d(coordinates);
        java.awt.geom.Point2D awtPoint2D = new java.awt.geom.Point2D.Double(123.45, 234.56);
        point2d = new Point2d(awtPoint2D);
        java.awt.geom.Point2D awtPoint2DCopy = point2d.toPoint2D();
        System.out.println(awtPoint2DCopy);
        Point2d secondPoint2d = new Point2d(234.56, 345.78);
        System.out.println("Direction to: " + point2d.directionTo(secondPoint2d));
        System.out.println("Distance: " + point2d.directionTo(secondPoint2d));
        System.out.println("Interpolation at fraction 0.3: " + point2d.interpolate(secondPoint2d, 0.3));
        
        System.out.println("The point: " + point2d);
        System.out.println("The point translated over 10, -20: " + point2d.translate(10, -20));
        System.out.println("The point scaled by 2: " + point2d.scale(2.0));
        System.out.println("The point negated: " + point2d.neg());
        System.out.println("The point normalized: " + point2d.normalize());
        System.out.println("The point with absolute values: " + point2d.abs());
        System.out.println("The point negated, then absolute: " + point2d.neg().abs());
        
    }
}
