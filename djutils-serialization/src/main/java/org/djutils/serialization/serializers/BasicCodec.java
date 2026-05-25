package org.djutils.serialization.serializers;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * Basic functions of the serializer/deserializer.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @param <T> class
 */
public abstract class BasicCodec<T>
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
    public BasicCodec(final byte type, final String dataClassName)
    {
        this.type = type;
        this.dataClassName = dataClassName;
    }

    /**
     * Compute the number of bytes needed to serialize an object of type T (excluding the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object Instance of the object
     * @return the number of bytes needed to serialize an object of type T
     * @throws SerializationException when the <code>object</code> cannot be serialized
     */
    public abstract int size(T object) throws SerializationException;

    /**
     * Compute the number of bytes needed to serialize an object of type T (including the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object Instance of the object
     * @return the number of bytes needed to serialize an object of type T
     * @throws SerializationException when the <code>object</code> cannot be serialized
     */
    public int sizeWithPrefix(final T object) throws SerializationException
    {
        return 1 + size(object);
    }

    /**
     * Return the byte representation of the field type.
     * @return byte representing the encoded field type
     */
    public final byte fieldType()
    {
        return this.type;
    }

    /**
     * Serialize an object of type T; not including the prefix byte(s).
     * @param object the object to serialize
     * @param buffer buffer for the serialized T
     * @param pointer position in buffer where the first byte of the serialized T will be stored
     * @param endianness selects bigEndian or littleEndian encoding
     * @throws SerializationException when a matrix has size zero or is jagged
     */
    public abstract void serialize(T object, byte[] buffer, Pointer pointer, Endianness endianness)
            throws SerializationException;

    /**
     * Serialize an object of type T including the prefix byte(s).
     * @param object the object to serialize
     * @param buffer buffer for the serialized T
     * @param pointer position in buffer where the first byte of the serialized T will be stored
     * @param endianness selects bigEndian or littleEndian encoding
     * @throws SerializationException when a matrix has size zero or is jagged
     */
    public void serializeWithPrefix(final T object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianness);
    }

    /**
     * Deserialize an object of type T. The <code>pointer</code> should be on the first byte of the object; i.e. just after the
     * prefix byte.
     * @param buffer the bytes with serialized data that must be reconstructed into a T
     * @param pointer position in the buffer where the first byte of the serialized T is located
     * @return a T object constructed from the data in the buffer
     * @param endianness selects bigEndian or littleEndian encoding
     * @throws SerializationException when the input data cannot be deserialized
     */
    public abstract T deserialize(byte[] buffer, Pointer pointer, Endianness endianness) throws SerializationException;

    /**
     * Return a description of the type of data that this serializer handles. The result of this method should <b>not</b> be
     * subject to localization because it is used in the SerialDataDecoder to identify the type of a serializer.
     * @return description of the type of data that this serializer handles
     */
    public final String dataClassName()
    {
        return this.dataClassName;
    }

    /**
     * Return the number of dimensions of the stored data.
     * @return 0 for plain data, 1 for array, 2 for matrix
     */
    public abstract int getNumberOfDimensions();

    /**
     * Return whether the serializer uses a single unit type or not.
     * @return whether the serializer uses a single unit type or not
     */
    public boolean hasUnit()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return "BasicSerializer [type=" + this.type + ", dataClassName=" + this.dataClassName + "]";
    }

}
