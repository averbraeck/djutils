package org.djutils.serialization.serializers;

import org.djutils.serialization.FieldTypes;

/**
 * Serializer for simple, fixed size, classes.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @param <T> class to be serialized
 */
public abstract class FixedSizeObjectSerializer<T extends Object> extends ObjectSerializer<T>
{
    /** Size of the encoded data. */
    private final int dataSize;

    /**
     * Construct the FixedSizeObjectSerializer.
     * @param fieldType the field type as defined by the {@link FieldTypes} class
     * @param serializedDataSize number of bytes required for the serialized object
     * @param dataClassName descriptive name of the type (not the class name)
     */
    public FixedSizeObjectSerializer(final byte fieldType, final int serializedDataSize, final String dataClassName)
    {
        super(fieldType, dataClassName);
        this.dataSize = serializedDataSize;
    }

    @Override
    public final int size(final Object object)
    {
        return this.dataSize;
    }

}
