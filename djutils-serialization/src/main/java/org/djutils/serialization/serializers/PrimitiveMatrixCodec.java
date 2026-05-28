package org.djutils.serialization.serializers;

import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * PrimitiveMatrixCodec is responsible for the serialization and deserialization of 2-dimensional matrices of primitive types,
 * (e.g., <code>int[][]</code>. Note that the matrices need to be non-jagged, and non-empty.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 * @param <T> the data type (2-dimensional)
 */
public abstract class PrimitiveMatrixCodec<T> extends BasicCodec<T>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /**
     * Construct a new PrimitiveMatrixCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param elementSize the number of bytes needed to encode one additional array element
     */
    public PrimitiveMatrixCodec(final byte type, final int elementSize)
    {
        super(type);
        this.elementSize = elementSize;
    }

    /**
     * Retrieve the number of bytes needed to encode one additional array element.
     * @return the number of bytes needed to encode one additional array element
     */
    public final int getElementSize()
    {
        return this.elementSize;
    }

    @Override
    public final int getNumberOfDimensions()
    {
        return 2;
    }

    /** Converter for byte matrix. */
    protected static final PrimitiveMatrixCodec<byte[][]> BYTE_MATRIX = new PrimitiveMatrixCodec<>(FieldTypes.BYTE_8_MATRIX, 1)
    {
        @Override
        public int size(final byte[][] matrix)
        {
            return 8 + getElementSize() * matrix.length * matrix[0].length;
        }

        @Override
        public void serialize(final byte[][] matrix, final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
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
        public byte[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for short matrix. */
    protected static final PrimitiveMatrixCodec<short[][]> SHORT_MATRIX =
            new PrimitiveMatrixCodec<short[][]>(FieldTypes.SHORT_16_MATRIX, 2)
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
                public short[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for int matrix. */
    protected static final PrimitiveMatrixCodec<int[][]> INT_MATRIX = new PrimitiveMatrixCodec<>(FieldTypes.INT_32_MATRIX, 4)
    {
        @Override
        public int size(final int[][] matrix)
        {
            return 8 + getElementSize() * matrix.length * matrix[0].length;
        }

        @Override
        public void serialize(final int[][] matrix, final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
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
        public int[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for long matrix. */
    protected static final PrimitiveMatrixCodec<long[][]> LONG_MATRIX = new PrimitiveMatrixCodec<>(FieldTypes.LONG_64_MATRIX, 8)
    {
        @Override
        public int size(final long[][] matrix)
        {
            return 8 + getElementSize() * matrix.length * matrix[0].length;
        }

        @Override
        public void serialize(final long[][] matrix, final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
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
        public long[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for float matrix. */
    protected static final PrimitiveMatrixCodec<float[][]> FLOAT_MATRIX =
            new PrimitiveMatrixCodec<>(FieldTypes.FLOAT_32_MATRIX, 4)
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
                public float[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for double matrix. */
    protected static final PrimitiveMatrixCodec<double[][]> DOUBLE_MATRIX =
            new PrimitiveMatrixCodec<>(FieldTypes.DOUBLE_64_MATRIX, 8)
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
                public double[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

    /** Converter for boolean matrix. */
    protected static final PrimitiveMatrixCodec<boolean[][]> BOOLEAN_MATRIX =
            new PrimitiveMatrixCodec<>(FieldTypes.BOOLEAN_8_MATRIX, 1)
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
                public boolean[][] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
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

}
