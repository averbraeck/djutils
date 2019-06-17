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
 * @param <T> class
 */
public abstract class BasicPrimitiveArraySerializer<T extends Object> extends BasicSerializer<T>
{
    /** Size of one element of the encoded data. */
    private final int dataSize;

    /**
     * Construct a new BasicNonClassArraySerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataSize int; the number of bytes needed to encode one additional array element
     * @param dataClassName String; returned by the dataClassName method
     */
    public BasicPrimitiveArraySerializer(final byte type, final int dataSize, final String dataClassName)
    {
        super(type, dataClassName);
        this.dataSize = dataSize;
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

    /**
     * Retrieve the number of bytes needed to encode one additional array element.
     * @return int; the number of bytes needed to encode one additional array element
     */
    public final int dataSize()
    {
        return this.dataSize;
    }

}
