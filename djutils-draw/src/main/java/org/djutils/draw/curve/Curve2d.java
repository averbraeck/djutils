package org.djutils.draw.curve;

import org.djutils.draw.curve.Flattener.FlattableCurve;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;

/**
 * This interface narrows down the interface of continuous curves for 2d use.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
interface Curve2d extends Curve<DirectedPoint2d, Double, Point2d, Flattener2d, PolyLine2d>, FlattableCurve<Point2d, Double>
{

    @Override
    default DirectedPoint2d getStartPoint()
    {
        return new DirectedPoint2d(getPoint(0.0), getDirection(0.0));
    }

    @Override
    default DirectedPoint2d getEndPoint()
    {
        return new DirectedPoint2d(getPoint(1.0), getDirection(1.0));
    }

    @Override
    default Double getStartDirection()
    {
        return getStartPoint().dirZ;
    }

    @Override
    default Double getEndDirection()
    {
        return getEndPoint().dirZ;
    }

    @Override
    default Double getDirection(final double fraction)
    {
        Point2d p1, p2;
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
