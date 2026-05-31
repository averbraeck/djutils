package org.djutils.serialization.codecs;

import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS Quantity.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class QuantityCodec extends BasicCodec<Quantity<?>>
{
    /**
     * Construct the QuantityCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param shortName the short name of the codec
     */
    public QuantityCodec(final byte type, final String shortName)
    {
        super(type, shortName);
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

    /** Converter for Quantity with a float value. */
    public static final BasicCodec<Quantity<?>> QUANTITY_FLOAT = new QuantityCodec(FieldTypes.FLOAT_32_UNIT, "quantity_32_unit")
    {
        @Override
        public int size(final Quantity<?> quantity)
        {
            return 2 + 4;
        }

        @Override
        public void serialize(final Quantity<?> quantity, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            UnitCodec.encodeQuantityUnit(quantity, buffer, pointer);
            float v = (float) quantity.si();
            endianness.encodeFloat(v, buffer, pointer.getAndIncrement(4));
        }

        @Override
        public Quantity<?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
            Quantity<?> quantity = unit.ofSi(endianness.decodeFloat(buffer, pointer.getAndIncrement(4)));
            UnitCodec.setDisplayUnit(quantity, unit);
            return quantity;
        }
    };

    /** Converter for Quantity with a double value. */
    public static final BasicCodec<Quantity<?>> QUANTITY_DOUBLE =
            new QuantityCodec(FieldTypes.DOUBLE_64_UNIT, "quantity_64_unit")
            {
                @Override
                public int size(final Quantity<?> quantity)
                {
                    return 2 + 8;
                }

                @Override
                public void serialize(final Quantity<?> quantity, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    UnitCodec.encodeQuantityUnit(quantity, buffer, pointer);
                    double v = quantity.si();
                    endianness.encodeDouble(v, buffer, pointer.getAndIncrement(8));
                }

                @Override
                public Quantity<?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                        throws SerializationException
                {
                    Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
                    Quantity<?> quantity = unit.ofSi(endianness.decodeDouble(buffer, pointer.getAndIncrement(8)));
                    UnitCodec.setDisplayUnit(quantity, unit);
                    return quantity;
                }
            };

}
