package org.djutils.serialization.serializers;

import org.djutils.serialization.FieldTypes;

/**
 * Basic functions of the serializer.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
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
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param dataClassName the name of the data type (not the class name)
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
