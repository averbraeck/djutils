package org.djutils.data.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.djunits.formatter.QuantityFormat;
import org.djunits.quantity.def.AbsQuantity;
import org.djunits.quantity.def.Quantity;
import org.djunits.quantity.def.Reference;

/**
 * AbsQuantitySerializer (de)serializes DJUNITS absolute quantities.
 * <p>
 * Copyright (c) 2020-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <A> the absolute quantity type
 * @param <Q> the quantity type
 * @param <R> the reference type
 */
public class AbsQuantitySerializer<A extends AbsQuantity<A, Q, R>, Q extends Quantity<Q>, R extends Reference<R, A, Q>>
        implements TextSerializer<A>
{
    /** cache of the retrieved valueOf(String) methods for absolute quantities based on the stored quantity + "_" + ref. */
    private static Map<String, Method> valueOfMethodCache = new LinkedHashMap<>();

    /** cache of the retrieved getId(String) methods for references based on the stored quantity type. */
    private static Map<Class<? extends AbsQuantity<?, ?, ?>>, Method> referenceIdMethodCache = new LinkedHashMap<>();

    /** format of the textual representation. */
    private static final QuantityFormat FORMAT = QuantityFormat.instance()
        .setTextual()
        .setVariableLength()
        .setMaxSigDigits(20)
        .setPrintReference()
        .setReferencePrefix(" (")
        .setReferencePostfix(")");

    /**
     * Serialize an absolute Quantity value to text in such a way that it can be deserialized with the corresponding
     * deserializer.
     * @param value the absolute quantity to serialize
     * @return a string representation of the value that can later be deserialized
     */
    @Override
    public String serialize(final A value)
    {
        if (value == null)
        {
            return null;
        }
        return value.format(FORMAT);
    }

    /**
     * Deserialize a String to the correct absolute Quantity value. The method caches the valueOf(String, Reference) method for
     * repeated use.
     * @param text the text to deserialize
     * @return the reconstructed absolute quantity
     */
    @SuppressWarnings("unchecked")
    @Override
    public A deserialize(final Class<A> type, final String text)
    {
        if (text == null || text.isEmpty())
        {
            return null;
        }
        try
        {
            Method referenceIdMethod = referenceIdMethodCache.get(type);
            if (referenceIdMethod == null)
            {
                Class<?> referenceClass = Class.forName(type.getName() + "$Reference");
                referenceIdMethod = referenceClass.getDeclaredMethod("get", String.class);
                referenceIdMethodCache.put(type, referenceIdMethod);
            }
            int par = text.lastIndexOf('(');
            String refStr = text.substring(par + 1, text.length() - 1);
            R reference = (R) referenceIdMethod.invoke(null, refStr);

            String key = type + "_" + refStr;
            Method valueOfMethod = valueOfMethodCache.get(key);
            if (valueOfMethod == null)
            {
                valueOfMethod = type.getDeclaredMethod("valueOf", String.class, reference.getClass());
                valueOfMethodCache.put(key, valueOfMethod);
            }

            String valueStr = text.substring(0, par - 1);
            return (A) valueOfMethod.invoke(null, valueStr, reference);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | ClassNotFoundException exception)
        {
            throw new RuntimeException(exception);
        }
    }

}
