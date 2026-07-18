package org.djutils.serialization.codecs;

import java.util.HashMap;
import java.util.Map;

import org.djunits.quantity.def.AbsQuantity;
import org.djunits.quantity.def.Quantity;
import org.djunits.unit.UnitInterface;
import org.djunits.vecmat.def.AbsMatrix;
import org.djunits.vecmat.def.AbsVector;
import org.djunits.vecmat.def.Matrix;
import org.djunits.vecmat.def.Vector;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.QuantityType;
import org.djutils.serialization.SerializationException;
import org.djutils.serialization.UnitType;

/**
 * The Codec class takes care of serialization and deserialization of a single object. These take into account the endianness
 * for coding the different values. Java is by default big-endian.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 */
public final class Codec
{
    /**
     * Do not instantiate this utility class.
     */
    private Codec()
    {
        // Utility class; do not instantiate.
    }

    /** The easy converters keyed by Class. */
    protected static final Map<Class<?>, BasicCodec<?>> ENCODERS = new HashMap<>();

    /** All the converters that decode into arrays and matrices of Objects, keyed by prefix. */
    protected static final Map<Byte, BasicCodec<?>> OBJECT_DECODERS = new HashMap<>();

    /** All the converters that decode into arrays and matrices of primitive types, keyed by prefix. */
    protected static final Map<Byte, BasicCodec<?>> PRIMITIVE_DECODERS = new HashMap<>();

