package org.djutils.data.serialization;

/**
 * StrictTextSerializer adds one method to the textSerializer class where it it is not necessary to specify the type
 * for the deserialize method, since it is a specific class. 
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
     * Deserialize a value from text that has been created with the corresponding serializer, where the value does not have a
     * unit, and the class is known and fixed (and not checked).
     * @param text String; the string to deserialize
     * @return T; an instance of the object created with the corresponding serializer
     */
    default T deserialize(final String text)
    {
        return deserialize(null, text, null);
    }

}
