package org.djutils.serialization;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.djunits.unit.Unit;
import org.djunits.value.vdouble.matrix.base.DoubleMatrix;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vfloat.matrix.base.FloatMatrix;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djunits.value.vfloat.vector.base.FloatVector;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.serializers.BasicPrimitiveArrayOrMatrixSerializer;
import org.djutils.serialization.serializers.DoubleMatrixSerializer;
import org.djutils.serialization.serializers.DoubleScalarSerializer;
import org.djutils.serialization.serializers.DoubleVectorArraySerializer;
import org.djutils.serialization.serializers.DoubleVectorSerializer;
import org.djutils.serialization.serializers.FixedSizeObjectSerializer;
import org.djutils.serialization.serializers.FloatMatrixSerializer;
import org.djutils.serialization.serializers.FloatScalarSerializer;
import org.djutils.serialization.serializers.FloatVectorArraySerializer;
import org.djutils.serialization.serializers.FloatVectorSerializer;
import org.djutils.serialization.serializers.ObjectArraySerializer;
import org.djutils.serialization.serializers.ObjectMatrixSerializer;
import org.djutils.serialization.serializers.ObjectSerializer;
import org.djutils.serialization.serializers.Pointer;
import org.djutils.serialization.serializers.Serializer;
import org.djutils.serialization.serializers.StringArraySerializer;
import org.djutils.serialization.serializers.StringMatrixSerializer;

