package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.scalar.base.DoubleScalarInterface;
import org.djunits.value.vdouble.vector.base.AbstractDoubleVector;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vdouble.vector.base.DoubleVectorInterface;
import org.djunits.value.vdouble.vector.data.DoubleVectorData;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes an array of (same length) DJUNITS DoubleVectors.
 * <p>
 * Copyright (c) 2019-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class DoubleVectorArraySerializer<U extends Unit<U>, S extends DoubleScalarInterface<U, S>,
        V extends DoubleVectorInterface<U, S, V>> extends ObjectWithUnitSerializer<U, V[]>
{
    /** */
    public DoubleVectorArraySerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT_COLUMN_ARRAY, "Djunits_vector_array");
    }

    /** {@inheritDoc} */
    @Override
    public int size(final V[] adva) throws SerializationException
    {
        int result = 4 + 4;
        int width = adva.length;
        int height = adva[0].size();
        for (int i = 0; i < width; i++)
        {
            V adv = adva[i];
            Throw.when(adv.size() != height, SerializationException.class,
                    "All AbstractDoubleVectors in array must have same size");
            result += 2;
        }
        result += height * width * 8;
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final V[] adva, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        int width = adva.length;
        int height = adva[0].size();
        endianUtil.encodeInt(height, buffer, pointer.getAndIncrement(4));
        endianUtil.encodeInt(adva.length, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < width; i++)
        {
            V adv = adva[i];
            Throw.when(adv.size() != height, SerializationException.class,
                    "All AbstractDoubleVectors in array must have same size");
            encodeUnit(adv.getDisplayUnit(), buffer, pointer, endianUtil);
        }
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                try
                {
                    endianUtil.encodeDouble(adva[col].getSI(row), buffer, pointer.getAndIncrement(8));
                }
                catch (ValueRuntimeException e)
                {
                    throw new SerializationException(e);
                }
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    public V[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        V[] result = (V[]) new AbstractDoubleVector[width];
        Unit<? extends Unit<?>>[] units = new Unit<?>[width];
        for (int col = 0; col < width; col++)
        {
            units[col] = getUnit(buffer, pointer, endianUtil);
        }
        double[][] values = new double[width][height];
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                values[col][row] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
            }
        }
        for (int col = 0; col < width; col++)
        {
            try
            {
                DoubleVectorData fvd = DoubleVectorData.instantiate(values[col], IdentityScale.SCALE, StorageType.DENSE);
                result[col] = DoubleVector.instantiateAnonymous(fvd, units[col]);
            }
            catch (ValueRuntimeException e)
            {
                throw new SerializationException(e);
            }
        }
        return result;
    }

}
