package org.djutils.serialization.serializers;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * Serializer for simple classes.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @param <T> class
 */
public abstract class ObjectSerializer<T extends Object> extends BasicSerializer<T>
{
    /**
     * Construct a new ObjectSerializer.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param dataClassName descriptive name of the type (not the class name)
     */
    public ObjectSerializer(final byte type, final String dataClassName)
    {
        super(type, dataClassName);
    }

    @Override
    public final int sizeWithPrefix(final T object) throws SerializationException
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final T object, final byte[] buffer, final Pointer pointer,
            final Endianness endianness) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianness);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 0;
    }

}
