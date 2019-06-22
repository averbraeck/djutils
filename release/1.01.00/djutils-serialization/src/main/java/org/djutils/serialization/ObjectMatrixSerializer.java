package org.djutils.serialization;

import java.lang.reflect.Array;

import org.djutils.exceptions.Throw;

/**
 * Serializer for simple matrix (non-jagged, non-empty, 2D array) classes.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> class
 */
public abstract class ObjectMatrixSerializer<T extends Object> extends BasicSerializer<T[][]>
{
    /** Size of one element of the encoded data. */
    private final int dataSize;

    /** Sample object with required type info (zero length array suffices). */
    private final T sample;

    /**
     * Construct a new ObjectMatrixSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param dataClassName String; returned by the dataClassName method
     * @param sample T[]; sample object (can be zero length array).
     */
    public ObjectMatrixSerializer(final byte type, final int dataSize, final T sample, final String dataClassName)
    {
        super(type, dataClassName);
        this.dataSize = dataSize;
        this.sample = sample;
    }

    @Override
    public final int size(final Object object) throws SerializationException
    {
        @SuppressWarnings("unchecked")
        T[][] matrix = (T[][]) object;
        Throw.when(matrix.length == 0 || matrix[0].length == 0, SerializationException.class, "Zero sized matrix not allowed");
        return 4 + 4 + this.dataSize * matrix.length * matrix[0].length;
    }

    @Override
    public final int sizeWithPrefix(final Object object) throws SerializationException
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final Object object, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianUtil);
    }

    @Override
    public final void serialize(final Object object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        @SuppressWarnings("unchecked")
        T[][] matrix = (T[][]) object;
        int height = matrix.length;
        Throw.when(0 == height, SerializationException.class, "Zero height matrix is not allowed");
        int width = matrix[0].length;
        Throw.when(0 == width, SerializationException.class, "Zero width matrix is not allowed");
        endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
        endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < height; i++)
        {
            Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
            for (int j = 0; j < width; j++)
            {
                serializeElement(matrix[i][j], buffer, pointer.getAndIncrement(this.dataSize), endianUtil);
            }
        }
    }

    @Override
    public final T[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        T[][] result = (T[][]) Array.newInstance(this.sample.getClass(), height, width);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                result[i][j] = deSerializeElement(buffer, pointer.getAndIncrement(this.dataSize), endianUtil);
            }
        }
        return result;
    }

    /**
     * Serializer for one matrix element (without type prefix) must be implemented in implementing sub classes.
     * @param object T; the object to serialize
     * @param buffer byte[]; the byte buffer for the serialized object
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @param offset int; index in byte buffer where first serialized byte must be stored
     */
    abstract void serializeElement(T object, byte[] buffer, int offset, EndianUtil endianUtil);

    /**
     * Deserializer for one matrix element (without type prefix) must be implemented in implementing sub classes.
     * @param buffer byte[]; the byte buffer from which the object is to be deserialized
     * @param offset int; index in byte buffer where first byte of the object is stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @return T; the deserialized object
     */
    abstract T deSerializeElement(byte[] buffer, int offset, EndianUtil endianUtil);

}
