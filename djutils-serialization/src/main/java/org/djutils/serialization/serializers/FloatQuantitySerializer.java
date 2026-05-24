package org.djutils.serialization.serializers;

import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS Quantity with 32-bits precision.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <Q> the scalar type
 */
public class FloatQuantitySerializer<U extends Unit<U, Q>, Q extends Quantity<Q>> extends ObjectWithUnitSerializer<U, Q>
{
    /** */
    public FloatQuantitySerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT, "Djunits_FloatQuantity");
    }

    @Override
    public int size(final Q afs) throws SerializationException
    {
        return 2 + 4;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final Q quantity, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        encodeUnit((U) quantity.getDisplayUnit(), buffer, pointer, endianness);
        float v = (float) quantity.si();
        endianness.encodeFloat(v, buffer, pointer.getAndIncrement(4));
    }

    @Override
    public Q deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness) throws SerializationException
    {
        U unit = getUnit(buffer, pointer, endianness);
        Q quantity = unit.ofSi(endianness.decodeFloat(buffer, pointer.getAndIncrement(4)));
        quantity.setDisplayUnit(unit);
        return quantity;
    }

}
