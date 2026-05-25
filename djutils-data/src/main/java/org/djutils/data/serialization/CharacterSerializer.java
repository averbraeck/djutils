package org.djutils.data.serialization;

/**
 * CharacterSerializer (de)serializes Character objects.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public class CharacterSerializer implements SpecificTextSerializer<Character>
{
    @Override
    public String serialize(final Character value)
    {
        return value == null ? null : String.valueOf(value.charValue());
    }

    @Override
    public Character deserialize(final Class<Character> type, final String text)
    {
        return (text == null || text.isEmpty()) ? null : text.charAt(0);
    }

}
