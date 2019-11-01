package org.djutils.reflection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.djutils.reflection.TestClass.InnerPublic;
import org.junit.Test;

/**
 * The JUNIT Test for <code>ClassUtil</code>.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class ClassUtilTest
{
    /** */
    @Test
    public void testClassUtilClass()
    {
        assertEquals(0, ClassUtil.getClass(null).length);
        assertEquals(String.class, ClassUtil.getClass(new Object[] {"Peter"})[0]);
        // Note that primitive types are always autoboxed to the corresponding object types.
        assertArrayEquals(new Class<?>[] {String.class, Double.class, Integer.class, Integer.class},
                ClassUtil.getClass(new Object[] {"X", 1.0d, 5, new Integer(5)}));
        assertArrayEquals(new Class<?>[] {String.class, Double.class, null, Integer.class},
                ClassUtil.getClass(new Object[] {"X", 1.0d, null, 5}));
    }
    
    /**
     * Test the toDescriptor method.
     */
    @Test
    public void testToDescriptor()
    {
        assertEquals("I", FieldSignature.toDescriptor(int.class));
        assertEquals("D", FieldSignature.toDescriptor(double.class));
        assertEquals("Z", FieldSignature.toDescriptor(boolean.class));
        assertEquals("C", FieldSignature.toDescriptor(char.class));
        assertEquals("B", FieldSignature.toDescriptor(byte.class));
        assertEquals("F", FieldSignature.toDescriptor(float.class));
        assertEquals("J", FieldSignature.toDescriptor(long.class));
        assertEquals("S", FieldSignature.toDescriptor(short.class));
        assertEquals("[I", FieldSignature.toDescriptor(int[].class));
        // System.out.println(FieldSignature.toDescriptor(int[].class));
        assertEquals("Ljava/lang/Integer;", FieldSignature.toDescriptor(Integer.class));
    }
    
    /**
     * Test the toClass method.
     * @throws ClassNotFoundException if that happens uncaught; this test has failed
     */
    @Test
    public void testToClass() throws ClassNotFoundException
    {
        assertEquals(int.class, FieldSignature.toClass("I"));
        assertEquals(double.class, FieldSignature.toClass("D"));
        assertEquals(boolean.class, FieldSignature.toClass("Z"));
        assertEquals(char.class, FieldSignature.toClass("C"));
        assertEquals(byte.class, FieldSignature.toClass("B"));
        assertEquals(float.class, FieldSignature.toClass("F"));
        assertEquals(long.class, FieldSignature.toClass("J"));
        assertEquals(short.class, FieldSignature.toClass("S"));
        assertEquals(int[].class, FieldSignature.toClass("[I"));
        assertEquals(int[].class, FieldSignature.toClass("[I")); // repeated call uses the cache
        assertEquals(Integer.class, FieldSignature.toClass("Ljava/lang/Integer;"));
    }
    
    /**
     * Test the constructors and fields of the FieldSignature class.
     * @throws ClassNotFoundException if that happens uncaught; this test has failed
     */
    @Test
    public void testFieldSignature() throws ClassNotFoundException
    {
        FieldSignature fs = new FieldSignature("[J");
        assertEquals("[J", fs.toString());
        assertEquals("[J", fs.getStringValue());
        assertEquals(long[].class, fs.getClassValue());
        FieldSignature fs2 = new FieldSignature(double[].class);
        assertEquals("[D", fs2.toString());
        assertEquals("[D", fs2.getStringValue());
        assertEquals(double[].class, fs2.getClassValue());
    }

    /**
     * Tests the ClassUtil Constructors
     * @throws NoSuchMethodException on error
     * @throws InvocationTargetException on error
     * @throws IllegalArgumentException on error
     * @throws IllegalAccessException on error
     * @throws InstantiationException on error
     */
    @Test
    public void testClassUtilConstructors() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        Constructor<TestClass> c1 = ClassUtil.resolveConstructor(TestClass.class, new Class<?>[] {});
        TestClass o1 = c1.newInstance();
        assertEquals("<init>", o1.getState());

        Constructor<TestClass> c2 = ClassUtil.resolveConstructor(TestClass.class, new Class<?>[] {String.class});
        TestClass o2 = c2.newInstance("c2");
        assertEquals("c2", o2.getState());

        Constructor<InnerPublic> c3 = ClassUtil.resolveConstructor(InnerPublic.class, new Class<?>[] {});
        InnerPublic o3 = c3.newInstance();
        assertEquals("<initInnerPublic>", o3.getInnerState());

        Constructor<InnerPublic> c4 = ClassUtil.resolveConstructor(InnerPublic.class, new Class<?>[] {String.class});
        InnerPublic o4 = c4.newInstance("inner");
        assertEquals("inner", o4.getInnerState());

        // test caching
        Constructor<InnerPublic> c4a = ClassUtil.resolveConstructor(InnerPublic.class, new Class<?>[] {String.class});
        InnerPublic o4a = c4a.newInstance("inner2");
        assertEquals("inner2", o4a.getInnerState());

        // test constructor that cannot be found
        try
        {
            ClassUtil.resolveConstructor(TestClass.class, new Class<?>[] {Integer.class});
            fail("Constructor TestClass(int) does not exist and resolving should throw an exception");
        }
        catch (NoSuchMethodException e)
        {
            // ok
        }

        // test access to public and private constructors
        ClassUtil.resolveConstructor(TestClass.class, new Class<?>[] {boolean.class});
        ClassUtil.resolveConstructor(TestClass.class, ClassUtilTest.class, new Class<?>[] {String.class});
        try
        {
            ClassUtil.resolveConstructor(TestClass.class, ClassUtilTest.class, new Class<?>[] {boolean.class});
            fail("Constructor TestClass(boolean) is private and resolving should throw an exception");
        }
        catch (IllegalAccessException e)
        {
            // ok
        }

        assertEquals(3, ClassUtil.getAllConstructors(TestClass.class).length);
    }
}
