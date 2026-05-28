package org.djutils.serialization.serializers;

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
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <Q> the scalar type
 */
public class QuantitySerializer<U extends Unit<U, Q>, Q extends Quantity<Q>> extends ObjectWithUnitSerializer<U, Q>
{
    /** */
    public QuantitySerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT, "Djunits_Quantity");
    }

    @Override
    public int size(final Q afs) throws SerializationException
    {
        return 2 + 8;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final Q quantity, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        encodeUnit((U) quantity.getDisplayUnit(), buffer, pointer, endianness);
        double v = quantity.si();
        endianness.encodeDouble(v, buffer, pointer.getAndIncrement(8));
    }

    @Override
    public Q deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness) throws SerializationException
    {
        U unit = getUnit(buffer, pointer, endianness);
        Q quantity = unit.ofSi(endianness.decodeDouble(buffer, pointer.getAndIncrement(8)));
        quantity.setDisplayUnit(unit);
        return quantity;
    }

}
