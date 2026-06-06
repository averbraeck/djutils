package org.djutils.serialization.codecs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.SerializationException;
import org.junit.jupiter.api.Test;

/**
 * ArrayCodecTest tests the {@link PrimitiveArrayCodec} and {@link ObjectArrayCodec}.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
@SuppressWarnings("checkstyle:needbraces")
public class ArrayCodecTest
{
    /**
     * Test byte array codec.
     * @throws SerializationException on error
     */
    @Test
    public void testByteArrayCodec() throws SerializationException
    {
        PrimitiveArrayCodec<byte[]> codecArr = PrimitiveArrayCodec.BYTE_ARRAY;
        assertEquals(11, codecArr.fieldType());
        assertEquals(1, codecArr.getNumberOfDimensions());
        assertEquals("byte_8_array", codecArr.getShortName());
        assertFalse(codecArr.hasUnit());
        byte[] valArr = {0x00};
        assertEquals(5, codecArr.size(valArr));
        assertEquals(6, codecArr.sizeWithPrefix(valArr));

        byte[] bArr = new byte[] {0x00, 0x01, -0x01};
        for (var endianness : Endianness.values())
        {
            byte[] buffer = Codec.encode(bArr, endianness);
            assertEquals(8, buffer.length);
            assertEquals(11, buffer[0]);
            Object decoded = Codec.decodeToPrimitiveDataType(endianness, buffer);
            if (decoded instanceof byte[] db)
                assertArrayEquals(bArr, db);
            else
                fail("wrong type " + decoded.getClass());
        }

        ObjectArrayCodec<Byte> codecObj = ObjectArrayCodec.BYTE_OBJECT_ARRAY;
        assertEquals(11, codecObj.fieldType());
        assertEquals(1, codecObj.getNumberOfDimensions());
        assertEquals("Byte_8_array", codecObj.getShortName());
        assertFalse(codecObj.hasUnit());
        Byte[] valObj = {0x00};
        assertEquals(5, codecObj.size(valObj));
        assertEquals(6, codecObj.sizeWithPrefix(valObj));

        Byte[] bObj = new Byte[] {0x00, 0x01, -0x01};
        for (var endianness : Endianness.values())
        {
            byte[] buffer = Codec.encode(bObj, endianness);
            assertEquals(8, buffer.length);
            assertEquals(11, buffer[0]);
            Object decoded = Codec.decodeToObjectDataType(endianness, buffer);
            if (decoded instanceof Byte[] db)
                assertArrayEquals(bObj, db);
            else
                fail("wrong type: " + decoded.getClass());
        }
    }

