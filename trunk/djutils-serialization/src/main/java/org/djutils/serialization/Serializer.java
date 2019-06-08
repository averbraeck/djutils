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
     * Container for an offset.
     */
    static class Pointer
    {
        /** Current value of the offset. */
        private int offset;

        /**
         * Construct a new Pointer with specified initial offset.
         * @param initialOffset int; the initial offset
         */
        Pointer(final int initialOffset)
        {
            this.offset = initialOffset;
        }

        /**
         * Construct a new Pointer with offset 0.
         */
        Pointer()
        {
            this(0);
        }

        /**
         * Retrieve the offset.
         * @return int; the offset
         */
        int get()
        {
            return this.offset;
        }

        /**
         * Retrieve the current value of offset and increment it. The returned value is the value <b>before</b> applying the
         * increment.
         * @param increment int; the amount by which the offset must be incremented
         * @return int; the offset (before the increment was added)
         */
        int getAndIncrement(int increment)
        {
            int result = this.offset;
            this.offset += increment;
            return result;
        }

        /**
         * Increment the offset.
         * @param increment int; the amount by which the offset must be incremented
         */
        public void inc(final int increment)
        {
            this.offset += increment;
        }

        @Override
        public String toString()
        {
            return "Pointer [offset=" + offset + "]";
        }
        
    }

    /**
     * Compute the number of bytes needed to serialize an object of type T (excluding the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object Instance of the object
     * @return int; the number of bytes needed to serialize an object of type T
     */
    int size(T object);

    /**
     * Compute the number of bytes needed to serialize an object of type T (including the byte(s) that indicate that an object
     * of type T is next in the data stream).
     * @param object Instance of the object
     * @return int; the number of bytes needed to serialize an object of type T
     */
    int sizeWithPrefix(T object);

    /**
     * Return the byte representation of the field type.
     * @return byte
     */
    byte fieldType();

    /**
     * Serialize an object of type T.
     * @param object T; the object to serialize
     * @param buffer byte[]; buffer for the serialized T
     * @param pointer Pointer; position in buffer where the first byte of the serialized T will be stored
     */
    void serialize(T object, byte[] buffer, Pointer pointer);

    /**
     * Serialize an object of type T.
     * @param object T; the object to serialize
     * @param buffer byte[]; buffer for the serialized T
     * @param pointer Pointer; position in buffer where the first byte of the serialized T will be stored
     */
    void serializeWithPrefix(T object, byte[] buffer, Pointer pointer);

    /**
     * Deserialize an object of type T.
     * @param buffer byte[]; the bytes with serialized data that must be reconstructed into a T
     * @param pointer Pointer; position in the buffer where the first byte of the serialized T is located
     * @return T; a T object constructed from the data in the buffer
     */
    T deSerialize(byte[] buffer, Pointer pointer);

}
