package org.djutils.serialization.serializers;

import java.lang.reflect.Array;

import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationException;

/**
 * Serializer for Object array classes.
 * <p>
 * Copyright (c) 2019-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <E> class of the element object
 */
public abstract class ObjectArraySerializer<E extends Object> extends ArrayOrMatrixSerializer<E[], E>
{
    /** Sample object with required type info (zero length array suffices). */
    private final E sample;

    /**
     * Construct a new ObjectArraySerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param sample E; sample object (can be zero length array).
     * @param dataClassName String; returned by the dataClassName method
     */
    public ObjectArraySerializer(final byte type, final int dataSize, final E sample, final String dataClassName)
    {
        super(type, dataSize, dataClassName, 1);
        this.sample = sample;
    }

    @Override
    public final int size(final E[] array)
    {
        return 4 + getElementSize() * array.length;
    }

    @Override
    public final int sizeWithPrefix(final E[] array)
    {
        return 1 + size(array);
    }

    @Override
    public final void serializeWithPrefix(final E[] array, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(array, buffer, pointer, endianUtil);
    }

    @Override
    public final void serialize(final E[] array, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < array.length; i++)
        {
            serializeElement(array[i], buffer, pointer.getAndIncrement(getElementSize()), endianUtil);
        }
    }

    @Override
    public final E[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        E[] result = (E[]) Array.newInstance(this.sample.getClass(), size);
        for (int i = 0; i < size; i++)
        {
            result[i] = deSerializeElement(buffer, pointer.getAndIncrement(getElementSize()), endianUtil);
        }
        return result;
    }

}
