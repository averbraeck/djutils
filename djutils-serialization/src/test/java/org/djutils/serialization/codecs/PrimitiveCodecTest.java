package org.djutils.serialization.codecs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.SerializationException;
import org.junit.jupiter.api.Test;

/**
 * PrimitiveCodecTest tests the {@link PrimitiveCodec}.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
@SuppressWarnings("checkstyle:needbraces")
public class PrimitiveCodecTest
{
    /**
     * Test byte codec.
     * @throws SerializationException on error
     */
    @Test
    public void testByteCodec() throws SerializationException
    {
        PrimitiveCodec<Byte> codec = PrimitiveCodec.BYTE;
        assertEquals(0, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Byte_8", codec.getShortName());
        assertFalse(codec.hasUnit());
        byte val = 0x00;
        assertEquals(1, codec.size(val));
        assertEquals(2, codec.sizeWithPrefix(val));

        for (byte b : new byte[] {0x00, 0x01, -0x01})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(b, endianness);
                assertEquals(2, buffer.length);
                assertEquals(0, buffer[0]);
                assertEquals(b, buffer[1]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Byte db)
                    assertEquals(b, db.byteValue());
                else
                    fail("wrong type");
            }
        }

        for (Byte b : new Byte[] {0x00, 0x01, -0x01})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(b, endianness);
                assertEquals(2, buffer.length);
                assertEquals(0, buffer[0]);
                assertEquals(b, buffer[1]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Byte db)
                    assertEquals(b, db);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test short codec.
     * @throws SerializationException on error
     */
    @Test
    public void testShortCodec() throws SerializationException
    {
        PrimitiveCodec<Short> codec = PrimitiveCodec.SHORT;
        assertEquals(1, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Short_16", codec.getShortName());
        assertFalse(codec.hasUnit());
        short val = (short) 0;
        assertEquals(2, codec.size(val));
        assertEquals(3, codec.sizeWithPrefix(val));

        for (short s : new short[] {0x00, 0x01, -0x01})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(s, endianness);
                assertEquals(3, buffer.length);
                assertEquals(1, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Short ds)
                    assertEquals(s, ds.shortValue());
                else
                    fail("wrong type");
            }
        }

        for (Short s : new Short[] {0x00, 0x01, -0x01})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(s, endianness);
                assertEquals(3, buffer.length);
                assertEquals(1, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Short ds)
                    assertEquals(s, ds);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test int codec.
     * @throws SerializationException on error
     */
    @Test
    public void testIntCodec() throws SerializationException
    {
        PrimitiveCodec<Integer> codec = PrimitiveCodec.INTEGER;
        assertEquals(2, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Integer_32", codec.getShortName());
        assertFalse(codec.hasUnit());
        int val = 0;
        assertEquals(4, codec.size(val));
        assertEquals(5, codec.sizeWithPrefix(val));

        for (int i : new int[] {0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(i, endianness);
                assertEquals(5, buffer.length);
                assertEquals(2, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Integer di)
                    assertEquals(i, di.intValue());
                else
                    fail("wrong type");
            }
        }

        for (Integer i : new Integer[] {0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(i, endianness);
                assertEquals(5, buffer.length);
                assertEquals(2, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Integer di)
                    assertEquals(i, di);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test long codec.
     * @throws SerializationException on error
     */
    @Test
    public void testLongCodec() throws SerializationException
    {
        PrimitiveCodec<Long> codec = PrimitiveCodec.LONG;
        assertEquals(3, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Long_64", codec.getShortName());
        assertFalse(codec.hasUnit());
        long val = 0;
        assertEquals(8, codec.size(val));
        assertEquals(9, codec.sizeWithPrefix(val));

        for (long l : new long[] {0, 1, -1, Long.MAX_VALUE, Long.MIN_VALUE})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(l, endianness);
                assertEquals(9, buffer.length);
                assertEquals(3, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Long dl)
                    assertEquals(l, dl.longValue());
                else
                    fail("wrong type");
            }
        }

        for (Long l : new Long[] {0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(l, endianness);
                assertEquals(9, buffer.length);
                assertEquals(3, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Long dl)
                    assertEquals(l, dl);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test float codec.
     * @throws SerializationException on error
     */
    @Test
    public void testFloatCodec() throws SerializationException
    {
        PrimitiveCodec<Float> codec = PrimitiveCodec.FLOAT;
        assertEquals(4, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Float_32", codec.getShortName());
        assertFalse(codec.hasUnit());
        float val = 0.0f;
        assertEquals(4, codec.size(val));
        assertEquals(5, codec.sizeWithPrefix(val));

        for (float f : new float[] {0f, 1f, -1f, 1234.5678f, -0.0f, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL,
                Float.MIN_VALUE, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(f, endianness);
                assertEquals(5, buffer.length);
                assertEquals(4, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Float df)
                    assertEquals(f, df.floatValue());
                else
                    fail("wrong type");
            }
        }

        for (Float f : new Float[] {0f, 1f, -1f, 1234.5678f, -0.0f, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL,
                Float.MIN_VALUE, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(f, endianness);
                assertEquals(5, buffer.length);
                assertEquals(4, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Float df)
                    assertEquals(f, df);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test double codec.
     * @throws SerializationException on error
     */
    @Test
    public void testDoubleCodec() throws SerializationException
    {
        PrimitiveCodec<Double> codec = PrimitiveCodec.DOUBLE;
        assertEquals(5, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Double_64", codec.getShortName());
        assertFalse(codec.hasUnit());
        double val = 0.0d;
        assertEquals(8, codec.size(val));
        assertEquals(9, codec.sizeWithPrefix(val));

        for (double d : new double[] {0, 1, -1, 1234.5678, -0.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL,
                Double.MIN_VALUE, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(d, endianness);
                assertEquals(9, buffer.length);
                assertEquals(5, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Double dd)
                    assertEquals(d, dd.doubleValue());
                else
                    fail("wrong type");
            }
        }

        for (Double d : new Double[] {0d, 1d, -1d, 1234.5678d, -0.0d, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL,
                Double.MIN_VALUE, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(d, endianness);
                assertEquals(9, buffer.length);
                assertEquals(5, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Double dd)
                    assertEquals(d, dd);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test boolean codec.
     * @throws SerializationException on error
     */
    @Test
    public void testBooleanCodec() throws SerializationException
    {
        PrimitiveCodec<Boolean> codec = PrimitiveCodec.BOOLEAN;
        assertEquals(6, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Boolean_8", codec.getShortName());
        assertFalse(codec.hasUnit());
        boolean val = true;
        assertEquals(1, codec.size(val));
        assertEquals(2, codec.sizeWithPrefix(val));

        for (boolean b : new boolean[] {true, false})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(b, endianness);
                assertEquals(2, buffer.length);
                assertEquals(6, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Boolean db)
                    assertEquals(b, db.booleanValue());
                else
                    fail("wrong type");
            }
        }

        for (Boolean d : new Boolean[] {true, false})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encode(d, endianness);
                assertEquals(2, buffer.length);
                assertEquals(6, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Boolean db)
                    assertEquals(d, db);
                else
                    fail("wrong type");
            }
        }
    }

    /**
     * Test char UTF8 codec.
     * @throws SerializationException on error
     */
    @Test
    public void testChar8Codec() throws SerializationException
    {
        PrimitiveCodec<Character> codec = PrimitiveCodec.CHARACTER8;
        assertEquals(7, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Char_8", codec.getShortName());
        assertFalse(codec.hasUnit());
        char val = 'a';
        assertEquals(1, codec.size(val));
        assertEquals(2, codec.sizeWithPrefix(val));

        for (char c : new char[] {'a', ' ', 'Z', '\t', '\n', '}'})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encodeUTF8(c, endianness);
                assertEquals(2, buffer.length);
                assertEquals(7, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Character dc)
                    assertEquals(c, dc.charValue());
                else
                    fail("wrong type");
            }
        }

        for (Character c : new Character[] {'a', ' ', 'Z', '\t', '\n', '}'})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encodeUTF8(c, endianness);
                assertEquals(2, buffer.length);
                assertEquals(7, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Character dc)
                    assertEquals(c, dc);
                else
                    fail("wrong type");
            }
        }

        // try to encode/decode UTF16 characters (should fail)
        char xi = '\u03BE';
        char perMille = '\u2030';
        char smiley = '\u263A';
        for (char c : new char[] {xi, perMille, smiley})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                assertThrows(SerializationException.class, () -> Codec.encodeUTF8(c, endianness));
            }
        }
    }

    /**
     * Test char UTF16 codec.
     * @throws SerializationException on error
     */
    @Test
    public void testChar16Codec() throws SerializationException
    {
        PrimitiveCodec<Character> codec = PrimitiveCodec.CHARACTER16;
        assertEquals(8, codec.fieldType());
        assertEquals(0, codec.getNumberOfDimensions());
        assertEquals("Char_16", codec.getShortName());
        assertFalse(codec.hasUnit());
        char val = 'a';
        assertEquals(2, codec.size(val));
        assertEquals(3, codec.sizeWithPrefix(val));

        char xi = '\u03BE';
        char copyright = '\u00A9';
        char perMille = '\u2030';
        char smiley = '\u263A';

        for (char c : new char[] {'a', ' ', 'Z', '\t', '\n', xi, copyright, perMille, smiley})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encodeUTF16(c, endianness);
                assertEquals(3, buffer.length);
                assertEquals(8, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Character dc)
                    assertEquals(c, dc.charValue());
                else
                    fail("wrong type");
            }
        }

        for (Character c : new Character[] {'a', ' ', 'Z', '\t', '\n', xi, copyright, perMille, smiley})
        {
            for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
            {
                byte[] buffer = Codec.encodeUTF16(c, endianness);
                assertEquals(3, buffer.length);
                assertEquals(8, buffer[0]);
                Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
                if (decoded instanceof Character dc)
                    assertEquals(c, dc);
                else
                    fail("wrong type");
            }
        }
    }

}
