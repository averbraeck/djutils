package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.unit.util.UnitRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vdouble.vector.data.DoubleVectorData;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes an array of (same length) DJUNITS DoubleVectors.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class DoubleVectorArraySerializer<U extends Unit<U>, S extends DoubleScalar<U, S>, V extends DoubleVector<U, S, V>>
        extends ObjectWithUnitSerializer<U, V[]>
{
    /** */
    public DoubleVectorArraySerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT_COLUMN_MATRIX, "Djunits_double_vector_array");
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
                    "All AbstractDoubleVectors in array must have same size");
            result += 2; // quantity and display unit
        }
        result += height * width * 8;
        return result;
    }

    @Override
    public void serialize(final V[] adva, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        int width = adva.length;
        int height = adva[0].size();
        endianness.encodeInt(height, buffer, pointer.getAndIncrement(4));
        endianness.encodeInt(adva.length, buffer, pointer.getAndIncrement(4));
        for (int i = 0; i < width; i++)
        {
            V adv = adva[i];
            Throw.when(adv.size() != height, SerializationException.class,
                    "All AbstractDoubleVectors in array must have same size");
            encodeUnit(adv.getDisplayUnit(), buffer, pointer, endianness);
        }
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                endianness.encodeDouble(adva[col].getSI(row), buffer, pointer.getAndIncrement(8));
            }
        }

    }

    @Override
    public V[] deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        @SuppressWarnings("unchecked")
        V[] result = (V[]) new DoubleVector[width];
        Unit<? extends Unit<?>>[] units = new Unit<?>[width];
        for (int col = 0; col < width; col++)
        {
            units[col] = getUnit(buffer, pointer, endianness);
        }
        double[][] values = new double[width][height];
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                values[col][row] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
            }
        }
        for (int col = 0; col < width; col++)
        {
            try
            {
                DoubleVectorData fvd = DoubleVectorData.instantiate(values[col], IdentityScale.SCALE, StorageType.DENSE);
                result[col] = DoubleVectorSerializer.instantiateAnonymous(fvd, units[col]);
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
