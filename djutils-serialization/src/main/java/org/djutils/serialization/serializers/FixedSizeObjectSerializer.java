package org.djutils.serialization.serializers;

/**
 * Serializer for simple, fixed size, classes.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> class
 */
public abstract class FixedSizeObjectSerializer<T extends Object> extends ObjectSerializer<T>
{
    /** Size of the encoded data. */
    private final int dataSize;

    /**
     * Construct the FixedSizeSerializer.
     * @param fieldType byte; the field type (returned by the <code>fieldType</code> method)
     * @param serializedDataSize int; number of bytes required for serialized T
     * @param dataClassName String; returned by the dataClassName method
     */
    public FixedSizeObjectSerializer(final byte fieldType, final int serializedDataSize, final String dataClassName)
    {
        super(fieldType, dataClassName);
        this.dataSize = serializedDataSize;
    }

    @Override
    public final int size(final Object object)
    {
        return this.dataSize;
    }

}