//    /**
//     * Test short array codec.
//     * @throws SerializationException on error
//     */
//    @Test
//    public void testShortArrayCodec() throws SerializationException
//    {
//        PrimitiveArrayCodec<Short> codec = PrimitiveArrayCodec.SHORT;
//        assertEquals(1, codec.fieldType());
//        assertEquals(1, codec.getNumberOfDimensions());
//        assertEquals("Short_16_array", codec.getShortName());
//        assertFalse(codec.hasUnit());
//        short val = (short) 0;
//        assertEquals(2, codec.size(val));
//        assertEquals(3, codec.sizeWithPrefix(val));
//
//        for (short s : new short[] {0x00, 0x01, -0x01})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(s, endianness);
//                assertEquals(3, buffer.length);
//                assertEquals(1, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Short ds)
//                    assertEquals(s, ds.shortValue());
//                else
//                    fail("wrong type");
//            }
//        }
//
//        for (Short s : new Short[] {0x00, 0x01, -0x01})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(s, endianness);
//                assertEquals(3, buffer.length);
//                assertEquals(1, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Short ds)
//                    assertEquals(s, ds);
//                else
//                    fail("wrong type");
//            }
//        }
//    }
//
//    /**
//     * Test int array codec.
//     * @throws SerializationException on error
//     */
//    @Test
//    public void testIntArrayCodec() throws SerializationException
//    {
//        PrimitiveArrayCodec<Integer> codec = PrimitiveArrayCodec.INTEGER;
//        assertEquals(2, codec.fieldType());
//        assertEquals(1, codec.getNumberOfDimensions());
//        assertEquals("Integer_32_array", codec.getShortName());
//        assertFalse(codec.hasUnit());
//        int val = 0;
//        assertEquals(4, codec.size(val));
//        assertEquals(5, codec.sizeWithPrefix(val));
//
//        for (int i : new int[] {0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(i, endianness);
//                assertEquals(5, buffer.length);
//                assertEquals(2, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Integer di)
//                    assertEquals(i, di.intValue());
//                else
//                    fail("wrong type");
//            }
//        }
//
//        for (Integer i : new Integer[] {0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(i, endianness);
//                assertEquals(5, buffer.length);
//                assertEquals(2, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Integer di)
//                    assertEquals(i, di);
//                else
//                    fail("wrong type");
//            }
//        }
//    }
//
//    /**
//     * Test long array codec.
//     * @throws SerializationException on error
//     */
//    @Test
//    public void testLongArrayCodec() throws SerializationException
//    {
//        PrimitiveArrayCodec<Long> codec = PrimitiveArrayCodec.LONG;
//        assertEquals(3, codec.fieldType());
//        assertEquals(1, codec.getNumberOfDimensions());
//        assertEquals("Long_64_array", codec.getShortName());
//        assertFalse(codec.hasUnit());
//        long val = 0;
//        assertEquals(8, codec.size(val));
//        assertEquals(9, codec.sizeWithPrefix(val));
//
//        for (long l : new long[] {0, 1, -1, Long.MAX_VALUE, Long.MIN_VALUE})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(l, endianness);
//                assertEquals(9, buffer.length);
//                assertEquals(3, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Long dl)
//                    assertEquals(l, dl.longValue());
//                else
//                    fail("wrong type");
//            }
//        }
//
//        for (Long l : new Long[] {0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(l, endianness);
//                assertEquals(9, buffer.length);
//                assertEquals(3, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Long dl)
//                    assertEquals(l, dl);
//                else
//                    fail("wrong type");
//            }
//        }
//    }
//
//    /**
//     * Test float array codec.
//     * @throws SerializationException on error
//     */
//    @Test
//    public void testFloatArrayCodec() throws SerializationException
//    {
//        PrimitiveArrayCodec<Float> codec = PrimitiveArrayCodec.FLOAT;
//        assertEquals(4, codec.fieldType());
//        assertEquals(1, codec.getNumberOfDimensions());
//        assertEquals("Float_32_array", codec.getShortName());
//        assertFalse(codec.hasUnit());
//        float val = 0.0f;
//        assertEquals(4, codec.size(val));
//        assertEquals(5, codec.sizeWithPrefix(val));
//
//        for (float f : new float[] {0f, 1f, -1f, 1234.5678f, -0.0f, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL,
//                Float.MIN_VALUE, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(f, endianness);
//                assertEquals(5, buffer.length);
//                assertEquals(4, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Float df)
//                    assertEquals(f, df.floatValue());
//                else
//                    fail("wrong type");
//            }
//        }
//
//        for (Float f : new Float[] {0f, 1f, -1f, 1234.5678f, -0.0f, Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL,
//                Float.MIN_VALUE, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(f, endianness);
//                assertEquals(5, buffer.length);
//                assertEquals(4, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Float df)
//                    assertEquals(f, df);
//                else
//                    fail("wrong type");
//            }
//        }
//    }
//
//    /**
//     * Test double array codec.
//     * @throws SerializationException on error
//     */
//    @Test
//    public void testDoubleArrayCodec() throws SerializationException
//    {
//        PrimitiveArrayCodec<Double> codec = PrimitiveArrayCodec.DOUBLE;
//        assertEquals(5, codec.fieldType());
//        assertEquals(1, codec.getNumberOfDimensions());
//        assertEquals("Double_64_array", codec.getShortName());
//        assertFalse(codec.hasUnit());
//        double val = 0.0d;
//        assertEquals(8, codec.size(val));
//        assertEquals(9, codec.sizeWithPrefix(val));
//
//        for (double d : new double[] {0, 1, -1, 1234.5678, -0.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL,
//                Double.MIN_VALUE, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(d, endianness);
//                assertEquals(9, buffer.length);
//                assertEquals(5, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Double dd)
//                    assertEquals(d, dd.doubleValue());
//                else
//                    fail("wrong type");
//            }
//        }
//
//        for (Double d : new Double[] {0d, 1d, -1d, 1234.5678d, -0.0d, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL,
//                Double.MIN_VALUE, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(d, endianness);
//                assertEquals(9, buffer.length);
//                assertEquals(5, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Double dd)
//                    assertEquals(d, dd);
//                else
//                    fail("wrong type");
//            }
//        }
//    }
//
//    /**
//     * Test boolean array codec.
//     * @throws SerializationException on error
//     */
//    @Test
//    public void testBooleanArrayCodec() throws SerializationException
//    {
//        PrimitiveArrayCodec<Boolean> codec = PrimitiveArrayCodec.BOOLEAN;
//        assertEquals(6, codec.fieldType());
//        assertEquals(1, codec.getNumberOfDimensions());
//        assertEquals("Boolean_8_array", codec.getShortName());
//        assertFalse(codec.hasUnit());
//        boolean val = true;
//        assertEquals(1, codec.size(val));
//        assertEquals(2, codec.sizeWithPrefix(val));
//
//        for (boolean b : new boolean[] {true, false})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(b, endianness);
//                assertEquals(2, buffer.length);
//                assertEquals(6, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Boolean db)
//                    assertEquals(b, db.booleanValue());
//                else
//                    fail("wrong type");
//            }
//        }
//
//        for (Boolean d : new Boolean[] {true, false})
//        {
//            for (var endianness : Endianness.values())
//            {
//                byte[] buffer = Codec.encode(d, endianness);
//                assertEquals(2, buffer.length);
//                assertEquals(6, buffer[0]);
//                Object decoded = Codec.decodeToPrimitiveDataTypes(endianness, buffer);
//                if (decoded instanceof Boolean db)
//                    assertEquals(d, db);
//                else
//                    fail("wrong type");
//            }
//        }
//    }

}
