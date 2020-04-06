package org.djutils.immutablecollections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test the enum Immutable.
 * <p>
 * Copyright (c) 2013-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Feb 26, 2019 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestImmutable
{
    /**
     * Test the methods of the enum Immutable.
     */
    @Test
    public void testImmutable()
    {
        assertTrue("isWrap", Immutable.WRAP.isWrap());
        assertTrue("isCopy", Immutable.COPY.isCopy());
        assertFalse("isWrap", Immutable.COPY.isWrap());
        assertFalse("isCopy", Immutable.WRAP.isCopy());
        // That was easy.
    }

}
