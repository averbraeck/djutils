package org.djutils.draw.curve;

import org.djutils.draw.Direction3d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;

/**
 * This interface narrows down the interface of continuous curves for 3d use.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
interface Curve3d extends Curve<DirectedPoint3d, Direction3d, Point3d, Flattener3d, PolyLine3d>
{

    @Override
    default DirectedPoint3d getStartPoint()
    {
        return new DirectedPoint3d(getPoint(0.0), getDirection(0.0));
    }

    @Override
    default DirectedPoint3d getEndPoint()
    {
        return new DirectedPoint3d(getPoint(1.0), getDirection(1.0));
    }

    @Override
    default Direction3d getStartDirection()
    {
        return getStartPoint().getDir();
    }

    @Override
    default Direction3d getEndDirection()
    {
        return getEndPoint().getDir();
    }

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
