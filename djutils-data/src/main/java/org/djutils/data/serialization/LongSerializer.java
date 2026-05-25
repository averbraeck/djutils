package org.djutils.data.serialization;

/**
 * LongSerializer (de)serializes Long objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class LongSerializer implements SpecificTextSerializer<Long>
{
    @Override
    public String serialize(final Long value)
    {
        return value == null ? null : String.valueOf(value.longValue());
    }

    @Override
    public Long deserialize(final Class<Long> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : Long.valueOf(text);
    }

}
