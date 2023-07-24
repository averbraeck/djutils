package org.djutils.serialization.serializers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.SIUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.unit.util.UnitRuntimeException;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vdouble.vector.SIVector;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vdouble.vector.data.DoubleVectorData;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS DoubleVector.
 * <p>
 * Copyright (c) 2019-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 */
public class DoubleVectorSerializer<U extends Unit<U>, S extends DoubleScalar<U, S>, V extends DoubleVector<U, S, V>>
        extends ArrayOrMatrixWithUnitSerializer<U, V>
{
    /** The cache to make the lookup of the constructor for a Vevtor belonging to a unit faster. */
    private static final Map<Unit<?>, Constructor<? extends DoubleVector<?, ?, ?>>> CACHE = new HashMap<>();

    /** */
    public DoubleVectorSerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT_ARRAY, "Djunits_DoubleVector", 1);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final V adv) throws SerializationException
    {
        try
        {
            return 4 + 2 + 8 * adv.size();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final V adv, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(adv.size(), buffer, pointer.getAndIncrement(4));
            encodeUnit(adv.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < adv.size(); i++)
            {
                endianUtil.encodeDouble(adv.get(i).getSI(), buffer, pointer.getAndIncrement(8));
            }
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public V deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil) throws SerializationException
    {
        int size = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
        double[] array = new double[size];
        for (int i = 0; i < size; i++)
        {
            array[i] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
        }
        try
        {
            DoubleVectorData fvd = DoubleVectorData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

    /**
     * Instantiate the DoubleVector based on its unit. Loose check for types on the compiler. This allows the unit to be
     * specified as a Unit&lt;?&gt; type.<br>
     * <b>Note</b> that it is possible to make mistakes with anonymous units.
     * @param data DoubleVectorData; the values
     * @param unit Unit&lt;?&gt;; the unit in which the value is expressed
     * @return V; an instantiated DoubleVector with the provided displayUunit
     * @param <U> the unit type
     * @param <S> the scalar type
     * @param <V> the vector type
     */
    @SuppressWarnings("unchecked")
    public static <U extends Unit<U>, S extends DoubleScalar<U, S>,
            V extends DoubleVector<U, S, V>> V instantiateAnonymous(final DoubleVectorData data, final Unit<?> unit)
    {
        try
        {
            Constructor<? extends DoubleVector<?, ?, ?>> vectorConstructor = CACHE.get(unit);
            if (vectorConstructor == null)
            {
                if (!unit.getClass().getSimpleName().endsWith("Unit"))
                {
                    throw new ClassNotFoundException("Unit " + unit.getClass().getSimpleName()
                            + " name does noet end with 'Unit'. Cannot find corresponding scalar");
                }
                Class<? extends DoubleVector<?, ?, ?>> vectorClass;
                if (unit instanceof SIUnit)
                {
                    vectorClass = SIVector.class;
                }
                else
                {
                    vectorClass = (Class<DoubleVector<?, ?, ?>>) Class.forName("org.djunits.value.vdouble.vector."
                            + unit.getClass().getSimpleName().replace("Unit", "") + "Vector");
                }
                vectorConstructor = vectorClass.getDeclaredConstructor(DoubleVectorData.class, unit.getClass());
                CACHE.put(unit, vectorConstructor);
            }
            return (V) vectorConstructor.newInstance(data, unit);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception)
        {
            throw new UnitRuntimeException(
                    "Cannot instantiate DoubleVector of unit " + unit.toString() + ". Reason: " + exception.getMessage());
        }
    }
}
