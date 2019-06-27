package org.djutils.serialization;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.djunits.unit.AreaUnit;
import org.djunits.unit.DurationUnit;
import org.djunits.unit.EnergyUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.MassUnit;
import org.djunits.unit.MoneyPerAreaUnit;
import org.djunits.unit.MoneyPerDurationUnit;
import org.djunits.unit.MoneyPerEnergyUnit;
import org.djunits.unit.MoneyPerLengthUnit;
import org.djunits.unit.MoneyPerMassUnit;
import org.djunits.unit.MoneyPerVolumeUnit;
import org.djunits.unit.MoneyUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.VolumeUnit;
import org.djunits.value.StorageType;
import org.djunits.value.ValueException;
import org.djunits.value.vdouble.matrix.AbstractDoubleMatrix;
import org.djunits.value.vdouble.matrix.DoubleMatrixUtil;
import org.djunits.value.vdouble.scalar.AbstractDoubleScalar;
import org.djunits.value.vdouble.scalar.DoubleScalarUtil;
import org.djunits.value.vdouble.scalar.Money;
import org.djunits.value.vdouble.scalar.MoneyPerArea;
import org.djunits.value.vdouble.scalar.MoneyPerDuration;
import org.djunits.value.vdouble.scalar.MoneyPerEnergy;
import org.djunits.value.vdouble.scalar.MoneyPerLength;
import org.djunits.value.vdouble.scalar.MoneyPerMass;
import org.djunits.value.vdouble.scalar.MoneyPerVolume;
import org.djunits.value.vdouble.vector.AbstractDoubleVector;
import org.djunits.value.vdouble.vector.DoubleVectorUtil;
import org.djunits.value.vfloat.matrix.AbstractFloatMatrix;
import org.djunits.value.vfloat.matrix.FloatMatrixUtil;
import org.djunits.value.vfloat.scalar.AbstractFloatScalar;
import org.djunits.value.vfloat.scalar.FloatMoney;
import org.djunits.value.vfloat.scalar.FloatMoneyPerArea;
import org.djunits.value.vfloat.scalar.FloatMoneyPerDuration;
import org.djunits.value.vfloat.scalar.FloatMoneyPerEnergy;
import org.djunits.value.vfloat.scalar.FloatMoneyPerLength;
import org.djunits.value.vfloat.scalar.FloatMoneyPerMass;
import org.djunits.value.vfloat.scalar.FloatMoneyPerVolume;
import org.djunits.value.vfloat.scalar.FloatScalarUtil;
import org.djunits.value.vfloat.vector.AbstractFloatVector;
import org.djunits.value.vfloat.vector.FloatVectorUtil;
import org.djutils.exceptions.Throw;