/**
 * Serialization and deserialization of a single object. These take into account the endianness for coding the different values.
 * Java is by default big-endian.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public final class TypedObject
{
    /**
     * Do not instantiate this utility class.
     */
    private TypedObject()
    {
        // Utility class; do not instantiate.
    }

    /** The easy converters keyed by Class. */
    protected static final Map<Class<?>, Serializer<?>> ENCODERS = new HashMap<>();

    /** All the converters that decode into primitive data when possible, keyed by prefix. */
    protected static final Map<Byte, Serializer<?>> PRIMITIVE_DATA_DECODERS = new HashMap<>();

    /** All the converters that decode into arrays and matrices of Objects, keyed by prefix. */
    protected static final Map<Byte, Serializer<?>> OBJECT_DECODERS = new HashMap<>();

    /** Converter for Byte. */
    protected static final Serializer<Byte> CONVERT_BYTE = new FixedSizeObjectSerializer<Byte>(FieldTypes.BYTE_8, 1, "Byte_8")
    {
        @Override
        public void serialize(final Byte object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            buffer[pointer.getAndIncrement(1)] = object;
        }

        @Override
        public Byte deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return buffer[pointer.getAndIncrement(1)];
        }
    };

    /** Converter for Short. */
    protected static final Serializer<Short> CONVERT_SHORT =
            new FixedSizeObjectSerializer<Short>(FieldTypes.SHORT_16, 2, "Short_16")
            {
                @Override
                public void serialize(final Short object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeShort(object, buffer, pointer.getAndIncrement(2));
                }

                @Override
                public Short deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeShort(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Integer. */
    protected static final Serializer<Integer> CONVERT_INTEGER =
            new FixedSizeObjectSerializer<Integer>(FieldTypes.INT_32, 4, "Integer_32")
            {
                @Override
                public void serialize(final Integer object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeInt(object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Integer deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Integer. */
    protected static final Serializer<Long> CONVERT_LONG = new FixedSizeObjectSerializer<Long>(FieldTypes.LONG_64, 8, "Long_64")
    {
        @Override
        public void serialize(final Long object, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            endianness.encodeLong(object, buffer, pointer.getAndIncrement(8));
        }

        @Override
        public Long deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            return endianness.decodeLong(buffer, pointer.getAndIncrement(8));
        }
    };

    /** Converter for Float. */
    protected static final Serializer<Float> CONVERT_FLOAT =
            new FixedSizeObjectSerializer<Float>(FieldTypes.FLOAT_32, 4, "Float_32")
            {
                @Override
                public void serialize(final Float object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeFloat(object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Float deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Double. */
    protected static final Serializer<Double> CONVERT_DOUBLE =
            new FixedSizeObjectSerializer<Double>(FieldTypes.DOUBLE_64, 8, "Double_64")
            {
                @Override
                public void serialize(final Double object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeDouble(object, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public Double deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            };

    /** Converter for Boolean. */
    protected static final Serializer<Boolean> CONVERT_BOOLEAN =
            new FixedSizeObjectSerializer<Boolean>(FieldTypes.BOOLEAN_8, 1, "Boolean_8")
            {
                @Override
                public void serialize(final Boolean object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    buffer[pointer.getAndIncrement(1)] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return buffer[pointer.getAndIncrement(1)] != 0;
                }
            };

    /** Converter for Character. */
    protected static final Serializer<Character> CONVERT_CHARACTER16 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_16, 2, "Char_16")
            {
                @Override
                public void serialize(final Character object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    endianness.encodeChar(object, buffer, pointer.getAndIncrement(size(object)));
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return endianness.decodeChar(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Character. */
    protected static final Serializer<Character> CONVERT_CHARACTER8 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_8, 1, "Char_8")
            {
                @Override
                public void serialize(final Character object, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    buffer[pointer.getAndIncrement(size(object))] = (byte) (object & 0xFF);
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    return Character.valueOf((char) buffer[pointer.getAndIncrement(1)]);
                }
            };

    /** Converter for String. */
    protected static final Serializer<String> CONVERT_STRING16 =
            new ObjectSerializer<String>(FieldTypes.STRING_UTF16, "String_16")
            {
                @Override
                public int size(final String object)
                {
                    return 4 + object.getBytes(UTF16).length;
                }

                @Override
                public void serialize(final String string, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness)
                {
                    // Note that according to https://stackoverflow.com/questions/74887443, String.length returns the number of
                    // code units (i.e. the number of 16-bit char values) needed to make up the String and not the number of
                    // Unicode codepoints.
                    char[] chars = new char[string.length()];
                    string.getChars(0, chars.length, chars, 0);
                    endianness.encodeInt(chars.length, buffer, pointer.getAndIncrement(4));
                    for (char c : chars)
                    {
                        endianness.encodeChar(c, buffer, pointer.getAndIncrement(2));
                    }
                }

                @Override
                public String deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                {
                    String s = endianness.decodeUTF16String(buffer, pointer.get());
                    pointer.getAndIncrement(4 + s.length() * 2);
                    return s;
                }
            };

    /** Converter for String. */
    protected static final Serializer<String> CONVERT_STRING8 = new ObjectSerializer<String>(FieldTypes.STRING_UTF8, "String_8")
    {
        @Override
        public int size(final String string)
        {
            return 4 + string.getBytes(UTF8).length;
        }

        @Override
        public void serialize(final String string, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            byte[] s = string.getBytes(UTF8);
            endianness.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
            for (byte b : s)
            {
                buffer[pointer.getAndIncrement(1)] = b;
            }
        }

        @Override
        public String deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int bytesUsed = endianness.decodeInt(buffer, pointer.get());
            String s = endianness.decodeUTF8String(buffer, pointer.get());
            pointer.getAndIncrement(4 + bytesUsed);
            return s;
        }
    };

    /** Converter for byte array. */
    protected static final Serializer<byte[]> CONVERT_BT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<byte[]>(FieldTypes.BYTE_8_ARRAY, 1, "byte_8_array", 1)
            {
                @Override
                public int size(final byte[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final byte[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        buffer[pointer.getAndIncrement(getElementSize())] = array[i];
                    }
                }

                @Override
                public byte[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    byte[] result = new byte[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(getElementSize())];
                    }
                    return result;
                }
            };

    /** Converter for Byte array. */
    protected static final Serializer<Byte[]> CONVERT_BYTE_ARRAY =
            new ObjectArraySerializer<Byte>(FieldTypes.BYTE_8_ARRAY, 1, Byte.valueOf((byte) 0), "Byte_8_array")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return buffer[offset];
                }
            };

    /** Converter for short array. */
    protected static final Serializer<short[]> CONVERT_SHRT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<short[]>(FieldTypes.SHORT_16_ARRAY, 2, "short_16_array", 1)
            {
                @Override
                public int size(final short[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final short[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeShort(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public short[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    short[] result = new short[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeShort(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Short array. */
    protected static final Serializer<Short[]> CONVERT_SHORT_ARRAY =
            new ObjectArraySerializer<Short>(FieldTypes.SHORT_16_ARRAY, 2, Short.valueOf((short) 0), "Short_16_array")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeShort(buffer, offset);
                }
            };

    /** Converter for int array. */
    protected static final Serializer<int[]> CONVERT_INT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<int[]>(FieldTypes.INT_32_ARRAY, 4, "int_32_array", 1)
            {
                @Override
                public int size(final int[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final int[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeInt(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public int[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int[] result = new int[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeInt(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Integer array. */
    protected static final Serializer<Integer[]> CONVERT_INTEGER_ARRAY =
            new ObjectArraySerializer<Integer>(FieldTypes.INT_32_ARRAY, 4, Integer.valueOf(0), "Integer_32_array")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeInt(buffer, offset);
                }
            };

    /** Converter for long array. */
    protected static final Serializer<long[]> CONVERT_LNG_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<long[]>(FieldTypes.LONG_64_ARRAY, 8, "long_64_array", 1)
            {
                @Override
                public int size(final long[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final long[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeLong(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public long[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    long[] result = new long[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeLong(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Long array. */
    protected static final Serializer<Long[]> CONVERT_LONG_ARRAY =
            new ObjectArraySerializer<Long>(FieldTypes.LONG_64_ARRAY, 8, Long.valueOf(0), "Long_64_array")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeLong(buffer, offset);
                }
            };

    /** Converter for float array. */
    protected static final Serializer<float[]> CONVERT_FLT_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<float[]>(FieldTypes.FLOAT_32_ARRAY, 4, "float_32_array", 1)
            {
                @Override
                public int size(final float[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final float[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeFloat(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public float[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    float[] result = new float[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeFloat(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Float array. */
    protected static final Serializer<Float[]> CONVERT_FLOAT_ARRAY =
            new ObjectArraySerializer<Float>(FieldTypes.FLOAT_32_ARRAY, 4, Float.valueOf(0), "Float_32_array")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeFloat(buffer, offset);
                }
            };

    /** Converter for double array. */
    protected static final Serializer<double[]> CONVERT_DBL_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<double[]>(FieldTypes.DOUBLE_64_ARRAY, 8, "double_64_array", 1)
            {
                @Override
                public int size(final double[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final double[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianness.encodeDouble(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public double[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    double[] result = new double[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianness.decodeDouble(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Double array. */
    protected static final Serializer<Double[]> CONVERT_DOUBLE_ARRAY =
            new ObjectArraySerializer<Double>(FieldTypes.DOUBLE_64_ARRAY, 8, Double.valueOf(0), "Double_64_array")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeDouble(buffer, offset);
                }
            };

    /** Converter for boolean array. */
    protected static final Serializer<boolean[]> CONVERT_BOOL_ARRAY =
            new BasicPrimitiveArrayOrMatrixSerializer<boolean[]>(FieldTypes.BOOLEAN_8_ARRAY, 1, "bool_8_array", 1)
            {
                @Override
                public int size(final boolean[] array)
                {
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final boolean[] array, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        buffer[pointer.getAndIncrement(getElementSize())] = (byte) (array[i] ? 1 : 0);
                    }
                }

                @Override
                public boolean[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    boolean[] result = new boolean[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(getElementSize())] != 0;
                    }
                    return result;
                }
            };

    /** Converter for Boolean array. */
    protected static final Serializer<Boolean[]> CONVERT_BOOLEAN_ARRAY =
            new ObjectArraySerializer<Boolean>(FieldTypes.BOOLEAN_8_ARRAY, 1, Boolean.FALSE, "Boolean_8_array")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return buffer[offset] != 0;
                }
            };

    /** Converter for byte matrix. */
    protected static final Serializer<byte[][]> CONVERT_BT_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<byte[][]>(FieldTypes.BYTE_8_MATRIX, 1, "byte_8_matrix", 2)
            {
                @Override
                public int size(final byte[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final byte[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            buffer[pointer.getAndIncrement(getElementSize())] = matrix[i][j];
                        }
                    }
                }

                @Override
                public byte[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    byte[][] result = new byte[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = buffer[pointer.getAndIncrement(getElementSize())];
                        }
                    }
                    return result;
                }
            };

    /** Converter for Byte matrix. */
    protected static final Serializer<Byte[][]> CONVERT_BYTE_MATRIX =
            new ObjectMatrixSerializer<Byte>(FieldTypes.BYTE_8_MATRIX, 1, Byte.class, "Byte_8_matrix")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return buffer[offset];
                }
            };

    /** Converter for short matrix. */
    protected static final Serializer<short[][]> CONVERT_SHRT_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<short[][]>(FieldTypes.SHORT_16_MATRIX, 2, "short_16_matrix", 2)
            {
                @Override
                public int size(final short[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final short[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianness.encodeShort(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public short[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    short[][] result = new short[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianness.decodeShort(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Short matrix. */
    protected static final Serializer<Short[][]> CONVERT_SHORT_MATRIX =
            new ObjectMatrixSerializer<Short>(FieldTypes.SHORT_16_MATRIX, 2, Short.class, "Short_16_matrix")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeShort(buffer, offset);
                }
            };

    /** Converter for int matrix. */
    protected static final Serializer<int[][]> CONVERT_INT_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<int[][]>(FieldTypes.INT_32_MATRIX, 4, "int_32_matrix", 2)
            {
                @Override
                public int size(final int[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final int[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianness.encodeInt(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public int[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int[][] result = new int[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianness.decodeInt(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Integer matrix. */
    protected static final Serializer<Integer[][]> CONVERT_INTEGER_MATRIX =
            new ObjectMatrixSerializer<Integer>(FieldTypes.INT_32_MATRIX, 4, Integer.class, "Integer_32_matrix")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeInt(buffer, offset);
                }
            };

    /** Converter for long matrix. */
    protected static final Serializer<long[][]> CONVERT_LNG_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<long[][]>(FieldTypes.LONG_64_MATRIX, 8, "long_64_matrix", 2)
            {
                @Override
                public int size(final long[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final long[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianness.encodeLong(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public long[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    long[][] result = new long[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianness.decodeLong(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Long matrix. */
    protected static final Serializer<Long[][]> CONVERT_LONG_MATRIX =
            new ObjectMatrixSerializer<Long>(FieldTypes.LONG_64_MATRIX, 8, Long.class, "Long_64_matrix")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeLong(buffer, offset);
                }
            };

    /** Converter for float matrix. */
    protected static final Serializer<float[][]> CONVERT_FLT_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<float[][]>(FieldTypes.FLOAT_32_MATRIX, 4, "float_32_matrix", 2)
            {
                @Override
                public int size(final float[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final float[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianness.encodeFloat(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public float[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    float[][] result = new float[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianness.decodeFloat(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Float matrix. */
    protected static final Serializer<Float[][]> CONVERT_FLOAT_MATRIX =
            new ObjectMatrixSerializer<Float>(FieldTypes.FLOAT_32_MATRIX, 4, Float.class, "Float_32_matrix")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeFloat(buffer, offset);
                }
            };

    /** Converter for double matrix. */
    protected static final Serializer<double[][]> CONVERT_DBL_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<double[][]>(FieldTypes.DOUBLE_64_MATRIX, 8, "double_64_matrix", 2)
            {
                @Override
                public int size(final double[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final double[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianness.encodeDouble(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public double[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    double[][] result = new double[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianness.decodeDouble(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Double matrix. */
    protected static final Serializer<Double[][]> CONVERT_DOUBLE_MATRIX =
            new ObjectMatrixSerializer<Double>(FieldTypes.DOUBLE_64_MATRIX, 8, Double.class, "Double_64_matrix")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    endianness.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return endianness.decodeDouble(buffer, offset);
                }
            };

    /** Converter for boolean matrix. */
    protected static final Serializer<boolean[][]> CONVERT_BOOL_MATRIX =
            new BasicPrimitiveArrayOrMatrixSerializer<boolean[][]>(FieldTypes.BOOLEAN_8_MATRIX, 1, "boolean_8_matrix", 2)
            {
                @Override
                public int size(final boolean[][] matrix)
                {
                    return 8 + getElementSize() * matrix.length * matrix[0].length;
                }

                @Override
                public void serialize(final boolean[][] matrix, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            buffer[pointer.getAndIncrement(getElementSize())] = (byte) (matrix[i][j] ? 1 : 0);
                        }
                    }
                }

                @Override
                public boolean[][] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    boolean[][] result = new boolean[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = buffer[pointer.getAndIncrement(getElementSize())] != 0;
                        }
                    }
                    return result;
                }
            };

    /** Converter for Boolean matrix. */
    protected static final Serializer<Boolean[][]> CONVERT_BOOLEAN_MATRIX =
            new ObjectMatrixSerializer<Boolean>(FieldTypes.BOOLEAN_8_MATRIX, 1, Boolean.class, "Boolean_8_matrix")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset,
                        final Endianness endianness)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset, final Endianness endianness)
                {
                    return buffer[offset] != 0;
                }
            };

    /** Converter for descendants of FloatScalar. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<FloatScalar<?, ?>> CONVERT_DJUNITS_FLOAT_SCALAR = new FloatScalarSerializer();

    /** Converter for descendants of DoubleScalar. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<DoubleScalar<?, ?>> CONVERT_DJUNITS_DOUBLE_SCALAR = new DoubleScalarSerializer();

    /** Converter for descendants of FloatVector. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<FloatVector<?, ?, ?>> CONVERT_DJUNITS_FLOAT_VECTOR = new FloatVectorSerializer();

    /** Converter for descendants of DoubleVector. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<DoubleVector<?, ?, ?>> CONVERT_DJUNITS_DOUBLE_VECTOR = new DoubleVectorSerializer();

    /** Converter for descendants of FloatMatrix. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<FloatMatrix<?, ?, ?, ?>> CONVERT_DJUNITS_FLOAT_MATRIX = new FloatMatrixSerializer();

    /** Converter for descendants of DoubleMatrix. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<DoubleMatrix> CONVERT_DJUNITS_DOUBLE_MATRIX = new DoubleMatrixSerializer();

    /** Serializer for array of FloatVector. Each FloatVector must have same size. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<FloatVector[]> CONVERT_FLOAT_UNIT_COLUMN_VECTOR_ARRAY = new FloatVectorArraySerializer();

    /** Serializer for array of DoubleVector. Each DoubleVector must have same size. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static final Serializer<DoubleVector[]> CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY =
            new DoubleVectorArraySerializer();

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
                    Serializer[] serializers = TypedMessage.buildEncoderList(false, objectArray);
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
                    Serializer[] serializers = TypedMessage.buildEncoderList(false, objectArray);
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
                        Serializer<?> deSerializer = PRIMITIVE_DATA_DECODERS.get(key);
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
                    Serializer[] serializers = TypedMessage.buildEncoderList(true, objectArray);
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
                    Serializer[] serializers = TypedMessage.buildEncoderList(true, objectArray);
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
                        Serializer<?> deSerializer = PRIMITIVE_DATA_DECODERS.get(key);
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

    static
    {
        ENCODERS.put(Byte.class, CONVERT_BYTE);
        ENCODERS.put(byte.class, CONVERT_BYTE);
        ENCODERS.put(Short.class, CONVERT_SHORT);
        ENCODERS.put(short.class, CONVERT_SHORT);
        ENCODERS.put(Integer.class, CONVERT_INTEGER);
        ENCODERS.put(int.class, CONVERT_INTEGER);
        ENCODERS.put(Long.class, CONVERT_LONG);
        ENCODERS.put(long.class, CONVERT_LONG);
        ENCODERS.put(Float.class, CONVERT_FLOAT);
        ENCODERS.put(float.class, CONVERT_FLOAT);
        ENCODERS.put(Double.class, CONVERT_DOUBLE);
        ENCODERS.put(double.class, CONVERT_DOUBLE);
        ENCODERS.put(Boolean.class, CONVERT_BOOLEAN);
        ENCODERS.put(boolean.class, CONVERT_BOOLEAN);
        ENCODERS.put(Byte[].class, CONVERT_BYTE_ARRAY);
        ENCODERS.put(byte[].class, CONVERT_BT_ARRAY);
        ENCODERS.put(Short[].class, CONVERT_SHORT_ARRAY);
        ENCODERS.put(short[].class, CONVERT_SHRT_ARRAY);
        ENCODERS.put(Integer[].class, CONVERT_INTEGER_ARRAY);
        ENCODERS.put(int[].class, CONVERT_INT_ARRAY);
        ENCODERS.put(Long[].class, CONVERT_LONG_ARRAY);
        ENCODERS.put(long[].class, CONVERT_LNG_ARRAY);
        ENCODERS.put(Float[].class, CONVERT_FLOAT_ARRAY);
        ENCODERS.put(float[].class, CONVERT_FLT_ARRAY);
        ENCODERS.put(Double[].class, CONVERT_DOUBLE_ARRAY);
        ENCODERS.put(double[].class, CONVERT_DBL_ARRAY);
        ENCODERS.put(Boolean[].class, CONVERT_BOOLEAN_ARRAY);
        ENCODERS.put(boolean[].class, CONVERT_BOOL_ARRAY);
        ENCODERS.put(Byte[][].class, CONVERT_BYTE_MATRIX);
        ENCODERS.put(byte[][].class, CONVERT_BT_MATRIX);
        ENCODERS.put(Short[][].class, CONVERT_SHORT_MATRIX);
        ENCODERS.put(short[][].class, CONVERT_SHRT_MATRIX);
        ENCODERS.put(Integer[][].class, CONVERT_INTEGER_MATRIX);
        ENCODERS.put(int[][].class, CONVERT_INT_MATRIX);
        ENCODERS.put(Long[][].class, CONVERT_LONG_MATRIX);
        ENCODERS.put(long[][].class, CONVERT_LNG_MATRIX);
        ENCODERS.put(Float[][].class, CONVERT_FLOAT_MATRIX);
        ENCODERS.put(float[][].class, CONVERT_FLT_MATRIX);
        ENCODERS.put(Double[][].class, CONVERT_DOUBLE_MATRIX);
        ENCODERS.put(double[][].class, CONVERT_DBL_MATRIX);
        ENCODERS.put(Boolean[][].class, CONVERT_BOOLEAN_MATRIX);
        ENCODERS.put(boolean[][].class, CONVERT_BOOL_MATRIX);

        PRIMITIVE_DATA_DECODERS.put(CONVERT_BYTE.fieldType(), CONVERT_BYTE);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_CHARACTER8.fieldType(), CONVERT_CHARACTER8);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_CHARACTER16.fieldType(), CONVERT_CHARACTER16);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_SHORT.fieldType(), CONVERT_SHORT);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_INTEGER.fieldType(), CONVERT_INTEGER);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_LONG.fieldType(), CONVERT_LONG);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_FLOAT.fieldType(), CONVERT_FLOAT);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DOUBLE.fieldType(), CONVERT_DOUBLE);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_BOOLEAN.fieldType(), CONVERT_BOOLEAN);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_STRING8.fieldType(), CONVERT_STRING8);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_STRING16.fieldType(), CONVERT_STRING16);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_BT_ARRAY.fieldType(), CONVERT_BT_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_SHRT_ARRAY.fieldType(), CONVERT_SHRT_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_INT_ARRAY.fieldType(), CONVERT_INT_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_LNG_ARRAY.fieldType(), CONVERT_LNG_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_FLT_ARRAY.fieldType(), CONVERT_FLT_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DBL_ARRAY.fieldType(), CONVERT_DBL_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_BOOL_ARRAY.fieldType(), CONVERT_BOOL_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_BT_MATRIX.fieldType(), CONVERT_BT_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_SHRT_MATRIX.fieldType(), CONVERT_SHRT_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_INT_MATRIX.fieldType(), CONVERT_INT_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_LNG_MATRIX.fieldType(), CONVERT_LNG_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_FLT_MATRIX.fieldType(), CONVERT_FLT_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DBL_MATRIX.fieldType(), CONVERT_DBL_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_BOOL_MATRIX.fieldType(), CONVERT_BOOL_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DJUNITS_FLOAT_SCALAR.fieldType(), CONVERT_DJUNITS_FLOAT_SCALAR);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DJUNITS_DOUBLE_SCALAR.fieldType(), CONVERT_DJUNITS_DOUBLE_SCALAR);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DJUNITS_FLOAT_VECTOR.fieldType(), CONVERT_DJUNITS_FLOAT_VECTOR);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DJUNITS_DOUBLE_VECTOR.fieldType(), CONVERT_DJUNITS_DOUBLE_VECTOR);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DJUNITS_FLOAT_MATRIX.fieldType(), CONVERT_DJUNITS_FLOAT_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DJUNITS_DOUBLE_MATRIX.fieldType(), CONVERT_DJUNITS_DOUBLE_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(COMPOUND_ARRAY_SERIALIZER_UTF16.fieldType(), COMPOUND_ARRAY_SERIALIZER_UTF16);
        PRIMITIVE_DATA_DECODERS.put(COMPOUND_ARRAY_SERIALIZER_UTF8.fieldType(), COMPOUND_ARRAY_SERIALIZER_UTF8);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY.fieldType(),
                CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_FLOAT_UNIT_COLUMN_VECTOR_ARRAY.fieldType(), CONVERT_FLOAT_UNIT_COLUMN_VECTOR_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_STRING8_ARRAY.fieldType(), CONVERT_STRING8_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_STRING16_ARRAY.fieldType(), CONVERT_STRING16_ARRAY);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_STRING8_MATRIX.fieldType(), CONVERT_STRING8_MATRIX);
        PRIMITIVE_DATA_DECODERS.put(CONVERT_STRING16_MATRIX.fieldType(), CONVERT_STRING16_MATRIX);

        OBJECT_DECODERS.put(CONVERT_BYTE.fieldType(), CONVERT_BYTE);
        OBJECT_DECODERS.put(CONVERT_CHARACTER8.fieldType(), CONVERT_CHARACTER8);
        OBJECT_DECODERS.put(CONVERT_CHARACTER16.fieldType(), CONVERT_CHARACTER16);
        OBJECT_DECODERS.put(CONVERT_SHORT.fieldType(), CONVERT_SHORT);
        OBJECT_DECODERS.put(CONVERT_INTEGER.fieldType(), CONVERT_INTEGER);
        OBJECT_DECODERS.put(CONVERT_LONG.fieldType(), CONVERT_LONG);
        OBJECT_DECODERS.put(CONVERT_FLOAT.fieldType(), CONVERT_FLOAT);
        OBJECT_DECODERS.put(CONVERT_DOUBLE.fieldType(), CONVERT_DOUBLE);
        OBJECT_DECODERS.put(CONVERT_BOOLEAN.fieldType(), CONVERT_BOOLEAN);
        OBJECT_DECODERS.put(CONVERT_STRING8.fieldType(), CONVERT_STRING8);
        OBJECT_DECODERS.put(CONVERT_STRING16.fieldType(), CONVERT_STRING16);
        OBJECT_DECODERS.put(CONVERT_BYTE_ARRAY.fieldType(), CONVERT_BYTE_ARRAY);
        OBJECT_DECODERS.put(CONVERT_SHORT_ARRAY.fieldType(), CONVERT_SHORT_ARRAY);
        OBJECT_DECODERS.put(CONVERT_INTEGER_ARRAY.fieldType(), CONVERT_INTEGER_ARRAY);
        OBJECT_DECODERS.put(CONVERT_LONG_ARRAY.fieldType(), CONVERT_LONG_ARRAY);
        OBJECT_DECODERS.put(CONVERT_FLOAT_ARRAY.fieldType(), CONVERT_FLOAT_ARRAY);
        OBJECT_DECODERS.put(CONVERT_DOUBLE_ARRAY.fieldType(), CONVERT_DOUBLE_ARRAY);
        OBJECT_DECODERS.put(CONVERT_BOOLEAN_ARRAY.fieldType(), CONVERT_BOOLEAN_ARRAY);
        OBJECT_DECODERS.put(CONVERT_BYTE_MATRIX.fieldType(), CONVERT_BYTE_MATRIX);
        OBJECT_DECODERS.put(CONVERT_SHORT_MATRIX.fieldType(), CONVERT_SHORT_MATRIX);
        OBJECT_DECODERS.put(CONVERT_INTEGER_MATRIX.fieldType(), CONVERT_INTEGER_MATRIX);
        OBJECT_DECODERS.put(CONVERT_LONG_MATRIX.fieldType(), CONVERT_LONG_MATRIX);
        OBJECT_DECODERS.put(CONVERT_FLOAT_MATRIX.fieldType(), CONVERT_FLOAT_MATRIX);
        OBJECT_DECODERS.put(CONVERT_DOUBLE_MATRIX.fieldType(), CONVERT_DOUBLE_MATRIX);
        OBJECT_DECODERS.put(CONVERT_BOOLEAN_MATRIX.fieldType(), CONVERT_BOOLEAN_MATRIX);
        OBJECT_DECODERS.put(CONVERT_DJUNITS_FLOAT_SCALAR.fieldType(), CONVERT_DJUNITS_FLOAT_SCALAR);
        OBJECT_DECODERS.put(CONVERT_DJUNITS_DOUBLE_SCALAR.fieldType(), CONVERT_DJUNITS_DOUBLE_SCALAR);
        OBJECT_DECODERS.put(CONVERT_DJUNITS_FLOAT_VECTOR.fieldType(), CONVERT_DJUNITS_FLOAT_VECTOR);
        OBJECT_DECODERS.put(CONVERT_DJUNITS_DOUBLE_VECTOR.fieldType(), CONVERT_DJUNITS_DOUBLE_VECTOR);
        OBJECT_DECODERS.put(CONVERT_DJUNITS_FLOAT_MATRIX.fieldType(), CONVERT_DJUNITS_FLOAT_MATRIX);
        OBJECT_DECODERS.put(CONVERT_DJUNITS_DOUBLE_MATRIX.fieldType(), CONVERT_DJUNITS_DOUBLE_MATRIX);
        OBJECT_DECODERS.put(COMPOUND_ARRAY_SERIALIZER_UTF16.fieldType(), COMPOUND_ARRAY_SERIALIZER_UTF16);
        OBJECT_DECODERS.put(COMPOUND_ARRAY_SERIALIZER_UTF8.fieldType(), COMPOUND_ARRAY_SERIALIZER_UTF8);
        OBJECT_DECODERS.put(CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY.fieldType(), CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY);
        OBJECT_DECODERS.put(CONVERT_FLOAT_UNIT_COLUMN_VECTOR_ARRAY.fieldType(), CONVERT_FLOAT_UNIT_COLUMN_VECTOR_ARRAY);
        OBJECT_DECODERS.put(CONVERT_STRING8_ARRAY.fieldType(), CONVERT_STRING8_ARRAY);
        OBJECT_DECODERS.put(CONVERT_STRING16_ARRAY.fieldType(), CONVERT_STRING16_ARRAY);
        OBJECT_DECODERS.put(CONVERT_STRING8_MATRIX.fieldType(), CONVERT_STRING8_MATRIX);
        OBJECT_DECODERS.put(CONVERT_STRING16_MATRIX.fieldType(), CONVERT_STRING16_MATRIX);
    }

    /** the UTF-8 charset. */
    protected static final Charset UTF8 = Charset.forName("UTF-8");

    /** the UTF-16 charset, big endian variant. */
    protected static final Charset UTF16 = Charset.forName("UTF-16BE");

    /**
     * Encode the object into a byte[] message. Use UTF8 for potential characters and Strings.
     * @param endianness encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
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
     * @return the zeroMQ message to send as a byte array
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
     * @return the zeroMQ message to send as a byte array
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
        if (object instanceof FloatScalar)
            return CONVERT_DJUNITS_FLOAT_SCALAR;
        if (object instanceof DoubleScalar)
            return CONVERT_DJUNITS_DOUBLE_SCALAR;
        if (object instanceof FloatVector)
            return CONVERT_DJUNITS_FLOAT_VECTOR;
        if (object instanceof DoubleVector)
            return CONVERT_DJUNITS_DOUBLE_VECTOR;
        if (object instanceof FloatMatrix)
            return CONVERT_DJUNITS_FLOAT_MATRIX;
        if (object instanceof DoubleMatrix)
            return CONVERT_DJUNITS_DOUBLE_MATRIX;
        if (object instanceof SerializableObject[])
            return utf8 ? COMPOUND_ARRAY_SERIALIZER_UTF8 : COMPOUND_ARRAY_SERIALIZER_UTF16;
        if (object instanceof DoubleVector[])
            return CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY;
        if (object instanceof FloatVector[])
            return CONVERT_FLOAT_UNIT_COLUMN_VECTOR_ARRAY;
        else
            throw new SerializationException("Unhandled data type " + object.getClass());
    }

    /**
     * Encode the object array into a byte array, conforming to the provided endianness and string encodin.
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param endianness encoder for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
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
        return decode(endianness, buffer, PRIMITIVE_DATA_DECODERS);
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
    public static <U extends Unit<U>> U getUnit(final byte[] buffer, final Pointer pointer, final Endianness endianness)
    {
        QuantityType unitType = QuantityType.getUnitType(buffer[pointer.getAndIncrement(1)]);
        UnitType displayType = UnitType.getDisplayType(unitType, 0 + buffer[pointer.getAndIncrement(1)]);
        return (U) displayType.getDjunitsType();
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
