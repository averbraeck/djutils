package org.djutils.data.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unit;

/**
 * DoubleScalarSerializer (de)serializes DJUNITS double scalars.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <Q> the quantity type
 * @param <U> the unit type
 */
public class QuantitySerializer<Q extends Quantity<Q>, U extends Unit<U, Q>> implements TextSerializer<Q>
{
    /** cache of the retrieved valueOf(String) methods for scalars based on the stored string. */
    private static Map<String, Method> valueOfMethodCache = new LinkedHashMap<>();

    /** cache of the retrieved unit instances based on the unit string. */
    private static Map<String, Unit<?, ?>> unitCache = new LinkedHashMap<>();

    /**
     * Serialize an Scalar value to text in such a way that it can be deserialized with the corresponding deserializer.
     * @param value the scalar to serialize
     * @return a string representation of the value that can later be deserialized
     */
    @Override
    public String serialize(final Q value, final String unitString)
    {
        if (value == null)
        {
            return null;
        }

        String key = value.getClass().getSimpleName() + "_" + unitString;
        Unit<?, ?> unit = unitCache.get(key);
        if (unit == null)
        {
            unit = value.setDisplayUnit(unitString).getDisplayUnit();
            unitCache.put(key, unit);
        }
        return String.valueOf(value.setDisplayUnit(unitString));
    }

    /**
     * Deserialize a String to the correct Scalar value. The method caches the valueOf(String) method for repeated use.
     * @param text the text to deserialize
     * @return the reconstructed scalar
     */
    @SuppressWarnings("unchecked")
    @Override
    public Q deserialize(final Class<Q> type, final String text, final String unit)
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
            return (Q) valueOfMethod.invoke(null, text + unit);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException exception)
        {
            throw new RuntimeException(exception);
        }
    }

}
