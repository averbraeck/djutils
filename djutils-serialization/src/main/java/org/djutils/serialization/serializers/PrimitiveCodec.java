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
 */
public final class PrimitiveCodec
{

    /** Static class, no constructor. */
    private PrimitiveCodec()
    {
        // Static class, no constructor
    }

    /** Converter for Byte. */
    protected static final Serializer<Byte> CONVERT_BYTE = new FixedSizeObjectSerializer<Byte>(FieldTypes.BYTE_8, 1, "Byte_8")
    {
        @Override
        public void serialize(final Byte object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            buffer[pointer.getAndIncrement(1)] = object;
        }

        @Override
        public Byte deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return buffer[pointer.getAndIncrement(1)];
        }
    };

    /** Converter for Short. */
    protected static final Serializer<Short> CONVERT_SHORT =
            new FixedSizeObjectSerializer<Short>(FieldTypes.SHORT_16, 2, "Short_16")
            {
                @Override
                public void serialize(final Short object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeShort(object, buffer, pointer.getAndIncrement(2));
                }

                @Override
                public Short deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeShort(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Integer. */
    protected static final Serializer<Integer> CONVERT_INTEGER =
            new FixedSizeObjectSerializer<Integer>(FieldTypes.INT_32, 4, "Integer_32")
            {
                @Override
                public void serialize(final Integer object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeInt(object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Integer deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Integer. */
    protected static final Serializer<Long> CONVERT_LONG = new FixedSizeObjectSerializer<Long>(FieldTypes.LONG_64, 8, "Long_64")
    {
        @Override
        public void serialize(final Long object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeLong(object, buffer, pointer.getAndIncrement(8));
        }

        @Override
        public Long deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeLong(buffer, pointer.getAndIncrement(8));
        }
    };

    /** Converter for Float. */
    protected static final Serializer<Float> CONVERT_FLOAT =
            new FixedSizeObjectSerializer<Float>(FieldTypes.FLOAT_32, 4, "Float_32")
            {
                @Override
                public void serialize(final Float object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeFloat(object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Float deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Double. */
    protected static final Serializer<Double> CONVERT_DOUBLE =
            new FixedSizeObjectSerializer<Double>(FieldTypes.DOUBLE_64, 8, "Double_64")
            {
                @Override
                public void serialize(final Double object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeDouble(object, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public Double deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            };

    /** Converter for Boolean. */
    protected static final Serializer<Boolean> CONVERT_BOOLEAN =
            new FixedSizeObjectSerializer<Boolean>(FieldTypes.BOOLEAN_8, 1, "Boolean_8")
            {
                @Override
                public void serialize(final Boolean object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    buffer[pointer.getAndIncrement(1)] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return buffer[pointer.getAndIncrement(1)] != 0;
                }
            };

    /** Converter for Character. */
    protected static final Serializer<Character> CONVERT_CHARACTER16 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_16, 2, "Char_16")
            {
                @Override
                public void serialize(final Character object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeChar(object, buffer, pointer.getAndIncrement(size(object)));
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeChar(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Character. */
    protected static final Serializer<Character> CONVERT_CHARACTER8 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_8, 1, "Char_8")
            {
                @Override
                public void serialize(final Character object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    buffer[pointer.getAndIncrement(size(object))] = (byte) (object & 0xFF);
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return Character.valueOf((char) buffer[pointer.getAndIncrement(1)]);
                }
            };

}
