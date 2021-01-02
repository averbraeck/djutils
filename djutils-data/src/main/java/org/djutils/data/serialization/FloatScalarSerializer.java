package org.djutils.data.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.djunits.unit.Unit;
import org.djunits.value.vfloat.scalar.base.FloatScalarInterface;

/**
 * ScalarSerializer (de)serializes DJUNITS scalars. <br>
 * <br>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 */
public class FloatScalarSerializer<U extends Unit<U>, S extends FloatScalarInterface<U, S>> implements TextSerializer<S>
{
    /** cache of the retrieved valueOf(String) methods for scalars based on the stored string. */
    private static Map<String, Method> valueOfMethodCache = new LinkedHashMap<>();

    /**
     * Serialize a FloatScalar value to text in such a way that it can be deserialized with the corresponding deserializer. In
     * this case, it serializes the class name of the Scalar, followed by a hash sign, followed by the printed value of the
     * scalar including the unit. So a Length of 12.5 kilometer will return "org.djunits.value.vdouble.scalar.Length#12.5 km".
     * @param value Object; the scalar to serialize
     * @return String; a string representation of the value that can later be deserialized
     */
    @SuppressWarnings("unchecked")
    @Override
    public String serialize(final Object value)
    {
        return value.getClass().getName() + "#" + ((S) value).toTextualString();
    }

    /**
     * Deserialize a String to the correct FloatScalar value in line with the FloatScalarSerializer.serialize method. In this
     * case, it assumes a string with the class name of the Scalar, followed by a hash sign, followed by the printed value of
     * the scalar including the unit. So, the String "org.djunits.value.vdouble.scalar.Length#12.5 km" will be deserialized into
     * a Length of 12500 m with LengthUnit.KILOMETER as the display unit. The method caches the valueOf(String) method for
     * repeated use.
     * @param text String; the text to deserialize
     * @return S; the reconstructed scalar
     */
    @SuppressWarnings("unchecked")
    @Override
    public S deserialize(final String text)
    {
        String[] parts = text.split("#");
        try
        {
            Method valueOfMethod = valueOfMethodCache.get(parts[0]);
            if (valueOfMethod == null)
            {
                Class<?> scalarClass = Class.forName(parts[0]);
                valueOfMethod = scalarClass.getDeclaredMethod("valueOf", String.class);
                valueOfMethodCache.put(parts[0], valueOfMethod);
            }
            return (S) valueOfMethodCache.get(parts[0]).invoke(null, parts[1]);
        }
        catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException exception)
        {
            throw new RuntimeException(exception);
        }
    }

}
