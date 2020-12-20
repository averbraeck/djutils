package org.djutils.draw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * SpaceTest.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class SpaceTest
{
    /**
     * Test the Space2d and Space3d classes.
     */
    @Test
    public void testSpace()
    {
        assertEquals("Space2d has 2 dimensions", 2, new Space2d().getDimensions());
        assertEquals("Space3d has 3 dimensions", 3, new Space3d().getDimensions());
    }
}