    static
    {
        registerObject(Byte.class, PrimitiveCodec.BYTE);
        registerPrimitive(byte.class, PrimitiveCodec.BYTE);
        registerObject(Short.class, PrimitiveCodec.SHORT);
        registerPrimitive(short.class, PrimitiveCodec.SHORT);
        registerObject(Integer.class, PrimitiveCodec.INTEGER);
        registerPrimitive(int.class, PrimitiveCodec.INTEGER);
        registerObject(Long.class, PrimitiveCodec.LONG);
        registerPrimitive(long.class, PrimitiveCodec.LONG);
        registerObject(Float.class, PrimitiveCodec.FLOAT);
        registerPrimitive(float.class, PrimitiveCodec.FLOAT);
        registerObject(Double.class, PrimitiveCodec.DOUBLE);
        registerPrimitive(double.class, PrimitiveCodec.DOUBLE);
        registerObject(Boolean.class, PrimitiveCodec.BOOLEAN);
        registerPrimitive(boolean.class, PrimitiveCodec.BOOLEAN);

        registerDecoder(PrimitiveCodec.CHARACTER8);
        registerDecoder(PrimitiveCodec.CHARACTER16);

        registerDecoder(StringCodec.STRING8);
        registerDecoder(StringCodec.STRING16);
        registerDecoder(StringArrayCodec.STRING8_ARRAY);
        registerDecoder(StringArrayCodec.STRING16_ARRAY);
        registerDecoder(StringMatrixCodec.STRING8_MATRIX);
        registerDecoder(StringMatrixCodec.STRING16_MATRIX);

        registerObject(Byte[].class, ObjectArrayCodec.BYTE_OBJECT_ARRAY);
        registerPrimitive(byte[].class, PrimitiveArrayCodec.BYTE_ARRAY);
        registerObject(Short[].class, ObjectArrayCodec.SHORT_OBJECT_ARRAY);
        registerPrimitive(short[].class, PrimitiveArrayCodec.SHORT_ARRAY);
        registerObject(Integer[].class, ObjectArrayCodec.INTEGER_OBJECT_ARRAY);
        registerPrimitive(int[].class, PrimitiveArrayCodec.INT_ARRAY);
        registerObject(Long[].class, ObjectArrayCodec.LONG_OBJECT_ARRAY);
        registerPrimitive(long[].class, PrimitiveArrayCodec.LONG_ARRAY);
        registerObject(Float[].class, ObjectArrayCodec.FLOAT_OBJECT_ARRAY);
        registerPrimitive(float[].class, PrimitiveArrayCodec.FLOAT_ARRAY);
        registerObject(Double[].class, ObjectArrayCodec.DOUBLE_OBJECT_ARRAY);
        registerPrimitive(double[].class, PrimitiveArrayCodec.DOUBLE_ARRAY);
        registerObject(Boolean[].class, ObjectArrayCodec.BOOLEAN_OBJECT_ARRAY);
        registerPrimitive(boolean[].class, PrimitiveArrayCodec.BOOLEAN_ARRAY);

        registerObject(Byte[][].class, ObjectMatrixCodec.BYTE_OBJECT_MATRIX);
        registerPrimitive(byte[][].class, PrimitiveMatrixCodec.BYTE_MATRIX);
        registerObject(Short[][].class, ObjectMatrixCodec.SHORT_OBJECT_MATRIX);
        registerPrimitive(short[][].class, PrimitiveMatrixCodec.SHORT_MATRIX);
        registerObject(Integer[][].class, ObjectMatrixCodec.INTEGER_OBJECT_MATRIX);
        registerPrimitive(int[][].class, PrimitiveMatrixCodec.INT_MATRIX);
        registerObject(Long[][].class, ObjectMatrixCodec.LONG_OBJECT_MATRIX);
        registerPrimitive(long[][].class, PrimitiveMatrixCodec.LONG_MATRIX);
        registerObject(Float[][].class, ObjectMatrixCodec.FLOAT_OBJECT_MATRIX);
        registerPrimitive(float[][].class, PrimitiveMatrixCodec.FLOAT_MATRIX);
        registerObject(Double[][].class, ObjectMatrixCodec.DOUBLE_OBJECT_MATRIX);
        registerPrimitive(double[][].class, PrimitiveMatrixCodec.DOUBLE_MATRIX);
        registerObject(Boolean[][].class, ObjectMatrixCodec.BOOLEAN_OBJECT_MATRIX);
        registerPrimitive(boolean[][].class, PrimitiveMatrixCodec.BOOLEAN_MATRIX);

        registerDecoder(QuantityCodec.QUANTITY_FLOAT);
        registerDecoder(QuantityCodec.QUANTITY_DOUBLE);
        registerDecoder(AbsQuantityCodec.ABS_QUANTITY_FLOAT);
        registerDecoder(AbsQuantityCodec.ABS_QUANTITY_DOUBLE);
        registerDecoder(VectorCodec.VECTOR_FLOAT);
        registerDecoder(VectorCodec.VECTOR_DOUBLE);
        registerDecoder(AbsVectorCodec.ABS_VECTOR_FLOAT);
        registerDecoder(AbsVectorCodec.ABS_VECTOR_DOUBLE);
        registerDecoder(MatrixCodec.MATRIX_FLOAT);
        registerDecoder(MatrixCodec.MATRIX_DOUBLE);
        registerDecoder(AbsMatrixCodec.ABS_MATRIX_FLOAT);
        registerDecoder(AbsMatrixCodec.ABS_MATRIX_DOUBLE);

        registerDecoder(VectorArrayCodec.COL_VECTOR_ARRAY_FLOAT);
        registerDecoder(VectorArrayCodec.COL_VECTOR_ARRAY_DOUBLE);
        registerDecoder(VectorArrayCodec.ROW_VECTOR_ARRAY_FLOAT);
        registerDecoder(VectorArrayCodec.ROW_VECTOR_ARRAY_DOUBLE);
    }

    /**
     * Register a codec for a primitive class (including arrays and matrices of primitive types).
     * @param clazz the class for which the serializer is registered
     * @param codec the codec for the class
     */
    private static void registerPrimitive(final Class<?> clazz, final BasicCodec<?> codec)
    {
        ENCODERS.put(clazz, codec);
        PRIMITIVE_DECODERS.put(codec.fieldType(), codec);
    }

