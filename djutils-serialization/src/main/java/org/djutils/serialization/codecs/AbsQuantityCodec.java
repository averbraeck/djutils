package org.djutils.serialization.codecs;

import org.djunits.quantity.def.AbsQuantity;
import org.djunits.quantity.def.Quantity;
import org.djunits.unit.UnitInterface;
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
public abstract class AbsQuantityCodec extends BasicCodec<AbsQuantity<?, ?, ?>>
{
    /**
     * Construct the QuantityCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     * @param shortName the short name of the codec
     */
    public AbsQuantityCodec(final byte type, final String shortName)
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

    /** Converter for Absolute Quantity with a float value. */
    public static final AbsFloatQuantityCodec ABS_QUANTITY_FLOAT = new AbsFloatQuantityCodec();

    /** Converter class for Absolute Quantity with a float value. */
    public static final class AbsFloatQuantityCodec extends AbsQuantityCodec
    {
        /** Construct the AbsFloatQuantityCodec. */
        public AbsFloatQuantityCodec()
        {
            super(FieldTypes.FLOAT_32_UNIT_ABS, "AbsQuantity_32_unit");
        }

        @Override
        public int size(final AbsQuantity<?, ?, ?> absQuantity)
        {
            return 2 + 4 + 1 + 4 + absQuantity.getReference().getId().length();
        }

        @Override
        public void serialize(final AbsQuantity<?, ?, ?> absQuantity, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            AbsUnitCodec.encodeAbsQuantityUnit(absQuantity, buffer, pointer);
            StringCodec.STRING8.serializeWithPrefix(absQuantity.getReference().getId(), buffer, pointer, endianness);
            float v = (float) absQuantity.si();
            endianness.encodeDouble(v, buffer, pointer.getAndIncrement(4));
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public AbsQuantity<?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            UnitInterface unit = AbsUnitCodec.getUnit(buffer, pointer);
            Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
            String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
            Quantity<?> quantity = unit.ofSi(endianness.decodeFloat(buffer, pointer.getAndIncrement(4)), unit);
            return AbsHelper.instantiateAbsQuantity(quantity, refStr);
        }
    }

    /** Converter for Quantity with a double value. */
    public static final AbsDoubleQuantityCodec ABS_QUANTITY_DOUBLE = new AbsDoubleQuantityCodec();

    /** Converter class for Absolute Quantity with a double value. */
    public static final class AbsDoubleQuantityCodec extends AbsQuantityCodec
    {
        /** Construct the AbsDoubleQuantityCodec. */
        public AbsDoubleQuantityCodec()
        {
            super(FieldTypes.DOUBLE_64_UNIT_ABS, "AbsQuantity_64_unit");
        }

        @Override
        public int size(final AbsQuantity<?, ?, ?> absQuantity)
        {
            return 2 + 8 + 1 + 4 + absQuantity.getReference().getId().length();
        }

        @Override
        public void serialize(final AbsQuantity<?, ?, ?> absQuantity, final byte[] buffer, final Pointer pointer,
                final Endianness endianness) throws SerializationException
        {
            AbsUnitCodec.encodeAbsQuantityUnit(absQuantity, buffer, pointer);
            StringCodec.STRING8.serializeWithPrefix(absQuantity.getReference().getId(), buffer, pointer, endianness);
            double v = absQuantity.si();
            endianness.encodeDouble(v, buffer, pointer.getAndIncrement(8));
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public AbsQuantity<?, ?, ?> deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            UnitInterface unit = AbsUnitCodec.getUnit(buffer, pointer);
            Throw.when(pointer.getAndIncrement(1) != 9, SerializationException.class, "No String prefix at position 7");
            String refStr = StringCodec.STRING8.deserialize(buffer, pointer, endianness);
            Quantity<?> quantity = unit.ofSi(endianness.decodeDouble(buffer, pointer.getAndIncrement(8)), unit);
            return AbsHelper.instantiateAbsQuantity(quantity, refStr);
        }
    }

}
