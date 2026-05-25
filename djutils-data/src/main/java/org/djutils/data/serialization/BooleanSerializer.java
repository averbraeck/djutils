package org.djutils.data.serialization;

/**
 * BooleanSerializer (de)serializes Boolean objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class BooleanSerializer implements SpecificTextSerializer<Boolean>
{
    @Override
    public String serialize(final Boolean value)
    {
        return value == null ? null : value.toString();
    }

    @Override
    public Boolean deserialize(final Class<Boolean> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : Boolean.valueOf(text);
    }

}
