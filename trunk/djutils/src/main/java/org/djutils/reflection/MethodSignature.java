package org.djutils.reflection;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A method descriptor represents the parameters that the method takes and the value that it returns. It is a series of
 * characters generated by the grammar described at
 * <a href = "https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3"> The Java Virtual Machine
 * Specification: Method Descriptors </a>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author Peter Jacobs, Niels Lang, Alexander Verbraeck
 */
public class MethodSignature implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the value of the methodDescriptor. */
    private String value = null;

    /**
     * constructs a new MethodSignature.
     * @param value String; the descriptor
     */
    public MethodSignature(final String value)
    {
        super();
        this.value = value;
    }

    /**
     * constructs a new MethodSignature.
     * @param method Method; the method
     */
    public MethodSignature(final Method method)
    {
        super();
        Class<?>[] parameterTypes = new Class<?>[0];
        if (method.getParameterTypes() != null)
        {
            parameterTypes = method.getParameterTypes();
        }
        this.value = "(";
        for (int i = 0; i < parameterTypes.length; i++)
        {
            this.value = this.value + FieldSignature.toDescriptor(parameterTypes[i]);
        }
        this.value = this.value + ")" + FieldSignature.toDescriptor(method.getReturnType());
    }

    /**
     * constructs a new MethodSignature.
     * @param constructor Constructor&lt;?&gt;; the constructor
     */
    public MethodSignature(final Constructor<?> constructor)
    {
        super();
        Class<?>[] parameterTypes = new Class<?>[0];
        if (constructor.getParameterTypes() != null)
        {
            parameterTypes = constructor.getParameterTypes();
        }

        this.value = "(";
        for (int i = 0; i < parameterTypes.length; i++)
        {
            this.value = this.value + FieldSignature.toDescriptor(parameterTypes[i]);
        }
        this.value = this.value + ")" + FieldSignature.toDescriptor(constructor.getDeclaringClass());
    }

    /**
     * @return Returns the parameterDescriptor
     */
    public String getParameterDescriptor()
    {
        return MethodSignature.getParameterDescriptor(this.value);
    }

    /**
     * returns the parameterTypes
     * @return ClassDescriptor[] the result
     * @throws ClassNotFoundException on incomplete classPath
     */
    public Class<?>[] getParameterTypes() throws ClassNotFoundException
    {
        return MethodSignature.getParameterTypes(this.value);
    }

    /**
     * @return Returns the returnDescriptor
     */
    public String getReturnDescriptor()
    {
        return MethodSignature.getReturnDescriptor(this.value);
    }

    /**
     * returns the returnType of this methodDescriptor
     * @return Returns the returnType
     * @throws ClassNotFoundException on incomplete classPath
     */
    public Class<?> getReturnType() throws ClassNotFoundException
    {
        return MethodSignature.getReturnType(this.value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.value;
    }

    /**
     * @return Returns the parameterDescriptor
     * @param methodDescriptor String; the methodDescriptor
     */
    public static String getParameterDescriptor(final String methodDescriptor)
    {
        return methodDescriptor.substring(1, methodDescriptor.indexOf(')'));
    }

    /**
     * returns the parameterTypes
     * @param methodDescriptor String; the string
     * @return ClassDescriptor[] the result
     * @throws ClassNotFoundException on incomplete classPath
     */
    public static Class<?>[] getParameterTypes(final String methodDescriptor) throws ClassNotFoundException
    {
        String parameterDescriptor = MethodSignature.getParameterDescriptor(methodDescriptor);
        List<Class<?>> result = new ArrayList<Class<?>>();
        int length = 0;
        while (length < parameterDescriptor.length())
        {
            String array = "";
            while (parameterDescriptor.charAt(length) == '[')
            {
                array = array + "[";
                length++;
            }
            if (parameterDescriptor.charAt(length) == 'L')
            {
                String argument = parameterDescriptor.substring(length);
                argument = array + argument.substring(0, argument.indexOf(';') + 1);
                result.add(FieldSignature.toClass(argument));
                length = length + argument.length() - array.length();
            }
            else
            {
                result.add(FieldSignature.toClass(array + parameterDescriptor.charAt(length)));
                length++;
            }
        }
        return result.toArray(new Class<?>[result.size()]);
    }

    /**
     * @return Returns the returnDescriptor
     * @param methodDescriptor String; the methodDescriptor
     */
    public static String getReturnDescriptor(final String methodDescriptor)
    {
        return methodDescriptor.substring(methodDescriptor.indexOf(')') + 1);
    }

    /**
     * returns the returnType of this methodDescriptor
     * @param methodDescriptor String; the returnDescriptor
     * @return Returns the returnType
     * @throws ClassNotFoundException on incomplete classPath
     */
    public static Class<?> getReturnType(final String methodDescriptor) throws ClassNotFoundException
    {
        return FieldSignature.toClass(MethodSignature.getReturnDescriptor(methodDescriptor));
    }
}
