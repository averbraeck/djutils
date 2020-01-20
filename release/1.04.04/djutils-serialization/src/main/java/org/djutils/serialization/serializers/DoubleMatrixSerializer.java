package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.matrix.base.DoubleMatrix;
import org.djunits.value.vdouble.matrix.base.DoubleMatrixInterface;
import org.djunits.value.vdouble.matrix.data.DoubleMatrixData;
import org.djunits.value.vdouble.scalar.base.DoubleScalarInterface;
import org.djunits.value.vdouble.vector.base.DoubleVectorInterface;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS DoubleMatrix.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 * @param <M> the matrix type
 */
public class DoubleMatrixSerializer<U extends Unit<U>, S extends DoubleScalarInterface<U, S>,
        V extends DoubleVectorInterface<U, S, V>, M extends DoubleMatrixInterface<U, S, V, M>>
        extends ArrayOrMatrixWithUnitSerializer<U, M>
{
    /** */
    public DoubleMatrixSerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT_MATRIX, "Djunits_DoubleMatrix", 2);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final M adm) throws SerializationException
    {
        try
        {
            return 4 + 4 + 2 + 8 * adm.rows() * adm.cols();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final M adm, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(adm.rows(), buffer, pointer.getAndIncrement(4));
            endianUtil.encodeInt(adm.cols(), buffer, pointer.getAndIncrement(4));
            encodeUnit(adm.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < adm.rows(); i++)
            {
                for (int j = 0; j < adm.cols(); j++)
                {
                    endianUtil.encodeDouble(adm.get(i, j).getSI(), buffer, pointer.getAndIncrement(8));
                }
            }
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public M deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil) throws SerializationException
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
            DoubleMatrixData fvd = DoubleMatrixData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return DoubleMatrix.instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

}
