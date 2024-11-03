package demo;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.Ray2d;
import org.djutils.draw.point.Point2d;

/**
 * BoundsDemos.java.
 * <p>
 * Copyright (c) 2021-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class BoundsDemos
{
    /**
     * Do not instantiate.
     */
    private BoundsDemos()
    {
        // Do not instantiate.
    }

    /**
     * Demonstrate the Bounds classes.
     * @param args String[]; the command line arguments; not used
     */
    public static void main(final String[] args)
    {
        Bounds2d b1 = new Bounds2d(3.4, 6.7, -5.6, 2.3); // Arguments are the absolute minimum and maximum values
        System.out.println("b1: " + b1);
        Bounds2d b2 = new Bounds2d(12.3, 23.4); // Arguments are ranges, symmetrically around 0.0
        System.out.println("b2: " + b2);
        PolyLine2d line = new PolyLine2d(new Point2d(1, 2), new Point2d(3, 4), new Point2d(-5, 12));
        Bounds2d b3 = new Bounds2d(line.getPoints()); // Argument is Iterator&lt;Point2d&gt;
        System.out.println("b3: " + b3);
        Bounds2d b4 = line.getBounds(); // Of course, the PolyLine2d can create a Bounds2d by itself
        System.out.println("b4: " + b4);
        Point2d[] pointArray = new Point2d[] {new Point2d(1, 2), new Point2d(3, 4), new Point2d(-5, 12)};
        Bounds2d b5 = new Bounds2d(pointArray);
        System.out.println("b5: " + b5);
        Collection<Drawable2d> drawableCollection = new LinkedHashSet<>();
        drawableCollection.add(new Point2d(1, 2));
        drawableCollection.add(new Point2d(3, 4));
        drawableCollection.add(new Point2d(-5, 12));
        Bounds2d b6 = new Bounds2d(drawableCollection);
        System.out.println("b6: " + b6);
        Bounds2d b7 = new Bounds2d(new Point2d(1, 2), new LineSegment2d(3, 4, -5, 12));
        System.out.println("b7: " + b7);

        Bounds2d bounds = new Bounds2d(new Ray2d(1, 2, Math.toRadians(45)));
        for (Iterator<Point2d> iterator = bounds.getPoints(); iterator.hasNext();)
        {
            System.out.println(iterator.next());
        }
    }
}
