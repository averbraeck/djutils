package org.djutils.serialization.serializers;

/**
 * Basics of the serializer
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <T> class
 */
public abstract class BasicSerializer<T extends Object> implements Serializer<T>
{
    /** The field type that usually prefixes the serialized data. */
    private final byte type;

    /** String returned by the dataClassName method. */
    private final String dataClassName;

    /**
     * Construct the BasicSerializer.
     * @param type the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName returned by the dataClassName method
     */
    public BasicSerializer(final byte type, final String dataClassName)
    {
        this.type = type;
        this.dataClassName = dataClassName;
    }

    @Override
    public final byte fieldType()
    {
        return this.type;
    }

    @Override
    public final String dataClassName()
    {
        return this.dataClassName;
    }

    @Override
    public String toString()
    {
        return "BasicSerializer [type=" + this.type + ", dataClassName=" + this.dataClassName + "]";
    }

}
