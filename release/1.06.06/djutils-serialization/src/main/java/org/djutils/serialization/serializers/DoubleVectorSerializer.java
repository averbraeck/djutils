package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.scalar.base.DoubleScalarInterface;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vdouble.vector.base.DoubleVectorInterface;
import org.djunits.value.vdouble.vector.data.DoubleVectorData;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS DoubleVector.
 * <p>
 * Copyright (c) 2019-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class DoubleVectorSerializer<U extends Unit<U>, S extends DoubleScalarInterface<U, S>,
        V extends DoubleVectorInterface<U, S, V>> extends ArrayOrMatrixWithUnitSerializer<U, V>
{
    /** */
    public DoubleVectorSerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT_ARRAY, "Djunits_DoubleVector", 1);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final V adv) throws SerializationException
    {
        try
        {
            return 4 + 2 + 8 * adv.size();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final V adv, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(adv.size(), buffer, pointer.getAndIncrement(4));
            encodeUnit(adv.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < adv.size(); i++)
            {
                endianUtil.encodeDouble(adv.get(i).getSI(), buffer, pointer.getAndIncrement(8));
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
        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
        double[] array = new double[size];
        for (int i = 0; i < size; i++)
        {
            array[i] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
        }
        try
        {
            DoubleVectorData fvd = DoubleVectorData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return DoubleVector.instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

}
