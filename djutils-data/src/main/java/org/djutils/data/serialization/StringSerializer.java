package org.djutils.data.serialization;

/**
 * StringSerializer (de)serializes String objects.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class StringSerializer implements SpecificTextSerializer<String>
{
    /** {@inheritDoc} */
    @Override
    public String serialize(final String value)
    {
        return value == null ? null : value.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String deserialize(final Class<String> type, final String text, final String unit)
    {
        // TODO: how do we distinguish between an empty string and 
        return (text == null || text.isEmpty()) ? null : text;
    }

}
