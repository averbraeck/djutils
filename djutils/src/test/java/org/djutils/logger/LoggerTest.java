package org.djutils.logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

import org.junit.Test;
import org.pmw.tinylog.Configuration;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.LogEntry;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.LogEntryValue;
import org.pmw.tinylog.writers.Writer;

/**
 * LoggerTest.java. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class LoggerTest
{
    /** Records last output of logger. */
    private StringWriter stringWriter = new StringWriter();

    /**
     * Helper method.
     * @param expectedMessage String; expected subString in result of stringWriter. If null; there should be no message recorded
     *            in the stringWriter.
     */
    private void verifyLogMessage(final String expectedMessage)
    {
        String actualMessage = this.stringWriter.getResult();
        if (expectedMessage != null)
        {
            assertNotNull(actualMessage);
            assertTrue(actualMessage.contains(expectedMessage));
        }
        else
        {
            assertNull(actualMessage);
        }
        this.stringWriter.clear();
    }

    private void removeConsoleWriter()
    {
        Writer consoleWriter = null;
        for (Writer writer : CategoryLogger.getWriters())
        {
            if (writer instanceof ConsoleWriter)
            {
                consoleWriter = writer;
            }
        }
        CategoryLogger.removeWriter(consoleWriter);
    }
    
    private void addConsoleWriter()
    {
        CategoryLogger.addWriter(new ConsoleWriter());
    }
    
    /**
     * Test whether Logger works correctly.
     */
    @Test
    public final void loggerTest()
    {
        removeConsoleWriter();
        CategoryLogger.addWriter(stringWriter);
        CategoryLogger.setAllLogLevel(Level.DEBUG);
        String testMessage = "test message";
        CategoryLogger.always().error(testMessage);
        verifyLogMessage(testMessage);
        CategoryLogger.when(false).error(testMessage);
        verifyLogMessage(null);
        CategoryLogger.when(true).error(testMessage);
        verifyLogMessage(testMessage);

        LogCategory testLogCategory = new LogCategory("TEST");
        CategoryLogger.removeLogCategory(LogCategory.ALL);
        CategoryLogger.filter(testLogCategory).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.addLogCategory(testLogCategory);
        CategoryLogger.filter(testLogCategory).info(testMessage);
        verifyLogMessage(testMessage);
        CategoryLogger.removeLogCategory(testLogCategory);
        CategoryLogger.filter(testLogCategory).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.filter(LogCategory.ALL).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.addLogCategory(LogCategory.ALL);
        CategoryLogger.filter(LogCategory.ALL).info(testMessage);
        verifyLogMessage(testMessage);
        CategoryLogger.always().info(testMessage);
        verifyLogMessage(testMessage);

        CategoryLogger.removeLogCategory(LogCategory.ALL);
        CategoryLogger.filter(testLogCategory).when(false).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.addLogCategory(testLogCategory);
        CategoryLogger.filter(testLogCategory).when(false).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.removeLogCategory(testLogCategory);
        CategoryLogger.filter(testLogCategory).when(false).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.filter(LogCategory.ALL).when(false).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.addLogCategory(LogCategory.ALL);
        CategoryLogger.filter(LogCategory.ALL).when(false).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.always().when(false).info(testMessage);
        verifyLogMessage(null);

        CategoryLogger.removeLogCategory(LogCategory.ALL);
        CategoryLogger.filter(testLogCategory).when(true).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.addLogCategory(testLogCategory);
        CategoryLogger.filter(testLogCategory).when(true).info(testMessage);
        verifyLogMessage(testMessage);
        CategoryLogger.removeLogCategory(testLogCategory);
        CategoryLogger.filter(testLogCategory).when(true).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.filter(LogCategory.ALL).when(true).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.addLogCategory(LogCategory.ALL);
        CategoryLogger.filter(LogCategory.ALL).when(true).info(testMessage);
        verifyLogMessage(testMessage);
        CategoryLogger.always().when(true).info(testMessage);
        verifyLogMessage(testMessage);

        CategoryLogger.always().when(new BooleanSupplier()
        {
            @Override
            public boolean getAsBoolean()
            {
                return true;
            }
        }).info(testMessage);
        verifyLogMessage(testMessage);

        CategoryLogger.always().when(new BooleanSupplier()
        {
            @Override
            public boolean getAsBoolean()
            {
                return false;
            }
        }).info(testMessage);
        verifyLogMessage(null);
        CategoryLogger.removeWriter(stringWriter);
        addConsoleWriter();
    }

    /**
     * Test varying the logging level.
     * @throws SecurityException when a logging method can not be found (should not happen)
     * @throws NoSuchMethodException when a logging method can not be found (should not happen)
     * @throws InvocationTargetException when calling a logging method through reflection fails (should not happen)
     * @throws IllegalArgumentException when calling a logging method through reflection fails (should not happen)
     * @throws IllegalAccessException when calling a logging method through reflection fails (should not happen)
     */
    @Test
    public void testLogLevels() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        CategoryLogger.addWriter(stringWriter);
        CategoryLogger.setAllLogLevel(Level.DEBUG);
        removeConsoleWriter();
        String[] methodNames = new String[] { "trace", "debug", "info", "warn", "error" };
        Level[] logLevels = new Level[] { Level.TRACE, Level.DEBUG, Level.INFO, Level.WARNING, Level.ERROR, Level.OFF };
        for (int levelIndex = 0; levelIndex < logLevels.length; levelIndex++)
        {
            CategoryLogger.setAllLogLevel(logLevels[levelIndex]);
            for (int methodIndex = 0; methodIndex < methodNames.length; methodIndex++)
            {
                // String; no additional arguments
                String message = "test message";
                String methodName = methodNames[methodIndex];
                Method method = CategoryLogger.delegateLogger.getClass().getDeclaredMethod(methodName, String.class);
                method.invoke(CategoryLogger.always(), message);
                if (methodIndex < levelIndex)
                {
                    verifyLogMessage(null);
                }
                else
                {
                    verifyLogMessage(message);
                }
                method.invoke(CategoryLogger.when(false), message);
                verifyLogMessage(null);

                // Object (no arguments - of course)
                method = CategoryLogger.delegateLogger.getClass().getDeclaredMethod(methodName, Object.class);
                method.invoke(CategoryLogger.always(), message);
                if (methodIndex < levelIndex)
                {
                    verifyLogMessage(null);
                }
                else
                {
                    verifyLogMessage(message);
                }
                method.invoke(CategoryLogger.when(false), message);
                verifyLogMessage(null);
                
                // Throwable
                String exceptionMessage = "ExceptionMessage";
                Exception exception = new Exception(exceptionMessage);
                method = CategoryLogger.delegateLogger.getClass().getDeclaredMethod(methodName, Throwable.class);
                method.invoke(CategoryLogger.always(), exception);
                if (methodIndex < levelIndex)
                {
                    verifyLogMessage(null);
                }
                else
                {
                    verifyLogMessage(exceptionMessage);
                }
                method.invoke(CategoryLogger.when(false), exception);
                verifyLogMessage(null);
                
                // Throwable with message
                String extraMessage = "Extra Message";
                method = CategoryLogger.delegateLogger.getClass().getDeclaredMethod(methodName, Throwable.class, String.class);
                method.invoke(CategoryLogger.always(), exception, extraMessage);
                if (methodIndex < levelIndex)
                {
                    verifyLogMessage(null);
                }
                else
                {
                    assertTrue(stringWriter.getResult().contains(extraMessage));
                    verifyLogMessage(exceptionMessage);
                }
                method.invoke(CategoryLogger.when(false), exception, extraMessage);
                verifyLogMessage(null);
                
                // String, with arguments
                message = "test message arg1={}, arg2={}";
                int arg1 = 1;
                String arg2 = "2";
                String expectedMessage = message.replaceFirst("\\{\\}", String.valueOf(arg1)).replaceFirst("\\{\\}", arg2);
                method = CategoryLogger.delegateLogger.getClass().getDeclaredMethod(methodName, String.class, Object[].class);
                method.invoke(CategoryLogger.always(), message, new Object[] { arg1, arg2 });
                if (methodIndex < levelIndex)
                {
                    verifyLogMessage(null);
                }
                else
                {
                    verifyLogMessage(expectedMessage);
                }
                method.invoke(CategoryLogger.when(false), message, new Object[] { arg1, arg2 });
                verifyLogMessage(null);

                // Throwable with message and arguments
                method = CategoryLogger.delegateLogger.getClass().getDeclaredMethod(methodName, Throwable.class, String.class,
                        Object[].class);
                method.invoke(CategoryLogger.always(), exception, message, new Object[] { arg1, arg2 });
                if (methodIndex < levelIndex)
                {
                    verifyLogMessage(null);
                }
                else
                {
                    assertTrue(stringWriter.getResult().contains(exceptionMessage));
                    verifyLogMessage(expectedMessage);
                }
                method.invoke(CategoryLogger.when(false), exception, message, new Object[] { arg1, arg2 });
                verifyLogMessage(null);

            }
        }
        addConsoleWriter();
        CategoryLogger.setAllLogLevel(Level.DEBUG);
        CategoryLogger.removeWriter(stringWriter);
    }
    
    /**
     * Filter with multiple categories.
     */
    @Test
    public void testFilterOnCategories()
    {
        removeConsoleWriter();
        String message = "Test message";
        CategoryLogger.setAllLogLevel(Level.DEBUG);
        CategoryLogger.addWriter(stringWriter);
        LogCategory one = new LogCategory("ONE");
        LogCategory two = new LogCategory("TWO");
        LogCategory three = new LogCategory("THREE");
        
        CategoryLogger.setLogCategories();
        CategoryLogger.always().info(message);
        verifyLogMessage(message);
        CategoryLogger.filter().info(message);
        verifyLogMessage(null);
        CategoryLogger.filter(one).info(message);
        verifyLogMessage(null);
        CategoryLogger.filter(one, two).info(message);
        verifyLogMessage(null);
        CategoryLogger.filter(one, two, three).info(message);
        verifyLogMessage(null);
        
        CategoryLogger.setLogCategories(one);
        CategoryLogger.always().info(message);
        verifyLogMessage(message);
        CategoryLogger.filter().info(message);
        verifyLogMessage(null);
        CategoryLogger.filter(one).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(one, two).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(one, two, three).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(two, three).info(message);
        verifyLogMessage(null);
        CategoryLogger.filter(three).info(message);
        verifyLogMessage(null);
        
        CategoryLogger.setLogCategories(one, two);
        CategoryLogger.always().info(message);
        verifyLogMessage(message);
        CategoryLogger.filter().info(message);
        verifyLogMessage(null);
        CategoryLogger.filter(one).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(one, two).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(one, two, three).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(two, three).info(message);
        verifyLogMessage(message);
        CategoryLogger.filter(three).info(message);
        verifyLogMessage(null);
        
        CategoryLogger.setLogCategories(LogCategory.ALL);
        CategoryLogger.removeWriter(stringWriter);
        addConsoleWriter();
    }

    /** ... */
    private static class StringWriter implements Writer
    {
        /** Last output. */
        private String result = null;

        @Override
        public Set<LogEntryValue> getRequiredLogEntryValues()
        {
            return EnumSet.of(LogEntryValue.LEVEL, LogEntryValue.RENDERED_LOG_ENTRY);
        }

        @Override
        public void init(final Configuration configuration) throws Exception
        {
            // Nothing to do
        }

        @Override
        public void write(final LogEntry logEntry) throws Exception
        {
            this.result = logEntry.getRenderedLogEntry();
        }

        @Override
        public void flush() throws Exception
        {
            // Nothing to do
        }

        @Override
        public void close() throws Exception
        {
            // Nothing to do
        }

        /**
         * Return the last logged message.
         * @return String; the last logged message
         */
        public String getResult()
        {
            return this.result;
        }

        /**
         * Nullify the last logged message (so we can distinguish a newly received message even when it is the empty string.
         */
        public void clear()
        {
            this.result = null;
        }

    }

}
