package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vfloat.matrix.base.FloatMatrix;
import org.djunits.value.vfloat.matrix.base.FloatMatrixInterface;
import org.djunits.value.vfloat.matrix.data.FloatMatrixData;
import org.djunits.value.vfloat.scalar.base.FloatScalarInterface;
import org.djunits.value.vfloat.vector.base.FloatVectorInterface;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS FloatMatrix.
 * <p>
 * Copyright (c) 2019-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 * @param <M> the matrix type
 */
public class FloatMatrixSerializer<U extends Unit<U>, S extends FloatScalarInterface<U, S>,
        V extends FloatVectorInterface<U, S, V>, M extends FloatMatrixInterface<U, S, V, M>>
        extends ArrayOrMatrixWithUnitSerializer<U, M>
{
    /** */
    public FloatMatrixSerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT_MATRIX, "Djunits_FloatMatrix", 2);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final M afm) throws SerializationException
    {
        try
        {
            return 4 + 4 + 2 + 4 * afm.rows() * afm.cols();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final M afm, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(afm.rows(), buffer, pointer.getAndIncrement(4));
            endianUtil.encodeInt(afm.cols(), buffer, pointer.getAndIncrement(4));
            encodeUnit(afm.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < afm.rows(); i++)
            {
                for (int j = 0; j < afm.cols(); j++)
                {
                    endianUtil.encodeFloat(afm.get(i, j).getSI(), buffer, pointer.getAndIncrement(4));
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
            FloatMatrixData fvd = FloatMatrixData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return FloatMatrix.instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

}
