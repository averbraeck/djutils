package org.djutils.serialization.codecs;

import org.djunits.unit.Unit;
import org.djunits.vecmat.def.Matrix;
import org.djunits.vecmat.dnxm.MatrixNxM;
import org.djunits.vecmat.storage.DenseDoubleDataSi;
import org.djunits.vecmat.storage.DenseFloatDataSi;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a quantity Matrix.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class MatrixCodec extends BasicCodec<Matrix<?, ?, ?, ?, ?>>
{
    /**
     * Construct the MatrixCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param shortName the short name of the codec
     */
    public MatrixCodec(final byte type, final String shortName)
    {
        super(type, shortName);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 2;
    }

    @Override
    public boolean hasUnit()
    {
        return true;
    }

    /** Converter for Quantity Matrix with float values. */
    public static final FloatMatrixCodec MATRIX_FLOAT = new FloatMatrixCodec();

    /** Converter class for Quantity Matrix with float values. */
    public static final class FloatMatrixCodec extends MatrixCodec
    {
        /** Construct the FloatMatrixCodec. */
        public FloatMatrixCodec()
        {
            super(FieldTypes.FLOAT_32_UNIT_MATRIX, "matrix_32_unit");
        }

        @Override
        public int size(final Matrix<?, ?, ?, ?, ?> matrix)
        {
            return 4 + 4 + 2 + 4 * matrix.rows() * matrix.cols();
        }

        @Override
        public void serialize(final Matrix<?, ?, ?, ?, ?> matrix, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            endianness.encodeInt(matrix.rows(), buffer, pointer.getAndIncrement(4));
            endianness.encodeInt(matrix.cols(), buffer, pointer.getAndIncrement(4));
            UnitCodec.encodeQuantityUnit(matrix.get(0, 0), buffer, pointer);
            for (int i = 0; i < matrix.rows(); i++)
            {
                for (int j = 0; j < matrix.cols(); j++)
                {
                    endianness.encodeFloat((float) matrix.si(i, j), buffer, pointer.getAndIncrement(4));
                }
            }
        }

        @Override
        public Matrix<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
            float[][] dataSi = new float[rows][cols];
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    dataSi[i][j] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                }
            }
            @SuppressWarnings({"unchecked", "rawtypes"})
            MatrixNxM<?> matrix = new MatrixNxM(DenseFloatDataSi.ofSi(dataSi), unit);
            UnitCodec.setDisplayUnit(matrix, unit);
            return matrix;
        }
    }

    /** Converter for Quantity Matrix with double values. */
    public static final DoubleMatrixCodec MATRIX_DOUBLE = new DoubleMatrixCodec();

    /** Converter class for Quantity Matrix with double values. */
    public static final class DoubleMatrixCodec extends MatrixCodec
    {
        /** Construct the DoubleMatrixCodec. */
        public DoubleMatrixCodec()
        {
            super(FieldTypes.DOUBLE_64_UNIT_MATRIX, "matrix_64_unit");
        }

        @Override
        public int size(final Matrix<?, ?, ?, ?, ?> matrix)
        {
            return 4 + 4 + 2 + 8 * matrix.rows() * matrix.cols();
        }

        @Override
        public void serialize(final Matrix<?, ?, ?, ?, ?> matrix, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            endianness.encodeInt(matrix.rows(), buffer, pointer.getAndIncrement(4));
            endianness.encodeInt(matrix.cols(), buffer, pointer.getAndIncrement(4));
            UnitCodec.encodeQuantityUnit(matrix.get(0, 0), buffer, pointer);
            for (int i = 0; i < matrix.rows(); i++)
            {
                for (int j = 0; j < matrix.cols(); j++)
                {
                    endianness.encodeDouble(matrix.si(i, j), buffer, pointer.getAndIncrement(8));
                }
            }
        }

        @Override
        public Matrix<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
            double[][] dataSi = new double[rows][cols];
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    dataSi[i][j] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            }
            @SuppressWarnings({"unchecked", "rawtypes"})
            MatrixNxM<?> matrix = new MatrixNxM(DenseDoubleDataSi.ofSi(dataSi), unit);
            UnitCodec.setDisplayUnit(matrix, unit);
            return matrix;
        }
    }

}
