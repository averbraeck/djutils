package org.djutils.serialization.codecs;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * PrimitiveArrayCodec is responsible for the serialization and deserialization of 1-dimensional arrays of primitive types,
 * which are offered as the primitive type (e.g., <code>int[]</code>.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 * @param <T> the data type (1-dimensional)
 */
public abstract class PrimitiveArrayCodec<T> extends BasicCodec<T>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /**
     * Construct a new PrimitiveArrayCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param elementSize the number of bytes needed to encode one additional array element
     * @param shortName the short name of the codec
     */
    public PrimitiveArrayCodec(final byte type, final int elementSize, final String shortName)
    {
        super(type, shortName);
        this.elementSize = elementSize;
    }

    /**
     * Retrieve the number of bytes needed to encode one additional array element.
     * @return the number of bytes needed to encode one additional array element
     */
    public final int getElementSize()
    {
        return this.elementSize;
    }

    @Override
    public final int getNumberOfDimensions()
    {
        return 1;
    }

    /** Converter for byte array. */
    public static final PrimitiveArrayCodec<byte[]> BYTE_ARRAY =
            new PrimitiveArrayCodec<>(FieldTypes.BYTE_8_ARRAY, 1, "byte_8_array")
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
                public byte[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for short array. */
    public static final PrimitiveArrayCodec<short[]> SHORT_ARRAY =
            new PrimitiveArrayCodec<>(FieldTypes.SHORT_16_ARRAY, 2, "short_16_array")
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
                public short[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for int array. */
    public static final PrimitiveArrayCodec<int[]> INT_ARRAY =
            new PrimitiveArrayCodec<>(FieldTypes.INT_32_ARRAY, 4, "int_32_array")
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
                public int[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for long array. */
    public static final PrimitiveArrayCodec<long[]> LONG_ARRAY =
            new PrimitiveArrayCodec<>(FieldTypes.LONG_64_ARRAY, 8, "long_64_array")
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
                public long[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for float array. */
    public static final PrimitiveArrayCodec<float[]> FLOAT_ARRAY =
            new PrimitiveArrayCodec<>(FieldTypes.FLOAT_32_ARRAY, 4, "float_32_array")
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
                public float[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for double array. */
    public static final PrimitiveArrayCodec<double[]> DOUBLE_ARRAY =
            new PrimitiveArrayCodec<double[]>(FieldTypes.DOUBLE_64_ARRAY, 8, "double_64_array")
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
                public double[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for boolean array. */
    public static final PrimitiveArrayCodec<boolean[]> BOOLEAN_ARRAY =
            new PrimitiveArrayCodec<>(FieldTypes.BOOLEAN_8_ARRAY, 1, "boolean_8_array")
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
                public boolean[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

}
