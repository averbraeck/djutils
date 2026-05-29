package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.ByteOrder;

import org.djutils.serialization.codecs.MessageCodec;
import org.djutils.serialization.codecs.Pointer;
import org.junit.jupiter.api.Test;

/**
 * Test compound objects and the basic classes of the serialization project such as Pointer and exceptions.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 */
public class SerializationTest extends AbstractSerializationTest
{

    /**
     * Test that the encoder throws a SerializationException when given something that it does not know how to serialize.
     */
    @Test
    public void testUnhandledObject()
    {
        File file = new File("whatever");
        Object[] objects = new Object[] {file};
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            try
            {
                MessageCodec.encodeUTF16(endianness, objects);
                fail("Non serializable object should have thrown a SerializationException");
            }
            catch (SerializationException se)
            {
                // Ignore expected exception
            }

            Integer[][] badMatrix = new Integer[0][0];
            objects = new Object[] {badMatrix};
            try
            {
                MessageCodec.encodeUTF16(endianness, objects);
                fail("Zero sized matrix should have thrown a SerializationException");
            }
            catch (SerializationException se)
            {
                // Ignore expected exception
            }
        }
    }

    /**
     * Test the Pointer class.
     */
    @Test
    public void pointerTest()
    {
        Pointer pointer = new Pointer();
        assertEquals(0, pointer.get(), "initial offset is 0");
        assertEquals(0, pointer.getAndIncrement(10), "initial offset is 0");
        assertEquals(10, pointer.get(), "offset is now 10");
        pointer.inc(20);
        assertEquals(30, pointer.get(), "offset is now 30");
        assertTrue(pointer.toString().startsWith("Pointer"), "ToString method returns something descriptive");
    }

    /**
     * Test all constructors for SerializationException.
     */
    @Test
    public final void serializationExceptionTest()
    {
        String message = "MessageString";
        Exception e = new SerializationException(message);
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(null, e.getCause(), "cause should be null");
        e = new SerializationException();
        assertEquals(null, e.getCause(), "cause should be null");
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new SerializationException(cause);
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
        e = new SerializationException(message, cause);
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
        for (boolean enableSuppression : new boolean[] {true, false})
        {
            for (boolean writableStackTrace : new boolean[] {true, false})
            {
                e = new SerializationException(message, cause, enableSuppression, writableStackTrace);
                assertTrue(null != e, "Exception should not be null");
                assertEquals(message, e.getMessage(), "message should be our message");
                assertEquals(cause, e.getCause(), "cause should not be our cause");
                assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
                // Don't know how to check if suppression is enabled/disabled
                StackTraceElement[] stackTrace = new StackTraceElement[1];
                stackTrace[0] = new StackTraceElement("a", "b", "c", 1234);
                try
                {
                    e.setStackTrace(stackTrace);
                }
                catch (Exception e1)
                {
                    assertTrue(writableStackTrace, "Stack trace should be writable");
                    continue;
                }
                // You wouldn't believe it, but a call to setStackTrace if non-writable is silently ignored
                StackTraceElement[] retrievedStackTrace = e.getStackTrace();
                if (retrievedStackTrace.length > 0)
                {
                    assertTrue(writableStackTrace, "stack trace should be writable");
                }
            }
        }
    }

    /**
     * Test all constructors for SerializationRuntimeException.
     */
    @Test
    public final void serializationRuntimeExceptionTest()
    {
        String message = "MessageString";
        Exception e = new SerializationRuntimeException(message);
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(null, e.getCause(), "cause should be null");
        e = new SerializationRuntimeException();
        assertEquals(null, e.getCause(), "cause should be null");
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new SerializationRuntimeException(cause);
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
        e = new SerializationRuntimeException(message, cause);
        assertEquals(message, e.getMessage(), "message should be our message");
        assertEquals(cause, e.getCause(), "cause should not be our cause");
        assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
        for (boolean enableSuppression : new boolean[] {true, false})
        {
            for (boolean writableStackTrace : new boolean[] {true, false})
            {
                e = new SerializationRuntimeException(message, cause, enableSuppression, writableStackTrace);
                assertTrue(null != e, "Exception should not be null");
                assertEquals(message, e.getMessage(), "message should be our message");
                assertEquals(cause, e.getCause(), "cause should not be our cause");
                assertEquals(causeString, e.getCause().getMessage(), "cause description should be our cause string");
                // Don't know how to check if suppression is enabled/disabled
                StackTraceElement[] stackTrace = new StackTraceElement[1];
                stackTrace[0] = new StackTraceElement("a", "b", "c", 1234);
                try
                {
                    e.setStackTrace(stackTrace);
                }
                catch (Exception e1)
                {
                    assertTrue(writableStackTrace, "Stack trace should be writable");
                    continue;
                }
                // You wouldn't believe it, but a call to setStackTrace if non-writable is silently ignored
                StackTraceElement[] retrievedStackTrace = e.getStackTrace();
                if (retrievedStackTrace.length > 0)
                {
                    assertTrue(writableStackTrace, "stack trace should be writable");
                }
            }
        }
    }

    /**
     * Test the remainder of the Endianness class.
     */
    @Test
    public void testEndianness()
    {
        assertTrue(Endianness.BIG_ENDIAN.isBigEndian(), "Endianness.BIG_ENDIAN is big endian");
        assertFalse(Endianness.LITTLE_ENDIAN.isBigEndian(), "Endianness.LITTLE_ENDIAN is not big endian");
        assertEquals(ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN), Endianness.isPlatformBigEndian(),
                "Platform endianness matches what Endianness says");
        assertTrue(Endianness.bigEndian().isBigEndian(), "Endianness.BIG_ENDIAN is big endian");
        assertFalse(Endianness.littleEndian().isBigEndian(), "Endianness.LITTLE_ENDIAN is not big endian");
        assertTrue(Endianness.BIG_ENDIAN.toString().startsWith("Endianness"), "Endianness has descriptive toString method");
    }
}
