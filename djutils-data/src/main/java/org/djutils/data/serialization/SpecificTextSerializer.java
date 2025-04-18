package org.djutils.data.serialization;

/**
 * StrictTextSerializer adds one method to the textSerializer class where it it is not necessary to specify the type for the
 * deserialize method, since it is a specific class.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <T> the value type
 */
public interface SpecificTextSerializer<T> extends TextSerializer<T>
{
    /**
     * Serialize a value to text in such a way that it can be deserialized with the corresponding deserializer. Note that
     * {@code null} values for value <b>are allowed</b>. A {@code null} values stands for an empty column value in a CVS-file, a
     * missing tag in an XML-file, etc. This version of the serialize method ignores the unit. 
     * @param value the value to serialize, may be {@code null}
     * @return a string representation of the value that can later be deserialized, or {@code null}to denote a missing
     *         value
     */
    default String serialize(final T value)
    {
        return serialize(value, null);
    }
    
    /**
     * Deserialize a value from text that has been created with the corresponding serializer, where the value does not have a
     * unit, and the class is known and fixed (and not checked). Note that {@code null} values for text <b>are allowed</b>. A
     * {@code null} values stands for an empty column value in a CVS-file, a missing tag in an XML-file, etc. In this way, we
     * can explicitly show values that were not specified in the file.
     * @param text the string to deserialize, may be {@code null}
     * @return an instance of the object created with the corresponding serializer, may be {@code null} when a value was not
     *         specified in the source from which the deserializer was called
     */
    default T deserialize(final String text)
    {
        return deserialize(null, text, null);
    }

}
