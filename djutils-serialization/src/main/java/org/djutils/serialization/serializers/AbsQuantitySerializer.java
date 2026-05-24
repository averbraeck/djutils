package org.djutils.serialization.serializers;

import org.djunits.quantity.def.AbsQuantity;
import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS absolute quantity.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <A> the absolute quantity type
 */
public class AbsQuantitySerializer<U extends Unit<U, ?>, A extends AbsQuantity<A, ?, ?>> extends ObjectWithUnitSerializer<U, A>
{
    /** */
    public AbsQuantitySerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT, "Djunits_AbsQuantity");
    }

    @Override
    public int size(final A quantity) throws SerializationException
    {
        return 2 + 8;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final A quantity, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        encodeUnit((U) quantity.getDisplayUnit(), buffer, pointer, endianness);
        double v = quantity.si();
        endianness.encodeDouble(v, buffer, pointer.getAndIncrement(8));
    }

    @Override
    public A deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness) throws SerializationException
    {
        U unit = getUnit(buffer, pointer, endianness);
        Quantity<?> quantity = unit.ofSi(endianness.decodeDouble(buffer, pointer.getAndIncrement(8)));
        quantity.setDisplayUnit(unit);
        return quantity;
    }
}
