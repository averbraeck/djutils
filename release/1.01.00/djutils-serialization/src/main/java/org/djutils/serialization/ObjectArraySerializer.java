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
public abstract class ObjectArraySerializer<T extends Object> extends BasicSerializer<T[]>
{
    /** Size of one element of the encoded data. */
    private final int dataSize;

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
        super(type, dataClassName);
        this.dataSize = dataSize;
        this.sample = sample;
    }

    @Override
    public final int size(final Object object)
    {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) object;
        return 4 + this.dataSize * array.length;
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
            serializeElement(array[i], buffer, pointer.getAndIncrement(this.dataSize), endianUtil);
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
            result[i] = deSerializeElement(buffer, pointer.getAndIncrement(this.dataSize), endianUtil);
        }
        return result;
    }

    /**
     * Serializer for one array element (without type prefix) must be implemented in implementing sub classes.
     * @param object T; the object to serialize
     * @param buffer byte[]; the byte buffer for the serialized object
     * @param offset int; index in byte buffer where first serialized byte must be stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     */
    abstract void serializeElement(T object, byte[] buffer, int offset, EndianUtil endianUtil);

    /**
     * Deserializer for one array element (without type prefix) must be implemented in implementing sub classes.
     * @param buffer byte[]; the byte buffer from which the object is to be deserialized
     * @param offset int; index in byte buffer where first byte of the object is stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @return T; the deserialized object
     */
    abstract T deSerializeElement(byte[] buffer, int offset, EndianUtil endianUtil);

}
