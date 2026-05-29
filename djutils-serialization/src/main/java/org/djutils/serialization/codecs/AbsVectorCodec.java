package org.djutils.serialization.codecs;

import org.djunits.unit.Unit;
import org.djunits.vecmat.def.AbsVector;
import org.djunits.vecmat.dn.VectorN;
import org.djunits.vecmat.storage.DenseDoubleDataSi;
import org.djunits.vecmat.storage.DenseFloatDataSi;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS vector with absolute quantities.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class AbsVectorCodec extends BasicCodec<AbsVector<?, ?, ?, ?, ?>>
{
    /**
     * Construct the AbsVectorCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     */
    public AbsVectorCodec(final byte type)
    {
        super(type);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 1;
    }

    @Override
    public boolean hasUnit()
    {
        return true;
    }

    /** Converter for Absolute Vector with a float value. */
    public static final BasicCodec<AbsVector<?, ?, ?, ?, ?>> ABS_VECTOR_FLOAT =
            new AbsVectorCodec(FieldTypes.FLOAT_32_UNIT_ABS_ARRAY)
            {
                @Override
                public int size(final AbsVector<?, ?, ?, ?, ?> absVector)
                {
                    return 2 + 4 * absVector.size() + 1 + 4 + absVector.getReference().getId().length();
                }

                @Override
                public void serialize(final AbsVector<?, ?, ?, ?, ?> absVector, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(absVector.size(), buffer, pointer.getAndIncrement(4));
                    UnitCodec.encodeQuantityUnit(absVector.getRelativeVecMat().get(0), buffer, pointer);
                    StringCodec.STRING8.serializeWithPrefix(absVector.getReference().getId(), buffer, pointer, endianness);
                    for (int i = 0; i < absVector.size(); i++)
                    {
                        endianness.encodeFloat((float) absVector.get(i).si(), buffer, pointer.getAndIncrement(4));
                    }
                }

                @Override
                public AbsVector<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
                    Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
                    String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
                    float[] dataSi = new float[size];
                    for (int i = 0; i < size; i++)
                    {
                        dataSi[i] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                    }
                    VectorN.Col<?> vector = VectorN.Col.ofSi(new DenseFloatDataSi(dataSi, 1, size), unit);
                    UnitCodec.setDisplayUnit(vector, unit);
                    return AbsHelper.instantiateAbsVector(vector, refStr);
                }
            };

    /** Converter for Absolute Vector with a double value. */
    public static final BasicCodec<AbsVector<?, ?, ?, ?, ?>> ABS_VECTOR_DOUBLE =
            new AbsVectorCodec(FieldTypes.DOUBLE_64_UNIT_ABS_ARRAY)
            {
                @Override
                public int size(final AbsVector<?, ?, ?, ?, ?> absVector)
                {
                    return 2 + 8 * absVector.size() + 1 + 4 + absVector.getReference().getId().length();
                }

                @Override
                public void serialize(final AbsVector<?, ?, ?, ?, ?> absVector, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(absVector.size(), buffer, pointer.getAndIncrement(4));
                    UnitCodec.encodeQuantityUnit(absVector.getRelativeVecMat().get(0), buffer, pointer);
                    StringCodec.STRING8.serializeWithPrefix(absVector.getReference().getId(), buffer, pointer, endianness);
                    for (int i = 0; i < absVector.size(); i++)
                    {
                        endianness.encodeDouble(absVector.get(i).si(), buffer, pointer.getAndIncrement(8));
                    }
                }

                @Override
                public AbsVector<?, ?, ?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
                    Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
                    String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
                    double[] dataSi = new double[size];
                    for (int i = 0; i < size; i++)
                    {
                        dataSi[i] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                    }
                    VectorN.Col<?> vector = VectorN.Col.ofSi(new DenseDoubleDataSi(dataSi, 1, size), unit);
                    UnitCodec.setDisplayUnit(vector, unit);
                    return AbsHelper.instantiateAbsVector(vector, refStr);
                }
            };

}
