package org.djutils.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.djutils.primitives.Primitive;

/**
 * ClassUtil is a utility class providing assistance for Java Classes.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author Peter Jacobs, Niels Lang, Alexander Verbraeck
 */
public final class ClassUtil
{
    /** CACHE reflects the internal repository CACHE. */
    private static final Map<String, Object> CACHE = Collections.synchronizedMap(new HashMap<String, Object>());

    /**
     * constructs a new ClassUtil.
     */
    private ClassUtil()
    {
        super();
        // unreachable code
    }

    /** ************ CONSTRUCTOR UTILITIES *********** */
    /**
     * gets all the constructors of a class and adds the result to result.
     * @param clazz Class&lt;?&gt;; the class
     * @param result Constructor&lt;?&gt;[]; the resulting set
     * @return result
     */
    public static Constructor<?>[] getAllConstructors(final Class<?> clazz, final Constructor<?>[] result)
    {
        List<Constructor<?>> list = new ArrayList<Constructor<?>>(Arrays.asList(result));
        list.addAll(Arrays.asList(clazz.getDeclaredConstructors()));
        if (clazz.getSuperclass() != null)
        {
            return ClassUtil.getAllConstructors(clazz.getSuperclass(), list.toArray(new Constructor[list.size()]));
        }
        return list.toArray(new Constructor[list.size()]);
    }

    /**
     * returns the interface method.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param callerClass Class&lt;?&gt;; the calling class
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Constructor
     * @throws NoSuchMethodException if the method cannot be resolved
     */
    public static Constructor<?> resolveConstructor(final Class<?> clazz, final Class<?> callerClass,
            final Class<?>[] parameterTypes) throws NoSuchMethodException
    {
        Constructor<?> constructor = ClassUtil.resolveConstructor(clazz, parameterTypes);
        if (ClassUtil.isVisible(constructor, callerClass.getClass()))
        {
            return constructor;
        }
        throw new NoSuchMethodException("constructor resolved but not visible");
    }

    /**
     * returns the interface method.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Constructor
     * @throws NoSuchMethodException if the method cannot be resolved
     */
    public static Constructor<?> resolveConstructor(final Class<?> clazz, final Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        try
        {
            return resolveConstructorSuper(clazz, (Class<?>[]) ClassUtil.checkInput(parameterTypes, Class.class));
        }
        catch (Exception exception)
        {
            String className = clazz.getName();
            if (className.indexOf("$") >= 0)
            {
                Class<?> parentClass = null;
                try
                {
                    parentClass = Class.forName(className.substring(0, className.lastIndexOf("$")));
                }
                catch (Exception e2)
                {
                    throw new NoSuchMethodException("class " + parentClass + " not found to resolve constructor");
                }
                return ClassUtil.resolveConstructor(parentClass,
                        (Class<?>[]) ClassUtil.checkInput(parameterTypes, Class.class));
            }
            throw new NoSuchMethodException("class " + clazz + " does not contain constructor");
        }
    }

    /**
     * returns the constructor.
     * @param clazz Class&lt;?&gt;; the clazz to start with
     * @param arguments Object[]; the arguments
     * @return Constructor
     * @throws NoSuchMethodException on lookup failure
     */
    public static Constructor<?> resolveConstructor(final Class<?> clazz, final Object[] arguments)
            throws NoSuchMethodException
    {
        Class<?>[] parameterTypes = ClassUtil.getClass(arguments);
        String key = "CONSTRUCTOR:" + clazz + "@" + FieldSignature.toDescriptor(parameterTypes);
        if (CACHE.containsKey(key))
        {
            return (Constructor<?>) CACHE.get(key);
        }
        try
        {
            return ClassUtil.resolveConstructor(clazz, parameterTypes);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            // We get all constructors
            Constructor<?>[] constructors = ClassUtil.getAllConstructors(clazz, new Constructor[0]);
            // now we match the signatures
            constructors = ClassUtil.matchSignature(constructors, parameterTypes);
            // Now we find the most specific
            Constructor<?> result = ClassUtil.getSpecificConstructor(constructors);
            CACHE.put(key, result);
            return result;
        }
    }

    /* ************ FIELD UTILITIES *********** */

