package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vfloat.scalar.base.FloatScalarInterface;
import org.djunits.value.vfloat.vector.base.FloatVector;
import org.djunits.value.vfloat.vector.base.FloatVectorInterface;
import org.djunits.value.vfloat.vector.data.FloatVectorData;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS FloatVector.
 * <p>
 * Copyright (c) 2019-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class FloatVectorSerializer<U extends Unit<U>, S extends FloatScalarInterface<U, S>,
        V extends FloatVectorInterface<U, S, V>> extends ArrayOrMatrixWithUnitSerializer<U, V>
{
    /** */
    public FloatVectorSerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT_ARRAY, "Djunits_FloatVector", 1);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final V afv) throws SerializationException
    {
        try
        {
            return 4 + 2 + 4 * afv.size();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final V afv, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(afv.size(), buffer, pointer.getAndIncrement(4));
            encodeUnit(afv.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < afv.size(); i++)
            {
                endianUtil.encodeFloat(afv.get(i).getSI(), buffer, pointer.getAndIncrement(4));
            }
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public V deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil) throws SerializationException
    {
        int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        Unit<?> unit = getUnit(buffer, pointer, endianUtil);
        float[] array = new float[size];
        for (int i = 0; i < size; i++)
        {
            array[i] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
        }
        try
        {
            FloatVectorData fvd = FloatVectorData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return FloatVector.instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

}
