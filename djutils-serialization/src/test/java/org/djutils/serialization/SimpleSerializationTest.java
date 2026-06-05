package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.djutils.decoderdumper.HexDumper;
import org.djutils.serialization.codecs.Codec;
import org.djutils.serialization.codecs.MessageCodec;
import org.djutils.serialization.util.SerialDataDumper;
import org.djutils.test.UnitTest;
import org.junit.jupiter.api.Test;

/**
 * SimpleSerializationTest tests the decode methods for primitives, and compares the big-endian and little-endian results.
 * <p>
 * Copyright (c) 2025-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
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
                byte[] serialized = encodeUTF8 ? MessageCodec.encodeUTF8(endianness, objects)
                        : MessageCodec.encodeUTF16(endianness, objects);
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
                    Object[] decodedObjects = primitive ? MessageCodec.decodeToPrimitiveDataTypes(endianness, serialized)
                            : MessageCodec.decodeToObjectDataTypes(endianness, serialized);
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
        byte[] intSerBE = Codec.encode(value, Endianness.BIG_ENDIAN);
        // System.out.println(IntStream.range(0, intSerBE.length).map(i -> intSerBE[i] >= 0 ? intSerBE[i] : intSerBE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intBE = Codec.decodeInt(Endianness.BIG_ENDIAN, intSerBE);
        assertEquals(value, intBE);

        byte[] intSerLE = Codec.encode(value, Endianness.LITTLE_ENDIAN);
        // System.out.println(IntStream.range(0, intSerLE.length).map(i -> intSerLE[i] >= 0 ? intSerLE[i] : intSerLE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intLE = Codec.decodeInt(Endianness.LITTLE_ENDIAN, intSerLE);
        assertEquals(value, intLE);

        assertNotEquals(intSerBE, intSerLE);
        assertEquals(intBE, intLE);

        assertEquals(Integer.valueOf(value), Codec.decodeToObjectDataType(Endianness.BIG_ENDIAN, intSerBE));
        assertArrayEquals(new Object[] {Integer.valueOf(value)},
                MessageCodec.decodeToObjectDataTypes(Endianness.BIG_ENDIAN, intSerBE));
        assertEquals(value, Codec.decodeToPrimitiveDataType(Endianness.BIG_ENDIAN, intSerBE));
        assertArrayEquals(new Object[] {value}, MessageCodec.decodeToPrimitiveDataTypes(Endianness.BIG_ENDIAN, intSerBE));

        assertEquals(Integer.valueOf(value), Codec.decodeToObjectDataType(Endianness.LITTLE_ENDIAN, intSerLE));
        assertArrayEquals(new Object[] {Integer.valueOf(value)},
                MessageCodec.decodeToObjectDataTypes(Endianness.LITTLE_ENDIAN, intSerLE));
        assertEquals(value, Codec.decodeToPrimitiveDataType(Endianness.LITTLE_ENDIAN, intSerLE));
        assertArrayEquals(new Object[] {value}, MessageCodec.decodeToPrimitiveDataTypes(Endianness.LITTLE_ENDIAN, intSerLE));
    }

    /**
     * Test decodeByte method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeByte() throws SerializationException
    {
        byte value = 55;
        byte[] valueSerBE = Codec.encode(value, Endianness.BIG_ENDIAN);
        byte valueBE = Codec.decodeByte(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encode(value, Endianness.LITTLE_ENDIAN);
        byte valueLE = Codec.decodeByte(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF8(value, Endianness.BIG_ENDIAN);
        short valueBE = Codec.decodeShort(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF8(value, Endianness.LITTLE_ENDIAN);
        short valueLE = Codec.decodeShort(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF8(value, Endianness.BIG_ENDIAN);
        long valueBE = Codec.decodeLong(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF8(value, Endianness.LITTLE_ENDIAN);
        long valueLE = Codec.decodeLong(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF8(value, Endianness.BIG_ENDIAN);
        float valueBE = Codec.decodeFloat(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF8(value, Endianness.LITTLE_ENDIAN);
        float valueLE = Codec.decodeFloat(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF8(value, Endianness.BIG_ENDIAN);
        double valueBE = Codec.decodeDouble(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF8(value, Endianness.LITTLE_ENDIAN);
        double valueLE = Codec.decodeDouble(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF8(value, Endianness.BIG_ENDIAN);
        boolean valueBE = Codec.decodeBoolean(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF8(value, Endianness.LITTLE_ENDIAN);
        boolean valueLE = Codec.decodeBoolean(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF8(value, Endianness.BIG_ENDIAN);
        char valueBE = Codec.decodeCharUtf8(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF8(value, Endianness.LITTLE_ENDIAN);
        char valueLE = Codec.decodeCharUtf8(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        byte[] valueSerBE = Codec.encodeUTF16(value, Endianness.BIG_ENDIAN);
        char valueBE = Codec.decodeCharUtf16(Endianness.BIG_ENDIAN, valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = Codec.encodeUTF16(value, Endianness.LITTLE_ENDIAN);
        char valueLE = Codec.decodeCharUtf16(Endianness.LITTLE_ENDIAN, valueSerLE);
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
        UnitTest.testFail(() -> Codec.decodeInt(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeShort(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeByte(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeLong(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeFloat(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeDouble(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeBoolean(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeCharUtf8(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeCharUtf16(endianness, buffer), SerializationException.class);

        final byte[] buffer2 = new byte[1];
        buffer2[0] = 127;
        UnitTest.testFail(() -> Codec.decodeInt(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeShort(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeByte(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeLong(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeFloat(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeDouble(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeBoolean(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeCharUtf8(endianness, buffer2), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeCharUtf16(endianness, buffer2), SerializationException.class);

        UnitTest.testFail(() -> Codec.decodeToObjectDataType(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> Codec.decodeToPrimitiveDataType(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> MessageCodec.decodeToObjectDataTypes(endianness, buffer), SerializationException.class);
        UnitTest.testFail(() -> MessageCodec.decodeToPrimitiveDataTypes(endianness, buffer), SerializationException.class);
    }
}
