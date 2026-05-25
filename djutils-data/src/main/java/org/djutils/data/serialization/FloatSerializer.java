package org.djutils.data.serialization;

/**
 * FloatSerializer (de)serializes Float objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class FloatSerializer implements SpecificTextSerializer<Float>
{
    @Override
    public String serialize(final Float value)
    {
        return value == null ? null : String.valueOf(value.floatValue());
    }

    @Override
    public Float deserialize(final Class<Float> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : Float.valueOf(text);
    }

}
