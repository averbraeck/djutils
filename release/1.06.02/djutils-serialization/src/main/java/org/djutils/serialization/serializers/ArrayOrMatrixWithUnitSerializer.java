package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djutils.serialization.DisplayType;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationException;
import org.djutils.serialization.SerializationUnits;

/**
 * Serializer for Djunits arrays and matrices.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <U> the unit type
 * @param <T> The object type to (de)serialize
 */
public abstract class ArrayOrMatrixWithUnitSerializer<U extends Unit<U>, T> extends BasicSerializer<T>
{
    /** Number of dimension; 1 for array, 2 for matrix. */
    private final int numberOfDimensions;

    /**
     * Construct a new serializer for Djunits arrays or matrices.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName String; returned by the dataClassName method
     * @param numberOfDimensions int; should be 1 for array serializer and 2 for matrix serializer
     */
    public ArrayOrMatrixWithUnitSerializer(final byte type, final String dataClassName, final int numberOfDimensions)
    {
        super(type, dataClassName);
        this.numberOfDimensions = numberOfDimensions;
    }

    @Override
    public final int sizeWithPrefix(final T object) throws SerializationException
    {
        return 1 + size(object);
    }

    @Override
    public final void serializeWithPrefix(final T object, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianUtil);
    }

    @Override
    public final int getNumberOfDimensions()
    {
        return this.numberOfDimensions;
    }

    /**
     * Code a unit, including MoneyUnits.
     * @param unit U; the unit to code in the byte array
     * @param message byte[]; the byte array
     * @param pointer Pointer; the start pointer in the byte array
     * @param endianUtil EndianUtil; encoder to use for multi-byte values
     */
    protected void encodeUnit(final U unit, final byte[] message, final Pointer pointer, final EndianUtil endianUtil)
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
