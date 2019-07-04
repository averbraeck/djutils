package org.djutils.serialization;

/**
 * Serializer for Djunits arrays and matrices.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> Either Double of Float
 */
public abstract class DjunitsArrayOrMatrixSerializer<T extends Object> extends BasicSerializer<T>
{
    /** Number of dimension; 1 for array, 2 for matrix. */
    private final int numberOfDimensions;

    /**
     * Construct a new serializer for Djunits arrays or matrices.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName String; returned by the dataClassName method
     * @param numberOfDimensions int; should be 1 for array serializer and 2 for matrix serializer
     */
    public DjunitsArrayOrMatrixSerializer(final byte type, final String dataClassName, final int numberOfDimensions)
    {
        super(type, dataClassName);
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

    @Override
    public final int getNumberOfDimensions()
    {
        return this.numberOfDimensions;
    }

}
