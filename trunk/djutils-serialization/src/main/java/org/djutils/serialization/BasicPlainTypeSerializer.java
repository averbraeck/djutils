package org.djutils.serialization;

/**
 * Serializer for simple classes.
 * @param <T> class
 */
public abstract class BasicPlainTypeSerializer<T extends Object> extends BasicSerializer<T>
{
    /**
     * Construct a new BasicPlainTypeSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     */
    public BasicPlainTypeSerializer(byte type)
    {
        super(type);
    }

    @Override
    public final int sizeWithPrefix(final Object object)
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final Object object, final byte[] buffer, final Pointer pointer) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer);
    }

}
