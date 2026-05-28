package org.djutils.serialization.serializers;

import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;
import org.djunits.value.Value;
import org.djutils.serialization.QuantityType;
import org.djutils.serialization.UnitType;

/**
 * Static class to (de)serializes a Unit from the djunits library.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public final class UnitCodec
{
    /** static class. */
    private UnitCodec()
    {
        // static class
    }

    /**
     * Encode a quantity code and unit code into a message byte array.
     * @param unit the unit to code in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     */
    protected static void encodeQuantityUnit(final Unit<?, ?> unit, final byte[] message, final Pointer pointer)
    {
        QuantityType quantityType = QuantityType.getQuantityType(unit);
        message[pointer.getAndIncrement(1)] = quantityType.getCode();
        UnitType unitType = UnitType.getUnitType(unit);
        message[pointer.getAndIncrement(1)] = unitType.getByteCode();
    }

    /**
     * Encode a quantity code and unit code based on a quantity into a message byte array.
     * @param quantity the quantity for which to encode the unit in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     */
    protected static void encodeQuantityUnit(final Quantity<?> quantity, final byte[] message, final Pointer pointer)
    {
        QuantityType quantityType = QuantityType.getQuantityType(quantity);
        message[pointer.getAndIncrement(1)] = quantityType.getCode();
        UnitType unitType = UnitType.getUnitType(quantity.getDisplayUnit());
        message[pointer.getAndIncrement(1)] = unitType.getByteCode();
    }

    /**
     * Decode and return a unit.
     * @param buffer the encoded data
     * @param pointer position in the encoded data where the unit is to be decoded from
     * @return the Unit
     */
    protected static Unit<?, ?> getUnit(final byte[] buffer, final Pointer pointer)
    {
        byte quantityCode = buffer[pointer.getAndIncrement(1)];
        byte unitCode = buffer[pointer.getAndIncrement(1)];
        return UnitType.getUnit(quantityCode, unitCode);
    }

    /**
     * Set anonymous display unit for anonymous value.
     * @param <V> the value type
     * @param <Q> the quantity type
     * @param <U> the corresponding unit type
     * @param value the value to set the display unit for
     * @param unit the display unit
     */
    @SuppressWarnings("unchecked")
    public static <V extends Value<V, Q>, Q extends Quantity<Q>,
            U extends Unit<U, Q>> void setDisplayUnit(final Value<?, ?> value, final Unit<?, ?> unit)
    {
        ((V) value).setDisplayUnit((U) unit);
    }

}