/**
 * Message conversions. These take into account the endianness for coding the different values. Java is by default big-endian.
 * <p>
 * Copyright (c) 2016-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Mar 1, 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
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

    /** The easy converters keyed by Class. */
    private static final Map<Class<?>, Serializer<?>> ENCODERS = new HashMap<>();

    /** All the converters that decode into primitive data when possible, keyed by prefix. */
    static final Map<Byte, Serializer<?>> PRIMITIVE_DATA_DECODERS = new HashMap<>();

    /** All the converters that decode into arrays and matrices of Objects, keyed by prefix. */
    private static final Map<Byte, Serializer<?>> OBJECT_DECODERS = new HashMap<>();

    /** Converter for Byte. */
    private static final Serializer<Byte> CONVERT_BYTE = new FixedSizeObjectSerializer<Byte>(FieldTypes.BYTE_8, 1, "Byte_8")
    {
        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            buffer[pointer.getAndIncrement(1)] = (Byte) object;
        }

        @Override
        public Byte deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            return buffer[pointer.getAndIncrement(1)];
        }
    };

    /** Converter for Short. */
    private static final Serializer<Short> CONVERT_SHORT =
            new FixedSizeObjectSerializer<Short>(FieldTypes.SHORT_16, 2, "Short_16")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeShort((Short) object, buffer, pointer.getAndIncrement(2));
                }

                @Override
                public Short deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeShort(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Integer. */
    private static final Serializer<Integer> CONVERT_INTEGER =
            new FixedSizeObjectSerializer<Integer>(FieldTypes.INT_32, 4, "Integer_32")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeInt((Integer) object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Integer deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Integer. */
    private static final Serializer<Long> CONVERT_LONG = new FixedSizeObjectSerializer<Long>(FieldTypes.LONG_64, 8, "Long_64")
    {
        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            endianUtil.encodeLong((Long) object, buffer, pointer.getAndIncrement(8));
        }

        @Override
        public Long deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            return endianUtil.decodeLong(buffer, pointer.getAndIncrement(8));
        }
    };

    /** Converter for Float. */
    private static final Serializer<Float> CONVERT_FLOAT =
            new FixedSizeObjectSerializer<Float>(FieldTypes.FLOAT_32, 4, "Float_32")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeFloat((Float) object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Float deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Double. */
    private static final Serializer<Double> CONVERT_DOUBLE =
            new FixedSizeObjectSerializer<Double>(FieldTypes.DOUBLE_64, 8, "Double_64")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeDouble((Double) object, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public Double deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            };

    /** Converter for Boolean. */
    private static final Serializer<Boolean> CONVERT_BOOLEAN =
            new FixedSizeObjectSerializer<Boolean>(FieldTypes.BOOLEAN_8, 1, "Boolean_8")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    buffer[pointer.getAndIncrement(1)] = (byte) (((Boolean) object) ? 1 : 0);
                }

                @Override
                public Boolean deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return buffer[pointer.getAndIncrement(1)] == 0 ? false : true;
                }
            };

    /** Converter for Character. */
    private static final Serializer<Character> CONVERT_CHARACTER16 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_16, 2, "Char_16")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeChar((Character) object, buffer, pointer.getAndIncrement(size(object)));
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeChar(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Character. */
    private static final Serializer<Character> CONVERT_CHARACTER8 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_8, 1, "Char_8")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil)
                {
                    buffer[pointer.getAndIncrement(size(object))] = (byte) (((Character) object) & 0xFF);
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                {
                    return Character.valueOf((char) buffer[pointer.getAndIncrement(1)]);
                }
            };

    /** Converter for String. */
    private static final Serializer<String> CONVERT_STRING16 = new ObjectSerializer<String>(FieldTypes.STRING_16, "String_16")
    {
        @Override
        public int size(final Object object)
        {
            return 4 + ((String) object).getBytes(UTF16).length;
        }

        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            String string = ((String) object);
            // System.out.println("Encoding string \"" + string + "\"");
            if (endianUtil == EndianUtil.BIG_ENDIAN)
            {
                byte[] s = string.getBytes(UTF16);
                // System.out.print(HexDumper.hexDumper(s));
                endianUtil.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
                for (byte b : s)
                {
                    buffer[pointer.getAndIncrement(1)] = b;
                }
            }
            else
            {
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);
                endianUtil.encodeInt(chars.length * 2, buffer, pointer.getAndIncrement(4));
                // int originalPos = pointer.get();
                for (char c : chars)
                {
                    endianUtil.encodeChar(c, buffer, pointer.getAndIncrement(2));
                }
                // System.out.println("encoded string starts at " + originalPos);
                // System.out.print(HexDumper.hexDumper(buffer));
            }
        }

        @Override
        public String deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            // System.out.println("Input bytes");
            // System.out.print(HexDumper.hexDumper(buffer));
            String s = endianUtil.decodeUTF16String(buffer, pointer.get());
            pointer.getAndIncrement(4 + s.length() * 2);
            return s;
        }
    };

    /** Converter for String. */
    private static final Serializer<String> CONVERT_STRING8 = new ObjectSerializer<String>(FieldTypes.STRING_8, "String_8")
    {
        @Override
        public int size(final Object object)
        {
            return 4 + ((String) object).getBytes(UTF8).length;
        }

        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
        {
            byte[] s = ((String) object).getBytes(UTF8);
            endianUtil.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
            for (byte b : s)
            {
                buffer[pointer.getAndIncrement(1)] = b;
            }
        }

        @Override
        public String deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                throws SerializationException
        {
            int bytesUsed = endianUtil.decodeInt(buffer, pointer.get());
            String s = endianUtil.decodeUTF8String(buffer, pointer.get());
            pointer.getAndIncrement(4 + bytesUsed);
            return s;
        }
    };

    /** Converter for byte array. */
    private static final Serializer<byte[]> CONVERT_BT_ARRAY =
            new BasicPrimitiveArraySerializer<byte[]>(FieldTypes.BYTE_8_ARRAY, 1, "byte_8_array")
            {
                @Override
                public int size(final Object object)
                {
                    byte[] array = (byte[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    byte[] array = (byte[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        array[i] = buffer[pointer.getAndIncrement(getElementSize())];
                    }
                }

                @Override
                public byte[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    byte[] result = new byte[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(getElementSize())];
                    }
                    return result;
                }
            };

    /** Converter for Byte array. */
    private static final Serializer<Byte[]> CONVERT_BYTE_ARRAY =
            new ObjectArraySerializer<Byte>(FieldTypes.BYTE_8_ARRAY, 1, Byte.valueOf((byte) 0), "Byte_8_array")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return buffer[offset];
                }
            };

    /** Converter for short array. */
    private static final Serializer<short[]> CONVERT_SHRT_ARRAY =
            new BasicPrimitiveArraySerializer<short[]>(FieldTypes.SHORT_16_ARRAY, 2, "short_16_array")
            {
                @Override
                public int size(final Object object)
                {
                    short[] array = (short[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    short[] array = (short[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianUtil.encodeShort(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public short[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    short[] result = new short[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianUtil.decodeShort(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Short array. */
    private static final Serializer<Short[]> CONVERT_SHORT_ARRAY =
            new ObjectArraySerializer<Short>(FieldTypes.SHORT_16_ARRAY, 2, Short.valueOf((short) 0), "Short_16_array")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeShort(buffer, offset);
                }
            };

    /** Converter for int array. */
    private static final Serializer<int[]> CONVERT_INT_ARRAY =
            new BasicPrimitiveArraySerializer<int[]>(FieldTypes.INT_32_ARRAY, 4, "int_32_array")
            {
                @Override
                public int size(final Object object)
                {
                    int[] array = (int[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int[] array = (int[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianUtil.encodeInt(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public int[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int[] result = new int[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianUtil.decodeInt(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Integer array. */
    private static final Serializer<Integer[]> CONVERT_INTEGER_ARRAY =
            new ObjectArraySerializer<Integer>(FieldTypes.INT_32_ARRAY, 4, Integer.valueOf(0), "Integer_32_array")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeInt(buffer, offset);
                }
            };

    /** Converter for long array. */
    private static final Serializer<long[]> CONVERT_LNG_ARRAY =
            new BasicPrimitiveArraySerializer<long[]>(FieldTypes.LONG_64_ARRAY, 8, "long_64_array")
            {
                @Override
                public int size(final Object object)
                {
                    long[] array = (long[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    long[] array = (long[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianUtil.encodeLong(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public long[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    long[] result = new long[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianUtil.decodeLong(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Long array. */
    private static final Serializer<Long[]> CONVERT_LONG_ARRAY =
            new ObjectArraySerializer<Long>(FieldTypes.LONG_64_ARRAY, 8, Long.valueOf(0), "Long_64_array")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeLong(buffer, offset);
                }
            };

    /** Converter for float array. */
    private static final Serializer<float[]> CONVERT_FLT_ARRAY =
            new BasicPrimitiveArraySerializer<float[]>(FieldTypes.FLOAT_32_ARRAY, 4, "float_32_array")
            {
                @Override
                public int size(final Object object)
                {
                    float[] array = (float[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    float[] array = (float[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianUtil.encodeFloat(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public float[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    float[] result = new float[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Float array. */
    private static final Serializer<Float[]> CONVERT_FLOAT_ARRAY =
            new ObjectArraySerializer<Float>(FieldTypes.FLOAT_32_ARRAY, 4, new Float(0), "Float_32_array")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeFloat(buffer, offset);
                }
            };

    /** Converter for double array. */
    private static final Serializer<double[]> CONVERT_DBL_ARRAY =
            new BasicPrimitiveArraySerializer<double[]>(FieldTypes.DOUBLE_64_ARRAY, 8, "double_64_array")
            {
                @Override
                public int size(final Object object)
                {
                    double[] array = (double[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    double[] array = (double[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        endianUtil.encodeDouble(array[i], buffer, pointer.getAndIncrement(getElementSize()));
                    }
                }

                @Override
                public double[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    double[] result = new double[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(getElementSize()));
                    }
                    return result;
                }
            };

    /** Converter for Double array. */
    private static final Serializer<Double[]> CONVERT_DOUBLE_ARRAY =
            new ObjectArraySerializer<Double>(FieldTypes.DOUBLE_64_ARRAY, 8, new Double(0), "Double_64_array")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeDouble(buffer, offset);
                }
            };

    /** Converter for boolean array. */
    private static final Serializer<boolean[]> CONVERT_BOOL_ARRAY =
            new BasicPrimitiveArraySerializer<boolean[]>(FieldTypes.BOOLEAN_8_ARRAY, 1, "bool_8_array")
            {
                @Override
                public int size(final Object object)
                {
                    boolean[] array = (boolean[]) object;
                    return 4 + getElementSize() * array.length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    boolean[] array = (boolean[]) object;
                    endianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        buffer[pointer.getAndIncrement(getElementSize())] = (byte) (array[i] ? 1 : 0);
                    }
                }

                @Override
                public boolean[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    boolean[] result = new boolean[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(getElementSize())] == 0 ? false : true;
                    }
                    return result;
                }
            };

    /** Converter for Boolean array. */
    private static final Serializer<Boolean[]> CONVERT_BOOLEAN_ARRAY =
            new ObjectArraySerializer<Boolean>(FieldTypes.BOOLEAN_8_ARRAY, 1, Boolean.FALSE, "Boolean_8_array")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return buffer[offset] == 0 ? false : true;
                }
            };

    /** Converter for byte matrix. */
    private static final Serializer<byte[][]> CONVERT_BT_MATRIX =
            new BasicPrimitiveArraySerializer<byte[][]>(FieldTypes.BYTE_8_MATRIX, 1, "byte_8_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    byte[][] array = (byte[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    byte[][] matrix = (byte[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            matrix[i][j] = buffer[pointer.getAndIncrement(getElementSize())];
                        }
                    }
                }

                @Override
                public byte[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
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
    private static final Serializer<Byte[][]> CONVERT_BYTE_MATRIX =
            new ObjectMatrixSerializer<Byte>(FieldTypes.BYTE_8_MATRIX, 1, new Byte((byte) 0), "Byte_8_matrix")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return buffer[offset];
                }
            };

    /** Converter for short matrix. */
    private static final Serializer<short[][]> CONVERT_SHRT_MATRIX =
            new BasicPrimitiveArraySerializer<short[][]>(FieldTypes.SHORT_16_MATRIX, 2, "short_16_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    short[][] array = (short[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    short[][] matrix = (short[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianUtil.encodeShort(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public short[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    short[][] result = new short[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianUtil.decodeShort(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Short matrix. */
    private static final Serializer<Short[][]> CONVERT_SHORT_MATRIX =
            new ObjectMatrixSerializer<Short>(FieldTypes.SHORT_16_MATRIX, 2, new Short((short) 0), "Short_16_matrix")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeShort(buffer, offset);
                }
            };

    /** Converter for int matrix. */
    private static final Serializer<int[][]> CONVERT_INT_MATRIX =
            new BasicPrimitiveArraySerializer<int[][]>(FieldTypes.INT_32_MATRIX, 4, "int_32_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    int[][] array = (int[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int[][] matrix = (int[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianUtil.encodeInt(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public int[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int[][] result = new int[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianUtil.decodeInt(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Integer matrix. */
    private static final Serializer<Integer[][]> CONVERT_INTEGER_MATRIX =
            new ObjectMatrixSerializer<Integer>(FieldTypes.INT_32_MATRIX, 4, new Integer(0), "Integer_32_matrix")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeInt(buffer, offset);
                }
            };

    /** Converter for long matrix. */
    private static final Serializer<long[][]> CONVERT_LNG_MATRIX =
            new BasicPrimitiveArraySerializer<long[][]>(FieldTypes.LONG_64_MATRIX, 8, "long_64_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    long[][] array = (long[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    long[][] matrix = (long[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianUtil.encodeLong(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public long[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    long[][] result = new long[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianUtil.decodeLong(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Long matrix. */
    private static final Serializer<Long[][]> CONVERT_LONG_MATRIX =
            new ObjectMatrixSerializer<Long>(FieldTypes.LONG_64_MATRIX, 8, new Long(0), "Long_64_matrix")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeLong(buffer, offset);
                }
            };

    /** Converter for float matrix. */
    private static final Serializer<float[][]> CONVERT_FLT_MATRIX =
            new BasicPrimitiveArraySerializer<float[][]>(FieldTypes.FLOAT_32_MATRIX, 4, "float_32_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    float[][] array = (float[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    float[][] matrix = (float[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianUtil.encodeFloat(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public float[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    float[][] result = new float[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Float matrix. */
    private static final Serializer<Float[][]> CONVERT_FLOAT_MATRIX =
            new ObjectMatrixSerializer<Float>(FieldTypes.FLOAT_32_MATRIX, 4, new Float(0), "Float_32_matrix")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeFloat(buffer, offset);
                }
            };

    /** Converter for double matrix. */
    private static final Serializer<double[][]> CONVERT_DBL_MATRIX =
            new BasicPrimitiveArraySerializer<double[][]>(FieldTypes.DOUBLE_64_MATRIX, 8, "double_64_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    double[][] array = (double[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    double[][] matrix = (double[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < height; i++)
                    {
                        Throw.when(matrix[i].length != width, SerializationException.class, "Jagged matrix is not allowed");
                        for (int j = 0; j < width; j++)
                        {
                            endianUtil.encodeDouble(matrix[i][j], buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                }

                @Override
                public double[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    double[][] result = new double[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(getElementSize()));
                        }
                    }
                    return result;
                }
            };

    /** Converter for Double matrix. */
    private static final Serializer<Double[][]> CONVERT_DOUBLE_MATRIX =
            new ObjectMatrixSerializer<Double>(FieldTypes.DOUBLE_64_MATRIX, 8, new Double(0), "Double_64_matrix")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    endianUtil.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return endianUtil.decodeDouble(buffer, offset);
                }
            };

    /** Converter for boolean matrix. */
    private static final Serializer<boolean[][]> CONVERT_BOOL_MATRIX =
            new BasicPrimitiveArraySerializer<boolean[][]>(FieldTypes.BOOLEAN_8_MATRIX, 1, "boolean_8_matrix")
            {
                @Override
                public int size(final Object object)
                {
                    boolean[][] array = (boolean[][]) object;
                    return 8 + getElementSize() * array.length * array[0].length;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    boolean[][] matrix = (boolean[][]) object;
                    int height = matrix.length;
                    int width = matrix[0].length;
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(width, buffer, pointer.getAndIncrement(4));
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
                public boolean[][] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                        throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    boolean[][] result = new boolean[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            result[i][j] = buffer[pointer.getAndIncrement(getElementSize())] == 0 ? false : true;
                        }
                    }
                    return result;
                }
            };

    /** Converter for Boolean matrix. */
    private static final Serializer<Boolean[][]> CONVERT_BOOLEAN_MATRIX =
            new ObjectMatrixSerializer<Boolean>(FieldTypes.BOOLEAN_8_MATRIX, 1, Boolean.FALSE, "Boolean_8_matrix")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset,
                        final EndianUtil endianUtil)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset, final EndianUtil endianUtil)
                {
                    return buffer[offset] == 0 ? false : true;
                }
            };

    /** Converter for descendants of AbstractFloatScalar. */
    private static final Serializer<AbstractFloatScalar<?, ?>> CONVERT_DJUNITS_FLOAT_SCALAR =
            new ObjectSerializer<AbstractFloatScalar<?, ?>>(FieldTypes.FLOAT_32_UNIT, "Djunits_FloatScalar")
            {
                @Override
                public int size(final Object object) throws SerializationException
                {
                    AbstractFloatScalar<?, ?> afs = (AbstractFloatScalar<?, ?>) object;
                    return 2 + extraBytesMoney(afs) + 4;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    AbstractFloatScalar<?, ?> afs = (AbstractFloatScalar<?, ?>) object;
                    encodeUnit(afs.getUnit(), buffer, pointer, endianUtil);
                    float v = afs.si;
                    endianUtil.encodeFloat(v, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public AbstractFloatScalar<?, ?> deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
                    return FloatScalarUtil.instantiateAnonymousSI(endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4)),
                            unit);
                }
            };

    /** Converter for descendants of AbstractDoubleScalar. */
    private static final Serializer<AbstractDoubleScalar<?, ?>> CONVERT_DJUNITS_DOUBLE_SCALAR =
            new ObjectSerializer<AbstractDoubleScalar<?, ?>>(FieldTypes.DOUBLE_64_UNIT, "Djunits_DoubleScalar")
            {
                @Override
                public int size(final Object object) throws SerializationException
                {
                    AbstractDoubleScalar<?, ?> ads = (AbstractDoubleScalar<?, ?>) object;
                    return 2 + extraBytesMoney(ads) + 8;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    AbstractDoubleScalar<?, ?> ads = (AbstractDoubleScalar<?, ?>) object;
                    encodeUnit(ads.getUnit(), buffer, pointer, endianUtil);
                    double v = ads.si;
                    endianUtil.encodeDouble(v, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public AbstractDoubleScalar<?, ?> deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
                    return DoubleScalarUtil.instantiateAnonymousSI(endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8)),
                            unit);
                }
            };

    /** Converter for descendants of AbstractFloatVector. */
    private static final Serializer<AbstractFloatVector<?, ?>> CONVERT_DJUNITS_FLOAT_VECTOR =
            new ObjectSerializer<AbstractFloatVector<?, ?>>(FieldTypes.FLOAT_32_UNIT_ARRAY, "Djunits_FloatVector")
            {
                @Override
                public int size(final Object object) throws SerializationException
                {
                    try
                    {
                        AbstractFloatVector<?, ?> afv = (AbstractFloatVector<?, ?>) object;
                        return 4 + 2 + extraBytesMoney(afv.get(0)) + 4 * afv.size();
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    try
                    {
                        AbstractFloatVector<?, ?> afv = (AbstractFloatVector<?, ?>) object;
                        endianUtil.encodeInt(afv.size(), buffer, pointer.getAndIncrement(4));
                        encodeUnit(afv.getUnit(), buffer, pointer, endianUtil);
                        for (int i = 0; i < afv.size(); i++)
                        {
                            endianUtil.encodeFloat(afv.get(i).si, buffer, pointer.getAndIncrement(4));
                        }
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public AbstractFloatVector<?, ?> deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
                    float[] array = new float[size];
                    for (int i = 0; i < size; i++)
                    {
                        array[i] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
                    }
                    try
                    {
                        return FloatVectorUtil.instantiateAnonymousSI(array, unit, StorageType.DENSE);
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
            };

    /** Converter for descendants of AbstractDoubleVector. */
    private static final Serializer<AbstractDoubleVector<?, ?>> CONVERT_DJUNITS_DOUBLE_VECTOR =
            new ObjectSerializer<AbstractDoubleVector<?, ?>>(FieldTypes.DOUBLE_64_UNIT_ARRAY, "Djunits_DoubleVector")
            {
                @Override
                public int size(final Object object) throws SerializationException
                {
                    try
                    {
                        AbstractDoubleVector<?, ?> adv = (AbstractDoubleVector<?, ?>) object;
                        return 4 + 2 + extraBytesMoney(adv.get(0)) + 8 * adv.size();
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    try
                    {
                        AbstractDoubleVector<?, ?> adv = (AbstractDoubleVector<?, ?>) object;
                        endianUtil.encodeInt(adv.size(), buffer, pointer.getAndIncrement(4));
                        encodeUnit(adv.getUnit(), buffer, pointer, endianUtil);
                        for (int i = 0; i < adv.size(); i++)
                        {
                            endianUtil.encodeDouble(adv.get(i).si, buffer, pointer.getAndIncrement(8));
                        }
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public AbstractDoubleVector<?, ?> deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
                    double[] array = new double[size];
                    for (int i = 0; i < size; i++)
                    {
                        array[i] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                    }
                    try
                    {
                        return DoubleVectorUtil.instantiateAnonymousSI(array, unit, StorageType.DENSE);
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
            };

    /** Converter for descendants of AbstractFloatMatrix. */
    private static final Serializer<AbstractFloatMatrix<?, ?>> CONVERT_DJUNITS_FLOAT_MATRIX =
            new ObjectSerializer<AbstractFloatMatrix<?, ?>>(FieldTypes.FLOAT_32_UNIT_MATRIX, "Djunits_FloatMatrix")
            {
                @Override
                public int size(final Object object) throws SerializationException
                {
                    try
                    {
                        AbstractFloatMatrix<?, ?> afm = (AbstractFloatMatrix<?, ?>) object;
                        return 4 + 4 + 2 + extraBytesMoney(afm.get(0, 0)) + 4 * afm.rows() * afm.columns();
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    try
                    {
                        AbstractFloatMatrix<?, ?> afm = (AbstractFloatMatrix<?, ?>) object;
                        endianUtil.encodeInt(afm.rows(), buffer, pointer.getAndIncrement(4));
                        endianUtil.encodeInt(afm.columns(), buffer, pointer.getAndIncrement(4));
                        encodeUnit(afm.getUnit(), buffer, pointer, endianUtil);
                        for (int i = 0; i < afm.rows(); i++)
                        {
                            for (int j = 0; j < afm.columns(); j++)
                            {
                                endianUtil.encodeFloat(afm.get(i, j).si, buffer, pointer.getAndIncrement(4));
                            }
                        }
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public AbstractFloatMatrix<?, ?> deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
                    float[][] array = new float[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            array[i][j] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
                        }
                    }
                    try
                    {
                        return FloatMatrixUtil.instantiateAnonymousSI(array, unit, StorageType.DENSE);
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
            };

    /** Converter for descendants of AbstractDoubleMatrix. */
    private static final Serializer<AbstractDoubleMatrix<?, ?>> CONVERT_DJUNITS_DOUBLE_MATRIX =
            new ObjectSerializer<AbstractDoubleMatrix<?, ?>>(FieldTypes.DOUBLE_64_UNIT_MATRIX, "Djunits_DoubleMatrix")
            {
                @Override
                public int size(final Object object) throws SerializationException
                {
                    try
                    {
                        AbstractDoubleMatrix<?, ?> adm = (AbstractDoubleMatrix<?, ?>) object;
                        return 4 + 4 + 2 + extraBytesMoney(adm.get(0, 0)) + 8 * adm.rows() * adm.columns();
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    try
                    {
                        AbstractDoubleMatrix<?, ?> adm = (AbstractDoubleMatrix<?, ?>) object;
                        endianUtil.encodeInt(adm.rows(), buffer, pointer.getAndIncrement(4));
                        endianUtil.encodeInt(adm.columns(), buffer, pointer.getAndIncrement(4));
                        encodeUnit(adm.getUnit(), buffer, pointer, endianUtil);
                        for (int i = 0; i < adm.rows(); i++)
                        {
                            for (int j = 0; j < adm.columns(); j++)
                            {
                                endianUtil.encodeDouble(adm.get(i, j).si, buffer, pointer.getAndIncrement(8));
                            }
                        }
                    }
                    catch (ValueException e)
                    {
                        throw new SerializationException(e);
                    }
                }

                @Override
                public AbstractDoubleMatrix<?, ?> deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    try
                    {
                        int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
                        double[][] array = new double[height][width];
                        for (int i = 0; i < height; i++)
                        {
                            for (int j = 0; j < width; j++)
                            {
                                array[i][j] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                            }
                        }
                        return DoubleMatrixUtil.instantiateAnonymousSI(array, unit, StorageType.DENSE);
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
            };

    /** Serializer for array of DoubleVector. Each DoubleVector must have same size. */
    private static final Serializer<AbstractDoubleVector<?, ?>[]> CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY =
            new ObjectSerializer<AbstractDoubleVector<?, ?>[]>(FieldTypes.DOUBLE_64_UNIT_COLUMN_ARRAY, "Djunits_vector_array")
            {

                @Override
                public int size(final Object object) throws SerializationException
                {
                    int result = 4 + 4;
                    AbstractDoubleVector<?, ?>[] adva = (AbstractDoubleVector<?, ?>[]) object;
                    int width = adva.length;
                    int height = adva[0].size();
                    for (int i = 0; i < width; i++)
                    {
                        AbstractDoubleVector<?, ?> adv = adva[i];
                        Throw.when(adv.size() != height, SerializationException.class,
                                "All AbstractDoubleVectors in array must have same size");
                        try
                        {
                            result += 2 + extraBytesMoney(adv.get(0));
                        }
                        catch (ValueException e)
                        {
                            throw new SerializationException(e);
                        }
                    }
                    result += height * width * 8;
                    return result;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    AbstractDoubleVector<?, ?>[] adva = (AbstractDoubleVector<?, ?>[]) object;
                    int width = adva.length;
                    int height = adva[0].size();
                    endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(adva.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < width; i++)
                    {
                        AbstractDoubleVector<?, ?> adv = adva[i];
                        Throw.when(adv.size() != height, SerializationException.class,
                                "All AbstractDoubleVectors in array must have same size");
                        encodeUnit(adv.getUnit(), buffer, pointer, endianUtil);
                    }
                    for (int row = 0; row < height; row++)
                    {
                        for (int col = 0; col < width; col++)
                        {
                            try
                            {
                                endianUtil.encodeDouble(adva[col].getSI(row), buffer, pointer.getAndIncrement(8));
                            }
                            catch (ValueException e)
                            {
                                throw new SerializationException(e);
                            }
                        }
                    }

                }

                @Override
                public AbstractDoubleVector<?, ?>[] deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    AbstractDoubleVector<?, ?>[] result = new AbstractDoubleVector<?, ?>[width];
                    Unit<? extends Unit<?>>[] units = new Unit<?>[width];
                    for (int col = 0; col < width; col++)
                    {
                        units[col] = getUnit(buffer, pointer, endianUtil);
                    }
                    double[][] values = new double[width][height];
                    for (int row = 0; row < height; row++)
                    {
                        for (int col = 0; col < width; col++)
                        {
                            values[col][row] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                        }
                    }
                    for (int col = 0; col < width; col++)
                    {
                        try
                        {
                            result[col] = DoubleVectorUtil.instantiateAnonymousSI(values[col], units[col], StorageType.DENSE);
                        }
                        catch (ValueException e)
                        {
                            throw new SerializationException(e);
                        }
                    }
                    return result;
                }
            };

    /** Converter for array of SerializebleObject using UTF16 for strings and characters. */
    private static final Serializer<SerializableObject<?>[]> COMPOUND_ARRAY_SERIALIZER_UTF16 =
            new ObjectSerializer<SerializableObject<?>[]>((byte) 120, "Compound")
            {

                @Override
                public int size(final Object object) throws SerializationException
                {
                    int result = 4 + 4;
                    SerializableObject<?>[] objects = (SerializableObject[]) object;
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    Serializer<?>[] serializers = buildEncoderList(false, objectArray);
                    result += serializers.length;
                    // TODO this assumes that objectArray does not contain money types that use a multi-byte field type
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        result += objects.length * serializers[i].size(objectArray[i]);
                    }
                    return result;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    SerializableObject<?>[] objects = (SerializableObject[]) object;
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    endianUtil.encodeInt(objects.length, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(objectArray.length, buffer, pointer.getAndIncrement(4));
                    Serializer<?>[] serializers = buildEncoderList(false, objectArray);
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
                            serializers[j].serialize(row.get(j), buffer, pointer, endianUtil);
                        }
                    }
                }

                @Override
                public SerializableObject<?>[] deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int arraySize = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int fieldCount = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
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
                            element.add(deSerializers[j].deSerialize(buffer, pointer, endianUtil));
                        }
                        result[i] = new MinimalSerializableObject(element);
                    }
                    return result;
                }
            };

    /** Converter for array of SerializebleObject using UTF8 for strings and characters. */
    private static final Serializer<SerializableObject<?>[]> COMPOUND_ARRAY_SERIALIZER_UTF8 =
            new ObjectSerializer<SerializableObject<?>[]>((byte) 121, "Compound")
            {

                @Override
                public int size(final Object object) throws SerializationException
                {
                    int result = 4 + 4;
                    SerializableObject<?>[] objects = (SerializableObject[]) object;
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    Serializer<?>[] serializers = buildEncoderList(true, objectArray);
                    result += serializers.length;
                    // TODO this assumes that objectArray does not contain money types that use a multi-byte field type
                    for (int i = 0; i < objectArray.length; i++)
                    {
                        result += objects.length * serializers[i].size(objectArray[i]);
                    }
                    return result;
                }

                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    SerializableObject<?>[] objects = (SerializableObject[]) object;
                    SerializableObject<?> so = objects[0];
                    Object[] objectArray = so.exportAsList().toArray();
                    endianUtil.encodeInt(objects.length, buffer, pointer.getAndIncrement(4));
                    endianUtil.encodeInt(objectArray.length, buffer, pointer.getAndIncrement(4));
                    Serializer<?>[] serializers = buildEncoderList(true, objectArray);
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
                            serializers[j].serialize(row.get(j), buffer, pointer, endianUtil);
                        }
                    }
                }

                @Override
                public SerializableObject<?>[] deSerialize(final byte[] buffer, final Pointer pointer,
                        final EndianUtil endianUtil) throws SerializationException
                {
                    int arraySize = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int fieldCount = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
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
                            element.add(deSerializers[j].deSerialize(buffer, pointer, endianUtil));
                        }
                        result[i] = new MinimalSerializableObject(element);
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

    }

    /** the UTF-8 charset. */
    protected static final Charset UTF8 = Charset.forName("UTF-8");

    /** the UTF-16 charset, big endian variant. */
    protected static final Charset UTF16 = Charset.forName("UTF-16BE");

    /**
     * Encode the object array into a byte[] message. Use UTF8 for the characters and for the String.
     * @param endianUtil EndianUtil; encoder to use for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF8(final EndianUtil endianUtil, final Object... content) throws SerializationException
    {
        return encode(true, endianUtil, content);
    }

    /**
     * Encode the object array into a byte[] message. Use UTF16 for the characters and for the String.
     * @param endianUtil EndianUtil; encoder for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF16(final EndianUtil endianUtil, final Object... content) throws SerializationException
    {
        return encode(false, endianUtil, content);
    }

    /**
     * Find the serializer for one object.
     * @param utf8 boolean; if true; use UTF8 encoding for characters and Strings; if false; use UTF16 encoding for characters
     *            and Strings
     * @param object Object; the object for which the serializer must be returned
     * @return Serializer; the serializer needed for <code>object</object>
     * @throws SerializationException when there is no known serializer for <code>object</code>
     */
    static Serializer<?> findEncoder(final boolean utf8, final Object object) throws SerializationException
    {
        Serializer<?> serializer = ENCODERS.get(object.getClass());
        if (serializer != null)
        {
            return serializer;
        }
        else if (object instanceof Character)
        {
            return utf8 ? CONVERT_CHARACTER8 : CONVERT_CHARACTER16;
        }
        else if (object instanceof String)
        {
            return utf8 ? CONVERT_STRING8 : CONVERT_STRING16;
        }
        else if (object instanceof AbstractFloatScalar)
        {
            return CONVERT_DJUNITS_FLOAT_SCALAR;
        }
        else if (object instanceof AbstractDoubleScalar)
        {
            return CONVERT_DJUNITS_DOUBLE_SCALAR;
        }
        else if (object instanceof AbstractFloatVector)
        {
            return CONVERT_DJUNITS_FLOAT_VECTOR;
        }
        else if (object instanceof AbstractDoubleVector)
        {
            return CONVERT_DJUNITS_DOUBLE_VECTOR;
        }
        else if (object instanceof AbstractFloatMatrix)
        {
            return CONVERT_DJUNITS_FLOAT_MATRIX;
        }
        else if (object instanceof AbstractDoubleMatrix)
        {
            return CONVERT_DJUNITS_DOUBLE_MATRIX;
        }
        else if (object instanceof SerializableObject[])
        {
            return utf8 ? COMPOUND_ARRAY_SERIALIZER_UTF8 : COMPOUND_ARRAY_SERIALIZER_UTF16;
        }
        else if (object instanceof AbstractDoubleVector[])
        {
            return CONVERT_DOUBLE_UNIT_COLUMN_VECTOR_ARRAY;
        }
        else
        {
            throw new SerializationException("Unhandled data type " + object.getClass());
        }
    }
    
    /**
     * Build the list of serializers corresponding to the data in an Object array.
     * @param utf8 boolean; if true; use UTF8 encoding for characters and Strings; if false; use UTF16 encoding for characters
     *            and Strings
     * @param content Object[]; the objects for which the serializers must be returned
     * @return Serializer[]; array filled with the serializers needed for the objects in the Object array
     * @throws SerializationException when an object in <code>content</code> cannot be serialized
     */
    static Serializer<?>[] buildEncoderList(final boolean utf8, final Object... content) throws SerializationException
    {
        Serializer<?>[] result = new Serializer[content.length];
        for (int i = 0; i < content.length; i++)
        {
            Object object = content[i];
            result[i] = findEncoder(utf8, object);
        }

        return result;
    }

    /**
     * Encode the object array into a Big Endian message.
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param endianUtil EndianUtil; encoder for multi-byte values
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    private static byte[] encode(final boolean utf8, final EndianUtil endianUtil, final Object... content)
            throws SerializationException
    {
        Serializer<?>[] serializers = buildEncoderList(utf8, content);
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
            serializers[i].serializeWithPrefix(content[i], message, pointer, endianUtil);
            // System.out.println("Expected increment: " + serializers[i].size(content[i]));
            // System.out.println(pointer);
        }
        Throw.when(pointer.get() != message.length, SerializationException.class, "Data size error (reserved %d, used %d)",
                message.length, pointer.get());
        return message;
    }

    /**
     * Code a unit, including MoneyUnits.
     * @param unit the unit to code in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     * @param endianUtil EndianUtil; encoder to use for multi-byte values
     */
    @SuppressWarnings("rawtypes")
    static void encodeUnit(final Unit unit, final byte[] message, final Pointer pointer, final EndianUtil endianUtil)
    {
        @SuppressWarnings("unchecked") // TODO see how this can be solved with type <U extends Unit<U>>
        SerializationUnits unitType = SerializationUnits.getUnitType(unit);
        message[pointer.getAndIncrement(1)] = unitType.getCode();
        if (unit instanceof MoneyUnit)
        {
            @SuppressWarnings("unchecked")
            DisplayType displayType = DisplayType.getDisplayType(unit);
            endianUtil.encodeShort((short) displayType.getIntCode(), message, pointer.getAndIncrement(2));
        }
        else if (unit instanceof MoneyPerAreaUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerAreaUnit) unit).getMoneyUnit());
            endianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerAreaUnit) unit).getAreaUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerEnergyUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerEnergyUnit) unit).getMoneyUnit());
            endianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerEnergyUnit) unit).getEnergyUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerLengthUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerLengthUnit) unit).getMoneyUnit());
            endianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerLengthUnit) unit).getLengthUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerMassUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerMassUnit) unit).getMoneyUnit());
            endianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerMassUnit) unit).getMassUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerDurationUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerDurationUnit) unit).getMoneyUnit());
            endianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerDurationUnit) unit).getDurationUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerVolumeUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerVolumeUnit) unit).getMoneyUnit());
            endianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerVolumeUnit) unit).getVolumeUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else
        {
            @SuppressWarnings("unchecked")
            DisplayType displayType = DisplayType.getDisplayType(unit);
            message[pointer.getAndIncrement(1)] = displayType.getByteCode();
        }
    }

    /**
     * Decode the 2-byte Money unit in the message (code 100).
     * @param moneyCode Short; the money code
     * @return Unit; decoded money unit
     */
    private static Unit<? extends Unit<?>> decodeMoneyUnit(final Integer moneyCode)
    {
        DisplayType displayType = DisplayType.getDisplayType(SerializationUnits.MONEY, moneyCode);
        return displayType.getDjunitsType();
    }

    /**
     * Decode the 2-byte MoneyPerUnit unit in the message (code 101 - 106).
     * @param unitType the unit type (e.g., MoneyPerArea)
     * @param moneyCode Short; the 16-bit money code
     * @param perCode Integer; the code for the reciprocal
     * @return decoded MoneyPerUnit unit
     */
    @SuppressWarnings("checkstyle:needbraces")
    private static Unit<? extends Unit<?>> decodeMoneyPerUnit(final SerializationUnits unitType, final Integer moneyCode,
            final Integer perCode)
    {
        DisplayType moneyDisplayType = DisplayType.getDisplayType(SerializationUnits.MONEY, moneyCode);
        DisplayType perDisplayType;
        if (unitType.getCode() == 101)
            perDisplayType = DisplayType.getDisplayType(SerializationUnits.AREA, perCode);
        else if (unitType.getCode() == 102)
            perDisplayType = DisplayType.getDisplayType(SerializationUnits.ENERGY, perCode);
        else if (unitType.getCode() == 103)
            perDisplayType = DisplayType.getDisplayType(SerializationUnits.LENGTH, perCode);
        else if (unitType.getCode() == 104)
            perDisplayType = DisplayType.getDisplayType(SerializationUnits.MASS, perCode);
        else if (unitType.getCode() == 105)
            perDisplayType = DisplayType.getDisplayType(SerializationUnits.DURATION, perCode);
        else if (unitType.getCode() == 106)
            perDisplayType = DisplayType.getDisplayType(SerializationUnits.VOLUME, perCode);
        else
            throw new RuntimeException(new SerializationException("Unknown MoneyPerUnit type with code " + unitType.getCode()));
        return moneyPerUnitType(moneyDisplayType, perDisplayType);
    }

    /** The MoneyPerUnit cache stores the instantiated types so they are not created again and again. */
    private static Map<MoneyUnit, Map<Unit<?>, Unit<?>>> moneyPerUnitCache = new HashMap<>();

    /**
     * Return the cached or created moneyPerUnitType.
     * @param moneyDisplayType the money type to use, e.g. USD
     * @param perDisplayType the per-unit to use, e.g. SQUARE_METER
     * @return the cached or created moneyPerUnitType
     */
    public static Unit<?> moneyPerUnitType(final DisplayType moneyDisplayType, final DisplayType perDisplayType)
    {
        Map<Unit<?>, Unit<?>> moneyMap = moneyPerUnitCache.get(moneyDisplayType.getDjunitsType());
        if (moneyMap == null)
        {
            moneyMap = new HashMap<>();
            moneyPerUnitCache.put((MoneyUnit) moneyDisplayType.getDjunitsType(), moneyMap);
        }
        Unit<?> moneyPerUnitType = moneyMap.get(perDisplayType.getDjunitsType());
        if (moneyPerUnitType != null)
        {
            return moneyPerUnitType;
        }
        String name = moneyDisplayType.getName() + "/" + perDisplayType.getName();
        String abbreviation = moneyDisplayType.getAbbreviation() + "/" + perDisplayType.getAbbreviation();
        if (perDisplayType.getUnitType().equals(SerializationUnits.AREA))
        {
            moneyPerUnitType = new MoneyPerAreaUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (AreaUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(SerializationUnits.ENERGY))
        {
            moneyPerUnitType = new MoneyPerEnergyUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (EnergyUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(SerializationUnits.LENGTH))
        {
            moneyPerUnitType = new MoneyPerLengthUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (LengthUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(SerializationUnits.MASS))
        {
            moneyPerUnitType = new MoneyPerMassUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (MassUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(SerializationUnits.DURATION))
        {
            moneyPerUnitType = new MoneyPerDurationUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (DurationUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(SerializationUnits.VOLUME))
        {
            moneyPerUnitType = new MoneyPerVolumeUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (VolumeUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else
        {
            throw new RuntimeException(new SerializationException("Unknown moneyPerUnit type: " + name));
        }
        moneyMap.put(perDisplayType.getDjunitsType(), moneyPerUnitType);
        return moneyPerUnitType;
    }

    /**
     * Retrieve and decode a DJUNITS unit.
     * @param buffer byte[]; the encoded data
     * @param pointer Pointer; position in the encoded data where the unit is to be decoded from
     * @param endianUtil EndianUtil; decoder for multi-byte values
     * @return Unit
     */
    static Unit<? extends Unit<?>> getUnit(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        SerializationUnits unitType = SerializationUnits.getUnitType(buffer[pointer.getAndIncrement(1)]);
        if (unitType.getCode() == 100) // money
        {
            int moneyCode = endianUtil.decodeShort(buffer, pointer.getAndIncrement(2));
            return decodeMoneyUnit(moneyCode);
        }
        else if (unitType.getCode() >= 101 && unitType.getCode() <= 106)
        {
            int moneyCode = endianUtil.decodeShort(buffer, pointer.getAndIncrement(2));
            return decodeMoneyPerUnit(unitType, moneyCode, 0 + buffer[pointer.getAndIncrement(1)]);
        }
        else
        {
            DisplayType displayType = DisplayType.getDisplayType(unitType, 0 + buffer[pointer.getAndIncrement(1)]);
            return displayType.getDjunitsType();
        }
    }

    /**
     * Decode the message into an object array, constructing Java Primitive data arrays and matrices where possible.
     * @param buffer the byte array to decode
     * @param endianUtil EndianUtil; decoder for multi-byte values
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object[] decodeToPrimitiveDataTypes(final byte[] buffer, final EndianUtil endianUtil)
            throws SerializationException
    {
        return decode(buffer, PRIMITIVE_DATA_DECODERS, endianUtil);
    }

    /**
     * Decode the message into an object array, constructing Java Object arrays and matrices where possible.
     * @param buffer the byte array to decode
     * @param endianUtil EndianUtil; decoder for multi-byte values
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    public static Object[] decodeToObjectDataTypes(final byte[] buffer, final EndianUtil endianUtil)
            throws SerializationException
    {
        return decode(buffer, OBJECT_DECODERS, endianUtil);
    }

    /**
     * Decode the message into an object array.
     * @param buffer the byte array to decode
     * @param decoderMap Map&lt;Byte, Serializer&lt;?&gt;&gt;; the map with decoders to use
     * @param endianUtil EndianUtil; decoder for multi-byte values
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({ "checkstyle:methodlength", "checkstyle:needbraces" })
    public static Object[] decode(final byte[] buffer, final Map<Byte, Serializer<?>> decoderMap, final EndianUtil endianUtil)
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
                // System.out.println("Applying deserializer for " + serializer.dataClassName());
                list.add(serializer.deSerialize(buffer, pointer, endianUtil));
            }
        }
        return list.toArray();
    }

    /**
     * Indicate whether extra bytes are needed for a Money per quantity type.
     * @param o the object to check
     * @return 1 or 2 to indicate whether an extra byte is needed
     */
    static int extraBytesMoney(final Object o)
    {
        if (o instanceof Money)
        {
            return 1;
        }
        else if (o instanceof MoneyPerArea || o instanceof MoneyPerEnergy || o instanceof MoneyPerLength
                || o instanceof MoneyPerMass || o instanceof MoneyPerDuration || o instanceof MoneyPerVolume)
        {
            return 2;
        }
        else if (o instanceof FloatMoney)
        {
            return 1;
        }
        else if (o instanceof FloatMoneyPerArea || o instanceof FloatMoneyPerEnergy || o instanceof FloatMoneyPerLength
                || o instanceof FloatMoneyPerMass || o instanceof FloatMoneyPerDuration || o instanceof FloatMoneyPerVolume)
        {
            return 2;
        }
        return 0;
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
         * @param list List&lt;Object&gt;; the object list that is returned by <code>exportAsList</code> method
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