    /**
     * Register a codec for an object class (including arrays and matrices of primitive object wrappers).
     * @param clazz the class for which the serializer is registered
     * @param codec the codec for the class
     */
    private static void registerObject(final Class<?> clazz, final BasicCodec<?> codec)
    {
        ENCODERS.put(clazz, codec);
        OBJECT_DECODERS.put(codec.fieldType(), codec);
    }

    /**
     * Register a codec for deserialization only.
     * @param codec the codec for which the deserializer is registered
     */
    private static void registerDecoder(final BasicCodec<?> codec)
    {
        PRIMITIVE_DECODERS.put(codec.fieldType(), codec);
        OBJECT_DECODERS.put(codec.fieldType(), codec);
    }

    /**
     * Return a safe copy of the encoder map.
     * @return a safe copy of the encoder map
     */
    public static Map<Class<?>, BasicCodec<?>> getEncoders()
    {
        return new HashMap<>(ENCODERS);
    }

    /**
     * Return a safe copy of the decoder map to primitive types.
     * @return a safe copy of the decoder map to primitive types
     */
    public static Map<Byte, BasicCodec<?>> getPrimitiveDecoders()
    {
        return new HashMap<>(PRIMITIVE_DECODERS);
    }

    /**
     * Return a safe copy of the decoder map to object types.
     * @return a safe copy of the decoder map to object types
     */
    public static Map<Byte, BasicCodec<?>> getObjectDecoders()
    {
        return new HashMap<>(OBJECT_DECODERS);
    }

    /**
     * Encode the object into a byte[] message. Use UTF8 for potential characters and Strings. Use double precision encoding
     * where appropriate.
     * @param content the objects to encode
     * @param endianness encoder to use for multi-byte values
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encode(final Object content, final Endianness endianness) throws SerializationException
    {
        return encode(content, endianness, true, false);
    }

    /**
     * Encode the object into a byte[] message. Use UTF8 for the characters and for the String. Use double precision encoding
     * where appropriate.
     * @param content the objects to encode
     * @param endianness encoder to use for multi-byte values
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF8(final Object content, final Endianness endianness) throws SerializationException
    {
        return encode(content, endianness, true, false);
    }

    /**
     * Encode the object array into a byte[] message. Use UTF16 for the characters and for the String. Use double precision
     * encoding where appropriate.
     * @param content the objects to encode
     * @param endianness encoder for multi-byte values
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF16(final Object content, final Endianness endianness) throws SerializationException
    {
        return encode(content, endianness, false, false);
    }

    /**
     * Find the codec for one object.
     * @param object the object for which the codec must be returned
     * @param utf8 if true, use UTF8 encoding for characters and Strings; if false; use UTF16 encoding
     * @param floatQuantity if true, use float encoding for quantities, vectors and matrices
     * @return the codec needed to encode/decode <code>object</code>
     * @throws SerializationException when there is no known codec for <code>object</code>
     */
    @SuppressWarnings("checkstyle:needbraces")
    protected static BasicCodec<?> findEncoder(final Object object, final boolean utf8, final boolean floatQuantity)
            throws SerializationException
    {
        BasicCodec<?> codec = ENCODERS.get(object.getClass());
        // System.out.println(object.getClass() + " " + codec.fieldType() + " - " + codec.getShortName());

        if (codec != null)
            return codec;

        if (object instanceof Character)
            return utf8 ? PrimitiveCodec.CHARACTER8 : PrimitiveCodec.CHARACTER16;
        if (object instanceof String)
            return utf8 ? StringCodec.STRING8 : StringCodec.STRING16;
        if (object instanceof String[])
            return utf8 ? StringArrayCodec.STRING8_ARRAY : StringArrayCodec.STRING16_ARRAY;
        if (object instanceof String[][])
            return utf8 ? StringMatrixCodec.STRING8_MATRIX : StringMatrixCodec.STRING16_MATRIX;
        if (object instanceof Quantity)
            return floatQuantity ? QuantityCodec.QUANTITY_FLOAT : QuantityCodec.QUANTITY_DOUBLE;
        if (object instanceof AbsQuantity)
            return floatQuantity ? AbsQuantityCodec.ABS_QUANTITY_FLOAT : AbsQuantityCodec.ABS_QUANTITY_DOUBLE;
        if (object instanceof Vector)
            return floatQuantity ? VectorCodec.VECTOR_FLOAT : VectorCodec.VECTOR_DOUBLE;
        if (object instanceof AbsVector)
            return floatQuantity ? AbsVectorCodec.ABS_VECTOR_FLOAT : AbsVectorCodec.ABS_VECTOR_DOUBLE;
        if (object instanceof Matrix)
            return floatQuantity ? MatrixCodec.MATRIX_FLOAT : MatrixCodec.MATRIX_DOUBLE;
        if (object instanceof AbsMatrix)
            return floatQuantity ? AbsMatrixCodec.ABS_MATRIX_FLOAT : AbsMatrixCodec.ABS_MATRIX_DOUBLE;
        if (object instanceof Vector[] v)
        {
            if (v[0].isColumnVector())
                return floatQuantity ? VectorArrayCodec.COL_VECTOR_ARRAY_FLOAT : VectorArrayCodec.COL_VECTOR_ARRAY_DOUBLE;
            return floatQuantity ? VectorArrayCodec.ROW_VECTOR_ARRAY_FLOAT : VectorArrayCodec.ROW_VECTOR_ARRAY_DOUBLE;
        }

        throw new SerializationException("Unhandled data type " + object.getClass());
    }

