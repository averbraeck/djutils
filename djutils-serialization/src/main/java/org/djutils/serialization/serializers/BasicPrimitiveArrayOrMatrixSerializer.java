package org.djutils.serialization.serializers;

import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationException;

/**
 * Serializer for primitive data array classes. *
 * <p>
 * Copyright (c) 2019-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <T> array type, e.g. int[]
 */
public abstract class BasicPrimitiveArrayOrMatrixSerializer<T extends Object> extends BasicSerializer<T>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /** Number of dimensions of the data. */
    private final int numberOfDimensions;

    /**
     * Construct a new BasicPrimitiveArrayOrMatrixSerializer.
     * @param type the field type (returned by the <code>fieldType</code> method)
     * @param elementSize the number of bytes needed to encode one additional array element
     * @param dataClassName returned by the dataClassName method
     * @param numberOfDimensions number of dimensions (1 for array, 2 for matrix)
     */
    public BasicPrimitiveArrayOrMatrixSerializer(final byte type, final int elementSize, final String dataClassName,
            final int numberOfDimensions)
    {
        super(type, dataClassName);
        this.elementSize = elementSize;
        this.numberOfDimensions = numberOfDimensions;
    }

    @Override
    public final int sizeWithPrefix(final T object) throws SerializationException
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final T object, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianUtil);
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
        return this.numberOfDimensions;
    }

}
