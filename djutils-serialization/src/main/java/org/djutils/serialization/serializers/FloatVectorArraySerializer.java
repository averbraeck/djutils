package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.unit.util.UnitRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djunits.value.vfloat.vector.base.FloatVector;
import org.djunits.value.vfloat.vector.data.FloatVectorData;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes an array of (same length) DJUNITS FloatVectors.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class FloatVectorArraySerializer<U extends Unit<U>, S extends FloatScalar<U, S>, V extends FloatVector<U, S, V>>
        extends ObjectWithUnitSerializer<U, V[]>
{
    /** */
    public FloatVectorArraySerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT_COLUMN_MATRIX, "Djunits_vector_array");
    }

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
                    "All AbstractFloatVectors in array must have same size");
            result += 2; // quantity and display unit
        }
        result += height * width * 4;
        return result;
    }

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
                    "All AbstractFloatVectors in array must have same size");
            encodeUnit(adv.getDisplayUnit(), buffer, pointer, endianUtil);
        }
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                endianUtil.encodeFloat(adva[col].getSI(row), buffer, pointer.getAndIncrement(4));
            }
        }

    }

    @Override
    public V[] deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        V[] result = (V[]) new FloatVector[width];
        Unit<? extends Unit<?>>[] units = new Unit<?>[width];
        for (int col = 0; col < width; col++)
        {
            units[col] = getUnit(buffer, pointer, endianUtil);
        }
        float[][] values = new float[width][height];
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                values[col][row] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
            }
        }
        for (int col = 0; col < width; col++)
        {
            try
            {
                FloatVectorData fvd = FloatVectorData.instantiate(values[col], IdentityScale.SCALE, StorageType.DENSE);
                result[col] = FloatVectorSerializer.instantiateAnonymous(fvd, units[col]);
            }
            catch (UnitRuntimeException e)
            {
                throw new SerializationException(e);
            }
        }
        return result;
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 2;
    }
}
