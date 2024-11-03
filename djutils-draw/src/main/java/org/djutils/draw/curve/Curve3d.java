package org.djutils.draw.curve;

import org.djutils.draw.Direction3d;
import org.djutils.draw.point.DirectedPoint3d;

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
interface Curve3d extends Curve<DirectedPoint3d, Direction3d>
{

    /**
     * Start direction of this ContinuousLine.
     * @return start direction of this ContinuousLine
     */
    @Override
    default Direction3d getStartDirection()
    {
        return getStartPoint().getDir();
    }

    /**
     * End direction of this ContinuousLine.
     * @return end direction of this ContinuousLine
     */
    @Override
    default Direction3d getEndDirection()
    {
        return getEndPoint().getDir();
    }

}
