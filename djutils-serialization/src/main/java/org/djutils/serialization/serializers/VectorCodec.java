package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.vecmat.def.Vector;
import org.djunits.vecmat.dn.VectorN;
import org.djunits.vecmat.storage.DenseDoubleDataSi;
import org.djunits.vecmat.storage.DenseFloatDataSi;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a quantity Vector.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class VectorCodec extends BasicCodec<Vector<?, ?, ?, ?, ?>>
{
    /**
     * Construct the VectorCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     */
    public VectorCodec(final byte type)
    {
        super(type);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 1;
    }

    /** Converter for Quantity Vector with float values. */
    protected static final BasicCodec<Vector<?, ?, ?, ?, ?>> VECTOR_FLOAT = new VectorCodec(FieldTypes.FLOAT_32_UNIT_ARRAY)
    {
        @Override
        public int size(final Vector<?, ?, ?, ?, ?> vector) throws SerializationException
        {
            return 4 + 2 + 4 * vector.size();
        }

        @Override
        public void serialize(final Vector<?, ?, ?, ?, ?> vector, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            endianness.encodeInt(vector.size(), buffer, pointer.getAndIncrement(4));
            UnitCodec.encodeQuantityUnit(vector.get(0), buffer, pointer);
            for (int i = 0; i < vector.size(); i++)
            {
                endianness.encodeFloat((float) vector.get(i).si(), buffer, pointer.getAndIncrement(4));
            }
        }

        @Override
        public Vector<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
            float[] dataSi = new float[size];
            for (int i = 0; i < size; i++)
            {
                dataSi[i] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
            }
            VectorN<?, ?, ?, ?, ?> vector = VectorN.Col.ofSi(new DenseFloatDataSi(dataSi, 1, size), unit);
            UnitCodec.setDisplayUnit(vector, unit);
            return vector;
        }
    };

    /** Converter for Quantity Vector with double values. */
    protected static final BasicCodec<Vector<?, ?, ?, ?, ?>> VECTOR_DOUBLE = new VectorCodec(FieldTypes.DOUBLE_64_UNIT_ARRAY)
    {
        @Override
        public int size(final Vector<?, ?, ?, ?, ?> vector) throws SerializationException
        {
            return 4 + 2 + 8 * vector.size();
        }

        @Override
        public void serialize(final Vector<?, ?, ?, ?, ?> vector, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            endianness.encodeInt(vector.size(), buffer, pointer.getAndIncrement(4));
            UnitCodec.encodeQuantityUnit(vector.get(0), buffer, pointer);
            for (int i = 0; i < vector.size(); i++)
            {
                endianness.encodeDouble(vector.get(i).si(), buffer, pointer.getAndIncrement(8));
            }
        }

        @Override
        public Vector<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
            double[] dataSi = new double[size];
            for (int i = 0; i < size; i++)
            {
                dataSi[i] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
            }
            VectorN<?, ?, ?, ?, ?> vector = VectorN.Col.ofSi(new DenseDoubleDataSi(dataSi, 1, size), unit);
            UnitCodec.setDisplayUnit(vector, unit);
            return vector;
        }
    };

}
