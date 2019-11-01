package org.djutils.primitives;

import junit.framework.TestCase;

/**
 * The JUNIT Test for the <code>Primitive</code>.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class PrimitiveTest extends TestCase
{

    /**
     * constructs a new PrimitiveTest.
     */
    public PrimitiveTest()
    {
        this("test");
    }

    /**
     * constructs a new PrimitiveTest.
     * @param arg0 arg
     */
    public PrimitiveTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the Primitive
     */
    public void test()
    {
        TestCase.assertEquals(Primitive.getPrimitive(Boolean.class), boolean.class);
        TestCase.assertEquals(Primitive.getPrimitive(Integer.class), int.class);
        TestCase.assertEquals(Primitive.getPrimitive(Double.class), double.class);
        TestCase.assertEquals(Primitive.getPrimitive(Float.class), float.class);
        TestCase.assertEquals(Primitive.getPrimitive(Character.class), char.class);
        TestCase.assertEquals(Primitive.getPrimitive(Byte.class), byte.class);
        TestCase.assertEquals(Primitive.getPrimitive(Short.class), short.class);
        TestCase.assertEquals(Primitive.getPrimitive(Long.class), long.class);

        TestCase.assertEquals(Primitive.getWrapper(boolean.class), Boolean.class);
        TestCase.assertEquals(Primitive.getWrapper(int.class), Integer.class);
        TestCase.assertEquals(Primitive.getWrapper(double.class), Double.class);
        TestCase.assertEquals(Primitive.getWrapper(float.class), Float.class);
        TestCase.assertEquals(Primitive.getWrapper(char.class), Character.class);
        TestCase.assertEquals(Primitive.getWrapper(byte.class), Byte.class);
        TestCase.assertEquals(Primitive.getWrapper(long.class), Long.class);
        TestCase.assertEquals(Primitive.getWrapper(short.class), Short.class);
        try
        {
            Primitive.getWrapper(String[].class);
            fail("getWrapper for non primitive class should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }

        TestCase.assertEquals(Primitive.toBoolean(new Integer(1)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Double(1.0)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Float(1.0)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Short((short) 1.0)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Long(1l)), Boolean.TRUE);

        TestCase.assertEquals(Primitive.toBoolean(new Integer(0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Double(0.0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Float(0.0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Short((short) 0.0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Long(0l)), Boolean.FALSE);
        try
        {
            Primitive.toBoolean(new Integer(-1));
            fail("casting number not in {0, 1} to boolean should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            Primitive.toBoolean(new Integer(2));
            fail("casting number not in {0, 1} to boolean should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // Ignore expected exception
        }
        try
        {
            Primitive.toBoolean(new String[] { "ab", "cd" });
            fail("casting unrelated object to boolean should have thrown a ClassCastException");
        }
        catch (ClassCastException cce)
        {
            // Ignore expected exception
        }
        TestCase.assertEquals(Primitive.toBoolean(new Boolean(false)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Boolean(true)), Boolean.TRUE);

    }

}
