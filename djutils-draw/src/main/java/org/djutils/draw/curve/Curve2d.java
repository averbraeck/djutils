package org.djutils.draw.curve;

import org.djutils.draw.point.DirectedPoint2d;

/**
 * This interface narrows down the interface of continuous curves for 2d use.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
interface Curve2d extends Curve<DirectedPoint2d, Double>
{

    /**
     * Start curvature of this Curve2d..
     * @return start curvature of this Curve2d.
     */
    double getStartCurvature();

    /**
     * End curvature of this Curve2d..
     * @return end curvature of this Curve2d
     */
    double getEndCurvature();

    /**
     * Start radius of this Curve2d..
     * @return start radius of this Curve2d
     */
    default double getStartRadius()
    {
        return 1.0 / getStartCurvature();
    }

    /**
     * End radius of this Curve2d..
     * @return end radius of this Curve2d
     */
    default double getEndRadius()
    {
        return 1.0 / getEndCurvature();
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

}
