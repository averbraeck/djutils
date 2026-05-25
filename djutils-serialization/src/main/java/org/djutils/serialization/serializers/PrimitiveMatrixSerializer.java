package org.djutils.serialization.serializers;

import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * PrimitiveArraySerializer is responsible for the serialization of 1-dimenisional arrays of primitive types, which can be
 * offered as the primitive type (e.g., <code>int[]</code> itself or as the object wrapper (e.g., <code>Integer[]</code>).
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public final class PrimitiveMatrixSerializer
{

    /** Static class, no constructor. */
    private PrimitiveMatrixSerializer()
    {
        // Static class, no constructor
    }

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

}
