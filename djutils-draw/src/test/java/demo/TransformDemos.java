package demo;

import org.djutils.draw.Transform2d;
import org.djutils.draw.Transform3d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;

/**
 * TransformDemos.java.
 * <p>
 * Copyright (c) 2021-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class TransformDemos
{
    /**
     * Do not instantiate.
     */
    private TransformDemos()
    {
        // Do not instantiate
    }

    /**
     * Demonstrate the transformations.
     * @param args String...; the command line arguments (not used)
     */
    public static void main(final String... args)
    {
        translateAndRotate();
        shear2d();
        shear3d();
        scale();
        transformPolyLine();
    }

    /**
     * Demonstrate translate and rotate.
     */
    public static void translateAndRotate()
    {
        System.out.println("Translate and rotate");
        Point3d point = new Point3d(1, 0, 0);
        System.out.println(point);
        Transform3d transform1 = new Transform3d();
        transform1.translate(2, 5, 8); // Translate
        System.out.println(transform1.transform(point));
        transform1.rotY(Math.PI / 2);
        System.out.println(transform1.transform(point));

        Transform3d transform2 = new Transform3d().translate(2, 5, 8).rotY(Math.PI / 2);
        System.out.println(transform2.transform(point));
    }

    /**
     * Demonstrate 2D shear.
     */
    public static void shear2d()
    {
        System.out.println("2D shear");
        Transform2d transform = new Transform2d().shear(2, 3);
        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 2; y++)
            {
                Point2d p = new Point2d(x, y);
                System.out.println(p + " -> " + transform.transform(p));
            }
        }
    }

    /**
     * Demonstrate 3d shear.
     */
    public static void shear3d()
    {
        System.out.println("3D shear");
        Transform3d transform = new Transform3d().shearXY(2, 3);
        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 2; y++)
            {
                for (int z = 0; z < 2; z++)
                {
                    Point3d p = new Point3d(x, y, z);
                    System.out.println(p + " -> " + transform.transform(p));
                }
            }
        }
    }

    /**
     * Demonstrate scale.
     */
    public static void scale()
    {
        System.out.println("Scaling");
        Transform2d transform = new Transform2d().scale(4, 6);
        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 2; y++)
            {
                Point2d p = new Point2d(x, y);
                System.out.println(p + " -> " + transform.transform(p));
            }
        }
    }

    /**
     * Demonstrate transforming of iterator of Point.
     */
    public static void transformPolyLine()
    {
        System.out.println("Transforming a multi-point Drawable");
        PolyLine2d line = new PolyLine2d(new Point2d(1, 2), new Point2d(2, 3), new Point2d(5, 0));
        System.out.println(line);
        Transform2d transform = new Transform2d().scale(2, 3);
        PolyLine2d transformedLine = new PolyLine2d(transform.transform(line.getPoints()));
        System.out.println(transformedLine);
    }
}
