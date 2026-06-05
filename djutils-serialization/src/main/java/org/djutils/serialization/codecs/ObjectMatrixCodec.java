package org.djutils.serialization.codecs;

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
     * @param shortName the short name of the codec
     */
    public ObjectMatrixCodec(final byte type, final int elementSize, final Class<? extends E> elementClass,
            final String shortName)
    {
        super(type, shortName);
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
        int height = matrix.length;
        Throw.when(0 == height, SerializationException.class, "Zero height matrix is not allowed");
        int width = matrix[0].length;
        Throw.when(0 == width, SerializationException.class, "Zero width matrix is not allowed");
        for (int i = 0; i < height; i++)
        {
            Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
        }
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
    public static final ByteMatrixCodec BYTE_OBJECT_MATRIX = new ByteMatrixCodec();

    /** Converter class for Byte matrix. */
    public static final class ByteMatrixCodec extends ObjectMatrixCodec<Byte>
    {
        /** Construct the ByteMatrixCodec. */
        public ByteMatrixCodec()
        {
            super(FieldTypes.BYTE_8_MATRIX, 1, Byte.class, "Byte_8_matrix");
        }

        @Override
        public void serializeElement(final Byte object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            buffer[offset] = object;
        }

        @Override
        public Byte deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return buffer[offset];
        }
    }

    /** Converter for Short matrix. */
    public static final ShortMatrixCodec SHORT_OBJECT_MATRIX = new ShortMatrixCodec();

    /** Converter class for Short matrix. */
    public static final class ShortMatrixCodec extends ObjectMatrixCodec<Short>
    {
        /** Construct the ShortMatrixCodec. */
        public ShortMatrixCodec()
        {
            super(FieldTypes.SHORT_16_MATRIX, 2, Short.class, "Short_16_matrix");
        }

        @Override
        public void serializeElement(final Short object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            endianness.encodeShort(object, buffer, offset);
        }

        @Override
        public Short deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return endianness.decodeShort(buffer, offset);
        }
    }

    /** Converter for Integer matrix. */
    public static final IntegerMatrixCodec INTEGER_OBJECT_MATRIX = new IntegerMatrixCodec();

    /** Converter class for Integer matrix. */
    public static final class IntegerMatrixCodec extends ObjectMatrixCodec<Integer>
    {
        /** Construct the IntegerMatrixCodec. */
        public IntegerMatrixCodec()
        {
            super(FieldTypes.INT_32_MATRIX, 4, Integer.class, "Integer_32_matrix");
        }

        @Override
        public void serializeElement(final Integer object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            endianness.encodeInt(object, buffer, offset);
        }

        @Override
        public Integer deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return endianness.decodeInt(buffer, offset);
        }
    }

    /** Converter for Long matrix. */
    public static final LongMatrixCodec LONG_OBJECT_MATRIX = new LongMatrixCodec();

    /** Converter class for Long matrix. */
    public static final class LongMatrixCodec extends ObjectMatrixCodec<Long>
    {
        /** Construct the LongMatrixCodec. */
        public LongMatrixCodec()
        {
            super(FieldTypes.LONG_64_MATRIX, 8, Long.class, "Long_64_matrix");
        }

        @Override
        public void serializeElement(final Long object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            endianness.encodeLong(object, buffer, offset);
        }

        @Override
        public Long deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return endianness.decodeLong(buffer, offset);
        }
    }

    /** Converter for Float matrix. */
    public static final FloatMatrixCodec FLOAT_OBJECT_MATRIX = new FloatMatrixCodec();

    /** Converter class for Float matrix. */
    public static final class FloatMatrixCodec extends ObjectMatrixCodec<Float>
    {
        /** Construct the FloatMatrixCodec. */
        public FloatMatrixCodec()
        {
            super(FieldTypes.FLOAT_32_MATRIX, 4, Float.class, "Float_32_matrix");
        }

        @Override
        public void serializeElement(final Float object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            endianness.encodeFloat(object, buffer, offset);
        }

        @Override
        public Float deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return endianness.decodeFloat(buffer, offset);
        }
    }

    /** Converter for Double matrix. */
    public static final DoubleMatrixCodec DOUBLE_OBJECT_MATRIX = new DoubleMatrixCodec();

    /** Converter class for Double matrix. */
    public static final class DoubleMatrixCodec extends ObjectMatrixCodec<Double>
    {
        /** Construct the DoubleMatrixCodec. */
        public DoubleMatrixCodec()
        {
            super(FieldTypes.DOUBLE_64_MATRIX, 8, Double.class, "Double_64_matrix");
        }

        @Override
        public void serializeElement(final Double object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            endianness.encodeDouble(object, buffer, offset);
        }

        @Override
        public Double deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return endianness.decodeDouble(buffer, offset);
        }
    }

    /** Converter for Boolean matrix. */
    public static final BooleanMatrixCodec BOOLEAN_OBJECT_MATRIX = new BooleanMatrixCodec();

    /** Converter class for Boolean matrix. */
    public static final class BooleanMatrixCodec extends ObjectMatrixCodec<Boolean>
    {
        /** Construct the BooleanMatrixCodec. */
        public BooleanMatrixCodec()
        {
            super(FieldTypes.BOOLEAN_8_MATRIX, 1, Boolean.class, "Boolean_8_matrix");
        }

        @Override
        public void serializeElement(final Boolean object, final byte[] buffer, final int offset, final Endianness endianness)
        {
            buffer[offset] = (byte) (object ? 1 : 0);
        }

        @Override
        public Boolean deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
        {
            return buffer[offset] != 0;
        }
    }

}
