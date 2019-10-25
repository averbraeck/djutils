package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djutils.serialization.DisplayType;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationUnits;

/**
 * Abstract class to (de)serializes a DJUNITS value.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <T> the object type
 */
public abstract class ObjectWithUnitSerializer<U extends Unit<U>, T> extends ObjectSerializer<T>
{
    /**
     * Construct a new ObjectWithUnitSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName String; returned by the dataClassName method
     */
    public ObjectWithUnitSerializer(final byte type, final String dataClassName)
    {
        super(type, dataClassName);
    }

    /**
     * Code a unit, including MoneyUnits.
     * @param unit the unit to code in the byte array
     * @param message the byte array
     * @param pointer the start pointer in the byte array
     * @param endianUtil EndianUtil; encoder to use for multi-byte values
     */
    protected void encodeUnit(final U unit, final byte[] message, final Pointer pointer,
            final EndianUtil endianUtil)
    {
        SerializationUnits unitType = SerializationUnits.getUnitType(unit);
        message[pointer.getAndIncrement(1)] = unitType.getCode();
        DisplayType displayType = DisplayType.getDisplayType(unit);
        message[pointer.getAndIncrement(1)] = displayType.getByteCode();
    }

    /**
     * Retrieve and decode a DJUNITS unit.
     * @param buffer byte[]; the encoded data
     * @param pointer Pointer; position in the encoded data where the unit is to be decoded from
     * @param endianUtil EndianUtil; decoder for multi-byte values
     * @return Unit
     */
    @SuppressWarnings("unchecked")
    protected U getUnit(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        SerializationUnits unitType = SerializationUnits.getUnitType(buffer[pointer.getAndIncrement(1)]);
        DisplayType displayType = DisplayType.getDisplayType(unitType, 0 + buffer[pointer.getAndIncrement(1)]);
        return (U) displayType.getDjunitsType();
    }


}
