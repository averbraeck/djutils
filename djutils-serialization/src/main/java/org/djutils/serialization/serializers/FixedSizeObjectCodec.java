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
public abstract class FixedSizeObjectCodec<T extends Object> extends BasicCodec<T>
{
    /** Size of the encoded data. */
    private final int dataSize;

    /**
     * Construct the FixedSizeObjectCodec.
     * @param fieldType the field type as defined by the {@link FieldTypes} class
     * @param serializedDataSize number of bytes required for the serialized object
     */
    public FixedSizeObjectCodec(final byte fieldType, final int serializedDataSize)
    {
        super(fieldType);
        this.dataSize = serializedDataSize;
    }

    @Override
    public final int size(final Object object)
    {
        return this.dataSize;
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfDimensions()
    {
        return 0;
    }

}
