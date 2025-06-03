package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djutils.serialization.DisplayType;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.QuantityType;

/**
 * Abstract class to (de)serializes a DJUNITS value.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <T> the object type
 */
public abstract class ObjectWithUnitSerializer<U extends Unit<U>, T> extends ObjectSerializer<T>
{
    /**
     * Construct a new ObjectWithUnitSerializer.
     * @param type the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName returned by the dataClassName method
     */
    public ObjectWithUnitSerializer(final byte type, final String dataClassName)
    {
        super(type, dataClassName);
    }

    /**
     * Code a unit.
     * @param unit the unit to code in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     * @param endianUtil encoder to use for multi-byte values
     */
    protected void encodeUnit(final U unit, final byte[] message, final Pointer pointer, final Endianness endianUtil)
    {
        QuantityType unitType = QuantityType.getUnitType(unit);
        message[pointer.getAndIncrement(1)] = unitType.getCode();
        DisplayType displayType = DisplayType.getDisplayType(unit);
        message[pointer.getAndIncrement(1)] = displayType.getByteCode();
    }

    /**
     * Retrieve and decode a DJUNITS unit.
     * @param buffer the encoded data
     * @param pointer position in the encoded data where the unit is to be decoded from
     * @param endianUtil decoder for multi-byte values
     * @return Unit
     */
    @SuppressWarnings("unchecked")
    protected U getUnit(final byte[] buffer, final Pointer pointer, final Endianness endianUtil)
    {
        QuantityType unitType = QuantityType.getUnitType(buffer[pointer.getAndIncrement(1)]);
        DisplayType displayType = DisplayType.getDisplayType(unitType, 0 + buffer[pointer.getAndIncrement(1)]);
        return (U) displayType.getDjunitsType();
    }

    @Override
    public boolean hasUnit()
    {
        return true;
    }

}
