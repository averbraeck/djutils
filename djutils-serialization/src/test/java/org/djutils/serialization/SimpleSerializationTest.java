package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.djutils.exceptions.Try;
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
     * Test decodeInt method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeInt() throws SerializationException
    {
        int value = 1024;
        byte[] intSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        // System.out.println(IntStream.range(0, intSerBE.length).map(i -> intSerBE[i] >= 0 ? intSerBE[i] : intSerBE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intBE = TypedMessage.decodeInt(intSerBE);
        assertEquals(value, intBE);

        byte[] intSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        // System.out.println(IntStream.range(0, intSerLE.length).map(i -> intSerLE[i] >= 0 ? intSerLE[i] : intSerLE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intLE = TypedMessage.decodeInt(intSerLE);
        assertEquals(value, intLE);

        assertNotEquals(intSerBE, intSerLE);
        assertEquals(intBE, intLE);
    }

    /**
     * Test decodeByte method.
     * @throws SerializationException on error
     */
    @Test
    public void testDecodeByte() throws SerializationException
    {
        byte value = 55;
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        byte valueBE = TypedMessage.decodeByte(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        byte valueLE = TypedMessage.decodeByte(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        short valueBE = TypedMessage.decodeShort(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        short valueLE = TypedMessage.decodeShort(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        long valueBE = TypedMessage.decodeLong(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        long valueLE = TypedMessage.decodeLong(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        float valueBE = TypedMessage.decodeFloat(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        float valueLE = TypedMessage.decodeFloat(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        double valueBE = TypedMessage.decodeDouble(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        double valueLE = TypedMessage.decodeDouble(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        boolean valueBE = TypedMessage.decodeBoolean(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        boolean valueLE = TypedMessage.decodeBoolean(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        char valueBE = TypedMessage.decodeCharUtf8(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        char valueLE = TypedMessage.decodeCharUtf8(valueSerLE);
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
        byte[] valueSerBE = TypedMessage.encodeUTF16(EndianUtil.BIG_ENDIAN, value);
        char valueBE = TypedMessage.decodeCharUtf16(valueSerBE);
        assertEquals(value, valueBE);

        byte[] valueSerLE = TypedMessage.encodeUTF16(EndianUtil.LITTLE_ENDIAN, value);
        char valueLE = TypedMessage.decodeCharUtf16(valueSerLE);
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
        final byte[] buffer = new byte[12];
        buffer[0] = 127;
        Try.testFail(() -> TypedMessage.decodeInt(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeShort(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeByte(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeLong(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeFloat(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeDouble(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeBoolean(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeCharUtf8(buffer), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeCharUtf16(buffer), SerializationException.class);

        final byte[] buffer2 = new byte[1];
        buffer2[0] = 127;
        Try.testFail(() -> TypedMessage.decodeInt(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeShort(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeByte(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeLong(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeFloat(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeDouble(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeBoolean(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeCharUtf8(buffer2), SerializationException.class);
        Try.testFail(() -> TypedMessage.decodeCharUtf16(buffer2), SerializationException.class);
    }
}
