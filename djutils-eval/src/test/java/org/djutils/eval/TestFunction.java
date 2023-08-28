package org.djutils.eval;

import static org.junit.Assert.assertEquals;

import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.junit.Test;

/**
 * TestFunction.java. Test the Function interface.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
        assertEquals("id of Function is returned", "id", this.f.getId());
        assertEquals("name in MetaData is returned", "short", this.f.getMetaData().getName());
        assertEquals("description in MetaData is returned", "long", this.f.getMetaData().getDescription());
        // Currently we can't verify the fields in the ObjectDescriptor due to those methods being non public
        assertEquals("function executes (4>3", true, this.f.function(new Object[] {4, 3}));
        assertEquals("function executes (4>4", false, this.f.function(new Object[] {3, 3}));
    }

    /**
     * Test Function.
     */
    // @formatter:off
    Function f = new F2("id", new MetaData("short", "long", new ObjectDescriptor("bool", "boolean", Boolean.class)), 
            (x, a, b) -> (int) a > (int) b);
    // @formatter:on

}
