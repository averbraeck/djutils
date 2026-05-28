package org.djutils.serialization.serializers;

import java.lang.reflect.Array;

import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * ObjectMatrixCodec is responsible for the serialization and deserialization of 2-dimensional matrices of wrapped primitive
 * types, (e.g., <code>Integer[][]</code>. Note that the matrices need to be non-jagged, and non-empty.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 * @param <E> the element type in the 2-dimensional matrix
 */
public abstract class ObjectMatrixCodec<E> extends BasicCodec<E[][]>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /** Element class to instantiate. */
    private final Class<? extends E> elementClass;

    /**
     * Construct a new ObjectMatrixCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param elementSize the number of bytes needed to encode one additional array element
     * @param elementClass the element classs to instantiate
     */
    public ObjectMatrixCodec(final byte type, final int elementSize, final Class<? extends E> elementClass)
    {
        super(type);
        this.elementSize = elementSize;
        this.elementClass = elementClass;
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
        return 2;
    }

    @Override
    public final int size(final E[][] matrix) throws SerializationException
    {
        Throw.when(matrix.length == 0 || matrix[0].length == 0, SerializationException.class, "Zero sized matrix not allowed");
        return 4 + 4 + getElementSize() * matrix.length * matrix[0].length;
    }

    @Override
    public final void serialize(final E[][] matrix, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        int height = matrix.length;
        Throw.when(0 == height, SerializationException.class, "Zero height matrix is not allowed");
        int width = matrix[0].length;
        Throw.when(0 == width, SerializationException.class, "Zero width matrix is not allowed");
        endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
        endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < height; i++)
        {
            Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
            for (int j = 0; j < width; j++)
            {
                serializeElement(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()), endianness);
            }
        }
    }

    @Override
    public final E[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
    {
        int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        E[][] result = (E[][]) Array.newInstance(this.elementClass, height, width);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                result[i][j] = deSerializeElement(buffer, pointer.getAndIncrement(getElementSize()), endianness);
            }
        }
        return result;
    }

    /**
     * Serializer for one array or matrix element (without type prefix) must be implemented in implementing sub classes.
     * @param object the object to serialize
     * @param buffer the byte buffer for the serialized object
     * @param offset index in byte buffer where first serialized byte must be stored
     * @param endianness selects bigEndian or littleEndian encoding
     */
    public abstract void serializeElement(E object, byte[] buffer, int offset, Endianness endianness);

    /**
     * Deserializer for one array or matrix element (without type prefix) must be implemented in implementing sub classes.
     * @param buffer the byte buffer from which the object is to be deserialized
     * @param offset index in byte buffer where first byte of the object is stored
     * @param endianness selects bigEndian or littleEndian encoding
     * @return the deserialized object
     */
    public abstract E deSerializeElement(byte[] buffer, int offset, Endianness endianness);

    /** Converter for Byte matrix. */
    protected static final ObjectMatrixCodec<Byte> BYTE_OBJECT_MATRIX =
            new ObjectMatrixCodec<>(FieldTypes.BYTE_8_MATRIX, 1, Byte.class)
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

    /** Converter for Short matrix. */
    protected static final ObjectMatrixCodec<Short> SHORT_OBJECT_MATRIX =
            new ObjectMatrixCodec<Short>(FieldTypes.SHORT_16_MATRIX, 2, Short.class)
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

    /** Converter for Integer matrix. */
    protected static final ObjectMatrixCodec<Integer> INTEGER_OBJECT_MATRIX =
            new ObjectMatrixCodec<>(FieldTypes.INT_32_MATRIX, 4, Integer.class)
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

    /** Converter for Long matrix. */
    protected static final ObjectMatrixCodec<Long> LONG_OBJECT_MATRIX =
            new ObjectMatrixCodec<>(FieldTypes.LONG_64_MATRIX, 8, Long.class)
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

    /** Converter for Float matrix. */
    protected static final ObjectMatrixCodec<Float> FLOAT_OBJECT_MATRIX =
            new ObjectMatrixCodec<>(FieldTypes.FLOAT_32_MATRIX, 4, Float.class)
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

    /** Converter for Double matrix. */
    protected static final ObjectMatrixCodec<Double> DOUBLE_OBJECT_MATRIX =
            new ObjectMatrixCodec<>(FieldTypes.DOUBLE_64_MATRIX, 8, Double.class)
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

    /** Converter for Boolean matrix. */
    protected static final ObjectMatrixCodec<Boolean> BOOLEAN_OBJECT_MATRIX =
            new ObjectMatrixCodec<>(FieldTypes.BOOLEAN_8_MATRIX, 1, Boolean.class)
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
