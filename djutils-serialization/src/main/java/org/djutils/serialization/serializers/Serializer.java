package org.djutils.serialization.serializers;

import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationException;

/**
 * Interface to serialize and deserialize data.
 * <p>
 * Copyright (c) 2019-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * version Jun 07, 2019 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <T> Type of object that can be serialized and deserialized
 */
public interface Serializer<T extends Object>
{
    /**
     * Compute the number of bytes needed to serialize an object of type T (excluding the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object T; Instance of the object
     * @return int; the number of bytes needed to serialize an object of type T
     * @throws SerializationException when the <code>object</code> cannot be serialized
     */
    int size(T object) throws SerializationException;

    /**
     * Compute the number of bytes needed to serialize an object of type T (including the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object T; Instance of the object
     * @return int; the number of bytes needed to serialize an object of type T
     * @throws SerializationException when the <code>object</code> cannot be serialized
     */
    int sizeWithPrefix(T object) throws SerializationException;

    /**
     * Return the byte representation of the field type.
     * @return byte
     */
    byte fieldType();

    /**
     * Serialize an object of type T; not including the prefix byte(s).
     * @param object T; the object to serialize
     * @param buffer byte[]; buffer for the serialized T
     * @param pointer Pointer; position in buffer where the first byte of the serialized T will be stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @throws SerializationException when a matrix has size zero or is jagged
     */
    void serialize(T object, byte[] buffer, Pointer pointer, EndianUtil endianUtil) throws SerializationException;

    /**
     * Serialize an object of type T including the prefix byte(s).
     * @param object T; the object to serialize
     * @param buffer byte[]; buffer for the serialized T
     * @param pointer Pointer; position in buffer where the first byte of the serialized T will be stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @throws SerializationException when a matrix has size zero or is jagged
     */
    void serializeWithPrefix(T object, byte[] buffer, Pointer pointer, EndianUtil endianUtil) throws SerializationException;

    /**
     * Deserialize an object of type T. The <code>pointer</code> should be on the first byte of the object; i.e. just after the
     * prefix byte.
     * @param buffer byte[]; the bytes with serialized data that must be reconstructed into a T
     * @param pointer Pointer; position in the buffer where the first byte of the serialized T is located
     * @return T; a T object constructed from the data in the buffer
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @throws SerializationException when the input data cannot be deserialized
     */
    T deSerialize(byte[] buffer, Pointer pointer, EndianUtil endianUtil) throws SerializationException;

    /**
     * Return a description of the type of data that this serializer handles. The result of this method should <b>not</b> be
     * subject to localization because it is used in the SerialDataDecoder to identify the type of a serializer.
     * @return String; description of the type of data that this serializer handles
     */
    String dataClassName();

    /**
     * Return the number of dimensions of the stored data.
     * @return int; 0 for plain data, 1 for array, 2 for matrix
     */
    int getNumberOfDimensions();

}
