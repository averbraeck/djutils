package org.djutils.serialization.serializers;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * PrimitiveArrayCodec is responsible for the serialization and deserialization of 1-dimensional arrays of primitive types,
 * which can be offered as the primitive type (e.g., <code>int[]</code> itself or as the object wrapper (e.g.,
 * <code>Integer[]</code>).
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public final class PrimitiveArrayCodec
{

    /** Static class, no constructor. */
    private PrimitiveArrayCodec()
    {
        // Static class, no constructor
    }

    /** Converter for byte array. */
    protected static final Serializer<byte[]> CONVERT_BT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<byte[]>(FieldTypes.BYTE_8_ARRAY, 1, "byte_8_array", 1)
            {
                @Override
                public int size(final byte[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final byte[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        buffer[pointer.getAndIncrement(getElementSize())] = array[i];
                    }
                }

                @Override
                public byte[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    byte[] result = new byte[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(getElementSize())];
                    }
                    return result;
                }
            };

    /** Converter for Byte array. */
    protected static final Serializer<Byte[]> CONVERT_BYTE_ARRAY =
            new ObjectArraySerializer<Byte>(FieldTypes.BYTE_8_ARRAY, 1, Byte.valueOf((byte) 0), "Byte_8_array")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return buffer[offset];
                }
            };

    /** Converter for short array. */
    protected static final Serializer<short[]> CONVERT_SHRT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<short[]>(FieldTypes.SHORT_16_ARRAY, 2, "short_16_array", 1)
            {
                @Override
                public int size(final short[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final short[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeShort(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public short[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    short[] result = new short[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeShort(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Short array. */
    protected static final Serializer<Short[]> CONVERT_SHORT_ARRAY =
            new ObjectArraySerializer<Short>(FieldTypes.SHORT_16_ARRAY, 2, Short.valueOf((short) 0), "Short_16_array")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeShort(buffer, offset);
                }
            };

    /** Converter for int array. */
    protected static final Serializer<int[]> CONVERT_INT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<int[]>(FieldTypes.INT_32_ARRAY, 4, "int_32_array", 1)
            {
                @Override
                public int size(final int[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final int[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeInt(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public int[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int[] result = new int[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeInt(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Integer array. */
    protected static final Serializer<Integer[]> CONVERT_INTEGER_ARRAY =
            new ObjectArraySerializer<Integer>(FieldTypes.INT_32_ARRAY, 4, Integer.valueOf(0), "Integer_32_array")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeInt(buffer, offset);
                }
            };

    /** Converter for long array. */
    protected static final Serializer<long[]> CONVERT_LNG_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<long[]>(FieldTypes.LONG_64_ARRAY, 8, "long_64_array", 1)
            {
                @Override
                public int size(final long[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final long[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeLong(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public long[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    long[] result = new long[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeLong(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Long array. */
    protected static final Serializer<Long[]> CONVERT_LONG_ARRAY =
            new ObjectArraySerializer<Long>(FieldTypes.LONG_64_ARRAY, 8, Long.valueOf(0), "Long_64_array")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeLong(buffer, offset);
                }
            };

    /** Converter for float array. */
    protected static final Serializer<float[]> CONVERT_FLT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<float[]>(FieldTypes.FLOAT_32_ARRAY, 4, "float_32_array", 1)
            {
                @Override
                public int size(final float[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final float[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeFloat(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public float[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    float[] result = new float[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeFloat(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Float array. */
    protected static final Serializer<Float[]> CONVERT_FLOAT_ARRAY =
            new ObjectArraySerializer<Float>(FieldTypes.FLOAT_32_ARRAY, 4, Float.valueOf(0), "Float_32_array")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeFloat(buffer, offset);
                }
            };

    /** Converter for double array. */
    protected static final Serializer<double[]> CONVERT_DBL_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<double[]>(FieldTypes.DOUBLE_64_ARRAY, 8, "double_64_array", 1)
            {
                @Override
                public int size(final double[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final double[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeDouble(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public double[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    double[] result = new double[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeDouble(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Double array. */
    protected static final Serializer<Double[]> CONVERT_DOUBLE_ARRAY =
            new ObjectArraySerializer<Double>(FieldTypes.DOUBLE_64_ARRAY, 8, Double.valueOf(0), "Double_64_array")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeDouble(buffer, offset);
                }
            };

    /** Converter for boolean array. */
    protected static final Serializer<boolean[]> CONVERT_BOOL_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<boolean[]>(FieldTypes.BOOLEAN_8_ARRAY, 1, "bool_8_array", 1)
            {
                @Override
                public int size(final boolean[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final boolean[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        buffer[pointer.getAndIncrement(getElementSize())] = (byte) (array[i] ? 1 : 0);
                    }
                }

                @Override
                public boolean[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    boolean[] result = new boolean[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(getElementSize())] != 0;
                    }
                    return result;
                }
            };

    /** Converter for Boolean array. */
    protected static final Serializer<Boolean[]> CONVERT_BOOLEAN_ARRAY =
            new ObjectArraySerializer<Boolean>(FieldTypes.BOOLEAN_8_ARRAY, 1, Boolean.FALSE, "Boolean_8_array")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return buffer[offset] != 0;
                }
            };

}
