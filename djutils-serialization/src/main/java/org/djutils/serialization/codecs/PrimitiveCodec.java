package org.djutils.serialization.codecs;

import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

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
     * @param shortName the short name of the codec
     */
    public PrimitiveCodec(final byte fieldType, final int serializedDataSize, final String shortName)
    {
        super(fieldType, shortName);
        this.dataSize = serializedDataSize;
    }

    @Override
    public final int size(final T object)
    {
        return this.dataSize;
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 0;
    }

    /** Converter for Byte. */
    public static final ByteCodec BYTE = new ByteCodec();

    /** Converter class for Byte. */
    public static final class ByteCodec extends PrimitiveCodec<Byte>
    {
        /** Construct the ByteCodec. */
        public ByteCodec()
        {
            super(FieldTypes.BYTE_8, 1, "Byte_8");
        }

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
    public static final ShortCodec SHORT = new ShortCodec();

    /** Converter class for Short. */
    public static final class ShortCodec extends PrimitiveCodec<Short>
    {
        /** Construct the ShortCodec. */
        public ShortCodec()
        {
            super(FieldTypes.SHORT_16, 2, "Short_16");
        }

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
    public static final IntegerCodec INTEGER = new IntegerCodec();

    /** Converter class for Integer. */
    public static final class IntegerCodec extends PrimitiveCodec<Integer>
    {
        /** Construct the IntegerCodec. */
        public IntegerCodec()
        {
            super(FieldTypes.INT_32, 4, "Integer_32");
        }

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

    /** Converter for Long. */
    public static final LongCodec LONG = new LongCodec();

    /** Converter class for Long. */
    public static final class LongCodec extends PrimitiveCodec<Long>
    {
        /** Construct the LongCodec. */
        public LongCodec()
        {
            super(FieldTypes.LONG_64, 8, "Long_64");
        }

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
    public static final FloatCodec FLOAT = new FloatCodec();

    /** Converter class for Float. */
    public static final class FloatCodec extends PrimitiveCodec<Float>
    {
        /** Construct the FloatCodec. */
        public FloatCodec()
        {
            super(FieldTypes.FLOAT_32, 4, "Float_32");
        }

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
    public static final DoubleCodec DOUBLE = new DoubleCodec();

    /** Converter class for Double. */
    public static final class DoubleCodec extends PrimitiveCodec<Double>
    {
        /** Construct the DoubleCodec. */
        public DoubleCodec()
        {
            super(FieldTypes.DOUBLE_64, 8, "Double_64");
        }

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
    public static final BooleanCodec BOOLEAN = new BooleanCodec();

    /** Converter class for Boolean. */
    public static final class BooleanCodec extends PrimitiveCodec<Boolean>
    {
        /** Construct the BooleanCodec. */
        public BooleanCodec()
        {
            super(FieldTypes.BOOLEAN_8, 1, "Boolean_8");
        }

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

    /** Converter for UTF-8 Character. */
    public static final Character8Codec CHARACTER8 = new Character8Codec();

    /** Converter class for UTF-8 Character. */
    public static final class Character8Codec extends PrimitiveCodec<Character>
    {
        /** Construct the Character8Codec. */
        public Character8Codec()
        {
            super(FieldTypes.CHAR_8, 1, "Char_8");
        }

        @Override
        public void serialize(final Character object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            Throw.when(object > 0xFF, SerializationException.class, "Character '%c' (U+%04X) exceeds 8-bit range", object,
                    (int) object);
            buffer[pointer.getAndIncrement(size(object))] = (byte) (object & 0xFF);
        }

        @Override
        public Character deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return Character.valueOf((char) buffer[pointer.getAndIncrement(1)]);
        }
    };

    /** Converter for UTF-16 Character. */
    public static final Character16Codec CHARACTER16 = new Character16Codec();

    /** Converter class for UTF-16 Character. */
    public static final class Character16Codec extends PrimitiveCodec<Character>
    {
        /** Construct the Character16Codec. */
        public Character16Codec()
        {
            super(FieldTypes.CHAR_16, 2, "Char_16");
        }

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

}
