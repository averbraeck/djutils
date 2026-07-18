package org.djutils.serialization.codecs;

import org.djunits.unit.UnitInterface;
import org.djunits.vecmat.def.AbsMatrix;
import org.djunits.vecmat.dnxm.MatrixNxM;
import org.djunits.vecmat.storage.DenseDoubleDataSi;
import org.djunits.vecmat.storage.DenseFloatDataSi;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS matrix with absolute quantities.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class AbsMatrixCodec extends BasicCodec<AbsMatrix<?, ?, ?, ?, ?>>
{
    /**
     * Construct the AbsMatrixCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param shortName the short name of the codec
     */
    public AbsMatrixCodec(final byte type, final String shortName)
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

    /** Converter for Absolute Quantity Matrix with float values. */
    public static final AbsFloatMatrixCodec ABS_MATRIX_FLOAT = new AbsFloatMatrixCodec();

    /** Converter class for Absolute Quantity Matrix with float values. */
    public static final class AbsFloatMatrixCodec extends AbsMatrixCodec
    {
        /** Construct the AbsFloatMatrixCodec. */
        public AbsFloatMatrixCodec()
        {
            super(FieldTypes.FLOAT_32_UNIT_ABS_MATRIX, "AbsMatrix_32_unit");
        }

        @Override
        public int size(final AbsMatrix<?, ?, ?, ?, ?> absMatrix)
        {
            return 2 + 4 * absMatrix.rows() * absMatrix.cols() + 1 + 4 + absMatrix.getReference().getId().length();
        }

        @Override
        public void serialize(final AbsMatrix<?, ?, ?, ?, ?> absMatrix, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            endianness.encodeInt(absMatrix.rows(), buffer, pointer.getAndIncrement(4));
            endianness.encodeInt(absMatrix.cols(), buffer, pointer.getAndIncrement(4));
            AbsUnitCodec.encodeAbsQuantityUnit(absMatrix.get(0, 0), buffer, pointer);
            StringCodec.STRING8.serializeWithPrefix(absMatrix.getReference().getId(), buffer, pointer, endianness);
            for (int i = 0; i < absMatrix.rows(); i++)
            {
                for (int j = 0; j < absMatrix.cols(); j++)
                {
                    endianness.encodeFloat((float) absMatrix.get(i, j).si(), buffer, pointer.getAndIncrement(4));
                }
            }
        }

        @Override
        public AbsMatrix<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            UnitInterface<?> unit = AbsUnitCodec.getUnit(buffer, pointer);
            Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
            String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
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
            return AbsHelper.instantiateAbsMatrix(matrix, refStr);
        }
    }

    /** Converter for Absolute Quantity Matrix with double values. */
    public static final AbsDoubleMatrixCodec ABS_MATRIX_DOUBLE = new AbsDoubleMatrixCodec();

    /** Converter class for Absolute Quantity Matrix with double values. */
    public static final class AbsDoubleMatrixCodec extends AbsMatrixCodec
    {
        /** Construct the AbsDoubleMatrixCodec. */
        public AbsDoubleMatrixCodec()
        {
            super(FieldTypes.DOUBLE_64_UNIT_ABS_MATRIX, "AbsMatrix_64_unit");
        }

        @Override
        public int size(final AbsMatrix<?, ?, ?, ?, ?> absMatrix)
        {
            return 2 + 8 * absMatrix.rows() * absMatrix.cols() + 1 + 4 + absMatrix.getReference().getId().length();
        }

        @Override
        public void serialize(final AbsMatrix<?, ?, ?, ?, ?> absMatrix, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            endianness.encodeInt(absMatrix.rows(), buffer, pointer.getAndIncrement(4));
            endianness.encodeInt(absMatrix.cols(), buffer, pointer.getAndIncrement(4));
            AbsUnitCodec.encodeAbsQuantityUnit(absMatrix.get(0, 0), buffer, pointer);
            StringCodec.STRING8.serializeWithPrefix(absMatrix.getReference().getId(), buffer, pointer, endianness);
            for (int i = 0; i < absMatrix.rows(); i++)
            {
                for (int j = 0; j < absMatrix.cols(); j++)
                {
                    endianness.encodeDouble(absMatrix.get(i, j).si(), buffer, pointer.getAndIncrement(8));
                }
            }
        }

        @Override
        public AbsMatrix<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            UnitInterface<?> unit = AbsUnitCodec.getUnit(buffer, pointer);
            Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
            String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
            double[][] dataSi = new double[rows][cols];
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    dataSi[i][j] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            }
            @SuppressWarnings({"unchecked", "rawtypes"})
            MatrixNxM<?> matrix = new MatrixNxM(DenseDoubleDataSi.of(dataSi, unit), unit);
            return AbsHelper.instantiateAbsMatrix(matrix, refStr);
        }
    }

}
