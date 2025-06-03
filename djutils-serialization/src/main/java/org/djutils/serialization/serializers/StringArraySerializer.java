package org.djutils.serialization.serializers;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.SerializationException;

/**
 * StringArraySerializercontains the basic methods to (de)serialize a String array.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class StringArraySerializer extends BasicSerializer<String[]>
{
    /**
     * Construct the StringArraySerializer.
     * @param type the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName returned by the dataClassName method
     */
    public StringArraySerializer(final byte type, final String dataClassName)
    {
        super(type, dataClassName);
    }

    @Override
    public final int sizeWithPrefix(final String[] object) throws SerializationException
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final String[] object, final byte[] buffer, final Pointer pointer,
            final Endianness endianness) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = endianness.isBigEndian() ? fieldType() : (byte) (fieldType() + 128);
        serialize(object, buffer, pointer, endianness);
    }

    @Override
    public final int getNumberOfDimensions()
    {
        return 1;
    }

}
