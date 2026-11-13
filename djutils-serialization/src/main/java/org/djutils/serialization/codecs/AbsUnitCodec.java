package org.djutils.serialization.codecs;

import org.djunits.quantity.def.AbsQuantity;
import org.djunits.unit.UnitInterface;
import org.djutils.serialization.AbsQuantityType;
import org.djutils.serialization.AbsUnitType;

/**
 * Static class to (de)serializes a Unit for an absolute quantity from the djunits library.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public final class AbsUnitCodec
{
    /** static class. */
    private AbsUnitCodec()
    {
        // static class
    }

    /**
     * Encode a absolute quantity code and unit code into a message byte array.
     * @param unit the unit to code in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     */
    protected static void encodeAbsQuantityUnit(final UnitInterface<?> unit, final byte[] message, final Pointer pointer)
    {
        AbsQuantityType absQuantityType = AbsQuantityType.getAbsQuantityType(unit);
        message[pointer.getAndIncrement(1)] = absQuantityType.getCode();
        AbsUnitType absUnitType = AbsUnitType.getAbsUnitType(unit);
        message[pointer.getAndIncrement(1)] = absUnitType.getByteCode();
    }

    /**
     * Encode an absolute quantity code and unit code based on an absolute quantity into a message byte array.
     * @param absQuantity the absolute quantity for which to encode the unit in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     */
    protected static void encodeAbsQuantityUnit(final AbsQuantity<?, ?, ?> absQuantity, final byte[] message,
            final Pointer pointer)
    {
        AbsQuantityType absQuantityType = AbsQuantityType.getAbsQuantityType(absQuantity);
        message[pointer.getAndIncrement(1)] = absQuantityType.getCode();
        AbsUnitType absUnitType = AbsUnitType.getAbsUnitType(absQuantity.getDisplayUnit());
        message[pointer.getAndIncrement(1)] = absUnitType.getByteCode();
    }

    /**
     * Decode and return a unit.
     * @param buffer the encoded data
     * @param pointer position in the encoded data where the unit is to be decoded from
     * @return the Unit
     */
    protected static UnitInterface<?> getUnit(final byte[] buffer, final Pointer pointer)
    {
        byte quantityCode = buffer[pointer.getAndIncrement(1)];
        byte unitCode = buffer[pointer.getAndIncrement(1)];
        return AbsUnitType.getUnit(quantityCode, unitCode);
    }

}
