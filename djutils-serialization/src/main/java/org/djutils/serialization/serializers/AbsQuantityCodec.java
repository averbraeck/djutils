package org.djutils.serialization.serializers;

import org.djunits.quantity.def.AbsBasic;
import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS absolute Quantity.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class AbsQuantityCodec extends BasicCodec<AbsBasic<?, ?, ?>>
{
    /**
     * Construct the QuantityCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     */
    public AbsQuantityCodec(final byte type)
    {
        super(type);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 0;
    }

    @Override
    public boolean hasUnit()
    {
        return true;
    }

    /** Converter for Absolute Quantity with a float value. */
    public static final BasicCodec<AbsBasic<?, ?, ?>> ABS_QUANTITY_FLOAT = new AbsQuantityCodec(FieldTypes.FLOAT_32_UNIT_ABS)
    {
        @Override
        public int size(final AbsBasic<?, ?, ?> absQuantity)
        {
            return 2 + 4 + 1 + 4 + absQuantity.getReference().getId().length();
        }

        @Override
        public void serialize(final AbsBasic<?, ?, ?> absQuantity, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            UnitCodec.encodeQuantityUnit(absQuantity.getQuantity(), buffer, pointer);
            StringCodec.STRING8.serializeWithPrefix(absQuantity.getReference().getId(), buffer, pointer, endianness);
            float v = (float) absQuantity.si();
            endianness.encodeDouble(v, buffer, pointer.getAndIncrement(4));
        }

        @Override
        public AbsBasic<?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
            Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
            String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
            Quantity<?> quantity = unit.ofSi(endianness.decodeFloat(buffer, pointer.getAndIncrement(4)));
            UnitCodec.setDisplayUnit(quantity, unit);
            return AbsHelper.instantiateAbsQuantity(quantity, refStr);
        }
    };

    /** Converter for Quantity with a double value. */
    public static final BasicCodec<AbsBasic<?, ?, ?>> ABS_QUANTITY_DOUBLE =
            new AbsQuantityCodec(FieldTypes.DOUBLE_64_UNIT_ABS)
            {
                @Override
                public int size(final AbsBasic<?, ?, ?> absQuantity)
                {
                    return 2 + 8 + 1 + 4 + absQuantity.getReference().getId().length();
                }

                @Override
                public void serialize(final AbsBasic<?, ?, ?> absQuantity, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    UnitCodec.encodeQuantityUnit(absQuantity.getQuantity(), buffer, pointer);
                    StringCodec.STRING8.serializeWithPrefix(absQuantity.getReference().getId(), buffer, pointer, endianness);
                    double v = absQuantity.si();
                    endianness.encodeDouble(v, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public AbsBasic<?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
                    Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
                    String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
                    Quantity<?> quantity = unit.ofSi(endianness.decodeDouble(buffer, pointer.getAndIncrement(8)));
                    UnitCodec.setDisplayUnit(quantity, unit);
                    return AbsHelper.instantiateAbsQuantity(quantity, refStr);
                }
            };

}
