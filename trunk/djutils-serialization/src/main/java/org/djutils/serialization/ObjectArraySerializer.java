package org.djutils.serialization;

import java.lang.reflect.Array;

/**
 * Serializer for Object array classes. *
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> class of the object version
 */
public abstract class ObjectArraySerializer<T extends Object> extends ArrayOrMatrixSerializer<T[], T>
{
    /** Sample object with required type info (zero length array suffices). */
    private final T sample;

    /**
     * Construct a new ObjectArraySerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param sample T[]; sample object (can be zero length array).
     * @param dataClassName String; returned by the dataClassName method
     */
    public ObjectArraySerializer(final byte type, final int dataSize, final T sample, final String dataClassName)
    {
        super(type, dataSize, dataClassName, 1);
        this.sample = sample;
    }

    @Override
    public final int size(final Object object)
    {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) object;
        return 4 + getElementSize() * array.length;
    }

    @Override
    public final int sizeWithPrefix(final Object object)
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final Object object, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil)
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianUtil);
    }

    @Override
    public final void serialize(final Object object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) object;
        endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < array.length; i++)
        {
            serializeElement(array[i], buffer, pointer.getAndIncrement(getElementSize()), endianUtil);
        }
    }

    @Override
    public final T[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(this.sample.getClass(), size);
        for (int i = 0; i < size; i++)
        {
            result[i] = deSerializeElement(buffer, pointer.getAndIncrement(getElementSize()), endianUtil);
        }
        return result;
    }
    
}
