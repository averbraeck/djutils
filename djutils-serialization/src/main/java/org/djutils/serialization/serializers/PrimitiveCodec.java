package org.djutils.serialization.serializers;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;

/**
 * PrimitiveCodec is responsible for the serialization and deserialization of primitive types, which can be offered as the
 * primitive type (e.g., <code>int</code> itself or as the object wrapper (e.g., <code>Integer</code>).
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @param <T> the primitive type (as object class)
 */
public abstract class PrimitiveCodec<T> extends BasicCodec<T>
{
    /** Size of the encoded data. */
    private final int dataSize;

    /**
     * Construct a PrimitiveCodec.
     * @param fieldType the field type as defined by the {@link FieldTypes} class
     * @param serializedDataSize number of bytes required for the serialized object
     */
    public PrimitiveCodec(final byte fieldType, final int serializedDataSize)
    {
        super(fieldType);
        this.dataSize = serializedDataSize;
    }

    @Override
    public final int size(final Object object)
    {
        return this.dataSize;
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 0;
    }

    /** Converter for Byte. */
    public static final PrimitiveCodec<Byte> BYTE = new PrimitiveCodec<>(FieldTypes.BYTE_8, 1)
    {
        @Override
        public void serialize(final Byte object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            buffer[pointer.getAndIncrement(1)] = object;
        }

        @Override
        public Byte deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return buffer[pointer.getAndIncrement(1)];
        }
    };

    /** Converter for Short. */
    public static final PrimitiveCodec<Short> SHORT = new PrimitiveCodec<>(FieldTypes.SHORT_16, 2)
    {
        @Override
        public void serialize(final Short object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeShort(object, buffer, pointer.getAndIncrement(2));
        }

        @Override
        public Short deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeShort(buffer, pointer.getAndIncrement(2));
        }
    };

    /** Converter for Integer. */
    public static final PrimitiveCodec<Integer> INTEGER = new PrimitiveCodec<>(FieldTypes.INT_32, 4)
    {
        @Override
        public void serialize(final Integer object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeInt(object, buffer, pointer.getAndIncrement(4));
        }

        @Override
        public Integer deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        }
    };

    /** Converter for Integer. */
    public static final PrimitiveCodec<Long> LONG = new PrimitiveCodec<>(FieldTypes.LONG_64, 8)
    {
        @Override
        public void serialize(final Long object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeLong(object, buffer, pointer.getAndIncrement(8));
        }

        @Override
        public Long deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeLong(buffer, pointer.getAndIncrement(8));
        }
    };

    /** Converter for Float. */
    public static final PrimitiveCodec<Float> FLOAT = new PrimitiveCodec<>(FieldTypes.FLOAT_32, 4)
    {
        @Override
        public void serialize(final Float object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeFloat(object, buffer, pointer.getAndIncrement(4));
        }

        @Override
        public Float deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
        }
    };

    /** Converter for Double. */
    public static final PrimitiveCodec<Double> DOUBLE = new PrimitiveCodec<>(FieldTypes.DOUBLE_64, 8)
    {
        @Override
        public void serialize(final Double object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeDouble(object, buffer, pointer.getAndIncrement(8));
        }

        @Override
        public Double deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
        }
    };

    /** Converter for Boolean. */
    public static final PrimitiveCodec<Boolean> BOOLEAN = new PrimitiveCodec<>(FieldTypes.BOOLEAN_8, 1)
    {
        @Override
        public void serialize(final Boolean object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            buffer[pointer.getAndIncrement(1)] = (byte) (object ? 1 : 0);
        }

        @Override
        public Boolean deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return buffer[pointer.getAndIncrement(1)] != 0;
        }
    };

    /** Converter for Character. */
    public static final PrimitiveCodec<Character> CHARACTER16 = new PrimitiveCodec<>(FieldTypes.CHAR_16, 2)
    {
        @Override
        public void serialize(final Character object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeChar(object, buffer, pointer.getAndIncrement(size(object)));
        }

        @Override
        public Character deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeChar(buffer, pointer.getAndIncrement(2));
        }
    };

    /** Converter for Character. */
    public static final PrimitiveCodec<Character> CHARACTER8 = new PrimitiveCodec<>(FieldTypes.CHAR_8, 1)
    {
        @Override
        public void serialize(final Character object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            buffer[pointer.getAndIncrement(size(object))] = (byte) (object & 0xFF);
        }

        @Override
        public Character deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return Character.valueOf((char) buffer[pointer.getAndIncrement(1)]);
        }
    };

}
