package org.djutils.data.serialization;

/**
 * DoubleSerializer (de)serializes Double objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class DoubleSerializer implements SpecificTextSerializer<Double>
{
    @Override
    public String serialize(final Double value)
    {
        return value == null ? null : String.valueOf(value.doubleValue());
    }

    @Override
    public Double deserialize(final Class<Double> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : Double.valueOf(text);
    }

}
