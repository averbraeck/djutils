package org.djutils.data.serialization;

/**
 * ShortSerializer (de)serializes Short objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class ShortSerializer implements SpecificTextSerializer<Short>
{
    @Override
    public String serialize(final Short value)
    {
        return value == null ? null : String.valueOf(value.shortValue());
    }

    @Override
    public Short deserialize(final Class<Short> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : Short.valueOf(text);
    }

}
