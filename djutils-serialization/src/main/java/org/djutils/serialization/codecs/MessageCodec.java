package org.djutils.serialization.codecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.SerializationException;

/**
 * Message conversions. These take into account the endianness for coding the different values. Java is by default big-endian.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 */
public final class MessageCodec
{
    /**
     * Do not instantiate this utility class.
     */
    private MessageCodec()
    {
        // Utility class; do not instantiate.
    }

    /**
     * Encode the object array into a byte[] message. Use UTF8 for the characters and for the String.
     * @param endianness encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the encoded message to send as a byte array
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
     * @return the encoded message to send as a byte array
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
     * @return the encoded message to send as a byte array
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
    protected static BasicCodec<?>[] buildEncoderList(final boolean utf8, final Object... content) throws SerializationException
    {
        BasicCodec<?>[] result = new BasicCodec[content.length];
        for (int i = 0; i < content.length; i++)
        {
            Object object = content[i];
            result[i] = Codec.findEncoder(object, utf8, false);
        }

        return result;
    }

    /**
     * Encode the object array into a message, taking the endianness into account.
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param endianness encoder for multi-byte values
     * @param content the objects to encode
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static byte[] encode(final boolean utf8, final Endianness endianness, final Object... content)
            throws SerializationException
    {
        BasicCodec[] serializers = buildEncoderList(utf8, content);
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
        return decode(endianness, buffer, Codec.DECODERS);
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
        return decode(endianness, buffer, Codec.DECODERS);
    }

    /**
     * Decode the message into an object array.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @param decoderMap the map with decoders to use
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object[] decode(final Endianness endianness, final byte[] buffer, final Map<Byte, BasicCodec<?>> decoderMap)
            throws SerializationException
    {
        List<Object> list = new ArrayList<>();
        Pointer pointer = new Pointer();
        while (pointer.get() < buffer.length)
        {
            Byte fieldType = buffer[pointer.getAndIncrement(1)];
            BasicCodec<?> serializer = decoderMap.get(fieldType);
            if (null == serializer)
            {
                throw new SerializationException("Bad FieldType or no defined decoder for fieldType " + fieldType
                        + " at position " + (pointer.get() - 1));
            }
            else
            {
                list.add(serializer.deserialize(buffer, pointer, endianness));
            }
        }
        return list.toArray();
    }
}
