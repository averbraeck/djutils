package org.djutils.data.serialization;

/**
 * ShortSerializer (de)serializes Short objects.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class ShortSerializer implements SpecificTextSerializer<Short>
{
    @Override
    public String serialize(final Short value, final String unit)
    {
        return value == null ? null : String.valueOf(value.shortValue());
    }

    @Override
    public Short deserialize(final Class<Short> type, final String text, final String unit)
    {
        return (text == null || text.isEmpty()) ? null : Short.valueOf(text);
    }

}
