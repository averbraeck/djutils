package demo;

import org.djutils.draw.line.Ray2d;
import org.djutils.draw.point.Point2d;

/**
 * RayDemos.java.
 * <p>
 * Copyright (c) 2021-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class RayDemos
{
    /**
     * Do not instantiate.
     */
    private RayDemos()
    {
        // Do not instantiate.
    }

    /**
     * Demonstrate the Ray classes.
     * @param args String[]; the command line arguments (not used)
     */
    public static void main(final String[] args)
    {
        Ray2d r1 = new Ray2d(new Point2d(1, 2), new Point2d(1, 6));
        System.out.println(r1);
        Ray2d r2 = new Ray2d(1, 2, 1, 6);
        System.out.println(r2);
        Ray2d r3 = new Ray2d(new Point2d(1, 2), 1, 6);
        System.out.println(r3);
        Ray2d r4 = new Ray2d(1, 2, new Point2d(1, 6));
        System.out.println(r4);
        Ray2d r5 = new Ray2d(1, 2, Math.PI / 2);
        System.out.println(r5);
        Ray2d r6 = new Ray2d(new Point2d(1, 2), Math.PI / 2);
        System.out.println(r6);

    }
}
