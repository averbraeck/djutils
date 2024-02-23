package org.djutils.serialization.serializers;

import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationException;

/**
 * Serializer for simple classes.
 * <p>
 * Copyright (c) 2019-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <T> class
 */
public abstract class ObjectSerializer<T extends Object> extends BasicSerializer<T>
{
    /**
     * Construct a new ObjectSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName String; returned by the dataClassName method
     */
    public ObjectSerializer(final byte type, final String dataClassName)
    {
        super(type, dataClassName);
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
        return 0;
    }

}
