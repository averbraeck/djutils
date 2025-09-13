package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.djutils.decoderdumper.HexDumper;
import org.djutils.exceptions.Try;
import org.djutils.serialization.util.SerialDataDumper;
import org.junit.jupiter.api.Test;

/**
 * SimpleSerializationTest tests the decode methods for primitives, and compares the big-endian and little-endian results.
 * <p>
 * Copyright (c) 2025-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimpleSerializationTest
{
    /**
     * Basic test encoding and decoding of the basic types.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void simpleTests() throws SerializationException
    {
        int intValue = 123;
        Integer integerValue = -456;
        short shortValue = 234;
        Short shortValue2 = -345;
        long longValue = 98765L;
        Long longValue2 = -98765L;
        Byte byteValue = 12;
        byte byteValue2 = -23;
        float floatValue = 1.234f;
        Float floatValue2 = -3.456f;
        double doubleValue = 4.56789;
        Double doubleValue2 = -4.56789;
        boolean boolValue = true;
        Boolean boolValue2 = false;
        Character charValue = 'a';
        char charValue2 = 'b';
        String stringValue = "abcDEF123!@#ȦȧȨ\u0776\u0806\u080e";
        Object[] objects = new Object[] {intValue, integerValue, shortValue, shortValue2, longValue, longValue2, byteValue,
                byteValue2, floatValue, floatValue2, doubleValue, doubleValue2, boolValue, boolValue2, charValue, charValue2,
                stringValue};
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                // System.out.println("" + endianness + ", UTF8=" + encodeUTF8);
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianness, objects)
                        : TypedMessage.encodeUTF16(endianness, objects);
                HexDumper.hexDumper(serialized);
                String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
                assertFalse(sdd.contains("Error"));
                assertTrue(sdd.contains("Integer_32"));
                assertTrue(sdd.contains("Short_16"));
                assertTrue(sdd.contains("Long_64"));
                assertTrue(sdd.contains("Byte_8"));
                assertTrue(sdd.contains("Float_32"));
                assertTrue(sdd.contains("Double_64"));
                assertTrue(sdd.contains("Boolean_8"));
                assertTrue(sdd.contains(encodeUTF8 ? "Char_8" : "Char_16"));
                assertTrue(sdd.contains(encodeUTF8 ? "String_8" : "String_16"));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(endianness, serialized)
                            : TypedMessage.decodeToObjectDataTypes(endianness, serialized);
                    assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                    for (int i = 0; i < objects.length; i++)
                    {
                        assertEquals(objects[i], decodedObjects[i],
                                "decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input");
                    }
                }
            }
        }
    }

    /**
     * Test decodeInt method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeInt() throws SerializationException
    {
        int value = 1024;
        byte[] intSerBE = TypedObject.encode(Endianness.BIG_ENDIAN, value);
        // System.out.println(IntStream.range(0, intSerBE.length).map(i -> intSerBE[i] >= 0 ? intSerBE[i] : intSerBE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intBE = TypedObject.decodeInt(Endianness.BIG_ENDIAN, intSerBE);
        assertEquals(value, intBE);

        byte[] intSerLE = TypedObject.encode(Endianness.LITTLE_ENDIAN, value);
        // System.out.println(IntStream.range(0, intSerLE.length).map(i -> intSerLE[i] >= 0 ? intSerLE[i] : intSerLE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intLE = TypedObject.decodeInt(Endianness.LITTLE_ENDIAN, intSerLE);
        assertEquals(value, intLE);

        assertNotEquals(intSerBE, intSerLE);
        assertEquals(intBE, intLE);

        assertEquals(Integer.valueOf(value), TypedObject.decodeToObjectDataTypes(Endianness.BIG_ENDIAN, intSerBE));
        assertArrayEquals(new Object[] {Integer.valueOf(value)},
                TypedMessage.decodeToObjectDataTypes(Endianness.BIG_ENDIAN, intSerBE));
        assertEquals(value, TypedObject.decodeToPrimitiveDataTypes(Endianness.BIG_ENDIAN, intSerBE));
        assertArrayEquals(new Object[] {value}, TypedMessage.decodeToPrimitiveDataTypes(Endianness.BIG_ENDIAN, intSerBE));

        assertEquals(Integer.valueOf(value), TypedObject.decodeToObjectDataTypes(Endianness.LITTLE_ENDIAN, intSerLE));
        assertArrayEquals(new Object[] {Integer.valueOf(value)},
                TypedMessage.decodeToObjectDataTypes(Endianness.LITTLE_ENDIAN, intSerLE));
        assertEquals(value, TypedObject.decodeToPrimitiveDataTypes(Endianness.LITTLE_ENDIAN, intSerLE));
        assertArrayEquals(new Object[] {value}, TypedMessage.decodeToPrimitiveDataTypes(Endianness.LITTLE_ENDIAN, intSerLE));
    }

    /**
     * Test decodeByte method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeByte() throws SerializationException
    {
        byte value = 55;
        byte[] valueSerBE = TypedMessage.encode(Endianness.BIG_ENDIAN, value);
        byte valueBE = TypedObject.decodeByte(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encode(Endianness.LITTLE_ENDIAN, value);
        byte valueLE = TypedObject.decodeByte(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeShort method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeShort() throws SerializationException
    {
        short value = 5534;
        byte[] valueSerBE = TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, value);
        short valueBE = TypedObject.decodeShort(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF8(Endianness.LITTLE_ENDIAN, value);
        short valueLE = TypedObject.decodeShort(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeLong method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeLong() throws SerializationException
    {
        long value = 5534766567L;
        byte[] valueSerBE = TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, value);
        long valueBE = TypedObject.decodeLong(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF8(Endianness.LITTLE_ENDIAN, value);
        long valueLE = TypedObject.decodeLong(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeFloat method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeFloat() throws SerializationException
    {
        float value = 5534.123f;
        byte[] valueSerBE = TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, value);
        float valueBE = TypedObject.decodeFloat(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF8(Endianness.LITTLE_ENDIAN, value);
        float valueLE = TypedObject.decodeFloat(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeDouble method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeDouble() throws SerializationException
    {
        double value = 55346533.77d;
        byte[] valueSerBE = TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, value);
        double valueBE = TypedObject.decodeDouble(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF8(Endianness.LITTLE_ENDIAN, value);
        double valueLE = TypedObject.decodeDouble(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeBoolean method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeBoolean() throws SerializationException
    {
        boolean value = true;
        byte[] valueSerBE = TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, value);
        boolean valueBE = TypedObject.decodeBoolean(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF8(Endianness.LITTLE_ENDIAN, value);
        boolean valueLE = TypedObject.decodeBoolean(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeCharUtf8 method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeCharUtf8() throws SerializationException
    {
        char value = '}';
        byte[] valueSerBE = TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, value);
        char valueBE = TypedObject.decodeCharUtf8(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF8(Endianness.LITTLE_ENDIAN, value);
        char valueLE = TypedObject.decodeCharUtf8(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test decodeCharUtf16 method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeCharUtf16() throws SerializationException
    {
        char value = '\u00A2'; // cent sign
        byte[] valueSerBE = TypedObject.encodeUTF16(Endianness.BIG_ENDIAN, value);
        char valueBE = TypedObject.decodeCharUtf16(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedObject.encodeUTF16(Endianness.LITTLE_ENDIAN, value);
        char valueLE = TypedObject.decodeCharUtf16(Endianness.LITTLE_ENDIAN, valueSerLE);
        assertEquals(value, valueLE);

        assertNotEquals(valueSerBE, valueSerLE);
        assertEquals(valueBE, valueLE);
    }

    /**
     * Test errors for simple method.
     */
    @Test
    public void testDecodeErrors()
    {
        Endianness endianness = Endianness.BIG_ENDIAN;
        final byte[] buffer = new byte[12];
        buffer[0] = 127;
        UnitTest.testFail(() -> TypedObject.decodeInt(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeShort(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeByte(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeLong(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeFloat(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeDouble(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeBoolean(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeCharUtf8(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeCharUtf16(endianness, buffer), SerializationException.class);

        final byte[] buffer2 = new byte[1];
        buffer2[0] = 127;
        UnitTest.testFail(() -> TypedObject.decodeInt(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeShort(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeByte(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeLong(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeFloat(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeDouble(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeBoolean(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeCharUtf8(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeCharUtf16(endianness, buffer2), SerializationException.class);

        UnitTest.testFail(() -> TypedObject.decodeToObjectDataTypes(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedObject.decodeToPrimitiveDataTypes(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedMessage.decodeToObjectDataTypes(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> TypedMessage.decodeToPrimitiveDataTypes(endianness, buffer), SerializationException.class);
    }
}
