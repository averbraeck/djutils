package org.djutils.draw;

/**
 * Space3d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Space3d  implements Space
{
    /** */
    private static final long serialVersionUID = 20201208L;

    /** {@inheritDoc} */
    @Override
    public int getDimensions()
    {
        return 3;
    }
    
}
