package org.djutils.serialization.serializers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.SIUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.unit.util.UnitRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djunits.value.vfloat.vector.FloatSIVector;
import org.djunits.value.vfloat.vector.base.FloatVector;
import org.djunits.value.vfloat.vector.data.FloatVectorData;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS FloatVector.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class FloatVectorSerializer<U extends Unit<U>, S extends FloatScalar<U, S>, V extends FloatVector<U, S, V>>
        extends ArrayOrMatrixWithUnitSerializer<U, V>
{
    /** The cache to make the lookup of the constructor for a Vevtor belonging to a unit faster. */
    private static final Map<Unit<?>, Constructor<? extends FloatVector<?, ?, ?>>> CACHE = new HashMap<>();

    /** */
    public FloatVectorSerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT_ARRAY, "Djunits_FloatVector", 1);
    }

    @Override
    public int size(final V afv)
    {
        return 4 + 2 + 4 * afv.size();
    }

    @Override
    public int getElementSize()
    {
        return 4;
    }

    @Override
    public void serialize(final V afv, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        endianUtil.encodeInt(afv.size(), buffer, pointer.getAndIncrement(4));
        encodeUnit(afv.getDisplayUnit(), buffer, pointer, endianUtil);
        for (int i = 0; i < afv.size(); i++)
        {
            endianUtil.encodeFloat(afv.get(i).getSI(), buffer, pointer.getAndIncrement(4));
        }
    }

    @Override
    public V deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil) throws SerializationException
    {
        int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        Unit<?> unit = getUnit(buffer, pointer, endianUtil);
        float[] array = new float[size];
        for (int i = 0; i < size; i++)
        {
            array[i] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
        }
        try
        {
            FloatVectorData fvd = FloatVectorData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return instantiateAnonymous(fvd, unit);
        }
        catch (UnitRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

    /**
     * Instantiate the FloatVector based on its unit. Loose check for types on the compiler. This allows the unit to be
     * specified as a Unit&lt;?&gt; type.<br>
     * <b>Note</b> that it is possible to make mistakes with anonymous units.
     * @param data the values
     * @param unit the unit in which the value is expressed
     * @return an instantiated FloatVector with the provided displayUunit
     * @param <U> the unit type
     * @param <S> the scalar type
     * @param <V> the vector type
     */
    @SuppressWarnings("unchecked")
    public static <U extends Unit<U>, S extends FloatScalar<U, S>,
            V extends FloatVector<U, S, V>> V instantiateAnonymous(final FloatVectorData data, final Unit<?> unit)
    {
        try
        {
            Constructor<? extends FloatVector<?, ?, ?>> vectorConstructor = CACHE.get(unit);
            if (vectorConstructor == null)
            {
                if (!unit.getClass().getSimpleName().endsWith("Unit"))
                {
                    throw new ClassNotFoundException("Unit " + unit.getClass().getSimpleName()
                            + " name does noet end with 'Unit'. Cannot find corresponding scalar");
                }
                Class<? extends FloatVector<?, ?, ?>> vectorClass;
                if (unit instanceof SIUnit)
                {
                    vectorClass = FloatSIVector.class;
                }
                else
                {
                    vectorClass = (Class<FloatVector<?, ?, ?>>) Class.forName("org.djunits.value.vfloat.vector.Float"
                            + unit.getClass().getSimpleName().replace("Unit", "") + "Vector");
                }
                vectorConstructor = vectorClass.getDeclaredConstructor(FloatVectorData.class, unit.getClass());
                CACHE.put(unit, vectorConstructor);
            }
            return (V) vectorConstructor.newInstance(data, unit);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception)
        {
            throw new UnitRuntimeException(
                    "Cannot instantiate FloatVector of unit " + unit.toString() + ". Reason: " + exception.getMessage());
        }
    }

}
