package org.djutils.eval;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.junit.jupiter.api.Test;

/**
 * TestFunction.java. Test the Function interface.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author Peter Knoppers</a>
 */
public class TestFunction
{
    /**
     * Test the function interface.
     */
    @Test
    public void functionTest()
    {
        assertEquals("id", this.f.getId(), "id of Function is returned");
        assertEquals("short", this.f.getMetaData().getName(), "name in MetaData is returned");
        assertEquals("long", this.f.getMetaData().getDescription(), "description in MetaData is returned");
        // Currently we can't verify the fields in the ObjectDescriptor due to those methods being non public
        assertEquals(true, this.f.function(new Object[] {4, 3}), "function executes (4>3");
        assertEquals(false, this.f.function(new Object[] {3, 3}), "function executes (4>4");
    }

    /**
     * Test Function.
     */
    // @formatter:off
    Function f = new F2("id", new MetaData("short", "long", new ObjectDescriptor("bool", "boolean", Boolean.class)), 
            (x, a, b) -> (int) a > (int) b);
    // @formatter:on

}
