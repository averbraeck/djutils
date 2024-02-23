package org.djutils.data.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.djunits.unit.Unit;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;

/**
 * DoubleScalarSerializer (de)serializes DJUNITS double scalars.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 */
public class DoubleScalarSerializer<U extends Unit<U>, S extends DoubleScalar<U, S>> implements TextSerializer<S>
{
    /** cache of the retrieved valueOf(String) methods for scalars based on the stored string. */
    private static Map<String, Method> valueOfMethodCache = new LinkedHashMap<>();

    /** cache of the retrieved unit instances based on the unit string. */
    private static Map<String, Unit<?>> unitCache = new LinkedHashMap<>();

    /**
     * Serialize an Scalar value to text in such a way that it can be deserialized with the corresponding deserializer.
     * @param value Object; the scalar to serialize
     * @return String; a string representation of the value that can later be deserialized
     */
    @SuppressWarnings("unchecked")
    @Override
    public String serialize(final S value, final String unitString)
    {
        if (value == null)
        {
            return null;
        }

        String key = value.getClass().getSimpleName() + "_" + unitString;
        Unit<?> unit = unitCache.get(key);
        if (unit == null)
        {
            unit = value.getDisplayUnit().getQuantity().of(unitString);
            unitCache.put(key, unit);
        }
        return String.valueOf(value.getInUnit((U) unit));
    }

    /**
     * Deserialize a String to the correct Scalar value. The method caches the valueOf(String) method for repeated use.
     * @param text String; the text to deserialize
     * @return S; the reconstructed scalar
     */
    @SuppressWarnings("unchecked")
    @Override
    public S deserialize(final Class<S> type, final String text, final String unit)
    {
        if (text == null || text.isEmpty())
        {
            return null;
        }
        try
        {
            Method valueOfMethod = valueOfMethodCache.get(type.getName());
            if (valueOfMethod == null)
            {
                valueOfMethod = type.getDeclaredMethod("valueOf", String.class);
                valueOfMethodCache.put(type.getName(), valueOfMethod);
            }
            return (S) valueOfMethod.invoke(null, text + unit);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException exception)
        {
            throw new RuntimeException(exception);
        }
    }

}
