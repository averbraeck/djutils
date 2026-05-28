package org.djutils.serialization.serializers;

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
     */
    public MatrixCodec(final byte type)
    {
        super(type);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 2;
    }

    /** Converter for Quantity Matrix with float values. */
    protected static final BasicCodec<Matrix<?, ?, ?, ?, ?>> MATRIX_FLOAT = new MatrixCodec(FieldTypes.FLOAT_32_UNIT_ARRAY)
    {
        @Override
        public int size(final Matrix<?, ?, ?, ?, ?> matrix) throws SerializationException
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
                    endianness.encodeFloat((float) matrix.get(i, j).si(), buffer, pointer.getAndIncrement(4));
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
            MatrixNxM<?> matrix = new MatrixNxM(DenseFloatDataSi.of(dataSi, unit), unit);
            UnitCodec.setDisplayUnit(matrix, unit);
            return matrix;
        }
    };

    /** Converter for Quantity Matrix with double values. */
    protected static final BasicCodec<Matrix<?, ?, ?, ?, ?>> MATRIX_DOUBLE = new MatrixCodec(FieldTypes.DOUBLE_64_UNIT_ARRAY)
    {
        @Override
        public int size(final Matrix<?, ?, ?, ?, ?> matrix) throws SerializationException
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
                    endianness.encodeDouble(matrix.get(i, j).si(), buffer, pointer.getAndIncrement(4));
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
                    dataSi[i][j] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                }
            }
            @SuppressWarnings({"unchecked", "rawtypes"})
            MatrixNxM<?> matrix = new MatrixNxM(DenseDoubleDataSi.of(dataSi, unit), unit);
            UnitCodec.setDisplayUnit(matrix, unit);
            return matrix;
        }
    };

}
