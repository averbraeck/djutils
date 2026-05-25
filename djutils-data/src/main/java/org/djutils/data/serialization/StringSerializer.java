package org.djutils.data.serialization;

/**
 * StringSerializer (de)serializes String objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class StringSerializer implements SpecificTextSerializer<String>
{
    @Override
    public String serialize(final String value)
    {
        return value == null ? null : value.toString();
    }

    @Override
    public String deserialize(final Class<String> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : text;
    }

}
