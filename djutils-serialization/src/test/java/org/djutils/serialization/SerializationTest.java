package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.djutils.decoderdumper.HexDumper;
import org.djutils.serialization.serializers.BasicSerializer;
import org.djutils.serialization.serializers.Pointer;
import org.junit.jupiter.api.Test;

/**
 * Test compound objects and the basic classes of the serialization project such as Pointer and exceptions.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class SerializationTest extends AbstractSerializationTest
{

    /** Class used to test serialization of classes that implement SerializableObject. */
    static class Compound implements SerializableObject<Compound>
    {
        /** Field 1. */
        @SuppressWarnings("checkstyle:visibilitymodifier")
        public Integer intValue;

        /** Field 2. */
        @SuppressWarnings("checkstyle:visibilitymodifier")
        public Double doubleValue;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.doubleValue == null) ? 0 : this.doubleValue.hashCode());
            result = prime * result + ((this.intValue == null) ? 0 : this.intValue.hashCode());
            return result;
        }

        @SuppressWarnings("checkstyle:needbraces")
        @Override
        public boolean equals(final Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Compound other = (Compound) obj;
            if (this.doubleValue == null)
            {
                if (other.doubleValue != null)
                    return false;
            }
            else if (!this.doubleValue.equals(other.doubleValue))
                return false;
            if (this.intValue == null)
            {
                if (other.intValue != null)
                    return false;
            }
            else if (!this.intValue.equals(other.intValue))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Compound [intValue=" + this.intValue + ", doubleValue=" + this.doubleValue + "]";
        }

        /**
         * Construct a new Compound object.
         * @param intValue the value to assign to intValue
         * @param doubleValue the value to assign to doubleValue
         */
        Compound(final int intValue, final double doubleValue)
        {
            this.intValue = intValue;
            this.doubleValue = doubleValue;
        }

        @Override
        public List<Object> exportAsList()
        {
            List<Object> result = new ArrayList<>();
            result.add(this.intValue);
            result.add(this.doubleValue);
            return result;
        }
    }

    /**
     * Test the compound array encoder and decoder.
     * @throws SerializationException when that happens uncaught, this test has failed
     */
    @Test
    public void testCompoundArrays() throws SerializationException
    {
        Compound[] testArray = new Compound[] {new Compound(1, 0.1), new Compound(2, 0.2), new Compound(3, 0.3)};
        Object[] objects = new Object[] {testArray};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                // System.out.println("Encoding " + (encodeUTF8 ? "UTF8" : "UTF16") + ", " + endianUtil);
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                HexDumper.hexDumper(serialized);
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized)
                            : TypedMessage.decodeToObjectDataTypes(serialized);
                    assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                    // Replace all List objects in the result by corresponding new Compound objects
                    for (int i = 0; i < objects.length; i++)
                    {
                        Object o = decodedObjects[i];
                        if (o instanceof TypedObject.MinimalSerializableObject[])
                        {
                            TypedObject.MinimalSerializableObject[] in = ((TypedObject.MinimalSerializableObject[]) o);
                            Compound[] out = new Compound[in.length];
                            for (int j = 0; j < in.length; j++)
                            {
                                List<Object> fields = in[j].exportAsList();
                                Integer intValue = (Integer) fields.get(0);
                                Double doubleValue = (Double) fields.get(1);
                                out[j] = new Compound(intValue, doubleValue);
                            }
                            decodedObjects[i] = out;
                        }
                    }
                    for (int i = 0; i < objects.length; i++)
                    {
                        if (objects[i] instanceof Compound[])
                        {
                            Compound[] in = (Compound[]) objects[i];
                            assertTrue(decodedObjects[i] instanceof Compound[], "decoded object is now also a Compound[]");
                            Compound[] out = (Compound[]) objects[i];
                            assertEquals(in.length, out.length, "Compound arrays have same length");
                            for (int j = 0; j < in.length; j++)
                            {
                                assertEquals(in[j], out[j], "reconstructed compound object matches input");
                            }
                        }
                        else
                        {
                            assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
                                    "decoded object at index " + i + "(" + objects[i]
                                            + ") equals corresponding object in input");
                        }
                    }
                }
            }
        }
    }

    /**
     * Test that the encoder throws a SerializationException when given something that it does not know how to serialize.
     */
    @Test
    public void testUnhandledObject()
    {
        File file = new File("whatever");
        Object[] objects = new Object[] {file};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            try
            {
                TypedMessage.encodeUTF16(endianUtil, objects);
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
                TypedMessage.encodeUTF16(endianUtil, objects);
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
     * Test the remainder of the EndianUtil class.
     */
    @Test
    public void testEndianUtil()
    {
        assertTrue(EndianUtil.BIG_ENDIAN.isBigEndian(), "EndianUtil.BIG_ENDIAN is big endian");
        assertFalse(EndianUtil.LITTLE_ENDIAN.isBigEndian(), "EndianUtil.LITTLE_ENDIAN is not big endian");
        assertEquals(ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN), EndianUtil.isPlatformBigEndian(),
                "Platform endianness matches what EndianUtil says");
        assertTrue(EndianUtil.bigEndian().isBigEndian(), "EndianUtil.BIG_ENDIAN is big endian");
        assertFalse(EndianUtil.littleEndian().isBigEndian(), "EndianUtil.LITTLE_ENDIAN is not big endian");
        assertTrue(EndianUtil.BIG_ENDIAN.toString().startsWith("EndianUtil"), "EndianUtil has descriptive toString method");
    }

    /**
     * Test the toString and dataClassName methods of the BasicSerializer.
     */
    @Test
    public void testBasicSerializer()
    {
        byte code = 123;
        String dataClassName = "dataClass";
        BasicSerializer<Byte> testSerializer = new BasicSerializer<Byte>(code, dataClassName)
        {

            @Override
            public int size(final Byte object) throws SerializationException
            {
                // Auto-generated method stub; never called
                return 0;
            }

            @Override
            public int sizeWithPrefix(final Byte object) throws SerializationException
            {
                // Auto-generated method stub; never called
                return 0;
            }

            @Override
            public void serialize(final Byte object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                    throws SerializationException
            {
                // Auto-generated method stub; never called
            }

            @Override
            public void serializeWithPrefix(final Byte object, final byte[] buffer, final Pointer pointer,
                    final EndianUtil endianUtil) throws SerializationException
            {
                // Auto-generated method stub; never called
            }

            @Override
            public Byte deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                    throws SerializationException
            {
                // Auto-generated method stub; never called
                return null;
            }

            @Override
            public int getNumberOfDimensions()
            {
                // Auto-generated method stub
                return 0;
            }
        };
        // We only want to test two methods; so we don't have to provide real implementation for other methods
        assertEquals(dataClassName, testSerializer.dataClassName(), "data class name is returned");
        assertTrue(testSerializer.toString().startsWith("BasicSerializer"), "toString returns something descriptive");
    }

}
