package org.djutils.serialization;

/**
 * Serializer for simple, fixed size, classes.
 * @param <T> class
 */
public abstract class FixedSizeSerializer<T extends Object> extends BasicPlainTypeSerializer<T>
{
    /** Size of the encoded data. */
    private final int dataSize;
    
    /**
     * Construct the FixedSizeSerializer.
     * @param fieldType byte; the field type (returned by the <code>fieldType</code> method)
     * @param serializedDataSize int; number of bytes required for serialized T
     */
    public FixedSizeSerializer(final byte fieldType, final int serializedDataSize)
    {
        super(fieldType);
        this.dataSize = serializedDataSize;
    }
    
    @Override
    public final int size(final Object object)
    {
        return this.dataSize;
    }

}
