package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.vecmat.def.AbsMatrix;
import org.djunits.vecmat.dnxm.MatrixNxM;
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
     */
    public AbsMatrixCodec(final byte type)
    {
        super(type);
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

    /** Converter for Absolute Matrix with a float value. */
    public static final BasicCodec<AbsMatrix<?, ?, ?, ?, ?>> ABS_MATRIX_FLOAT =
            new AbsMatrixCodec(FieldTypes.FLOAT_32_UNIT_ABS_MATRIX)
            {
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
                    UnitCodec.encodeQuantityUnit(absMatrix.getRelativeVecMat().get(0, 0), buffer, pointer);
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
                public AbsMatrix<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
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
                    UnitCodec.setDisplayUnit(matrix, unit);
                    return AbsHelper.instantiateAbsMatrix(matrix, refStr);
                }
            };

    /** Converter for Absolute Matrix with a double value. */
    public static final BasicCodec<AbsMatrix<?, ?, ?, ?, ?>> ABS_MATRIX_DOUBLE =
            new AbsMatrixCodec(FieldTypes.DOUBLE_64_UNIT_ABS_MATRIX)
            {
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
                    UnitCodec.encodeQuantityUnit(absMatrix.getRelativeVecMat().get(0, 0), buffer, pointer);
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
                public AbsMatrix<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
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
                    MatrixNxM<?> matrix = new MatrixNxM(DenseFloatDataSi.of(dataSi, unit), unit);
                    UnitCodec.setDisplayUnit(matrix, unit);
                    return AbsHelper.instantiateAbsMatrix(matrix, refStr);
                }
            };

}