    /**
     * Encode the object into a byte array, conforming to the provided endianness, floating point precision and string encoding.
     * @param content the objects to encode
     * @param endianness encoder for multi-byte values
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param floatQuantity if true, use float encoding for quantities, vectors and matrices
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static byte[] encode(final Object content, final Endianness endianness, final boolean utf8,
            final boolean floatQuantity) throws SerializationException
    {
        BasicCodec codec = findEncoder(content, utf8, floatQuantity);

        // Pass one: compute total size
        int size = codec.sizeWithPrefix(content);

        // Allocate buffer
        byte[] buffer = new byte[size];

        // Pass 2 fill buffer
        Pointer pointer = new Pointer();
        codec.serializeWithPrefix(content, buffer, pointer, endianness);

        Throw.when(pointer.get() != buffer.length, SerializationException.class, "Data size error (reserved %d, used %d)",
                buffer.length, pointer.get());

        return buffer;
    }

    /**
     * Decode the message into an object, constructing Java primitives, primitive data arrays and matrices where possible.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @return an object of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object decodeToPrimitiveDataType(final Endianness endianness, final byte[] buffer)
            throws SerializationException
    {
        return decode(endianness, buffer, PRIMITIVE_DECODERS);
    }

    /**
     * Decode the message into an object, constructing Java Objects, Object arrays and matrices where possible.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @return an object of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object decodeToObjectDataType(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        return decode(endianness, buffer, OBJECT_DECODERS);
    }

    /**
     * Decode the message into a single object.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @param decoderMap the map with decoders to use
     * @return an object of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object decode(final Endianness endianness, final byte[] buffer, final Map<Byte, BasicCodec<?>> decoderMap)
            throws SerializationException
    {
        Pointer pointer = new Pointer();
        Byte fieldType = buffer[pointer.getAndIncrement(1)];
        BasicCodec<?> codec = decoderMap.get(fieldType);
        if (null == codec)
        {
            throw new SerializationException(
                    "Bad FieldType or no defined decoder for fieldType " + fieldType + " at position " + (pointer.get() - 1));
        }
        else
        {
            return codec.deserialize(buffer, pointer, endianness);
        }
    }

    /**
     * Decode a byte value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded byte
     * @return the byte value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static byte decodeByte(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 2, SerializationException.class, "decodeByte expects a buffer of at least 2 bytes");
        if (buffer[0] == FieldTypes.BYTE_8)
        {
            return (byte) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeByte did not detect byte in first byte");
    }

    /**
     * Decode a short value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded short
     * @return the short value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static short decodeShort(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 3, SerializationException.class, "decodeShort expects a buffer of at least 3 bytes");
        if (buffer[0] == FieldTypes.SHORT_16)
        {
            return (short) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeShort did not detect short in first byte");
    }

    /**
     * Decode an integer value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded int
     * @return the integer value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static int decodeInt(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 5, SerializationException.class, "decodeInt expects a buffer of at least 5 bytes");
        if (buffer[0] == FieldTypes.INT_32)
        {
            return (int) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeInt did not detect integer in first byte");
    }

    /**
     * Decode a long value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded long
     * @return the long value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static long decodeLong(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 9, SerializationException.class, "decodeLong expects a buffer of at least 9 bytes");
        if (buffer[0] == FieldTypes.LONG_64)
        {
            return (long) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeLong did not detect long in first byte");
    }

    /**
     * Decode a float value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded float
     * @return the float value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static float decodeFloat(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 5, SerializationException.class, "decodeFloat expects a buffer of at least 5 bytes");
        if (buffer[0] == FieldTypes.FLOAT_32)
        {
            return (float) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeFloat did not detect float in first byte");
    }

    /**
     * Decode a double value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded double
     * @return the double value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static double decodeDouble(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 9, SerializationException.class, "decodeDouble expects a buffer of at least 9 bytes");
        if (buffer[0] == FieldTypes.DOUBLE_64)
        {
            return (double) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeDouble did not detect double in first byte");
    }

    /**
     * Decode a boolean value from the buffer, including the prefix.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded boolean
     * @return the boolean value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static boolean decodeBoolean(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 2, SerializationException.class, "decodeBoolean expects a buffer of at least 2 bytes");
        if (buffer[0] == FieldTypes.BOOLEAN_8)
        {
            return (boolean) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeBoolean did not detect boolean in first byte");
    }

    /**
     * Decode a char value from the buffer, including the prefix, based on a one-byte UTF-8 value.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded char
     * @return the char value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static char decodeCharUtf8(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 2, SerializationException.class, "decodeShort expects a buffer of at least 2 bytes");
        if (buffer[0] == FieldTypes.CHAR_8)
        {
            return (char) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeCharUtf8 did not detect char in first byte");
    }

    /**
     * Decode a char value from the buffer, including the prefix, based on a one-byte UTF-16 value.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the buffer with the byte-encoded char
     * @return the char value belonging to the byte buffer content
     * @throws SerializationException when decoding fails
     */
    public static char decodeCharUtf16(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        Throw.when(buffer.length < 3, SerializationException.class, "decodeShort expects a buffer of at least 3 bytes");
        if (buffer[0] == FieldTypes.CHAR_16)
        {
            return (char) decodeToPrimitiveDataType(endianness, buffer);
        }
        throw new SerializationException("decodeCharUtf8 did not detect char in first byte");
    }

    /**
     * Retrieve and decode a DJUNITS unit.
     * @param buffer the encoded data
     * @param pointer position in the encoded data where the unit is to be decoded from
     * @param endianness decoder for multi-byte values
     * @return decoded Unit
     * @param <U> the unit type
     */
    @SuppressWarnings("unchecked")
    public static <U extends UnitInterface<?>> U getUnit(final byte[] buffer, final Pointer pointer,
            final Endianness endianness)
    {
        QuantityType quantityType = QuantityType.getQuantityType(buffer[pointer.getAndIncrement(1)]);
        UnitType unitType = UnitType.getUnitType(quantityType, 0 + buffer[pointer.getAndIncrement(1)]);
        return (U) unitType.getUnit();
    }

}
