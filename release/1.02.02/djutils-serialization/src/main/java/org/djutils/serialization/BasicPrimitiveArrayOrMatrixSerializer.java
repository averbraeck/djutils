package org.djutils.serialization;

/**
 * Serializer for primitive data array classes. *
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> array type, e.g. int[]
 */
public abstract class BasicPrimitiveArrayOrMatrixSerializer<T extends Object> extends BasicSerializer<T>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /** Number of dimensions of the data. */
    private final int numberOfDimensions;

    /**
     * Construct a new BasicPrimitiveArrayOrMatrixSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param elementSize int; the number of bytes needed to encode one additional array element
     * @param dataClassName String; returned by the dataClassName method
     * @param numberOfDimensions int; number of dimensions (1 for array, 2 for matrix)
     */
    public BasicPrimitiveArrayOrMatrixSerializer(final byte type, final int elementSize, final String dataClassName,
            final int numberOfDimensions)
    {
        super(type, dataClassName);
        this.elementSize = elementSize;
        this.numberOfDimensions = numberOfDimensions;
    }

    @Override
    public final int sizeWithPrefix(final T object) throws SerializationException
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final T object, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianUtil);
    }

    /**
     * Retrieve the number of bytes needed to encode one additional array element.
     * @return int; the number of bytes needed to encode one additional array element
     */
    public final int getElementSize()
    {
        return this.elementSize;
    }

    @Override
    public final int getNumberOfDimensions()
    {
        return this.numberOfDimensions;
    }

}
