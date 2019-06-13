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
 * Copyright (c) 2016-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Mar 1, 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class TypedMessage
{
    /** All the converters keyed by Class. */
    static final private Map<Class<?>, Serializer<?>> encoders = new HashMap<>();

    /** All the converters keyed by prefix. */
    static final private Map<Byte, Serializer<?>> decoders = new HashMap<>();

    /** Converter for Byte. */
    static final private Serializer<Byte> convertByte = new FixedSizeObjectSerializer<Byte>(FieldTypes.BYTE_8, 1, "Byte_8")
    {
        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
        {
            buffer[pointer.getAndIncrement(1)] = (Byte) object;
        }

        @Override
        public Byte deSerialize(final byte[] buffer, final Pointer pointer)
        {
            return buffer[pointer.getAndIncrement(1)];
        }
    };

    /** Converter for Short. */
    static final private Serializer<Short> convertShort =
            new FixedSizeObjectSerializer<Short>(FieldTypes.SHORT_16, 2, "Short_16")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    EndianUtil.encodeShort((Short) object, buffer, pointer.getAndIncrement(2));
                }

                @Override
                public Short deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return EndianUtil.decodeShort(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Integer. */
    static final private Serializer<Integer> convertInteger =
            new FixedSizeObjectSerializer<Integer>(FieldTypes.INT_32, 4, "Int_32")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    EndianUtil.encodeInt((Integer) object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Integer deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Integer. */
    static final private Serializer<Long> convertLong = new FixedSizeObjectSerializer<Long>(FieldTypes.LONG_64, 8, "Long_64")
    {
        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
        {
            EndianUtil.encodeLong((Long) object, buffer, pointer.getAndIncrement(8));
        }

        @Override
        public Long deSerialize(final byte[] buffer, final Pointer pointer)
        {
            return EndianUtil.decodeLong(buffer, pointer.getAndIncrement(8));
        }
    };

    /** Converter for Float. */
    static final private Serializer<Float> convertFloat =
            new FixedSizeObjectSerializer<Float>(FieldTypes.FLOAT_32, 4, "Float_32")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    EndianUtil.encodeFloat((Float) object, buffer, pointer.getAndIncrement(4));
                }

                @Override
                public Float deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return EndianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
                }
            };

    /** Converter for Double. */
    static final private Serializer<Double> convertDouble =
            new FixedSizeObjectSerializer<Double>(FieldTypes.DOUBLE_64, 8, "Double_64")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    EndianUtil.encodeDouble((Double) object, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public Double deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return EndianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            };

    /** Converter for Boolean. */
    static final private Serializer<Boolean> convertBoolean =
            new FixedSizeObjectSerializer<Boolean>(FieldTypes.BOOLEAN_8, 1, "Boolean_8")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    buffer[pointer.getAndIncrement(1)] = (byte) (((Boolean) object) ? 1 : 0);
                }

                @Override
                public Boolean deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return buffer[pointer.getAndIncrement(1)] == 0 ? false : true;
                }
            };

    /** Converter for Character. */
    static final private Serializer<Character> convertCharacter16 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_16, 2, "Char_16")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    EndianUtil.encodeChar((Character) object, buffer, pointer.getAndIncrement(size(object)));
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return EndianUtil.decodeChar(buffer, pointer.getAndIncrement(2));
                }
            };

    /** Converter for Character. */
    static final private Serializer<Character> convertCharacter8 =
            new FixedSizeObjectSerializer<Character>(FieldTypes.CHAR_8, 1, "Char_8")
            {
                @Override
                public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
                {
                    buffer[pointer.getAndIncrement(size(object))] = (byte) (((Character) object) & 0xFF);
                }

                @Override
                public Character deSerialize(final byte[] buffer, final Pointer pointer)
                {
                    return new Character((char) buffer[pointer.getAndIncrement(1)]);
                }
            };

    /** Converter for String. */
    static final private Serializer<String> convertString16 = new ObjectSerializer<String>(FieldTypes.STRING_16, "String_16")
    {
        @Override
        public int size(final Object object)
        {
            return 4 + ((String) object).getBytes(UTF16).length;
        }

        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
        {
            byte[] s = ((String) object).getBytes(UTF16);
            EndianUtil.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
            for (byte b : s)
            {
                buffer[pointer.getAndIncrement(1)] = b;
            }
        }

        @Override
        public String deSerialize(final byte[] buffer, final Pointer pointer)
        {
            String s = EndianUtil.decodeUTF16String(buffer, pointer.get());
            pointer.getAndIncrement(4 + s.length() * 2);
            return s;
        }
    };

    /** Converter for String. */
    static final private Serializer<String> convertString8 = new ObjectSerializer<String>(FieldTypes.STRING_8, "String_8")
    {
        @Override
        public int size(final Object object)
        {
            return 4 + ((String) object).getBytes(UTF8).length;
        }

        @Override
        public void serialize(final Object object, final byte[] buffer, final Pointer pointer)
        {
            byte[] s = ((String) object).getBytes(UTF8);
            EndianUtil.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
            for (byte b : s)
            {
                buffer[pointer.getAndIncrement(1)] = b;
            }
        }

        @Override
        public String deSerialize(final byte[] buffer, final Pointer pointer) throws SerializationException
        {
            int bytesUsed = EndianUtil.decodeInt(buffer, pointer.get());
            String s = EndianUtil.decodeUTF8String(buffer, pointer.get());
            pointer.getAndIncrement(4 + bytesUsed);
            return s;
        }
    };

    /** Converter for byte array. */
    static final private Serializer<byte[]> convertBytArray =
            new BasicPrimitiveArraySerializer<byte[]>(FieldTypes.BYTE_8_ARRAY, 1, "byte_8_array")
            {
                @Override
                public final int size(final Object object)
                {
                    byte[] array = (byte[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    byte[] array = (byte[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        array[i] = buffer[pointer.getAndIncrement(dataSize())];
                    }
                }

                @Override
                public byte[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    byte[] result = new byte[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(dataSize())];
                    }
                    return result;
                }
            };

    /** Converter for Byte array. */
    static final private Serializer<Byte[]> convertByteArray =
            new ObjectArraySerializer<Byte>(FieldTypes.BYTE_8_ARRAY, 1, new Byte((byte) 0), "Byte_8_array")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset)
                {
                    return buffer[offset];
                }
            };

    /** Converter for short array. */
    static final private Serializer<short[]> convertShrtArray =
            new BasicPrimitiveArraySerializer<short[]>(FieldTypes.SHORT_16_ARRAY, 2, "short_16_array")
            {
                @Override
                public final int size(final Object object)
                {
                    short[] array = (short[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    short[] array = (short[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        EndianUtil.encodeShort(array[i], buffer, pointer.getAndIncrement(dataSize()));
                    }
                }

                @Override
                public short[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    short[] result = new short[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = EndianUtil.decodeShort(buffer, pointer.getAndIncrement(dataSize()));
                    }
                    return result;
                }
            };

    /** Converter for Short array. */
    static final private Serializer<Short[]> convertShortArray =
            new ObjectArraySerializer<Short>(FieldTypes.SHORT_16_ARRAY, 2, new Short((short) 0), "Short_16_array")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeShort(buffer, offset);
                }
            };

    /** Converter for int array. */
    static final private Serializer<int[]> convertIntArray =
            new BasicPrimitiveArraySerializer<int[]>(FieldTypes.INT_32_ARRAY, 4, "int_32_array")
            {
                @Override
                public final int size(final Object object)
                {
                    int[] array = (int[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int[] array = (int[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        EndianUtil.encodeInt(array[i], buffer, pointer.getAndIncrement(dataSize()));
                    }
                }

                @Override
                public int[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    int[] result = new int[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(dataSize()));
                    }
                    return result;
                }
            };

    /** Converter for Integer array. */
    static final private Serializer<Integer[]> convertIntegerArray =
            new ObjectArraySerializer<Integer>(FieldTypes.INT_32_ARRAY, 4, new Integer(0), "Integer_32_array")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeInt(buffer, offset);
                }
            };

    /** Converter for long array. */
    static final private Serializer<long[]> convertLngArray =
            new BasicPrimitiveArraySerializer<long[]>(FieldTypes.LONG_64_ARRAY, 8, "long_64_array")
            {
                @Override
                public final int size(final Object object)
                {
                    long[] array = (long[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    long[] array = (long[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        EndianUtil.encodeLong(array[i], buffer, pointer.getAndIncrement(dataSize()));
                    }
                }

                @Override
                public long[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    long[] result = new long[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = EndianUtil.decodeLong(buffer, pointer.getAndIncrement(dataSize()));
                    }
                    return result;
                }
            };

    /** Converter for Long array. */
    static final private Serializer<Long[]> convertLongArray =
            new ObjectArraySerializer<Long>(FieldTypes.LONG_64_ARRAY, 8, new Long(0), "Long_64_array")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeLong(buffer, offset);
                }
            };

    /** Converter for float array. */
    static final private Serializer<float[]> convertFltArray =
            new BasicPrimitiveArraySerializer<float[]>(FieldTypes.FLOAT_32_ARRAY, 4, "float_32_array")
            {
                @Override
                public final int size(final Object object)
                {
                    float[] array = (float[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    float[] array = (float[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        EndianUtil.encodeFloat(array[i], buffer, pointer.getAndIncrement(dataSize()));
                    }
                }

                @Override
                public float[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    float[] result = new float[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = EndianUtil.decodeFloat(buffer, pointer.getAndIncrement(dataSize()));
                    }
                    return result;
                }
            };

    /** Converter for Float array. */
    static final private Serializer<Float[]> convertFloatArray =
            new ObjectArraySerializer<Float>(FieldTypes.FLOAT_32_ARRAY, 4, new Float(0), "Float_32_array")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeFloat(buffer, offset);
                }
            };

    /** Converter for double array. */
    static final private Serializer<double[]> convertDblArray =
            new BasicPrimitiveArraySerializer<double[]>(FieldTypes.DOUBLE_64_ARRAY, 8, "double_64_array")
            {
                @Override
                public final int size(final Object object)
                {
                    double[] array = (double[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    double[] array = (double[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        EndianUtil.encodeDouble(array[i], buffer, pointer.getAndIncrement(dataSize()));
                    }
                }

                @Override
                public double[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    double[] result = new double[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = EndianUtil.decodeDouble(buffer, pointer.getAndIncrement(dataSize()));
                    }
                    return result;
                }
            };

    /** Converter for Double array. */
    static final private Serializer<Double[]> convertDoubleArray =
            new ObjectArraySerializer<Double>(FieldTypes.DOUBLE_64_ARRAY, 8, new Double(0), "Double_64_array")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeDouble(buffer, offset);
                }
            };

    /** Converter for boolean array. */
    static final private Serializer<boolean[]> convertBoolArray =
            new BasicPrimitiveArraySerializer<boolean[]>(FieldTypes.BOOLEAN_8_ARRAY, 1, "bool_8_array")
            {
                @Override
                public final int size(final Object object)
                {
                    boolean[] array = (boolean[]) object;
                    return 4 + dataSize() * array.length;
                }

                @Override
                public void serialize(Object object, byte[] buffer, Pointer pointer) throws SerializationException
                {
                    boolean[] array = (boolean[]) object;
                    EndianUtil.encodeInt(array.length, buffer, pointer.getAndIncrement(4));
                    for (int i = 0; i < array.length; i++)
                    {
                        buffer[pointer.getAndIncrement(dataSize())] = (byte) (array[i] ? 1 : 0);
                    }
                }

                @Override
                public boolean[] deSerialize(byte[] buffer, Pointer pointer) throws SerializationException
                {
                    int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                    boolean[] result = new boolean[size];
                    for (int i = 0; i < size; i++)
                    {
                        result[i] = buffer[pointer.getAndIncrement(dataSize())] == 0 ? false : true;
                    }
                    return result;
                }
            };

    /** Converter for Boolean array. */
    static final private Serializer<Boolean[]> convertBooleanArray =
            new ObjectArraySerializer<Boolean>(FieldTypes.BOOLEAN_8_ARRAY, 1, new Boolean(false), "Boolean_8_array")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset)
                {
                    return buffer[offset] == 0 ? false : true;
                }
            };

    /** Converter for Byte array. */
    static final private Serializer<Byte[][]> convertByteMatrix =
            new ObjectMatrixSerializer<Byte>(FieldTypes.BYTE_8_MATRIX, 1, new Byte[0][0], "Byte_8_matrix")
            {
                @Override
                public void serializeElement(final Byte object, final byte[] buffer, final int offset)
                {
                    buffer[offset] = object;
                }

                @Override
                public Byte deSerializeElement(final byte[] buffer, final int offset)
                {
                    return buffer[offset];
                }
            };

    /** Converter for Short array. */
    static final private Serializer<Short[][]> convertShortMatrix =
            new ObjectMatrixSerializer<Short>(FieldTypes.SHORT_16_MATRIX, 2, new Short[0][0], "Short_16_matrix")
            {
                @Override
                public void serializeElement(final Short object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeShort(object, buffer, offset);
                }

                @Override
                public Short deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeShort(buffer, offset);
                }
            };

    /** Converter for Integer array. */
    static final private Serializer<Integer[][]> convertIntegerMatrix =
            new ObjectMatrixSerializer<Integer>(FieldTypes.INT_32_MATRIX, 4, new Integer[0][0], "Int_32_matrix")
            {
                @Override
                public void serializeElement(final Integer object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeInt(object, buffer, offset);
                }

                @Override
                public Integer deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeInt(buffer, offset);
                }
            };

    /** Converter for Long array. */
    static final private Serializer<Long[][]> convertLongMatrix =
            new ObjectMatrixSerializer<Long>(FieldTypes.LONG_64_MATRIX, 8, new Long[0][0], "Long_64_matrix")
            {
                @Override
                public void serializeElement(final Long object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeLong(object, buffer, offset);
                }

                @Override
                public Long deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeLong(buffer, offset);
                }
            };

    /** Converter for Float array. */
    static final private Serializer<Float[][]> convertFloatMatrix =
            new ObjectMatrixSerializer<Float>(FieldTypes.FLOAT_32_MATRIX, 4, new Float[0][0], "Float_32_matrix")
            {
                @Override
                public void serializeElement(final Float object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeFloat(object, buffer, offset);
                }

                @Override
                public Float deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeFloat(buffer, offset);
                }
            };

    /** Converter for Float array. */
    static final private Serializer<Double[][]> convertDoubleMatrix =
            new ObjectMatrixSerializer<Double>(FieldTypes.DOUBLE_64_MATRIX, 8, new Double[0][0], "Double_64_matrix")
            {
                @Override
                public void serializeElement(final Double object, final byte[] buffer, final int offset)
                {
                    EndianUtil.encodeDouble(object, buffer, offset);
                }

                @Override
                public Double deSerializeElement(final byte[] buffer, final int offset)
                {
                    return EndianUtil.decodeDouble(buffer, offset);
                }
            };

    /** Converter for Boolean array. */
    static final private Serializer<Boolean[][]> convertBooleanMatrix =
            new ObjectMatrixSerializer<Boolean>(FieldTypes.BOOLEAN_8_MATRIX, 1, new Boolean[0][0], "Boolean_8_matrix")
            {
                @Override
                public void serializeElement(final Boolean object, final byte[] buffer, final int offset)
                {
                    buffer[offset] = (byte) (object ? 1 : 0);
                }

                @Override
                public Boolean deSerializeElement(final byte[] buffer, final int offset)
                {
                    return buffer[offset] == 0 ? false : true;
                }
            };

    static
    {
        encoders.put(Byte.class, convertByte);
        encoders.put(byte.class, convertByte);
        encoders.put(Short.class, convertShort);
        encoders.put(short.class, convertShort);
        encoders.put(Integer.class, convertInteger);
        encoders.put(int.class, convertInteger);
        encoders.put(Long.class, convertLong);
        encoders.put(long.class, convertLong);
        encoders.put(Float.class, convertFloat);
        encoders.put(float.class, convertFloat);
        encoders.put(Double.class, convertDouble);
        encoders.put(double.class, convertDouble);
        encoders.put(Boolean.class, convertBoolean);
        encoders.put(boolean.class, convertBoolean);
        encoders.put(Byte[].class, convertByteArray);
        encoders.put(byte[].class, convertBytArray);
        encoders.put(Short[].class, convertShortArray);
        encoders.put(short[].class, convertShrtArray);
        encoders.put(Integer[].class, convertIntegerArray);
        encoders.put(int[].class, convertIntArray);
        encoders.put(Long[].class, convertLongArray);
        encoders.put(long[].class, convertLngArray);
        encoders.put(Float[].class, convertFloatArray);
        encoders.put(float[].class, convertFltArray);
        encoders.put(Double[].class, convertDoubleArray);
        encoders.put(double[].class, convertDblArray);
        encoders.put(Boolean[].class, convertBooleanArray);
        encoders.put(boolean[].class, convertBoolArray);
        encoders.put(Byte[][].class, convertByteMatrix);
        encoders.put(byte[][].class, convertByteMatrix);
        encoders.put(Short[][].class, convertShortMatrix);
        encoders.put(short[][].class, convertShortMatrix);
        encoders.put(Integer[][].class, convertIntegerMatrix);
        encoders.put(int[][].class, convertIntegerMatrix);
        encoders.put(Long[][].class, convertLongMatrix);
        encoders.put(long[][].class, convertLongMatrix);
        encoders.put(Float[][].class, convertFloatMatrix);
        encoders.put(float[][].class, convertFloatMatrix);
        encoders.put(Double[][].class, convertDoubleMatrix);
        encoders.put(double[][].class, convertDoubleMatrix);
        encoders.put(Boolean[][].class, convertBooleanMatrix);
        encoders.put(boolean[][].class, convertBooleanMatrix);

        decoders.put(convertByte.fieldType(), convertByte);
        decoders.put(convertCharacter8.fieldType(), convertCharacter8);
        decoders.put(convertCharacter16.fieldType(), convertCharacter16);
        decoders.put(convertShort.fieldType(), convertShort);
        decoders.put(convertInteger.fieldType(), convertInteger);
        decoders.put(convertLong.fieldType(), convertLong);
        decoders.put(convertFloat.fieldType(), convertFloat);
        decoders.put(convertDouble.fieldType(), convertDouble);
        decoders.put(convertBoolean.fieldType(), convertBoolean);
        decoders.put(convertString8.fieldType(), convertString8);
        decoders.put(convertString16.fieldType(), convertString16);
        decoders.put(convertBytArray.fieldType(), convertBytArray);
        decoders.put(convertShrtArray.fieldType(), convertShrtArray);
        decoders.put(convertIntArray.fieldType(), convertIntArray);
        decoders.put(convertLngArray.fieldType(), convertLngArray);
        decoders.put(convertFltArray.fieldType(), convertFltArray);
        decoders.put(convertDblArray.fieldType(), convertDblArray);
        decoders.put(convertBoolArray.fieldType(), convertBoolArray);
        decoders.put(convertByteMatrix.fieldType(), convertByteMatrix);
        decoders.put(convertShortMatrix.fieldType(), convertShortMatrix);
        decoders.put(convertIntegerMatrix.fieldType(), convertIntegerMatrix);
        decoders.put(convertLongMatrix.fieldType(), convertLongMatrix);
        decoders.put(convertFloatMatrix.fieldType(), convertFloatMatrix);
        decoders.put(convertDoubleMatrix.fieldType(), convertDoubleMatrix);
        decoders.put(convertBooleanMatrix.fieldType(), convertBooleanMatrix);
    }

    /** the UTF-8 charset. */
    protected static final Charset UTF8 = Charset.forName("UTF-8");

    /** the UTF-16 charset, big endian variant. */
    protected static final Charset UTF16 = Charset.forName("UTF-16BE");

    /**
     * Do not instantiate this utility class.
     */
    private TypedMessage()
    {
        // Utility class; do not instantiate.
    }

    /**
     * Encode the object array into a byte[] message. Use UTF8 for the characters and for the String.
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF8(final Object... content) throws SerializationException
    {
        return encode(true, false, content);
    }

    /**
     * Encode the object array into a byte[] message. Use UTF16 for the characters and for the String.
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encodeUTF16(final Object... content) throws SerializationException
    {
        return encode(false, false, content);
    }

    /**
     * Encode the object array into a byte[] message. Use UTF16 for the characters and for the String. Make sure the first field
     * is a String and always encoded as UTF8 (for the "SIM##" magic number at the start).
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    public static byte[] encode0MQMessageUTF16(final Object... content) throws SerializationException
    {
        Throw.when(content.length == 0, SerializationException.class, "empty array to encode");
        Throw.when(!(content[1] instanceof String), SerializationException.class, "first field in array is not a String");
        return encode(false, false, content);
    }

    /**
     * Encode the object array into a Big Endian message.
     * @param utf8 whether to encode String fields and characters in utf8 or not
     * @param firstUtf8 whether to encode the first String field in utf8 or not
     * @param content the objects to encode
     * @return the zeroMQ message to send as a byte array
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({ "checkstyle:methodlength", "checkstyle:needbraces" })
    private static byte[] encode(final boolean utf8, final boolean firstUtf8, final Object... content)
            throws SerializationException
    {
        // Pass one: compute total size
        int size = 0;
        for (int i = 0; i < content.length; i++)
        {
            Object object = content[i];
            Serializer<?> serializer = encoders.get(object.getClass());
            if (serializer != null)
            {
                size += serializer.sizeWithPrefix(object);
            }
            else if (object instanceof Character)
            {
                size += utf8 ? convertCharacter8.sizeWithPrefix(object) : convertCharacter16.sizeWithPrefix(object);
            }
            else if (object instanceof String)
            {
                size += utf8 ? convertString8.sizeWithPrefix(object) : convertString16.sizeWithPrefix(object);
            }
            else
            {
                if (object instanceof AbstractFloatScalar)
                    size += 6 + extraBytesMoney(content[i]);
                else if (object instanceof AbstractDoubleScalar)
                    size += 10 + extraBytesMoney(content[i]);
                else if (object instanceof AbstractFloatVector)
                {
                    AbstractFloatVector<?, ?> afv = (AbstractFloatVector<?, ?>) object;
                    try
                    {
                        size += 4 + 2 + extraBytesMoney(afv.get(0)) + 4 * afv.size();
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (object instanceof AbstractDoubleVector)
                {
                    AbstractDoubleVector<?, ?> adv = (AbstractDoubleVector<?, ?>) object;
                    try
                    {
                        size += 4 + 2 + extraBytesMoney(adv.get(0)) + 8 * adv.size();
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (object instanceof AbstractFloatMatrix)
                {
                    AbstractFloatMatrix<?, ?> afm = (AbstractFloatMatrix<?, ?>) object;
                    try
                    {
                        size += 4 + 4 + 2 + extraBytesMoney(afm.get(0, 0)) + 4 * afm.rows() * afm.columns();
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (object instanceof AbstractDoubleMatrix)
                {
                    AbstractDoubleMatrix<?, ?> adm = (AbstractDoubleMatrix<?, ?>) object;
                    try
                    {
                        size += 4 + 4 + 2 + extraBytesMoney(adm.get(0, 0)) + 8 * adm.rows() * adm.columns();
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (object instanceof AbstractFloatVector[])
                {
                    AbstractFloatVector<?, ?>[] afvArray = (AbstractFloatVector<?, ?>[]) object;
                    try
                    {
                        size += 4 + 4; // rows, cols
                        for (int j = 0; j < afvArray.length; j++)
                        {
                            size += 2 + extraBytesMoney(afvArray[j].get(0)) + 4 * afvArray[j].size();
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (object instanceof AbstractDoubleVector[])
                {
                    AbstractDoubleVector<?, ?>[] afvArray = (AbstractDoubleVector<?, ?>[]) object;
                    try
                    {
                        size += 4 + 4; // rows, cols
                        for (int j = 0; j < afvArray.length; j++)
                        {
                            size += 2 + extraBytesMoney(afvArray[j].get(0)) + 8 * afvArray[j].size();
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else
                    throw new SerializationException(
                            "Unknown data type " + object.getClass() + " for encoding the ZeroMQ message");
            }
        }
        // Allocate buffer
        byte[] message = new byte[size];
        // Pass 2 fill buffer
        Pointer pointer = new Pointer();

        for (int i = 0; i < content.length; i++)
        {
            Object object = content[i];
            Serializer<?> serializer = encoders.get(object.getClass());
            if (serializer != null)
            {
                serializer.serializeWithPrefix(object, message, pointer);
            }
            else
            {
                if (object instanceof Character)
                {
                    if (utf8)
                    {
                        convertCharacter8.serializeWithPrefix(object, message, pointer);
                    }
                    else
                    {
                        convertCharacter16.serializeWithPrefix(object, message, pointer);
                    }
                }
                else if (object instanceof String)
                {
                    if (utf8)
                    {
                        convertString8.serializeWithPrefix(object, message, pointer);
                    }
                    else
                    {
                        convertString16.serializeWithPrefix(object, message, pointer);
                    }
                }
                else if (object instanceof AbstractFloatScalar)
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.FLOAT_32_UNIT;
                    encodeUnit(((AbstractFloatScalar<?, ?>) object).getUnit(), message, pointer);
                    float v = ((AbstractFloatScalar<?, ?>) object).si;
                    EndianUtil.encodeFloat(v, message, pointer.getAndIncrement(4));
                }
                else if (content[i] instanceof AbstractDoubleScalar)
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.DOUBLE_64_UNIT;
                    encodeUnit(((AbstractDoubleScalar<?, ?>) object).getUnit(), message, pointer);
                    double v = ((AbstractDoubleScalar<?, ?>) object).si;
                    EndianUtil.encodeDouble(v, message, pointer.getAndIncrement(8));
                }
                else if (content[i] instanceof AbstractFloatVector)
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.FLOAT_32_UNIT_ARRAY;
                    AbstractFloatVector<?, ?> afv = (AbstractFloatVector<?, ?>) content[i];
                    EndianUtil.encodeInt(afv.size(), message, pointer.getAndIncrement(4));
                    encodeUnit(afv.getUnit(), message, pointer);
                    try
                    {
                        for (int j = 0; j < afv.size(); j++)
                        {
                            EndianUtil.encodeFloat(afv.getSI(j), message, pointer.getAndIncrement(4));
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (content[i] instanceof AbstractDoubleVector)
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.DOUBLE_64_UNIT_ARRAY;
                    AbstractDoubleVector<?, ?> adv = (AbstractDoubleVector<?, ?>) content[i];
                    EndianUtil.encodeInt(adv.size(), message, pointer.getAndIncrement(1));
                    encodeUnit(adv.getUnit(), message, pointer);
                    try
                    {
                        for (int j = 0; j < adv.size(); j++)
                        {
                            EndianUtil.encodeDouble(adv.getSI(j), message, pointer.getAndIncrement(8));
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (content[i] instanceof AbstractFloatMatrix)
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.FLOAT_32_UNIT_MATRIX;
                    AbstractFloatMatrix<?, ?> afm = (AbstractFloatMatrix<?, ?>) content[i];
                    EndianUtil.encodeInt(afm.rows(), message, pointer.getAndIncrement(4));
                    EndianUtil.encodeInt(afm.columns(), message, pointer.getAndIncrement(4));
                    encodeUnit(afm.getUnit(), message, pointer);
                    try
                    {
                        for (int row = 0; row < afm.rows(); row++)
                        {
                            for (int col = 0; col < afm.columns(); col++)
                            {
                                EndianUtil.encodeFloat(afm.getSI(row, col), message, pointer.getAndIncrement(4));
                            }
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (content[i] instanceof AbstractDoubleMatrix)
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.DOUBLE_64_UNIT_MATRIX;
                    AbstractDoubleMatrix<?, ?> adm = (AbstractDoubleMatrix<?, ?>) content[i];
                    EndianUtil.encodeInt(adm.rows(), message, pointer.getAndIncrement(4));
                    EndianUtil.encodeInt(adm.columns(), message, pointer.getAndIncrement(4));
                    encodeUnit(adm.getUnit(), message, pointer);
                    try
                    {
                        for (int row = 0; row < adm.rows(); row++)
                        {
                            for (int col = 0; col < adm.columns(); col++)
                            {
                                EndianUtil.encodeDouble(adm.getSI(row, col), message, pointer.getAndIncrement(8));
                            }
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (content[i] instanceof AbstractFloatVector[])
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.FLOAT_32_UNIT_COLUMN_ARRAY;
                    AbstractFloatVector<?, ?>[] afvArray = (AbstractFloatVector<?, ?>[]) content[i];
                    EndianUtil.encodeInt(afvArray[0].size(), message, pointer.getAndIncrement(4)); // rows
                    EndianUtil.encodeInt(afvArray.length, message, pointer.getAndIncrement(4)); // cols
                    for (int col = 0; col < afvArray.length; col++)
                    {
                        encodeUnit(afvArray[col].getUnit(), message, pointer);
                    }
                    try
                    {
                        for (int row = 0; row < afvArray[0].size(); row++)
                        {
                            for (int col = 0; col < afvArray.length; col++)
                            {
                                EndianUtil.encodeFloat(afvArray[col].getSI(row), message, pointer.getAndIncrement(4));
                            }
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else if (content[i] instanceof AbstractDoubleVector[])
                {
                    message[pointer.getAndIncrement(1)] = FieldTypes.DOUBLE_64_UNIT_COLUMN_ARRAY;
                    AbstractDoubleVector<?, ?>[] advArray = (AbstractDoubleVector<?, ?>[]) content[i];
                    EndianUtil.encodeInt(advArray[0].size(), message, pointer.getAndIncrement(4)); // rows
                    EndianUtil.encodeInt(advArray.length, message, pointer.getAndIncrement(4)); // cols
                    for (int col = 0; col < advArray.length; col++)
                    {
                        encodeUnit(advArray[col].getUnit(), message, pointer);
                    }
                    try
                    {
                        for (int row = 0; row < advArray[0].size(); row++)
                        {
                            for (int col = 0; col < advArray.length; col++)
                            {
                                EndianUtil.encodeDouble(advArray[col].getSI(row), message, pointer.getAndIncrement(8));
                            }
                        }
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                else
                    throw new SerializationException(
                            "Unknown data type " + content[i].getClass() + " for encoding the ZeroMQ message");
            }
        }
        return message;
    }

    /**
     * Code a unit, including MoneyUnits.
     * @param unit the unit to code in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     */
    @SuppressWarnings("rawtypes")
    private static void encodeUnit(final Unit unit, final byte[] message, final Pointer pointer)
    {
        @SuppressWarnings("unchecked") // TODO see how this can be solved with type <U extends Unit<U>>
        SerializationUnits unitType = SerializationUnits.getUnitType(unit);
        message[pointer.getAndIncrement(1)] = unitType.getCode();
        if (unit instanceof MoneyUnit)
        {
            @SuppressWarnings("unchecked")
            DisplayType displayType = DisplayType.getDisplayType(unit);
            EndianUtil.encodeShort((short) displayType.getIntCode(), message, pointer.getAndIncrement(2));
        }
        else if (unit instanceof MoneyPerAreaUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerAreaUnit) unit).getMoneyUnit());
            EndianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerAreaUnit) unit).getAreaUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerEnergyUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerEnergyUnit) unit).getMoneyUnit());
            EndianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerEnergyUnit) unit).getEnergyUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerLengthUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerLengthUnit) unit).getMoneyUnit());
            EndianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerLengthUnit) unit).getLengthUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerMassUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerMassUnit) unit).getMoneyUnit());
            EndianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerMassUnit) unit).getMassUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerDurationUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerDurationUnit) unit).getMoneyUnit());
            EndianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
            DisplayType perType = DisplayType.getDisplayType(((MoneyPerDurationUnit) unit).getDurationUnit());
            message[pointer.getAndIncrement(1)] = perType.getByteCode();
        }
        else if (unit instanceof MoneyPerVolumeUnit)
        {
            DisplayType moneyType = DisplayType.getDisplayType(((MoneyPerVolumeUnit) unit).getMoneyUnit());
            EndianUtil.encodeShort((short) moneyType.getIntCode(), message, pointer.getAndIncrement(2));
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
        DisplayType displayType = DisplayType.getDisplayType(UnitType.MONEY, moneyCode);
        return displayType.getDjunitsType();
    }

    /**
     * Decode the 2-byte MoneyPerUnit unit in the message (code 101 - 106).
     * @param unitType the unit type (e.g., MoneyPerArea)
     * @param moneyCode Short; the 16-bit money code
     * @param perCode
     * @return decoded MoneyPerUnit unit
     */
    @SuppressWarnings("checkstyle:needbraces")
    private static Unit<? extends Unit<?>> decodeMoneyPerUnit(final UnitType unitType, final Integer moneyCode,
            final Integer perCode)
    {
        DisplayType moneyDisplayType = DisplayType.getDisplayType(UnitType.MONEY, moneyCode);
        DisplayType perDisplayType;
        if (unitType.getCode() == 101)
            perDisplayType = DisplayType.getDisplayType(UnitType.AREA, perCode);
        else if (unitType.getCode() == 102)
            perDisplayType = DisplayType.getDisplayType(UnitType.ENERGY, perCode);
        else if (unitType.getCode() == 103)
            perDisplayType = DisplayType.getDisplayType(UnitType.LENGTH, perCode);
        else if (unitType.getCode() == 104)
            perDisplayType = DisplayType.getDisplayType(UnitType.MASS, perCode);
        else if (unitType.getCode() == 105)
            perDisplayType = DisplayType.getDisplayType(UnitType.DURATION, perCode);
        else if (unitType.getCode() == 106)
            perDisplayType = DisplayType.getDisplayType(UnitType.VOLUME, perCode);
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
        if (perDisplayType.getUnitType().equals(UnitType.AREA))
        {
            moneyPerUnitType = new MoneyPerAreaUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (AreaUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(UnitType.ENERGY))
        {
            moneyPerUnitType = new MoneyPerEnergyUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (EnergyUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(UnitType.LENGTH))
        {
            moneyPerUnitType = new MoneyPerLengthUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (LengthUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(UnitType.MASS))
        {
            moneyPerUnitType = new MoneyPerMassUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (MassUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(UnitType.DURATION))
        {
            moneyPerUnitType = new MoneyPerDurationUnit((MoneyUnit) moneyDisplayType.getDjunitsType(),
                    (DurationUnit) perDisplayType.getDjunitsType(), name, abbreviation);
        }
        else if (perDisplayType.getUnitType().equals(UnitType.VOLUME))
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
     * @return Unit
     */
    static private Unit<? extends Unit<?>> getUnit(final byte[] buffer, final Pointer pointer)
    {
        UnitType unitType = UnitType.getUnitType(buffer[pointer.getAndIncrement(1)]);
        int moneyCode = EndianUtil.decodeShort(buffer, pointer.getAndIncrement(2));
        if (unitType.getCode() == 100) // money
        {
            return decodeMoneyUnit(moneyCode);
        }
        else if (unitType.getCode() >= 101 && unitType.getCode() <= 106)
        {
            return decodeMoneyPerUnit(unitType, moneyCode, 0 + buffer[pointer.getAndIncrement(1)]);
        }
        else
        {
            DisplayType displayType = DisplayType.getDisplayType(unitType, 0 + buffer[pointer.getAndIncrement(1)]);
            return displayType.getDjunitsType();
        }
    }

    /**
     * Decode the message into an object array.
     * @param buffer the ZeroMQ byte array to decode
     * @return an array of objects of the right type
     * @throws SerializationException on unknown data type
     */
    @SuppressWarnings({ "checkstyle:methodlength", "checkstyle:needbraces" })
    public static Object[] decode(final byte[] buffer) throws SerializationException
    {
        List<Object> list = new ArrayList<>();
        Pointer pointer = new Pointer();
        while (pointer.get() < buffer.length)
        {
            Byte fieldType = buffer[pointer.getAndIncrement(1)];
            Serializer<?> serializer = decoders.get(fieldType);
            if (null != serializer)
            {
                System.out.println("Applying deserializer for " + serializer.dataClassName());
                list.add(serializer.deSerialize(buffer, pointer));
            }
            else
            {
                switch (fieldType)
                {
                    case FieldTypes.FLOAT_32_UNIT:
                    {
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer);
                        list.add(FloatScalarUtil
                                .instantiateAnonymousSI(EndianUtil.decodeFloat(buffer, pointer.getAndIncrement(4)), unit));
                    }
                        break;

                    case FieldTypes.DOUBLE_64_UNIT:
                    {
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer);
                        list.add(DoubleScalarUtil
                                .instantiateAnonymousSI(EndianUtil.decodeDouble(buffer, pointer.getAndIncrement(8)), unit));
                    }
                        break;

                    case FieldTypes.FLOAT_32_UNIT_ARRAY:
                    {
                        int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer);
                        float[] array = new float[size];
                        for (int i = 0; i < size; i++)
                        {
                            array[i] = EndianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
                        }
                        try
                        {
                            list.add(FloatVectorUtil.instantiateAnonymousSI(array, unit, StorageType.DENSE));
                        }
                        catch (ValueException exception)
                        {
                            throw new SerializationException(exception);
                        }
                    }
                        break;

                    case FieldTypes.DOUBLE_64_UNIT_ARRAY:
                    {
                        int size = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer);
                        double[] array = new double[size];
                        for (int i = 0; i < size; i++)
                        {
                            array[i] = EndianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                        }
                        try
                        {
                            list.add(DoubleVectorUtil.instantiateAnonymousSI(array, unit, StorageType.DENSE));
                        }
                        catch (ValueException exception)
                        {
                            throw new SerializationException(exception);
                        }
                    }
                        break;

                    case FieldTypes.FLOAT_32_UNIT_MATRIX:
                    {
                        int height = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        int width = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer);
                        float[][] matrix = new float[height][width];
                        for (int i = 0; i < height; i++)
                        {
                            for (int j = 0; j < width; j++)
                            {
                                matrix[i][j] = EndianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
                            }
                        }
                        try
                        {
                            list.add(FloatMatrixUtil.instantiateAnonymousSI(matrix, unit, StorageType.DENSE));
                        }
                        catch (ValueException exception)
                        {
                            throw new SerializationException(exception);
                        }
                    }
                        break;

                    case FieldTypes.DOUBLE_64_UNIT_MATRIX:
                    {
                        int height = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        int width = EndianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
                        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer);
                        double[][] matrix = new double[height][width];
                        for (int i = 0; i < height; i++)
                        {
                            for (int j = 0; j < width; j++)
                            {
                                matrix[i][j] = EndianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                            }
                        }
                        try
                        {
                            list.add(DoubleMatrixUtil.instantiateAnonymousSI(matrix, unit, StorageType.DENSE));
                        }
                        catch (ValueException exception)
                        {
                            throw new SerializationException(exception);
                        }
                    }
                        break;

                    default:
                        throw new SerializationException("Bad FieldType: " + fieldType + " at position " + (pointer.get() - 1));
                }

                /*- Confusing rows and colums. This can't possibly be correct.
                else if (type == FieldTypes.FLOAT_32_UNIT_COLUMN_ARRAY)
                {
                    int rows = mb.getInt();
                    int cols = mb.getInt();
                    Unit<? extends Unit<?>>[] units = new Unit<?>[cols];
                    AbstractFloatVector<?, ?>[] vArray = new AbstractFloatVector[cols];
                    for (int col = 0; col < cols; col++)
                    {
                        units[col] = mb.getUnit();
                    }
                    // here we use a column-first matrix (!) for storage
                    float[][] matrix = new float[cols][rows];
                    for (int row = 0; row < rows; row++)
                    {
                        for (int col = 0; col < cols; col++)
                        {
                            if (col == 0)
                                matrix[row] = new float[cols];
                            matrix[col][row] = mb.getFloat();
                        }
                    }
                    try
                    {
                        for (int col = 0; col < cols; col++)
                        {
                            vArray[col] = FloatVectorUtil.instantiateAnonymousSI(matrix[col], units[col], StorageType.DENSE);
                        }
                        list.add(vArray);
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                
                else if (type == FieldTypes.DOUBLE_64_UNIT_COLUMN_ARRAY)
                {
                    int rows = mb.getInt();
                    int cols = mb.getInt();
                    Unit<? extends Unit<?>>[] units = new Unit<?>[cols];
                    AbstractDoubleVector<?, ?>[] vArray = new AbstractDoubleVector[cols];
                    for (int col = 0; col < cols; col++)
                    {
                        units[col] = mb.getUnit();
                    }
                    // here we use a column-first matrix (!) for storage
                    double[][] matrix = new double[cols][rows];
                    for (int row = 0; row < rows; row++)
                    {
                        for (int col = 0; col < cols; col++)
                        {
                            if (col == 0)
                                matrix[row] = new double[cols];
                            matrix[col][row] = mb.getDouble();
                        }
                    }
                    try
                    {
                        for (int col = 0; col < cols; col++)
                        {
                            vArray[col] = DoubleVectorUtil.instantiateAnonymousSI(matrix[col], units[col], StorageType.DENSE);
                        }
                        list.add(vArray);
                    }
                    catch (ValueException exception)
                    {
                        throw new SerializationException(exception);
                    }
                }
                
                else
                {
                    throw new SerializationException("Unknown data type " + type + " in the ZeroMQ message while decoding");
                }
                */
            }

        }

        return list.toArray();
    }

    /**
     * Indicate whether extra bytes are needed for a Money per quantity type.
     * @param o the object to check
     * @return 0 or 1 to indicate whether an extra byte is needed
     */
    private static int extraBytesMoney(final Object o)
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
     * Return a readable string with the bytes in a byte[] message.
     * @param bytes byte[]; the byte array to display
     * @return String; a readable string with the bytes in a byte[] message
     */
    public static String printBytes(final byte[] bytes)
    {
        StringBuffer s = new StringBuffer();
        s.append("|");
        for (int b : bytes)
        {
            if (b < 0)
            {
                b += 128;
            }
            if (b >= 32 && b <= 127)
            {
                s.append("#" + Integer.toString(b, 16).toUpperCase() + "(" + (char) (byte) b + ")|");
            }
            else
            {
                s.append("#" + Integer.toString(b, 16).toUpperCase() + "|");
            }
        }
        return s.toString();
    }

}
