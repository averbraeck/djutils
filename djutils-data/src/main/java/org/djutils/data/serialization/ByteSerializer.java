package org.djutils.data.serialization;

/**
 * ByteSerializer (de)serializes Byte objects.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class ByteSerializer implements SpecificTextSerializer<Byte>
{
    @Override
    public String serialize(final Byte value, final String unit)
    {
        return value == null ? null : String.valueOf(value.byteValue());
    }

    @Override
    public Byte deserialize(final Class<Byte> type, final String text, final String unit)
    {
        return (text == null || text.isEmpty()) ? null : Byte.valueOf(text);
    }

}
