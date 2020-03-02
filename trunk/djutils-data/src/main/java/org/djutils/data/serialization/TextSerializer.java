package org.djutils.data.serialization;

/**
 * TextSerializer defines the serialize and deserialize methods.
 * <br><br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> the value type
 */
public interface TextSerializer<T>
{
    /**
     * Serialize a value to text in such a way that it can be deserialized with the corresponding deserializer.
     * @param value T; the value to serialize
     * @return String; a string representation of the value that can later be deserialized
     */
    String serialize(T value);

    /**
     * Deserialize a value from text that has been created with the corresponding serializer.
     * @param text String; the string to deserialize
     * @return T; an instance of the object created with the corresponding serializer
     */
    T deserialize(String text);
}

