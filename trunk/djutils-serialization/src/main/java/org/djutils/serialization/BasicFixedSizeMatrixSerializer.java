package org.djutils.serialization;

import java.lang.reflect.Array;

import org.djutils.exceptions.Throw;

/**
 * Serializer for simple matrix (non-jagged, non-empty, 2D array) classes.
 * @param <T> class
 */
public abstract class BasicFixedSizeMatrixSerializer<T extends Object> extends BasicSerializer<T[][]>
{
    /** Size of one element of the encoded data. */
    private final int dataSize;

    /** Sample object with required type info (zero length array suffices). */
    private final T[][] sample;

    /**
     * Construct a new BasicFixedSizeMatrixSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param sample T[]; sample object (can be zero length array).
     */
    public BasicFixedSizeMatrixSerializer(final byte type, final int dataSize, final T[][] sample)
    {
        super(type);
        this.dataSize = dataSize;
        this.sample = sample;
    }

    @Override
    public final int size(final Object object)
    {
        @SuppressWarnings("unchecked")
        T[][] matrix = (T[][]) object;
        return 4 + 4 + this.dataSize * matrix.length * matrix[0].length;
    }

    @Override
    public final int sizeWithPrefix(final Object object)
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final Object object, final byte[] buffer, final Pointer pointer)
            throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer);
    }
    
    @Override
    public final void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
    {
        @SuppressWarnings("unchecked")
        T[][] matrix = (T[][]) object;
        int height = matrix.length;
        Throw.when(0 == height, SerializationException.class, "Zero height matrix is not allowed");
        int width = matrix[0].length;
        Throw.when(0 == width, SerializationException.class, "Zero width matrix is not allowed");
        EndianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
        EndianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < height; i++)
        {
            Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
            for (int j = 0; j < width; j++)
            {
                serializeElement(matrix[i][j], buffer, pointer.getAndIncrement(this.dataSize));
            }
        }
    }

    @Override
    public final T[][] deSerialize(byte[] buffer, Pointer pointer)
    {
        int height = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        T[][] result = (T[][]) Array.newInstance(sample.getClass(), height, width);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < height; j++)
            {
                result[i][j] = deSerializeElement(buffer, pointer.getAndIncrement(this.dataSize));
            }
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