    /**
     * gets all the fields of a class (public, protected, package, and private) and adds the result to the return value.
     * @param clazz Class&lt;?&gt;; the class
     * @param result Set&lt;Field&gt;; the resulting set
     * @return the set of fields including all fields of the field clazz
     */
    public static Set<Field> getAllFields(final Class<?> clazz, final Set<Field> result)
    {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            result.add(fields[i]);
        }
        if (clazz.getSuperclass() != null)
        {
            return ClassUtil.getAllFields(clazz.getSuperclass(), result);
        }
        return result;
    }

    /**
     * gets all the fields of a class (public, protected, package, and private).
     * @param clazz Class&lt;?&gt;; the class
     * @return all fields of the class
     */
    public static Set<Field> getAllFields(final Class<?> clazz)
    {
        Set<Field> fieldSet = new HashSet<Field>();
        return ClassUtil.getAllFields(clazz, fieldSet);
    }

    /**
     * resolves the field for a class, taking into account inner classes.
     * @param clazz the class to resolve the field for, including inner classes
     * @param fieldName name of the field
     * @return Field the field
     * @throws NoSuchFieldException on no such field
     */

    public static Field resolveField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException
    {
        try
        {
            return resolveFieldSuper(clazz, fieldName);
        }
        catch (NoSuchFieldException noSuchFieldException)
        {
            String className = clazz.getName();
            if (className.indexOf("$") >= 0)
            {
                Class<?> clazz2 = null;
                try
                {
                    clazz2 = Class.forName(className.substring(0, className.lastIndexOf("$")));
                }
                catch (ClassNotFoundException classNotFoundException)
                {
                    throw new NoSuchFieldException("class " + clazz + " not found to resolve field " + fieldName);
                }
                return ClassUtil.resolveField(clazz2, fieldName);
            }
            throw new NoSuchFieldException("class " + clazz + " does not contain field " + fieldName);
        }
    }

    /**
     * returns the field.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param callerClass Class&lt;?&gt;; the calling class
     * @param name String; the fieldName
     * @return Constructor
     * @throws NoSuchFieldException if the method cannot be resolved
     */
    public static Field resolveField(final Class<?> clazz, final Class<?> callerClass, final String name)
            throws NoSuchFieldException
    {
        Field field = ClassUtil.resolveField(clazz, name);
        if (ClassUtil.isVisible(field, callerClass.getClass()))
        {
            return field;
        }
        throw new NoSuchFieldException("field resolved but not visible");
    }

    /**
     * resolves the field for a given object instance.
     * @param object Object; the object to resolve the field for
     * @param fieldName String; name of the field to resolve
     * @return the field (if found)
     * @throws NoSuchFieldException if the field cannot be resolved
     */
    public static Field resolveField(final Object object, final String fieldName) throws NoSuchFieldException
    {
        if (object == null)
        {
            throw new NoSuchFieldException("resolveField: object is null for field " + fieldName);
        }
        return resolveField(object.getClass(), fieldName);
    }

    /** ************ METHOD UTILITIES *********** */
    /**
     * gets all the methods of a class and adds the result to result.
     * @param clazz Class&lt;?&gt;; the class
     * @param name String; the name of the method
     * @param result Method[]; the resulting set
     * @return result
     */
    public static Method[] getAllMethods(final Class<?> clazz, final String name, final Method[] result)
    {
        List<Method> list = new ArrayList<Method>(Arrays.asList(result));
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (methods[i].getName().equals(name))
            {
                list.add(methods[i]);
            }
        }
        if (clazz.getSuperclass() != null)
        {
            return ClassUtil.getAllMethods(clazz.getSuperclass(), name, list.toArray(new Method[list.size()]));
        }
        return list.toArray(new Method[list.size()]);
    }

    /**
     * returns the interface method.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param callerClass Class&lt;?&gt;; the caller class
     * @param name String; the name of the method
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Method
     * @throws NoSuchMethodException on lookup failure
     */
    public static Method resolveMethod(final Class<?> clazz, final Class<?> callerClass, final String name,
            final Class<?>[] parameterTypes) throws NoSuchMethodException
    {
        Method method = ClassUtil.resolveMethod(clazz, name, parameterTypes);
        if (ClassUtil.isVisible(method, callerClass))
        {
            return method;
        }
        throw new NoSuchMethodException("method found but not visible");
    }

    /**
     * returns the interface method.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param name String; the name of the method
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Method
     * @throws NoSuchMethodException on lookup failure
     */
    public static Method resolveMethod(final Class<?> clazz, final String name, final Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        try
        {
            return resolveMethodSuper(clazz, name, (Class<?>[]) ClassUtil.checkInput(parameterTypes, Class.class));
        }
        catch (Exception exception)
        {
            String className = clazz.getName();
            if (className.indexOf("$") >= 0)
            {
                Class<?> parentClass = null;
                try
                {
                    parentClass = Class.forName(className.substring(0, className.lastIndexOf("$")));
                }
                catch (Exception e2)
                {
                    throw new NoSuchMethodException("class " + parentClass + " not found to resolve method " + name);
                }
                return ClassUtil.resolveMethod(parentClass, name,
                        (Class<?>[]) ClassUtil.checkInput(parameterTypes, Class.class));
            }
            throw new NoSuchMethodException("class " + clazz + " does not contain method " + name);
        }
    }

    /**
     * resolves a method the method.
     * @param object Object; the object to start with
     * @param name String; the name of the method
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Method
     * @throws NoSuchMethodException on lookup failure
     */
    public static Method resolveMethod(final Object object, final String name, final Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        if (object == null)
        {
            throw new NoSuchMethodException("resolveField: object is null for method " + name);
        }
        return resolveMethod(object.getClass(), name, parameterTypes);
    }

    /**
     * returns the method.
     * @param object Object; the object to start with
     * @param name String; the name of the method
     * @param arguments Object[]; the arguments
     * @return Method
     * @throws NoSuchMethodException on lookup failure
     */
    public static Method resolveMethod(final Object object, final String name, final Object[] arguments)
            throws NoSuchMethodException
    {
        Class<?>[] parameterTypes = ClassUtil.getClass(arguments);
        String key = "METHOD:" + object.getClass() + "@" + name + "@" + FieldSignature.toDescriptor(parameterTypes);
        if (CACHE.containsKey(key))
        {
            return (Method) CACHE.get(key);
        }
        try
        {
            return ClassUtil.resolveMethod(object, name, parameterTypes);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            // We get all methods
            Method[] methods = ClassUtil.getAllMethods(object.getClass(), name, new Method[0]);
            if (methods.length == 0)
            {
                throw new NoSuchMethodException("No such method: " + name + " for object " + object);
            }
            // now we match the signatures
            methods = ClassUtil.matchSignature(methods, name, parameterTypes);
            if (methods.length == 0)
            {
                throw new NoSuchMethodException("No method with right signature: " + name + " for object " + object);
            }
            // Now we find the most specific
            Method result = ClassUtil.getSpecificMethod(methods);
            CACHE.put(key, result);
            return result;
        }
    }

    /**
     * Returns whether a declaringClass is accessible according to the modifiers.
     * @param modifiers int; the modifiers
     * @param declaringClass Class&lt;?&gt;; the declaringClass
     * @param caller Class&lt;?&gt;; the caller
     * @return boolean isVisible
     */
    public static boolean isVisible(final int modifiers, final Class<?> declaringClass, final Class<?> caller)
    {
        if (Modifier.isPublic(modifiers))
        {
            return true;
        }
        if (Modifier.isProtected(modifiers))
        {
            if (declaringClass.isAssignableFrom(caller))
            {
                return true;
            }
            if (declaringClass.getPackage().equals(caller.getPackage()))
            {
                return true;
            }
            return false;
        }
        if (declaringClass.equals(caller))
        {
            return true;
        }
        return false;
    }

    /**
     * Determines &amp; returns whether constructor 'a' is more specific than constructor 'b', as defined in the Java
     * Language Specification ???15.12.
     * @return true if 'a' is more specific than b, false otherwise. 'false' is also returned when constructors are
     *         incompatible, e.g. have different names or a different number of parameters.
     * @param a Class&lt;?&gt;[]; reflects the first constructor
     * @param b Class&lt;?&gt;[]; reflects the second constructor
     */
    public static boolean isMoreSpecific(final Class<?>[] a, final Class<?>[] b)
    {
        if (a.length != b.length)
        {
            return false;
        }
        int i = 0;
        while (i < a.length)
        {
            if (!b[i].isAssignableFrom(a[i]))
            {
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Determines &amp; returns whether constructor 'a' is more specific than constructor 'b', as defined in the Java
     * Language Specification ???15.12.
     * @return true if 'a' is more specific than b, false otherwise. 'false' is also returned when constructors are
     *         incompatible, e.g. have different names or a different number of parameters.
     * @param a Constructor&lt;?&gt;; reflects the first constructor
     * @param b Constructor&lt;?&gt;; reflects the second constructor
     */
    public static boolean isMoreSpecific(final Constructor<?> a, final Constructor<?> b)
    {
        if (a.getParameterTypes().equals(b.getParameterTypes()))
        {
            if (b.getDeclaringClass().isAssignableFrom(a.getDeclaringClass()))
            {
                return true;
            }
        }
        return ClassUtil.isMoreSpecific(a.getParameterTypes(), b.getParameterTypes());
    }

    /**
     * Determines &amp; returns whether constructor 'a' is more specific than constructor 'b', as defined in the Java
     * Language Specification ???15.12.
     * @return true if 'a' is more specific than b, false otherwise. 'false' is also returned when constructors are
     *         incompatible, e.g. have different names or a different number of parameters.
     * @param a Method; reflects the first method
     * @param b Method; reflects the second method
     */
    public static boolean isMoreSpecific(final Method a, final Method b)
    {
        if (!a.getName().equals(b.getName()))
        {
            return false;
        }
        return ClassUtil.isMoreSpecific(a.getParameterTypes(), b.getParameterTypes());
    }

    /**
     * Returns whether a field is visible for a caller.
     * @param field Field; The field
     * @param caller Class&lt;?&gt;; The class of the caller for whom invocation visibility is checked.
     * @return boolean yes or no
     */
    public static boolean isVisible(final Field field, final Class<?> caller)
    {
        return ClassUtil.isVisible(field.getModifiers(), field.getDeclaringClass(), caller);
    }

    /**
     * Returns whether a constructor is visible for a caller.
     * @param constructor Constructor&lt;?&gt;; The constructor
     * @param caller Class&lt;?&gt;; The class of the caller for whom invocation visibility is checked.
     * @return boolean yes or no
     */
    public static boolean isVisible(final Constructor<?> constructor, final Class<?> caller)
    {
        return ClassUtil.isVisible(constructor.getModifiers(), constructor.getDeclaringClass(), caller);
    }

    /**
     * Returns whether a method is visible for a caller.
     * @param method Method; The method
     * @param caller Class&lt;?&gt;; The class of the caller for whom invocation visibility is checked.
     * @return boolean yes or no
     */
    public static boolean isVisible(final Method method, final Class<?> caller)
    {
        return ClassUtil.isVisible(method.getModifiers(), method.getDeclaringClass(), caller);
    }

    /**
     * Filters an array methods for signatures that are compatible with a given signature.
     * @param methods Method[]; which are methods to be filtered.
     * @param name String; reflects the method's name, part of the signature
     * @param argTypes Class&lt;?&gt;[]; are the method's argument types
     * @return Method[] An unordered Method-array consisting of the elements of 'methods' that match with the given
     *         signature. An array with 0 elements is returned when no matching Method objects are found.
     */
    public static Method[] matchSignature(final Method[] methods, final String name, final Class<?>[] argTypes)
    {
        List<Method> results = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++)
        {
            if (ClassUtil.matchSignature(methods[i], name, argTypes))
            {
                results.add(methods[i]);
            }
        }
        return results.toArray(new Method[results.size()]);
    }

    /**
     * Filters an array methods for signatures that are compatible with a given signature.
     * @param method Method; The method to be filtered.
     * @param name String; reflects the method's name, part of the signature
     * @param argTypes Class&lt;?&gt;[]; are the method's argument types
     * @return boolean if methodParameters assignable from argTypes
     */
    public static boolean matchSignature(final Method method, final String name, final Class<?>[] argTypes)
    {
        if (!method.getName().equals(name))
        {
            return false;
        }
        if (method.getParameterTypes().length != argTypes.length)
        {
            return false;
        }
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < method.getParameterTypes().length; i++)
        {
            if (!(types[i].isAssignableFrom(argTypes[i]) || types[i].equals(Primitive.getPrimitive(argTypes[i]))))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Filters an array methods for signatures that are compatible with a given signature.
     * @param constructor Constructor&lt;?&gt;; which are constructors to be filtered.
     * @param argTypes Class&lt;?&gt;[]; are the constructor's argument types
     * @return boolean if methodParameters assignable from argTypes
     */
    public static boolean matchSignature(final Constructor<?> constructor, final Class<?>[] argTypes)
    {
        if (constructor.getParameterTypes().length != argTypes.length)
        {
            return false;
        }
        Class<?>[] types = constructor.getParameterTypes();
        for (int i = 0; i < constructor.getParameterTypes().length; i++)
        {
            if (!(types[i].isAssignableFrom(argTypes[i]) || types[i].equals(Primitive.getPrimitive(argTypes[i]))))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Filters an array methods for signatures that are compatible with a given signature.
     * @param constructors Constructor&lt;?&gt;[]; which are constructors to be filtered.
     * @param argTypes Class&lt;?&gt;[]; are the constructor's argument types
     * @return Constructor&lt;?&gt;[] An unordered Constructor-array consisting of the elements of 'constructors' that
     *         match with the given signature. An array with 0 elements is returned when no matching Method objects are
     *         found.
     */
    public static Constructor<?>[] matchSignature(final Constructor<?>[] constructors, final Class<?>[] argTypes)
    {
        List<Constructor<?>> results = new ArrayList<Constructor<?>>();
        for (int i = 0; i < constructors.length; i++)
        {
            if (ClassUtil.matchSignature(constructors[i], argTypes))
            {
                results.add(constructors[i]);
            }
        }
        return results.toArray(new Constructor[results.size()]);
    }

    /**
     * converts an array of objects to their corresponding classes.
     * @param array Object[]; the array to invoke
     * @return Class&lt;?&gt;[] the result;
     */
    public static Class<?>[] getClass(final Object[] array)
    {
        if (array == null)
        {
            return new Class[0];
        }
        Class<?>[] result = new Class[array.length];
        for (int i = 0; i < result.length; i++)
        {
            if (array[i] == null)
            {
                result[i] = null;
            }
            else
            {
                result[i] = array[i].getClass();
            }
        }
        return result;
    }

    /** ************** PRIVATE METHODS ********* */

    /**
     * checks the input of an array.
     * @param array Object[]; the array
     * @param myClass Class&lt;?&gt;; the class of the result
     * @return Returns array if array!=null else returns myClass[0]
     */
    private static Object checkInput(final Object[] array, final Class<?> myClass)
    {
        if (array != null)
        {
            return array;
        }
        return Array.newInstance(myClass, 0);
    }

    /**
     * Determines & returns the most specific constructor as defined in the Java Language Specification par 15.12. The
     * current algorithm is simple and reliable, but probably slow.
     * @param methods Constructor&lt;?&gt;[]; are the constructors to be searched. They are assumed to have the same
     *            name and number of parameters, as determined by the constructor matchSignature.
     * @return Constructor which is the most specific constructor.
     * @throws NoSuchMethodException when no constructor is found that's more specific than the others.
     */
    private static Constructor<?> getSpecificConstructor(final Constructor<?>[] methods) throws NoSuchMethodException
    {
        if (methods.length == 0)
        {
            throw new NoSuchMethodException();
        }
        if (methods.length == 1)
        {
            return methods[0];
        }
        // Apply generic algorithm
        int resultID = 0; // Assume first method to be most specific
        while (resultID < methods.length)
        {
            // Verify assumption
            boolean success = true;
            for (int i = 0; i < methods.length; i++)
            {
                if (resultID == i)
                {
                    continue;
                }
                if (!isMoreSpecific(methods[resultID], methods[i]))
                {
                    success = false;
                }
            }
            // Assumption verified
            if (success)
            {
                return methods[resultID];
            }
            resultID++;
        }
        // No method is most specific, thus:
        throw new NoSuchMethodException();
    }

    /**
     * Determines & returns the most specific method as defined in the Java Language Specification par 15.12. The
     * current algorithm is simple and reliable, but probably slow.
     * @param methods Method[]; which are the methods to be searched. They are assumed to have the same name and number
     *            of parameters, as determined by the method matchSignature.
     * @return The most specific method.
     * @throws NoSuchMethodException when no method is found that's more specific than the others.
     */
    private static Method getSpecificMethod(final Method[] methods) throws NoSuchMethodException
    {
        // Check for evident cases
        if (methods.length == 0)
        {
            throw new NoSuchMethodException();
        }
        if (methods.length == 1)
        {
            return methods[0];
        }
        // Apply generic algorithm
        int resultID = 0; // Assume first method to be most specific
        while (resultID < methods.length)
        {
            // Verify assumption
            boolean success = true;
            for (int i = 0; i < methods.length; i++)
            {
                if (resultID == i)
                {
                    continue;
                }
                if (!isMoreSpecific(methods[resultID], methods[i]))
                {
                    success = false;
                }
            }
            // Assumption verified
            if (success)
            {
                return methods[resultID];
            }
            resultID++;
        }
        // No method is most specific, thus:
        throw new NoSuchMethodException();
    }

    /**
     * returns the constructor.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Method
     * @throws NoSuchMethodException if the method cannot be resolved
     */
    private static Constructor<?> resolveConstructorSuper(final Class<?> clazz, final Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        String key = "CONSTRUCTOR:" + clazz + "@" + FieldSignature.toDescriptor(parameterTypes);
        try
        {
            if (CACHE.containsKey(key))
            {
                return (Constructor<?>) CACHE.get(key);
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            CACHE.put(key, constructor);
            return constructor;
        }
        catch (Exception exception)
        {
            if (clazz.getSuperclass() != null)
            {
                Constructor<?> constructor = ClassUtil.resolveConstructorSuper(clazz.getSuperclass(), parameterTypes);
                CACHE.put(key, constructor);
                return constructor;
            }
            throw new NoSuchMethodException(exception.getMessage());
        }
    }

    /**
     * returns the interface method.
     * @param clazz Class&lt;?&gt;; the class to start with
     * @param name String; the name of the method
     * @param parameterTypes Class&lt;?&gt;[]; the parameterTypes
     * @return Method
     * @throws NoSuchMethodException on lookup failure
     */
    private static Method resolveMethodSuper(final Class<?> clazz, final String name, final Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        String key = "METHOD:" + clazz + "@" + name + "@" + FieldSignature.toDescriptor(parameterTypes);
        try
        {
            if (CACHE.containsKey(key))
            {
                return (Method) CACHE.get(key);
            }
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            CACHE.put(key, method);
            return method;
        }
        catch (Exception exception)
        {
            if (clazz.getSuperclass() != null)
            {
                Method method = ClassUtil.resolveMethodSuper(clazz.getSuperclass(), name, parameterTypes);
                CACHE.put(key, method);
                return method;
            }
            throw new NoSuchMethodException(exception.getMessage());
        }
    }

    /**
     * resolves the field for a class, taking into account superclasses.
     * @param clazz Class&lt;?&gt;; the class for which superclasses will be probed
     * @param fieldName String; the name of the field to resolve
     * @return the field (if found)
     * @throws NoSuchFieldException if the field cannot be resolved
     */
    private static Field resolveFieldSuper(final Class<?> clazz, final String fieldName) throws NoSuchFieldException
    {
        String key = "FIELD:" + clazz + "@" + fieldName;
        try
        {
            if (CACHE.containsKey(key))
            {
                return (Field) CACHE.get(key);
            }
            Field result = clazz.getDeclaredField(fieldName);
            CACHE.put(key, result);
            return result;
        }
        catch (Exception exception)
        {
            if (clazz.getSuperclass() != null)
            {
                Field result = ClassUtil.resolveFieldSuper(clazz.getSuperclass(), fieldName);
                CACHE.put(key, result);
                return result;
            }
            throw new NoSuchFieldException(exception.getMessage());
        }
    }
}
