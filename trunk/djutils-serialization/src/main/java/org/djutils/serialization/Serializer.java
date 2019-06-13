package org.djutils.serialization;

/**
 * Interface to serialize and deserialize data.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2019-06-07 01:33:02 +0200 (Mon, 7 Jun 2019) $, @version $Revision: 1401 $, by $Author: pknoppers $, initial
 * version Jun 07, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <T> Type of object that can be serialized and deserialized
 */
public interface Serializer<T extends Object>
{
    /**
     * Compute the number of bytes needed to serialize an object of type T (excluding the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object Object; Instance of the object (should be of type T)
     * @return int; the number of bytes needed to serialize an object of type T
     */
    int size(Object object);

    /**
     * Compute the number of bytes needed to serialize an object of type T (including the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object Instance of the object (should be instance of T)
     * @return int; the number of bytes needed to serialize an object of type T
     */
    int sizeWithPrefix(Object object);

    /**
     * Return the byte representation of the field type.
     * @return byte
     */
    byte fieldType();

    /**
     * Serialize an object of type T.
     * @param object Object; the object to serialize (should be of type T)
     * @param buffer byte[]; buffer for the serialized T
     * @param pointer Pointer; position in buffer where the first byte of the serialized T will be stored
     * @throws SerializationException when a matrix has size zero or is jagged
     */
    void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException;

    /**
     * Serialize an object of type T.
     * @param object Object; the object to serialize (should be of type T)
     * @param buffer byte[]; buffer for the serialized T
     * @param pointer Pointer; position in buffer where the first byte of the serialized T will be stored
     * @throws SerializationException when a matrix has size zero or is jagged
     */
    void serializeWithPrefix(Object object, byte[] buffer, Pointer pointer) throws SerializationException;

    /**
     * Deserialize an object of type T.
     * @param buffer byte[]; the bytes with serialized data that must be reconstructed into a T
     * @param pointer Pointer; position in the buffer where the first byte of the serialized T is located
     * @return T; a T object constructed from the data in the buffer
     * @throws SerializationException when the input data cannot be deserialized
     */
    T deSerialize(byte[] buffer, Pointer pointer) throws SerializationException;

    /**
     * Return a description of the type of data that this serializer handles.
     * @return String; description of the type of data that this serializer handles
     */
    abstract String dataClassName();

}
