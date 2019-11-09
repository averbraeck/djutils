package org.djutils.exceptions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;

import org.djutils.reflection.ClassUtil;

/**
 * The Try class has a number of static methods that make it easy to try-catch an exception for any Throwable class, including
 * the standard Java exceptions and exceptions from libraries that are used in the project. Instead of:
 * 
 * <pre>
 * FileInputStream fis;
 * try
 * {
 *     fis = new FileInputStream(fileString);
 * }
 * catch (FileNotFoundException exception)
 * {
 *     throw new IllegalArgumentException("File " + fileString + " is not a valid file.", exception);
 * }
 * try
 * {
 *     fis.close();
 * }
 * catch (IOException exception)
 * {
 *     throw new RuntimeException("Could not close the file.", exception);
 * }
 * </pre>
 * 
 * we can write:
 * 
 * <pre>
 * FileInputStream fis = Try.assign(() -&gt; new FileInputStream(fileString), IllegalArgumentException.class,
 *         "File %s is not a valid file.", fileString);
 * Try.execute(() -&gt; fis.close(), "Could not close the file.");
 * </pre>
 * 
 * The exception message can be formatted with additional arguments, such that the overhead of building the exception message
 * only occurs if the exception condition is met. For each method there is a version without Throwable class, in which case a
 * RuntimeException will be thrown.<br>
 * <br>
 * Try is not suitable for try-with-resource statements.<br>
 * <br>
 * Try also has a few methods to aid JUNIT tests: {@code testFail(...)} and {@code testNotFail(...)}.
 * <p>
 * Copyright (c) 2016-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public final class Try
{
    /** private constructor for utility class. */
    private Try()
    {
        // utility class
    }

    // Assign

    /**
     * Tries to return a value to assign. Will throw a RuntimeException if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; the message to use in the throwable
     * @param <V> value type
     * @return V; value to assign
     * @throws RuntimeException on failed Try
     */
    public static <V> V assign(final Assignment<V> assignment, final String message) throws RuntimeException
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            throw catchThrowable(RuntimeException.class, message, new ArrayList<>(), cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a RuntimeException if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; the message to use in the throwable, with formatting identifier
     * @param arg Object; value to use for the formatting identifier
     * @param <V> value type
     * @return V; value to assign
     * @throws RuntimeException on failed Try
     */
    public static <V> V assign(final Assignment<V> assignment, final String message, final Object arg) throws RuntimeException
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg);
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a RuntimeException if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param <V> value type
     * @return V; value to assign
     * @throws RuntimeException on failed Try
     */
    public static <V> V assign(final Assignment<V> assignment, final String message, final Object arg1, final Object arg2)
            throws RuntimeException
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a RuntimeException if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param <V> value type
     * @return V; value to assign
     * @throws RuntimeException on failed Try
     */
    public static <V> V assign(final Assignment<V> assignment, final String message, final Object arg1, final Object arg2,
            final Object arg3) throws RuntimeException
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a RuntimeException if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param args Object...; potential 4th and further values to use for the formatting identifiers
     * @param <V> value type
     * @return V; value to assign
     * @throws RuntimeException on failed Try
     */
    public static <V> V assign(final Assignment<V> assignment, final String message, final Object arg1, final Object arg2,
            final Object arg3, final Object... args) throws RuntimeException
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            argList.addAll(Arrays.asList(args));
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a specified Throwable if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     * @throws T throwable on failed Try
     */
    public static <V, T extends Throwable> V assign(final Assignment<V> assignment, final Class<T> throwableClass,
            final String message) throws T
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            throw catchThrowable(throwableClass, message, new ArrayList<>(), cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a specified Throwable if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifier
     * @param arg Object; value to use for the formatting identifier
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     * @throws T throwable on failed Try
     */
    public static <V, T extends Throwable> V assign(final Assignment<V> assignment, final Class<T> throwableClass,
            final String message, final Object arg) throws T
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg);
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a specified Throwable if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     * @throws T throwable on failed Try
     */
    public static <V, T extends Throwable> V assign(final Assignment<V> assignment, final Class<T> throwableClass,
            final String message, final Object arg1, final Object arg2) throws T
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a specified Throwable if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     * @throws T throwable on failed Try
     */
    public static <V, T extends Throwable> V assign(final Assignment<V> assignment, final Class<T> throwableClass,
            final String message, final Object arg1, final Object arg2, final Object arg3) throws T
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    /**
     * Tries to return a value to assign. Will throw a specified Throwable if the try fails.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param args Object...; potential 4th and further values to use for the formatting identifiers
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     * @throws T throwable on failed Try
     */
    public static <V, T extends Throwable> V assign(final Assignment<V> assignment, final Class<T> throwableClass,
            final String message, final Object arg1, final Object arg2, final Object arg3, final Object... args) throws T
    {
        try
        {
            return assignment.assign();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            argList.addAll(Arrays.asList(args));
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    // Execute

    /**
     * Tries to execute. Will throw a RuntimeException if the try fails.
     * @param execution Execution; functional interface to execute
     * @param message String; the message to use in the throwable
     * @throws RuntimeException on failed Try
     */
    public static void execute(final Execution execution, final String message) throws RuntimeException
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            throw catchThrowable(RuntimeException.class, message, new ArrayList<>(), cause);
        }
    }

    /**
     * Tries to execute. Will throw a RuntimeException if the try fails.
     * @param execution Execution; functional interface to execute
     * @param message String; the message to use in the throwable, with formatting identifier
     * @param arg Object; value to use for the formatting identifier
     * @throws RuntimeException on failed Try
     */
    public static void execute(final Execution execution, final String message, final Object arg) throws RuntimeException
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg);
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a RuntimeException if the try fails.
     * @param execution Execution; functional interface to execute
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @throws RuntimeException on failed Try
     */
    public static void execute(final Execution execution, final String message, final Object arg1, final Object arg2)
            throws RuntimeException
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a RuntimeException if the try fails.
     * @param execution Execution; functional interface to execute
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @throws RuntimeException on failed Try
     */
    public static void execute(final Execution execution, final String message, final Object arg1, final Object arg2,
            final Object arg3) throws RuntimeException
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a RuntimeException if the try fails.
     * @param execution Execution; functional interface to execute
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param args Object...; potential 4th and further values to use for the formatting identifiers
     * @throws RuntimeException on failed Try
     */
    public static void execute(final Execution execution, final String message, final Object arg1, final Object arg2,
            final Object arg3, final Object... args) throws RuntimeException
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            argList.addAll(Arrays.asList(args));
            throw catchThrowable(RuntimeException.class, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a specified Throwable if the try fails.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable
     * @param <T> throwable type
     * @throws T throwable on failed Try
     */
    public static <T extends Throwable> void execute(final Execution execution, final Class<T> throwableClass,
            final String message) throws T
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            throw catchThrowable(throwableClass, message, new ArrayList<>(), cause);
        }
    }

    /**
     * Tries to execute. Will throw a specified Throwable if the try fails.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifier
     * @param arg Object; value to use for the formatting identifier
     * @param <T> throwable type
     * @throws T throwable on failed Try
     */
    public static <T extends Throwable> void execute(final Execution execution, final Class<T> throwableClass,
            final String message, final Object arg) throws T
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg);
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a specified Throwable if the try fails.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param <T> throwable type
     * @throws T throwable on failed Try
     */
    public static <T extends Throwable> void execute(final Execution execution, final Class<T> throwableClass,
            final String message, final Object arg1, final Object arg2) throws T
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a specified Throwable if the try fails.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param <T> throwable type
     * @throws T throwable on failed Try
     */
    public static <T extends Throwable> void execute(final Execution execution, final Class<T> throwableClass,
            final String message, final Object arg1, final Object arg2, final Object arg3) throws T
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    /**
     * Tries to execute. Will throw a specified Throwable if the try fails.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; class of the throwable to throw
     * @param message String; the message to use in the throwable, with formatting identifiers
     * @param arg1 Object; 1st value to use for the formatting identifiers
     * @param arg2 Object; 2nd value to use for the formatting identifiers
     * @param arg3 Object; 3rd value to use for the formatting identifiers
     * @param args Object...; potential 4th and further values to use for the formatting identifiers
     * @param <T> throwable type
     * @throws T throwable on failed Try
     */
    public static <T extends Throwable> void execute(final Execution execution, final Class<T> throwableClass,
            final String message, final Object arg1, final Object arg2, final Object arg3, final Object... args) throws T
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            List<Object> argList = new ArrayList<>();
            argList.add(arg1);
            argList.add(arg2);
            argList.add(arg3);
            argList.addAll(Arrays.asList(args));
            throw catchThrowable(throwableClass, message, argList, cause);
        }
    }

    // Core of assign/execute methods

    /**
     * Core method to create the Throwable to throw.
     * @param throwableClass Class&lt;T&gt;; the throwable class
     * @param message String; the message to construct when an exception is thrown.
     * @param argList List&lt;Object&gt;; List&lt;Object&gt; the arguments as implied by format escapes in
     *            &lt;code&gt;message&lt;/code&gt;
     * @param cause Throwable; underlying cause thrown inside the assign()/execute()
     * @param <T> throwable type
     * @return T; throwable
     */
    private static <T extends Throwable> T catchThrowable(final Class<T> throwableClass, final String message,
            final List<Object> argList, final Throwable cause)
    {
        // create a clear message
        List<StackTraceElement> steList = new ArrayList<>(Arrays.asList(new Throwable().getStackTrace()));
        // see https://stackoverflow.com/questions/2411487/nullpointerexception-in-java-with-no-stacktrace
        // and https://hg.openjdk.java.net/jdk/jdk/file/tip/src/hotspot/share/opto/graphKit.cpp
        if (steList.size() > 2)
        {
            steList.remove(0); // remove the catchThrowable(...) call
            steList.remove(0); // remove the Try.assign/execute(...) call
        }
        StackTraceElement[] ste = steList.toArray(new StackTraceElement[steList.size()]);
        String where = ste[0].getClassName() + "." + ste[0].getMethodName() + " (" + ste[0].getLineNumber() + "): ";
        Object[] args = argList.toArray();
        String formattedMessage;
        try
        {
            formattedMessage = where + String.format(message, args);
        }
        catch (IllegalFormatException exception)
        {
            formattedMessage = where + message + " [FormatException; args=" + argList + "]";
        }

        // throw all other exceptions through reflection
        T exception;
        try
        {
            Constructor<T> constructor =
                    ClassUtil.resolveConstructor(throwableClass, new Class<?>[] {String.class, Throwable.class});
            List<StackTraceElement> steCause = new ArrayList<>(Arrays.asList(cause.getStackTrace()));
            // see https://stackoverflow.com/questions/2411487/nullpointerexception-in-java-with-no-stacktrace
            // and https://hg.openjdk.java.net/jdk/jdk/file/tip/src/hotspot/share/opto/graphKit.cpp
            if (steCause.size() > 3)
            {
                steCause.remove(steCause.size() - 1); // remove method that called Try.assign/execute(...) 
                steCause.remove(steCause.size() - 1); // remove the Try.assign/execute(...) call
                steCause.remove(steCause.size() - 1); // remove the Assignment/Execution implementation (can be lambda$#)
                cause.setStackTrace(steCause.toArray(new StackTraceElement[steCause.size()]));
            }
            exception = constructor.newInstance(formattedMessage, cause);
            exception.setStackTrace(ste);
        }
        catch (Throwable t)
        {
            RuntimeException rte = new RuntimeException(t.getMessage(), new Exception(formattedMessage, cause));
            rte.setStackTrace(ste);
            throw rte;
        }
        return exception;
    }

    // Test fail/succeed (JUNIT)

    /**
     * Fails if the assignment succeeds.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param <V> value type
     * @return V; value to assign
     */
    public static <V> V testFail(final Assignment<V> assignment)
    {
        return testFail(assignment, null, Throwable.class);
    }

    /**
     * Fails if the assignment succeeds.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; fail message
     * @param <V> value type
     * @return V; value to assign
     */
    public static <V> V testFail(final Assignment<V> assignment, final String message)
    {
        return testFail(assignment, message, Throwable.class);
    }

    /**
     * Fails if the assignment succeeds.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     */
    public static <V, T extends Throwable> V testFail(final Assignment<V> assignment, final Class<T> throwableClass)
    {
        return testFail(assignment, null, throwableClass);
    }

    /**
     * Fails if the assignment succeeds.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; fail message
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     */
    public static <V, T extends Throwable> V testFail(final Assignment<V> assignment, final String message,
            final Class<T> throwableClass)
    {
        V value;
        try
        {
            value = assignment.assign();
            throw new AssertionError(message);
        }
        catch (Throwable cause)
        {
            if (!throwableClass.isAssignableFrom(cause.getClass()))
            {
                throw new AssertionError("Assignment failed on unexpected Throwable, expected ("
                        + throwableClass.getSimpleName() + "), but got (" + cause.getClass().getSimpleName() + ").");
            }
            // expected to fail
            value = null;
        }
        return value;
    }

    /**
     * Fails if the assignment does not succeed.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param <V> value type
     * @return V; value to assign
     */
    public static <V> V testSucceed(final Assignment<V> assignment)
    {
        return testSucceed(assignment, null, Throwable.class);
    }

    /**
     * Fails if the assignment does not succeed.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; fail message
     * @param <V> value type
     * @return V; value to assign
     */
    public static <V> V testSucceed(final Assignment<V> assignment, final String message)
    {
        return testSucceed(assignment, message, Throwable.class);
    }

    /**
     * Fails if the assignment does not succeed.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     */
    public static <V, T extends Throwable> V testSucceed(final Assignment<V> assignment, final Class<T> throwableClass)
    {
        return testSucceed(assignment, null, throwableClass);
    }

    /**
     * Fails if the assignment does not succeed.
     * @param assignment Assignment&lt;V&gt;; functional interface to assign value
     * @param message String; fail message
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <V> value type
     * @param <T> throwable type
     * @return V; value to assign
     */
    public static <V, T extends Throwable> V testSucceed(final Assignment<V> assignment, final String message,
            final Class<T> throwableClass)
    {
        V value;
        try
        {
            value = assignment.assign();
        }
        catch (Throwable cause)
        {
            if (throwableClass.isAssignableFrom(cause.getClass()))
            {
                value = null;
            }
            else
            {
                throw new AssertionError("Assignment failed on unexpected Throwable, expected ("
                        + throwableClass.getSimpleName() + "), but got (" + cause.getClass().getSimpleName() + ").");
            }
        }
        return value;
    }

    /**
     * Fails if the execution succeeds.
     * @param execution Execution; functional interface to execute
     */
    public static void testFail(final Execution execution)
    {
        testFail(execution, null, Throwable.class);
    }

    /**
     * Fails if the execution succeeds.
     * @param execution Execution; functional interface to execute
     * @param message String; fail message
     */
    public static void testFail(final Execution execution, final String message)
    {
        testFail(execution, message, Throwable.class);
    }

    /**
     * Fails if the execution succeeds.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <T> throwable type
     */
    public static <T extends Throwable> void testFail(final Execution execution, final Class<T> throwableClass)
    {
        testFail(execution, null, throwableClass);
    }

    /**
     * Fails if the execution succeeds.
     * @param execution Execution; functional interface to execute
     * @param message String; fail message
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <T> throwable type
     */
    public static <T extends Throwable> void testFail(final Execution execution, final String message,
            final Class<T> throwableClass)
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            if (!throwableClass.isAssignableFrom(cause.getClass()))
            {
                throw new AssertionError("Execution failed on unexpected Throwable, expected (" + throwableClass.getSimpleName()
                        + "), but got (" + cause.getClass().getSimpleName() + ").");
            }
            // expected to fail
            return;
        }
        throw new AssertionError(message);
    }

    /**
     * Fails if the execution does not succeed.
     * @param execution Execution; functional interface to execute
     */
    public static void testSucceed(final Execution execution)
    {
        testSucceed(execution, null, Throwable.class);
    }

    /**
     * Fails if the execution does not succeed.
     * @param execution Execution; functional interface to execute
     * @param message String; fail message
     */
    public static void testSucceed(final Execution execution, final String message)
    {
        testSucceed(execution, message, Throwable.class);
    }

    /**
     * Fails if the execution does not succeed.
     * @param execution Execution; functional interface to execute
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <T> throwable type
     */
    public static <T extends Throwable> void testSucceed(final Execution execution, final Class<T> throwableClass)
    {
        testSucceed(execution, null, throwableClass);
    }

    /**
     * Fails if the execution does not succeed.
     * @param execution Execution; functional interface to execute
     * @param message String; fail message
     * @param throwableClass Class&lt;T&gt;; throwable class to catch
     * @param <T> throwable type
     */
    public static <T extends Throwable> void testSucceed(final Execution execution, final String message,
            final Class<T> throwableClass)
    {
        try
        {
            execution.execute();
        }
        catch (Throwable cause)
        {
            if (throwableClass.isAssignableFrom(cause.getClass()))
            {
                throw new AssertionError(message);
            }
            else
            {
                throw new AssertionError("Execution failed on unexpected Throwable, expected (" + throwableClass.getSimpleName()
                        + "), but got (" + cause.getClass().getSimpleName() + ").");
            }
        }
    }

    // Interfaces

    /**
     * Functional interface for calls to Try.assign(...). For this a lambda expression can be used.
     * 
     * <pre>
     * FileInputStream fis = Try.assign(() -&gt; new FileInputStream(fileString), IllegalArgumentException.class,
     *         "File %s is not a valid file.", fileString);
     * </pre>
     * <p>
     * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 31 jan. 2018 <br>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
     * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     * @param <V> value type
     */
    @FunctionalInterface
    public interface Assignment<V>
    {
        /**
         * Returns a value which is obtained from the context in which the Assignment was created.
         * @return value which is obtained from the context in which the Assignment was created
         * @throws Throwable on any throwable in the try
         */
        V assign() throws Throwable;
    }

    /**
     * Functional interface for calls to Try.execute(...). For this a lambda expression can be used.
     * 
     * <pre>
     * Try.execute(() -&gt; fis.close(), "Could not close the file.");
     * </pre>
     * <p>
     * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 31 jan. 2018 <br>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
     * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    @FunctionalInterface
    public interface Execution
    {
        /**
         * Executes some code using the context in which the Execution was created.
         * @throws Throwable on any throwable in the try
         */
        void execute() throws Throwable;
    }

}
