package org.djutils.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.djutils.exceptions.Throw;
import org.djutils.serialization.serializers.Pointer;
import org.djutils.serialization.serializers.Serializer;

/**
 * Message conversions. These take into account the endianness for coding the different values. Java is by default big-endian.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public final class TypedMessage
{
    /**
     * Do not instantiate this utility class.
     */
    private TypedMessage()
    {
        // Utility class; do not instantiate.
    }

    /**
     * Encode the object array into a byte[] message. Use UTF8 for the characters and for the String.
     * @param endianness encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encode(final Endianness endianness, final Object... content) throws SerializationException
    {
        return encode(true, endianness, content);
    }

    /**
     * Encode the object array into a byte[] message. Use UTF8 for the characters and for the String.
     * @param endianness encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF8(final Endianness endianness, final Object... content) throws SerializationException
    {
        return encode(true, endianness, content);
    }

    /**
     * Encode the object array into a byte[] message. Use UTF16 for the characters and for the String.
     * @param endianness encoder for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF16(final Endianness endianness, final Object... content) throws SerializationException
    {
        return encode(false, endianness, content);
    }

    /**
     * Build the list of serializers corresponding to the data in an Object array.
     * @param utf8 if true; use UTF8 encoding for characters and Strings; if false; use UTF16 encoding for characters and
     *            Strings
     * @param content the objects for which the serializers must be returned
     * @return array filled with the serializers needed for the objects in the Object array
     * @throws SerializationException when an object in <code>content</code> cannot be serialized
     */
    protected static Serializer<?>[] buildEncoderList(final boolean utf8, final Object... content) throws SerializationException
    {
        Serializer<?>[] result = new Serializer[content.length];
        for (int i = 0; i < content.length; i++)
        {
            Object object = content[i];
            result[i] = TypedObject.findEncoder(utf8, object);
        }

        return result;
    }

    /**
     * Encode the object array into a Big Endian message.
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param endianness encoder for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static byte[] encode(final boolean utf8, final Endianness endianness, final Object... content)
            throws SerializationException
    {
        Serializer[] serializers = buildEncoderList(utf8, content);
        // Pass one: compute total size
        int size = 0;
        for (int i = 0; i < serializers.length; i++)
        {
            size += serializers[i].sizeWithPrefix(content[i]);
        }
        // Allocate buffer
        byte[] message = new byte[size];
        // Pass 2 fill buffer
        Pointer pointer = new Pointer();

        for (int i = 0; i < serializers.length; i++)
        {
            serializers[i].serializeWithPrefix(content[i], message, pointer, endianness);
            // System.out.println("Expected increment: " + serializers[i].size(content[i]));
            // System.out.println(pointer);
        }
        Throw.when(pointer.get() != message.length, SerializationException.class, "Data size error (reserved %d, used %d)",
                message.length, pointer.get());
        return message;
    }

    /**
     * Decode the message into an object array, constructing Java Primitive data arrays and matrices where possible.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object[] decodeToPrimitiveDataTypes(final Endianness endianness, final byte[] buffer)
            throws SerializationException
    {
        return decode(endianness, buffer, TypedObject.PRIMITIVE_DATA_DECODERS);
    }

    /**
     * Decode the message into an object array, constructing Java Object arrays and matrices where possible.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object[] decodeToObjectDataTypes(final Endianness endianness, final byte[] buffer)
            throws SerializationException
    {
        return decode(endianness, buffer, TypedObject.OBJECT_DECODERS);
    }

    /**
     * Decode the message into an object array.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @param decoderMap the map with decoders to use
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object[] decode(final Endianness endianness, final byte[] buffer, final Map<Byte, Serializer<?>> decoderMap)
            throws SerializationException
    {
        List<Object> list = new ArrayList<>();
        Pointer pointer = new Pointer();
        while (pointer.get() < buffer.length)
        {
            Byte fieldType = buffer[pointer.getAndIncrement(1)];
            Serializer<?> serializer = decoderMap.get(fieldType);
            if (null == serializer)
            {
                throw new SerializationException("Bad FieldType or no defined decoder for fieldType " + fieldType
                        + " at position " + (pointer.get() - 1));
            }
            else
            {
                list.add(serializer.deSerialize(buffer, pointer, endianness));
            }
        }
        return list.toArray();
    }
}
