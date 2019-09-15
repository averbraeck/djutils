package org.djutils.serialization;

import java.lang.reflect.Array;

import org.djutils.exceptions.Throw;

/**
 * Serializer for simple matrix (non-jagged, non-empty, 2D array) classes.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <E> class of the single (not matrix or vector) type
 */
public abstract class ObjectMatrixSerializer<E extends Object> extends ArrayOrMatrixSerializer<E[][], E>
{
    /** class of the single object that needs to be serialized. */
    private final Class<E> dataClass;

    /**
     * Construct a new ObjectMatrixSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param dataClass Class&lt;T&gt;; class of the single object that needs to be serialized
     * @param dataClassName String; returned by the dataClassName method
     */
    public ObjectMatrixSerializer(final byte type, final int dataSize, final Class<E> dataClass, final String dataClassName)
    {
        super(type, dataSize, dataClassName, 2);
        this.dataClass = dataClass;
    }

    @Override
    public final int size(final E[][] matrix) throws SerializationException
    {
        Throw.when(matrix.length == 0 || matrix[0].length == 0, SerializationException.class, "Zero sized matrix not allowed");
        return 4 + 4 + getElementSize() * matrix.length * matrix[0].length;
    }

    @Override
    public final int sizeWithPrefix(final E[][] matrix) throws SerializationException
    {
        return 1 + size(matrix);
    }

    @Override
    public final void serializeWithPrefix(final E[][] matrix, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(matrix, buffer, pointer, endianUtil);
    }

    @Override
    public final void serialize(final E[][] matrix, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
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
                serializeElement(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()), endianUtil);
            }
        }
    }

    @Override
    public final E[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        E[][] result = (E[][]) Array.newInstance(this.dataClass, height, width);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                result[i][j] = deSerializeElement(buffer, pointer.getAndIncrement(getElementSize()), endianUtil);
            }
        }
        return result;
    }

}
