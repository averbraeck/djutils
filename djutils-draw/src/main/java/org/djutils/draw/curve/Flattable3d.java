package org.djutils.draw.curve;

import org.djutils.draw.Direction3d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.Point3d;

/**
 * A Flattable3d has the required methods to allow it to be converted to a PolyLine3d using a Flattener3d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
interface Flattable3d extends Flattable<Flattener3d, PolyLine3d, Point3d, Direction3d>
{

    /**
     * Returns the point at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a B&eacute;zier
     * curve, <i>s</i> in a Clothoid, or simply the fraction of length.
     * @param fraction double; the fraction
     * @return Point3d; the point at the given fraction
     */
    @Override
    Point3d getPoint(double fraction);

    /**
     * Returns the direction at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a
     * B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length. The default implementation performs a
     * numerical approach by looking at the direction between the points at fraction, and a point 1e-6 away.
     * @param fraction double; the fraction
     * @return double; the direction at the given fraction
     */
    @Override
    default Direction3d getDirection(final double fraction)
    {
        Point3d p1, p2;
        if (fraction < 0.5) // to prevent going above 1.0
        {
            p1 = getPoint(fraction);
            p2 = getPoint(fraction + 1e-6);
        }
        else
        {
            p1 = getPoint(fraction - 1e-6);
            p2 = getPoint(fraction);
        }
        return p1.directionTo(p2);
    }

}
