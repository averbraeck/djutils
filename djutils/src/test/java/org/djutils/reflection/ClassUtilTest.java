package org.djutils.reflection;

import junit.framework.TestCase;

/**
 * The JUNIT Test for the <code>ClassUtilTest</code>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class ClassUtilTest extends TestCase
{

    /**
     * constructs a new ClassUtilTest.
     */
    public ClassUtilTest()
    {
        this("test");
    }

    /**
     * constructs a new ClassUtilTest.
     * @param arg0
     */
    public ClassUtilTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the ClassUtil
     */
    public void test()
    {
        // the getClass method
        TestCase.assertEquals(ClassUtil.getClass(null).length, 0);
        TestCase.assertEquals(ClassUtil.getClass(new Object[]{"Peter"})[0], String.class);
    }
}
