package org.djutils.serialization.serializers;

import java.lang.reflect.Array;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;

/**
 * ObjectArrayCodec is responsible for the serialization and deserialization of 1-dimensional arrays of primitive types, which
 * are offered as the object wrapper (e.g., <code>Integer[]</code>).
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 * @param <E> the element type in the 1-dimensional array
 */
public abstract class ObjectArrayCodec<E> extends BasicCodec<E[]>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /** Element class to instantiate. */
    private final Class<? extends E> elementClass;

    /**
     * Construct a new ObjectArrayCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param elementSize the number of bytes needed to encode one additional array element
     * @param elementClass the element classs to instantiate
     */
    public ObjectArrayCodec(final byte type, final int elementSize, final Class<? extends E> elementClass)
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
        return 1;
    }

    @Override
    public final int size(final E[] array)
    {
        return 4 + getElementSize() * array.length;
    }

    @Override
    public final void serialize(final E[] array, final byte[] buffer, final Pointer pointer, final Endianness endianness)
    {
        endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < array.length; i++)
        {
            serializeElement(array[i], buffer, pointer.getAndIncrement(getElementSize()), endianness);
        }
    }

    @Override
    public final E[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
    {
        int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        E[] result = (E[]) Array.newInstance(this.elementClass, size);
        for (int i = 0; i < size; i++)
        {
            result[i] = deSerializeElement(buffer, pointer.getAndIncrement(getElementSize()), endianness);
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

    /** Converter for Byte array. */
    protected static final ObjectArrayCodec<Byte> BYTE_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.BYTE_8_ARRAY, 1, Byte.class)
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

    /** Converter for Short array. */
    protected static final ObjectArrayCodec<Short> SHORT_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.SHORT_16_ARRAY, 2, Short.class)
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

    /** Converter for Integer array. */
    protected static final ObjectArrayCodec<Integer> INTEGER_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.INT_32_ARRAY, 4, Integer.class)
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

    /** Converter for Long array. */
    protected static final ObjectArrayCodec<Long> LONG_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.LONG_64_ARRAY, 8, Long.class)
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

    /** Converter for Float array. */
    protected static final ObjectArrayCodec<Float> FLOAT_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.FLOAT_32_ARRAY, 4, Float.class)
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

    /** Converter for Double array. */
    protected static final ObjectArrayCodec<Double> DOUBLE_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.DOUBLE_64_ARRAY, 8, Double.class)
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

    /** Converter for Boolean array. */
    protected static final ObjectArrayCodec<Boolean> BOOLEAN_OBJECT_ARRAY =
            new ObjectArrayCodec<>(FieldTypes.BOOLEAN_8_ARRAY, 1, Boolean.class)
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
