package org.djutils.draw;

import java.io.Serializable;

/**
 * Dimension interface to show the number of dimensions for the drawable objects.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Space extends Serializable
{
    /**
     * Return the number of dimensions.
     * @return int; the number of dimensions
     */
    int getDimensions();
}

