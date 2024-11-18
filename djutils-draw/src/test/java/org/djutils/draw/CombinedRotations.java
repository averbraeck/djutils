package org.djutils.draw;

import org.djutils.draw.point.Point3d;

/**
 * CombinedRotations.java.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class CombinedRotations
{
    /**
     * Do not instantiate.
     */
    private CombinedRotations()
    {
        // Cannot be instantiated
    }

    /**
     * Demonstrate all combinations of 90 degree rotations.
     * @param args the command line arguments (not used)
     */
    public static void main(final String[] args)
    {
        Point3d unitVector = new Point3d(1, 0, 0);
        double[] rotations = new double[] {0, Math.PI / 2, Math.PI, -Math.PI / 2};
        for (double rotX : rotations)
        {
            for (double rotY : rotations)
            {
                for (double rotZ : rotations)
                {
                    Transform3d combinedTransform = new Transform3d().rotZ(rotZ).rotY(rotY).rotX(rotX);
                    System.out.println(String.format(
                            "rotX=%4.0f, rotY=%4.0f, rotZ=%4.0f, transformed unit vector %s, transformation Matrix %s",
                            Math.toDegrees(rotX), Math.toDegrees(rotY), Math.toDegrees(rotZ),
                            combinedTransform.transform(unitVector).toString("%4.0f"), combinedTransform));
                }
            }
        }
    }
}
