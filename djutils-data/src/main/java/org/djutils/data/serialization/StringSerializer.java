package org.djutils.data.serialization;

/**
 * StringSerializer (de)serializes String objects. <br>
 * <br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class StringSerializer implements TextSerializer<String>
{
    /** {@inheritDoc} */
    @Override
    public String serialize(final Object value)
    {
        return value.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String deserialize(final String text)
    {
        return text;
    }

}
