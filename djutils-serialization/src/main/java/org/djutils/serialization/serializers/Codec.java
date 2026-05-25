package org.djutils.serialization.serializers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.djunits.quantity.def.AbsQuantity;
import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;
import org.djunits.vecmat.def.AbsMatrix;
import org.djunits.vecmat.def.AbsVector;
import org.djunits.vecmat.def.Matrix;
import org.djunits.vecmat.def.Vector;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.QuantityType;
import org.djutils.serialization.SerializableObject;
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
    protected static final Map<Class<?>, Serializer<?>> ENCODERS = new HashMap<>();

    /** All the converters that decode into arrays and matrices of Objects, keyed by prefix. */
    protected static final Map<Byte, Serializer<?>> DECODERS = new HashMap<>();

    static
    {
        register(Byte.class, PrimitiveSerializer.CONVERT_BYTE);
        register(Byte.class, PrimitiveSerializer.CONVERT_BYTE);
        register(byte.class, PrimitiveSerializer.CONVERT_BYTE);
        register(Short.class, PrimitiveSerializer.CONVERT_SHORT);
        register(short.class, PrimitiveSerializer.CONVERT_SHORT);
        register(Integer.class, PrimitiveSerializer.CONVERT_INTEGER);
        register(int.class, PrimitiveSerializer.CONVERT_INTEGER);
        register(Long.class, PrimitiveSerializer.CONVERT_LONG);
        register(long.class, PrimitiveSerializer.CONVERT_LONG);
        register(Float.class, PrimitiveSerializer.CONVERT_FLOAT);
        register(float.class, PrimitiveSerializer.CONVERT_FLOAT);
        register(Double.class, PrimitiveSerializer.CONVERT_DOUBLE);
        register(double.class, PrimitiveSerializer.CONVERT_DOUBLE);
        register(Boolean.class, PrimitiveSerializer.CONVERT_BOOLEAN);
        register(boolean.class, PrimitiveSerializer.CONVERT_BOOLEAN);
        
        DECODERS.put(StringSerializer.CONVERT_STRING8.fieldType(), StringSerializer.CONVERT_STRING8);
        DECODERS.put(StringSerializer.CONVERT_STRING16.fieldType(), StringSerializer.CONVERT_STRING16);
        
        register(Byte[].class, PrimitiveArraySerializer.CONVERT_BYTE_ARRAY);
        register(byte[].class, PrimitiveArraySerializer.CONVERT_BT_ARRAY);
        register(Short[].class, PrimitiveArraySerializer.CONVERT_SHORT_ARRAY);
        register(short[].class, PrimitiveArraySerializer.CONVERT_SHRT_ARRAY);
        register(Integer[].class, PrimitiveArraySerializer.CONVERT_INTEGER_ARRAY);
        register(int[].class, PrimitiveArraySerializer.CONVERT_INT_ARRAY);
        register(Long[].class, PrimitiveArraySerializer.CONVERT_LONG_ARRAY);
        register(long[].class, PrimitiveArraySerializer.CONVERT_LNG_ARRAY);
        register(Float[].class, PrimitiveArraySerializer.CONVERT_FLOAT_ARRAY);
        register(float[].class, PrimitiveArraySerializer.CONVERT_FLT_ARRAY);
        register(Double[].class, PrimitiveArraySerializer.CONVERT_DOUBLE_ARRAY);
        register(double[].class, PrimitiveArraySerializer.CONVERT_DBL_ARRAY);
        register(Boolean[].class, PrimitiveArraySerializer.CONVERT_BOOLEAN_ARRAY);
        register(boolean[].class, PrimitiveArraySerializer.CONVERT_BOOL_ARRAY);
        
        register(Byte[][].class, PrimitiveMatrixSerializer.CONVERT_BYTE_MATRIX);
        register(byte[][].class, PrimitiveMatrixSerializer.CONVERT_BT_MATRIX);
        register(Short[][].class, PrimitiveMatrixSerializer.CONVERT_SHORT_MATRIX);
        register(short[][].class, PrimitiveMatrixSerializer.CONVERT_SHRT_MATRIX);
        register(Integer[][].class, PrimitiveMatrixSerializer.CONVERT_INTEGER_MATRIX);
        register(int[][].class, PrimitiveMatrixSerializer.CONVERT_INT_MATRIX);
        register(Long[][].class, PrimitiveMatrixSerializer.CONVERT_LONG_MATRIX);
        register(long[][].class, PrimitiveMatrixSerializer.CONVERT_LNG_MATRIX);
        register(Float[][].class, PrimitiveMatrixSerializer.CONVERT_FLOAT_MATRIX);
        register(float[][].class, PrimitiveMatrixSerializer.CONVERT_FLT_MATRIX);
        register(Double[][].class, PrimitiveMatrixSerializer.CONVERT_DOUBLE_MATRIX);
        register(double[][].class, PrimitiveMatrixSerializer.CONVERT_DBL_MATRIX);
        register(Boolean[][].class, PrimitiveMatrixSerializer.CONVERT_BOOLEAN_MATRIX);
        register(boolean[][].class, PrimitiveMatrixSerializer.CONVERT_BOOL_MATRIX);

        DECODERS.put(CONVERT_DJUNITS_QUANTITY.fieldType(), CONVERT_DJUNITS_QUANTITY);
        DECODERS.put(CONVERT_DJUNITS_ABSQUANTITY.fieldType(), CONVERT_DJUNITS_ABSQUANTITY);
        DECODERS.put(CONVERT_DJUNITS_VECTOR.fieldType(), CONVERT_DJUNITS_VECTOR);
        DECODERS.put(CONVERT_DJUNITS_ABSVECTOR.fieldType(), CONVERT_DJUNITS_ABSVECTOR);
        DECODERS.put(CONVERT_DJUNITS_MATRIX.fieldType(), CONVERT_DJUNITS_MATRIX);
        DECODERS.put(CONVERT_DJUNITS_ABSMATRIX.fieldType(), CONVERT_DJUNITS_ABSMATRIX);
        DECODERS.put(CONVERT_UNIT_COLUMN_ABSVECTOR_ARRAY.fieldType(), CONVERT_UNIT_COLUMN_ABSVECTOR_ARRAY);
        DECODERS.put(CONVERT_UNIT_COLUMN_VECTOR_ARRAY.fieldType(), CONVERT_UNIT_COLUMN_VECTOR_ARRAY);
        
        DECODERS.put(CONVERT_STRING8_ARRAY.fieldType(), CONVERT_STRING8_ARRAY);
        DECODERS.put(CONVERT_STRING16_ARRAY.fieldType(), CONVERT_STRING16_ARRAY);
        DECODERS.put(CONVERT_STRING8_MATRIX.fieldType(), CONVERT_STRING8_MATRIX);
        DECODERS.put(CONVERT_STRING16_MATRIX.fieldType(), CONVERT_STRING16_MATRIX);
        DECODERS.put(COMPOUND_ARRAY_SERIALIZER_UTF16.fieldType(), COMPOUND_ARRAY_SERIALIZER_UTF16);
        DECODERS.put(COMPOUND_ARRAY_SERIALIZER_UTF8.fieldType(), COMPOUND_ARRAY_SERIALIZER_UTF8);
    }

    /**
     * Register a serializer for a primitive class (including arrays and matrices of primitive types).
     * @param clazz the class for which the serializer is registered
     * @param serializer the serializer for the class
     */
    private static void register(final Class<?> clazz, final Serializer<?> serializer)
    {
        ENCODERS.put(clazz, serializer);
        DECODERS.put(serializer.fieldType(), serializer);
    }
    
    
    /** Converter for descendants of Quantity. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<Quantity<?>> CONVERT_DJUNITS_QUANTITY = new QuantitySerializer();

    /** Converter for descendants of AbsQuantity. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<AbsQuantity<?, ?, ?>> CONVERT_DJUNITS_ABSQUANTITY = new AbsQuantitySerializer();

    /** Converter for descendants of Vector. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<Vector<?, ?, ?, ?, ?>> CONVERT_DJUNITS_VECTOR = new VectorSerializer();

    /** Converter for descendants of AbsVector. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<AbsVector<?, ?, ?, ?, ?>> CONVERT_DJUNITS_ABSVECTOR = new AbsVectorSerializer();

    /** Converter for descendants of Matrix. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<Matrix<?, ?, ?, ?, ?>> CONVERT_DJUNITS_MATRIX = new MatrixSerializer();

    /** Converter for descendants of AbsMatrix. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<AbsMatrix<?, ?, ?, ?, ?>> CONVERT_DJUNITS_ABSMATRIX = new AbsMatrixSerializer();

    /** Serializer for array of FloatVector. Each FloatVector must have same size. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<Vector[]> CONVERT_UNIT_COLUMN_VECTOR_ARRAY = new VectorArraySerializer();

    /** Serializer for array of DoubleVector. Each DoubleVector must have same size. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<AbsVector[]> CONVERT_UNIT_COLUMN_ABSVECTOR_ARRAY = new AbsVectorArraySerializer();

    /** Converter for array of SerializebleObject using UTF16 for strings and characters. */
    protected static final Serializer<SerializableObject<?>[]> COMPOUND_ARRAY_SERIALIZER_UTF16 =
            new ObjectSerializer<SerializableObject<?>[]>((byte) 120, "Compound")
            {

                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public int size(final SerializableObject<?>[] objects) throws SerializationException
                {
                    int result = 4 + 4;
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    Serializer[] serializers = MessageCodec.buildEncoderList(false, objectArray);
                    result += serializers.length;
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        result += objects.length * serializers[i].size(objectArray[i]);
                    }
                    return result;
                }

                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void serialize(final SerializableObject<?>[] objects, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    endianness.encodeInt(objects.length, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(objectArray.length, buffer, pointer.getAndIncrement(4));
                    Serializer[] serializers = MessageCodec.buildEncoderList(false, objectArray);
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        buffer[pointer.getAndIncrement(1)] = serializers[i].fieldType();
                    }
                    for (int i = 0; i < objects.length; i++)
                    {
                        List<Object> row = objects[i].exportAsList();
                        Throw.when(row.size() != objectArray.length, SerializationException.class,
                                "List in row %d has %d elements which differs from the %d elements in row 0", i, row.size(),
                                objectArray.length);
                        for (int j = 0; j < row.size(); j++)
                        {
                            serializers[j].serialize(row.get(j), buffer, pointer, endianness);
                        }
                    }
                }

                @Override
                public SerializableObject<?>[] deSerialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int arraySize = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int fieldCount = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Serializer<?>[] deSerializers = new Serializer[fieldCount];
                    for (int i = 0; i < fieldCount; i++)
                    {
                        Byte key = buffer[pointer.getAndIncrement(1)];
                        Serializer<?> deSerializer = DECODERS.get(key);
                        Throw.whenNull(SerializationException.class, "No decoder for %d", key);
                        deSerializers[i] = deSerializer;
                    }
                    MinimalSerializableObject[] result = new MinimalSerializableObject[arraySize];
                    for (int i = 0; i < arraySize; i++)
                    {
                        List<Object> element = new ArrayList<>();
                        for (int j = 0; j < fieldCount; j++)
                        {
                            element.add(deSerializers[j].deSerialize(buffer, pointer, endianness));
                        }
                        result[i] = new MinimalSerializableObject(element);
                    }
                    return result;
                }
            };

    /** Converter for array of SerializebleObject using UTF8 for strings and characters. */
    protected static final Serializer<SerializableObject<?>[]> COMPOUND_ARRAY_SERIALIZER_UTF8 =
            new ObjectSerializer<SerializableObject<?>[]>((byte) 121, "Compound")
            {

                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public int size(final SerializableObject<?>[] objects) throws SerializationException
                {
                    int result = 4 + 4;
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    Serializer[] serializers = MessageCodec.buildEncoderList(true, objectArray);
                    result += serializers.length;
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        result += objects.length * serializers[i].size(objectArray[i]);
                    }
                    return result;
                }

                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void serialize(final SerializableObject<?>[] objects, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    endianness.encodeInt(objects.length, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(objectArray.length, buffer, pointer.getAndIncrement(4));
                    Serializer[] serializers = MessageCodec.buildEncoderList(true, objectArray);
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        buffer[pointer.getAndIncrement(1)] = serializers[i].fieldType();
                    }
                    for (int i = 0; i < objects.length; i++)
                    {
                        List<Object> row = objects[i].exportAsList();
                        Throw.when(row.size() != objectArray.length, SerializationException.class,
                                "List in row %d has %d elements which differs from the %d elements in row 0", i, row.size(),
                                objectArray.length);
                        for (int j = 0; j < row.size(); j++)
                        {
                            serializers[j].serialize(row.get(j), buffer, pointer, endianness);
                        }
                    }
                }

                @Override
                public SerializableObject<?>[] deSerialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int arraySize = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int fieldCount = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Serializer<?>[] deSerializers = new Serializer[fieldCount];
                    for (int i = 0; i < fieldCount; i++)
                    {
                        Byte key = buffer[pointer.getAndIncrement(1)];
                        Serializer<?> deSerializer = DECODERS.get(key);
                        Throw.whenNull(SerializationException.class, "No decoder for %d", key);
                        deSerializers[i] = deSerializer;
                    }
                    MinimalSerializableObject[] result = new MinimalSerializableObject[arraySize];
                    for (int i = 0; i < arraySize; i++)
                    {
                        List<Object> element = new ArrayList<>();
                        for (int j = 0; j < fieldCount; j++)
                        {
                            element.add(deSerializers[j].deSerialize(buffer, pointer, endianness));
                        }
                        result[i] = new MinimalSerializableObject(element);
                    }
                    return result;
                }
            };

    /** Converter for String UTF-8 array. */
    protected static final Serializer<String[]> CONVERT_STRING8_ARRAY =
            new StringArraySerializer(FieldTypes.STRING_UTF8_ARRAY, "String_8_array")
            {
                @Override
                public int size(final String[] stringArray)
                {
                    int size = 4;
                    for (String string : stringArray)
                    {
                        size += 4 + string.getBytes(UTF8).length;
                    }
                    return size;
                }

                @Override
                public void serialize(final String[] stringArray, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeInt(stringArray.length, buffer, pointer.getAndIncrement(4));
                    for (String string : stringArray)
                    {
                        byte[] s = string.getBytes(UTF8);
                        endianness.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
                        for (byte b : s)
                        {
                            buffer[pointer.getAndIncrement(1)] = b;
                        }
                    }
                }

                @Override
                public String[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    String[] result = new String[size];
                    for (int i = 0; i < size; i++)
                    {
                        int bytesUsed = endianness.decodeInt(buffer, pointer.get());
                        result[i] = endianness.decodeUTF8String(buffer, pointer.get());
                        pointer.getAndIncrement(4 + bytesUsed);
                    }
                    return result;
                }

            };

    /** Converter for String UTF-16 array. */
    protected static final Serializer<String[]> CONVERT_STRING16_ARRAY =
            new StringArraySerializer(FieldTypes.STRING_UTF16_ARRAY, "String_16_array")
            {
                @Override
                public int size(final String[] stringArray)
                {
                    int size = 4;
                    for (String string : stringArray)
                    {
                        size += 4 + string.getBytes(UTF16).length;
                    }
                    return size;
                }

                @Override
                public void serialize(final String[] stringArray, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeInt(stringArray.length, buffer, pointer.getAndIncrement(4));
                    for (String string : stringArray)
                    {
                        // Note that according to https://stackoverflow.com/questions/74887443, String.length returns
                        // the number of code units (i.e. the number of 16-bit char values) needed to make up the String
                        // and not the number of Unicode codepoints.
                        char[] chars = new char[string.length()];
                        string.getChars(0, chars.length, chars, 0);
                        endianness.encodeInt(chars.length, buffer, pointer.getAndIncrement(4));
                        for (char c : chars)
                        {
                            endianness.encodeChar(c, buffer, pointer.getAndIncrement(2));
                        }
                    }
                }

                @Override
                public String[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    String[] result = new String[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeUTF16String(buffer, pointer.get());
                        pointer.getAndIncrement(4 + result[i].length() * 2);
                    }
                    return result;
                }
            };

    /** Converter for String UTF-8 matrix. */
    protected static final Serializer<String[][]> CONVERT_STRING8_MATRIX =
            new StringMatrixSerializer(FieldTypes.STRING_UTF8_MATRIX, "String_8_matrix")
            {
                @Override
                public int size(final String[][] stringMatrix)
                {
                    int size = 8;
                    for (String[] stringArray : stringMatrix)
                    {
                        for (String string : stringArray)
                        {
                            size += 4 + string.getBytes(UTF8).length;
                        }
                    }
                    return size;
                }

                @Override
                public void serialize(final String[][] stringMatrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = stringMatrix.length;
                    int width = stringMatrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(stringMatrix[i].length != width, SerializationException.class,
                                "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            byte[] s = stringMatrix[i][j].getBytes(UTF8);
                            endianness.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
                            for (byte b : s)
                            {
                                buffer[pointer.getAndIncrement(1)] = b;
                            }
                        }
                    }
                }

                @Override
                public String[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    String[][] result = new String[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            int bytesUsed = endianness.decodeInt(buffer, pointer.get());
                            result[i][j] = endianness.decodeUTF8String(buffer, pointer.get());
                            pointer.getAndIncrement(4 + bytesUsed);
                        }
                    }
                    return result;
                }
            };

    /** Converter for String UTF-16 matrix. */
    protected static final Serializer<String[][]> CONVERT_STRING16_MATRIX =
            new StringMatrixSerializer(FieldTypes.STRING_UTF16_MATRIX, "String_16_matrix")
            {
                @Override
                public int size(final String[][] stringMatrix)
                {
                    int size = 8;
                    for (String[] stringArray : stringMatrix)
                    {
                        for (String string : stringArray)
                        {
                            size += 4 + string.getBytes(UTF16).length;
                        }
                    }
                    return size;
                }

                @Override
                public void serialize(final String[][] stringMatrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = stringMatrix.length;
                    int width = stringMatrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(stringMatrix[i].length != width, SerializationException.class,
                                "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            // Note that according to https://stackoverflow.com/questions/74887443, String.length returns
                            // the number of code units (i.e. the number of 16-bit char values) needed to make up the String
                            // and not the number of Unicode codepoints.
                            char[] chars = new char[stringMatrix[i][j].length()];
                            stringMatrix[i][j].getChars(0, chars.length, chars, 0);
                            endianness.encodeInt(chars.length, buffer, pointer.getAndIncrement(4));
                            for (char c : chars)
                            {
                                endianness.encodeChar(c, buffer, pointer.getAndIncrement(2));
                            }
                        }
                    }
                }

                @Override
                public String[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    String[][] result = new String[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianness.decodeUTF16String(buffer, pointer.get());
                            pointer.getAndIncrement(4 + result[i][j].length() * 2);
                        }
                    }
                    return result;

                }
            };

    /**
     * Encode the object into a byte[] message. Use UTF8 for potential characters and Strings.
     * @param endianness encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encode(final Endianness endianness, final Object content) throws SerializationException
    {
        return encode(true, endianness, content);
    }

    /**
     * Encode the object into a byte[] message. Use UTF8 for the characters and for the String.
     * @param endianness encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF8(final Endianness endianness, final Object content) throws SerializationException
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
    public static byte[] encodeUTF16(final Endianness endianness, final Object content) throws SerializationException
    {
        return encode(false, endianness, content);
    }

    /**
     * Find the serializer for one object.
     * @param utf8 if true; use UTF8 encoding for characters and Strings; if false; use UTF16 encoding for characters and
     *            Strings
     * @param object the object for which the serializer must be returned
     * @return the serializer needed for <code>object</code>
     * @throws SerializationException when there is no known serializer for <code>object</code>
     */
    @SuppressWarnings("checkstyle:needbraces")
    protected static Serializer<?> findEncoder(final boolean utf8, final Object object) throws SerializationException
    {
        Serializer<?> serializer = ENCODERS.get(object.getClass());

        if (serializer != null)
            return serializer;

        if (object instanceof Character)
            return utf8 ? CONVERT_CHARACTER8 : CONVERT_CHARACTER16;
        if (object instanceof String)
            return utf8 ? CONVERT_STRING8 : CONVERT_STRING16;
        if (object instanceof String[])
            return utf8 ? CONVERT_STRING8_ARRAY : CONVERT_STRING16_ARRAY;
        if (object instanceof String[][])
            return utf8 ? CONVERT_STRING8_MATRIX : CONVERT_STRING16_MATRIX;
        if (object instanceof Quantity)
            return CONVERT_DJUNITS_QUANTITY;
        if (object instanceof AbsQuantity)
            return CONVERT_DJUNITS_ABSQUANTITY;
        if (object instanceof Vector)
            return CONVERT_DJUNITS_VECTOR;
        if (object instanceof AbsVector)
            return CONVERT_DJUNITS_ABSVECTOR;
        if (object instanceof Matrix)
            return CONVERT_DJUNITS_MATRIX;
        if (object instanceof AbsMatrix)
            return CONVERT_DJUNITS_ABSMATRIX;
        if (object instanceof SerializableObject[])
            return utf8 ? COMPOUND_ARRAY_SERIALIZER_UTF8 : COMPOUND_ARRAY_SERIALIZER_UTF16;
        if (object instanceof Vector[])
            return CONVERT_UNIT_COLUMN_VECTOR_ARRAY;
        if (object instanceof AbsVector[])
            return CONVERT_UNIT_COLUMN_ABSVECTOR_ARRAY;

        throw new SerializationException("Unhandled data type " + object.getClass());
    }

    /**
     * Encode the object array into a byte array, conforming to the provided endianness and string encodin.
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param endianness encoder for multi-byte values
     * @param content the objects to encode
     * @return the encoded message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static byte[] encode(final boolean utf8, final Endianness endianness, final Object content)
            throws SerializationException
    {
        Serializer serializer = findEncoder(utf8, content);
        // Pass one: compute total size
        int size = serializer.sizeWithPrefix(content);
        // Allocate buffer
        byte[] buffer = new byte[size];
        // Pass 2 fill buffer
        Pointer pointer = new Pointer();

        serializer.serializeWithPrefix(content, buffer, pointer, endianness);
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
    public static Object decodeToPrimitiveDataTypes(final Endianness endianness, final byte[] buffer)
            throws SerializationException
    {
        return decode(endianness, buffer, DECODERS);
    }

    /**
     * Decode the message into an object, constructing Java Objects, Object arrays and matrices where possible.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @return an object of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object decodeToObjectDataTypes(final Endianness endianness, final byte[] buffer) throws SerializationException
    {
        return decode(endianness, buffer, DECODERS);
    }

    /**
     * Decode the message into a single object.
     * @param endianness use big-endian or little-endian encoding
     * @param buffer the byte array to decode
     * @param decoderMap the map with decoders to use
     * @return an object of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object decode(final Endianness endianness, final byte[] buffer, final Map<Byte, Serializer<?>> decoderMap)
            throws SerializationException
    {
        Pointer pointer = new Pointer();
        Byte fieldType = buffer[pointer.getAndIncrement(1)];
        Serializer<?> serializer = decoderMap.get(fieldType);
        if (null == serializer)
        {
            throw new SerializationException(
                    "Bad FieldType or no defined decoder for fieldType " + fieldType + " at position " + (pointer.get() - 1));
        }
        else
        {
            return serializer.deSerialize(buffer, pointer, endianness);
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
            return (byte) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (short) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (int) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (long) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (float) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (double) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (boolean) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (char) decodeToPrimitiveDataTypes(endianness, buffer);
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
            return (char) decodeToPrimitiveDataTypes(endianness, buffer);
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
    public static <U extends Unit<U, ?>> U getUnit(final byte[] buffer, final Pointer pointer, final Endianness endianness)
    {
        QuantityType quantityType = QuantityType.getQuantityType(buffer[pointer.getAndIncrement(1)]);
        UnitType unitType = UnitType.getDisplayType(quantityType, 0 + buffer[pointer.getAndIncrement(1)]);
        return (U) unitType.getDjunitsType();
    }

    /**
     * Minimal implementation of SerializableObject.
     */
    static class MinimalSerializableObject implements SerializableObject<MinimalSerializableObject>
    {
        /** The List that is returned by the <code>exportAsList</code> method. */
        private final List<Object> list;

        /**
         * Construct a new MinimalCompound object.
         * @param list the object list that is returned by <code>exportAsList</code> method
         */
        MinimalSerializableObject(final List<Object> list)
        {
            this.list = list;
        }

        @Override
        public List<Object> exportAsList()
        {
            return this.list;
        }
    }

}
