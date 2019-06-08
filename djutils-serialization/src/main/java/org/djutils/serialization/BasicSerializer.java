package org.djutils.serialization;

/**
 * Serializer for simple (fixed size) classes.
 * @param <T> class
 */
public abstract class BasicSerializer<T extends Object> implements Serializer<T>
{
    /** The field type that is usually prefixes the serialized data. */
    private final byte type;
    
    /** Size of the encoded data. */
    private final int dataSize;
    
    /**
     * Construct the BasicSerializer.
     * @param fieldType byte; the field type (returned by the <code>fieldType</code> method)
     * @param serializedDataSize int; number of bytes required for serialized T
     */
    public BasicSerializer(final byte fieldType, final int serializedDataSize)
    {
        this.type = fieldType;
        this.dataSize = serializedDataSize;
    }
    
    @Override
    public int size(T object)
    {
        return this.dataSize;
    }

    @Override
    public int sizeWithPrefix(T object)
    {
        return 1 + size(object);
    }

    @Override
    public byte fieldType()
    {
        return this.type;
    }

    @Override
    public void serializeWithPrefix(T object, byte[] buffer, Pointer pointer)
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer);
    }

}
