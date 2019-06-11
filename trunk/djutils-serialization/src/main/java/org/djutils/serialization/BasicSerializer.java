package org.djutils.serialization;

/**
 * Basics of the serializer
 * @param <T> class
 */
public abstract class BasicSerializer<T extends Object> implements Serializer<T>
{
    /** The field type that usually prefixes the serialized data. */
    private final byte type;
    
    /**
     * Construct the BasicSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     */
    public BasicSerializer(final byte type)
    {
        this.type = type;
    }
    
    @Override
    public final byte fieldType()
    {
        return this.type;
    }

}
