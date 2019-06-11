package org.djutils.serialization;

import java.lang.reflect.Array;

/**
* Serializer for simple array classes.
* @param <T> class
*/
public abstract class BasicFixedSizeArraySerializer<T extends Object> extends BasicSerializer<T[]> 
{
    /** Size of one element of the encoded data. */
    private final int dataSize;
    
    /** Sample object with required type info (zero length array suffices). */
    private final T[] sample;
    
    /**
     * Construct a new BasicFixedSizeArraySerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param sample T[]; sample object (can be zero length array).
     */
    public BasicFixedSizeArraySerializer(final byte type, final int dataSize, final T[] sample)
    {
        super(type);
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
    public final void serializeWithPrefix(final Object object, final byte[] buffer, final Pointer pointer)
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) object;
        serialize(array, buffer, pointer);
    }
    
    @Override
    public final void serialize(Object object, byte[] buffer, Pointer pointer)
    {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) object;
        EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < array.length; i++)
        {
             serializeElement(array[i], buffer, pointer.getAndIncrement(this.dataSize));
        }
    }

    @Override
    public final T[] deSerialize(byte[] buffer, Pointer pointer)
    {
        int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(sample.getClass(),  size);
        for (int i = 0; i < size; i++)
        {
            result[i] = deSerializeElement(buffer, pointer.getAndIncrement(this.dataSize));
        }
        return result;
    }

    /**
     * Serializer for one element (without type prefix) must be implemented in implementing sub classes.
     * @param object T; the object to serialize
     * @param buffer byte[]; the byte buffer for the serialized object
     * @param offset int; index in byte buffer where first serialized byte must be stored
     */
    abstract void serializeElement(T object, byte[] buffer, int offset);
    
    /**
     * Deserializer for one element (without type prefix) must be implemented in implementing sub classes.
     * @param buffer byte[]; the byte buffer from which the object is to be deserialized
     * @param offset int; index in byte buffer where first byte of the object is stored
     * @return T; the deserialized object
     */
    abstract T deSerializeElement(byte[] buffer, int offset);
    
}
